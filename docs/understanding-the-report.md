# Understanding the Report

A visual guide to reading and interpreting the analysis report.

---

## Report Structure Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    REPORT SECTIONS                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Dataset & Run Configuration  ← What was tested          │
│  2. Executive Highlights         ← Key takeaways            │
│  3. Diagnostic Ceiling (RQ1)     ← Upper bound of detection │
│  4. Cognitive Alignment (RQ2)    ← Depth analysis           │
│  5. Matcher Ablation             ← Fuzzy vs Semantic        │
│  6. Topic Difficulty             ← Which bugs are hard      │
│  7. Model Agreement              ← Do AIs agree?            │
│  8. Confidence Calibration       ← Is confidence reliable?  │
│  9. Per-Misconception Rates      ← Individual bug detection │
│ 10. Hallucination Analysis       ← False positives          │
│ 11. Agreement & Significance     ← Statistical tests        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Section 1: Dataset Summary

```markdown
### Dataset Summary
- **Assignment:** A1 – Kinematics & Geometry (CS1)
- **Students:** 99
- **Questions:** 4 (Q1, Q2, Q3, Q4)
- **Total files:** 396
- **Seeded files:** 95 (24.0%)
- **Clean files:** 301 (76.0%)
- **Detection opportunities:** 2280
```

### What Each Line Means

```
┌──────────────────────────────────────────────────────────────┐
│  BREAKDOWN                                                    │
│                                                              │
│  99 students × 4 questions = 396 total files                 │
│                                                              │
│  Of these 396 files:                                         │
│    • 95 have intentional bugs (seeded)                       │
│    • 301 are correct (clean)                                 │
│                                                              │
│  Detection opportunities = 95 bugs × 24 AI configurations    │
│                          = 2280 chances to find bugs         │
│                                                              │
│  24 configurations = 3 models × 4 strategies × 2 (±reasoning)│
└──────────────────────────────────────────────────────────────┘
```

---

## Section 2: The Diagnostic Ceiling

```
┌──────────────────────────────────────────────────────────────────┐
│           UNDERSTANDING THE CEILING                               │
│                                                                   │
│  Potential Recall = 100%                                         │
│                                                                   │
│  ┌────────────────────────────┐                                  │
│  │ This means:                │                                  │
│  │                            │                                  │
│  │ ALL 95 bugs were found     │                                  │
│  │ by AT LEAST ONE config.    │                                  │
│  │                            │                                  │
│  │ The bugs ARE detectable.   │                                  │
│  └────────────────────────────┘                                  │
│                                                                   │
│  Average Recall = 86%                                            │
│                                                                   │
│  ┌────────────────────────────┐                                  │
│  │ On average, each config    │                                  │
│  │ finds 86% of bugs.         │                                  │
│  │                            │                                  │
│  │ Some configs do better,    │                                  │
│  │ some do worse.             │                                  │
│  └────────────────────────────┘                                  │
│                                                                   │
│  Consistency = 86% (Average ÷ Potential)                         │
│  → High consistency means configs agree                          │
│  → Low consistency means they find DIFFERENT bugs                │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## Section 3: Reading the Results Table

```
| Matcher | Strategy | Model   | TP  | FP  | FN  | Precision | Recall | F1    | CI        |
| ------- | -------- | ------- | --- | --- | --- | --------- | ------ | ----- | --------- |
| hybrid  | baseline | GPT-5.1 | 88  | 37  | 13  | 0.704     | 0.871  | 0.779 | 0.70–0.85 |
```

### Column Definitions

```
┌─────────────┬────────────────────────────────────────────────────┐
│   Column    │   Meaning                                          │
├─────────────┼────────────────────────────────────────────────────┤
│ Matcher     │ How we matched AI output to ground truth           │
│ Strategy    │ Which prompt was used (baseline/taxonomy/cot/etc)  │
│ Model       │ Which AI (GPT-5.1, Gemini, Haiku)                  │
│ TP          │ True Positives: Correctly identified bugs          │
│ FP          │ False Positives: Hallucinations (claimed wrong bug)│
│ FN          │ False Negatives: Missed bugs                       │
│ Precision   │ TP ÷ (TP + FP) = How trustworthy are detections?   │
│ Recall      │ TP ÷ (TP + FN) = What % of bugs found?            │
│ F1          │ Harmonic mean of Precision and Recall              │
│ CI          │ 95% Confidence Interval for F1                     │
└─────────────┴────────────────────────────────────────────────────┘
```

### Visual Example

```
┌──────────────────────────────────────────────────────────────┐
│  INTERPRETING: TP=88, FP=37, FN=13                           │
│                                                              │
│  There were 95 seeded bugs (88 + 13 = 101? No, see below)   │
│                                                              │
│  Actually: 88 + 13 = 101 > 95 because some bugs have         │
│            multiple detection attempts per file              │
│                                                              │
│  ┌──────────────────────┐                                    │
│  │ 88 True Positives    │ ███████████████████████░░░ 87%    │
│  │ (Correctly found)    │                                    │
│  └──────────────────────┘                                    │
│                                                              │
│  ┌──────────────────────┐                                    │
│  │ 13 False Negatives   │ ░░░░░░░░░░░░░░░░░░░░░░░███ 13%    │
│  │ (Missed)             │                                    │
│  └──────────────────────┘                                    │
│                                                              │
│  37 False Positives = Hallucinations (flagged non-bugs)      │
│                                                              │
│  Precision = 88/(88+37) = 70.4%                              │
│  Recall = 88/(88+13) = 87.1%                                 │
│  F1 = 77.9%                                                  │
└──────────────────────────────────────────────────────────────┘
```

---

## Section 4: Reading the Charts

### Matcher Ablation Bar Chart

```
┌──────────────────────────────────────────────────────────────┐
│         WHAT THE MATCHER ABLATION SHOWS                       │
│                                                              │
│  Each bar group shows Precision, Recall, F1 for each matcher │
│                                                              │
│  fuzzy_only        │▓▓░░░░░░░░│ P=0.05  Very low!           │
│                    │▓▓░░░░░░░░│ R=0.13                       │
│                    │▓░░░░░░░░░│ F1=0.07                      │
│                                                              │
│  semantic_only     │▓▓▓▓▓▓░░░░│ P=0.59                       │
│                    │▓▓▓▓▓▓▓▓▓░│ R=0.87                       │
│                    │▓▓▓▓▓▓▓░░░│ F1=0.70                      │
│                                                              │
│  hybrid            │▓▓▓▓▓▓▓░░░│ P=0.65  Best precision      │
│                    │▓▓▓▓▓▓▓▓▓░│ R=0.87                       │
│                    │▓▓▓▓▓▓▓▓░░│ F1=0.74  Best overall       │
│                                                              │
│  CONCLUSION: Hybrid matcher is the best choice               │
└──────────────────────────────────────────────────────────────┘
```

### Topic Recall Heatmap

```
┌──────────────────────────────────────────────────────────────┐
│         READING THE TOPIC HEATMAP                             │
│                                                              │
│  Y-axis: Misconception categories                            │
│  X-axis: Strategy | Model combinations                       │
│  Color:  Dark blue (high %) → Light yellow (low %)           │
│                                                              │
│  Example interpretation:                                      │
│                                                              │
│  Row "Fluid Type Machine" has light colors                   │
│  → This category is HARD to detect                           │
│                                                              │
│  Row "Algebraic Syntax Machine" has dark colors              │
│  → This category is EASY to detect                           │
│                                                              │
│  Column "socratic | Gemini" has dark colors                  │
│  → This config performs well across categories               │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Model Agreement Matrix

```
┌──────────────────────────────────────────────────────────────┐
│         READING THE AGREEMENT HEATMAP                         │
│                                                              │
│  Each cell shows Cohen's κ between two models                │
│                                                              │
│          │ GPT-5.1 │ Gemini │ Haiku │                        │
│  ────────┼─────────┼────────┼───────┤                        │
│  GPT-5.1 │   1.0   │  0.71  │ 0.60  │                        │
│  Gemini  │  0.71   │  1.0   │ 0.55  │                        │
│  Haiku   │  0.60   │  0.55  │  1.0  │                        │
│                                                              │
│  κ = 1.0: Perfect agreement (diagonal, same model)           │
│  κ > 0.6: Substantial agreement (they find same bugs)        │
│  κ < 0.4: Low agreement (they find DIFFERENT bugs)           │
│                                                              │
│  FOR ENSEMBLES:                                               │
│  Lower κ = Models are complementary = Combining helps        │
│  Higher κ = Models are redundant = Combining less useful     │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## Section 5: Understanding Confidence Intervals

```
┌──────────────────────────────────────────────────────────────┐
│         WHAT CI [0.70-0.85] MEANS                            │
│                                                              │
│  "We are 95% confident the TRUE F1 is between 0.70 and 0.85"│
│                                                              │
│           0.70              0.779              0.85          │
│            │                  │                  │           │
│            │    ◄────────────●────────────►    │           │
│            │         Confidence Interval         │           │
│            │                  │                  │           │
│                          Our estimate                        │
│                                                              │
│  NARROW CI (e.g., [0.75-0.80]):                             │
│  → More data, more certainty                                 │
│  → Can trust the estimate                                    │
│                                                              │
│  WIDE CI (e.g., [0.50-0.90]):                               │
│  → Less data or high variance                                │
│  → Estimate is uncertain                                     │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## Section 6: Statistical Significance Tables

### Cohen's Kappa Table

```
| Strategy | Model A | Model B | κ    | Interpretation |
| -------- | ------- | ------- | ---- | -------------- |
| baseline | GPT-5.1 | Gemini  | 0.71 | Substantial    |
```

```
┌──────────────────────────────────────────────────────────────┐
│  INTERPRETATION SCALE                                         │
│                                                              │
│  κ < 0.20   Slight     → Almost random agreement            │
│  κ 0.20-0.40 Fair       → Some agreement beyond chance       │
│  κ 0.40-0.60 Moderate   → Reasonable agreement               │
│  κ 0.60-0.80 Substantial → Strong agreement                  │
│  κ > 0.80   Almost Perfect → Near identical predictions     │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### McNemar's Test Table

```
| Strategy | Model A | Model B | χ²   | p-value | Significant? |
| -------- | ------- | ------- | ---- | ------- | ------------ |
| baseline | GPT-5.1 | Gemini  | 7.35 | 0.0067  | Yes          |
```

```
┌──────────────────────────────────────────────────────────────┐
│  HOW TO READ McNEMAR'S TEST                                   │
│                                                              │
│  p-value < 0.05:                                             │
│    → Difference is STATISTICALLY SIGNIFICANT                 │
│    → One model is truly better than the other                │
│    → Not just random luck                                    │
│                                                              │
│  p-value ≥ 0.05:                                             │
│    → Difference is NOT significant                            │
│    → Could be random chance                                   │
│    → Models perform similarly                                 │
│                                                              │
│  The χ² statistic:                                            │
│    → Higher = bigger difference                               │
│    → Lower = smaller difference                               │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## Quick Checklist: What to Look For

When reviewing a report, check these key items:

```
✓ DIAGNOSTIC CEILING
  □ Is Potential Recall close to 100%?
  □ Is Average Recall above 80%?

✓ MATCHER COMPARISON
  □ Is Hybrid the best performer?
  □ Is the F1 gap between Fuzzy and Hybrid large? (should be!)

✓ TOPIC DIFFICULTY
  □ Which categories have low recall?
  □ Are there any concerning gaps (< 50%)?

✓ BEST CONFIGURATION
  □ Which model + strategy has highest F1?
  □ How tight is the confidence interval?

✓ MODEL AGREEMENT
  □ Is κ moderate (0.4-0.6)? → Good for ensembles
  □ Any statistical significant differences?

✓ HALLUCINATIONS
  □ What are the top false positive types?
  □ Any patterns to investigate?
```

---

## Common Questions

### "What's a good F1 score?"

```
Below 0.50  → Poor (random guessing is ~0.50)
0.50 - 0.65 → Fair (needs improvement)
0.65 - 0.80 → Good (publishable)
Above 0.80  → Excellent (state-of-the-art)
```

### "Why is Recall higher than Precision?"

```
High Recall + Low Precision means:
  → AI finds most bugs (good!)
  → But also reports many false alarms (bad!)
  → AI is "trigger happy"
  
Low Recall + High Precision means:
  → AI is conservative
  → When it flags something, it's usually right
  → But it misses many bugs
```

### "Which config should I use in production?"

```
1. Check the Full Results Table
2. Sort by F1 score
3. Pick the top 3 configs
4. Compare their Precision (for trustworthiness)
5. Consider using ALL three in ensemble
```
