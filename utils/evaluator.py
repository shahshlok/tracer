"""Evaluation helpers for the EME framework."""
from __future__ import annotations

import asyncio
import logging
from pathlib import Path
from statistics import mean
from typing import Any, Dict, Optional

from prompts.eme_prompt import build_eme_prompt
from .ai_clients import get_eduai_eval_async, get_openai_eval

logger = logging.getLogger(__name__)


async def evaluate_submission(code_path: Path, question: str, rubric: Dict[str, Any]) -> Dict[str, Any]:
    """Evaluate a single Java submission with both GPT-5 and EduAI in parallel."""
    student = code_path.parent.name or code_path.stem
    student_code = code_path.read_text(encoding="utf-8")
    prompt = build_eme_prompt(question, rubric, student_code)

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
