# Run Storage System

The run storage system saves complete experiment runs for reproducibility and comparison.

## Overview

Each run is saved to `runs/<run_id>/` with:
- Full configuration for reproducibility
- All metrics and results
- Copy of the manifest used
- Generated assets (plots)

## Directory Structure

```
runs/
├── index.json                         # Summary of all runs
├── run_baseline_v1/
│   ├── config.json                    # Full configuration
│   ├── manifest.json                  # Dataset snapshot
│   ├── metrics.json                   # Raw metrics
│   ├── report.md                      # Generated report
│   └── assets/
│       ├── matcher_ablation.png
│       ├── matcher_strategy_grid.png
│       ├── misconception_recall.png
│       ├── topic_heatmap.png
│       └── ...
└── run_seed9999/
    └── ...
```

## config.json

Each run's configuration includes:

```json
{
  "run_id": "run_baseline_v1",
  "created_at": "2025-12-04T17:35:00Z",
  
  "dataset": {
    "assignment": "A2",
    "seed": 1764863751,
    "generation_model": "gpt-5.1-2025-11-13",
    "generated_at": "2025-12-04T07:55:51.615649",
    "students": 60,
    "questions": 4,
    "total_files": 240,
    "seeded_files": 54,
    "seeded_pct": 22.5
  },
  
  "pipeline": {
    "detection_models": ["gpt-5.1", "gemini-2.5-flash"],
    "strategies": ["baseline", "minimal", "rubric_only", "socratic"],
    "matchers": ["fuzzy_only", "semantic_only", "hybrid"],
    "embedding_model": "text-embedding-3-large"
  },
  
  "results": {
    "by_matcher": {
      "fuzzy_only": {"avg_f1": 0.053, "avg_precision": 0.042, "avg_recall": 0.078},
      "semantic_only": {"avg_f1": 0.598, "avg_precision": 0.542, "avg_recall": 0.679},
      "hybrid": {"avg_f1": 0.591, "avg_precision": 0.538, "avg_recall": 0.664}
    },
    "best_overall": {
      "matcher": "semantic_only",
      "strategy": "rubric_only",
      "model": "gpt-5.1",
      "f1": 0.650
    }
  },
  
  "notes": "Baseline run with 22.5% seeding"
}
```

## index.json

The index provides a quick comparison table:

```json
{
  "runs": [
    {
      "run_id": "run_baseline_v1",
      "created_at": "2025-12-04T17:35:00Z",
      "seed": 1764863751,
      "seeded_pct": 22.5,
      "fuzzy_f1": 0.053,
      "semantic_f1": 0.598,
      "hybrid_f1": 0.591,
      "best_f1": 0.650,
      "best_config": "rubric_only+gpt-5.1",
      "notes": "Baseline run"
    },
    {
      "run_id": "run_seed9999",
      "created_at": "2025-12-05T10:00:00Z",
      "seed": 9999,
      "seeded_pct": 22.5,
      "fuzzy_f1": 0.048,
      "semantic_f1": 0.612,
      "hybrid_f1": 0.605,
      "best_f1": 0.668,
      "best_config": "rubric_only+gpt-5.1",
      "notes": "Different seed"
    }
  ]
}
```

## Usage

### Saving a Run

```bash
# Save with custom tag
uv run analyze analyze --match-mode all --run-tag "baseline_v1" --notes "Initial experiment"

# Save with auto-generated ID (uses seed)
uv run analyze analyze --match-mode all

# Don't save (for quick tests)
uv run analyze analyze --no-save
```

### Listing Runs

```bash
uv run analyze list-runs
```

Output:
```
┌──────────────────┬──────┬─────────┬──────────┬─────────────┬──────────┬─────────┬─────────────────┬───────┐
│ Run ID           │ Seed │ Seeded% │ Fuzzy F1 │ Semantic F1 │ Hybrid F1│ Best F1 │ Best Config     │ Notes │
├──────────────────┼──────┼─────────┼──────────┼─────────────┼──────────┼─────────┼─────────────────┼───────┤
│ run_baseline_v1  │ 1764 │ 22.5%   │ 0.053    │ 0.598       │ 0.591    │ 0.650   │ rubric_only+gpt │ First │
│ run_seed9999     │ 9999 │ 22.5%   │ 0.048    │ 0.612       │ 0.605    │ 0.668   │ rubric_only+gpt │       │
└──────────────────┴──────┴─────────┴──────────┴─────────────┴──────────┴─────────┴─────────────────┴───────┘
```

### Viewing a Run

```bash
# View config
cat runs/run_baseline_v1/config.json | jq

# View report
cat runs/run_baseline_v1/report.md

# Open plots
open runs/run_baseline_v1/assets/
```

## Run ID Format

Run IDs are generated as:
- With `--run-tag`: `run_<tag>` (e.g., `run_baseline_v1`)
- Without tag: `run_<date>_seed<seed>` (e.g., `run_2025-12-04_seed1764863751`)

## Comparison Workflow

1. Run experiments with different seeds:
   ```bash
   # First run
   uv run analyze analyze --match-mode all --run-tag "seed1764"
   
   # Change seed in manifest, regenerate dataset, run detection
   # Then analyze again
   uv run analyze analyze --match-mode all --run-tag "seed9999"
   ```

2. Compare results:
   ```bash
   uv run analyze list-runs
   ```

3. Key questions to answer:
   - Is semantic consistently better than hybrid across seeds?
   - Does F1 vary significantly with different seeds?
   - Which strategy is most robust?

## Best Practices

1. **Use descriptive tags**: `--run-tag "35pct_seeding"` not `--run-tag "test1"`

2. **Add notes**: `--notes "Testing higher seeding rate"`

3. **Keep runs small**: Only save runs you'll actually compare

4. **Clean up old runs**: Delete runs you don't need
   ```bash
   rm -rf runs/run_old_experiment
   ```

5. **Commit index.json**: Track experiment history in git
   ```bash
   git add runs/index.json
   git commit -m "Add experiment runs"
   ```
