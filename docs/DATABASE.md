# Database System Documentation

## Overview

The EduBench evaluation system includes a lightweight SQLite database for persistent storage of evaluation results. The database automatically captures all benchmark runs, enabling historical analysis, reproducibility, and data management without modifying the existing JSON-based workflow.

**Design Philosophy**: "Validated JSON in, validated JSON out"
The database acts as a persistent store without changing how evaluations are created or analyzed. JSON files remain the primary interface for ad-hoc analysis.

---

## Architecture

### Components

```
EME_testing/
├── db/
│   ├── __init__.py          # Package initialization
│   ├── schema.sql           # SQLite schema definition
│   └── manager.py           # Database manager (all DB operations)
├── evaluation_schema.json   # JSON schema for validation
├── evaluations.db           # SQLite database file (created automatically)
└── data/                    # JSON files (temporary workspace)
```

### Database Schema

#### `runs` Table
Tracks each evaluation run (one row per JSON file).

| Column         | Type    | Description                                      |
|----------------|---------|--------------------------------------------------|
| `run_id`       | INTEGER | Primary key (auto-increment)                     |
| `run_timestamp`| TEXT    | ISO timestamp from filename (e.g., "2025-11-13T17-52-45") |
| `source_file`  | TEXT    | Original filename (e.g., "results_direct_2025-11-13T17-52-45.json") |
| `strategy`     | TEXT    | Grading strategy: "direct", "reverse", or "eme"  |
| `notes`        | TEXT    | Optional manual notes (currently unused)         |

#### `evaluations` Table
Stores individual student evaluations (one row per student per run).

| Column         | Type    | Description                                      |
|----------------|---------|--------------------------------------------------|
| `eval_id`      | INTEGER | Primary key (auto-increment)                     |
| `run_id`       | INTEGER | Foreign key to `runs.run_id` (ON DELETE CASCADE) |
| `student_name` | TEXT    | Student identifier from evaluation["student"]    |
| `strategy`     | TEXT    | Grading strategy (duplicated for convenience)    |
| `raw_json`     | TEXT    | Complete evaluation JSON as text                 |

**Unique Constraint**: `(run_id, student_name, strategy)` - Prevents duplicate evaluations.

**Indexes**:
- `idx_evaluations_run` on `run_id` (fast lookup by run)
- `idx_evaluations_student` on `student_name` (fast lookup by student)

---

## Validation

### JSON Schema Validation

All evaluation data is validated against `evaluation_schema.json` before ingestion:

- **Schema checks**: Field types, required fields, structure
- **Business rules**: Metrics calculations (avg_pct, diff_pct must match computed values)
- **Student format**: Non-empty student identifiers

Validation uses the existing `utils.validation` module with `jsonschema` package.

**Validation Result Example**:
```
✓ Validation passed
OR
✗ Validation failed with 2 error(s)

Errors:
  1. avg_pct (57.5) doesn't match calculated average (58.00)
  2. Record 3 (student: Smith_John_123456): Schema validation failed
```

If validation fails, the entire file is rejected (no partial ingestion).

---

## Module: `db/manager.py`

### Class: `DatabaseManager`

Main interface for all database operations.

#### Constructor

```python
DatabaseManager(db_path: str = "evaluations.db")
```

**Parameters**:
- `db_path`: Path to SQLite database file (default: "evaluations.db" at project root)

#### Methods

##### `init_db()`

Initialize the database by creating tables if they don't exist.

```python
manager = DatabaseManager()
manager.init_db()
```

- Reads and executes `db/schema.sql`
- Safe to call multiple times (uses `CREATE TABLE IF NOT EXISTS`)
- Creates database file if it doesn't exist

**Raises**:
- `FileNotFoundError` if `schema.sql` is missing

---

##### `ingest_results_file(file_path: Path)`

Ingest a results JSON file into the database with validation.

```python
manager = DatabaseManager()
manager.ingest_results_file(Path("data/results_direct_2025-11-13T17-52-45.json"))
```

**Workflow**:
1. Parse strategy and timestamp from filename
2. Validate JSON against schema
3. If valid: Insert run record + all evaluation records
4. If invalid: Abort (no partial insert)

**Parameters**:
- `file_path`: Path to JSON file (must match format: `results_{strategy}_{timestamp}.json`)

**Raises**:
- `FileNotFoundError` if file doesn't exist
- `ValueError` if validation fails or filename format is invalid

**Logs**:
- INFO: Success message with record count
- WARNING: Schema validation warnings
- ERROR: Validation failures

---

##### `restore_json_files(data_dir: Path, validate: bool = False)`

Restore all JSON files from the database to a directory.

```python
manager = DatabaseManager()
files_restored = manager.restore_json_files(Path("data"), validate=False)
print(f"Restored {files_restored} files")
```

**Workflow**:
1. Query all runs from database (ordered by timestamp, strategy)
2. For each run: Query all evaluations
3. Reconstruct JSON array
4. Write to `data_dir/source_file`

**Parameters**:
- `data_dir`: Directory to write JSON files (created if doesn't exist)
- `validate`: If True, re-validate records before writing (currently unimplemented)

**Returns**:
- Number of files restored

**Output**:
- Files use original `source_file` names
- Evaluations sorted by `student_name` (may differ from original order)
- Content is identical to original (when sorted)

---

### Convenience Functions

For simpler imports and backward compatibility:

```python
from db.manager import init_db, ingest_results_file, restore_json_files

# Initialize database
init_db("evaluations.db")

# Ingest a file
ingest_results_file("evaluations.db", Path("data/results_direct_2025-11-13T17-52-45.json"))

# Restore all files
files_restored = restore_json_files("evaluations.db", Path("data"), validate=False)
```

---

## Integration with CLI

### Automatic Ingestion

The database is **automatically populated** during benchmark runs:

```python
# In cli.py _save_results()
def _save_results(mode: str, results: List[Dict[str, Any]]) -> None:
    # 1. Save JSON to data/
    output_path = data_dir / f"results_{mode}_{iso_date}.json"
    output_path.write_text(json.dumps(cleaned_results, indent=2))

    # 2. Automatically ingest into database
    db_manager.init_db(DB_PATH)
    db_manager.ingest_results_file(DB_PATH, output_path)
```

**User Experience**:
- Users run: `uv run bench benchmark`
- Results saved to both `data/` (temporary) and database (persistent)
- No manual database commands required

**Error Handling**:
- If database ingestion fails, a warning is printed but the benchmark continues
- JSON files are always saved regardless of database status

---

### Analysis Menu

Access database features through the CLI:

```bash
uv run bench benchmark
# Select option [5] Analysis
# Select option [1] Restore JSON from Database
```

**Implementation** (`cli.py`):

```python
def _restore_json_from_db() -> None:
    """Restore JSON files from the database to the data/ directory."""
    try:
        data_dir = Path("data")
        files_restored = db_manager.restore_json_files(DB_PATH, data_dir, validate=False)
        console.print(f"[green]✓[/green] Successfully restored {files_restored} file(s)")
    except FileNotFoundError:
        console.print("[yellow]No database found. Run a benchmark first.[/yellow]")
    except Exception as e:
        console.print(f"[red]Error restoring files: {e}[/red]")
```

---

## Usage Examples

### Example 1: Manual Database Operations

```python
from pathlib import Path
from db.manager import DatabaseManager

# Initialize database
db = DatabaseManager("evaluations.db")
db.init_db()

# Ingest a single file
json_file = Path("data/results_direct_2025-11-13T17-52-45.json")
db.ingest_results_file(json_file)
```

### Example 2: Bulk Ingestion

```python
from pathlib import Path
from db.manager import DatabaseManager

db = DatabaseManager()
db.init_db()

# Ingest all JSON files in data/
data_dir = Path("data")
for json_file in data_dir.glob("results_*.json"):
    try:
        db.ingest_results_file(json_file)
        print(f"✓ Ingested {json_file.name}")
    except ValueError as e:
        print(f"✗ Failed {json_file.name}: {e}")
```

### Example 3: Restore and Analyze

```python
from pathlib import Path
from db.manager import restore_json_files
import json

# Restore all files from database
restore_json_files("evaluations.db", Path("data"))

# Now do ad-hoc analysis on the files
for json_file in Path("data").glob("results_*.json"):
    with json_file.open() as f:
        data = json.load(f)
    print(f"{json_file.name}: {len(data)} evaluations")
```

---

## Filename Format

The database parses run metadata from filenames:

**Format**: `results_{strategy}_{timestamp}.json`

**Examples**:
- `results_direct_2025-11-13T17-52-45.json`
  - Strategy: `direct`
  - Timestamp: `2025-11-13T17-52-45`

- `results_eme_2025-11-14T01-12-57.json`
  - Strategy: `eme`
  - Timestamp: `2025-11-14T01-12-57`

**Valid Strategies**: `direct`, `reverse`, `eme`

**Timestamp Format**: ISO 8601 with colons replaced by hyphens (generated by `datetime.now().isoformat(timespec="seconds").replace(":", "-")`)

If filename doesn't match format, `_parse_filename()` raises `ValueError`.

---

## Database Queries

### Query All Runs

```sql
SELECT run_id, run_timestamp, source_file, strategy
FROM runs
ORDER BY run_timestamp, strategy;
```

### Query Evaluations for a Run

```sql
SELECT student_name, raw_json
FROM evaluations
WHERE run_id = ?
ORDER BY student_name;
```

### Count Evaluations by Strategy

```sql
SELECT strategy, COUNT(*) as count
FROM evaluations
GROUP BY strategy;
```

### Find All Evaluations for a Student

```sql
SELECT r.run_timestamp, r.strategy, e.raw_json
FROM evaluations e
JOIN runs r ON e.run_id = r.run_id
WHERE e.student_name = 'Smith_John_123456'
ORDER BY r.run_timestamp;
```

---

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Benchmark Execution                     │
│                  (uv run bench benchmark)                │
└────────────────────────────┬────────────────────────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │  Grading Mode  │
                    │  (direct/      │
                    │   reverse/eme) │
                    └────────┬───────┘
                             │
                             ▼
                    ┌────────────────┐
                    │  _save_results │
                    └────────┬───────┘
                             │
                ┌────────────┴────────────┐
                ▼                         ▼
       ┌─────────────────┐      ┌─────────────────┐
       │  Save JSON to   │      │   Ingest into   │
       │  data/ dir      │      │   SQLite DB     │
       │  (temporary)    │      │  (persistent)   │
       └─────────────────┘      └─────────────────┘
                                          │
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    ▼                     ▼                     ▼
            ┌──────────────┐      ┌──────────────┐    ┌──────────────┐
            │ Validate via │      │ Insert into  │    │ Insert into  │
            │ JSON Schema  │──OK──│ runs table   │    │ evaluations  │
            └──────────────┘      └──────────────┘    │ table        │
                    │                                 └──────────────┘
                    │
                  FAIL
                    │
                    ▼
            ┌──────────────┐
            │ Log error +  │
            │ Show warning │
            └──────────────┘


┌─────────────────────────────────────────────────────────────┐
│                      Analysis Mode                          │
│              (uv run bench → Option 5 → 1)               │
└────────────────────────────┬────────────────────────────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │ restore_json_files  │
                  └──────────┬──────────┘
                             │
                ┌────────────┴────────────┐
                ▼                         ▼
       ┌─────────────────┐      ┌─────────────────┐
       │  Query all runs │      │ For each run:   │
       │  from DB        │──────│ Query evals     │
       └─────────────────┘      └────────┬────────┘
                                          │
                                          ▼
                                ┌─────────────────┐
                                │ Reconstruct     │
                                │ JSON array      │
                                └────────┬────────┘
                                         │
                                         ▼
                                ┌─────────────────┐
                                │ Write to data/  │
                                │ {source_file}   │
                                └─────────────────┘
```

---

## Design Decisions

### Why SQLite?

- **Zero configuration**: No server setup, single file database
- **Portable**: Database file can be versioned, shared, backed up
- **Python built-in**: No external dependencies beyond `jsonschema`
- **Sufficient for use case**: Thousands of evaluations with instant queries

### Why Store Raw JSON?

- **Simplicity**: No need to flatten complex nested structures
- **Flexibility**: Schema can evolve without database migrations
- **Validation at boundary**: JSON schema validation happens once on ingest
- **Easy export**: Reconstructing JSON files is trivial

### Why Not Use JSONB Features?

This is a **lean implementation** that prioritizes:
- Compatibility (works on older SQLite versions)
- Simplicity (no complex queries needed)
- Maintainability (easy to understand and modify)

Future enhancements could add JSONB columns or virtual columns for querying, but they're not needed for the current workflow.

### Why Duplicate `strategy` in Both Tables?

- **Convenience**: Common query pattern is "get all evaluations for strategy X"
- **Performance**: Avoids JOIN on every evaluation query
- **Denormalization trade-off**: Slight redundancy for better query ergonomics

---

## Error Handling

### Validation Errors

**Scenario**: JSON file fails schema validation

**Behavior**:
- Error logged with details
- File NOT ingested (database remains consistent)
- User sees warning in CLI
- Benchmark continues (JSON file still saved to `data/`)

**Example**:
```
ERROR:db.manager:Validation failed for results_direct_2025-11-13T17-52-45.json:
✗ Validation failed with 1 error(s)

Errors:
  1. Record 5 (student: unknown): avg_pct (57.5) doesn't match calculated average (58.00)
```

### Database Errors

**Scenario**: SQLite operation fails (e.g., disk full, permissions)

**Behavior**:
- Exception caught in `_save_results()`
- Error logged
- User sees: `[yellow]Warning: Could not save to database: {error}[/yellow]`
- Benchmark continues (JSON files are the source of truth)

### Missing Database

**Scenario**: User tries to restore but database doesn't exist

**Behavior**:
```
[yellow]No database found. Run a benchmark first to create the database.[/yellow]
```

---

## Future Enhancements

### Potential Features

1. **Query Interface**: CLI command to query database directly
   ```bash
   uv run bench query --strategy direct --student "Smith_John_123456"
   ```

2. **Statistics Dashboard**: Summary stats across all runs
   ```bash
   uv run bench stats
   # Shows: total runs, evaluations, mean scores by strategy, etc.
   ```

3. **Export Filters**: Restore only specific runs
   ```python
   restore_json_files(db_path, data_dir, strategy="direct", after="2025-11-01")
   ```

4. **Comparison Tool**: Compare specific runs
   ```bash
   uv run bench compare --run1 42 --run2 43
   ```

5. **JSONB Virtual Columns**: For advanced queries (requires SQLite 3.31+)
   ```sql
   ALTER TABLE evaluations ADD COLUMN gpt5_score GENERATED ALWAYS AS (
       json_extract(raw_json, '$.metrics.gpt5_nano.pct')
   );
   ```

6. **Backup/Export**: Export database to CSV or other formats

7. **Annotations**: Add manual notes to runs via CLI
   ```bash
   uv run bench annotate --run 42 --note "Baseline run with default prompts"
   ```

### Migration Strategy

If schema changes are needed:
1. Create `db/migrations/` directory
2. Version schema files (e.g., `001_initial.sql`, `002_add_annotations.sql`)
3. Add migration runner in `db/manager.py`

---

## Testing

### Validation Testing

Test with invalid JSON:
```python
# Create invalid JSON (avg_pct mismatch)
invalid_data = [{
    "student": "Test_Student",
    "metrics": {
        "gpt5_nano": {"pct": 50.0, "total": 50, "max": 100},
        "gpt_oss_120b": {"pct": 60.0, "total": 60, "max": 100},
        "avg_pct": 99.0,  # Should be 55.0!
        "diff_pct": 10.0
    }
}]

# Attempt to ingest (should fail)
db.ingest_results_file(Path("invalid.json"))  # Raises ValueError
```

### Round-Trip Testing

Verify data integrity:
```python
# 1. Ingest original files
db.ingest_results_file(Path("data/results_direct_2025-11-13T17-52-45.json"))

# 2. Clear data directory
shutil.rmtree("data")

# 3. Restore from database
db.restore_json_files(Path("data"))

# 4. Compare (ignoring order)
import json
with open("data/results_direct_2025-11-13T17-52-45.json") as f:
    restored = sorted(json.load(f), key=lambda x: x["student"])

with open("data_backup/results_direct_2025-11-13T17-52-45.json") as f:
    original = sorted(json.load(f), key=lambda x: x["student"])

assert restored == original  # ✓ Content matches!
```

---

## Troubleshooting

### Database is Locked

**Symptom**: `sqlite3.OperationalError: database is locked`

**Cause**: Another process has the database open with an active transaction

**Solution**:
- Close other connections to `evaluations.db`
- Check for long-running queries
- SQLite only allows one writer at a time

### Schema Mismatch

**Symptom**: `sqlite3.OperationalError: no such table: runs`

**Cause**: Database exists but schema wasn't initialized

**Solution**:
```python
db_manager.init_db(DB_PATH)  # Re-run initialization
```

### Validation Always Fails

**Symptom**: Every ingestion fails validation

**Cause**: Missing `jsonschema` package OR incorrect `evaluation_schema.json`

**Solution**:
```bash
# Check if jsonschema is installed
uv pip list | grep jsonschema

# Install if missing
uv pip install jsonschema

# Verify schema file exists
ls evaluation_schema.json
```

---

## Appendix: Complete API Reference

### `db.manager` Module

```python
class DatabaseManager:
    def __init__(self, db_path: str = DEFAULT_DB_PATH) -> None
    def init_db(self) -> None
    def ingest_results_file(self, file_path: Path) -> None
    def restore_json_files(self, data_dir: Path, validate: bool = False) -> int
    def _parse_filename(self, filename: str) -> tuple[str, str]

# Convenience functions
def init_db(db_path: str = DEFAULT_DB_PATH) -> None
def ingest_results_file(db_path: str, file_path: Path) -> None
def restore_json_files(db_path: str, data_dir: Path, validate: bool = False) -> int
```

### Constants

```python
DEFAULT_DB_PATH = "evaluations.db"
SCHEMA_FILE = Path(__file__).parent / "schema.sql"
```

---

## Summary

The database system provides:

✅ **Automatic persistence** of all benchmark runs
✅ **JSON schema validation** before storage
✅ **Zero-configuration** SQLite backend
✅ **Bidirectional sync** (JSON ⟷ Database)
✅ **Clean separation of concerns** (manager.py handles all DB logic)
✅ **Graceful error handling** (never blocks benchmarks)
✅ **Reproducibility** (restore exact evaluation sets for analysis)

The implementation follows the "lean" philosophy: minimal, modular, and maintainable.
