"""
Full Pipeline: Generate Dataset -> Detect Misconceptions -> Analyze Results

Interactive pipeline for running the entire research experiment:
    uv run pipeline

This will prompt for configuration and run all steps.
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
from rich.prompt import Confirm, IntPrompt, Prompt
from rich.table import Table
from rich.text import Text

from analyze_cli import (
    ASSET_DIR,
    DEFAULT_GROUNDTRUTH_PATH,
    DEFAULT_MANIFEST_PATH,
    MatchMode,
    bootstrap_metrics,
    build_dataframes,
    compute_dataset_summary,
    compute_misconception_recall,
    ensure_asset_dir,
    generate_report,
    generate_run_id,
    load_groundtruth,
    load_manifest,
    plot_hallucinations,
    plot_matcher_ablation,
    plot_matcher_pr_scatter,
    plot_matcher_strategy_grid,
    plot_misconception_recall_bars,
    plot_model_comparison,
    plot_precision_recall_scatter,
    plot_strategy_f1_comparison,
    plot_topic_heatmap,
    plot_topic_recall_bars,
    save_run,
    summarize_metrics,
)
from llm_miscons_cli import (
    DEFAULT_OUTPUT_DIR as DETECTIONS_DIR,
)
from llm_miscons_cli import (
    MODELS,
    MODEL_SHORT_NAMES,
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

app = typer.Typer(
    help="Full pipeline: generate -> detect -> analyze",
    invoke_without_command=True,
)
console = Console()


@app.callback()
def callback(ctx: typer.Context):
    """Interactive pipeline - runs by default when no subcommand given."""
    if ctx.invoked_subcommand is None:
        # Run interactive mode
        interactive()


async def run_pipeline_async(
    students: int,
    seed: int | None,
    strategy_list: list[str],
    run_tag: str | None,
    notes: str,
    skip_generation: bool,
    skip_detection: bool,
    skip_analysis: bool,
    concurrency: int,
    force: bool,
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
    # Step 3: Analysis with Full Matcher Ablation
    # -------------------------------------------------------------------------
    if not skip_analysis:
        console.rule("[bold cyan]Step 3: Analysis & Report Generation[/bold cyan]")
        console.print()

        groundtruth = load_groundtruth(DEFAULT_GROUNDTRUTH_PATH)
        manifest_full = load_manifest(DEFAULT_MANIFEST_PATH)

        # Extract metadata
        manifest_meta = {
            "seed": manifest_full.get("seed"),
            "model": manifest_full.get("model"),
            "generated_at": manifest_full.get("generated_at"),
        }
        manifest_students = manifest_full.get("students", [])
        dataset_summary = compute_dataset_summary(manifest_students)

        available_strategies = [
            d.name for d in DETECTIONS_DIR.iterdir() if d.is_dir() and not d.name.startswith("_")
        ]

        if not available_strategies:
            console.print("[red]No detection results found.[/red]")
            return results

        console.print(f"[dim]Analyzing strategies: {', '.join(available_strategies)}[/dim]")
        console.print("[dim]Running matcher ablation: fuzzy_only, semantic_only, hybrid[/dim]")
        console.print()

        # Run with match_mode=all for full ablation
        detections_df, opportunities_df = build_dataframes(
            detections_dir=DETECTIONS_DIR,
            strategies=available_strategies,
            manifest=manifest_full,
            groundtruth=groundtruth,
            match_mode=MatchMode.ALL,
            use_embeddings=True,
        )

        # Compute metrics with match_mode grouping
        group_cols = ["match_mode", "strategy", "model"]
        metrics = summarize_metrics(detections_df, group_cols)
        ci = bootstrap_metrics(
            detections_df,
            group_cols=group_cols,
            unit_cols=["student", "question"],
            iters=400,
        )

        # Generate all plots
        ensure_asset_dir()
        asset_paths = {
            "heatmap": ASSET_DIR / "topic_heatmap.png",
            "hallucinations": ASSET_DIR / "hallucinations.png",
            "strategy_f1": ASSET_DIR / "strategy_f1_comparison.png",
            "pr_scatter": ASSET_DIR / "precision_recall_scatter.png",
            "topic_bars": ASSET_DIR / "topic_recall_bars.png",
            "model_comparison": ASSET_DIR / "model_comparison.png",
            "misconception_recall": ASSET_DIR / "misconception_recall.png",
            "matcher_ablation": ASSET_DIR / "matcher_ablation.png",
            "matcher_pr_scatter": ASSET_DIR / "matcher_pr_scatter.png",
            "matcher_strategy_grid": ASSET_DIR / "matcher_strategy_grid.png",
        }

        # Filter to hybrid for topic-related plots
        hybrid_opps = opportunities_df[opportunities_df["match_mode"] == "hybrid"]
        hybrid_dets = detections_df[detections_df["match_mode"] == "hybrid"]
        hybrid_metrics = metrics[metrics["match_mode"] == "hybrid"].copy()

        plot_topic_heatmap(hybrid_opps, asset_paths["heatmap"])
        plot_hallucinations(hybrid_dets, asset_paths["hallucinations"])
        plot_topic_recall_bars(hybrid_opps, asset_paths["topic_bars"])

        # Per-misconception analysis
        misconception_stats = compute_misconception_recall(hybrid_opps, groundtruth)
        if not misconception_stats.empty:
            plot_misconception_recall_bars(misconception_stats, asset_paths["misconception_recall"])

        # Ablation-specific plots
        plot_matcher_ablation(metrics, asset_paths["matcher_ablation"])
        plot_matcher_pr_scatter(metrics, asset_paths["matcher_pr_scatter"])
        plot_matcher_strategy_grid(metrics, asset_paths["matcher_strategy_grid"])

        # Strategy/model comparison (use hybrid)
        if not hybrid_metrics.empty:
            plot_strategy_f1_comparison(hybrid_metrics, asset_paths["strategy_f1"])
            plot_precision_recall_scatter(hybrid_metrics, asset_paths["pr_scatter"])
            plot_model_comparison(hybrid_metrics, asset_paths["model_comparison"])

        # Build asset paths for run-local report
        run_asset_paths = {k: Path("assets") / v.name for k, v in asset_paths.items()}

        report_text = generate_report(
            metrics,
            ci,
            opportunities_df,
            detections_df,
            run_asset_paths,
            misconception_stats=misconception_stats,
            dataset_summary=dataset_summary,
            manifest_meta=manifest_meta,
            match_mode="all",
        )

        # Save run
        run_id = generate_run_id(run_tag, manifest_meta.get("seed"))
        run_dir = save_run(
            run_id=run_id,
            manifest_full=manifest_full,
            manifest_meta=manifest_meta,
            dataset_summary=dataset_summary,
            metrics=metrics,
            ci=ci,
            opportunities_df=opportunities_df,
            report_text=report_text,
            asset_paths=asset_paths,
            match_mode="all",
            notes=notes,
        )

        console.print(f"[green]✓ Run saved to {run_dir}[/green]")
        console.print(f"[dim]Report: {run_dir / 'report.md'}[/dim]")
        console.print(f"[dim]Data: {run_dir / 'data.json'}[/dim]")

        results["analysis"] = {
            "run_id": run_id,
            "run_dir": str(run_dir),
            "metrics": metrics.to_dict(orient="records"),
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


def interactive():
    """Interactive mode - prompts for configuration before running."""
    console.print(create_pipeline_header())
    console.print()

    console.print("[bold cyan]Interactive Pipeline Configuration[/bold cyan]")
    console.print()

    # Step 1: Generation
    console.print("[bold]Step 1: Dataset Generation[/bold]")
    skip_generation = not Confirm.ask("Generate new student submissions?", default=False)

    students = 60
    seed = None
    force = False

    if not skip_generation:
        students = IntPrompt.ask("Number of students to generate", default=60)
        seed_input = Prompt.ask("Random seed (leave blank for auto)", default="")
        seed = int(seed_input) if seed_input else None
        force = Confirm.ask("Overwrite existing manifest if present?", default=True)

    console.print()

    # Step 2: Detection
    console.print("[bold]Step 2: LLM Detection[/bold]")
    skip_detection = not Confirm.ask("Run LLM misconception detection?", default=True)

    strategy_list = STRATEGIES  # All strategies
    console.print(f"[dim]Strategies: {', '.join(STRATEGIES)}[/dim]")
    console.print(f"[dim]Models: {', '.join(MODEL_SHORT_NAMES.values())}[/dim]")

    console.print()

    # Step 3: Analysis
    console.print("[bold]Step 3: Analysis[/bold]")
    skip_analysis = not Confirm.ask("Run analysis with matcher ablation?", default=True)
    console.print("[dim]Matchers: fuzzy_only, semantic_only, hybrid[/dim]")

    console.print()

    # Run tag
    console.print("[bold]Run Configuration[/bold]")
    run_tag = Prompt.ask("Run tag (e.g., 'run2', 'experiment_a')", default="")
    notes = Prompt.ask("Notes for this run", default="")

    console.print()

    # Summary
    summary = Table(box=box.SIMPLE, show_header=False)
    summary.add_column("Setting", style="bold")
    summary.add_column("Value", style="cyan")

    summary.add_row(
        "1. Generation", "[green]SKIP[/green]" if skip_generation else f"{students} students"
    )
    summary.add_row(
        "2. Detection",
        "[green]SKIP[/green]"
        if skip_detection
        else f"{len(strategy_list)} strategies × {len(MODELS)} models",
    )
    summary.add_row(
        "3. Analysis", "[green]SKIP[/green]" if skip_analysis else "3 matchers (ablation)"
    )
    summary.add_row("Run tag", run_tag if run_tag else "(auto-generated)")
    if notes:
        summary.add_row("Notes", notes[:50])

    console.print(Panel(summary, title="[bold]Configuration Summary[/bold]", border_style="blue"))
    console.print()

    if not Confirm.ask("Proceed with pipeline?", default=True):
        console.print("[yellow]Cancelled.[/yellow]")
        return

    console.print()
    start_time = datetime.now()

    # Run pipeline
    asyncio.run(
        run_pipeline_async(
            students=students,
            seed=seed,
            strategy_list=strategy_list,
            run_tag=run_tag if run_tag else None,
            notes=notes,
            skip_generation=skip_generation,
            skip_detection=skip_detection,
            skip_analysis=skip_analysis,
            concurrency=20,
            force=force,
        )
    )

    # Summary
    elapsed = datetime.now() - start_time
    console.print()
    console.rule("[bold green]Pipeline Complete[/bold green]")
    console.print(f"[bold]Total time:[/bold] {elapsed}")


@app.command("run")
def run_cmd(
    students: int = typer.Option(60, help="Number of students to generate"),
    seed: int = typer.Option(None, help="Random seed (default: current timestamp)"),
    strategies: str = typer.Option(
        "all",
        help="Strategies to run: 'all' or comma-separated (minimal,baseline,socratic,rubric_only)",
    ),
    run_tag: str = typer.Option(None, "--run-tag", "-t", help="Tag for this run"),
    notes: str = typer.Option("", "--notes", "-n", help="Notes to attach to this run"),
    skip_generation: bool = typer.Option(False, help="Skip dataset generation (use existing)"),
    skip_detection: bool = typer.Option(False, help="Skip LLM detection (use existing)"),
    skip_analysis: bool = typer.Option(False, help="Skip analysis"),
    concurrency: int = typer.Option(20, help="Max concurrent API requests"),
    force: bool = typer.Option(False, help="Overwrite existing manifest"),
    yes: bool = typer.Option(False, "--yes", "-y", help="Skip confirmation prompt"),
):
    """
    Run the full research pipeline (non-interactive).

    Steps:
    1. Generate manifest + student submissions
    2. Run misconception detection with configured models (4 strategies)
    3. Analyze with matcher ablation and save to runs/
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
        else f"[cyan]{len(strategy_list)} strategies × {len(MODELS)} models[/cyan]",
    )
    summary.add_row(
        "3. Analysis",
        "[green]SKIP[/green]" if skip_analysis else "[cyan]3 matchers (ablation)[/cyan]",
    )
    summary.add_row("Strategies", ", ".join(strategy_list))
    summary.add_row("Run tag", run_tag if run_tag else "(auto-generated)")

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
            run_tag=run_tag,
            notes=notes,
            skip_generation=skip_generation,
            skip_detection=skip_detection,
            skip_analysis=skip_analysis,
            concurrency=concurrency,
            force=force,
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


@app.command()
def quick(
    students: int = typer.Option(10, help="Number of students (default: 10 for quick test)"),
    strategy: str = typer.Option("minimal", help="Single strategy to test"),
):
    """Quick test run with fewer students and one strategy."""
    console.print("[bold yellow]Quick Test Mode[/bold yellow]")
    console.print(f"[dim]Students: {students}, Strategy: {strategy}[/dim]")
    console.print()

    run_cmd(
        students=students,
        strategies=strategy,
        run_tag="quick_test",
        notes="Quick test run",
        force=True,
        yes=True,
    )


if __name__ == "__main__":
    app()
