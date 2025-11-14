# Database Architecture

## Overview

The EME Testing database system uses SQLite 3.51.0+ with JSONB format for storing and analyzing student evaluation data. This document covers the technical architecture, schema design, and implementation details.

## Requirements

### System Requirements
- **Python**: 3.10 or higher
- **SQLite**: 3.51.0 or higher (released November 2025)
  - Includes JSONB binary format support (since 3.45.0)
  - Includes `jsonb_each()` and `jsonb_tree()` functions (added in 3.51.0)
- **Package Manager**: uv (recommended for November 2025)

### Python Dependencies
- `jsonschema>=4.20.0` - JSON schema validation

## Schema Design Philosophy

The database uses a **Pure JSONB with Virtual Columns** approach:

### Design Principles

1. **Zero Data Duplication**
   - All evaluation data stored once as JSONB
   - Virtual columns compute values on-demand from JSON
   - No redundancy between structured columns and JSON

2. **Performance Optimized**
   - JSONB is 2-5× faster than text JSON
   - Virtual columns are indexed for O(log N) lookups
   - Partial indexes reduce storage overhead

3. **Flexible & Future-Proof**
   - JSON structure can evolve without migrations
   - Metadata JSONB tracks changing requirements
   - No ALTER TABLE needed for new fields

4. **Integrity Enforced**
   - Multi-layer validation (schema + business logic + database constraints)
   - Foreign keys with cascade delete
   - Check constraints for data validity

## Database Schema

### Tables

#### `runs` Table
Stores metadata for each evaluation batch.

```sql
CREATE TABLE runs (
    run_id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_timestamp TEXT NOT NULL UNIQUE,
    metadata BLOB  -- JSONB format
);
```

**Metadata Structure** (JSONB):
```json
{
  "assignment_type": "sum_calculator",
  "rubric_version": "v1.0",
  "model_versions": {
    "gpt5_nano": "2025.01",
    "gpt_oss_120b": "2025.01"
  },
  "experiment_id": "exp_123",
  "notes": "Optional notes"
}
```

#### `evaluations` Table
Stores individual student evaluations.

```sql
CREATE TABLE evaluations (
    eval_id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id INTEGER NOT NULL,
    student_name TEXT NOT NULL,
    strategy TEXT NOT NULL CHECK(strategy IN ('direct', 'reverse', 'eme')),
    raw_json BLOB NOT NULL,  -- JSONB format

    -- Virtual generated columns (not stored)
    nano_pct REAL GENERATED ALWAYS AS (...) VIRTUAL,
    oss_pct REAL GENERATED ALWAYS AS (...) VIRTUAL,
    avg_pct REAL GENERATED ALWAYS AS (...) VIRTUAL,
    diff_pct REAL GENERATED ALWAYS AS (...) VIRTUAL,
    flag TEXT GENERATED ALWAYS AS (...) VIRTUAL,
    nano_feedback TEXT GENERATED ALWAYS AS (...) VIRTUAL,
    oss_feedback TEXT GENERATED ALWAYS AS (...) VIRTUAL,

    UNIQUE(run_id, student_name, strategy)
);
```

**Evaluation JSON Structure** (JSONB):
```json
{
  "student": "LastName_FirstName_ID",
  "gpt5_nano_result": {
    "total_score": 100,
    "max_possible_score": 100,
    "overall_feedback": "..."
  },
  "gpt_oss_120b_result": {
    "total_score": 95,
    "max_possible_score": 100,
    "overall_feedback": "..."
  },
  "metrics": {
    "gpt5_nano": {"total": 100.0, "max": 100.0, "pct": 100.0},
    "gpt_oss_120b": {"total": 95.0, "max": 100.0, "pct": 95.0},
    "avg_pct": 97.5,
    "diff_pct": 5.0,
    "flag": "✅",
    "comment": "Models agree within tolerance"
  }
}
```

#### `feedback_fts` Table (FTS5 Virtual Table)
Full-text search index for feedback analysis.

```sql
CREATE VIRTUAL TABLE feedback_fts USING fts5(
    eval_id UNINDEXED,
    student_name,
    strategy,
    nano_feedback,
    oss_feedback,
    content=evaluations,
    content_rowid=eval_id
);
```

### Indexes

| Index Name | Columns | Purpose |
|------------|---------|---------|
| `idx_run_timestamp` | `run_timestamp` | Query runs by date |
| `idx_eval_run_student` | `run_id, student_name` | Student-focused queries |
| `idx_eval_strategy` | `strategy` | Strategy-based filtering |
| `idx_eval_student_name` | `student_name` | Cross-run student lookups |
| `idx_eval_nano_pct` | `nano_pct` | Score-based queries (partial) |
| `idx_eval_oss_pct` | `oss_pct` | Score-based queries (partial) |
| `idx_eval_avg_pct` | `avg_pct` | Average score queries (partial) |
| `idx_eval_diff_pct` | `diff_pct` | Disagreement analysis (partial) |
| `idx_eval_flag` | `flag` | Flag-based filtering (partial) |

**Note**: Partial indexes (`WHERE ... IS NOT NULL`) reduce index size while maintaining query performance for non-NULL values.

### Views

#### `latest_run` View
Summary of the most recent evaluation run.

```sql
SELECT
    r.run_id,
    r.run_timestamp,
    e.strategy,
    COUNT(*) as student_count,
    AVG(e.avg_pct) as avg_performance,
    AVG(e.diff_pct) as avg_disagreement,
    SUM(CASE WHEN e.flag = '🚩' THEN 1 END) as disagreement_count
FROM runs r
JOIN evaluations e ON r.run_id = e.run_id
WHERE r.run_id = (SELECT run_id FROM runs ORDER BY run_timestamp DESC LIMIT 1)
GROUP BY e.strategy;
```

#### `student_summary` View
Performance summary across all strategies for each student.

```sql
SELECT
    e.student_name,
    e.run_id,
    MAX(CASE WHEN e.strategy = 'direct' THEN e.avg_pct END) as direct_avg,
    MAX(CASE WHEN e.strategy = 'reverse' THEN e.avg_pct END) as reverse_avg,
    MAX(CASE WHEN e.strategy = 'eme' THEN e.avg_pct END) as eme_avg,
    AVG(e.avg_pct) as overall_avg
FROM evaluations e
GROUP BY e.student_name, e.run_id;
```

## JSONB Format

### What is JSONB?

JSONB is SQLite's internal binary representation of JSON, stored on disk as a BLOB. Introduced in SQLite 3.45.0, it provides:

- **Faster Processing**: 2-5× faster than text JSON (no parsing overhead)
- **Smaller Storage**: ~18% smaller than equivalent text JSON
- **Compatibility**: Any function accepting text JSON also accepts JSONB

### JSONB Functions (SQLite 3.51.0+)

| Function | Purpose | Example |
|----------|---------|---------|
| `jsonb(json_text)` | Convert text JSON to JSONB | `jsonb('{"a":1}')` |
| `json(jsonb_blob)` | Convert JSONB to text JSON | `json(raw_json)` |
| `jsonb_extract()` | Extract value (returns JSONB for objects/arrays) | `jsonb_extract(raw_json, '$.student')` |
| `jsonb_each()` | Iterate object/array (new in 3.51.0) | `SELECT * FROM jsonb_each(raw_json)` |
| `jsonb_tree()` | Recursive tree traversal (new in 3.51.0) | `SELECT * FROM jsonb_tree(raw_json)` |

### JSON Path Operators

SQLite 3.38.0+ supports `->` and `->>` operators:

```sql
-- Extract as JSON
SELECT raw_json -> '$.metrics.gpt5_nano.pct' FROM evaluations;

-- Extract as SQL value
SELECT raw_json ->> '$.student' FROM evaluations;
```

## Virtual Columns

Virtual generated columns compute values on-the-fly from JSONB data without storing them.

### Benefits

1. **Zero Storage Overhead**: Values computed during query execution
2. **Always Synchronized**: Impossible for extracted values to drift from source JSON
3. **Indexable**: Can create indexes on virtual columns for fast lookups
4. **Transparent**: Use like regular columns in WHERE, ORDER BY, SELECT

### Example

```sql
-- Definition
nano_pct REAL GENERATED ALWAYS AS (
    json_extract(raw_json, '$.metrics.gpt5_nano.pct')
) VIRTUAL

-- Usage (uses index for fast lookup)
SELECT student_name, nano_pct
FROM evaluations
WHERE nano_pct > 80;
```

### Performance Considerations

- **Indexed queries**: O(log N) using the index (fast)
- **Unindexed queries**: O(N) with JSON extraction per row (slower)
- **Best practice**: Always use virtual column name in WHERE/ORDER BY clauses to leverage indexes

## Validation

### Multi-Layer Validation

1. **JSON Schema Validation** (application layer)
   - Validates structure and required fields
   - Checks data types and value ranges
   - Uses `jsonschema` package

2. **Business Logic Validation** (application layer)
   - Score consistency (total ≤ max)
   - Metrics calculation accuracy
   - Flag/comment consistency

3. **Database Constraints** (database layer)
   - `CHECK(json_valid(raw_json))` - syntax validation
   - `CHECK(strategy IN ('direct', 'reverse', 'eme'))` - enum validation
   - `UNIQUE(run_id, student_name, strategy)` - uniqueness
   - `FOREIGN KEY` - referential integrity

### Validation Flow

```
JSON File → Schema Validator → Business Logic Validator → Database Insert
                ↓                     ↓                          ↓
            Structure OK          Calculations OK           Constraints OK
```

## Performance Characteristics

Based on testing with 50 students × 3 strategies:

| Operation | Time | Notes |
|-----------|------|-------|
| Insert 150 evaluations | ~50ms | Batched in transaction with indexes |
| Query single student (all strategies) | <1ms | Uses `idx_eval_student_name` |
| Aggregate stats (50 students) | ~5ms | Scans 150 rows, computes AVG |
| Model disagreement query | ~2ms | Uses `idx_eval_diff_pct` |
| Full-text search | ~10ms | FTS5 search across 150 feedback texts |
| Retrieve full JSON | <0.5ms | Direct ROWID lookup |

### Scaling Projections

| Scale | Rows | DB Size | Typical Query Time |
|-------|------|---------|-------------------|
| 50 students × 3 strategies × 1 run | 150 | ~250 KB | <5ms |
| 50 students × 3 strategies × 100 runs | 15,000 | ~18 MB | <20ms |
| 50 students × 3 strategies × 1,000 runs | 150,000 | ~180 MB | <100ms |

## Storage Breakdown

For 100 runs (50 students × 3 strategies each):

| Component | Size | Percentage |
|-----------|------|------------|
| Raw JSONB data | ~10 MB | 56% |
| Indexes | ~3 MB | 17% |
| FTS5 table | ~5 MB | 28% |
| **Total** | **~18 MB** | **100%** |

**Savings vs Text JSON**: ~14% (21 MB → 18 MB)

## Query Patterns

### Common Query Examples

#### 1. Compare Strategies for Student
```sql
SELECT student_name, strategy, nano_pct, oss_pct, avg_pct
FROM evaluations
WHERE student_name = ? AND run_id = ?
ORDER BY strategy;
```

#### 2. Find Model Disagreements
```sql
SELECT student_name, strategy, nano_pct, oss_pct, diff_pct
FROM evaluations
WHERE ABS(diff_pct) > 10
ORDER BY diff_pct DESC;
```

#### 3. Full-Text Search for Misconceptions
```sql
SELECT e.student_name, e.nano_feedback
FROM feedback_fts f
JOIN evaluations e ON f.rowid = e.eval_id
WHERE feedback_fts MATCH 'loop AND incorrect'
ORDER BY rank;
```

#### 4. Temporal Analysis
```sql
SELECT r.run_timestamp, e.strategy, AVG(e.avg_pct) as performance
FROM evaluations e
JOIN runs r ON e.run_id = r.run_id
GROUP BY r.run_timestamp, e.strategy
ORDER BY r.run_timestamp;
```

## Maintenance

### Backup

```bash
# SQLite database is a single file
cp evaluations.db evaluations_backup_$(date +%Y%m%d).db

# Or use SQLite backup API
sqlite3 evaluations.db ".backup evaluations_backup.db"
```

### Optimization

```sql
-- Rebuild indexes and optimize
VACUUM;
ANALYZE;

-- Rebuild FTS index
INSERT INTO feedback_fts(feedback_fts) VALUES ('rebuild');
```

### Schema Migrations

Version tracking via `schema_version` table:

```sql
SELECT version, applied_at FROM schema_version;
```

For migrations, create numbered SQL files:
- `migrations/001_initial_schema.sql`
- `migrations/002_add_new_virtual_column.sql`

## Troubleshooting

### Common Issues

**Issue**: "no such function: jsonb"
- **Cause**: SQLite version < 3.45.0
- **Solution**: Upgrade SQLite to 3.51.0+

**Issue**: Virtual columns not using indexes
- **Cause**: Query uses `json_extract()` directly instead of column name
- **Solution**: Use virtual column name in WHERE clause

**Issue**: UNIQUE constraint violation
- **Cause**: Duplicate (run_id, student_name, strategy)
- **Solution**: Delete existing record or skip duplicate

**Issue**: Slow full-text search
- **Cause**: FTS index needs rebuilding
- **Solution**: `INSERT INTO feedback_fts(feedback_fts) VALUES ('rebuild');`

## References

- [SQLite JSON Functions](https://sqlite.org/json1.html)
- [SQLite JSONB Format](https://sqlite.org/jsonb.html)
- [SQLite FTS5](https://sqlite.org/fts5.html)
- [SQLite Generated Columns](https://sqlite.org/gencol.html)
