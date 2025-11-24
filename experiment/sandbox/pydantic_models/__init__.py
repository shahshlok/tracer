"""
Pydantic models for ensemble evaluation system.

This package provides a complete set of models for representing student evaluations
graded by multiple LLMs, along with comprehensive comparison and ensemble analysis.

Main Components:
- EvaluationDocument: Root model representing a complete evaluation
- Context: Course, assignment, and question metadata
- Submission: Student work and files
- Rubric: Grading criteria and categories
- ModelEvaluation: Individual model grading results
- Comparison: Multi-model comparison and ensemble analysis
"""

__version__ = "1.0.0"

# Root evaluation document
# Comparison models
from .comparison import (
    AlternativeScores,
    CategoryAgreement,
    CategoryDifference,
    CategoryInsights,
    CategoryStatistics,
    Comparison,
    ComparisonMetadata,
    ConfidenceAnalysis,
    ConfidenceInterval95,
    ConfidenceRange,
    ConfidenceStats,
    ConfidenceStatsPerCategory,
    ConsistencyScore,
    EnsembleDecision,
    EnsembleQuality,
    Flags,
    HighConfidenceDisagreement,
    LargestCategoryDisagreement,
    LowConfidenceCategory,
    LowestConfidenceCategory,
    MisconceptionOverlap,
    MisconceptionSummary,
    ModelCharacteristics,
    MostAgreedCategory,
    MostControversialCategory,
    PairwiseComparison,
    ReliabilityMetrics,
    ScoreSummary,
    StrictnessRanking,
    ThresholdsConfig,
)

# Context models
from .context import Context
from .evaluation import EvaluationDocument

# Model evaluation models
from .models import (
    CategoryScore,
    Config,
    Evidence,
    Feedback,
    LLMEvaluationResponse,
    Misconception,
    ModelEvaluation,
    Scores,
)

# Rubric models
from .rubric import Rubric, RubricCategory

# Submission models
from .submission import StudentFile, Submission

__all__ = [
    # Root model
    "EvaluationDocument",
    # Context
    "Context",
    # Submission
    "Submission",
    "StudentFile",
    # Rubric
    "Rubric",
    "RubricCategory",
    # Model evaluation
    "ModelEvaluation",
    "LLMEvaluationResponse",
    "Config",
    "Scores",
    "CategoryScore",
    "Feedback",
    "Evidence",
    "Misconception",
    # Comparison - main
    "Comparison",
    # Comparison - score analysis
    "ScoreSummary",
    "PairwiseComparison",
    "CategoryDifference",
    "LargestCategoryDisagreement",
    "CategoryAgreement",
    "CategoryStatistics",
    "ConfidenceStats",
    "CategoryInsights",
    "MostControversialCategory",
    "MostAgreedCategory",
    "LowestConfidenceCategory",
    # Comparison - misconception analysis
    "MisconceptionSummary",
    "MisconceptionOverlap",
    # Comparison - confidence analysis
    "ConfidenceAnalysis",
    "ConfidenceRange",
    "ConfidenceStatsPerCategory",
    "HighConfidenceDisagreement",
    "LowConfidenceCategory",
    # Comparison - model characteristics
    "ModelCharacteristics",
    "StrictnessRanking",
    "ConsistencyScore",
    # Comparison - reliability
    "ReliabilityMetrics",
    "ConfidenceInterval95",
    # Comparison - ensemble
    "EnsembleDecision",
    "AlternativeScores",
    "EnsembleQuality",
    "Flags",
    "ComparisonMetadata",
    "ThresholdsConfig",
]
