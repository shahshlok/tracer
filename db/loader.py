#!/usr/bin/env python3
"""
Data loading script for EME Testing evaluation database.

This script loads evaluation JSON files into SQLite database with:
- Comprehensive validation before insertion
- JSONB format for optimal storage and performance
- Transaction support for data integrity
- Detailed logging and error reporting
"""

import sqlite3
import json
import sys
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Optional, Tuple
from dataclasses import dataclass, field

from .models import ValidationResult, get_sql_schema_path
from .validator import EvaluationValidator


@dataclass
class LoadStats:
    """Statistics for a data loading operation."""
    total_files: int = 0
    successful_files: int = 0
    failed_files: int = 0
    total_records: int = 0
    inserted_records: int = 0
    skipped_records: int = 0
    validation_errors: int = 0
    validation_warnings: int = 0

    def print_summary(self):
        """Print a summary of the loading operation."""
        print("\n" + "=" * 70)
        print("LOADING SUMMARY")
        print("=" * 70)
        print(f"Files processed: {self.successful_files}/{self.total_files}")
        if self.failed_files > 0:
            print(f"Files failed: {self.failed_files}")
        print(f"Records inserted: {self.inserted_records}/{self.total_records}")
        if self.skipped_records > 0:
            print(f"Records skipped: {self.skipped_records}")
        if self.validation_errors > 0:
            print(f"Validation errors: {self.validation_errors}")
        if self.validation_warnings > 0:
            print(f"Validation warnings: {self.validation_warnings}")
        print("=" * 70)


class EvaluationLoader:
    """Loads evaluation data into SQLite database with validation."""

    def __init__(self, db_path: str, schema_path: Optional[str] = None):
        """
        Initialize the data loader.

        Args:
            db_path: Path to SQLite database file
            schema_path: Optional path to JSON schema for validation
        """
        self.db_path = Path(db_path)
        self.validator = EvaluationValidator(schema_path)
        self.conn = None

    def __enter__(self):
        """Context manager entry."""
        self.connect()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        """Context manager exit."""
        self.close()

    def connect(self):
        """Connect to the database."""
        self.conn = sqlite3.connect(self.db_path)
        # Enable foreign key constraints
        self.conn.execute("PRAGMA foreign_keys = ON")

    def close(self):
        """Close database connection."""
        if self.conn:
            self.conn.close()
            self.conn = None

    def init_database(self, schema_sql_path: Optional[str] = None):
        """
        Initialize database schema from SQL file.

        Args:
            schema_sql_path: Path to schema SQL file (defaults to db/schema.sql)
        """
        if schema_sql_path is None:
            schema_path = get_sql_schema_path()
        else:
            schema_path = Path(schema_sql_path)

        if not schema_path.exists():
            raise FileNotFoundError(f"Schema file not found: {schema_path}")

        with open(schema_path, 'r') as f:
            schema_sql = f.read()

        print(f"Initializing database schema from {schema_path}...")
        self.conn.executescript(schema_sql)
        self.conn.commit()
        print("✓ Database schema initialized")

    def parse_timestamp_from_filename(self, filename: str) -> Optional[str]:
        """
        Extract timestamp from filename.

        Expected format: results_{strategy}_YYYY-MM-DDTHH-MM-SS.json

        Args:
            filename: Name of the file

        Returns:
            ISO timestamp string or None if parsing fails
        """
        # Example: results_direct_2025-11-13T17-52-45.json
        parts = filename.split('_')
        if len(parts) >= 3:
            # Extract date and time parts: "2025-11-13T17-52-45.json" -> "2025-11-13T17-52-45"
            date_time = '_'.join(parts[2:]).replace('.json', '')

            # Split into date and time parts
            if 'T' in date_time:
                date_part, time_part = date_time.split('T')
                # Convert time from HH-MM-SS to HH:MM:SS
                time_part = time_part.replace('-', ':')
                return f"{date_part}T{time_part}"

        return None

    def extract_strategy_from_filename(self, filename: str) -> Optional[str]:
        """
        Extract strategy from filename.

        Expected format: results_{strategy}_YYYY-MM-DDTHH-MM-SS.json

        Args:
            filename: Name of the file

        Returns:
            Strategy name ('direct', 'reverse', or 'eme') or None
        """
        # Example: results_direct_2025-11-13T17-52-45.json
        parts = filename.split('_')
        if len(parts) >= 2:
            strategy = parts[1]
            if strategy in ['direct', 'reverse', 'eme']:
                return strategy
        return None

    def get_or_create_run(
        self,
        timestamp: str,
        metadata: Optional[Dict] = None
    ) -> int:
        """
        Get existing run or create a new one.

        Args:
            timestamp: ISO timestamp for the run
            metadata: Optional metadata dictionary

        Returns:
            run_id for the run
        """
        cursor = self.conn.cursor()

        # Check if run already exists
        cursor.execute("SELECT run_id FROM runs WHERE run_timestamp = ?", (timestamp,))
        row = cursor.fetchone()

        if row:
            return row[0]

        # Create new run
        if metadata:
            metadata_json = json.dumps(metadata)
            cursor.execute(
                "INSERT INTO runs (run_timestamp, metadata) VALUES (?, jsonb(?))",
                (timestamp, metadata_json)
            )
        else:
            cursor.execute(
                "INSERT INTO runs (run_timestamp) VALUES (?)",
                (timestamp,)
            )

        return cursor.lastrowid

    def load_evaluation_file(
        self,
        file_path: str,
        run_metadata: Optional[Dict] = None,
        skip_validation: bool = False,
        verbose: bool = True
    ) -> Tuple[int, int, int]:
        """
        Load evaluations from a JSON file.

        Args:
            file_path: Path to JSON file
            run_metadata: Optional metadata for the run
            skip_validation: If True, skip validation (not recommended)
            verbose: If True, print detailed progress

        Returns:
            Tuple of (inserted_count, skipped_count, error_count)
        """
        file_path = Path(file_path)

        if verbose:
            print(f"\nLoading: {file_path.name}")

        # Parse timestamp and strategy from filename
        timestamp = self.parse_timestamp_from_filename(file_path.name)
        strategy = self.extract_strategy_from_filename(file_path.name)

        if not timestamp:
            print(f"✗ Could not parse timestamp from filename: {file_path.name}")
            return 0, 0, 1

        if not strategy:
            print(f"✗ Could not parse strategy from filename: {file_path.name}")
            return 0, 0, 1

        if verbose:
            print(f"  Timestamp: {timestamp}")
            print(f"  Strategy: {strategy}")

        # Load JSON data
        try:
            with open(file_path, 'r') as f:
                evaluations = json.load(f)
        except json.JSONDecodeError as e:
            print(f"✗ Invalid JSON: {e}")
            return 0, 0, 1
        except FileNotFoundError:
            print(f"✗ File not found: {file_path}")
            return 0, 0, 1

        if not isinstance(evaluations, list):
            print(f"✗ JSON must contain an array of evaluations")
            return 0, 0, 1

        # Validate all evaluations
        if not skip_validation:
            if verbose:
                print(f"  Validating {len(evaluations)} records...")
            results, valid_count, invalid_count = self.validator.validate_evaluation_batch(
                evaluations
            )

            if invalid_count > 0:
                print(f"✗ Validation failed for {invalid_count} record(s):")
                for i, result in enumerate(results):
                    if not result.is_valid:
                        print(f"\n  Record {i+1} ({evaluations[i].get('student', 'unknown')}):")
                        for error in result.errors:
                            print(f"    - {error}")
                return 0, 0, invalid_count

            # Count warnings
            warning_count = sum(len(r.warnings) for r in results)
            if warning_count > 0 and verbose:
                print(f"  ⚠ {warning_count} validation warning(s)")

        # Get or create run
        run_id = self.get_or_create_run(timestamp, run_metadata)
        if verbose:
            print(f"  Run ID: {run_id}")

        # Insert evaluations in a transaction
        inserted_count = 0
        skipped_count = 0
        error_count = 0

        cursor = self.conn.cursor()

        try:
            for evaluation in evaluations:
                student_name = evaluation['student']

                # Convert to JSONB
                json_str = json.dumps(evaluation)

                try:
                    cursor.execute("""
                        INSERT INTO evaluations (run_id, student_name, strategy, raw_json)
                        VALUES (?, ?, ?, jsonb(?))
                    """, (run_id, student_name, strategy, json_str))

                    inserted_count += 1

                except sqlite3.IntegrityError as e:
                    # Likely duplicate (run_id, student_name, strategy)
                    if "UNIQUE constraint failed" in str(e):
                        skipped_count += 1
                        if verbose:
                            print(f"  ⊗ Skipped duplicate: {student_name}")
                    else:
                        raise

            self.conn.commit()

            if verbose:
                print(f"✓ Inserted {inserted_count} record(s)")
                if skipped_count > 0:
                    print(f"  Skipped {skipped_count} duplicate(s)")

        except Exception as e:
            self.conn.rollback()
            print(f"✗ Transaction failed: {e}")
            error_count = len(evaluations) - inserted_count - skipped_count
            return inserted_count, skipped_count, error_count

        return inserted_count, skipped_count, error_count

    def load_directory(
        self,
        directory: str,
        pattern: str = "results_*.json",
        run_metadata: Optional[Dict] = None,
        skip_validation: bool = False,
        verbose: bool = True
    ) -> LoadStats:
        """
        Load all matching JSON files from a directory.

        Args:
            directory: Path to directory containing JSON files
            pattern: Glob pattern for matching files
            run_metadata: Optional metadata for all runs
            skip_validation: If True, skip validation (not recommended)
            verbose: If True, print detailed progress

        Returns:
            LoadStats with summary information
        """
        directory = Path(directory)
        files = sorted(directory.glob(pattern))

        if not files:
            print(f"No files matching '{pattern}' found in {directory}")
            return LoadStats()

        print(f"Found {len(files)} file(s) to load")

        stats = LoadStats(total_files=len(files))

        for file_path in files:
            inserted, skipped, errors = self.load_evaluation_file(
                file_path,
                run_metadata=run_metadata,
                skip_validation=skip_validation,
                verbose=verbose
            )

            stats.total_records += inserted + skipped + errors
            stats.inserted_records += inserted
            stats.skipped_records += skipped

            if errors > 0:
                stats.failed_files += 1
                stats.validation_errors += errors
            else:
                stats.successful_files += 1

        return stats


# ============================================================================
# CLI Interface
# ============================================================================

def main():
    """Main CLI entry point."""
    import argparse

    parser = argparse.ArgumentParser(
        description="Load evaluation data into SQLite database with validation"
    )
    parser.add_argument(
        "input",
        help="JSON file or directory containing evaluation data"
    )
    parser.add_argument(
        "--db",
        default="evaluations.db",
        help="Path to SQLite database (default: evaluations.db)"
    )
    parser.add_argument(
        "--schema",
        default="schema.sql",
        help="Path to SQL schema file (default: schema.sql)"
    )
    parser.add_argument(
        "--init-db",
        action="store_true",
        help="Initialize database schema before loading"
    )
    parser.add_argument(
        "--skip-validation",
        action="store_true",
        help="Skip JSON schema validation (not recommended)"
    )
    parser.add_argument(
        "--quiet",
        action="store_true",
        help="Suppress detailed output"
    )
    parser.add_argument(
        "--pattern",
        default="results_*.json",
        help="File pattern when loading directory (default: results_*.json)"
    )

    args = parser.parse_args()

    input_path = Path(args.input)

    # Create loader
    with EvaluationLoader(args.db) as loader:
        # Initialize database if requested
        if args.init_db:
            loader.init_database(args.schema)

        # Load data
        if input_path.is_file():
            # Load single file
            inserted, skipped, errors = loader.load_evaluation_file(
                input_path,
                skip_validation=args.skip_validation,
                verbose=not args.quiet
            )

            stats = LoadStats(
                total_files=1,
                successful_files=1 if errors == 0 else 0,
                failed_files=1 if errors > 0 else 0,
                total_records=inserted + skipped + errors,
                inserted_records=inserted,
                skipped_records=skipped,
                validation_errors=errors
            )

        elif input_path.is_dir():
            # Load directory
            stats = loader.load_directory(
                input_path,
                pattern=args.pattern,
                skip_validation=args.skip_validation,
                verbose=not args.quiet
            )

        else:
            print(f"Error: {input_path} is neither a file nor directory")
            sys.exit(1)

        # Print summary
        stats.print_summary()

        # Exit with error code if there were failures
        if stats.failed_files > 0 or stats.validation_errors > 0:
            sys.exit(1)


if __name__ == "__main__":
    main()
