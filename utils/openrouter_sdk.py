import os
from typing import TypeVar

import instructor
from dotenv import load_dotenv
from openai import OpenAI
from pydantic import BaseModel

# Load environment variables from .env file
load_dotenv()

T = TypeVar("T", bound=BaseModel)

# Default model
DEFAULT_MODEL = "google/gemini-2.5-flash-lite"

# Setup Client
# We use the standard OpenAI client but point it to OpenRouter
client = instructor.from_openai(
    OpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key=os.environ.get("OPENROUTER_API_KEY"),
    ),
    # Mode.JSON is safest for OpenRouter as it works across
    # providers that might not support strict "Tools" mode.
    mode=instructor.Mode.JSON,
)


def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = DEFAULT_MODEL,
) -> T:
    """
    Get a structured response from OpenRouter matching the Pydantic model.

    Args:
        messages: List of chat messages (role, content)
        response_model: The Pydantic model class to parse the response into
        model: The model to use (default: google/gemini-2.5-flash-lite)

    Returns:
        Instance of response_model populated with the LLM's response
    """
    print(f"🤖 Querying {model} via OpenRouter...")

    try:
        response = client.chat.completions.create(
            model=model,
            messages=messages,
            response_model=response_model,
            # Optional: OpenRouter specific routing
            extra_body={"provider": {"allow_fallbacks": False}},
        )
        return response
    except Exception as e:
        print(f"❌ Failed with {model}: {e}")
        raise e
