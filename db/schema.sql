-- SQLite schema for evaluation results database
-- Stores evaluation runs and individual student evaluations

-- One row per batch of evaluations (per JSON file or logical run)
CREATE TABLE IF NOT EXISTS runs (
    run_id        INTEGER PRIMARY KEY,
    run_timestamp TEXT NOT NULL,        -- e.g. "2025-11-13T17-52-45" or full ISO
    source_file   TEXT NOT NULL,        -- original filename, e.g. "results_direct_2025-11-13T17-52-45.json"
    strategy      TEXT NOT NULL,        -- "direct", "reverse", "eme"
    notes         TEXT                  -- optional manual notes
);

-- One row per student evaluation (per student per strategy per run)
CREATE TABLE IF NOT EXISTS evaluations (
    eval_id        INTEGER PRIMARY KEY,
    run_id         INTEGER NOT NULL REFERENCES runs(run_id) ON DELETE CASCADE,
    student_name   TEXT    NOT NULL,    -- from evaluation["student"]
    strategy       TEXT    NOT NULL,    -- duplicate of runs.strategy for convenience
    raw_json       TEXT    NOT NULL,    -- full evaluation JSON as text

    UNIQUE (run_id, student_name, strategy)
);

-- Indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_evaluations_run ON evaluations(run_id);
CREATE INDEX IF NOT EXISTS idx_evaluations_student ON evaluations(student_name);
