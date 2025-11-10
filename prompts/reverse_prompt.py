"""Reverse grading prompt builder.

In this paradigm, the model first generates an ideal reference solution,
then compares the student's code against that reference.
"""
from __future__ import annotations

import json
from typing import Any, Dict


def build_prompt(question: str, rubric_json: Dict[str, Any], student_code: str) -> str:
    """Build a reverse grading prompt.

    The model is instructed to:
    1. Generate an ideal reference solution following the rubric
    2. Compare the student's code against that ideal solution
    3. Grade strictly based on the comparison
    """
    rubric_block = json.dumps(rubric_json, indent=2)

    prompt = f"""
You are a teaching assistant using reverse grading methodology.
First, write an IDEAL REFERENCE SOLUTION following the rubric.
Then, compare the student's code against your ideal solution.
Grade strictly according to rubric criteria and return VALID JSON only.

Problem:
{question}

Rubric:
{rubric_block}

Student Code:
{student_code}

STRICT INSTRUCTIONS:
1) First, mentally construct an ideal reference solution that satisfies all rubric criteria.
2) Compare the student's code against your reference solution.
3) Score according to how well the student's code matches the ideal approach.
4) For each criterion, provide: criterion name, awarded score, max score, and feedback explaining the gap.
5) total_score MUST equal the sum of all criterion scores.
6) max_possible_score MUST match the rubric total.
7) Return ONLY JSON with EXACTLY these top-level keys:
   {{
     "criteria_scores": [
       {{"criterion": "<name>", "score": <int>, "max_score": <int>, "feedback": "<comparison note>"}},
       ...
     ],
     "total_score": <number>,
     "max_possible_score": <number>,
     "overall_feedback": "<2-3 sentences comparing student code to ideal solution>"
   }}
8) Do NOT include the reference solution code, markdown fences, or any text outside the JSON.
""".strip()
    return prompt
