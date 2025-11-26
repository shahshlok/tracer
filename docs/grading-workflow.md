# Grading Workflow

This document explains the end-to-end grading workflow, from student submissions to evaluation output.

## Table of Contents

- [Overview](#overview)
- [Step-by-Step Process](#step-by-step-process)
- [Detailed Walkthrough](#detailed-walkthrough)
- [Error Handling](#error-handling)
- [Output Structure](#output-structure)

## Overview

The grading workflow processes student code submissions through multiple LLMs, collecting structured evaluations including scores, feedback, and misconceptions.

```
Student Submission (Q1.java)
         │
         ▼
┌─────────────────────────┐
│  1. Load Resources      │  question.md + rubric.md + student code
└─────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│  2. Construct Prompt    │  Using direct/reverse/EME strategy
└─────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│  3. Multi-Model Eval    │  Parallel calls to Gemini, GPT-5, etc.
└─────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│  4. Parse Responses     │  Validate with Pydantic models
└─────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│  5. Assemble Document   │  Create EvaluationDocument
└─────────────────────────┘
         │
         ▼
┌─────────────────────────┐
│  6. Save JSON           │  student_evals/{id}_{q}_eval.json
└─────────────────────────┘
```

## Step-by-Step Process

### Step 1: Discovery

The CLI scans `student_submissions/` for student directories:

```python
def get_student_list(submission_dir: str = "student_submissions") -> list[str]:
    return sorted([
        d for d in os.listdir(submission_dir) 
        if os.path.isdir(os.path.join(submission_dir, d))
    ])

# Result: ["Chen_Wei_200023", "Alice_Smith_200024", ...]
```

### Step 2: Load Resources

For each question (Q1-Q4), load:

```python
# Question markdown
question_text = load_question("data/a2/q1.md")

# Rubric (JSON or markdown table)
rubric_data = load_rubric("data/a2/rubric_q1.md")

# Student code
with open(f"student_submissions/{student_id}/Q1.java") as f:
    student_code = f.read()
```

**Rubric Parsing:**

The rubric can be in two formats:

1. **JSON format:**
```json
{
  "total_points": 4,
  "categories": [
    {"task": "Reading input", "points": 1, "topic": "Scanner"},
    {"task": "Variables", "points": 1, "topic": "Data Types"}
  ]
}
```

2. **Markdown table format:**
```markdown
| Tasks | Marks | Topic | Why? |
|-------|-------|-------|------|
| Reading input using Scanner | +1 | Reading input | ... |
| Declaring variables (double) | +1 | Variables, Data types | ... |
```

### Step 3: Construct Prompt

Using the Direct grading strategy (default):

```python
def construct_prompt(question: str, rubric: dict, student_code: str) -> str:
    return f"""
You are an expert code evaluator. Evaluate the following student submission...

## Assignment Requirements:
{question}

## Grading Rubric:
{json.dumps(rubric, indent=2)}

## Student Submission:
```java
{student_code}
```

Please evaluate this submission and provide:
1. Scores (total, per-category)
2. Category Scores (with reasoning and confidence)
3. Feedback (strengths, areas for improvement)
4. Misconceptions (with evidence)
"""
```

### Step 4: Multi-Model Evaluation

All models are called in parallel:

```python
async def grade_student_with_models(student_code, question, rubric):
    prompt = construct_prompt(question, rubric, student_code)
    messages = [{"role": "user", "content": prompt}]
    
    # Parallel model calls
    tasks = [grade_with_model(model, messages) for model in MODELS]
    results = await asyncio.gather(*tasks)
    
    return dict(zip(MODELS, results))
```

Each model call uses Instructor for structured output:

```python
async def grade_with_model(model: str, messages: list) -> ModelEvaluation:
    response = await get_structured_response(
        messages=messages,
        response_model=LLMEvaluationResponse,
        model=model
    )
    
    return ModelEvaluation(
        model_name=model.split("/")[-1],
        provider=model.split("/")[0],
        run_id=f"run_{uuid4().hex[:8]}",
        config=Config(system_prompt_id="direct_v1", rubric_prompt_id="q1_v1"),
        **response.model_dump()
    )
```

### Step 5: Assemble Document

Create the complete evaluation document:

```python
def create_evaluation_document(
    student_id, student_name, question_text, rubric_data, 
    student_file_name, model_evaluations, **kwargs
) -> EvaluationDocument:
    
    return EvaluationDocument(
        evaluation_id=f"eval_{student_id}_{datetime.now().strftime('%Y%m%d')}",
        schema_version="1.0.0",
        created_at=datetime.now(),
        created_by="cli.py",
        context=Context(
            course_id="COSC121",
            course_name="Introduction to Programming",
            assignment_id=2,
            assignment_title="Assignment 2",
            question_id="q1",
            question_title="Question 1",
            question_source_path=kwargs.get("question_source_path", ""),
            rubric_source_path=kwargs.get("rubric_source_path", "")
        ),
        submission=Submission(
            student_id=student_id,
            student_name=student_name,
            submitted_at=datetime.now(),
            programming_language="java",
            files=[StudentFile(path=student_file_name, language="java")]
        ),
        rubric=Rubric(
            rubric_id=f"a2_{kwargs.get('question_id', 'q1')}_v1",
            title="Q1 Rubric",
            total_points=rubric_data["total_points"],
            categories=[
                RubricCategory(
                    category_id=f"cat_{i}",
                    task=cat["task"],
                    points=cat["points"],
                    topic=cat.get("topic", ""),
                    description=cat.get("description", "")
                )
                for i, cat in enumerate(rubric_data["categories"])
            ]
        ),
        models=model_evaluations
    )
```

### Step 6: Save Output

```python
output_file = f"student_evals/{student_id}_{q_id}_eval.json"
with open(output_file, "w") as f:
    f.write(eval_doc.model_dump_json(indent=2))
```

## Detailed Walkthrough

### Example: Grading Chen_Wei_200023

**Input Files:**

1. `data/a2/q1.md` - Question requirements
2. `data/a2/rubric_q1.md` - Grading criteria
3. `student_submissions/Chen_Wei_200023/Q1.java` - Student code

**Student Code:**
```java
import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter x1: ");
        double x1 = input.nextDouble();
        // ... (contains a bug: uses + instead of pow for squaring)
        double distance = Math.sqrt((x2 - x1) + (y2 - y1));  // Bug!
        System.out.println("Distance: " + distance);
    }
}
```

**Model A Response (Gemini):**
```json
{
  "scores": {
    "total_points_awarded": 3.0,
    "max_points": 4.0,
    "percentage": 75.0
  },
  "category_scores": [
    {
      "task": "Reading input using Scanner",
      "points_awarded": 1.0,
      "max_points": 1.0,
      "reasoning": "Correctly uses Scanner with nextDouble()",
      "confidence": 0.95
    },
    {
      "task": "Distance formula calculation",
      "points_awarded": 0.0,
      "max_points": 1.0,
      "reasoning": "Uses addition instead of squaring differences",
      "confidence": 0.92
    }
  ],
  "misconceptions": [
    {
      "topic": "Variables",
      "task": "Distance formula calculation",
      "name": "Incorrect application of distance formula",
      "description": "Student adds differences instead of squaring them",
      "confidence": 0.95,
      "evidence": [
        {
          "source": "student_code",
          "file_path": "Q1.java",
          "snippet": "Math.sqrt((x2 - x1) + (y2 - y1))",
          "line_start": 8,
          "line_end": 8,
          "note": "Missing Math.pow() for squaring"
        }
      ]
    }
  ]
}
```

**Model B Response (GPT-5 Nano):**
```json
{
  "scores": {
    "total_points_awarded": 3.5,
    "max_points": 4.0,
    "percentage": 87.5
  },
  "misconceptions": [
    {
      "name": "Incorrect formula for Euclidean distance",
      "topic": "Variables",
      "confidence": 0.88
    }
  ]
}
```

**Final Output:** `student_evals/Chen_Wei_200023_q1_eval.json`

Both model evaluations are stored, allowing comparison and ensemble analysis.

## Error Handling

### Missing Files

```python
if not os.path.exists(student_file_path):
    student_results.append({
        "status": "skipped",
        "student": student_id,
        "question": q_id,
        "error": "File not found"
    })
    continue
```

### API Failures

The OpenRouter SDK includes retry logic:

```python
@retry(
    wait=wait_exponential(multiplier=1, min=4, max=10),
    stop=stop_after_attempt(3),
    before_sleep=before_sleep_log(logger, logging.WARNING)
)
async def get_structured_response(...):
    # Retries up to 3 times with exponential backoff
```

### All Models Failed

```python
valid_evals = {k: v for k, v in model_evals.items() if v is not None}

if not valid_evals:
    progress.console.print(f"[red]All models failed for {student_id} {q_id}[/red]")
    student_results.append({
        "status": "error",
        "student": student_id,
        "question": q_id,
        "error": "All models failed"
    })
```

## Output Structure

### Per-Student Per-Question

Each evaluation file contains:

```
student_evals/
├── Chen_Wei_200023_q1_eval.json
├── Chen_Wei_200023_q2_eval.json
├── Chen_Wei_200023_q3_eval.json
├── Chen_Wei_200023_q4_eval.json
├── Alice_Smith_200024_q1_eval.json
└── ...
```

### File Contents

See [Pydantic Models](pydantic-models.md) for the complete schema.

Key sections:
- `context` - Course, assignment, question metadata
- `submission` - Student info and files
- `rubric` - Grading criteria
- `models` - Per-model evaluations with scores, feedback, misconceptions

## Related Documentation

- [Architecture](architecture.md) - System design
- [Prompt Strategies](prompts.md) - Different grading approaches
- [Misconception Analysis](misconception-analysis.md) - Post-grading analysis
- [CLI Reference](cli-reference.md) - Command usage
