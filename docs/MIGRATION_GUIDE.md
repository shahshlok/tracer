# 🔄 Migration Guide: v1 → v2

**Upgrading from Version 1 to Version 2.0.0**

This guide helps you transition from the old EduBench system to the new EME Framework v2.

---

## 🎯 Overview

### What Changed?

| Aspect | v1 | v2 |
|--------|----|----|
| **Focus** | Simple benchmark comparison | Research-grade ensemble evaluation |
| **Schema** | Basic scores + metrics | Rich comparison + misconceptions |
| **Database** | Single evaluations table | evaluations + misconceptions tables |
| **Metrics** | avg_pct, diff_pct | ICC, correlations, ensemble quality |
| **Misconceptions** | Not tracked | Structured with evidence |

### Migration Difficulty

⭐⭐⭐☆☆ **Moderate** - Schema changes require data migration

**Time Estimate:** 1-2 hours for code updates, 30 minutes for data migration

---

## ⚠️ Breaking Changes

### 1. Evaluation JSON Structure

**v1 Structure:**
```json
{
  "student": "Smith_John_123456",
  "gpt5_nano_result": {
    "total_score": 95,
    "max_possible_score": 100,
    "overall_feedback": "..."
  },
  "gpt_oss_120b_result": {...},
  "metrics": {
    "avg_pct": 92.5,
    "diff_pct": 5.0
  }
}
```

**v2 Structure:**
```json
{
  "evaluation_id": "eval_...",
  "context": {...},
  "submission": {...},
  "rubric": {...},
  "models": {
    "gpt-5-nano": {
      "scores": {...},
      "category_scores": [{
        "confidence": 0.95,        // NEW
        "reasoning_tokens": 156    // NEW
      }],
      "misconceptions": [...]      // NEW
    }
  },
  "comparison": {...}              // COMPLETELY NEW
}
```

**Impact:** ⚠️ **High** - Completely different structure

---

### 2. Database Schema

**v1 Tables:**
```sql
CREATE TABLE runs (...);
CREATE TABLE evaluations (
    id INTEGER PRIMARY KEY,
    run_id INTEGER,
    student TEXT,
    evaluation_json TEXT
);
```

**v2 Tables:**
```sql
CREATE TABLE evaluations (
    evaluation_id TEXT PRIMARY KEY,
    assignment_id INTEGER NOT NULL,  -- NEW: integer!
    full_evaluation_json JSON NOT NULL
);

CREATE TABLE misconceptions (      -- COMPLETELY NEW
    id INTEGER PRIMARY KEY,
    evaluation_id TEXT,
    misconception_name TEXT,
    confidence REAL,
    ...
);
```

**Impact:** ⚠️ **High** - Cannot directly migrate old data

---

### 3. LLM Response Format

**v1:** Models returned free-form JSON
**v2:** Models must provide:
- Per-category `confidence` scores
- `reasoning_tokens` count
- Structured `misconceptions` with evidence

**Impact:** ⚠️ **Medium** - Update prompts and parsing

---

### 4. Assignment ID Type

**v1:** String (`"assignment_5"`)
**v2:** Integer (`5`)

**Impact:** ⚠️ **Low** - Easy find-and-replace

---

## 📋 Migration Checklist

### Phase 1: Preparation (30 minutes)

- [ ] **Backup existing data**
  ```bash
  cp evaluations.db evaluations_v1_backup.db
  cp -r data/ data_v1_backup/
  ```

- [ ] **Review v2 schema**
  - Read [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md)
  - Study [../example.jsonc](../example.jsonc)

- [ ] **Identify what you need to migrate**
  - Historical evaluations?
  - Just the code/config?

### Phase 2: Code Updates (1-2 hours)

- [ ] **Update LLM prompts** to request v2 fields
  - Add per-category confidence
  - Add misconception evidence
  - Remove deprecated fields

- [ ] **Update parsing code**
  - Handle new JSON structure
  - Extract misconceptions
  - Compute comparison metrics

- [ ] **Update database code**
  - Create v2 tables
  - Implement misconception extraction

- [ ] **Update display code**
  - Show new metrics (ICC, CV, etc.)
  - Display misconceptions

### Phase 3: Data Migration (30 minutes)

- [ ] **Convert old evaluations** (if needed)
  - Write conversion script
  - Test on sample data
  - Run full migration

- [ ] **Verify migrated data**
  - Check JSON validity
  - Verify field mappings
  - Test queries

### Phase 4: Testing (30 minutes)

- [ ] **Run test evaluations**
  - Grade sample submissions
  - Verify output format
  - Check database storage

- [ ] **Validate metrics**
  - Ensure calculations correct
  - Compare with expected values

---

## 🛠️ Step-by-Step Migration

### Step 1: Update Environment

```bash
# Pull latest code
git checkout nov10-add-db-lean  # or latest v2 branch
git pull

# Update dependencies
uv sync

# Verify new structure
ls -la docs/  # Should see new documentation
```

### Step 2: Backup Data

```bash
# Backup database
cp evaluations.db evaluations_v1_backup.db

# Backup JSON results
mkdir -p data_v1_backup
cp data/results_*.json data_v1_backup/
```

### Step 3: Update Configuration

**Old `.env` (v1):**
```env
QUESTION="..."
RUBRIC='{"totalPoints": 100, ...}'
```

**New `.env` (v2) - Same, but can use files:**
```env
# Option 1: Keep in .env (works)
QUESTION="..."
RUBRIC='...'

# Option 2: Use files (recommended)
# Just create question_*.md and rubric_*.json
# CLI will auto-discover them
```

### Step 4: Database Migration

**Option A: Start Fresh (Recommended)**

```bash
# Remove old database
rm evaluations.db

# Run new system (auto-creates v2 schema)
uv run bench benchmark
```

**Option B: Migrate Existing Data**

Create `scripts/migrate_v1_to_v2.py`:

```python
import sqlite3
import json
from pathlib import Path

# Connect to databases
old_db = sqlite3.connect("evaluations_v1_backup.db")
new_db = sqlite3.connect("evaluations.db")

# Read old evaluations
old_cursor = old_db.execute("SELECT student, evaluation_json FROM evaluations")

for student, json_str in old_cursor:
    old_eval = json.loads(json_str)

    # Convert to v2 format
    new_eval = {
        "evaluation_id": f"migrated_{student}",
        "schema_version": "1.0.0",
        "context": {
            "assignment_id": 1,  # Set appropriately
            # ... fill in context
        },
        "submission": {
            "student_id": student,
            # ... extract from old format
        },
        "models": {
            # Convert old gpt5_nano_result → new structure
            # Convert old gpt_oss_120b_result → new structure
        },
        # Note: comparison section will be empty (need to recompute)
    }

    # Insert into new database
    new_db.execute(
        "INSERT INTO evaluations VALUES (?, ?, ?, ?, ?, ?)",
        (new_eval["evaluation_id"], student, 1, "q1", "2024-11-01", json.dumps(new_eval))
    )

new_db.commit()
```

**Run migration:**
```bash
python scripts/migrate_v1_to_v2.py
```

### Step 5: Verify Migration

```bash
# Check new database
sqlite3 evaluations.db

sqlite> SELECT COUNT(*) FROM evaluations;
sqlite> SELECT COUNT(*) FROM misconceptions;
sqlite> SELECT * FROM evaluations LIMIT 1;  # Check structure
```

### Step 6: Test New System

```bash
# Run a test evaluation
python single_submission.py student_submissions/TestStudent/Main.java

# Verify output matches v2 schema
cat data/results_*.json | jq '.[] | keys'

# Should see: evaluation_id, context, submission, models, comparison
```

---

## 🔍 Field Mapping Reference

### Top-Level Fields

| v1 Field | v2 Field | Notes |
|----------|----------|-------|
| `student` | `submission.student_id` | Moved to submission |
| N/A | `evaluation_id` | NEW - unique ID |
| N/A | `context` | NEW - course/assignment info |
| N/A | `comparison` | NEW - rich statistics |

### Model Results

| v1 Field | v2 Field | Notes |
|----------|----------|-------|
| `gpt5_nano_result` | `models["gpt-5-nano"]` | Nested under models |
| `total_score` | `scores.total_points_awarded` | |
| `overall_feedback` | `feedback.overall_comment` | |
| N/A | `category_scores[].confidence` | NEW |
| N/A | `misconceptions[]` | NEW |

### Metrics

| v1 Field | v2 Field | Notes |
|----------|----------|-------|
| `metrics.avg_pct` | `comparison.score_summary.mean` | |
| `metrics.diff_pct` | `comparison.pairwise_differences[].percentage_diff` | |
| `metrics.flag` | `comparison.flags.overall_agreement` | Now text not emoji |
| N/A | `comparison.reliability_metrics` | NEW - ICC, correlations |

---

## 🚨 Common Migration Issues

### Issue 1: "Schema validation failed"

**Problem:** Old JSON doesn't match v2 schema

**Solution:**
```bash
# Don't try to load old JSON into v2
# Either convert it or start fresh
```

### Issue 2: "assignment_id must be integer"

**Problem:** Old code uses strings

**Solution:**
```python
# Old
assignment_id = "assignment_5"

# New
assignment_id = 5
```

### Issue 3: "misconceptions table doesn't exist"

**Problem:** Using old database

**Solution:**
```bash
# Recreate database
rm evaluations.db
uv run bench benchmark  # Creates v2 schema
```

### Issue 4: "Missing confidence field"

**Problem:** LLM not providing v2 fields

**Solution:**
Update your grading prompts to request:
```
For each category, provide:
- points_awarded
- reasoning
- confidence (0.0-1.0)
- reasoning_tokens (count)
```

---

## 📝 What You Can Keep

✅ **Student submissions** - No changes needed
✅ **Question files** (`.md`) - Compatible
✅ **Rubric files** (`.json`) - Compatible (add `category_id` if desired)
✅ **API keys** (`.env`) - Same format
✅ **Basic workflow** - CLI commands similar

---

## ❌ What You Must Change

⚠️ **Database schema** - Not backward compatible
⚠️ **JSON structure** - Completely redesigned
⚠️ **LLM prompts** - Must request v2 fields
⚠️ **Parsing code** - Update to handle new structure
⚠️ **Display code** - Show new metrics

---

## 🎯 Migration Strategies

### Strategy 1: Fresh Start (Recommended for Most)

**When to use:** Starting new research, few old evaluations

**Steps:**
1. Backup old data
2. Pull v2 code
3. Run fresh evaluations
4. Keep old data as reference

**Pros:** Clean, no conversion headaches
**Cons:** Lose historical data

---

### Strategy 2: Dual System

**When to use:** Need both v1 and v2 running

**Steps:**
1. Clone repo twice
2. Keep v1 in one directory
3. Run v2 in another
4. Gradually transition

**Pros:** No data loss, can compare
**Cons:** More complex, disk space

---

### Strategy 3: Full Migration

**When to use:** Need all historical data in v2

**Steps:**
1. Write conversion script
2. Test thoroughly
3. Migrate data
4. Validate results

**Pros:** All data in new format
**Cons:** Time-consuming, error-prone

---

## ✅ Post-Migration Checklist

After migration, verify:

- [ ] New evaluations run successfully
- [ ] JSON output matches v2 schema
- [ ] Database has both tables (evaluations + misconceptions)
- [ ] Comparison metrics calculate correctly
- [ ] Misconceptions are extracted properly
- [ ] Old data backed up safely
- [ ] Documentation updated (if you have custom docs)

---

## 🆘 Need Help?

### Migration Issues
1. Check this guide first
2. Review [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md)
3. Look at [../example.jsonc](../example.jsonc) for target format
4. Try a fresh start if stuck

### Understanding v2
- [GETTING_STARTED.md](GETTING_STARTED.md) - Quickstart
- [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) - Field reference
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design

---

## 📞 Still Using v1?

If you're not ready to migrate yet:

**v1 Documentation:**
- [DATABASE.md](DATABASE.md) - Old database structure
- [JSON_OUTPUT.md](JSON_OUTPUT.md) - Old JSON format
- Main README has legacy section

**When to migrate:**
- ✅ When starting new research project
- ✅ When you need rich statistical metrics
- ✅ When tracking misconceptions matters
- ⏳ Wait if mid-project and v1 works fine

---

**Migration complete? Welcome to EME Framework v2!** 🚀
