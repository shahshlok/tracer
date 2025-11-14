-- EME Testing Database Schema
-- SQLite 3.51.0+ required (released November 2025)
--
-- This version includes:
-- - JSONB support for binary JSON storage (since 3.45.0)
-- - jsonb_each() and jsonb_tree() functions (added in 3.51.0)
-- - Enhanced performance for JSON operations
--
-- This schema stores student evaluations with:
-- - Full JSONB storage of all evaluation data
-- - Virtual generated columns for fast querying
-- - Full-text search for feedback analysis
-- - Metadata tracking for evolving requirements

-- ============================================================================
-- Table 1: Evaluation Runs (metadata for each evaluation batch)
-- ============================================================================
CREATE TABLE IF NOT EXISTS runs (
    run_id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_timestamp TEXT NOT NULL UNIQUE,
    metadata BLOB,  -- JSONB: {assignment_type, rubric_version, model_versions: {...}, notes}

    -- Ensure metadata is valid JSON (SQLite checks syntax only)
    CHECK(json_valid(metadata) OR metadata IS NULL)
);

-- Index for querying runs by timestamp
CREATE INDEX IF NOT EXISTS idx_run_timestamp ON runs(run_timestamp);

-- ============================================================================
-- Table 2: Individual Evaluations (one row per student-strategy-run)
-- ============================================================================
CREATE TABLE IF NOT EXISTS evaluations (
    eval_id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id INTEGER NOT NULL,
    student_name TEXT NOT NULL,
    strategy TEXT NOT NULL CHECK(strategy IN ('direct', 'reverse', 'eme')),

    -- THE CORE: Complete evaluation stored as JSONB for fast access
    raw_json BLOB NOT NULL,

    -- Virtual columns extract frequently-queried fields WITHOUT storing them
    -- These exist purely for indexing and querying (computed on-the-fly)
    nano_pct REAL GENERATED ALWAYS AS (json_extract(raw_json, '$.metrics.gpt5_nano.pct')) VIRTUAL,
    oss_pct REAL GENERATED ALWAYS AS (json_extract(raw_json, '$.metrics.gpt_oss_120b.pct')) VIRTUAL,
    avg_pct REAL GENERATED ALWAYS AS (json_extract(raw_json, '$.metrics.avg_pct')) VIRTUAL,
    diff_pct REAL GENERATED ALWAYS AS (json_extract(raw_json, '$.metrics.diff_pct')) VIRTUAL,
    flag TEXT GENERATED ALWAYS AS (json_extract(raw_json, '$.metrics.flag')) VIRTUAL,
    nano_feedback TEXT GENERATED ALWAYS AS (json_extract(raw_json, '$.gpt5_nano_result.overall_feedback')) VIRTUAL,
    oss_feedback TEXT GENERATED ALWAYS AS (json_extract(raw_json, '$.gpt_oss_120b_result.overall_feedback')) VIRTUAL,

    -- Note: raw_json is JSONB (BLOB), so we can't use json_valid() CHECK constraint here
    -- Application-level validation ensures data integrity before insertion

    -- Foreign key with cascade delete
    FOREIGN KEY (run_id) REFERENCES runs(run_id) ON DELETE CASCADE,

    -- Ensure unique combination of run, student, and strategy
    UNIQUE(run_id, student_name, strategy)
);

-- ============================================================================
-- Indexes for Performance
-- ============================================================================

-- Composite index for student-focused queries
CREATE INDEX IF NOT EXISTS idx_eval_run_student ON evaluations(run_id, student_name);

-- Strategy-based queries
CREATE INDEX IF NOT EXISTS idx_eval_strategy ON evaluations(strategy);

-- Student name lookups across all runs
CREATE INDEX IF NOT EXISTS idx_eval_student_name ON evaluations(student_name);

-- Percentage-based queries (partial indexes for better performance)
CREATE INDEX IF NOT EXISTS idx_eval_nano_pct ON evaluations(nano_pct) WHERE nano_pct IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_eval_oss_pct ON evaluations(oss_pct) WHERE oss_pct IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_eval_avg_pct ON evaluations(avg_pct) WHERE avg_pct IS NOT NULL;

-- Disagreement analysis queries
CREATE INDEX IF NOT EXISTS idx_eval_diff_pct ON evaluations(diff_pct) WHERE diff_pct IS NOT NULL;

-- Flag-based filtering
CREATE INDEX IF NOT EXISTS idx_eval_flag ON evaluations(flag) WHERE flag IS NOT NULL;

-- ============================================================================
-- Full-Text Search for Feedback Analysis
-- ============================================================================

-- FTS5 virtual table for finding common misconceptions in feedback
CREATE VIRTUAL TABLE IF NOT EXISTS feedback_fts USING fts5(
    eval_id UNINDEXED,
    student_name,
    strategy,
    nano_feedback,
    oss_feedback,
    content=evaluations,
    content_rowid=eval_id
);

-- ============================================================================
-- Triggers to Keep FTS Table Synchronized
-- ============================================================================

-- Sync INSERT operations
CREATE TRIGGER IF NOT EXISTS evaluations_ai AFTER INSERT ON evaluations BEGIN
    INSERT INTO feedback_fts(rowid, student_name, strategy, nano_feedback, oss_feedback)
    VALUES (new.eval_id, new.student_name, new.strategy, new.nano_feedback, new.oss_feedback);
END;

-- Sync DELETE operations
CREATE TRIGGER IF NOT EXISTS evaluations_ad AFTER DELETE ON evaluations BEGIN
    INSERT INTO feedback_fts(feedback_fts, rowid, student_name, strategy, nano_feedback, oss_feedback)
    VALUES ('delete', old.eval_id, old.student_name, old.strategy, old.nano_feedback, old.oss_feedback);
END;

-- Sync UPDATE operations
CREATE TRIGGER IF NOT EXISTS evaluations_au AFTER UPDATE ON evaluations BEGIN
    INSERT INTO feedback_fts(feedback_fts, rowid, student_name, strategy, nano_feedback, oss_feedback)
    VALUES ('delete', old.eval_id, old.student_name, old.strategy, old.nano_feedback, old.oss_feedback);
    INSERT INTO feedback_fts(rowid, student_name, strategy, nano_feedback, oss_feedback)
    VALUES (new.eval_id, new.student_name, new.strategy, new.nano_feedback, new.oss_feedback);
END;

-- ============================================================================
-- Useful Views for Common Queries
-- ============================================================================

-- View: Latest run for each strategy
CREATE VIEW IF NOT EXISTS latest_run AS
SELECT
    r.run_id,
    r.run_timestamp,
    e.strategy,
    COUNT(*) as student_count,
    AVG(e.avg_pct) as avg_performance,
    AVG(e.diff_pct) as avg_disagreement,
    SUM(CASE WHEN e.flag = '🚩' THEN 1 ELSE 0 END) as disagreement_count
FROM runs r
JOIN evaluations e ON r.run_id = e.run_id
WHERE r.run_id = (SELECT run_id FROM runs ORDER BY run_timestamp DESC LIMIT 1)
GROUP BY e.strategy;

-- View: Student performance summary across strategies
CREATE VIEW IF NOT EXISTS student_summary AS
SELECT
    e.student_name,
    e.run_id,
    MAX(CASE WHEN e.strategy = 'direct' THEN e.avg_pct END) as direct_avg,
    MAX(CASE WHEN e.strategy = 'reverse' THEN e.avg_pct END) as reverse_avg,
    MAX(CASE WHEN e.strategy = 'eme' THEN e.avg_pct END) as eme_avg,
    AVG(e.avg_pct) as overall_avg,
    MAX(CASE WHEN e.strategy = 'direct' THEN e.diff_pct END) as direct_diff,
    MAX(CASE WHEN e.strategy = 'reverse' THEN e.diff_pct END) as reverse_diff,
    MAX(CASE WHEN e.strategy = 'eme' THEN e.diff_pct END) as eme_diff
FROM evaluations e
GROUP BY e.student_name, e.run_id;

-- View: Readable evaluations (converts JSONB to text for DB browsers like DBeaver)
-- Note: We extract from raw_json directly instead of using virtual columns
-- to avoid "malformed JSON" errors in DB browsers that preview BLOBs
CREATE VIEW IF NOT EXISTS evaluations_readable AS
SELECT
    eval_id,
    run_id,
    student_name,
    strategy,
    CAST(json_extract(raw_json, '$.metrics.gpt5_nano.pct') AS REAL) as nano_pct,
    CAST(json_extract(raw_json, '$.metrics.gpt_oss_120b.pct') AS REAL) as oss_pct,
    CAST(json_extract(raw_json, '$.metrics.avg_pct') AS REAL) as avg_pct,
    CAST(json_extract(raw_json, '$.metrics.diff_pct') AS REAL) as diff_pct,
    json_extract(raw_json, '$.metrics.flag') as flag,
    json_extract(raw_json, '$.gpt5_nano_result.overall_feedback') as nano_feedback,
    json_extract(raw_json, '$.gpt_oss_120b_result.overall_feedback') as oss_feedback,
    json(raw_json) as raw_json_text  -- Convert JSONB BLOB to readable text JSON
FROM evaluations;

-- View: Full evaluation details with readable JSON
CREATE VIEW IF NOT EXISTS evaluation_details AS
SELECT
    e.eval_id,
    r.run_timestamp,
    r.metadata as run_metadata_blob,
    json(r.metadata) as run_metadata_text,  -- Readable metadata
    e.student_name,
    e.strategy,
    e.nano_pct,
    e.oss_pct,
    e.avg_pct,
    e.diff_pct,
    e.flag,
    json_extract(e.raw_json, '$.metrics.comment') as metric_comment,
    json(e.raw_json) as full_evaluation_json  -- Full readable JSON
FROM evaluations e
JOIN runs r ON e.run_id = r.run_id;

-- ============================================================================
-- Database Metadata
-- ============================================================================

-- Store schema version for future migrations
CREATE TABLE IF NOT EXISTS schema_version (
    version INTEGER PRIMARY KEY,
    applied_at TEXT DEFAULT CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO schema_version (version) VALUES (1);
