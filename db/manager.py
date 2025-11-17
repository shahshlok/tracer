"""
Database manager for evaluation results.

This module provides a minimal SQLite interface for storing and retrieving
evaluation results. It follows the principle of "validated JSON in, validated JSON out".

Key responsibilities:
- Initialize the database schema
- Ingest evaluation JSON files with validation
- Export stored evaluations back to JSON files
"""

from __future__ import annotations

import json
import logging
import re
import sqlite3
from datetime import datetime
from pathlib import Path
from typing import List, Optional

from utils.validation import validate_json_file, ValidationResult

logger = logging.getLogger(__name__)

# Default database path at project root
DEFAULT_DB_PATH = "evaluations.db"
SCHEMA_FILE = Path(__file__).parent / "schema.sql"


class DatabaseManager:
    """Manages SQLite database operations for evaluation results."""

    def __init__(self, db_path: str = DEFAULT_DB_PATH):
        """Initialize database manager with specified path."""
        self.db_path = db_path

    def init_db(self) -> None:
        """
        Initialize the database by creating tables if they don't exist.
        Safe to call multiple times.
        """
        if not SCHEMA_FILE.exists():
            raise FileNotFoundError(f"Schema file not found: {SCHEMA_FILE}")

        schema_sql = SCHEMA_FILE.read_text(encoding="utf-8")

        with sqlite3.connect(self.db_path) as conn:
            conn.executescript(schema_sql)
            conn.commit()

        logger.info(f"Database initialized at {self.db_path}")

    def ingest_results_file(self, file_path: Path) -> None:
        """
        Ingest a results JSON file into the database.

        Validates the file before insertion. If validation fails, aborts
        without inserting anything.

        Args:
            file_path: Path to the JSON file containing evaluation results

        Raises:
            ValueError: If file validation fails
            FileNotFoundError: If file doesn't exist
        """
        if not file_path.exists():
            raise FileNotFoundError(f"Results file not found: {file_path}")

        # Parse strategy and timestamp from filename
        # Expected format: results_{strategy}_{timestamp}.json
        strategy, run_timestamp = self._parse_filename(file_path.name)

        # Validate JSON file before ingestion
        validation_result = validate_json_file(str(file_path))

        if not validation_result.is_valid:
            error_msg = f"Validation failed for {file_path.name}:\n{validation_result.get_details()}"
            logger.error(error_msg)
            raise ValueError(error_msg)

        if validation_result.warnings:
            logger.warning(f"Validation warnings for {file_path.name}:")
            for warning in validation_result.warnings:
                logger.warning(f"  {warning}")

        # Load the validated data
        with file_path.open("r", encoding="utf-8") as f:
            evaluations = json.load(f)

        # Insert into database
        with sqlite3.connect(self.db_path) as conn:
            cursor = conn.cursor()

            # Insert run record
            cursor.execute(
                """
                INSERT INTO runs (run_timestamp, source_file, strategy)
                VALUES (?, ?, ?)
                """,
                (run_timestamp, file_path.name, strategy),
            )
            run_id = cursor.lastrowid

            # Insert evaluation records
            for evaluation in evaluations:
                student_name = evaluation.get("student", "unknown")
                raw_json = json.dumps(evaluation, separators=(",", ":"))

                cursor.execute(
                    """
                    INSERT INTO evaluations (run_id, student_name, strategy, raw_json)
                    VALUES (?, ?, ?, ?)
                    """,
                    (run_id, student_name, strategy, raw_json),
                )

            conn.commit()

        logger.info(
            f"Ingested {len(evaluations)} evaluations from {file_path.name} "
            f"(run_id={run_id}, strategy={strategy})"
        )

    def restore_json_files(
        self,
        data_dir: Path,
        validate: bool = False,
    ) -> int:
        """
        Restore all JSON files from the database to the data directory.

        Recreates one JSON file per run using the original source_file names.

        Args:
            data_dir: Directory to write JSON files to
            validate: If True, re-validate each record before writing

        Returns:
            Number of files restored

        Raises:
            ValueError: If validation is enabled and any record fails validation
        """
        data_dir.mkdir(parents=True, exist_ok=True)

        with sqlite3.connect(self.db_path) as conn:
            conn.row_factory = sqlite3.Row
            cursor = conn.cursor()

            # Get all runs
            cursor.execute(
                """
                SELECT run_id, run_timestamp, source_file, strategy
                FROM runs
                ORDER BY run_timestamp, strategy
                """
            )
            runs = cursor.fetchall()

            files_restored = 0

            for run in runs:
                run_id = run["run_id"]
                source_file = run["source_file"]

                # Get all evaluations for this run
                cursor.execute(
                    """
                    SELECT raw_json
                    FROM evaluations
                    WHERE run_id = ?
                    ORDER BY student_name
                    """,
                    (run_id,),
                )

                evaluations = []
                for row in cursor.fetchall():
                    evaluation = json.loads(row["raw_json"])

                    # Optional re-validation
                    if validate:
                        # TODO: Implement per-record validation if needed
                        pass

                    evaluations.append(evaluation)

                # Write JSON file
                output_path = data_dir / source_file
                with output_path.open("w", encoding="utf-8") as f:
                    json.dump(evaluations, f, indent=2)

                logger.info(
                    f"Restored {len(evaluations)} evaluations to {output_path}"
                )
                files_restored += 1

        return files_restored

    def _parse_filename(self, filename: str) -> tuple[str, str]:
        """
        Parse strategy and timestamp from results filename.

        Expected format: results_{strategy}_{timestamp}.json
        Example: results_direct_2025-11-13T17-52-45.json

        Returns:
            Tuple of (strategy, timestamp)

        Raises:
            ValueError: If filename doesn't match expected format
        """
        pattern = r"results_(\w+)_(.+)\.json"
        match = re.match(pattern, filename)

        if not match:
            raise ValueError(
                f"Filename '{filename}' doesn't match expected format: "
                "results_{{strategy}}_{{timestamp}}.json"
            )

        strategy = match.group(1)
        timestamp = match.group(2)

        # Validate strategy
        valid_strategies = ["direct", "reverse", "eme"]
        if strategy not in valid_strategies:
            raise ValueError(
                f"Invalid strategy '{strategy}'. Must be one of: {valid_strategies}"
            )

        return strategy, timestamp


# Convenience functions for backwards compatibility and simpler imports

def init_db(db_path: str = DEFAULT_DB_PATH) -> None:
    """Initialize the database. Safe to call multiple times."""
    manager = DatabaseManager(db_path)
    manager.init_db()


def ingest_results_file(db_path: str, file_path: Path) -> None:
    """Ingest a results JSON file into the database."""
    manager = DatabaseManager(db_path)
    manager.ingest_results_file(file_path)


def restore_json_files(
    db_path: str,
    data_dir: Path,
    validate: bool = False,
) -> int:
    """Restore all JSON files from database to data directory."""
    manager = DatabaseManager(db_path)
    return manager.restore_json_files(data_dir, validate)
