"""OpenAI client for structured evaluation using responses.parse API."""

import json
import os
from pathlib import Path

from dotenv import load_dotenv
from openai import OpenAI

from pydantic_models import Config, LLMEvaluationResponse, ModelEvaluation
from prompts import direct_prompt

load_dotenv()

# Initialize OpenAI client
client = OpenAI(
    api_key=os.environ.get("OPENAI_API_KEY"),
)

# Hardcoded metadata (user-defined)
MODEL_NAME = "gpt-5-nano-2025-08-07"
PROVIDER = "openai"
RUN_ID = "run_2025_11_16_01"
CONFIG = Config(
    system_prompt_id="grading_system_v3",
    rubric_prompt_id="rubric_cuboid_prompt_v2",
)


def evaluate_with_openai(
    question: str,
    rubric: str,
    student_code: str,
    file_name: str = "Cuboid.java",
) -> ModelEvaluation:
    """
    Evaluate student submission using OpenAI structured outputs.

    Args:
        question: The assignment question/requirements
        rubric: The grading rubric (JSON string or dict)
        student_code: The student's code submission
        file_name: Name of the student's code file

    Returns:
        Complete ModelEvaluation with LLM-generated content and metadata
    """
    # Build the evaluation prompt
    evaluation_prompt = direct_prompt.build_prompt(
        question=question,
        rubric_json=json.loads(rubric) if isinstance(rubric, str) else rubric,
        student_code=student_code,
    )

    # Request structured evaluation from OpenAI
    rsp = client.responses.parse(
        input=evaluation_prompt,
        model=MODEL_NAME,
        text_format=LLMEvaluationResponse,
    )

    # Extract the parsed response, skipping reasoning items (from extended thinking)
    message = None
    for item in rsp.output:
        if item.type == "message":
            message = item
            break

    if message is None:
        raise Exception("No message found in response output")

    text = message.content[0]
    if text.type != "output_text":
        raise Exception(f"Unexpected content type: {text.type}")

    if not text.parsed:
        raise Exception("Could not parse response from OpenAI")

    llm_response: LLMEvaluationResponse = text.parsed

    # Combine LLM response with user-defined metadata to create full ModelEvaluation
    evaluation = ModelEvaluation(
        model_name=MODEL_NAME,
        provider=PROVIDER,
        run_id=RUN_ID,
        config=CONFIG,
        scores=llm_response.scores,
        category_scores=llm_response.category_scores,
        feedback=llm_response.feedback,
        misconceptions=llm_response.misconceptions,
    )

    return evaluation


if __name__ == "__main__":
    # Test the evaluation function
    with open("question_cuboid.md") as f:
        question = f.read()

    with open("rubric_cuboid.json") as f:
        rubric = f.read()

    with open("student_submissions/Johnson_Natalie_100010/Cuboid.java") as f:
        student_code = f.read()

    evaluation = evaluate_with_openai(question, rubric, student_code)

    print("Parsed Evaluation Response (JSON):")
    print(json.dumps(evaluation.model_dump(), indent=2))
