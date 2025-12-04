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
from utils.analysis_helpers import (
    ConfidenceAnalysis,
    MisconceptionStats,
    StrategyComparison,
    cluster_false_positives,
    compute_misconception_stats,
    calculate_mcnemar_test,
    ModelStats
)

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
    confidence_analysis = ConfidenceAnalysis()
    
    # Track results for McNemar's test (per model)
    # Map: model_name -> list of booleans (success/fail for each opportunity)
    # We only track "opportunities" (seeded misconceptions) for McNemar's recall test
    model_recall_tracking = {} 
    
    for det in detections:
        student = det.get("student", "")
        question = det.get("question", "")
        models_data = det.get("models", {})
        
        expected_id, is_clean = get_expected_misconception(manifest, student, question)
        
        # Build detections by model
        detections_by_model = {}
        for model, model_data in models_data.items():
            detections_by_model[model] = model_data.get("misconceptions", [])
            if model not in model_recall_tracking:
                model_recall_tracking[model] = []
        
        # Analyze this student-question
        analysis = StudentQuestionAnalysis(
            student=student,
            question=question,
            expected_id=expected_id,
            is_clean=is_clean,
        )
        
        for model, misconceptions in detections_by_model.items():
            has_tp = False
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
                
                if classification.result == MatchResult.TRUE_POSITIVE:
                    has_tp = True
                
                # Track confidence
                conf = m.get("confidence", 0.0)
                confidence_analysis.add_detection(classification, conf)
                
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
            
            # For McNemar's: Did this model find the expected misconception?
            if not is_clean and expected_id:
                model_recall_tracking[model].append(has_tp)
        
        analyses.append(analysis)
    
    # Compute metrics
    metrics = compute_metrics(analyses)
    metrics["strategy"] = strategy
    metrics["interesting_discoveries"] = interesting_discoveries
    metrics["total_detections_files"] = len(detections)
    
    # Advanced stats
    metrics["misconception_stats"] = compute_misconception_stats(analyses, groundtruth)
    metrics["fp_clusters"] = cluster_false_positives(analyses)
    metrics["confidence_stats"] = {
        "avg_tp_confidence": confidence_analysis.avg_tp_confidence,
        "avg_fp_confidence": confidence_analysis.avg_fp_confidence,
        "calibration_gap": confidence_analysis.calibration_gap,
    }
    metrics["model_recall_tracking"] = model_recall_tracking
    
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


def generate_thesis_report(
    all_metrics: dict[str, dict],
    output_path: Path,
):
    """Generate comprehensive thesis-quality markdown report."""
    lines = [
        "# LLM Misconception Detection: Research Analysis",
        f"\nGenerated: {datetime.now(timezone.utc).isoformat()}",
        "\n## Executive Summary\n",
    ]
    
    # Strategy Comparison Matrix
    lines.append("### Strategy Comparison Matrix")
    lines.append("| Strategy | Precision | Recall | F1 Score | TP | FP | FN | Conf. Gap |")
    lines.append("|----------|-----------|--------|----------|----|----|----|-----------|")
    
    for strategy, metrics in all_metrics.items():
        if "error" in metrics:
            lines.append(f"| {strategy} | ERROR | - | - | - | - | - | - |")
        else:
            conf = metrics.get("confidence_stats", {})
            lines.append(
                f"| **{strategy}** | {metrics['precision']:.3f} | {metrics['recall']:.3f} | "
                f"**{metrics['f1_score']:.3f}** | {metrics['true_positives']} | "
                f"{metrics['false_positives']} | {metrics['false_negatives']} | "
                f"{conf.get('calibration_gap', 0):.3f} |"
            )
            
    # Model Comparison Section
    lines.append("\n## Model Showdown: GPT-5.1 vs Gemini-2.5-Flash\n")
    lines.append("Aggregate performance comparison across all strategies.")
    
    # Aggregate model stats
    model_stats = {}
    
    # For McNemar's
    agg_recall_tracking = {}
    
    for strategy, metrics in all_metrics.items():
        if "error" in metrics: continue
        
        # Per-model breakdown from metrics
        pm = metrics.get("per_model", {})
        for model, stats in pm.items():
            short = model.split("/")[-1]
            if short not in model_stats:
                model_stats[short] = {"tp": 0, "fp": 0, "total": 0}
            model_stats[short]["tp"] += stats["tp"]
            model_stats[short]["fp"] += stats["fp"]
            model_stats[short]["total"] += stats["total"]
            
        # Aggregate tracking for McNemar's
        tracking = metrics.get("model_recall_tracking", {})
        for model, results in tracking.items():
            short = model.split("/")[-1]
            if short not in agg_recall_tracking:
                agg_recall_tracking[short] = []
            agg_recall_tracking[short].extend(results)
            
    lines.append("\n### Aggregate Metrics")
    lines.append("| Model | TP | FP | Precision | Recall (est) |")
    lines.append("|-------|----|----|-----------|--------------|")
    
    for model, stats in model_stats.items():
        tp = stats["tp"]
        fp = stats["fp"]
        precision = tp / (tp + fp) if (tp + fp) > 0 else 0
        # Recall estimation is tricky without total FN per model, but we can use McNemar's tracking length
        # Total opportunities = len(agg_recall_tracking[model])
        total_opps = len(agg_recall_tracking.get(model, []))
        recall = tp / total_opps if total_opps > 0 else 0
        
        lines.append(f"| **{model}** | {tp} | {fp} | {precision:.3f} | {recall:.3f} |")
        
    # Statistical Significance
    lines.append("\n### Statistical Significance (McNemar's Test)")
    lines.append("Testing the null hypothesis that both models have equal sensitivity (recall).")
    
    models = list(agg_recall_tracking.keys())
    if len(models) >= 2:
        m1, m2 = models[0], models[1]
        stat, p_val, table = calculate_mcnemar_test(agg_recall_tracking[m1], agg_recall_tracking[m2])
        
        lines.append(f"\n**Comparison: {m1} vs {m2}**")
        lines.append(f"- **Statistic**: {stat:.4f}")
        lines.append(f"- **P-Value**: {p_val:.4e}")
        if p_val < 0.05:
            lines.append("- **Result**: Statistically Significant Difference (p < 0.05)")
        else:
            lines.append("- **Result**: No Significant Difference (p >= 0.05)")
            
        lines.append("\n**Contingency Table**")
        lines.append(f"| | {m2} Correct | {m2} Wrong |")
        lines.append("|---|---|---|")
        lines.append(f"| **{m1} Correct** | {table['both_correct']} | {table['only_a']} |")
        lines.append(f"| **{m1} Wrong** | {table['only_b']} | {table['both_wrong']} |")
        
    # Category Breakdown
    lines.append("\n## Performance by Category\n")
    lines.append("Breakdown of detection performance by misconception category (Topic).")
    
    # Aggregate by topic
    topic_stats = {}
    for metrics in all_metrics.values():
        if "error" in metrics: continue
        for mid, stat in metrics.get("misconception_stats", {}).items():
            topic = stat.topic
            if topic not in topic_stats:
                topic_stats[topic] = {"tp": 0, "fn": 0, "fp": 0}
            topic_stats[topic]["tp"] += stat.true_positives
            topic_stats[topic]["fn"] += stat.false_negatives
            topic_stats[topic]["fp"] += stat.false_positives
            
    lines.append("| Category | Recall | Precision | TP | FN | FP |")
    lines.append("|----------|--------|-----------|----|----|----|")
    
    for topic, s in sorted(topic_stats.items()):
        recall = s["tp"] / (s["tp"] + s["fn"]) if (s["tp"] + s["fn"]) > 0 else 0
        precision = s["tp"] / (s["tp"] + s["fp"]) if (s["tp"] + s["fp"]) > 0 else 0
        lines.append(f"| {topic} | {recall:.2f} | {precision:.2f} | {s['tp']} | {s['fn']} | {s['fp']} |")

    lines.append("\n## Deep Dive: Misconception Difficulty\n")
    
    # Aggregate misconception stats across strategies
    agg_stats = {}
    for metrics in all_metrics.values():
        if "error" in metrics: continue
        for mid, stat in metrics.get("misconception_stats", {}).items():
            if mid not in agg_stats:
                agg_stats[mid] = {"tp": 0, "fn": 0, "fp": 0, "name": stat.name, "topic": stat.topic}
            agg_stats[mid]["tp"] += stat.true_positives
            agg_stats[mid]["fn"] += stat.false_negatives
            agg_stats[mid]["fp"] += stat.false_positives
            
    # Sort by Recall (Hardest first)
    sorted_stats = sorted(
        agg_stats.items(), 
        key=lambda x: x[1]["tp"] / (x[1]["tp"] + x[1]["fn"]) if (x[1]["tp"] + x[1]["fn"]) > 0 else 0
    )
    
    lines.append("\n### Hardest Misconceptions (Low Recall)")
    lines.append("| ID | Name | Topic | Recall | TP | FN |")
    lines.append("|----|------|-------|--------|----|----|")
    
    for mid, s in sorted_stats[:5]:
        total = s["tp"] + s["fn"]
        recall = s["tp"] / total if total > 0 else 0
        lines.append(f"| {mid} | {s['name']} | {s['topic']} | {recall:.2f} | {s['tp']} | {s['fn']} |")
        
    lines.append("\n### Most Confusing Misconceptions (High FP)")
    # Sort by FP count
    sorted_fp = sorted(agg_stats.items(), key=lambda x: x[1]["fp"], reverse=True)
    
    lines.append("| ID | Name | Topic | FP Count |")
    lines.append("|----|------|-------|----------|")
    
    for mid, s in sorted_fp[:5]:
        if s["fp"] > 0:
            lines.append(f"| {mid} | {s['name']} | {s['topic']} | {s['fp']} |")
            
    lines.append("\n## Hallucination Analysis\n")
    lines.append("Recurring false positives that do not match any known misconception ID.")
    
    for strategy, metrics in all_metrics.items():
        clusters = metrics.get("fp_clusters", [])
        if clusters:
            lines.append(f"\n### {strategy.title()} Hallucinations")
            for c in clusters[:3]:
                lines.append(f"- **\"{c['name']}\"** ({c['count']} times)")
                lines.append(f"  - Example: {c['example_student']} {c['example_question']}")
                lines.append(f"  - Models: {', '.join(c['models'])}")

    lines.append("\n## Interesting Discoveries (Clean Files)\n")
    lines.append("Potential genuine issues found in clean files.")
    
    for strategy, metrics in all_metrics.items():
        discoveries = metrics.get("interesting_discoveries", [])
        if discoveries:
            lines.append(f"### {strategy.title()}")
            for d in discoveries[:5]:
                lines.append(f"- **{d['student']}** {d['question']}: {d['detected_name']}")
                lines.append(f"  - Matched to: {d.get('matched_to', 'None')} ({d.get('match_score', 0):.2f})")
    
    output_path.write_text("\n".join(lines))


@app.command()
def analyze(
    strategy: str = typer.Option(None, help="Specific strategy to analyze (default: all)"),
    detections_dir: Path = typer.Option(DEFAULT_DETECTIONS_DIR, help="Detections directory"),
    output: Path = typer.Option(Path("thesis_report.md"), help="Output report path"),
):
    """Analyze detections and generate thesis-quality report."""
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
        if "error" not in all_metrics[s]:
            display_metrics(all_metrics[s], s)
    
    generate_thesis_report(all_metrics, output)
    console.print(f"[green]Thesis report saved to {output}[/green]")


if __name__ == "__main__":
    app()
