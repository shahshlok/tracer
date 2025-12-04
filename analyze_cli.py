"""
Revamped analysis CLI for misconception detection.

- Builds a tidy dataframe of all detections with hybrid matching.
- Computes rich metrics (precision/recall/F1, calibration, agreement, CIs).
- Generates figures (heatmaps, calibration curves, hallucination bars).
- Writes a thesis-grade markdown report that links to generated assets.
"""

from __future__ import annotations

import json
import math
import random
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Iterable, Tuple

import matplotlib

matplotlib.use("Agg")  # Headless rendering
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
import typer
from rich.console import Console
from rich.table import Table

from utils.matching.hybrid import (
    HybridMatchResult,
    hybrid_match_misconception,
    precompute_gt_embeddings_for_hybrid,
)
from utils.matching.classifier import MatchResult

app = typer.Typer(help="Analyze LLM misconception detections against ground truth (revamped)")
console = Console()

DEFAULT_DETECTIONS_DIR = Path("detections")
DEFAULT_MANIFEST_PATH = Path("authentic_seeded/manifest.json")
DEFAULT_GROUNDTRUTH_PATH = Path("data/a2/groundtruth.json")
ASSET_DIR = Path("docs/report_assets")
REPORT_PATH = Path("thesis_report.md")
JSON_EXPORT_PATH = Path("thesis_report.json")


# ---------------------------------------------------------------------------
# Loading helpers
# ---------------------------------------------------------------------------
def load_json(path: Path) -> Any:
    return json.loads(path.read_text())


def load_groundtruth(path: Path) -> list[dict[str, Any]]:
    return load_json(path)


def load_manifest(path: Path) -> dict[str, Any]:
    return load_json(path)


def discover_strategies(detections_dir: Path) -> list[str]:
    return sorted([d.name for d in detections_dir.iterdir() if d.is_dir() and not d.name.startswith("_")])


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
# Core data assembly
# ---------------------------------------------------------------------------
def build_dataframes(
    detections_dir: Path,
    strategies: Iterable[str],
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    use_embeddings: bool = True,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    Return (detections_df, opportunities_df)

    detections_df columns:
      strategy, model, student, question, expected_id, matched_id, result,
      confidence, match_score, match_detail, is_clean, detected_name, detected_topic

    opportunities_df columns:
      strategy, model, student, question, expected_id, topic, success
    """
    gt_map = {m["id"]: m for m in groundtruth}
    gt_embeddings = precompute_gt_embeddings_for_hybrid(groundtruth) if use_embeddings else None

    detection_rows: list[dict[str, Any]] = []
    opportunity_rows: list[dict[str, Any]] = []

    for strategy in strategies:
        strategy_dir = detections_dir / strategy
        if not strategy_dir.exists():
            console.print(f"[yellow]Strategy directory missing: {strategy_dir}[/yellow]")
            continue

        detections = load_detections_for_strategy(strategy_dir)
        for det in detections:
            student = det.get("student", "")
            question = det.get("question", "")
            models = det.get("models", {})
            expected_id, is_clean = get_expected(manifest, student, question)
            expected_topic = gt_map.get(expected_id, {}).get("category", "") if expected_id else ""

            for model, payload in models.items():
                mis_list = payload.get("misconceptions", []) or []
                has_tp = False

                for mis in mis_list:
                    hybrid: HybridMatchResult = hybrid_match_misconception(
                        {**mis, "topic": mis.get("topic", "")},
                        groundtruth,
                        gt_embeddings=gt_embeddings,
                    )

                    if is_clean:
                        result = MatchResult.INTERESTING if hybrid.matched_id else MatchResult.FALSE_POSITIVE
                    elif hybrid.matched_id == expected_id:
                        result = MatchResult.TRUE_POSITIVE
                        has_tp = True
                    else:
                        result = MatchResult.FALSE_POSITIVE

                    detection_rows.append(
                        {
                            "strategy": strategy,
                            "model": model,
                            "student": student,
                            "question": question,
                            "expected_id": expected_id,
                            "matched_id": hybrid.matched_id,
                            "match_score": hybrid.score,
                            "match_detail": hybrid.detail,
                            "result": result.value,
                            "is_clean": is_clean,
                            "confidence": mis.get("confidence"),
                            "detected_name": mis.get("name", ""),
                            "detected_topic": mis.get("topic", ""),
                            "expected_topic": expected_topic,
                        }
                    )

                # Record opportunity and possible FN
                if not is_clean and expected_id:
                    opportunity_rows.append(
                        {
                            "strategy": strategy,
                            "model": model,
                            "student": student,
                            "question": question,
                            "expected_id": expected_id,
                            "topic": expected_topic,
                            "success": has_tp,
                        }
                    )
                    if not has_tp:
                        detection_rows.append(
                            {
                                "strategy": strategy,
                                "model": model,
                                "student": student,
                                "question": question,
                                "expected_id": expected_id,
                                "matched_id": None,
                                "match_score": 0.0,
                                "match_detail": "missed_injection",
                                "result": MatchResult.FALSE_NEGATIVE.value,
                                "is_clean": False,
                                "confidence": None,
                                "detected_name": "",
                                "detected_topic": "",
                                "expected_topic": expected_topic,
                            }
                        )

    detections_df = pd.DataFrame(detection_rows)
    opportunities_df = pd.DataFrame(opportunity_rows)
    return detections_df, opportunities_df


# ---------------------------------------------------------------------------
# Metrics & stats
# ---------------------------------------------------------------------------
def _metric_counts(df: pd.DataFrame) -> tuple[int, int, int]:
    tp = (df["result"] == MatchResult.TRUE_POSITIVE.value).sum()
    fp = (df["result"] == MatchResult.FALSE_POSITIVE.value).sum()
    fn = (df["result"] == MatchResult.FALSE_NEGATIVE.value).sum()
    return int(tp), int(fp), int(fn)


def summarize_metrics(df: pd.DataFrame, group_cols: list[str]) -> pd.DataFrame:
    records = []
    for keys, grp in df.groupby(group_cols):
        tp, fp, fn = _metric_counts(grp)
        precision = tp / (tp + fp) if (tp + fp) else 0.0
        recall = tp / (tp + fn) if (tp + fn) else 0.0
        f1 = 2 * precision * recall / (precision + recall) if (precision + recall) else 0.0
        rec = {col: key for col, key in zip(group_cols, keys if isinstance(keys, tuple) else (keys,))}
        rec.update({"tp": tp, "fp": fp, "fn": fn, "precision": precision, "recall": recall, "f1": f1})
        records.append(rec)
    return pd.DataFrame(records).sort_values(group_cols)


def bootstrap_metrics(
    df: pd.DataFrame,
    group_cols: list[str],
    unit_cols: list[str],
    iters: int = 400,
    seed: int = 1337,
) -> pd.DataFrame:
    rng = random.Random(seed)
    rows = []
    for keys, grp in df.groupby(group_cols):
        units = grp[unit_cols].drop_duplicates()
        if units.empty:
            continue
        metrics = {"precision": [], "recall": [], "f1": []}
        for i in range(iters):
            sample_units = units.sample(frac=1.0, replace=True, random_state=rng.randint(0, 1_000_000))
            sampled = grp.merge(sample_units, on=unit_cols, how="inner")
            tp, fp, fn = _metric_counts(sampled)
            precision = tp / (tp + fp) if (tp + fp) else 0.0
            recall = tp / (tp + fn) if (tp + fn) else 0.0
            f1 = 2 * precision * recall / (precision + recall) if (precision + recall) else 0.0
            metrics["precision"].append(precision)
            metrics["recall"].append(recall)
            metrics["f1"].append(f1)

        rec = {col: key for col, key in zip(group_cols, keys if isinstance(keys, tuple) else (keys,))}
        for m, values in metrics.items():
            rec[f"{m}_lo"] = float(np.percentile(values, 2.5))
            rec[f"{m}_hi"] = float(np.percentile(values, 97.5))
        rows.append(rec)
    return pd.DataFrame(rows)


def expected_calibration_error(df: pd.DataFrame, bins: int = 10) -> float:
    subset = df[df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])]
    subset = subset.dropna(subset=["confidence"])
    if subset.empty:
        return 0.0
    confidences = subset["confidence"].clip(0, 1)
    labels = (subset["result"] == MatchResult.TRUE_POSITIVE.value).astype(float)

    bin_edges = np.linspace(0, 1, bins + 1)
    ece = 0.0
    for i in range(bins):
        mask = (confidences >= bin_edges[i]) & (confidences < bin_edges[i + 1])
        if i == bins - 1:
            mask |= confidences == 1.0  # Include right edge
        if not mask.any():
            continue
        bin_conf = confidences[mask].mean()
        bin_acc = labels[mask].mean()
        ece += (mask.sum() / len(confidences)) * abs(bin_acc - bin_conf)
    return float(ece)


def brier_score(df: pd.DataFrame) -> float:
    subset = df[df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])]
    subset = subset.dropna(subset=["confidence"])
    if subset.empty:
        return 0.0
    confidences = subset["confidence"].clip(0, 1)
    labels = (subset["result"] == MatchResult.TRUE_POSITIVE.value).astype(float)
    return float(np.mean((confidences - labels) ** 2))


def cohen_kappa(success_a: list[bool], success_b: list[bool]) -> float:
    if len(success_a) != len(success_b) or not success_a:
        return 0.0
    a = np.array(success_a, dtype=int)
    b = np.array(success_b, dtype=int)
    agree = (a == b).mean()
    pa = a.mean()
    pb = b.mean()
    pe = pa * pb + (1 - pa) * (1 - pb)
    if pe == 1:
        return 1.0
    return float((agree - pe) / (1 - pe)) if (1 - pe) else 0.0


def mcnemar(success_a: list[bool], success_b: list[bool]) -> tuple[float, float, dict[str, int]]:
    if len(success_a) != len(success_b):
        raise ValueError("success lists must be same length")
    b_only = sum(1 for x, y in zip(success_a, success_b) if (not x) and y)
    a_only = sum(1 for x, y in zip(success_a, success_b) if x and (not y))
    both = sum(1 for x, y in zip(success_a, success_b) if x and y)
    neither = sum(1 for x, y in zip(success_a, success_b) if (not x) and (not y))
    if (a_only + b_only) == 0:
        stat = 0.0
        p = 1.0
    else:
        stat = (abs(a_only - b_only) - 0.5) ** 2 / (a_only + b_only)
        # p-value using chi-square with 1 dof
        p = 2 * (1 - _normal_cdf(math.sqrt(stat)))
    return stat, p, {"both_correct": both, "only_a": a_only, "only_b": b_only, "both_wrong": neither}


def _normal_cdf(x: float) -> float:
    return (1.0 + math.erf(x / math.sqrt(2.0))) / 2.0


# ---------------------------------------------------------------------------
# Visualization helpers
# ---------------------------------------------------------------------------
def ensure_asset_dir() -> Path:
    ASSET_DIR.mkdir(parents=True, exist_ok=True)
    return ASSET_DIR


def plot_topic_heatmap(opportunities: pd.DataFrame, path: Path) -> Path:
    if opportunities.empty:
        return path
    df = opportunities.copy()
    df["strategy_model"] = df["strategy"] + " | " + df["model"].apply(lambda m: m.split("/")[-1])
    pivot = df.pivot_table(index="topic", columns="strategy_model", values="success", aggfunc="mean").fillna(0)
    plt.figure(figsize=(12, max(4, 0.4 * len(pivot))))
    sns.heatmap(pivot, annot=True, fmt=".2f", cmap="YlGnBu", vmin=0, vmax=1)
    plt.title("Recall by Topic (strategy | model)")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_calibration(df: pd.DataFrame, path: Path) -> Path:
    subset = df[df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])]
    subset = subset.dropna(subset=["confidence"])
    if subset.empty:
        return path

    plt.figure(figsize=(10, 6))
    for (strategy, model), grp in subset.groupby(["strategy", "model"]):
        confidences = grp["confidence"].clip(0, 1)
        labels = (grp["result"] == MatchResult.TRUE_POSITIVE.value).astype(float)
        bin_edges = np.linspace(0, 1, 11)
        bin_ids = np.digitize(confidences, bin_edges, right=True) - 1
        points = []
        for b in range(10):
            mask = bin_ids == b
            if mask.any():
                points.append((confidences[mask].mean(), labels[mask].mean()))
        if points:
            xs, ys = zip(*points)
            plt.plot(xs, ys, marker="o", label=f"{strategy}/{model.split('/')[-1]}")
    plt.plot([0, 1], [0, 1], linestyle="--", color="gray", label="Perfect calibration")
    plt.xlabel("Confidence")
    plt.ylabel("Empirical accuracy")
    plt.title("Calibration Curves")
    plt.legend()
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_hallucinations(df: pd.DataFrame, path: Path) -> Path:
    fps = df[df["result"] == MatchResult.FALSE_POSITIVE.value]
    if fps.empty:
        return path
    fps["label"] = fps["detected_name"].fillna("").replace("", "<unknown>")
    top = fps["label"].value_counts().head(10).reset_index()
    top.columns = ["name", "count"]
    plt.figure(figsize=(10, 6))
    sns.barplot(data=top, y="name", x="count", palette="Reds_r")
    plt.title("Top Hallucinated Misconceptions (FP)")
    plt.xlabel("Count")
    plt.ylabel("")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


# ---------------------------------------------------------------------------
# Reporting
# ---------------------------------------------------------------------------
def render_metrics_table(metrics: pd.DataFrame, ci: pd.DataFrame | None) -> str:
    merged = metrics.copy()
    if ci is not None and not ci.empty:
        merged = merged.merge(ci, on=["strategy", "model"], how="left")
    lines = ["| Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (Precision) | CI (Recall) | CI (F1) |"]
    lines.append("|----------|-------|----|----|----|-----------|--------|----|----------------|-------------|---------|")
    for _, row in merged.iterrows():
        prec_ci = f"{row['precision_lo']:.2f}–{row['precision_hi']:.2f}" if "precision_lo" in row else "-"
        rec_ci = f"{row['recall_lo']:.2f}–{row['recall_hi']:.2f}" if "recall_lo" in row else "-"
        f1_ci = f"{row['f1_lo']:.2f}–{row['f1_hi']:.2f}" if "f1_lo" in row else "-"
        lines.append(
            f"| {row['strategy']} | {row['model'].split('/')[-1]} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
            f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} | {prec_ci} | {rec_ci} | {f1_ci} |"
        )
    return "\n".join(lines)


def render_topic_table(opportunities: pd.DataFrame) -> str:
    if opportunities.empty:
        return "_No opportunities to report_"
    pivot = (
        opportunities.groupby(["topic"])
        .agg(recall=("success", "mean"), n=("success", "count"))
        .reset_index()
        .sort_values("recall")
    )
    lines = ["| Topic | Recall | N |", "|-------|--------|---|"]
    for _, row in pivot.iterrows():
        lines.append(f"| {row['topic']} | {row['recall']:.3f} | {int(row['n'])} |")
    return "\n".join(lines)


def render_hallucination_snippets(df: pd.DataFrame, limit: int = 5) -> str:
    fps = df[df["result"] == MatchResult.FALSE_POSITIVE.value]
    if fps.empty:
        return "_No hallucinations detected_"
    counts = fps["detected_name"].fillna("").replace("", "<unknown>").value_counts().head(limit)
    lines = []
    for name, count in counts.items():
        lines.append(f"- **{name}** ({count} times)")
    return "\n".join(lines)


def generate_report(
    metrics: pd.DataFrame,
    ci: pd.DataFrame,
    opportunities: pd.DataFrame,
    detections: pd.DataFrame,
    asset_paths: dict[str, Path],
) -> str:
    ts = datetime.now(timezone.utc).isoformat()

    report = [
        "# LLM Misconception Detection: Revamped Analysis",
        f"_Generated: {ts}_",
        "",
        "## Executive Highlights",
        "- Hybrid matcher (fuzzy + semantic + topic prior) applied across all strategies/models.",
        "- Bootstrap CIs included for statistical rigor.",
        "",
        "## Strategy × Model Performance",
        render_metrics_table(metrics, ci),
        "",
        "## Topic Difficulty (Recall)",
        render_topic_table(opportunities),
        "",
        "## Hallucination Analysis",
        f"- Hallucination bar chart: see `{asset_paths.get('hallucinations', '')}`",
        "",
        render_hallucination_snippets(detections),
        "",
        "## Topic Heatmap",
        f"![Topic Heatmap]({asset_paths.get('heatmap', '')})" if asset_paths.get("heatmap") else "_No heatmap generated_",
        "",
        "## Methods",
        "- Data: 60 students × 4 questions (seeded/clean) with manifest-driven ground truth.",
        "- Detection: GPT-5.1 and Gemini 2.5 Flash across strategies (baseline, minimal, rubric_only, socratic).",
        "- Matching: Hybrid fusion of fuzzy similarity, semantic embeddings (OpenAI/OpenRouter optional), and topic priors.",
        "- Metrics: Precision/Recall/F1 with bootstrap CIs; agreement via κ; significance via McNemar where applicable.",
    ]

    # Agreement & significance
    agreements = []
    for strategy, grp in opportunities.groupby("strategy"):
        models = sorted(grp["model"].unique())
        if len(models) < 2:
            continue
        base = models[0]
        other = models[1]
        base_res = grp[grp["model"] == base]["success"].tolist()
        other_res = grp[grp["model"] == other]["success"].tolist()
        kappa = cohen_kappa(base_res, other_res)
        stat, p, table = mcnemar(base_res, other_res)
        agreements.append(
            f"- {strategy}: κ={kappa:.3f}, McNemar p={p:.4f} (stat={stat:.3f}) | table={table}"
        )
    if agreements:
        report.extend(["", "## Agreement & Significance", *agreements])

    return "\n".join(report)


# ---------------------------------------------------------------------------
# Display helpers
# ---------------------------------------------------------------------------
def display_summary(metrics: pd.DataFrame):
    if metrics.empty:
        console.print("[red]No metrics to display[/red]")
        return
    table = Table(title="Strategy × Model Performance", show_header=True, header_style="bold")
    for col in ["Strategy", "Model", "TP", "FP", "FN", "Precision", "Recall", "F1"]:
        table.add_column(col)
    for _, row in metrics.iterrows():
        table.add_row(
            row["strategy"],
            row["model"].split("/")[-1],
            str(int(row["tp"])),
            str(int(row["fp"])),
            str(int(row["fn"])),
            f"{row['precision']:.3f}",
            f"{row['recall']:.3f}",
            f"{row['f1']:.3f}",
        )
    console.print(table)


# ---------------------------------------------------------------------------
# CLI entrypoint
# ---------------------------------------------------------------------------
@app.command()
def main(
    detections_dir: Path = typer.Option(DEFAULT_DETECTIONS_DIR, help="Detections root", show_default=True),
    manifest_path: Path = typer.Option(DEFAULT_MANIFEST_PATH, help="Manifest path", show_default=True),
    groundtruth_path: Path = typer.Option(DEFAULT_GROUNDTRUTH_PATH, help="Ground truth path", show_default=True),
    quick: bool = typer.Option(False, help="Quick mode (reuse cached assets if available)"),
):
    """
    Interactive-ish analysis: discovers strategies, runs hybrid matching, writes report + assets.
    """
    console.rule("[bold green]Revamped Analysis[/bold green]")

    strategies = discover_strategies(detections_dir)
    if not strategies:
        console.print("[red]No strategies found under detections/[/red]")
        raise typer.Exit(code=1)

    console.print(f"[cyan]Strategies discovered:[/cyan] {', '.join(strategies)}")
    groundtruth = load_groundtruth(groundtruth_path)
    manifest = load_manifest(manifest_path)

    detections_df, opportunities_df = build_dataframes(
        detections_dir=detections_dir,
        strategies=strategies,
        manifest=manifest,
        groundtruth=groundtruth,
        use_embeddings=not quick,
    )

    metrics = summarize_metrics(detections_df, ["strategy", "model"])
    ci = bootstrap_metrics(
        detections_df,
        group_cols=["strategy", "model"],
        unit_cols=["student", "question"],
        iters=150 if quick else 400,
    )

    display_summary(metrics)

    ensure_asset_dir()
    asset_paths = {
        "heatmap": ASSET_DIR / "topic_heatmap.png",
        "hallucinations": ASSET_DIR / "hallucinations.png",
    }
    plot_topic_heatmap(opportunities_df, asset_paths["heatmap"])
    plot_hallucinations(detections_df, asset_paths["hallucinations"])

    report_text = generate_report(metrics, ci, opportunities_df, detections_df, asset_paths)
    REPORT_PATH.write_text(report_text)
    JSON_EXPORT_PATH.write_text(
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
    console.print(f"[green]Report saved to {REPORT_PATH}[/green]")
    console.print(f"[dim]Assets: {asset_paths}[/dim]")


if __name__ == "__main__":
    app()
