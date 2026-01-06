"""Matching module for comparing LLM detections against ground truth.

This project currently uses semantic embedding matching as the primary scientific
instrument. Other matchers (e.g., fuzzy/hybrid) are intentionally optional and
may not be present in all configurations.
"""

from .semantic import semantic_match_misconception

__all__ = ["semantic_match_misconception"]
