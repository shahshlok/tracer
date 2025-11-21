# Architecture Guide

Deep technical overview of the system design, data flow, and components.

## Table of Contents

- [System Overview](#system-overview)
- [Data Flow Pipeline](#data-flow-pipeline)
- [Module Architecture](#module-architecture)
- [Design Patterns](#design-patterns)
- [Extension Guide](#extension-guide)

---

## System Overview

The EME Framework is built on a **pipeline architecture** with clear separation of concerns:

```
┌──────────────────────────────────────────────────────────────┐
│                     INPUT LAYER                               │
│  • Assignment question (markdown)                             │
│  • Grading rubric (JSON)                                      │
│  • Student submission (code files)                            │
└──────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                  PROMPT GENERATION LAYER                      │
│  Transforms input into LLM instructions                       │
│  Strategies: Direct, Reverse, EME                             │
└──────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                   LLM INTEGRATION LAYER                       │
│  Communicates with AI services (OpenAI, OpenRouter)           │
│  Handles authentication, requests, responses                  │
└──────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                STRUCTURED RESPONSE PARSING                    │
│  Pydantic validates LLM output against schema                 │
│  Ensures type safety and completeness                         │
└──────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│              EVALUATION DOCUMENT ASSEMBLY                     │
│  Combines all data into single comprehensive document         │
│  Adds metadata (IDs, timestamps, versions)                    │
└──────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                    OUTPUT LAYER                               │
│  • JSON files (student_evals/)                                │
│  • [Future] SQLite database                                   │
│  • [Future] Dashboard visualizations                          │
└──────────────────────────────────────────────────────────────┘
```

### Core Design Principles

1. **Schema-First Design**
   - Pydantic models define the contract for all data
   - All data flows through validated schemas
   - Type safety throughout the system

2. **Separation of Concerns**
   - Input handling separate from processing
   - Processing separate from output
   - Each layer has single responsibility

3. **Provider Agnostic**
   - LLM provider abstraction via utils/
   - Swap providers without changing data models
   - Support for multiple models simultaneously

4. **Type Safety**
   - Full Python type hints
   - IDE autocomplete support
   - Catch errors at development time

5. **Extensibility**
   - Easy to add new prompting strategies
   - Easy to add new LLM providers
   - Easy to add new comparison metrics

6. **Reproducibility**
   - Every evaluation includes full provenance
   - Model names, versions, timestamps, config
   - Can recreate identical evaluations

---

## Data Flow Pipeline

### Step-by-Step Process

```
1. INPUT PREPARATION
   ├─ Read question_*.md
   ├─ Parse rubric_*.json
   ├─ Load student_submissions/{id}/*.java
   └─ Output: (question_text, rubric_dict, code_string)

2. CONTEXT BUILDING
   ├─ Create Context object (course/assignment metadata)
   ├─ Create Submission object (student info and files)
   ├─ Create Rubric object (parse JSON into Pydantic model)
   └─ Output: (context, submission, rubric) objects

3. PROMPT CONSTRUCTION
   ├─ Choose strategy (direct/reverse/eme)
   ├─ Call builder function with question/rubric/code
   ├─ Receive formatted prompt string
   └─ Output: prompt_string

4. LLM EVALUATION (Parallel for N models)
   │
   For each model:
   ├─ Send prompt to LLM via utils/{provider}_sdk.py
   ├─ Get JSON response
   ├─ Validate against LLMEvaluationResponse schema
   ├─ Parse errors if validation fails
   └─ Retry or skip on error
   │
   └─ Output: dict[model_name → LLMEvaluationResponse]

5. WRAP IN ModelEvaluation
   ├─ Add metadata (model_name, provider, run_id)
   ├─ Add config (prompt_ids, temperature, max_tokens)
   ├─ Attach LLMEvaluationResponse data
   └─ Output: dict[model_name → ModelEvaluation]

6. ASSEMBLE EvaluationDocument
   ├─ Generate unique evaluation_id
   ├─ Set schema_version ("1.0.0")
   ├─ Add created_at timestamp
   ├─ Add created_by (script name)
   ├─ Attach Context, Submission, Rubric
   ├─ Attach Models dict
   └─ Output: EvaluationDocument instance

7. VALIDATION & SERIALIZATION
   ├─ Pydantic validates entire document
   ├─ Convert to JSON via model_dump_json()
   └─ Output: JSON string

8. PERSISTENCE
   ├─ Write JSON to student_evals/{student_id}_eval.json
   ├─ [Future] Insert into SQLite database
   └─ Output: File on disk
```

### Data Structure Hierarchy

```
EvaluationDocument (root)
│
├─ evaluation_id: str                    "eval_a1b2c3d4"
├─ schema_version: str                   "1.0.0"
├─ created_at: datetime                  ISO 8601 timestamp
├─ created_by: str                       "grade_sergio.py"
│
├─ context: Context
│  ├─ course_id: str
│  ├─ course_name: str
│  ├─ assignment_id: int
│  ├─ assignment_title: str
│  ├─ question_source_path: str
│  ├─ question_id: str
│  ├─ question_title: str
│  └─ rubric_source_path: str
│
├─ submission: Submission
│  ├─ student_id: str
│  ├─ student_name: str
│  ├─ submitted_at: datetime
│  ├─ programming_language: str
│  └─ files: list[StudentFile]
│     ├─ path: str
│     └─ language: str
│
├─ rubric: Rubric
│  ├─ rubric_id: str
│  ├─ title: str
│  ├─ total_points: int
│  └─ categories: list[RubricCategory]
│     ├─ category_id: str
│     ├─ name: str
│     ├─ max_points: int
│     └─ description: str
│
└─ models: dict[str, ModelEvaluation]
   │
   ├─ "model_1_key": ModelEvaluation
   │  ├─ model_name: str
   │  ├─ provider: str
   │  ├─ model_version: str | None
   │  ├─ run_id: str
   │  ├─ config: Config
   │  │  ├─ system_prompt_id: str
   │  │  ├─ rubric_prompt_id: str
   │  │  ├─ temperature: float | None
   │  │  └─ max_tokens: int | None
   │  │
   │  ├─ scores: Scores
   │  │  ├─ total_points_awarded: float
   │  │  ├─ max_points: int
   │  │  ├─ percentage: float
   │  │  └─ confidence: float | None
   │  │
   │  ├─ category_scores: list[CategoryScore]
   │  │  ├─ category_id: str
   │  │  ├─ points_awarded: float
   │  │  ├─ max_points: int
   │  │  ├─ reasoning: str
   │  │  └─ confidence: float | None
   │  │
   │  ├─ feedback: Feedback
   │  │  ├─ strengths: list[str]
   │  │  └─ areas_for_improvement: list[str]
   │  │
   │  └─ misconceptions: list[Misconception]
   │     ├─ description: str
   │     ├─ confidence: float
   │     ├─ evidence: list[Evidence]
   │     │  ├─ code_snippet: str
   │     │  ├─ line_number: int | None
   │     │  └─ explanation: str
   │     ├─ identified_by_model: str | None
   │     └─ validated: bool
   │
   ├─ "model_2_key": ModelEvaluation
   │  └─ ...
   │
   └─ "model_N_key": ModelEvaluation
      └─ ...
```

---

## Module Architecture

### Layer Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    USER CODE                                 │
│  (Your evaluation scripts, e.g., grade_sergio.py)            │
└─────────────────────────────────────────────────────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  pydantic_   │  │   prompts/   │  │    utils/    │
│   models/    │  │              │  │              │
│              │  ├──────────────┤  ├──────────────┤
│ Data Layer   │  │ Prompt Layer │  │Integration   │
│              │  │              │  │   Layer      │
│ • evaluation │  │ • direct     │  │• openai_     │
│ • context    │  │ • reverse    │  │  client      │
│ • submission │  │ • eme        │  │• openrouter_ │
│ • rubric     │  │              │  │  sdk         │
│ • models     │  │              │  │              │
│ • comparison │  │              │  │              │
└──────────────┘  └──────────────┘  └──────────────┘
```

### Directory Details

#### `pydantic_models/` - Data Layer

**Responsibility:** Define and validate all data structures

**Key Files:**
- `evaluation.py` - Root EvaluationDocument
- `context/models.py` - Educational metadata
- `submission/models.py` - Student information
- `rubric/models.py` - Grading scales
- `models/evaluation.py` - Model evaluation results
- `comparison/` - Multi-model analysis (schemas only)

**Key Principles:**
- One model per concept
- Comprehensive field descriptions
- Type safety through Pydantic
- Validation rules where appropriate
- Backward compatibility via versioning

**Usage:** Import in other layers
```python
from pydantic_models import EvaluationDocument, ModelEvaluation
```

#### `prompts/` - Prompt Layer

**Responsibility:** Build prompts for LLM from input data

**Key Files:**
- `direct_prompt.py` - Direct grading
- `reverse_prompt.py` - Reverse engineering
- `eme_prompt.py` - Ensemble method

**Interface:**
```python
def build_X_prompt(
    question: str,
    rubric: str,
    student_code: str
) -> str:
    # Returns formatted prompt string
```

**Key Principles:**
- Pure functions (no side effects)
- Take question, rubric, code as inputs
- Return formatted prompt string
- No external dependencies

**Usage in user code:**
```python
from prompts.direct_prompt import build_direct_prompt

prompt = build_direct_prompt(q, r, c)
messages = [{"role": "user", "content": prompt}]
```

#### `utils/` - Integration Layer

**Responsibility:** Communicate with external LLM services

**Key Files:**
- `openai_client.py` - OpenAI API client
- `openrouter_sdk.py` - OpenRouter API client

**Interface:**
```python
# OpenAI style (direct)
def evaluation_with_openai(...) -> ModelEvaluation

# OpenRouter style (generic)
def get_structured_response(...) -> T
```

**Key Principles:**
- Handle authentication via environment variables
- Request formatting for each provider
- Response parsing and validation
- Error handling and retries
- Return validated Pydantic objects

**Usage in user code:**
```python
from utils.openrouter_sdk import get_structured_response
from pydantic_models import LLMEvaluationResponse

response = get_structured_response(
    messages=[...],
    response_model=LLMEvaluationResponse,
    model="google/gemini-2.5-flash-lite"
)
```

---

## Design Patterns

### Pattern 1: Provider Abstraction

Problem: Different LLM providers have different APIs

Solution: Abstract common interface
```python
# User code doesn't know provider details
response = get_structured_response(
    messages=messages,
    response_model=MyModel,
    model="provider/model-name"
)

# Implementation hidden in utils/
class OpenAIProvider:
    def get_response(...)

class OpenRouterProvider:
    def get_response(...)
```

### Pattern 2: Schema Validation

Problem: LLM outputs can be invalid or incomplete

Solution: Pydantic validates automatically
```python
# Raw LLM response (might be invalid)
raw_response = llm.generate(prompt)

# Automatic validation and parsing
validated = LLMEvaluationResponse(**json.loads(raw_response))
# Raises ValidationError if invalid
```

### Pattern 3: Composition

Problem: Multiple concepts need to be combined

Solution: Build larger objects from smaller ones
```python
# Build from components
eval_doc = EvaluationDocument(
    context=context,
    submission=submission,
    rubric=rubric,
    models=model_evals  # Dict of ModelEvaluation objects
)

# Serialize entire tree
json_str = eval_doc.model_dump_json(indent=2)
```

### Pattern 4: Metadata Tracking

Problem: Need to know how evaluations were created

Solution: Store full provenance
```python
ModelEvaluation(
    model_name="google/gemini-2.5-flash-lite",  # Which model
    provider="openrouter",                       # Which provider
    run_id="run_abc123",                         # Which run
    config=Config(                               # How it was configured
        system_prompt_id="direct_v1",
        rubric_prompt_id="rubric_v1",
        temperature=0.7,
        max_tokens=2000
    )
)
```

---

## Extension Guide

### Adding a New LLM Provider

**Step 1:** Create new file `utils/{provider}_sdk.py`

```python
# utils/anthropic_sdk.py
from anthropic import Anthropic
from pydantic import BaseModel
from typing import TypeVar

T = TypeVar("T", bound=BaseModel)

client = Anthropic(api_key=os.environ["ANTHROPIC_API_KEY"])

def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = "claude-3-opus-20240229"
) -> T:
    """Get structured response from Claude."""
    response = client.messages.create(
        model=model,
        messages=messages,
        # Claude-specific configuration
    )

    # Parse response into Pydantic model
    import json
    parsed = json.loads(response.content)
    return response_model(**parsed)
```

**Step 2:** Use in your evaluation script

```python
from utils.anthropic_sdk import get_structured_response
from pydantic_models import LLMEvaluationResponse

response = get_structured_response(
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,
    model="claude-3-opus-20240229"
)
```

### Adding a New Prompting Strategy

**Step 1:** Create new file `prompts/{strategy}_prompt.py`

```python
# prompts/expert_prompt.py
def build_expert_prompt(
    question: str,
    rubric: str,
    student_code: str
) -> str:
    """Prompts model to act as expert evaluator."""
    return f"""You are an expert software engineer with 20+ years of experience.

Your task is to evaluate this student's code:

**Assignment:**
{question}

**Rubric:**
{rubric}

**Code:**
```
{student_code}
```

Provide a thorough evaluation that would be helpful for the student's learning.
"""
```

**Step 2:** Use in evaluation script

```python
from prompts.expert_prompt import build_expert_prompt

prompt = build_expert_prompt(question, rubric_json, code)
```

### Adding New Comparison Metrics

Comparison models are in `pydantic_models/comparison/` but computation isn't implemented yet.

**When implemented, you would:**

1. Add field to appropriate model in `pydantic_models/comparison/`
2. Implement computation function
3. Update main `compute_comparison()` function

```python
# Future: pydantic_models/comparison/reliability.py
class ReliabilityMetrics(BaseModel):
    intraclass_correlation_icc: float | None
    fleiss_kappa: float | None  # New metric

# Future: comparison_engine.py
def compute_comparison(models: dict[str, ModelEvaluation]) -> Comparison:
    # ... existing code ...

    reliability = ReliabilityMetrics(
        intraclass_correlation_icc=compute_icc(...),
        fleiss_kappa=compute_fleiss_kappa(...)  # New
    )

    return Comparison(..., reliability_metrics=reliability)
```

---

## Performance Considerations

### Current Status

**LLM Queries:** Sequential (one model after another)
- Simple to implement
- Slower for multiple models
- ~2-5 minutes for 3 models

**Storage:** JSON files only
- Human-readable
- Easy to debug
- Slower for large cohorts (1000+ students)

### Future Optimizations

**Parallel LLM Queries:**
```python
import asyncio

async def evaluate_with_model(model_name):
    return await async_get_structured_response(...)

responses = await asyncio.gather(
    *[evaluate_with_model(m) for m in models]
)
```

**Caching:**
```python
# Cache by (model, prompt_hash, code_hash)
cache_key = hashlib.sha256(
    f"{model_name}:{prompt}:{code}".encode()
).hexdigest()
```

**Database:**
- Store evaluations in SQLite for querying
- Lazy loading for large datasets
- Pagination for UI display

---

## Security & Privacy

### API Key Management

```python
# ✅ Good: Store in .env
OPENAI_API_KEY=sk-...

# ✅ Good: Load via environment variable
import os
api_key = os.environ["OPENAI_API_KEY"]

# ❌ Bad: Hardcode in source
api_key = "sk-..."
```

### Data Handling

```python
# Student data sent to third-party APIs
# Check provider data retention policies:
# - OpenAI: https://platform.openai.com/account/api-keys
# - OpenRouter: https://openrouter.ai/privacy

# For sensitive code:
# - Self-host models (Ollama, vLLM)
# - On-premise evaluations
```

### File Permissions

```bash
# Mark sensitive files
chmod 600 .env
chmod 600 .env.local

# Add to .gitignore
echo ".env" >> .gitignore
echo "*.pem" >> .gitignore
```

---

## Testing Strategy

### Current Test Coverage

**Tested:** ~5-10%
- `tests/simple_openai_test.py` - Basic OpenAI integration

**Not tested:**
- OpenRouter integration
- Prompt builders
- Comparison logic
- Error scenarios

### Testing Best Practices

**Mock LLM Responses:**
```python
from unittest.mock import patch

def test_evaluation_with_mock():
    mock_response = LLMEvaluationResponse(
        scores=Scores(
            total_points_awarded=85,
            max_points=100,
            percentage=85.0,
            confidence=0.92
        ),
        category_scores=[...],
        feedback=Feedback(...),
        misconceptions=[]
    )

    with patch('utils.openrouter_sdk.get_structured_response',
               return_value=mock_response):
        result = grade_student(...)
        assert result.scores.percentage == 85.0
```

**Test Validation:**
```python
def test_invalid_scores():
    with pytest.raises(ValidationError):
        Scores(
            total_points_awarded=150,  # Invalid
            max_points=100,
            percentage=150,
            confidence=0.92
        )
```

---

## Maintenance & Versioning

### Schema Versioning

Current version: `1.0.0` (Major.Minor.Patch)

**Breaking changes** (bump major):
- Remove required field
- Change field type
- Rename field

**Non-breaking changes** (bump minor):
- Add optional field
- Add new category
- Expand allowed values

**Bug fixes** (bump patch):
- Fix validation rule
- Fix type annotation
- Fix documentation

### Backward Compatibility

```python
# v1.1.0 adds new optional field
class ModelEvaluation(BaseModel):
    # ... existing fields ...
    custom_score: float | None = None  # New in v1.1

# Old v1.0 files still load:
old_data = {"model_name": "...", ...}
model = ModelEvaluation(**old_data)  # custom_score is None
```

---

**Next:** Return to [`00-QUICK-START.md`](00-QUICK-START.md) or see [`INDEX.md`](INDEX.md) for navigation.
