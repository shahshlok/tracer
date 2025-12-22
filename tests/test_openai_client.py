import pytest
from pydantic import BaseModel
from utils.llm import openai as openai_module


class DemoModel(BaseModel):
    value: str
    explanation: str


@pytest.mark.asyncio
async def test_get_structured_response_real():
    """Real test for standard structured response using gpt-4o-mini."""
    result = await openai_module.get_structured_response(
        messages=[
            {
                "role": "user",
                "content": "Say 'hello world' in the value field and explain why in explanation.",
            }
        ],
        response_model=DemoModel,
        model="gpt-5.1-2025-11-13",
    )

    assert isinstance(result, DemoModel)
    assert "hello" in result.value.lower()
    assert len(result.explanation) > 0


@pytest.mark.asyncio
async def test_get_reasoning_response_real():
    """Real test for reasoning response using o1-mini."""
    result = await openai_module.get_reasoning_response(
        messages=[
            {
                "role": "user",
                "content": "Solve this riddle: I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I? Put the answer in 'value' and your reasoning in 'explanation'.",
            }
        ],
        response_model=DemoModel,
        model="gpt-5.1-2025-11-13",
    )

    assert isinstance(result, DemoModel)
    assert "echo" in result.value.lower()
    assert len(result.explanation) > 0
