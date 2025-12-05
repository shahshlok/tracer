import os
from typing import TypeVar

import instructor
from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = "google/gemini-2.5-flash-preview-09-2025"

client = instructor.from_openai(
    AsyncOpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key=os.environ.get("OPENROUTER_API_KEY"),
    ),
    mode=instructor.Mode.JSON,
)


# Add automatic retries for rate limits (429) and server errors (5xx)
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
    """
    Get a structured response with automatic retries and reasoning enabled.

    OpenRouter normalizes reasoning parameters across providers:
    - For Gemini 2.5: maps to `thinkingBudget`
    - For Claude Haiku 4.5: maps to extended thinking
    - For GPT-5.1: maps to reasoning effort/tokens

    Args:
        messages: List of message dicts with 'role' and 'content'.
        response_model: Pydantic model for structured output.
        model: Model identifier (default: gemini-2.5-flash-preview).
        reasoning_tokens: Max tokens for the reasoning process (default: 1024).

    Returns:
        Parsed response matching the response_model schema.
    """
    extra_body = {
        "provider": {"allow_fallbacks": False},
        "reasoning": {"max_tokens": reasoning_tokens},
    }

    return await client.chat.completions.create(
        model=model,
        messages=messages,
        response_model=response_model,
        extra_body=extra_body,
    )
