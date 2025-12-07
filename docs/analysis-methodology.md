# Analysis Pipeline: Complete Guide

A beginner-friendly, comprehensive guide to understanding how this framework analyzes LLM misconception detection.

---

## Table of Contents

1. [What Are We Measuring?](#1-what-are-we-measuring)
2. [The Detection Process](#2-the-detection-process)
3. [The Matching Problem](#3-the-matching-problem)
4. [Understanding the Metrics](#4-understanding-the-metrics)
5. [The Report Explained](#5-the-report-explained)
6. [Statistical Concepts Made Simple](#6-statistical-concepts-made-simple)

---

## 1. What Are We Measuring?

### The Core Question

> Can AI models detect when students have wrong mental models about how code works?

For example, a student might believe that `Math.sqrt(x)` changes `x` to its square root. This is a **misconception** about how Java works—the function returns a value but doesn't change `x`.

### Ground Truth

We know exactly what misconceptions are in each file because **we put them there**. This is called "seeded" data.

```
┌──────────────────────────────────────────────────────────────────┐
│                        GROUND TRUTH                               │
│                                                                   │
│  Student: Alice_Smith_12345                                       │
│  Question: Q1                                                     │
│  Misconception: NM_STATE_01 (Spreadsheet View)                   │
│                                                                   │
│  We KNOW this file has this exact misconception.                 │
│  The question is: Can the AI find it?                            │
└──────────────────────────────────────────────────────────────────┘
```

### Clean vs Seeded Files

```
┌─────────────────────────────────────────────────────────────────┐
│                    ALL STUDENT FILES (396)                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌───────────────────────┐    ┌───────────────────────────┐    │
│   │    CLEAN FILES        │    │    SEEDED FILES           │    │
│   │    301 (76%)          │    │    95 (24%)               │    │
│   │                       │    │                           │    │
│   │  ✓ No bugs            │    │  ✗ Has ONE misconception  │    │
│   │  ✓ Correct output     │    │  ✗ We know which one      │    │
│   │                       │    │                           │    │
│   └───────────────────────┘    └───────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. The Detection Process

### Step-by-Step Flow

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   STUDENT   │     │     LLM     │     │  DETECTION  │
│    FILE     │────▶│  ANALYZES   │────▶│   OUTPUT    │
│   (Java)    │     │    CODE     │     │   (JSON)    │
└─────────────┘     └─────────────┘     └─────────────┘
     │                    │                    │
     │                    │                    │
     ▼                    ▼                    ▼
  Alice_Q1.java      "What bugs      {misconceptions: [
                      do you see?"     {name: "...",
                                        description: "..."}
                                      ]}
```

### What We Test

We test **3 different AI models** and **4 different ways of asking** (prompts):

```
                    ┌────────────────────────────────────┐
                    │         DETECTION GRID             │
                    ├────────────┬───────────┬───────────┤
                    │   GPT-5.1  │ Gemini    │ Haiku     │
┌───────────────────┼────────────┼───────────┼───────────┤
│ baseline          │     ✓      │     ✓     │     ✓     │
│ (simple ask)      │            │           │           │
├───────────────────┼────────────┼───────────┼───────────┤
│ taxonomy          │     ✓      │     ✓     │     ✓     │
│ (with categories) │            │           │           │
├───────────────────┼────────────┼───────────┼───────────┤
│ cot               │     ✓      │     ✓     │     ✓     │
│ (step by step)    │            │           │           │
├───────────────────┼────────────┼───────────┼───────────┤
│ socratic          │     ✓      │     ✓     │     ✓     │
│ (mental model)    │            │           │           │
└───────────────────┴────────────┴───────────┴───────────┘

Total combinations: 3 models × 4 strategies = 12 different approaches
```

### Detection Opportunities

For each seeded file, each model+strategy combination has ONE chance to detect the misconception:

```
95 seeded files × 12 combinations = 1,140 "detection opportunities"
```

If using reasoning variants too: `95 × 24 = 2,280 opportunities`

---

## 3. The Matching Problem

### Why Matching is Hard

The AI might use different words than our ground truth:

```
┌────────────────────────────────────────────────────────────────┐
│                    THE NAMING PROBLEM                           │
│                                                                 │
│  Ground Truth:        "Spreadsheet View (Early Calculation)"   │
│  AI Output:           "Variable Auto-Update Error"             │
│                                                                 │
│  Same concept, different words!                                 │
│  How do we know they match?                                     │
└────────────────────────────────────────────────────────────────┘
```

### Three Matching Strategies

#### 1. Fuzzy Matching (Simple Word Comparison)

```
Ground Truth: "Integer Division Blindness"
AI Output:    "Division Truncation Error"
              
Common words: "Division" ✓
Score: Low (only 1 word matches)
```

**How it works:**
- Split both phrases into words
- Count how many words overlap
- Calculate a similarity score (0 to 1)

**Problem:** Misses paraphrases with different vocabulary.

---

#### 2. Semantic Matching (Meaning Comparison)

```
┌─────────────────────────────────────────────────────────────────┐
│                    SEMANTIC MATCHING                             │
│                                                                  │
│  Step 1: Convert text to numbers (embeddings)                   │
│                                                                  │
│  "Spreadsheet View"  →  [0.12, -0.45, 0.78, ...]  (3072 numbers)│
│  "Auto-Update Error" →  [0.15, -0.42, 0.81, ...]  (3072 numbers)│
│                                                                  │
│  Step 2: Compare the number patterns                            │
│                                                                  │
│  Cosine Similarity = 0.87  (very similar meaning!)              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**How it works:**
- Use OpenAI's text-embedding-3-large model
- Convert text descriptions into 3072-dimensional vectors
- Calculate cosine similarity (how much they "point" in same direction)
- Score of 0.7+ usually means same concept

**Advantage:** Catches paraphrases even with zero word overlap.

---

#### 3. Hybrid Matching (Best of Both)

```
┌─────────────────────────────────────────────────────────────────┐
│                    HYBRID FORMULA                                │
│                                                                  │
│  Final Score = (0.55 × Fuzzy) + (0.45 × Semantic) + Topic Bonus │
│                                                                  │
│  Example:                                                        │
│    Fuzzy Score:    0.40                                          │
│    Semantic Score: 0.85                                          │
│    Topic Bonus:    0.05 (same category)                          │
│                                                                  │
│    Final = (0.55 × 0.40) + (0.45 × 0.85) + 0.05                 │
│          = 0.22 + 0.38 + 0.05                                    │
│          = 0.65  ✓ Match!                                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**Why hybrid is best:**
- Fuzzy catches exact matches quickly
- Semantic catches paraphrases
- Topic bonus helps when categories match

---

## 4. Understanding the Metrics

### The Confusion Matrix

Every detection falls into one of four categories:

```
                          ┌─────────────────────────────────────┐
                          │        GROUND TRUTH                 │
                          ├─────────────────┬───────────────────┤
                          │  Has Bug        │  No Bug (Clean)   │
┌─────────────────────────┼─────────────────┼───────────────────┤
│  AI      │ Found Bug    │ TRUE POSITIVE   │ FALSE POSITIVE    │
│  Output  │              │ (Correct!)      │ (Hallucination)   │
│          ├──────────────┼─────────────────┼───────────────────┤
│          │ No Bug Found │ FALSE NEGATIVE  │ TRUE NEGATIVE     │
│          │              │ (Missed it!)    │ (Correct!)        │
└──────────┴──────────────┴─────────────────┴───────────────────┘
```

### Real Example

```
File: Alice_Q1.java
Ground Truth: NM_STATE_01 (Spreadsheet View)

Scenario 1: AI says "Early Calculation Error" → Matches NM_STATE_01 → TRUE POSITIVE ✓
Scenario 2: AI says nothing wrong              → Missed it           → FALSE NEGATIVE ✗
Scenario 3: AI says "Syntax Error"             → Wrong!              → FALSE POSITIVE ✗
            (on a file that's actually clean)
```

---

### Precision, Recall, and F1 Score

These are the three most important numbers in our analysis:

#### Precision: "When the AI flags something, how often is it right?"

```
                    True Positives
Precision = ─────────────────────────────────
            True Positives + False Positives

Example:
  AI flagged 100 misconceptions
  80 were actually real misconceptions
  20 were hallucinations (false positives)
  
  Precision = 80 / (80 + 20) = 0.80 = 80%
  
  Interpretation: "80% of the AI's flags are correct"
```

#### Recall: "Of all the real bugs, how many did the AI find?"

```
                  True Positives
Recall = ─────────────────────────────────
         True Positives + False Negatives

Example:
  There are 95 seeded misconceptions
  AI found 80 of them
  AI missed 15
  
  Recall = 80 / (80 + 15) = 0.84 = 84%
  
  Interpretation: "The AI found 84% of all bugs"
```

#### F1 Score: "The balanced combination"

```
         2 × Precision × Recall
F1 = ──────────────────────────────
         Precision + Recall

Example:
  Precision = 0.80
  Recall = 0.84
  
  F1 = 2 × 0.80 × 0.84 / (0.80 + 0.84)
     = 1.344 / 1.64
     = 0.82 = 82%
  
  Interpretation: "Overall detection quality is 82%"
```

#### Visual Summary

```
┌──────────────────────────────────────────────────────────────────┐
│                    METRIC INTERPRETATION                          │
├────────────┬─────────────────────────────────────────────────────┤
│ Precision  │ Are the AI's detections trustworthy?                │
│            │ High = Few false alarms                              │
├────────────┼─────────────────────────────────────────────────────┤
│ Recall     │ Does the AI catch most bugs?                        │
│            │ High = Few missed detections                         │
├────────────┼─────────────────────────────────────────────────────┤
│ F1         │ Overall balance of both                              │
│            │ High = Good at both finding bugs AND being accurate │
└────────────┴─────────────────────────────────────────────────────┘
```

---

## 5. The Report Explained

### Section by Section Guide

#### 1. Dataset Summary

```markdown
### Dataset Summary
- **Students:** 99
- **Questions:** 4 (Q1, Q2, Q3, Q4)
- **Total files:** 396
- **Seeded files:** 95 (24%)
- **Clean files:** 301 (76%)
- **Detection opportunities:** 2280
```

**What this tells you:**
- 99 synthetic students, each with 4 Java files
- 24% of files have intentional bugs (we know exactly which ones)
- 2280 = 95 seeded files × 24 model combinations (chances to detect)

---

#### 2. The Diagnostic Ceiling (RQ1)

```
┌────────────────────────────────────────────────────────────────┐
│  POTENTIAL RECALL = 100%                                        │
│                                                                 │
│  This means: Every single seeded bug was detected by at        │
│  least ONE model/strategy combination.                          │
│                                                                 │
│  Good news! The bugs are detectable.                           │
│  The question is: which configuration works best?               │
└────────────────────────────────────────────────────────────────┘
```

**Ceiling = maximum possible if you use ALL models together**

---

#### 3. Matcher Ablation Study

This section compares our three matching methods:

```
┌──────────────────────────────────────────────────────────────┐
│            MATCHER COMPARISON (The "Ablation")                │
├──────────────┬───────────┬──────────┬────────────────────────┤
│   Matcher    │ Precision │  Recall  │  F1 (overall score)    │
├──────────────┼───────────┼──────────┼────────────────────────┤
│ Fuzzy Only   │   5%      │   13%    │   7%   ← Bad!         │
│ Semantic     │   59%     │   87%    │   70%  ← Good         │
│ Hybrid       │   65%     │   87%    │   74%  ← Best!        │
└──────────────┴───────────┴──────────┴────────────────────────┘

Interpretation:
- Fuzzy alone is terrible (can't match paraphrases)
- Semantic is good (understands meaning)
- Hybrid is best (combines both strengths)
```

---

#### 4. Topic Difficulty

Some types of misconceptions are harder to detect than others:

```
┌────────────────────────────────────────────────────────────────┐
│                    TOPIC DETECTION RATES                        │
├────────────────────────────────────┬───────────────────────────┤
│  The Fluid Type Machine            │  60% recall  ← HARDEST   │
│  (integer division confusion)      │                           │
├────────────────────────────────────┼───────────────────────────┤
│  The Reactive State Machine        │  91% recall               │
│  (variables updating automatically)│                           │
├────────────────────────────────────┼───────────────────────────┤
│  The Anthropomorphic I/O Machine   │  97% recall               │
│  (computer reads prompts)          │                           │
├────────────────────────────────────┼───────────────────────────┤
│  The Algebraic Syntax Machine      │  100% recall ← EASIEST   │
│  (using ^ for power)               │                           │
└────────────────────────────────────┴───────────────────────────┘
```

**Why does this matter?**
- Syntax errors (like using `^` wrong) are easy to spot
- Logic errors (like integer division) are subtle and hard to detect
- This tells us WHERE AI needs improvement

---

#### 5. Model Agreement

Do different AI models agree with each other?

```
┌──────────────────────────────────────────────────────────────────┐
│                    MODEL AGREEMENT                                │
│                                                                   │
│  Cohen's Kappa (κ) measures how much two models agree            │
│  beyond what you'd expect by random chance.                       │
│                                                                   │
│  Scale:                                                          │
│    0.0 - 0.2  = Slight agreement                                 │
│    0.2 - 0.4  = Fair agreement                                   │
│    0.4 - 0.6  = Moderate agreement                               │
│    0.6 - 0.8  = Substantial agreement                            │
│    0.8 - 1.0  = Almost perfect agreement                         │
│                                                                   │
│  Example: κ = 0.71 between GPT and Gemini                        │
│  → They substantially agree on most detections                    │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

**Why this matters for ensembles:**
- If models DISAGREE a lot → combining them might find MORE bugs
- If models AGREE a lot → less benefit from using multiple models

---

#### 6. Confidence Calibration

Is the AI's confidence score trustworthy?

```
┌──────────────────────────────────────────────────────────────────┐
│                    CALIBRATION                                    │
│                                                                   │
│  When AI says "90% confident," is it correct 90% of the time?    │
│                                                                   │
│  Perfect calibration:                                             │
│    Confidence 0.9 → Accuracy 90%                                 │
│    Confidence 0.7 → Accuracy 70%                                 │
│                                                                   │
│  Bad calibration (overconfident):                                 │
│    Confidence 0.9 → Accuracy 60%  ← Says 90% but only 60% right! │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

#### 7. Hallucination Analysis

"Hallucinations" are when the AI invents problems that don't exist:

```
┌──────────────────────────────────────────────────────────────────┐
│              TOP HALLUCINATED MISCONCEPTIONS                      │
│                                                                   │
│  1. "Tautological Conditional Logic" - 63 times                  │
│     → AI claims redundant if-statements, but code is fine        │
│                                                                   │
│  2. "Redundant Variable Assignment" - 41 times                   │
│     → AI claims wasted assignments, but code is fine             │
│                                                                   │
│  These are FALSE POSITIVES that hurt precision.                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 6. Statistical Concepts Made Simple

### Bootstrap Confidence Intervals

**Problem:** If we run the analysis once, we get one F1 score. But how reliable is that number?

**Solution:** Bootstrap sampling

```
┌──────────────────────────────────────────────────────────────────┐
│                    BOOTSTRAP PROCESS                              │
│                                                                   │
│  1. Take the 95 seeded files                                     │
│  2. Randomly pick 95 files WITH REPLACEMENT (some repeat)        │
│  3. Calculate F1 on this sample                                  │
│  4. Repeat 400 times                                             │
│  5. Look at the range of F1 values                               │
│                                                                   │
│  If 95% of samples gave F1 between 0.72 and 0.81:                │
│  → "95% Confidence Interval: [0.72, 0.81]"                       │
│                                                                   │
│  Interpretation: We're 95% confident the true F1 is              │
│  somewhere between 0.72 and 0.81                                 │
└──────────────────────────────────────────────────────────────────┘
```

In the report, you'll see: `F1: 0.76 CI [0.72-0.81]`

---

### McNemar's Test

**Question:** Is Model A actually better than Model B, or did it just get lucky?

```
┌──────────────────────────────────────────────────────────────────┐
│                    McNEMAR'S TEST                                 │
│                                                                   │
│  For each file, record which model got it right:                 │
│                                                                   │
│  ┌───────────────────────────────────────────────────────┐       │
│  │              │   Model B Right  │  Model B Wrong     │       │
│  ├──────────────┼──────────────────┼────────────────────┤       │
│  │ Model A Right│   Both got it    │  Only A got it     │       │
│  │              │      (68)        │      (10)          │       │
│  ├──────────────┼──────────────────┼────────────────────┤       │
│  │ Model A Wrong│  Only B got it   │  Both missed it    │       │
│  │              │      (11)        │       (6)          │       │
│  └──────────────┴──────────────────┴────────────────────┘       │
│                                                                   │
│  McNemar looks at the DISAGREEMENT cells (10 vs 11)              │
│  If they're similar → no significant difference                   │
│  If very different → one model is truly better                    │
│                                                                   │
│  p-value < 0.05 → Difference is STATISTICALLY SIGNIFICANT        │
│  p-value > 0.05 → Difference could be random chance              │
└──────────────────────────────────────────────────────────────────┘
```

---

### Expected Calibration Error (ECE)

How well does the AI's confidence match reality?

```
┌──────────────────────────────────────────────────────────────────┐
│                    ECE CALCULATION                                │
│                                                                   │
│  1. Group predictions by confidence (0-10%, 10-20%, etc.)        │
│  2. For each group, compare:                                     │
│     - Average confidence in that group                           │
│     - Actual accuracy in that group                              │
│  3. ECE = weighted average of |confidence - accuracy|            │
│                                                                   │
│  ECE = 0.00  →  Perfect calibration                              │
│  ECE = 0.10  →  10% average miscalibration                       │
│  ECE = 0.30  →  Poorly calibrated                                │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## Summary: Reading the Report

When you open a report, here's what to look for:

| Section            | Key Question                     | Good Sign                 |
| ------------------ | -------------------------------- | ------------------------- |
| Diagnostic Ceiling | Can bugs be detected at all?     | High percentage           |
| Matcher Ablation   | Which matching method works?     | Hybrid > Semantic > Fuzzy |
| Topic Difficulty   | Which bug types are hard?        | Even recall across topics |
| Full Results Table | Which config is best?            | High F1 with tight CIs    |
| Model Agreement    | Do models complement each other? | Moderate κ (0.4-0.6)      |
| Calibration        | Is confidence trustworthy?       | ECE < 0.10                |
| Hallucinations     | What false positives occur?      | Low counts                |

---

## Quick Reference: Metric Targets

| Metric    | Poor  | Acceptable | Good      | Excellent |
| --------- | ----- | ---------- | --------- | --------- |
| Precision | <50%  | 50-65%     | 65-80%    | >80%      |
| Recall    | <50%  | 50-70%     | 70-85%    | >85%      |
| F1        | <50%  | 50-65%     | 65-80%    | >80%      |
| Cohen's κ | <0.2  | 0.2-0.4    | 0.4-0.6   | >0.6      |
| ECE       | >0.20 | 0.15-0.20  | 0.10-0.15 | <0.10     |
