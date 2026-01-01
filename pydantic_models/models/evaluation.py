"""Models for notional machine misconception detection.

Simplified schema for thesis research on LLM-based misconception discovery.
Focuses on blind discovery - LLMs infer category names rather than
matching to a predefined taxonomy.

Structure:

    LLMDetectionResponse
    └─ misconceptions: list[NotionalMisconception]
        └─ NotionalMisconception
            ├─ inferred_category_name: str
            ├─ student_thought_process: str
            ├─ conceptual_gap: str
            ├─ error_manifestation: str
            ├─ confidence: float
            └─ evidence: list[Evidence]
                └─ Evidence
                    ├─ line_number: int
                    └─ code_snippet: str
"""

from __future__ import annotations

from pydantic import BaseModel, ConfigDict, Field


class Evidence(BaseModel):
    """Code snippet demonstrating a misconception."""

    model_config = ConfigDict(extra="forbid")

    line_number: int = Field(..., description="Line number where the evidence appears (1-indexed)")
    code_snippet: str = Field(
        ..., description="The actual code that demonstrates the misconception"
    )


class NotionalMisconception(BaseModel):
    """
    A misconception rooted in a flawed mental model of the computer.

    Designed for blind discovery: the LLM names the category rather than
    choosing from a predefined taxonomy.
    """

    model_config = ConfigDict(extra="forbid")

    # The Label (Open-ended for Blind Discovery)
    inferred_category_name: str = Field(
        ...,
        description=(
            "A short, descriptive name for the type of mental model failure "
            "(e.g., 'Automatic Variable Updates', 'Input Order Confusion')."
        ),
    )

    # Mental Model Analysis
    student_thought_process: str = Field(
        ...,
        description=(
            "Reconstruct the student's flawed belief. Start with 'The student believes...'"
        ),
    )
    conceptual_gap: str = Field(
        ...,
        description=(
            "Explain the gap between the student's mental model "
            "and the actual Java execution model."
        ),
    )

    # Error Manifestation
    error_manifestation: str = Field(
        default="",
        description=(
            "How does this misconception manifest? "
            "(e.g., 'wrong output', 'runtime exception', 'compile error')"
        ),
    )

    # Confidence & Evidence
    confidence: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Model's confidence (0-1) that this misconception is present",
    )
    evidence: list[Evidence] = Field(..., description="Code snippets demonstrating this belief")


class LLMDetectionResponse(BaseModel):
    """Response from LLM containing detected misconceptions."""

    model_config = ConfigDict(extra="forbid")

    misconceptions: list[NotionalMisconception] = Field(
        default_factory=list,
        description="List of detected notional machine misconceptions",
    )
