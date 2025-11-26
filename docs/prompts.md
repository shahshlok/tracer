# Prompt Strategies

This document describes the three grading prompt strategies available in the EME CLI framework.

## Table of Contents

- [Overview](#overview)
- [Direct Grading](#direct-grading)
- [Reverse Grading](#reverse-grading)
- [EME (Ensemble Method Evaluation)](#eme-ensemble-method-evaluation)
- [Comparison](#comparison)
- [Custom Prompts](#custom-prompts)

## Overview

The framework supports three distinct grading strategies, each with different approaches to evaluating student code:

| Strategy | File | Approach |
|----------|------|----------|
| Direct | `prompts/direct_prompt.py` | Grade code directly against rubric |
| Reverse | `prompts/reverse_prompt.py` | Generate ideal solution, then compare |
| EME | `prompts/eme_prompt.py` | Strict 100-point rubric enforcement |

## Direct Grading

**Location:** `prompts/direct_prompt.py`

The default strategy. The model evaluates student code directly against the rubric without intermediate steps.

### How It Works

1. Present the question requirements
2. Present the grading rubric
3. Present the student code
4. Ask for structured evaluation

### Prompt Structure

```python
def build_prompt(question: str, rubric_json: dict, student_code: str) -> str:
    return f"""
You are an expert code evaluator. Evaluate the following student submission 
against the assignment requirements and rubric.

## Assignment Requirements:
{question}

## Grading Rubric:
{json.dumps(rubric_json, indent=2)}

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
   - Topic (Variables, Data Types, Constants, or Reading input)
   - Name and description
   - Confidence (0-1)
   - Evidence (code snippet, file path, line numbers)
"""
```

### Strengths

- Simple and straightforward
- Fast (single-pass evaluation)
- Works well for clear-cut rubrics

### Weaknesses

- May miss subtle issues
- Can be biased by first impressions
- No reference point for comparison

## Reverse Grading

**Location:** `prompts/reverse_prompt.py`

The model first generates an ideal reference solution, then compares the student's code against it.

### How It Works

1. Present the question and rubric
2. Ask model to mentally construct ideal solution
3. Compare student code against ideal
4. Grade based on the gap

### Prompt Structure

```python
def build_prompt(question: str, rubric_json: dict, student_code: str) -> str:
    return f"""
You are a teaching assistant using reverse grading methodology.
First, write an IDEAL REFERENCE SOLUTION following the rubric.
Then, compare the student's code against your ideal solution.
Grade strictly according to rubric criteria and return VALID JSON only.

Problem:
{question}

Rubric:
{json.dumps(rubric_json, indent=2)}

Student Code:
{student_code}

STRICT INSTRUCTIONS:
1) First, mentally construct an ideal reference solution that satisfies all rubric criteria.
2) Compare the student's code against your reference solution.
3) Score according to how well the student's code matches the ideal approach.
4) For each criterion, provide: criterion name, awarded score, max score, 
   and feedback explaining the gap.
5) total_score MUST equal the sum of all criterion scores.
6) max_possible_score MUST match the rubric total.
7) Return ONLY JSON with EXACTLY these top-level keys:
   {{
     "criteria_scores": [...],
     "total_score": <number>,
     "max_possible_score": <number>,
     "overall_feedback": "<comparison note>"
   }}
8) Do NOT include the reference solution code, markdown fences, 
   or any text outside the JSON.
"""
```

### Strengths

- Creates consistent evaluation baseline
- Better at identifying missing features
- More objective comparison

### Weaknesses

- Slower (two-phase reasoning)
- Reference solution may not match instructor intent
- Can penalize valid alternative approaches

## EME (Ensemble Method Evaluation)

**Location:** `prompts/eme_prompt.py`

A strict 100-point normalized rubric approach designed for multi-model ensemble analysis.

### How It Works

1. Enforce 100-point scale
2. Strict JSON-only output
3. Independent category evaluation
4. No inference or assumptions

### Prompt Structure

```python
def build_eme_prompt(question: str, rubric_json: dict, student_code: str) -> str:
    return f"""
You are an automated programming assignment grader using a standardized rubric.
Be impartial, consistent, and evaluate purely by rubric adherence.
Do not infer or assume intent beyond what is in the student's code.

Provide your evaluation in VALID JSON format only (no markdown, no prose).

Problem:
{question}

Rubric (target total = 100 points):
{json.dumps(rubric_json, indent=2)}

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
5) Include an "overall_feedback" field (2–3 concise sentences).
6) Return ONLY JSON with exactly these keys:
   {{
     "criteria_scores": [...],
     "total_score": <int>,
     "max_possible_score": 100,
     "overall_feedback": "<summary>"
   }}
7) If the output cannot be validated as JSON, it is incorrect.
"""
```

### Strengths

- Consistent 100-point normalization
- Strict JSON enforcement
- Impartial evaluation
- Best for cross-model comparison

### Weaknesses

- Less flexible
- May not capture nuanced feedback
- Requires rubric adaptation to 100-point scale

## Comparison

| Aspect | Direct | Reverse | EME |
|--------|--------|---------|-----|
| Speed | Fast | Medium | Fast |
| Consistency | Medium | High | Highest |
| Flexibility | High | Medium | Low |
| Misconception Detection | Good | Good | Basic |
| Feedback Quality | Detailed | Comparative | Concise |
| Best For | General use | Missing features | Ensemble analysis |

### When to Use Each

**Direct Grading:**
- Standard grading tasks
- When detailed feedback is important
- Quick evaluations

**Reverse Grading:**
- When you want to check for completeness
- Complex multi-part problems
- When students might have partial solutions

**EME:**
- Research comparing multiple models
- When you need consistent scoring
- Statistical analysis of grading

## Custom Prompts

### Creating a New Strategy

1. Create a new file in `prompts/`:

```python
# prompts/custom_prompt.py
"""Custom grading prompt builder."""

from __future__ import annotations
import json
from typing import Any

def build_prompt(question: str, rubric_json: dict[str, Any], student_code: str) -> str:
    """Build a custom grading prompt."""
    rubric_block = json.dumps(rubric_json, indent=2)
    
    prompt = f"""
    [Your custom prompt here]
    
    Question: {question}
    Rubric: {rubric_block}
    Code: {student_code}
    """
    
    return prompt.strip()
```

2. Import in `utils/grading.py`:

```python
from prompts.custom_prompt import build_prompt
```

### Prompt Design Guidelines

1. **Be explicit about output format**
   - Specify exact JSON structure expected
   - List required fields

2. **Provide clear instructions**
   - Number each instruction
   - Be specific about constraints

3. **Include validation rules**
   - Total must equal sum of categories
   - Percentages must be calculated correctly

4. **Specify topic constraints**
   - List the canonical topics
   - Explain what each topic covers

### Example: Detailed Misconception Prompt

```python
def build_detailed_misconception_prompt(question, rubric, code):
    return f"""
You are analyzing student code for misconceptions. Focus on:

1. Conceptual misunderstandings (not just syntax errors)
2. Evidence from the code (specific lines)
3. Confidence in each finding

For each misconception found:
- Topic: One of [Variables, Data Types, Constants, Reading input from the keyboard]
- Name: Brief descriptive label
- Description: What the student misunderstands
- Evidence: Exact code snippet with line numbers
- Confidence: 0.0-1.0 based on evidence strength

Question: {question}
Rubric: {json.dumps(rubric, indent=2)}
Student Code:
```java
{code}
```

Return JSON with "misconceptions" array.
"""
```

## Integration with Instructor

All prompts are used with the Instructor library for structured output:

```python
from pydantic_models.models import LLMEvaluationResponse

response = await client.chat.completions.create(
    model=model,
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,  # Enforces structure
)
```

Instructor automatically:
- Converts Pydantic model to JSON schema
- Validates LLM output against schema
- Retries on validation failure

## Related Documentation

- [Grading Workflow](grading-workflow.md) - How prompts are used
- [Architecture](architecture.md) - System design
- [Pydantic Models](pydantic-models.md) - Response schemas
- [Configuration](configuration.md) - Switching strategies
