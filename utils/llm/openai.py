import os
from typing import TypeVar

import instructor
from dotenv import load_dotenv
from openai import AsyncOpenAI
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)

DEFAULT_MODEL = os.getenv("OPENAI_DEFAULT_MODEL", "gpt-5.1")

client = instructor.from_openai(
    AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY")),
    mode=instructor.Mode.JSON_SCHEMA,
)


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

    input_text = messages[-1].get("content", "")

    parsed = await client.responses.parse(
        model=model,
        input=input_text,
        text_format=response_model,
    )

    for output in parsed.output:
        for item in output.content:
            parsed_value = getattr(item, "parsed", None)
            if parsed_value is not None:
                return parsed_value

    raise ValueError("No parsed content found in OpenAI response")


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

    input_text = messages[-1].get("content", "")

    # Prefer explicit reasoning configuration when supported by the installed SDK.
    try:
        parsed = await client.responses.parse(
            model=model,
            input=input_text,
            text_format=response_model,
            reasoning={"effort": "medium"},
        )
    except TypeError:
        # Older SDKs may not support the `reasoning` parameter; fall back gracefully.
        parsed = await client.responses.parse(
            model=model,
            input=input_text,
            text_format=response_model,
        )

    for output in parsed.output:
        for item in output.content:
            parsed_value = getattr(item, "parsed", None)
            if parsed_value is not None:
                return parsed_value

    raise ValueError("No parsed content found in OpenAI reasoning response")
