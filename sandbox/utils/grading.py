import re
import json
import os
import uuid
from datetime import datetime, timezone
from typing import Any

from sandbox.pydantic_models import (
    Config,
    Context,
    EvaluationDocument,
    LLMEvaluationResponse,
    ModelEvaluation,
    Rubric,
    StudentFile,
    Submission,
)
from sandbox.utils.openrouter_sdk import get_structured_response


def parse_markdown_rubric(md_content: str) -> dict[str, Any]:
    """
    Parses a markdown table rubric into a dictionary.
    Expected columns: Tasks, Marks Assigned, Bloom's Level, Why?
    """
    lines = md_content.splitlines()
    categories = []
    total_points = 0.0

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
        # parts[3] is Bloom's Level
        # parts[4] is Why?
        
        if len(parts) < 6:
            continue
            
        task = parts[1]
        marks_str = parts[2].replace("+", "").strip()
        try:
            points = float(marks_str)
        except ValueError:
            continue # Skip if marks not parseable
            
        bloom_level_full = parts[3]
        # Extract "Understand" from "Level 2: Understand"
        if ":" in bloom_level_full:
            bloom_level = bloom_level_full.split(":", 1)[1].strip()
        else:
            bloom_level = bloom_level_full
            
        description = parts[4]
        
        categories.append({
            "task": task,
            "points": points,
            "bloom_level": bloom_level,
            "description": description
        })
        total_points += points

    return {
        "totalPoints": total_points,
        "categories": categories
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


def construct_prompt(question_text: str, rubric_data: dict[str, Any], student_code: str) -> str:
    rubric_str = json.dumps(rubric_data)
    prompt = f"""
You are an expert grader for a Computer Science assignment.

**Question:**
{question_text}

**Rubric:**
{rubric_str}

**Student Submission:**
```java
{student_code}
```

Evaluate the student's submission based on the provided rubric.
Provide a structured output containing:
1. Scores for each category in the rubric.
2. Specific feedback for each category.
3. Identification of any misconceptions.
4. Overall feedback.
"""
    return prompt


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
            scores=llm_response.scores,
            category_scores=llm_response.category_scores,
            feedback=llm_response.feedback,
            misconceptions=llm_response.misconceptions,
        )
    except Exception as e:
        raise e


def create_evaluation_document(
    student_id: str,
    student_name: str,
    question_text: str,
    rubric_data: dict[str, Any],
    filename: str,
    model_evals: dict[str, ModelEvaluation],
    question_source_path: str = "data/question_cuboid.md",
    rubric_source_path: str = "data/rubric_cuboid.json",
) -> EvaluationDocument:
    # Context
    context = Context(
        course_id="CS101",
        course_name="Intro to CS",
        assignment_id=1,
        assignment_title="Cuboid",  # Hardcoded as per grade_sergio.py
        question_source_path=question_source_path,
        question_id="q1",
        question_title="Cuboid Class",
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

    # Rubric
    rubric_categories = []
    for cat in rubric_data["categories"]:
        # Handle both old JSON format (name) and new MD format (task)
        task_name = cat.get("task", cat.get("name", "Unknown Task"))
        points = cat.get("points", cat.get("max_points", 0.0))
        
        rubric_categories.append(
            {
                "category_id": task_name.lower().replace(" ", "_").replace("&", "and")[:50], # Truncate id
                "task": task_name,
                "points": float(points),
                "bloom_level": cat.get("bloom_level", "Unspecified"),
                "description": cat["description"],
            }
        )

    rubric = Rubric(
        rubric_id="rubric_cuboid_v1",
        title="Assignment Rubric",
        total_points=float(rubric_data["totalPoints"]),
        categories=rubric_categories,
    )

    return EvaluationDocument(
        evaluation_id=f"eval_{uuid.uuid4()}",
        schema_version="1.0.0",
        created_at=datetime.now(timezone.utc),
        created_by="cli.py",
        context=context,
        submission=submission,
        rubric=rubric,
        models=model_evals,
    )
