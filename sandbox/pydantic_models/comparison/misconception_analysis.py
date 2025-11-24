"""Models for misconception detection analysis across models."""

from pydantic import BaseModel, ConfigDict, Field


class MisconceptionOverlap(BaseModel):
    """Pairwise overlap statistics for misconception detection."""

    model_config = ConfigDict(extra="forbid")

    shared: int = Field(..., description="Number of misconceptions identified by both models")
    only_model_a: int = Field(..., description="Misconceptions found only by model A")
    only_model_b: int = Field(..., description="Misconceptions found only by model B")
    jaccard_similarity: float = Field(
        ..., description="Jaccard index: shared / (model_a + model_b - shared)"
    )


class MisconceptionSummary(BaseModel):
    """
    Compare misconception detection across models.

    Shows how models agree/disagree on identifying student misconceptions.
    """

    model_config = ConfigDict(extra="forbid")

    total_by_model: dict[str, int] = Field(
        ..., description="Map of model name to total misconceptions detected"
    )
    total_unique_misconceptions: int = Field(
        ..., description="Total distinct misconceptions across all models (after deduplication)"
    )
    unique_to_single_model: int = Field(
        ...,
        description="Misconceptions found by only one model (potential blind spots or false positives)",
    )
    consensus_misconceptions: int = Field(
        ..., description="Misconceptions identified by 2+ models (higher confidence)"
    )
    consensus_ratio: float = Field(
        ...,
        description="consensus / total_unique. High ratio (>0.7) = models agree on what's wrong. Low ratio (<0.4) = models see different issues",
    )
    average_misconceptions_per_model: float = Field(
        ..., description="Average number of misconceptions per model"
    )
    misconception_overlap_matrix: dict[str, MisconceptionOverlap] = Field(
        ...,
        description="Pairwise overlap between models (scales to N models). Key format: 'model_a_vs_model_b'",
    )
