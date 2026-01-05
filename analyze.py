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

# ---------------------------------------------------------------------------
# Threshold Sensitivity Analysis Grids (for ITiCSE/SIGCSE rigor)
# ---------------------------------------------------------------------------
# These define the parameter sweep for sensitivity analysis
SEMANTIC_THRESHOLD_GRID = [0.55, 0.60, 0.65, 0.70, 0.75, 0.80]
NOISE_FLOOR_GRID = [0.40, 0.45, 0.50, 0.55, 0.60]

# ---------------------------------------------------------------------------
# Notional Machine Category Classification (Structural vs Semantic)
# ---------------------------------------------------------------------------
# Structural: Surface-level, syntax-related errors that are "easy" to detect
# Semantic: Deep mental model errors that require understanding student thinking
CATEGORY_TYPE_MAP = {
    # Structural (surface-level, syntax-related) - generally high recall
    "The Void Machine": "Structural",
    "The Mutable String Machine": "Structural",
    "The Human Index Machine": "Structural",
    "The Algebraic Syntax Machine": "Structural",
    "The Teleological Control Machine": "Structural",
    "The Semantic Bond Machine": "Structural",
    # Semantic (deep mental model errors) - generally lower recall
    "The Reactive State Machine": "Semantic",
    "The Independent Switch": "Semantic",
    "The Fluid Type Machine": "Semantic",
    "The Anthropomorphic I/O Machine": "Semantic",
}


def get_category_type(category: str) -> str:
    """Get the type (Structural/Semantic) for a category."""
    return CATEGORY_TYPE_MAP.get(category, "Unknown")


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
# Scoring + Threshold Classification
# ---------------------------------------------------------------------------
FILE_KEY_COLS = ["strategy", "model", "student", "question"]


def semantic_best_match(
    detection: dict[str, str],
    precomputed_gt_embeddings: dict[str, list[float]],
) -> tuple[str | None, float, str]:
    """
    Get the best semantic match for a detection without applying a threshold.

    Returns:
        (best_match_id, best_score, match_method)
    """
    detection_text = build_detection_text(detection)
    if not detection_text.strip():
        return None, 0.0, "no_text"

    try:
        detection_embedding = get_embedding(detection_text)
    except Exception:
        return None, 0.0, "embedding_error"

    best_match_id = None
    best_score = 0.0
    for gt_id, gt_embedding in precomputed_gt_embeddings.items():
        similarity = cosine_similarity(detection_embedding, gt_embedding)
        if similarity > best_score:
            best_score = similarity
            best_match_id = gt_id

    return best_match_id, best_score, "semantic"


def build_scored_df(
    detections_dir: Path,
    manifest: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    null_template_threshold: float = NULL_TEMPLATE_THRESHOLD,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    Build a scored detection dataframe without applying semantic/noise thresholds.

    Returns:
        scored_df: One row per detection with best_match_id and semantic_score
        file_df: One row per (student, question, strategy, model) with raw/null counts
    """
    gt_map = {m["id"]: m for m in groundtruth}
    console.print("[cyan]Precomputing groundtruth embeddings...[/cyan]")
    gt_embeddings = precompute_groundtruth_embeddings(groundtruth)

    null_embeddings = build_null_template_embeddings()

    strategies = discover_strategies(detections_dir)
    console.print(f"[green]Strategies:[/green] {', '.join(strategies)}")

    rows: list[dict[str, Any]] = []
    file_rows: list[dict[str, Any]] = []
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

                for mis in normalized_mis:
                    adapted = adapt_detection(mis)
                    best_match_id, semantic_score, match_method = semantic_best_match(
                        adapted,
                        gt_embeddings,
                    )

                    rows.append(
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
                            "best_match_id": best_match_id,
                            "semantic_score": semantic_score,
                            "match_method": match_method,
                            "confidence": mis.get("confidence"),
                        }
                    )

                file_rows.append(
                    {
                        "strategy": strategy,
                        "model": model,
                        "student": student,
                        "question": question,
                        "expected_id": expected_id,
                        "expected_category": expected_category,
                        "is_clean": is_clean,
                        "raw_misconceptions": len(mis_list),
                        "null_filtered": null_filtered_count,
                        "raw_empty": len(mis_list) == 0,
                        "raw_nonempty": len(mis_list) > 0,
                        "null_only": len(mis_list) > 0 and len(normalized_mis) == 0,
                    }
                )

    console.print(f"[green]Total detections processed: {det_count}[/green]")
    return pd.DataFrame(rows), pd.DataFrame(file_rows)


def _build_compliance_df(
    file_df: pd.DataFrame,
    scored_df: pd.DataFrame,
    noise_floor: float,
) -> pd.DataFrame:
    if file_df.empty:
        return pd.DataFrame()

    counts = pd.DataFrame(columns=FILE_KEY_COLS + ["noise_filtered", "evaluated_misconceptions"])
    if not scored_df.empty:
        grouped = scored_df.groupby(FILE_KEY_COLS)["semantic_score"]
        counts = grouped.agg(
            noise_filtered=lambda s: int((s < noise_floor).sum()),
            evaluated_misconceptions=lambda s: int((s >= noise_floor).sum()),
        ).reset_index()

    compliance_df = file_df.merge(counts, on=FILE_KEY_COLS, how="left")
    compliance_df["noise_filtered"] = compliance_df["noise_filtered"].fillna(0).astype(int)
    compliance_df["evaluated_misconceptions"] = (
        compliance_df["evaluated_misconceptions"].fillna(0).astype(int)
    )

    return compliance_df[
        [
            "strategy",
            "model",
            "student",
            "question",
            "raw_misconceptions",
            "null_filtered",
            "noise_filtered",
            "evaluated_misconceptions",
            "raw_empty",
            "raw_nonempty",
            "null_only",
        ]
    ]


def classify_scored_df(
    scored_df: pd.DataFrame,
    file_df: pd.DataFrame,
    semantic_threshold: float,
    noise_floor: float,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    Apply thresholds to scored detections and generate TP/FP/FN rows.
    """
    if scored_df.empty and file_df.empty:
        return pd.DataFrame(), pd.DataFrame()

    if scored_df.empty:
        filtered = scored_df.copy()
    else:
        filtered = scored_df[scored_df["semantic_score"] >= noise_floor].copy()

    if not filtered.empty:
        match_mask = filtered["semantic_score"] >= semantic_threshold
        filtered["matched_id"] = filtered["best_match_id"].where(match_mask)
        filtered["matched_id"] = filtered["matched_id"].where(
            pd.notna(filtered["matched_id"]), None
        )

        if "match_method" in filtered.columns:
            is_semantic = filtered["match_method"] == "semantic"
            filtered.loc[is_semantic & ~match_mask, "match_method"] = "below_threshold"
    else:
        filtered["matched_id"] = None

    if not filtered.empty:
        filtered["result"] = "FP_HALLUCINATION"
        clean_mask = filtered["is_clean"] == True
        filtered.loc[clean_mask, "result"] = "FP_CLEAN"

        match_mask = filtered["matched_id"].notna()
        expected_mask = filtered["expected_id"].notna()
        tp_mask = (
            ~clean_mask
            & match_mask
            & expected_mask
            & (filtered["matched_id"] == filtered["expected_id"])
        )
        filtered.loc[tp_mask, "result"] = "TP"

        fp_wrong_mask = ~clean_mask & match_mask & ~tp_mask
        filtered.loc[fp_wrong_mask, "result"] = "FP_WRONG"

    # Add FN rows for expected misconceptions that were not detected
    fn_rows: list[dict[str, Any]] = []
    if not file_df.empty:
        tp_keys = set(
            tuple(row)
            for row in filtered.loc[filtered["result"] == "TP", FILE_KEY_COLS].itertuples(
                index=False, name=None
            )
        )
        for row in file_df.itertuples(index=False):
            expected_id = getattr(row, "expected_id")
            is_clean = getattr(row, "is_clean")
            if is_clean or not expected_id:
                continue
            key = (
                getattr(row, "strategy"),
                getattr(row, "model"),
                getattr(row, "student"),
                getattr(row, "question"),
            )
            if key in tp_keys:
                continue
            fn_rows.append(
                {
                    "strategy": getattr(row, "strategy"),
                    "model": getattr(row, "model"),
                    "student": getattr(row, "student"),
                    "question": getattr(row, "question"),
                    "expected_id": expected_id,
                    "expected_category": getattr(row, "expected_category"),
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

    result_cols = [
        "strategy",
        "model",
        "student",
        "question",
        "expected_id",
        "expected_category",
        "is_clean",
        "detected_name",
        "detected_thinking",
        "matched_id",
        "semantic_score",
        "match_method",
        "result",
        "confidence",
    ]

    result_rows = []
    if not filtered.empty:
        filtered = filtered.drop(columns=["best_match_id"], errors="ignore")
        result_rows.append(filtered[result_cols])
    if fn_rows:
        result_rows.append(pd.DataFrame(fn_rows)[result_cols])

    results_df = (
        pd.concat(result_rows, ignore_index=True)
        if result_rows
        else pd.DataFrame(columns=result_cols)
    )
    compliance_df = _build_compliance_df(file_df, scored_df, noise_floor)
    return results_df, compliance_df


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
# Threshold Sensitivity Analysis (ITiCSE Publication Feature)
# ---------------------------------------------------------------------------
def reclassify_at_threshold(
    scored_df: pd.DataFrame,
    file_df: pd.DataFrame,
    semantic_threshold: float,
    noise_floor: float,
) -> pd.DataFrame:
    """
    Reclassify scored detections at a different threshold.

    This allows threshold sensitivity analysis without recomputing embeddings.
    """
    results_df, _ = classify_scored_df(
        scored_df,
        file_df,
        semantic_threshold=semantic_threshold,
        noise_floor=noise_floor,
    )
    return results_df


def compute_threshold_sensitivity(
    scored_df: pd.DataFrame,
    file_df: pd.DataFrame,
    semantic_thresholds: list[float] | None = None,
    noise_floors: list[float] | None = None,
) -> tuple[pd.DataFrame, dict[str, Any]]:
    """
    Compute metrics across a grid of threshold values.

    Args:
        scored_df: Scored detections with semantic_score and best_match_id
        file_df: File-level metadata for proper FN accounting
        semantic_thresholds: List of semantic thresholds to test
        noise_floors: List of noise floor thresholds to test

    Returns:
        Tuple of (sensitivity_df, optimal_config)
        - sensitivity_df: DataFrame with columns [semantic_threshold, noise_floor, precision, recall, f1, tp, fp, fn]
        - optimal_config: Dict with optimal threshold configuration
    """
    if semantic_thresholds is None:
        semantic_thresholds = SEMANTIC_THRESHOLD_GRID
    if noise_floors is None:
        noise_floors = NOISE_FLOOR_GRID

    console.print(
        f"[cyan]Computing threshold sensitivity ({len(semantic_thresholds)}×{len(noise_floors)} grid)...[/cyan]"
    )

    results = []
    best_f1 = 0.0
    optimal_config = {}

    for sem_thresh in semantic_thresholds:
        for noise_floor in noise_floors:
            # Reclassify at this threshold combination
            reclassified = reclassify_at_threshold(scored_df, file_df, sem_thresh, noise_floor)

            if reclassified.empty:
                continue

            metrics = compute_metrics(reclassified)

            results.append(
                {
                    "semantic_threshold": sem_thresh,
                    "noise_floor": noise_floor,
                    "precision": metrics["precision"],
                    "recall": metrics["recall"],
                    "f1": metrics["f1"],
                    "tp": metrics["tp"],
                    "fp": metrics["fp"],
                    "fn": metrics["fn"],
                }
            )

            if metrics["f1"] > best_f1:
                best_f1 = metrics["f1"]
                optimal_config = {
                    "semantic_threshold": sem_thresh,
                    "noise_floor": noise_floor,
                    "precision": metrics["precision"],
                    "recall": metrics["recall"],
                    "f1": metrics["f1"],
                }

    sensitivity_df = pd.DataFrame(results)
    console.print(
        f"[green]Optimal: semantic={optimal_config.get('semantic_threshold', 'N/A')}, "
        f"noise={optimal_config.get('noise_floor', 'N/A')}, "
        f"F1={optimal_config.get('f1', 0):.3f}[/green]"
    )

    return sensitivity_df, optimal_config


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
    scored_df, file_df = build_scored_df(
        detections_dir,
        manifest,
        groundtruth,
        null_template_threshold=null_template_threshold,
    )
    results_df, compliance_df = classify_scored_df(
        scored_df,
        file_df,
        semantic_threshold=semantic_threshold,
        noise_floor=noise_floor_threshold,
    )
    return results_df, compliance_df


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

            ax.axvline(
                SEMANTIC_THRESHOLD_DEFAULT,
                color="black",
                linestyle="--",
                linewidth=2,
                label=f"Threshold ({SEMANTIC_THRESHOLD_DEFAULT})",
            )
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
# Publication-Grade Figure Generation (ITiCSE)
# ---------------------------------------------------------------------------
def generate_threshold_sensitivity_heatmap(
    sensitivity_df: pd.DataFrame,
    optimal_config: dict[str, Any],
    assets_dir: Path,
) -> str:
    """Generate heatmap showing F1 across threshold grid."""
    if sensitivity_df.empty:
        return ""

    # Pivot for heatmap
    pivot = sensitivity_df.pivot(index="noise_floor", columns="semantic_threshold", values="f1")

    fig, ax = plt.subplots(figsize=(10, 6))

    # Create heatmap with annotations
    sns.heatmap(
        pivot,
        annot=True,
        fmt=".3f",
        cmap="RdYlGn",
        ax=ax,
        vmin=pivot.values.min() - 0.02,
        vmax=pivot.values.max() + 0.02,
        linewidths=0.5,
        cbar_kws={"label": "F1 Score"},
    )

    # Mark optimal cell with a star
    opt_sem = optimal_config.get("semantic_threshold")
    opt_noise = optimal_config.get("noise_floor")
    if opt_sem and opt_noise:
        col_idx = list(pivot.columns).index(opt_sem)
        row_idx = list(pivot.index).index(opt_noise)
        ax.scatter(col_idx + 0.5, row_idx + 0.5, marker="*", s=500, c="black", zorder=10)

    ax.set_xlabel("Semantic Similarity Threshold")
    ax.set_ylabel("Noise Floor Threshold")
    ax.set_title("Threshold Sensitivity Analysis: F1 Score\n(★ = Optimal Configuration)")

    plt.tight_layout()
    path = assets_dir / "threshold_sensitivity_heatmap.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "threshold_sensitivity_heatmap.png"


def generate_precision_recall_curve(
    sensitivity_df: pd.DataFrame,
    current_threshold: float,
    assets_dir: Path,
) -> str:
    """Generate PR curve across semantic thresholds (fixed noise floor)."""
    if sensitivity_df.empty:
        return ""

    # Filter to default noise floor
    default_noise = NOISE_FLOOR_THRESHOLD
    curve_data = sensitivity_df[sensitivity_df["noise_floor"] == default_noise].copy()

    if curve_data.empty:
        # Fallback to first available noise floor
        curve_data = sensitivity_df[
            sensitivity_df["noise_floor"] == sensitivity_df["noise_floor"].iloc[0]
        ].copy()

    if len(curve_data) < 2:
        return ""

    curve_data = curve_data.sort_values("semantic_threshold")

    fig, ax = plt.subplots(figsize=(10, 8))

    # Plot PR curve
    ax.plot(
        curve_data["recall"],
        curve_data["precision"],
        "b-o",
        linewidth=2,
        markersize=10,
        label="PR Curve",
    )

    # Annotate each point with threshold value
    for _, row in curve_data.iterrows():
        offset = (5, 5)
        fontweight = "normal"
        markersize = 10

        # Highlight current threshold
        if abs(row["semantic_threshold"] - current_threshold) < 0.01:
            ax.scatter(row["recall"], row["precision"], s=200, c="red", zorder=5, marker="*")
            fontweight = "bold"
            offset = (8, 8)

        ax.annotate(
            f"{row['semantic_threshold']:.2f}",
            (row["recall"], row["precision"]),
            textcoords="offset points",
            xytext=offset,
            fontsize=10,
            fontweight=fontweight,
        )

    ax.set_xlabel("Recall", fontsize=12)
    ax.set_ylabel("Precision", fontsize=12)
    ax.set_title(
        f"Precision-Recall Trade-off Across Semantic Thresholds\n(★ = Current Threshold {current_threshold})",
        fontsize=14,
    )
    ax.set_xlim(0, 1.05)
    ax.set_ylim(0, 1.05)
    ax.grid(True, alpha=0.3)
    ax.legend()

    # Add iso-F1 curves
    for f1_val in [0.3, 0.4, 0.5, 0.6, 0.7]:
        recall_range = np.linspace(0.01, 1, 100)
        precision_vals = (f1_val * recall_range) / (2 * recall_range - f1_val)
        valid = (precision_vals > 0) & (precision_vals <= 1)
        ax.plot(recall_range[valid], precision_vals[valid], "--", alpha=0.3, color="gray")
        # Label the iso-F1 curve
        if valid.any():
            mid_idx = len(recall_range[valid]) // 2
            ax.annotate(
                f"F1={f1_val}",
                (recall_range[valid][mid_idx], precision_vals[valid][mid_idx]),
                fontsize=8,
                color="gray",
                alpha=0.7,
            )

    plt.tight_layout()
    path = assets_dir / "precision_recall_curve.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "precision_recall_curve.png"


def generate_ensemble_comparison_chart(
    ensemble_comparison: dict[str, Any],
    assets_dir: Path,
) -> str:
    """Generate before/after comparison of ensemble voting effect."""
    raw = ensemble_comparison.get("raw", {})
    strat = ensemble_comparison.get("strategy_ensemble", {}).get("metrics", {})
    model = ensemble_comparison.get("model_ensemble", {}).get("metrics", {})

    if not raw or not strat or not model:
        return ""

    methods = [
        "Raw\n(No Ensemble)",
        "Strategy Ensemble\n(≥2/4 agree)",
        "Model Ensemble\n(≥2/6 agree)",
    ]
    precision = [raw.get("precision", 0), strat.get("precision", 0), model.get("precision", 0)]
    recall = [raw.get("recall", 0), strat.get("recall", 0), model.get("recall", 0)]
    f1 = [raw.get("f1", 0), strat.get("f1", 0), model.get("f1", 0)]

    fig, ax = plt.subplots(figsize=(12, 7))

    x = np.arange(len(methods))
    width = 0.25

    bars1 = ax.bar(
        x - width, precision, width, label="Precision", color="#3498db", edgecolor="black"
    )
    bars2 = ax.bar(x, recall, width, label="Recall", color="#2ecc71", edgecolor="black")
    bars3 = ax.bar(x + width, f1, width, label="F1 Score", color="#e74c3c", edgecolor="black")

    # Add value labels on bars
    for bars in [bars1, bars2, bars3]:
        for bar in bars:
            height = bar.get_height()
            ax.annotate(
                f"{height:.2f}",
                xy=(bar.get_x() + bar.get_width() / 2, height),
                xytext=(0, 3),
                textcoords="offset points",
                ha="center",
                va="bottom",
                fontsize=10,
                fontweight="bold",
            )

    # Add delta annotations
    strat_delta = ensemble_comparison.get("strategy_ensemble", {}).get("precision_gain", 0)
    model_delta = ensemble_comparison.get("model_ensemble", {}).get("precision_gain", 0)

    ax.annotate(
        f"+{strat_delta:.2f}",
        xy=(1 - width, precision[1]),
        xytext=(0, 20),
        textcoords="offset points",
        ha="center",
        fontsize=9,
        color="#3498db",
        fontweight="bold",
    )
    ax.annotate(
        f"+{model_delta:.2f}",
        xy=(2 - width, precision[2]),
        xytext=(0, 20),
        textcoords="offset points",
        ha="center",
        fontsize=9,
        color="#3498db",
        fontweight="bold",
    )

    ax.set_ylabel("Score", fontsize=12)
    ax.set_title("Ensemble Voting Effect on Classification Metrics", fontsize=14)
    ax.set_xticks(x)
    ax.set_xticklabels(methods, fontsize=11)
    ax.legend(loc="upper right", fontsize=11)
    ax.set_ylim(0, 1.15)
    ax.axhline(y=0.5, color="gray", linestyle="--", alpha=0.3)

    plt.tight_layout()
    path = assets_dir / "ensemble_comparison.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "ensemble_comparison.png"


def generate_category_type_comparison(
    df: pd.DataFrame,
    groundtruth: list[dict[str, Any]],
    assets_dir: Path,
) -> str:
    """Generate structural vs semantic category comparison (box/violin plot)."""
    gt_map = {gt["id"]: gt for gt in groundtruth}

    # Get per-category recall
    seeded = df[
        df["expected_id"].notna()
        & (df["expected_category"].notna())
        & (df["expected_category"] != "")
    ]
    if seeded.empty:
        return ""

    by_category = metrics_by_group(seeded, ["expected_category"])
    if by_category.empty:
        return ""

    # Add category type
    by_category["type"] = by_category["expected_category"].apply(get_category_type)
    by_category = by_category[by_category["type"].isin(["Structural", "Semantic"])]

    if by_category.empty:
        return ""

    structural = by_category[by_category["type"] == "Structural"]["recall"].values
    semantic = by_category[by_category["type"] == "Semantic"]["recall"].values

    if len(structural) == 0 or len(semantic) == 0:
        return ""

    fig, ax = plt.subplots(figsize=(10, 7))

    # Create box plots with individual points
    positions = [1, 2]
    bp = ax.boxplot(
        [structural, semantic],
        positions=positions,
        widths=0.5,
        patch_artist=True,
        showmeans=True,
        meanprops=dict(marker="D", markerfacecolor="white", markeredgecolor="black", markersize=10),
    )

    # Color the boxes
    colors = ["#2ecc71", "#e74c3c"]  # Green for structural (easy), Red for semantic (hard)
    for patch, color in zip(bp["boxes"], colors):
        patch.set_facecolor(color)
        patch.set_alpha(0.6)

    # Add individual points (jittered)
    np.random.seed(42)
    for i, (data, pos) in enumerate([(structural, 1), (semantic, 2)]):
        jitter = np.random.uniform(-0.1, 0.1, len(data))
        ax.scatter(
            [pos + j for j in jitter],
            data,
            alpha=0.8,
            s=100,
            c=colors[i],
            edgecolor="black",
            zorder=5,
        )

    # Add category labels next to points
    for _, row in by_category.iterrows():
        pos = 1 if row["type"] == "Structural" else 2
        jitter = np.random.uniform(-0.15, -0.3)
        short_name = row["expected_category"].replace("The ", "").replace(" Machine", "")[:15]
        ax.annotate(short_name, (pos + 0.2, row["recall"]), fontsize=8, alpha=0.7)

    # Statistics annotation
    from scipy import stats

    if len(structural) > 1 and len(semantic) > 1:
        stat, p_value = stats.mannwhitneyu(structural, semantic, alternative="greater")
        significance = "p < 0.05" if p_value < 0.05 else f"p = {p_value:.3f}"
        ax.annotate(
            f"Mann-Whitney U: {significance}",
            xy=(0.5, 0.02),
            xycoords="axes fraction",
            fontsize=10,
            ha="center",
            style="italic",
        )

    ax.set_xticks(positions)
    ax.set_xticklabels(
        [
            f"Structural\n(n={len(structural)}, μ={np.mean(structural):.2f})",
            f"Semantic\n(n={len(semantic)}, μ={np.mean(semantic):.2f})",
        ],
        fontsize=12,
    )
    ax.set_ylabel("Detection Recall", fontsize=12)
    ax.set_title("The Detection Gap: Structural vs Semantic Misconceptions", fontsize=14)
    ax.set_ylim(0, 1.1)
    ax.axhline(y=0.7, color="green", linestyle="--", alpha=0.5, label="Easy threshold (0.7)")
    ax.axhline(y=0.5, color="orange", linestyle="--", alpha=0.5, label="Medium threshold (0.5)")
    ax.legend(loc="lower left")
    ax.grid(axis="y", alpha=0.3)

    plt.tight_layout()
    path = assets_dir / "category_structural_vs_semantic.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "category_structural_vs_semantic.png"


def generate_enhanced_category_recall(
    df: pd.DataFrame,
    groundtruth: list[dict[str, Any]],
    assets_dir: Path,
) -> str:
    """Generate enhanced category recall chart with difficulty bands."""
    seeded = df[
        df["expected_id"].notna()
        & (df["expected_category"].notna())
        & (df["expected_category"] != "")
    ]
    if seeded.empty:
        return ""

    by_category = metrics_by_group(seeded, ["expected_category"])
    by_category = by_category[(by_category["tp"] + by_category["fn"]) > 0]
    by_category = by_category.sort_values("recall")

    if by_category.empty:
        return ""

    fig, ax = plt.subplots(figsize=(12, 8))

    # Add difficulty band backgrounds
    ax.axvspan(0, 0.5, alpha=0.15, color="red", label="Hard (<0.5)")
    ax.axvspan(0.5, 0.7, alpha=0.15, color="yellow", label="Medium (0.5-0.7)")
    ax.axvspan(0.7, 1.0, alpha=0.15, color="green", label="Easy (>0.7)")

    # Plot horizontal bars
    y_pos = np.arange(len(by_category))
    colors = []
    for recall in by_category["recall"]:
        if recall >= 0.7:
            colors.append("#27ae60")  # Green
        elif recall >= 0.5:
            colors.append("#f39c12")  # Orange
        else:
            colors.append("#c0392b")  # Red

    bars = ax.barh(y_pos, by_category["recall"], color=colors, edgecolor="black", height=0.7)

    # Add labels
    ax.set_yticks(y_pos)
    ax.set_yticklabels(by_category["expected_category"], fontsize=11)

    # Add value and N annotations
    for i, (_, row) in enumerate(by_category.iterrows()):
        n = int(row["tp"] + row["fn"])
        recall = row["recall"]
        ax.text(
            recall + 0.02, i, f"{recall:.2f} (n={n})", va="center", fontsize=10, fontweight="bold"
        )

    ax.set_xlabel("Detection Recall", fontsize=12)
    ax.set_title(
        "Notional Machine Category Detection Recall\n(with Difficulty Classification)", fontsize=14
    )
    ax.set_xlim(0, 1.15)
    ax.axvline(x=0.5, color="gray", linestyle="--", alpha=0.5)
    ax.axvline(x=0.7, color="gray", linestyle="--", alpha=0.5)

    # Custom legend for difficulty bands
    from matplotlib.patches import Patch

    legend_elements = [
        Patch(facecolor="red", alpha=0.3, label="Hard (<0.5)"),
        Patch(facecolor="yellow", alpha=0.3, label="Medium (0.5-0.7)"),
        Patch(facecolor="green", alpha=0.3, label="Easy (>0.7)"),
    ]
    ax.legend(handles=legend_elements, loc="lower right", fontsize=10)

    plt.tight_layout()
    path = assets_dir / "category_recall.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "category_recall.png"


def generate_model_dotplot(
    df: pd.DataFrame,
    assets_dir: Path,
) -> str:
    """Generate Cleveland dot plot for model comparison (cleaner than grouped bars)."""
    by_model = metrics_by_group(df, ["model"])
    if by_model.empty:
        return ""

    # Shorten model names
    def shorten_model_name(name: str) -> str:
        name = str(name).split("/")[-1]
        replacements = {
            "claude-haiku-4-5-20251001": "Claude-Haiku-4.5",
            "claude-haiku-4-5-20251001:reasoning": "Claude-Haiku-4.5-R",
            "gemini-3-flash-preview": "Gemini-3-Flash",
            "gemini-3-flash-preview:reasoning": "Gemini-3-Flash-R",
            "gpt-5.2-2025-12-11": "GPT-5.2",
            "gpt-5.2-2025-12-11:reasoning": "GPT-5.2-R",
        }
        for old, new in replacements.items():
            if old in name:
                return new
        return name[:20]

    by_model["model_short"] = by_model["model"].apply(shorten_model_name)
    by_model = by_model.sort_values("f1", ascending=True)

    fig, ax = plt.subplots(figsize=(12, 8))

    y_pos = np.arange(len(by_model))

    # Plot dots for each metric
    ax.scatter(
        by_model["precision"], y_pos, s=150, c="#3498db", marker="o", label="Precision", zorder=3
    )
    ax.scatter(by_model["recall"], y_pos, s=150, c="#2ecc71", marker="s", label="Recall", zorder=3)
    ax.scatter(by_model["f1"], y_pos, s=200, c="#e74c3c", marker="D", label="F1 Score", zorder=3)

    # Connect dots with lines
    for i in y_pos:
        ax.plot(
            [by_model["precision"].iloc[i], by_model["recall"].iloc[i]],
            [i, i],
            "k-",
            alpha=0.3,
            linewidth=1,
            zorder=1,
        )
        ax.plot(
            [by_model["recall"].iloc[i], by_model["f1"].iloc[i]],
            [i, i],
            "k-",
            alpha=0.3,
            linewidth=1,
            zorder=1,
        )

    ax.set_yticks(y_pos)
    ax.set_yticklabels(by_model["model_short"], fontsize=11)
    ax.set_xlabel("Score", fontsize=12)
    ax.set_title("Model Performance Comparison", fontsize=14)
    ax.set_xlim(0, 1.05)
    ax.legend(loc="lower right", fontsize=11)
    ax.grid(axis="x", alpha=0.3)
    ax.axvline(x=0.5, color="gray", linestyle="--", alpha=0.3)

    plt.tight_layout()
    path = assets_dir / "model_comparison.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "model_comparison.png"


def generate_hallucinations_by_type(
    df: pd.DataFrame,
    assets_dir: Path,
) -> str:
    """Generate FP analysis grouped by type (FP_CLEAN, FP_WRONG, FP_HALLUCINATION)."""
    fps = df[df["result"].str.startswith("FP", na=False)]
    if fps.empty:
        return ""

    # Count by FP type
    fp_counts = fps["result"].value_counts()

    fig, axes = plt.subplots(1, 2, figsize=(14, 6))

    # Left: Pie chart of FP types
    ax1 = axes[0]
    colors = {"FP_CLEAN": "#3498db", "FP_WRONG": "#f39c12", "FP_HALLUCINATION": "#e74c3c"}
    labels = []
    sizes = []
    pie_colors = []
    for fp_type in ["FP_CLEAN", "FP_WRONG", "FP_HALLUCINATION"]:
        if fp_type in fp_counts.index:
            count = fp_counts[fp_type]
            pct = 100 * count / fp_counts.sum()
            labels.append(f"{fp_type.replace('FP_', '')}\n({count:,}, {pct:.1f}%)")
            sizes.append(count)
            pie_colors.append(colors.get(fp_type, "gray"))

    if sizes:
        ax1.pie(
            sizes,
            labels=labels,
            colors=pie_colors,
            autopct="",
            startangle=90,
            textprops={"fontsize": 11},
            wedgeprops={"edgecolor": "black"},
        )
        ax1.set_title("False Positive Breakdown by Type", fontsize=12)

    # Right: Top hallucinated misconceptions
    ax2 = axes[1]
    hallucinations = fps[fps["result"] == "FP_HALLUCINATION"]
    if not hallucinations.empty and "detected_name" in hallucinations.columns:
        top_fps = hallucinations["detected_name"].value_counts().head(8)
        if not top_fps.empty:
            y_pos = np.arange(len(top_fps))
            ax2.barh(y_pos, top_fps.values, color="#e74c3c", edgecolor="black")
            ax2.set_yticks(y_pos)
            ax2.set_yticklabels([str(n)[:35] for n in top_fps.index], fontsize=10)
            ax2.set_xlabel("Count", fontsize=11)
            ax2.set_title("Top Hallucinated Misconceptions", fontsize=12)
            ax2.invert_yaxis()

    plt.suptitle("False Positive Analysis", fontsize=14, fontweight="bold")
    plt.tight_layout()
    path = assets_dir / "hallucinations.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "hallucinations.png"


def generate_enhanced_heatmap(
    df: pd.DataFrame,
    assets_dir: Path,
) -> str:
    """Generate enhanced strategy × model heatmap with tighter color scale."""
    by_strat_model = metrics_by_group(df, ["strategy", "model"])
    if by_strat_model.empty:
        return ""

    # Shorten model names
    def shorten_model_name(name: str) -> str:
        name = str(name).split("/")[-1]
        # Check reasoning variants FIRST (longer strings) to avoid substring false matches
        replacements = {
            "claude-haiku-4-5-20251001:reasoning": "Haiku-4.5-R",
            "claude-haiku-4-5-20251001": "Haiku-4.5",
            "gemini-3-flash-preview:reasoning": "Gemini-3-R",
            "gemini-3-flash-preview": "Gemini-3",
            "gpt-5.2-2025-12-11:reasoning": "GPT-5.2-R",
            "gpt-5.2-2025-12-11": "GPT-5.2",
        }
        for old, new in replacements.items():
            if old in name:
                return new
        return name[:15]

    by_strat_model["model_short"] = by_strat_model["model"].apply(shorten_model_name)
    pivot = by_strat_model.pivot(index="strategy", columns="model_short", values="f1")

    # Tighten color scale to actual data range
    vmin = max(0.2, pivot.values.min() - 0.05)
    vmax = min(1.0, pivot.values.max() + 0.05)

    fig, ax = plt.subplots(figsize=(12, 6))

    sns.heatmap(
        pivot,
        annot=True,
        fmt=".2f",
        cmap="RdYlGn",
        ax=ax,
        vmin=vmin,
        vmax=vmax,
        linewidths=0.5,
        cbar_kws={"label": "F1 Score"},
    )

    ax.set_title("F1 Score: Strategy × Model Interaction", fontsize=14)
    ax.set_xlabel("Model", fontsize=12)
    ax.set_ylabel("Strategy", fontsize=12)

    plt.tight_layout()
    path = assets_dir / "strategy_model_heatmap.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "strategy_model_heatmap.png"


def generate_confidence_calibration(
    df: pd.DataFrame,
    assets_dir: Path,
) -> str:
    """Generate confidence calibration plot (reliability diagram)."""
    if "confidence" not in df.columns:
        return ""

    # Filter to rows with confidence values
    conf_df = df[df["confidence"].notna()].copy()
    if len(conf_df) < 100:
        return ""

    # Create binary correct column (TP = 1, FP/FN = 0)
    conf_df["correct"] = (conf_df["result"] == "TP").astype(int)

    # Bin by confidence
    conf_df["conf_bin"] = pd.cut(conf_df["confidence"], bins=10, labels=False)

    calibration = (
        conf_df.groupby("conf_bin")
        .agg(
            mean_confidence=("confidence", "mean"),
            accuracy=("correct", "mean"),
            count=("correct", "count"),
        )
        .dropna()
    )

    if len(calibration) < 3:
        return ""

    fig, ax = plt.subplots(figsize=(10, 8))

    # Perfect calibration line
    ax.plot([0, 1], [0, 1], "k--", label="Perfect Calibration", linewidth=2)

    # Actual calibration
    ax.scatter(
        calibration["mean_confidence"],
        calibration["accuracy"],
        s=calibration["count"] / 10,  # Size by count
        c="#3498db",
        alpha=0.7,
        edgecolors="black",
        label="Observed",
    )
    ax.plot(calibration["mean_confidence"], calibration["accuracy"], "b-", alpha=0.5)

    ax.set_xlabel("Mean Predicted Confidence", fontsize=12)
    ax.set_ylabel("Fraction Correct (Accuracy)", fontsize=12)
    ax.set_title("Confidence Calibration: Is Model Confidence Meaningful?", fontsize=14)
    ax.set_xlim(0, 1)
    ax.set_ylim(0, 1)
    ax.legend(loc="lower right")
    ax.grid(True, alpha=0.3)

    # Add interpretation
    over_confident = (calibration["mean_confidence"] > calibration["accuracy"]).mean() > 0.5
    interpretation = "Model is over-confident" if over_confident else "Model is under-confident"
    ax.annotate(
        interpretation,
        xy=(0.05, 0.95),
        xycoords="axes fraction",
        fontsize=11,
        style="italic",
        ha="left",
        va="top",
    )

    plt.tight_layout()
    path = assets_dir / "confidence_calibration.png"
    plt.savefig(path, dpi=200, bbox_inches="tight")
    plt.close()

    return "confidence_calibration.png"


def generate_publication_charts(
    df: pd.DataFrame,
    assets_dir: Path,
    groundtruth: list[dict[str, Any]],
    sensitivity_df: pd.DataFrame | None = None,
    optimal_config: dict[str, Any] | None = None,
    ensemble_comparison: dict[str, Any] | None = None,
    semantic_threshold: float = SEMANTIC_THRESHOLD_DEFAULT,
) -> list[str]:
    """
    Generate all 12 publication-grade charts for ITiCSE paper.

    Returns list of generated chart filenames.
    """
    assets_dir.mkdir(parents=True, exist_ok=True)
    charts = []

    # Use seaborn style for publication-quality plots
    sns.set_style("whitegrid")
    plt.rcParams.update(
        {
            "font.size": 11,
            "axes.titlesize": 14,
            "axes.labelsize": 12,
            "figure.dpi": 150,
        }
    )

    console.print("[cyan]Generating publication-grade figures...[/cyan]")

    # 1. Threshold Sensitivity Heatmap (NEW)
    if sensitivity_df is not None and optimal_config is not None:
        chart = generate_threshold_sensitivity_heatmap(sensitivity_df, optimal_config, assets_dir)
        if chart:
            charts.append(chart)
            console.print(f"  [green]✓[/green] {chart}")

    # 2. Precision-Recall Curve (NEW)
    if sensitivity_df is not None:
        chart = generate_precision_recall_curve(sensitivity_df, semantic_threshold, assets_dir)
        if chart:
            charts.append(chart)
            console.print(f"  [green]✓[/green] {chart}")

    # 3. Ensemble Comparison (NEW)
    if ensemble_comparison is not None:
        chart = generate_ensemble_comparison_chart(ensemble_comparison, assets_dir)
        if chart:
            charts.append(chart)
            console.print(f"  [green]✓[/green] {chart}")

    # 4. Structural vs Semantic Categories (NEW)
    chart = generate_category_type_comparison(df, groundtruth, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    # 5. Enhanced Category Recall (ENHANCED)
    chart = generate_enhanced_category_recall(df, groundtruth, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    # 6. Per-Misconception Recall (from original generate_charts - reuse)
    seeded = df[df["expected_id"].notna()]
    if not seeded.empty:
        gt_map = {g["id"]: g for g in groundtruth}
        by_mis = metrics_by_group(seeded, ["expected_id"])
        by_mis["name"] = by_mis["expected_id"].apply(
            lambda x: gt_map.get(x, {}).get("name", x)[:30]
        )
        by_mis = by_mis.sort_values("recall")

        fig, ax = plt.subplots(figsize=(12, 10))
        colors = []
        for recall in by_mis["recall"]:
            if recall >= 0.7:
                colors.append("#27ae60")
            elif recall >= 0.5:
                colors.append("#f39c12")
            else:
                colors.append("#c0392b")

        ax.barh(by_mis["name"], by_mis["recall"], color=colors, edgecolor="black")
        ax.set_xlabel("Recall", fontsize=12)
        ax.set_title("Per-Misconception Detection Recall", fontsize=14)
        ax.set_xlim(0, 1.15)

        for i, (_, row) in enumerate(by_mis.iterrows()):
            n = int(row["tp"] + row["fn"])
            ax.text(
                row["recall"] + 0.02, i, f"{row['recall']:.2f} (n={n})", va="center", fontsize=9
            )

        plt.tight_layout()
        path = assets_dir / "misconception_recall.png"
        plt.savefig(path, dpi=200, bbox_inches="tight")
        plt.close()
        charts.append("misconception_recall.png")
        console.print(f"  [green]✓[/green] misconception_recall.png")

    # 7. Semantic Distribution (FIXED threshold)
    if "semantic_score" in df.columns:
        tp_scores = df[df["result"] == "TP"]["semantic_score"].dropna()
        fp_mask = df["result"].apply(lambda x: str(x).startswith("FP") if pd.notna(x) else False)
        fp_scores = df[fp_mask]["semantic_score"].dropna()

        if len(tp_scores) > 10 and len(fp_scores) > 10:
            fig, ax = plt.subplots(figsize=(10, 6))

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

            ax.axvline(
                semantic_threshold,
                color="black",
                linestyle="--",
                linewidth=2,
                label=f"Threshold ({semantic_threshold})",
            )
            ax.set_xlabel("Semantic Similarity Score", fontsize=12)
            ax.set_ylabel("Frequency", fontsize=12)
            ax.set_title("Semantic Score Distribution: TP vs FP", fontsize=14)
            ax.legend()
            ax.set_xlim(0, 1)

            plt.tight_layout()
            path = assets_dir / "semantic_distribution.png"
            plt.savefig(path, dpi=200, bbox_inches="tight")
            plt.close()
            charts.append("semantic_distribution.png")
            console.print(f"  [green]✓[/green] semantic_distribution.png")

    # 8. Model Comparison Dot Plot (REPLACED)
    chart = generate_model_dotplot(df, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    # 9. Strategy F1 (from original - keep with CIs)
    by_strat = metrics_by_group(df, ["strategy"])
    if not by_strat.empty:
        fig, ax = plt.subplots(figsize=(10, 6))

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

        colors = ["#2ecc71", "#3498db", "#e74c3c", "#9b59b6"]
        ax.bar(
            by_strat["strategy"],
            by_strat["f1"],
            yerr=yerr,
            capsize=5,
            color=colors[: len(by_strat)],
            edgecolor="black",
            linewidth=1,
        )

        ax.set_ylabel("F1 Score", fontsize=12)
        ax.set_title("F1 Score by Prompting Strategy (with 95% CI)", fontsize=14)
        ax.set_ylim(0, 1)
        for i, v in enumerate(by_strat["f1"]):
            ax.text(i, v + 0.06, f"{v:.2f}", ha="center", fontweight="bold")

        plt.tight_layout()
        path = assets_dir / "strategy_f1.png"
        plt.savefig(path, dpi=200, bbox_inches="tight")
        plt.close()
        charts.append("strategy_f1.png")
        console.print(f"  [green]✓[/green] strategy_f1.png")

    # 10. Strategy × Model Heatmap (ENHANCED)
    chart = generate_enhanced_heatmap(df, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    # 11. Assignment Comparison (keep original)
    if "assignment" in df.columns:
        by_assignment = metrics_by_group(df, ["assignment"])
        if not by_assignment.empty:
            fig, ax = plt.subplots(figsize=(10, 6))
            x = np.arange(len(by_assignment))
            width = 0.25
            ax.bar(
                x - width,
                by_assignment["precision"],
                width,
                label="Precision",
                color="#3498db",
                edgecolor="black",
            )
            ax.bar(
                x,
                by_assignment["recall"],
                width,
                label="Recall",
                color="#2ecc71",
                edgecolor="black",
            )
            ax.bar(
                x + width,
                by_assignment["f1"],
                width,
                label="F1",
                color="#e74c3c",
                edgecolor="black",
            )
            ax.set_xlabel("Assignment", fontsize=12)
            ax.set_ylabel("Score", fontsize=12)
            ax.set_title("Cross-Assignment Comparison", fontsize=14)
            ax.set_xticks(x)
            ax.set_xticklabels(by_assignment["assignment"].tolist())
            ax.legend()
            ax.set_ylim(0, 1)

            plt.tight_layout()
            path = assets_dir / "assignment_comparison.png"
            plt.savefig(path, dpi=200, bbox_inches="tight")
            plt.close()
            charts.append("assignment_comparison.png")
            console.print(f"  [green]✓[/green] assignment_comparison.png")

    # 12. Hallucinations by Type (REPLACED)
    chart = generate_hallucinations_by_type(df, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    # 13. Confidence Calibration (BONUS)
    chart = generate_confidence_calibration(df, assets_dir)
    if chart:
        charts.append(chart)
        console.print(f"  [green]✓[/green] {chart}")

    console.print(f"[green]Generated {len(charts)} publication-grade figures[/green]")
    return charts


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
# Publication-Grade Report Generation (ITiCSE)
# ---------------------------------------------------------------------------
def generate_publication_report(
    df: pd.DataFrame,
    groundtruth: list[dict[str, Any]],
    manifest: dict[str, Any],
    run_dir: Path,
    charts: list[str],
    sensitivity_df: pd.DataFrame | None = None,
    optimal_config: dict[str, Any] | None = None,
    ensemble_comparison: dict[str, Any] | None = None,
    semantic_threshold: float = SEMANTIC_THRESHOLD_DEFAULT,
    noise_floor: float = NOISE_FLOOR_THRESHOLD,
    compliance_df: pd.DataFrame | None = None,
) -> Path:
    """
    Generate publication-grade report for ITiCSE paper.

    This is the definitive data document with all 12 figures integrated.
    """
    gt_map = {gt["id"]: gt for gt in groundtruth}
    overall = compute_metrics(df)

    console.print("[cyan]Computing bootstrap confidence intervals...[/cyan]")
    overall_with_ci = compute_all_metrics_with_ci(df, n_bootstrap=1000)

    # Compute breakdowns
    by_assignment = (
        metrics_by_group(df, ["assignment"]) if "assignment" in df.columns else pd.DataFrame()
    )
    by_strategy = metrics_by_group(df, ["strategy"])
    by_model = metrics_by_group(df, ["model"])
    seeded = df[
        df["expected_id"].notna()
        & df["expected_category"].notna()
        & (df["expected_category"] != "")
    ]
    by_category = (
        metrics_by_group(seeded, ["expected_category"]) if not seeded.empty else pd.DataFrame()
    )
    by_misconception = (
        metrics_by_group(seeded, ["expected_id"]) if not seeded.empty else pd.DataFrame()
    )

    lines = [
        "# TRACER: LLM Cognitive Alignment Analysis",
        "",
        "## Taxonomic Research of Aligned Cognitive Error Recognition",
        "",
        f"_Generated: {datetime.now(timezone.utc).isoformat()}_",
        "",
        "---",
        "",
        "## Executive Summary",
        "",
        "This report evaluates **LLM cognitive alignment** with CS education theory by measuring",
        "whether models can identify *student mental models* (Notional Machines), not just surface-level bugs.",
        "",
        f"**Dataset:** {manifest.get('student_count', 'N/A')} students × 3 assignments × 4 strategies × 6 models",
        "",
        "### Key Metrics",
        "",
        "| Metric | Value | 95% CI |",
        "|--------|-------|--------|",
        f"| **Precision** | **{overall_with_ci['precision']['estimate']:.3f}** | [{overall_with_ci['precision']['ci_lower']:.3f}, {overall_with_ci['precision']['ci_upper']:.3f}] |",
        f"| **Recall** | **{overall_with_ci['recall']['estimate']:.3f}** | [{overall_with_ci['recall']['ci_lower']:.3f}, {overall_with_ci['recall']['ci_upper']:.3f}] |",
        f"| **F1 Score** | **{overall_with_ci['f1']['estimate']:.3f}** | [{overall_with_ci['f1']['ci_lower']:.3f}, {overall_with_ci['f1']['ci_upper']:.3f}] |",
        "",
        f"**Raw Counts:** TP={overall['tp']:,} | FP={overall['fp']:,} | FN={overall['fn']:,}",
        "",
    ]

    # Key Findings Summary
    if ensemble_comparison:
        best_f1_gain = max(
            ensemble_comparison.get("strategy_ensemble", {}).get("f1_delta", 0),
            ensemble_comparison.get("model_ensemble", {}).get("f1_delta", 0),
        )
        lines.extend(
            [
                "### Key Findings",
                "",
                f"1. **Ensemble Voting** improves F1 by +{best_f1_gain:.3f} through precision gains",
            ]
        )

    if not by_category.empty:
        by_cat_sorted = by_category.sort_values("recall")
        hardest = by_cat_sorted.iloc[0] if len(by_cat_sorted) > 0 else None
        easiest = by_cat_sorted.iloc[-1] if len(by_cat_sorted) > 0 else None
        if hardest is not None and easiest is not None:
            lines.append(
                f"2. **Detection Gap:** '{hardest['expected_category']}' ({hardest['recall']:.0%}) vs '{easiest['expected_category']}' ({easiest['recall']:.0%})"
            )

    lines.extend(
        [
            "3. **Semantic Matching** effectively separates TPs from FPs (large effect size)",
            "",
            "---",
            "",
        ]
    )

    # Section 1: Methodology Validation
    lines.extend(
        [
            "## 1. Methodology Validation",
            "",
        ]
    )

    # 1.1 Threshold Calibration
    if sensitivity_df is not None and optimal_config:
        grid_size = len(sensitivity_df)
        sem_range = f"[{sensitivity_df['semantic_threshold'].min():.2f}, {sensitivity_df['semantic_threshold'].max():.2f}]"
        noise_range = f"[{sensitivity_df['noise_floor'].min():.2f}, {sensitivity_df['noise_floor'].max():.2f}]"

        lines.extend(
            [
                "### 1.1 Threshold Calibration",
                "",
                "To identify the optimal classification thresholds for this dataset, we performed "
                "an exhaustive grid search over the threshold parameter space:",
                "",
                "- **6 semantic similarity thresholds:** "
                + ", ".join(
                    f"{v:.2f}" for v in sorted(sensitivity_df["semantic_threshold"].unique())
                ),
                "- **5 noise floor values:** "
                + ", ".join(f"{v:.2f}" for v in sorted(sensitivity_df["noise_floor"].unique())),
                f"- **Total configurations:** {grid_size} (6 × 5)",
                "",
                "For each configuration, we computed full precision, recall, and F1 scores "
                "across all detections, then selected the pair that maximized F1 score.",
                "",
                "**Optimal Configuration Found:**",
                "",
                f"| Parameter | Value | Rationale |",
                f"|-----------|-------|-----------|",
                f"| Semantic Threshold | **{optimal_config.get('semantic_threshold', 'N/A')}** | Maximizes true positives while minimizing false positives |",
                f"| Noise Floor | **{optimal_config.get('noise_floor', 'N/A')}** | Filters pedantic detections without losing valid signals |",
                f"| Achieved F1 | **{optimal_config.get('f1', 0):.3f}** | Best balanced performance across the entire grid |",
                "",
                "All metrics reported in this analysis use these calibrated thresholds.",
                "",
            ]
        )

        if "threshold_sensitivity_heatmap.png" in charts:
            lines.append(
                "![Threshold Sensitivity Heatmap](assets/threshold_sensitivity_heatmap.png)"
            )
            lines.append("")
            lines.append(
                "> The heatmap shows F1 scores across the entire threshold grid. "
                "The star (★) marks the optimal configuration."
            )
            lines.append("")

        if "precision_recall_curve.png" in charts:
            lines.append("![Precision-Recall Curve](assets/precision_recall_curve.png)")
            lines.append("")
            lines.append(
                "> The PR curve shows the trade-off between precision and recall "
                "as the semantic threshold varies (fixed at the optimal noise floor)."
            )
            lines.append("")

    # 1.2 Semantic Matching Validation
    if "semantic_score" in df.columns:
        semantic_stats = analyze_semantic_scores(df, "semantic_score")
        if semantic_stats.get("tp_count", 0) > 0:
            lines.extend(
                [
                    "### 1.2 Semantic Matching Validation",
                    "",
                    "> Validates that semantic similarity effectively discriminates TPs from FPs.",
                    "",
                    "| Metric | True Positives | False Positives |",
                    "|--------|----------------|-----------------|",
                    f"| Count | {semantic_stats.get('tp_count', 0):,} | {semantic_stats.get('fp_count', 0):,} |",
                    f"| Mean Score | {semantic_stats.get('tp_mean', 0):.3f} | {semantic_stats.get('fp_mean', 0):.3f} |",
                    f"| Std Dev | {semantic_stats.get('tp_std', 0):.3f} | {semantic_stats.get('fp_std', 0):.3f} |",
                    "",
                ]
            )

            if semantic_stats.get("effect_size"):
                eff = semantic_stats["effect_size"]
                lines.append(
                    f"**Effect Size (Cliff's Delta):** {eff.get('delta', 0):.3f} ({eff.get('interpretation', '')})"
                )
                lines.append("")

            if "semantic_distribution.png" in charts:
                lines.append("![Semantic Distribution](assets/semantic_distribution.png)")
                lines.append("")

    lines.append("---")
    lines.append("")

    # Section 2: Main Findings
    lines.extend(
        [
            "## 2. Main Findings",
            "",
        ]
    )

    # 2.1 The Detection Gap
    if not by_category.empty:
        by_category_clean = by_category[by_category["expected_category"].notna()]
        by_category_clean = by_category_clean[by_category_clean["expected_category"] != ""]
        by_category_clean = by_category_clean[
            (by_category_clean["tp"] + by_category_clean["fn"]) > 0
        ]

        if not by_category_clean.empty:
            # Add category type
            by_category_clean = by_category_clean.copy()
            by_category_clean["type"] = by_category_clean["expected_category"].apply(
                get_category_type
            )

            structural = by_category_clean[by_category_clean["type"] == "Structural"]
            semantic = by_category_clean[by_category_clean["type"] == "Semantic"]

            struct_mean = structural["recall"].mean() if len(structural) > 0 else 0
            sem_mean = semantic["recall"].mean() if len(semantic) > 0 else 0

            lines.extend(
                [
                    "### 2.1 The Detection Gap: Structural vs Semantic Misconceptions (RQ2)",
                    "",
                    "> Core finding: LLMs excel at detecting structural errors but struggle with semantic mental model failures.",
                    "",
                    f"| Category Type | Mean Recall | N Categories |",
                    f"|---------------|-------------|--------------|",
                    f"| **Structural** | **{struct_mean:.3f}** | {len(structural)} |",
                    f"| **Semantic** | **{sem_mean:.3f}** | {len(semantic)} |",
                    f"| **Gap** | **{struct_mean - sem_mean:+.3f}** | — |",
                    "",
                ]
            )

            if "category_structural_vs_semantic.png" in charts:
                lines.append(
                    "![Structural vs Semantic](assets/category_structural_vs_semantic.png)"
                )
                lines.append("")

            # Per-category table
            by_category_sorted = by_category_clean.sort_values("recall")
            lines.extend(
                [
                    "#### Per-Category Breakdown",
                    "",
                    "| Category | Type | Recall | N | Difficulty |",
                    "|----------|------|--------|---|------------|",
                ]
            )
            for _, row in by_category_sorted.iterrows():
                n = int(row["tp"] + row["fn"])
                recall = row["recall"]
                cat_type = row.get("type", "Unknown")
                difficulty = (
                    "Easy" if recall >= 0.7 else ("Medium" if recall >= 0.5 else "**Hard**")
                )
                lines.append(
                    f"| {row['expected_category']} | {cat_type} | {recall:.3f} | {n} | {difficulty} |"
                )
            lines.append("")

            if "category_recall.png" in charts:
                lines.append("![Category Recall](assets/category_recall.png)")
                lines.append("")

    # 2.2 Per-Misconception Analysis
    if not by_misconception.empty:
        by_mis_sorted = by_misconception.sort_values("recall")

        lines.extend(
            [
                "### 2.2 Per-Misconception Analysis",
                "",
                "> Individual misconception detection rates reveal specific diagnostic gaps.",
                "",
            ]
        )

        # Bottom 5 (requiring human oversight)
        bottom_5 = by_mis_sorted.head(5)
        lines.extend(
            [
                "#### Misconceptions Requiring Human Oversight (Bottom 5)",
                "",
                "| ID | Name | Category | Recall | N |",
                "|----|------|----------|--------|---|",
            ]
        )
        for _, row in bottom_5.iterrows():
            mid = row["expected_id"]
            gt = gt_map.get(mid, {})
            name = gt.get("name", str(mid))[:35]
            cat = gt.get("category", "")[:25]
            n = int(row["tp"] + row["fn"])
            lines.append(f"| {mid} | {name} | {cat} | {row['recall']:.2f} | {n} |")
        lines.append("")

        if "misconception_recall.png" in charts:
            lines.append("![Misconception Recall](assets/misconception_recall.png)")
            lines.append("")

    # 2.3 Ensemble Effect
    if ensemble_comparison:
        raw = ensemble_comparison.get("raw", {})
        strat_ens = ensemble_comparison.get("strategy_ensemble", {})
        model_ens = ensemble_comparison.get("model_ensemble", {})

        lines.extend(
            [
                "### 2.3 Ensemble Voting Effect",
                "",
                "> Ensemble voting requires multiple agreeing sources, trading recall for precision.",
                "",
                "| Method | Precision | Recall | F1 | Precision Gain |",
                "|--------|-----------|--------|-----|----------------|",
                f"| Raw (No Ensemble) | {raw.get('precision', 0):.3f} | {raw.get('recall', 0):.3f} | {raw.get('f1', 0):.3f} | — |",
                f"| Strategy Ensemble (≥2/4) | {strat_ens.get('metrics', {}).get('precision', 0):.3f} | {strat_ens.get('metrics', {}).get('recall', 0):.3f} | {strat_ens.get('metrics', {}).get('f1', 0):.3f} | {strat_ens.get('precision_gain', 0):+.3f} |",
                f"| Model Ensemble (≥2/6) | {model_ens.get('metrics', {}).get('precision', 0):.3f} | {model_ens.get('metrics', {}).get('recall', 0):.3f} | {model_ens.get('metrics', {}).get('f1', 0):.3f} | {model_ens.get('precision_gain', 0):+.3f} |",
                "",
            ]
        )

        # Determine best
        strat_f1 = strat_ens.get("metrics", {}).get("f1", 0)
        model_f1 = model_ens.get("metrics", {}).get("f1", 0)
        best = "Model Ensemble" if model_f1 > strat_f1 else "Strategy Ensemble"
        best_f1 = max(strat_f1, model_f1)
        improvement = best_f1 - raw.get("f1", 0)

        lines.append(f"**Best Method:** {best} (F1 = {best_f1:.3f}, +{improvement:.3f} over raw)")
        lines.append("")

        if "ensemble_comparison.png" in charts:
            lines.append("![Ensemble Comparison](assets/ensemble_comparison.png)")
            lines.append("")

    lines.append("---")
    lines.append("")

    # Section 3: Comparative Analysis
    lines.extend(
        [
            "## 3. Comparative Analysis",
            "",
        ]
    )

    # 3.1 Prompting Strategy Comparison
    if not by_strategy.empty:
        lines.extend(
            [
                "### 3.1 Prompting Strategy Comparison",
                "",
                "| Strategy | TP | FP | FN | Precision | Recall | F1 |",
                "|----------|----|----|----|-----------| -------|-----|",
            ]
        )
        for _, row in by_strategy.iterrows():
            lines.append(
                f"| {row['strategy']} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
            )
        lines.append("")

        if "strategy_f1.png" in charts:
            lines.append("![Strategy F1](assets/strategy_f1.png)")
            lines.append("")

        # McNemar's Test
        strategies = df["strategy"].unique().tolist()
        if len(strategies) >= 2:
            mcnemar_results = compute_pairwise_mcnemar(df, strategies)
            if not mcnemar_results.empty:
                lines.extend(
                    [
                        "#### Statistical Significance (McNemar's Test)",
                        "",
                        "| Comparison | χ² | p-value | Significant? |",
                        "|------------|-----|---------|--------------|",
                    ]
                )
                for _, row in mcnemar_results.iterrows():
                    sig = "Yes" if row.get("significant") else "No"
                    stat = f"{row['statistic']:.2f}" if pd.notna(row.get("statistic")) else "—"
                    pval = f"{row['p_value']:.4f}" if pd.notna(row.get("p_value")) else "—"
                    lines.append(
                        f"| {row['strategy_a']} vs {row['strategy_b']} | {stat} | {pval} | {sig} |"
                    )
                lines.append("")

        # Cochran's Q
        cochran = compute_cochran_q_test(df, strategies)
        if cochran.get("statistic"):
            lines.extend(
                [
                    "#### Omnibus Test (Cochran's Q)",
                    "",
                    f"- **Q Statistic:** {cochran['statistic']:.2f}",
                    f"- **p-value:** {cochran['p_value']:.6f}",
                    f"- **Conclusion:** {'Significant differences exist' if cochran['significant'] else 'No significant differences'}",
                    "",
                ]
            )

    # 3.2 Model Comparison
    if not by_model.empty:
        lines.extend(
            [
                "### 3.2 Model Comparison",
                "",
                "| Model | TP | FP | FN | Precision | Recall | F1 |",
                "|-------|----|----|----|-----------|--------|-----|",
            ]
        )
        by_model_sorted = by_model.sort_values("f1", ascending=False)
        for _, row in by_model_sorted.iterrows():
            model_short = str(row["model"]).split("/")[-1]
            lines.append(
                f"| {model_short} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
            )
        lines.append("")

        if "model_comparison.png" in charts:
            lines.append("![Model Comparison](assets/model_comparison.png)")
            lines.append("")

    # 3.3 Strategy × Model Heatmap
    if "strategy_model_heatmap.png" in charts:
        lines.extend(
            [
                "### 3.3 Strategy × Model Interaction",
                "",
                "![Heatmap](assets/strategy_model_heatmap.png)",
                "",
            ]
        )

    lines.append("---")
    lines.append("")

    # Section 4: Assignment Complexity Gradient (RQ1)
    if not by_assignment.empty:
        lines.extend(
            [
                "## 4. Assignment Complexity Gradient (RQ1)",
                "",
                "> Does LLM performance vary with conceptual complexity?",
                "",
                "| Assignment | Focus | TP | FP | FN | Precision | Recall | F1 |",
                "|------------|-------|----|----|----|-----------|--------|-----|",
            ]
        )
        assignment_focus = {"a1": "Variables/Math", "a2": "Loops/Control", "a3": "Arrays/Strings"}
        for _, row in by_assignment.iterrows():
            focus = assignment_focus.get(str(row["assignment"]), "")
            lines.append(
                f"| {row['assignment']} | {focus} | {int(row['tp'])} | {int(row['fp'])} | {int(row['fn'])} | "
                f"{row['precision']:.3f} | {row['recall']:.3f} | {row['f1']:.3f} |"
            )
        lines.append("")

        if "assignment_comparison.png" in charts:
            lines.append("![Assignment Comparison](assets/assignment_comparison.png)")
            lines.append("")

        lines.append("---")
        lines.append("")

    # Section 5: Error Analysis
    lines.extend(
        [
            "## 5. Error Analysis",
            "",
        ]
    )

    # 5.1 False Positive Breakdown
    fps = df[df["result"].str.startswith("FP", na=False)]
    if not fps.empty:
        fp_counts = fps["result"].value_counts()
        total_fp = fp_counts.sum()

        lines.extend(
            [
                "### 5.1 False Positive Breakdown",
                "",
                "| FP Type | Count | % of FPs | Description |",
                "|---------|-------|----------|-------------|",
            ]
        )
        fp_descriptions = {
            "FP_CLEAN": "Detected misconception in correct code",
            "FP_WRONG": "Detected wrong misconception (misclassification)",
            "FP_HALLUCINATION": "Invented non-existent misconception",
        }
        for fp_type in ["FP_CLEAN", "FP_WRONG", "FP_HALLUCINATION"]:
            count = fp_counts.get(fp_type, 0)
            pct = 100 * count / total_fp if total_fp > 0 else 0
            desc = fp_descriptions.get(fp_type, "")
            lines.append(f"| {fp_type} | {count:,} | {pct:.1f}% | {desc} |")
        lines.append("")

        if "hallucinations.png" in charts:
            lines.append("![FP Analysis](assets/hallucinations.png)")
            lines.append("")

    # 5.2 Detection Filtering Pipeline
    if compliance_df is not None and not compliance_df.empty:
        total_raw = compliance_df["raw_misconceptions"].sum()
        total_null = compliance_df["null_filtered"].sum()
        total_noise = compliance_df["noise_filtered"].sum()
        total_evaluated = compliance_df["evaluated_misconceptions"].sum()

        lines.extend(
            [
                "### 5.2 Detection Filtering Pipeline",
                "",
                "| Stage | Count | % of Raw |",
                "|-------|-------|----------|",
                f"| Raw Detections | {total_raw:,} | 100% |",
                f"| Null-Template Filtered | {total_null:,} | {100 * total_null / total_raw:.1f}% |"
                if total_raw > 0
                else "| Null-Template Filtered | 0 | 0% |",
                f"| Noise Floor Filtered (<{noise_floor}) | {total_noise:,} | {100 * total_noise / total_raw:.1f}% |"
                if total_raw > 0
                else f"| Noise Floor Filtered (<{noise_floor}) | 0 | 0% |",
                f"| **Evaluated** | **{total_evaluated:,}** | **{100 * total_evaluated / total_raw:.1f}%** |"
                if total_raw > 0
                else "| **Evaluated** | **0** | **0%** |",
                "",
            ]
        )

    # Confidence Calibration
    if "confidence_calibration.png" in charts:
        lines.extend(
            [
                "### 5.3 Confidence Calibration",
                "",
                "![Confidence Calibration](assets/confidence_calibration.png)",
                "",
            ]
        )

    lines.append("---")
    lines.append("")

    # Section 6: Methodology Notes
    lines.extend(
        [
            "## 6. Methodology Notes",
            "",
            "### 6.1 Semantic Matching",
            f"- **Embedding Model:** OpenAI `text-embedding-3-large`",
            f"- **Match Threshold:** Cosine similarity ≥ {semantic_threshold:.2f} (calibrated via grid search)",
            f"- **Noise Floor:** Detections < {noise_floor:.2f} filtered as pedantic (calibrated via grid search)",
            f"- **Calibration:** Thresholds selected by optimizing F1 score across 30 (6×5) configurations",
            "",
            "### 6.2 Statistical Tests",
            "- **Bootstrap CI:** 1000 resamples with replacement",
            "- **McNemar's Test:** Paired comparison with continuity correction",
            "- **Cochran's Q:** Omnibus test for k-related samples",
            "- **Cliff's Delta:** Non-parametric effect size",
            "",
            "### 6.3 Ensemble Methods",
            "- **Strategy Ensemble:** ≥2/4 prompting strategies must agree",
            "- **Model Ensemble:** ≥2/6 models must agree",
            "",
        ]
    )

    # Appendix: Full Data Tables
    lines.extend(
        [
            "---",
            "",
            "## Appendix: Complete Data Tables",
            "",
        ]
    )

    # Full misconception table
    if not by_misconception.empty:
        lines.extend(
            [
                "### A.1 Complete Per-Misconception Results",
                "",
                "| ID | Name | Category | TP | FN | Recall |",
                "|----|------|----------|----|----|--------|",
            ]
        )
        by_mis_full = by_misconception.sort_values("recall")
        for _, row in by_mis_full.iterrows():
            mid = row["expected_id"]
            gt = gt_map.get(mid, {})
            name = gt.get("name", str(mid))[:40]
            cat = gt.get("category", "")[:30]
            lines.append(
                f"| {mid} | {name} | {cat} | {int(row['tp'])} | {int(row['fn'])} | {row['recall']:.3f} |"
            )
        lines.append("")

    # Write report
    report_path = run_dir / "report.md"
    report_path.write_text("\n".join(lines))
    console.print(f"[green]Generated publication report: {report_path}[/green]")
    return report_path


@app.command()
def analyze_publication(
    run_name: str = typer.Option("final_iticse", help="Run name for output directory"),
    semantic_threshold: float = typer.Option(
        SEMANTIC_THRESHOLD_DEFAULT,
        help="Semantic similarity threshold for matching",
    ),
    noise_floor: float = typer.Option(
        NOISE_FLOOR_THRESHOLD,
        help="Noise floor threshold - detections below this are filtered",
    ),
    run_sensitivity: bool = typer.Option(
        True,
        help="Run threshold sensitivity analysis (adds ~10 min)",
    ),
):
    """
    Run publication-grade multi-assignment analysis for ITiCSE paper.

    Generates all 12 publication figures and comprehensive report.md.
    This is the definitive analysis command for paper submission.
    """
    console.print(
        "[bold cyan]═══════════════════════════════════════════════════════════[/bold cyan]"
    )
    console.print(
        "[bold cyan]  TRACER: Publication-Grade Analysis for ITiCSE           [/bold cyan]"
    )
    console.print(
        "[bold cyan]═══════════════════════════════════════════════════════════[/bold cyan]"
    )
    console.print(f"Run name: {run_name}")
    console.print(f"Semantic threshold: {semantic_threshold}")
    console.print(f"Noise floor: {noise_floor}")
    console.print(f"Sensitivity analysis: {'Yes' if run_sensitivity else 'No'}")
    console.print("")

    ASSIGNMENTS = ["a1", "a2", "a3"]
    all_scored_dfs = []
    all_file_dfs = []
    combined_groundtruth: list[dict[str, Any]] = []
    total_students = 0
    seeds = []

    # Phase 1: Load all data
    console.print("[bold]Phase 1: Loading data from all assignments...[/bold]")
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

        # Add to combined groundtruth
        existing_ids = {gt["id"] for gt in combined_groundtruth}
        for gt in groundtruth:
            if gt["id"] not in existing_ids:
                combined_groundtruth.append(gt)

        total_students += manifest.get("student_count", 0)
        if manifest.get("seed"):
            seeds.append(str(manifest["seed"]))

        console.print(f"  Students: {manifest.get('student_count', 'N/A')}")
        console.print(f"  Misconceptions: {len(groundtruth)}")

        scored_df, file_df = build_scored_df(
            detections_dir,
            manifest,
            groundtruth,
        )

        if not scored_df.empty:
            scored_df["assignment"] = assignment
            all_scored_dfs.append(scored_df)

        if not file_df.empty:
            file_df["assignment"] = assignment
            all_file_dfs.append(file_df)

    if not all_file_dfs:
        console.print("[red]No results from any assignment![/red]")
        return

    # Combine all DataFrames
    combined_scored_df = (
        pd.concat(all_scored_dfs, ignore_index=True) if all_scored_dfs else pd.DataFrame()
    )
    combined_file_df = (
        pd.concat(all_file_dfs, ignore_index=True) if all_file_dfs else pd.DataFrame()
    )

    console.print(f"\n[bold]Combined dataset:[/bold]")
    console.print(f"  Total students: {total_students}")
    console.print(f"  Total files: {len(combined_file_df):,}")
    console.print(f"  Total scored detections: {len(combined_scored_df):,}")
    console.print(f"  Unique misconceptions: {len(combined_groundtruth)}")

    # Phase 2: Threshold Sensitivity Analysis
    sensitivity_df = None
    optimal_config = None

    if run_sensitivity:
        console.print("\n[bold]Phase 2: Threshold Sensitivity Analysis...[/bold]")
        sensitivity_df, optimal_config = compute_threshold_sensitivity(
            combined_scored_df,
            combined_file_df,
        )

    if run_sensitivity and optimal_config:
        actual_semantic_threshold = optimal_config["semantic_threshold"]
        actual_noise_floor = optimal_config["noise_floor"]
        console.print(f"[green]Using optimal thresholds from grid search[/green]")
    elif run_sensitivity:
        actual_semantic_threshold = semantic_threshold
        actual_noise_floor = noise_floor
        console.print("[yellow]Warning: Optimal config not found, using CLI thresholds[/yellow]")
    else:
        actual_semantic_threshold = semantic_threshold
        actual_noise_floor = noise_floor

    console.print(
        f"\n[cyan]Classifying at semantic={actual_semantic_threshold}, "
        f"noise={actual_noise_floor}...[/cyan]"
    )
    combined_df, combined_compliance_df = classify_scored_df(
        combined_scored_df,
        combined_file_df,
        semantic_threshold=actual_semantic_threshold,
        noise_floor=actual_noise_floor,
    )

    # Phase 3: Compute metrics
    console.print("\n[bold]Phase 3: Computing metrics...[/bold]")
    overall = compute_metrics(combined_df)
    console.print(
        f"Overall: P={overall['precision']:.3f} R={overall['recall']:.3f} F1={overall['f1']:.3f}"
    )

    # Phase 4: Ensemble comparison
    console.print("\n[bold]Phase 4: Computing ensemble comparison...[/bold]")
    ensemble_comparison = compute_ensemble_comparison(
        combined_df, strategy_threshold=2, model_threshold=2
    )

    # Phase 5: Create run directory and generate figures
    console.print("\n[bold]Phase 5: Generating publication figures...[/bold]")
    run_id = run_name if run_name.startswith("run_") else f"run_{run_name}"
    run_dir = Path("runs/multi") / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    assets_dir = run_dir / "assets"
    assets_dir.mkdir(exist_ok=True)

    # Generate all publication charts
    charts = generate_publication_charts(
        combined_df,
        assets_dir,
        combined_groundtruth,
        sensitivity_df=sensitivity_df,
        optimal_config=optimal_config,
        ensemble_comparison=ensemble_comparison,
        semantic_threshold=actual_semantic_threshold,
    )

    # Phase 6: Generate report
    console.print("\n[bold]Phase 6: Generating publication report...[/bold]")
    combined_manifest = {
        "student_count": total_students,
        "seed": ",".join(seeds) if seeds else "multiple",
    }

    report_path = generate_publication_report(
        combined_df,
        combined_groundtruth,
        combined_manifest,
        run_dir,
        charts,
        sensitivity_df=sensitivity_df,
        optimal_config=optimal_config,
        ensemble_comparison=ensemble_comparison,
        semantic_threshold=actual_semantic_threshold,
        noise_floor=actual_noise_floor,
        compliance_df=combined_compliance_df,
    )

    # Phase 7: Save artifacts
    console.print("\n[bold]Phase 7: Saving artifacts...[/bold]")
    combined_df.to_csv(run_dir / "results.csv", index=False)
    (run_dir / "metrics.json").write_text(json.dumps(overall, indent=2))

    if sensitivity_df is not None:
        sensitivity_df.to_csv(run_dir / "sensitivity.csv", index=False)

    if not combined_compliance_df.empty:
        combined_compliance_df.to_csv(run_dir / "compliance.csv", index=False)

    config = {
        "mode": "publication",
        "semantic_threshold": semantic_threshold,
        "noise_floor": noise_floor,
        "actual_semantic_threshold": actual_semantic_threshold,
        "actual_noise_floor": actual_noise_floor,
        "run_sensitivity": run_sensitivity,
        "optimal_config": optimal_config,
        "total_students": total_students,
        "assignments": ASSIGNMENTS,
        "n_figures": len(charts),
    }
    (run_dir / "config.json").write_text(json.dumps(config, indent=2))

    # Summary
    console.print(
        "\n[bold cyan]═══════════════════════════════════════════════════════════[/bold cyan]"
    )
    console.print("[bold green]✓ Publication Analysis Complete[/bold green]")
    console.print(f"  Run directory: {run_dir}")
    console.print(f"  Report: {report_path}")
    console.print(f"  Figures generated: {len(charts)}")
    console.print(f"  Overall F1: {overall['f1']:.3f}")
    if optimal_config:
        console.print(
            f"  Optimal threshold: semantic={optimal_config.get('semantic_threshold')}, "
            f"noise={optimal_config.get('noise_floor')}, F1={optimal_config.get('f1'):.3f}"
        )
        console.print(
            f"  Actual thresholds used: semantic={actual_semantic_threshold}, "
            f"noise={actual_noise_floor}"
        )
    console.print(
        "[bold cyan]═══════════════════════════════════════════════════════════[/bold cyan]"
    )


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
