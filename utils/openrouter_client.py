"""OpenRouter client using the OpenAI SDK.

This module provides a dedicated client for calling models hosted on
OpenRouter, while keeping the existing OpenAI and EduAI clients
unchanged. It is currently configured for a single model, controlled
via the OPENROUTER_MODEL environment variable.
"""
from __future__ import annotations

import asyncio
import logging
import os
from typing import Any, Dict, Optional

from openai import OpenAI

from .openai_client import _safe_json_loads, _serialize_openai_response, _get_semaphore
from .structured_outputs import get_grading_response_format

logger = logging.getLogger(__name__)


async def get_openrouter_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Call the configured OpenRouter model asynchronously and return parsed JSON."""
    semaphore = _get_semaphore()
    if semaphore:
        async with semaphore:
            return await asyncio.to_thread(_call_openrouter_eval, prompt)
    return await asyncio.to_thread(_call_openrouter_eval, prompt)


def _build_extra_headers() -> Dict[str, str]:
    """Build optional ranking/analytics headers for OpenRouter."""
    headers: Dict[str, str] = {}
    site_url = os.getenv("OPENROUTER_SITE_URL")
    site_name = os.getenv("OPENROUTER_SITE_NAME")
    if site_url:
        headers["HTTP-Referer"] = site_url
    if site_name:
        headers["X-Title"] = site_name
    return headers


def _call_openrouter_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Internal helper to call OpenRouter via the OpenAI SDK."""
    api_key = os.getenv("OPENROUTER_API_KEY")
    if not api_key:
        raise RuntimeError("OPENROUTER_API_KEY is not set")

    model = os.getenv("OPENROUTER_MODEL", "google/gemini-2.5-flash-preview-09-2025")

    client = OpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key=api_key,
    )

    extra_headers = _build_extra_headers()

    try:
        completion = client.chat.completions.create(
            model=model,
            messages=[{"role": "user", "content": prompt}],
            # Ask the model (via OpenRouter) for strictly structured JSON.
            response_format=get_grading_response_format(),
            extra_headers=extra_headers or None,
        )
        # Extract the first message content from the completion
        content = ""
        try:
            choice = completion.choices[0]
            message = getattr(choice, "message", None)
            if message is not None:
                content = getattr(message, "content", "") or ""
        except Exception:
            content = str(completion)

        parsed = _safe_json_loads(content)
        serialized = _serialize_openai_response(completion)

        if isinstance(parsed, dict):
            parsed["_raw_response"] = serialized
            parsed["_raw_content"] = content
            return parsed
        return {"_raw_content": content, "_raw_response": serialized}
    except Exception as exc:  # pragma: no cover - network call
        logger.warning("OpenRouter evaluation failed: %s", exc)
        return None


__all__ = ["get_openrouter_eval", "_call_openrouter_eval"]
