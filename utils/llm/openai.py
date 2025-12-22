import os
from typing import TypeVar

from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = os.getenv("OPENAI_DEFAULT_MODEL", "gpt-5.2-2025-12-11")

# Module-level singleton client to reuse connections and avoid cleanup issues
_client_instance: AsyncOpenAI | None = None


def _client() -> AsyncOpenAI:
    global _client_instance
    if _client_instance is None:
        _client_instance = AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))
    return _client_instance


async def cleanup() -> None:
    """Explicitly close the client to avoid 'Event loop is closed' errors."""
    global _client_instance
    if _client_instance is not None:
        await _client_instance.close()
        _client_instance = None


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

    response = await _client().responses.parse(
        model=model,
        input=messages,
        text_format=response_model,
    )
    if response.output_parsed is None:
        raise ValueError("OpenAI response missing parsed output")
    return response.output_parsed


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

    response = await _client().responses.parse(
        model=model,
        input=messages,
        text_format=response_model,
        reasoning={"effort": "medium"},
    )
    if response.output_parsed is None:
        raise ValueError("OpenAI response missing parsed output")
    return response.output_parsed
