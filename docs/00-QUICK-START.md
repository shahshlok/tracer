# Quick Start Guide

This document is a succinct path from a blank environment to a working evaluation run. It assumes familiarity with Python tooling, virtual environments, and environment variables.

## Overview

The Ensemble Model Evaluation (EME) Framework grades student code with multiple LLMs (OpenAI, Anthropic, Gemini, etc.), produces structured evaluation documents, and enables cross-model comparison and ensemble strategies.

High-level workflow:
```
Student Code → Multiple LLMs → Structured Evaluations → Comparison / Analysis
```

## Installation

### Step 1: Clone the repository

```bash
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli
```

### Step 2: Install dependencies (uv or pip)

Using `uv` (recommended):

```bash
uv sync
```

Or with pip in an existing environment:

```bash
pip install -e .
```

### Step 3: Configure API keys

The framework reads API keys from environment variables (via `python-dotenv`). Create a `.env` file in the project root:

```bash
# Required: Get from https://platform.openai.com/api-keys
OPENAI_API_KEY=sk-...

# Required: Get from https://openrouter.ai/keys
OPENROUTER_API_KEY=sk-or-...
```

## Run the reference evaluation

From the project root:

```bash
uv run python grade_sergio.py
```

This script:
1. Loads the example assignment (`question_cuboid.md`) and rubric (`rubric_cuboid.json`)
2. Loads a sample Java submission under `student_submissions/Diaz_Sergio_100029/`
3. Evaluates the submission with one or more configured LLMs
4. Writes a fully-populated `EvaluationDocument` JSON file:

```
student_evals/
└── sergio_eval.json          ← Results file with all grading data
```

The resulting JSON contains:
- Course/assignment context
- Submission metadata and file list
- Rubric definition
- Per-model scores, breakdowns, feedback, and misconceptions

For a detailed breakdown of the schema, see `docs/04-API-REFERENCE.md`.

## Recommended next reads

- Conceptual model and terminology: `01-GETTING-STARTED.md`
- Project layout and root scripts (including CLI): `02-PROJECT-STRUCTURE.md`
- Custom evaluations and batch workflows: `03-USAGE-GUIDE.md`
- Architecture and extension points: `05-ARCHITECTURE.md`

## Troubleshooting

**Error: "OpenAI API key not found"**
- Ensure `.env` exists in the project root and defines `OPENAI_API_KEY=...`
- Confirm your shell is not overriding the variable with a different value

**Error: "OPENROUTER_API_KEY not set"**
- Ensure the key is present in `.env` and formatted correctly

**Import / module errors**
- Re-run `uv sync` (or `pip install -e .`) from the project root
- Verify that you are invoking Python from the environment where dependencies are installed

**API rate limiting**
- Back off and retry; consider reducing the number of models or concurrent requests

For a broader troubleshooting view and error-handling patterns, see `docs/04-API-REFERENCE.md` and `docs/05-ARCHITECTURE.md`.
