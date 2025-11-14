"""
EME Testing Database Module

A modular SQLite-based storage and analysis system for student evaluation data
using JSONB format with comprehensive validation.

This package provides:
- Full JSON storage using SQLite's JSONB format (SQLite 3.51.0+)
- Comprehensive validation before data insertion
- Fast querying via indexed virtual columns
- Full-text search for finding common misconceptions
- Batch analysis capabilities for comparing runs over time
"""

__version__ = "1.0.0"

# Import main classes for convenient access
from .models import EvaluationData, RunMetadata, ValidationResult
from .validator import EvaluationValidator, validate_json_file
from .loader import EvaluationLoader, LoadStats
from .analyzer import EvaluationAnalyzer

__all__ = [
    # Data Models
    "EvaluationData",
    "RunMetadata",
    "ValidationResult",

    # Validator
    "EvaluationValidator",
    "validate_json_file",

    # Loader
    "EvaluationLoader",
    "LoadStats",

    # Analyzer
    "EvaluationAnalyzer",
]
