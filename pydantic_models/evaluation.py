"""Root evaluation document model."""

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field, model_validator

from . import __version__ as MODELS_VERSION
from .comparison.models import Comparison
from .context import Context
from .models import ModelEvaluation
from .rubric import Rubric
from .submission import Submission


class EvaluationDocument(BaseModel):
    """
    Complete evaluation document.

    One document represents one student's answer to one question, graded by 2+ models.
    Includes context, submission, rubric, and per-model evaluations.
    """

    model_config = ConfigDict(extra="forbid")

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

    # Comparison and analysis
    comparison: Comparison | None = Field(
        default=None, description="Cross-model analysis and ensemble decision"
    )

    @model_validator(mode="after")
    def validate_schema_version(self) -> "EvaluationDocument":
        """
        Ensure the document schema_version matches the library version.

        This keeps stored data and code in sync; relax here if you
        intentionally allow multiple schema versions per code version.
        """
        if self.schema_version != MODELS_VERSION:
            raise ValueError(
                f"schema_version {self.schema_version!r} does not match "
                f"pydantic_models version {MODELS_VERSION!r}"
            )
        return self
