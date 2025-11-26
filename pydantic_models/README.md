# Pydantic Models for Ensemble Evaluation

This package provides a complete, schema-first set of Pydantic models for representing student evaluations graded by multiple LLMs, together with comparison and ensemble analysis structures.

> **Full Documentation:** See [docs/pydantic-models.md](../docs/pydantic-models.md) for complete model reference with examples.

## Quick Reference

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

## Basic Usage

```python
from pydantic_models import EvaluationDocument

# Load evaluation from JSON
with open('evaluation.json') as f:
    evaluation = EvaluationDocument(**json.load(f))

# Access scores
for model_name, model_eval in evaluation.models.items():
    print(f"{model_name}: {model_eval.scores.percentage}%")

# Access misconceptions
for model_name, model_eval in evaluation.models.items():
    for misc in model_eval.misconceptions:
        print(f"  {misc.name} ({misc.confidence})")
```

## Model Hierarchy

- **EvaluationDocument** - Root document
  - **Context** - Course/assignment/question metadata
  - **Submission** - Student files and info
  - **Rubric** - Grading criteria
  - **ModelEvaluation** - Per-model results
    - Scores, CategoryScores, Feedback, Misconceptions
  - **Comparison** - Cross-model analysis (optional)

## Version

Current version: **1.0.0**

Schema version is validated on load to ensure data/code compatibility.

## Related Documentation

- [Pydantic Models Reference](../docs/pydantic-models.md) - Complete documentation
- [Architecture](../docs/architecture.md) - System design
- [Grading Workflow](../docs/grading-workflow.md) - How evaluations are created
