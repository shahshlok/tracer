"""
Utility functions for generating comparison sections of the EvaluationDocument.
"""

import statistics
import itertools
from typing import Dict, List

from pydantic_models.comparison.score_analysis import (
    CategoryDifference,
    LargestCategoryDisagreement,
    PairwiseComparison,
    ScoreSummary,
)
from pydantic_models.models import ModelEvaluation


def generate_score_summary(models: Dict[str, ModelEvaluation]) -> ScoreSummary:
    """
    Generate the ScoreSummary section of the comparison.

    Calculates aggregate statistics across all grading models based on their
    percentage scores.

    Args:
        models: A dictionary mapping model names to their evaluation results.

    Returns:
        A ScoreSummary object containing the calculated statistics.
    """
    # Extract percentage scores from all models
    model_scores = {
        name: eval_data.scores.percentage for name, eval_data in models.items()
    }
    scores = list(model_scores.values())

    if not scores:
        # Handle edge case with no models (though unlikely in practice)
        return ScoreSummary(
            per_model_percentage={},
            mean=0.0,
            median=0.0,
            std_dev=0.0,
            variance=0.0,
            coefficient_of_variation=0.0,
            min=0.0,
            max=0.0,
            range=0.0,
        )

    # Calculate statistics
    mean_val = statistics.mean(scores)
    median_val = statistics.median(scores)
    
    if len(scores) > 1:
        std_dev_val = statistics.stdev(scores)
        variance_val = statistics.variance(scores)
    else:
        std_dev_val = 0.0
        variance_val = 0.0

    # Calculate Coefficient of Variation (CV)
    # Handle division by zero if mean is 0
    if mean_val > 0:
        cv_val = std_dev_val / mean_val
    else:
        cv_val = 0.0

    min_val = min(scores)
    max_val = max(scores)
    range_val = max_val - min_val

    return ScoreSummary(
        per_model_percentage=model_scores,
        mean=round(mean_val, 2),
        median=round(median_val, 2),
        std_dev=round(std_dev_val, 2),
        variance=round(variance_val, 2),
        coefficient_of_variation=round(cv_val, 3),
        min=round(min_val, 2),
        max=round(max_val, 2),
        range=round(range_val, 2),
    )


def generate_pairwise_differences(
    models: Dict[str, ModelEvaluation]
) -> List[PairwiseComparison]:
    """
    Generate pairwise comparisons between all models.

    Args:
        models: A dictionary mapping model names to their evaluation results.

    Returns:
        A list of PairwiseComparison objects for every unique pair of models.
    """
    comparisons = []
    model_names = list(models.keys())

    # Generate all unique pairs (combinations of length 2)
    for name_a, name_b in itertools.combinations(model_names, 2):
        model_a = models[name_a]
        model_b = models[name_b]

        # 1. Total Score Differences
        score_a = model_a.scores.total_points_awarded
        score_b = model_b.scores.total_points_awarded
        max_points = model_a.scores.max_points  # Assuming max points are same for both

        total_diff = score_a - score_b
        
        # Avoid division by zero
        if max_points > 0:
            percent_diff = (total_diff / max_points) * 100
        else:
            percent_diff = 0.0

        # 2. Category Differences
        category_diffs = []
        
        # Create a map for model B's categories for easy lookup
        # Index by both ID and Name to handle model inconsistencies
        cats_b_by_id = {c.category_id: c for c in model_b.category_scores}
        cats_b_by_name = {c.category_name.lower().strip(): c for c in model_b.category_scores}

        for cat_a in model_a.category_scores:
            # Try exact ID match first
            cat_b = cats_b_by_id.get(cat_a.category_id)
            
            # Fallback to Name match
            if not cat_b:
                cat_b = cats_b_by_name.get(cat_a.category_name.lower().strip())
            
            if not cat_b:
                # Should not happen if models follow same rubric, but handle gracefully
                continue

            cat_diff = cat_a.points_awarded - cat_b.points_awarded
            
            if cat_a.max_points > 0:
                cat_percent_max = (cat_diff / cat_a.max_points) * 100
            else:
                cat_percent_max = 0.0

            category_diffs.append(
                CategoryDifference(
                    category_id=cat_a.category_id,
                    category_name=cat_a.category_name,
                    model_a_points=cat_a.points_awarded,
                    model_b_points=cat_b.points_awarded,
                    difference=cat_diff,
                    percent_of_category_max=round(cat_percent_max, 2),
                )
            )

        # 3. Find Largest Disagreement
        if category_diffs:
            # Find category with max absolute percent difference
            largest_disagreement = max(
                category_diffs, key=lambda x: abs(x.percent_of_category_max)
            )
            largest_cat_disagreement = LargestCategoryDisagreement(
                category_id=largest_disagreement.category_id,
                difference_percent=largest_disagreement.percent_of_category_max,
            )
        else:
            # Fallback if no categories (unlikely)
            largest_cat_disagreement = LargestCategoryDisagreement(
                category_id="none", difference_percent=0.0
            )

        comparisons.append(
            PairwiseComparison(
                model_a=name_a,
                model_b=name_b,
                total_points_diff=total_diff,
                percentage_diff=round(percent_diff, 2),
                absolute_percentage_diff=round(abs(percent_diff), 2),
                category_differences=category_diffs,
                largest_category_disagreement=largest_cat_disagreement,
            )
        )

    return comparisons
