"""Models for score comparison and agreement analysis."""

from typing import Literal

from pydantic import BaseModel, ConfigDict, Field


class ScoreSummary(BaseModel):
    """
    Aggregate statistics across all grading models.

    Provides measures of central tendency and spread for model scores.
    """

    model_config = ConfigDict(extra="forbid")

    per_model_percentage: dict[str, float] = Field(
        ..., description="Map of model name to percentage score"
    )
    mean: float = Field(..., description="Arithmetic mean of all model scores")
    median: float = Field(..., description="Median score (middle value; robust to outliers)")
    std_dev: float = Field(..., description="Standard deviation (measure of spread)")
    variance: float = Field(..., description="Variance (σ²)")
    coefficient_of_variation: float = Field(
        ...,
        description="CV = std_dev / mean. Measures relative variability (lower = more consistent grading). Typical: <0.1 = low variance, 0.1-0.2 = moderate, >0.2 = high",
    )
    min: float = Field(..., description="Minimum score across all models")
    max: float = Field(..., description="Maximum score across all models")
    range: float = Field(..., description="Total spread (max - min)")


class CategoryDifference(BaseModel):
    """Difference in scores for a specific category between two models."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Rubric category ID")
    category_name: str = Field(..., description="Human-readable category name")
    model_a_points: int = Field(..., description="Points awarded by model A")
    model_b_points: int = Field(..., description="Points awarded by model B")
    difference: int = Field(..., description="Raw point difference (model_a - model_b)")
    percent_of_category_max: float = Field(
        ...,
        description="(difference / max_points) * 100. Shows how significant the disagreement is for this category",
    )


class LargestCategoryDisagreement(BaseModel):
    """Identifies the category with the largest disagreement."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Rubric category ID with largest disagreement")
    difference_percent: float = Field(
        ..., description="Highest percent_of_category_max among all categories"
    )


class PairwiseComparison(BaseModel):
    """
    Detailed model-vs-model differences.

    Scales to N models by creating pairwise comparisons.
    """

    model_config = ConfigDict(extra="forbid")

    model_a: str = Field(..., description="First model name")
    model_b: str = Field(..., description="Second model name")
    total_points_diff: int = Field(..., description="Raw point difference (model_a - model_b)")
    percentage_diff: float = Field(..., description="Percentage point difference")
    absolute_percentage_diff: float = Field(
        ..., description="|percentage_diff| for threshold checks"
    )
    category_differences: list[CategoryDifference] = Field(
        ..., description="Per-category comparison between the two models"
    )
    largest_category_disagreement: LargestCategoryDisagreement = Field(
        ..., description="Category with the largest disagreement between models"
    )


class CategoryStatistics(BaseModel):
    """Statistical measures for a category."""

    model_config = ConfigDict(extra="forbid")

    mean: float = Field(..., description="Mean score across models")
    median: float = Field(..., description="Median score across models")
    std_dev: float = Field(..., description="Standard deviation")
    variance: float = Field(..., description="Variance")
    coefficient_of_variation: float = Field(..., description="CV for this category specifically")
    range: float = Field(..., description="Range (max - min)")


class ConfidenceStats(BaseModel):
    """Confidence statistics for a category."""

    model_config = ConfigDict(extra="forbid")

    mean_confidence: float = Field(
        ..., description="Mean confidence across models for this category"
    )
    confidence_range: float = Field(
        ..., description="Max - min confidence across models for this category"
    )


class CategoryAgreement(BaseModel):
    """
    Per-rubric-category statistics and agreement levels.

    Shows how models agree on specific grading categories.
    """

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Rubric category ID")
    category_name: str = Field(..., description="Human-readable category name")
    max_points: int = Field(..., description="Maximum points for this category")
    all_model_scores: dict[str, int] = Field(
        ..., description="Map of model name to score for this category"
    )
    statistics: CategoryStatistics = Field(
        ..., description="Statistical measures for this category"
    )
    agreement_level: Literal["perfect", "high", "medium", "low"] = Field(
        ...,
        description="Categorization based on CV or std_dev: 'perfect' (std_dev=0), 'high' (CV<0.15), 'medium' (0.15≤CV<0.30), 'low' (CV≥0.30)",
    )
    normalized_variance: float = Field(
        ...,
        description="variance / max_points². Allows cross-category comparison (accounts for different max_points)",
    )
    confidence_stats: ConfidenceStats = Field(
        ..., description="Confidence statistics for this category"
    )


class MostControversialCategory(BaseModel):
    """Category with highest disagreement."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Category ID")
    category_name: str = Field(..., description="Category name")
    cv: float = Field(..., description="Coefficient of variation")
    reason: str = Field(..., description="Why this category is most controversial")


class MostAgreedCategory(BaseModel):
    """Category with highest agreement."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Category ID")
    category_name: str = Field(..., description="Category name")
    cv: float = Field(..., description="Coefficient of variation")
    reason: str = Field(..., description="Why this category has most agreement")


class LowestConfidenceCategory(BaseModel):
    """Category with lowest model confidence."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Category ID")
    mean_confidence: float = Field(..., description="Mean confidence across models")
    reason: str = Field(..., description="Why this category has lowest confidence")


class CategoryInsights(BaseModel):
    """
    High-level insights about category-level agreement and confidence.
    """

    model_config = ConfigDict(extra="forbid")

    most_controversial: MostControversialCategory = Field(
        ..., description="Category with highest disagreement"
    )
    most_agreed: MostAgreedCategory = Field(
        ..., description="Category with perfect/highest agreement"
    )
    lowest_confidence: LowestConfidenceCategory = Field(
        ..., description="Category where models are least confident"
    )
