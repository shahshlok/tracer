"""Editable Ensembling Method Evaluation (EME) prompt templates.

This prompt is the single place to shape how both OpenAI and EduAI grade.
By default, it enforces a 100-point rubric and a strict JSON-only reply.
"""
from __future__ import annotations

import json
from typing import Any, Dict, List


def build_eme_prompt(question: str, rubric_json: Dict[str, Any], student_code: str) -> str:
    """Build the grading prompt enforcing a 100-point rubric and JSON-only output.

    Assumptions:
    - `rubric_json` can include a 100-point structure like:
        {"totalPoints": 100, "categories": [{"name": str, "points": int, "description": str}, ...]}
    - Models MUST return max_possible_score = 100 and total_score in [0, 100].
    - Each category becomes a criterion with `max_score = points` and an integer `score`.
    """

    total_points = rubric_json.get("totalPoints")
    categories: List[Dict[str, Any]] = rubric_json.get("categories", [])

    # Fall back to printing whatever rubric was provided, but emphasize 100-point requirement
    rubric_block = json.dumps(rubric_json, indent=2)

    prompt = f"""
    You are an automated programming assignment grader using a standardized rubric.
    Be impartial, consistent, and evaluate purely by rubric adherence.
    Do not infer or assume intent beyond what is in the student's code.

    Provide your evaluation in VALID JSON format only (no markdown, no prose).

    Problem:
    {question}

    Rubric (target total = 100 points):
    {rubric_block}

    Student Code:
    {student_code}

    STRICT INSTRUCTIONS:
    1) Evaluate each rubric category independently. If a category is missing evidence, assign 0.
    2) For each category, return:
       {{
         "criterion": "<category name>",
         "score": <int>,
         "max_score": <int>,
         "feedback": "<<=2 short sentences>"
       }}
    3) The "total_score" MUST equal the sum of all category scores.
    4) "max_possible_score" MUST be 100.
    5) Include an "overall_feedback" field (2–3 concise sentences on correctness, clarity, and code quality).
    6) Return ONLY JSON with exactly these keys:
       {{
         "criteria_scores": [...],
         "total_score": <int>,
         "max_possible_score": 100,
         "overall_feedback": "<summary>"
       }}
    7) If the output cannot be validated as JSON, it is incorrect.
    """.strip()

    return prompt
