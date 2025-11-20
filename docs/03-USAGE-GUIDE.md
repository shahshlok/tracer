# Usage Guide: Step-by-Step Instructions

This guide shows you exactly how to use the EME Framework for your own evaluations.

## Table of Contents

1. [Basic Workflow](#basic-workflow)
2. [Evaluating Your First Student](#evaluating-your-first-student)
3. [Evaluating Multiple Students](#evaluating-multiple-students)
4. [Choosing Different AI Models](#choosing-different-ai-models)
5. [Understanding Your Results](#understanding-your-results)
6. [Advanced Usage](#advanced-usage)

---

## Basic Workflow

The standard workflow has 4 steps:

```
Step 1: Prepare        → Organize your files
         ├─ Assignment question
         ├─ Grading rubric
         └─ Student code

Step 2: Create script  → Write Python code to run evaluation
         └─ Copy from grade_sergio.py, modify for your needs

Step 3: Run            → Execute the script
         └─ uv run python your_script.py

Step 4: Analyze        → Look at the results
         └─ Check the JSON file in student_evals/
```

---

## Evaluating Your First Student

### Step 1: Create Assignment Files

Create a folder for your assignment:

```bash
# Create folders
mkdir -p assignment_materials/my_assignment
```

Inside that folder, create:

**question.md** - The assignment description:
```markdown
# Assignment: Implement a Calculator

Write a Calculator class in Java that implements:
- add(a, b) - returns sum
- subtract(a, b) - returns difference
- multiply(a, b) - returns product
- divide(a, b) - returns quotient

Requirements:
- Handle division by zero
- Use appropriate access modifiers
- Include proper error handling
```

**rubric.json** - The grading scale:
```json
{
  "totalPoints": 100,
  "categories": [
    {
      "categoryId": "correctness",
      "name": "Correctness",
      "maxPoints": 50,
      "description": "Code performs required operations correctly"
    },
    {
      "categoryId": "errorHandling",
      "name": "Error Handling",
      "maxPoints": 25,
      "description": "Properly handles edge cases and errors"
    },
    {
      "categoryId": "codeQuality",
      "name": "Code Quality",
      "maxPoints": 25,
      "description": "Code is readable and well-structured"
    }
  ]
}
```

### Step 2: Add Student Code

Create a student folder:

```bash
# Create student submission folder
mkdir -p student_submissions/Johnson_Alice_100042

# Put their code in it
# (You would copy their file here)
```

**student_submissions/Johnson_Alice_100042/Calculator.java:**
```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
        return (double) a / b;
    }
}
```

### Step 3: Create Evaluation Script

Create **evaluate_calculator.py** (copy from `grade_sergio.py` and modify):

```python
import json
import uuid
from datetime import datetime, timezone

from pydantic_models import (
    Context, Submission, Rubric, RubricCategory, StudentFile,
    EvaluationDocument, ModelEvaluation, Config, LLMEvaluationResponse
)
from utils.openrouter_sdk import get_structured_response

# ============================================================================
# 1. LOAD ASSIGNMENT MATERIALS
# ============================================================================

print("Loading assignment materials...")

# Load question
with open("assignment_materials/my_assignment/question.md") as f:
    question_text = f.read()

# Load rubric
with open("assignment_materials/my_assignment/rubric.json") as f:
    rubric_data = json.load(f)

# Load student code
student_id = "Johnson_Alice_100042"
with open(f"student_submissions/{student_id}/Calculator.java") as f:
    student_code = f.read()

print(f"✓ Loaded question, rubric, and code for {student_id}")

# ============================================================================
# 2. BUILD EVALUATION PROMPT
# ============================================================================

print("Building evaluation prompt...")

prompt = f"""You are an expert computer science instructor grading a student assignment.

**ASSIGNMENT QUESTION:**
{question_text}

**GRADING RUBRIC:**
{json.dumps(rubric_data, indent=2)}

**STUDENT SUBMISSION:**
```java
{student_code}
```

Please evaluate this submission against the rubric. Provide:
1. A score for each category (with reasoning)
2. Overall score as a percentage
3. Feedback on strengths and areas for improvement
4. Any misconceptions you identify in the code

Be fair but rigorous in your evaluation."""

# ============================================================================
# 3. GET EVALUATIONS FROM MULTIPLE MODELS
# ============================================================================

print("Getting evaluations from AI models...")
print("(This may take a minute or two...)\n")

models_to_test = [
    "google/gemini-2.5-flash-lite",      # Fast and cheap
    "anthropic/claude-3-sonnet",         # Balanced
    "openai/gpt-4o"                      # Most expensive but highest quality
]

model_evals = {}

for model_name in models_to_test:
    print(f"  Evaluating with {model_name}...")

    # Get structured response from the model
    try:
        llm_response = get_structured_response(
            messages=[{"role": "user", "content": prompt}],
            response_model=LLMEvaluationResponse,
            model=model_name
        )

        # Wrap in ModelEvaluation with metadata
        model_eval = ModelEvaluation(
            model_name=model_name,
            provider="openrouter",
            run_id=f"run_{uuid.uuid4().hex[:8]}",
            config=Config(
                system_prompt_id="direct_grading",
                rubric_prompt_id="calculator_v1"
            ),
            scores=llm_response.scores,
            category_scores=llm_response.category_scores,
            feedback=llm_response.feedback,
            misconceptions=llm_response.misconceptions,
        )

        # Store with short model name as key
        short_name = model_name.split("/")[-1]
        model_evals[short_name] = model_eval
        print(f"    ✓ Score: {model_eval.scores.percentage}%")

    except Exception as e:
        print(f"    ✗ Error: {str(e)}")

# ============================================================================
# 4. BUILD CONTEXT AND RUBRIC OBJECTS
# ============================================================================

print("\nAssembling evaluation document...")

# Create context (metadata about the assignment)
context = Context(
    course_id="CS101",
    course_name="Introduction to Computer Science",
    assignment_id=1,
    assignment_title="Calculator Implementation",
    question_source_path="assignment_materials/my_assignment/question.md",
    question_id="calc_1",
    question_title="Build a Calculator Class",
    rubric_source_path="assignment_materials/my_assignment/rubric.json"
)

# Create submission (student info)
submission = Submission(
    student_id=student_id,
    student_name="Alice Johnson",
    submitted_at=datetime(2025, 11, 19, 14, 30, 0, tzinfo=timezone.utc),
    programming_language="Java",
    files=[StudentFile(path="Calculator.java", language="Java")]
)

# Parse rubric JSON into Rubric object
rubric = Rubric(
    rubric_id="calc_rubric_v1",
    title="Calculator Assignment Rubric",
    total_points=rubric_data.get("totalPoints", 100),
    categories=[
        RubricCategory(
            category_id=cat["categoryId"],
            name=cat["name"],
            max_points=cat["maxPoints"],
            description=cat["description"]
        )
        for cat in rubric_data["categories"]
    ]
)

# ============================================================================
# 5. CREATE EVALUATION DOCUMENT
# ============================================================================

eval_doc = EvaluationDocument(
    evaluation_id=f"eval_{uuid.uuid4().hex[:8]}",
    schema_version="1.0.0",
    created_at=datetime.now(timezone.utc),
    created_by="evaluate_calculator.py",
    context=context,
    submission=submission,
    rubric=rubric,
    models=model_evals
)

# ============================================================================
# 6. SAVE RESULTS
# ============================================================================

output_file = f"student_evals/{student_id}_eval.json"

with open(output_file, "w") as f:
    f.write(eval_doc.model_dump_json(indent=2))

print(f"✓ Evaluation saved to {output_file}")

# ============================================================================
# 7. PRINT SUMMARY
# ============================================================================

print("\n" + "="*60)
print("EVALUATION SUMMARY")
print("="*60)

for model_name, model_eval in model_evals.items():
    print(f"\n{model_name}:")
    print(f"  Score: {model_eval.scores.percentage}% ({model_eval.scores.total_points_awarded}/{model_eval.scores.max_points})")
    print(f"  Confidence: {model_eval.scores.confidence}")

    if model_eval.feedback.strengths:
        print(f"  Strengths:")
        for strength in model_eval.feedback.strengths[:2]:
            print(f"    • {strength}")

    if model_eval.misconceptions:
        print(f"  Misconceptions found:")
        for misc in model_eval.misconceptions[:2]:
            print(f"    • {misc.description}")

print("\n" + "="*60)
print("Done! Check the JSON file for complete details.")
```

### Step 4: Run the Evaluation

```bash
# Make sure you're in the project root
cd ensemble-eval-cli

# Run your evaluation script
uv run python evaluate_calculator.py
```

**Expected output:**
```
Loading assignment materials...
✓ Loaded question, rubric, and code for Johnson_Alice_100042

Building evaluation prompt...

Getting evaluations from AI models...
(This may take a minute or two...)

  Evaluating with google/gemini-2.5-flash-lite...
    ✓ Score: 82.0%
  Evaluating with anthropic/claude-3-sonnet...
    ✓ Score: 85.0%
  Evaluating with openai/gpt-4o...
    ✓ Score: 83.0%

Assembling evaluation document...
✓ Evaluation saved to student_evals/Johnson_Alice_100042_eval.json

============================================================
EVALUATION SUMMARY
============================================================

google-gemini-2.5-flash-lite:
  Score: 82.0% (82/100)
  Confidence: 0.88
  Strengths:
    • Clear method names
    • Good error handling for division
  Misconceptions found:
    • Missing null checks for input parameters

anthropic/claude-3-sonnet:
  Score: 85.0% (85/100)
  ...
```

### Step 5: View Your Results

Open **student_evals/Johnson_Alice_100042_eval.json** to see the complete evaluation in JSON format.

---

## Evaluating Multiple Students

To grade a whole class, create a loop:

**evaluate_all_students.py:**

```python
import json
import uuid
from datetime import datetime, timezone
import os

from pydantic_models import (
    Context, Submission, Rubric, RubricCategory, StudentFile,
    EvaluationDocument, ModelEvaluation, Config, LLMEvaluationResponse
)
from utils.openrouter_sdk import get_structured_response

# Load materials once
with open("assignment_materials/my_assignment/question.md") as f:
    question_text = f.read()

with open("assignment_materials/my_assignment/rubric.json") as f:
    rubric_data = json.load(f)

# List all students
student_folder = "student_submissions"
student_ids = [d for d in os.listdir(student_folder)
               if os.path.isdir(os.path.join(student_folder, d))]

print(f"Found {len(student_ids)} students to evaluate")
print("="*60)

# Evaluate each student
for student_id in student_ids:
    print(f"\nEvaluating {student_id}...")

    # Load student code
    student_path = os.path.join(student_folder, student_id)
    code_files = [f for f in os.listdir(student_path)
                  if f.endswith('.java')]

    if not code_files:
        print(f"  ✗ No Java files found, skipping")
        continue

    student_code = ""
    for code_file in code_files:
        with open(os.path.join(student_path, code_file)) as f:
            student_code += f"// File: {code_file}\n{f.read()}\n\n"

    # Build prompt
    prompt = f"""You are an expert computer science instructor grading a student assignment.

**ASSIGNMENT QUESTION:**
{question_text}

**GRADING RUBRIC:**
{json.dumps(rubric_data, indent=2)}

**STUDENT SUBMISSION:**
```java
{student_code}
```

Please evaluate this submission against the rubric."""

    # Get evaluations
    model_evals = {}
    models = ["google/gemini-2.5-flash-lite"]  # Use just one for speed

    for model_name in models:
        try:
            llm_response = get_structured_response(
                messages=[{"role": "user", "content": prompt}],
                response_model=LLMEvaluationResponse,
                model=model_name
            )

            model_eval = ModelEvaluation(
                model_name=model_name,
                provider="openrouter",
                run_id=f"run_{uuid.uuid4().hex[:8]}",
                config=Config(
                    system_prompt_id="direct_grading",
                    rubric_prompt_id="v1"
                ),
                scores=llm_response.scores,
                category_scores=llm_response.category_scores,
                feedback=llm_response.feedback,
                misconceptions=llm_response.misconceptions,
            )

            short_name = model_name.split("/")[-1]
            model_evals[short_name] = model_eval
            print(f"  ✓ Score: {model_eval.scores.percentage}%")

        except Exception as e:
            print(f"  ✗ Error: {str(e)}")
            continue

    if not model_evals:
        print(f"  No successful evaluations, skipping")
        continue

    # Build evaluation document
    eval_doc = EvaluationDocument(
        evaluation_id=f"eval_{uuid.uuid4().hex[:8]}",
        schema_version="1.0.0",
        created_at=datetime.now(timezone.utc),
        created_by="evaluate_all_students.py",
        context=Context(
            course_id="CS101",
            course_name="Introduction to Computer Science",
            assignment_id=1,
            assignment_title="Calculator Implementation",
            question_source_path="assignment_materials/my_assignment/question.md",
            question_id="calc_1",
            question_title="Build a Calculator Class",
            rubric_source_path="assignment_materials/my_assignment/rubric.json"
        ),
        submission=Submission(
            student_id=student_id,
            student_name=student_id.replace("_", " "),
            submitted_at=datetime.now(timezone.utc),
            programming_language="Java",
            files=[StudentFile(path=f, language="Java") for f in code_files]
        ),
        rubric=Rubric(
            rubric_id="calc_rubric_v1",
            title="Calculator Assignment Rubric",
            total_points=rubric_data.get("totalPoints", 100),
            categories=[
                RubricCategory(
                    category_id=cat["categoryId"],
                    name=cat["name"],
                    max_points=cat["maxPoints"],
                    description=cat["description"]
                )
                for cat in rubric_data["categories"]
            ]
        ),
        models=model_evals
    )

    # Save
    output_file = f"student_evals/{student_id}_eval.json"
    with open(output_file, "w") as f:
        f.write(eval_doc.model_dump_json(indent=2))

print("\n" + "="*60)
print("All evaluations complete!")
```

**Run it:**
```bash
uv run python evaluate_all_students.py
```

---

## Choosing Different AI Models

The framework supports many models. Change them in your script:

### Available Models via OpenRouter

```python
models_to_test = [
    # FAST (cheapest, fastest)
    "google/gemini-2.5-flash-lite",

    # BALANCED (good speed and quality)
    "anthropic/claude-3-sonnet",
    "google/gemini-pro",

    # HIGH QUALITY (best but slower/more expensive)
    "openai/gpt-4o",
    "anthropic/claude-3-opus",

    # EXPERIMENTAL/SPECIALIZED
    "moonshotai/kimi-k2-0905",
    "meta-llama/llama-3-70b",
]
```

### Cost vs Quality Trade-off

```
Budget Limited?          → google/gemini-2.5-flash-lite
Want Balanced?          → anthropic/claude-3-sonnet
Need Best Quality?      → openai/gpt-4o
Evaluating Code?        → openai/gpt-4o (excels at technical tasks)
```

### Using Only OpenAI Models

If you prefer OpenAI only:

```python
from utils.openai_client import evaluation_with_openai

result = evaluation_with_openai(
    question=question_text,
    rubric=json.dumps(rubric_data),
    student_code=student_code,
    model="gpt-4o"
)
```

---

## Understanding Your Results

### The JSON Structure

Your results file contains:

```json
{
  "evaluation_id": "eval_a1b2c3d4",
  "schema_version": "1.0.0",
  "created_at": "2025-11-19T10:30:00Z",
  "created_by": "evaluate_calculator.py",

  "context": {
    "course_id": "CS101",
    "assignment_title": "Calculator Implementation"
  },

  "submission": {
    "student_id": "Johnson_Alice_100042",
    "student_name": "Alice Johnson",
    "programming_language": "Java"
  },

  "models": {
    "gemini-2.5-flash-lite": {
      "scores": {
        "total_points_awarded": 82,
        "max_points": 100,
        "percentage": 82.0,
        "confidence": 0.88
      },
      "category_scores": [
        {
          "category_id": "correctness",
          "points_awarded": 45,
          "max_points": 50,
          "reasoning": "Code implements required operations correctly..."
        }
      ],
      "feedback": {
        "strengths": ["Clear method names", "Good error handling"],
        "areas_for_improvement": ["Could add input validation"]
      },
      "misconceptions": [
        {
          "description": "Doesn't handle null inputs",
          "confidence": 0.82,
          "evidence": [
            {
              "code_snippet": "public int add(int a, int b) {",
              "line_number": 3,
              "explanation": "No null check before operation"
            }
          ]
        }
      ]
    }
  }
}
```

### How to Read the Scores

**Percentage (0-100):**
- 90-100: Excellent
- 80-89: Good
- 70-79: Satisfactory
- 60-69: Needs improvement
- Below 60: Major issues

**Confidence (0-1):**
- 0.9+: Model is very confident
- 0.7-0.9: Fairly confident
- Below 0.7: Uncertain

### Comparing Models

Look for patterns:

```python
# Load your evaluation
with open("student_evals/Johnson_Alice_100042_eval.json") as f:
    eval_doc = json.load(f)

# Compare scores
print("Score Comparison:")
for model_name, model_eval in eval_doc["models"].items():
    score = model_eval["scores"]["percentage"]
    print(f"  {model_name}: {score}%")

# Find common misconceptions
all_misconceptions = {}
for model_name, model_eval in eval_doc["models"].items():
    for misc in model_eval["misconceptions"]:
        desc = misc["description"]
        if desc not in all_misconceptions:
            all_misconceptions[desc] = []
        all_misconceptions[desc].append(model_name)

print("\nMisconceptions found by multiple models:")
for desc, models in all_misconceptions.items():
    if len(models) > 1:
        print(f"  {desc}")
        print(f"    Found by: {', '.join(models)}")
```

---

## Advanced Usage

### Custom Prompts

Create your own grading strategy:

**prompts/custom_prompt.py:**
```python
def build_custom_prompt(question, rubric, student_code):
    """Custom evaluation focusing on security issues."""
    return f"""
You are a security expert evaluating code.

**ASSIGNMENT:**
{question}

**RUBRIC:**
{rubric}

**CODE:**
{student_code}

Focus on security issues:
- Input validation
- Error handling
- Edge cases

Evaluate on the provided rubric.
"""
```

Use it:
```python
from prompts.custom_prompt import build_custom_prompt

prompt = build_custom_prompt(question, rubric_json_str, code)
```

### Batch Processing with Progress

```python
from tqdm import tqdm  # Progress bar library

for student_id in tqdm(student_ids, desc="Evaluating"):
    # ... evaluation code ...
```

### Saving Intermediate Results

```python
# Save partially completed evaluations
import json

progress_file = "evaluation_progress.json"
completed = {}

for student_id in student_ids:
    # ... evaluation code ...

    # Save after each student
    with open(progress_file, "w") as f:
        json.dump(completed, f)

    completed[student_id] = results
```

---

## Quick Reference

**Run example:** `uv run python grade_sergio.py`

**View tests:** `uv run pytest -v`

**Quick evaluation:**
```python
from utils.openrouter_sdk import get_structured_response
from pydantic_models import LLMEvaluationResponse

response = get_structured_response(
    messages=[{"role": "user", "content": your_prompt}],
    response_model=LLMEvaluationResponse,
    model="google/gemini-2.5-flash-lite"
)
print(response.scores.percentage)
```

---

**Next:** See [`04-API-REFERENCE.md`](04-API-REFERENCE.md) for complete technical details.
