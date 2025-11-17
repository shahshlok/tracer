from pydantic import BaseModel, Field


class ConfigModel(BaseModel):
    """Configuration settings for model evaluation."""
    system_prompt_id: str = Field(..., description="ID of the system prompt used")
    rubric_prompt_id: str = Field(..., description="ID of the rubric prompt used")


class ScoresModel(BaseModel):
    """Overall scoring information."""
    total_points_awarded: int = Field(..., description="Total points awarded to the submission")
    max_points: int = Field(..., description="Maximum possible points")
    percentage: float = Field(..., description="Percentage score (0-100)")


class CategoryScoreModel(BaseModel):
    """Scoring information for a specific category."""
    category_id: str = Field(..., description="Unique identifier for the category")
    category_name: str = Field(..., description="Human-readable name of the category")
    points_awarded: int = Field(..., description="Points awarded in this category")
    max_points: int = Field(..., description="Maximum possible points in this category")
    reasoning: str = Field(..., description="Explanation for the score in this category")
    confidence: float = Field(..., description="Model's confidence in this score (0-1)")
    reasoning_tokens: int = Field(..., description="Number of tokens used for reasoning")


class EvidenceModel(BaseModel):
    """Evidence supporting a misconception finding."""
    source: str = Field(..., description="Where the snippet comes from (e.g., student_code, tests)")
    file_path: str = Field(..., description="Path to the file containing the evidence")
    language: str = Field(..., description="Programming language of the snippet")
    snippet: str = Field(..., description="Concrete code/text demonstrating the issue")
    line_start: int = Field(..., description="Starting line number")
    line_end: int = Field(..., description="Ending line number")
    note: str = Field(..., description="Explanation of why this is evidence")


class MisconceptionModel(BaseModel):
    """Identified misconception in student's work."""
    name: str = Field(..., description="Human-readable label for this misconception")
    description: str = Field(..., description="Description of what behavior/understanding this misconception reflects")
    confidence: float = Field(..., description="Model's confidence that this misconception applies (0-1)")
    evidence: list[EvidenceModel] = Field(..., description="Evidence supporting this misconception")
    generated_by: str = Field(..., description="Which model produced this misconception annotation")
    validated_by: str | None = Field(None, description="Optional human rater ID when a TA confirms this")


class FeedbackModel(BaseModel):
    """Feedback on the submission."""
    overall_comment: str = Field(..., description="Overall assessment of the submission")
    strengths: list[str] = Field(..., description="List of strengths in the submission")
    areas_for_improvement: list[str] = Field(..., description="List of areas that need improvement")


class ModelEvaluationResponse(BaseModel):
    """Complete evaluation response from a model."""
    scores: ScoresModel = Field(..., description="Overall scoring information")
    category_scores: list[CategoryScoreModel] = Field(..., description="Scores for each category")
    feedback: FeedbackModel = Field(..., description="Feedback on the submission")
    misconceptions: list[MisconceptionModel] = Field(..., description="Identified misconceptions in the work")
