"""Root evaluation document model."""

from datetime import datetime

from pydantic import BaseModel, Field

from .comparison import Comparison
from .context import Context
from .models import ModelEvaluation
from .rubric import Rubric
from .submission import Submission


class EvaluationDocument(BaseModel):
    """
    Complete evaluation document.

    One document represents one student's answer to one question, graded by 2+ models.
    Includes context, submission, rubric, per-model evaluations, and comparison analysis.
    """

    # Top-level metadata
    evaluation_id: str = Field(
        ..., description="Unique ID for this evaluation record (student + question + run)"
    )
    schema_version: str = Field(
        ..., description="Version of this evaluation schema for evolution over time"
    )
    created_at: datetime = Field(..., description="When this evaluation JSON was generated")
    created_by: str = Field(..., description="Which pipeline/script/tool produced this evaluation")

    # Educational context
    context: Context = Field(..., description="Course, assignment, question, and rubric context")

    # Student submission
    submission: Submission = Field(
        ..., description="Student submission data with files and metadata"
    )

    # Grading rubric
    rubric: Rubric = Field(..., description="Rubric structure with categories and scoring criteria")

    # Per-model evaluations
    models: dict[str, ModelEvaluation] = Field(
        ..., description="Per-model grading results. Key: model name/alias"
    )

    # Comparison and ensemble analysis
    comparison: Comparison = Field(
        ..., description="Computed comparison and analysis across all model outputs"
    )
