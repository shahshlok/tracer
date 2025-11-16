"""Shared structured output schemas for model clients.

This module defines the JSON schema used with the OpenAI SDK
`response_format={"type": "json_schema", "json_schema": ...}` so
that both the OpenAI GPT-5 client and the OpenRouter (Gemini) client
produce consistently structured grading outputs.
"""
from __future__ import annotations

from typing import Any, Dict


def get_grading_response_format() -> Dict[str, Any]:
    """Return the text.format dict for structured grading outputs (Responses API).

    The schema is intentionally minimal and focused on the fields that the
    rest of the system expects:
      - total_score: numeric final score
      - max_possible_score: numeric maximum score
      - overall_feedback: string feedback for the student

    Additional fields are allowed so prompts can include richer structure
    without breaking the parser.

    Note: This returns the format for the Responses API (GPT-5), which uses
    text.format instead of response_format.
    """

    return {
        "type": "json_schema",
        "name": "grading_response",
        "schema": {
            "type": "object",
            "properties": {
                "total_score": {"type": "number"},
                "max_possible_score": {"type": "number"},
                "overall_feedback": {"type": "string"},
            },
            "required": ["total_score", "max_possible_score", "overall_feedback"],
            "additionalProperties": False,
        },
        "strict": True,
    }


__all__ = ["get_grading_response_format"]

