"""
Validation helpers for evaluation JSON files.

This is a lean extraction of the previous validation logic that lived under
the old database implementation. It provides:

- ValidationResult: simple result container with pretty-print helpers.
- validate_json_file(): validate a JSON file of evaluations against the
  JSON schema and basic business rules.
"""

from dataclasses import dataclass, field
from pathlib import Path
from typing import Any, Dict, List, Optional, Tuple
import json

try:
    import jsonschema
    from jsonschema import validate as jsonschema_validate, ValidationError

    JSONSCHEMA_AVAILABLE = True
except ImportError:
    JSONSCHEMA_AVAILABLE = False


SCHEMA_FILENAME = "evaluation_schema.json"


@dataclass
class ValidationResult:
    """Result of validation with detailed error information."""

    is_valid: bool
    errors: List[str] = field(default_factory=list)
    warnings: List[str] = field(default_factory=list)

    def __bool__(self) -> bool:  # pragma: no cover - convenience
        return self.is_valid

    def get_summary(self) -> str:
        """Get a human-readable summary of validation results."""
        if self.is_valid:
            msg = "✓ Validation passed"
            if self.warnings:
                msg += f" with {len(self.warnings)} warning(s)"
            return msg

        return f"✗ Validation failed with {len(self.errors)} error(s)"

    def get_details(self) -> str:
        """Get detailed validation report."""
        lines: List[str] = [self.get_summary()]

        if self.errors:
            lines.append("\nErrors:")
            for i, error in enumerate(self.errors, 1):
                lines.append(f"  {i}. {error}")

        if self.warnings:
            lines.append("\nWarnings:")
            for i, warning in enumerate(self.warnings, 1):
                lines.append(f"  {i}. {warning}")

        return "\n".join(lines)


def _get_default_schema_path() -> Path:
    """Return the default path to the JSON schema."""
    # Assume schema lives at project root alongside this utils package.
    return Path(__file__).resolve().parent.parent / SCHEMA_FILENAME


def _load_schema(schema_path: Optional[str]) -> Dict[str, Any]:
    """Load the JSON schema from a given path or the default location."""
    if schema_path is None:
        path = _get_default_schema_path()
    else:
        path = Path(schema_path)

    if not path.exists():
        raise FileNotFoundError(f"Schema file not found: {path}")

    with path.open("r") as f:
        return json.load(f)


def _validate_record_structure(
    record: Dict[str, Any],
    schema: Dict[str, Any],
) -> Tuple[List[str], List[str]]:
    """Run JSON Schema + basic business checks for a single record."""
    errors: List[str] = []
    warnings: List[str] = []

    # JSON Schema validation (if available)
    if JSONSCHEMA_AVAILABLE:
        try:
            jsonschema_validate(instance=record, schema=schema)
        except ValidationError as e:  # pragma: no cover - thin wrapper
            errors.append(f"Schema validation failed: {e.message}")
            if e.path:
                path_str = ".".join(str(p) for p in e.path)
                errors.append(f"  Location: {path_str}")
    else:
        warnings.append(
            "jsonschema package not available - skipping schema validation"
        )

    # Basic business rules reused from the previous implementation

    # Check student format (non-empty)
    student = record.get("student")
    if not isinstance(student, str) or not student.strip():
        warnings.append("Missing or empty 'student' field")

    metrics = record.get("metrics", {})

    # Validate avg_pct and diff_pct if model metrics available.
    # Metrics may legitimately contain nulls when one model failed to return
    # a usable score; in that case we skip consistency checks but still
    # enforce that any present percentage values are numeric.
    gpt5 = metrics.get("gpt5_nano")
    oss = metrics.get("gpt_oss_120b")

    if isinstance(gpt5, dict) and isinstance(oss, dict):
        def _as_optional_float(value: Any) -> Optional[float]:
            if value is None:
                return None
            try:
                return float(value)
            except (TypeError, ValueError):
                errors.append("Metrics pct values must be numeric")
                return None

        gpt5_pct = _as_optional_float(gpt5.get("pct"))
        oss_pct = _as_optional_float(oss.get("pct"))
        avg_pct = _as_optional_float(metrics.get("avg_pct"))
        diff_pct = _as_optional_float(metrics.get("diff_pct"))

        # If numeric conversion failed, do not attempt further consistency checks.
        if not any("Metrics pct values must be numeric" in e for e in errors):
            if gpt5_pct is not None and oss_pct is not None and avg_pct is not None:
                expected_avg = (gpt5_pct + oss_pct) / 2
                if abs(expected_avg - avg_pct) > 0.1:
                    errors.append(
                        f"avg_pct ({avg_pct}) doesn't match calculated average "
                        f"({expected_avg:.2f})"
                    )

            if gpt5_pct is not None and oss_pct is not None and diff_pct is not None:
                expected_diff = abs(gpt5_pct - oss_pct)
                if abs(expected_diff - abs(diff_pct)) > 0.1:
                    errors.append(
                        f"diff_pct ({diff_pct}) doesn't match calculated "
                        f"difference ({expected_diff:.2f})"
                    )

    return errors, warnings


def validate_json_file(
    file_path: str,
    schema_path: Optional[str] = None,
) -> ValidationResult:
    """
    Validate an entire JSON file containing evaluation data.

    - Ensures the file exists and is valid JSON.
    - Ensures the root is a list of records.
    - Validates each record against the JSON schema and basic checks.
    """
    try:
        schema = _load_schema(schema_path)
    except FileNotFoundError as e:
        return ValidationResult(is_valid=False, errors=[str(e)], warnings=[])

    try:
        with open(file_path, "r") as f:
            data = json.load(f)
    except json.JSONDecodeError as e:
        return ValidationResult(
            is_valid=False,
            errors=[f"Invalid JSON in file: {e}"],
            warnings=[],
        )
    except FileNotFoundError:
        return ValidationResult(
            is_valid=False,
            errors=[f"File not found: {file_path}"],
            warnings=[],
        )

    if not isinstance(data, list):
        return ValidationResult(
            is_valid=False,
            errors=["JSON file must contain an array of evaluations"],
            warnings=[],
        )

    all_errors: List[str] = []
    all_warnings: List[str] = []

    for i, record in enumerate(data):
        if not isinstance(record, dict):
            all_errors.append(f"Record {i + 1} is not an object")
            continue

        rec_errors, rec_warnings = _validate_record_structure(record, schema)

        if rec_errors:
            all_errors.append(
                f"Record {i + 1} (student: {record.get('student', 'unknown')}):"
            )
            all_errors.extend(f"  - {err}" for err in rec_errors)

        for warn in rec_warnings:
            all_warnings.append(f"Record {i + 1}: {warn}")

    is_valid = len(all_errors) == 0
    return ValidationResult(is_valid=is_valid, errors=all_errors, warnings=all_warnings)

