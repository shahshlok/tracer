import os
from typing import TypeVar

from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = os.getenv("OPENAI_DEFAULT_MODEL", "gpt-5.2-2025-12-11")


def _client() -> AsyncOpenAI:
    return AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))


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
    reasoning_tokens: int = 1024,
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
