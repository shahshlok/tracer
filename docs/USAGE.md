# Usage Guide

This guide provides practical examples for common tasks and workflows with the EME Testing database system.

## Table of Contents

- [Command-Line Usage](#command-line-usage)
- [Programmatic Usage](#programmatic-usage)
- [Common Analysis Patterns](#common-analysis-patterns)
- [Advanced Queries](#advanced-queries)
- [Workflows](#workflows)

## Command-Line Usage

### Validating Data

#### Validate a Single File
```bash
python3 validate_data.py data/results_direct_2025-11-13T17-52-45.json
```

Output:
```
Validating: data/results_direct_2025-11-13T17-52-45.json
----------------------------------------------------------------------
✓ Validation passed
```

#### Validate with Custom Schema
```bash
python3 validate_data.py data/results_direct_2025-11-13T17-52-45.json --schema custom_schema.json
```

### Loading Data

#### Initialize Database and Load Directory
```bash
python3 run_loader.py data/ --init-db
```

#### Load Additional Files to Existing Database
```bash
python3 run_loader.py data/new_results_*.json
```

#### Load with Custom Database Path
```bash
python3 run_loader.py data/ --db experiments/run1.db --init-db
```

#### Skip Validation (Not Recommended)
```bash
python3 run_loader.py data/ --skip-validation
```

#### Quiet Mode (Minimal Output)
```bash
python3 run_loader.py data/ --quiet
```

### Analyzing Data

#### Run All Demo Queries
```bash
python3 run_analyzer.py
```

#### Find Model Disagreements
```bash
# Default threshold (10%)
python3 run_analyzer.py --query disagreements

# Custom threshold
python3 run_analyzer.py --query disagreements --threshold 15
```

#### Compare Strategies
```bash
# All students
python3 run_analyzer.py --query strategies

# Specific student
python3 run_analyzer.py --query strategies --student "Smith_John_123456"
```

#### Find Common Misconceptions
```bash
python3 run_analyzer.py --query misconceptions
```

#### Analyze Trends Over Time
```bash
python3 run_analyzer.py --query trends
```

#### Analyze Specific Run
```bash
python3 run_analyzer.py --run-id 1 --query disagreements
```

#### Custom Database Path
```bash
python3 run_analyzer.py --db experiments/run1.db
```

## Programmatic Usage

### Loading Data

#### Basic Loading
```python
from db import EvaluationLoader

with EvaluationLoader("evaluations.db") as loader:
    # Initialize database schema
    loader.init_database()

    # Load a single file
    inserted, skipped, errors = loader.load_evaluation_file(
        "data/results_direct_2025-11-13T17-52-45.json"
    )

    print(f"Inserted: {inserted}, Skipped: {skipped}, Errors: {errors}")
```

#### Loading Directory with Metadata
```python
from db import EvaluationLoader, RunMetadata

metadata = RunMetadata(
    assignment_type="loops_assignment",
    rubric_version="v2.0",
    model_versions={
        "gpt5_nano": "2025.01",
        "gpt_oss_120b": "2025.01"
    },
    experiment_id="exp_123",
    notes="Testing new rubric"
)

with EvaluationLoader("evaluations.db") as loader:
    stats = loader.load_directory(
        "data/",
        pattern="results_*.json",
        run_metadata=metadata.to_dict()
    )

    stats.print_summary()
```

### Validation

#### Validate Before Loading
```python
from db import EvaluationValidator

validator = EvaluationValidator()

# Validate single evaluation
evaluation_data = {...}  # Your JSON data
result = validator.validate_evaluation(evaluation_data)

if result.is_valid:
    print("✓ Valid")
else:
    print("✗ Invalid:")
    for error in result.errors:
        print(f"  - {error}")

# Validate batch
evaluations = [...]  # List of evaluations
results, valid_count, invalid_count = validator.validate_evaluation_batch(
    evaluations,
    fail_fast=False
)

print(f"Valid: {valid_count}, Invalid: {invalid_count}")
```

#### Custom Schema
```python
from db import EvaluationValidator

validator = EvaluationValidator(schema_path="custom_schema.json")
result = validator.validate_evaluation(evaluation_data)
```

### Analysis

#### Basic Queries
```python
from db import EvaluationAnalyzer

with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Get latest run ID
    run_id = analyzer.get_latest_run_id()

    # List all runs
    runs = analyzer.list_all_runs()

    # Compare strategies for a student
    results = analyzer.compare_strategies_for_student(
        "Smith_John_123456",
        run_id
    )

    # Find disagreements
    results = analyzer.find_model_disagreements(
        threshold=10.0,
        run_id=run_id
    )

    # Get aggregate stats
    stats = analyzer.aggregate_stats_per_strategy(run_id)
```

#### Full JSON Retrieval
```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Get complete evaluation as dictionary
    evaluation = analyzer.get_full_evaluation(
        student_name="Smith_John_123456",
        strategy="direct",
        run_id=1
    )

    # Access specific fields
    print(f"Nano Score: {evaluation['metrics']['gpt5_nano']['pct']}")
    print(f"Feedback: {evaluation['gpt5_nano_result']['overall_feedback']}")
```

#### Custom Queries
```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Execute custom SQL
    query = """
    SELECT student_name, strategy, avg_pct
    FROM evaluations
    WHERE avg_pct BETWEEN 70 AND 85
    ORDER BY avg_pct DESC
    """

    results = analyzer.execute_query(query)
    analyzer.print_results(results, "Students in Good Range (70-85%)")
```

## Common Analysis Patterns

### Pattern 1: Compare Strategies for All Students

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    run_id = analyzer.get_latest_run_id()

    # Get comparison for all students
    results = analyzer.compare_all_students_across_strategies(run_id)

    # Find which strategy performs best
    for row in results:
        student = row['student_name']
        best = row['best_strategy']
        best_score = max(
            row['direct_avg'] or 0,
            row['reverse_avg'] or 0,
            row['eme_avg'] or 0
        )
        print(f"{student}: Best={best} ({best_score}%)")
```

### Pattern 2: Track Performance Over Time

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Get performance trends for a specific strategy
    results = analyzer.compare_runs_over_time(strategy="eme")

    # Plot or analyze trends
    timestamps = [row['run_timestamp'] for row in results]
    performances = [row['avg_performance'] for row in results]

    print("EME Strategy Performance Over Time:")
    for ts, perf in zip(timestamps, performances):
        print(f"  {ts}: {perf:.2f}%")
```

### Pattern 3: Find Common Misconceptions

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    run_id = analyzer.get_latest_run_id()

    # Search for specific misconceptions
    misconceptions = [
        "loop condition",
        "off by one",
        "incorrect sum",
        "missing semicolon"
    ]

    for term in misconceptions:
        results = analyzer.search_feedback(term, run_id)
        if results:
            print(f"\n'{term}' found in {len(results)} evaluations:")
            for row in results:
                print(f"  - {row['student_name']} ({row['strategy']})")
```

### Pattern 4: Identify Struggling Students

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    run_id = analyzer.get_latest_run_id()

    # Find students scoring below 60% across all strategies
    results = analyzer.identify_struggling_students(
        threshold=60.0,
        run_id=run_id
    )

    for row in results:
        student = row['student_name']
        avg = row['overall_avg']
        strategies_below = row['strategies_below_threshold']

        print(f"{student}: {avg:.1f}% avg, {strategies_below}/3 strategies below threshold")
```

### Pattern 5: Model Agreement Analysis

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    run_id = analyzer.get_latest_run_id()

    # Get distribution of agreements
    distribution = analyzer.model_agreement_distribution(run_id)

    print("Model Agreement Distribution:")
    current_strategy = None
    for row in distribution:
        if row['strategy'] != current_strategy:
            current_strategy = row['strategy']
            print(f"\n{current_strategy}:")

        print(f"  {row['disagreement_range']}: {row['count']} students")
```

## Advanced Queries

### Direct SQL Access

```python
import sqlite3
import json

conn = sqlite3.connect("evaluations.db")
conn.row_factory = sqlite3.Row
cursor = conn.cursor()

# Query with JSON extraction
cursor.execute("""
    SELECT
        student_name,
        strategy,
        json_extract(raw_json, '$.gpt5_nano_result.total_score') as nano_score,
        json_extract(raw_json, '$.gpt_oss_120b_result.total_score') as oss_score,
        json_extract(raw_json, '$.metrics.comment') as comment
    FROM evaluations
    WHERE run_id = 1 AND avg_pct > 90
    ORDER BY avg_pct DESC
""")

for row in cursor.fetchall():
    print(dict(row))

conn.close()
```

### Full-Text Search with Ranking

```python
conn = sqlite3.connect("evaluations.db")
cursor = conn.cursor()

# Search for multiple terms with ranking
cursor.execute("""
    SELECT
        e.student_name,
        e.strategy,
        e.avg_pct,
        e.nano_feedback,
        rank
    FROM feedback_fts f
    JOIN evaluations e ON f.rowid = e.eval_id
    WHERE feedback_fts MATCH 'loop AND (incorrect OR missing OR wrong)'
    ORDER BY rank
    LIMIT 10
""")

results = cursor.fetchall()
print(f"Found {len(results)} relevant evaluations")

conn.close()
```

### Temporal Delta Analysis

```python
with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Compare two specific runs
    delta = analyzer.get_performance_delta_between_runs(
        run_id1=1,
        run_id2=2,
        strategy="direct"
    )

    print("Performance Changes (Run 1 → Run 2):")
    for row in delta:
        student = row['student_name']
        change = row['delta']
        symbol = "↑" if change > 0 else "↓" if change < 0 else "="
        print(f"  {student}: {symbol} {abs(change):.1f}%")
```

### Custom Aggregations

```python
conn = sqlite3.connect("evaluations.db")
cursor = conn.cursor()

# Find average performance by score range
cursor.execute("""
    SELECT
        CASE
            WHEN avg_pct < 50 THEN 'Failing'
            WHEN avg_pct < 70 THEN 'Poor'
            WHEN avg_pct < 85 THEN 'Good'
            ELSE 'Excellent'
        END as grade_range,
        strategy,
        COUNT(*) as student_count,
        ROUND(AVG(diff_pct), 2) as avg_model_disagreement,
        ROUND(MIN(avg_pct), 2) as min_score,
        ROUND(MAX(avg_pct), 2) as max_score
    FROM evaluations
    WHERE run_id = 1
    GROUP BY grade_range, strategy
    ORDER BY
        CASE grade_range
            WHEN 'Failing' THEN 1
            WHEN 'Poor' THEN 2
            WHEN 'Good' THEN 3
            WHEN 'Excellent' THEN 4
        END,
        strategy
""")

for row in cursor.fetchall():
    print(row)

conn.close()
```

## Workflows

### Workflow 1: Weekly Evaluation Analysis

```python
#!/usr/bin/env python3
"""Weekly evaluation analysis workflow."""

from db import EvaluationLoader, EvaluationAnalyzer
from pathlib import Path
from datetime import datetime

# 1. Load new data
data_dir = Path("data")
db_path = "evaluations.db"

with EvaluationLoader(db_path) as loader:
    # Load all new files (skip duplicates automatically)
    stats = loader.load_directory(data_dir)
    stats.print_summary()

# 2. Analyze latest run
with EvaluationAnalyzer(db_path) as analyzer:
    run_id = analyzer.get_latest_run_id()

    # Strategy comparison
    print("\n=== Strategy Performance ===")
    analyzer.aggregate_stats_per_strategy(run_id)

    # Model disagreements
    print("\n=== Model Disagreements (>10%) ===")
    analyzer.find_model_disagreements(threshold=10.0, run_id=run_id)

    # Struggling students
    print("\n=== Students Needing Support (<60%) ===")
    analyzer.identify_struggling_students(threshold=60.0, run_id=run_id)

    # Common issues
    print("\n=== Common Misconceptions ===")
    analyzer.find_common_feedback_patterns(run_id)

print(f"\nAnalysis completed: {datetime.now()}")
```

### Workflow 2: Batch Comparison

```python
"""Compare multiple runs to find trends."""

from db import EvaluationAnalyzer

with EvaluationAnalyzer("evaluations.db") as analyzer:
    # Get all runs
    runs = analyzer.list_all_runs()

    print("Comparing all runs for EME strategy:")

    for strategy in ["direct", "reverse", "eme"]:
        print(f"\n=== {strategy.upper()} Strategy ===")

        results = analyzer.compare_runs_over_time(strategy=strategy)

        for row in results:
            ts = row['run_timestamp']
            perf = row['avg_performance']
            disagreements = row['disagreement_count']

            print(f"{ts}: {perf:.1f}% avg, {disagreements} disagreements")
```

### Workflow 3: Export for External Analysis

```python
"""Export data to CSV/JSON for external tools."""

import sqlite3
import csv
import json

conn = sqlite3.connect("evaluations.db")
conn.row_factory = sqlite3.Row
cursor = conn.cursor()

# Export to CSV
cursor.execute("""
    SELECT
        student_name,
        strategy,
        nano_pct,
        oss_pct,
        avg_pct,
        diff_pct,
        flag
    FROM evaluations
    WHERE run_id = 1
""")

with open("export.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["student", "strategy", "nano_pct", "oss_pct", "avg_pct", "diff_pct", "flag"])

    for row in cursor.fetchall():
        writer.writerow(row)

# Export full JSON
cursor.execute("""
    SELECT json(raw_json) as data
    FROM evaluations
    WHERE run_id = 1
""")

evaluations = [json.loads(row[0]) for row in cursor.fetchall()]

with open("export.json", "w") as f:
    json.dump(evaluations, f, indent=2)

print("✓ Exported to export.csv and export.json")
conn.close()
```

## Tips and Best Practices

### 1. Always Validate Before Loading
```python
from db import validate_json_file

# Validate first
result = validate_json_file("data/new_data.json")
if not result.is_valid:
    print(result.get_details())
    exit(1)

# Then load
# ...
```

### 2. Use Transactions for Batch Operations
```python
with EvaluationLoader("evaluations.db") as loader:
    # Automatic transaction management
    stats = loader.load_directory("data/")
    # Committed automatically if no errors
```

### 3. Leverage Indexes with Virtual Columns
```sql
-- ✓ Good: Uses index
SELECT * FROM evaluations WHERE avg_pct > 80;

-- ✗ Bad: Doesn't use index
SELECT * FROM evaluations WHERE json_extract(raw_json, '$.metrics.avg_pct') > 80;
```

### 4. Use Views for Common Queries
```sql
-- Create a custom view
CREATE VIEW high_performers AS
SELECT student_name, strategy, avg_pct
FROM evaluations
WHERE avg_pct >= 85;

-- Query the view
SELECT * FROM high_performers;
```

### 5. Regular Maintenance
```python
import sqlite3

conn = sqlite3.connect("evaluations.db")

# Optimize database
conn.execute("VACUUM")
conn.execute("ANALYZE")

# Rebuild FTS index if needed
conn.execute("INSERT INTO feedback_fts(feedback_fts) VALUES ('rebuild')")

conn.close()
```

## Next Steps

- Explore custom queries for your specific analysis needs
- Create automated workflows for regular evaluations
- Build visualization dashboards using the analysis results
- Extend the database schema for additional metrics

See [DATABASE.md](DATABASE.md) for technical details and [SETUP.md](SETUP.md) for installation instructions.
