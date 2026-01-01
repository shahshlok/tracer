"""
Pydantic models for notional machine misconception detection.

Main Components:
- NotionalMisconception: A misconception rooted in a flawed mental model
- Evidence: Code snippet demonstrating a misconception
- LLMDetectionResponse: What the LLM returns
"""

__version__ = "2.0.0"

from .models import (
    Evidence,
    LLMDetectionResponse,
    NotionalMisconception,
)

__all__ = [
    "NotionalMisconception",
    "Evidence",
    "LLMDetectionResponse",
]
