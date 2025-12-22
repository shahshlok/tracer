import pytest
from pydantic import BaseModel
from utils.llm import gemini as gemini_module


class DemoModel(BaseModel):
    value: str
    explanation: str


@pytest.mark.asyncio
async def test_get_structured_response_real():
    """Real test for standard structured response using Gemini 3 Flash."""
    result = await gemini_module.get_structured_response(
        messages=[
            {
                "role": "user",
                "content": "Say 'hello world' in the value field and explain why in explanation.",
            }
        ],
        response_model=DemoModel,
        model="gemini-3-flash-preview",
    )

    assert isinstance(result, DemoModel)
    assert "hello" in result.value.lower()
    assert len(result.explanation) > 0


@pytest.mark.asyncio
async def test_get_reasoning_response_real():
    """Real test for reasoning response using Gemini 3 Flash with thinking enabled."""
    result = await gemini_module.get_reasoning_response(
        messages=[
            {
                "role": "user",
                "content": "Solve this riddle: I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I? Put the answer in 'value' and your reasoning in 'explanation'.",
            }
        ],
        response_model=DemoModel,
        model="gemini-3-flash-preview",
    )

    assert isinstance(result, DemoModel)
    assert "echo" in result.value.lower()
    assert len(result.explanation) > 0


@pytest.mark.asyncio
async def test_system_message_handling():
    """Test that system messages are properly extracted and passed to Gemini."""
    result = await gemini_module.get_structured_response(
        messages=[
            {
                "role": "system",
                "content": "You are a helpful assistant that always responds with exactly 'SYSTEM_TEST' in the value field.",
            },
            {
                "role": "user",
                "content": "What should you put in the value field? Explain in the explanation field.",
            },
        ],
        response_model=DemoModel,
        model="gemini-3-flash-preview",
    )

    assert isinstance(result, DemoModel)
    assert "system_test" in result.value.lower()
    assert len(result.explanation) > 0
