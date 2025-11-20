"""
Utility functions for generating comparison sections of the EvaluationDocument.
"""

import statistics
from typing import Dict

from pydantic_models.comparison.score_analysis import ScoreSummary
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
