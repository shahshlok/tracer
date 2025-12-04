# Dataset Format

This document describes the data formats used throughout the pipeline.

## Ground Truth (Misconception Taxonomy)

**File:** `data/a2/groundtruth.json`

An array of misconceptions that can be detected:

```json
[
  {
    "id": "INP-01",
    "misconception_name": "Prompt-Logic Mismatch",
    "description": "Student's prompt asks for one value but logic expects another",
    "category": "Input",
    "sub_category": "Input Handling",
    "symptoms": ["Prompt text doesn't match variable usage"],
    "common_triggers": ["Copy-paste errors", "Misreading requirements"]
  },
  {
    "id": "TYP-01",
    "misconception_name": "Integer Division Trap",
    "description": "Student performs integer division when floating-point is needed",
    "category": "Data Types",
    "sub_category": "Type Selection"
  }
]
```

**Fields:**

| Field | Required | Description |
|-------|----------|-------------|
| `id` | Yes | Unique identifier (e.g., `INP-01`, `TYP-01`) |
| `misconception_name` | Yes | Human-readable name |
| `description` | Yes | Detailed description |
| `category` | Yes | Topic category (Input, Data Types, State, etc.) |
| `sub_category` | No | More specific category |
| `symptoms` | No | Observable signs |
| `common_triggers` | No | What causes this misconception |

## Manifest (Dataset Configuration)

**File:** `authentic_seeded/manifest.json`

Describes the generated dataset:

```json
{
  "generated_at": "2025-12-04T07:55:51.615649",
  "seed": 1764863751,
  "student_count": 60,
  "model": "gpt-5.1-2025-11-13",
  "questions": {
    "Q1": "Acceleration: compute (v1 - v0) / t using user input.",
    "Q2": "Road trip cost: (distance / mpg) * price using user input.",
    "Q3": "Distance between two points using sqrt formula.",
    "Q4": "Triangle area with Heron's formula."
  },
  "students": [
    {
      "folder_name": "Davis_Rodney_403107",
      "first_name": "Rodney",
      "last_name": "Davis",
      "student_id": 403107,
      "persona": "Extra blank lines, descriptive camelCase variables",
      "assigned_misconceptions": [],
      "files": {
        "Q1": {"type": "CLEAN", "misconception_id": null},
        "Q2": {"type": "SEEDED", "misconception_id": "TYP-01"},
        "Q3": {"type": "CLEAN", "misconception_id": null},
        "Q4": {"type": "CLEAN", "misconception_id": null}
      }
    }
  ]
}
```

**Top-level fields:**

| Field | Description |
|-------|-------------|
| `generated_at` | ISO timestamp of generation |
| `seed` | Random seed for reproducibility |
| `student_count` | Number of students |
| `model` | LLM used for code generation |
| `questions` | Question descriptions |
| `students` | Array of student configurations |

**Student fields:**

| Field | Description |
|-------|-------------|
| `folder_name` | Directory name for student's files |
| `persona` | Code style description |
| `files` | Map of question → file info |
| `files.<Q>.type` | `CLEAN` or `SEEDED` |
| `files.<Q>.misconception_id` | ID of injected misconception (or null) |

## Detection Files

**Location:** `detections/<strategy>/<student>_<question>.json`

LLM detection output:

```json
{
  "student": "Davis_Rodney_403107",
  "question": "Q2",
  "model": "gpt-5.1",
  "strategy": "baseline",
  "timestamp": "2025-12-04T08:00:00Z",
  "detections": [
    {
      "name": "Integer Division Trap",
      "description": "The student used integer division for the cost calculation",
      "student_belief": "Division always produces decimal results",
      "correct_understanding": "Integer division truncates decimals",
      "symptoms": ["int result = distance / mpg"],
      "root_cause": "Confusion about Java type system",
      "remediation_hint": "Use double for division",
      "evidence": "Line 5: int gallons = distance / mpg;",
      "confidence": 0.85,
      "topic": "Data Types"
    }
  ]
}
```

**Detection fields:**

| Field | Description |
|-------|-------------|
| `name` | Misconception name (free-form) |
| `description` | Detailed explanation |
| `student_belief` | What the student thinks |
| `correct_understanding` | What is actually correct |
| `symptoms` | Observable signs in code |
| `evidence` | Specific code reference |
| `confidence` | Model's confidence (0-1) |
| `topic` | Detected topic category |

## Metrics Output

**File:** `thesis_report.json`

```json
{
  "metrics": [
    {
      "match_mode": "hybrid",
      "strategy": "baseline",
      "model": "gpt-5.1",
      "tp": 34,
      "fp": 20,
      "fn": 22,
      "precision": 0.63,
      "recall": 0.607,
      "f1": 0.618
    }
  ],
  "ci": [
    {
      "match_mode": "hybrid",
      "strategy": "baseline",
      "model": "gpt-5.1",
      "precision_lo": 0.52,
      "precision_hi": 0.73,
      "recall_lo": 0.50,
      "recall_hi": 0.71,
      "f1_lo": 0.50,
      "f1_hi": 0.73
    }
  ],
  "opportunities": [
    {
      "student": "Davis_Rodney_403107",
      "question": "Q2",
      "expected_id": "TYP-01",
      "success": true,
      "topic": "Data Types"
    }
  ]
}
```

## File Locations Summary

| Data | Location |
|------|----------|
| Ground truth | `data/a2/groundtruth.json` |
| Manifest | `authentic_seeded/manifest.json` |
| Student code | `authentic_seeded/<student>/Q*.java` |
| Detections | `detections/<strategy>/<student>_<question>.json` |
| Report | `thesis_report.md` |
| Metrics JSON | `thesis_report.json` |
| Plots | `docs/report_assets/*.png` |
| Saved runs | `runs/<run_id>/` |
