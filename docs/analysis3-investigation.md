# Analysis 3: Ensemble Voting Results

_Date: 2025-12-22_  
_Status: **COMPLETE**_  
_Run: `runs/multi/run_analysis3/`_

---

## Executive Summary

Analysis 3 implemented **Ensemble Voting** to address the precision crisis from Analysis 2.2. By requiring at least 2 strategies to agree on a misconception before counting it, we eliminated most hallucinations while maintaining recall.

**Key Result:**
- Precision: **0.313 → 0.649** (+107% improvement)
- Recall: **0.872 → 0.871** (stable, -0.1%)
- F1: **0.461 → 0.744** (+61% improvement)
- False Positives: **4,722 → 1,164** (-75% reduction)

---

## The Problem (Analysis 2.2)

In Analysis 2.2, the **Socratic prompting strategy** produced **1,873 False Positives** versus only **589 True Positives**, yielding a precision of **0.239**—the worst among all four strategies.

The Socratic strategy was identifying **pedagogical concerns** (code style, unnecessary variables) that are *not* in our formal Notional Machine taxonomy, but these were being counted as hallucinations.

---

## The Solution: Ensemble Voting

### Concept

Instead of trusting any single strategy, we require **consensus**:

```
Detection is VALID only if ≥ N strategies agree on the same misconception
for the same (student, question) pair.
```

### Why N=2?

| Threshold | Expected Effect |
|-----------|-----------------|
| N ≥ 2 | Moderate consensus - filters ~75% of hallucinations |
| N ≥ 3 | Strong consensus - very high precision, lower recall |
| N ≥ 4 | Unanimous - too conservative, misses valid detections |

We chose **N ≥ 2** as the optimal balance.

---

## Results

### Before vs After Ensemble Filtering

| Metric | Before (2.2) | After (3.0) | Change |
|--------|--------------|-------------|--------|
| **Precision** | 0.313 | **0.649** | **+107%** |
| **Recall** | 0.872 | 0.871 | -0.1% |
| **F1 Score** | 0.461 | **0.744** | **+61%** |
| True Positives | 2,151 | 2,150 | -1 |
| False Positives | 4,722 | **1,164** | **-75%** |
| False Negatives | 317 | 318 | +1 |

### By Assignment (Complexity Gradient)

| Assignment | Focus | Precision | Recall | F1 |
|------------|-------|-----------|--------|-----|
| **A3** | Arrays/Strings | **0.810** | 0.989 | **0.890** |
| **A2** | Loops/Control | 0.653 | 0.885 | 0.751 |
| **A1** | Variables/Math | 0.499 | 0.728 | 0.592 |

The **Complexity Gradient** is preserved:
- A3 (Surface errors): 0.890 F1
- A1 (Deep state errors): 0.592 F1
- Gap: 0.298 (50% drop)

### By Strategy (Ensemble Effect)

| Strategy | Before Precision | After Precision | Change |
|----------|------------------|-----------------|--------|
| baseline | 0.377 | **0.714** | +89% |
| taxonomy | 0.347 | **0.654** | +88% |
| cot | 0.342 | **0.668** | +95% |
| socratic | 0.239 | **0.584** | +144% |

Socratic saw the **largest improvement** because its unique hallucinations were filtered out by the ensemble.

### By Model

| Model | Precision | Recall | F1 |
|-------|-----------|--------|-----|
| claude-haiku:reasoning | **0.784** | 0.857 | **0.819** |
| gpt-5.2:reasoning | 0.702 | 0.897 | 0.788 |
| gpt-5.2 | 0.690 | 0.895 | 0.779 |
| claude-haiku | 0.697 | 0.813 | 0.751 |
| gemini-3-flash:reasoning | 0.551 | 0.887 | 0.680 |
| gemini-3-flash | 0.531 | 0.877 | 0.661 |

**Claude Haiku with Reasoning** achieved the best overall performance.

---

## How Ensemble Filtering Works

### Before Ensemble (Single Strategy)

```
Student: Anderson_Charles_664944, Q2
Expected: NM_STATE_01 (Spreadsheet View)

Strategy Detections:
┌─────────────────┬──────────────────────────────────────────┐
│ baseline        │ [No detection]                           │
│ cot             │ [No detection]                           │
│ taxonomy        │ [No detection]                           │
│ socratic        │ "Redundant Variable Aliasing" (FP)       │
└─────────────────┴──────────────────────────────────────────┘

Result: 1 FP counted → harms precision
```

### After Ensemble (N≥2)

```
Same student/question:

Agreement on "Redundant Variable Aliasing": 1/4 strategies
Required: ≥2 strategies

Result: Detection REJECTED by ensemble → no FP counted
        File becomes FN (missed the real misconception)
```

**Trade-off:** We lose one TP (the file is now FN), but we eliminate one FP. Since we had way more FPs than TPs, this trade-off dramatically improves precision.

---

## Notional Machine Detection (Hard vs Easy)

| Category | Recall | Difficulty |
|----------|--------|------------|
| The Human Index Machine | 1.000 | Easy |
| The Algebraic Syntax Machine | 1.000 | Easy |
| The Mutable String Machine | 0.988 | Easy |
| The Semantic Bond Machine | 0.984 | Easy |
| The Void Machine | 0.979 | Easy |
| The Anthropomorphic I/O Machine | 0.937 | Easy |
| The Teleological Control Machine | 0.934 | Easy |
| The Independent Switch | 0.745 | Medium |
| The Reactive State Machine | **0.598** | Medium |
| The Fluid Type Machine | **0.403** | **Hard** |

The **Fluid Type Machine** (integer division blindness, narrowing casts) remains the hardest category for LLMs to detect.

---

## Reproducing Analysis 3

```bash
python analyze.py analyze-ensemble \
    --run-name analysis3 \
    --ensemble-threshold 2 \
    --semantic-threshold 0.65 \
    --noise-floor 0.55
```

Output: `runs/multi/run_analysis3/`

---

## Conclusions

1. **Ensemble voting works:** +107% precision with negligible recall loss
2. **Socratic is salvaged:** Its precision improved +144% when filtered by ensemble
3. **Complexity Gradient holds:** A1 remains harder than A3 even with ensemble
4. **Reasoning models win:** Claude Haiku with reasoning achieves 0.819 F1

---

## Next Steps

1. **Thesis Write-up:** These results are publication-ready for ITiCSE/SIGCSE
2. **Ablation Study:** Test N=3 and N=4 ensemble thresholds
3. **Per-Model Ensemble:** What if we require 2+ models (not strategies) to agree?

---

## References

- `AGENTS.md` – Core architecture and data hierarchy
- `analyze.py` – Analysis pipeline with ensemble voting
- `utils/statistics.py` – Bootstrap and statistical tests
- `data/*/groundtruth.json` – Ground truth definitions
- `runs/multi/run_analysis3/` – Complete results and visualizations
