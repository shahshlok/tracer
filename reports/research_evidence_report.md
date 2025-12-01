# Research Evidence Report: LLM Misconception Detection

Statistical analysis of prompt strategies for automated misconception detection.

---

## 1. Executive Statistical Summary

![Strategy Comparison](figures/strategy_comparison.png)

### Understanding the Metrics

Before diving into the results, here's what each metric means:

| Metric        | What It Measures       | Plain English                                                                                         |
| ------------- | ---------------------- | ----------------------------------------------------------------------------------------------------- |
| **Precision** | TP / (TP + FP)         | When the system flags an error, how often is it actually an error? High precision = few false alarms. |
| **Recall**    | TP / (TP + FN)         | Of all the actual errors, how many did the system catch? High recall = few missed errors.             |
| **F1 Score**  | Harmonic mean of P & R | A balanced measure combining precision and recall. Best single metric for overall performance.        |
| **Accuracy**  | (TP + TN) / Total      | Overall correctness rate. Can be misleading if classes are imbalanced.                                |

**Confusion Matrix Terms:**
- **TP (True Positive):** Correctly identified an error that exists
- **FP (False Positive):** Flagged an error when code was actually correct (false alarm)
- **FN (False Negative):** Missed an actual error (the dangerous one!)
- **TN (True Negative):** Correctly identified correct code as correct

---

### Strategy Performance Overview

| Strategy    | Precision | Recall | F1 Score | Accuracy |  TP   |  FP   |  FN   |  TN   |
| ----------- | :-------: | :----: | :------: | :------: | :---: | :---: | :---: | :---: |
| Minimal **  |   54.5%   | 92.3%  |  68.6%   |  72.5%   |  12   |  10   |   1   |  17   |
| Baseline    |   55.6%   | 76.9%  |  64.5%   |  72.5%   |  10   |   8   |   3   |  19   |
| Socratic    |   55.0%   | 84.6%  |  66.7%   |  72.5%   |  11   |   9   |   2   |  18   |
| Rubric_only |   55.6%   | 76.9%  |  64.5%   |  72.5%   |  10   |   8   |   3   |  19   |

*Strategy with highest F1 is marked with **

### Omnibus Test: Cochran's Q

<details>
<summary><strong>What is Cochran's Q Test and why do we use it?</strong></summary>

Cochran's Q test is a statistical test used when you want to compare **more than two related groups** 
on a binary outcome (success/failure). Think of it as an extension of McNemar's test for multiple groups.

**Why we use it here:** We have 4 different prompt strategies, and we want to know if there's ANY 
significant difference between them before doing pairwise comparisons. This prevents us from 
'p-hacking' by running many pairwise tests without first checking if differences exist at all.

**How to interpret:**
- **p < 0.05:** At least one strategy performs significantly differently from the others. We should proceed with pairwise tests.
- **p >= 0.05:** No significant difference between strategies. Any observed differences are likely due to random chance.

</details>

**Results:**

- **Q Statistic:** 0.0
- **p-value:** 1.0
- **Degrees of Freedom:** 3 (number of strategies - 1)
- **N samples:** 40

> **Result:** No significant difference between strategies (p >= 0.05). 
> The observed performance differences are likely due to random variation, not true differences in strategy effectiveness.

---

## 2. Strategy Comparison (Pairwise Analysis)

<details>
<summary><strong>What is McNemar's Test and why do we use it?</strong></summary>

McNemar's test is used to compare **two related groups** on a binary outcome. It's specifically designed 
for **paired data** where the same subjects are measured under two different conditions.

**Why we use it here:** Each student submission is evaluated by both Strategy A and Strategy B. 
We want to know if one strategy is significantly better than the other at correctly identifying misconceptions.

**The key insight:** McNemar's test focuses on the **discordant pairs** - cases where the two strategies disagree:
- **b:** Strategy 1 was correct, Strategy 2 was wrong
- **c:** Strategy 1 was wrong, Strategy 2 was correct

If b and c are roughly equal, the strategies perform similarly. If one is much larger, that strategy is worse.

**Bonferroni Correction:** When running multiple comparisons (6 pairs for 4 strategies), we increase the risk 
of false positives. Bonferroni correction divides the significance threshold (0.05) by the number of tests 
to maintain overall reliability. Here: 0.05 / 6 = 0.0083.

**How to interpret:**
- **p < Bonferroni alpha:** The difference is statistically significant even after correction
- **p >= Bonferroni alpha:** No significant difference; observed variation is likely random
- **Winner:** The strategy that was correct more often when the two disagreed

</details>

| Comparison              | Chi-squared | p-value  | Bonferroni alpha | Significant | Winner |
| ----------------------- | :---------: | :------: | :--------------: | :---------: | :----: |
| Minimal vs Baseline     |    0.25     | 0.617075 |     0.008333     |     No      |  Tie   |
| Minimal vs Socratic     |   0.1667    | 0.683091 |     0.008333     |     No      |  Tie   |
| Minimal vs Rubric_only  |    0.25     | 0.617075 |     0.008333     |     No      |  Tie   |
| Baseline vs Socratic    |     0.5     |  0.4795  |     0.008333     |     No      |  Tie   |
| Baseline vs Rubric_only |      0      |   1.0    |     0.008333     |     No      |  N/a   |
| Socratic vs Rubric_only |     0.5     |  0.4795  |     0.008333     |     No      |  Tie   |

*Significant after Bonferroni correction

### Per-Model Performance by Strategy

#### gemini-2.5-flash-lite

| Strategy    | Precision | Recall |  F1   |
| ----------- | :-------: | :----: | :---: |
| Minimal     |   52.4%   | 84.6%  | 64.7% |
| Baseline    |   52.9%   | 69.2%  | 60.0% |
| Socratic    |   55.6%   | 83.3%  | 66.7% |
| Rubric_only |   55.6%   | 76.9%  | 64.5% |

#### gpt-5-nano

| Strategy    | Precision | Recall |  F1   |
| ----------- | :-------: | :----: | :---: |
| Minimal     |   61.1%   | 84.6%  | 71.0% |
| Baseline    |   75.0%   | 69.2%  | 72.0% |
| Socratic    |   50.0%   | 66.7%  | 57.1% |
| Rubric_only |  100.0%   | 15.4%  | 26.7% |

---

## 3. Inter-Rater Reliability

This section answers: **Do the LLM models agree with each other?** If models frequently disagree, 
we can't trust their judgments. High agreement suggests the detection task is well-defined and models are reliable.

<details>
<summary><strong>What are Kappa and Alpha, and why do they matter?</strong></summary>

### Cohen's Kappa
Cohen's Kappa measures agreement between **exactly two raters** while accounting for agreement that would 
happen by random chance. A kappa of 0 means agreement is no better than random; 1 means perfect agreement.

**Interpretation scale:**
| Kappa       | Interpretation           |
| ----------- | ------------------------ |
| < 0         | Poor (worse than chance) |
| 0.00 - 0.20 | Slight                   |
| 0.21 - 0.40 | Fair                     |
| 0.41 - 0.60 | Moderate                 |
| 0.61 - 0.80 | Substantial              |
| 0.81 - 1.00 | Almost Perfect           |

### Krippendorff's Alpha
Krippendorff's Alpha is more flexible than Kappa - it works with **any number of raters** and handles 
missing data. It's the standard metric for content analysis reliability studies.

**Interpretation thresholds:**
- **alpha >= 0.80:** Good reliability - conclusions are trustworthy
- **0.667 <= alpha < 0.80:** Acceptable for tentative conclusions
- **alpha < 0.667:** Unacceptable - too much disagreement to draw conclusions

**Why this matters for the thesis:** If models strongly agree (high Kappa/Alpha), it suggests that 
misconception detection is a well-defined task that LLMs can perform reliably. If they disagree, 
either the task is inherently ambiguous or the models have different 'opinions' about what constitutes an error.

</details>

### Krippendorff's Alpha (Multi-Rater Agreement)

![Model Agreement](figures/model_agreement.png)

| Strategy    |  Alpha  | Interpretation                       |
| ----------- | :-----: | ------------------------------------ |
| Minimal     | 0.7498  | Acceptable for tentative conclusions |
| Baseline    | 0.6214  | Unacceptable for conclusions         |
| Socratic    | 0.6026  | Unacceptable for conclusions         |
| Rubric_only | -0.0667 | Unacceptable for conclusions         |

### Cohen's Kappa (Pairwise Model Agreement)

#### Minimal

- **gemini-2.5-flash-lite** vs **gpt-5-nano**: kappa=0.7512 (Substantial)

#### Baseline

- **gemini-2.5-flash-lite** vs **gpt-5-nano**: kappa=0.6277 (Substantial)

#### Socratic

- **gemini-2.5-flash-lite** vs **gpt-5-nano**: kappa=0.6173 (Substantial)

#### Rubric_only

- **gemini-2.5-flash-lite** vs **gpt-5-nano**: kappa=0.1209 (Slight)

---

## 4. Misconception Detection Analysis

This section answers: **Which types of errors are easy vs. hard to detect?**

Understanding this helps identify:
- Which misconceptions need better prompting strategies
- Which error types might require specialized detection approaches
- Patterns in what LLMs miss (e.g., subtle vs. obvious errors)

**Detection Rate:** The percentage of times a misconception was correctly identified when it was present 
in the ground truth. A rate of 100% means the LLMs caught every instance; 0% means they missed all of them.

### Detection Rates by Misconception Type

![Misconception Heatmap](figures/misconception_heatmap.png)

| ID       | Name                                | Minimal | Baseline | Socratic | Rubric_only |  Avg  |
| :------- | :---------------------------------- | :-----: | :------: | :------: | :---------: | :---: |
| CONST001 | Using ^ instead of Math.pow() for e |   50%   |   50%    |   50%    |     50%     |  50%  |
| CONST002 | Missing Math.sqrt() for square root |   50%   |   100%   |   50%    |     50%     |  62%  |
| DT002    | Integer division truncation         |  100%   |   50%    |   100%   |    100%     |  88%  |
| DT003    | Type mismatch in Scanner input      |   25%   |   25%    |   50%    |     25%     |  31%  |
| INPUT001 | Missing Scanner import              |   50%   |    0%    |    0%    |     0%      |  12%  |
| INPUT002 | Scanner not reading correct number  |  100%   |   50%    |   50%    |     50%     |  62%  |
| INPUT003 | Not closing Scanner resource        |   50%   |   50%    |   50%    |     50%     |  50%  |
| OTHER001 | Computing wrong quantity (different |   50%   |   50%    |   50%    |     50%     |  50%  |
| OTHER002 | Hardcoded values instead of user in |  100%   |   100%   |   100%   |    100%     | 100%  |
| VAR001   | Incorrect operator precedence       |  100%   |   100%   |   100%   |    100%     | 100%  |

### Hardest-to-Detect Misconceptions

1. **INPUT001** (Missing Scanner import): 12% average detection rate
1. **DT003** (Type mismatch in Scanner input): 31% average detection rate
1. **CONST001** (Using ^ instead of Math.pow() for exponentiation): 50% average detection rate
1. **INPUT003** (Not closing Scanner resource): 50% average detection rate
1. **OTHER001** (Computing wrong quantity (different problem)): 50% average detection rate

### Easiest-to-Detect Misconceptions

1. **VAR001** (Incorrect operator precedence): 100% average detection rate
1. **OTHER002** (Hardcoded values instead of user input): 100% average detection rate
1. **DT002** (Integer division truncation): 88% average detection rate
1. **INPUT002** (Scanner not reading correct number of inputs): 62% average detection rate
1. **CONST002** (Missing Math.sqrt() for square root): 62% average detection rate

### LLM Detection vs Ground Truth: Per-Model Breakdown

This table breaks down detection performance by individual models across all strategies.
It helps identify if specific models are better at detecting certain misconceptions.

#### Model: gemini-2.5-flash-lite

| ID       | Misconception                  | Ground Truth | Detected | Delta | Assessment   |
| :------- | :----------------------------- | :----------: | :------: | :---: | :----------- |
| OTHER002 | Hardcoded values instead of us |      13      |   1.0    | -12.0 | Under (-92%) |
| INPUT003 | Not closing Scanner resource   |      9       |   0.2    | -8.8  | Under (-97%) |
| DT003    | Type mismatch in Scanner input |      8       |   0.0    | -8.0  | Missed       |
| INPUT001 | Missing Scanner import         |      8       |   0.0    | -8.0  | Missed       |
| VAR001   | Incorrect operator precedence  |      6       |   0.8    | -5.2  | Under (-88%) |
| DT001    | Using int instead of double fo |      5       |   0.0    | -5.0  | Missed       |
| INPUT002 | Scanner not reading correct nu |      5       |   0.0    | -5.0  | Missed       |
| OTHER001 | Computing wrong quantity (diff |      5       |   0.0    | -5.0  | Missed       |
| CONST002 | Missing Math.sqrt() for square |      4       |   0.2    | -3.8  | Under (-94%) |
| CONST001 | Using ^ instead of Math.pow()  |      3       |   0.0    | -3.0  | Missed       |
| VAR003   | Incorrect formula derivation f |      3       |   0.0    | -3.0  | Missed       |
| CONST003 | Incorrect Math.pow() argument  |      2       |   0.0    | -2.0  | Missed       |
| DT002    | Integer division truncation    |      2       |   1.2    | -0.8  | Under (-38%) |
| VAR002   | Wrong formula - addition inste |      2       |   0.0    | -2.0  | Missed       |
| VAR004   | Missing intermediate variable  |      1       |   0.0    | -1.0  | Missed       |

**Summary for gemini-2.5-flash-lite:**
- Average Total Detections: 3.5 (vs 76 real errors)

#### Model: gpt-5-nano

| ID       | Misconception                  | Ground Truth | Detected | Delta | Assessment   |
| :------- | :----------------------------- | :----------: | :------: | :---: | :----------- |
| OTHER002 | Hardcoded values instead of us |      13      |   0.0    | -13.0 | Missed       |
| INPUT003 | Not closing Scanner resource   |      9       |   0.0    | -9.0  | Missed       |
| DT003    | Type mismatch in Scanner input |      8       |   0.0    | -8.0  | Missed       |
| INPUT001 | Missing Scanner import         |      8       |   0.0    | -8.0  | Missed       |
| VAR001   | Incorrect operator precedence  |      6       |   0.8    | -5.2  | Under (-88%) |
| DT001    | Using int instead of double fo |      5       |   0.8    | -4.2  | Under (-85%) |
| INPUT002 | Scanner not reading correct nu |      5       |   0.8    | -4.2  | Under (-85%) |
| OTHER001 | Computing wrong quantity (diff |      5       |   0.0    | -5.0  | Missed       |
| CONST002 | Missing Math.sqrt() for square |      4       |   0.5    | -3.5  | Under (-88%) |
| CONST001 | Using ^ instead of Math.pow()  |      3       |   0.0    | -3.0  | Missed       |
| VAR003   | Incorrect formula derivation f |      3       |   0.0    | -3.0  | Missed       |
| CONST003 | Incorrect Math.pow() argument  |      2       |   0.0    | -2.0  | Missed       |
| DT002    | Integer division truncation    |      2       |   0.2    | -1.8  | Under (-88%) |
| VAR002   | Wrong formula - addition inste |      2       |   0.0    | -2.0  | Missed       |
| VAR004   | Missing intermediate variable  |      1       |   0.2    | -0.8  | Under (-75%) |

**Summary for gpt-5-nano:**
- Average Total Detections: 3.2 (vs 76 real errors)

---

## 5. Appendix: Raw Data

### Ground Truth Summary

| Question | Correct | With Errors | Error Rate |
| -------- | :-----: | :---------: | :--------: |
| q1       |   39    |     21      |    35%     |
| q2       |   45    |     15      |    25%     |
| q3       |   37    |     23      |    38%     |
| q4       |   43    |     17      |    28%     |

### Misconception Distribution in Ground Truth

| ID       | Name                                     | Count |
| :------- | :--------------------------------------- | :---: |
| OTHER002 | Hardcoded values instead of user input   |  13   |
| INPUT003 | Not closing Scanner resource             |   9   |
| INPUT001 | Missing Scanner import                   |   8   |
| DT003    | Type mismatch in Scanner input           |   8   |
| VAR001   | Incorrect operator precedence            |   6   |
| OTHER001 | Computing wrong quantity (different prob |   5   |
| INPUT002 | Scanner not reading correct number of in |   5   |
| DT001    | Using int instead of double for decimal  |   5   |
| CONST002 | Missing Math.sqrt() for square root      |   4   |
| VAR003   | Incorrect formula derivation for fuel co |   3   |
| CONST001 | Using ^ instead of Math.pow() for expone |   3   |
| DT002    | Integer division truncation              |   2   |
| CONST003 | Incorrect Math.pow() argument order      |   2   |
| VAR002   | Wrong formula - addition instead of subt |   2   |
| VAR004   | Missing intermediate variable for semi-p |   1   |

---

*Report generated by utils/analytics.py*