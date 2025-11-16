# Ensemble Model Evaluation (EME) Framework

A research tool for grading student code submissions using multiple LLM models, discovering misconception patterns, and evaluating ensemble grading strategies through comprehensive cross-model analytics.

## ⚠️ Schema Revamp (v1.0.0)

**Major update in progress**: The evaluation schema has been completely redesigned to support research-grade ensemble analysis and misconception pattern discovery. See **[SCHEMA_DOCUMENTATION.md](./SCHEMA_DOCUMENTATION.md)** for the new comprehensive schema.

### What's New
- ✅ **Rich comparison metrics**: ICC, correlations, ensemble quality scores
- ✅ **Misconception tracking**: Inductive approach with evidence linking
- ✅ **Model characteristics**: Strictness rankings, confidence analysis
- ✅ **Publication-ready stats**: Krippendorff's alpha, SEM, confidence intervals
- ✅ **Extensible design**: Add metrics as research needs evolve

### Key Files
- **[SCHEMA_DOCUMENTATION.md](./SCHEMA_DOCUMENTATION.md)** - Complete schema reference and research applications
- **[example.jsonc](./example.jsonc)** - Fully annotated example with all metrics

---

## Current CLI Tool (Legacy)

The existing CLI benchmarking system is being updated to match the new schema. Documentation below reflects the current implementation.

---

## Quick Start

### Run a Benchmark

```bash
# Interactive mode
uv run bench benchmark

# Direct command
uv run bench benchmark --mode direct
```

### Access Your Results

```bash
# Launch analysis menu
uv run bench benchmark
# Select option [5] Analysis
# Select option [1] Restore JSON from Database
```

---

## Features

✅ **Three Grading Strategies**
- **Direct Grading**: Grade student code directly against rubric
- **Reverse Grading**: Generate ideal solution first, then compare
- **Ensemble (EME)**: Multi-model ensemble approach from RIAYN paper

✅ **Automatic Database Persistence**
- All benchmark results automatically stored in SQLite
- JSON files remain as temporary workspace for ad-hoc analysis
- Validated JSON in, validated JSON out

✅ **Rich CLI Interface**
- Interactive menu system
- Real-time progress bars
- Beautiful result tables with Rich library
- Cross-strategy comparison views

✅ **Data Validation**
- JSON Schema validation on all results
- Business rule checks (metrics calculations)
- Graceful error handling

---

## Project Structure (High-Level)

```text
EME_testing/
├── cli.py                   # Main CLI application (bench command)
├── single_submission.py     # One-off EME run for a single student
├── modes/                   # Grading strategies (direct / reverse / EME)
├── prompts/                 # Grading prompt templates
├── utils/                   # Shared utilities (models, evaluation, validation, display)
├── db/                      # SQLite schema + database manager
├── docs/                    # Documentation hub (see below)
├── data/                    # Temporary JSON workspace (results_*.json)
├── student_submissions/     # Input code submissions (.java)
├── evaluation_schema.json   # JSON Schema for validation
└── evaluations.db           # SQLite database (auto-created)
```

For a **beginner‑friendly tour of the codebase**, diagrams, and detailed examples, see:

- `docs/INDEX.md` – docs hub and quickstart
- `docs/ARCHITECTURE.md` – pipeline diagrams and module map
- `docs/DATABASE.md` – database schema and ingestion
- `docs/JSON_OUTPUT.md` – structured JSON outputs from models

---

## Database System

### Overview

The database automatically captures all benchmark runs for persistence and historical analysis without changing the JSON-based workflow.

**Philosophy**: "Validated JSON in, validated JSON out"

### Schema

- **`runs` table**: Tracks each evaluation run (timestamp, filename, strategy)
- **`evaluations` table**: Stores individual student evaluations as JSON text

### Automatic Ingestion

Every benchmark run automatically:
1. Saves results to `data/results_{strategy}_{timestamp}.json`
2. Validates against `evaluation_schema.json`
3. Ingests into SQLite database (`evaluations.db`)

### Restoration

Restore all JSON files from database to `data/` directory:

```bash
uv run bench benchmark
# Select [5] Analysis → [1] Restore JSON from Database
```

Or programmatically:

```python
from db.manager import restore_json_files
from pathlib import Path

files_restored = restore_json_files("evaluations.db", Path("data"))
```

### Documentation

📚 **[Complete Database Documentation](docs/DATABASE.md)**

Covers:
- Architecture and schema design
- API reference for `db/manager.py`
- Validation workflow
- Usage examples and recipes
- Error handling and troubleshooting
- Future enhancements

---

## CLI Commands

### Benchmark Command

```bash
# Interactive mode (recommended)
uv run bench benchmark

# With flags
uv run bench benchmark --mode direct
uv run bench benchmark --mode all      # Run all strategies
uv run bench benchmark --advanced      # Show per-student progress
```

**Available Modes**:
- `direct` - Direct grading against rubric
- `reverse` - Generate reference solution first
- `eme` - Ensemble method
- `all` - Run all three strategies and compare

### Analysis Menu

Access from the main menu (option 5) or when running benchmarks:

**Options**:
1. **Restore JSON from Database** - Export all evaluation results to `data/`
2. **Back to Main Menu** - Return to benchmark selection

---

## Validation

All evaluation results are validated before database storage:

### JSON Schema Validation
- Field types and required fields
- Nested structure validation
- Score ranges (0-100%)

### Business Rule Validation
- `avg_pct` must match calculated average
- `diff_pct` must match absolute difference
- Student identifiers must be non-empty

**Example Validation Output**:
```
✓ Validation passed

OR

✗ Validation failed with 2 error(s)

Errors:
  1. Record 3 (student: Smith_John_123456):
     - avg_pct (57.5) doesn't match calculated average (58.00)
  2. Record 5: Missing required field 'metrics'
```

If validation fails, the file is **not ingested** (database remains consistent), but the JSON file is still saved to `data/`.

---

## Development

### Installation

```bash
# Clone repository
git clone <repo-url>
cd EME_testing

# Install dependencies
uv sync
```

### Project Dependencies

- `typer` - CLI framework
- `rich` - Terminal UI and formatting
- `jsonschema` - JSON validation
- `openai` / LLM clients - Model interactions
- `sqlite3` - Built-in Python (no install needed)

### Testing

```bash
# Test database ingestion
python -c "
from pathlib import Path
from db.manager import DatabaseManager

db = DatabaseManager()
db.init_db()

for f in Path('data').glob('results_*.json'):
    db.ingest_results_file(f)
    print(f'✓ Ingested {f.name}')
"

# Test restoration
python -c "
from pathlib import Path
from db.manager import restore_json_files

count = restore_json_files('evaluations.db', Path('data'))
print(f'✓ Restored {count} files')
"
```

---

## Configuration

### Environment Variables

Create a `.env` file:

```env
QUESTION="Write a Java program that computes the sum of integers from 1 to 100."

RUBRIC='{
  "correctness": {"weight": 40, "description": "Program produces correct output"},
  "compilation": {"weight": 20, "description": "Code compiles without errors"},
  "style": {"weight": 20, "description": "Code follows conventions"},
  "documentation": {"weight": 20, "description": "Adequate comments"}
}'

# API Keys
OPENAI_API_KEY=sk-...
OPENROUTER_API_KEY=sk-or-...
OPENROUTER_MODEL=google/gemini-2.5-flash-preview-09-2025
# Optional analytics headers for OpenRouter rankings (can be left unset)
# OPENROUTER_SITE_URL=https://your-app-url.com
# OPENROUTER_SITE_NAME=Your App Name
```

---

## Output Format

### Evaluation JSON Structure

Each evaluation file (`data/results_{strategy}_{timestamp}.json`) contains an array:

```json
[
  {
    "student": "Smith_John_123456",
    "gpt5_nano_result": {
      "total_score": 95,
      "max_possible_score": 100,
      "overall_feedback": "Excellent work..."
    },
    "gpt_oss_120b_result": {
      "total_score": 90,
      "max_possible_score": 100,
      "overall_feedback": "Good implementation..."
    },
    "metrics": {
      "gpt5_nano": {"total": 95.0, "max": 100.0, "pct": 95.0},
      "gpt_oss_120b": {"total": 90.0, "max": 100.0, "pct": 90.0},
      "avg_pct": 92.5,
      "diff_pct": 5.0,
      "flag": "✅",
      "comment": "Models agree within tolerance"
    }
  }
]
```

### Database Storage

- **Runs**: Metadata about each benchmark run
- **Evaluations**: Individual student results stored as JSON text
- **Indexes**: Optimized for querying by student or run

---

## Schema Migration Roadmap

### Phase 1: Schema & Infrastructure ✅
- [x] Finalize evaluation JSON schema (v1.0.0)
- [x] Define misconception structure (inductive approach)
- [x] Design comprehensive comparison metrics
- [x] Document all fields and interpretations

### Phase 2: Implementation (Next)
- [ ] Update CLI to generate new schema format
- [ ] Implement comparison computation engine
- [ ] Migrate database schema to match new structure
- [ ] Build misconception extraction pipeline

### Phase 3: Analysis Tools
- [ ] LLM-powered misconception pattern analysis
- [ ] Statistical analysis scripts (ICC, correlations, etc.)
- [ ] Instructor dashboard with rich visualizations

### Phase 4: Research Applications
- [ ] Ensemble strategy comparison studies
- [ ] Model reliability & calibration analysis
- [ ] Misconception clustering and insights
- [ ] Publication-ready data exports

---

## Future Enhancements (Legacy)

Potential features for current CLI (see [DATABASE.md](docs/DATABASE.md#future-enhancements) for details):

- Query interface for database (`uv run bench query --student "Smith_John"`)
- Statistics dashboard across all runs
- Export filters (by strategy, date range, student)
- Run comparison tool
- Annotation system for manual notes
- JSONB virtual columns for advanced queries

---

## Contributing

When adding features:

1. **Follow separation of concerns**:
   - CLI logic in `cli.py`
   - Database operations in `db/manager.py`
   - Grading strategies in `modes/`
   - Shared utilities in `utils/`

2. **Maintain validation**:
   - Update `evaluation_schema.json` for schema changes
   - Add business rules to `utils/validation.py`

3. **Document changes**:
   - Update relevant docs in `docs/`
   - Add docstrings to all functions
   - Include usage examples

4. **Test database operations**:
   - Verify round-trip integrity (ingest → restore)
   - Test validation with invalid data
   - Check error handling

---

## Research Context

**Project:** Honours Thesis Research - Ensemble Model Evaluation for Code Grading
**Institution:** University of British Columbia Okanagan (UBCO)
**Course:** COSC 499 - Honours Thesis
**Researcher:** Shlok Shah
**Academic Year:** 2024-2025

## Citation

If you use this schema or framework in your research, please cite:

```bibtex
@software{eme_framework_2025,
  author = {Shah, Shlok},
  title = {Ensemble Model Evaluation Framework for Code Grading},
  year = {2025},
  institution = {University of British Columbia Okanagan},
  note = {Honours Thesis Research Project}
}
```

## License

TBD (Academic Research Project)
