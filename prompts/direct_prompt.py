"""Direct grading prompt builder.

In this paradigm, the model grades the student code directly against the rubric
without any intermediate steps.
"""
from __future__ import annotations

import json
from typing import Any, Dict


def build_prompt(question: str, rubric_json: Dict[str, Any], student_code: str) -> str:
    """Build a direct grading prompt.

    The model is instructed to evaluate the student's code strictly according to
    the rubric and return structured JSON feedback.
    """
    rubric_block = json.dumps(rubric_json, indent=2)

    prompt = f"""
You are a teaching assistant grading a student's program according to the provided rubric.
Evaluate the student's code strictly and return VALID JSON only.

Problem:
{question}

Rubric:
{rubric_block}

Student Code:
{student_code}

STRICT INSTRUCTIONS:
1) Score ONLY according to the rubric criteria above.
2) For each criterion, provide: criterion name, awarded score, max score, and concise feedback.
3) total_score MUST equal the sum of all criterion scores.
4) max_possible_score MUST match the rubric total.
5) Return ONLY JSON with EXACTLY these top-level keys:
   {{
     "criteria_scores": [
       {{"criterion": "<name>", "score": <int>, "max_score": <int>, "feedback": "<brief>"}},
       ...
     ],
     "total_score": <number>,
     "max_possible_score": <number>,
     "overall_feedback": "<2-3 sentences summarizing the evaluation>"
   }}
6) Do NOT include markdown fences, explanations, or any text outside the JSON.
""".strip()
    return prompt
