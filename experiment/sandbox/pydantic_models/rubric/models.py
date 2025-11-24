"""Rubric models for grading criteria."""

from pydantic import BaseModel, ConfigDict, Field


class RubricCategory(BaseModel):
    """A single category in the grading rubric."""

    model_config = ConfigDict(extra="forbid")
    category_id: str = Field(
        ..., description="Stable ID used in category_scores and misconceptions"
    )
    name: str = Field(..., description="Display name for this rubric category")
    max_points: int = Field(..., description="Maximum points for this category")
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
    total_points: int = Field(..., description="Maximum score according to rubric")
    categories: list[RubricCategory] = Field(
        ..., description="List of rubric categories with scoring criteria"
    )
