"""Main comparison model combining all comparison components."""

from pydantic import BaseModel, ConfigDict, Field

from .confidence_analysis import ConfidenceAnalysis, ModelCharacteristics
from .ensemble import ComparisonMetadata, EnsembleDecision, EnsembleQuality, Flags
from .misconception_analysis import MisconceptionSummary
from .reliability import ReliabilityMetrics
from .score_analysis import (
    CategoryAgreement,
    CategoryInsights,
    PairwiseComparison,
    ScoreSummary,
)


class Comparison(BaseModel):
    """
    Complete comparison and analysis of multiple model evaluations.

    Computed by backend code after collecting all model outputs. Provides
    comprehensive analysis including score statistics, agreement metrics,
    misconception analysis, reliability measures, and ensemble decisions.
    """

    model_config = ConfigDict(extra="forbid")

    score_summary: ScoreSummary = Field(
        ..., description="Aggregate statistics across all grading models"
    )
    pairwise_differences: list[PairwiseComparison] = Field(
        ..., description="Detailed model-vs-model differences (scales to N models)"
    )
    category_agreement: list[CategoryAgreement] = Field(
        ..., description="Per-rubric-category statistics and agreement levels"
    )
    category_insights: CategoryInsights = Field(
        ..., description="High-level insights about category-level agreement and confidence"
    )
    misconception_summary: MisconceptionSummary = Field(
        ..., description="Compare misconception detection across models"
    )
    confidence_analysis: ConfidenceAnalysis = Field(
        ..., description="Examine model confidence patterns and confidence-score relationships"
    )
    model_characteristics: ModelCharacteristics = Field(
        ..., description="Understanding grading tendencies and behavior patterns"
    )
    reliability_metrics: ReliabilityMetrics = Field(
        ..., description="Statistical measures of agreement (critical for publication)"
    )
    ensemble_decision: EnsembleDecision = Field(
        ..., description="Final grade recommendation using ensemble strategies"
    )
    ensemble_quality: EnsembleQuality = Field(
        ..., description="Assess the value and effectiveness of this ensemble"
    )
    flags: Flags = Field(
        ..., description="Automated decision support for human review and research"
    )
    metadata: ComparisonMetadata = Field(..., description="Tracking and versioning information")
