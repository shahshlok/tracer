import os
from typing import TypeVar

import instructor
from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = "google/gemini-2.5-flash-lite"

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
async def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    """
    Get a structured response with automatic retries.
    """
    # No extra try/catch here, let Tenacity handle the exceptions
    return await client.chat.completions.create(
        model=model,
        messages=messages,
        response_model=response_model,
        extra_body={"provider": {"allow_fallbacks": False}},
    )
