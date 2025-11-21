# API Reference

Complete technical reference for all functions, classes, and data models.

## Table of Contents

- [Data Models](#data-models)
- [Utility Functions](#utility-functions)
- [Prompt Builders](#prompt-builders)
- [Error Handling](#error-handling)

---

## Data Models

All data models are located in `pydantic_models/` and use Pydantic v2 for validation.

### Root: EvaluationDocument

**Location:** `pydantic_models/evaluation.py`

The main container holding all evaluation data for one student.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `evaluation_id` | `str` | Unique ID (e.g., "eval_a1b2c3d4") |
| `schema_version` | `str` | Version of schema (e.g., "1.0.0") |
| `created_at` | `datetime` | When evaluation was created |
| `created_by` | `str` | Script that created it (e.g., "grade_sergio.py") |
| `context` | `Context` | Course/assignment metadata |
| `submission` | `Submission` | Student info |
| `rubric` | `Rubric` | Grading criteria |
| `models` | `dict[str, ModelEvaluation]` | Results from each AI model |

**Validation Rules:**
- Schema version must match library version
- All nested models validated recursively
- Timestamps must be timezone-aware

**Example:**
```python
from pydantic_models import EvaluationDocument
from datetime import datetime, timezone
import uuid

eval_doc = EvaluationDocument(
    evaluation_id=f"eval_{uuid.uuid4().hex[:8]}",
    schema_version="1.0.0",
    created_at=datetime.now(timezone.utc),
    created_by="my_script.py",
    context=...,
    submission=...,
    rubric=...,
    models={...}
)

# Serialize to JSON
json_string = eval_doc.model_dump_json(indent=2)

# Deserialize from JSON
eval_doc = EvaluationDocument(**json.loads(json_string))
```

---

### Context Models

#### Context

**Location:** `pydantic_models/context/models.py`

Educational metadata about the course, assignment, and question.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `course_id` | `str` | Course identifier (e.g., "CS101") |
| `course_name` | `str` | Full course name |
| `assignment_id` | `int` | Assignment number |
| `assignment_title` | `str` | Assignment name |
| `question_source_path` | `str` | Path to question file |
| `question_id` | `str` | Question identifier |
| `question_title` | `str` | Question name |
| `rubric_source_path` | `str` | Path to rubric file |

**Example:**
```python
from pydantic_models import Context

context = Context(
    course_id="CS101",
    course_name="Introduction to Computer Science",
    assignment_id=1,
    assignment_title="Object-Oriented Programming",
    question_source_path="question_cuboid.md",
    question_id="q1",
    question_title="Cuboid Class Implementation",
    rubric_source_path="rubric_cuboid.json"
)
```

---

### Submission Models

#### Submission

**Location:** `pydantic_models/submission/models.py`

Student submission data and metadata.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `student_id` | `str` | Student ID (e.g., "Diaz_Sergio_100029") |
| `student_name` | `str` | Full name |
| `submitted_at` | `datetime` | Submission timestamp |
| `programming_language` | `str` | Primary language (e.g., "Java") |
| `files` | `list[StudentFile]` | List of submitted files |

**Example:**
```python
from pydantic_models import Submission, StudentFile
from datetime import datetime, timezone

submission = Submission(
    student_id="Diaz_Sergio_100029",
    student_name="Sergio Diaz",
    submitted_at=datetime(2025, 11, 19, 9, 0, 0, tzinfo=timezone.utc),
    programming_language="Java",
    files=[
        StudentFile(path="Cuboid.java", language="Java"),
        StudentFile(path="CuboidTest.java", language="Java")
    ]
)
```

#### StudentFile

**Location:** `pydantic_models/submission/models.py`

Represents a single submitted file.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `path` | `str` | Relative path to file |
| `language` | `str` | Programming language |

---

### Rubric Models

#### Rubric

**Location:** `pydantic_models/rubric/models.py`

Complete grading rubric with categories.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `rubric_id` | `str` | Unique rubric identifier |
| `title` | `str` | Rubric title |
| `total_points` | `int` | Sum of all category points |
| `categories` | `list[RubricCategory]` | Grading categories |

**Example:**
```python
from pydantic_models import Rubric, RubricCategory

rubric = Rubric(
    rubric_id="rubric_cuboid_v1",
    title="Cuboid Assignment Rubric",
    total_points=100,
    categories=[
        RubricCategory(
            category_id="correctness",
            name="Correctness",
            max_points=40,
            description="Code produces correct outputs"
        ),
        RubricCategory(
            category_id="code_quality",
            name="Code Quality",
            max_points=30,
            description="Well-structured, readable code"
        ),
        RubricCategory(
            category_id="efficiency",
            name="Efficiency",
            max_points=30,
            description="Optimal time and space complexity"
        )
    ]
)
```

#### RubricCategory

**Location:** `pydantic_models/rubric/models.py`

Individual grading category.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `category_id` | `str` | Unique identifier |
| `name` | `str` | Display name |
| `max_points` | `int` | Maximum points for this category |
| `description` | `str` | What this category evaluates |

---

### Model Evaluation

#### ModelEvaluation

**Location:** `pydantic_models/models/evaluation.py`

Complete evaluation from a single LLM model.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `model_name` | `str` | Model identifier (e.g., "google/gemini-2.5-flash-lite") |
| `provider` | `str` | Provider (e.g., "openrouter", "openai") |
| `model_version` | `str \| None` | Specific version if available |
| `run_id` | `str` | Unique run identifier |
| `config` | `Config` | Configuration used |
| `scores` | `Scores` | Aggregate scores |
| `category_scores` | `list[CategoryScore]` | Per-category breakdown |
| `feedback` | `Feedback` | Overall feedback |
| `misconceptions` | `list[Misconception]` | Identified misconceptions |

**Example:**
```python
from pydantic_models import ModelEvaluation, Config
import uuid

model_eval = ModelEvaluation(
    model_name="google/gemini-2.5-flash-lite",
    provider="openrouter",
    run_id=f"run_{uuid.uuid4().hex[:8]}",
    config=Config(
        system_prompt_id="direct_v1",
        rubric_prompt_id="rubric_v1",
        temperature=0.7,
        max_tokens=2000
    ),
    scores=scores,
    category_scores=category_scores,
    feedback=feedback,
    misconceptions=misconceptions
)
```

#### LLMEvaluationResponse

**Location:** `pydantic_models/models/evaluation.py`

Direct output from the LLM (before wrapping in ModelEvaluation).

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `scores` | `Scores` | Overall scores |
| `category_scores` | `list[CategoryScore]` | Per-category breakdown |
| `feedback` | `Feedback` | Feedback |
| `misconceptions` | `list[Misconception]` | Identified issues |

**Used when:** LLM responses are parsed and validated

#### Config

**Location:** `pydantic_models/models/evaluation.py`

Configuration metadata for a model evaluation.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `system_prompt_id` | `str` | Which system prompt used |
| `rubric_prompt_id` | `str` | Which rubric template used |
| `temperature` | `float \| None` | Sampling temperature (0-1) |
| `max_tokens` | `int \| None` | Max response length |

#### Scores

**Location:** `pydantic_models/models/evaluation.py`

Aggregate scoring information.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `total_points_awarded` | `float` | Sum across categories |
| `max_points` | `int` | Total possible points |
| `percentage` | `float` | Score as percentage (0-100) |
| `confidence` | `float \| None` | Model's confidence (0-1) |

**Validation Rules:**
- Percentage must be 0-100
- Confidence must be 0-1
- Total points awarded ≤ max points

#### CategoryScore

**Location:** `pydantic_models/models/evaluation.py`

Score for a single rubric category.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `category_id` | `str` | Links to rubric category |
| `points_awarded` | `float` | Points given |
| `max_points` | `int` | Max possible points |
| `reasoning` | `str` | Explanation of score |
| `confidence` | `float \| None` | Model's confidence (0-1) |

#### Feedback

**Location:** `pydantic_models/models/evaluation.py`

Overall feedback on submission.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `strengths` | `list[str]` | Positive aspects |
| `areas_for_improvement` | `list[str]` | Areas needing work |

#### Misconception

**Location:** `pydantic_models/models/evaluation.py`

Identified misconception with evidence.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `description` | `str` | What the misconception is |
| `confidence` | `float` | Model's confidence (0-1) |
| `evidence` | `list[Evidence]` | Supporting code examples |
| `identified_by_model` | `str \| None` | Which model found this |
| `validated` | `bool` | Human-confirmed? (default: False) |

**Example:**
```python
from pydantic_models import Misconception, Evidence

misconception = Misconception(
    description="Uses mutable default arguments",
    confidence=0.85,
    evidence=[
        Evidence(
            code_snippet="public Cuboid(int[] dims = new int[3]) {}",
            line_number=12,
            explanation="Default arguments are evaluated once at class definition time"
        )
    ],
    identified_by_model="google/gemini-2.5-flash-lite",
    validated=False
)
```

#### Evidence

**Location:** `pydantic_models/models/evaluation.py`

Evidence supporting a misconception.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `code_snippet` | `str` | Exact problematic code |
| `line_number` | `int \| None` | Line where it occurs |
| `explanation` | `str` | Why this is a problem |

---

## Utility Functions

### OpenAI Integration

**Location:** `utils/openai_client.py`

#### evaluation_with_openai()

Get structured evaluation from OpenAI models.

**Signature:**
```python
def evaluation_with_openai(
    question: str,
    rubric: str,
    student_code: str,
    model: str = "gpt-4o"
) -> ModelEvaluation
```

**Parameters:**
- `question` (str) - Assignment question text
- `rubric` (str) - Rubric as JSON string
- `student_code` (str) - Student's code
- `model` (str) - OpenAI model name (default: "gpt-4o")

**Returns:**
- `ModelEvaluation` - Complete evaluation with metadata

**Supported Models:**
- `gpt-4o` - Best overall (recommended)
- `gpt-4-turbo`
- `gpt-4`
- `gpt-3.5-turbo` - Cheapest

**Example:**
```python
from utils.openai_client import evaluation_with_openai
import json

result = evaluation_with_openai(
    question="Implement a Calculator class",
    rubric=json.dumps(rubric_data),
    student_code=code,
    model="gpt-4o"
)

print(f"Score: {result.scores.percentage}%")
```

**Raises:**
- `APIError` - If OpenAI API fails
- `ValidationError` - If response doesn't match schema

---

### OpenRouter Integration

**Location:** `utils/openrouter_sdk.py`

#### get_structured_response()

Get structured response from any OpenRouter model.

**Signature:**
```python
def get_structured_response(
    messages: list[dict[str, str]],
    response_model: type[T],
    model: str = "google/gemini-2.5-flash-lite"
) -> T
```

**Parameters:**
- `messages` (list) - Chat messages list
  - Format: `[{"role": "user/assistant", "content": "..."}]`
- `response_model` (type) - Pydantic model class to parse into
- `model` (str) - OpenRouter model identifier

**Returns:**
- Instance of `response_model` with LLM response data

**Popular Models:**
```python
# Fast (cheap)
"google/gemini-2.5-flash-lite"
"google/gemini-flash-1.5"

# Balanced
"anthropic/claude-3-sonnet"
"google/gemini-pro"

# Best quality
"openai/gpt-4o"
"anthropic/claude-3-opus"

# Specialized
"moonshotai/kimi-k2-0905"
"meta-llama/llama-3-70b"
```

**Example:**
```python
from utils.openrouter_sdk import get_structured_response
from pydantic_models import LLMEvaluationResponse

messages = [
    {
        "role": "user",
        "content": f"Grade this code:\n{code}"
    }
]

response = get_structured_response(
    messages=messages,
    response_model=LLMEvaluationResponse,
    model="anthropic/claude-3-sonnet"
)

print(f"Total points: {response.scores.total_points_awarded}")
```

**Raises:**
- `APIError` - If OpenRouter API fails
- `ValidationError` - If response doesn't match schema

---

## Prompt Builders

Located in `prompts/` directory. Each returns a formatted string to send to LLM.

### Direct Prompt

**Location:** `prompts/direct_prompt.py`

Direct grading without intermediate steps.

**Function:**
```python
def build_direct_prompt(
    question: str,
    rubric: str,
    student_code: str
) -> str
```

**Example:**
```python
from prompts.direct_prompt import build_direct_prompt

prompt = build_direct_prompt(
    question="Implement a Cuboid class",
    rubric=json.dumps(rubric_data),
    student_code=student_code
)

# Use prompt with LLM
response = get_structured_response(
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,
    model="google/gemini-2.5-flash-lite"
)
```

**Best for:**
- Quick evaluations
- Simple assignments
- When you're confident in rubric clarity

---

### Reverse Prompt

**Location:** `prompts/reverse_prompt.py`

Generate ideal solution first, then compare.

**Function:**
```python
def build_reverse_prompt(
    question: str,
    rubric: str,
    student_code: str
) -> str
```

**Example:**
```python
from prompts.reverse_prompt import build_reverse_prompt

prompt = build_reverse_prompt(
    question="Implement a Cuboid class",
    rubric=json.dumps(rubric_data),
    student_code=student_code
)

response = get_structured_response(
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,
    model="anthropic/claude-3-sonnet"
)
```

**Best for:**
- Detailed analysis
- Complex assignments
- Finding subtle bugs

---

### EME Prompt

**Location:** `prompts/eme_prompt.py`

Ensemble Method Evaluation with structured 100-point scale.

**Function:**
```python
def build_eme_prompt(
    question: str,
    rubric: str,
    student_code: str
) -> str
```

**Example:**
```python
from prompts.eme_prompt import build_eme_prompt

prompt = build_eme_prompt(
    question="Implement a Cuboid class",
    rubric=json.dumps(rubric_data),
    student_code=student_code
)

response = get_structured_response(
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,
    model="openai/gpt-4o"
)
```

**Best for:**
- Research and detailed analysis
- High-stakes grading
- Ensemble comparisons

---

## Error Handling

### Pydantic ValidationError

```python
from pydantic import ValidationError

try:
    eval_doc = EvaluationDocument(**data)
except ValidationError as e:
    print(f"Validation failed with {len(e.errors())} errors")

    for error in e.errors():
        print(f"Field: {'.'.join(map(str, error['loc']))}")
        print(f"Error: {error['msg']}")
        print(f"Type: {error['type']}")
```

**Common errors:**
- Missing required field
- Wrong type (e.g., string instead of number)
- Value out of range
- Schema version mismatch

### API Errors

```python
from openai import APIError, RateLimitError

try:
    response = get_structured_response(...)
except RateLimitError:
    print("Rate limited. Wait and try again.")
except APIError as e:
    print(f"API error: {e}")
```

### Type Safety

Use type hints to catch errors early:

```python
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from pydantic_models import EvaluationDocument

def process_evaluation(eval_doc: "EvaluationDocument") -> float:
    return eval_doc.models["gpt-4o"].scores.percentage
```

---

## Complete Example

```python
import json
import uuid
from datetime import datetime, timezone

from pydantic_models import (
    Context, Submission, Rubric, RubricCategory, StudentFile,
    EvaluationDocument, ModelEvaluation, Config, LLMEvaluationResponse
)
from utils.openrouter_sdk import get_structured_response
from prompts.direct_prompt import build_direct_prompt

# 1. Load materials
with open("question.md") as f:
    question = f.read()

with open("rubric.json") as f:
    rubric_data = json.load(f)

with open("student_code.java") as f:
    code = f.read()

# 2. Build prompt
prompt = build_direct_prompt(
    question=question,
    rubric=json.dumps(rubric_data),
    student_code=code
)

# 3. Get evaluation
response = get_structured_response(
    messages=[{"role": "user", "content": prompt}],
    response_model=LLMEvaluationResponse,
    model="google/gemini-2.5-flash-lite"
)

# 4. Wrap in ModelEvaluation
model_eval = ModelEvaluation(
    model_name="google/gemini-2.5-flash-lite",
    provider="openrouter",
    run_id=f"run_{uuid.uuid4().hex[:8]}",
    config=Config(system_prompt_id="direct", rubric_prompt_id="v1"),
    scores=response.scores,
    category_scores=response.category_scores,
    feedback=response.feedback,
    misconceptions=response.misconceptions
)

# 5. Create evaluation document
eval_doc = EvaluationDocument(
    evaluation_id=f"eval_{uuid.uuid4().hex[:8]}",
    schema_version="1.0.0",
    created_at=datetime.now(timezone.utc),
    created_by="my_script.py",
    context=Context(
        course_id="CS101",
        course_name="Intro to CS",
        assignment_id=1,
        assignment_title="My Assignment",
        question_source_path="question.md",
        question_id="q1",
        question_title="Problem 1",
        rubric_source_path="rubric.json"
    ),
    submission=Submission(
        student_id="student_123",
        student_name="John Doe",
        submitted_at=datetime.now(timezone.utc),
        programming_language="Java",
        files=[StudentFile(path="Code.java", language="Java")]
    ),
    rubric=Rubric(
        rubric_id="rubric_v1",
        title="Assignment Rubric",
        total_points=rubric_data["totalPoints"],
        categories=[
            RubricCategory(
                category_id=cat["categoryId"],
                name=cat["name"],
                max_points=cat["maxPoints"],
                description=cat["description"]
            )
            for cat in rubric_data["categories"]
        ]
    ),
    models={"gemini": model_eval}
)

# 6. Save to JSON
with open("output.json", "w") as f:
    f.write(eval_doc.model_dump_json(indent=2))
```

---

**Next:** See [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) for system design details.
