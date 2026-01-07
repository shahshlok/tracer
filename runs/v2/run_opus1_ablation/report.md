# TRACER: LLM Cognitive Alignment Analysis

## Taxonomic Research of Aligned Cognitive Error Recognition

_Generated: 2026-01-07T04:33:06.981063+00:00_

---

## Executive Summary

This report evaluates **LLM cognitive alignment** with CS education theory by measuring
whether models can identify *student mental models* (Notional Machines), not just surface-level bugs.

**Dataset:** 300 students × 3 assignments × 4 strategies × 6 models

**Validation:** 5-fold stratified cross-validation by Notional Machine category (seed 42)

### Cross-Validation Results

| Metric | Mean | Std Dev | 95% CI (approx) |
|--------|------|---------|-----------------|
| **Precision** | **0.511** | ±0.026 | [0.460, 0.562] |
| **Recall** | **0.982** | ±0.006 | [0.969, 0.995] |
| **F1 Score** | **0.673** | ±0.024 | [0.626, 0.719] |

**Raw Counts (aggregated):** TP=5,891 | FP=5,616 | FN=107

### Key Findings

1. **Ensemble Voting** improves F1 by +0.063 through precision gains
2. **Detection Gap:** 'The Independent Switch' (96%) vs 'The Void Machine' (99%)
3. **Semantic Matching** effectively separates TPs from FPs (large effect size)

---

## 1. Methodology Validation

### 1.0 Cross-Validation Fold Breakdown

> Each fold uses 80% of files for threshold calibration (dev) and 20% for evaluation (test).
> Stratification by Notional Machine category ensures balanced representation across folds.

| Fold | Dev Files | Test Files | Sem. Thresh | Noise Floor | Dev F1 | Test F1 | Gap |
|------|-----------|------------|-------------|-------------|--------|---------|-----|
| 1 | 957 | 243 | 0.55 | 0.60 | 0.668 | 0.691 | -0.023 ✓ |
| 2 | 958 | 242 | 0.55 | 0.60 | 0.671 | 0.682 | -0.011 ✓ |
| 3 | 960 | 240 | 0.55 | 0.60 | 0.670 | 0.686 | -0.016 ✓ |
| 4 | 962 | 238 | 0.55 | 0.60 | 0.683 | 0.632 | +0.051 ⚠ |
| 5 | 963 | 237 | 0.55 | 0.60 | 0.673 | 0.672 | +0.001 ✓ |

**Generalization Analysis:**

- **Mean Dev-Test Gap:** +0.000 ± 0.030
- **Interpretation:** Excellent generalization (thresholds transfer well)
- **Threshold Consistency:** All folds selected same thresholds

### 1.1 Threshold Calibration

To identify the optimal classification thresholds for this dataset, we performed an exhaustive grid search over the threshold parameter space:

- **6 semantic similarity thresholds:** 0.55, 0.60, 0.65, 0.70, 0.75, 0.80
- **5 noise floor values:** 0.40, 0.45, 0.50, 0.55, 0.60
- **Total configurations:** 30 (6 × 5)

Thresholds were calibrated on the **dev split** and applied to the held-out **test split**.

For each configuration, we computed full precision, recall, and F1 scores across all detections, then selected the pair that maximized F1 score.

**Optimal Configuration Found:**

| Parameter | Value | Rationale |
|-----------|-------|-----------|
| Semantic Threshold | **0.55** | Maximizes true positives while minimizing false positives |
| Noise Floor | **0.6** | Filters pedantic detections without losing valid signals |
| Achieved F1 | **0.668** | Best balanced performance across the entire grid |

All metrics reported in this analysis use these calibrated thresholds.

![Threshold Sensitivity Heatmap](assets/threshold_sensitivity_heatmap.png)

> The heatmap shows F1 scores across the entire threshold grid. The star (★) marks the optimal configuration.

### 1.2 Semantic Matching Validation

> Validates that semantic similarity effectively discriminates TPs from FPs.

| Metric | True Positives | False Positives |
|--------|----------------|-----------------|
| Count | 5,891 | 5,616 |
| Mean Score | 0.749 | 0.674 |
| Std Dev | 0.058 | 0.051 |

**Effect Size (Cliff's Delta):** 0.663 (Large)

---

## 2. Main Findings

### 2.1 The Detection Gap: Structural vs Semantic Misconceptions (RQ2)

![Structural vs Semantic](assets/category_structural_vs_semantic.png)

#### Per-Category Breakdown

| Category | Type | Recall | N | Difficulty |
|----------|------|--------|---|------------|
| The Independent Switch | Semantic | 0.955 | 374 | Easy |
| The Fluid Type Machine | Semantic | 0.977 | 525 | Easy |
| The Anthropomorphic I/O Machine | Semantic | 0.978 | 447 | Easy |
| The Algebraic Syntax Machine | Structural | 0.979 | 432 | Easy |
| The Reactive State Machine | Semantic | 0.979 | 287 | Easy |
| The Teleological Control Machine | Structural | 0.985 | 1789 | Easy |
| The Semantic Bond Machine | Structural | 0.986 | 768 | Easy |
| The Human Index Machine | Structural | 0.988 | 608 | Easy |
| The Mutable String Machine | Structural | 0.988 | 600 | Easy |
| The Void Machine | Structural | 0.994 | 168 | Easy |

### 2.2 Per-Misconception Analysis

> Individual misconception detection rates reveal specific diagnostic gaps.

#### Misconceptions Requiring Human Oversight (Bottom 5)

| ID | Name | Category | Recall | N |
|----|------|----------|--------|---|
| NM_LOGIC_02 | Dangling Else (Indentation Trap) | The Independent Switch | 0.77 | 62 |
| NM_IO_01 | Prompt-Logic Mismatch | The Anthropomorphic I/O M | 0.90 | 60 |
| NM_SYN_02 | Precedence Blindness | The Algebraic Syntax Mach | 0.97 | 312 |
| NM_FLOW_03 | Infinite Loop (State Stagnation) | The Teleological Control  | 0.97 | 550 |
| NM_TYP_01 | Integer Division Blindness | The Fluid Type Machine | 0.97 | 355 |

![Misconception Recall](assets/misconception_recall.png)

### 2.3 Ensemble Voting Effect

> Ensemble voting requires multiple agreeing sources, trading recall for precision.

| Method | Precision | Recall | F1 | Precision Gain |
|--------|-----------|--------|-----|----------------|
| Raw (No Ensemble) | 0.512 | 0.982 | 0.673 | — |
| Strategy Ensemble (≥2/4) | 0.548 | 0.982 | 0.703 | +0.036 |
| Model Ensemble (≥2/6) | 0.591 | 0.976 | 0.736 | +0.079 |

**Best Method:** Model Ensemble (F1 = 0.736, +0.063 over raw)

---

## 3. Comparative Analysis

### 3.1 Prompting Strategy Comparison

| Strategy | TP | FP | FN | Precision | Recall | F1 |
|----------|----|----|----|-----------| -------|-----|
| baseline | 1473 | 1108 | 13 | 0.571 | 0.991 | 0.724 |
| cot | 1424 | 1199 | 75 | 0.543 | 0.950 | 0.691 |
| socratic | 1491 | 2131 | 11 | 0.412 | 0.993 | 0.582 |
| taxonomy | 1503 | 1178 | 8 | 0.561 | 0.995 | 0.717 |

![Strategy Performance Comparison](assets/strategy_comparison.png)

> Grouped bar chart comparing 7 metrics across 4 prompting strategies. Each metric shows 4 bars side-by-side (baseline, chain-of-thought, socratic, taxonomy-guided). The diagnostic ceiling line (0.7) marks the threshold below which human oversight is required.

#### Statistical Significance (McNemar's Test)

| Comparison | χ² | p-value | Significant? |
|------------|-----|---------|--------------|
| baseline vs cot | 15.67 | 0.0001 | Yes |
| baseline vs socratic | 2.58 | 0.1082 | No |
| baseline vs taxonomy | 7.25 | 0.0071 | Yes |
| cot vs socratic | 26.40 | 0.0000 | Yes |
| cot vs taxonomy | 37.33 | 0.0000 | Yes |
| socratic vs taxonomy | 1.10 | 0.2943 | No |

#### Omnibus Test (Cochran's Q)

- **Q Statistic:** 53.50
- **p-value:** 0.000000
- **Conclusion:** Significant differences exist

### 3.2 Model Comparison

| Model | TP | FP | FN | Precision | Recall | F1 |
|-------|----|----|----|-----------|--------|-----|
| claude-haiku-4-5-20251001:reasoning | 967 | 459 | 15 | 0.678 | 0.985 | 0.803 |
| gpt-5.2-2025-12-11:reasoning | 1009 | 831 | 3 | 0.548 | 0.997 | 0.708 |
| gpt-5.2-2025-12-11 | 994 | 838 | 5 | 0.543 | 0.995 | 0.702 |
| claude-haiku-4-5-20251001 | 919 | 719 | 73 | 0.561 | 0.926 | 0.699 |
| gemini-3-flash-preview:reasoning | 1000 | 1330 | 7 | 0.429 | 0.993 | 0.599 |
| gemini-3-flash-preview | 1002 | 1439 | 4 | 0.410 | 0.996 | 0.581 |

![Model Comparison](assets/model_comparison.png)

---

## 4. Assignment Complexity Gradient (RQ1)

> Does LLM performance vary with conceptual complexity?

| Assignment | Focus | TP | FP | FN | Precision | Recall | F1 |
|------------|-------|----|----|----|-----------|--------|-----|
| a1 | Variables/Math | 1821 | 2382 | 38 | 0.433 | 0.980 | 0.601 |
| a2 | Loops/Control | 2119 | 1997 | 44 | 0.515 | 0.980 | 0.675 |
| a3 | Arrays/Strings | 1951 | 1237 | 25 | 0.612 | 0.987 | 0.756 |

---

## 5. Error Analysis

### 5.1 False Positive Breakdown

| FP Type | Count | % of FPs | Description |
|---------|-------|----------|-------------|
| FP_CLEAN | 5,014 | 89.3% | Detected misconception in correct code |
| FP_WRONG | 602 | 10.7% | Detected wrong misconception (misclassification) |
| FP_HALLUCINATION | 0 | 0.0% | Invented non-existent misconception |

![Detection Classification Flow](assets/hallucinations_sankey.png)

> The flow diagram shows how detections move through the classification pipeline, from total scored detections through to final outcomes (TP, FP_CLEAN, FP_WRONG, FN).

### 5.2 Detection Filtering Pipeline

| Stage | Count | % of Raw |
|-------|-------|----------|
| Raw Detections | 29,164 | 100% |
| Null-Template Filtered | 634 | 2.2% |
| Noise Floor Filtered (<0.6) | 12,441 | 42.7% |
| **Evaluated** | **16,089** | **55.2%** |

---

## 6. Methodology Notes

### 6.1 Semantic Matching
- **Embedding Model:** OpenAI `text-embedding-3-large`
- **Match Threshold:** Cosine similarity ≥ 0.55 (calibrated via grid search)
- **Noise Floor:** Detections < 0.60 filtered as pedantic (calibrated via grid search)
- **Calibration:** Thresholds selected by optimizing F1 score across 30 (6×5) configurations

### 6.2 Statistical Tests
- **Bootstrap CI:** 1000 resamples with replacement
- **McNemar's Test:** Paired comparison with continuity correction
- **Cochran's Q:** Omnibus test for k-related samples
- **Cliff's Delta:** Non-parametric effect size

### 6.3 Ensemble Methods
- **Strategy Ensemble:** ≥2/4 prompting strategies must agree
- **Model Ensemble:** ≥2/6 models must agree

---

## Appendix: Complete Data Tables

### A.1 Complete Per-Misconception Results

| ID | Name | Category | TP | FN | Recall |
|----|------|----------|----|----|--------|
| NM_LOGIC_02 | Dangling Else (Indentation Trap) | The Independent Switch | 48 | 14 | 0.774 |
| NM_IO_01 | Prompt-Logic Mismatch | The Anthropomorphic I/O Machin | 54 | 6 | 0.900 |
| NM_SYN_02 | Precedence Blindness | The Algebraic Syntax Machine | 304 | 8 | 0.974 |
| NM_FLOW_03 | Infinite Loop (State Stagnation) | The Teleological Control Machi | 536 | 14 | 0.975 |
| NM_TYP_01 | Integer Division Blindness | The Fluid Type Machine | 346 | 9 | 0.975 |
| NM_STATE_01 | Spreadsheet View (Early Calculation) | The Reactive State Machine | 281 | 6 | 0.979 |
| NM_MEM_05 | Lossy Swap (Data Destruction) | The Semantic Bond Machine | 235 | 5 | 0.979 |
| NM_TYP_02 | Narrowing Cast in Division | The Fluid Type Machine | 167 | 3 | 0.982 |
| NM_FLOW_02 | The Intent Loop (Off-by-One) | The Teleological Control Machi | 663 | 9 | 0.987 |
| NM_MEM_03 | String Identity Trap (Immutability) | The Mutable String Machine | 593 | 7 | 0.988 |
| NM_MEM_04 | The 1-Based Offset (OOB) | The Human Index Machine | 601 | 7 | 0.988 |
| NM_MEM_01 | Parallel Array Desync | The Semantic Bond Machine | 522 | 6 | 0.989 |
| NM_FLOW_04 | Sabotaging the Future (Inner Loop Mod) | The Teleological Control Machi | 277 | 3 | 0.989 |
| NM_IO_02 | The Ghost Read | The Anthropomorphic I/O Machin | 383 | 4 | 0.990 |
| NM_LOGIC_01 | Mutually Exclusive Fallacy | The Independent Switch | 309 | 3 | 0.990 |
| NM_SYN_01 | XOR as Power | The Algebraic Syntax Machine | 119 | 1 | 0.992 |
| NM_API_01 | The Void Assumption | The Void Machine | 167 | 1 | 0.994 |
| NM_FLOW_01 | Accumulator Amnesia (Scope Error) | The Teleological Control Machi | 286 | 1 | 0.997 |
