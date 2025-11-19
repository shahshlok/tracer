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
    the rubric and return structured JSON feedback matching the LLMEvaluationResponse schema.
    """
    rubric_block = json.dumps(rubric_json, indent=2)

    prompt = f"""
You are an expert code evaluator. Evaluate the following student submission against the assignment requirements and rubric.

## Assignment Requirements:
{question}

## Grading Rubric:
{rubric_block}

## Student Submission:
```java
{student_code}
```

Please evaluate this submission and provide a structured response containing:

1. **Scores**:
   - Total points awarded
   - Max possible points (must match rubric total)
   - Percentage score

2. **Category Scores** (for each rubric category):
   - Points awarded and max points
   - Reasoning for the score
   - Confidence score (0-1)
   - Reasoning tokens (estimate)

3. **Feedback**:
   - Overall comment
   - List of strengths
   - List of areas for improvement

4. **Misconceptions**:
   - Identify any specific misconceptions about the requirements or programming concepts.
   - For each misconception, provide:
     - Name and description
     - Confidence (0-1)
     - Evidence (code snippet, file path, line numbers)

Ensure you calculate the total points and percentage correctly.
""".strip()
    return prompt
