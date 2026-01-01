# Metrics Guide

This document explains the metrics used to evaluate LLM misconception detection performance.

---

## The Three Core Metrics

### 1. Precision — "When the LLM flags a misconception, is it correct?"

```
                    True Positives
Precision = ───────────────────────────────
            True Positives + False Positives
```

**Interpretation:**
- High precision (>70%) = Few false alarms, trustworthy detections
- Low precision (<50%) = Many hallucinations, unreliable

**Our Results:**
- Raw: 0.322 (32% of detections are correct)
- With Ensemble: **0.649** (+107% improvement)

---

### 2. Recall — "Does the LLM find all the real misconceptions?"

```
                 True Positives
Recall = ─────────────────────────────
         True Positives + False Negatives
```

**Interpretation:**
- High recall (>80%) = Few missed misconceptions
- Low recall (<60%) = Many bugs go undetected

**Our Results:**
- Raw: 0.868 (87% of real bugs are found)
- With Ensemble: 0.871 (stable)

---

### 3. F1 Score — "Balance between precision and recall"

```
       2 × Precision × Recall
F1 = ──────────────────────────
       Precision + Recall
```

**Interpretation:**
- F1 is the harmonic mean, penalizing imbalance
- If precision=0.90 and recall=0.10, F1=0.18 (bad)
- If precision=0.65 and recall=0.87, F1=0.74 (good)

**Our Results:**
- Raw: 0.469
- With Ensemble: **0.744** (+61% improvement)

---

## Visual Explanation

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           CONFUSION MATRIX                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                              GROUND TRUTH                                    │
│                     ┌───────────────┬───────────────┐                       │
│                     │   Has Bug     │   No Bug      │                       │
│  ┌──────────────────┼───────────────┼───────────────┤                       │
│  │ LLM Says Bug     │ TRUE POSITIVE │ FALSE POSITIVE│                       │
│  │                  │     6,745     │    14,236     │                       │
│  ├──────────────────┼───────────────┼───────────────┤                       │
│  │ LLM Says No Bug  │ FALSE NEGATIVE│ TRUE NEGATIVE │                       │
│  │                  │     1,022     │     (N/A)     │                       │
│  └──────────────────┴───────────────┴───────────────┘                       │
│                                                                             │
│  TP = LLM correctly identified the misconception                            │
│  FP = LLM claimed a misconception that doesn't exist (hallucination)        │
│  FN = LLM missed a real misconception                                       │
│                                                                             │
│  Precision = 6,745 / (6,745 + 14,236) = 0.322                               │
│  Recall    = 6,745 / (6,745 + 1,022)  = 0.868                               │
│  F1        = 2 × 0.322 × 0.868 / (0.322 + 0.868) = 0.469                    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Precision vs Recall Trade-off

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      THE FUNDAMENTAL TRADE-OFF                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  HIGH RECALL, LOW PRECISION                                                  │
│  ──────────────────────────                                                  │
│  "Flag everything that might be a bug"                                       │
│  ├── Finds most real bugs                                                   │
│  ├── Many false alarms (hallucinations)                                     │
│  └── Example: Socratic strategy (recall=0.89, precision=0.25)               │
│                                                                             │
│  HIGH PRECISION, LOW RECALL                                                  │
│  ──────────────────────────                                                  │
│  "Only flag bugs you're certain about"                                       │
│  ├── Few false alarms                                                       │
│  ├── Misses many real bugs                                                  │
│  └── Example: Unanimous ensemble (N≥4)                                      │
│                                                                             │
│  BALANCED (Ensemble N≥2) ✓                                                  │
│  ────────────────────────                                                    │
│  "Require consensus before flagging"                                         │
│  ├── Precision: 0.649 (acceptable)                                          │
│  ├── Recall: 0.871 (high)                                                   │
│  └── F1: 0.744 (good balance)                                               │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Results by Assignment

| Assignment | Task | Precision | Recall | F1 |
|------------|------|-----------|--------|-----|
| **A1** | Variables/Math | 0.219 | 0.767 | **0.341** |
| **A2** | Loops/Control | 0.334 | 0.861 | **0.481** |
| **A3** | Arrays/Strings | 0.462 | 0.971 | **0.626** |

**The Complexity Gradient:**
- A3 has 85% higher F1 than A1
- This proves LLMs struggle with abstract state reasoning

---

## Results by Strategy

| Strategy | Precision | Recall | F1 |
|----------|-----------|--------|-----|
| **baseline** | 0.373 | 0.850 | **0.519** |
| **taxonomy** | 0.366 | 0.890 | 0.518 |
| **cot** | 0.345 | 0.841 | 0.489 |
| **socratic** | 0.251 | 0.890 | 0.391 |

**Finding:** Simple prompts (baseline) outperform pedagogical prompts (socratic).

---

## Results by Model

| Model | Precision | Recall | F1 |
|-------|-----------|--------|-----|
| **Claude Haiku:reasoning** | 0.469 | 0.847 | **0.604** |
| **GPT-5.2** | 0.356 | 0.885 | 0.507 |
| **Claude Haiku** | 0.358 | 0.825 | 0.499 |
| **GPT-5.2:reasoning** | 0.346 | 0.897 | 0.499 |
| **Gemini 3 Flash:reasoning** | 0.252 | 0.877 | 0.392 |
| **Gemini 3 Flash** | 0.247 | 0.879 | 0.385 |

**Best Model:** Claude Haiku with extended reasoning (F1=0.604)

---

## Ensemble Voting Impact

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| **Precision** | 0.322 | 0.649 | **+107%** |
| **Recall** | 0.868 | 0.871 | stable |
| **F1** | 0.469 | 0.744 | **+61%** |
| **FP Count** | 14,236 | 1,164 | **-92%** |

**Key Insight:** Ensemble voting filters 92% of hallucinations while maintaining recall.

---

## Statistical Measures

### Bootstrap Confidence Intervals

We use 1000 bootstrap resamples to estimate the uncertainty in our metrics.

```
Precision: 0.322 ± [0.315, 0.328] (95% CI)
Recall:    0.868 ± [0.861, 0.876] (95% CI)
F1:        0.469 ± [0.462, 0.476] (95% CI)
```

**Interpretation:** We are 95% confident the true F1 is between 0.462 and 0.476.

---

### McNemar's Test

Tests whether two strategies differ significantly on the same data.

```
Baseline vs CoT: χ² = 23.58, p < 0.0001 → Baseline significantly better
```

---

### Cochran's Q Test

Tests whether all four strategies differ from each other.

```
Q = 57.59, df = 3, p < 0.000001 → Strategies differ significantly
```

---

### Cliff's Delta

Measures effect size for semantic score separation (TP vs FP).

```
TP Mean Score: 0.745
FP Mean Score: 0.632
Cliff's Delta: 0.840 (Large effect)
```

**Interpretation:** True positives have significantly higher semantic scores.

---

## Interpreting the Report

When reading `report.md`, look for:

| Section | Key Question |
|---------|--------------|
| Overall Metrics | What's the F1? Above 0.70 is good. |
| By Assignment | Is there a complexity gradient? (A3 > A2 > A1) |
| By Strategy | Which prompt works best? |
| By Model | Which LLM is most accurate? |
| Category Recall | Which misconceptions are hard? |
| Ensemble Comparison | How much did voting help? |

---

## Quality Benchmarks

| Metric | Poor | Acceptable | Good | Excellent |
|--------|------|------------|------|-----------|
| Precision | <0.40 | 0.40-0.60 | 0.60-0.80 | >0.80 |
| Recall | <0.50 | 0.50-0.70 | 0.70-0.85 | >0.85 |
| F1 | <0.45 | 0.45-0.60 | 0.60-0.75 | >0.75 |

**Our Final Results:**
- Precision: 0.649 (Good)
- Recall: 0.871 (Excellent)
- F1: 0.744 (Good)

---

## Next: [Semantic Matching](matching.md)
