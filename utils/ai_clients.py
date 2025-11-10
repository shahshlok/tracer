"""Model client helpers for GPT-5 (OpenAI) and EduAI."""
from __future__ import annotations

import asyncio
import json
import logging
import os
import subprocess
from typing import Any, Dict, Optional

import openai

logger = logging.getLogger(__name__)

# Optional rate limiting via semaphore
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
        response = openai.responses.create(model="gpt-5-nano", input=prompt)
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
        # Some SDKs expose a convenience property aggregating text outputs
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


def get_eduai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Call the EduAI GPT-OSS 120B endpoint via curl (synchronous version)."""
    return _call_eduai_eval(prompt)


async def get_eduai_eval_async(prompt: str) -> Optional[Dict[str, Any]]:
    """Call the EduAI GPT-OSS 120B endpoint via curl (async version)."""
    semaphore = _get_semaphore()
    if semaphore:
        async with semaphore:
            return await asyncio.to_thread(_call_eduai_eval, prompt)
    return await asyncio.to_thread(_call_eduai_eval, prompt)


def _call_eduai_eval(prompt: str) -> Optional[Dict[str, Any]]:
    """Internal helper to call EduAI endpoint via curl."""
    endpoint = os.getenv("EDUAI_ENDPOINT", "https://eduai.ok.ubc.ca/api/chat")
    api_key = os.getenv("EDUAI_API_KEY")
    if not api_key:
        raise RuntimeError("EDUAI_API_KEY is not set")

    payload = json.dumps(
        {
            "messages": [{"role": "user", "content": prompt}],
            "model": os.getenv("EDUAI_MODEL", "ollama:gpt-oss:120b"),
            "apiKeys": {"ollama": {"isEnabled": True}},
            "courseCode": os.getenv("EDUAI_COURSE_CODE", ""),
            "streaming": False,
        }
    )

    cmd = [
        "curl",
        "-s",
        "-X",
        "POST",
        endpoint,
        "-H",
        "Content-Type: application/json",
        "-H",
        f"x-api-key: {api_key}",
        "-d",
        payload,
    ]

    try:
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        parsed = _safe_json_loads(result.stdout)
        if parsed and isinstance(parsed.get("content"), str):
            # EduAI nests the rubric JSON in the 'content' string.
            content_json = _safe_json_loads(parsed["content"])
            if content_json:
                content_json["_raw_response"] = parsed
                return content_json
        return parsed
    except subprocess.CalledProcessError as exc:  # pragma: no cover - network call
        logger.warning("EduAI curl failed (code %s): %s", exc.returncode, exc.stderr)
    except Exception as exc:  # pragma: no cover - network call
        logger.warning("EduAI evaluation failed: %s", exc)
    return None


def _safe_json_loads(raw_text: str) -> Optional[Dict[str, Any]]:
    if raw_text is None:
        return None
    cleaned = raw_text.strip()
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
