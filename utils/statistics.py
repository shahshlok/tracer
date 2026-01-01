"""
Statistical analysis module for ITiCSE/SIGCSE-grade rigor.

Provides:
- Bootstrap confidence intervals for precision, recall, F1
- McNemar's test for paired strategy comparison
- Cohen's Kappa for inter-rater reliability
- Effect size calculations (Cohen's d, Cliff's delta)
"""

from __future__ import annotations

from typing import Any, Literal

import numpy as np
import pandas as pd
from scipy import stats


# ---------------------------------------------------------------------------
# Bootstrap Confidence Intervals
# ---------------------------------------------------------------------------
def compute_bootstrap_ci(
    df: pd.DataFrame,
    metric: Literal["precision", "recall", "f1"],
    n_bootstrap: int = 1000,
    confidence_level: float = 0.95,
    random_state: int = 42,
) -> dict[str, float]:
    """
    Compute bootstrap confidence interval for a classification metric.

    Args:
        df: DataFrame with 'result' column containing TP, FP_*, FN values
        metric: Which metric to compute CI for
        n_bootstrap: Number of bootstrap samples
        confidence_level: Confidence level (e.g., 0.95 for 95% CI)
        random_state: Random seed for reproducibility

    Returns:
        Dict with 'estimate', 'ci_lower', 'ci_upper', 'std_error'
    """
    rng = np.random.default_rng(random_state)
    n_samples = len(df)

    if n_samples == 0:
        return {"estimate": 0.0, "ci_lower": 0.0, "ci_upper": 0.0, "std_error": 0.0}

    def compute_metric(sample_df: pd.DataFrame) -> float:
        tp = (sample_df["result"] == "TP").sum()
        fp = sample_df["result"].isin(["FP_CLEAN", "FP_WRONG", "FP_HALLUCINATION"]).sum()
        fn = (sample_df["result"] == "FN").sum()

        if metric == "precision":
            return tp / (tp + fp) if (tp + fp) > 0 else 0.0
        elif metric == "recall":
            return tp / (tp + fn) if (tp + fn) > 0 else 0.0
        else:  # f1
            precision = tp / (tp + fp) if (tp + fp) > 0 else 0.0
            recall = tp / (tp + fn) if (tp + fn) > 0 else 0.0
            return (
                2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0.0
            )

    # Point estimate
    point_estimate = compute_metric(df)

    # Bootstrap resampling
    bootstrap_estimates = []
    for _ in range(n_bootstrap):
        # Resample with replacement
        indices = rng.choice(n_samples, size=n_samples, replace=True)
        sample_df = df.iloc[indices]
        bootstrap_estimates.append(compute_metric(sample_df))

    bootstrap_estimates = np.array(bootstrap_estimates)

    # Calculate CI using percentile method
    alpha = 1 - confidence_level
    ci_lower = np.percentile(bootstrap_estimates, 100 * alpha / 2)
    ci_upper = np.percentile(bootstrap_estimates, 100 * (1 - alpha / 2))
    std_error = np.std(bootstrap_estimates, ddof=1)

    return {
        "estimate": round(point_estimate, 4),
        "ci_lower": round(ci_lower, 4),
        "ci_upper": round(ci_upper, 4),
        "std_error": round(std_error, 4),
    }


def compute_all_metrics_with_ci(
    df: pd.DataFrame,
    n_bootstrap: int = 1000,
    confidence_level: float = 0.95,
) -> dict[str, dict[str, float]]:
    """
    Compute precision, recall, and F1 with bootstrap CIs.

    Returns:
        Dict mapping metric name to CI dict
    """
    return {
        "precision": compute_bootstrap_ci(df, "precision", n_bootstrap, confidence_level),
        "recall": compute_bootstrap_ci(df, "recall", n_bootstrap, confidence_level),
        "f1": compute_bootstrap_ci(df, "f1", n_bootstrap, confidence_level),
    }


# ---------------------------------------------------------------------------
# McNemar's Test for Paired Comparison
# ---------------------------------------------------------------------------
def compute_mcnemar_test(
    df: pd.DataFrame,
    strategy_a: str,
    strategy_b: str,
) -> dict[str, Any]:
    """
    Perform McNemar's test to compare two strategies on the same data.

    This is appropriate because the same student code is analyzed by both strategies,
    making the observations paired.

    Args:
        df: DataFrame with 'strategy', 'student', 'question', 'result' columns
        strategy_a: First strategy name
        strategy_b: Second strategy name

    Returns:
        Dict with 'statistic', 'p_value', 'significant', 'contingency_table'
    """
    # Filter to each strategy
    df_a = df[df["strategy"] == strategy_a].copy()
    df_b = df[df["strategy"] == strategy_b].copy()

    if df_a.empty or df_b.empty:
        return {
            "statistic": None,
            "p_value": None,
            "significant": False,
            "contingency_table": None,
            "error": "One or both strategies have no data",
        }

    # Create keys for pairing
    df_a["key"] = df_a["student"] + "_" + df_a["question"] + "_" + df_a["model"].astype(str)
    df_b["key"] = df_b["student"] + "_" + df_b["question"] + "_" + df_b["model"].astype(str)

    # Get correct/incorrect for each
    df_a["correct_a"] = df_a["result"] == "TP"
    df_b["correct_b"] = df_b["result"] == "TP"

    # Merge on key
    merged = pd.merge(
        df_a[["key", "correct_a"]].drop_duplicates("key"),
        df_b[["key", "correct_b"]].drop_duplicates("key"),
        on="key",
        how="inner",
    )

    if len(merged) < 10:
        return {
            "statistic": None,
            "p_value": None,
            "significant": False,
            "contingency_table": None,
            "error": f"Insufficient paired samples ({len(merged)})",
        }

    # Build 2x2 contingency table
    # Rows: Strategy A (correct/incorrect)
    # Cols: Strategy B (correct/incorrect)
    n_both_correct = ((merged["correct_a"]) & (merged["correct_b"])).sum()
    n_a_only = ((merged["correct_a"]) & (~merged["correct_b"])).sum()
    n_b_only = ((~merged["correct_a"]) & (merged["correct_b"])).sum()
    n_both_wrong = ((~merged["correct_a"]) & (~merged["correct_b"])).sum()

    contingency_table = np.array([[n_both_correct, n_a_only], [n_b_only, n_both_wrong]])

    # McNemar's test (with continuity correction)
    # We only care about the off-diagonal elements (b and c)
    b = n_a_only  # A correct, B wrong
    c = n_b_only  # A wrong, B correct

    if b + c == 0:
        return {
            "statistic": 0.0,
            "p_value": 1.0,
            "significant": False,
            "contingency_table": contingency_table.tolist(),
            "interpretation": "No discordant pairs; strategies perform identically",
        }

    # McNemar's chi-squared statistic with continuity correction
    statistic = (abs(b - c) - 1) ** 2 / (b + c)
    p_value = 1 - stats.chi2.cdf(statistic, df=1)

    return {
        "statistic": round(float(statistic), 4),
        "p_value": round(float(p_value), 6),
        "significant": p_value < 0.05,
        "contingency_table": contingency_table.tolist(),
        "n_pairs": len(merged),
        "a_better_count": int(b),
        "b_better_count": int(c),
        "interpretation": (
            f"{strategy_a} wins {b} cases, {strategy_b} wins {c} cases"
            + (" (statistically significant)" if p_value < 0.05 else " (not significant)")
        ),
    }


def compute_pairwise_mcnemar(
    df: pd.DataFrame,
    strategies: list[str] | None = None,
) -> pd.DataFrame:
    """
    Compute McNemar's test for all pairs of strategies.

    Returns:
        DataFrame with pairwise comparison results
    """
    if strategies is None:
        strategies = df["strategy"].unique().tolist()

    results = []
    for i, strat_a in enumerate(strategies):
        for strat_b in strategies[i + 1 :]:
            test_result = compute_mcnemar_test(df, strat_a, strat_b)
            results.append(
                {
                    "strategy_a": strat_a,
                    "strategy_b": strat_b,
                    "statistic": test_result.get("statistic"),
                    "p_value": test_result.get("p_value"),
                    "significant": test_result.get("significant"),
                    "a_wins": test_result.get("a_better_count"),
                    "b_wins": test_result.get("b_better_count"),
                    "interpretation": test_result.get("interpretation", test_result.get("error")),
                }
            )

    return pd.DataFrame(results)


# ---------------------------------------------------------------------------
# Cochran's Q Test (For >2 Strategies)
# ---------------------------------------------------------------------------
def compute_cochran_q_test(
    df: pd.DataFrame,
    strategies: list[str] | None = None,
) -> dict[str, Any]:
    """
    Perform Cochran's Q test to determine if there are significant differences
    among multiple strategies on the same data.

    This is the extension of McNemar's test for >2 groups.

    Returns:
        Dict with 'statistic', 'p_value', 'significant', 'df'
    """
    if strategies is None:
        strategies = sorted(df["strategy"].unique().tolist())

    if len(strategies) < 2:
        return {"error": "Need at least 2 strategies"}

    # Create binary matrix: rows = samples, cols = strategies
    # 1 = correct (TP), 0 = incorrect

    # Build sample keys
    df["key"] = df["student"] + "_" + df["question"] + "_" + df["model"].astype(str)
    df["correct"] = (df["result"] == "TP").astype(int)

    # Pivot to get matrix
    pivot = df.pivot_table(
        index="key",
        columns="strategy",
        values="correct",
        aggfunc="max",
    ).dropna()

    if len(pivot) < 10:
        return {"error": f"Insufficient complete cases ({len(pivot)})"}

    # Filter to requested strategies
    pivot = pivot[[s for s in strategies if s in pivot.columns]]

    if len(pivot.columns) < 2:
        return {"error": "Not enough strategies with data"}

    # Cochran's Q statistic
    X = pivot.values
    n, k = X.shape
    row_sums = X.sum(axis=1)
    col_sums = X.sum(axis=0)
    total = X.sum()

    # Q = (k-1) * [k * sum(col_sums^2) - total^2] / [k * total - sum(row_sums^2)]
    numerator = (k - 1) * (k * np.sum(col_sums**2) - total**2)
    denominator = k * total - np.sum(row_sums**2)

    if denominator == 0:
        return {"statistic": 0.0, "p_value": 1.0, "significant": False, "df": k - 1}

    Q = numerator / denominator
    p_value = 1 - stats.chi2.cdf(Q, df=k - 1)

    return {
        "statistic": round(float(Q), 4),
        "p_value": round(float(p_value), 6),
        "significant": p_value < 0.05,
        "df": k - 1,
        "n_samples": n,
        "strategies": strategies,
    }


# ---------------------------------------------------------------------------
# Cohen's Kappa (Inter-Rater Agreement)
# ---------------------------------------------------------------------------
def compute_cohens_kappa(
    predictions: list[str],
    ground_truth: list[str],
) -> dict[str, float]:
    """
    Compute Cohen's Kappa for agreement between LLM predictions and ground truth.

    Args:
        predictions: List of predicted category IDs
        ground_truth: List of true category IDs

    Returns:
        Dict with 'kappa', 'interpretation'
    """
    if len(predictions) != len(ground_truth):
        return {"kappa": 0.0, "error": "Length mismatch"}

    n = len(predictions)
    if n == 0:
        return {"kappa": 0.0, "error": "Empty lists"}

    # Get all unique labels
    labels = list(set(predictions) | set(ground_truth))
    label_to_idx = {label: i for i, label in enumerate(labels)}
    k = len(labels)

    # Build confusion matrix
    confusion = np.zeros((k, k), dtype=int)
    for pred, true in zip(predictions, ground_truth):
        confusion[label_to_idx[pred], label_to_idx[true]] += 1

    # Observed agreement
    p_o = np.trace(confusion) / n

    # Expected agreement (by chance)
    row_sums = confusion.sum(axis=1)
    col_sums = confusion.sum(axis=0)
    p_e = np.sum(row_sums * col_sums) / (n * n)

    if p_e == 1:
        kappa = 1.0
    else:
        kappa = (p_o - p_e) / (1 - p_e)

    # Interpretation
    if kappa < 0:
        interp = "Poor (worse than chance)"
    elif kappa < 0.20:
        interp = "Slight agreement"
    elif kappa < 0.40:
        interp = "Fair agreement"
    elif kappa < 0.60:
        interp = "Moderate agreement"
    elif kappa < 0.80:
        interp = "Substantial agreement"
    else:
        interp = "Almost perfect agreement"

    return {
        "kappa": round(float(kappa), 4),
        "observed_agreement": round(float(p_o), 4),
        "expected_agreement": round(float(p_e), 4),
        "interpretation": interp,
    }


# ---------------------------------------------------------------------------
# Effect Size (Cliff's Delta for Non-Parametric)
# ---------------------------------------------------------------------------
def compute_cliffs_delta(
    group_a: list[float],
    group_b: list[float],
) -> dict[str, Any]:
    """
    Compute Cliff's Delta effect size for comparing two groups.

    This is a non-parametric effect size suitable for ordinal data.

    Returns:
        Dict with 'delta', 'interpretation'
    """
    n_a, n_b = len(group_a), len(group_b)
    if n_a == 0 or n_b == 0:
        return {"delta": 0.0, "error": "Empty group"}

    # Count dominance
    greater = sum(1 for a in group_a for b in group_b if a > b)
    less = sum(1 for a in group_a for b in group_b if a < b)

    delta = (greater - less) / (n_a * n_b)

    # Interpretation (using Cliff's thresholds)
    abs_delta = abs(delta)
    if abs_delta < 0.147:
        interp = "Negligible"
    elif abs_delta < 0.33:
        interp = "Small"
    elif abs_delta < 0.474:
        interp = "Medium"
    else:
        interp = "Large"

    return {
        "delta": round(float(delta), 4),
        "interpretation": interp,
        "direction": "A > B" if delta > 0 else ("B > A" if delta < 0 else "Equal"),
    }


# ---------------------------------------------------------------------------
# Semantic Similarity Analysis
# ---------------------------------------------------------------------------
def analyze_semantic_scores(
    df: pd.DataFrame,
    score_column: str = "semantic_score",
) -> dict[str, Any]:
    """
    Analyze the distribution of semantic similarity scores.

    Useful for understanding the "quality" of matches.

    Returns:
        Statistics about score distributions for TP vs FP
    """
    tp_scores = df[df["result"] == "TP"][score_column].dropna()
    fp_scores = df[df["result"].str.startswith("FP")][score_column].dropna()

    result = {
        "tp_count": len(tp_scores),
        "fp_count": len(fp_scores),
    }

    if len(tp_scores) > 0:
        result["tp_mean"] = round(float(tp_scores.mean()), 4)
        result["tp_std"] = round(float(tp_scores.std()), 4)
        result["tp_median"] = round(float(tp_scores.median()), 4)
        result["tp_q25"] = round(float(tp_scores.quantile(0.25)), 4)
        result["tp_q75"] = round(float(tp_scores.quantile(0.75)), 4)

    if len(fp_scores) > 0:
        result["fp_mean"] = round(float(fp_scores.mean()), 4)
        result["fp_std"] = round(float(fp_scores.std()), 4)
        result["fp_median"] = round(float(fp_scores.median()), 4)
        result["fp_q25"] = round(float(fp_scores.quantile(0.25)), 4)
        result["fp_q75"] = round(float(fp_scores.quantile(0.75)), 4)

    # Statistical test for difference
    if len(tp_scores) >= 5 and len(fp_scores) >= 5:
        # Mann-Whitney U test (non-parametric)
        stat, p_value = stats.mannwhitneyu(tp_scores, fp_scores, alternative="greater")
        result["separation_test"] = {
            "statistic": round(float(stat), 4),
            "p_value": round(float(p_value), 6),
            "significant": p_value < 0.05,
            "interpretation": (
                "TP scores significantly higher than FP scores"
                if p_value < 0.05
                else "No significant difference in score distributions"
            ),
        }

        # Effect size
        effect = compute_cliffs_delta(tp_scores.tolist(), fp_scores.tolist())
        result["effect_size"] = effect

    return result
