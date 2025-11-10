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
    gpt5_result, eduai_result = await asyncio.gather(
        get_openai_eval(prompt),
        get_eduai_eval_async(prompt),
        return_exceptions=False,
    )

    metrics = compute_metrics(gpt5_result, eduai_result)
    return {
        "student": student,
        "code_path": str(code_path),
        "prompt": prompt,
        "gpt5_result": gpt5_result,
        "eduai_result": eduai_result,
        "metrics": metrics,
    }


def compute_metrics(gpt5_result: Optional[Dict[str, Any]], eduai_result: Optional[Dict[str, Any]]) -> Dict[str, Any]:
    """Compute comparison statistics between GPT-5 and EduAI responses."""

    def extract_scores(payload: Optional[Dict[str, Any]]) -> Dict[str, Optional[float]]:
        if not payload:
            return {"total": None, "max": None, "pct": None}
        total = _to_number(payload.get("total_score"))
        max_score = _to_number(payload.get("max_possible_score")) or _to_number(payload.get("max_score"))
        pct = (total / max_score * 100.0) if total is not None and max_score else None
        return {"total": total, "max": max_score, "pct": pct}

    gpt5_scores = extract_scores(gpt5_result)
    eduai_scores = extract_scores(eduai_result)

    available_pcts = [v for v in (gpt5_scores["pct"], eduai_scores["pct"]) if v is not None]
    avg_pct = mean(available_pcts) if available_pcts else None
    diff_pct = None
    if gpt5_scores["pct"] is not None and eduai_scores["pct"] is not None:
        diff_pct = abs(gpt5_scores["pct"] - eduai_scores["pct"])

    if diff_pct is None:
        flag = "🚩"
        comment = "Missing comparison data"
    else:
        threshold = 10.0
        flag = "✅" if diff_pct <= threshold else "🚩"
        if flag == "✅":
            comment = "Models agree within tolerance"
        elif gpt5_scores["pct"] < eduai_scores["pct"]:
            comment = f"GPT-5 stricter by {diff_pct:.1f} pts"
        else:
            comment = f"EduAI stricter by {diff_pct:.1f} pts"

    return {
        "gpt5": gpt5_scores,
        "eduai": eduai_scores,
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
