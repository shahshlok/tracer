# Getting Started

This guide describes the conceptual model of the Ensemble Model Evaluation (EME) Framework: what an evaluation is, how models and rubrics interact, and how the pipeline is structured.

## The Big Picture

At a high level, the framework takes an assignment specification, a grading rubric, and one or more student submissions, and produces structured, per-model evaluations suitable for downstream analysis.

```
┌──────────────────────────────────────────────────────────┐
│                  WHAT YOU START WITH                      │
├──────────────────────────────────────────────────────────┤
│  • Assignment question (what students are solving)        │
│  • Grading rubric (how to evaluate the solution)          │
│  • Student code (what the student wrote)                  │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                  THE SYSTEM DOES THIS                     │
├──────────────────────────────────────────────────────────┤
│  1. Sends code + rubric to multiple AI models             │
│  2. Each model evaluates independently                    │
│  3. System collects and compares results                  │
│  4. Identifies patterns and misconceptions                │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                  WHAT YOU GET OUT                         │
├──────────────────────────────────────────────────────────┤
│  • Scores from each model                                 │
│  • Feedback on strengths and areas for improvement        │
│  • Identified misconceptions (with evidence)              │
│  • Confidence levels for each evaluation                  │
│  • Comparison of how models disagree                      │
└──────────────────────────────────────────────────────────┘
```

## Key Concepts

### 1. Evaluation

An **evaluation** is the complete grade for one student's submission. It includes:
- The student's code
- Scores from one or more AI models
- Feedback and identified misconceptions
- Metadata (when it was created, which models were used)

### 2. Models

A **model** is an LLM configuration that grades code. The framework supports:
- **OpenAI models**: GPT-4, GPT-3.5-turbo
- **Anthropic Claude**: Claude 3 Opus, Sonnet
- **Google Gemini**: Multiple versions
- **And many more via OpenRouter**

The same submission can be evaluated by multiple models, enabling comparison and ensemble strategies.

### 3. Rubric

A **rubric** is the grading scale. It breaks grades into categories:

Example rubric for a Cuboid class assignment:
```
Total Points: 100

Categories:
├─ Correctness (40 points)
│  └─ Does the code work correctly?
├─ Code Quality (30 points)
│  └─ Is it well-structured and readable?
├─ Documentation (15 points)
│  └─ Is it well-commented?
└─ Efficiency (15 points)
   └─ Does it perform well?
```

### 4. Misconceptions

A **misconception** is a structured representation of a student misunderstanding. The framework expects models to surface these explicitly and to attach evidence.

Example misconception:
```
Description: Student uses mutable default arguments in method signature
Confidence: 0.85
Evidence: Line 12 - "public Cuboid(int[] dimensions = new int[3])"
Explanation: Default arguments are evaluated once at class definition time.
```

### 5. Confidence

**Confidence** represents the model’s self-reported certainty on a [0, 1] scale; it is useful for weighting ensemble decisions and identifying low-certainty evaluations, but is not itself a guarantee of correctness.

## Evaluation Lifecycle

Here's what happens when you run an evaluation:

```
1. INPUT PREPARATION
   │
   ├─ Read the assignment question
   ├─ Parse the grading rubric
   └─ Load the student's code files
   │
   ▼
2. PROMPT CONSTRUCTION
   │
   └─ Build a detailed prompt with:
      ├─ The question
      ├─ The rubric
      └─ The student's code
   │
   ▼
3. LLM EVALUATION (repeated for each model)
   │
   ├─ Send prompt to Model A
   ├─ Model A analyzes and scores
   ├─ Parse and validate response
   │
   ├─ Send prompt to Model B
   ├─ Model B analyzes and scores
   ├─ Parse and validate response
   │
   └─ ... (repeat for as many models as you want)
   │
   ▼
4. RESULTS ASSEMBLY
   │
   ├─ Collect all model evaluations
   ├─ Combine into one document
   └─ Add metadata (timestamp, IDs, etc.)
   │
   ▼
5. OUTPUT
   │
   └─ Save as JSON file (human-readable)
```

## Core Components

The system has four main technical components:

### 1. Pydantic Models (`pydantic_models/`)

- Define the schema for all evaluation artefacts (`EvaluationDocument`, `ModelEvaluation`, `Scores`, `Misconception`, etc.)
- Enforce constraints (e.g. percentages in [0, 100], confidence in [0, 1], required fields present)
- Provide consistent JSON serialization/deserialization semantics for downstream tools

### 2. LLM Utilities (`utils/`)

- Implement provider-specific clients (`openai_client.py`, `openrouter_sdk.py`)
- Encapsulate authentication, request construction, and error handling
- Return validated Pydantic instances rather than ad-hoc dictionaries

### 3. Prompt Templates (`prompts/`)

- Provide reusable prompt builders for Direct, Reverse, and EME strategies
- Take assignment text, rubric JSON, and student code as inputs and emit a single prompt string
- Allow experiments with different prompting strategies without altering the rest of the pipeline

### 4. Orchestration Scripts and CLI

- `grade_sergio.py`: a complete, single-student example of the evaluation lifecycle
- `cli.py` + `utils/grading.py`: async batch evaluation and summarization over multiple students

## Files and Directories

The project organizes files into clear directories:

```
ensemble-eval-cli/
│
├─ student_submissions/        ← Where you put student code
│  └─ Diaz_Sergio_100029/
│     └─ Cuboid.java           ← Example: one student's code
│
├─ student_evals/              ← Where results are saved
│  └─ sergio_eval.json         ← Example: evaluation results
│
├─ prompts/                    ← How to instruct the AI
├─ utils/                      ← How to contact the AI
├─ pydantic_models/            ← Data structures
│
└─ grade_sergio.py             ← Example evaluation script
```

### Adding Your Own Student

To evaluate your own student, either:

- Copy `grade_sergio.py`, point it at a new `student_submissions/{student_id}/` directory and corresponding assignment/rubric; or
- Use the batch CLI (`uv run python cli.py bench`) after placing student folders under `student_submissions/`.

## Output Format

When you run an evaluation, you get a JSON file. Here's what it contains:

```json
{
  "evaluation_id": "eval_a1b2c3d4",
  "schema_version": "1.0.0",
  "created_at": "2025-11-19T10:30:00Z",

  "context": {
    "course_id": "CS101",
    "assignment_title": "Cuboid Class"
  },

  "submission": {
    "student_id": "Diaz_Sergio_100029",
    "student_name": "Sergio Diaz",
    "programming_language": "Java"
  },

  "rubric": {
    "total_points": 100,
    "categories": [...]
  },

  "models": {
    "gpt-4o": {
      "scores": {
        "percentage": 85.0,
        "confidence": 0.92
      },
      "feedback": {
        "strengths": ["Clear variable naming"],
        "areas_for_improvement": ["Missing input validation"]
      },
      "misconceptions": [
        {
          "description": "Uses mutable default arguments",
          "confidence": 0.85,
          "evidence": [...]
        }
      ]
    },
    "claude-3-opus": {
      "scores": {
        "percentage": 87.0,
        "confidence": 0.89
      },
      ...
    }
  }
}
```

**Key sections:**

- **context**: Educational metadata (course, assignment)
- **submission**: Student info and what they submitted
- **rubric**: The grading scale used
- **models**: Results from each AI model used

## Common Workflows

### Workflow 1: Grade one student via script

```bash
# 1. Prepare student code in student_submissions/{id}/
# 2. Create/modify grade_sergio.py with your details
# 3. Run it
uv run python grade_sergio.py

# 4. Results in student_evals/
```

### Workflow 2: Grade multiple students (CLI)

See `docs/03-USAGE-GUIDE.md` for a full batch example and the CLI workflow. At a high level:

- Place one folder per student under `student_submissions/`
- Ensure `question_cuboid.md` and `rubric_cuboid.json` (or your equivalents) are present
- Run `uv run python cli.py bench`

### Workflow 3: Try different models

Change the models list in your script:

```python
models_to_test = [
    "gpt-4o",
    "anthropic/claude-3-opus",
    "google/gemini-2.5-flash-lite"
]
```

### Workflow 4: Use different grading strategies

Choose which prompt template to use:

```python
from prompts import direct_prompt, reverse_prompt, eme_prompt

# Direct grading
prompt = direct_prompt.build_direct_prompt(question, rubric, code)

# Or reverse grading
prompt = reverse_prompt.build_reverse_prompt(question, rubric, code)
```

## Next Steps

- For a detailed view of the directory tree and where to plug in new components, see `02-PROJECT-STRUCTURE.md`.
- For step-by-step usage (including the batch CLI and more advanced patterns), see `03-USAGE-GUIDE.md`.
- For system design and extension guidance, see `05-ARCHITECTURE.md`.

## Quick Reference

| Term | Meaning |
|------|---------|
| **Model** | An AI system (ChatGPT, Claude, etc.) |
| **Evaluation** | Grade for one student from one or more models |
| **Rubric** | The grading scale with categories |
| **Misconception** | A mistake or misunderstanding by the student |
| **Confidence** | How sure the AI is (0-1 scale) |
| **JSON** | Serialization format for evaluation artefacts |
| **Pydantic** | Validation and serialization layer for all models |
