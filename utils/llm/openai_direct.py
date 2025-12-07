import os
from typing import TypeVar

import instructor
from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = "gpt-5.1"
REASONING_MODEL = "gpt-5.1-reasoning"

client = instructor.from_openai(
    AsyncOpenAI(api_key=os.environ.get("OPENAI_API_KEY")),
    mode=instructor.Mode.JSON,
)


@retry(
    stop=stop_after_attempt(3),
    wait=wait_exponential(multiplier=1, min=4, max=10) + wait_random(0, 0.4),
)
async def get_openai_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
    extra_body: dict | None = None,
) -> T:
    """
    Structured JSON output via AsyncOpenAI + instructor for GPT-5.1.
    """
    return await client.chat.completions.create(
        model=model,
        messages=messages,
        response_model=response_model,
        extra_body=extra_body or {},
    )


@retry(
    stop=stop_after_attempt(3),
    wait=wait_exponential(multiplier=1, min=4, max=10) + wait_random(0, 0.4),
)
async def get_openai_reasoning_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = REASONING_MODEL,
    reasoning_tokens: int = 1024,
    extra_body: dict | None = None,
) -> T:
    """
    Structured JSON output for GPT-5.1-Reasoning with enforced medium effort.
    """
    body = extra_body.copy() if extra_body else {}
    # OpenAI reasoning API expects effort; enforce medium per requirement.
    body["reasoning"] = {"effort": "medium"}
    if reasoning_tokens:
        # Limit output tokens for the final answer (not the reasoning trace).
        body["max_output_tokens"] = reasoning_tokens

    return await client.chat.completions.create(
        model=model,
        messages=messages,
        response_model=response_model,
        extra_body=body,
    )
