import os
from typing import Any, TypeVar

from dotenv import load_dotenv
from google import genai
from google.genai import types
from pydantic import BaseModel
from tenacity import retry, stop_after_attempt, wait_exponential, wait_random

load_dotenv()

T = TypeVar("T", bound=BaseModel)
DEFAULT_MODEL = os.getenv("GEMINI_DEFAULT_MODEL", "gemini-3-flash-preview")


def _strip_additional_properties(schema: dict[str, Any]) -> dict[str, Any]:
    """Recursively remove additionalProperties from schema for Gemini compatibility.

    The Gemini ML Developer API does not support the 'additionalProperties' field
    in JSON schemas. Pydantic models with `extra="forbid"` generate this field,
    causing API errors. This function strips it from the schema.
    """
    if isinstance(schema, dict):
        schema.pop("additionalProperties", None)
        for key, value in schema.items():
            if isinstance(value, dict):
                _strip_additional_properties(value)
            elif isinstance(value, list):
                for item in value:
                    if isinstance(item, dict):
                        _strip_additional_properties(item)
    return schema


# Module-level singleton client to maintain aiohttp session across async calls
_client_instance: genai.Client | None = None


def _client() -> genai.Client:
    global _client_instance
    if _client_instance is None:
        _client_instance = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))
    return _client_instance


async def cleanup() -> None:
    """Reset the client to avoid 'Event loop is closed' errors.

    The google-genai client uses aiohttp internally. By setting the instance
    to None before the event loop closes, we prevent the destructor from
    attempting cleanup on a closed loop.
    """
    global _client_instance
    _client_instance = None


def _build_contents(messages: list[dict[str, str]]) -> list[types.Content]:
    """Convert OpenAI-style messages to Gemini Content format.

    Gemini uses a different format for messages:
    - System messages become the first user message with a system instruction prefix
    - User/assistant messages map to user/model roles
    """
    contents: list[types.Content] = []
    system_instruction = None

    for msg in messages:
        role = msg.get("role", "user")
        content = msg.get("content", "")

        if role == "system":
            # Accumulate system messages
            if system_instruction is None:
                system_instruction = content
            else:
                system_instruction += "\n\n" + content
        elif role == "assistant":
            contents.append(types.Content(role="model", parts=[types.Part.from_text(text=content)]))
        else:
            # user or any other role treated as user
            contents.append(types.Content(role="user", parts=[types.Part.from_text(text=content)]))

    # Prepend system instruction to the first user message if present
    if system_instruction and contents:
        first_user_idx = next((i for i, c in enumerate(contents) if c.role == "user"), None)
        if first_user_idx is not None:
            original_text = (
                contents[first_user_idx].parts[0].text if contents[first_user_idx].parts else ""
            )
            combined_text = (
                f"[System Instructions]\n{system_instruction}\n\n[User Message]\n{original_text}"
            )
            contents[first_user_idx] = types.Content(
                role="user", parts=[types.Part.from_text(text=combined_text)]
            )

    return contents


def _get_system_instruction(messages: list[dict[str, str]]) -> str | None:
    """Extract system instruction from messages."""
    system_parts = []
    for msg in messages:
        if msg.get("role") == "system":
            system_parts.append(msg.get("content", ""))
    return "\n\n".join(system_parts) if system_parts else None


def _build_user_contents(messages: list[dict[str, str]]) -> list[types.Content]:
    """Convert non-system messages to Gemini Content format."""
    contents: list[types.Content] = []

    for msg in messages:
        role = msg.get("role", "user")
        content = msg.get("content", "")

        if role == "system":
            continue  # Skip system messages, handled separately
        elif role == "assistant":
            contents.append(types.Content(role="model", parts=[types.Part.from_text(text=content)]))
        else:
            contents.append(types.Content(role="user", parts=[types.Part.from_text(text=content)]))

    return contents


@retry(
    stop=stop_after_attempt(10),
    wait=wait_exponential(multiplier=1, min=4, max=60) + wait_random(0, 1),
)
async def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    if not messages:
        raise ValueError("messages must contain at least one item")

    system_instruction = _get_system_instruction(messages)
    contents = _build_user_contents(messages)

    # Get JSON schema and strip additionalProperties for Gemini compatibility
    schema = response_model.model_json_schema()
    cleaned_schema = _strip_additional_properties(schema)

    config = types.GenerateContentConfig(
        response_mime_type="application/json",
        response_schema=cleaned_schema,
    )
    if system_instruction:
        config.system_instruction = system_instruction

    try:
        response = await _client().aio.models.generate_content(
            model=model,
            contents=contents,
            config=config,
        )
    except Exception as e:
        # Re-raise to trigger tenacity retry
        raise e

    if response.text is None:
        raise ValueError("Gemini response missing text output")

    return response_model.model_validate_json(response.text)


@retry(
    stop=stop_after_attempt(10),
    wait=wait_exponential(multiplier=1, min=4, max=60) + wait_random(0, 1),
)
async def get_reasoning_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    if not messages:
        raise ValueError("messages must contain at least one item")

    system_instruction = _get_system_instruction(messages)
    contents = _build_user_contents(messages)

    # Get JSON schema and strip additionalProperties for Gemini compatibility
    schema = response_model.model_json_schema()
    cleaned_schema = _strip_additional_properties(schema)

    config = types.GenerateContentConfig(
        response_mime_type="application/json",
        response_schema=cleaned_schema,
        thinking_config=types.ThinkingConfig(thinking_level="medium"),
    )
    if system_instruction:
        config.system_instruction = system_instruction

    response = await _client().aio.models.generate_content(
        model=model,
        contents=contents,
        config=config,
    )

    if response.text is None:
        raise ValueError("Gemini response missing text output")

    return response_model.model_validate_json(response.text)
