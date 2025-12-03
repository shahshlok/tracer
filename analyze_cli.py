"""
Analysis CLI for comparing LLM detections against ground truth.

Computes TP/FP/FN metrics and generates accuracy reports.
"""

import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import typer
from rich import box
from rich.console import Console
from rich.panel import Panel
from rich.progress import Progress, SpinnerColumn, TextColumn
from rich.prompt import Prompt
from rich.table import Table
from rich.text import Text

from utils.matching import classify_detection, MatchResult
from utils.matching.classifier import compute_metrics, StudentQuestionAnalysis

app = typer.Typer(help="Analyze LLM misconception detections against ground truth")
console = Console()

DEFAULT_DETECTIONS_DIR = Path("detections")
DEFAULT_MANIFEST_PATH = Path("authentic_seeded/manifest.json")
DEFAULT_GROUNDTRUTH_PATH = Path("data/a2/groundtruth.json")


def load_groundtruth(path: Path) -> list[dict[str, Any]]:
    """Load ground truth misconception definitions."""
    return json.loads(path.read_text())


def load_manifest(path: Path) -> dict[str, Any]:
    """Load manifest with injection info."""
    return json.loads(path.read_text())


def get_expected_misconception(manifest: dict, student: str, question: str) -> tuple[str | None, bool]:
    """
    Get expected misconception ID for a student-question pair.
    
    Returns (misconception_id, is_clean_file)
    """
    for s in manifest.get("students", []):
        if s.get("folder_name") == student:
            file_info = s.get("files", {}).get(question, {})
            if file_info.get("type") == "CLEAN":
                return None, True
            return file_info.get("misconception_id"), False
    return None, True  # Not found, treat as clean


def load_detections_for_strategy(strategy_dir: Path) -> list[dict[str, Any]]:
    """Load all detection files for a strategy."""
    detections = []
    for f in strategy_dir.glob("*.json"):
        if f.name.startswith("_"):  # Skip stats files
            continue
        try:
            data = json.loads(f.read_text())
            if data.get("status") == "success":
                detections.append(data)
        except Exception:
            pass
    return detections


def analyze_strategy(
    strategy: str,
    detections_dir: Path,
    manifest: dict,
    groundtruth: list[dict],
    fuzzy_threshold: float = 0.8,
    semantic_threshold: float = 0.7,
) -> dict[str, Any]:
    """Analyze all detections for a single strategy."""
    strategy_dir = detections_dir / strategy
    if not strategy_dir.exists():
        return {"error": f"Strategy directory not found: {strategy_dir}"}
    
    detections = load_detections_for_strategy(strategy_dir)
    if not detections:
        return {"error": "No detections found"}
    
    analyses: list[StudentQuestionAnalysis] = []
    interesting_discoveries: list[dict] = []
    
    for det in detections:
        student = det.get("student", "")
        question = det.get("question", "")
        models_data = det.get("models", {})
        
        expected_id, is_clean = get_expected_misconception(manifest, student, question)
        
        # Build detections by model
        detections_by_model = {}
        for model, model_data in models_data.items():
            detections_by_model[model] = model_data.get("misconceptions", [])
        
        # Analyze this student-question
        analysis = StudentQuestionAnalysis(
            student=student,
            question=question,
            expected_id=expected_id,
            is_clean=is_clean,
        )
        
        for model, misconceptions in detections_by_model.items():
            for m in misconceptions:
                classification = classify_detection(
                    detection=m,
                    expected_misconception_id=expected_id,
                    groundtruth=groundtruth,
                    is_clean_file=is_clean,
                    model=model,
                    student=student,
                    question=question,
                    fuzzy_threshold=fuzzy_threshold,
                    semantic_threshold=semantic_threshold,
                )
                analysis.classifications.append(classification)
                
                # Track interesting discoveries
                if classification.result == MatchResult.INTERESTING:
                    interesting_discoveries.append({
                        "student": student,
                        "question": question,
                        "model": model,
                        "detected_name": classification.detected_name,
                        "detected_description": classification.detected_description,
                        "matched_to": classification.matched_id,
                        "match_score": classification.match_score,
                    })
        
        analyses.append(analysis)
    
    # Compute metrics
    metrics = compute_metrics(analyses)
    metrics["strategy"] = strategy
    metrics["interesting_discoveries"] = interesting_discoveries
    metrics["total_detections_files"] = len(detections)
    
    return metrics


def display_metrics(metrics: dict[str, Any], strategy: str):
    """Display metrics in a nice format."""
    console.print()
    console.rule(f"[bold cyan]Analysis: {strategy}[/bold cyan]")
    console.print()
    
    # Main metrics table
    table = Table(box=box.ROUNDED, title="Detection Metrics", title_style="bold")
    table.add_column("Metric", style="white")
    table.add_column("Value", justify="right", style="cyan")
    
    table.add_row("True Positives (TP)", f"[green]{metrics['true_positives']}[/green]")
    table.add_row("False Positives (FP)", f"[red]{metrics['false_positives']}[/red]")
    table.add_row("False Negatives (FN)", f"[yellow]{metrics['false_negatives']}[/yellow]")
    table.add_row("Interesting Discoveries", f"[magenta]{metrics['interesting_discoveries']}[/magenta]")
    table.add_row("", "")
    table.add_row("Precision", f"{metrics['precision']:.3f}")
    table.add_row("Recall", f"{metrics['recall']:.3f}")
    table.add_row("F1 Score", f"[bold]{metrics['f1_score']:.3f}[/bold]")
    
    console.print(table)
    console.print()
    
    # Per-model breakdown
    if metrics.get("per_model"):
        model_table = Table(box=box.SIMPLE, title="Per-Model Breakdown", title_style="bold")
        model_table.add_column("Model", style="white")
        model_table.add_column("TP", justify="right", style="green")
        model_table.add_column("FP", justify="right", style="red")
        model_table.add_column("Interesting", justify="right", style="magenta")
        model_table.add_column("Total", justify="right", style="cyan")
        
        for model, stats in metrics["per_model"].items():
            short_name = model.split("/")[-1]
            model_table.add_row(
                short_name,
                str(stats["tp"]),
                str(stats["fp"]),
                str(stats["interesting"]),
                str(stats["total"]),
            )
        
        console.print(model_table)
        console.print()
    
    # Interesting discoveries preview
    discoveries = metrics.get("interesting_discoveries", [])
    if discoveries:
        console.print(f"[bold magenta]Interesting Discoveries ({len(discoveries)} found)[/bold magenta]")
        console.print()
        for i, d in enumerate(discoveries[:5], 1):
            console.print(f"  {i}. [cyan]{d['student']}[/cyan] {d['question']} ({d['model'].split('/')[-1]})")
            console.print(f"     [dim]{d['detected_name'][:60]}...[/dim]" if len(d.get('detected_name', '')) > 60 else f"     [dim]{d.get('detected_name', 'N/A')}[/dim]")
        if len(discoveries) > 5:
            console.print(f"  [dim]... and {len(discoveries) - 5} more[/dim]")
        console.print()


def generate_report(
    all_metrics: dict[str, dict],
    output_path: Path,
):
    """Generate markdown report."""
    lines = [
        "# LLM Misconception Detection Analysis Report",
        f"\nGenerated: {datetime.now(timezone.utc).isoformat()}",
        "\n## Summary\n",
    ]
    
    # Summary table
    lines.append("| Strategy | TP | FP | FN | Precision | Recall | F1 |")
    lines.append("|----------|----|----|----|-----------|---------|----|")
    
    for strategy, metrics in all_metrics.items():
        if "error" in metrics:
            lines.append(f"| {strategy} | ERROR | - | - | - | - | - |")
        else:
            lines.append(
                f"| {strategy} | {metrics['true_positives']} | {metrics['false_positives']} | "
                f"{metrics['false_negatives']} | {metrics['precision']:.3f} | "
                f"{metrics['recall']:.3f} | {metrics['f1_score']:.3f} |"
            )
    
    lines.append("\n## Per-Strategy Details\n")
    
    for strategy, metrics in all_metrics.items():
        lines.append(f"### {strategy.title()}\n")
        
        if "error" in metrics:
            lines.append(f"Error: {metrics['error']}\n")
            continue
        
        lines.append(f"- Total detection files: {metrics.get('total_detections_files', 'N/A')}")
        lines.append(f"- True Positives: {metrics['true_positives']}")
        lines.append(f"- False Positives: {metrics['false_positives']}")
        lines.append(f"- False Negatives: {metrics['false_negatives']}")
        lines.append(f"- Precision: {metrics['precision']:.3f}")
        lines.append(f"- Recall: {metrics['recall']:.3f}")
        lines.append(f"- F1 Score: {metrics['f1_score']:.3f}")
        lines.append("")
        
        # Per-model
        if metrics.get("per_model"):
            lines.append("#### Per-Model Breakdown\n")
            lines.append("| Model | TP | FP | Interesting | Total |")
            lines.append("|-------|----|----|-------------|-------|")
            for model, stats in metrics["per_model"].items():
                lines.append(f"| {model.split('/')[-1]} | {stats['tp']} | {stats['fp']} | {stats['interesting']} | {stats['total']} |")
            lines.append("")
    
    # Interesting discoveries section
    lines.append("\n## Interesting Discoveries\n")
    lines.append("These are misconceptions detected in CLEAN files (no injection). They may represent:")
    lines.append("- Genuine issues in the generated 'correct' code")
    lines.append("- LLM over-detection / false alarms")
    lines.append("- Edge cases worth investigating\n")
    
    for strategy, metrics in all_metrics.items():
        discoveries = metrics.get("interesting_discoveries", [])
        if discoveries:
            lines.append(f"### {strategy.title()} ({len(discoveries)} discoveries)\n")
            for d in discoveries[:10]:
                lines.append(f"- **{d['student']}** {d['question']} ({d['model'].split('/')[-1]})")
                lines.append(f"  - Detected: {d['detected_name']}")
                lines.append(f"  - Matched to: {d.get('matched_to', 'None')} (score: {d.get('match_score', 0):.2f})")
            if len(discoveries) > 10:
                lines.append(f"\n*... and {len(discoveries) - 10} more*\n")
            lines.append("")
    
    output_path.write_text("\n".join(lines))


def interactive_main():
    """Interactive analysis CLI."""
    console.print(Panel(
        Text("LLM Misconception Analysis", style="bold white on blue", justify="center"),
        box=box.DOUBLE,
        border_style="blue",
    ))
    console.print()
    
    # Check for detections
    if not DEFAULT_DETECTIONS_DIR.exists():
        console.print("[red]No detections directory found. Run 'uv run llm-miscons' first.[/red]")
        return
    
    strategies = [d.name for d in DEFAULT_DETECTIONS_DIR.iterdir() if d.is_dir() and not d.name.startswith("_")]
    if not strategies:
        console.print("[red]No strategy directories found in detections/[/red]")
        return
    
    console.print(f"[green]Found strategies: {', '.join(strategies)}[/green]")
    console.print()
    
    # Load groundtruth and manifest
    try:
        groundtruth = load_groundtruth(DEFAULT_GROUNDTRUTH_PATH)
        manifest = load_manifest(DEFAULT_MANIFEST_PATH)
    except Exception as e:
        console.print(f"[red]Error loading data: {e}[/red]")
        return
    
    console.print(f"[dim]Loaded {len(groundtruth)} misconception definitions[/dim]")
    console.print(f"[dim]Loaded manifest with {manifest.get('student_count', '?')} students[/dim]")
    console.print()
    
    # Select strategies to analyze
    console.print("[bold]Analyze:[/bold]")
    console.print("  [1] All strategies")
    console.print("  [2] Select specific strategy")
    choice = Prompt.ask("Choice", choices=["1", "2"], default="1")
    
    if choice == "2":
        console.print(f"Available: {', '.join(strategies)}")
        selected = Prompt.ask("Strategy name", default=strategies[0])
        strategies = [selected] if selected in strategies else strategies[:1]
    
    console.print()
    
    # Run analysis
    all_metrics = {}
    with Progress(SpinnerColumn(), TextColumn("[progress.description]{task.description}"), console=console) as progress:
        for strategy in strategies:
            task = progress.add_task(f"Analyzing {strategy}...", total=None)
            metrics = analyze_strategy(
                strategy=strategy,
                detections_dir=DEFAULT_DETECTIONS_DIR,
                manifest=manifest,
                groundtruth=groundtruth,
            )
            all_metrics[strategy] = metrics
            progress.remove_task(task)
    
    # Display results
    for strategy, metrics in all_metrics.items():
        if "error" in metrics:
            console.print(f"[red]{strategy}: {metrics['error']}[/red]")
        else:
            display_metrics(metrics, strategy)
    
    # Save report
    console.print()
    if typer.confirm("Save detailed report?", default=True):
        report_path = Path("analysis_report.md")
        generate_report(all_metrics, report_path)
        console.print(f"[green]Report saved to {report_path}[/green]")
        
        # Also save JSON
        json_path = Path("analysis_report.json")
        # Convert non-serializable items
        for strategy in all_metrics:
            if "interesting_discoveries" in all_metrics[strategy]:
                pass  # Already serializable
        json_path.write_text(json.dumps(all_metrics, indent=2, default=str))
        console.print(f"[green]JSON saved to {json_path}[/green]")


@app.command()
def analyze(
    strategy: str = typer.Option(None, help="Specific strategy to analyze (default: all)"),
    detections_dir: Path = typer.Option(DEFAULT_DETECTIONS_DIR, help="Detections directory"),
    output: Path = typer.Option(Path("analysis_report.md"), help="Output report path"),
):
    """Analyze detections and generate report (non-interactive)."""
    groundtruth = load_groundtruth(DEFAULT_GROUNDTRUTH_PATH)
    manifest = load_manifest(DEFAULT_MANIFEST_PATH)
    
    if strategy:
        strategies = [strategy]
    else:
        strategies = [d.name for d in detections_dir.iterdir() if d.is_dir() and not d.name.startswith("_")]
    
    all_metrics = {}
    for s in strategies:
        console.print(f"[cyan]Analyzing {s}...[/cyan]")
        all_metrics[s] = analyze_strategy(s, detections_dir, manifest, groundtruth)
    
    for s, m in all_metrics.items():
        if "error" not in m:
            display_metrics(m, s)
    
    generate_report(all_metrics, output)
    console.print(f"[green]Report saved to {output}[/green]")


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    """Analyze LLM misconception detections."""
    if ctx.invoked_subcommand is None:
        interactive_main()


if __name__ == "__main__":
    app()
