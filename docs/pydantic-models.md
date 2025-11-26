# Pydantic Models Reference

This document provides a complete reference for all Pydantic models used in the EME CLI framework. The models enforce schema validation, type safety, and structured LLM outputs.

## Table of Contents

- [Model Hierarchy](#model-hierarchy)
- [Root Model: EvaluationDocument](#root-model-evaluationdocument)
- [Context Models](#context-models)
- [Submission Models](#submission-models)
- [Rubric Models](#rubric-models)
- [Model Evaluation](#model-evaluation)
- [Comparison Models](#comparison-models)
- [Usage Examples](#usage-examples)

## Model Hierarchy

```
EvaluationDocument
├── evaluation_id: str
├── schema_version: str (must match library version)
├── created_at: datetime
├── created_by: str
├── context: Context
│   ├── course_id: str
│   ├── course_name: str
│   ├── assignment_id: int
│   ├── assignment_title: str
│   ├── question_source_path: str
│   ├── question_id: str
│   ├── question_title: str
│   └── rubric_source_path: str
├── submission: Submission
│   ├── student_id: str
│   ├── student_name: str
│   ├── submitted_at: datetime
│   ├── programming_language: str
│   └── files: list[StudentFile]
├── rubric: Rubric
│   ├── rubric_id: str
│   ├── title: str
│   ├── total_points: float
│   └── categories: list[RubricCategory]
├── models: dict[str, ModelEvaluation]
│   └── ModelEvaluation
│       ├── model_name: str
│       ├── provider: str
│       ├── run_id: str
│       ├── config: Config
│       ├── scores: Scores
│       ├── category_scores: list[CategoryScore]
│       ├── feedback: Feedback
│       └── misconceptions: list[Misconception]
└── comparison: Comparison | None
    ├── score_summary: ScoreSummary
    ├── pairwise_differences: list[PairwiseComparison]
    ├── category_agreement: list[CategoryAgreement]
    └── ... (reliability, ensemble, flags)
```

## Root Model: EvaluationDocument

**Location:** `pydantic_models/evaluation.py`

The root model representing one student's answer to one question, graded by multiple models.

| Field | Type | Description |
|-------|------|-------------|
| `evaluation_id` | `str` | Unique ID (student + question + run) |
| `schema_version` | `str` | Must match library version (1.0.0) |
| `created_at` | `datetime` | When evaluation was generated |
| `created_by` | `str` | Pipeline/script that produced it |
| `context` | `Context` | Course, assignment, question metadata |
| `submission` | `Submission` | Student work and files |
| `rubric` | `Rubric` | Grading criteria |
| `models` | `dict[str, ModelEvaluation]` | Per-model results (key: model name) |
| `comparison` | `Comparison \| None` | Cross-model analysis |

**Validation:** Schema version must match `pydantic_models.__version__` (currently 1.0.0).

```python
from pydantic_models import EvaluationDocument

# Load from JSON
with open("student_evals/student_q1_eval.json") as f:
    doc = EvaluationDocument(**json.load(f))

# Access components
print(doc.context.course_name)
print(doc.models["google/gemini-2.5-flash-lite"].scores.percentage)
```

## Context Models

**Location:** `pydantic_models/context/models.py`

### Context

Educational context tying evaluation to course/assignment/question.

| Field | Type | Description |
|-------|------|-------------|
| `course_id` | `str` | Machine-friendly course ID |
| `course_name` | `str` | Human-readable course title |
| `assignment_id` | `int` | Assignment number |
| `assignment_title` | `str` | Human-readable assignment name |
| `question_source_path` | `str` | Path to question file |
| `question_id` | `str` | Question ID (e.g., "q1") |
| `question_title` | `str` | Human-readable question title |
| `rubric_source_path` | `str` | Path to rubric file |

## Submission Models

**Location:** `pydantic_models/submission/models.py`

### Submission

| Field | Type | Description |
|-------|------|-------------|
| `student_id` | `str` | Unique student identifier |
| `student_name` | `str` | Student's full name |
| `submitted_at` | `datetime` | Submission timestamp |
| `programming_language` | `str` | Main language (e.g., "java") |
| `files` | `list[StudentFile]` | Submitted files |

### StudentFile

| Field | Type | Description |
|-------|------|-------------|
| `path` | `str` | File path as submitted |
| `language` | `str` | Language of this file |

## Rubric Models

**Location:** `pydantic_models/rubric/models.py`

### Rubric

| Field | Type | Description |
|-------|------|-------------|
| `rubric_id` | `str` | Unique rubric version ID |
| `title` | `str` | Human-readable rubric name |
| `total_points` | `float` | Maximum possible score |
| `categories` | `list[RubricCategory]` | Grading categories |

### RubricCategory

| Field | Type | Description |
|-------|------|-------------|
| `category_id` | `str` | Stable ID for linking |
| `task` | `str` | Task name |
| `points` | `float` | Maximum points for category |
| `topic` | `str` | Concept being assessed |
| `description` | `str` | Detailed explanation |

## Model Evaluation

**Location:** `pydantic_models/models/evaluation.py`

### ModelEvaluation

Complete evaluation from a single grading model. Extends `LLMEvaluationResponse`.

| Field | Type | Description |
|-------|------|-------------|
| `model_name` | `str` | Model name with version |
| `provider` | `str` | Provider (openai, google, etc.) |
| `run_id` | `str` | Invocation ID for tracing |
| `config` | `Config` | Configuration settings |
| `scores` | `Scores` | Aggregate scores |
| `category_scores` | `list[CategoryScore]` | Per-category scores |
| `feedback` | `Feedback` | Human-readable feedback |
| `misconceptions` | `list[Misconception]` | Identified misconceptions |

### LLMEvaluationResponse

What the LLM returns (without metadata). Used as response model for Instructor.

### Scores

| Field | Type | Description |
|-------|------|-------------|
| `total_points_awarded` | `float` | Sum of category scores |
| `max_points` | `float` | Maximum possible |
| `percentage` | `float` | Percentage (validated) |

**Validation:** `percentage` must equal `total_points_awarded / max_points * 100`

### CategoryScore

| Field | Type | Description |
|-------|------|-------------|
| `task` | `str` | Task name from rubric |
| `points_awarded` | `float` | Points given |
| `max_points` | `float` | Maximum for category |
| `reasoning` | `str` | Justification for score |
| `confidence` | `float` | Model confidence (0-1) |
| `reasoning_tokens` | `int` | Token count in reasoning |

### Feedback

| Field | Type | Description |
|-------|------|-------------|
| `overall_comment` | `str` | Holistic feedback |
| `strengths` | `list[str]` | Bullet-style strengths |
| `areas_for_improvement` | `list[str]` | Areas to improve |

### Misconception

| Field | Type | Description |
|-------|------|-------------|
| `topic` | `str` | Canonical topic (Variables, Data Types, Constants, Reading input) |
| `task` | `str` | Related rubric task |
| `name` | `str` | Human-readable label |
| `description` | `str` | What the misconception reflects |
| `confidence` | `float` | Model confidence (0-1) |
| `evidence` | `list[Evidence]` | Code snippets showing issue |
| `validated_by` | `str \| None` | TA ID if manually confirmed |

### Evidence

| Field | Type | Description |
|-------|------|-------------|
| `source` | `str` | Origin (student_code, tests, etc.) |
| `file_path` | `str` | File containing snippet |
| `language` | `str` | Language of snippet |
| `snippet` | `str` | Actual code/text |
| `line_start` | `int` | Starting line number |
| `line_end` | `int` | Ending line number |
| `note` | `str` | Why this is evidence |

### Config

| Field | Type | Description |
|-------|------|-------------|
| `system_prompt_id` | `str` | System prompt template ID |
| `rubric_prompt_id` | `str` | Grading prompt template ID |

## Comparison Models

**Location:** `pydantic_models/comparison/`

The comparison models are organized into submodules:

### Comparison (Main)

**File:** `models.py`

| Field | Type | Description |
|-------|------|-------------|
| `score_summary` | `ScoreSummary` | Aggregate statistics |
| `pairwise_differences` | `list[PairwiseComparison]` | Model-vs-model diffs |
| `category_agreement` | `list[CategoryAgreement]` | Per-category agreement |
| `category_insights` | `CategoryInsights` | High-level insights |
| `misconception_summary` | `MisconceptionSummary \| None` | Misconception comparison |
| `confidence_analysis` | `ConfidenceAnalysis \| None` | Confidence patterns |
| `model_characteristics` | `ModelCharacteristics \| None` | Grading tendencies |
| `reliability_metrics` | `ReliabilityMetrics \| None` | ICC, Pearson, etc. |
| `ensemble_decision` | `EnsembleDecision \| None` | Final grade recommendation |
| `ensemble_quality` | `EnsembleQuality \| None` | Ensemble effectiveness |
| `flags` | `Flags \| None` | Review/research flags |
| `metadata` | `ComparisonMetadata \| None` | Versioning info |

### Score Analysis (`score_analysis.py`)

- **ScoreSummary**: Mean, median, std_dev, variance, CV, min, max, range
- **PairwiseComparison**: Model A vs B differences
- **CategoryDifference**: Per-category point differences
- **CategoryAgreement**: Agreement level (perfect/high/medium/low)
- **CategoryInsights**: Most controversial, most agreed categories

### Confidence Analysis (`confidence_analysis.py`)

- **ConfidenceAnalysis**: Overall and per-category confidence patterns
- **ModelCharacteristics**: Strictness ranking, consistency scores
- **StrictnessRanking**: Models ordered by average score

### Reliability (`reliability.py`)

- **ReliabilityMetrics**: Pearson, Spearman, ICC, Cohen's Kappa, Krippendorff's alpha
- **ConfidenceInterval95**: 95% CI for true score

### Ensemble (`ensemble.py`)

- **EnsembleDecision**: Recommended score (mean/median/weighted)
- **EnsembleQuality**: Diversity, redundancy, complementarity
- **Flags**: Review urgency, research interest
- **ComparisonMetadata**: Computation versioning

## Usage Examples

### Loading an Evaluation

```python
import json
from pydantic_models import EvaluationDocument

with open("student_evals/Chen_Wei_200023_q1_eval.json") as f:
    eval_doc = EvaluationDocument(**json.load(f))

# Access context
print(f"Course: {eval_doc.context.course_name}")
print(f"Question: {eval_doc.context.question_id}")

# Access submission
print(f"Student: {eval_doc.submission.student_name}")
print(f"Files: {[f.path for f in eval_doc.submission.files]}")

# Access model scores
for model_name, model_eval in eval_doc.models.items():
    print(f"{model_name}: {model_eval.scores.percentage}%")
```

### Creating a Model Evaluation

```python
from pydantic_models.models import (
    ModelEvaluation, Config, Scores, CategoryScore,
    Feedback, Misconception, Evidence
)

model_eval = ModelEvaluation(
    model_name="gpt-4o",
    provider="openai",
    run_id="run_abc123",
    config=Config(
        system_prompt_id="grading_v1",
        rubric_prompt_id="q1_rubric_v1"
    ),
    scores=Scores(
        total_points_awarded=3.5,
        max_points=4.0,
        percentage=87.5
    ),
    category_scores=[
        CategoryScore(
            task="Reading input using Scanner",
            points_awarded=1.0,
            max_points=1.0,
            reasoning="Correctly uses Scanner with nextDouble()",
            confidence=0.95,
            reasoning_tokens=15
        ),
        # ... more categories
    ],
    feedback=Feedback(
        overall_comment="Good implementation with minor issues.",
        strengths=["Correct variable types", "Proper input handling"],
        areas_for_improvement=["Consider closing Scanner"]
    ),
    misconceptions=[]
)
```

### Validation

```python
from pydantic import ValidationError

try:
    eval_doc = EvaluationDocument(**invalid_data)
except ValidationError as e:
    print(f"Validation errors: {e}")
    for error in e.errors():
        print(f"  - {error['loc']}: {error['msg']}")
```

### JSON Serialization

```python
# To JSON string
json_str = eval_doc.model_dump_json(indent=2)

# To dict
data = eval_doc.model_dump()

# From JSON
eval_doc = EvaluationDocument.model_validate_json(json_str)
```

## Design Decisions

### Extra Fields Forbidden

All models use `ConfigDict(extra="forbid")` to catch typos and schema drift:

```python
class Scores(BaseModel):
    model_config = ConfigDict(extra="forbid")
    # Fields with unknown keys will raise ValidationError
```

### Schema Versioning

The root `EvaluationDocument` validates that `schema_version` matches the library version, ensuring data/code compatibility.

### Canonical Topics

Misconceptions must use one of 4 canonical topics:
1. Variables
2. Data Types
3. Constants
4. Reading input from the keyboard

The `MisconceptionAnalyzer` normalizes LLM-generated topics to these canonical values.

## Related Documentation

- [Architecture](architecture.md) - System design overview
- [Grading Workflow](grading-workflow.md) - How evaluations are created
- [Misconception Analysis](misconception-analysis.md) - How misconceptions are processed
