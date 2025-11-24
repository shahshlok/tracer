"""Models for individual model evaluation results.

Top‑down structure of the final model:

    ModelEvaluation
    ├─ model_name: str
    ├─ provider: str
    ├─ run_id: str
    ├─ config: Config
    │   ├─ system_prompt_id: str
    │   └─ rubric_prompt_id: str
--- Everything from here is from the LLMEvaluationResponse model ---
    ├─ scores: Scores
    │   ├─ total_points_awarded: int
    │   ├─ max_points: int
    │   └─ percentage: float
    ├─ category_scores: list[CategoryScore]
    │   └─ CategoryScore
    │       ├─ category_id: str
    │       ├─ category_name: str
    │       ├─ points_awarded: int
    │       ├─ max_points: int
    │       ├─ reasoning: str
    │       ├─ confidence: float
    │       └─ reasoning_tokens: int
    ├─ feedback: Feedback
    │   ├─ overall_comment: str
    │   ├─ strengths: list[str]
    │   └─ areas_for_improvement: list[str]
    └─ misconceptions: list[Misconception]
        └─ Misconception
            ├─ name: str
            ├─ description: str
            ├─ confidence: float
            ├─ evidence: list[Evidence]
            │   └─ Evidence
            │       ├─ source: str
            │       ├─ file_path: str
            │       ├─ language: str
            │       ├─ snippet: str
            │       ├─ line_start: int
            │       ├─ line_end: int
            │       └─ note: str
            ├─ generated_by: str
            └─ validated_by: str | None
"""

from pydantic import BaseModel, ConfigDict, Field, model_validator


class Config(BaseModel):
    """Configuration settings for model evaluation."""

    # extra="forbid" means that bad keys trigger validation errors instead of being dropped silently
    model_config = ConfigDict(extra="forbid")

    system_prompt_id: str = Field(..., description="ID of system prompt template used")
    rubric_prompt_id: str = Field(
        ..., description="ID of grading prompt template specific to this rubric/question"
    )


class Scores(BaseModel):
    """Overall scoring information."""

    model_config = ConfigDict(extra="forbid")

    total_points_awarded: float = Field(
        ..., description="Sum of all category_scores[*].points_awarded"
    )
    max_points: float = Field(
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
    task: str = Field(..., description="The task name from the rubric")
    points_awarded: float = Field(..., description="Points this model gave in this category")
    max_points: float = Field(..., description="Maximum possible points in this category")
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

    # Check the Evidence Pydantic Model
    evidence: list[Evidence] = Field(
        ...,
        description="Evidence showing exactly where this misconception appears in the submission",
    )
    validated_by: str | None = Field(
        None, description="Optional human rater ID when a TA confirms this annotation"
    )


class LLMEvaluationResponse(BaseModel):
    """
    Response from LLM containing only the evaluation content (no metadata).

    This is what the LLM fills out. Metadata (model_name, provider, run_id, config)
    is added by the developer to create the full ModelEvaluation.
    """

    scores: Scores = Field(..., description="Aggregate score for this model")
    category_scores: list[CategoryScore] = Field(
        ..., description="Per-category rubric scores with justification"
    )
    feedback: Feedback = Field(..., description="Human-readable feedback bundle for this model")

    # Misconceptions has the Evidence model nested in it
    misconceptions: list[Misconception] = Field(
        ..., description="Misconceptions for this submission according to this model"
    )


class ModelEvaluation(LLMEvaluationResponse):
    """
    Complete evaluation result from a single grading model.

    See the module docstring at the top of this file for
    a full ASCII diagram of the JSON structure.
    """

    model_config = ConfigDict(extra="forbid")

    model_name: str = Field(
        ..., description="Human-readable model name with version (e.g., gpt-5-nano-2025-08-07)"
    )
    provider: str = Field(..., description="Who provides this model (for analysis across vendors)")
    run_id: str = Field(..., description="ID of this model invocation for traceability in logs")
    config: Config = Field(..., description="Configuration settings for this model run")
