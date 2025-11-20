import json
import os
import uuid
from datetime import datetime, timezone
from typing import List, Dict, Any, Optional

from pydantic_models import (
    Config,
    Context,
    EvaluationDocument,
    LLMEvaluationResponse,
    ModelEvaluation,
    Rubric,
    StudentFile,
    Submission,
)
from utils.openrouter_sdk import get_structured_response

def load_question(file_path: str) -> str:
    with open(file_path, "r") as f:
        return f.read()

def load_rubric(file_path: str) -> Dict[str, Any]:
    with open(file_path, "r") as f:
        return json.load(f)

def load_student_submission(student_id: str, submission_dir: str = "student_submissions") -> tuple[str, str]:
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
            with open(os.path.join(student_dir, file), "r") as f:
                return f.read(), file
    
    raise FileNotFoundError(f"No .java file found for student {student_id}")

def construct_prompt(question_text: str, rubric_data: Dict[str, Any], student_code: str) -> str:
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

def grade_with_model(
    model_name: str, 
    messages: List[Dict[str, str]]
) -> ModelEvaluation:
    """
    Calls the LLM to grade the submission.
    """
    try:
        llm_response = get_structured_response(
            messages, LLMEvaluationResponse, model=model_name
        )
        
        return ModelEvaluation(
            model_name=model_name,
            provider="openrouter",
            run_id=f"run_{uuid.uuid4().hex[:8]}",
            config=Config(
                system_prompt_id="simple_direct_prompt", rubric_prompt_id="rubric_v1"
            ),
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
    rubric_data: Dict[str, Any],
    filename: str,
    model_evals: Dict[str, ModelEvaluation]
) -> EvaluationDocument:
    
    # Context
    context = Context(
        course_id="CS101",
        course_name="Intro to CS",
        assignment_id=1,
        assignment_title="Cuboid", # Hardcoded as per grade_sergio.py
        question_source_path="question_cuboid.md",
        question_id="q1",
        question_title="Cuboid Class",
        rubric_source_path="rubric_cuboid.json",
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
        rubric_categories.append(
            {
                "category_id": cat["name"].lower().replace(" ", "_").replace("&", "and"),
                "name": cat["name"],
                "max_points": cat["points"],
                "description": cat["description"],
            }
        )

    rubric = Rubric(
        rubric_id="rubric_cuboid_v1",
        title="Cuboid Assignment Rubric",
        total_points=rubric_data["totalPoints"],
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
