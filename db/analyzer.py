#!/usr/bin/env python3
"""
Query and analysis module for EME Testing evaluation database.

This module provides comprehensive analysis capabilities for querying evaluation data,
including strategy comparisons, temporal analysis, model agreement, and
misconception discovery.
"""

import sqlite3
import json
from typing import List, Dict, Any, Optional
from pathlib import Path


class EvaluationAnalyzer:
    """Analyzer for querying and analyzing evaluation data."""

    def __init__(self, db_path: str = "evaluations.db"):
        """Initialize analyzer with database connection."""
        self.db_path = db_path
        self.conn = sqlite3.connect(db_path)
        self.conn.row_factory = sqlite3.Row  # Enable column access by name

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()

    def close(self):
        """Close database connection."""
        if self.conn:
            self.conn.close()

    def execute_query(self, query: str, params: tuple = ()) -> List[sqlite3.Row]:
        """Execute a query and return all results."""
        cursor = self.conn.cursor()
        cursor.execute(query, params)
        return cursor.fetchall()

    def print_results(self, results: List[sqlite3.Row], title: str = None):
        """Pretty print query results."""
        if title:
            print(f"\n{'=' * 70}")
            print(f"{title}")
            print('=' * 70)

        if not results:
            print("No results found.")
            return

        # Print header
        headers = list(results[0].keys())
        print("\n" + " | ".join(f"{h:15}" for h in headers))
        print("-" * (len(headers) * 18))

        # Print rows
        for row in results:
            values = [str(row[h])[:15] for h in headers]
            print(" | ".join(f"{v:15}" for v in values))

        print(f"\nTotal rows: {len(results)}\n")

    # ========================================================================
    # 1. COMPARE STRATEGIES FOR SAME STUDENT
    # ========================================================================

    def compare_strategies_for_student(self, student_name: str, run_id: int = None):
        """
        Compare all three strategies for a specific student.

        Args:
            student_name: Name of the student
            run_id: Optional run_id (defaults to latest run)
        """
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            student_name,
            strategy,
            nano_pct,
            oss_pct,
            avg_pct,
            diff_pct,
            flag,
            json_extract(raw_json, '$.metrics.comment') as comment
        FROM evaluations
        WHERE student_name = ? AND run_id = ?
        ORDER BY strategy
        """

        results = self.execute_query(query, (student_name, run_id))
        self.print_results(
            results,
            f"Strategy Comparison for {student_name} (Run {run_id})"
        )
        return results

    def compare_all_students_across_strategies(self, run_id: int = None):
        """Show which strategy gives highest scores for each student."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            student_name,
            MAX(CASE WHEN strategy = 'direct' THEN avg_pct END) as direct_avg,
            MAX(CASE WHEN strategy = 'reverse' THEN avg_pct END) as reverse_avg,
            MAX(CASE WHEN strategy = 'eme' THEN avg_pct END) as eme_avg,
            CASE
                WHEN MAX(CASE WHEN strategy = 'direct' THEN avg_pct END) >
                     MAX(CASE WHEN strategy = 'reverse' THEN avg_pct END) AND
                     MAX(CASE WHEN strategy = 'direct' THEN avg_pct END) >
                     MAX(CASE WHEN strategy = 'eme' THEN avg_pct END)
                THEN 'direct'
                WHEN MAX(CASE WHEN strategy = 'reverse' THEN avg_pct END) >
                     MAX(CASE WHEN strategy = 'eme' THEN avg_pct END)
                THEN 'reverse'
                ELSE 'eme'
            END as best_strategy
        FROM evaluations
        WHERE run_id = ?
        GROUP BY student_name
        ORDER BY student_name
        """

        results = self.execute_query(query, (run_id,))
        self.print_results(
            results,
            f"Strategy Performance Comparison (Run {run_id})"
        )
        return results

    # ========================================================================
    # 2. COMPARE RUNS OVER TIME
    # ========================================================================

    def compare_runs_over_time(self, strategy: str = None):
        """
        Track performance trends across multiple runs.

        Args:
            strategy: Optional filter by strategy
        """
        query = """
        SELECT
            r.run_timestamp,
            e.strategy,
            COUNT(*) as student_count,
            ROUND(AVG(e.avg_pct), 2) as avg_performance,
            ROUND(MIN(e.avg_pct), 2) as min_performance,
            ROUND(MAX(e.avg_pct), 2) as max_performance,
            ROUND(AVG(e.diff_pct), 2) as avg_disagreement,
            SUM(CASE WHEN e.flag = '🚩' THEN 1 ELSE 0 END) as disagreement_count
        FROM evaluations e
        JOIN runs r ON e.run_id = r.run_id
        """

        params = ()
        if strategy:
            query += " WHERE e.strategy = ?"
            params = (strategy,)

        query += """
        GROUP BY r.run_timestamp, e.strategy
        ORDER BY r.run_timestamp, e.strategy
        """

        results = self.execute_query(query, params)
        title = f"Performance Trends Over Time"
        if strategy:
            title += f" (Strategy: {strategy})"
        self.print_results(results, title)
        return results

    def get_performance_delta_between_runs(self, run_id1: int, run_id2: int, strategy: str):
        """Calculate performance change between two runs for a strategy."""
        query = """
        SELECT
            e1.student_name,
            e1.avg_pct as run1_avg,
            e2.avg_pct as run2_avg,
            ROUND(e2.avg_pct - e1.avg_pct, 2) as delta
        FROM evaluations e1
        JOIN evaluations e2 ON e1.student_name = e2.student_name
        WHERE e1.run_id = ? AND e2.run_id = ?
          AND e1.strategy = ? AND e2.strategy = ?
        ORDER BY delta DESC
        """

        results = self.execute_query(query, (run_id1, run_id2, strategy, strategy))
        self.print_results(
            results,
            f"Performance Delta: Run {run_id1} → Run {run_id2} ({strategy})"
        )
        return results

    # ========================================================================
    # 3. AGGREGATE STATS PER STRATEGY
    # ========================================================================

    def aggregate_stats_per_strategy(self, run_id: int = None):
        """Calculate aggregate statistics for each strategy."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            strategy,
            COUNT(*) as student_count,
            ROUND(AVG(avg_pct), 2) as mean_score,
            ROUND(MIN(avg_pct), 2) as min_score,
            ROUND(MAX(avg_pct), 2) as max_score,
            ROUND(AVG(nano_pct), 2) as avg_nano_score,
            ROUND(AVG(oss_pct), 2) as avg_oss_score,
            ROUND(AVG(diff_pct), 2) as avg_disagreement,
            ROUND(MAX(diff_pct), 2) as max_disagreement,
            SUM(CASE WHEN flag = '🚩' THEN 1 ELSE 0 END) as disagreement_cases
        FROM evaluations
        WHERE run_id = ?
        GROUP BY strategy
        ORDER BY strategy
        """

        results = self.execute_query(query, (run_id,))
        self.print_results(
            results,
            f"Aggregate Statistics by Strategy (Run {run_id})"
        )
        return results

    # ========================================================================
    # 4. MODEL AGREEMENT ANALYSIS
    # ========================================================================

    def find_model_disagreements(self, threshold: float = 10.0, run_id: int = None):
        """
        Find cases where models disagree significantly.

        Args:
            threshold: Percentage point difference threshold
            run_id: Optional run_id (defaults to latest run)
        """
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            student_name,
            strategy,
            nano_pct,
            oss_pct,
            diff_pct,
            flag,
            json_extract(raw_json, '$.metrics.comment') as comment
        FROM evaluations
        WHERE run_id = ? AND ABS(diff_pct) > ?
        ORDER BY ABS(diff_pct) DESC
        """

        results = self.execute_query(query, (run_id, threshold))
        self.print_results(
            results,
            f"Model Disagreements (threshold: {threshold}%, Run {run_id})"
        )
        return results

    def model_agreement_distribution(self, run_id: int = None):
        """Analyze the distribution of model agreements."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            strategy,
            CASE
                WHEN ABS(diff_pct) <= 5 THEN '0-5%'
                WHEN ABS(diff_pct) <= 10 THEN '5-10%'
                WHEN ABS(diff_pct) <= 15 THEN '10-15%'
                WHEN ABS(diff_pct) <= 20 THEN '15-20%'
                ELSE '>20%'
            END as disagreement_range,
            COUNT(*) as count
        FROM evaluations
        WHERE run_id = ?
        GROUP BY strategy, disagreement_range
        ORDER BY strategy, disagreement_range
        """

        results = self.execute_query(query, (run_id,))
        self.print_results(
            results,
            f"Model Agreement Distribution (Run {run_id})"
        )
        return results

    # ========================================================================
    # 5. FIND COMMON MISCONCEPTIONS
    # ========================================================================

    def search_feedback(self, search_term: str, run_id: int = None):
        """
        Search for specific terms in feedback using full-text search.

        Args:
            search_term: Term to search for (supports FTS5 syntax)
            run_id: Optional run_id filter
        """
        query = """
        SELECT
            e.student_name,
            e.strategy,
            e.nano_pct,
            e.oss_pct,
            SUBSTR(e.nano_feedback, 1, 100) as nano_feedback_preview,
            SUBSTR(e.oss_feedback, 1, 100) as oss_feedback_preview
        FROM feedback_fts f
        JOIN evaluations e ON f.rowid = e.eval_id
        WHERE feedback_fts MATCH ?
        """

        params = [search_term]
        if run_id:
            query += " AND e.run_id = ?"
            params.append(run_id)

        query += " ORDER BY rank LIMIT 20"

        results = self.execute_query(query, tuple(params))
        self.print_results(
            results,
            f"Feedback Search: '{search_term}'"
        )
        return results

    def find_common_feedback_patterns(self, run_id: int = None):
        """Find frequently mentioned terms in feedback for low-scoring students."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        # This is a simplified example - you might want to use more sophisticated NLP
        common_terms = ['loop', 'incorrect', 'missing', 'error', 'fail', 'wrong']

        print(f"\n{'=' * 70}")
        print(f"Common Feedback Patterns for Low-Scoring Students (Run {run_id})")
        print('=' * 70)

        for term in common_terms:
            query = """
            SELECT COUNT(*) as count
            FROM evaluations
            WHERE run_id = ?
              AND avg_pct < 70
              AND (nano_feedback LIKE ? OR oss_feedback LIKE ?)
            """

            results = self.execute_query(
                query,
                (run_id, f'%{term}%', f'%{term}%')
            )

            if results and results[0]['count'] > 0:
                print(f"  '{term}': {results[0]['count']} occurrences")

    # ========================================================================
    # 6. RETRIEVE COMPLETE JSON
    # ========================================================================

    def get_full_evaluation(self, student_name: str, strategy: str, run_id: int = None):
        """
        Retrieve complete evaluation JSON for a student.

        Args:
            student_name: Name of the student
            strategy: Evaluation strategy
            run_id: Optional run_id (defaults to latest run)
        """
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT json(raw_json) as evaluation_json
        FROM evaluations
        WHERE student_name = ? AND strategy = ? AND run_id = ?
        """

        results = self.execute_query(query, (student_name, strategy, run_id))

        if results:
            evaluation_data = json.loads(results[0]['evaluation_json'])
            print(f"\n{'=' * 70}")
            print(f"Full Evaluation: {student_name} ({strategy}, Run {run_id})")
            print('=' * 70)
            print(json.dumps(evaluation_data, indent=2))
            return evaluation_data

        return None

    # ========================================================================
    # 7. ADVANCED ANALYSIS
    # ========================================================================

    def identify_struggling_students(self, threshold: float = 60.0, run_id: int = None):
        """Identify students scoring below threshold across all strategies."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            student_name,
            ROUND(AVG(avg_pct), 2) as overall_avg,
            COUNT(CASE WHEN avg_pct < ? THEN 1 END) as strategies_below_threshold
        FROM evaluations
        WHERE run_id = ?
        GROUP BY student_name
        HAVING AVG(avg_pct) < ?
        ORDER BY overall_avg ASC
        """

        results = self.execute_query(query, (threshold, run_id, threshold))
        self.print_results(
            results,
            f"Students Scoring Below {threshold}% (Run {run_id})"
        )
        return results

    def strategy_effectiveness_by_score_range(self, run_id: int = None):
        """Analyze which strategy is most effective for different score ranges."""
        if run_id is None:
            run_id = self.get_latest_run_id()

        query = """
        SELECT
            CASE
                WHEN avg_pct < 50 THEN 'Failing (0-50%)'
                WHEN avg_pct < 70 THEN 'Poor (50-70%)'
                WHEN avg_pct < 85 THEN 'Good (70-85%)'
                ELSE 'Excellent (85-100%)'
            END as score_range,
            strategy,
            COUNT(*) as count,
            ROUND(AVG(diff_pct), 2) as avg_model_disagreement
        FROM evaluations
        WHERE run_id = ?
        GROUP BY score_range, strategy
        ORDER BY score_range, strategy
        """

        results = self.execute_query(query, (run_id,))
        self.print_results(
            results,
            f"Strategy Effectiveness by Score Range (Run {run_id})"
        )
        return results

    # ========================================================================
    # UTILITY METHODS
    # ========================================================================

    def get_latest_run_id(self) -> int:
        """Get the most recent run_id."""
        query = "SELECT run_id FROM runs ORDER BY run_timestamp DESC LIMIT 1"
        results = self.execute_query(query)
        if results:
            return results[0]['run_id']
        raise ValueError("No runs found in database")

    def list_all_runs(self):
        """List all evaluation runs."""
        query = """
        SELECT
            r.run_id,
            r.run_timestamp,
            COUNT(e.eval_id) as evaluation_count,
            json_extract(r.metadata, '$.assignment_type') as assignment_type
        FROM runs r
        LEFT JOIN evaluations e ON r.run_id = e.run_id
        GROUP BY r.run_id
        ORDER BY r.run_timestamp DESC
        """

        results = self.execute_query(query)
        self.print_results(results, "All Evaluation Runs")
        return results


# ============================================================================
# DEMO / CLI INTERFACE
# ============================================================================

def run_demo():
    """Run a demonstration of common queries."""
    print("\n" + "=" * 70)
    print("EME TESTING EVALUATION DATABASE - QUERY EXAMPLES")
    print("=" * 70)

    with EvaluationAnalyzer() as analyzer:
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


if __name__ == "__main__":
    import sys

    if len(sys.argv) > 1:
        # Custom query mode (you can extend this)
        print("Custom query mode not yet implemented. Running demo...")

    run_demo()
