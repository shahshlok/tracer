"""Models for ensemble decision making and quality assessment."""

from datetime import datetime
from typing import Literal

from pydantic import BaseModel, ConfigDict, Field


class AlternativeScores(BaseModel):
    """Alternative scoring methods for the ensemble."""

    model_config = ConfigDict(extra="forbid")

    mean: float = Field(..., description="Arithmetic mean")
    median: float = Field(..., description="Median score")
    weighted_mean: float = Field(..., description="Weighted mean (if using model-specific weights)")
    trimmed_mean: float = Field(..., description="Mean after removing outliers (if ≥3 models)")


class EnsembleDecision(BaseModel):
    """
    Final grade recommendation using ensemble strategies.

    Combines multiple model assessments into a single recommended grade.
    """

    model_config = ConfigDict(extra="forbid")

    recommended_score: float = Field(..., description="Final recommended score")
    scoring_method: Literal["mean", "median", "weighted", "trimmed_mean"] = Field(
        ...,
        description="How the score was computed: 'mean', 'median', 'weighted', or 'trimmed_mean'",
    )
    alternative_scores: AlternativeScores = Field(
        ..., description="Alternative scoring methods for comparison"
    )
    weights_used: dict[str, float] = Field(
        ..., description="Weights applied to each model. Key: model_name"
    )
    weighting_rationale: Literal[
        "equal", "historical_accuracy", "confidence_based", "performance_calibrated"
    ] = Field(
        ...,
        description="Rationale for weights: 'equal', 'historical_accuracy', 'confidence_based', or 'performance_calibrated'",
    )
    confidence_in_decision: float = Field(
        ...,
        description="Based on model agreement (high agreement = high confidence). Computed from: 1 - (CV or normalized_variance)",
    )
    decision_uncertainty: float = Field(..., description="Standard error of the ensemble decision")
    letter_grade: str = Field(
        ..., description="Letter grade if applicable (based on configurable thresholds)"
    )
    pass_fail: str = Field(..., description="Pass/fail status if applicable")
    consensus_level: str = Field(
        ...,
        description="Qualitative assessment: 'strong' (CV<0.10), 'moderate' (0.10≤CV<0.20), 'weak' (0.20≤CV<0.30), 'divided' (CV≥0.30)",
    )


class EnsembleQuality(BaseModel):
    """
    Assess the value and effectiveness of this ensemble.

    Helps determine if the ensemble adds value over a single model.
    """

    model_config = ConfigDict(extra="forbid")

    diversity_score: float = Field(
        ...,
        description="Normalized measure of how different model assessments are. Computed from CV or variance. Sweet spot: 0.05-0.15 (enough diversity to add value, not so much to distrust)",
    )
    redundancy_score: float = Field(
        ...,
        description="Proportion of misconceptions flagged by multiple models. High redundancy (>0.7) = reliable, consistent detection. Low redundancy (<0.4) = models catching different issues (complementary)",
    )
    complementarity_score: float = Field(
        ...,
        description="Proportion of misconceptions unique to individual models. Computed as: 1 - redundancy_score",
    )
    overall_ensemble_value: Literal["excellent", "high", "moderate", "low", "poor"] = Field(
        ...,
        description="Qualitative assessment: 'excellent', 'high', 'moderate', 'low', or 'poor'. Considers: diversity, redundancy, reliability, agreement",
    )
    recommendation: Literal[
        "ensemble_reliable",
        "ensemble_adds_value",
        "single_model_sufficient",
        "needs_more_models",
        "too_divergent",
    ] = Field(
        ...,
        description="Action recommendation: 'ensemble_reliable' (use ensemble grade confidently), 'ensemble_adds_value' (ensemble better than single model), 'single_model_sufficient' (models too similar), 'needs_more_models' (insufficient coverage), 'too_divergent' (models disagree too much)",
    )
    confidence_improvement_vs_single: float = Field(
        ...,
        description="How much more confident we are with ensemble vs single model. Positive = ensemble reduces uncertainty",
    )


class Flags(BaseModel):
    """
    Automated decision support for human review and research.

    Flags unusual cases that may need human attention or are interesting for research.
    """

    model_config = ConfigDict(extra="forbid")

    needs_human_review: bool = Field(
        ..., description="Boolean flag for escalation to TA/instructor"
    )
    review_urgency: Literal["none", "low", "medium", "high", "critical"] = Field(
        ..., description="Urgency level: 'none', 'low', 'medium', 'high', or 'critical'"
    )
    review_reasons: list[
        Literal[
            "large_score_gap",
            "boundary_score",
            "low_confidence",
            "high_confidence_disagreement",
            "outlier_detected",
            "category_conflict",
            "misconception_mismatch",
            "low_reliability",
        ]
    ] = Field(
        ...,
        description="Reasons for review (empty if none). Possible: 'large_score_gap', 'boundary_score', 'low_confidence', 'high_confidence_disagreement', 'outlier_detected', 'category_conflict', 'misconception_mismatch', 'low_reliability'",
    )
    overall_agreement: Literal["perfect", "high", "medium", "low", "conflicted"] = Field(
        ...,
        description="Qualitative summary based on CV, ICC, correlation: 'perfect', 'high', 'medium', 'low', or 'conflicted'",
    )
    interesting_for_research: bool = Field(
        ..., description="Flag unusual or valuable cases for deeper analysis"
    )
    research_interest_reasons: list[
        Literal[
            "high_inter_rater_reliability",
            "low_inter_rater_reliability",
            "strong_model_agreement",
            "strong_model_disagreement",
            "unique_misconception_pattern",
            "confidence_score_anomaly",
            "boundary_case",
            "outlier_model_behavior",
            "category_specific_patterns",
        ]
    ] = Field(
        ...,
        description="Why this case is interesting for research. Possible: 'high_inter_rater_reliability', 'low_inter_rater_reliability', 'strong_model_agreement', 'strong_model_disagreement', 'unique_misconception_pattern', 'confidence_score_anomaly', 'boundary_case', 'outlier_model_behavior', 'category_specific_patterns'",
    )
    recommended_actions: list[
        Literal[
            "accept_ensemble_grade",
            "review_recommended",
            "review_required",
            "investigate_model_calibration",
            "refine_rubric",
            "add_to_training_set",
            "flag_for_research",
        ]
    ] = Field(
        ...,
        description="What should happen next. Possible: 'accept_ensemble_grade', 'review_recommended', 'review_required', 'investigate_model_calibration', 'refine_rubric', 'add_to_training_set', 'flag_for_research'",
    )


class ThresholdsConfig(BaseModel):
    """Configurable thresholds used in flagging and categorization."""

    model_config = ConfigDict(extra="forbid")

    human_review_score_diff_percent: float = Field(
        ..., description="Score difference threshold for human review (%)"
    )
    boundary_score_margin_percent: float = Field(
        ..., description="Margin around pass/fail boundary (%)"
    )
    low_confidence_threshold: float = Field(
        ..., ge=0.0, le=1.0, description="Threshold for low confidence (0-1)"
    )
    high_confidence_threshold: float = Field(
        ..., ge=0.0, le=1.0, description="Threshold for high confidence (0-1)"
    )
    outlier_std_devs: float = Field(
        ..., description="Number of standard deviations for outlier detection"
    )
    cv_high_agreement_max: float = Field(
        ..., description="Maximum CV for high agreement classification"
    )
    cv_medium_agreement_max: float = Field(
        ..., description="Maximum CV for medium agreement classification"
    )
    icc_excellent_min: float = Field(..., description="Minimum ICC for excellent reliability")
    icc_good_min: float = Field(..., description="Minimum ICC for good reliability")


class ComparisonMetadata(BaseModel):
    """Tracking and versioning information for comparison computation."""

    model_config = ConfigDict(extra="forbid")

    computed_at: datetime = Field(..., description="When the comparison was computed")
    computation_version: str = Field(..., description="Version of comparison algorithm/metrics")
    models_evaluated: list[str] = Field(..., description="List of model names evaluated")
    num_models: int = Field(..., description="Number of models in the comparison")
    thresholds_config: ThresholdsConfig = Field(
        ..., description="Configurable thresholds used in flagging/categorization"
    )
