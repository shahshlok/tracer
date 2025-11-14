"""
Validation module for EME Testing evaluation data.

This module provides comprehensive validation for evaluation JSON data before
database insertion, ensuring data integrity beyond SQLite's basic json_valid() check.
"""

import json
from pathlib import Path
from typing import Dict, List, Tuple, Any, Optional

from .models import ValidationResult, get_json_schema_path

try:
    import jsonschema
    from jsonschema import validate, ValidationError
    JSONSCHEMA_AVAILABLE = True
except ImportError:
    JSONSCHEMA_AVAILABLE = False
    print("Warning: jsonschema package not found. Install with: uv add jsonschema")


class EvaluationValidator:
    """Validator for student evaluation data."""

    def __init__(self, schema_path: Optional[str] = None):
        """
        Initialize validator with JSON schema.

        Args:
            schema_path: Path to JSON schema file. Defaults to evaluation_schema.json
                        in the project root.
        """
        if schema_path is None:
            schema_path = get_json_schema_path()
        else:
            schema_path = Path(schema_path)

        if not schema_path.exists():
            raise FileNotFoundError(f"Schema file not found: {schema_path}")

        with open(schema_path, 'r') as f:
            self.schema = json.load(f)

        self.schema_path = schema_path

    def validate_evaluation(self, evaluation_data: Dict[str, Any]) -> ValidationResult:
        """
        Validate a single evaluation record.

        Args:
            evaluation_data: Dictionary containing evaluation data

        Returns:
            ValidationResult with validation status and any errors/warnings
        """
        errors = []
        warnings = []

        # 1. JSON Schema validation (if jsonschema is available)
        if JSONSCHEMA_AVAILABLE:
            try:
                validate(instance=evaluation_data, schema=self.schema)
            except ValidationError as e:
                errors.append(f"Schema validation failed: {e.message}")
                # Add path information for better debugging
                if e.path:
                    path_str = ".".join(str(p) for p in e.path)
                    errors.append(f"  Location: {path_str}")
        else:
            warnings.append("jsonschema package not available - skipping schema validation")

        # 2. Additional business logic validations

        # Check student name format
        if 'student' in evaluation_data:
            student = evaluation_data['student']
            if not self._validate_student_name(student):
                warnings.append(
                    f"Student name '{student}' doesn't match expected format "
                    "(LastName_FirstName_ID)"
                )

        # Check score consistency
        if 'gpt5_nano_result' in evaluation_data:
            nano = evaluation_data['gpt5_nano_result']
            if not self._validate_score_consistency(nano, 'gpt5_nano_result'):
                errors.append("GPT-5 Nano score exceeds maximum possible score")

        if 'gpt_oss_120b_result' in evaluation_data:
            oss = evaluation_data['gpt_oss_120b_result']
            if not self._validate_score_consistency(oss, 'gpt_oss_120b_result'):
                errors.append("GPT-OSS-120B score exceeds maximum possible score")

        # Check metrics consistency
        if 'metrics' in evaluation_data:
            metrics = evaluation_data['metrics']

            # Validate percentage calculations
            if not self._validate_metrics_calculations(evaluation_data):
                errors.append("Metrics calculations are inconsistent with raw scores")

            # Validate avg_pct calculation
            if 'gpt5_nano' in metrics and 'gpt_oss_120b' in metrics:
                expected_avg = (metrics['gpt5_nano']['pct'] + metrics['gpt_oss_120b']['pct']) / 2
                actual_avg = metrics.get('avg_pct', 0)
                if abs(expected_avg - actual_avg) > 0.1:  # Allow small floating-point error
                    errors.append(
                        f"avg_pct ({actual_avg}) doesn't match calculated average "
                        f"({expected_avg:.2f})"
                    )

            # Validate diff_pct calculation
            if 'gpt5_nano' in metrics and 'gpt_oss_120b' in metrics:
                expected_diff = abs(metrics['gpt5_nano']['pct'] - metrics['gpt_oss_120b']['pct'])
                actual_diff = abs(metrics.get('diff_pct', 0))
                if abs(expected_diff - actual_diff) > 0.1:  # Allow small floating-point error
                    errors.append(
                        f"diff_pct ({actual_diff}) doesn't match calculated difference "
                        f"({expected_diff:.2f})"
                    )

            # Validate flag consistency
            if 'diff_pct' in metrics and 'flag' in metrics:
                if not self._validate_flag_consistency(metrics):
                    warnings.append(
                        f"Flag '{metrics['flag']}' might not match diff_pct "
                        f"({metrics['diff_pct']})"
                    )

        # Check for empty or missing feedback
        if 'gpt5_nano_result' in evaluation_data:
            if not evaluation_data['gpt5_nano_result'].get('overall_feedback', '').strip():
                warnings.append("GPT-5 Nano feedback is empty")

        if 'gpt_oss_120b_result' in evaluation_data:
            if not evaluation_data['gpt_oss_120b_result'].get('overall_feedback', '').strip():
                warnings.append("GPT-OSS-120B feedback is empty")

        return ValidationResult(
            is_valid=len(errors) == 0,
            errors=errors,
            warnings=warnings
        )

    def validate_evaluation_batch(
        self,
        evaluations: List[Dict[str, Any]],
        fail_fast: bool = False
    ) -> Tuple[List[ValidationResult], int, int]:
        """
        Validate a batch of evaluations.

        Args:
            evaluations: List of evaluation dictionaries
            fail_fast: If True, stop on first error

        Returns:
            Tuple of (results_list, valid_count, invalid_count)
        """
        results = []
        valid_count = 0
        invalid_count = 0

        for i, evaluation in enumerate(evaluations):
            result = self.validate_evaluation(evaluation)
            results.append(result)

            if result.is_valid:
                valid_count += 1
            else:
                invalid_count += 1
                if fail_fast:
                    print(f"Validation failed at record {i+1}/{len(evaluations)}")
                    print(result.get_details())
                    break

        return results, valid_count, invalid_count

    def _validate_student_name(self, name: str) -> bool:
        """Check if student name follows expected format: LastName_FirstName_ID"""
        parts = name.split('_')
        return len(parts) == 3 and all(part.strip() for part in parts)

    def _validate_score_consistency(self, result: Dict[str, Any], model_name: str) -> bool:
        """Check if total_score <= max_possible_score"""
        total = result.get('total_score', 0)
        max_score = result.get('max_possible_score', 0)
        return total <= max_score

    def _validate_metrics_calculations(self, evaluation_data: Dict[str, Any]) -> bool:
        """Validate that metrics match the raw result scores"""
        metrics = evaluation_data.get('metrics', {})

        # Check GPT-5 Nano
        nano_result = evaluation_data.get('gpt5_nano_result', {})
        nano_metrics = metrics.get('gpt5_nano', {})

        if nano_result and nano_metrics:
            if nano_metrics.get('total') != nano_result.get('total_score'):
                return False
            if nano_metrics.get('max') != nano_result.get('max_possible_score'):
                return False
            # Check percentage calculation
            expected_pct = (nano_result.get('total_score', 0) /
                          nano_result.get('max_possible_score', 1)) * 100
            if abs(nano_metrics.get('pct', 0) - expected_pct) > 0.1:
                return False

        # Check GPT-OSS-120B
        oss_result = evaluation_data.get('gpt_oss_120b_result', {})
        oss_metrics = metrics.get('gpt_oss_120b', {})

        if oss_result and oss_metrics:
            if oss_metrics.get('total') != oss_result.get('total_score'):
                return False
            if oss_metrics.get('max') != oss_result.get('max_possible_score'):
                return False
            # Check percentage calculation
            expected_pct = (oss_result.get('total_score', 0) /
                          oss_result.get('max_possible_score', 1)) * 100
            if abs(oss_metrics.get('pct', 0) - expected_pct) > 0.1:
                return False

        return True

    def _validate_flag_consistency(self, metrics: Dict[str, Any]) -> bool:
        """
        Check if flag emoji is consistent with diff_pct.

        Assumes:
        - 🚩 (red flag) = models disagree (high diff_pct)
        - ✅ (checkmark) = models agree (low diff_pct)
        """
        diff_pct = abs(metrics.get('diff_pct', 0))
        flag = metrics.get('flag', '')

        # These thresholds are assumptions - adjust based on your criteria
        AGREEMENT_THRESHOLD = 10.0  # diff_pct <= 10 means agreement

        if diff_pct > AGREEMENT_THRESHOLD and flag == '✅':
            return False
        if diff_pct <= AGREEMENT_THRESHOLD and flag == '🚩':
            return False

        return True


def validate_json_file(file_path: str, schema_path: Optional[str] = None) -> ValidationResult:
    """
    Validate an entire JSON file containing evaluation data.

    Args:
        file_path: Path to JSON file containing array of evaluations
        schema_path: Optional path to JSON schema file

    Returns:
        ValidationResult aggregating all evaluations in the file
    """
    validator = EvaluationValidator(schema_path)

    try:
        with open(file_path, 'r') as f:
            data = json.load(f)
    except json.JSONDecodeError as e:
        return ValidationResult(
            is_valid=False,
            errors=[f"Invalid JSON in file: {e}"],
            warnings=[]
        )
    except FileNotFoundError:
        return ValidationResult(
            is_valid=False,
            errors=[f"File not found: {file_path}"],
            warnings=[]
        )

    if not isinstance(data, list):
        return ValidationResult(
            is_valid=False,
            errors=["JSON file must contain an array of evaluations"],
            warnings=[]
        )

    results, valid_count, invalid_count = validator.validate_evaluation_batch(data)

    # Aggregate all errors and warnings
    all_errors = []
    all_warnings = []

    for i, result in enumerate(results):
        if result.errors:
            all_errors.append(f"Record {i+1} (student: {data[i].get('student', 'unknown')}):")
            all_errors.extend(f"  - {err}" for err in result.errors)
        if result.warnings:
            all_warnings.extend(f"Record {i+1}: {warn}" for warn in result.warnings)

    return ValidationResult(
        is_valid=invalid_count == 0,
        errors=all_errors,
        warnings=all_warnings
    )


# ============================================================================
# CLI Interface for Testing
# ============================================================================

if __name__ == "__main__":
    import sys

    if len(sys.argv) < 2:
        print("Usage: python validator.py <json_file_path> [schema_path]")
        print("\nExample:")
        print("  python validator.py data/results_direct_2025-11-13T17-52-45.json")
        sys.exit(1)

    file_path = sys.argv[1]
    schema_path = sys.argv[2] if len(sys.argv) > 2 else None

    print(f"Validating: {file_path}")
    if schema_path:
        print(f"Using schema: {schema_path}")
    print("-" * 70)

    result = validate_json_file(file_path, schema_path)
    print(result.get_details())

    if not result.is_valid:
        sys.exit(1)
