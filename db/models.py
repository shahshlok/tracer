"""
Data models and structures for EME Testing database.

This module defines the data structures used throughout the database system,
including evaluation data, metadata, and validation results.
"""

from dataclasses import dataclass, field
from typing import Dict, List, Optional, Any
from pathlib import Path
import json


@dataclass
class ModelResult:
    """Result from a single grading model."""
    total_score: float
    max_possible_score: float
    overall_feedback: str

    def to_dict(self) -> Dict[str, Any]:
        return {
            "total_score": self.total_score,
            "max_possible_score": self.max_possible_score,
            "overall_feedback": self.overall_feedback
        }


@dataclass
class ModelMetrics:
    """Metrics for a single grading model."""
    total: float
    max: float
    pct: float

    def to_dict(self) -> Dict[str, Any]:
        return {
            "total": self.total,
            "max": self.max,
            "pct": self.pct
        }


@dataclass
class ComparisonMetrics:
    """Metrics comparing two grading models."""
    gpt5_nano: ModelMetrics
    gpt_oss_120b: ModelMetrics
    avg_pct: float
    diff_pct: float
    flag: str
    comment: str

    def to_dict(self) -> Dict[str, Any]:
        return {
            "gpt5_nano": self.gpt5_nano.to_dict(),
            "gpt_oss_120b": self.gpt_oss_120b.to_dict(),
            "avg_pct": self.avg_pct,
            "diff_pct": self.diff_pct,
            "flag": self.flag,
            "comment": self.comment
        }


@dataclass
class EvaluationData:
    """Complete evaluation data for a single student."""
    student: str
    gpt5_nano_result: ModelResult
    gpt_oss_120b_result: ModelResult
    metrics: ComparisonMetrics

    def to_dict(self) -> Dict[str, Any]:
        return {
            "student": self.student,
            "gpt5_nano_result": self.gpt5_nano_result.to_dict(),
            "gpt_oss_120b_result": self.gpt_oss_120b_result.to_dict(),
            "metrics": self.metrics.to_dict()
        }

    def to_json(self) -> str:
        """Convert to JSON string."""
        return json.dumps(self.to_dict())

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'EvaluationData':
        """Create EvaluationData from dictionary."""
        return cls(
            student=data['student'],
            gpt5_nano_result=ModelResult(**data['gpt5_nano_result']),
            gpt_oss_120b_result=ModelResult(**data['gpt_oss_120b_result']),
            metrics=ComparisonMetrics(
                gpt5_nano=ModelMetrics(**data['metrics']['gpt5_nano']),
                gpt_oss_120b=ModelMetrics(**data['metrics']['gpt_oss_120b']),
                avg_pct=data['metrics']['avg_pct'],
                diff_pct=data['metrics']['diff_pct'],
                flag=data['metrics']['flag'],
                comment=data['metrics']['comment']
            )
        )


@dataclass
class RunMetadata:
    """Metadata for an evaluation run."""
    assignment_type: Optional[str] = None
    rubric_version: Optional[str] = None
    model_versions: Optional[Dict[str, str]] = None
    experiment_id: Optional[str] = None
    notes: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        """Convert to dictionary, excluding None values."""
        return {
            k: v for k, v in {
                "assignment_type": self.assignment_type,
                "rubric_version": self.rubric_version,
                "model_versions": self.model_versions,
                "experiment_id": self.experiment_id,
                "notes": self.notes
            }.items() if v is not None
        }

    def to_json(self) -> str:
        """Convert to JSON string."""
        return json.dumps(self.to_dict())

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'RunMetadata':
        """Create RunMetadata from dictionary."""
        return cls(
            assignment_type=data.get('assignment_type'),
            rubric_version=data.get('rubric_version'),
            model_versions=data.get('model_versions'),
            experiment_id=data.get('experiment_id'),
            notes=data.get('notes')
        )


@dataclass
class ValidationResult:
    """Result of validation with detailed error information."""
    is_valid: bool
    errors: List[str] = field(default_factory=list)
    warnings: List[str] = field(default_factory=list)

    def __bool__(self):
        return self.is_valid

    def get_summary(self) -> str:
        """Get a human-readable summary of validation results."""
        if self.is_valid:
            msg = "✓ Validation passed"
            if self.warnings:
                msg += f" with {len(self.warnings)} warning(s)"
            return msg
        else:
            return f"✗ Validation failed with {len(self.errors)} error(s)"

    def get_details(self) -> str:
        """Get detailed validation report."""
        lines = [self.get_summary()]

        if self.errors:
            lines.append("\nErrors:")
            for i, error in enumerate(self.errors, 1):
                lines.append(f"  {i}. {error}")

        if self.warnings:
            lines.append("\nWarnings:")
            for i, warning in enumerate(self.warnings, 1):
                lines.append(f"  {i}. {warning}")

        return "\n".join(lines)


# Schema paths
def get_schema_dir() -> Path:
    """Get the path to the db schema directory."""
    return Path(__file__).parent


def get_sql_schema_path() -> Path:
    """Get the path to the SQL schema file."""
    return get_schema_dir() / "schema.sql"


def get_json_schema_path() -> Path:
    """Get the path to the JSON schema file."""
    # JSON schema is in the project root
    return get_schema_dir().parent / "evaluation_schema.json"
