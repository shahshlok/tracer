"""OpenRouter client for structured evaluation using Gemini models."""

import json
import os
from typing import Any, Dict, List, Type, TypeVar

import requests
from dotenv import load_dotenv
from pydantic import BaseModel

load_dotenv()

T = TypeVar("T", bound=BaseModel)

# Hardcoded metadata (user-defined)
MODEL_NAME = "google/gemini-2.5-flash-lite"


def resolve_refs(schema, defs=None):
    """
    Recursively resolve $ref in JSON schema to make it self-contained.
    Many models struggle with $defs/definitions in the schema.
    """
    if defs is None:
        defs = schema.get('$defs', {})
    
    if isinstance(schema, dict):
        if '$ref' in schema:
            ref = schema['$ref']
            # ref is usually like '#/$defs/Scores'
            name = ref.split('/')[-1]
            if name in defs:
                return resolve_refs(defs[name], defs)
        
        return {k: resolve_refs(v, defs) for k, v in schema.items() if k != '$defs'}
    
    if isinstance(schema, list):
        return [resolve_refs(x, defs) for x in schema]
    
    return schema


def get_gemini_structured_response(
    messages: List[Dict[str, str]],
    response_model: Type[T],
) -> T:
    """
    Get a structured response from OpenRouter (Gemini) matching the Pydantic model.

    Args:
        messages: List of chat messages (role, content)
        response_model: The Pydantic model class to parse the response into

    Returns:
        Instance of response_model populated with the LLM's response
    """
    # Get the JSON schema from the Pydantic model and resolve refs
    raw_schema = response_model.model_json_schema()
    schema = resolve_refs(raw_schema)

    # Define the tool structure for OpenRouter/Gemini
    tool_name = "submit_structured_output"
    tool_schema = {
        "type": "function",
        "function": {
            "name": tool_name,
            "description": f"Submit the structured output matching {response_model.__name__}",
            "parameters": schema,
        },
    }

    response = requests.post(
        "https://openrouter.ai/api/v1/chat/completions",
        headers={
            "Authorization": f"Bearer {os.environ.get('OPENROUTER_API_KEY')}",
            "Content-Type": "application/json",
            "HTTP-Referer": "https://github.com/shahshlok/ensemble-eval-cli", 
            "X-Title": "Ensemble Eval CLI", 
        },
        json={
            "model": MODEL_NAME,
            "messages": messages,
            "tools": [tool_schema],
            "tool_choice": {
                "type": "function",
                "function": {"name": tool_name},
            },
        },
        timeout=120,
    )

    if response.status_code != 200:
        raise Exception(f"OpenRouter API Error ({response.status_code}): {response.text}")

    data = response.json()
    
    try:
        tool_calls = data['choices'][0]['message']['tool_calls']
        if not tool_calls:
             raise Exception("No tool calls found in response")
        
        arguments_str = tool_calls[0]['function']['arguments']
        llm_response_data = json.loads(arguments_str)
        
        # Validate with Pydantic
        return response_model(**llm_response_data)

    except (KeyError, IndexError, json.JSONDecodeError) as e:
        raise Exception(f"Failed to parse OpenRouter response: {e}. Response data: {data}")
