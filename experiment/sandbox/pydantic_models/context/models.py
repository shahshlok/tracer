"""Context models for course, assignment, and question metadata."""

from pydantic import BaseModel, ConfigDict, Field


class Context(BaseModel):
    """
    Educational context for the evaluation.

    Ties the evaluation record to the course, assignment, question, and rubric used.
    """

    model_config = ConfigDict(extra="forbid")

    course_id: str = Field(..., description="Machine-friendly ID to group evaluations by course")
    course_name: str = Field(..., description="Human-readable course title")
    assignment_id: int = Field(..., description="Assignment ID (integer)")
    assignment_title: str = Field(..., description="Human-readable assignment name")
    question_source_path: str = Field(..., description="Path to the question file")
    question_id: str = Field(..., description="ID for this specific question within the assignment")
    question_title: str = Field(..., description="Human-readable question title")
    rubric_source_path: str = Field(..., description="Path to the rubric file for traceability")
