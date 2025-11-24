"""Models for confidence analysis and model characteristics."""

from typing import Literal

from pydantic import BaseModel, ConfigDict, Field


class ConfidenceRange(BaseModel):
    """Statistical range for confidence values."""

    model_config = ConfigDict(extra="forbid")

    mean: float = Field(..., description="Mean confidence")
    std_dev: float = Field(..., description="Standard deviation of confidence")
    min: float = Field(..., description="Minimum confidence")
    max: float = Field(..., description="Maximum confidence")


class ConfidenceStatsPerCategory(BaseModel):
    """Confidence statistics for a specific category."""

    model_config = ConfigDict(extra="forbid")
    mean: float = Field(..., description="Mean confidence for this category")
    std_dev: float = Field(..., description="Standard deviation of confidence")


class HighConfidenceDisagreement(BaseModel):
    """
    Case where models are confident (>0.8) but disagree significantly.

    Particularly interesting for research.
    """

    model_config = ConfigDict(extra="forbid")
    category_id: str = Field(..., description="Category where disagreement occurs")
    model_confidences: dict[str, float] = Field(
        ..., description="Map of model name to confidence value"
    )
    score_difference: int = Field(..., description="Raw score difference between models")
    percent_difference: float = Field(..., description="Percentage difference in scores")
    flag: str = Field(..., description="Flag for this case (e.g., 'confident_disagreement')")


class LowConfidenceCategory(BaseModel):
    """Category where any model has confidence below threshold."""

    model_config = ConfigDict(extra="forbid")
    category_id: str = Field(..., description="Category ID")
    min_confidence: float = Field(..., description="Lowest confidence value")
    models_with_low_confidence: list[str] = Field(
        ..., description="List of models with confidence below threshold"
    )


class ConfidenceAnalysis(BaseModel):
    """
    Examine model confidence patterns and confidence-score relationships.

    Helps understand how confident models are in their assessments and whether
    confidence correlates with scores or disagreements.
    """

    model_config = ConfigDict(extra="forbid")

    overall_misconception_confidence: ConfidenceRange = Field(
        ..., description="Overall confidence statistics for misconception detection"
    )
    per_category_confidence: dict[str, ConfidenceStatsPerCategory] = Field(
        ..., description="Confidence statistics per category. Key: category_id"
    )
    confidence_score_correlation: float = Field(
        ...,
        description="Correlation between model confidence and scores awarded. High positive = models confident when giving high/low scores. Low/negative = confidence doesn't track with scores",
    )
    high_confidence_disagreements: list[HighConfidenceDisagreement] = Field(
        ..., description="Cases where models are confident (>0.8) but disagree significantly"
    )
    low_confidence_categories: list[LowConfidenceCategory] = Field(
        ..., description="Categories where any model has confidence < 0.7"
    )


class StrictnessRanking(BaseModel):
    """Model ranking by strictness (lowest average score = strictest)."""

    model_config = ConfigDict(extra="forbid")
    rank: int = Field(..., description="Rank (1 = strictest)")
    model: str = Field(..., description="Model name")
    average_score: float = Field(..., description="Average score given by this model")
    strictness_label: Literal["strict", "moderate", "lenient"] = Field(
        ..., description="Label: 'strict', 'moderate', or 'lenient' (based on deviation from mean)"
    )
    deviation_from_mean: float = Field(
        ..., description="How far this model's average deviates from the overall mean"
    )


class ConsistencyScore(BaseModel):
    """Measure of internal consistency across categories for a model."""

    model_config = ConfigDict(extra="forbid")
    category_cv: float = Field(
        ..., description="CV of category scores (higher = more variance across categories)"
    )
    label: Literal["high_consistency", "moderate_consistency", "low_consistency"] = Field(
        ..., description="Label: 'high_consistency', 'moderate_consistency', 'low_consistency'"
    )


class ModelCharacteristics(BaseModel):
    """
    Understanding grading tendencies and behavior patterns.

    Analyzes how different models behave in terms of strictness, consistency,
    and misconception detection.
    """

    model_config = ConfigDict(extra="forbid")

    strictness_ranking: list[StrictnessRanking] = Field(
        ..., description="Models ordered by average score given (lowest = strictest)"
    )
    consistency_scores: dict[str, ConsistencyScore] = Field(
        ...,
        description="How internally consistent each model is across categories. Key: model_name",
    )
    misconception_detection_rate: dict[str, int] = Field(
        ..., description="Number of misconceptions detected per model. Key: model_name"
    )
    average_misconception_confidence: dict[str, float] = Field(
        ..., description="Average confidence in misconception detection per model. Key: model_name"
    )
    reasoning_depth: dict[str, float] = Field(
        ...,
        description="Average tokens used in reasoning across categories per model. Key: model_name",
    )
