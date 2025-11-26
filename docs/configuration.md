# Configuration

This document covers all configuration options for the EME CLI framework.

## Table of Contents

- [Environment Variables](#environment-variables)
- [CLI Configuration](#cli-configuration)
- [Model Configuration](#model-configuration)
- [Rubric Formats](#rubric-formats)
- [Directory Structure](#directory-structure)

## Environment Variables

Create a `.env` file in the project root:

```bash
# Required: OpenRouter API key for multi-model access
OPENROUTER_API_KEY=sk-or-v1-...

# Optional: Direct OpenAI API key
OPENAI_API_KEY=sk-...
```

### OpenRouter API Key

The primary way to access models. Get your key from [openrouter.ai](https://openrouter.ai/).

**Supported providers via OpenRouter:**
- OpenAI (GPT-4, GPT-4o, GPT-5 series)
- Google (Gemini Flash, Gemini Pro)
- Anthropic (Claude 3 series)
- Meta (Llama 3)
- And many more

### OpenAI API Key

Optional direct access to OpenAI models. If provided, can be used instead of OpenRouter for OpenAI-specific models.

## CLI Configuration

The main configuration constants are in `cli.py`:

```python
# Maximum number of students to process concurrently
MAX_CONCURRENT_STUDENTS = 5

# Maximum students per batch run
BATCH_LIMIT = 25

# Models to use for evaluation
MODELS = [
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5-nano",
]
```

### Concurrency Settings

| Setting | Default | Description |
|---------|---------|-------------|
| `MAX_CONCURRENT_STUDENTS` | 5 | Students processed in parallel |
| `BATCH_LIMIT` | 25 | Maximum students per batch |

**Tuning tips:**
- Reduce `MAX_CONCURRENT_STUDENTS` if hitting rate limits (429 errors)
- Increase if your API tier allows higher throughput
- Set `BATCH_LIMIT` based on time constraints

### Model Selection

The `MODELS` list determines which LLMs evaluate each submission:

```python
# Conservative (cheaper, faster)
MODELS = [
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5-nano",
]

# Research-grade (more accurate, slower, costly)
MODELS = [
    "google/gemini-2.5-flash",
    "openai/gpt-5.1",
    "anthropic/claude-3-sonnet",
    "openai/gpt-4o",
]

# Single model (testing)
MODELS = [
    "google/gemini-2.5-flash-lite",
]
```

## Model Configuration

### OpenRouter SDK Settings

Located in `utils/openrouter_sdk.py`:

```python
# Default model for fallback
DEFAULT_MODEL = "google/gemini-2.5-flash-lite"

# Retry configuration
@retry(
    wait=wait_exponential(multiplier=1, min=4, max=10),
    stop=stop_after_attempt(3),
    before_sleep=before_sleep_log(logger, logging.WARNING)
)
```

### Retry Settings

| Setting | Value | Description |
|---------|-------|-------------|
| Attempts | 3 | Maximum retry attempts |
| Min wait | 4s | Minimum wait between retries |
| Max wait | 10s | Maximum wait between retries |
| Backoff | Exponential | Wait time increases each attempt |

### Instructor Mode

The SDK uses Instructor in JSON mode for structured outputs:

```python
client = instructor.from_openai(
    AsyncOpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key=os.getenv("OPENROUTER_API_KEY"),
    ),
    mode=instructor.Mode.JSON,
)
```

## Rubric Formats

The system supports two rubric formats:

### JSON Format

```json
{
  "total_points": 4,
  "categories": [
    {
      "task": "Reading input using Scanner",
      "points": 1,
      "topic": "Reading input from the keyboard",
      "description": "Student correctly uses Scanner to read double values"
    },
    {
      "task": "Declaring variables (double)",
      "points": 1,
      "topic": "Variables, Data types",
      "description": "Student declares appropriate double variables"
    }
  ]
}
```

### Markdown Table Format

```markdown
# Marking Guide for Q1 (4 marks)

| Tasks | Marks | Topic | Why? |
|-------|-------|-------|------|
| Reading input using Scanner | +1 | Reading input from the keyboard | ... |
| Declaring variables (double) | +1 | Variables, Data types | ... |
| Using Math.sqrt correctly | +1 | Constants | ... |
| Computing distance formula | +1 | Variables | ... |
```

The system auto-detects the format and parses accordingly.

### Custom Rubric Fields

| Field | Required | Description |
|-------|----------|-------------|
| `task` | Yes | Task name (used in category_scores) |
| `points` / `marks` | Yes | Maximum points for this category |
| `topic` | No | Topic for misconception classification |
| `description` / `why` | No | Explanation of what's being assessed |

## Directory Structure

### Required Directories

```
ensemble-eval-cli/
├── student_submissions/     # Input: student code
│   └── {student_id}/
│       ├── Q1.java
│       ├── Q2.java
│       ├── Q3.java
│       └── Q4.java
├── data/a2/                 # Questions and rubrics
│   ├── q1.md
│   ├── q2.md
│   ├── q3.md
│   ├── q4.md
│   ├── rubric_q1.md
│   ├── rubric_q2.md
│   ├── rubric_q3.md
│   └── rubric_q4.md
└── .env                     # API keys
```

### Generated Directories

```
ensemble-eval-cli/
├── student_evals/           # Created by grading
│   └── {student_id}_{q_id}_eval.json
└── sandbox/evals/           # Created by sandbox script
```

### Student ID Format

Student directories should follow a consistent naming pattern:

```
student_submissions/
├── Chen_Wei_200023/
├── Alice_Smith_200024/
├── Bob_Jones_200025/
```

The student name is derived from the directory name by replacing underscores with spaces.

## Topic Configuration

### Canonical Topics

The 4 canonical topics for misconception classification:

```python
CANONICAL_TOPICS = [
    "Variables",
    "Data Types",
    "Constants",
    "Reading input from the keyboard",
]
```

### Custom Topic Mappings

Add mappings in `utils/misconception_analyzer.py`:

```python
TOPIC_MAPPING = {
    # Add custom mappings
    "my custom topic": "Variables",
    "another variation": "Data Types",
}
```

### Fuzzy Clustering Threshold

```python
# Default: 0.8 (80% similarity required for merge)
clusters = analyzer.cluster_misconceptions(threshold=0.8)

# More aggressive merging
clusters = analyzer.cluster_misconceptions(threshold=0.7)

# Conservative (fewer false positives)
clusters = analyzer.cluster_misconceptions(threshold=0.9)
```

## Prompt Configuration

### Prompt IDs

Each evaluation includes prompt configuration for traceability:

```python
config = Config(
    system_prompt_id="direct_v1",
    rubric_prompt_id="q1_rubric_v1"
)
```

### Switching Prompt Strategies

In `utils/grading.py`, import the desired prompt builder:

```python
# Direct grading (default)
from prompts.direct_prompt import build_prompt

# Reverse grading
from prompts.reverse_prompt import build_prompt

# EME grading
from prompts.eme_prompt import build_eme_prompt as build_prompt
```

## Schema Version

The schema version is defined in `pydantic_models/__init__.py`:

```python
__version__ = "1.0.0"
```

All evaluation documents are validated against this version. Update the version when making breaking schema changes.

## Example Configuration

### Production Setup

```python
# cli.py
MAX_CONCURRENT_STUDENTS = 3  # Conservative for rate limits
BATCH_LIMIT = 50             # Larger batches
MODELS = [
    "google/gemini-2.5-flash",
    "openai/gpt-4o",
    "anthropic/claude-3-sonnet",
]
```

### Development/Testing Setup

```python
# cli.py
MAX_CONCURRENT_STUDENTS = 5  # Faster iteration
BATCH_LIMIT = 5              # Small batches for testing
MODELS = [
    "google/gemini-2.5-flash-lite",  # Cheaper model
]
```

### Research Setup (Maximum Diversity)

```python
# cli.py
MAX_CONCURRENT_STUDENTS = 2  # Careful with rate limits
BATCH_LIMIT = 25
MODELS = [
    "google/gemini-2.5-flash",
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5.1",
    "openai/gpt-5-nano",
    "anthropic/claude-3-sonnet",
    "anthropic/claude-3-haiku",
]
```

## Related Documentation

- [Architecture](architecture.md) - System design
- [CLI Reference](cli-reference.md) - Command usage
- [Prompt Strategies](prompts.md) - Grading approaches
