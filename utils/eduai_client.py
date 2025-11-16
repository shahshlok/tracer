"""EduAI GPT-OSS 120B client helpers.

This module now talks to the EduAI LLM server using the **OpenAI SDK**
with an OpenAI-compatible base URL, instead of shelling out to ``curl``.

Configuration (environment variables):

- ``EDUAI_API_KEY``   – API key for the EduAI LLM server (required)
- ``EDUAI_BASE_URL``  – OpenAI-compatible base URL (e.g. http://host:port/v1) (required)
- ``EDUAI_MODEL``     – model name on the EduAI server (default: ``gpt-oss-120b``)
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


def get_eduai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Call the EduAI GPT-OSS 120B endpoint via the OpenAI SDK (sync wrapper)."""
    return _call_eduai_eval(prompt)


async def get_eduai_eval_async(prompt: str) -> Optional[Dict[str, Any]]:
    """Call the EduAI GPT-OSS 120B endpoint via the OpenAI SDK (async wrapper)."""
    semaphore = _get_semaphore()
    if semaphore:
        async with semaphore:
            return await asyncio.to_thread(_call_eduai_eval, prompt)
    return await asyncio.to_thread(_call_eduai_eval, prompt)


def _call_eduai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Internal helper to call the EduAI server via the OpenAI SDK.

    The EduAI server is expected to expose an OpenAI-compatible API
    (chat/completions or similar) at ``EDUAI_BASE_URL``.
    """
    api_key = os.getenv("EDUAI_API_KEY")
    if not api_key:
        raise RuntimeError("EDUAI_API_KEY is not set")

    try:
        base_url = os.getenv("EDUAI_BASE_URL")
        if not base_url:
            raise RuntimeError("EDUAI_BASE_URL is not set")

        model = os.getenv("EDUAI_MODEL", "gpt-oss-120b")

        client = OpenAI(
            base_url=base_url,
            api_key=api_key,
        )

        # Ask EduAI to return strictly structured JSON using the same
        # grading schema as the OpenAI + OpenRouter clients.
        completion = client.chat.completions.create(
            model=model,
            messages=[{"role": "user", "content": prompt}],
            response_format=get_grading_response_format(),
        )

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
        logger.warning("EduAI evaluation failed: %s", exc)
        return None


__all__ = ["get_eduai_eval", "get_eduai_eval_async", "_call_eduai_eval"]
