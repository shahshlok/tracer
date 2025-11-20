# Getting Started: The Basics

This guide explains what the EME Framework does and the key concepts you need to understand.

## The Big Picture

The Ensemble Model Evaluation Framework helps educators grade student code using AI. Here's how it works:

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

A **model** is an AI system that grades code. The framework supports:
- **OpenAI models**: GPT-4, GPT-3.5-turbo
- **Anthropic Claude**: Claude 3 Opus, Sonnet
- **Google Gemini**: Multiple versions
- **And many more via OpenRouter**

You can evaluate the same code with multiple models and compare their results.

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

A **misconception** is when a student shows a misunderstanding of a concept. The framework identifies these automatically by analyzing the code.

Example misconception:
```
Description: Student uses mutable default arguments in method signature
Confidence: 0.85
Evidence: Line 12 - "public Cuboid(int[] dimensions = new int[3])"
Explanation: Default arguments are evaluated once at class definition time.
```

### 5. Confidence

**Confidence** is how sure the AI model is about its evaluation. It ranges from 0 to 1:
- 0.9+ = Very sure
- 0.7-0.9 = Fairly sure
- 0.5-0.7 = Somewhat sure
- Below 0.5 = Uncertain

High confidence doesn't always mean the evaluation is correct—it's the model's opinion about its own certainty.

## The Evaluation Process

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

The system has four main parts:

### 1. Pydantic Models (`pydantic_models/`)

**What it is:** Strict definitions of data structures

**Why it matters:** Ensures all data is valid and consistent. No surprises!

**Example:** The `Scores` model says "percentage must be 0-100". If something tries to set percentage to 150, it gets rejected immediately.

### 2. LLM Utilities (`utils/`)

**What it is:** Code that talks to AI services (OpenAI, OpenRouter, etc.)

**Why it matters:** Handles all the complex API requests and responses

**Two main functions:**
- `utils/openai_client.py` - For OpenAI models only
- `utils/openrouter_sdk.py` - For many models via OpenRouter

### 3. Prompt Templates (`prompts/`)

**What it is:** Instructions sent to the AI model

**Why it matters:** Different prompt styles get different results

**Three strategies:**
- **Direct**: "Grade this code against the rubric"
- **Reverse**: "First write ideal code, then compare to this submission"
- **EME**: "Use ensemble method to evaluate systematically"

### 4. Example Script (`grade_sergio.py`)

**What it is:** A complete example showing how to use the system

**Why it matters:** You can copy and modify this for your own evaluations

## Working with Files

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

To grade your own student:

1. Create a folder: `student_submissions/{student_id}/`
2. Put their code files in that folder
3. Create an evaluation script (copy from `grade_sergio.py`)
4. Change the student ID and run it

## Output Format: Understanding JSON Results

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

### Workflow 1: Grade One Student

```bash
# 1. Prepare student code in student_submissions/{id}/
# 2. Create/modify grade_sergio.py with your details
# 3. Run it
uv run python grade_sergio.py

# 4. Results in student_evals/
```

### Workflow 2: Grade Multiple Students

Create a script that loops through student IDs:

```python
for student_id in ["Student_A", "Student_B", "Student_C"]:
    # Load their code
    # Create evaluation
    # Save results
```

### Workflow 3: Try Different Models

Change the models list in your script:

```python
models_to_test = [
    "gpt-4o",
    "anthropic/claude-3-opus",
    "google/gemini-2.5-flash-lite"
]
```

### Workflow 4: Use Different Grading Strategies

Choose which prompt template to use:

```python
from prompts import direct_prompt, reverse_prompt, eme_prompt

# Direct grading
prompt = direct_prompt.build_direct_prompt(question, rubric, code)

# Or reverse grading
prompt = reverse_prompt.build_reverse_prompt(question, rubric, code)
```

## What's Next?

Now that you understand the basics:

1. **Quick test**: Run `uv run python grade_sergio.py` to see it in action
2. **Learn structure**: Read [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
3. **Practical guide**: Read [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) for step-by-step instructions
4. **Deep dive**: Read [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) for technical details

## Quick Reference

| Term | Meaning |
|------|---------|
| **Model** | An AI system (ChatGPT, Claude, etc.) |
| **Evaluation** | Grade for one student from one or more models |
| **Rubric** | The grading scale with categories |
| **Misconception** | A mistake or misunderstanding by the student |
| **Confidence** | How sure the AI is (0-1 scale) |
| **JSON** | Text format storing the results |
| **Pydantic** | System ensuring data is always valid |

---

**Next:** Read [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) to understand where everything is located.
