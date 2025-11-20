import json
import os
import uuid
from datetime import datetime, timezone

from dotenv import load_dotenv

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

load_dotenv()


def grade_sergio():
    # 1. Load Data
    print("Loading data...")
    with open("question_cuboid.md") as f:
        question_text = f.read()

    with open("rubric_cuboid.json") as f:
        rubric_data = json.load(f)
        rubric_str = json.dumps(rubric_data)

    student_id = "Diaz_Sergio_100029"
    file_path = f"student_submissions/{student_id}/Cuboid.java"
    with open(file_path) as f:
        student_code = f.read()

    # 2. Construct Prompt
    print("Constructing prompt...")
    prompt = f"""
You are an expert grader for a Computer Science assignment.

**Question:**
{question_text}

**Rubric:**
{rubric_str}

**Student Submission (Cuboid.java):**
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

    messages = [{"role": "user", "content": prompt}]

    # 3. Get LLM Response

    # 4. Construct EvaluationDocument components

    # Context
    context = Context(
        course_id="CS101",
        course_name="Intro to CS",
        assignment_id=1,
        assignment_title="Cuboid",
        question_source_path="question_cuboid.md",
        question_id="q1",
        question_title="Cuboid Class",
        rubric_source_path="rubric_cuboid.json",
    )

    # Submission
    submission = Submission(
        student_id=student_id,
        student_name="Sergio Diaz",
        submitted_at=datetime.now(timezone.utc),
        programming_language="Java",
        files=[StudentFile(path="Cuboid.java", language="Java")],
    )

    # Rubric
    # Transform rubric data to match Pydantic model
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

    # ModelEvaluation
    models_to_test = ["google/gemini-2.5-flash-lite", "moonshotai/kimi-k2-0905"]
    model_evals = {}

    for model_name in models_to_test:
        print(f"Calling OpenRouter ({model_name})...")
        try:
            llm_response = get_structured_response(
                messages, LLMEvaluationResponse, model=model_name
            )
            print(f"Received response from {model_name}.")

            model_eval = ModelEvaluation(
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
            # Use a simplified key for the dictionary (e.g., "gemini-2.5-flash-lite")
            # or just use the full model name if preferred. Let's use the full name for clarity
            # or split by slash to get the model part.
            key_name = model_name.split("/")[-1] if "/" in model_name else model_name
            model_evals[key_name] = model_eval
        except Exception as e:
            print(f"Failed to get response from {model_name}: {e}")

    # 5. Assemble EvaluationDocument
    eval_doc = EvaluationDocument(
        evaluation_id=f"eval_{uuid.uuid4()}",
        schema_version="1.0.0",
        created_at=datetime.now(timezone.utc),
        created_by="grade_sergio.py",
        context=context,
        submission=submission,
        rubric=rubric,
        models=model_evals,
    )

    # 6. Save to file
    output_dir = "student_evals"
    os.makedirs(output_dir, exist_ok=True)
    output_file = f"{output_dir}/sergio_eval.json"

    print(f"Saving evaluation to {output_file}...")
    with open(output_file, "w") as f:
        f.write(eval_doc.model_dump_json(indent=2))

    print("Done!")


if __name__ == "__main__":
    grade_sergio()
