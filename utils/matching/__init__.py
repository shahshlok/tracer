"""Matching module for comparing LLM detections against ground truth."""

from .classifier import MatchResult, classify_detection
from .fuzzy import fuzzy_match_misconception
from .semantic import semantic_match_misconception

__all__ = [
    "fuzzy_match_misconception",
    "semantic_match_misconception",
    "classify_detection",
    "MatchResult",
]
