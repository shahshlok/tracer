# Synthetic Dataset Generation with GPT-5.1

This document outlines how we generate synthetic student submissions using OpenAI's GPT-5.1 model (`gpt-5.1-2025-11-13`).

## Overview

The synthetic dataset generator creates realistic CS1 student Java code submissions with **seeded misconceptions**. This allows us to measure LLM misconception detection capabilities against a known ground truth.

## Pipeline Stages

### 1. Manifest Generation

The manifest defines the entire dataset structure *before* any LLM calls are made.

**Key inputs:**
- `groundtruth.json` — Misconception taxonomy with IDs, categories, and injection instructions
- Question prompts (`q1.md`, `q2.md`, etc.) — Assignment specifications
- Random seed — For reproducibility

**Distribution (realistic classroom):**
| Category | % | Misconceptions per Student |
|----------|---|---------------------------|
| Perfect | 40% | 0 (all clean files) |
| Single-issue | 35% | 1 |
| Struggling | 20% | 2 |
| Severely struggling | 5% | 3 |

**Constraint:** Each file contains *at most one* seeded misconception.

**Output:** `manifest.json` containing:
- Student metadata (name, ID, persona)
- Per-question file assignments (CLEAN or SEEDED)
- Misconception IDs and injection instructions

### 2. Persona System

Each synthetic student is assigned a coding style persona to introduce natural variation:

```
"Single-letter variables, minimal whitespace, no comments."
"Verbose comments everywhere, snake_case variables, standard indentation."
"Uses System.out.printf for everything, avoids temporary variables."
...
```

This ensures generated code doesn't all look identical, mimicking real classroom diversity.

### 3. Code Generation (GPT-5.1 Calls)

For each student-question pair, we construct a prompt and call the OpenAI Responses API.

**System prompt:**
```
You are a CS1 student writing Java code.
Your coding style is: {persona}
Respond with Java source code only, no explanations or markdown fences.
```

**User prompt (SEEDED file):**
```
Question Q1: Acceleration: compute (v1 - v0) / t using user input.

Full assignment text:
[Question markdown]

Write a solution for this question.
You must include the following specific conceptual error:
{injection_instruction}

Your mindset as this student: {student_thinking}

Keep the style consistent with the given persona. Output only the Java code.
```

**User prompt (CLEAN file):**
```
Question Q1: ...

Write a correct, working, novice-level solution.
Keep the style consistent with the persona. Output only the Java code.
```

### 4. Two-Phase Verification

Generated SEEDED files undergo verification to ensure the misconception was actually injected:

**Phase 1 — Compiler Gate:**
- Syntax errors: Code must NOT compile
- Logic errors: Code must compile successfully

**Phase 2 — Differential Execution:**
- Run seeded code and compare output to a clean reference
- Output must differ (otherwise the bug is "silent" and wasn't injected)

If verification fails, the file is regenerated (up to 3 retries).

## Misconception Taxonomy Example

From `groundtruth.json`:

```json
{
    "id": "NM_STATE_01",
    "category": "The Reactive State Machine",
    "name": "Spreadsheet View (Early Calculation)",
    "applicable_questions": ["Q1", "Q2"],
    "explanation": "The student views variables as reactive constraints...",
    "student_thinking": "\"I'll define the formula at the top so it's ready...\"",
    "instructions_for_llm": {
        "Q1": "Initialize v0=0, v1=0, t=0. Compute double a = (v1-v0)/t; _before_ reading input..."
    }
}
```

Each misconception has:
- Unique ID for ground-truth alignment
- Category (State, I/O, Types, Syntax, API)
- Applicable questions
- Detailed injection instructions per question
- "Student thinking" to guide realistic generation

## CLI Usage

```bash
# Full pipeline: manifest + generation
uv run python -m utils.generators.dataset_generator run \
    --assignment a2 \
    --students 100 \
    --seed 2025 \
    --model gpt-5.1-2025-11-13 \
    --concurrency 20

# Manifest only (no API calls)
uv run python -m utils.generators.dataset_generator manifest \
    --assignment a2 \
    --students 100 \
    --seed 2025

# Generate from existing manifest
uv run python -m utils.generators.dataset_generator generate \
    --manifest-path authentic_seeded/a2/manifest.json
```

## Output Structure

```
authentic_seeded/a2/
├── manifest.json
├── Smith_John_123456/
│   ├── Q1.java  (SEEDED: NM_STATE_01)
│   ├── Q2.java  (CLEAN)
│   ├── Q3.java  (CLEAN)
│   └── Q4.java  (CLEAN)
├── Doe_Jane_789012/
│   ├── Q1.java  (CLEAN)
│   ├── Q2.java  (SEEDED: NM_IO_02)
│   ├── Q3.java  (SEEDED: NM_SYN_01)
│   └── Q4.java  (CLEAN)
...
```

## Why GPT-5.1?

- Strong instruction following for precise error injection
- Code quality suitable for CS1-level submissions
- Consistent persona adherence
- Reliable structured output (code-only responses)

## Limitations

1. **Synthetic data caveat:** Generated code may not capture all nuances of real student submissions
2. **Single-error constraint:** Real students often have multiple overlapping errors per file
3. **Verification gaps:** Some subtle logic errors may pass differential execution if they produce identical output on test inputs
