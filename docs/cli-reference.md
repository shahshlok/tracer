# CLI Reference

Complete reference for all CLI commands in the pipeline.

## analyze

Main analysis CLI for misconception detection evaluation.

### Commands

#### `analyze analyze`

Run misconception detection analysis.

```bash
uv run analyze analyze [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--detections-dir` | PATH | `detections` | Root directory for detection files |
| `--manifest-path` | PATH | `authentic_seeded/manifest.json` | Path to dataset manifest |
| `--groundtruth-path` | PATH | `data/a2/groundtruth.json` | Path to ground truth taxonomy |
| `--match-mode` | ENUM | `hybrid` | Matching mode: `fuzzy_only`, `semantic_only`, `hybrid`, `all` |
| `--quick/--no-quick` | BOOL | `--no-quick` | Quick mode (fewer bootstrap iterations) |
| `--run-tag`, `-t` | TEXT | None | Tag for this run (saves to `runs/<tag>/`) |
| `--notes`, `-n` | TEXT | "" | Notes to attach to this run |
| `--save/--no-save` | BOOL | `--save` | Save run to `runs/` directory |

**Examples:**

```bash
# Basic analysis with hybrid matcher
uv run analyze analyze

# Full ablation study with all matchers
uv run analyze analyze --match-mode all

# Save run with custom tag
uv run analyze analyze --match-mode all --run-tag "baseline_v1" --notes "Initial experiment"

# Quick run without saving
uv run analyze analyze --quick --no-save

# Use different manifest
uv run analyze analyze --manifest-path authentic_seeded/alt_manifest.json
```

#### `analyze list-runs`

List all saved runs with comparison table.

```bash
uv run analyze list-runs
```

**Output:**

```
┌──────────────────┬──────┬─────────┬──────────┬─────────────┬──────────┬─────────┬─────────────────┬───────┐
│ Run ID           │ Seed │ Seeded% │ Fuzzy F1 │ Semantic F1 │ Hybrid F1│ Best F1 │ Best Config     │ Notes │
├──────────────────┼──────┼─────────┼──────────┼─────────────┼──────────┼─────────┼─────────────────┼───────┤
│ run_baseline_v1  │ 1764 │ 22.5%   │ 0.053    │ 0.598       │ 0.591    │ 0.650   │ rubric_only+gpt │ First │
└──────────────────┴──────┴─────────┴──────────┴─────────────┴──────────┴─────────┴─────────────────┴───────┘
```

---

## pipeline

End-to-end pipeline orchestration.

### `pipeline run`

Run the complete pipeline: generate → detect → analyze.

```bash
uv run pipeline run [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--students` | INT | 60 | Number of students to generate |
| `--strategies` | TEXT | `all` | Detection strategies (`all`, or comma-separated) |
| `--force` | FLAG | False | Force regeneration of dataset |
| `--yes`, `-y` | FLAG | False | Skip confirmation prompts |
| `--skip-detect` | FLAG | False | Skip detection step |
| `--skip-analyze` | FLAG | False | Skip analysis step |

**Examples:**

```bash
# Full pipeline with defaults
uv run pipeline run --yes

# Force regenerate with 30 students
uv run pipeline run --students 30 --force --yes

# Only run analysis (skip generation and detection)
uv run pipeline run --skip-detect --yes
```

---

## llm-miscons

LLM detection runner.

### `llm-miscons detect`

Run LLM misconception detection on student code.

```bash
uv run llm-miscons detect [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--strategy` | TEXT | `baseline` | Detection strategy or `all` |
| `--model` | TEXT | `gpt-5.1` | Model or `all` |
| `--student` | TEXT | None | Specific student folder (optional) |
| `--question` | TEXT | None | Specific question (optional) |
| `--force` | FLAG | False | Overwrite existing detections |

**Examples:**

```bash
# Run all strategies with all models
uv run llm-miscons detect --strategy all --model all

# Run specific strategy
uv run llm-miscons detect --strategy rubric_only --model gpt-5.1

# Run for specific student
uv run llm-miscons detect --student Davis_Rodney_403107 --question Q1
```

---

## miscons

Interactive dataset generator.

```bash
uv run miscons
```

Launches interactive mode for generating synthetic student submissions with injected misconceptions.

**Features:**
- Configure number of students
- Set seeding distribution
- Choose misconceptions to inject
- Generate personas and code

---

## Match Modes Explained

The `--match-mode` option controls how LLM detections are aligned to ground truth:

| Mode | Description | Use Case |
|------|-------------|----------|
| `fuzzy_only` | String similarity only | Baseline comparison |
| `semantic_only` | Embedding similarity | Best accuracy |
| `hybrid` | Fuzzy + Semantic + Topic prior | Default |
| `all` | Run all three for ablation | Research comparison |

**Example ablation:**

```bash
# Run all matchers and compare
uv run analyze analyze --match-mode all --run-tag "ablation_study"
```

This produces a report showing:
- F1 for each matcher
- Matcher × Strategy distribution plot
- Precision-Recall by matcher

---

## Quick Reference

```bash
# Most common commands

# 1. Run full ablation analysis
uv run analyze analyze --match-mode all --run-tag "experiment_v1"

# 2. Quick analysis (hybrid only)
uv run analyze analyze --quick

# 3. Compare all runs
uv run analyze list-runs

# 4. Full pipeline
uv run pipeline run --students 60 --strategies all --force --yes

# 5. Run detection only
uv run llm-miscons detect --strategy all --model all
```

---

## Environment Variables

| Variable | Purpose |
|----------|---------|
| `OPENAI_API_KEY` | Required for GPT-5.1 and embeddings |
| `GOOGLE_API_KEY` | Required for Gemini 2.5 Flash |

---

## Output Files

| File | Generated By | Description |
|------|--------------|-------------|
| `thesis_report.md` | `analyze analyze` | Main analysis report |
| `thesis_report.json` | `analyze analyze` | Raw metrics data |
| `docs/report_assets/*.png` | `analyze analyze` | Visualizations |
| `runs/<run_id>/` | `analyze analyze` | Saved run data |
| `detections/<strategy>/*.json` | `llm-miscons detect` | Detection results |
| `authentic_seeded/manifest.json` | `miscons` | Dataset manifest |
