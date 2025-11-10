"""Reverse grading mode implementation."""
from __future__ import annotations

import asyncio
import logging
from pathlib import Path
from typing import Any, Dict, List

from prompts.reverse_prompt import build_prompt
from utils.evaluator import evaluate_submission

logger = logging.getLogger(__name__)


async def run_reverse_grading(
    submissions: List[Path],
    question: str,
    rubric: Dict[str, Any],
    progress_callback=None,
) -> List[Dict[str, Any]]:
    """Run reverse grading paradigm on all submissions.

    In this mode, the model first generates an ideal solution, then compares
    the student code against it.

    Args:
        submissions: List of paths to student Java files
        question: Problem statement
        rubric: Grading rubric
        progress_callback: Optional callback function to report progress

    Returns:
        List of evaluation results
    """
    logger.info("Starting Reverse Grading mode with %d submissions", len(submissions))

    # Create evaluation tasks with reverse prompt builder
    tasks = [
        evaluate_submission(code_path, question, rubric, prompt_builder=build_prompt)
        for code_path in submissions
    ]

    results: List[Dict[str, Any]] = []
    for coro in asyncio.as_completed(tasks):
        try:
            evaluation = await coro
            results.append(evaluation)
            logger.debug("Completed reverse grading for %s", evaluation.get("student", "Unknown"))
            if progress_callback:
                progress_callback(evaluation)
        except Exception as exc:
            logger.exception("Failed to evaluate submission: %s", exc)
            if progress_callback:
                progress_callback(None)

    return results
