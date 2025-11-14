#!/usr/bin/env python3
"""
CLI for validating evaluation JSON files.

Usage:
    python validate_data.py data/results_direct_2025-11-13T17-52-45.json
    python validate_data.py data/results_direct_2025-11-13T17-52-45.json --schema custom_schema.json
"""

import sys
from pathlib import Path

from db import validate_json_file


def main():
    """Main CLI entry point."""
    import argparse

    parser = argparse.ArgumentParser(
        description="Validate evaluation JSON files"
    )
    parser.add_argument(
        "json_file",
        help="Path to JSON file containing evaluation data"
    )
    parser.add_argument(
        "--schema",
        help="Path to JSON schema file (optional)"
    )

    args = parser.parse_args()

    print(f"Validating: {args.json_file}")
    if args.schema:
        print(f"Using schema: {args.schema}")
    print("-" * 70)

    result = validate_json_file(args.json_file, args.schema)
    print(result.get_details())

    if not result.is_valid:
        sys.exit(1)


if __name__ == "__main__":
    main()
