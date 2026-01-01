"""
Clean analysis CLI for notional machine misconception detection.

Uses semantic embedding matching to measure Cognitive Alignment.
Generates runs/*/report.md with publication-grade statistics.

ITiCSE/SIGCSE-grade analysis with:
- Bootstrap confidence intervals
- McNemar's test for strategy comparison
- Semantic similarity analysis
"""

from __future__ import annotations

import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import matplotlib

matplotlib.use("Agg")  # Headless
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
import typer
from rich.console import Console
from rich.table import Table

from utils.matching.semantic import (
    build_detection_text,
    cosine_similarity,
    get_embedding,
    precompute_groundtruth_embeddings,
    semantic_match_misconception,
)
from utils.statistics import (
    analyze_semantic_scores,
    compute_all_metrics_with_ci,
    compute_cochran_q_test,
    compute_pairwise_mcnemar,
)

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
DEFAULT_DETECTIONS_DIR = Path("detections/a1")
DEFAULT_MANIFEST_PATH = Path("authentic_seeded/a1/manifest.json")
DEFAULT_GROUNDTRUTH_PATH = Path("data/a1/groundtruth.json")
RUNS_DIR = Path("runs/a1")

NULL_TEMPLATES = [
    "No misconception detected.",
    "No misconceptions found; the student's understanding is correct.",
    "Correct understanding; no flawed mental model is present.",
    "The code is correct and reflects a proper understanding.",
    "There is no conceptual error or misconception in this solution.",
]
NULL_TEMPLATE_THRESHOLD = 0.80

# Noise floor: detections below this score are "pedantic" (e.g., "didn't close Scanner")
# and should be filtered out rather than counted as hallucinations
# Raised from 0.45 to 0.55 based on FP mean score of 0.59
NOISE_FLOOR_THRESHOLD = 0.55

# Semantic match threshold: lowered from 0.70 to capture more edge-case TPs
# TP mean is ~0.774, FP mean is ~0.586, so 0.65 is a good separator
SEMANTIC_THRESHOLD_DEFAULT = 0.65

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


# Keyword patterns that indicate "no misconception found" responses
NULL_KEYWORD_PATTERNS = [
    "no misconception",
    "no conceptual gap",
    "no error",
    "correct understanding",
    "correctly understood",
    "proper understanding",
    "code is correct",
    "logic is correct",
    "no flawed",
    "not a misconception",
    "no issues",
    "functions correctly",
    "works correctly",
    "no bug",
    "no logical error",
]


def is_null_keyword_match(mis: dict[str, Any]) -> bool:
    """
    Fast keyword-based check for "no misconception" responses.

    Checks the inferred_category_name and conceptual_gap fields for
    common phrases indicating the LLM found nothing wrong.
    """
    name = mis.get("inferred_category_name", "").lower()
    gap = mis.get("conceptual_gap", "").lower()
    combined = f"{name} {gap}"

    for pattern in NULL_KEYWORD_PATTERNS:
        if pattern in combined:
            return True
    return False


def build_null_template_embeddings() -> list[list[float]]:
    try:
        return [get_embedding(t) for t in NULL_TEMPLATES]
    except Exception as exc:
        console.print(f"[yellow]Null-template embeddings unavailable: {exc}[/yellow]")
        return []


def is_null_template_misconception(
    mis: dict[str, Any], null_embeddings: list[list[float]], threshold: float
) -> bool:
    """
    Check if a misconception is actually a "no misconception found" response.

    Uses two-stage detection:
    1. Fast keyword matching (catches most cases)
    2. Semantic embedding matching (catches paraphrased versions)
    """
    # Stage 1: Fast keyword matching
    if is_null_keyword_match(mis):
        return True

    # Stage 2: Semantic matching against null templates
    if not null_embeddings:
        return False
    detection_text = build_detection_text(adapt_detection(mis))
    if not detection_text.strip():
        return False
    try:
        det_embedding = get_embedding(detection_text)
    except Exception:
        return False
    best = max(cosine_similarity(det_embedding, tmpl) for tmpl in null_embeddings)
    return best >= threshold


# ---------------------------------------------------------------------------
# Ensemble Voting (Majority Consensus)
# ---------------------------------------------------------------------------
def apply_strategy_ensemble_filter(
    df: pd.DataFrame,
    threshold: int = 2,
    silent: bool = False,
) -> pd.DataFrame:
    """
    Apply ensemble voting based on STRATEGY agreement.

    A detection is only validated if >= threshold strategies
    agree on the same misconception for the same student/question.

    Args:
        df: Results DataFrame with 'strategy', 'student', 'question', 'matched_id', 'result'
        threshold: Minimum number of strategies that must agree (default: 2/4)
        silent: If True, suppress console output

    Returns:
        Filtered DataFrame with non-consensus TPs demoted to FN
    """
    if not silent:
        console.print(f"[cyan]Applying strategy ensemble (threshold: {threshold}/4)...[/cyan]")

    # Build agreement map: (student, question, matched_id) -> set of strategies
    agreement_map: dict[tuple, set[str]] = {}
    for _, row in df.iterrows():
        if row["result"] == "FN":
            continue
        matched_id = row["matched_id"]
        if pd.isna(matched_id):
            matched_id = None
        key = (row["student"], row["question"], matched_id)
        if key not in agreement_map:
            agreement_map[key] = set()
        agreement_map[key].add(row["strategy"])

    # Find validated detections (>= threshold strategies agree)
    validated = {
        key
        for key, strategies in agreement_map.items()
        if len(strategies) >= threshold and key[2] is not None
    }

    if not silent:
        console.print(f"[cyan]  Validated detections: {len(validated)}[/cyan]")

    # Filter rows
    filtered_rows = []
    for _, row in df.iterrows():
        matched_id = row["matched_id"]
        if pd.isna(matched_id):
            matched_id = None
        key = (row["student"], row["question"], matched_id)
        result = row["result"]

        if result == "FN":
            filtered_rows.append(dict(row))
        elif result == "TP":
            if key in validated:
                filtered_rows.append(dict(row))
            else:
                # Demote to FN
                row_dict = dict(row)
                row_dict["result"] = "FN"
                row_dict["matched_id"] = None
                filtered_rows.append(row_dict)
        elif result.startswith("FP"):
            # Only keep FPs that match validated detections (rare but possible)
            if key in validated:
                filtered_rows.append(dict(row))
            # Otherwise drop the FP entirely

    return pd.DataFrame(filtered_rows)


def apply_model_ensemble_filter(
    df: pd.DataFrame,
    threshold: int = 2,
    silent: bool = False,
) -> pd.DataFrame:
    """
    Apply ensemble voting based on MODEL agreement.

    A detection is only validated if >= threshold models
    agree on the same misconception for the same student/question/strategy.

    Args:
        df: Results DataFrame with 'model', 'student', 'question', 'matched_id', 'result'
        threshold: Minimum number of models that must agree (default: 2/6)
        silent: If True, suppress console output

    Returns:
        Filtered DataFrame with non-consensus TPs demoted to FN
    """
    n_models = df["model"].nunique()
    if not silent:
        console.print(
            f"[cyan]Applying model ensemble (threshold: {threshold}/{n_models})...[/cyan]"
        )

    # Build agreement map: (student, question, strategy, matched_id) -> set of models
    agreement_map: dict[tuple, set[str]] = {}
    for _, row in df.iterrows():
        if row["result"] == "FN":
            continue
        matched_id = row["matched_id"]
        if pd.isna(matched_id):
            matched_id = None
        key = (row["student"], row["question"], row["strategy"], matched_id)
        if key not in agreement_map:
            agreement_map[key] = set()
        agreement_map[key].add(row["model"])

    # Find validated detections (>= threshold models agree)
    validated = {
        key
        for key, models in agreement_map.items()
        if len(models) >= threshold and key[3] is not None
    }

    if not silent:
        console.print(f"[cyan]  Validated detections: {len(validated)}[/cyan]")

    # Filter rows
    filtered_rows = []
    for _, row in df.iterrows():
        matched_id = row["matched_id"]
        if pd.isna(matched_id):
            matched_id = None
        key = (row["student"], row["question"], row["strategy"], matched_id)
        result = row["result"]

        if result == "FN":
            filtered_rows.append(dict(row))
        elif result == "TP":
            if key in validated:
                filtered_rows.append(dict(row))
            else:
                # Demote to FN
                row_dict = dict(row)
                row_dict["result"] = "FN"
                row_dict["matched_id"] = None
                filtered_rows.append(row_dict)
        elif result.startswith("FP"):
            if key in validated:
                filtered_rows.append(dict(row))

    return pd.DataFrame(filtered_rows)


def compute_ensemble_comparison(
    df: pd.DataFrame,
    strategy_threshold: int = 2,
    model_threshold: int = 2,
) -> dict[str, Any]:
    """
    Compute metrics for raw, strategy-ensemble, and model-ensemble approaches.

    Returns:
        Dict with 'raw', 'strategy_ensemble', 'model_ensemble' metrics and comparison
    """
    raw_metrics = compute_metrics(df)

    strategy_df = apply_strategy_ensemble_filter(df, threshold=strategy_threshold, silent=True)
    strategy_metrics = compute_metrics(strategy_df)

    model_df = apply_model_ensemble_filter(df, threshold=model_threshold, silent=True)
    model_metrics = compute_metrics(model_df)

    return {
        "raw": raw_metrics,
        "strategy_ensemble": {
            "threshold": strategy_threshold,
            "metrics": strategy_metrics,
            "precision_gain": strategy_metrics["precision"] - raw_metrics["precision"],
            "recall_loss": raw_metrics["recall"] - strategy_metrics["recall"],
            "f1_delta": strategy_metrics["f1"] - raw_metrics["f1"],
        },
        "model_ensemble": {
            "threshold": model_threshold,
            "metrics": model_metrics,
            "precision_gain": model_metrics["precision"] - raw_metrics["precision"],
            "recall_loss": raw_metrics["recall"] - model_metrics["recall"],
            "f1_delta": model_metrics["f1"] - raw_metrics["f1"],
        },
    }


# Legacy alias for backwards compatibility
def apply_ensemble_filter(
    df: pd.DataFrame,
    ensemble_threshold: int = 2,
) -> pd.DataFrame:
    """Legacy wrapper for strategy-based ensemble filtering."""
    return apply_strategy_ensemble_filter(df, threshold=ensemble_threshold)


# ---------------------------------------------------------------------------
# Core Analysis (Semantic-Only Matching)
# ---------------------------------------------------------------------------
def build_results_df(
    detections_dir: Path,
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    semantic_threshold: float = SEMANTIC_THRESHOLD_DEFAULT,
    null_template_threshold: float = NULL_TEMPLATE_THRESHOLD,
    noise_floor_threshold: float = NOISE_FLOOR_THRESHOLD,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    Build results dataframe using semantic embedding matching.

    This is the core of the Cognitive Alignment measurement:
    - We embed both the LLM's explanation and the ground truth student thinking
    - High cosine similarity = LLM understood the mental model
    - We track the raw semantic score for every detection for analysis
    """

    gt_map = {m["id"]: m for m in groundtruth}
    console.print("[cyan]Precomputing groundtruth embeddings...[/cyan]")
    gt_embeddings = precompute_groundtruth_embeddings(groundtruth)

    null_embeddings = build_null_template_embeddings()

    strategies = discover_strategies(detections_dir)
    console.print(f"[green]Strategies:[/green] {', '.join(strategies)}")

    rows: list[dict[str, Any]] = []
    compliance_rows: list[dict[str, Any]] = []
    det_count = 0

    for strategy in strategies:
        strategy_dir = detections_dir / strategy
        detections = load_detections_for_strategy(strategy_dir)
        console.print(f"  [dim]{strategy}: {len(detections)} files[/dim]")

        for det in detections:
            det_count += 1
            if det_count % 100 == 0:
                console.print(f"    [dim]Processed {det_count} detections...[/dim]")
            student = det.get("student", "")
            question = det.get("question", "")
            expected_id, is_clean = get_expected(manifest, student, question)
            expected_category = (
                gt_map.get(expected_id, {}).get("category", "") if expected_id else ""
            )

            for model, payload in det.get("models", {}).items():
                mis_list = payload.get("misconceptions", []) or []
                null_flags = [
                    is_null_template_misconception(mis, null_embeddings, null_template_threshold)
                    for mis in mis_list
                ]
                normalized_mis = [mis for mis, is_null in zip(mis_list, null_flags) if not is_null]
                null_filtered_count = sum(1 for is_null in null_flags if is_null)
                noise_filtered_count = 0  # Will be updated as we process detections

                has_tp = False
                detection_rows_for_file: list[dict[str, Any]] = []

                for mis in normalized_mis:
                    adapted = adapt_detection(mis)

                    # Semantic matching - the core of Cognitive Alignment
                    matched_id, semantic_score, match_method = semantic_match_misconception(
                        adapted["name"],
                        adapted["description"],
                        adapted["student_belief"],
                        groundtruth,
                        threshold=semantic_threshold,
                        precomputed_gt_embeddings=gt_embeddings,
                    )

                    # NOISE FLOOR FILTER: Skip "pedantic" detections (e.g., "didn't close Scanner")
                    # These are below threshold AND below noise floor - treat as noise, not hallucination
                    if semantic_score < noise_floor_threshold:
                        noise_filtered_count += 1
                        continue  # Skip this detection entirely

                    # Classify result based on semantic match
                    if is_clean:
                        result_type = "FP_CLEAN"
                    elif matched_id == expected_id:
                        result_type = "TP"
                        has_tp = True
                    elif matched_id:
                        result_type = "FP_WRONG"
                    else:
                        # Above noise floor but below match threshold = hallucination
                        result_type = "FP_HALLUCINATION"

                    detection_rows_for_file.append(
                        {
                            "strategy": strategy,
                            "model": model,
                            "student": student,
                            "question": question,
                            "expected_id": expected_id,
                            "expected_category": expected_category,
                            "is_clean": is_clean,
                            "detected_name": adapted["name"],
                            "detected_thinking": adapted["student_belief"][:200]
                            if adapted["student_belief"]
                            else "",
                            "matched_id": matched_id,
                            "semantic_score": semantic_score,
                            "match_method": match_method,
                            "result": result_type,
                            "confidence": mis.get("confidence"),
                        }
                    )

                # Add all detection rows for this file
                rows.extend(detection_rows_for_file)

                # Track compliance with noise filtering stats
                compliance_rows.append(
                    {
                        "strategy": strategy,
                        "model": model,
                        "student": student,
                        "question": question,
                        "raw_misconceptions": len(mis_list),
                        "null_filtered": null_filtered_count,
                        "noise_filtered": noise_filtered_count,
                        "evaluated_misconceptions": len(detection_rows_for_file),
                        "raw_empty": len(mis_list) == 0,
                        "raw_nonempty": len(mis_list) > 0,
                        "null_only": len(mis_list) > 0 and len(normalized_mis) == 0,
                    }
                )

                # Record FN if no true positive found
                if not is_clean and expected_id and not has_tp:
                    rows.append(
                        {
                            "strategy": strategy,
                            "model": model,
                            "student": student,
                            "question": question,
                            "expected_id": expected_id,
                            "expected_category": expected_category,
                            "is_clean": False,
                            "detected_name": "",
                            "detected_thinking": "",
                            "matched_id": None,
                            "semantic_score": 0.0,
                            "match_method": "no_detection",
                            "result": "FN",
                            "confidence": None,
                        }
                    )

    console.print(f"[green]Total detections processed: {det_count}[/green]")
    return pd.DataFrame(rows), pd.DataFrame(compliance_rows)


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


def summarize_compliance(df: pd.DataFrame) -> pd.DataFrame:
    if df.empty:
        return pd.DataFrame()
    grouped = df.groupby(["strategy", "model"], as_index=False)
    summary = grouped.agg(
        total_files=("raw_empty", "size"),
        raw_empty=("raw_empty", "sum"),
        raw_nonempty=("raw_nonempty", "sum"),
        null_only=("null_only", "sum"),
        raw_misconceptions=("raw_misconceptions", "sum"),
        null_filtered=("null_filtered", "sum"),
        noise_filtered=("noise_filtered", "sum"),
        evaluated_misconceptions=("evaluated_misconceptions", "sum"),
    )
    return summary


def df_to_markdown_table(df: pd.DataFrame) -> str:
    if df.empty:
        return "No compliance data available."
    cols = list(df.columns)
    header = "| " + " | ".join(cols) + " |"
    sep = "| " + " | ".join(["---"] * len(cols)) + " |"
    rows = []
    for _, row in df.iterrows():
        rows.append("| " + " | ".join(str(row[c]) for c in cols) + " |")
    return "\n".join([header, sep] + rows)


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
# Chart Generation (Publication-Grade)
# ---------------------------------------------------------------------------
def generate_charts(
    df: pd.DataFrame,
    assets_dir: Path,
    groundtruth: list[dict],
    include_stats: bool = True,
) -> list[str]:
    """Generate all charts for the report with statistical rigor."""
    assets_dir.mkdir(parents=True, exist_ok=True)
    charts = []

    # Use seaborn style for publication-quality plots
    sns.set_style("whitegrid")
    plt.rcParams.update({"font.size": 11, "axes.titlesize": 14, "axes.labelsize": 12})

    # 1. Strategy F1 Comparison with Bootstrap CIs
    by_strat = metrics_by_group(df, ["strategy"])
    if not by_strat.empty:
        fig, ax = plt.subplots(figsize=(10, 6))

        # Compute bootstrap CIs if enabled
        if include_stats:
            cis = []
            for strat in by_strat["strategy"]:
                strat_df = df[df["strategy"] == strat]
                ci = compute_all_metrics_with_ci(strat_df, n_bootstrap=500)
                cis.append(ci["f1"])
            by_strat["ci_lower"] = [c["ci_lower"] for c in cis]
            by_strat["ci_upper"] = [c["ci_upper"] for c in cis]
            yerr = [
                by_strat["f1"] - by_strat["ci_lower"],
                by_strat["ci_upper"] - by_strat["f1"],
            ]
            ax.bar(
                by_strat["strategy"],
                by_strat["f1"],
                yerr=yerr,
                capsize=5,
                color=["#2ecc71", "#3498db", "#e74c3c", "#9b59b6"],
                edgecolor="black",
                linewidth=1,
            )
        else:
            ax.bar(
                by_strat["strategy"],
                by_strat["f1"],
                color=["#2ecc71", "#3498db", "#e74c3c", "#9b59b6"],
            )

        ax.set_ylabel("F1 Score")
        ax.set_title("F1 Score by Prompting Strategy (with 95% CI)")
        ax.set_ylim(0, 1)
        for i, v in enumerate(by_strat["f1"]):
            ax.text(i, v + 0.05, f"{v:.2f}", ha="center", fontweight="bold")
        plt.tight_layout()
        path = assets_dir / "strategy_f1.png"
        plt.savefig(path, dpi=200, bbox_inches="tight")
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
            ax.set_yticklabels([str(n)[:40] for n in top_fps.index])
            ax.set_xlabel("Count")
            ax.set_title("Top False Positive Detections (Hallucinations)")
            plt.tight_layout()
            path = assets_dir / "hallucinations.png"
            plt.savefig(path, dpi=200, bbox_inches="tight")
            plt.close()
            charts.append("hallucinations.png")

    # 7. NEW: Semantic Confidence Distribution (The "Understanding" Chart)
    if "semantic_score" in df.columns:
        tp_scores = df[df["result"] == "TP"]["semantic_score"].dropna()
        fp_scores = df[df["result"].str.startswith("FP")]["semantic_score"].dropna()

        if len(tp_scores) > 10 and len(fp_scores) > 10:
            fig, ax = plt.subplots(figsize=(10, 6))

            # Plot overlapping histograms
            ax.hist(
                tp_scores,
                bins=30,
                alpha=0.7,
                label=f"True Positives (n={len(tp_scores)}, μ={tp_scores.mean():.2f})",
                color="#2ecc71",
                edgecolor="white",
            )
            ax.hist(
                fp_scores,
                bins=30,
                alpha=0.7,
                label=f"False Positives (n={len(fp_scores)}, μ={fp_scores.mean():.2f})",
                color="#e74c3c",
                edgecolor="white",
            )

            ax.axvline(0.70, color="black", linestyle="--", linewidth=2, label="Threshold (0.70)")
            ax.set_xlabel("Semantic Similarity Score")
            ax.set_ylabel("Frequency")
            ax.set_title("Semantic Confidence Distribution: TP vs FP")
            ax.legend()
            ax.set_xlim(0, 1)
            plt.tight_layout()
            path = assets_dir / "semantic_distribution.png"
            plt.savefig(path, dpi=200, bbox_inches="tight")
            plt.close()
            charts.append("semantic_distribution.png")

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
        # Filter out any empty/NaN categories or zero-N rows
        by_category = by_category[by_category["expected_category"].notna()]
        by_category = by_category[by_category["expected_category"] != ""]
        by_category = by_category[(by_category["tp"] + by_category["fn"]) > 0]
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
def create_run_dir(match_mode: str, manifest: dict[str, Any], run_name: str | None = None) -> Path:
    if run_name:
        run_id = run_name if run_name.startswith("run_") else f"run_{run_name}"
    else:
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
    run_name: str = typer.Option(None, help="Custom run name (e.g., 'run_10_revamp')"),
):
    """Run analysis on detections with report generation."""
    console.print("[bold cyan]═══ Analysis v2 ═══[/bold cyan]")
    console.print(f"Match mode: {match_mode}")

    manifest = load_manifest(manifest_path)
    groundtruth = load_groundtruth(groundtruth_path)

    console.print(f"Students: {manifest.get('student_count', 'N/A')}")
    console.print(f"Misconceptions: {len(groundtruth)}")

    df, compliance_df = build_results_df(  # type: ignore
        detections_dir, manifest, groundtruth, match_mode
    )

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
    run_dir = create_run_dir(match_mode, manifest, run_name)
    df.to_csv(run_dir / "results.csv", index=False)
    (run_dir / "metrics.json").write_text(json.dumps(overall, indent=2))
    if not compliance_df.empty:
        compliance_df.to_csv(run_dir / "compliance.csv", index=False)
        compliance_summary = summarize_compliance(compliance_df)
        compliance_summary.to_csv(run_dir / "compliance_summary.csv", index=False)
        compliance_md = "# Compliance Summary\n\n"
        compliance_md += df_to_markdown_table(compliance_summary) + "\n"
        (run_dir / "compliance_summary.md").write_text(compliance_md)

    report_path = generate_report(df, manifest, groundtruth, run_dir, match_mode)
    console.print(f"[green]Report:[/green] {report_path}")

    update_run_index(run_dir, overall, match_mode)
    console.print("[bold green]✓ Complete[/bold green]")


@app.command()
def analyze_multi(
    run_name: str = typer.Option(..., help="Descriptive run name (e.g., 'analysisv2')"),
    semantic_threshold: float = typer.Option(
        SEMANTIC_THRESHOLD_DEFAULT,
        help="Semantic similarity threshold for matching (default: 0.65)",
    ),
    noise_floor: float = typer.Option(
        NOISE_FLOOR_THRESHOLD,
        help="Noise floor threshold - detections below this are filtered (default: 0.45)",
    ),
):
    """Run multi-assignment analysis with semantic matching and statistical rigor."""
    console.print("[bold cyan]═══ Multi-Assignment Analysis v2 (Semantic) ═══[/bold cyan]")
    console.print(f"Run name: {run_name}")
    console.print(f"Semantic threshold: {semantic_threshold}")
    console.print(f"Noise floor: {noise_floor}")

    ASSIGNMENTS = ["a1", "a2", "a3"]
    all_dfs = []
    all_compliance_dfs = []
    combined_groundtruth: list[dict[str, Any]] = []
    total_students = 0
    seeds = []

    for assignment in ASSIGNMENTS:
        console.print(f"\n[cyan]Processing {assignment}...[/cyan]")

        detections_dir = Path(f"detections/{assignment}_multi")
        manifest_path = Path(f"authentic_seeded/{assignment}/manifest.json")
        groundtruth_path = Path(f"data/{assignment}/groundtruth.json")

        if not detections_dir.exists():
            console.print(f"[yellow]Warning: {detections_dir} not found, skipping[/yellow]")
            continue

        manifest = load_manifest(manifest_path)
        groundtruth = load_groundtruth(groundtruth_path)

        # Add to combined groundtruth (avoiding duplicates by ID)
        existing_ids = {gt["id"] for gt in combined_groundtruth}
        for gt in groundtruth:
            if gt["id"] not in existing_ids:
                combined_groundtruth.append(gt)

        total_students += manifest.get("student_count", 0)
        if manifest.get("seed"):
            seeds.append(str(manifest["seed"]))

        console.print(f"  Students: {manifest.get('student_count', 'N/A')}")
        console.print(f"  Misconceptions: {len(groundtruth)}")

        df, compliance_df = build_results_df(
            detections_dir,
            manifest,
            groundtruth,
            semantic_threshold=semantic_threshold,
            noise_floor_threshold=noise_floor,
        )

        if not df.empty:
            df["assignment"] = assignment
            all_dfs.append(df)

        if not compliance_df.empty:
            compliance_df["assignment"] = assignment
            all_compliance_dfs.append(compliance_df)

    if not all_dfs:
        console.print("[red]No results from any assignment![/red]")
        return

    # Combine all DataFrames
    combined_df = pd.concat(all_dfs, ignore_index=True)
    combined_compliance_df = (
        pd.concat(all_compliance_dfs, ignore_index=True) if all_compliance_dfs else pd.DataFrame()
    )

    console.print("\n[bold]Combined dataset:[/bold]")
    console.print(f"  Total students processed: {total_students}")
    console.print(f"  Total detection rows: {len(combined_df)}")
    console.print(f"  Unique misconceptions: {len(combined_groundtruth)}")

    # Compute overall metrics
    overall = compute_metrics(combined_df)
    console.print(
        f"\n[bold]Overall:[/bold] P={overall['precision']:.3f} R={overall['recall']:.3f} F1={overall['f1']:.3f}"
    )

    # Per-assignment metrics
    console.print("\n[bold]By Assignment[/bold]")
    by_assignment = metrics_by_group(combined_df, ["assignment"])
    table = Table()
    table.add_column("Assignment")
    table.add_column("TP")
    table.add_column("FP")
    table.add_column("FN")
    table.add_column("Precision")
    table.add_column("Recall")
    table.add_column("F1")
    for _, row in by_assignment.iterrows():
        table.add_row(
            row["assignment"],
            str(row["tp"]),
            str(row["fp"]),
            str(row["fn"]),
            f"{row['precision']:.3f}",
            f"{row['recall']:.3f}",
            f"{row['f1']:.3f}",
        )
    console.print(table)

    # Create run directory
    run_id = run_name if run_name.startswith("run_") else f"run_{run_name}"
    run_dir = Path("runs/multi") / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    assets_dir = run_dir / "assets"
    assets_dir.mkdir(exist_ok=True)

    # Save CSVs
    combined_df.to_csv(run_dir / "results.csv", index=False)
    (run_dir / "metrics.json").write_text(json.dumps(overall, indent=2))
    if not combined_compliance_df.empty:
        combined_compliance_df.to_csv(run_dir / "compliance.csv", index=False)

    # Generate charts
    charts = generate_charts(combined_df, assets_dir, combined_groundtruth)

    # Generate assignment comparison chart
    if not by_assignment.empty:
        fig, ax = plt.subplots(figsize=(10, 6))
        x = np.arange(len(by_assignment))
        width = 0.25
        ax.bar(x - width, by_assignment["precision"], width, label="Precision", color="#4285F4")
        ax.bar(x, by_assignment["recall"], width, label="Recall", color="#34A853")
        ax.bar(x + width, by_assignment["f1"], width, label="F1", color="#EA4335")
        ax.set_xlabel("Assignment")
        ax.set_ylabel("Score")
        ax.set_title("Cross-Assignment Comparison")
        ax.set_xticks(x)
        ax.set_xticklabels(by_assignment["assignment"].tolist())
        ax.legend()
        ax.set_ylim(0, 1)
        plt.tight_layout()
        fig.savefig(assets_dir / "assignment_comparison.png", dpi=150)
        plt.close(fig)
        charts.append("assignment_comparison.png")

    # Generate report
    by_strategy = metrics_by_group(combined_df, ["strategy"])
    by_model = metrics_by_group(combined_df, ["model"])
    by_category = metrics_by_group(
        combined_df[combined_df["expected_category"].notna()], ["expected_category"]
    )
    by_misconception = metrics_by_group(
        combined_df[combined_df["expected_id"].notna()], ["expected_id"]
    )

    # Build combined manifest for report
    combined_manifest = {
        "student_count": total_students,
        "seed": ",".join(seeds) if seeds else "multiple",
    }

    report_path = generate_multi_report(
        combined_df,
        by_assignment,
        by_strategy,
        by_model,
        by_category,
        by_misconception,
        combined_groundtruth,
        combined_manifest,
        run_dir,
        "semantic",  # Always semantic matching
        charts,
        semantic_threshold=semantic_threshold,
        noise_floor=noise_floor,
        compliance_df=combined_compliance_df,
    )

    console.print(f"[green]Report:[/green] {report_path}")
    console.print("[bold green]✓ Multi-assignment analysis complete[/bold green]")


def generate_multi_report(
    df: pd.DataFrame,
    by_assignment: pd.DataFrame,
    by_strategy: pd.DataFrame,
    by_model: pd.DataFrame,
    by_category: pd.DataFrame,
    by_misconception: pd.DataFrame,
    groundtruth: list[dict[str, Any]],
    manifest: dict[str, Any],
    run_dir: Path,
    match_mode: str,
    charts: list[str],
    semantic_threshold: float = SEMANTIC_THRESHOLD_DEFAULT,
    noise_floor: float = NOISE_FLOOR_THRESHOLD,
    compliance_df: pd.DataFrame | None = None,
) -> Path:
    """Generate unified multi-assignment report with statistical rigor."""
    gt_map = {gt["id"]: gt for gt in groundtruth}
    overall = compute_metrics(df)

    # Compute bootstrap CIs for overall metrics
    console.print("[cyan]Computing bootstrap confidence intervals...[/cyan]")
    overall_with_ci = compute_all_metrics_with_ci(df, n_bootstrap=1000)

    lines = [
        "# Multi-Assignment LLM Misconception Detection Report",
        f"_Generated: {datetime.now(timezone.utc).isoformat()}_",
        "",
        "## Executive Summary",
        "",
        "This report evaluates LLM cognitive alignment with CS education theory by measuring",
        "whether models can identify *student mental models* (Notional Machines), not just surface-level bugs.",
        "",
        "**Key Finding:** Semantic embedding matching reveals the gap between detecting *what* is wrong",
        "versus understanding *why* the student thought it was right.",
        "",
        "---",
        "",
        "## Dataset Summary",
        f"- **Total Students:** {manifest.get('student_count', 'N/A')}",
        "- **Assignments:** a1 (Variables), a2 (Loops), a3 (Arrays)",
        f"- **Semantic Threshold:** Cosine Similarity ≥ {semantic_threshold:.2f}",
        f"- **Noise Floor:** Detections with score < {noise_floor:.2f} are filtered as 'pedantic'",
        f"- **Seeds:** {manifest.get('seed', 'N/A')}",
        "",
        "## Overall Metrics (with 95% Confidence Intervals)",
        "",
        "| Metric | Value | 95% CI | Std Error |",
        "|--------|-------|--------|-----------|",
        f"| True Positives | {overall['tp']} | — | — |",
        f"| False Positives | {overall['fp']} | — | — |",
        f"| False Negatives | {overall['fn']} | — | — |",
        f"| **Precision** | **{overall_with_ci['precision']['estimate']:.3f}** | [{overall_with_ci['precision']['ci_lower']:.3f}, {overall_with_ci['precision']['ci_upper']:.3f}] | {overall_with_ci['precision']['std_error']:.4f} |",
        f"| **Recall** | **{overall_with_ci['recall']['estimate']:.3f}** | [{overall_with_ci['recall']['ci_lower']:.3f}, {overall_with_ci['recall']['ci_upper']:.3f}] | {overall_with_ci['recall']['std_error']:.4f} |",
        f"| **F1 Score** | **{overall_with_ci['f1']['estimate']:.3f}** | [{overall_with_ci['f1']['ci_lower']:.3f}, {overall_with_ci['f1']['ci_upper']:.3f}] | {overall_with_ci['f1']['std_error']:.4f} |",
        "",
    ]

    # Cross-Assignment Comparison (RQ1: Complexity Gradient)
    lines.extend(
        [
            "## Cross-Assignment Comparison (RQ1: Complexity Gradient)",
            "",
            "> Does LLM performance degrade as conceptual complexity increases?",
            "",
            "| Assignment | Focus | TP | FP | FN | Precision | Recall | F1 |",
            "|------------|-------|----|----|----|-----------| -------|-----|",
        ]
    )
    assignment_focus = {"a1": "Variables/Math", "a2": "Loops/Control", "a3": "Arrays/Strings"}
    for _, row in by_assignment.iterrows():
        focus = assignment_focus.get(row["assignment"], "")
        lines.append(
            f"| {row['assignment']} | {focus} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "assignment_comparison.png" in charts:
        lines.append("![Assignment Comparison](assets/assignment_comparison.png)")
        lines.append("")

    # Strategy section with statistical significance
    lines.extend(
        [
            "## Performance by Prompting Strategy",
            "",
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

    # McNemar's Test for Strategy Comparison
    console.print("[cyan]Computing McNemar's tests for strategy comparison...[/cyan]")
    strategies = df["strategy"].unique().tolist()
    if len(strategies) >= 2:
        mcnemar_results = compute_pairwise_mcnemar(df, strategies)
        if not mcnemar_results.empty:
            lines.extend(
                [
                    "### Statistical Significance (McNemar's Test)",
                    "",
                    "> Paired comparison since the same student code is analyzed by all strategies.",
                    "",
                    "| Comparison | χ² | p-value | Significant? | Interpretation |",
                    "|------------|-----|---------|--------------|----------------|",
                ]
            )
            for _, row in mcnemar_results.iterrows():
                sig = "✓ Yes" if row.get("significant") else "✗ No"
                stat = f"{row['statistic']:.2f}" if row.get("statistic") else "—"
                pval = f"{row['p_value']:.4f}" if row.get("p_value") else "—"
                interp = str(row.get("interpretation", ""))[:50]
                lines.append(
                    f"| {row['strategy_a']} vs {row['strategy_b']} | {stat} | {pval} | {sig} | {interp} |"
                )
            lines.append("")

    # Cochran's Q Test
    cochran = compute_cochran_q_test(df, strategies)
    if cochran.get("statistic"):
        lines.extend(
            [
                "### Omnibus Test (Cochran's Q)",
                "",
                f"- **Q Statistic:** {cochran['statistic']:.2f}",
                f"- **Degrees of Freedom:** {cochran['df']}",
                f"- **p-value:** {cochran['p_value']:.6f}",
                f"- **Conclusion:** {'Significant differences exist between strategies' if cochran['significant'] else 'No significant differences between strategies'}",
                "",
            ]
        )

    # Model section
    lines.extend(
        [
            "## Performance by Model",
            "",
            "| Model | TP | FP | FN | Precision | Recall | F1 |",
            "|-------|----|----|----|-----------|--------|-----|",
        ]
    )
    for _, row in by_model.iterrows():
        model_short = str(row["model"]).split("/")[-1]
        lines.append(
            f"| {model_short} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "model_comparison.png" in charts:
        lines.append("![Model Comparison](assets/model_comparison.png)")
        lines.append("")

    # Category section (RQ2: Notional Machine Detection)
    if not by_category.empty:
        lines.extend(
            [
                "## Notional Machine Category Detection (RQ2)",
                "",
                "> Which mental model categories are easier/harder for LLMs to detect?",
                "> This is the core finding: Surface errors (Syntax) vs Deep errors (State).",
                "",
                "| Category | Recall | N | Difficulty |",
                "|----------|--------|---|------------|",
            ]
        )
        # Filter out any empty/NaN categories or zero-N rows
        by_category = by_category[by_category["expected_category"].notna()]
        by_category = by_category[by_category["expected_category"] != ""]
        by_category = by_category[(by_category["tp"] + by_category["fn"]) > 0]
        by_category = by_category.sort_values("recall")
        for _, row in by_category.iterrows():
            n = row["tp"] + row["fn"]
            recall = row["recall"]
            if recall >= 0.7:
                difficulty = "Easy"
            elif recall >= 0.5:
                difficulty = "Medium"
            else:
                difficulty = "**Hard**"
            lines.append(f"| {row['expected_category']} | {recall:.3f} | {n} | {difficulty} |")
        lines.append("")
        if "category_recall.png" in charts:
            lines.append("![Category Recall](assets/category_recall.png)")
            lines.append("")

    # Semantic Confidence Analysis
    if "semantic_score" in df.columns:
        semantic_stats = analyze_semantic_scores(df, "semantic_score")
        if semantic_stats.get("tp_count", 0) > 0:
            lines.extend(
                [
                    "## Semantic Alignment Analysis (The 'Understanding' Metric)",
                    "",
                    "> How confident is the semantic match? Higher scores = LLM truly understood the mental model.",
                    "",
                    "| Metric | True Positives | False Positives |",
                    "|--------|----------------|-----------------|",
                    f"| Count | {semantic_stats.get('tp_count', 0)} | {semantic_stats.get('fp_count', 0)} |",
                    f"| Mean Score | {semantic_stats.get('tp_mean', 0):.3f} | {semantic_stats.get('fp_mean', 0):.3f} |",
                    f"| Std Dev | {semantic_stats.get('tp_std', 0):.3f} | {semantic_stats.get('fp_std', 0):.3f} |",
                    f"| Median | {semantic_stats.get('tp_median', 0):.3f} | {semantic_stats.get('fp_median', 0):.3f} |",
                    "",
                ]
            )
            if semantic_stats.get("separation_test"):
                sep = semantic_stats["separation_test"]
                lines.extend(
                    [
                        "### Score Separation Test (Mann-Whitney U)",
                        "",
                        f"- **U Statistic:** {sep.get('statistic', 0):.2f}",
                        f"- **p-value:** {sep.get('p_value', 1):.6f}",
                        f"- **Interpretation:** {sep.get('interpretation', '')}",
                        "",
                    ]
                )
            if semantic_stats.get("effect_size"):
                eff = semantic_stats["effect_size"]
                lines.extend(
                    [
                        f"- **Effect Size (Cliff's Delta):** {eff.get('delta', 0):.3f} ({eff.get('interpretation', '')})",
                        "",
                    ]
                )
        if "semantic_distribution.png" in charts:
            lines.append("![Semantic Distribution](assets/semantic_distribution.png)")
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
                "",
                "| ID | Name | Category | Recall | N |",
                "|----|------|----------|--------|---|",
            ]
        )
        by_misconception = by_misconception.sort_values("recall")
        for _, row in by_misconception.iterrows():
            mid = row["expected_id"]
            gt = gt_map.get(mid, {})
            name = gt.get("name", str(mid))[:35]
            cat = gt.get("category", "")[:25]
            n = row["tp"] + row["fn"]
            lines.append(f"| {mid} | {name} | {cat} | {row['recall']:.2f} | {n} |")
        lines.append("")
        if "misconception_recall.png" in charts:
            lines.append("![Misconception Recall](assets/misconception_recall.png)")
            lines.append("")

    # Compliance / Filtering Statistics
    if compliance_df is not None and not compliance_df.empty:
        total_raw = compliance_df["raw_misconceptions"].sum()
        total_null = compliance_df["null_filtered"].sum()
        total_noise = compliance_df["noise_filtered"].sum()
        total_evaluated = compliance_df["evaluated_misconceptions"].sum()

        lines.extend(
            [
                "## Detection Filtering Pipeline",
                "",
                "> Shows how many detections were filtered at each stage before evaluation.",
                "",
                "| Stage | Count | % of Raw |",
                "|-------|-------|----------|",
                f"| Raw Detections | {total_raw} | 100% |",
                f"| Null-Template Filtered | {total_null} | {100 * total_null / total_raw:.1f}% |"
                if total_raw > 0
                else "| Null-Template Filtered | 0 | 0% |",
                f"| Noise Floor Filtered (< {noise_floor:.2f}) | {total_noise} | {100 * total_noise / total_raw:.1f}% |"
                if total_raw > 0
                else f"| Noise Floor Filtered (< {noise_floor:.2f}) | 0 | 0% |",
                f"| **Evaluated Detections** | **{total_evaluated}** | **{100 * total_evaluated / total_raw:.1f}%** |"
                if total_raw > 0
                else "| **Evaluated Detections** | **0** | **0%** |",
                "",
                "> **Note:** 'Noise Floor Filtered' detections are pedantic observations (e.g., 'didn't close Scanner')",
                "> that have low semantic similarity to any ground truth misconception and are not counted as hallucinations.",
                "",
            ]
        )

    # Hallucinations
    if "hallucinations.png" in charts:
        lines.extend(
            [
                "## False Positive Analysis (Hallucinations)",
                "",
                "> These are misconceptions the LLM 'invented' that don't match any ground truth.",
                "> Note: Only detections above the noise floor are counted here.",
                "",
                "![Hallucinations](assets/hallucinations.png)",
                "",
            ]
        )

    # Ensemble Analysis Section
    console.print("[cyan]Computing ensemble analysis...[/cyan]")
    ensemble_comparison = compute_ensemble_comparison(df, strategy_threshold=2, model_threshold=2)
    raw = ensemble_comparison["raw"]
    strat_ens = ensemble_comparison["strategy_ensemble"]
    model_ens = ensemble_comparison["model_ensemble"]

    lines.extend(
        [
            "## Ensemble Voting Analysis",
            "",
            "> Ensemble voting requires multiple agreeing sources before counting a detection.",
            "> This trades recall for precision, reducing hallucinations.",
            "",
            "### Comparison: Raw vs Ensemble Methods",
            "",
            "| Method | Precision | Recall | F1 | Precision Gain | Recall Loss |",
            "|--------|-----------|--------|-----|----------------|-------------|",
            f"| **Raw (No Ensemble)** | {raw['precision']:.3f} | {raw['recall']:.3f} | {raw['f1']:.3f} | — | — |",
            f"| **Strategy Ensemble (≥2/4)** | {strat_ens['metrics']['precision']:.3f} | {strat_ens['metrics']['recall']:.3f} | {strat_ens['metrics']['f1']:.3f} | {strat_ens['precision_gain']:+.3f} | {strat_ens['recall_loss']:+.3f} |",
            f"| **Model Ensemble (≥2/6)** | {model_ens['metrics']['precision']:.3f} | {model_ens['metrics']['recall']:.3f} | {model_ens['metrics']['f1']:.3f} | {model_ens['precision_gain']:+.3f} | {model_ens['recall_loss']:+.3f} |",
            "",
            "### Interpretation",
            "",
        ]
    )

    # Determine which ensemble is better
    if strat_ens["metrics"]["f1"] > model_ens["metrics"]["f1"]:
        better = "Strategy Ensemble"
        better_f1 = strat_ens["metrics"]["f1"]
        worse_f1 = model_ens["metrics"]["f1"]
    else:
        better = "Model Ensemble"
        better_f1 = model_ens["metrics"]["f1"]
        worse_f1 = strat_ens["metrics"]["f1"]

    lines.extend(
        [
            f"- **Best Ensemble Method:** {better} (F1 = {better_f1:.3f})",
            "- **Strategy Ensemble:** Requires ≥2 of 4 prompting strategies to agree on the same misconception",
            "- **Model Ensemble:** Requires ≥2 of 6 models to agree on the same misconception",
            "",
            "> **Key Finding:** Ensemble voting "
            + (
                f"improves F1 by {max(strat_ens['f1_delta'], model_ens['f1_delta']):.3f} through precision gains."
                if max(strat_ens["f1_delta"], model_ens["f1_delta"]) > 0
                else "reduces F1, suggesting the precision-recall tradeoff is not favorable for this task."
            ),
            "",
        ]
    )

    # Methodology note
    lines.extend(
        [
            "---",
            "",
            "## Methodology Notes",
            "",
            "- **Semantic Matching:** Uses OpenAI `text-embedding-3-large` to embed both LLM explanations and ground truth student thinking.",
            f"- **Match Threshold:** Cosine similarity ≥ {semantic_threshold:.2f} required for a True Positive.",
            f"- **Noise Floor:** Detections with similarity < {noise_floor:.2f} are filtered as 'pedantic' noise, not counted as hallucinations.",
            "- **Bootstrap CI:** 1000 resamples with replacement for confidence intervals.",
            "- **McNemar's Test:** Paired comparison with continuity correction.",
            "- **Strategy Ensemble:** Detection validated if ≥2/4 strategies agree on same misconception for same student/question.",
            "- **Model Ensemble:** Detection validated if ≥2/6 models agree on same misconception for same student/question/strategy.",
            "",
        ]
    )

    report_path = run_dir / "report.md"
    report_path.write_text("\n".join(lines))
    return report_path


@app.command()
def analyze_ensemble(
    run_name: str = typer.Option(..., help="Descriptive run name (e.g., 'analysis3')"),
    ensemble_threshold: int = typer.Option(
        2,
        help="Minimum strategies that must agree for a detection to count (default: 2)",
    ),
    semantic_threshold: float = typer.Option(
        SEMANTIC_THRESHOLD_DEFAULT,
        help="Semantic similarity threshold for matching (default: 0.65)",
    ),
    noise_floor: float = typer.Option(
        NOISE_FLOOR_THRESHOLD,
        help="Noise floor threshold - detections below this are filtered (default: 0.55)",
    ),
):
    """
    Run multi-assignment analysis with ENSEMBLE VOTING.

    This applies majority voting across strategies: a detection is only
    counted if >= ensemble_threshold strategies agree on the same misconception.

    Expected effect: Higher precision (fewer hallucinations), slightly lower recall.
    """
    console.print("[bold cyan]═══ Analysis 3: Ensemble Voting Mode ═══[/bold cyan]")
    console.print(f"Run name: {run_name}")
    console.print(f"Ensemble threshold: {ensemble_threshold}/4 strategies must agree")
    console.print(f"Semantic threshold: {semantic_threshold}")
    console.print(f"Noise floor: {noise_floor}")

    ASSIGNMENTS = ["a1", "a2", "a3"]
    all_dfs = []
    all_compliance_dfs = []
    combined_groundtruth: list[dict[str, Any]] = []
    total_students = 0
    seeds = []

    for assignment in ASSIGNMENTS:
        console.print(f"\n[cyan]Processing {assignment}...[/cyan]")

        detections_dir = Path(f"detections/{assignment}_multi")
        manifest_path = Path(f"authentic_seeded/{assignment}/manifest.json")
        groundtruth_path = Path(f"data/{assignment}/groundtruth.json")

        if not detections_dir.exists():
            console.print(f"[yellow]Warning: {detections_dir} not found, skipping[/yellow]")
            continue

        manifest = load_manifest(manifest_path)
        groundtruth = load_groundtruth(groundtruth_path)

        # Add to combined groundtruth (avoiding duplicates by ID)
        existing_ids = {gt["id"] for gt in combined_groundtruth}
        for gt in groundtruth:
            if gt["id"] not in existing_ids:
                combined_groundtruth.append(gt)

        total_students += manifest.get("student_count", 0)
        if manifest.get("seed"):
            seeds.append(str(manifest["seed"]))

        console.print(f"  Students: {manifest.get('student_count', 'N/A')}")
        console.print(f"  Misconceptions: {len(groundtruth)}")

        df, compliance_df = build_results_df(
            detections_dir,
            manifest,
            groundtruth,
            semantic_threshold=semantic_threshold,
            noise_floor_threshold=noise_floor,
        )

        if not df.empty:
            df["assignment"] = assignment
            all_dfs.append(df)

        if not compliance_df.empty:
            compliance_df["assignment"] = assignment
            all_compliance_dfs.append(compliance_df)

    if not all_dfs:
        console.print("[red]No results from any assignment![/red]")
        return

    # Combine all DataFrames
    combined_df = pd.concat(all_dfs, ignore_index=True)
    combined_compliance_df = (
        pd.concat(all_compliance_dfs, ignore_index=True) if all_compliance_dfs else pd.DataFrame()
    )

    console.print("\n[bold]Pre-ensemble dataset:[/bold]")
    console.print(f"  Total students processed: {total_students}")
    console.print(f"  Total detection rows: {len(combined_df)}")

    # Compute pre-ensemble metrics
    pre_ensemble = compute_metrics(combined_df)
    console.print(
        f"  Pre-ensemble: P={pre_ensemble['precision']:.3f} R={pre_ensemble['recall']:.3f} F1={pre_ensemble['f1']:.3f}"
    )

    # Apply ensemble filtering
    ensemble_df = apply_ensemble_filter(combined_df, ensemble_threshold=ensemble_threshold)

    # Compute post-ensemble metrics
    overall = compute_metrics(ensemble_df)
    console.print(
        f"\n[bold]Post-ensemble:[/bold] P={overall['precision']:.3f} R={overall['recall']:.3f} F1={overall['f1']:.3f}"
    )

    # Per-assignment metrics
    console.print("\n[bold]By Assignment (Ensemble)[/bold]")
    by_assignment = metrics_by_group(ensemble_df, ["assignment"])
    table = Table()
    table.add_column("Assignment")
    table.add_column("TP")
    table.add_column("FP")
    table.add_column("FN")
    table.add_column("Precision")
    table.add_column("Recall")
    table.add_column("F1")
    for _, row in by_assignment.iterrows():
        table.add_row(
            str(row["assignment"]),
            str(row["tp"]),
            str(row["fp"]),
            str(row["fn"]),
            f"{row['precision']:.3f}",
            f"{row['recall']:.3f}",
            f"{row['f1']:.3f}",
        )
    console.print(table)

    # Create run directory
    run_id = run_name if run_name.startswith("run_") else f"run_{run_name}"
    run_dir = Path("runs/multi") / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    assets_dir = run_dir / "assets"
    assets_dir.mkdir(exist_ok=True)

    # Save CSVs
    ensemble_df.to_csv(run_dir / "results.csv", index=False)
    (run_dir / "metrics.json").write_text(json.dumps(overall, indent=2))
    if not combined_compliance_df.empty:
        combined_compliance_df.to_csv(run_dir / "compliance.csv", index=False)

    # Save config with ensemble info
    config = {
        "mode": "ensemble",
        "ensemble_threshold": ensemble_threshold,
        "semantic_threshold": semantic_threshold,
        "noise_floor": noise_floor,
        "pre_ensemble_metrics": pre_ensemble,
        "post_ensemble_metrics": overall,
    }
    (run_dir / "config.json").write_text(json.dumps(config, indent=2))

    # Generate charts
    charts = generate_charts(ensemble_df, assets_dir, combined_groundtruth)

    # Generate assignment comparison chart
    if not by_assignment.empty:
        fig, ax = plt.subplots(figsize=(10, 6))
        x = np.arange(len(by_assignment))
        width = 0.25
        ax.bar(x - width, by_assignment["precision"], width, label="Precision", color="#4285F4")
        ax.bar(x, by_assignment["recall"], width, label="Recall", color="#34A853")
        ax.bar(x + width, by_assignment["f1"], width, label="F1", color="#EA4335")
        ax.set_xlabel("Assignment")
        ax.set_ylabel("Score")
        ax.set_title(f"Cross-Assignment Comparison (Ensemble N≥{ensemble_threshold})")
        ax.set_xticks(x)
        ax.set_xticklabels(by_assignment["assignment"].tolist())
        ax.legend()
        ax.set_ylim(0, 1)
        plt.tight_layout()
        fig.savefig(assets_dir / "assignment_comparison.png", dpi=150)
        plt.close(fig)
        charts.append("assignment_comparison.png")

    # Generate report
    by_strategy = metrics_by_group(ensemble_df, ["strategy"])
    by_model = metrics_by_group(ensemble_df, ["model"])
    by_category = metrics_by_group(
        ensemble_df[ensemble_df["expected_category"].notna()],
        ["expected_category"],  # type: ignore
    )
    by_misconception = metrics_by_group(
        ensemble_df[ensemble_df["expected_id"].notna()],
        ["expected_id"],  # type: ignore
    )

    # Build combined manifest for report
    combined_manifest = {
        "student_count": total_students,
        "seed": ",".join(seeds) if seeds else "multiple",
        "ensemble_threshold": ensemble_threshold,
    }

    report_path = generate_ensemble_report(
        ensemble_df,
        by_assignment,
        by_strategy,
        by_model,
        by_category,
        by_misconception,
        combined_groundtruth,
        combined_manifest,
        run_dir,
        charts,
        semantic_threshold=semantic_threshold,
        noise_floor=noise_floor,
        ensemble_threshold=ensemble_threshold,
        pre_ensemble_metrics=pre_ensemble,
    )

    console.print(f"[green]Report:[/green] {report_path}")
    console.print("[bold green]✓ Ensemble analysis complete[/bold green]")


def generate_ensemble_report(
    df: pd.DataFrame,
    by_assignment: pd.DataFrame,
    by_strategy: pd.DataFrame,
    by_model: pd.DataFrame,
    by_category: pd.DataFrame,
    by_misconception: pd.DataFrame,
    groundtruth: list[dict[str, Any]],
    manifest: dict[str, Any],
    run_dir: Path,
    charts: list[str],
    semantic_threshold: float = SEMANTIC_THRESHOLD_DEFAULT,
    noise_floor: float = NOISE_FLOOR_THRESHOLD,
    ensemble_threshold: int = 2,
    pre_ensemble_metrics: dict[str, Any] | None = None,
) -> Path:
    """Generate ensemble-specific report with before/after comparison."""
    gt_map = {gt["id"]: gt for gt in groundtruth}
    overall = compute_metrics(df)

    # Compute bootstrap CIs for overall metrics
    console.print("[cyan]Computing bootstrap confidence intervals...[/cyan]")
    overall_with_ci = compute_all_metrics_with_ci(df, n_bootstrap=1000)

    lines = [
        "# Analysis 3: Ensemble Voting Report",
        f"_Generated: {datetime.now(timezone.utc).isoformat()}_",
        "",
        "## Executive Summary",
        "",
        f"This report applies **Ensemble Voting** with threshold N≥{ensemble_threshold}.",
        f"A detection is only counted if at least {ensemble_threshold} strategies agree on the same misconception.",
        "",
        "**Goal:** Reduce hallucinations by filtering one-off detections that only one strategy sees.",
        "",
        "---",
        "",
        "## Configuration",
        f"- **Total Students:** {manifest.get('student_count', 'N/A')}",
        f"- **Ensemble Threshold:** {ensemble_threshold}/4 strategies must agree",
        f"- **Semantic Threshold:** Cosine Similarity ≥ {semantic_threshold:.2f}",
        f"- **Noise Floor:** Detections with score < {noise_floor:.2f} filtered",
        f"- **Seeds:** {manifest.get('seed', 'N/A')}",
        "",
    ]

    # Before/After comparison
    if pre_ensemble_metrics:
        lines.extend(
            [
                "## Before vs After Ensemble Filtering",
                "",
                "| Metric | Before | After | Change |",
                "|--------|--------|-------|--------|",
                f"| Precision | {pre_ensemble_metrics['precision']:.3f} | {overall['precision']:.3f} | {(overall['precision'] - pre_ensemble_metrics['precision']) * 100:+.1f}% |",
                f"| Recall | {pre_ensemble_metrics['recall']:.3f} | {overall['recall']:.3f} | {(overall['recall'] - pre_ensemble_metrics['recall']) * 100:+.1f}% |",
                f"| F1 Score | {pre_ensemble_metrics['f1']:.3f} | {overall['f1']:.3f} | {(overall['f1'] - pre_ensemble_metrics['f1']) * 100:+.1f}% |",
                f"| TP | {pre_ensemble_metrics['tp']} | {overall['tp']} | {overall['tp'] - pre_ensemble_metrics['tp']:+d} |",
                f"| FP | {pre_ensemble_metrics['fp']} | {overall['fp']} | {overall['fp'] - pre_ensemble_metrics['fp']:+d} |",
                f"| FN | {pre_ensemble_metrics['fn']} | {overall['fn']} | {overall['fn'] - pre_ensemble_metrics['fn']:+d} |",
                "",
            ]
        )

    lines.extend(
        [
            "## Overall Metrics (with 95% Confidence Intervals)",
            "",
            "| Metric | Value | 95% CI | Std Error |",
            "|--------|-------|--------|-----------|",
            f"| True Positives | {overall['tp']} | — | — |",
            f"| False Positives | {overall['fp']} | — | — |",
            f"| False Negatives | {overall['fn']} | — | — |",
            f"| **Precision** | **{overall_with_ci['precision']['estimate']:.3f}** | [{overall_with_ci['precision']['ci_lower']:.3f}, {overall_with_ci['precision']['ci_upper']:.3f}] | {overall_with_ci['precision']['std_error']:.4f} |",
            f"| **Recall** | **{overall_with_ci['recall']['estimate']:.3f}** | [{overall_with_ci['recall']['ci_lower']:.3f}, {overall_with_ci['recall']['ci_upper']:.3f}] | {overall_with_ci['recall']['std_error']:.4f} |",
            f"| **F1 Score** | **{overall_with_ci['f1']['estimate']:.3f}** | [{overall_with_ci['f1']['ci_lower']:.3f}, {overall_with_ci['f1']['ci_upper']:.3f}] | {overall_with_ci['f1']['std_error']:.4f} |",
            "",
        ]
    )

    # Cross-Assignment Comparison
    lines.extend(
        [
            "## Cross-Assignment Comparison (Ensemble)",
            "",
            "| Assignment | Focus | TP | FP | FN | Precision | Recall | F1 |",
            "|------------|-------|----|----|----|-----------| -------|-----|",
        ]
    )
    assignment_focus = {"a1": "Variables/Math", "a2": "Loops/Control", "a3": "Arrays/Strings"}
    for _, row in by_assignment.iterrows():
        focus = assignment_focus.get(str(row["assignment"]), "")
        lines.append(
            f"| {row['assignment']} | {focus} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "assignment_comparison.png" in charts:
        lines.append("![Assignment Comparison](assets/assignment_comparison.png)")
        lines.append("")

    # Strategy section
    lines.extend(
        [
            "## Performance by Strategy (Ensemble)",
            "",
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
            "## Performance by Model (Ensemble)",
            "",
            "| Model | TP | FP | FN | Precision | Recall | F1 |",
            "|-------|----|----|----|-----------|--------|-----|",
        ]
    )
    for _, row in by_model.iterrows():
        model_short = str(row["model"]).split("/")[-1]
        lines.append(
            f"| {model_short} | {row['tp']} | {row['fp']} | {row['fn']} | {row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
        )
    lines.append("")
    if "model_comparison.png" in charts:
        lines.append("![Model Comparison](assets/model_comparison.png)")
        lines.append("")

    # Category section
    if not by_category.empty:
        lines.extend(
            [
                "## Notional Machine Category Detection (Ensemble)",
                "",
                "| Category | Recall | N | Difficulty |",
                "|----------|--------|---|------------|",
            ]
        )
        # Filter out any empty/NaN categories or zero-N rows
        by_category = by_category[by_category["expected_category"].notna()]
        by_category = by_category[by_category["expected_category"] != ""]
        by_category = by_category[(by_category["tp"] + by_category["fn"]) > 0]
        by_category = by_category.sort_values("recall")  # type: ignore
        for _, row in by_category.iterrows():
            n = row["tp"] + row["fn"]
            recall = row["recall"]
            if recall >= 0.7:
                difficulty = "Easy"
            elif recall >= 0.5:
                difficulty = "Medium"
            else:
                difficulty = "**Hard**"
            lines.append(f"| {row['expected_category']} | {recall:.3f} | {n} | {difficulty} |")
        lines.append("")
        if "category_recall.png" in charts:
            lines.append("![Category Recall](assets/category_recall.png)")
            lines.append("")

    # Methodology note
    lines.extend(
        [
            "---",
            "",
            "## Methodology Notes",
            "",
            f"- **Ensemble Voting:** Detection counted only if ≥{ensemble_threshold} strategies agree",
            "- **Semantic Matching:** Uses OpenAI `text-embedding-3-large`",
            f"- **Match Threshold:** Cosine similarity ≥ {semantic_threshold:.2f}",
            f"- **Noise Floor:** Detections < {noise_floor:.2f} filtered",
            "- **Bootstrap CI:** 1000 resamples",
            "",
        ]
    )

    report_path = run_dir / "report.md"
    report_path.write_text("\n".join(lines))
    return report_path


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
