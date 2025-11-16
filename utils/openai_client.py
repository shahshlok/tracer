"""OpenAI GPT-5 client helpers.

This module provides the OpenAI client used for GPT-5 Nano evaluations.
It mirrors the existing behavior in utils.ai_clients so that current
callers can continue to use that facade while newer code can import
directly from this module if desired.
"""
from __future__ import annotations

import asyncio
import json
import logging
import os
from typing import Any, Dict, Optional

import openai

from .structured_outputs import get_grading_response_format

logger = logging.getLogger(__name__)

# Optional rate limiting via semaphore, shared via MAX_CONCURRENT_MODELS
_rate_limit_semaphore: Optional[asyncio.Semaphore] = None


def _get_semaphore() -> Optional[asyncio.Semaphore]:
    """Get or create the rate limit semaphore based on MAX_CONCURRENT_MODELS env variable."""
    global _rate_limit_semaphore
    if _rate_limit_semaphore is None:
        max_concurrent = os.getenv("MAX_CONCURRENT_MODELS")
        if max_concurrent and max_concurrent.isdigit():
            limit = int(max_concurrent)
            logger.info("Rate limiting enabled: max %d concurrent model calls", limit)
            _rate_limit_semaphore = asyncio.Semaphore(limit)
    return _rate_limit_semaphore


async def get_openai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Call OpenAI GPT-5 asynchronously and return parsed JSON."""
    semaphore = _get_semaphore()
    if semaphore:
        async with semaphore:
            return await asyncio.to_thread(_call_openai_eval, prompt)
    return await asyncio.to_thread(_call_openai_eval, prompt)


def _call_openai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        raise RuntimeError("OPENAI_API_KEY is not set")

    openai.api_key = api_key
    try:
        # Ask the model to return strictly structured JSON using the Responses API format.
        # The Responses API uses text.format (not response_format).
        response = openai.responses.create(
            model="gpt-5-nano",
            input=prompt,
            text={
                "format": get_grading_response_format(),
            },
        )
        content = _extract_text_response(response)
        parsed = _safe_json_loads(content)
        serialized = _serialize_openai_response(response)
        if isinstance(parsed, dict):
            parsed["_raw_response"] = serialized
            parsed["_raw_content"] = content
            return parsed
        return {"_raw_content": content, "_raw_response": serialized}
    except Exception as exc:  # pragma: no cover - network call
        logger.warning("OpenAI evaluation failed: %s", exc)
        return None


def _extract_text_response(response: Any) -> str:
    """Extract the assistant's textual JSON from the Responses API result.

    Prefers the assistant message over the reasoning block by iterating the
    `output` list in reverse and selecting the first chunk that exposes a
    `text` field. Falls back to `output_text` or generic string conversion.
    """
    try:
        output_text = getattr(response, "output_text", None)
        if output_text:
            if isinstance(output_text, list):
                return "\n".join(str(x) for x in output_text if x)
            return str(output_text)

        output = getattr(response, "output", None)
        if isinstance(output, list) and output:
            for item in reversed(output):
                content = item.get("content") if isinstance(item, dict) else getattr(item, "content", None)
                # Typical shape: list of chunks with {"type": "output_text", "text": "..."}
                if isinstance(content, list):
                    for chunk in content:
                        if isinstance(chunk, dict) and "text" in chunk:
                            return str(chunk.get("text") or "")
                    # fallback to first element stringification
                    if content:
                        return str(content[0])
                elif isinstance(content, dict) and "text" in content:
                    return str(content.get("text") or "")
                elif isinstance(content, str):
                    return content

        # Fallbacks
        text_field = getattr(response, "text", None)
        if isinstance(text_field, str):
            return text_field
        return str(response)
    except Exception:  # pragma: no cover - defensive fallback
        return str(response)


def _serialize_openai_response(response: Any) -> Any:
    try:
        if hasattr(response, "model_dump"):
            return response.model_dump()
        if hasattr(response, "to_dict"):
            return response.to_dict()  # type: ignore[attr-defined]
        return json.loads(response.json())  # type: ignore[attr-defined]
    except Exception:
        return str(response)


def _safe_json_loads(raw_text: str) -> Optional[Dict[str, Any]]:
    """Robust JSON loader that tolerates markdown fences and extra prose."""
    if raw_text is None:
        return None
    cleaned = raw_text.strip()
    if not cleaned:
        return None
    if cleaned.startswith("```"):
        lines = cleaned.splitlines()
        # drop opening fence
        lines = lines[1:]
        # drop closing fence if present
        if lines and lines[-1].strip().startswith("```"):
            lines = lines[:-1]
        cleaned = "\n".join(lines).strip()
    # Attempt to grab the largest JSON object span if extra prose is present.
    if cleaned and (cleaned[0] != "{" or cleaned[-1] != "}"):
        start = cleaned.find("{")
        end = cleaned.rfind("}")
        if start != -1 and end != -1 and end > start:
            cleaned = cleaned[start : end + 1]
    try:
        return json.loads(cleaned)
    except json.JSONDecodeError:
        logger.warning("Received invalid JSON: %s", cleaned[:200])
        return None


__all__ = [
    "get_openai_eval",
    "_call_openai_eval",
    "_extract_text_response",
    "_serialize_openai_response",
    "_safe_json_loads",
]
