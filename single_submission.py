"""Convenience script to run the EME pipeline on a single submission."""
from __future__ import annotations

import argparse
import asyncio
import json
import logging
from pathlib import Path
from threading import Thread
import time
from typing import Any, Dict

from dotenv import load_dotenv
from rich.progress import Progress, SpinnerColumn, BarColumn, TextColumn, TimeElapsedColumn

from cli import _load_question_and_rubric  # reuse existing helper
from utils.evaluator import evaluate_submission
from utils.display import display_results

logger = logging.getLogger(__name__)


def _discover_code(path: Path) -> Path:
    if path.is_file() and path.suffix == ".java":
        return path
    if path.is_dir():
        java_files = list(path.rglob("*.java"))
        if java_files:
            return java_files[0]
    raise FileNotFoundError(f"No Java file found at {path}")


def main() -> None:
    parser = argparse.ArgumentParser(description="Evaluate a single student submission via EME")
    parser.add_argument("submission", help="Path to a .java file or a student directory")
    parser.add_argument("--dump-json", dest="dump_json", help="Optional path to store the raw JSON result")
    parser.add_argument("--show-raw", action="store_true", help="Print raw model responses for debugging")
    args = parser.parse_args()

    load_dotenv()
    question, rubric = _load_question_and_rubric()

    code_path = _discover_code(Path(args.submission))
    logger.info("Evaluating %s", code_path)

    # Run evaluation with a progress bar (single student)
    result = _evaluate_with_progress(code_path, question, rubric)

    # Rich table view (parity with batch runner)
    summary = _build_summary([result])
    display_results([result], summary)

    # Also print a concise grade-only line under the table
    _print_grades(result)

    if args.show_raw:
        _print_raw_payloads(result)

    if args.dump_json:
        cleaned = _clean_result(result)
        Path(args.dump_json).write_text(json.dumps(cleaned, indent=2), encoding="utf-8")
        logger.info("Wrote raw result to %s", args.dump_json)


def _print_raw_payloads(result: Dict[str, Any]) -> None:
    from pprint import pprint

    print("\n=== GPT-5 raw response ===")
    pprint(result.get("gpt5_result"), width=120)
    print("\n=== EduAI raw response ===")
    pprint(result.get("eduai_result"), width=120)


def _fmt_score_block(block: Dict[str, Any]) -> str:
    total = block.get("total")
    max_score = block.get("max")
    pct = block.get("pct")
    if total is None and pct is None:
        return "N/A"
    parts = []
    if total is not None:
        if max_score:
            parts.append(f"{total:.1f}/{max_score:.1f}")
        else:
            parts.append(f"{total:.1f}")
    if pct is not None:
        parts.append(f"({pct:.1f}%)")
    return " ".join(parts)


def _print_grades(result: Dict[str, Any]) -> None:
    student = result.get("student", "Unknown")
    metrics = result.get("metrics", {})
    gpt5 = metrics.get("gpt5", {})
    eduai = metrics.get("eduai", {})
    avg_pct = metrics.get("avg_pct")
    diff_pct = metrics.get("diff_pct")
    flag = metrics.get("flag")

    print("\n=== Grade Summary ===")
    print(f"Student: {student}")
    print(f"GPT-5:  {_fmt_score_block(gpt5)}")
    print(f"EduAI:  {_fmt_score_block(eduai)}")
    if avg_pct is not None:
        print(f"Avg %:  {avg_pct:.1f}%")
    if diff_pct is not None:
        print(f"Diff %: {diff_pct:.1f}%")
    if flag:
        print(f"Flag:   {flag}")


def _evaluate_with_progress(code_path: Path, question: str, rubric: Dict[str, Any]) -> Dict[str, Any]:
    container: Dict[str, Any] = {"result": None, "error": None}

    def runner() -> None:
        try:
            # Run the async evaluation (GPT-5 and EduAI in parallel)
            container["result"] = asyncio.run(evaluate_submission(code_path, question, rubric))
        except Exception as exc:  # pragma: no cover - defensive
            container["error"] = exc

    t = Thread(target=runner, daemon=True)

    with Progress(
        SpinnerColumn(style="cyan"),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(),
        TextColumn("{task.percentage:>3.0f}%"),
        TimeElapsedColumn(),
        transient=True,
    ) as progress:
        task_id = progress.add_task("Evaluating submission", total=100)
        t.start()
        pct = 0
        while t.is_alive():
            # Simulate forward progress while the evaluation runs
            step = 5 if pct < 80 else 2
            pct = min(95, pct + step)
            progress.update(task_id, completed=pct)
            time.sleep(0.15)
        t.join()
        progress.update(task_id, completed=100)

    if container["error"]:
        raise container["error"]
    return container["result"]


def _build_summary(results: list[Dict[str, Any]]) -> Dict[str, Any]:
    diffs = [res["metrics"].get("diff_pct") for res in results if res.get("metrics", {}).get("diff_pct") is not None]
    mean_diff = sum(diffs) / len(diffs) if diffs else None
    flagged = sum(1 for res in results if res.get("metrics", {}).get("flag") == "🚩")
    return {"mean_diff_pct": mean_diff, "flagged_count": flagged, "total": len(results)}


def _clean_result(result: Dict[str, Any]) -> Dict[str, Any]:
    """Strip down to essentials: student name and clean model responses."""
    def clean_response(response: Any) -> Any:
        if response is None:
            return None
        if not isinstance(response, dict):
            return response
        # Remove internal metadata fields (those starting with _) and criteria_scores
        return {k: v for k, v in response.items() if not k.startswith("_") and k != "criteria_scores"}

    return {
        "student": result.get("student"),
        "gpt5_result": clean_response(result.get("gpt5_result")),
        "eduai_result": clean_response(result.get("eduai_result")),
    }


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format="%(levelname)s:%(name)s:%(message)s")
    main()
