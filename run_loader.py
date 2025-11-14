#!/usr/bin/env python3
"""
CLI for loading evaluation data into SQLite database.

Usage:
    # Load all files from data directory
    python run_loader.py data/ --init-db

    # Load a single file
    python run_loader.py data/results_direct_2025-11-13T17-52-45.json

    # Specify custom database path
    python run_loader.py data/ --db custom.db --init-db
"""

import sys
from pathlib import Path

from db import EvaluationLoader, LoadStats


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
        help="Path to SQL schema file (optional, uses built-in schema by default)"
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
