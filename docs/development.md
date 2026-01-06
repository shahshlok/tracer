# Development Guide

How to extend and contribute to the framework.

---

## Setup

### Prerequisites

- Python 3.12+
- [uv](https://github.com/astral-sh/uv) package manager
- API keys for Anthropic, Google, and OpenAI

### Installation

```bash
git clone https://github.com/shahshlok/tracer
cd tracer
uv sync
```

### Environment Variables

```bash
export ANTHROPIC_API_KEY="sk-ant-..."    # For Claude models
export GOOGLE_API_KEY="..."              # For Gemini models
export OPENAI_API_KEY="sk-..."           # For GPT models & semantic embeddings
```

---

## Running Tests

```bash
# Run all tests
uv run pytest

# Run with verbose output
uv run pytest -v

# Run specific test file
uv run pytest tests/test_anthropic_client.py
```

---

## Code Style

We use [ruff](https://github.com/astral-sh/ruff) for linting and formatting.

```bash
# Format code
uv run ruff format .

# Check for issues
uv run ruff check .

# Auto-fix issues
uv run ruff check --fix .
```

---

## Adding a New Assignment

### Step 1: Create Data Directory

```bash
mkdir -p data/a4
```

### Step 2: Add Question Files

```
data/a4/
├── a4.md           # Assignment description
├── q1.md           # Question 1 prompt
├── q2.md           # Question 2 prompt
├── q3.md           # Question 3 prompt
└── groundtruth.json # Misconception definitions
```

### Step 3: Define Misconceptions

```json
{
  "misconceptions": [
    {
      "id": "NM_OBJ_01",
      "category": "The Reference Machine",
      "name": "Object Aliasing",
      "explanation": "Student doesn't understand that objects are references...",
      "student_thinking": "I made a copy of the object...",
      "code_pattern": "Assign object without copy, expect independence",
      "applicable_questions": ["Q1", "Q2"]
    }
  ]
}
```

### Step 4: Generate Synthetic Data

```bash
uv run python utils/generators/dataset_generator.py --assignment a4
```

---

## Adding a New LLM Model

### Step 1: Add to Model List

In `miscons.py`:

```python
MODELS = [
    "openai/gpt-5.2",
    "anthropic/claude-haiku-4.5",
    "google/gemini-3-flash",
    "your-provider/your-model",  # Add here
]
```

### Step 2: Add Short Name

```python
MODEL_SHORT_NAMES = {
    "openai/gpt-5.2": "gpt-5.2",
    "anthropic/claude-haiku-4.5": "claude-haiku",
    "google/gemini-3-flash": "gemini-flash",
    "your-provider/your-model": "your-model",  # Add here
}
```

### Step 3: Ensure API Support

Models must have a client implementation in `utils/llm/` (anthropic.py, gemini.py, openai.py).

---

## Adding a New Prompt Strategy

### Step 1: Create Builder Function

In `prompts/strategies.py`:

```python
def build_my_strategy_prompt(problem_description: str, student_code: str) -> str:
    return f'''
Your custom prompt instructions...

Problem:
{problem_description}

Student Code:
{student_code}

{OUTPUT_SCHEMA}
'''.strip()
```

### Step 2: Add to Enum

```python
class PromptStrategy(str, Enum):
    BASELINE = "baseline"
    TAXONOMY = "taxonomy"
    COT = "cot"
    SOCRATIC = "socratic"
    MY_STRATEGY = "my_strategy"
```

### Step 3: Register

```python
STRATEGIES = {
    PromptStrategy.BASELINE: build_baseline_prompt,
    PromptStrategy.TAXONOMY: build_taxonomy_prompt,
    PromptStrategy.COT: build_cot_prompt,
    PromptStrategy.SOCRATIC: build_socratic_prompt,
    PromptStrategy.MY_STRATEGY: build_my_strategy_prompt,
}
```

---

## Adding a New Matcher

### Step 1: Create Matcher Module

In `utils/matching/my_matcher.py`:

```python
def my_match_misconception(
    detected_name: str,
    detected_description: str,
    groundtruth: list[dict],
    threshold: float = 0.5,
) -> tuple[str | None, float, str]:
    """
    Custom matching logic.
    
    Returns:
        (matched_id, score, "my_method")
    """
    # Your matching logic here
    return matched_id, score, "my_method"
```

### Step 2: Register in Analysis

In `analyze.py`, add to the matching dispatch:

```python
from utils.matching.my_matcher import my_match_misconception

# In the matching section:
if match_mode == "my_matcher":
    result = my_match_misconception(...)
```

---

## Key Files Reference

| File | Purpose | Lines |
|------|---------|-------|
| `analyze.py` | Main analysis with semantic matching | ~800 |
| `miscons.py` | LLM detection orchestration | ~400 |
| `prompts/strategies.py` | Prompt templates | ~200 |
| `utils/matching/semantic.py` | OpenAI embedding pipeline | ~150 |
| `utils/statistics.py` | Bootstrap CI, McNemar's test | ~300 |
| `utils/generators/dataset_generator.py` | Synthetic data generation | ~250 |

---

## Architecture Decisions

### Why Semantic Matching?

LLMs use different terminology than our taxonomy. "Auto-update error" and "Reactive State Machine" are the same concept. Semantic embeddings bridge this gap with 87%+ accuracy.

### Why Ensemble Voting?

Single strategies hallucinate frequently (68% FP rate). Requiring ≥2 strategies to agree filters 92% of false positives while maintaining recall.

### Why Synthetic Data?

Real student code has multiple interacting errors, making it hard to evaluate. Synthetic data with one bug per file enables clean labeling.

---

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Run `uv run ruff format .` and `uv run ruff check .`
5. Submit a pull request

---

## Research Extensions

Potential future work:

1. **A4 Assignment** — Objects and references (heap indirection)
2. **Cross-language** — Python, JavaScript, C++
3. **Confidence weighting** — Weight votes by LLM confidence
4. **Interactive diagnosis** — Socratic dialogue with students
5. **Misconception chains** — Track how errors propagate

---

## Previous: [CLI Reference](cli-reference.md) | Next: [Context & Limitations](context.md)
