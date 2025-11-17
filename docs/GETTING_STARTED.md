# 🚀 Getting Started with EME Framework

**Version 2.0.0** - Complete beginner's guide to installation and first evaluation

> **Prerequisites**: Python 3.10+, basic command line knowledge

---

## 📑 Table of Contents

1. [Installation](#installation)
2. [Project Setup](#project-setup)
3. [Your First Evaluation](#your-first-evaluation)
4. [Understanding the Output](#understanding-the-output)
5. [Next Steps](#next-steps)

---

## Installation

### Step 1: Clone the Repository

```bash
cd ~/Projects  # or wherever you keep code
git clone <repo-url>
cd EME_testing
```

### Step 2: Install Dependencies

We use `uv` for dependency management:

```bash
# Install uv if you don't have it
pip install uv

# Install project dependencies
uv sync
```

This reads `pyproject.toml` and installs everything you need.

### Step 3: Verify Installation

```bash
# Check that dependencies are installed
uv run python -c "import openai; import typer; print('✓ Dependencies OK')"
```

You should see: `✓ Dependencies OK`

---

## Project Setup

### Configure Environment Variables

Create a `.env` file in the project root:

```bash
touch .env
```

Add your configuration:

```env
# API Keys (get these from your providers)
OPENAI_API_KEY=sk-your-openai-key-here
EDUAI_API_KEY=sk-your-eduai-key-here
EDUAI_ENDPOINT=https://eduai.ok.ubc.ca/api/chat
EDUAI_MODEL=ollama:gpt-oss:120b

# Optional: OpenRouter for additional models
OPENROUTER_API_KEY=sk-or-your-key-here
OPENROUTER_MODEL=google/gemini-2.0-flash-exp

# Question and Rubric (can also use files - see below)
QUESTION="Write a Java program that computes the sum of integers from 1 to 100."

RUBRIC='{
  "totalPoints": 100,
  "categories": [
    {"name": "Correctness", "points": 40, "description": "Program produces correct output"},
    {"name": "Compilation", "points": 20, "description": "Code compiles without errors"},
    {"name": "Style", "points": 20, "description": "Code follows conventions"},
    {"name": "Documentation", "points": 20, "description": "Adequate comments"}
  ]
}'
```

> **Tip**: Use files instead of env vars for complex questions/rubrics. See [Using Question Files](#using-question-files) below.

### Verify Configuration

```bash
# Check if .env is loaded correctly
uv run python -c "from dotenv import load_dotenv; import os; load_dotenv(); print('OPENAI_API_KEY:', 'SET' if os.getenv('OPENAI_API_KEY') else 'MISSING')"
```

---

## Your First Evaluation

### Prepare Student Submissions

Create the `student_submissions/` directory structure:

```bash
mkdir -p student_submissions
```

Add student code:

```
student_submissions/
├── Smith_John_123456/
│   └── Main.java
├── Doe_Jane_654321/
│   └── Solution.java
└── Lee_Alice_789012/
    └── Program.java
```

**Example submission** (`Smith_John_123456/Main.java`):

```java
public class Main {
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("Sum: " + sum);
    }
}
```

### Run Your First Evaluation

#### Option 1: Interactive CLI (Recommended)

```bash
uv run bench benchmark
```

You'll see a menu:

```
┌─────────────────────────────────────────────────────┐
│          EME Framework v2.0                         │
│    Ensemble Model Evaluation for Code Grading      │
└─────────────────────────────────────────────────────┘

Select a grading strategy:

[1] Direct Grading        Grade code directly against rubric
[2] Reverse Grading       Generate ideal solution first
[3] Ensemble (EME)        Multi-model ensemble approach
[4] Run All               Compare all strategies
[5] Analysis              Restore and analyze results

Enter your choice [1-5]:
```

**Choose option 3** (Ensemble - EME) for the full v2 experience.

The CLI will:
1. Ask you to select a question file (or use `.env`)
2. Ask you to select a rubric file (or use `.env`)
3. Discover student submissions automatically
4. Grade each submission with multiple models
5. Show progress bars
6. Display results table

#### Option 2: Single Submission Test

To quickly test one student:

```bash
python single_submission.py student_submissions/Smith_John_123456/Main.java
```

Output:

```
Evaluating: Smith_John_123456/Main.java
Progress: ████████████████████ 100%

=== Grade Summary ===
Student: Smith_John_123456
GPT-5:   85.0/100.0 (85.0%)
EduAI:   82.0/100.0 (82.0%)
Avg %:   83.5%
Diff %:  3.0%
Flag:    ✅
```

---

## Understanding the Output

### Terminal Output

After grading, you'll see a table:

```
┌────────────────────┬─────────┬─────────┬─────────┬─────────┬──────┐
│ Student            │ GPT-5 % │ EduAI % │ Avg %   │ Diff %  │ Flag │
├────────────────────┼─────────┼─────────┼─────────┼─────────┼──────┤
│ Smith_John_123456  │ 85.0    │ 82.0    │ 83.5    │ 3.0     │ ✅   │
│ Doe_Jane_654321    │ 90.0    │ 88.0    │ 89.0    │ 2.0     │ ✅   │
│ Lee_Alice_789012   │ 75.0    │ 65.0    │ 70.0    │ 10.0    │ 🚩   │
└────────────────────┴─────────┴─────────┴─────────┴─────────┴──────┘

✅ = Models agree (difference < 10%)
🚩 = Models disagree significantly
```

### JSON Output

Results are saved to `data/results_{strategy}_{timestamp}.json`:

```json
[
  {
    "evaluation_id": "eval_sum_s123_q1_001",
    "context": {
      "course_id": "cosc111",
      "assignment_id": 1,
      "question_id": "q1_sum_program"
    },
    "submission": {
      "student_id": "Smith_John_123456",
      "files": [...]
    },
    "models": {
      "gpt-5-nano": {
        "scores": {...},
        "category_scores": [...],
        "feedback": {...},
        "misconceptions": [...]
      },
      "gpt-oss-120b": {...}
    },
    "comparison": {
      "score_summary": {...},
      "reliability_metrics": {...},
      "ensemble_decision": {...}
    }
  }
]
```

See [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) or [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md) for complete field reference.

### Database Storage

Results are automatically stored in `evaluations.db`:

- **`evaluations` table**: Full JSON for each evaluation
- **`misconceptions` table**: Flattened misconceptions for querying

View the database:

```bash
sqlite3 evaluations.db
```

```sql
-- Count total evaluations
SELECT COUNT(*) FROM evaluations;

-- See all students
SELECT DISTINCT student_id FROM evaluations;

-- Find common misconceptions
SELECT misconception_name, COUNT(*) as count
FROM misconceptions
GROUP BY misconception_name
ORDER BY count DESC
LIMIT 10;
```

---

## Using Question Files

Instead of putting questions in `.env`, use markdown files:

**Create `question_sum.md`:**

```markdown
# Sum Program

Write a Java program that computes the sum of integers from 1 to 100.

## Requirements
- Use a loop (for or while)
- Print the result to console
- Handle the calculation correctly

## Sample Output
```
Sum: 5050
```
```

**Create `rubric_sum.json`:**

```json
{
  "totalPoints": 100,
  "categories": [
    {
      "name": "Correctness",
      "points": 40,
      "description": "Program produces correct output (5050)"
    },
    {
      "name": "Logic",
      "points": 25,
      "description": "Proper use of loop and variables"
    },
    {
      "name": "Compilation",
      "points": 20,
      "description": "Code compiles without errors"
    },
    {
      "name": "Style",
      "points": 15,
      "description": "Clear variable names, proper formatting"
    }
  ]
}
```

Place these files in the project root. The CLI will discover them automatically!

---

## Common Commands

```bash
# Run interactive benchmark
uv run bench benchmark

# Grade one student quickly
python single_submission.py path/to/Student.java

# Restore JSON files from database
uv run bench benchmark
# → Select [5] Analysis → [1] Restore JSON

# View database
sqlite3 evaluations.db
sqlite> SELECT * FROM evaluations LIMIT 1;
```

---

## Understanding the Workflow

```
┌──────────────────┐
│ Your Input       │
│ • Question       │
│ • Rubric         │
│ • Student Code   │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ EME Framework    │
│ • Calls LLMs     │
│ • Normalizes     │
│ • Computes Stats │
└────────┬─────────┘
         │
         ├─────────────────────┬──────────────────┐
         ▼                     ▼                  ▼
┌──────────────┐    ┌────────────────┐   ┌─────────────┐
│ Terminal     │    │ JSON Files     │   │ SQLite DB   │
│ • Tables     │    │ • Full details │   │ • Queryable │
│ • Summary    │    │ • Portable     │   │ • Persistent│
└──────────────┘    └────────────────┘   └─────────────┘
```

---

## Next Steps

Now that you've run your first evaluation:

### 1. Explore the Results
- Open the JSON output in a text editor
- Query the database with SQL
- Look at misconceptions identified

### 2. Understand the System
- Read [ARCHITECTURE.md](ARCHITECTURE.md) to understand how it works
- See [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) for data structure details

### 3. Use for Research
- Read [RESEARCH_GUIDE.md](RESEARCH_GUIDE.md) for analysis workflows
- Understand the statistical metrics
- Export data for publication

### 4. Extend the Framework
- Add new LLM providers (see [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md))
- Create custom comparison metrics
- Build analysis tools

---

## Troubleshooting

### "API Key Error"
```bash
# Check if .env is in the right place
ls -la .env

# Verify keys are loaded
uv run python -c "from dotenv import load_dotenv; import os; load_dotenv(); print(os.getenv('OPENAI_API_KEY'))"
```

### "No submissions found"
```bash
# Check directory structure
ls -la student_submissions/

# Submissions should be:
# student_submissions/{student_id}/*.java
```

### "JSON Validation Failed"
- Check that your rubric JSON is valid
- Ensure `totalPoints` matches sum of category points
- Use https://jsonlint.com to validate

### Database Issues
```bash
# Delete and recreate database
rm evaluations.db
uv run bench benchmark  # Will recreate automatically
```

---

## Quick Reference Card

```
# Installation
uv sync

# Configuration
.env (API keys + question + rubric)
OR
question_*.md + rubric_*.json

# Running
uv run bench benchmark          # Interactive
python single_submission.py     # One student

# Output Locations
data/results_*.json             # Evaluation results
evaluations.db                  # SQLite database
student_submissions/            # Input code (you create)

# Key Files
.env                            # Configuration
pyproject.toml                  # Dependencies
SCHEMA_DOCUMENTATION.md         # Complete reference
example.jsonc                   # Annotated example
```

---

## What's Next?

✅ You've successfully run your first evaluation!

**Beginner Path:**
1. Grade a few more submissions
2. Explore the JSON output structure
3. Try querying the database

**Developer Path:**
1. Read [ARCHITECTURE.md](ARCHITECTURE.md)
2. Study the code in `utils/` and `modes/`
3. Try adding a new LLM provider

**Researcher Path:**
1. Read [RESEARCH_GUIDE.md](RESEARCH_GUIDE.md)
2. Understand the statistical metrics
3. Design your research questions

---

## Need Help?

- **Documentation Hub**: [docs/INDEX.md](INDEX.md)
- **Schema Reference**: [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md)
- **Examples**: [../example.jsonc](../example.jsonc)
- **Issues**: Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md) (if exists)

---

**Welcome to the EME Framework! Happy grading!** 🎓
