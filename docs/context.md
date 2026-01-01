# Paper Writing Context Brief

> **Use this document alongside `report.md` to write or refine the paper.**

---

## Target Venue

**ITiCSE 2025** (Innovation and Technology in Computer Science Education)
- ACM SIGCSE conference
- 6–10 pages, two-column ACM format (`sigconf` document class)
- Audience: CS educators, educational technology researchers, learning scientists
- Review criteria: Novelty, rigor, practical relevance to teaching

---

## Paper Title

**"The Diagnostic Ceiling: Measuring LLM Alignment with Notional Machines in Introductory Programming"**

---

## The Core Argument (Memorize This)

> LLMs can detect *what* is wrong in student code, but struggle to diagnose *why* students made the error. We measure this gap using Notional Machine theory and find a **Diagnostic Ceiling**: 99% recall on structural misconceptions vs. 16% on semantic misconceptions. Ensemble voting provides a practical solution, improving F1 by 63%.

---

## Key Statistics (Exact Numbers from Report)

### Overall Performance
| Metric | Value | 95% CI |
|--------|-------|--------|
| True Positives | 6,745 | — |
| False Positives | 14,236 | — |
| False Negatives | 1,022 | — |
| Precision | 0.322 | [0.315, 0.328] |
| Recall | 0.868 | [0.861, 0.876] |
| F1 Score | 0.469 | [0.462, 0.476] |

### The Diagnostic Ceiling (Per-Misconception Recall)
| Misconception | Recall | N | Verdict |
|---------------|--------|---|---------|
| Dangling Else | **0.16** | 289 | Human required |
| Narrowing Cast | **0.31** | 458 | Human required |
| Spreadsheet View | 0.65 | 312 | Borderline |
| Integer Division | 0.90 | 425 | Acceptable |
| String Immutability | 0.99 | 716 | Safe to automate |
| Void Assumption | 0.99 | 175 | Safe to automate |

### Category-Level Performance
| Category | Recall | Type |
|----------|--------|------|
| Void Machine | 0.994 | Structural |
| Mutable String | 0.990 | Structural |
| Human Index | 0.973 | Structural |
| Algebraic Syntax | 0.972 | Structural |
| Semantic Bond | 0.954 | Structural |
| Teleological Control | 0.931 | Structural |
| Anthropomorphic I/O | 0.881 | Structural |
| Reactive State | 0.654 | **Semantic** |
| Independent Switch | 0.625 | **Semantic** |
| Fluid Type | 0.590 | **Semantic** |

### Ensemble Voting Results
| Method | Precision | Recall | F1 | Precision Gain |
|--------|-----------|--------|-----|----------------|
| Raw | 0.321 | 0.868 | 0.469 | — |
| Strategy (≥2/4) | 0.640 | 0.868 | 0.737 | +99% |
| Model (≥2/6) | **0.684** | 0.862 | **0.763** | **+113%** |

### Prompting Strategy Comparison
| Strategy | F1 | Notes |
|----------|-----|-------|
| Baseline | **0.519** | Simple wins |
| Taxonomy | 0.518 | Near-best |
| CoT | 0.489 | Worse than baseline |
| Socratic | 0.391 | Worst |

**Statistical significance:**
- Baseline vs CoT: χ²=23.58, p<0.0001 (Baseline wins)
- Cochran's Q: 57.59, p<0.000001 (strategies differ significantly)

### Model Performance
| Model | Precision | Recall | F1 |
|-------|-----------|--------|-----|
| Claude Haiku 4.5:reasoning | **0.469** | 0.847 | **0.604** |
| GPT-5.2 | 0.356 | 0.885 | 0.507 |
| Claude Haiku 4.5 | 0.358 | 0.825 | 0.499 |
| GPT-5.2:reasoning | 0.346 | **0.897** | 0.499 |
| Gemini 3 Flash:reasoning | 0.252 | 0.877 | 0.392 |
| Gemini 3 Flash | 0.247 | 0.879 | 0.385 |

### Semantic Score Validation
- TP mean: 0.745 (SD=0.054)
- FP mean: 0.632 (SD=0.057)
- Mann-Whitney U: p<0.000001
- Cliff's Delta: **0.840** (large effect)

---

## Three Contributions (For Abstract & Introduction)

1. **Empirical:** First large-scale measurement of LLM alignment with Notional Machine theory, revealing 16–99% recall variance by category type.

2. **Practical:** A taxonomy for educators: which misconceptions are safe for AI vs. require humans.

3. **Methodological:** Ensemble voting improves F1 from 0.469 to 0.763 (+63%), providing a deployment strategy.

---

## Methodology Summary

### Dataset
- 300 synthetic students (100 per assignment)
- 3 assignments: A1 (Variables), A2 (Loops), A3 (Arrays)
- 18 misconceptions across 10 Notional Machine categories
- 77% clean files, 23% seeded with exactly 1 misconception
- Generated using GPT-4 with structured injection prompts

### Detection Setup
- 6 models × 4 strategies × 300 students × 4 questions = ~28,800 detection attempts
- After filtering: 20,981 evaluated instances

### Semantic Matching
- OpenAI `text-embedding-3-large` (3072 dimensions)
- Cosine similarity threshold: 0.65
- Noise floor: 0.55 (below this = pedantic, not hallucination)

### Statistical Tests Used
- Bootstrap CI (1000 resamples)
- McNemar's test (paired strategy comparison)
- Cochran's Q (omnibus strategy test)
- Cliff's Delta (effect size)
- Mann-Whitney U (score distribution separation)

---

## Key Insights for Framing

### Why Structural vs. Semantic?

**Structural misconceptions** leave visible traces in code:
- Array bounds violations → visible index expressions
- String mutation attempts → visible method calls
- Unused return values → visible assignment absence

**Semantic misconceptions** require inferring student *intent*:
- Dangling Else → intent visible only in indentation
- Integer Division → expectation visible only in variable naming
- Spreadsheet View → expectation visible only in code structure

### Why Dangling Else is So Hard (16% Recall)

```java
if (x > 0)
    if (y > 0)
        print("both positive");
else                              // Student indented to bind to outer if
    print("x not positive");      // But Java binds to inner if
```

- Code is **syntactically valid**
- No runtime error
- The "bug" exists only in the **gap between expectation and semantics**
- LLMs would need to infer intent from indentation style

### Why Simple Prompts Beat CoT

1. **Task mismatch:** CoT designed for reasoning problems with verifiable steps. Misconception detection requires *empathy with wrong reasoning*.

2. **Hallucination amplification:** Longer generation = more chances to invent plausible-sounding but incorrect diagnoses.

3. **Socratic backfires:** Asking "what might the student think?" explicitly encourages speculation.

---

## Limitations to Acknowledge Honestly

1. **Synthetic data:** Students are LLM-generated. Real student errors may be messier, have multiple misconceptions, or show idiosyncratic patterns. This is standard in SE research (cite Defects4J precedent).

2. **Single language:** Java only. Dangling Else doesn't exist in Python. Core concepts likely transfer.

3. **Threshold not human-validated:** 0.65 threshold chosen via score distribution analysis (Cliff's Delta = 0.84), not human annotation. Sensitivity analysis shows ±0.05 changes F1 by <3%.

4. **18 misconceptions, not exhaustive:** Recursion, OOP, concurrency not covered.

---

## Related Work to Cite

### Foundational CS Ed Theory
- du Boulay (1986) - Original Notional Machine concept
- Sorva (2013) - Comprehensive Notional Machine synthesis
- Pea (1986) - Language-independent bugs (control flow)
- Kaczmarczyk et al. (2010) - Misconception identification

### Automated Feedback
- Keuning et al. (2018) - Systematic review of 101 feedback systems
- Gulwani et al. (2018) - Program repair for intro assignments

### LLMs in CS Education
- MacNeil et al. (2023) - Code explanations in e-books
- Phung et al. (2023) - Syntax error feedback
- Savelka et al. (2023) - GPT-4 on programming assessments

### Semantic Similarity
- Taghipour & Ng (2016) - Neural essay scoring
- Sultan et al. (2016) - Short answer grading

---

## Paper Structure

```
Abstract (150 words)
├── Problem: LLMs detect what, not why
├── Method: Semantic alignment with Notional Machines
├── Finding: 16-99% recall variance (Diagnostic Ceiling)
├── Contribution: Taxonomy + Ensemble (+63% F1)

1. Introduction (1 page)
├── Motivating example (Integer Division)
├── Notional Machine concept
├── Gap: No empirical measurement of LLM alignment
├── Contributions (3 bullets)
├── Research Questions (4)

2. Background & Related Work (0.75 page)
├── 2.1 Notional Machines
├── 2.2 Automated Feedback Systems
├── 2.3 LLMs in CS Education
├── 2.4 Semantic Similarity Methods

3. Methodology (1.5 pages)
├── 3.1 Dataset Construction
│   ├── Assignments
│   ├── Misconception Injection
│   └── Rationale for Synthetic Data
├── 3.2 Detection Instrument
│   ├── Models (6)
│   └── Prompting Strategies (4)
├── 3.3 Semantic Alignment
│   ├── Embedding approach
│   ├── Threshold validation
│   └── Noise floor filtering
├── 3.4 Ensemble Voting
├── 3.5 Statistical Analysis

4. Results (2 pages)
├── 4.1 Overall Performance (Table 2)
├── 4.2 RQ1: Category-Level Variance (Table 3)
├── 4.3 RQ2: Diagnostic Ceiling (Table 4)
├── 4.4 RQ3: Ensemble Voting (Table 5)
├── 4.5 RQ4: Prompting Strategies (Table 6)
├── 4.6 Model Comparison (Table 7)

5. Discussion (1 page)
├── 5.1 Tiered Deployment Model
├── 5.2 Why Semantic Misconceptions Are Hard
├── 5.3 The Prompting Paradox
├── 5.4 Ensemble as Deployment Strategy

6. Limitations (0.5 page)

7. Conclusion (0.25 page)

References (~13 citations)
```

---

## Phrases to Use

- "Diagnostic Ceiling" — the upper bound on AI misconception detection
- "Structural vs. semantic misconceptions" — the key explanatory dichotomy
- "Theory of mind for code" — what LLMs lack for semantic errors
- "The gap between expectation and semantics" — where hard misconceptions live
- "Cognitive root cause" — what we're measuring, not just "bugs"

---

## What NOT to Do

- **Don't oversell:** This is not "LLMs will replace teachers"
- **Don't undersell:** 16% → 99% variance is genuinely novel
- **Don't bury the lead:** Category-level variance is the headline
- **Don't ignore FPs:** 14,236 false positives is real (hence ensemble)
- **Don't claim exhaustive coverage:** 18 misconceptions is illustrative
- **Don't hide synthetic data:** Acknowledge it early, cite precedent, argue for value
