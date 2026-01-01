# Analysis Pipeline

This document explains the complete data flow from raw student code through LLM detection to final metrics. Read this after [Architecture](architecture.md).

---

## Pipeline Overview

```
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

## Stage 1: Data Preparation

### Input Files

| File | Description |
|------|-------------|
| `data/a{1,2,3}/groundtruth.json` | Misconception definitions (18 total) |
| `authentic_seeded/a{1,2,3}/manifest.json` | Student → misconception mapping |
| `authentic_seeded/a{1,2,3}/{Student}/*.java` | 360 Java files |

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
│                         DETECTION COMBINATIONS                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Files:       360 (100 students × 3 assignments × varies by question)       │
│  Models:      6 (GPT-5.2, GPT-5.2:r, Claude, Claude:r, Gemini, Gemini:r)    │
│  Strategies:  4 (baseline, taxonomy, cot, socratic)                         │
│                                                                             │
│  Total Detections: 360 × 6 × 4 = 8,640 JSON files                           │
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

| Threshold | Value | Purpose |
|-----------|-------|---------|
| **Noise Floor** | 0.55 | Below this = pedantic noise, not counted |
| **Match Threshold** | 0.65 | Above this = semantic match found |
| **Null Template** | 0.80 | For recognizing "no bug found" responses |

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

| Metric | Before Ensemble | After Ensemble | Improvement |
|--------|-----------------|----------------|-------------|
| Precision | 0.322 | **0.649** | +107% |
| Recall | 0.868 | 0.871 | stable |
| F1 | 0.469 | **0.744** | +61% |
| False Positives | 14,236 | 1,164 | -92% |

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

### Option 1: Full Analysis (Recommended)

```bash
uv run python analyze.py analyze-multi \
    --run-name my-analysis \
    --semantic-threshold 0.65 \
    --noise-floor 0.55
```

### Option 2: Single Assignment

```bash
uv run python analyze.py analyze-multi \
    --assignment a1 \
    --run-name a1-only
```

### Option 3: Reproduce Final Results

```bash
# The final analysis run used these settings:
uv run python analyze.py analyze-multi \
    --run-name final_analysis_100 \
    --semantic-threshold 0.65 \
    --noise-floor 0.55
```

---

## Key Metrics from Final Run

### Overall Performance

| Metric | Value | 95% CI |
|--------|-------|--------|
| True Positives | 6,745 | — |
| False Positives | 14,236 | — |
| False Negatives | 1,022 | — |
| **Precision** | 0.322 | [0.315, 0.328] |
| **Recall** | 0.868 | [0.861, 0.876] |
| **F1 Score** | 0.469 | [0.462, 0.476] |

### By Assignment

| Assignment | Focus | Precision | Recall | F1 |
|------------|-------|-----------|--------|-----|
| A1 | Variables/Math | 0.219 | 0.767 | 0.341 |
| A2 | Loops/Control | 0.334 | 0.861 | 0.481 |
| A3 | Arrays/Strings | 0.462 | 0.971 | 0.626 |

### By Category (Recall)

| Category | Recall | Type |
|----------|--------|------|
| Void Machine | 99.4% | Structural |
| Mutable String | 99.0% | Structural |
| Human Index | 97.3% | Structural |
| Teleological Control | 93.1% | Structural |
| Reactive State | 65.4% | **Semantic** |
| Independent Switch | 62.5% | **Semantic** |
| Fluid Type | 59.0% | **Semantic** |

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

## Next: [Notional Machines](notional-machines.md)
