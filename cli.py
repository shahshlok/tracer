"""EduBench: Unified AI Grading Benchmark CLI."""
from __future__ import annotations

import asyncio
import json
import logging
import os
from datetime import datetime
from pathlib import Path
from typing import Any, Dict, List, Optional

import typer
from dotenv import load_dotenv
from rich import box
from rich.console import Console

from rich.progress import (
    BarColumn,
    Progress,
    SpinnerColumn,
    TextColumn,
    TimeElapsedColumn,
)
from rich.prompt import Prompt
from rich.table import Table

from modes.direct_grading import run_direct_grading
from modes.eme_grading import run_eme_grading
from modes.reverse_grading import run_reverse_grading

logging.basicConfig(level=logging.INFO, format="%(levelname)s:%(name)s:%(message)s")
logger = logging.getLogger(__name__)

app = typer.Typer(help="EduBench: Unified AI Grading Benchmark")
console = Console()


def main() -> None:
    """Entry point for the CLI."""
    app()


@app.command()
def benchmark(
    mode: Optional[str] = typer.Option(
        None,
        "--mode",
        "-m",
        help="Grading mode: direct, reverse, eme, or all",
    ),
    advanced: bool = typer.Option(
        False,
        "--advanced",
        "-a",
        help="Show advanced progress view with per-student bars",
    ),
) -> None:
    """Run the EduBench grading benchmark.

    If no mode is specified, displays an interactive menu.
    """
    # Display banner
    _display_banner()

    # Interactive mode selection if not provided
    if mode is None:
        mode = _prompt_mode_selection()

    # Run the selected mode
    asyncio.run(_run_benchmark_async(mode, advanced))


async def _run_benchmark_async(mode: str, advanced: bool) -> None:
    """Async main pipeline for running benchmarks."""
    load_dotenv()

    # Load question and rubric
    question, rubric = _load_question_and_rubric()

    # Discover submissions
    submissions = _discover_submissions(Path("student_submissions"))
    if not submissions:
        console.print("[red]No .java submissions found under student_submissions/[/red]")
        return

    console.print(f"\n[cyan]Found {len(submissions)} submissions[/cyan]")

    # Run selected mode(s)
    if mode == "all":
        await _run_all_modes(submissions, question, rubric, advanced)
    else:
        results = await _run_single_mode(mode, submissions, question, rubric, advanced)
        if results:
            _display_mode_results(mode, results)
            _save_results(mode, results)


async def _run_single_mode(
    mode: str,
    submissions: List[Path],
    question: str,
    rubric: Dict[str, Any],
    advanced: bool,
) -> List[Dict[str, Any]]:
    """Run a single grading mode."""
    console.print(f"\n[bold cyan]Running {mode.upper()} mode...[/bold cyan]")

    # Select the appropriate grading function
    mode_runners = {
        "direct": run_direct_grading,
        "reverse": run_reverse_grading,
        "eme": run_eme_grading,
    }

    runner = mode_runners.get(mode.lower())
    if not runner:
        console.print(f"[red]Unknown mode: {mode}[/red]")
        return []

    # Temporarily silence logging during evaluation for clean progress display
    original_level = logging.root.level
    logging.root.setLevel(logging.WARNING)

    # Run with progress bar
    with Progress(
        SpinnerColumn(style="cyan"),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(bar_width=40),
        TextColumn("[bold cyan]{task.completed}[/bold cyan]/[bold]{task.total}[/bold]"),
        TimeElapsedColumn(),
        console=console,
        transient=False,
    ) as progress:
        task_id = progress.add_task(
            f"Evaluating with {mode.upper()}",
            total=len(submissions)
        )

        # Create progress callback that updates the bar in real-time
        def update_progress(evaluation):
            progress.advance(task_id)

        # Run the evaluation with progress callback
        results = await runner(submissions, question, rubric, progress_callback=update_progress)

    # Restore logging level
    logging.root.setLevel(original_level)

    console.print(f"[green]✓[/green] Completed {len(results)}/{len(submissions)} evaluations\n")
    return results


async def _run_all_modes(
    submissions: List[Path],
    question: str,
    rubric: Dict[str, Any],
    advanced: bool,
) -> None:
    """Run all three grading modes and display comparison."""
    console.print("\n[bold]Running all grading strategies...[/bold]")

    # Run all modes
    all_results = {}
    for mode in ["direct", "reverse", "eme"]:
        results = await _run_single_mode(mode, submissions, question, rubric, advanced)
        all_results[mode] = results

    # Display individual results
    for mode, results in all_results.items():
        _display_mode_results(mode, results)
        _save_results(mode, results)

    # Display strategy comparison
    _display_cross_paradigm_comparison(all_results)


def _display_banner() -> None:
    """Display the EduBench banner."""
    banner = """

   [bold]EduBench: AI Grading Benchmark[/bold]
   Compare GPT-5 Nano & GPT-OSS 120B grading strategies

"""
    console.print(banner)


def _prompt_mode_selection() -> str:
    """Prompt user to select a grading strategy."""
    console.print("\n[bold]Select a grading strategy:[/bold]\n")

    # Create a clean options table
    options_table = Table(
        show_header=False,
        box=None,
        padding=(0, 2),
        collapse_padding=True,
    )
    options_table.add_column("Choice", style="cyan", width=3)
    options_table.add_column("Strategy", style="bold", width=20)
    options_table.add_column("Description", style="dim")

    options_table.add_row(
        "[1]",
        "Direct Grading",
        "Grade student code directly against rubric"
    )
    options_table.add_row(
        "[2]",
        "Reverse Grading",
        "Generate ideal solution first, then compare"
    )
    options_table.add_row(
        "[3]",
        "Ensemble (EME)",
        "The prompt in the RIAYN paper, just tweaked a bit"
    )
    options_table.add_row(
        "[4]",
        "Run All",
        "Compare all three strategies side-by-side"
    )

    console.print(options_table)
    console.print()

    choice = Prompt.ask("Enter choice", choices=["1", "2", "3", "4"], default="1")

    mode_map = {
        "1": "direct",
        "2": "reverse",
        "3": "eme",
        "4": "all",
    }

    return mode_map[choice]


def _load_question_and_rubric() -> tuple[str, Dict[str, Any]]:
    """Load question and rubric from environment."""
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
    """Discover all Java submissions."""
    return sorted(root.rglob("*.java"))


def _display_mode_results(mode: str, results: List[Dict[str, Any]]) -> None:
    """Display results for a single mode as a Rich table."""
    if not results:
        console.print(f"[yellow]No results for {mode.upper()} mode[/yellow]")
        return

    # Calculate summary statistics
    summary = _build_summary(results)

    # Create results table - minimal, clean design
    table = Table(
        title=f"{mode.upper()} Grading Results",
        box=box.SIMPLE,
        show_header=True,
        header_style="bold",
        border_style="dim",
    )
    table.add_column("Student")
    table.add_column("GPT-5 Nano", justify="right")
    table.add_column("GPT-OSS 120B", justify="right")
    table.add_column("Avg %", justify="right")
    table.add_column("Diff %", justify="right")
    table.add_column("Flag", justify="center")
    table.add_column("Comment", style="dim")

    for result in results:
        metrics = result.get("metrics", {})
        flag = metrics.get("flag", "")
        diff_pct = metrics.get("diff_pct")

        # Only color the diff % when flagged (red = disagreement)
        if flag == "✅":
            diff_str = _fmt_pct(diff_pct)
        else:
            diff_str = f"[red]{_fmt_pct(diff_pct)}[/red]"

        table.add_row(
            result.get("student", "Unknown"),
            _fmt_score(metrics.get("gpt5_nano", {})),
            _fmt_score(metrics.get("gpt_oss_120b", {})),
            _fmt_pct(metrics.get("avg_pct")),
            diff_str,
            flag,
            metrics.get("comment", ""),
        )

    console.print("\n")
    console.print(table)

    # Display summary
    _display_summary(mode, summary)


def _display_summary(mode: str, summary: Dict[str, Any]) -> None:
    """Display summary statistics for a mode."""
    mean_diff = summary.get("mean_diff_pct")
    flagged = summary.get("flagged_count", 0)
    total = summary.get("total", 0)
    agreement_rate = ((total - flagged) / total * 100) if total > 0 else 0

    summary_text = f"""Summary for {mode.upper()}:
  Mean difference: {_fmt_pct(mean_diff)}
  Flagged: {flagged}/{total} submissions
  Agreement rate: {agreement_rate:.1f}%
"""
    console.print(summary_text)


def _display_cross_paradigm_comparison(all_results: Dict[str, List[Dict[str, Any]]]) -> None:
    # Aggregate statistics
    stats = {}
    for mode, results in all_results.items():
        summary = _build_summary(results)
        stats[mode] = summary

    # Create comparison table
    table = Table(
        title="Strategy Comparison",
        box=box.SIMPLE,
        show_header=True,
        header_style="bold",
        border_style="dim",
    )
    table.add_column("Strategy")
    table.add_column("Mean Diff %", justify="right")
    table.add_column("Flagged", justify="right")
    table.add_column("Agreement Rate", justify="right")
    table.add_column("GPT-5 Nano Stricter", justify="right")
    table.add_column("GPT-OSS 120B Stricter", justify="right")

    for mode, summary in stats.items():
        total = summary.get("total", 0)
        flagged = summary.get("flagged_count", 0)
        agreement_rate = ((total - flagged) / total * 100) if total > 0 else 0

        # Count which model is stricter
        results = all_results[mode]
        gpt5_nano_stricter = sum(
            1
            for r in results
            if r.get("metrics", {}).get("gpt5_nano", {}).get("pct", 0)
            < r.get("metrics", {}).get("gpt_oss_120b", {}).get("pct", 0)
        )
        gpt_oss_120b_stricter = sum(
            1
            for r in results
            if r.get("metrics", {}).get("gpt_oss_120b", {}).get("pct", 0)
            < r.get("metrics", {}).get("gpt5_nano", {}).get("pct", 0)
        )

        table.add_row(
            mode.upper(),
            _fmt_pct(summary.get("mean_diff_pct")),
            f"{flagged}/{total}",
            f"{agreement_rate:.1f}%",
            str(gpt5_nano_stricter),
            str(gpt_oss_120b_stricter),
        )

    console.print(table)

    # Research insights
    console.print("\nResearch Insights:")
    best_mode = min(stats.items(), key=lambda x: x[1].get("mean_diff_pct", float("inf")))
    console.print(f"  • Lowest variance: {best_mode[0].upper()} ({_fmt_pct(best_mode[1].get('mean_diff_pct'))})")

    overall_gpt5_nano = sum(
        1
        for mode_results in all_results.values()
        for r in mode_results
        if r.get("metrics", {}).get("gpt5_nano", {}).get("pct", 0)
        < r.get("metrics", {}).get("gpt_oss_120b", {}).get("pct", 0)
    )
    overall_gpt_oss_120b = sum(
        1
        for mode_results in all_results.values()
        for r in mode_results
        if r.get("metrics", {}).get("gpt_oss_120b", {}).get("pct", 0)
        < r.get("metrics", {}).get("gpt5_nano", {}).get("pct", 0)
    )

    console.print(f"  • GPT-5 Nano stricter in {overall_gpt5_nano} evaluations across all modes")
    console.print(f"  • GPT-OSS 120B stricter in {overall_gpt_oss_120b} evaluations across all modes")


def _build_summary(results: List[Dict[str, Any]]) -> Dict[str, Any]:
    """Build summary statistics from results."""
    diffs = [
        res["metrics"].get("diff_pct")
        for res in results
        if res.get("metrics", {}).get("diff_pct") is not None
    ]
    mean_diff = sum(diffs) / len(diffs) if diffs else None
    flagged = sum(1 for res in results if res.get("metrics", {}).get("flag") == "🚩")
    return {
        "mean_diff_pct": mean_diff,
        "flagged_count": flagged,
        "total": len(results),
    }


def _fmt_score(payload: Dict[str, Any]) -> str:
    """Format a score for display."""
    total = payload.get("total")
    max_score = payload.get("max")
    pct = payload.get("pct")
    if total is None:
        return "—"
    total_str = f"{total:.1f}" if isinstance(total, float) else str(total)
    if max_score:
        max_str = f"{max_score:.1f}" if isinstance(max_score, float) else str(max_score)
        total_str = f"{total_str}/{max_str}"
    if pct is not None:
        total_str += f" ({pct:.1f}%)"
    return total_str


def _fmt_pct(value: Any) -> str:
    """Format a percentage for display."""
    if value is None:
        return "—"
    try:
        return f"{float(value):.1f}%"
    except (TypeError, ValueError):
        return "—"


def _save_results(mode: str, results: List[Dict[str, Any]]) -> None:
    """Save results to a JSON file."""
    data_dir = Path("data")
    data_dir.mkdir(parents=True, exist_ok=True)
    output_path = data_dir / f"results_{mode}.json"

    # Strip down to essentials
    cleaned_results = []
    for result in results:
        cleaned = {
            "student": result.get("student"),
            "gpt5_nano_result": _clean_response(result.get("gpt5_nano_result")),
            "gpt_oss_120b_result": _clean_response(result.get("gpt_oss_120b_result")),
            "metrics": result.get("metrics"),
        }
        cleaned_results.append(cleaned)

    output_path.write_text(json.dumps(cleaned_results, indent=2), encoding="utf-8")
    logger.info("Saved %s results to %s", mode.upper(), output_path)


def _clean_response(response: Any) -> Any:
    """Remove metadata fields from model responses."""
    if response is None:
        return None
    if not isinstance(response, dict):
        return response

    cleaned = {k: v for k, v in response.items() if not k.startswith("_") and k != "criteria_scores"}
    return cleaned


if __name__ == "__main__":
    main()
