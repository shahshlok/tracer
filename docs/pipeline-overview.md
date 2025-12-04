# Pipeline Overview

The misconception detection pipeline has three main stages: Dataset Generation, Detection, and Analysis.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PIPELINE ARCHITECTURE                              │
└─────────────────────────────────────────────────────────────────────────────┘

                    ┌─────────────────────────────────────┐
                    │         1. DATASET GENERATION       │
                    │         (utils/generators/)         │
                    └─────────────────┬───────────────────┘
                                      │
                    ┌─────────────────▼───────────────────┐
                    │  manifest.json + student code files │
                    │  (authentic_seeded/)                │
                    └─────────────────┬───────────────────┘
                                      │
                    ┌─────────────────▼───────────────────┐
                    │         2. LLM DETECTION            │
                    │         (llm_miscons_cli.py)        │
                    │                                     │
                    │  ┌───────────┐    ┌───────────┐    │
                    │  │  GPT-5.1  │    │  Gemini   │    │
                    │  │           │    │ 2.5 Flash │    │
                    │  └─────┬─────┘    └─────┬─────┘    │
                    │        │                │          │
                    │        └───────┬────────┘          │
                    └────────────────┼────────────────────┘
                                     │
                    ┌────────────────▼────────────────────┐
                    │  detections/<strategy>/<file>.json  │
                    └────────────────┬────────────────────┘
                                     │
                    ┌────────────────▼────────────────────┐
                    │         3. ANALYSIS & MATCHING      │
                    │         (analyze_cli.py)            │
                    │                                     │
                    │  ┌─────────┐ ┌──────────┐ ┌──────┐ │
                    │  │ Fuzzy   │ │ Semantic │ │Hybrid│ │
                    │  │ Matcher │ │ Matcher  │ │Matcher│ │
                    │  └────┬────┘ └────┬─────┘ └──┬───┘ │
                    │       └───────────┼──────────┘     │
                    └───────────────────┼─────────────────┘
                                        │
                    ┌───────────────────▼─────────────────┐
                    │  thesis_report.md + plots + runs/   │
                    └─────────────────────────────────────┘
```

## Stage 1: Dataset Generation

**Purpose:** Create synthetic student code with known injected misconceptions.

**Input:**
- `data/a2/groundtruth.json` - Misconception taxonomy (18 misconceptions)
- Assignment text (Q1-Q4 problem statements)

**Output:**
- `authentic_seeded/manifest.json` - Dataset configuration
- `authentic_seeded/<student_folder>/Q*.java` - Generated code files

**Key parameters:**
- 60 students × 4 questions = 240 files
- ~22.5% seeded (with misconception), ~77.5% clean
- One misconception per seeded file (for simpler evaluation)

```bash
# Interactive generator
uv run miscons

# Or via pipeline
uv run pipeline generate --students 60
```

## Stage 2: LLM Detection

**Purpose:** Have LLMs identify misconceptions in student code.

**Input:**
- Student code files
- Question text and rubric
- Prompt strategy (baseline, minimal, rubric_only, socratic)

**Output:**
- `detections/<strategy>/<student>_<question>.json`

**Models used:**
- GPT-5.1 (OpenAI)
- Gemini 2.5 Flash (Google)

**Detection format:**
```json
{
  "model": "gpt-5.1",
  "strategy": "baseline",
  "detections": [
    {
      "name": "Integer Division Trap",
      "description": "Student used integer division...",
      "student_belief": "Division always works the same way",
      "confidence": 0.85,
      "evidence": "Line 5: int result = a / b;"
    }
  ]
}
```

```bash
# Run detection
uv run llm-miscons --strategy all --model all
```

## Stage 3: Analysis & Matching

**Purpose:** Align LLM detections to ground truth and compute metrics.

**Matching process:**
1. For each LLM detection, find the best matching ground truth misconception
2. Classify as TP (correct match), FP (hallucination), or FN (missed)
3. Aggregate metrics by strategy, model, and matcher

**Matcher types:**

| Matcher | How it works | Performance |
|---------|--------------|-------------|
| `fuzzy_only` | String similarity on name/description | F1 ~0.05 |
| `semantic_only` | Cosine similarity of embeddings | F1 ~0.60 |
| `hybrid` | Weighted combination + topic prior | F1 ~0.59 |

**Output:**
- `thesis_report.md` - Markdown report with analysis
- `thesis_report.json` - Raw metrics data
- `docs/report_assets/*.png` - Visualizations
- `runs/<run_id>/` - Saved run data

```bash
# Run analysis with ablation
uv run analyze analyze --match-mode all --run-tag "experiment_v1"
```

## Full Pipeline Command

Run all stages in sequence:

```bash
uv run pipeline run --students 60 --strategies all --force --yes
```

This will:
1. Generate dataset (if needed or `--force`)
2. Run LLM detection for all strategies and models
3. Run analysis with default matcher

## Data Flow

```
groundtruth.json ──┐
                   │
manifest.json ─────┼──► build_dataframes() ──► metrics DataFrame
                   │         │
detections/*.json ─┘         │
                             ▼
                    ┌────────────────────┐
                    │ For each detection:│
                    │ 1. Match to GT     │
                    │ 2. Classify result │
                    │ 3. Record metrics  │
                    └────────────────────┘
                             │
                             ▼
                    ┌────────────────────┐
                    │ Aggregate by:      │
                    │ - strategy         │
                    │ - model            │
                    │ - match_mode       │
                    └────────────────────┘
                             │
                             ▼
                    ┌────────────────────┐
                    │ Generate:          │
                    │ - Report           │
                    │ - Plots            │
                    │ - Bootstrap CIs    │
                    └────────────────────┘
```

## Key Files

| File | Purpose |
|------|---------|
| `pipeline.py` | End-to-end orchestration |
| `analyze_cli.py` | Analysis, matching, reporting |
| `llm_miscons_cli.py` | LLM detection runner |
| `utils/matching/fuzzy.py` | Fuzzy string matching |
| `utils/matching/semantic.py` | Embedding-based matching |
| `utils/matching/hybrid.py` | Combined matching |
| `utils/generators/dataset_generator.py` | Dataset generation |
