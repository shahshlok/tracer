"""Comparison models for multi-model evaluation analysis."""

from .confidence_analysis import (
    ConfidenceAnalysis,
    ConfidenceRange,
    ConfidenceStatsPerCategory,
    ConsistencyScore,
    HighConfidenceDisagreement,
    LowConfidenceCategory,
    ModelCharacteristics,
    StrictnessRanking,
)
from .ensemble import (
    AlternativeScores,
    ComparisonMetadata,
    EnsembleDecision,
    EnsembleQuality,
    Flags,
    ThresholdsConfig,
)
from .misconception_analysis import MisconceptionOverlap, MisconceptionSummary
from .models import Comparison
from .reliability import ConfidenceInterval95, ReliabilityMetrics
from .score_analysis import (
    CategoryAgreement,
    CategoryDifference,
    CategoryInsights,
    CategoryStatistics,
    ConfidenceStats,
    LargestCategoryDisagreement,
    LowestConfidenceCategory,
    MostAgreedCategory,
    MostControversialCategory,
    PairwiseComparison,
    ScoreSummary,
)

__all__ = [
    # Main comparison model
    "Comparison",
    # Score analysis
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
    # Misconception analysis
    "MisconceptionSummary",
    "MisconceptionOverlap",
    # Confidence analysis
    "ConfidenceAnalysis",
    "ConfidenceRange",
    "ConfidenceStatsPerCategory",
    "HighConfidenceDisagreement",
    "LowConfidenceCategory",
    # Model characteristics
    "ModelCharacteristics",
    "StrictnessRanking",
    "ConsistencyScore",
    # Reliability
    "ReliabilityMetrics",
    "ConfidenceInterval95",
    # Ensemble
    "EnsembleDecision",
    "AlternativeScores",
    "EnsembleQuality",
    "Flags",
    "ComparisonMetadata",
    "ThresholdsConfig",
]
