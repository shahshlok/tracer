# Report Guide

This document explains how to read the generated `thesis_report.md`.

## Report Structure

The report is organized into these sections:

1. **Dataset & Run Configuration** - What data was analyzed
2. **Executive Highlights** - Key findings at a glance
3. **Matcher Ablation** (if `--match-mode all`) - Comparison of matchers
4. **Topic Difficulty** - Which topics are hardest to detect
5. **Per-Misconception Detection Rates** - Individual misconception analysis
6. **Hallucination Analysis** - False positives
7. **Methods** - How the analysis was done
8. **Agreement & Significance** - Model agreement metrics

## Section Details

### Dataset & Run Configuration

Shows what data was analyzed:

```markdown
## Dataset & Run Configuration

### Dataset Summary
- **Assignment:** A2 – Kinematics & Geometry (CS1)
- **Students:** 60
- **Questions:** 4 (Q1, Q2, Q3, Q4)
- **Total files:** 240
- **Seeded files:** 54 (22.5%)
- **Clean files:** 186 (77.5%)
- **Detection opportunities:** 432

### Run Configuration
- **Generation seed:** 1764863751
- **Match mode:** all (ablation)
- **Embedding model:** text-embedding-3-large (OpenAI)
```

**Key points:**
- 22.5% seeded = 22.5% of files have an injected misconception
- 432 opportunities = 54 seeded files × 8 detection runs (4 strategies × 2 models)

### Executive Highlights

Quick summary of key findings:

```markdown
## Executive Highlights
- **Matcher Ablation Study**: Comparing fuzzy_only, semantic_only, and hybrid matchers.
- Bootstrap CIs included for statistical rigor.
- Same detection data, different matching strategies.
```

### Matcher Ablation

Comparison of matching strategies (only in `--match-mode all`):

```markdown
## Matcher Ablation: Fuzzy vs Semantic vs Hybrid

### Summary (averaged across strategies and models)
| Matcher | Total TP | Total FP | Total FN | Avg Precision | Avg Recall | Avg F1 |
|---------|----------|----------|----------|---------------|------------|--------|
| fuzzy_only | 34 | 798 | 400 | 0.042 | 0.078 | 0.053 |
| semantic_only | 305 | 271 | 144 | 0.542 | 0.679 | 0.598 |
| hybrid | 298 | 268 | 151 | 0.538 | 0.664 | 0.591 |
```

**How to read:**
- **Total TP/FP/FN**: Summed across all strategies and models
- **Avg F1**: Average F1 score (the main metric)
- Fuzzy F1 ~0.05 = fails; Semantic/Hybrid ~0.59 = reasonable

**Plots:**
- `matcher_ablation.png` - Bar chart comparing matchers
- `matcher_strategy_grid.png` - Matcher × Strategy breakdown
- `matcher_pr_scatter.png` - Precision-Recall scatter

### Topic Difficulty

Which topics are hardest to detect:

```markdown
## Topic Difficulty (Recall)
| Topic | Recall | N |
|-------|--------|---|
| Input | 0.287 | 80 |
| State / Variables | 0.385 | 104 |
| Algebraic Reasoning | 0.792 | 24 |
| Data Types | 0.852 | 88 |
```

**How to read:**
- Lower recall = harder to detect
- Input misconceptions (28.7% recall) are hardest
- Data Types misconceptions (85.2% recall) are easiest

**Plot:** `topic_recall_bars.png`

### Per-Misconception Detection Rates

Individual misconception analysis:

```markdown
## Per-Misconception Detection Rates
| ID | Misconception | Category | Recall | N |
|----|---------------|----------|--------|---|
| INP-01 | Prompt-Logic Mismatch | Input | 0.25 | 16 |
| STA-06 | Default Zero Assumption | State | 0.35 | 20 |
| TYP-01 | Integer Division Trap | Data Types | 0.88 | 24 |
```

**How to read:**
- Sorted by recall (hardest at top)
- N = number of opportunities (how many times this misconception was seeded)
- Low recall = LLMs struggle to detect this misconception

**Plot:** `misconception_recall.png`

### Hallucination Analysis

Most common false positives:

```markdown
## Hallucination Analysis
- **Incorrect order of input reading** (18 times)
- **Incorrect data type for input variables** (17 times)
- **Unnecessary absolute value check** (12 times)
```

**How to read:**
- These are misconceptions the LLM detected but weren't actually seeded
- Some may be "bonus" detections (real issues in generated code)
- Others are true hallucinations

**Plot:** `hallucinations.png`

### Agreement & Significance

Model agreement metrics:

```markdown
## Agreement & Significance
- baseline: κ=0.279, McNemar p=0.1949 (stat=1.681)
- rubric_only: κ=0.279, McNemar p=0.1949 (stat=1.681)
```

**How to read:**
- **κ (kappa)**: Agreement between GPT and Gemini (0 = random, 1 = perfect)
- **McNemar p**: Statistical test for systematic differences
- κ ~0.3 = fair agreement; not highly correlated

## Key Metrics Explained

### Precision, Recall, F1

| Metric | Formula | Meaning |
|--------|---------|---------|
| Precision | TP / (TP + FP) | "Of detections made, how many were correct?" |
| Recall | TP / (TP + FN) | "Of misconceptions present, how many were found?" |
| F1 | 2 × (P × R) / (P + R) | Harmonic mean of precision and recall |

### Bootstrap Confidence Intervals

The `CI (F1)` column shows 95% confidence intervals from bootstrap resampling:

```
| Strategy | F1 | CI (F1) |
|----------|-------|---------|
| baseline | 0.618 | 0.50–0.73 |
```

**How to read:**
- We're 95% confident true F1 is between 0.50 and 0.73
- Wider intervals = more uncertainty
- Non-overlapping intervals = significant difference

## Generated Plots

| Plot | What it shows |
|------|---------------|
| `model_comparison.png` | P/R/F1 for GPT vs Gemini |
| `strategy_f1_comparison.png` | F1 by strategy, colored by model |
| `precision_recall_scatter.png` | P/R tradeoff with F1 iso-curves |
| `topic_recall_bars.png` | Recall by topic (sorted) |
| `topic_heatmap.png` | Topic × Strategy heatmap |
| `hallucinations.png` | Most common false positives |
| `matcher_ablation.png` | F1 by matcher |
| `matcher_strategy_grid.png` | Matcher × Strategy grid |
| `matcher_pr_scatter.png` | P/R scatter by matcher |
| `misconception_recall.png` | Per-misconception recall bars |

## Quick Interpretation Guide

1. **Is semantic matching essential?**
   - Check ablation table: if fuzzy F1 << semantic F1, yes

2. **Which strategy is best?**
   - Check Strategy × Model Performance table, look at F1 column

3. **What misconceptions are hard to detect?**
   - Check Per-Misconception table, look at low recall items

4. **Are the models agreeing?**
   - Check Agreement section, κ > 0.6 = substantial agreement

5. **How much can we trust the numbers?**
   - Check CI columns, narrower = more reliable
