"""Tests for utils.validation module."""

from __future__ import annotations

import json
import tempfile
from pathlib import Path
from unittest.mock import patch

import pytest

from utils.validation import (
    ValidationResult,
    validate_json_file,
    _validate_record_structure,
    _load_schema,
    _get_default_schema_path,
)


class TestValidationResult:
    """Test ValidationResult class."""

    def test_valid_result_creation(self):
        """Test creating a valid ValidationResult."""
        result = ValidationResult(is_valid=True)
        assert result.is_valid is True
        assert result.errors == []
        assert result.warnings == []

    def test_invalid_result_creation(self):
        """Test creating an invalid ValidationResult."""
        errors = ["Error 1", "Error 2"]
        warnings = ["Warning 1"]
        result = ValidationResult(is_valid=False, errors=errors, warnings=warnings)

        assert result.is_valid is False
        assert result.errors == errors
        assert result.warnings == warnings

    def test_bool_conversion(self):
        """Test boolean conversion of ValidationResult."""
        valid_result = ValidationResult(is_valid=True)
        invalid_result = ValidationResult(is_valid=False)

        assert bool(valid_result) is True
        assert bool(invalid_result) is False

    def test_get_summary_valid(self):
        """Test summary for valid result."""
        result = ValidationResult(is_valid=True)
        assert result.get_summary() == "✓ Validation passed"

    def test_get_summary_valid_with_warnings(self):
        """Test summary for valid result with warnings."""
        result = ValidationResult(is_valid=True, warnings=["Warning 1", "Warning 2"])
        assert result.get_summary() == "✓ Validation passed with 2 warning(s)"

    def test_get_summary_invalid(self):
        """Test summary for invalid result."""
        result = ValidationResult(is_valid=False, errors=["Error 1"])
        assert result.get_summary() == "✗ Validation failed with 1 error(s)"

    def test_get_details(self):
        """Test detailed validation report."""
        errors = ["Schema error", "Business rule error"]
        warnings = ["Missing field"]
        result = ValidationResult(is_valid=False, errors=errors, warnings=warnings)

        details = result.get_details()
        assert "✗ Validation failed with 2 error(s)" in details
        assert "1. Schema error" in details
        assert "2. Business rule error" in details
        assert "1. Missing field" in details


class TestSchemaLoading:
    """Test schema loading functions."""

    def test_get_default_schema_path(self):
        """Test getting default schema path."""
        path = _get_default_schema_path()
        assert path.name == "evaluation_schema.json"
        assert path.exists()

    def test_load_schema_default(self):
        """Test loading default schema."""
        schema = _load_schema(None)
        assert isinstance(schema, dict)
        assert "$schema" in schema
        assert "title" in schema

    def test_load_schema_custom_path(self, temp_dir):
        """Test loading schema from custom path."""
        custom_schema = {
            "$schema": "http://json-schema.org/draft-07/schema#",
            "type": "object",
            "properties": {"test": {"type": "string"}},
        }
        schema_file = temp_dir / "custom_schema.json"
        schema_file.write_text(json.dumps(custom_schema))

        loaded_schema = _load_schema(str(schema_file))
        assert loaded_schema == custom_schema

    def test_load_schema_nonexistent_file(self):
        """Test loading schema from nonexistent file."""
        with pytest.raises(FileNotFoundError, match="Schema file not found"):
            _load_schema("/nonexistent/path/schema.json")


class TestRecordValidation:
    """Test individual record validation."""

    def test_validate_valid_record(self, sample_evaluation):
        """Test validating a valid record."""
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) == 0
        assert len(warnings) == 0

    def test_validate_record_empty_student(self, sample_evaluation):
        """Test validating record with empty student name."""
        sample_evaluation["student"] = ""
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # Schema validation should catch empty student (minLength: 1)
        assert len(errors) > 0
        assert any("student" in error.lower() for error in errors)

    def test_validate_record_missing_student(self, sample_evaluation):
        """Test validating record with missing student field."""
        del sample_evaluation["student"]
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) > 0
        assert len(warnings) > 0

    def test_validate_record_incorrect_metrics(self, sample_evaluation):
        """Test validating record with incorrect metrics calculations."""
        # Break the avg_pct calculation
        sample_evaluation["metrics"]["avg_pct"] = 50.0  # Should be 87.5
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) > 0
        assert any("avg_pct" in error for error in errors)

    def test_validate_record_incorrect_diff(self, sample_evaluation):
        """Test validating record with incorrect diff calculation."""
        # Break the diff_pct calculation
        sample_evaluation["metrics"]["diff_pct"] = 20.0  # Should be 5.0
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) > 0
        assert any("diff_pct" in error for error in errors)

    def test_validate_record_non_numeric_metrics(self, sample_evaluation):
        """Test validating record with non-numeric metric values."""
        sample_evaluation["metrics"]["avg_pct"] = "not_a_number"
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) > 0
        assert any("numeric" in error for error in errors)

    def test_validate_record_with_missing_model_scores(self, sample_evaluation):
        """Test that records with a missing model score are allowed."""
        # Simulate a case where GPT-5 Nano did not return usable grading JSON.
        sample_evaluation["gpt5_nano_result"] = None
        sample_evaluation["metrics"]["gpt5_nano"]["total"] = None
        sample_evaluation["metrics"]["gpt5_nano"]["max"] = None
        sample_evaluation["metrics"]["gpt5_nano"]["pct"] = None
        # Only OSS percentage is available; avg_pct is based on that, diff_pct is None.
        sample_evaluation["metrics"]["avg_pct"] = sample_evaluation["metrics"]["gpt_oss_120b"]["pct"]
        sample_evaluation["metrics"]["diff_pct"] = None

        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # Partial records should still pass validation.
        assert len(errors) == 0

    def test_validate_record_missing_model_metrics(self, sample_evaluation):
        """Test validating record with missing model metrics."""
        del sample_evaluation["metrics"]["gpt5_nano"]
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # Schema validation should catch missing required field
        assert len(errors) > 0
        assert any("gpt5_nano" in error for error in errors)

    @patch("utils.validation.JSONSCHEMA_AVAILABLE", False)
    def test_validate_without_jsonschema(self, sample_evaluation):
        """Test validation when jsonschema is not available."""
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) == 0
        assert len(warnings) > 0
        assert any("jsonschema" in warning.lower() for warning in warnings)


class TestFileValidation:
    """Test file-level validation."""

    def test_validate_valid_file(self, temp_json_file):
        """Test validating a valid JSON file."""
        result = validate_json_file(str(temp_json_file))

        assert result.is_valid is True
        assert len(result.errors) == 0

    def test_validate_nonexistent_file(self):
        """Test validating a nonexistent file."""
        result = validate_json_file("/nonexistent/file.json")

        assert result.is_valid is False
        assert len(result.errors) > 0
        assert "not found" in result.errors[0]

    def test_validate_invalid_json(self, temp_dir):
        """Test validating file with invalid JSON."""
        invalid_file = temp_dir / "invalid.json"
        invalid_file.write_text("{ invalid json content")

        result = validate_json_file(str(invalid_file))

        assert result.is_valid is False
        assert len(result.errors) > 0
        assert "Invalid JSON" in result.errors[0]

    def test_validate_non_array_file(self, temp_dir):
        """Test validating file that doesn't contain an array."""
        object_file = temp_dir / "object.json"
        object_file.write_text('{"key": "value"}')

        result = validate_json_file(str(object_file))

        assert result.is_valid is False
        assert len(result.errors) > 0
        assert "array" in result.errors[0]

    def test_validate_mixed_validity_file(self, temp_dir, sample_evaluation, invalid_evaluation):
        """Test validating file with mixed valid/invalid records."""
        mixed_data = [sample_evaluation, invalid_evaluation]
        mixed_file = temp_dir / "mixed.json"
        mixed_file.write_text(json.dumps(mixed_data, indent=2))

        result = validate_json_file(str(mixed_file))

        assert result.is_valid is False
        assert len(result.errors) > 0

    def test_validate_empty_array_file(self, temp_dir):
        """Test validating file with empty array."""
        empty_file = temp_dir / "empty.json"
        empty_file.write_text("[]")

        result = validate_json_file(str(empty_file))

        assert result.is_valid is True
        assert len(result.errors) == 0

    def test_validate_file_with_invalid_schema(self, temp_dir):
        """Test validating file with missing schema file."""
        # Create a valid JSON file
        valid_file = temp_dir / "valid.json"
        valid_file.write_text(json.dumps([{"student": "test"}]))

        # Try to validate with nonexistent schema
        result = validate_json_file(str(valid_file), schema_path="/nonexistent/schema.json")

        assert result.is_valid is False
        assert len(result.errors) > 0
        assert "Schema file not found" in result.errors[0]

    def test_validate_file_with_custom_schema(self, temp_dir, sample_evaluation):
        """Test validating file with custom schema."""
        # Create a custom schema that only requires student field
        custom_schema = {
            "$schema": "http://json-schema.org/draft-07/schema#",
            "type": "object",
            "required": ["student"],
            "properties": {"student": {"type": "string"}},
        }
        schema_file = temp_dir / "custom_schema.json"
        schema_file.write_text(json.dumps(custom_schema))

        # Create minimal valid data
        minimal_data = [{"student": "test_student"}]
        data_file = temp_dir / "minimal.json"
        data_file.write_text(json.dumps(minimal_data))

        result = validate_json_file(str(data_file), schema_path=str(schema_file))

        assert result.is_valid is True
        assert len(result.errors) == 0


class TestEdgeCases:
    """Test edge cases and boundary conditions."""

    def test_validate_record_boundary_scores(self, sample_evaluation):
        """Test record with boundary score values."""
        # Test with 0 and 100 scores
        sample_evaluation["gpt5_nano_result"]["total_score"] = 0
        sample_evaluation["gpt_oss_120b_result"]["total_score"] = 100
        sample_evaluation["metrics"]["gpt5_nano"]["pct"] = 0.0
        sample_evaluation["metrics"]["gpt_oss_120b"]["pct"] = 100.0
        sample_evaluation["metrics"]["avg_pct"] = 50.0
        sample_evaluation["metrics"]["diff_pct"] = 100.0

        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        assert len(errors) == 0

    def test_validate_record_negative_scores(self, sample_evaluation):
        """Test record with negative scores."""
        sample_evaluation["gpt5_nano_result"]["total_score"] = -10
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # JSON schema should catch this
        assert len(errors) > 0

    def test_validate_record_scores_above_max(self, sample_evaluation):
        """Test record with scores above maximum."""
        sample_evaluation["gpt5_nano_result"]["total_score"] = 150
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # JSON schema only checks minimum >= 0, not maximum relative to max_possible_score
        # So this should pass schema validation (business logic might catch it elsewhere)
        assert len(errors) == 0

    def test_validate_record_with_null_values(self, sample_evaluation):
        """Test record with null values in optional fields."""
        sample_evaluation["gpt5_nano_result"]["overall_feedback"] = None
        schema = _load_schema(None)
        errors, warnings = _validate_record_structure(sample_evaluation, schema)

        # Schema validation should catch null string field
        assert len(errors) > 0
        assert any("overall_feedback" in error for error in errors)
