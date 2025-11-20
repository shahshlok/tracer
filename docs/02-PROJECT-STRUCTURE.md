# Project Structure Guide

This guide explains where everything is located in the project and what each folder does.

## Directory Tree

```
ensemble-eval-cli/
│
├─ @docs/                          ← Documentation (you are here!)
│  ├─ 00-QUICK-START.md           ← Get running in 5 minutes
│  ├─ 01-GETTING-STARTED.md       ← Understand the basics
│  ├─ 02-PROJECT-STRUCTURE.md     ← This file
│  ├─ 03-USAGE-GUIDE.md           ← How to use the system
│  ├─ 04-API-REFERENCE.md         ← Technical API details
│  ├─ 05-ARCHITECTURE.md          ← Deep technical dive
│  └─ INDEX.md                    ← Documentation index
│
├─ pydantic_models/                ← Data definitions
│  ├─ __init__.py
│  ├─ evaluation.py                ← Root EvaluationDocument model
│  ├─ README.md                    ← Model documentation
│  │
│  ├─ context/                     ← Course/assignment metadata
│  │  ├─ __init__.py
│  │  └─ models.py                 ← Context, AssignmentInfo
│  │
│  ├─ submission/                  ← Student submission data
│  │  ├─ __init__.py
│  │  └─ models.py                 ← Submission, StudentFile
│  │
│  ├─ rubric/                      ← Grading criteria
│  │  ├─ __init__.py
│  │  └─ models.py                 ← Rubric, RubricCategory
│  │
│  ├─ models/                      ← Per-model evaluation
│  │  ├─ __init__.py
│  │  └─ evaluation.py             ← ModelEvaluation, Scores, Feedback
│  │
│  └─ comparison/                  ← Multi-model analysis (schema only)
│     ├─ __init__.py
│     ├─ models.py                 ← Main Comparison model
│     ├─ score_analysis.py         ← Score statistics
│     ├─ misconception_analysis.py ← Misconception patterns
│     ├─ confidence_analysis.py    ← Confidence patterns
│     ├─ reliability.py            ← ICC, correlations
│     └─ ensemble.py               ← Ensemble decisions
│
├─ prompts/                        ← Grading instructions
│  ├─ __init__.py
│  ├─ direct_prompt.py             ← Direct grading strategy
│  ├─ reverse_prompt.py            ← Reverse grading strategy
│  └─ eme_prompt.py                ← Ensemble method strategy
│
├─ utils/                          ← AI service integrations
│  ├─ openai_client.py             ← OpenAI API wrapper
│  └─ openrouter_sdk.py            ← OpenRouter API wrapper
│
├─ tests/                          ← Test suite
│  ├─ __init__.py
│  └─ simple_openai_test.py        ← Example test
│
├─ student_submissions/            ← INPUT: Student code
│  └─ Diaz_Sergio_100029/          ← One folder per student (by ID)
│     └─ Cuboid.java               ← Student's code file
│
├─ student_evals/                  ← OUTPUT: Evaluation results
│  └─ sergio_eval.json             ← Results (one JSON per evaluation)
│
├─ grade_sergio.py                 ← Example: Complete workflow
├─ question_cuboid.md              ← Example: Assignment specification
├─ rubric_cuboid.json              ← Example: Grading rubric
│
├─ pyproject.toml                  ← Project configuration
├─ .env                            ← API keys (not in repository)
├─ .gitignore                      ← Files to ignore in version control
├─ README.md                       ← Main project README
└─ uv.lock                         ← Dependency lock file
```

## Understanding Each Directory

### `@docs/` - Documentation

**Purpose:** All user and developer documentation

**What's inside:**
- `00-QUICK-START.md` - 5-minute getting started
- `01-GETTING-STARTED.md` - Basic concepts explained
- `02-PROJECT-STRUCTURE.md` - This file
- `03-USAGE-GUIDE.md` - Practical how-to guide
- `04-API-REFERENCE.md` - Technical reference
- `05-ARCHITECTURE.md` - System design deep dive
- `INDEX.md` - Navigation guide

**When to use:** Start with `00-QUICK-START.md`, then pick the topic you need.

---

### `pydantic_models/` - Data Definitions

**Purpose:** Defines the structure of all data in the system

**Key concepts:** Pydantic is a Python library that validates data. It ensures:
- All scores are numbers (not text)
- Percentages are between 0-100
- Required fields are present
- Data types are correct

**What's inside:**

#### `evaluation.py` - Root Model
The main container that holds everything:
```
EvaluationDocument
├─ evaluation_id
├─ schema_version
├─ created_at
├─ created_by
├─ context (metadata)
├─ submission (student info)
├─ rubric (grading scale)
└─ models (results from each AI)
```

#### `context/models.py` - Educational Metadata
```
Context
├─ course_id
├─ course_name
├─ assignment_id
├─ assignment_title
├─ question_id
└─ question_title
```

#### `submission/models.py` - Student Information
```
Submission
├─ student_id
├─ student_name
├─ submitted_at
├─ programming_language
└─ files (list of StudentFile)
   ├─ path
   └─ language
```

#### `rubric/models.py` - Grading Scale
```
Rubric
├─ rubric_id
├─ title
├─ total_points
└─ categories (list of RubricCategory)
   ├─ category_id
   ├─ name
   ├─ max_points
   └─ description
```

#### `models/evaluation.py` - AI Evaluation Results
```
ModelEvaluation (one per model)
├─ model_name
├─ provider
├─ run_id
├─ config
├─ scores
├─ category_scores
├─ feedback
└─ misconceptions
```

#### `comparison/` - Multi-Model Analysis
Models for comparing results across models (not yet computed):
- `score_analysis.py` - Statistics and comparisons
- `misconception_analysis.py` - Shared misconceptions
- `reliability.py` - Agreement metrics
- `ensemble.py` - Combined recommendations

**When you use it:**
- Usually: Import from here in your scripts
- Rarely: Modify unless changing data structure

---

### `prompts/` - Grading Instructions

**Purpose:** Templates for asking AI to grade code

**How it works:**
1. You write the student code, question, and rubric
2. A prompt function builds the instruction message
3. That message is sent to the AI model

**What's inside:**

#### `direct_prompt.py`
**Strategy:** Grade directly against the rubric
```
"Here is the assignment and rubric.
Here is the student's code.
Grade it."
```
**Best for:** Quick, straightforward evaluations

#### `reverse_prompt.py`
**Strategy:** Generate ideal code first, then compare
```
"Here is the assignment.
Generate ideal code that solves it.
Now compare the student's code to your ideal."
```
**Best for:** Deeper analysis, identifying deviations

#### `eme_prompt.py`
**Strategy:** Ensemble Method Evaluation
```
"Grade on 100-point scale with detailed breakdown."
```
**Best for:** Research and detailed analysis

**When you use it:**
- Import and call in your evaluation script
- Or create a new one if you want a different grading style

---

### `utils/` - AI Service Integration

**Purpose:** Code that talks to AI companies' APIs

**How it works:**
1. Takes your data (code, question, rubric)
2. Sends it to the AI service
3. Gets the response back
4. Validates and returns it

**What's inside:**

#### `openai_client.py`
**What it does:** Talks to OpenAI (GPT-4, GPT-3.5, etc.)

**Function:** `evaluation_with_openai()`
```python
from utils.openai_client import evaluation_with_openai

result = evaluation_with_openai(
    question="...",
    rubric="...",
    student_code="...",
    model="gpt-4o"
)
```

#### `openrouter_sdk.py`
**What it does:** Talks to OpenRouter (routes to many models)

**Function:** `get_structured_response()`
```python
from utils.openrouter_sdk import get_structured_response
from pydantic_models import LLMEvaluationResponse

result = get_structured_response(
    messages=[{"role": "user", "content": "..."}],
    response_model=LLMEvaluationResponse,
    model="google/gemini-2.5-flash-lite"
)
```

**Supported models via OpenRouter:**
- Google Gemini
- Anthropic Claude
- OpenAI GPT
- Moonshot AI Kimi
- Meta Llama
- And more...

**When you use it:**
- Import in your evaluation scripts
- Or create a new one if you want to add another AI provider

---

### `tests/` - Automated Testing

**Purpose:** Code that verifies the system works

**What's inside:**
- `simple_openai_test.py` - Tests OpenAI integration

**Current status:** Minimal tests (5-10% coverage)

**When you use it:**
```bash
uv run pytest              # Run all tests
uv run pytest -v           # Run with verbose output
uv run pytest tests/       # Run specific test file
```

---

### `student_submissions/` - Input Data

**Purpose:** Where student code goes

**Structure:**
```
student_submissions/
└─ {student_id}/           ← One folder per student
   ├─ Code1.java           ← Student's files
   ├─ Code2.java           ← (one or more)
   └─ ...
```

**Example:**
```
student_submissions/
└─ Diaz_Sergio_100029/
   └─ Cuboid.java
```

**How to add a student:**
1. Create a folder: `student_submissions/FirstName_LastName_ID/`
2. Put their code files in it
3. Reference it in your evaluation script

---

### `student_evals/` - Output Data

**Purpose:** Where evaluation results are saved

**What's inside:**
- JSON files with evaluation results
- One file per evaluation
- Human-readable format

**Example output:**
```
student_evals/
├─ sergio_eval.json        ← Results for Sergio
├─ alice_eval.json         ← Results for Alice
└─ ...
```

**File structure:**
```json
{
  "evaluation_id": "...",
  "schema_version": "1.0.0",
  "context": {...},
  "submission": {...},
  "rubric": {...},
  "models": {
    "gpt-4o": {...},
    "claude-3-opus": {...}
  }
}
```

---

### Root Level Files

#### `grade_sergio.py`
**What it is:** Complete example evaluation script

**What it does:**
1. Loads the Cuboid assignment question
2. Loads Cuboid grading rubric
3. Loads Sergio's Java code
4. Evaluates with multiple models
5. Saves results to JSON

**How to use it:**
- Run directly: `uv run python grade_sergio.py`
- Copy and modify for your own evaluations
- Use as a template for your scripts

#### `question_cuboid.md`
**What it is:** Example assignment specification

**Format:** Markdown (plain text with formatting)

**Content:** Complete assignment description (could be homework, project, exam question)

#### `rubric_cuboid.json`
**What it is:** Example grading rubric

**Format:** JSON (structured text data)

**Content:** Grading scale with categories and point values

#### `pyproject.toml`
**What it is:** Project configuration file

**Contains:**
- Project name and version
- Dependencies needed
- Python version requirements
- Build instructions

**You usually don't edit this.**

#### `.env`
**What it is:** Secret configuration (API keys)

**Important:** This file is NOT in version control (see `.gitignore`)

**Content:**
```env
OPENAI_API_KEY=sk-...
OPENROUTER_API_KEY=sk-or-...
```

**⚠️ NEVER commit this file!**

#### `.gitignore`
**What it is:** List of files Git should ignore

**Includes:**
- `.env` (API keys)
- `__pycache__/` (compiled Python)
- `.venv/` (virtual environment)

#### `README.md`
**What it is:** Main project documentation

**Content:** Overview, features, installation instructions

---

## How It All Fits Together

Here's a visual flow:

```
┌──────────────────────────────────┐
│   You (the user)                 │
└──────────────────────────────────┘
           │
           │ Creates evaluation script (copy grade_sergio.py)
           ▼
┌──────────────────────────────────┐
│   Your evaluation script          │
│   (e.g., my_evaluation.py)        │
└──────────────────────────────────┘
           │
           │ Uses these imports:
           ├─→ pydantic_models/     (Data structures)
           ├─→ prompts/             (How to ask AI)
           ├─→ utils/               (Talk to AI services)
           │
           │ Reads from:
           ├─→ student_submissions/ (Student code)
           ├─→ question_*.md        (Assignment)
           └─→ rubric_*.json        (Grading scale)
           │
           ▼
┌──────────────────────────────────┐
│   AI Services                    │
│   (OpenAI, OpenRouter, etc.)     │
│   Returns grades and feedback    │
└──────────────────────────────────┘
           │
           │ Your script writes results
           ▼
┌──────────────────────────────────┐
│   student_evals/                 │
│   *.json files (results)          │
└──────────────────────────────────┘
```

---

## Navigation Map

**Starting out?**
1. Read this file ← You are here
2. Next: `03-USAGE-GUIDE.md` (how to actually use it)

**Need specific information?**
- How to add students: `03-USAGE-GUIDE.md`
- How to change models: `03-USAGE-GUIDE.md`
- Understanding evaluation scores: `01-GETTING-STARTED.md`
- Complete API details: `04-API-REFERENCE.md`
- System architecture: `05-ARCHITECTURE.md`

---

**Next:** Go to [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) to learn how to use the system.
