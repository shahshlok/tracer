"""Models for inter-rater reliability metrics."""

from typing import Literal

from pydantic import BaseModel, ConfigDict, Field


class ConfidenceInterval95(BaseModel):
    """95% confidence interval for the true score."""

    model_config = ConfigDict(extra="forbid")

    lower: float = Field(..., description="Lower bound of 95% CI: mean - (1.96 * SEM)")
    upper: float = Field(..., description="Upper bound of 95% CI: mean + (1.96 * SEM)")


class ReliabilityMetrics(BaseModel):
    """
    Statistical measures of agreement between models.

    Critical metrics for publication and establishing credibility of ensemble grading.
    """

    model_config = ConfigDict(extra="forbid")

    pearson_correlation: float = Field(
        ...,
        description="Linear correlation between model scores across categories. Interpretation: >0.9 excellent, 0.7-0.9 strong, 0.4-0.7 moderate, <0.4 weak",
    )
    spearman_correlation: float = Field(
        ..., description="Rank-order correlation (robust to non-linearity and outliers)"
    )
    intraclass_correlation_icc: float = Field(
        ...,
        description="ICC(2,1) for absolute agreement. Interpretation: >0.90 excellent, 0.75-0.90 good, 0.50-0.75 moderate, <0.50 poor",
    )
    cohens_kappa: float | None = Field(
        ..., description="For categorical agreement (e.g., pass/fail); null if not applicable"
    )
    krippendorff_alpha: float = Field(
        ...,
        description="General reliability across multiple raters/categories. Interpretation: >0.80 good reliability, 0.67-0.80 tentative, <0.67 discard",
    )
    reliability_interpretation: Literal["poor", "fair", "moderate", "good", "excellent"] = Field(
        ..., description="Overall assessment: 'poor', 'fair', 'moderate', 'good', or 'excellent'"
    )
    standard_error_of_measurement: float = Field(
        ..., description="SEM = std_dev * sqrt(1 - ICC). Typical error in individual measurements"
    )
    confidence_interval_95: ConfidenceInterval95 = Field(
        ..., description="95% CI for the true score: mean ± (1.96 * SEM)"
    )
