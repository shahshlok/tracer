# CLI Reference

This document covers all command-line interfaces in the project.

## Quick Reference

| Command              | Entry Point            | Purpose                      |
| -------------------- | ---------------------- | ---------------------------- |
| `uv run pipeline`    | `pipeline.py`          | Full pipeline wizard         |
| `uv run miscons`     | `dataset_generator.py` | Generate synthetic students  |
| `uv run llm-miscons` | `llm_miscons_cli.py`   | Run LLM detection            |
| `uv run analyze`     | `analyze_cli.py`       | Analyze and generate reports |

---

## 1. Pipeline (Full Workflow)

```bash
uv run pipeline
```

Interactive wizard that runs all 3 stages:
1. Generate manifest + student submissions  
2. Run misconception detection (3 LLMs × 4 strategies)
3. Analyze with matcher ablation

### Options

```bash
uv run pipeline run \
  --students 60 \           # Number of students
  --seed 12345 \            # Random seed
  --strategies baseline,taxonomy,cot,socratic \
  --run-tag my-experiment \ # Custom run ID
  --notes "Testing new prompt" \
  --skip-generation \       # Skip step 1
  --skip-detection \        # Skip step 2
  --skip-analysis           # Skip step 3
```

### Quick Test

```bash
uv run pipeline quick --students 10 --strategy minimal
```

---

## 2. Dataset Generation

```bash
uv run miscons
```

Generates synthetic student Java files with seeded misconceptions.

### What It Does

1. Loads `groundtruth.json` misconception definitions
2. Creates `manifest.json` assigning students to misconceptions
3. Calls GPT-5.1 to generate Java code for each student×question

### Distribution

- 40% Perfect students (all clean files)
- 35% Single-issue (1 misconception)
- 15% Multi-issue (2 misconceptions)
- 10% Struggling (3+ misconceptions)

---

## 3. LLM Detection

```bash
uv run llm-miscons detect --strategy taxonomy --students 0
```

Runs LLM misconception detection on generated files.

### Options

| Option           | Default       | Description                       |
| ---------------- | ------------- | --------------------------------- |
| `--strategy`     | taxonomy      | baseline, taxonomy, cot, socratic |
| `--students`     | 0             | Number to process (0 = all)       |
| `--output`       | detections/a1 | Output directory                  |
| `--no-reasoning` | false         | Disable reasoning model variants  |

### Run All Strategies

```bash
uv run llm-miscons all-strategies --students 0
```

### Models Used

| Model                                   | Short Name       |
| --------------------------------------- | ---------------- |
| openai/gpt-5.1                          | GPT-5.1          |
| google/gemini-2.5-flash-preview-09-2025 | Gemini-2.5-Flash |
| anthropic/claude-haiku-4.5              | Haiku-4.5        |

Plus `:reasoning` variants for each.

---

## 4. Analysis

```bash
uv run analyze analyze --match-mode hybrid
```

Analyzes detections against ground truth and generates reports.

### Options

| Option         | Default | Description                            |
| -------------- | ------- | -------------------------------------- |
| `--match-mode` | hybrid  | fuzzy_only, semantic_only, hybrid, all |
| `--run-tag`    | auto    | Custom run identifier                  |
| `--notes`      | ""      | Notes for this run                     |
| `--quick`      | false   | Fewer bootstrap iterations             |

### Matcher Ablation

```bash
uv run analyze analyze --match-mode all --run-tag ablation-study
```

Compares all 3 matchers on the same data.

### Output

Saves to `runs/a1/run_{id}/`:
- `report.md` - Full analysis with embedded charts
- `config.json` - Run configuration
- `data.json` - Raw metrics and opportunities
- `assets/` - PNG visualizations

---

## Environment Variables

```bash
# Required
OPENROUTER_API_KEY=sk-or-...

# Optional (for semantic matching)
OPENAI_API_KEY=sk-...
```
