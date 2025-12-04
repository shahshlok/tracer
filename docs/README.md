# LLM Misconception Detection Pipeline

A research framework for evaluating LLM-based misconception detection in CS1 programming assignments.

## Quick Start

```bash
# Run full analysis with matcher ablation
uv run analyze analyze --match-mode all --run-tag "my_experiment"

# List all saved runs
uv run analyze list-runs

# Run the complete pipeline (generate + detect + analyze)
uv run pipeline run --students 60 --strategies all --force
```

## Documentation

| Document | Description |
|----------|-------------|
| [Pipeline Overview](pipeline-overview.md) | End-to-end workflow and architecture |
| [CLI Reference](cli-reference.md) | All commands with examples |
| [Matcher Ablation](matcher-ablation.md) | Fuzzy vs Semantic vs Hybrid matching |
| [Run Storage](run-storage.md) | How runs are saved and compared |
| [Dataset Format](dataset-format.md) | Manifest, groundtruth, and detection formats |
| [Report Guide](report-guide.md) | Understanding the generated reports |

## Project Structure

```
ensemble-eval-cli/
├── analyze_cli.py          # Analysis and reporting (main entry point)
├── pipeline.py             # End-to-end orchestration
├── llm_miscons_cli.py      # LLM detection runner
├── authentic_seeded/       # Generated student submissions
│   └── manifest.json       # Dataset configuration
├── data/a2/
│   └── groundtruth.json    # Misconception taxonomy
├── detections/             # LLM detection outputs
│   ├── baseline/
│   ├── minimal/
│   ├── rubric_only/
│   └── socratic/
├── runs/                   # Saved experiment runs
│   ├── index.json
│   └── run_<tag>/
├── docs/                   # Documentation
└── thesis_report.md        # Generated analysis report
```

## Key Concepts

### Misconception Detection Pipeline

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Generate   │    │   Detect    │    │    Match    │    │   Analyze   │
│  Dataset    │───►│   (LLMs)    │───►│  (Align)    │───►│  & Report   │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
     │                   │                  │                  │
     ▼                   ▼                  ▼                  ▼
  manifest.json     detections/      matched_id,score    thesis_report.md
  + code files      *.json           for each detection   + plots
```

### Matchers

| Matcher | Method | Typical F1 |
|---------|--------|------------|
| `fuzzy_only` | String similarity on names | ~0.05 (fails) |
| `semantic_only` | OpenAI embeddings (text-embedding-3-large) | ~0.60 |
| `hybrid` | Fuzzy + Semantic + Topic prior | ~0.59 |

### Prompt Strategies

| Strategy | Description |
|----------|-------------|
| `baseline` | Standard prompt with full rubric |
| `minimal` | Minimal instructions |
| `rubric_only` | Only rubric, no examples |
| `socratic` | Step-by-step reasoning |

## Typical Workflow

### 1. Generate Dataset (if needed)

```bash
uv run miscons  # Interactive dataset generator
```

### 2. Run Detection

```bash
uv run llm-miscons --strategy all --model all
```

### 3. Run Analysis

```bash
# Quick analysis with hybrid matcher
uv run analyze analyze --quick

# Full ablation study
uv run analyze analyze --match-mode all --run-tag "experiment_v1"
```

### 4. Compare Runs

```bash
uv run analyze list-runs
```

## Research Context

This framework supports a Bachelor's honours thesis evaluating LLM-based misconception detection for CS1 education, targeting publication at venues like ITiCSE and SIGCSE.

**Key findings so far:**
- Semantic matching (embeddings) is essential; fuzzy string matching fails
- Hybrid matching doesn't significantly outperform pure semantic
- LLMs detect obvious structural errors well, but struggle with subtle semantic issues

## License

Research project - contact author for usage.
