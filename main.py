from __future__ import annotations

import asyncio
import json
import logging
import os
from pathlib import Path
from typing import Any, Dict, List

from dotenv import load_dotenv
from rich.progress import (
    Progress,
    SpinnerColumn,
    BarColumn,
    TextColumn,
    TimeElapsedColumn,
)

from utils.display import display_results
from utils.evaluator import evaluate_submission

logging.basicConfig(level=logging.INFO, format="%(levelname)s:%(name)s:%(message)s")
logger = logging.getLogger(__name__)


def main() -> None:
    load_dotenv()
    question, rubric = _load_question_and_rubric()

    submissions = _discover_submissions(Path("student_submissions"))
    if not submissions:
        logger.warning("No .java submissions found under student_submissions/")
        return

    results: List[Dict[str, Any]] = []
    with Progress(
        SpinnerColumn(style="white"),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(),
        TextColumn("{task.completed}/{task.total}"),
        TimeElapsedColumn(),
        transient=True,
    ) as progress:
        task_id = progress.add_task("Evaluating submissions", total=len(submissions))
        for code_path in submissions:
            progress.log(f"Evaluating {code_path}")
            try:
                # Run the async evaluation (GPT-5 and EduAI in parallel)
                evaluation = asyncio.run(evaluate_submission(code_path, question, rubric))
                results.append(evaluation)
            except Exception as exc:
                logger.exception("Failed to evaluate %s: %s", code_path, exc)
            finally:
                progress.advance(task_id)

    if not results:
        logger.warning("No evaluation results to display")
        return

    summary = _build_summary(results)
    display_results(results, summary)
    _dump_results(results)


def _load_question_and_rubric() -> tuple[str, Dict[str, Any]]:
    question = os.getenv("QUESTION")
    rubric_raw = os.getenv("RUBRIC")
    if not question:
        raise RuntimeError("QUESTION is not set in the environment")
    if not rubric_raw:
        raise RuntimeError("RUBRIC is not set in the environment")

    try:
        rubric = json.loads(rubric_raw)
    except json.JSONDecodeError as exc:
        raise RuntimeError("RUBRIC must be valid JSON") from exc
    return question, rubric


def _discover_submissions(root: Path) -> List[Path]:
    return sorted(root.rglob("*.java"))


def _build_summary(results: List[Dict[str, Any]]) -> Dict[str, Any]:
    diffs = [res["metrics"].get("diff_pct") for res in results if res.get("metrics", {}).get("diff_pct") is not None]
    mean_diff = sum(diffs) / len(diffs) if diffs else None
    flagged = sum(1 for res in results if res.get("metrics", {}).get("flag") == "🚩")
    return {
        "mean_diff_pct": mean_diff,
        "flagged_count": flagged,
        "total": len(results),
    }


def _clean_response(response: Any) -> Any:
    """Remove metadata fields from model responses, keeping only the core output."""
    if response is None:
        return None
    if not isinstance(response, dict):
        return response

    # Create a copy and remove internal metadata fields and criteria_scores
    cleaned = {k: v for k, v in response.items() if not k.startswith("_") and k != "criteria_scores"}
    return cleaned


def _dump_results(results: List[Dict[str, Any]]) -> None:
    data_dir = Path("data")
    data_dir.mkdir(parents=True, exist_ok=True)
    output_path = data_dir / "results.json"

    # Strip down to essentials: student name and clean model responses
    cleaned_results = []
    for result in results:
        cleaned = {
            "student": result.get("student"),
            "gpt5_result": _clean_response(result.get("gpt5_result")),
            "eduai_result": _clean_response(result.get("eduai_result")),
        }
        cleaned_results.append(cleaned)

    output_path.write_text(json.dumps(cleaned_results, indent=2), encoding="utf-8")
    logger.info("Saved raw results to %s", output_path)


if __name__ == "__main__":
    main()
