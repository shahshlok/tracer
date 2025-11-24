"""Submission models for student work and metadata."""

from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class StudentFile(BaseModel):
    """A single file submitted by the student."""

    model_config = ConfigDict(extra="forbid")
    path: str = Field(..., description="Path/name exactly as in the submission")
    language: str = Field(..., description="Programming language of this specific file")


class Submission(BaseModel):
    """
    Student submission data.

    Contains student information, submission metadata, and all submitted files.
    """

    model_config = ConfigDict(extra="forbid")

    student_id: str = Field(..., description="Unique identifier for the student")
    student_name: str = Field(..., description="Student's full name")
    submitted_at: datetime = Field(..., description="Timestamp when this submission was received")
    programming_language: str = Field(
        ..., description="Main language of the submission; used for routing to compilers/LLMs"
    )
    files: list[StudentFile] = Field(..., description="List of files submitted by the student")
