# CLI Reference

This document provides a complete reference for the EME CLI commands, options, and usage patterns.

## Table of Contents

- [Installation](#installation)
- [Commands](#commands)
- [Interactive Mode](#interactive-mode)
- [Output Format](#output-format)
- [Examples](#examples)

## Installation

```bash
# Clone repository
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli

# Install dependencies with uv
uv sync

# Configure environment
echo "OPENROUTER_API_KEY=sk-or-..." > .env
```

## Commands

### Interactive Mode (Default)

```bash
uv run python cli.py
```

Launches an interactive menu:

```
┌──────────────────────────────────────────────────────────────┐
│                 ENSEMBLE EVALUATION BENCHMARK                  │
│            Async Batch Protocol v3.0 (Parallel)                │
└──────────────────────────────────────────────────────────────┘

What would you like to do?

  [1] Grade Students (Process up to 25 students)
  [2] Analyze Misconceptions (Analyze existing evaluations)
  [3] Exit
```

### grade

Grade all students in the `student_submissions/` directory.

```bash
uv run python cli.py grade
```

**What it does:**
1. Discovers student directories in `student_submissions/`
2. For each student, loads Q1-Q4 submissions
3. Grades each submission against all configured models in parallel
4. Saves results to `student_evals/{student_id}_{q_id}_eval.json`
5. Displays summary table with scores and disagreement flags

**Output:**
```
Found 25 submissions. Processing 25...
Concurrency Limit: 5 students at a time

🌍 Batch Progress ████████████████████████████████████████ 100% 0:02:34

────────────────────── Ensembling Method Evaluation ───────────────────────

Student          Q  GemFlash  GPT5.1  GemLite  GPT5N   Avg  Range  Conf  Flag
Chen_Wei_200023  q1    -        -       3.0     3.5    3.3   0.5    92%   OK
Chen_Wei_200023  q2    -        -       4.0     4.0    4.0   0.0    95%   OK
...

Processed 100 students successfully. Logs in student_evals/
```

### analyze

Analyze misconceptions from existing evaluation files.

```bash
uv run python cli.py analyze
```

**What it does:**
1. Loads all `*_eval.json` files from `student_evals/`
2. Extracts and normalizes misconceptions
3. Performs fuzzy clustering to merge similar names
4. Calculates class-wide statistics
5. Displays analysis tables
6. Optionally generates `misconception_report.md`

**Output:**
```
─────────────────────── Misconception Analysis ────────────────────────

╭──────────────────────────────────────────────────────────────────────╮
│                        Analysis Summary                               │
├──────────────────────────────────────────────────────────────────────┤
│ Total Students: 25                                                    │
│ Total Misconceptions: 142                                             │
│ Unique Topic+Task Areas: 18                                          │
│ Unique Misconception Types: 34                                        │
╰──────────────────────────────────────────────────────────────────────╯

──────────────── Most Difficult Areas by Topic + Task ─────────────────

Rank  Topic       Task                               Students  %     Avg Conf  Agreement
1     Variables   Computing cost vs. distance...    15/25     60%   0.85      75%
2     Data Types  Declaring variables (double)      12/25     48%   0.82      100%
...

──────────────────── Most Common Misconceptions ───────────────────────

Rank  Misconception                        Topic       Count  Students  Models
1     Incorrect formula application        Variables   28     12/25     4/4
2     Missing import statement             Constants   15     8/25      3/4
...

Save detailed report to markdown? [y/n]: y
Report saved to misconception_report.md
```

## Interactive Mode

The default interactive mode provides a Rich-styled terminal interface:

### Menu Navigation

| Key | Action |
|-----|--------|
| `1` | Run grading workflow |
| `2` | Run misconception analysis |
| `3` | Exit |

### Progress Display

During grading, a real-time progress bar shows:
- Current student being processed
- Overall batch progress percentage
- Elapsed time

### Results Table

After grading, a summary table displays:

| Column | Description |
|--------|-------------|
| Student | Student ID |
| Q | Question number (q1-q4) |
| GemFlash | Score from Gemini Flash |
| GPT5.1 | Score from GPT-5.1 |
| GemLite | Score from Gemini Flash Lite |
| GPT5N | Score from GPT-5 Nano |
| Avg | Average score across models |
| Range | Score range (max - min) |
| Conf | Average confidence percentage |
| Flag | `OK` if range ≤ 1.5, `!!` if significant disagreement |

## Output Format

### Evaluation JSON

Each evaluation is saved as `{student_id}_{question_id}_eval.json`:

```json
{
  "evaluation_id": "eval_Chen_Wei_200023_q1_20250115",
  "schema_version": "1.0.0",
  "created_at": "2025-01-15T10:30:00Z",
  "created_by": "cli.py",
  "context": {
    "course_id": "COSC121",
    "course_name": "Introduction to Programming",
    "assignment_id": 2,
    "assignment_title": "Assignment 2",
    "question_id": "q1",
    "question_title": "Question 1",
    "question_source_path": "data/a2/q1.md",
    "rubric_source_path": "data/a2/rubric_q1.md"
  },
  "submission": {
    "student_id": "Chen_Wei_200023",
    "student_name": "Chen Wei",
    "submitted_at": "2025-01-15T10:30:00Z",
    "programming_language": "java",
    "files": [{"path": "Q1.java", "language": "java"}]
  },
  "rubric": {
    "rubric_id": "a2_q1_v1",
    "title": "Q1 Rubric",
    "total_points": 4.0,
    "categories": [...]
  },
  "models": {
    "google/gemini-2.5-flash-lite": {
      "model_name": "gemini-2.5-flash-lite",
      "provider": "google",
      "scores": {"total_points_awarded": 3.0, "max_points": 4.0, "percentage": 75.0},
      "category_scores": [...],
      "feedback": {...},
      "misconceptions": [...]
    },
    "openai/gpt-5-nano": {...}
  }
}
```

### Misconception Report

The `misconception_report.md` contains:

1. **Executive Summary** - Total students, misconceptions, unique types
2. **Most Difficult Areas** - Topics ranked by % of class affected
3. **Most Common Misconceptions** - Specific issues ranked by occurrence
4. **Per-Question Analysis** - Breakdown by Q1-Q4
5. **Progression Analysis** - Q3→Q4 improvement/persistence tracking
6. **Model Agreement** - How many misconceptions each model detected
7. **Legend** - Formulas and metric explanations

## Examples

### Grade a Subset of Students

Modify `BATCH_LIMIT` in `cli.py`:

```python
BATCH_LIMIT = 10  # Only process 10 students
```

### Use Different Models

Modify `MODELS` in `cli.py`:

```python
MODELS = [
    "anthropic/claude-3-sonnet",
    "google/gemini-2.0-flash",
    "openai/gpt-4o",
]
```

### Reduce Concurrency for Rate Limits

```python
MAX_CONCURRENT_STUDENTS = 2  # Down from 5
```

### Run Single Student (Sandbox)

For testing, use the sandbox script:

```bash
uv run python sandbox/single_submission.py
```

This processes a single student with detailed output, saving to `sandbox/evals/`.

### Programmatic Usage

```python
import asyncio
from cli import batch_grade_students, get_student_list

async def main():
    students = get_student_list()[:5]  # First 5 students
    results = await batch_grade_students(students)
    
    for result in results:
        if result["status"] == "success":
            print(f"{result['student']} {result['question']}: OK")
        else:
            print(f"{result['student']} {result['question']}: {result['error']}")

asyncio.run(main())
```

## Exit Codes

| Code | Meaning |
|------|---------|
| 0 | Success |
| 1 | Error (missing files, API errors, etc.) |

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `OPENROUTER_API_KEY` | Yes | OpenRouter API key |
| `OPENAI_API_KEY` | No | Direct OpenAI API key (if not using OpenRouter) |

## File Structure Requirements

```
ensemble-eval-cli/
├── student_submissions/     # Required for grading
│   ├── {student_id}/
│   │   ├── Q1.java
│   │   ├── Q2.java
│   │   ├── Q3.java
│   │   └── Q4.java
├── data/a2/                 # Required for grading
│   ├── q1.md, q2.md, q3.md, q4.md
│   └── rubric_q1.md, rubric_q2.md, rubric_q3.md, rubric_q4.md
├── student_evals/           # Required for analysis (created by grading)
│   └── *_eval.json
└── .env                     # Required: API keys
```

## Related Documentation

- [Configuration](configuration.md) - Detailed setup options
- [Grading Workflow](grading-workflow.md) - How grading works
- [Architecture](architecture.md) - System design
