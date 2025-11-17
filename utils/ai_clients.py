"""Model client facade for GPT-5 (OpenAI), EduAI, and OpenRouter.

This module preserves the original public interface used throughout the
codebase while delegating the actual implementations to dedicated
client modules:

- utils.openai_client     → GPT-5 Nano via OpenAI
- utils.eduai_client      → GPT-OSS 120B via EduAI
- utils.openrouter_client → OpenRouter (e.g., Gemini 2.5 Flash)
"""
from __future__ import annotations

from typing import Any, Dict, Optional

from .openai_client import (
    get_openai_eval,
    _call_openai_eval,
    _extract_text_response,
    _serialize_openai_response,
    _safe_json_loads,
)
from .eduai_client import get_eduai_eval, get_eduai_eval_async, _call_eduai_eval
from .openrouter_client import get_openrouter_eval, _call_openrouter_eval

__all__ = [
    # OpenAI GPT-5
    "get_openai_eval",
    "_call_openai_eval",
    "_extract_text_response",
    "_serialize_openai_response",
    "_safe_json_loads",
    # EduAI GPT-OSS 120B
    "get_eduai_eval",
    "get_eduai_eval_async",
    "_call_eduai_eval",
    # OpenRouter client
    "get_openrouter_eval",
    "_call_openrouter_eval",
]
