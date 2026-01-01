# CLI Reference

Complete reference for all command-line tools in the framework.

---

## Quick Reference

| Command | Purpose |
|---------|---------|
| `uv run python analyze.py analyze-multi` | Run full analysis with semantic matching |
| `uv run python miscons.py` | Run LLM detection on student files |
| `uv run python pipeline.py` | Run complete pipeline (generate + detect + analyze) |

---

## Main Analysis: `analyze.py`

### `analyze-multi` — Full Analysis

Analyzes detection outputs against ground truth using semantic matching.

```bash
uv run python analyze.py analyze-multi [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--run-name` | string | auto | Name for this analysis run |
| `--semantic-threshold` | float | 0.65 | Cosine similarity threshold for match |
| `--noise-floor` | float | 0.55 | Below this score, ignore as noise |
| `--assignment` | string | multi | Which assignment: a1, a2, a3, or multi |
| `--output-dir` | string | runs/multi | Where to save results |
| `--verbose` | flag | false | Print detailed progress |

**Example:**

```bash
# Run analysis with default settings
uv run python analyze.py analyze-multi --run-name my-analysis

# Analyze single assignment
uv run python analyze.py analyze-multi --assignment a1 --run-name a1-only

# Custom thresholds
uv run python analyze.py analyze-multi \
    --semantic-threshold 0.70 \
    --noise-floor 0.60 \
    --run-name strict-thresholds
```

**Output:**

```
runs/multi/run_my-analysis/
├── report.md           # Full markdown report
├── metrics.json        # Numeric results
├── results.csv         # Per-detection breakdown
├── compliance.csv      # Per-file summary
└── assets/
    ├── assignment_comparison.png
    ├── model_comparison.png
    ├── strategy_f1.png
    ├── category_recall.png
    ├── semantic_distribution.png
    └── strategy_model_heatmap.png
```

---

## Detection: `miscons.py`

Runs LLM detection on student code files.

```bash
uv run python miscons.py [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--assignment` | string | required | Which assignment: a1, a2, a3 |
| `--strategy` | string | all | Which prompt: baseline, taxonomy, cot, socratic |
| `--model` | string | all | Which LLM model to use |
| `--output-dir` | string | detections/ | Where to save outputs |

**Example:**

```bash
# Run all strategies on A1
uv run python miscons.py --assignment a1

# Run specific strategy and model
uv run python miscons.py \
    --assignment a2 \
    --strategy baseline \
    --model gpt-5.2
```

---

## Pipeline: `pipeline.py`

Runs the complete pipeline: generate data, run detection, analyze results.

```bash
uv run python pipeline.py [OPTIONS]
```

**Options:**

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--skip-generation` | flag | false | Skip synthetic data generation |
| `--skip-detection` | flag | false | Skip LLM detection |
| `--skip-analysis` | flag | false | Skip analysis |

**Example:**

```bash
# Run full pipeline
uv run python pipeline.py

# Only run analysis (detection already done)
uv run python pipeline.py --skip-generation --skip-detection
```

---

## Environment Variables

### Required

```bash
export OPENROUTER_API_KEY="sk-or-..."    # For LLM detection
export OPENAI_API_KEY="sk-..."           # For semantic embeddings
```

### Optional

```bash
export VERBOSE=true                      # Enable debug logging
export CACHE_EMBEDDINGS=true             # Cache embedding results
```

---

## Output Files Reference

### `report.md`

Markdown report containing:
- Executive summary with key metrics
- Results by assignment (complexity gradient)
- Results by model (LLM comparison)
- Results by strategy (prompt comparison)
- Per-misconception detection rates
- Statistical significance tests
- Embedded visualizations

### `metrics.json`

```json
{
  "tp": 6745,
  "fp": 14236,
  "fn": 1022,
  "precision": 0.322,
  "recall": 0.868,
  "f1": 0.469
}
```

### `results.csv`

| Column | Description |
|--------|-------------|
| student | Student identifier |
| question | Q1, Q2, or Q3 |
| strategy | Prompt strategy used |
| model | LLM model used |
| expected_id | Ground truth misconception ID |
| matched_id | Detected misconception ID |
| score | Semantic similarity score |
| result | TP, FP, or FN |

### `compliance.csv`

| Column | Description |
|--------|-------------|
| student | Student identifier |
| question | Q1, Q2, or Q3 |
| assignment | a1, a2, or a3 |
| expected_id | Ground truth misconception ID |
| ensemble_result | TP, FP, FN, or TN |

---

## Common Workflows

### 1. Reproduce Final Results

```bash
uv run python analyze.py analyze-multi \
    --run-name reproduce-final \
    --semantic-threshold 0.65 \
    --noise-floor 0.55
```

### 2. Compare Thresholds

```bash
for thresh in 0.60 0.65 0.70 0.75; do
    uv run python analyze.py analyze-multi \
        --run-name "threshold-${thresh}" \
        --semantic-threshold $thresh
done
```

### 3. Single Assignment Deep Dive

```bash
uv run python analyze.py analyze-multi \
    --assignment a1 \
    --run-name a1-deep-dive \
    --verbose
```

### 4. View Results

```bash
# Read the report
cat runs/multi/run_my-analysis/report.md

# Check key metrics
cat runs/multi/run_my-analysis/metrics.json

# Open visualizations
open runs/multi/run_my-analysis/assets/*.png
```

---

## Troubleshooting

| Error | Solution |
|-------|----------|
| `OPENAI_API_KEY not set` | `export OPENAI_API_KEY="sk-..."` |
| `No detections found` | Run `miscons.py` first or check `detections/` |
| `Out of memory` | Use `--assignment a1` instead of `multi` |
| `Rate limit exceeded` | Wait and retry, or reduce batch size |

---

## Next: [Prompts](prompts.md)
