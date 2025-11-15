"""Evaluation helpers for the EME framework."""
from __future__ import annotations

import asyncio
import logging
import os
from pathlib import Path
from statistics import mean
from typing import Any, Callable, Dict, Optional

from prompts.eme_prompt import build_eme_prompt
from .ai_clients import get_eduai_eval_async, get_openai_eval

logger = logging.getLogger(__name__)

# Type alias for prompt builder functions
PromptBuilder = Callable[[str, Dict[str, Any], str], str]

# Optional rate limiting for student-level concurrency
_student_semaphore: Optional[asyncio.Semaphore] = None


def _get_student_semaphore() -> Optional[asyncio.Semaphore]:
    """Get or create the student rate limit semaphore based on MAX_CONCURRENT_STUDENTS env variable."""
    global _student_semaphore
    if _student_semaphore is None:
        max_concurrent = os.getenv("MAX_CONCURRENT_STUDENTS")
        if max_concurrent and max_concurrent.isdigit():
            limit = int(max_concurrent)
            logger.info("Student rate limiting enabled: max %d concurrent student evaluations", limit)
            _student_semaphore = asyncio.Semaphore(limit)
    return _student_semaphore


async def evaluate_submission(
    code_path: Path,
    question: str,
    rubric: Dict[str, Any],
    prompt_builder: Optional[PromptBuilder] = None,
) -> Dict[str, Any]:
    """Evaluate a single Java submission with both GPT-5 and EduAI in parallel.

    Args:
        code_path: Path to the student's Java file
        question: The problem statement
        rubric: The grading rubric as a dictionary
        prompt_builder: Optional custom prompt builder function. If None, uses EME prompt.
    """
    semaphore = _get_student_semaphore()
    if semaphore:
        async with semaphore:
            return await _evaluate_submission_impl(code_path, question, rubric, prompt_builder)
    return await _evaluate_submission_impl(code_path, question, rubric, prompt_builder)


async def _evaluate_submission_impl(
    code_path: Path,
    question: str,
    rubric: Dict[str, Any],
    prompt_builder: Optional[PromptBuilder] = None,
) -> Dict[str, Any]:
    """Internal implementation of evaluate_submission."""
    student = code_path.parent.name or code_path.stem
    student_code = code_path.read_text(encoding="utf-8")

    # Use provided prompt builder or default to EME
    if prompt_builder is None:
        prompt_builder = build_eme_prompt
    prompt = prompt_builder(question, rubric, student_code)

    # Call both models in parallel using asyncio.gather
    gpt5_nano_result, gpt_oss_120b_result = await asyncio.gather(
        get_openai_eval(prompt),
        get_eduai_eval_async(prompt),
        return_exceptions=False,
    )

    gpt5_nano_result = _normalize_model_result("GPT-5 Nano", gpt5_nano_result)
    gpt_oss_120b_result = _normalize_model_result("GPT-OSS 120B", gpt_oss_120b_result)

    metrics = compute_metrics(gpt5_nano_result, gpt_oss_120b_result)
    return {
        "student": student,
        "code_path": str(code_path),
        "prompt": prompt,
        "gpt5_nano_result": gpt5_nano_result,
        "gpt_oss_120b_result": gpt_oss_120b_result,
        "metrics": metrics,
    }


def compute_metrics(gpt5_nano_result: Optional[Dict[str, Any]], gpt_oss_120b_result: Optional[Dict[str, Any]]) -> Dict[str, Any]:
    """Compute comparison statistics between GPT-5 Nano and GPT-OSS 120B responses."""

    def extract_scores(payload: Optional[Dict[str, Any]]) -> Dict[str, Optional[float]]:
        if not payload:
            return {"total": None, "max": None, "pct": None}
        total = _to_number(payload.get("total_score"))
        max_score = _to_number(payload.get("max_possible_score")) or _to_number(payload.get("max_score"))
        pct = (total / max_score * 100.0) if total is not None and max_score else None
        return {"total": total, "max": max_score, "pct": pct}

    gpt5_nano_scores = extract_scores(gpt5_nano_result)
    gpt_oss_120b_scores = extract_scores(gpt_oss_120b_result)

    available_pcts = [v for v in (gpt5_nano_scores["pct"], gpt_oss_120b_scores["pct"]) if v is not None]
    avg_pct = mean(available_pcts) if available_pcts else None
    diff_pct = None
    if gpt5_nano_scores["pct"] is not None and gpt_oss_120b_scores["pct"] is not None:
        diff_pct = abs(gpt5_nano_scores["pct"] - gpt_oss_120b_scores["pct"])

    if diff_pct is None:
        flag = "🚩"
        comment = "Missing comparison data"
    else:
        threshold = 10.0
        flag = "✅" if diff_pct <= threshold else "🚩"
        if flag == "✅":
            comment = "Models agree within tolerance"
        elif gpt5_nano_scores["pct"] < gpt_oss_120b_scores["pct"]:
            comment = f"GPT-5 Nano stricter by {diff_pct:.1f} pts"
        else:
            comment = f"GPT-OSS 120B stricter by {diff_pct:.1f} pts"

    return {
        "gpt5_nano": gpt5_nano_scores,
        "gpt_oss_120b": gpt_oss_120b_scores,
        "avg_pct": avg_pct,
        "diff_pct": diff_pct,
        "flag": flag,
        "comment": comment,
    }


def _to_number(value: Any) -> Optional[float]:
    if value is None:
        return None
    try:
        return float(value)
    except (TypeError, ValueError):
        return None


def _normalize_model_result(model_name: str, payload: Optional[Dict[str, Any]]) -> Optional[Dict[str, Any]]:
    """Ensure model payload includes the grading fields required by downstream schema.

    Returns None if the payload is invalid or missing required fields, allowing
    partial results to be processed downstream.
    """

    if not isinstance(payload, dict):
        logger.warning(f"{model_name} returned no structured grading payload")
        return None

    normalized: Dict[str, Any] = dict(payload)

    # Provide graceful fallbacks for common alias names emitted by different evaluators.
    alias_map = {
        "total_score": ["total", "score", "final_score"],
        "max_possible_score": ["max_score", "max", "max_points"],
        "overall_feedback": ["feedback", "comment", "comments"],
    }

    for canonical_key, aliases in alias_map.items():
        if normalized.get(canonical_key) in (None, ""):
            for alias in aliases:
                value = normalized.get(alias)
                if value not in (None, ""):
                    normalized[canonical_key] = value
                    break

    total = _to_number(normalized.get("total_score"))
    max_score = _to_number(normalized.get("max_possible_score"))
    feedback = normalized.get("overall_feedback")

    if total is None or max_score is None or not isinstance(feedback, str) or not feedback.strip():
        logger.warning(
            f"{model_name} response missing required grading fields (total={total}, max={max_score}, feedback={'present' if feedback else 'missing'})"
        )
        return None

    normalized["total_score"] = total
    normalized["max_possible_score"] = max_score
    normalized["overall_feedback"] = feedback.strip()

    return normalized
