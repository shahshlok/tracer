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
from dataclasses import dataclass
from datetime import datetime, timezone
from enum import Enum
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
from utils.matching.fuzzy import fuzzy_match_misconception
from utils.matching.semantic import semantic_match_misconception
from utils.matching.classifier import MatchResult


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


def dispatch_matcher(
    detection: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    match_mode: MatchMode,
    gt_embeddings: dict[str, list[float]] | None = None,
) -> MatchResult_:
    """
    Dispatch to the appropriate matcher based on match_mode.
    Returns a unified MatchResult_ regardless of which matcher is used.
    """
    name = detection.get("name", "")
    description = detection.get("description", "")
    student_belief = detection.get("student_belief", "")
    
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
            {**detection, "topic": detection.get("topic", "")},
            groundtruth,
            gt_embeddings=gt_embeddings,
        )
        return MatchResult_(result.matched_id, result.score, result.detail)
    
    else:
        raise ValueError(f"Unknown match mode: {match_mode}")

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
                expected_topic = gt_map.get(expected_id, {}).get("category", "") if expected_id else ""

                for model, payload in models.items():
                    mis_list = payload.get("misconceptions", []) or []
                    has_tp = False

                    for mis in mis_list:
                        match_result = dispatch_matcher(
                            mis, groundtruth, current_mode, gt_embeddings
                        )

                        if is_clean:
                            result = MatchResult.INTERESTING if match_result.matched_id else MatchResult.FALSE_POSITIVE
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
                                "detected_name": mis.get("name", ""),
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
    fps = df[df["result"] == MatchResult.FALSE_POSITIVE.value].copy()
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


def plot_strategy_f1_comparison(metrics: pd.DataFrame, path: Path) -> Path:
    """Grouped bar chart: F1 by strategy, colored by model."""
    if metrics.empty:
        return path
    df = metrics.copy()
    df["model_short"] = df["model"].apply(lambda m: m.split("/")[-1])
    
    plt.figure(figsize=(10, 6))
    strategies = df["strategy"].unique()
    models = df["model_short"].unique()
    x = np.arange(len(strategies))
    width = 0.35
    
    colors = ["#2ecc71", "#3498db"]  # Green for GPT, Blue for Gemini
    for i, model in enumerate(models):
        model_data = df[df["model_short"] == model].set_index("strategy").reindex(strategies)
        bars = plt.bar(x + i * width, model_data["f1"], width, label=model, color=colors[i % len(colors)])
        # Add value labels on bars
        for bar, val in zip(bars, model_data["f1"]):
            if not np.isnan(val):
                plt.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 0.01, 
                        f'{val:.2f}', ha='center', va='bottom', fontsize=9)
    
    plt.xlabel("Strategy")
    plt.ylabel("F1 Score")
    plt.title("F1 Score by Strategy and Model")
    plt.xticks(x + width/2, strategies)
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
    models = df["model_short"].unique()
    strategies = df["strategy"].unique()
    colors = {"gpt-5.1": "#2ecc71", "gemini-2.5-flash": "#3498db"}
    markers = {"baseline": "o", "minimal": "s", "rubric_only": "^", "socratic": "D"}
    
    for _, row in df.iterrows():
        model = row["model_short"]
        strategy = row["strategy"]
        plt.scatter(row["recall"], row["precision"], 
                   c=colors.get(model, "#999"),
                   marker=markers.get(strategy, "o"),
                   s=150, alpha=0.8,
                   label=f"{strategy} / {model}")
    
    # Add diagonal lines for F1 iso-curves
    for f1 in [0.5, 0.6, 0.7, 0.8]:
        p = np.linspace(0.01, 1, 100)
        r = (f1 * p) / (2 * p - f1)
        valid = (r > 0) & (r <= 1)
        plt.plot(r[valid], p[valid], '--', color='gray', alpha=0.3, linewidth=1)
        # Label the iso-curve
        idx = np.argmin(np.abs(r - 0.9))
        if valid[idx]:
            plt.text(r[idx], p[idx], f'F1={f1}', fontsize=8, color='gray')
    
    plt.xlabel("Recall")
    plt.ylabel("Precision")
    plt.title("Precision-Recall Tradeoff by Strategy and Model")
    plt.xlim(0.4, 1.0)
    plt.ylim(0.4, 0.85)
    plt.legend(loc='lower left', fontsize=8)
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_topic_recall_bars(opportunities: pd.DataFrame, path: Path) -> Path:
    """Horizontal bar chart showing recall by topic, sorted by difficulty."""
    if opportunities.empty:
        return path
    
    topic_stats = opportunities.groupby("topic").agg(
        recall=("success", "mean"),
        n=("success", "count")
    ).reset_index().sort_values("recall")
    
    plt.figure(figsize=(10, 6))
    colors = plt.cm.RdYlGn(topic_stats["recall"])  # Red=low, Green=high
    bars = plt.barh(topic_stats["topic"], topic_stats["recall"], color=colors)
    
    # Add count labels
    for bar, n in zip(bars, topic_stats["n"]):
        plt.text(bar.get_width() + 0.02, bar.get_y() + bar.get_height()/2,
                f'n={int(n)}', va='center', fontsize=9, color='gray')
    
    plt.xlabel("Recall")
    plt.ylabel("Topic")
    plt.title("Detection Recall by Topic (sorted by difficulty)")
    plt.xlim(0, 1.15)
    plt.axvline(x=0.5, color='gray', linestyle='--', alpha=0.5, label='50% threshold')
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_model_comparison(metrics: pd.DataFrame, path: Path) -> Path:
    """Side-by-side comparison of GPT vs Gemini on P/R/F1 (averaged across strategies)."""
    if metrics.empty:
        return path
    
    df = metrics.copy()
    df["model_short"] = df["model"].apply(lambda m: m.split("/")[-1])
    
    # Average across strategies
    model_avg = df.groupby("model_short")[["precision", "recall", "f1"]].mean().reset_index()
    
    plt.figure(figsize=(8, 6))
    x = np.arange(3)  # precision, recall, f1
    width = 0.35
    
    colors = ["#2ecc71", "#3498db"]
    for i, (_, row) in enumerate(model_avg.iterrows()):
        values = [row["precision"], row["recall"], row["f1"]]
        bars = plt.bar(x + i * width, values, width, label=row["model_short"], color=colors[i])
        for bar, val in zip(bars, values):
            plt.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 0.01,
                    f'{val:.2f}', ha='center', va='bottom', fontsize=10, fontweight='bold')
    
    plt.ylabel("Score")
    plt.title("Model Comparison (averaged across strategies)")
    plt.xticks(x + width/2, ["Precision", "Recall", "F1"])
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
    
    colors = {"fuzzy_only": "#e74c3c", "semantic_only": "#3498db", "hybrid": "#2ecc71"}
    
    # Plot precision, recall, F1 side by side for each matcher
    for i, metric in enumerate(["precision", "recall", "f1"]):
        values = matcher_avg[metric].values
        bars = plt.bar(x + i * width, values, width, label=metric.capitalize(),
                      color=[colors.get(m, "#999") for m in matcher_avg["match_mode"]] if i == 0 else None,
                      alpha=0.8 if i == 0 else 0.6 + i * 0.15)
        for bar, val in zip(bars, values):
            plt.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 0.01,
                    f'{val:.2f}', ha='center', va='bottom', fontsize=9)
    
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
    
    for match_mode in metrics["match_mode"].unique():
        subset = metrics[metrics["match_mode"] == match_mode]
        plt.scatter(
            subset["recall"], subset["precision"],
            c=colors.get(match_mode, "#999"),
            marker=markers.get(match_mode, "o"),
            s=100, alpha=0.7,
            label=match_mode
        )
    
    # Add F1 iso-curves
    for f1 in [0.4, 0.5, 0.6, 0.7]:
        p = np.linspace(0.01, 1, 100)
        r = (f1 * p) / (2 * p - f1)
        valid = (r > 0) & (r <= 1)
        plt.plot(r[valid], p[valid], '--', color='gray', alpha=0.3, linewidth=1)
    
    plt.xlabel("Recall")
    plt.ylabel("Precision")
    plt.title("Precision-Recall by Matcher (each point = strategy × model)")
    plt.xlim(0.3, 1.0)
    plt.ylim(0.3, 0.85)
    plt.legend(title="Matcher")
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(path, dpi=200)
    plt.close()
    return path


def plot_matcher_strategy_grid(metrics: pd.DataFrame, path: Path) -> Path:
    """Grouped bar chart: F1 by strategy, grouped by matcher."""
    if "match_mode" not in metrics.columns:
        return path
    
    # Aggregate across models (average GPT + Gemini)
    agg = metrics.groupby(["match_mode", "strategy"]).agg({
        "f1": "mean"
    }).reset_index()
    
    fig, ax = plt.subplots(figsize=(10, 6))
    
    strategies = ["baseline", "minimal", "rubric_only", "socratic"]
    matchers = ["fuzzy_only", "semantic_only", "hybrid"]
    x = np.arange(len(strategies))
    width = 0.25
    
    colors = {"fuzzy_only": "#e74c3c", "semantic_only": "#3498db", "hybrid": "#2ecc71"}
    
    for i, matcher in enumerate(matchers):
        data = agg[agg["match_mode"] == matcher]
        vals = [data[data["strategy"] == s]["f1"].values[0] if len(data[data["strategy"] == s]) > 0 else 0 for s in strategies]
        ax.bar(x + i*width, vals, width, label=matcher, color=colors[matcher])
    
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


# ---------------------------------------------------------------------------
# Reporting
# ---------------------------------------------------------------------------
def render_metrics_table(metrics: pd.DataFrame, ci: pd.DataFrame | None, include_match_mode: bool = False) -> str:
    merged = metrics.copy()
    
    # Determine merge columns based on whether match_mode is present
    if include_match_mode and "match_mode" in metrics.columns:
        merge_cols = ["match_mode", "strategy", "model"]
        header = "| Matcher | Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (F1) |"
        separator = "|---------|----------|-------|----|----|----|-----------|--------|----|---------|"
    else:
        merge_cols = ["strategy", "model"]
        header = "| Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (Precision) | CI (Recall) | CI (F1) |"
        separator = "|----------|-------|----|----|----|-----------|--------|----|----------------|-------------|---------|"
    
    if ci is not None and not ci.empty:
        merged = merged.merge(ci, on=merge_cols, how="left")
    
    lines = [header, separator]
    
    for _, row in merged.iterrows():
        f1_ci = f"{row['f1_lo']:.2f}–{row['f1_hi']:.2f}" if "f1_lo" in row and not pd.isna(row.get("f1_lo")) else "-"
        
        if include_match_mode and "match_mode" in row:
            lines.append(
                f"| {row['match_mode']} | {row['strategy']} | {row['model'].split('/')[-1]} | "
                f"{int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} | {f1_ci} |"
            )
        else:
            prec_ci = f"{row['precision_lo']:.2f}–{row['precision_hi']:.2f}" if "precision_lo" in row and not pd.isna(row.get("precision_lo")) else "-"
            rec_ci = f"{row['recall_lo']:.2f}–{row['recall_hi']:.2f}" if "recall_lo" in row and not pd.isna(row.get("recall_lo")) else "-"
            lines.append(
                f"| {row['strategy']} | {row['model'].split('/')[-1]} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} | {prec_ci} | {rec_ci} | {f1_ci} |"
            )
    return "\n".join(lines)


def render_ablation_summary(metrics: pd.DataFrame) -> str:
    """Render a summary table comparing matchers averaged across strategies and models."""
    if "match_mode" not in metrics.columns:
        return ""
    
    summary = metrics.groupby("match_mode").agg({
        "tp": "sum",
        "fp": "sum",
        "fn": "sum",
        "precision": "mean",
        "recall": "mean",
        "f1": "mean"
    }).reset_index()
    
    lines = [
        "| Matcher | Total TP | Total FP | Total FN | Avg Precision | Avg Recall | Avg F1 |",
        "|---------|----------|----------|----------|---------------|------------|--------|"
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
    opportunities: pd.DataFrame, 
    groundtruth: list[dict[str, Any]]
) -> pd.DataFrame:
    """Compute recall per misconception ID."""
    if opportunities.empty:
        return pd.DataFrame()
    
    gt_map = {g["id"]: g for g in groundtruth}
    
    stats = opportunities.groupby("expected_id").agg(
        recall=("success", "mean"),
        n=("success", "count")
    ).reset_index()
    
    # Add names and categories from groundtruth
    stats["name"] = stats["expected_id"].map(
        lambda x: gt_map.get(x, {}).get("misconception_name", x)
    )
    stats["category"] = stats["expected_id"].map(
        lambda x: gt_map.get(x, {}).get("category", "")
    )
    
    return stats.sort_values("recall")


def render_misconception_table(stats: pd.DataFrame) -> str:
    """Render markdown table of per-misconception recall."""
    if stats.empty:
        return "_No misconception data available_"
    
    lines = [
        "| ID | Misconception | Category | Recall | N |",
        "|----|---------------|----------|--------|---|"
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
        plt.text(bar.get_width() + 0.02, bar.get_y() + bar.get_height()/2,
                f'n={int(n)}', va='center', fontsize=8, color='gray')
    
    plt.xlabel("Recall")
    plt.ylabel("Misconception")
    plt.title("Detection Recall by Misconception (sorted by difficulty)")
    plt.xlim(0, 1.15)
    plt.axvline(x=0.5, color='gray', linestyle='--', alpha=0.5)
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
        f"- **Assignment:** A2 – Kinematics & Geometry (CS1)",
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
        f"- **Embedding model:** text-embedding-3-large (OpenAI)",
        f"- **Detection models:** GPT-5.1, Gemini 2.5 Flash",
        f"- **Strategies:** baseline, minimal, rubric_only, socratic",
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
            opp_count = len(opportunities[opportunities["match_mode"] == "hybrid"]) if "match_mode" in opportunities.columns else len(opportunities)
        else:
            opp_count = len(opportunities)
        
        report.append(render_dataset_summary(
            dataset_summary, 
            manifest_meta, 
            match_mode if not is_ablation else "all (ablation)",
            opp_count
        ))
    
    # Executive highlights differ for ablation vs single-matcher
    if is_ablation:
        report.extend([
            "## Executive Highlights",
            "- **Matcher Ablation Study**: Comparing fuzzy_only, semantic_only, and hybrid matchers.",
            "- Bootstrap CIs included for statistical rigor.",
            "- Same detection data, different matching strategies.",
            "",
        ])
    else:
        report.extend([
            "## Executive Highlights",
            "- Hybrid matcher (fuzzy + semantic + topic prior) applied across all strategies/models.",
            "- Bootstrap CIs included for statistical rigor.",
            "",
        ])
    
    # Ablation section (only when running all matchers)
    if is_ablation:
        report.extend([
            "## Matcher Ablation: Fuzzy vs Semantic vs Hybrid",
            "",
            "### Summary (averaged across strategies and models)",
            render_ablation_summary(metrics),
            "",
            f"![Matcher Ablation]({asset_paths.get('matcher_ablation', '')})" if asset_paths.get("matcher_ablation") else "",
            "",
            "### Precision-Recall by Matcher",
            f"![Matcher PR Scatter]({asset_paths.get('matcher_pr_scatter', '')})" if asset_paths.get("matcher_pr_scatter") else "",
            "",
            "### Matcher × Strategy Distribution",
            f"![Matcher Strategy Grid]({asset_paths.get('matcher_strategy_grid', '')})" if asset_paths.get("matcher_strategy_grid") else "",
            "",
            "### Full Results Table",
            render_metrics_table(metrics, ci, include_match_mode=True),
            "",
        ])
    else:
        # Non-ablation: standard report structure
        report.extend([
            "## Model Comparison Overview",
            f"![Model Comparison]({asset_paths.get('model_comparison', '')})" if asset_paths.get("model_comparison") else "",
            "",
            "## Strategy × Model Performance",
            render_metrics_table(metrics, ci),
            "",
            f"![F1 by Strategy]({asset_paths.get('strategy_f1', '')})" if asset_paths.get("strategy_f1") else "",
            "",
            "### Precision-Recall Tradeoff",
            f"![Precision-Recall Scatter]({asset_paths.get('pr_scatter', '')})" if asset_paths.get("pr_scatter") else "",
            "",
        ])
    
    # Topic analysis (filter to hybrid for ablation, or use all)
    if is_ablation:
        hybrid_opps = opportunities[opportunities["match_mode"] == "hybrid"]
        topic_opps = hybrid_opps if not hybrid_opps.empty else opportunities
    else:
        topic_opps = opportunities
    
    report.extend([
        "## Topic Difficulty (Recall)",
        render_topic_table(topic_opps),
        "",
        f"![Topic Recall]({asset_paths.get('topic_bars', '')})" if asset_paths.get("topic_bars") else "",
        "",
        "## Topic Heatmap",
        f"![Topic Heatmap]({asset_paths.get('heatmap', '')})" if asset_paths.get("heatmap") else "_No heatmap generated_",
        "",
    ])
    
    # Per-misconception analysis section
    if misconception_stats is not None and not misconception_stats.empty:
        report.extend([
            "## Per-Misconception Detection Rates",
            "",
            "Detection recall for each seeded misconception, sorted by difficulty (hardest to detect at top):",
            "",
            render_misconception_table(misconception_stats),
            "",
            f"![Misconception Recall]({asset_paths.get('misconception_recall', '')})" if asset_paths.get("misconception_recall") else "",
            "",
        ])
    
    report.extend([
        "## Hallucination Analysis",
        f"![Hallucinations]({asset_paths.get('hallucinations', '')})" if asset_paths.get("hallucinations") else "",
        "",
        render_hallucination_snippets(detections),
        "",
        "## Methods",
        "- Data: 60 students × 4 questions (seeded/clean) with manifest-driven ground truth.",
        "- Detection: GPT-5.1 and Gemini 2.5 Flash across strategies (baseline, minimal, rubric_only, socratic).",
    ])
    
    if is_ablation:
        report.append("- Matching: Ablation comparing fuzzy-only, semantic-only (text-embedding-3-large), and hybrid (fuzzy + semantic + topic prior).")
    else:
        report.append("- Matching: Hybrid fusion of fuzzy similarity, semantic embeddings, and topic priors.")
    
    report.append("- Metrics: Precision/Recall/F1 with bootstrap CIs; agreement via κ; significance via McNemar where applicable.")

    # Agreement & significance (use hybrid for ablation)
    if is_ablation:
        opps_for_agreement = opportunities[opportunities["match_mode"] == "hybrid"]
    else:
        opps_for_agreement = opportunities
    
    agreements = []
    for strategy, grp in opps_for_agreement.groupby("strategy"):
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
        row_data.extend([
            row["strategy"],
            row["model"].split("/")[-1],
            str(int(row["tp"])),
            str(int(row["fp"])),
            str(int(row["fn"])),
            f"{row['precision']:.3f}",
            f"{row['recall']:.3f}",
            f"{row['f1']:.3f}",
        ])
        table.add_row(*row_data)
    console.print(table)


# ---------------------------------------------------------------------------
# CLI entrypoint
# ---------------------------------------------------------------------------
@app.command()
def main(
    detections_dir: Path = typer.Option(DEFAULT_DETECTIONS_DIR, help="Detections root", show_default=True),
    manifest_path: Path = typer.Option(DEFAULT_MANIFEST_PATH, help="Manifest path", show_default=True),
    groundtruth_path: Path = typer.Option(DEFAULT_GROUNDTRUTH_PATH, help="Ground truth path", show_default=True),
    match_mode: MatchMode = typer.Option(MatchMode.HYBRID, help="Matching mode: fuzzy_only, semantic_only, hybrid, or all"),
    quick: bool = typer.Option(False, help="Quick mode (fewer bootstrap iterations)"),
):
    """
    Analyze LLM misconception detections with configurable matching modes.
    
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
        iters=150 if quick else 400,
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

    report_text = generate_report(
        metrics, ci, opportunities_df, detections_df, asset_paths,
        misconception_stats=misconception_stats,
        dataset_summary=dataset_summary,
        manifest_meta=manifest_meta,
        match_mode=match_mode.value,
    )
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
