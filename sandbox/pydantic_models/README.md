# Pydantic Models for Ensemble Evaluation

This package provides a complete, schema-first set of Pydantic models for representing student evaluations graded by multiple LLMs, together with comparison and ensemble analysis structures.

## Structure

```
pydantic_models/
├── evaluation.py              # Root EvaluationDocument model
├── context/                   # Course, assignment, question metadata
├── submission/                # Student work and files
├── rubric/                    # Grading criteria and categories
├── models/                    # Individual model evaluation results
└── comparison/                # Multi-model comparison and ensemble analysis
    ├── score_analysis.py      # Score comparison models
    ├── misconception_analysis.py  # Misconception detection models
    ├── confidence_analysis.py # Confidence and model characteristics
    ├── reliability.py         # Inter-rater reliability metrics
    └── ensemble.py            # Ensemble decisions and quality
```

## Usage

### Basic Import

```python
from pydantic_models import EvaluationDocument

# Load evaluation from JSON
with open('evaluation.json') as f:
    eval_data = json.load(f)

evaluation = EvaluationDocument(**eval_data)
```

### Accessing Specific Components

```python
# Access context
course_id = evaluation.context.course_id
question_title = evaluation.context.question_title

# Access submission
student_name = evaluation.submission.student_name
files = evaluation.submission.files

# Access rubric
total_points = evaluation.rubric.total_points
categories = evaluation.rubric.categories

# Access model evaluations
for model_name, model_eval in evaluation.models.items():
    print(f"{model_name}: {model_eval.scores.percentage}%")

# Access comparison and ensemble
ensemble_score = evaluation.comparison.ensemble_decision.recommended_score
reliability = evaluation.comparison.reliability_metrics.intraclass_correlation_icc
```

### Creating Model Evaluation Responses

```python
from pydantic_models import (
    ModelEvaluation, Config, Scores, CategoryScore,
    Feedback, Misconception, Evidence
)

# Create a model evaluation programmatically
model_eval = ModelEvaluation(
    model_name="gpt-4o",
    provider="openai",
    model_version="2024-11-01",
    run_id="run_001",
    config=Config(
        system_prompt_id="grading_v1",
        rubric_prompt_id="rubric_v1"
    ),
    scores=Scores(
        total_points_awarded=85,
        max_points=100,
        percentage=85.0
    ),
    category_scores=[...],
    feedback=Feedback(...),
    misconceptions=[...]
)
```

### Validation

All models include Pydantic validation:

```python
from pydantic import ValidationError

try:
    evaluation = EvaluationDocument(**data)
except ValidationError as e:
    print(f"Validation errors: {e}")
```

## Model Overview

### Root Model
- **EvaluationDocument**: Complete evaluation with all sections

### Context
- **Context**: Course, assignment, question metadata

### Submission
- **Submission**: Student work and metadata
- **StudentFile**: Individual submitted files

### Rubric
- **Rubric**: Grading criteria
- **RubricCategory**: Individual rubric categories

### Model Evaluation
- **ModelEvaluation**: Complete per-model grading result
- **Config**: Model configuration
- **Scores**: Aggregate scores
- **CategoryScore**: Per-category scores
- **Feedback**: Strengths and areas for improvement
- **Evidence**: Evidence for misconceptions
- **Misconception**: Identified misconceptions

### Comparison (Ensemble Analysis)
- **Comparison**: Main comparison model
- **ScoreSummary**: Score statistics across models
- **PairwiseComparison**: Model-vs-model differences
- **CategoryAgreement**: Per-category agreement analysis
- **MisconceptionSummary**: Misconception detection comparison
- **ConfidenceAnalysis**: Confidence patterns
- **ModelCharacteristics**: Grading tendencies
- **ReliabilityMetrics**: Inter-rater reliability (ICC, Pearson, etc.)
- **EnsembleDecision**: Final grade recommendation
- **EnsembleQuality**: Quality assessment
- **Flags**: Automated review recommendations

## Features

- **Type Safety**: Full type hints for IDE support
- **Validation**: Automatic validation of all fields
- **JSON Serialization**: Easy conversion to/from JSON
- **Documentation**: Comprehensive field descriptions
- **Extensibility**: Easy to extend with new models

## Version

Current version: 1.0.0
