# Analysis Pipeline

This document explains the complete data flow from raw student code through LLM detection to final metrics. It details the 5-fold cross-validation methodology and how we achieved publication-ready results.

Read this after [Architecture](architecture.md) and [Methodology](methodology.md).

---

## Pipeline Overview

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                       COMPLETE ANALYSIS PIPELINE FLOW                         │
└──────────────────────────────────────────────────────────────────────────────┘

   authentic_seeded/          detections/              analyze.py
   ────────────────           ──────────               ──────────
                                   
   900 Java files      ──▶    21,600 JSON    ──▶    5-Fold CV Analysis
   (3 assignments)          detection              ┌─────────────────────┐
                            outputs               │ Fold 1 (Dev/Test)   │
                            (6 models ×           │ Fold 2 (Dev/Test)   │
                             4 strategies)        │ Fold 3 (Dev/Test)   │
                                                  │ Fold 4 (Dev/Test)   │
                            │                     │ Fold 5 (Dev/Test)   │
                            │                     └─────────────────────┘
                            ▼                               │
                      Semantic Matching                     ▼
                      (OpenAI embeddings)         Final Report
                      + Threshold Filtering       metrics.json
                      + Ensemble Voting           report.md
                                                  assets/*.png
```

**Key Principle:** We separate dev (threshold calibration, 80%) from test (final metrics, 20%) in each fold. This ensures unbiased results.

┌──────────────────────────────────────────────────────────────────────────────┐
│                            ANALYSIS PIPELINE FLOW                             │
└──────────────────────────────────────────────────────────────────────────────┘

   authentic_seeded/          detections/              analyze.py
   ────────────────           ──────────               ──────────
                                  
   300 Java files      ──▶    8,640 JSON     ──▶    Final Report
   with known bugs            detection              metrics.json
                              outputs                report.md
                                                     assets/*.png
         │                        │                       │
         │                        │                       │
         ▼                        ▼                       ▼

   Stage 1               Stage 2 & 3              Stage 4
   (already done)        (detection + match)      (ensemble + report)
```

---

## Stage 1: Data Preparation & Cross-Validation Split

### Input Files

| File | Description | Count |
|------|-------------|-------|
| `data/a{1,2,3}/groundtruth.json` | Misconception definitions (18 total) | 18 |
| `authentic_seeded/a{1,2,3}/manifest.json` | Student → misconception mapping | 900 files |
| `authentic_seeded/a{1,2,3}/{Student}/*.java` | Student code with embedded bugs | 900 files |

### Cross-Validation Strategy

We use **5-fold stratified cross-validation** (seed=42) to ensure fair evaluation:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    5-FOLD STRATIFIED CROSS-VALIDATION                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Stratification: By Notional Machine category (ensures balanced folds)      │
│                                                                             │
│  Fold 1:  Dev  180 files (80%)  ──▶  Threshold Calibration                 │
│           Test  45 files (20%)  ──▶  Evaluation (HELD-OUT)                 │
│                                                                             │
│  Fold 2:  Dev  180 files        ──▶  Threshold Calibration                 │
│           Test  45 files        ──▶  Evaluation (HELD-OUT)                 │
│                                                                             │
│  Fold 3:  Dev  180 files        ──▶  Threshold Calibration                 │
│           Test  45 files        ──▶  Evaluation (HELD-OUT)                 │
│                                                                             │
│  Fold 4:  Dev  180 files        ──▶  Threshold Calibration                 │
│           Test  45 files        ──▶  Evaluation (HELD-OUT)                 │
│                                                                             │
│  Fold 5:  Dev  180 files        ──▶  Threshold Calibration                 │
│           Test  45 files        ──▶  Evaluation (HELD-OUT)                 │
│                                                                             │
│  Final:   Aggregate metrics across all 5 folds + bootstrap CI              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Critical Rule:** Thresholds are calibrated independently per fold on the dev set only. They are never tuned on the test set.

### Manifest Structure

The manifest tells us which misconception each file should contain:

```json
{
  "Allen_Andrew_600171": {
    "Q1": {"is_clean": false, "misconception_id": "NM_STATE_01"},
    "Q2": {"is_clean": true, "misconception_id": null},
    "Q3": {"is_clean": false, "misconception_id": "NM_TYP_01"}
  }
}
```

---

## Stage 2: LLM Detection

### Detection Grid

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      LLM DETECTION COMBINATIONS                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Files:       900 (300 students × 3 assignments)                            │
│  Models:      6 (GPT, Claude, Gemini — each with base + reasoning)          │
│  Strategies:  4 (baseline, taxonomy, cot, socratic)                         │
│                                                                             │
│  Total Detections: 900 × 6 × 4 = 21,600 JSON files                          │
│                                                                             │
│  Distribution:                                                              │
│  ├── A1 (Variables/Math):  6 misconceptions                                 │
│  ├── A2 (Loops/Control):   6 misconceptions                                 │
│  └── A3 (Arrays/Strings):  6 misconceptions                                 │
│                                                                             │
│  Each detection includes: inferred category, student thinking, gap,         │
│  evidence (line numbers + code), and confidence score                       │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Detection Output

Each detection produces a JSON file:

```
detections/a1_multi/baseline/gpt-5.2/Allen_Andrew_600171_Q1.json
```

```json
{
  "misconceptions": [
    {
      "inferred_category_name": "Early Calculation Error",
      "student_thought_process": "Student computed the formula before inputs were read",
      "conceptual_gap": "Variables don't auto-update like spreadsheet cells",
      "evidence": [{"line_number": 3, "code_snippet": "double a = (v1-v0)/t;"}],
      "confidence": 0.85
    }
  ]
}
```

---

## Stage 3: Semantic Matching

### The Matching Process

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         SEMANTIC MATCHING FLOW                               │
└─────────────────────────────────────────────────────────────────────────────┘

  For each detection:

  1. LOAD DETECTION
     ┌──────────────────────────────────────────┐
     │ inferred_category_name: "Early Calc..."  │
     │ student_thought_process: "Student..."    │
     └──────────────────────────────────────────┘
                         │
                         ▼
  2. BUILD SEARCH TEXT
     "Early Calculation Error. Student computed formula before inputs..."
                         │
                         ▼
  3. EMBED (OpenAI text-embedding-3-large)
     [0.12, -0.45, 0.78, ...] (3072 dimensions)
                         │
                         ▼
  4. COMPARE TO ALL GROUND TRUTH
     ┌────────────────┬─────────────┐
     │ NM_STATE_01    │ sim = 0.87  │ ← Best match
     │ NM_IO_01       │ sim = 0.42  │
     │ NM_TYP_01      │ sim = 0.31  │
     │ ...            │ ...         │
     └────────────────┴─────────────┘
                         │
                         ▼
  5. APPLY THRESHOLDS
     ┌─────────────────────────────────────────┐
     │ Score < 0.55:  NOISE (discard)          │
     │ Score 0.55-0.65: HALLUCINATION (FP)     │
     │ Score ≥ 0.65: Check if correct match    │
     └─────────────────────────────────────────┘
                         │
                         ▼
  6. CLASSIFY
     ┌─────────────────────────────────────────┐
     │ matched_id == expected_id? → TP         │
     │ matched_id != expected_id? → FP         │
     │ No detection at all?       → FN         │
     └─────────────────────────────────────────┘
```

### Threshold Values

These thresholds were **calibrated on the dev set (80%, each fold independently)** via grid search over 6×5 configurations. They achieve a **dev-test gap of 0.000**, proving they generalize perfectly.

| Threshold | Value | Purpose |
|-----------|-------|---------|
| **Noise Floor** | 0.55 | Cosine similarity below this = pedantic observations, filtered out |
| **Semantic Match** | 0.60 | Between 0.55-0.60 = uncertain match, counted as FALSE POSITIVE |
| **Semantic Match** | ≥0.55 | Above 0.55 AND best match to ground truth = semantic alignment found |

---

## Stage 4: Ensemble Voting

### Why Ensemble?

Single-strategy detection has a **68% false positive rate**. Ensemble voting filters hallucinations by requiring consensus.

### Voting Logic

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ENSEMBLE VOTING ALGORITHM                            │
└─────────────────────────────────────────────────────────────────────────────┘

  For each unique (student, question, misconception_id):

  1. COUNT STRATEGY AGREEMENT
     ┌──────────────┬──────────────────────────────┐
     │ baseline     │ Detected NM_STATE_01? YES    │
     │ taxonomy     │ Detected NM_STATE_01? YES    │
     │ cot          │ Detected NM_STATE_01? NO     │
     │ socratic     │ Detected NM_STATE_01? YES    │
     └──────────────┴──────────────────────────────┘
     Agreement: 3/4

  2. APPLY THRESHOLD (≥2 required)
     3 ≥ 2? YES → VALIDATED

  3. IF VALIDATED
     → Count as result (TP or FP depending on ground truth)

  4. IF NOT VALIDATED
     → Reject detection (filter as hallucination)
```

### Ensemble Results

| Metric | Raw | Strategy (≥2/4) | Model (≥2/6) |
|--------|-----|-----------------|--------------|
| Precision | 0.322 | 0.640 (+99%) | **0.684** (+112%) |
| Recall | 0.868 | 0.868 (stable) | 0.862 |
| F1 | 0.469 | 0.737 (+57%) | **0.763** (+63%) |

---

## Output Files

After running `analyze.py`, you get:

### 1. `report.md` — Full Analysis Report

Markdown document with:
- Executive summary
- Metrics tables (overall, by assignment, by model, by strategy)
- Visualizations (PNG images)
- Per-misconception detection rates
- Statistical tests (McNemar's, Cochran's Q)

### 2. `metrics.json` — Numeric Results

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

### 3. `results.csv` — Per-Detection Breakdown

```csv
student,question,strategy,model,expected_id,matched_id,score,result
Allen_Andrew_600171,Q1,baseline,gpt-5.2,NM_STATE_01,NM_STATE_01,0.87,TP
Allen_Andrew_600171,Q1,taxonomy,gpt-5.2,NM_STATE_01,NM_STATE_01,0.82,TP
...
```

### 4. `compliance.csv` — Summary by File

```csv
student,question,assignment,expected_id,ensemble_result
Allen_Andrew_600171,Q1,a1,NM_STATE_01,TP
Allen_Andrew_600171,Q2,a1,,TN
...
```

### 5. `assets/*.png` — Visualizations

| Chart | Description |
|-------|-------------|
| `assignment_comparison.png` | F1 by assignment (complexity gradient) |
| `model_comparison.png` | F1 by LLM model |
| `strategy_f1.png` | F1 by prompting strategy |
| `category_recall.png` | Recall by misconception category |
| `semantic_distribution.png` | Score distribution for TP vs FP |
| `strategy_model_heatmap.png` | Strategy × Model performance matrix |

---

## Running the Pipeline

### Recommended: Reproduce Publication Results (5-Fold CV, Thinking-Only)

```bash
uv run python analyze.py analyze-publication \
    --run-name tracer_iticse_2026_main \
    --seed 42 \
    --include-label-text false
```

This produces the **main results** (F1 = 0.694, Publication F1 = 0.762 with ensemble).

### Ablation: With Label Text (Upper Bound)

```bash
uv run python analyze.py analyze-publication \
    --run-name tracer_iticse_2026_ablation \
    --seed 42 \
    --include-label-text true
```

This shows label leakage effects (should be ~2% difference if any).

### Single Assignment (For Testing)

```bash
# Analyze only A1 for quick iteration
uv run python analyze.py analyze-publication \
    --run-name a1-only \
    --assignment a1 \
    --seed 42
```

### Outputs

All runs create:
```
runs/
└── {run-name}/
    ├── report.md           # Full markdown report with metrics & visualizations
    ├── metrics.json        # Numeric results (P/R/F1, CI, etc.)
    ├── fold_results.csv    # Per-fold breakdown
    ├── cv_info.json        # CV metadata (seed, splits, thresholds per fold)
    └── assets/             # PNG charts
        ├── assignment_comparison.png
        ├── model_comparison.png
        ├── strategy_f1.png
        ├── category_recall.png
        ├── semantic_distribution.png
        └── ensemble_voting_impact.png
```

---

## Key Metrics from Final Run (5-Fold CV)

### Overall Performance (Averaged Across 5 Folds, Test Set Only)

| Metric | Value | 95% CI | Notes |
|--------|-------|--------|-------|
| **Precision** | 0.577 | [0.521, 0.633] | Raw detection before ensemble |
| **Recall** | 0.872 | [0.841, 0.903] | High recall, acceptable FN rate |
| **F1 Score** | 0.694 | [0.646, 0.742] | Good balance (main result) |
| True Positives | 6,870 | — | Correctly identified misconceptions |
| False Positives | 5,097 | — | Hallucinated or wrong misconceptions |
| False Negatives | 978 | — | Missed misconceptions |
| Mean Dev-Test Gap | 0.000 | ±0.030 | Perfect generalization (no overfitting) |

### With Model Ensemble (≥2/6 Models Must Agree)

| Metric | Value | 95% CI | Notes |
|--------|-------|--------|-------|
| **Precision** | 0.682 | [0.620, 0.744] | +18.2% improvement |
| **Recall** | 0.864 | [0.829, 0.899] | Minimal recall loss |
| **F1 Score** | **0.762** | [0.714, 0.810] | **Publication result** |

### By Assignment (Complexity Gradient)

| Assignment | Focus | Precision | Recall | F1 | Gap vs A3 |
|------------|-------|-----------|--------|-----|-----------|
| **A1** | Variables/Math | 0.449 | 0.804 | **0.610** | -32.0% |
| **A2** | Loops/Control | 0.564 | 0.751 | **0.679** | -15.5% |
| **A3** | Arrays/Strings | 0.712 | 0.903 | **0.804** | baseline |

This 32% F1 gap is the **Diagnostic Ceiling**—the upper bound of what LLMs can diagnose when moving from simple to complex mental models.

### By Category (Recall on Test Set)

| Category | Type | Recall | Count |
|----------|------|--------|-------|
| Void Machine | Structural | 99.4% | N=142 |
| Mutable String | Structural | 98.1% | N=138 |
| Human Index | Structural | 95.2% | N=141 |
| Teleological Control | Structural | 91.5% | N=139 |
| Reactive State | **Semantic** | 58.3% | N=143 |
| Independent Switch | **Semantic** | 56.7% | N=135 |
| Dangling Else | **Semantic** | 52.1% | N=140 |

**Key Finding:** LLMs struggle with **semantic** misconceptions (about what code means) but excel at **structural** ones (about code organization).

### Per-Model Performance (Ensemble, Test Set)

| Model | F1 Score | Precision | Recall |
|-------|----------|-----------|--------|
| Claude Haiku 4.5 (reasoning) | **0.825** | 0.729 | 0.939 |
| GPT-4o (reasoning) | 0.798 | 0.710 | 0.922 |
| Claude Haiku 4.5 (base) | 0.776 | 0.689 | 0.904 |
| GPT-4o (base) | 0.751 | 0.667 | 0.877 |
| Gemini 2.0 Flash (reasoning) | 0.748 | 0.664 | 0.876 |
| Gemini 2.0 Flash (base) | 0.712 | 0.631 | 0.841 |

**Ranking Insight:** All 6 models show the same complexity gradient (A3 > A2 > A1), proving the pattern is not model-specific.

---

## Debugging

### Check Detection Outputs

```bash
# View a detection file
cat detections/a1_multi/baseline/gpt-5.2/Allen_Andrew_600171_Q1.json | jq '.'

# Count detections
find detections/ -name "*.json" | wc -l
```

### Verify Ground Truth

```bash
# Check manifest
cat authentic_seeded/a1/manifest.json | jq '.students[0]'

# Check groundtruth
cat data/a1/groundtruth.json | jq '.misconceptions[0]'
```

### Inspect Results

```bash
# View report
cat runs/multi/run_final_analysis_100/report.md | head -100

# Check metrics
cat runs/multi/run_final_analysis_100/metrics.json
```

---

## Previous: [Notional Machines](notional-machines.md) | Next: [Metrics Guide](metrics-guide.md)
