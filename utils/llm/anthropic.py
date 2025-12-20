import os
from typing import TypeVar

from anthropic import AsyncAnthropic
from dotenv import load_dotenv
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = os.getenv("ANTHROPIC_DEFAULT_MODEL", "claude-haiku-4-5-20251001")

# Beta feature identifiers
STRUCTURED_OUTPUTS_BETA = "structured-outputs-2025-11-13"


def _client() -> AsyncAnthropic:
    return AsyncAnthropic(api_key=os.getenv("ANTHROPIC_API_KEY"))


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


@retry(
    stop=stop_after_attempt(3),
    wait=wait_exponential(multiplier=1, min=4, max=10) + wait_random(0, 0.4),
)
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


@retry(
    stop=stop_after_attempt(3),
    wait=wait_exponential(multiplier=1, min=4, max=10) + wait_random(0, 0.4),
)
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
        "messages": filtered_messages,
        "betas": [STRUCTURED_OUTPUTS_BETA],
        "output_format": response_model,
        "thinking": {
            "type": "enabled",
            "budget_tokens": 10000,  # Medium effort equivalent
        },
    }
    if system_content:
        kwargs["system"] = system_content

    response = await _client().beta.messages.parse(**kwargs)
    
    if response.parsed_output is None:
        raise ValueError("Anthropic response missing parsed output")
    return response.parsed_output
