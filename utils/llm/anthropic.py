import os
from typing import TypeVar

from anthropic import AsyncAnthropic
from dotenv import load_dotenv
from pydantic import BaseModel

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = os.getenv("ANTHROPIC_DEFAULT_MODEL", "claude-haiku-4-5-20251001")

# Beta feature identifiers
STRUCTURED_OUTPUTS_BETA = "structured-outputs-2025-11-13"

# Module-level singleton client to reuse connections
# Using built-in retry with max_retries=5 for rate limit handling
# The SDK automatically respects retry-after headers and uses exponential backoff
_client_instance: AsyncAnthropic | None = None


def _client() -> AsyncAnthropic:
    global _client_instance
    if _client_instance is None:
        _client_instance = AsyncAnthropic(
            api_key=os.getenv("ANTHROPIC_API_KEY"),
            max_retries=5,  # Built-in retry with exponential backoff
        )
    return _client_instance


async def cleanup() -> None:
    """Explicitly close the client to avoid 'Event loop is closed' errors."""
    global _client_instance
    if _client_instance is not None:
        await _client_instance.close()
        _client_instance = None


def _split_system_and_messages(
    messages: list[dict[str, str]],
) -> tuple[str | None, list[dict[str, str]]]:
    """Extract system message from messages list (Anthropic requires it separately)."""
    system_content = None
    filtered_messages = []

    for msg in messages:
        if msg.get("role") == "system":
            # Combine multiple system messages if present
            if system_content is None:
                system_content = msg["content"]
            else:
                system_content += "\n\n" + msg["content"]
        else:
            filtered_messages.append(msg)

    return system_content, filtered_messages


async def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    if not messages:
        raise ValueError("messages must contain at least one item")

    system_content, filtered_messages = _split_system_and_messages(messages)

    kwargs: dict = {
        "model": model,
        "max_tokens": 8192,
        "messages": filtered_messages,
        "betas": [STRUCTURED_OUTPUTS_BETA],
        "output_format": response_model,
    }
    if system_content:
        kwargs["system"] = system_content

    response = await _client().beta.messages.parse(**kwargs)

    if response.parsed_output is None:
        raise ValueError("Anthropic response missing parsed output")
    return response.parsed_output


async def get_reasoning_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    if not messages:
        raise ValueError("messages must contain at least one item")

    system_content, filtered_messages = _split_system_and_messages(messages)

    kwargs: dict = {
        "model": model,
        "max_tokens": 16000,
        "messages": filtered_messages,
        "betas": [STRUCTURED_OUTPUTS_BETA],
        "output_format": response_model,
        "thinking": {
            "type": "enabled",
            "budget_tokens": 5000,  # Medium effort equivalent
        },
    }
    if system_content:
        kwargs["system"] = system_content

    response = await _client().beta.messages.parse(**kwargs)

    if response.parsed_output is None:
        raise ValueError("Anthropic response missing parsed output")
    return response.parsed_output
