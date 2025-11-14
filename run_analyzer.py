#!/usr/bin/env python3
"""
CLI for analyzing evaluation data.

Usage:
    # Run all demo queries
    python run_analyzer.py

    # Specify custom database
    python run_analyzer.py --db custom.db

    # Run specific analysis
    python run_analyzer.py --query disagreements --threshold 15
"""

import sys
from pathlib import Path

from db import EvaluationAnalyzer


def run_demo(db_path: str = "evaluations.db"):
    """Run a demonstration of common queries."""
    print("\n" + "=" * 70)
    print("EME TESTING EVALUATION DATABASE - QUERY EXAMPLES")
    print("=" * 70)

    with EvaluationAnalyzer(db_path) as analyzer:
        # List all runs
        analyzer.list_all_runs()

        try:
            run_id = analyzer.get_latest_run_id()

            # 1. Strategy comparison
            analyzer.compare_strategies_for_student("Smith_John_123456", run_id)
            analyzer.compare_all_students_across_strategies(run_id)

            # 2. Aggregate stats
            analyzer.aggregate_stats_per_strategy(run_id)

            # 3. Model agreement
            analyzer.find_model_disagreements(threshold=10.0, run_id=run_id)
            analyzer.model_agreement_distribution(run_id)

            # 4. Misconceptions
            analyzer.search_feedback("loop", run_id)
            analyzer.find_common_feedback_patterns(run_id)

            # 5. Advanced analysis
            analyzer.identify_struggling_students(threshold=60.0, run_id=run_id)
            analyzer.strategy_effectiveness_by_score_range(run_id)

            # 6. Full JSON retrieval
            analyzer.get_full_evaluation("Smith_John_123456", "direct", run_id)

        except ValueError as e:
            print(f"\nError: {e}")
            print("Make sure you have loaded data into the database first.")
            print("Run: python run_loader.py data/ --init-db")


def main():
    """Main CLI entry point."""
    import argparse

    parser = argparse.ArgumentParser(
        description="Analyze evaluation data from SQLite database"
    )
    parser.add_argument(
        "--db",
        default="evaluations.db",
        help="Path to SQLite database (default: evaluations.db)"
    )
    parser.add_argument(
        "--query",
        choices=["disagreements", "strategies", "misconceptions", "trends", "demo"],
        default="demo",
        help="Type of analysis to run (default: demo runs all queries)"
    )
    parser.add_argument(
        "--threshold",
        type=float,
        default=10.0,
        help="Threshold for disagreement analysis (default: 10.0)"
    )
    parser.add_argument(
        "--student",
        help="Student name for student-specific queries"
    )
    parser.add_argument(
        "--run-id",
        type=int,
        help="Specific run ID to analyze (defaults to latest)"
    )

    args = parser.parse_args()

    if not Path(args.db).exists():
        print(f"Error: Database not found: {args.db}")
        print("Create it first by running: python run_loader.py data/ --init-db")
        sys.exit(1)

    if args.query == "demo":
        run_demo(args.db)
    else:
        with EvaluationAnalyzer(args.db) as analyzer:
            run_id = args.run_id or analyzer.get_latest_run_id()

            if args.query == "disagreements":
                analyzer.find_model_disagreements(args.threshold, run_id)
            elif args.query == "strategies":
                if args.student:
                    analyzer.compare_strategies_for_student(args.student, run_id)
                else:
                    analyzer.compare_all_students_across_strategies(run_id)
            elif args.query == "misconceptions":
                analyzer.find_common_feedback_patterns(run_id)
            elif args.query == "trends":
                analyzer.compare_runs_over_time()


if __name__ == "__main__":
    main()
