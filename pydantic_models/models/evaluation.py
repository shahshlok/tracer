"""Models for individual model evaluation results."""

from pydantic import BaseModel, ConfigDict, Field, model_validator


class Config(BaseModel):
    model_config = ConfigDict(extra="forbid")

    """Configuration settings for model evaluation."""

    system_prompt_id: str = Field(..., description="ID of system prompt template used")
    rubric_prompt_id: str = Field(
        ..., description="ID of grading prompt template specific to this rubric/question"
    )


class Scores(BaseModel):
    """Overall scoring information."""

    model_config = ConfigDict(extra="forbid")

    total_points_awarded: int = Field(
        ..., description="Sum of all category_scores[*].points_awarded"
    )
    max_points: int = Field(
        ..., description="Maximum possible points (should match rubric.total_points)"
    )
    percentage: float = Field(
        ..., description="Percentage score (total_points_awarded / max_points * 100)"
    )

    @model_validator(mode="after")
    def validate_percentage(self) -> "Scores":
        if self.max_points <= 0:
            return self
        expected = (self.total_points_awarded / self.max_points) * 100
        # Allow small floating point slack
        if abs(self.percentage - expected) > 1e-6:
            raise ValueError(
                f"percentage {self.percentage} does not match "
                f"total_points_awarded / max_points * 100 ({expected})"
            )
        return self


class CategoryScore(BaseModel):
    """Scoring information for a specific rubric category."""

    model_config = ConfigDict(extra="forbid")

    category_id: str = Field(..., description="Links to rubric.categories[*].category_id")
    category_name: str = Field(..., description="Human-readable name of the category")
    points_awarded: int = Field(..., description="Points this model gave in this category")
    max_points: int = Field(..., description="Maximum possible points in this category")
    reasoning: str = Field(..., description="Category-specific reasoning for the score")
    confidence: float = Field(
        ..., ge=0.0, le=1.0, description="Model's confidence in this category score (0-1)"
    )
    reasoning_tokens: int = Field(
        ..., description="Number of tokens in reasoning (proxy for explanation depth)"
    )


class Feedback(BaseModel):
    """Human-readable feedback bundle for a submission."""

    model_config = ConfigDict(extra="forbid")

    overall_comment: str = Field(
        ...,
        description="Holistic comment on the submission (descriptive, what you'd show a student)",
    )
    strengths: list[str] = Field(
        ..., description="Bullet-style list of strengths in the submission"
    )
    areas_for_improvement: list[str] = Field(
        ..., description="Bullet-style list of weaknesses/next steps for the student"
    )


class Evidence(BaseModel):
    """Evidence supporting a misconception finding."""

    model_config = ConfigDict(extra="forbid")

    source: str = Field(
        ..., description="Where the snippet comes from (e.g., student_code, tests, text_answer)"
    )
    file_path: str = Field(..., description="Which file the snippet lives in")
    language: str = Field(
        ...,
        description="Language of the snippet (could also be 'text' if it's a written explanation)",
    )
    snippet: str = Field(..., description="Concrete code/text that demonstrates the issue")
    line_start: int = Field(
        ..., description="Approximate starting line number in that file (for UI highlighting)"
    )
    line_end: int = Field(
        ..., description="Approximate ending line number in that file (for UI highlighting)"
    )
    note: str = Field(
        ..., description="Concise explanation of why this snippet is considered evidence"
    )


class Misconception(BaseModel):
    """Identified misconception in student's work."""

    model_config = ConfigDict(extra="forbid")

    name: str = Field(..., description="Human-readable label for this misconception")
    description: str = Field(
        ..., description="Description of what behavior/understanding this misconception reflects"
    )
    confidence: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Model's confidence (0-1) that this misconception truly applies",
    )
    evidence: list[Evidence] = Field(
        ...,
        description="Evidence showing exactly where this misconception appears in the submission",
    )
    generated_by: str = Field(..., description="Which model produced this misconception annotation")
    validated_by: str | None = Field(
        None, description="Optional human rater ID when a TA confirms this annotation"
    )


class ModelEvaluation(BaseModel):
    """
    Complete evaluation result from a single grading model.

    Contains model identity, configuration, scores, feedback, and misconceptions.
    """

    model_config = ConfigDict(extra="forbid")

    model_name: str = Field(..., description="Human-readable model name/alias")
    provider: str = Field(..., description="Who provides this model (for analysis across vendors)")
    model_version: str = Field(..., description="Specific version/snapshot used")
    run_id: str = Field(..., description="ID of this model invocation for traceability in logs")
    config: Config = Field(..., description="Configuration settings for this model run")
    scores: Scores = Field(..., description="Aggregate score for this model")
    category_scores: list[CategoryScore] = Field(
        ..., description="Per-category rubric scores with justification"
    )
    feedback: Feedback = Field(..., description="Human-readable feedback bundle for this model")
    misconceptions: list[Misconception] = Field(
        ..., description="Misconceptions for this submission according to this model"
    )
