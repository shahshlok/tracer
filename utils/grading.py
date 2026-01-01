import json
import os
import uuid
from datetime import datetime, timezone
from typing import Any

from pydantic_models import (
    Config,
    Context,
    EvaluationDocument,
    LLMEvaluationResponse,
    ModelEvaluation,
    StudentFile,
    Submission,
)
from utils.llm.openrouter import get_structured_response


def load_student_submission(
    student_id: str, submission_dir: str = "student_submissions"
) -> tuple[str, str]:
    """
    Loads the student submission.
    Returns: (code_content, filename)
    """
    student_dir = os.path.join(submission_dir, student_id)
    if not os.path.exists(student_dir):
        raise FileNotFoundError(f"Student directory not found: {student_dir}")

    # Find the first .java file (assuming Java for now as per grade_sergio.py)
    for file in os.listdir(student_dir):
        if file.endswith(".java"):
            with open(os.path.join(student_dir, file)) as f:
                return f.read(), file

    raise FileNotFoundError(f"No .java file found for student {student_id}")


def parse_markdown_rubric(md_content: str) -> dict[str, Any]:
    """
    Parses a markdown table rubric into a dictionary.
    Expected columns: Tasks, Marks, Topic, Why?
    """
    lines = md_content.splitlines()
    categories = []
    total_points = 0.0
    title = "Rubric"
    rubric_id = "rubric_unknown"

    # Extract title from first heading line
    for line in lines:
        stripped = line.strip()
        if stripped.startswith("#"):
            title = stripped.lstrip("#").strip()
            # Generate rubric_id from title
            rubric_id = "rubric_" + title.lower().replace(" ", "_").replace("(", "").replace(
                ")", ""
            ).replace("-", "_")
            break

    # Skip header and separator lines
    # Find the start of the table
    start_index = 0
    for i, line in enumerate(lines):
        if line.strip().startswith("|") and "Tasks" in line:
            start_index = i + 2  # Skip header and separator
            break

    for line in lines[start_index:]:
        if not line.strip().startswith("|"):
            continue

        parts = [p.strip() for p in line.split("|")]
        # parts[0] is empty string before first |
        # parts[1] is Task
        # parts[2] is Marks
        # parts[3] is Topic
        # parts[4] is Why?

        if len(parts) < 5:
            continue

        task = parts[1]
        marks_str = parts[2].replace("+", "").strip()
        try:
            points = float(marks_str)
        except ValueError:
            continue  # Skip if marks not parseable

        topic = parts[3]
        description = parts[4] if len(parts) > 4 else ""

        categories.append(
            {"task": task, "points": points, "topic": topic, "description": description}
        )
        total_points += points

    return {
        "totalPoints": total_points,
        "categories": categories,
        "title": title,
        "rubric_id": rubric_id,
    }


def load_question(file_path: str) -> str:
    with open(file_path) as f:
        return f.read()


def load_rubric(file_path: str) -> dict[str, Any]:
    if file_path.endswith(".md"):
        with open(file_path) as f:
            return parse_markdown_rubric(f.read())
    with open(file_path) as f:
        return json.load(f)


def construct_prompt(
    question_text: str,
    rubric_data: dict[str, Any],
    student_code: str,
    strategy: str = "minimal",
) -> str:
    """
    Construct a grading prompt using the specified strategy.

    Args:
        question_text: The assignment question
        rubric_data: The rubric dict
        student_code: Student's code submission
        strategy: One of "baseline", "minimal", "socratic", "rubric_only"
                  Default is "minimal" for unbiased misconception detection research

    Returns:
        The constructed prompt string
    """
    from prompts.strategies import PromptStrategy, build_prompt

    strategy_enum = PromptStrategy(strategy)
    return build_prompt(strategy_enum, question_text, rubric_data, student_code)


async def grade_with_model(model_name: str, messages: list[dict[str, str]]) -> ModelEvaluation:
    """
    Calls the LLM to grade the submission.
    """
    try:
        llm_response = await get_structured_response(
            messages, LLMEvaluationResponse, model=model_name
        )

        return ModelEvaluation(
            model_name=model_name,
            provider="openrouter",
            run_id=f"run_{uuid.uuid4().hex[:8]}",
            config=Config(system_prompt_id="simple_direct_prompt", rubric_prompt_id="rubric_v1"),
            misconceptions=llm_response.misconceptions,
        )
    except Exception as e:
        raise e


def create_evaluation_document(
    student_id: str,
    student_name: str,
    question_text: str,
    filename: str,
    model_evals: dict[str, ModelEvaluation],
    question_source_path: str = "data/question_insurance.md",
    rubric_source_path: str = "data/rubric_insurance2.md",
) -> EvaluationDocument:
    # Context
    context = Context(
        course_id="COSC 111",
        course_name="Intro to Programming",
        assignment_id=1,
        assignment_title="Insurance Compute",
        question_source_path=question_source_path,
        question_id="q1",
        question_title="Insurance Compute",
        rubric_source_path=rubric_source_path,
    )

    # Submission
    submission = Submission(
        student_id=student_id,
        student_name=student_name,
        submitted_at=datetime.now(timezone.utc),
        programming_language="Java",
        files=[StudentFile(path=filename, language="Java")],
    )

    return EvaluationDocument(
        evaluation_id=f"eval_{uuid.uuid4()}",
        schema_version="1.0.0",
        created_at=datetime.now(timezone.utc),
        created_by="cli.py",
        context=context,
        submission=submission,
        models=model_evals,
    )
