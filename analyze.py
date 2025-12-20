"""
Clean analysis CLI for notional machine misconception detection.

Supports fuzzy, semantic, and hybrid matching.
Generates runs/a1/index.json, report.md, and charts.
"""

from __future__ import annotations

import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Literal

import matplotlib

matplotlib.use("Agg")  # Headless
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
import typer
from rich.console import Console
from rich.table import Table

from utils.matching.fuzzy import fuzzy_match_misconception
from utils.matching.hybrid import hybrid_match_misconception
from utils.matching.semantic import (
    precompute_groundtruth_embeddings,
    semantic_match_misconception,
)

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
DEFAULT_DETECTIONS_DIR = Path("detections/a1")
DEFAULT_MANIFEST_PATH = Path("authentic_seeded/a1/manifest.json")
DEFAULT_GROUNDTRUTH_PATH = Path("data/a1/groundtruth.json")
RUNS_DIR = Path("runs/a1")

app = typer.Typer(help="Analyze LLM misconception detections (v2)")
console = Console()


# ---------------------------------------------------------------------------
# Data Loading
# ---------------------------------------------------------------------------
def load_json(path: Path) -> Any:
    return json.loads(path.read_text())


def load_groundtruth(path: Path) -> list[dict[str, Any]]:
    return load_json(path)


def load_manifest(path: Path) -> dict[str, Any]:
    return load_json(path)


def discover_strategies(detections_dir: Path) -> list[str]:
    return sorted(
        [
            d.name
            for d in detections_dir.iterdir()
            if d.is_dir() and not d.name.startswith("_") and not d.name.startswith(".")
        ]
    )


def load_detections_for_strategy(strategy_dir: Path) -> list[dict[str, Any]]:
    results = []
    for f in strategy_dir.glob("*.json"):
        if f.name.startswith("_"):
            continue
        try:
            data = load_json(f)
            if data.get("status") == "success":
                results.append(data)
        except Exception:
            continue
    return results


def get_expected(manifest: dict[str, Any], student: str, question: str) -> tuple[str | None, bool]:
    for s in manifest.get("students", []):
        if s.get("folder_name") == student:
            info = s.get("files", {}).get(question, {})
            if info.get("type") == "CLEAN":
                return None, True
            return info.get("misconception_id"), False
    return None, True


# ---------------------------------------------------------------------------
# Field Adapter
# ---------------------------------------------------------------------------
def adapt_detection(mis: dict[str, Any]) -> dict[str, str]:
    """Adapt NotionalMisconception fields to matcher fields."""
    return {
        "name": mis.get("inferred_category_name", ""),
        "description": mis.get("conceptual_gap", ""),
        "student_belief": mis.get("student_thought_process", ""),
        "topic": "",  # Not available in new model
    }


# ---------------------------------------------------------------------------
# Core Analysis
# ---------------------------------------------------------------------------
MatchMode = Literal["fuzzy", "semantic", "hybrid", "all"]


def build_results_df(
    detections_dir: Path,
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    match_mode: MatchMode,
) -> pd.DataFrame:
    """Build results dataframe with matching."""

    gt_map = {m["id"]: m for m in groundtruth}
    gt_embeddings = None
    if match_mode in ("semantic", "hybrid", "all"):
        console.print("[cyan]Precomputing groundtruth embeddings...[/cyan]")
        gt_embeddings = precompute_groundtruth_embeddings(groundtruth)

    strategies = discover_strategies(detections_dir)
    console.print(f"[green]Strategies:[/green] {', '.join(strategies)}")

    # Determine which matchers to run
    if match_mode == "all":
        modes = ["fuzzy", "semantic", "hybrid"]
    else:
        modes = [match_mode]

    all_rows = []

    for current_mode in modes:
        console.print(f"[cyan]Running matcher: {current_mode}[/cyan]")
        rows = []

        for strategy in strategies:
            strategy_dir = detections_dir / strategy
            detections = load_detections_for_strategy(strategy_dir)

            for det in detections:
                student = det.get("student", "")
                question = det.get("question", "")
                expected_id, is_clean = get_expected(manifest, student, question)
                expected_category = (
                    gt_map.get(expected_id, {}).get("category", "") if expected_id else ""
                )

                for model, payload in det.get("models", {}).items():
                    mis_list = payload.get("misconceptions", []) or []
                    has_tp = False

                    for mis in mis_list:
                        adapted = adapt_detection(mis)

                        # Run appropriate matcher
                        if current_mode == "fuzzy":
                            matched_id, match_score, _ = fuzzy_match_misconception(
                                adapted["name"], adapted["description"], groundtruth
                            )
                            fuzzy_score, semantic_score = match_score, 0.0
                        elif current_mode == "semantic":
                            matched_id, match_score, _ = semantic_match_misconception(
                                adapted["name"],
                                adapted["description"],
                                adapted["student_belief"],
                                groundtruth,
                            )
                            fuzzy_score, semantic_score = 0.0, match_score
                        else:  # hybrid
                            result = hybrid_match_misconception(adapted, groundtruth)
                            matched_id = result.matched_id
                            match_score = result.score
                            fuzzy_score = result.fuzzy_score
                            semantic_score = result.semantic_score

                        # Classify result
                        if is_clean:
                            result_type = "FP_CLEAN"
                        elif matched_id == expected_id:
                            result_type = "TP"
                            has_tp = True
                        elif matched_id:
                            result_type = "FP_WRONG"
                        else:
                            result_type = "FP_HALLUCINATION"

                        rows.append(
                            {
                                "match_mode": current_mode,
                                "strategy": strategy,
                                "model": model,
                                "student": student,
                                "question": question,
                                "expected_id": expected_id,
                                "expected_category": expected_category,
                                "is_clean": is_clean,
                                "detected_name": adapted["name"],
                                "matched_id": matched_id,
                                "fuzzy_score": fuzzy_score,
                                "semantic_score": semantic_score,
                                "match_score": match_score,
                                "result": result_type,
                                "confidence": mis.get("confidence"),
                            }
                        )

                    # Record FN
                    if not is_clean and expected_id and not has_tp:
                        rows.append(
                            {
                                "match_mode": current_mode,
                                "strategy": strategy,
                                "model": model,
                                "student": student,
                                "question": question,
                                "expected_id": expected_id,
                                "expected_category": expected_category,
                                "is_clean": False,
                                "detected_name": "",
                                "matched_id": None,
                                "fuzzy_score": 0.0,
                                "semantic_score": 0.0,
                                "match_score": 0.0,
                                "result": "FN",
                                "confidence": None,
                            }
                        )

        all_rows.extend(rows)

    return pd.DataFrame(all_rows)


def compute_metrics(df: pd.DataFrame) -> dict[str, Any]:
    tp = (df["result"] == "TP").sum()
    fp = df["result"].isin(["FP_CLEAN", "FP_WRONG", "FP_HALLUCINATION"]).sum()
    fn = (df["result"] == "FN").sum()

    precision = tp / (tp + fp) if (tp + fp) > 0 else 0.0
    recall = tp / (tp + fn) if (tp + fn) > 0 else 0.0
    f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0.0

    return {
        "tp": int(tp),
        "fp": int(fp),
        "fn": int(fn),
        "precision": round(precision, 3),
        "recall": round(recall, 3),
        "f1": round(f1, 3),
    }


def metrics_by_group(df: pd.DataFrame, group_cols: list[str]) -> pd.DataFrame:
    records = []
    for keys, grp in df.groupby(group_cols):
        metrics = compute_metrics(grp)
        if isinstance(keys, tuple):
            rec = {col: key for col, key in zip(group_cols, keys)}
        else:
            rec = {group_cols[0]: keys}
        rec.update(metrics)
        records.append(rec)
    return pd.DataFrame(records).sort_values(group_cols)


# ---------------------------------------------------------------------------
# Chart Generation
# ---------------------------------------------------------------------------
def generate_charts(df: pd.DataFrame, assets_dir: Path, groundtruth: list[dict]) -> list[str]:
    """Generate all charts for the report."""
    assets_dir.mkdir(parents=True, exist_ok=True)
    charts = []

    # 1. Strategy F1 Comparison
    by_strat = metrics_by_group(df, ["strategy"])
    if not by_strat.empty:
        fig, ax = plt.subplots(figsize=(10, 6))
        ax.bar(
            by_strat["strategy"], by_strat["f1"], color=["#2ecc71", "#3498db", "#e74c3c", "#9b59b6"]
        )
        ax.set_ylabel("F1 Score")
        ax.set_title("F1 Score by Strategy")
        ax.set_ylim(0, 1)
        for i, v in enumerate(by_strat["f1"]):
            ax.text(i, v + 0.02, f"{v:.2f}", ha="center")
        plt.tight_layout()
        path = assets_dir / "strategy_f1.png"
        plt.savefig(path, dpi=150)
        plt.close()
        charts.append("strategy_f1.png")

    # 2. Model Comparison
    by_model = metrics_by_group(df, ["model"])
    if not by_model.empty:
        by_model["model_short"] = by_model["model"].apply(lambda x: x.split("/")[-1])
        fig, ax = plt.subplots(figsize=(12, 6))
        x = np.arange(len(by_model))
        width = 0.25
        ax.bar(x - width, by_model["precision"], width, label="Precision", color="#3498db")
        ax.bar(x, by_model["recall"], width, label="Recall", color="#2ecc71")
        ax.bar(x + width, by_model["f1"], width, label="F1", color="#e74c3c")
        ax.set_xticks(x)
        ax.set_xticklabels(by_model["model_short"], rotation=45, ha="right")
        ax.set_ylabel("Score")
        ax.set_title("Model Comparison")
        ax.legend()
        ax.set_ylim(0, 1)
        plt.tight_layout()
        path = assets_dir / "model_comparison.png"
        plt.savefig(path, dpi=150)
        plt.close()
        charts.append("model_comparison.png")

    # 3. Category Recall (Topic Difficulty)
    seeded = df[df["expected_id"].notna()]
    if not seeded.empty:
        by_cat = metrics_by_group(seeded, ["expected_category"])
        by_cat = by_cat.sort_values("recall")
        fig, ax = plt.subplots(figsize=(10, 6))
        colors = plt.cm.RdYlGn(by_cat["recall"])
        ax.barh(by_cat["expected_category"], by_cat["recall"], color=colors)
        ax.set_xlabel("Recall")
        ax.set_title("Detection Recall by Notional Machine Category")
        ax.set_xlim(0, 1)
        ax.axvline(0.5, color="gray", linestyle="--", alpha=0.5)
        for i, (_, row) in enumerate(by_cat.iterrows()):
            ax.text(row["recall"] + 0.02, i, f"{row['recall']:.2f}", va="center")
        plt.tight_layout()
        path = assets_dir / "category_recall.png"
        plt.savefig(path, dpi=150)
        plt.close()
        charts.append("category_recall.png")

    # 4. Strategy x Model Heatmap
    by_strat_model = metrics_by_group(df, ["strategy", "model"])
    if not by_strat_model.empty:
        by_strat_model["model_short"] = by_strat_model["model"].apply(lambda x: x.split("/")[-1])
        pivot = by_strat_model.pivot(index="strategy", columns="model_short", values="f1")
        fig, ax = plt.subplots(figsize=(12, 6))
        sns.heatmap(pivot, annot=True, fmt=".2f", cmap="YlGnBu", ax=ax, vmin=0, vmax=1)
        ax.set_title("F1 Score: Strategy × Model")
        plt.tight_layout()
        path = assets_dir / "strategy_model_heatmap.png"
        plt.savefig(path, dpi=150)
        plt.close()
        charts.append("strategy_model_heatmap.png")

    # 5. Per-Misconception Recall
    if not seeded.empty:
        gt_map = {g["id"]: g for g in groundtruth}
        by_mis = metrics_by_group(seeded, ["expected_id"])
        by_mis["name"] = by_mis["expected_id"].apply(
            lambda x: gt_map.get(x, {}).get("name", x)[:30]
        )
        by_mis = by_mis.sort_values("recall")
        fig, ax = plt.subplots(figsize=(10, 6))
        colors = plt.cm.RdYlGn(by_mis["recall"])
        ax.barh(by_mis["name"], by_mis["recall"], color=colors)
        ax.set_xlabel("Recall")
        ax.set_title("Per-Misconception Detection Recall")
        ax.set_xlim(0, 1.1)
        for i, (_, row) in enumerate(by_mis.iterrows()):
            n = row["tp"] + row["fn"]
            ax.text(
                row["recall"] + 0.02, i, f"{row['recall']:.2f} (n={n})", va="center", fontsize=9
            )
        plt.tight_layout()
        path = assets_dir / "misconception_recall.png"
        plt.savefig(path, dpi=150)
        plt.close()
        charts.append("misconception_recall.png")

    # 6. Hallucination Analysis
    fps = df[df["result"].str.startswith("FP")]
    if not fps.empty:
        top_fps = fps["detected_name"].value_counts().head(10)
        if not top_fps.empty:
            fig, ax = plt.subplots(figsize=(10, 6))
            ax.barh(range(len(top_fps)), top_fps.values, color="#e74c3c")
            ax.set_yticks(range(len(top_fps)))
            ax.set_yticklabels([n[:40] for n in top_fps.index])
            ax.set_xlabel("Count")
            ax.set_title("Top False Positive Detections")
            plt.tight_layout()
            path = assets_dir / "hallucinations.png"
            plt.savefig(path, dpi=150)
            plt.close()
            charts.append("hallucinations.png")

    return charts


# ---------------------------------------------------------------------------
# Report Generation
# ---------------------------------------------------------------------------
def generate_report(
    df: pd.DataFrame,
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    run_dir: Path,
    match_mode: str,
) -> Path:
    """Generate markdown report with embedded charts."""

    assets_dir = run_dir / "assets"
    charts = generate_charts(df, assets_dir, groundtruth)

    # For "all" mode, we need to show matcher comparison and use best (hybrid) for other sections
    is_ablation = match_mode == "all"
    gt_map = {g["id"]: g for g in groundtruth}

    if is_ablation:
        by_matcher = metrics_by_group(df, ["match_mode"])
        # Use hybrid subset for main report
        df_main = df[df["match_mode"] == "hybrid"]
        overall = compute_metrics(df_main)
        by_strategy = metrics_by_group(df_main, ["strategy"])
        by_model = metrics_by_group(df_main, ["model"])
        seeded = df_main[df_main["expected_id"].notna()]
        by_category = (
            metrics_by_group(seeded, ["expected_category"]) if not seeded.empty else pd.DataFrame()
        )
        by_misconception = (
            metrics_by_group(seeded, ["expected_id"]) if not seeded.empty else pd.DataFrame()
        )
    else:
        overall = compute_metrics(df)
        by_strategy = metrics_by_group(df, ["strategy"])
        by_model = metrics_by_group(df, ["model"])
        seeded = df[df["expected_id"].notna()]
        by_category = (
            metrics_by_group(seeded, ["expected_category"]) if not seeded.empty else pd.DataFrame()
        )
        by_misconception = (
            metrics_by_group(seeded, ["expected_id"]) if not seeded.empty else pd.DataFrame()
        )

    lines = [
        "# LLM Misconception Detection: Analysis Report",
        f"_Generated: {datetime.now(timezone.utc).isoformat()}_",
        "",
        "## Dataset Configuration",
        f"- **Students:** {manifest.get('student_count', 'N/A')}",
        f"- **Questions:** {len(manifest.get('questions', {}))}",
        f"- **Seed:** {manifest.get('seed', 'N/A')}",
        f"- **Match Mode:** {match_mode}",
        "",
    ]

    # Matcher Ablation section (only for "all" mode)
    if is_ablation:
        lines.extend(
            [
                "## Matcher Ablation Study",
                "",
                "> Comparing fuzzy (string-based), semantic (embedding-based), and hybrid (combined) matchers.",
                "",
                "| Matcher | TP | FP | FN | Precision | Recall | F1 |",
                "|---------|----|----|----|-----------| -------|-----|",
            ]
        )
        for _, row in by_matcher.iterrows():
            lines.append(
                f"| {row['match_mode']} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
            )
        lines.append("")
        lines.append("> **Note:** Remaining sections use **hybrid** matcher results only.")
        lines.append("")

    lines.extend(
        [
            "## Overall Metrics" + (" (Hybrid)" if is_ablation else ""),
            "| Metric | Value |",
            "|--------|-------|",
            f"| True Positives | {overall['tp']} |",
            f"| False Positives | {overall['fp']} |",
            f"| False Negatives | {overall['fn']} |",
            f"| **Precision** | **{overall['precision']:.3f}** |",
            f"| **Recall** | **{overall['recall']:.3f}** |",
            f"| **F1 Score** | **{overall['f1']:.3f}** |",
            "",
        ]
    )

    # Strategy section
    lines.extend(
        [
            "## Performance by Strategy",
            "| Strategy | TP | FP | FN | Precision | Recall | F1 |",
            "|----------|----|----|----|-----------| -------|-----|",
        ]
    )
    for _, row in by_strategy.iterrows():
        lines.append(
            f"| {row['strategy']} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "strategy_f1.png" in charts:
        lines.append("![Strategy F1](assets/strategy_f1.png)")
        lines.append("")

    # Model section
    lines.extend(
        [
            "## Performance by Model",
            "| Model | TP | FP | FN | Precision | Recall | F1 |",
            "|-------|----|----|----|-----------|--------|-----|",
        ]
    )
    for _, row in by_model.iterrows():
        model_short = row["model"].split("/")[-1]
        lines.append(
            f"| {model_short} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "model_comparison.png" in charts:
        lines.append("![Model Comparison](assets/model_comparison.png)")
        lines.append("")

    # Category section (The Money Chart for Thesis!)
    if not by_category.empty:
        lines.extend(
            [
                "## Notional Machine Category Detection (RQ2)",
                "",
                "> This table shows which mental model categories are easier/harder for LLMs to detect.",
                "",
                "| Category | Recall | N |",
                "|----------|--------|---|",
            ]
        )
        by_category = by_category.sort_values("recall")
        for _, row in by_category.iterrows():
            n = row["tp"] + row["fn"]
            lines.append(f"| {row['expected_category']} | {row['recall']:.3f} | {n} |")
        lines.append("")
        if "category_recall.png" in charts:
            lines.append("![Category Recall](assets/category_recall.png)")
            lines.append("")

    # Heatmap
    if "strategy_model_heatmap.png" in charts:
        lines.extend(
            [
                "## Strategy × Model Heatmap",
                "![Heatmap](assets/strategy_model_heatmap.png)",
                "",
            ]
        )

    # Per-misconception
    if not by_misconception.empty:
        lines.extend(
            [
                "## Per-Misconception Detection Rates",
                "| ID | Name | Category | Recall | N |",
                "|----|------|----------|--------|---|",
            ]
        )
        by_misconception = by_misconception.sort_values("recall")
        for _, row in by_misconception.iterrows():
            mid = row["expected_id"]
            gt = gt_map.get(mid, {})
            name = gt.get("name", mid)[:35]
            cat = gt.get("category", "")[:25]
            n = row["tp"] + row["fn"]
            lines.append(f"| {mid} | {name} | {cat} | {row['recall']:.2f} | {n} |")
        lines.append("")
        if "misconception_recall.png" in charts:
            lines.append("![Misconception Recall](assets/misconception_recall.png)")
            lines.append("")

    # Hallucinations
    if "hallucinations.png" in charts:
        lines.extend(
            [
                "## False Positive Analysis",
                "![Hallucinations](assets/hallucinations.png)",
                "",
            ]
        )

    report_path = run_dir / "report.md"
    report_path.write_text("\n".join(lines))
    return report_path


# ---------------------------------------------------------------------------
# Run Management
# ---------------------------------------------------------------------------
def create_run_dir(match_mode: str, manifest: dict[str, Any]) -> Path:
    seed = manifest.get("seed", "unknown")
    timestamp = datetime.now(timezone.utc).strftime("%Y-%m-%d_%H%M%S")
    run_id = f"run_{timestamp}_seed{seed}_{match_mode}"
    run_dir = RUNS_DIR / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    return run_dir


def update_run_index(run_dir: Path, metrics: dict[str, Any], match_mode: str) -> None:
    index_path = RUNS_DIR / "index.json"

    if index_path.exists():
        index = load_json(index_path)
    else:
        index = {"runs": []}

    run_entry = {
        "id": run_dir.name,
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "match_mode": match_mode,
        "metrics": metrics,
    }

    index["runs"].append(run_entry)
    index_path.write_text(json.dumps(index, indent=2))
    console.print(f"[green]Updated:[/green] {index_path}")


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------
@app.command()
def analyze(
    detections_dir: Path = typer.Option(DEFAULT_DETECTIONS_DIR, help="Detections directory"),
    manifest_path: Path = typer.Option(DEFAULT_MANIFEST_PATH, help="Manifest path"),
    groundtruth_path: Path = typer.Option(DEFAULT_GROUNDTRUTH_PATH, help="Groundtruth path"),
    match_mode: str = typer.Option("hybrid", help="Match mode: fuzzy, semantic, hybrid, or all"),
):
    """Run analysis on detections with report generation."""
    console.print("[bold cyan]═══ Analysis v2 ═══[/bold cyan]")
    console.print(f"Match mode: {match_mode}")

    manifest = load_manifest(manifest_path)
    groundtruth = load_groundtruth(groundtruth_path)

    console.print(f"Students: {manifest.get('student_count', 'N/A')}")
    console.print(f"Misconceptions: {len(groundtruth)}")

    df = build_results_df(detections_dir, manifest, groundtruth, match_mode)  # type: ignore

    if df.empty:
        console.print("[red]No results![/red]")
        return

    overall = compute_metrics(df)
    console.print(
        f"\n[bold]Overall:[/bold] P={overall['precision']:.3f} R={overall['recall']:.3f} F1={overall['f1']:.3f}"
    )

    # Quick console tables
    by_strategy = metrics_by_group(df, ["strategy"])
    console.print("\n[bold]By Strategy[/bold]")
    table = Table()
    table.add_column("Strategy")
    table.add_column("F1")
    for _, row in by_strategy.iterrows():
        table.add_row(row["strategy"], f"{row['f1']:.3f}")
    console.print(table)

    # Save everything
    run_dir = create_run_dir(match_mode, manifest)
    df.to_csv(run_dir / "results.csv", index=False)
    (run_dir / "metrics.json").write_text(json.dumps(overall, indent=2))

    report_path = generate_report(df, manifest, groundtruth, run_dir, match_mode)
    console.print(f"[green]Report:[/green] {report_path}")

    update_run_index(run_dir, overall, match_mode)
    console.print("[bold green]✓ Complete[/bold green]")


@app.command()
def list_runs():
    """List all runs in index."""
    index_path = RUNS_DIR / "index.json"
    if not index_path.exists():
        console.print("[yellow]No runs[/yellow]")
        return

    index = load_json(index_path)
    table = Table(title="Runs")
    table.add_column("ID")
    table.add_column("Mode")
    table.add_column("F1")

    for run in index.get("runs", []):
        m = run.get("metrics", {})
        table.add_row(run["id"], run.get("match_mode", "?"), f"{m.get('f1', 0):.3f}")
    console.print(table)


if __name__ == "__main__":
    app()
