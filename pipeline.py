"""
Full Pipeline: Generate Dataset -> Detect Misconceptions -> Analyze Results

One command to run the entire research experiment:
    uv run pipeline

Or with options:
    uv run pipeline --students 60 --skip-generation --strategies minimal,baseline
"""

import asyncio
import json
from datetime import datetime
from pathlib import Path
from typing import Any

import typer
from rich import box
from rich.console import Console
from rich.panel import Panel
from rich.progress import BarColumn, Progress, SpinnerColumn, TextColumn, TimeElapsedColumn
from rich.table import Table
from rich.text import Text

from analyze_cli import (
    ASSET_DIR,
    DEFAULT_GROUNDTRUTH_PATH,
    DEFAULT_MANIFEST_PATH,
    JSON_EXPORT_PATH,
    REPORT_PATH,
    bootstrap_metrics,
    build_dataframes,
    ensure_asset_dir,
    generate_report,
    load_groundtruth,
    load_manifest,
    plot_calibration,
    plot_hallucinations,
    plot_topic_heatmap,
    summarize_metrics,
)
from llm_miscons_cli import (
    DEFAULT_OUTPUT_DIR as DETECTIONS_DIR,
)
from llm_miscons_cli import (
    STRATEGIES,
    get_student_list,
    run_detection,
)
from llm_miscons_cli import (
    display_results as display_detection_results,
)

# Import from existing modules
from utils.generators.dataset_generator import (
    DEFAULT_MANIFEST_PATH,
    DEFAULT_MODEL,
    DEFAULT_OUTPUT_ROOT,
    generate_manifest,
    load_misconceptions,
    load_question_texts,
    run_generation,
    write_manifest,
)

app = typer.Typer(help="Full pipeline: generate -> detect -> analyze")
console = Console()


async def run_pipeline_async(
    students: int,
    seed: int | None,
    strategy_list: list[str],
    skip_generation: bool,
    skip_detection: bool,
    skip_analysis: bool,
    concurrency: int,
    force: bool,
    output_report: Path,
) -> dict[str, Any]:
    """Run the entire pipeline in a single async context."""
    results = {"generation": None, "detection": {}, "analysis": {}}
    
    # -------------------------------------------------------------------------
    # Step 1: Generate Dataset
    # -------------------------------------------------------------------------
    if not skip_generation:
        console.rule("[bold green]Step 1: Dataset Generation[/bold green]")
        console.print()

        if seed is None:
            seed = int(datetime.utcnow().timestamp())

        console.print(f"[dim]Seed: {seed}[/dim]")
        console.print(f"[dim]Students: {students}[/dim]")
        console.print()

        # Generate manifest
        console.print("[cyan]Generating manifest...[/cyan]")
        question_texts = load_question_texts()
        misconceptions = load_misconceptions(Path("data/a2/groundtruth.json"))
        manifest_data = generate_manifest(
            misconceptions, question_texts, seed=seed, student_count=students
        )

        try:
            write_manifest(manifest_data, DEFAULT_MANIFEST_PATH, force=force)
        except FileExistsError:
            console.print("[yellow]Manifest exists. Use --force to overwrite.[/yellow]")
            if typer.confirm("Overwrite existing manifest?", default=False):
                write_manifest(manifest_data, DEFAULT_MANIFEST_PATH, force=True)
            else:
                console.print("[yellow]Using existing manifest.[/yellow]")

        # Generate Java files
        console.print()
        console.print("[cyan]Generating student submissions via OpenAI...[/cyan]")
        await run_generation(
            manifest_path=DEFAULT_MANIFEST_PATH,
            output_root=DEFAULT_OUTPUT_ROOT,
            model=DEFAULT_MODEL,
            concurrency=concurrency,
        )
        console.print("[green]✓ Dataset generation complete[/green]")
        console.print()
        results["generation"] = {"students": students, "seed": seed}
    else:
        console.print("[dim]Skipping dataset generation (using existing)[/dim]")
        console.print()

    # -------------------------------------------------------------------------
    # Step 2: LLM Detection
    # -------------------------------------------------------------------------
    if not skip_detection:
        console.rule("[bold magenta]Step 2: LLM Misconception Detection[/bold magenta]")
        console.print()

        student_list = get_student_list()
        if not student_list:
            console.print("[red]No students found in authentic_seeded/[/red]")
            return results

        console.print(f"[dim]Found {len(student_list)} students[/dim]")
        console.print(f"[dim]Running strategies: {', '.join(strategy_list)}[/dim]")
        console.print()

        for i, strategy in enumerate(strategy_list, 1):
            console.print(f"[bold][{i}/{len(strategy_list)}] Strategy: {strategy}[/bold]")
            stats = await run_detection(student_list, strategy, DETECTIONS_DIR)
            results["detection"][strategy] = stats
            display_detection_results(stats, strategy)
            console.print()

        console.print("[green]✓ LLM detection complete[/green]")
        console.print()
    else:
        console.print("[dim]Skipping LLM detection (using existing)[/dim]")
        console.print()

    # -------------------------------------------------------------------------
    # Step 3: Analysis (revamped)
    # -------------------------------------------------------------------------
    if not skip_analysis:
        console.rule("[bold cyan]Step 3: Analysis & Report Generation[/bold cyan]")
        console.print()

        groundtruth = load_groundtruth(DEFAULT_GROUNDTRUTH_PATH)
        manifest = load_manifest(DEFAULT_MANIFEST_PATH)

        available_strategies = [
            d.name for d in DETECTIONS_DIR.iterdir() if d.is_dir() and not d.name.startswith("_")
        ]

        if not available_strategies:
            console.print("[red]No detection results found.[/red]")
            return results

        console.print(f"[dim]Analyzing strategies: {', '.join(available_strategies)}[/dim]")
        console.print()

        detections_df, opportunities_df = build_dataframes(
            detections_dir=DETECTIONS_DIR,
            strategies=available_strategies,
            manifest=manifest,
            groundtruth=groundtruth,
            use_embeddings=True,
        )
        metrics = summarize_metrics(detections_df, ["strategy", "model"])
        ci = bootstrap_metrics(
            detections_df,
            group_cols=["strategy", "model"],
            unit_cols=["student", "question"],
            iters=400,
        )

        ensure_asset_dir()
        asset_paths = {
            "heatmap": ASSET_DIR / "topic_heatmap.png",
            "calibration": ASSET_DIR / "calibration.png",
            "hallucinations": ASSET_DIR / "hallucinations.png",
        }
        plot_topic_heatmap(opportunities_df, asset_paths["heatmap"])
        plot_calibration(detections_df, asset_paths["calibration"])
        plot_hallucinations(detections_df, asset_paths["hallucinations"])

        report_text = generate_report(metrics, ci, opportunities_df, detections_df, asset_paths)
        output_report.write_text(report_text)
        json_report = output_report.with_suffix(".json")
        json_report.write_text(
            json.dumps(
                {
                    "metrics": metrics.to_dict(orient="records"),
                    "ci": ci.to_dict(orient="records"),
                    "opportunities": opportunities_df.to_dict(orient="records"),
                },
                indent=2,
                default=str,
            )
        )

        console.print(f"[green]✓ Thesis report saved to {output_report}[/green]")
        console.print(f"[dim]JSON data saved to {json_report}[/dim]")
        results["analysis"] = {
            "metrics": metrics.to_dict(orient="records"),
            "ci": ci.to_dict(orient="records"),
        }
    else:
        console.print("[dim]Skipping analysis[/dim]")

    return results


def create_pipeline_header():
    """Create the pipeline header."""
    title = Text("MISCONCEPTION RESEARCH PIPELINE", style="bold white on green", justify="center")
    subtitle = Text("Generate → Detect → Analyze", style="italic cyan", justify="center")
    content = Text.assemble(title, "\n", subtitle)
    return Panel(
        content,
        box=box.DOUBLE,
        border_style="green",
        padding=(1, 2),
    )


def parse_strategies(strategies_str: str) -> list[str]:
    """Parse comma-separated strategies or 'all'."""
    if strategies_str.lower() == "all":
        return STRATEGIES
    parts = [s.strip() for s in strategies_str.split(",")]
    valid = [s for s in parts if s in STRATEGIES]
    if not valid:
        console.print(
            f"[yellow]Warning: No valid strategies in '{strategies_str}', using all[/yellow]"
        )
        return STRATEGIES
    return valid


@app.command()
def run(
    students: int = typer.Option(60, help="Number of students to generate"),
    seed: int = typer.Option(None, help="Random seed (default: current timestamp)"),
    strategies: str = typer.Option(
        "all",
        help="Strategies to run: 'all' or comma-separated (minimal,baseline,socratic,rubric_only)",
    ),
    skip_generation: bool = typer.Option(False, help="Skip dataset generation (use existing)"),
    skip_detection: bool = typer.Option(False, help="Skip LLM detection (use existing)"),
    skip_analysis: bool = typer.Option(False, help="Skip analysis"),
    concurrency: int = typer.Option(20, help="Max concurrent API requests"),
    force: bool = typer.Option(False, help="Overwrite existing manifest"),
    output_report: Path = typer.Option(Path("thesis_report.md"), help="Output report path"),
    yes: bool = typer.Option(False, "--yes", "-y", help="Skip confirmation prompt"),
):
    """
    Run the full research pipeline.

    Steps:
    1. Generate manifest + student submissions (60 students, 4 questions each)
    2. Run misconception detection with GPT-5.1 and Gemini-2.5-Flash
    3. Analyze results and generate thesis report
    """
    console.print(create_pipeline_header())
    console.print()

    strategy_list = parse_strategies(strategies)

    # Summary of what we'll do
    summary = Table(box=box.SIMPLE, show_header=False)
    summary.add_column("Step", style="bold")
    summary.add_column("Status", style="cyan")

    summary.add_row(
        "1. Dataset Generation",
        "[green]SKIP[/green]" if skip_generation else f"[cyan]{students} students[/cyan]",
    )
    summary.add_row(
        "2. LLM Detection",
        "[green]SKIP[/green]"
        if skip_detection
        else f"[cyan]{len(strategy_list)} strategies[/cyan]",
    )
    summary.add_row(
        "3. Analysis", "[green]SKIP[/green]" if skip_analysis else f"[cyan]{output_report}[/cyan]"
    )
    summary.add_row("Strategies", ", ".join(strategy_list))

    console.print(Panel(summary, title="[bold]Pipeline Configuration[/bold]", border_style="blue"))
    console.print()

    if not yes and not typer.confirm("Proceed with pipeline?", default=True):
        console.print("[yellow]Cancelled.[/yellow]")
        raise typer.Exit(0)

    console.print()
    start_time = datetime.now()

    # Run the entire pipeline in a single async context
    asyncio.run(
        run_pipeline_async(
            students=students,
            seed=seed,
            strategy_list=strategy_list,
            skip_generation=skip_generation,
            skip_detection=skip_detection,
            skip_analysis=skip_analysis,
            concurrency=concurrency,
            force=force,
            output_report=output_report,
        )
    )

    # -------------------------------------------------------------------------
    # Summary
    # -------------------------------------------------------------------------
    elapsed = datetime.now() - start_time
    console.print()
    console.rule("[bold green]Pipeline Complete[/bold green]")
    console.print()
    console.print(f"[bold]Total time:[/bold] {elapsed}")
    console.print()

    if not skip_analysis:
        console.print("[bold]Outputs:[/bold]")
        console.print(f"  • Student submissions: {DEFAULT_OUTPUT_ROOT}/")
        console.print(f"  • Detection results:   {DETECTIONS_DIR}/")
        console.print(f"  • Thesis report:       {output_report}")


@app.command()
def quick(
    students: int = typer.Option(10, help="Number of students (default: 10 for quick test)"),
    strategy: str = typer.Option("minimal", help="Single strategy to test"),
):
    """Quick test run with fewer students and one strategy."""
    console.print("[bold yellow]Quick Test Mode[/bold yellow]")
    console.print(f"[dim]Students: {students}, Strategy: {strategy}[/dim]")
    console.print()

    run(
        students=students,
        strategies=strategy,
        force=True,
        output_report=Path("quick_test_report.md"),
    )


if __name__ == "__main__":
    app()
