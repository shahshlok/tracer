"""Rubric models for grading criteria."""

from pydantic import BaseModel, ConfigDict, Field


class RubricCategory(BaseModel):
    """A single category in the grading rubric."""

    model_config = ConfigDict(extra="forbid")
    category_id: str = Field(
        ..., description="Stable ID used in category_scores and misconceptions"
    )
    task: str = Field(..., description="The specific task or requirement being graded")
    points: float = Field(..., description="Maximum points for this category")
    bloom_level: str = Field(..., description="Bloom's Taxonomy level for this task")
    description: str = Field(
        ..., description="Detailed explanation of what this category is assessing"
    )


class Rubric(BaseModel):
    """
    Grading rubric structure.

    A copy of the rubric information with IDs for downstream linking.
    """

    model_config = ConfigDict(extra="forbid")

    rubric_id: str = Field(..., description="Unique identifier for this rubric version")
    title: str = Field(..., description="Human-readable rubric name")
    total_points: float = Field(..., description="Maximum score according to rubric")
    categories: list[RubricCategory] = Field(
        ..., description="List of rubric categories with scoring criteria"
    )
