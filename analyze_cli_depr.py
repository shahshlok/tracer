"""
Revamped analysis CLI for misconception detection.

- Builds a tidy dataframe of all detections with configurable matching modes.
- Supports matcher ablation: fuzzy_only, semantic_only, hybrid, or all.
- Computes rich metrics (precision/recall/F1, agreement, CIs).
- Generates figures (heatmaps, scatter plots, bar charts).
- Writes a thesis-grade markdown report that links to generated assets.
"""

from __future__ import annotations

import json
import math
import random
from collections.abc import Iterable
from dataclasses import dataclass
from datetime import datetime, timezone
from enum import Enum
from itertools import combinations
from pathlib import Path
from typing import Any

import matplotlib

matplotlib.use("Agg")  # Headless rendering
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
import typer
from rich.console import Console
from rich.table import Table

from llm_miscons_cli import MODEL_SHORT_NAMES
from utils.matching.classifier import MatchResult
from utils.matching.fuzzy import fuzzy_match_misconception
from utils.matching.hybrid import (
    HybridMatchResult,
    hybrid_match_misconception,
    precompute_gt_embeddings_for_hybrid,
)
from utils.matching.semantic import semantic_match_misconception

# ---------------------------------------------------------------------------
# Model Color Palette (supports up to 8 models dynamically)
# ---------------------------------------------------------------------------
MODEL_COLOR_PALETTE = [
    "#2ecc71",  # Green
    "#3498db",  # Blue
    "#e74c3c",  # Red
    "#9b59b6",  # Purple
    "#f39c12",  # Orange
    "#1abc9c",  # Teal
    "#e91e63",  # Pink
    "#795548",  # Brown
]


def get_model_colors(models: list[str]) -> dict[str, str]:
    """
    Generate a color mapping for a list of model names.
    Handles both full model names (openai/gpt-5.1) and short names (gpt-5.1).
    """
    colors = {}
    for i, model in enumerate(models):
        color = MODEL_COLOR_PALETTE[i % len(MODEL_COLOR_PALETTE)]
        colors[model] = color
        # Also add short name variant
        short_name = model.split("/")[-1]
        colors[short_name] = color
    return colors


# ---------------------------------------------------------------------------
# Match Mode Configuration
# ---------------------------------------------------------------------------
class MatchMode(str, Enum):
    FUZZY_ONLY = "fuzzy_only"
    SEMANTIC_ONLY = "semantic_only"
    HYBRID = "hybrid"
    ALL = "all"


@dataclass
class MatchResult_:
    """Unified match result across all matchers."""

    matched_id: str | None
    score: float
    detail: str


def adapt_detection_fields(detection: dict[str, Any]) -> dict[str, Any]:
    """
    Adapt new NotionalMisconception fields to legacy matcher field names.

    New model fields -> Legacy matcher fields:
    - inferred_category_name -> name
    - conceptual_gap -> description
    - student_thought_process -> student_belief
    - error_manifestation -> (not used, but preserved)

    This allows the existing fuzzy/semantic/hybrid matchers to work
    without modification.
    """
    return {
        "name": detection.get("inferred_category_name", detection.get("name", "")),
        "description": detection.get("conceptual_gap", detection.get("description", "")),
        "student_belief": detection.get(
            "student_thought_process", detection.get("student_belief", "")
        ),
        "topic": detection.get("topic", ""),  # Not in new model, but needed for hybrid
        "confidence": detection.get("confidence"),
        "evidence": detection.get("evidence", []),
        "error_manifestation": detection.get("error_manifestation", ""),
    }


def dispatch_matcher(
    detection: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    match_mode: MatchMode,
    gt_embeddings: dict[str, list[float]] | None = None,
) -> MatchResult_:
    """
    Dispatch to the appropriate matcher based on match_mode.
    Returns a unified MatchResult_ regardless of which matcher is used.

    Automatically adapts NotionalMisconception fields to legacy format.
    """
    # Adapt new field names to legacy format for matchers
    adapted = adapt_detection_fields(detection)
    name = adapted["name"]
    description = adapted["description"]
    student_belief = adapted["student_belief"]

    if match_mode == MatchMode.FUZZY_ONLY:
        matched_id, score, method = fuzzy_match_misconception(
            name, description, groundtruth, threshold=0.5
        )
        return MatchResult_(matched_id, score, f"fuzzy:{method}")

    elif match_mode == MatchMode.SEMANTIC_ONLY:
        matched_id, score, method = semantic_match_misconception(
            name, description, student_belief, groundtruth, threshold=0.5
        )
        return MatchResult_(matched_id, score, f"semantic:{method}")

    elif match_mode == MatchMode.HYBRID:
        result: HybridMatchResult = hybrid_match_misconception(
            adapted,
            groundtruth,
            gt_embeddings=gt_embeddings,
        )
        return MatchResult_(result.matched_id, result.score, result.detail)

    else:
        raise ValueError(f"Unknown match mode: {match_mode}")


app = typer.Typer(
    help="Analyze LLM misconception detections against ground truth (revamped)",
    invoke_without_command=True,
)
console = Console()


@app.callback()
def callback(ctx: typer.Context):
    """
    Main entry point. Use 'analyze' subcommand to run analysis,
    or 'list-runs' to view saved runs.
    """
    if ctx.invoked_subcommand is None:
        console.print("[yellow]Usage: analyze <command> [options][/yellow]")
        console.print("Commands:")
        console.print("  analyze    Run misconception detection analysis")
        console.print("  list-runs  List all saved runs")
        console.print("\nRun 'analyze --help' for more info.")


DEFAULT_DETECTIONS_DIR = Path("detections/a3")
DEFAULT_MANIFEST_PATH = Path("authentic_seeded/a3/manifest.json")
DEFAULT_GROUNDTRUTH_PATH = Path("data/a3/groundtruth.json")
ASSET_DIR = Path("docs/report_assets")
REPORT_PATH = Path("thesis_report.md")
JSON_EXPORT_PATH = Path("thesis_report.json")
RUNS_DIR = Path("runs/a3")
RUNS_INDEX_PATH = RUNS_DIR / "index.json"


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
    return sorted(
        [d.name for d in detections_dir.iterdir() if d.is_dir() and not d.name.startswith("_")]
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
# Core data assembly
# ---------------------------------------------------------------------------
def build_dataframes(
    detections_dir: Path,
    strategies: Iterable[str],
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    match_mode: MatchMode = MatchMode.HYBRID,
    use_embeddings: bool = True,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    Return (detections_df, opportunities_df)

    detections_df columns:
      match_mode, strategy, model, student, question, expected_id, matched_id, result,
      confidence, match_score, match_detail, is_clean, detected_name, detected_topic

    opportunities_df columns:
      match_mode, strategy, model, student, question, expected_id, topic, success

    If match_mode is ALL, runs all three matchers and includes match_mode column.
    """
    gt_map = {m["id"]: m for m in groundtruth}
    gt_embeddings = precompute_gt_embeddings_for_hybrid(groundtruth) if use_embeddings else None

    # Determine which modes to run
    if match_mode == MatchMode.ALL:
        modes_to_run = [MatchMode.FUZZY_ONLY, MatchMode.SEMANTIC_ONLY, MatchMode.HYBRID]
    else:
        modes_to_run = [match_mode]

    detection_rows: list[dict[str, Any]] = []
    opportunity_rows: list[dict[str, Any]] = []

    for current_mode in modes_to_run:
        console.print(f"[cyan]Running matcher: {current_mode.value}[/cyan]")

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
                expected_topic = (
                    gt_map.get(expected_id, {}).get("category", "") if expected_id else ""
                )

                for model, payload in models.items():
                    mis_list = payload.get("misconceptions", []) or []
                    has_tp = False

                    for mis in mis_list:
                        match_result = dispatch_matcher(
                            mis, groundtruth, current_mode, gt_embeddings
                        )

                        if is_clean:
                            result = (
                                MatchResult.INTERESTING
                                if match_result.matched_id
                                else MatchResult.FALSE_POSITIVE
                            )
                        elif match_result.matched_id == expected_id:
                            result = MatchResult.TRUE_POSITIVE
                            has_tp = True
                        else:
                            result = MatchResult.FALSE_POSITIVE

                        detection_rows.append(
                            {
                                "match_mode": current_mode.value,
                                "strategy": strategy,
                                "model": model,
                                "student": student,
                                "question": question,
                                "expected_id": expected_id,
                                "matched_id": match_result.matched_id,
                                "match_score": match_result.score,
                                "match_detail": match_result.detail,
                                "result": result.value,
                                "is_clean": is_clean,
                                "confidence": mis.get("confidence"),
                                "detected_name": mis.get(
                                    "inferred_category_name", mis.get("name", "")
                                ),
                                "detected_topic": mis.get("topic", ""),
                                "expected_topic": expected_topic,
                            }
                        )

                    # Record opportunity and possible FN
                    if not is_clean and expected_id:
                        opportunity_rows.append(
                            {
                                "match_mode": current_mode.value,
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
                                    "match_mode": current_mode.value,
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
        rec = {
            col: key for col, key in zip(group_cols, keys if isinstance(keys, tuple) else (keys,))
        }
        rec.update(
            {"tp": tp, "fp": fp, "fn": fn, "precision": precision, "recall": recall, "f1": f1}
        )
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
            sample_units = units.sample(
                frac=1.0, replace=True, random_state=rng.randint(0, 1_000_000)
            )
            sampled = grp.merge(sample_units, on=unit_cols, how="inner")
            tp, fp, fn = _metric_counts(sampled)
            precision = tp / (tp + fp) if (tp + fp) else 0.0
            recall = tp / (tp + fn) if (tp + fn) else 0.0
            f1 = 2 * precision * recall / (precision + recall) if (precision + recall) else 0.0
            metrics["precision"].append(precision)
            metrics["recall"].append(recall)
            metrics["f1"].append(f1)

        rec = {
            col: key for col, key in zip(group_cols, keys if isinstance(keys, tuple) else (keys,))
        }
        for m, values in metrics.items():
            rec[f"{m}_lo"] = float(np.percentile(values, 2.5))
            rec[f"{m}_hi"] = float(np.percentile(values, 97.5))
        rows.append(rec)
    return pd.DataFrame(rows)


def expected_calibration_error(df: pd.DataFrame, bins: int = 10) -> float:
    subset = df[
        df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])
    ]
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
    subset = df[
        df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])
    ]
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
    return (
        stat,
        p,
        {"both_correct": both, "only_a": a_only, "only_b": b_only, "both_wrong": neither},
    )


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
    pivot = df.pivot_table(
        index="topic", columns="strategy_model", values="success", aggfunc="mean"
    ).fillna(0)
    # Increase figure width and height for better readability
    plt.figure(figsize=(16, max(6, 0.6 * len(pivot))))
    ax = sns.heatmap(pivot, annot=True, fmt=".2f", cmap="YlGnBu", vmin=0, vmax=1)
    plt.title("Recall by Topic (strategy | model)", fontsize=14, fontweight="bold")
    # Rotate x-axis labels 45 degrees for readability
    plt.xticks(rotation=45, ha="right", fontsize=9)
    # Ensure full y-axis label visibility
    plt.yticks(fontsize=10)
    ax.set_ylabel("Notional Machine Category", fontsize=11)
    ax.set_xlabel("Strategy | Model", fontsize=11)
    plt.tight_layout()
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()
    return path


def plot_calibration(df: pd.DataFrame, path: Path) -> Path:
    subset = df[
        df["result"].isin([MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value])
    ]
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
    fps = df[df["result"] == MatchResult.FALSE_POSITIVE.value].copy()
    if fps.empty:
        return path
    fps["label"] = fps["detected_name"].fillna("").replace("", "<unknown>")
    top = fps["label"].value_counts().head(10).reset_index()
    top.columns = ["name", "count"]
    plt.figure(figsize=(10, 6))
    # Use categorical color palette (deep) instead of all-red shades
    sns.barplot(data=top, y="name", x="count", palette="deep")
    plt.title("Top Hallucinated Misconceptions (FP)", fontsize=13, fontweight="bold")
    plt.xlabel("Count")
    plt.ylabel("Misconception")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_strategy_f1_comparison(metrics: pd.DataFrame, path: Path) -> Path:
    """Grouped bar chart: F1 by strategy, colored by model."""
    if metrics.empty:
        return path
    df = metrics.copy()
    df["model_short"] = df["model"].apply(lambda m: m.split("/")[-1])

    plt.figure(figsize=(10, 6))
    strategies = df["strategy"].unique()
    models = list(df["model_short"].unique())
    x = np.arange(len(strategies))

    # Dynamic width based on number of models
    n_models = len(models)
    total_width = 0.8  # Total width for all bars in a group
    width = total_width / n_models

    # Dynamic colors based on models
    model_colors = get_model_colors(models)

    for i, model in enumerate(models):
        model_data = df[df["model_short"] == model].set_index("strategy").reindex(strategies)
        offset = (i - (n_models - 1) / 2) * width  # Center bars around x tick
        bars = plt.bar(x + offset, model_data["f1"], width, label=model, color=model_colors[model])
        # Add value labels on bars
        for bar, val in zip(bars, model_data["f1"]):
            if not np.isnan(val):
                plt.text(
                    bar.get_x() + bar.get_width() / 2,
                    bar.get_height() + 0.01,
                    f"{val:.2f}",
                    ha="center",
                    va="bottom",
                    fontsize=9,
                )

    plt.xlabel("Strategy")
    plt.ylabel("F1 Score")
    plt.title("F1 Score by Strategy and Model")
    plt.xticks(x, strategies)
    plt.ylim(0, 1)
    plt.legend(title="Model")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_precision_recall_scatter(metrics: pd.DataFrame, path: Path) -> Path:
    """Scatter plot showing precision vs recall for each strategy×model."""
    if metrics.empty:
        return path
    df = metrics.copy()
    df["model_short"] = df["model"].apply(lambda m: m.split("/")[-1])

    plt.figure(figsize=(8, 8))

    # Color by model, marker by strategy
    models = list(df["model_short"].unique())
    strategies = df["strategy"].unique()

    # Dynamic colors based on models present in data
    model_colors = get_model_colors(models)
    markers = {"baseline": "o", "taxonomy": "s", "cot": "^", "socratic": "D"}

    for _, row in df.iterrows():
        model = row["model_short"]
        strategy = row["strategy"]
        plt.scatter(
            row["recall"],
            row["precision"],
            c=model_colors.get(model, "#999"),
            marker=markers.get(strategy, "o"),
            s=150,
            alpha=0.8,
            label=f"{strategy} / {model}",
        )

    # Add diagonal lines for F1 iso-curves
    for f1 in [0.5, 0.6, 0.7, 0.8]:
        p = np.linspace(0.01, 1, 100)
        r = (f1 * p) / (2 * p - f1)
        valid = (r > 0) & (r <= 1)
        plt.plot(r[valid], p[valid], "--", color="gray", alpha=0.3, linewidth=1)
        # Label the iso-curve
        idx = np.argmin(np.abs(r - 0.9))
        if valid[idx]:
            plt.text(r[idx], p[idx], f"F1={f1}", fontsize=8, color="gray")

    plt.xlabel("Recall")
    plt.ylabel("Precision")
    plt.title("Precision-Recall Tradeoff by Strategy and Model")
    plt.xlim(0.4, 1.0)
    plt.ylim(0.4, 0.85)
    plt.legend(loc="lower left", fontsize=8)
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_topic_recall_bars(opportunities: pd.DataFrame, path: Path) -> Path:
    """Horizontal bar chart showing recall by topic, sorted by difficulty."""
    if opportunities.empty:
        return path

    topic_stats = (
        opportunities.groupby("topic")
        .agg(recall=("success", "mean"), n=("success", "count"))
        .reset_index()
        .sort_values("recall")
    )

    plt.figure(figsize=(10, 6))
    colors = plt.cm.RdYlGn(topic_stats["recall"])  # Red=low, Green=high
    bars = plt.barh(topic_stats["topic"], topic_stats["recall"], color=colors)

    # Add count labels
    for bar, n in zip(bars, topic_stats["n"]):
        plt.text(
            bar.get_width() + 0.02,
            bar.get_y() + bar.get_height() / 2,
            f"n={int(n)}",
            va="center",
            fontsize=9,
            color="gray",
        )

    plt.xlabel("Recall")
    plt.ylabel("Topic")
    plt.title("Detection Recall by Topic (sorted by difficulty)")
    plt.xlim(0, 1.15)
    plt.axvline(x=0.5, color="gray", linestyle="--", alpha=0.5, label="50% threshold")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_per_misconception_recall(
    opportunities: pd.DataFrame, groundtruth: list[dict[str, Any]], path: Path
) -> Path:
    """Horizontal bar chart showing recall by individual misconception ID."""
    if opportunities.empty:
        return path

    gt_map = {m["id"]: m for m in groundtruth}

    # Group by expected_id and calculate recall
    mis_stats = (
        opportunities.groupby("expected_id")
        .agg(recall=("success", "mean"), n=("success", "count"))
        .reset_index()
        .sort_values("recall")
    )

    # Add misconception names
    mis_stats["name"] = mis_stats["expected_id"].apply(
        lambda x: gt_map.get(x, {}).get("name", x)[:25] if x else "Unknown"
    )
    mis_stats["label"] = mis_stats["expected_id"] + "\n" + mis_stats["name"]

    plt.figure(figsize=(12, max(6, 0.5 * len(mis_stats))))
    colors = plt.cm.RdYlGn(mis_stats["recall"])  # Red=low, Green=high
    bars = plt.barh(mis_stats["label"], mis_stats["recall"], color=colors)

    # Add count and recall labels
    for bar, (_, row) in zip(bars, mis_stats.iterrows()):
        plt.text(
            bar.get_width() + 0.02,
            bar.get_y() + bar.get_height() / 2,
            f"n={int(row['n'])} ({row['recall']:.0%})",
            va="center",
            fontsize=9,
            color="gray",
        )

    plt.xlabel("Recall")
    plt.ylabel("Misconception")
    plt.title("Detection Recall by Misconception ID (sorted by difficulty)")
    plt.xlim(0, 1.25)
    plt.axvline(x=0.5, color="gray", linestyle="--", alpha=0.5)
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_model_comparison(metrics: pd.DataFrame, path: Path) -> Path:
    """Side-by-side comparison of all models on P/R/F1 (averaged across strategies)."""
    if metrics.empty:
        return path

    df = metrics.copy()
    df["model_short"] = df["model"].apply(lambda m: m.split("/")[-1])

    # Average across strategies
    model_avg = df.groupby("model_short")[["precision", "recall", "f1"]].mean().reset_index()
    models = list(model_avg["model_short"])
    n_models = len(models)

    plt.figure(figsize=(max(8, 2 * n_models), 6))
    x = np.arange(3)  # precision, recall, f1

    # Dynamic width based on number of models
    total_width = 0.8
    width = total_width / n_models

    # Dynamic colors
    model_colors = get_model_colors(models)

    for i, (_, row) in enumerate(model_avg.iterrows()):
        values = [row["precision"], row["recall"], row["f1"]]
        offset = (i - (n_models - 1) / 2) * width
        bars = plt.bar(
            x + offset,
            values,
            width,
            label=row["model_short"],
            color=model_colors[row["model_short"]],
        )
        for bar, val in zip(bars, values):
            plt.text(
                bar.get_x() + bar.get_width() / 2,
                bar.get_height() + 0.01,
                f"{val:.2f}",
                ha="center",
                va="bottom",
                fontsize=10,
                fontweight="bold",
            )

    plt.ylabel("Score")
    plt.title("Model Comparison (averaged across strategies)")
    plt.xticks(x, ["Precision", "Recall", "F1"])
    plt.ylim(0, 0.9)
    plt.legend(title="Model")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_matcher_ablation(metrics: pd.DataFrame, path: Path) -> Path:
    """
    Create a grouped bar chart comparing matchers (fuzzy_only, semantic_only, hybrid).
    Shows F1 averaged across strategies and models.
    """
    if metrics.empty or "match_mode" not in metrics.columns:
        return path

    # Average F1 by match_mode
    matcher_avg = metrics.groupby("match_mode")[["precision", "recall", "f1"]].mean().reset_index()

    plt.figure(figsize=(10, 6))
    x = np.arange(len(matcher_avg))
    width = 0.25

    # Consistent colors for each metric across all matchers
    metric_colors = {"precision": "#e74c3c", "recall": "#3498db", "f1": "#2ecc71"}

    # Plot precision, recall, F1 side by side for each matcher
    for i, metric in enumerate(["precision", "recall", "f1"]):
        values = matcher_avg[metric].values
        bars = plt.bar(
            x + i * width,
            values,
            width,
            label=metric.capitalize(),
            color=metric_colors[metric],
            alpha=0.85,
        )
        for bar, val in zip(bars, values):
            plt.text(
                bar.get_x() + bar.get_width() / 2,
                bar.get_height() + 0.01,
                f"{val:.2f}",
                ha="center",
                va="bottom",
                fontsize=9,
            )

    plt.xlabel("Matcher")
    plt.ylabel("Score")
    plt.title("Matcher Ablation: Fuzzy vs Semantic vs Hybrid")
    plt.xticks(x + width, matcher_avg["match_mode"])
    plt.ylim(0, 1.0)
    plt.legend()
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_matcher_pr_scatter(metrics: pd.DataFrame, path: Path) -> Path:
    """
    Precision-Recall scatter colored by matcher.
    Each point is a (strategy, model) combination.
    """
    if metrics.empty or "match_mode" not in metrics.columns:
        return path

    plt.figure(figsize=(10, 8))

    colors = {"fuzzy_only": "#e74c3c", "semantic_only": "#3498db", "hybrid": "#2ecc71"}
    markers = {"fuzzy_only": "o", "semantic_only": "s", "hybrid": "^"}

    # Create main axes and inset axes for fuzzy zoom
    fig, ax_main = plt.subplots(figsize=(12, 8))

    # Main plot (semantic + hybrid focus)
    for match_mode in metrics["match_mode"].unique():
        subset = metrics[metrics["match_mode"] == match_mode]
        ax_main.scatter(
            subset["recall"],
            subset["precision"],
            c=colors.get(match_mode, "#999"),
            marker=markers.get(match_mode, "o"),
            s=100,
            alpha=0.7,
            label=match_mode,
        )

    # Add F1 iso-curves
    for f1 in [0.4, 0.5, 0.6, 0.7]:
        p = np.linspace(0.01, 1, 100)
        r = (f1 * p) / (2 * p - f1)
        valid = (r > 0) & (r <= 1)
        ax_main.plot(r[valid], p[valid], "--", color="gray", alpha=0.3, linewidth=1)

    ax_main.set_xlabel("Recall", fontsize=12)
    ax_main.set_ylabel("Precision", fontsize=12)
    ax_main.set_title("Precision-Recall by Matcher (each point = strategy × model)", fontsize=14)
    ax_main.set_xlim(0.0, 1.0)
    ax_main.set_ylim(0.0, 0.85)
    ax_main.legend(title="Matcher", loc="upper right")
    ax_main.grid(True, alpha=0.3)

    # Inset axes for fuzzy zoom (bottom-left cluster)
    ax_inset = fig.add_axes([0.15, 0.55, 0.25, 0.3])  # [left, bottom, width, height]
    fuzzy_data = metrics[metrics["match_mode"] == "fuzzy_only"]
    if not fuzzy_data.empty:
        ax_inset.scatter(
            fuzzy_data["recall"],
            fuzzy_data["precision"],
            c="#e74c3c",
            marker="o",
            s=60,
            alpha=0.8,
        )
        ax_inset.set_xlim(0, 0.25)
        ax_inset.set_ylim(0, 0.15)
        ax_inset.set_xlabel("Recall", fontsize=9)
        ax_inset.set_ylabel("Precision", fontsize=9)
        ax_inset.set_title("Fuzzy Zoom", fontsize=10)
        ax_inset.grid(True, alpha=0.3)
        ax_inset.patch.set_edgecolor("black")
        ax_inset.patch.set_linewidth(2)

    plt.tight_layout()
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()
    return path


def plot_matcher_strategy_grid(metrics: pd.DataFrame, path: Path) -> Path:
    """Grouped bar chart: F1 by strategy, grouped by matcher."""
    if "match_mode" not in metrics.columns:
        return path

    # Aggregate across models (average GPT + Gemini)
    agg = metrics.groupby(["match_mode", "strategy"]).agg({"f1": "mean"}).reset_index()

    fig, ax = plt.subplots(figsize=(10, 6))

    strategies = ["baseline", "taxonomy", "cot", "socratic"]
    matchers = ["fuzzy_only", "semantic_only", "hybrid"]
    x = np.arange(len(strategies))
    width = 0.25

    colors = {"fuzzy_only": "#e74c3c", "semantic_only": "#3498db", "hybrid": "#2ecc71"}

    for i, matcher in enumerate(matchers):
        data = agg[agg["match_mode"] == matcher]
        vals = [
            data[data["strategy"] == s]["f1"].values[0]
            if len(data[data["strategy"] == s]) > 0
            else 0
            for s in strategies
        ]
        ax.bar(x + i * width, vals, width, label=matcher, color=colors[matcher])

    ax.set_ylabel("F1 Score")
    ax.set_xlabel("Prompt Strategy")
    ax.set_xticks(x + width)
    ax.set_xticklabels(strategies)
    ax.legend(title="Matcher")
    ax.set_ylim(0, 0.8)

    plt.title("F1 by Matcher × Strategy (averaged across models)")
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_mcnemar_bar_chart(agreement_data: list[dict], path: Path) -> Path:
    """
    Bar chart showing McNemar test p-values for model pairs.
    Significant pairs (p < 0.05) are highlighted in red.
    """
    if not agreement_data:
        return path

    # Filter to unique model pairs (aggregate across strategies)
    from collections import defaultdict

    pair_pvals = defaultdict(list)
    for row in agreement_data:
        key = (row["model_a"], row["model_b"])
        pair_pvals[key].append(row["mcnemar_p"])

    # Average p-value per pair
    pairs = []
    for (model_a, model_b), p_list in pair_pvals.items():
        avg_p = sum(p_list) / len(p_list)
        pairs.append(
            {
                "label": f"{model_a[:8]}...\nvs\n{model_b[:8]}...",
                "p_value": avg_p,
                "significant": avg_p < 0.05,
            }
        )

    # Sort by p-value (lowest first = most significant)
    pairs.sort(key=lambda x: x["p_value"])

    # Take top 15 for readability
    pairs = pairs[:15]

    if not pairs:
        return path

    fig, ax = plt.subplots(figsize=(14, 7))
    x = np.arange(len(pairs))
    colors = ["#e74c3c" if p["significant"] else "#95a5a6" for p in pairs]

    bars = ax.bar(x, [p["p_value"] for p in pairs], color=colors, alpha=0.8, edgecolor="black")

    # Significance threshold line
    ax.axhline(y=0.05, color="red", linestyle="--", linewidth=2, label="α = 0.05")

    ax.set_xlabel("Model Pair", fontsize=11)
    ax.set_ylabel("p-value (avg across strategies)", fontsize=11)
    ax.set_title("McNemar's Test: Model Pair Significance", fontsize=14, fontweight="bold")
    ax.set_xticks(x)
    ax.set_xticklabels([p["label"] for p in pairs], rotation=45, ha="right", fontsize=8)
    ax.legend(loc="upper right")
    ax.set_ylim(0, min(1.0, max(p["p_value"] for p in pairs) * 1.2))
    ax.grid(axis="y", alpha=0.3)

    # Add count annotations
    sig_count = sum(1 for p in pairs if p["significant"])
    ax.text(
        0.02,
        0.98,
        f"Significant: {sig_count}/{len(pairs)}",
        transform=ax.transAxes,
        fontsize=10,
        va="top",
        bbox=dict(boxstyle="round", facecolor="wheat", alpha=0.5),
    )

    plt.tight_layout()
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()
    return path


def compute_model_rankings(metrics: pd.DataFrame) -> pd.DataFrame:
    """
    Compute model rankings based on average F1 score across ALL matchers.
    Returns DataFrame with Rank, Model, Avg F1, Best Config, Worst Config.
    """
    if metrics.empty:
        return pd.DataFrame()

    data = metrics.copy()

    # Get model short names
    data["model_short"] = data["model"].apply(lambda m: m.split("/")[-1])

    # Create config column combining match_mode + strategy
    if "match_mode" in data.columns:
        data["config"] = data["match_mode"] + " / " + data["strategy"]
    else:
        data["config"] = data["strategy"]

    # Compute average F1 per model
    model_stats = []
    for model in data["model_short"].unique():
        model_data = data[data["model_short"] == model]
        avg_f1 = model_data["f1"].mean()
        best_idx = model_data["f1"].idxmax()
        worst_idx = model_data["f1"].idxmin()
        model_stats.append(
            {
                "model_short": model,
                "avg_f1": avg_f1,
                "best_config": model_data.loc[best_idx, "config"],
                "best_f1": model_data.loc[best_idx, "f1"],
                "worst_config": model_data.loc[worst_idx, "config"],
                "worst_f1": model_data.loc[worst_idx, "f1"],
            }
        )

    rankings = pd.DataFrame(model_stats)
    rankings = rankings.sort_values("avg_f1", ascending=False).reset_index(drop=True)
    rankings["rank"] = range(1, len(rankings) + 1)

    return rankings


def render_model_leaderboard(rankings: pd.DataFrame) -> str:
    """Render model leaderboard as markdown table."""
    if rankings.empty:
        return ""

    lines = [
        "## Model Leaderboard",
        "",
        "> Ranking by average F1 across all matchers and strategies.",
        "",
        "| Rank | Model | Avg F1 | Best Config (F1) | Worst Config (F1) |",
        "|------|-------|--------|------------------|-------------------|",
    ]

    for _, row in rankings.iterrows():
        lines.append(
            f"| {row['rank']} | {row['model_short']} | {row['avg_f1']:.3f} | {row['best_config']} ({row['best_f1']:.2f}) | {row['worst_config']} ({row['worst_f1']:.2f}) |"
        )

    lines.append("")
    return "\n".join(lines)


# ---------------------------------------------------------------------------
# New Visualizations: Topic Recall by Model, Agreement Matrix, Confidence Calibration
# ---------------------------------------------------------------------------
def plot_topic_recall_by_model(opportunities: pd.DataFrame, path: Path) -> Path:
    """
    Grouped bar chart showing recall by topic, grouped by model.
    X=Topic, Y=Recall, Group=Model.
    Reveals model-specific strengths/weaknesses per topic.
    """
    if opportunities.empty:
        return path

    # Compute recall per (topic, model)
    topic_model_stats = (
        opportunities.groupby(["topic", "model"])
        .agg(recall=("success", "mean"), n=("success", "count"))
        .reset_index()
    )
    topic_model_stats["model_short"] = topic_model_stats["model"].apply(lambda m: m.split("/")[-1])

    # Get unique topics and models
    topics = sorted(topic_model_stats["topic"].unique())
    models = sorted(topic_model_stats["model_short"].unique())
    n_models = len(models)
    n_topics = len(topics)

    if n_topics == 0 or n_models == 0:
        return path

    # Create figure
    fig, ax = plt.subplots(figsize=(max(12, n_topics * 1.5), 7))

    x = np.arange(n_topics)
    total_width = 0.8
    width = total_width / n_models

    # Dynamic colors
    model_colors = get_model_colors(models)

    for i, model in enumerate(models):
        model_data = topic_model_stats[topic_model_stats["model_short"] == model]
        recalls = []
        for topic in topics:
            topic_row = model_data[model_data["topic"] == topic]
            if not topic_row.empty:
                recalls.append(topic_row["recall"].values[0])
            else:
                recalls.append(0)

        offset = (i - (n_models - 1) / 2) * width
        bars = ax.bar(
            x + offset,
            recalls,
            width,
            label=model,
            color=model_colors.get(model, "#999"),
            alpha=0.85,
        )

    ax.set_xlabel("Topic", fontsize=11)
    ax.set_ylabel("Recall", fontsize=11)
    ax.set_title("Topic Recall by Model", fontsize=13, fontweight="bold")
    ax.set_xticks(x)
    ax.set_xticklabels(topics, rotation=30, ha="right", fontsize=9)
    ax.set_ylim(0, 1.05)
    ax.axhline(y=0.5, color="gray", linestyle="--", alpha=0.5, linewidth=1)
    ax.legend(title="Model", loc="upper right")
    ax.grid(axis="y", alpha=0.3)

    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_model_agreement_matrix(opportunities: pd.DataFrame, path: Path) -> Path:
    """
    Heatmap showing pairwise Cohen's Kappa between models.
    Visual evidence of whether models make correlated or complementary errors.
    """
    if opportunities.empty:
        return path

    # Get unique models
    models = sorted(opportunities["model"].unique())
    n_models = len(models)

    if n_models < 2:
        return path

    # Build agreement matrix
    kappa_matrix = np.zeros((n_models, n_models))
    model_short_names = [m.split("/")[-1] for m in models]

    for i, model_a in enumerate(models):
        for j, model_b in enumerate(models):
            if i == j:
                kappa_matrix[i, j] = 1.0  # Perfect agreement with self
            elif i < j:
                # Get success lists for both models on same (student, question) pairs
                a_data = opportunities[opportunities["model"] == model_a].copy()
                b_data = opportunities[opportunities["model"] == model_b].copy()

                # Merge on student+question to align
                merged = a_data.merge(
                    b_data[["student", "question", "success"]],
                    on=["student", "question"],
                    suffixes=("_a", "_b"),
                )

                if not merged.empty:
                    success_a = merged["success_a"].tolist()
                    success_b = merged["success_b"].tolist()
                    kappa = cohen_kappa(success_a, success_b)
                    kappa_matrix[i, j] = kappa
                    kappa_matrix[j, i] = kappa  # Symmetric

    # Create heatmap
    fig, ax = plt.subplots(figsize=(max(6, n_models * 1.5), max(5, n_models * 1.2)))

    mask = np.zeros_like(kappa_matrix, dtype=bool)
    # Optionally mask the diagonal for cleaner look
    # np.fill_diagonal(mask, True)

    sns.heatmap(
        kappa_matrix,
        annot=True,
        fmt=".3f",
        cmap="RdYlGn",
        vmin=-0.2,
        vmax=1.0,
        xticklabels=model_short_names,
        yticklabels=model_short_names,
        square=True,
        ax=ax,
        cbar_kws={"label": "Cohen's κ"},
        mask=mask,
    )

    ax.set_title("Model Agreement Matrix (Cohen's κ)", fontsize=13, fontweight="bold")
    ax.set_xlabel("Model", fontsize=11)
    ax.set_ylabel("Model", fontsize=11)

    # Add interpretation guide
    fig.text(
        0.5,
        -0.02,
        "κ interpretation: <0.2 slight, 0.2-0.4 fair, 0.4-0.6 moderate, 0.6-0.8 substantial, >0.8 near-perfect",
        ha="center",
        fontsize=9,
        style="italic",
        color="gray",
    )

    plt.tight_layout()
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()
    return path


def plot_confidence_calibration_distribution(detections: pd.DataFrame, path: Path) -> Path:
    """
    Overlaid histograms/density plots of confidence scores for TP vs FP.
    Reveals whether model confidence can be trusted (e.g., "Is model uncertain when it hallucinates?").
    """
    if detections.empty:
        return path

    # Filter to TP and FP only (these have confidence scores)
    tp_fp = detections[
        detections["result"].isin(
            [MatchResult.TRUE_POSITIVE.value, MatchResult.FALSE_POSITIVE.value]
        )
    ].copy()
    tp_fp = tp_fp.dropna(subset=["confidence"])

    if tp_fp.empty or len(tp_fp) < 10:
        return path

    tp_conf = tp_fp[tp_fp["result"] == MatchResult.TRUE_POSITIVE.value]["confidence"].clip(0, 1)
    fp_conf = tp_fp[tp_fp["result"] == MatchResult.FALSE_POSITIVE.value]["confidence"].clip(0, 1)

    fig, axes = plt.subplots(1, 2, figsize=(14, 5))

    # Left: Overlaid histograms
    ax1 = axes[0]
    bins = np.linspace(0, 1, 21)

    if len(tp_conf) > 0:
        ax1.hist(
            tp_conf,
            bins=bins,
            alpha=0.6,
            label=f"True Positives (n={len(tp_conf)})",
            color="#2ecc71",
            edgecolor="white",
        )
    if len(fp_conf) > 0:
        ax1.hist(
            fp_conf,
            bins=bins,
            alpha=0.6,
            label=f"False Positives (n={len(fp_conf)})",
            color="#e74c3c",
            edgecolor="white",
        )

    ax1.set_xlabel("Confidence Score", fontsize=11)
    ax1.set_ylabel("Count", fontsize=11)
    ax1.set_title("Confidence Distribution: TP vs FP", fontsize=12, fontweight="bold")
    ax1.legend(loc="upper left")
    ax1.set_xlim(0, 1)
    ax1.grid(axis="y", alpha=0.3)

    # Add mean lines
    if len(tp_conf) > 0:
        ax1.axvline(
            tp_conf.mean(),
            color="#27ae60",
            linestyle="--",
            linewidth=2,
            label=f"TP mean: {tp_conf.mean():.2f}",
        )
    if len(fp_conf) > 0:
        ax1.axvline(
            fp_conf.mean(),
            color="#c0392b",
            linestyle="--",
            linewidth=2,
            label=f"FP mean: {fp_conf.mean():.2f}",
        )

    # Right: KDE density plot
    ax2 = axes[1]
    if len(tp_conf) > 5:
        sns.kdeplot(tp_conf, ax=ax2, color="#2ecc71", fill=True, alpha=0.4, label="True Positives")
    if len(fp_conf) > 5:
        sns.kdeplot(fp_conf, ax=ax2, color="#e74c3c", fill=True, alpha=0.4, label="False Positives")

    ax2.set_xlabel("Confidence Score", fontsize=11)
    ax2.set_ylabel("Density", fontsize=11)
    ax2.set_title("Confidence Density: TP vs FP", fontsize=12, fontweight="bold")
    ax2.legend(loc="upper left")
    ax2.set_xlim(0, 1)
    ax2.grid(axis="y", alpha=0.3)

    # Add summary statistics as text box
    stats_text = []
    if len(tp_conf) > 0:
        stats_text.append(f"TP: μ={tp_conf.mean():.2f}, σ={tp_conf.std():.2f}")
    if len(fp_conf) > 0:
        stats_text.append(f"FP: μ={fp_conf.mean():.2f}, σ={fp_conf.std():.2f}")

    if stats_text:
        # Compute separation metric
        if len(tp_conf) > 0 and len(fp_conf) > 0:
            mean_diff = tp_conf.mean() - fp_conf.mean()
            stats_text.append(f"Δμ (TP-FP): {mean_diff:+.2f}")

        textstr = "\n".join(stats_text)
        props = dict(boxstyle="round", facecolor="wheat", alpha=0.8)
        ax2.text(
            0.02,
            0.98,
            textstr,
            transform=ax2.transAxes,
            fontsize=9,
            verticalalignment="top",
            bbox=props,
        )

    plt.suptitle(
        "Confidence Calibration Analysis: Can We Trust Model Confidence?",
        fontsize=13,
        fontweight="bold",
        y=1.02,
    )
    plt.tight_layout()
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()
    return path


# ---------------------------------------------------------------------------
# Reporting
# ---------------------------------------------------------------------------
def render_metrics_table(
    metrics: pd.DataFrame, ci: pd.DataFrame | None, include_match_mode: bool = False
) -> str:
    merged = metrics.copy()

    # Determine merge columns based on whether match_mode is present
    if include_match_mode and "match_mode" in metrics.columns:
        merge_cols = ["match_mode", "strategy", "model"]
        header = "| Matcher | Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (F1) |"
        separator = (
            "|---------|----------|-------|----|----|----|-----------|--------|----|---------|"
        )
    else:
        merge_cols = ["strategy", "model"]
        header = "| Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (Precision) | CI (Recall) | CI (F1) |"
        separator = "|----------|-------|----|----|----|-----------|--------|----|----------------|-------------|---------|"

    if ci is not None and not ci.empty:
        merged = merged.merge(ci, on=merge_cols, how="left")

    lines = [header, separator]

    for _, row in merged.iterrows():
        f1_ci = (
            f"{row['f1_lo']:.2f}–{row['f1_hi']:.2f}"
            if "f1_lo" in row and not pd.isna(row.get("f1_lo"))
            else "-"
        )

        if include_match_mode and "match_mode" in row:
            lines.append(
                f"| {row['match_mode']} | {row['strategy']} | {row['model'].split('/')[-1]} | "
                f"{int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} | {f1_ci} |"
            )
        else:
            prec_ci = (
                f"{row['precision_lo']:.2f}–{row['precision_hi']:.2f}"
                if "precision_lo" in row and not pd.isna(row.get("precision_lo"))
                else "-"
            )
            rec_ci = (
                f"{row['recall_lo']:.2f}–{row['recall_hi']:.2f}"
                if "recall_lo" in row and not pd.isna(row.get("recall_lo"))
                else "-"
            )
            lines.append(
                f"| {row['strategy']} | {row['model'].split('/')[-1]} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} | {prec_ci} | {rec_ci} | {f1_ci} |"
            )
    return "\n".join(lines)


def render_ablation_summary(metrics: pd.DataFrame) -> str:
    """Render a summary table comparing matchers averaged across strategies and models."""
    if "match_mode" not in metrics.columns:
        return ""

    summary = (
        metrics.groupby("match_mode")
        .agg(
            {
                "tp": "sum",
                "fp": "sum",
                "fn": "sum",
                "precision": "mean",
                "recall": "mean",
                "f1": "mean",
            }
        )
        .reset_index()
    )

    lines = [
        "| Matcher | Total TP | Total FP | Total FN | Avg Precision | Avg Recall | Avg F1 |",
        "|---------|----------|----------|----------|---------------|------------|--------|",
    ]

    for _, row in summary.iterrows():
        lines.append(
            f"| {row['match_mode']} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
            f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
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


def compute_misconception_recall(
    opportunities: pd.DataFrame, groundtruth: list[dict[str, Any]]
) -> pd.DataFrame:
    """
    Compute recall per misconception ID.
    Returns a DataFrame with columns: expected_id, name, category, recall, n
    sorted by recall (hardest to detect first).
    """
    if opportunities.empty or "expected_id" not in opportunities.columns:
        return pd.DataFrame()

    # Build a lookup from misconception ID to name/category
    gt_lookup = {}
    for m in groundtruth:
        mid = m.get("id", "")
        gt_lookup[mid] = {
            "name": m.get("name", mid),
            "category": m.get("category", "Unknown"),
        }

    # Group by expected_id and compute recall
    stats = (
        opportunities.groupby("expected_id")
        .agg(recall=("success", "mean"), n=("success", "count"))
        .reset_index()
    )

    # Add name and category from groundtruth
    stats["name"] = stats["expected_id"].apply(lambda x: gt_lookup.get(x, {}).get("name", x))
    stats["category"] = stats["expected_id"].apply(
        lambda x: gt_lookup.get(x, {}).get("category", "Unknown")
    )

    # Sort by recall (hardest first)
    stats = stats.sort_values("recall").reset_index(drop=True)

    return stats


def compute_potential_recall(opportunities: pd.DataFrame) -> dict[str, Any]:
    """
    Compute "Potential Recall" (The Diagnostic Ceiling) metrics.

    Potential Recall = (Unique files detected by ANY model/strategy) / (Total seeded files)

    This metric answers RQ1: "What is the theoretical upper bound of LLM detection?"

    Returns a dict with:
      - potential_recall: float (the ceiling)
      - average_recall: float (standard mean across all runs)
      - consistency: float (average / potential, measures reliability)
      - unique_files_detected: int
      - total_seeded_files: int
      - by_misconception: DataFrame with per-misconception potential recall
    """
    if opportunities.empty:
        return {
            "potential_recall": 0.0,
            "average_recall": 0.0,
            "consistency": 0.0,
            "unique_files_detected": 0,
            "total_seeded_files": 0,
            "by_misconception": pd.DataFrame(),
        }

    # Filter to hybrid matcher if we're in ablation mode
    if "match_mode" in opportunities.columns:
        opps = opportunities[opportunities["match_mode"] == "hybrid"].copy()
    else:
        opps = opportunities.copy()

    # Create unique file key (student + question)
    opps["file_key"] = opps["student"] + "_" + opps["question"]

    # Total unique seeded files
    total_seeded_files = opps["file_key"].nunique()

    # For each unique file, check if ANY model/strategy detected it
    # Group by file_key, take max of success (1 if any detected, 0 if all missed)
    file_detection = opps.groupby("file_key")["success"].max().reset_index()
    unique_files_detected = int(file_detection["success"].sum())

    # Potential Recall = files detected by anyone / total files
    potential_recall = unique_files_detected / total_seeded_files if total_seeded_files else 0.0

    # Average Recall = standard mean across all opportunities
    average_recall = float(opps["success"].mean()) if not opps.empty else 0.0

    # Consistency = Average / Potential (how reliable are individual runs?)
    consistency = average_recall / potential_recall if potential_recall > 0 else 0.0

    # Per-misconception potential recall
    by_misconception = (
        opps.groupby("expected_id")
        .apply(
            lambda g: pd.Series(
                {
                    "potential_recall": g.groupby("file_key")["success"].max().mean(),
                    "average_recall": g["success"].mean(),
                    "n_files": g["file_key"].nunique(),
                    "n_opportunities": len(g),
                }
            ),
            include_groups=False,
        )
        .reset_index()
    )
    by_misconception["consistency"] = (
        by_misconception["average_recall"] / by_misconception["potential_recall"]
    ).fillna(0)
    by_misconception = by_misconception.sort_values("potential_recall")

    return {
        "potential_recall": potential_recall,
        "average_recall": average_recall,
        "consistency": consistency,
        "unique_files_detected": unique_files_detected,
        "total_seeded_files": total_seeded_files,
        "by_misconception": by_misconception,
    }


def compute_cognitive_depth_analysis(
    opportunities: pd.DataFrame, groundtruth: list[dict[str, Any]]
) -> dict[str, Any]:
    """
    Analyze detection performance by cognitive depth.

    This metric answers RQ2: "Does LLM diagnostic performance correlate with cognitive depth?"

    Uses the 'cognitive_depth' field in groundtruth.json if present.
    Falls back to heuristic classification based on category if not.

    Returns a dict with:
      - by_depth: DataFrame with recall per depth level
      - depth_gap: float (surface_recall - notional_recall)
      - is_significant: bool (whether gap exceeds threshold)
    """
    if opportunities.empty:
        return {
            "by_depth": pd.DataFrame(),
            "depth_gap": 0.0,
            "is_significant": False,
        }

    # Filter to hybrid matcher if in ablation mode
    if "match_mode" in opportunities.columns:
        opps = opportunities[opportunities["match_mode"] == "hybrid"].copy()
    else:
        opps = opportunities.copy()

    # Build depth lookup from groundtruth
    depth_lookup = {}
    # Heuristic: classify based on category if no explicit depth field
    surface_categories = {"Methods", "Data Types", "Algebraic Reasoning", "State / Representation"}
    notional_categories = {"Input", "State / Variables", "State / Input", "Input / Data Types"}

    for m in groundtruth:
        mid = m.get("id", "")
        explicit_depth = m.get("cognitive_depth")
        if explicit_depth:
            depth_lookup[mid] = explicit_depth
        else:
            # Fallback heuristic
            category = m.get("category", "")
            if category in surface_categories:
                depth_lookup[mid] = "surface"
            elif category in notional_categories:
                depth_lookup[mid] = "notional"
            else:
                depth_lookup[mid] = "unknown"

    # Add depth to opportunities
    opps["cognitive_depth"] = opps["expected_id"].map(depth_lookup).fillna("unknown")

    # Compute recall by depth
    by_depth = (
        opps.groupby("cognitive_depth")
        .agg(
            recall=("success", "mean"),
            n=("success", "count"),
        )
        .reset_index()
    )

    # Compute depth gap (surface - notional)
    surface_recall = by_depth[by_depth["cognitive_depth"] == "surface"]["recall"].values
    notional_recall = by_depth[by_depth["cognitive_depth"] == "notional"]["recall"].values

    surface_val = float(surface_recall[0]) if len(surface_recall) > 0 else 0.0
    notional_val = float(notional_recall[0]) if len(notional_recall) > 0 else 0.0
    depth_gap = surface_val - notional_val

    # Consider significant if gap > 20%
    is_significant = depth_gap > 0.20

    return {
        "by_depth": by_depth,
        "depth_gap": depth_gap,
        "is_significant": is_significant,
        "surface_recall": surface_val,
        "notional_recall": notional_val,
    }


def render_potential_recall_section(ceiling_stats: dict[str, Any]) -> str:
    """Render markdown section for the Diagnostic Ceiling (RQ1)."""
    lines = [
        "## The Diagnostic Ceiling (RQ1)",
        "",
        "**Potential Recall** measures the theoretical upper bound: what percentage of seeded errors",
        "were found by *at least one* model/strategy combination?",
        "",
        "| Metric | Value |",
        "|--------|-------|",
        f"| Potential Recall (Ceiling) | {ceiling_stats['potential_recall']:.1%} |",
        f"| Average Recall (Reliability) | {ceiling_stats['average_recall']:.1%} |",
        f"| Consistency (Avg/Potential) | {ceiling_stats['consistency']:.1%} |",
        f"| Unique Files Detected | {ceiling_stats['unique_files_detected']} / {ceiling_stats['total_seeded_files']} |",
        "",
    ]

    # Interpretation
    if ceiling_stats["potential_recall"] > 0.9:
        lines.append("> [!TIP]")
        lines.append("> High Ceiling: Most errors are detectable by at least one configuration.")
    elif ceiling_stats["potential_recall"] < 0.5:
        lines.append("> [!CAUTION]")
        lines.append("> Low Ceiling: Many errors are invisible to all model/strategy combinations.")

    if ceiling_stats["consistency"] < 0.5:
        lines.append("")
        lines.append("> [!WARNING]")
        lines.append(
            "> Low Consistency: Individual runs are unreliable even when detection is possible."
        )

    return "\n".join(lines)


def render_cognitive_depth_section(depth_stats: dict[str, Any]) -> str:
    """Render markdown section for Cognitive Alignment (RQ2)."""
    by_depth = depth_stats.get("by_depth", pd.DataFrame())
    if by_depth.empty:
        return ""

    lines = [
        "## Cognitive Alignment (RQ2)",
        "",
        "Does LLM performance correlate with the **cognitive depth** of the misconception?",
        "",
        "| Depth Level | Recall | N |",
        "|-------------|--------|---|",
    ]

    for _, row in by_depth.iterrows():
        lines.append(f"| {row['cognitive_depth']} | {row['recall']:.1%} | {int(row['n'])} |")

    lines.extend(
        [
            "",
            f"**Depth Gap (Surface - Notional):** {depth_stats['depth_gap']:.1%}",
            "",
        ]
    )

    if depth_stats["is_significant"]:
        lines.append("> [!IMPORTANT]")
        lines.append(
            "> Significant Depth Gap: LLMs detect Surface errors far better than Notional Machine violations."
        )
        lines.append(
            "> This supports the hypothesis that LLMs act as 'Compiler++' rather than 'Pedagogical Tutors'."
        )
    else:
        lines.append("> [!NOTE]")
        lines.append("> No significant depth gap observed in this run.")

    return "\n".join(lines)


def render_misconception_table(stats: pd.DataFrame) -> str:
    """Render markdown table of per-misconception recall."""
    if stats.empty:
        return "_No misconception data available_"

    lines = [
        "| ID | Misconception | Category | Recall | N |",
        "|----|---------------|----------|--------|---|",
    ]
    for _, row in stats.iterrows():
        # Truncate long names
        name = row["name"][:35] + "..." if len(row["name"]) > 35 else row["name"]
        lines.append(
            f"| {row['expected_id']} | {name} | {row['category']} | "
            f"{row['recall']:.2f} | {int(row['n'])} |"
        )
    return "\n".join(lines)


def plot_misconception_recall_bars(stats: pd.DataFrame, path: Path) -> Path:
    """Horizontal bar chart of recall per misconception, color-coded by recall."""
    if stats.empty:
        return path

    plt.figure(figsize=(12, max(6, len(stats) * 0.4)))

    # Create labels with ID and short name
    labels = [f"{row['expected_id']}: {row['name'][:25]}" for _, row in stats.iterrows()]
    recalls = stats["recall"].values

    # Color by recall (red=low, green=high)
    colors = plt.cm.RdYlGn(recalls)

    bars = plt.barh(labels, recalls, color=colors)

    # Add count labels
    for bar, n in zip(bars, stats["n"]):
        plt.text(
            bar.get_width() + 0.02,
            bar.get_y() + bar.get_height() / 2,
            f"n={int(n)}",
            va="center",
            fontsize=8,
            color="gray",
        )

    plt.xlabel("Recall")
    plt.ylabel("Misconception")
    plt.title("Detection Recall by Misconception (sorted by difficulty)")
    plt.xlim(0, 1.15)
    plt.axvline(x=0.5, color="gray", linestyle="--", alpha=0.5)
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def compute_dataset_summary(manifest: list[dict[str, Any]]) -> dict[str, Any]:
    """Compute dataset summary statistics from manifest."""
    if not manifest:
        return {}

    # Get metadata from first entry's parent structure if available
    # The manifest is a list of students
    total_students = len(manifest)

    # Count questions per student (assume all have same questions)
    first_student = manifest[0] if manifest else {}
    questions = list(first_student.get("files", {}).keys())
    total_questions = len(questions)
    total_files = total_students * total_questions

    # Count seeded vs clean
    seeded_count = 0
    clean_count = 0
    for student in manifest:
        for q, file_info in student.get("files", {}).items():
            if file_info.get("type") == "SEEDED":
                seeded_count += 1
            else:
                clean_count += 1

    seeded_pct = (seeded_count / total_files * 100) if total_files > 0 else 0

    return {
        "total_students": total_students,
        "total_questions": total_questions,
        "total_files": total_files,
        "seeded_count": seeded_count,
        "clean_count": clean_count,
        "seeded_pct": seeded_pct,
        "questions": questions,
    }


def render_dataset_summary(
    summary: dict[str, Any],
    manifest_meta: dict[str, Any],
    match_mode: str,
    opportunities_count: int,
) -> str:
    """Render dataset summary section for the report."""
    if not summary:
        return ""

    lines = [
        "## Dataset & Run Configuration",
        "",
        "### Dataset Summary",
        "- **Assignment:** A1 – Kinematics & Geometry (CS1)",
        f"- **Students:** {summary.get('total_students', 'N/A')}",
        f"- **Questions:** {summary.get('total_questions', 'N/A')} ({', '.join(summary.get('questions', []))})",
        f"- **Total files:** {summary.get('total_files', 'N/A')}",
        f"- **Seeded files:** {summary.get('seeded_count', 0)} ({summary.get('seeded_pct', 0):.1f}%)",
        f"- **Clean files:** {summary.get('clean_count', 0)} ({100 - summary.get('seeded_pct', 0):.1f}%)",
        f"- **Detection opportunities:** {opportunities_count}",
        "",
        "### Run Configuration",
        f"- **Generation seed:** {manifest_meta.get('seed', 'N/A')}",
        f"- **Generation model:** {manifest_meta.get('model', 'N/A')}",
        f"- **Match mode:** {match_mode}",
        "- **Embedding model:** text-embedding-3-large (OpenAI)",
        f"- **Detection models:** {', '.join(MODEL_SHORT_NAMES.values())}",
        "- **Strategies:** baseline, taxonomy, cot, socratic",
        "",
    ]
    return "\n".join(lines)


def generate_report(
    metrics: pd.DataFrame,
    ci: pd.DataFrame,
    opportunities: pd.DataFrame,
    detections: pd.DataFrame,
    asset_paths: dict[str, Path],
    misconception_stats: pd.DataFrame | None = None,
    dataset_summary: dict[str, Any] | None = None,
    manifest_meta: dict[str, Any] | None = None,
    match_mode: str = "hybrid",
    ceiling_stats: dict[str, Any] | None = None,
    depth_stats: dict[str, Any] | None = None,
    groundtruth: list[dict[str, Any]] | None = None,
) -> str:
    ts = datetime.now(timezone.utc).isoformat()
    is_ablation = "match_mode" in metrics.columns

    report = [
        "# LLM Misconception Detection: Analysis Report",
        f"_Generated: {ts}_",
        "",
    ]

    # Dataset & Run Configuration section
    if dataset_summary and manifest_meta:
        # For ablation, count opportunities from hybrid only
        if is_ablation:
            opp_count = (
                len(opportunities[opportunities["match_mode"] == "hybrid"])
                if "match_mode" in opportunities.columns
                else len(opportunities)
            )
        else:
            opp_count = len(opportunities)

        report.append(
            render_dataset_summary(
                dataset_summary,
                manifest_meta,
                match_mode if not is_ablation else "all (ablation)",
                opp_count,
            )
        )

    # Executive highlights differ for ablation vs single-matcher
    if is_ablation:
        report.extend(
            [
                "## Executive Highlights",
                "- **Matcher Ablation Study**: Comparing fuzzy_only, semantic_only, and hybrid matchers.",
                "- Bootstrap CIs included for statistical rigor.",
                "- Same detection data, different matching strategies.",
                "",
            ]
        )
    else:
        report.extend(
            [
                "## Executive Highlights",
                "- Hybrid matcher (fuzzy + semantic + topic prior) applied across all strategies/models.",
                "- Bootstrap CIs included for statistical rigor.",
                "",
            ]
        )

    # Model Leaderboard (ranking best to worst)
    model_rankings = compute_model_rankings(metrics)
    if not model_rankings.empty:
        report.append(render_model_leaderboard(model_rankings))

    # RQ1: Diagnostic Ceiling section
    if ceiling_stats:
        report.append(render_potential_recall_section(ceiling_stats))
        report.append("")

    # Ablation section (only when running all matchers)
    if is_ablation:
        report.extend(
            [
                "## Matcher Ablation: Fuzzy vs Semantic vs Hybrid",
                "",
                "### Summary (averaged across strategies and models)",
                render_ablation_summary(metrics),
                "",
                f"![Matcher Ablation]({asset_paths.get('matcher_ablation', '')})"
                if asset_paths.get("matcher_ablation")
                else "",
                "",
                "### Precision-Recall by Matcher",
                f"![Matcher PR Scatter]({asset_paths.get('matcher_pr_scatter', '')})"
                if asset_paths.get("matcher_pr_scatter")
                else "",
                "",
                "### Matcher × Strategy Distribution",
                f"![Matcher Strategy Grid]({asset_paths.get('matcher_strategy_grid', '')})"
                if asset_paths.get("matcher_strategy_grid")
                else "",
                "",
            ]
        )
    else:
        # Non-ablation: standard report structure
        report.extend(
            [
                "## Model Comparison Overview",
                f"![Model Comparison]({asset_paths.get('model_comparison', '')})"
                if asset_paths.get("model_comparison")
                else "",
                "",
                "## Strategy × Model Performance",
                render_metrics_table(metrics, ci),
                "",
                f"![F1 by Strategy]({asset_paths.get('strategy_f1', '')})"
                if asset_paths.get("strategy_f1")
                else "",
                "",
                "### Precision-Recall Tradeoff",
                f"![Precision-Recall Scatter]({asset_paths.get('pr_scatter', '')})"
                if asset_paths.get("pr_scatter")
                else "",
                "",
            ]
        )

    # Topic analysis (filter to hybrid for ablation, or use all)
    if is_ablation:
        hybrid_opps = opportunities[opportunities["match_mode"] == "hybrid"]
        topic_opps = hybrid_opps if not hybrid_opps.empty else opportunities
    else:
        topic_opps = opportunities

    report.extend(
        [
            "## Topic Difficulty (Recall)",
            render_topic_table(topic_opps),
            "",
            f"![Topic Recall]({asset_paths.get('topic_bars', '')})"
            if asset_paths.get("topic_bars")
            else "",
            "",
            "## Topic Heatmap",
            f"![Topic Heatmap]({asset_paths.get('heatmap', '')})"
            if asset_paths.get("heatmap")
            else "_No heatmap generated_",
            "",
        ]
    )

    # Topic Recall by Model section
    report.extend(
        [
            "## Topic Recall by Model",
            "",
            "Grouped bar chart showing recall per topic, split by model. Reveals model-specific strengths and weaknesses.",
            "",
            f"![Topic Recall by Model]({asset_paths.get('topic_recall_by_model', '')})"
            if asset_paths.get("topic_recall_by_model")
            else "_No plot generated_",
            "",
        ]
    )

    # Model Agreement Matrix section
    report.extend(
        [
            "## Model Agreement Matrix",
            "",
            "Pairwise Cohen's κ between models. Higher values indicate correlated predictions; lower values suggest complementary errors (good for ensembles).",
            "",
            f"![Model Agreement Matrix]({asset_paths.get('model_agreement_matrix', '')})"
            if asset_paths.get("model_agreement_matrix")
            else "_No plot generated_",
            "",
        ]
    )

    # Confidence Calibration Analysis section
    report.extend(
        [
            "## Confidence Calibration Analysis",
            "",
            "Distribution of model confidence scores for True Positives vs False Positives. A well-calibrated model should show higher confidence for TPs than FPs.",
            "",
            f"![Confidence Calibration]({asset_paths.get('confidence_calibration', '')})"
            if asset_paths.get("confidence_calibration")
            else "_No plot generated_",
            "",
        ]
    )

    # Per-misconception analysis section
    if misconception_stats is not None and not misconception_stats.empty:
        report.extend(
            [
                "## Per-Misconception Detection Rates",
                "",
                "Detection recall for each seeded misconception, sorted by difficulty (hardest to detect at top):",
                "",
                render_misconception_table(misconception_stats),
                "",
                f"![Misconception Recall]({asset_paths.get('misconception_recall', '')})"
                if asset_paths.get("misconception_recall")
                else "",
                "",
            ]
        )

    report.extend(
        [
            "## Hallucination Analysis",
            f"![Hallucinations]({asset_paths.get('hallucinations', '')})"
            if asset_paths.get("hallucinations")
            else "",
            "",
            render_hallucination_snippets(detections),
            "",
            "## Methods",
            "- Data: 60 students × 4 questions (seeded/clean) with manifest-driven ground truth.",
            f"- Detection: {', '.join(MODEL_SHORT_NAMES.values())} across strategies (baseline, taxonomy, cot, socratic).",
        ]
    )

    if is_ablation:
        report.append(
            "- Matching: Ablation comparing fuzzy-only, semantic-only (text-embedding-3-large), and hybrid (fuzzy + semantic + topic prior)."
        )
    else:
        report.append(
            "- Matching: Hybrid fusion of fuzzy similarity, semantic embeddings, and topic priors."
        )

    report.append(
        "- Metrics: Precision/Recall/F1 with bootstrap CIs; agreement via κ; significance via McNemar where applicable."
    )

    # Agreement & significance (use hybrid for ablation)
    if is_ablation:
        opps_for_agreement = opportunities[opportunities["match_mode"] == "hybrid"]
    else:
        opps_for_agreement = opportunities

    # Collect agreement data for tables
    agreement_rows = []
    for strategy, grp in opps_for_agreement.groupby("strategy"):
        models = sorted(grp["model"].unique())
        if len(models) < 2:
            continue

        # Compare all pairs of models
        for model_a, model_b in combinations(models, 2):
            # Get short names for display
            model_a_short = model_a.split("/")[-1]
            model_b_short = model_b.split("/")[-1]

            a_res = grp[grp["model"] == model_a]["success"].tolist()
            b_res = grp[grp["model"] == model_b]["success"].tolist()

            # Ensure same length (matching on student×question)
            if len(a_res) != len(b_res):
                continue

            kappa = cohen_kappa(a_res, b_res)
            stat, p, table = mcnemar(a_res, b_res)
            agreement_rows.append(
                {
                    "strategy": strategy,
                    "model_a": model_a_short,
                    "model_b": model_b_short,
                    "kappa": kappa,
                    "mcnemar_stat": stat,
                    "mcnemar_p": p,
                    "both_correct": table["both_correct"],
                    "only_a": table["only_a"],
                    "only_b": table["only_b"],
                    "both_wrong": table["both_wrong"],
                }
            )

    if agreement_rows:
        report.extend(
            [
                "",
                "## Agreement & Significance",
                "",
                "### Cohen's Kappa (Inter-Model Agreement)",
                "",
                "| Strategy | Model A | Model B | Cohen's κ | Interpretation |",
                "|----------|---------|---------|-----------|----------------|",
            ]
        )
        for row in agreement_rows:
            # Interpret kappa
            k = row["kappa"]
            if k >= 0.8:
                interp = "Almost Perfect"
            elif k >= 0.6:
                interp = "Substantial"
            elif k >= 0.4:
                interp = "Moderate"
            elif k >= 0.2:
                interp = "Fair"
            else:
                interp = "Slight"
            report.append(
                f"| {row['strategy']} | {row['model_a']} | {row['model_b']} | {k:.3f} | {interp} |"
            )

        report.extend(
            [
                "",
                "### McNemar's Test (Significance of Differences)",
                "",
                "| Strategy | Model A | Model B | χ² Stat | p-value | Significant? | Both✓ | A only | B only | Both✗ |",
                "|----------|---------|---------|---------|---------|--------------|-------|--------|--------|-------|",
            ]
        )
        for row in agreement_rows:
            sig = "Yes" if row["mcnemar_p"] < 0.05 else "No"
            report.append(
                f"| {row['strategy']} | {row['model_a']} | {row['model_b']} | {row['mcnemar_stat']:.2f} | {row['mcnemar_p']:.4f} | {sig} | {row['both_correct']} | {row['only_a']} | {row['only_b']} | {row['both_wrong']} |"
            )

        # McNemar bar chart
        if asset_paths.get("mcnemar_chart"):
            report.extend(
                [
                    "",
                    f"![McNemar Significance Chart]({asset_paths.get('mcnemar_chart')})",
                ]
            )

    # Full Results Table at the end (for ablation mode)
    if is_ablation:
        report.extend(
            [
                "",
                "## Full Results Table",
                "",
                render_metrics_table(metrics, ci, include_match_mode=True),
            ]
        )

    return "\n".join(report)


# ---------------------------------------------------------------------------
# Run Storage
# ---------------------------------------------------------------------------
def generate_run_id(run_tag: str | None, seed: int | None) -> str:
    """Generate a unique run ID from tag or timestamp + seed."""
    date_str = datetime.now().strftime("%Y-%m-%d")
    if run_tag:
        return f"run_{run_tag}"
    elif seed:
        return f"run_{date_str}_seed{seed}"
    else:
        return f"run_{date_str}_{datetime.now().strftime('%H%M%S')}"


def compute_results_summary(metrics: pd.DataFrame) -> dict[str, Any]:
    """Compute summary results for config.json."""
    if metrics.empty:
        return {}

    results: dict[str, Any] = {}

    # Per-matcher summary (if ablation mode)
    if "match_mode" in metrics.columns:
        by_matcher = {}
        for mode in metrics["match_mode"].unique():
            mode_df = metrics[metrics["match_mode"] == mode]
            by_matcher[mode] = {
                "avg_f1": float(mode_df["f1"].mean()),
                "avg_precision": float(mode_df["precision"].mean()),
                "avg_recall": float(mode_df["recall"].mean()),
            }
        results["by_matcher"] = by_matcher

    # Best overall config
    best_idx = metrics["f1"].idxmax()
    best_row = metrics.loc[best_idx]
    results["best_overall"] = {
        "matcher": best_row.get("match_mode", "hybrid"),
        "strategy": best_row["strategy"],
        "model": best_row["model"],
        "f1": float(best_row["f1"]),
    }

    return results


def save_run(
    run_id: str,
    manifest_full: dict[str, Any],
    manifest_meta: dict[str, Any],
    dataset_summary: dict[str, Any],
    metrics: pd.DataFrame,
    ci: pd.DataFrame,
    opportunities_df: pd.DataFrame,
    report_text: str,
    asset_paths: dict[str, Path],
    match_mode: str,
    notes: str = "",
) -> Path:
    """Save a complete run to runs/<run_id>/."""
    run_dir = RUNS_DIR / run_id
    assets_dir = run_dir / "assets"

    run_dir.mkdir(parents=True, exist_ok=True)
    assets_dir.mkdir(parents=True, exist_ok=True)

    # Build config.json
    config = {
        "run_id": run_id,
        "created_at": datetime.now(timezone.utc).isoformat(),
        "dataset": {
            "assignment": "A1",
            "seed": manifest_meta.get("seed"),
            "generation_model": manifest_meta.get("model"),
            "generated_at": manifest_meta.get("generated_at"),
            "students": dataset_summary.get("total_students"),
            "questions": dataset_summary.get("total_questions"),
            "total_files": dataset_summary.get("total_files"),
            "seeded_files": dataset_summary.get("seeded_count"),
            "seeded_pct": dataset_summary.get("seeded_pct"),
        },
        "pipeline": {
            "detection_models": sorted(metrics["model"].unique().tolist()),
            "strategies": sorted(metrics["strategy"].unique().tolist()),
            "matchers": ["fuzzy_only", "semantic_only", "hybrid"]
            if match_mode == "all"
            else [match_mode],
            "embedding_model": "text-embedding-3-large",
        },
        "results": compute_results_summary(metrics),
        "notes": notes,
    }

    # Save files
    (run_dir / "config.json").write_text(json.dumps(config, indent=2, default=str))
    (run_dir / "manifest.json").write_text(json.dumps(manifest_full, indent=2, default=str))
    (run_dir / "report.md").write_text(report_text)

    # Save full data export (metrics, ci, opportunities)
    full_export = {
        "metrics": metrics.to_dict(orient="records"),
        "ci": ci.to_dict(orient="records"),
        "opportunities": opportunities_df.to_dict(orient="records"),
    }
    (run_dir / "data.json").write_text(json.dumps(full_export, indent=2, default=str))

    # Copy assets
    import shutil

    for name, src_path in asset_paths.items():
        if src_path.exists():
            shutil.copy(src_path, assets_dir / src_path.name)

    # Update index
    update_runs_index(run_id, config)

    return run_dir


def update_runs_index(run_id: str, config: dict[str, Any]) -> None:
    """Update runs/index.json with a new run entry."""
    RUNS_DIR.mkdir(parents=True, exist_ok=True)

    # Load existing index or create new
    if RUNS_INDEX_PATH.exists():
        index = json.loads(RUNS_INDEX_PATH.read_text())
    else:
        index = {"runs": []}

    # Build summary entry
    results = config.get("results", {})
    by_matcher = results.get("by_matcher", {})
    best = results.get("best_overall", {})

    entry = {
        "run_id": run_id,
        "created_at": config.get("created_at"),
        "seed": config.get("dataset", {}).get("seed"),
        "seeded_pct": config.get("dataset", {}).get("seeded_pct"),
        "fuzzy_f1": by_matcher.get("fuzzy_only", {}).get("avg_f1"),
        "semantic_f1": by_matcher.get("semantic_only", {}).get("avg_f1"),
        "hybrid_f1": by_matcher.get("hybrid", {}).get("avg_f1"),
        "best_f1": best.get("f1"),
        "best_config": f"{best.get('strategy')}+{best.get('model', '').split('/')[-1]}",
        "notes": config.get("notes", ""),
    }

    # Remove existing entry with same run_id (if re-running)
    index["runs"] = [r for r in index["runs"] if r.get("run_id") != run_id]
    index["runs"].append(entry)

    # Sort by created_at
    index["runs"].sort(key=lambda x: x.get("created_at", ""), reverse=True)

    RUNS_INDEX_PATH.write_text(json.dumps(index, indent=2, default=str))


def load_runs_index() -> list[dict[str, Any]]:
    """Load runs index for display."""
    if not RUNS_INDEX_PATH.exists():
        return []
    index = json.loads(RUNS_INDEX_PATH.read_text())
    return index.get("runs", [])


# ---------------------------------------------------------------------------
# Display helpers
# ---------------------------------------------------------------------------
def display_summary(metrics: pd.DataFrame):
    if metrics.empty:
        console.print("[red]No metrics to display[/red]")
        return

    has_match_mode = "match_mode" in metrics.columns

    table = Table(title="Strategy × Model Performance", show_header=True, header_style="bold")
    if has_match_mode:
        table.add_column("Matcher")
    for col in ["Strategy", "Model", "TP", "FP", "FN", "Precision", "Recall", "F1"]:
        table.add_column(col)

    for _, row in metrics.iterrows():
        row_data = []
        if has_match_mode:
            row_data.append(row["match_mode"])
        row_data.extend(
            [
                row["strategy"],
                row["model"].split("/")[-1],
                str(int(row["tp"])),
                str(int(row["fp"])),
                str(int(row["fn"])),
                f"{row['precision']:.3f}",
                f"{row['recall']:.3f}",
                f"{row['f1']:.3f}",
            ]
        )
        table.add_row(*row_data)
    console.print(table)


# ---------------------------------------------------------------------------
# CLI entrypoint
# ---------------------------------------------------------------------------
@app.command("analyze")
def main(
    detections_dir: Path = typer.Option(
        DEFAULT_DETECTIONS_DIR, help="Detections root", show_default=True
    ),
    manifest_path: Path = typer.Option(
        DEFAULT_MANIFEST_PATH, help="Manifest path", show_default=True
    ),
    groundtruth_path: Path = typer.Option(
        DEFAULT_GROUNDTRUTH_PATH, help="Ground truth path", show_default=True
    ),
    match_mode: MatchMode = typer.Option(
        MatchMode.HYBRID, help="Matching mode: fuzzy_only, semantic_only, hybrid, or all"
    ),
    quick: bool = typer.Option(False, help="Quick mode (fewer bootstrap iterations)"),
    run_tag: str = typer.Option(
        None,
        "--run-tag",
        "-t",
        help="Tag for this run (e.g., 'run1'). Auto-generated from seed if not provided.",
    ),
    notes: str = typer.Option("", "--notes", "-n", help="Notes to attach to this run"),
):
    """
    Analyze LLM misconception detections with configurable matching modes.

    All results are saved to runs/<run_id>/ with:
    - report.md: Full analysis report with embedded figures
    - data.json: Complete metrics, CIs, and opportunities data
    - config.json: Run configuration for reproducibility
    - assets/: All generated plots

    Use --match-mode all to run matcher ablation (fuzzy vs semantic vs hybrid).
    """
    console.rule("[bold green]Revamped Analysis[/bold green]")
    console.print(f"[cyan]Match mode:[/cyan] {match_mode.value}")

    strategies = discover_strategies(detections_dir)
    if not strategies:
        console.print("[red]No strategies found under detections/[/red]")
        raise typer.Exit(code=1)

    console.print(f"[cyan]Strategies discovered:[/cyan] {', '.join(strategies)}")
    groundtruth = load_groundtruth(groundtruth_path)
    manifest_full = load_manifest(manifest_path)

    # Extract metadata and students list from manifest
    manifest_meta = {
        "seed": manifest_full.get("seed"),
        "model": manifest_full.get("model"),
        "generated_at": manifest_full.get("generated_at"),
    }
    manifest = manifest_full.get("students", [])
    dataset_summary = compute_dataset_summary(manifest)

    detections_df, opportunities_df = build_dataframes(
        detections_dir=detections_dir,
        strategies=strategies,
        manifest=manifest_full,  # Pass full manifest, not just students list
        groundtruth=groundtruth,
        match_mode=match_mode,
        use_embeddings=True,  # Always use embeddings for semantic/hybrid
    )

    # Determine grouping columns based on match mode
    if match_mode == MatchMode.ALL:
        group_cols = ["match_mode", "strategy", "model"]
    else:
        group_cols = ["strategy", "model"]

    metrics = summarize_metrics(detections_df, group_cols)
    ci = bootstrap_metrics(
        detections_df,
        group_cols=group_cols,
        unit_cols=["student", "question"],
        iters=10 if quick else 400,
    )

    display_summary(metrics)

    ensure_asset_dir()
    asset_paths = {
        "heatmap": ASSET_DIR / "topic_heatmap.png",
        "hallucinations": ASSET_DIR / "hallucinations.png",
        "strategy_f1": ASSET_DIR / "strategy_f1_comparison.png",
        "pr_scatter": ASSET_DIR / "precision_recall_scatter.png",
        "topic_bars": ASSET_DIR / "topic_recall_bars.png",
        "model_comparison": ASSET_DIR / "model_comparison.png",
        "misconception_recall": ASSET_DIR / "misconception_recall.png",
        # New visualizations
        "topic_recall_by_model": ASSET_DIR / "topic_recall_by_model.png",
        "model_agreement_matrix": ASSET_DIR / "model_agreement_matrix.png",
        "confidence_calibration": ASSET_DIR / "confidence_calibration.png",
    }

    # For ablation mode, use hybrid data for topic plots; add ablation-specific plots
    if match_mode == MatchMode.ALL:
        asset_paths["matcher_ablation"] = ASSET_DIR / "matcher_ablation.png"
        asset_paths["matcher_pr_scatter"] = ASSET_DIR / "matcher_pr_scatter.png"
        asset_paths["matcher_strategy_grid"] = ASSET_DIR / "matcher_strategy_grid.png"

        # Filter to hybrid for topic-related plots and misconception analysis
        hybrid_opps = opportunities_df[opportunities_df["match_mode"] == "hybrid"]
        hybrid_dets = detections_df[detections_df["match_mode"] == "hybrid"]

        plot_topic_heatmap(hybrid_opps, asset_paths["heatmap"])
        plot_hallucinations(hybrid_dets, asset_paths["hallucinations"])
        plot_topic_recall_bars(hybrid_opps, asset_paths["topic_bars"])

        # Per-misconception analysis (use hybrid)
        misconception_stats = compute_misconception_recall(hybrid_opps, groundtruth)
        if not misconception_stats.empty:
            plot_misconception_recall_bars(misconception_stats, asset_paths["misconception_recall"])

        # Ablation-specific plots
        plot_matcher_ablation(metrics, asset_paths["matcher_ablation"])
        plot_matcher_pr_scatter(metrics, asset_paths["matcher_pr_scatter"])
        plot_matcher_strategy_grid(metrics, asset_paths["matcher_strategy_grid"])

        # For strategy/model comparison, filter to hybrid
        hybrid_metrics = metrics[metrics["match_mode"] == "hybrid"].copy()
        if not hybrid_metrics.empty:
            plot_strategy_f1_comparison(hybrid_metrics, asset_paths["strategy_f1"])
            plot_precision_recall_scatter(hybrid_metrics, asset_paths["pr_scatter"])
            plot_model_comparison(hybrid_metrics, asset_paths["model_comparison"])

        # New visualizations (use hybrid data)
        plot_topic_recall_by_model(hybrid_opps, asset_paths["topic_recall_by_model"])
        plot_model_agreement_matrix(hybrid_opps, asset_paths["model_agreement_matrix"])
        plot_confidence_calibration_distribution(hybrid_dets, asset_paths["confidence_calibration"])
    else:
        plot_topic_heatmap(opportunities_df, asset_paths["heatmap"])
        plot_hallucinations(detections_df, asset_paths["hallucinations"])
        plot_strategy_f1_comparison(metrics, asset_paths["strategy_f1"])
        plot_precision_recall_scatter(metrics, asset_paths["pr_scatter"])
        plot_topic_recall_bars(opportunities_df, asset_paths["topic_bars"])
        plot_model_comparison(metrics, asset_paths["model_comparison"])

        # Per-misconception analysis
        misconception_stats = compute_misconception_recall(opportunities_df, groundtruth)
        if not misconception_stats.empty:
            plot_misconception_recall_bars(misconception_stats, asset_paths["misconception_recall"])

        # New visualizations
        plot_topic_recall_by_model(opportunities_df, asset_paths["topic_recall_by_model"])
        plot_model_agreement_matrix(opportunities_df, asset_paths["model_agreement_matrix"])
        plot_confidence_calibration_distribution(
            detections_df, asset_paths["confidence_calibration"]
        )

    # Compute RQ1: Diagnostic Ceiling metrics
    ceiling_stats = compute_potential_recall(opportunities_df)
    console.print(
        f"[cyan]Diagnostic Ceiling: {ceiling_stats['potential_recall']:.1%} (Avg: {ceiling_stats['average_recall']:.1%})[/cyan]"
    )

    # Compute RQ2: Cognitive Depth metrics
    depth_stats = compute_cognitive_depth_analysis(opportunities_df, groundtruth)
    if depth_stats["by_depth"] is not None and not depth_stats["by_depth"].empty:
        console.print(
            f"[cyan]Depth Gap (Surface - Notional): {depth_stats['depth_gap']:.1%}[/cyan]"
        )

    # Build asset paths for run-local report (relative to run directory)
    run_asset_paths = {k: Path("assets") / v.name for k, v in asset_paths.items()}

    report_text = generate_report(
        metrics,
        ci,
        opportunities_df,
        detections_df,
        run_asset_paths,  # Use run-local paths for the report
        misconception_stats=misconception_stats,
        dataset_summary=dataset_summary,
        manifest_meta=manifest_meta,
        match_mode=match_mode.value,
        ceiling_stats=ceiling_stats,
        depth_stats=depth_stats,
        groundtruth=groundtruth,
    )

    # Save run to runs/ directory
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
        asset_paths=asset_paths,  # Original paths for copying
        match_mode=match_mode.value,
        notes=notes,
    )
    console.print(f"[green]Run saved to {run_dir}[/green]")
    console.print(f"[dim]Report: {run_dir / 'report.md'}[/dim]")
    console.print(f"[dim]Data: {run_dir / 'data.json'}[/dim]")


@app.command("list-runs")
def list_runs():
    """List all saved runs with comparison table."""
    runs = load_runs_index()

    if not runs:
        console.print(
            "[yellow]No runs found. Run 'analyze analyze --match-mode all' to create one.[/yellow]"
        )
        return

    table = Table(title="Saved Runs", show_header=True, header_style="bold")
    table.add_column("Run ID")
    table.add_column("Seed")
    table.add_column("Seeded%")
    table.add_column("Fuzzy F1")
    table.add_column("Semantic F1")
    table.add_column("Hybrid F1")
    table.add_column("Best F1")
    table.add_column("Best Config")
    table.add_column("Notes")

    for run in runs:
        table.add_row(
            run.get("run_id", ""),
            str(run.get("seed", "")),
            f"{run.get('seeded_pct', 0):.1f}%",
            f"{run.get('fuzzy_f1', 0):.3f}" if run.get("fuzzy_f1") else "-",
            f"{run.get('semantic_f1', 0):.3f}" if run.get("semantic_f1") else "-",
            f"{run.get('hybrid_f1', 0):.3f}" if run.get("hybrid_f1") else "-",
            f"{run.get('best_f1', 0):.3f}" if run.get("best_f1") else "-",
            run.get("best_config", ""),
            run.get("notes", "")[:30],
        )

    console.print(table)


@app.command("validate-dataset")
def validate_dataset(
    manifest: Path = typer.Option(DEFAULT_MANIFEST_PATH, help="Path to manifest.json"),
    groundtruth: Path = typer.Option(DEFAULT_GROUNDTRUTH_PATH, help="Path to groundtruth.json"),
):
    """Validate and summarize the synthetic dataset before analysis."""
    if not manifest.exists():
        console.print(f"[red]Manifest not found: {manifest}[/red]")
        raise typer.Exit(1)

    manifest_data = load_manifest(manifest)
    gt_data = load_groundtruth(groundtruth) if groundtruth.exists() else []
    gt_map = {m["id"]: m for m in gt_data}

    # Basic counts
    students = manifest_data.get("students", [])
    total_students = len(students)
    manifest_version = manifest_data.get("manifest_version", "1.0")

    console.print("\n[bold cyan]Dataset Summary[/bold cyan]")
    console.print(f"  Manifest Version: {manifest_version}")
    console.print(f"  Total Students: {total_students}")
    console.print(f"  Model: {manifest_data.get('model', 'unknown')}")
    console.print(f"  Seed: {manifest_data.get('seed', 'unknown')}")

    # Count per question and per misconception
    question_counts = {
        "Q1": {"SEEDED": 0, "CLEAN": 0},
        "Q2": {"SEEDED": 0, "CLEAN": 0},
        "Q3": {"SEEDED": 0, "CLEAN": 0},
        "Q4": {"SEEDED": 0, "CLEAN": 0},
    }
    misconception_counts: dict[str, int] = {}

    for student in students:
        files = student.get("files", {})
        for question, info in files.items():
            q_type = info.get("type", "CLEAN")
            if question in question_counts:
                question_counts[question][q_type] = question_counts[question].get(q_type, 0) + 1

            if q_type == "SEEDED":
                mis_id = info.get("misconception_id")
                if mis_id:
                    misconception_counts[mis_id] = misconception_counts.get(mis_id, 0) + 1

    # Question distribution table
    console.print("\n[bold]Question Distribution:[/bold]")
    q_table = Table(show_header=True, header_style="bold")
    q_table.add_column("Question")
    q_table.add_column("SEEDED", justify="right")
    q_table.add_column("CLEAN", justify="right")
    q_table.add_column("Total", justify="right")

    total_seeded = 0
    total_clean = 0
    for q in ["Q1", "Q2", "Q3", "Q4"]:
        seeded = question_counts[q]["SEEDED"]
        clean = question_counts[q]["CLEAN"]
        total_seeded += seeded
        total_clean += clean
        q_table.add_row(q, str(seeded), str(clean), str(seeded + clean))

    q_table.add_row(
        "[bold]Total[/bold]",
        f"[bold]{total_seeded}[/bold]",
        f"[bold]{total_clean}[/bold]",
        f"[bold]{total_seeded + total_clean}[/bold]",
    )
    console.print(q_table)

    # Misconception distribution table
    console.print(f"\n[bold]Misconception Distribution ({len(misconception_counts)} types):[/bold]")
    m_table = Table(show_header=True, header_style="bold")
    m_table.add_column("ID")
    m_table.add_column("Name")
    m_table.add_column("Category")
    m_table.add_column("Count", justify="right")
    m_table.add_column("%", justify="right")

    # Sort by count descending
    sorted_miscns = sorted(misconception_counts.items(), key=lambda x: x[1], reverse=True)
    for mis_id, count in sorted_miscns:
        gt_entry = gt_map.get(mis_id, {})
        name = gt_entry.get("name", mis_id)
        category = gt_entry.get("category", "Unknown")
        pct = (count / total_seeded * 100) if total_seeded > 0 else 0
        m_table.add_row(mis_id, name[:30], category[:25], str(count), f"{pct:.1f}%")

    console.print(m_table)

    # Summary stats
    seeded_pct = (
        (total_seeded / (total_seeded + total_clean) * 100)
        if (total_seeded + total_clean) > 0
        else 0
    )
    console.print("\n[bold green]Summary:[/bold green]")
    console.print(f"  Seeded questions: {total_seeded} ({seeded_pct:.1f}%)")
    console.print(f"  Clean questions: {total_clean}")
    console.print(f"  Unique misconception types: {len(misconception_counts)}")


if __name__ == "__main__":
    app()
