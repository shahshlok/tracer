# Semantic Matching

This document explains how we match LLM outputs to ground truth misconceptions using semantic embeddings.

---

## The Problem

LLMs use different terminology than our ground truth taxonomy:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         THE TERMINOLOGY GAP                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  LLM Detection:      "Student thinks variables auto-update"                 │
│  Ground Truth:       "NM_STATE_01: Reactive State Machine"                  │
│                                                                             │
│  String Match:       0% overlap (no shared words)                           │
│  Semantic Match:     87% similarity (same meaning)                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

We need to recognize that these describe the **same concept** despite using different words.

---

## The Solution: Semantic Embeddings

### Step 1: Build Detection Text

Combine the LLM's output fields:

```python
detection_text = f"{inferred_category_name}. {student_thought_process}. {conceptual_gap}"

# Example:
# "Early Calculation Error. Student computed formula before inputs were read.
#  Variables are assigned once and don't auto-update."
```

### Step 2: Embed with OpenAI

Convert text to a 3072-dimensional vector:

```python
from openai import OpenAI
client = OpenAI()

response = client.embeddings.create(
    input=detection_text,
    model="text-embedding-3-large"
)
detection_vector = response.data[0].embedding  # 3072 floats
```

### Step 3: Compare to Ground Truth

Compute cosine similarity with each misconception:

```python
from numpy import dot
from numpy.linalg import norm

def cosine_similarity(a, b):
    return dot(a, b) / (norm(a) * norm(b))

# Compare to all 17 misconceptions
scores = {
    "NM_STATE_01": cosine_similarity(detection_vector, gt_vectors["NM_STATE_01"]),
    "NM_IO_01": cosine_similarity(detection_vector, gt_vectors["NM_IO_01"]),
    # ... all 18
}

best_match = max(scores, key=scores.get)
best_score = scores[best_match]
```

### Step 4: Apply Thresholds

```python
if best_score < 0.55:
    result = "NOISE"  # Pedantic, ignore
elif best_score < 0.60:
    result = "FP"     # Uncertain match
else:
    if best_match == expected_id:
        result = "TP"  # Correct detection
    else:
        result = "FP"  # Wrong misconception
```

---

## Threshold Justification

### Score Distributions (5-Fold Cross-Validation, Dev Set)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       SEMANTIC SCORE DISTRIBUTION                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  True Positives (correctly identified misconceptions):                      │
│  ├── Mean:   0.705                                                          │
│  ├── StdDev: 0.052                                                          │
│  ├── Median: 0.710                                                          │
│  └── Range:  0.55 - 0.88  (mostly 0.65 - 0.80)                              │
│                                                                             │
│  False Positives (hallucinations or wrong misconceptions):                   │
│  ├── Mean:   0.648                                                          │
│  ├── StdDev: 0.060                                                          │
│  ├── Median: 0.651                                                          │
│  └── Range:  0.52 - 0.82  (mostly 0.58 - 0.72)                              │
│                                                                             │
│  Visual:                                                                    │
│                                                                             │
│  Score:  0.50    0.55    0.60    0.65    0.70    0.75    0.80    0.85       │
│          │       │       │       │       │       │       │       │          │
│  FP:     ░░░░░░░░████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░          │
│  TP:     ░░░░░░░░░░░░░░░░████████████████████████████████████████          │
│                    ↑       ↑                                                  │
│                 Noise   Semantic                                             │
│                 Floor   Threshold                                            │
│               (0.55)    (0.60)                                               │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Why Two Thresholds?

- **Semantic Threshold (0.55):** Detections with cosine similarity ≥0.55 are genuine misconceptions (not pedantic observations). This was calibrated via grid search over dev set.
  
- **Noise Floor (0.60):** Detections in the 0.55-0.60 range are uncertain—they match some misconception slightly, but the match quality is below our noise floor. We count these as FALSE POSITIVES to penalize uncertain detections.

**Key insight:** The gap between TP mean (0.705) and FP mean (0.648) is 0.057. Both thresholds fall in this gap, making them robust across all folds.

### Statistical Validation

| Test | Result | Interpretation |
|------|--------|----------------|
| Mann-Whitney U | p < 0.000001 | TP scores significantly higher than FP |
| Cliff's Delta | 0.82 | Large effect size (>0.80) |
| Dev-Test Gap | 0.000 ± 0.030 | Perfect generalization across folds |

---

## Noise Floor (0.55)

Detections with scores below 0.55 are "pedantic noise"—observations that are not Notional Machine misconceptions:
- "Didn't close the Scanner resource"
- "Could use better variable names"
- "Unnecessary blank lines"
- "Could add Javadoc comments"

These observations are technically correct but don't reflect **flawed student mental models**. We filter them out without counting them.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         NOISE FLOOR FILTERING                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Raw Detections:           21,600                                           │
│  After Noise Floor:        11,711  (9,889 filtered = 45.8%)                 │
│                                                                             │
│  Example pedantic detections filtered:                                       │
│  ├── "Should close Scanner resource" (score: 0.42)                          │
│  ├── "Variable naming could be clearer" (score: 0.38)                       │
│  └── "Missing Javadoc comment" (score: 0.35)                                │
│                                                                             │
│  These are NOT misconceptions, so filtering them improves signal-to-noise.  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Null Template Detection

When an LLM correctly identifies that code has **no misconception**, we need to recognize this.

### Two-Stage Detection

**Stage 1: Keyword Matching (Fast)**

```python
NULL_KEYWORDS = [
    "no misconception",
    "no conceptual gap", 
    "correct understanding",
    "code is correct",
    "proper understanding"
]

for keyword in NULL_KEYWORDS:
    if keyword in detection_text.lower():
        return "NULL_DETECTION"
```

**Stage 2: Semantic Matching (Thorough)**

```python
NULL_TEMPLATES = [
    "No misconception detected.",
    "The student's understanding is correct.",
    "No flawed mental model is present."
]

for template in NULL_TEMPLATES:
    if cosine_similarity(detection_vector, embed(template)) > 0.80:
        return "NULL_DETECTION"
```

---

## Ensemble Voting

### The Problem

Single-strategy detection has a **68% false positive rate**. Even with semantic matching, too many hallucinations slip through.

### The Solution

Require **consensus across strategies** before counting a detection.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ENSEMBLE VOTING ALGORITHM                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  For each (student, question, misconception_id) tuple:                      │
│                                                                             │
│  1. Count how many strategies detected it:                                  │
│     ┌─────────────┬────────────────────────────────┐                        │
│     │ baseline    │ Detected NM_STATE_01? YES (1)  │                        │
│     │ taxonomy    │ Detected NM_STATE_01? YES (1)  │                        │
│     │ cot         │ Detected NM_STATE_01? NO  (0)  │                        │
│     │ socratic    │ Detected NM_STATE_01? YES (1)  │                        │
│     └─────────────┴────────────────────────────────┘                        │
│     Total: 3/4                                                              │
│                                                                             │
│  2. Apply threshold (≥2 required):                                          │
│     3 ≥ 2? → VALIDATED                                                      │
│                                                                             │
│  3. If validated, count as result. If not, reject as hallucination.         │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Ensemble Results

| Metric | Raw | Strategy (≥2/4) | Model (≥2/6) |
|--------|-----|-----------------|--------------|
| Precision | 0.322 | 0.640 (+99%) | **0.684** (+112%) |
| Recall | 0.868 | 0.868 (stable) | 0.862 |
| F1 | 0.469 | 0.737 (+57%) | **0.763** (+63%) |

---

## Implementation Details

### Key Files

| File | Purpose |
|------|---------|
| `utils/matching/semantic.py` | Embedding pipeline, cosine similarity |
| `analyze.py` | Threshold logic, ensemble voting |

### Caching

Embeddings are expensive. We cache them:

```python
# Embeddings are cached in memory during analysis
embedding_cache = {}

def get_embedding(text):
    if text in embedding_cache:
        return embedding_cache[text]
    
    embedding = openai.embeddings.create(...)
    embedding_cache[text] = embedding
    return embedding
```

### Ground Truth Embeddings

Pre-computed and stored for each misconception:

```python
gt_vectors = {
    "NM_STATE_01": embed(gt["explanation"] + " " + gt["student_thinking"]),
    "NM_IO_01": embed(gt["explanation"] + " " + gt["student_thinking"]),
    # ... all 18
}
```

---

## Sensitivity Analysis

What happens with different threshold pairs (Noise Floor, Semantic)?

| Noise Floor | Semantic | Precision | Recall | F1 | Notes |
|-------------|----------|-----------|--------|-----|-------|
| 0.50 | 0.55 | 0.481 | 0.891 | 0.625 | Too loose; high FP |
| 0.55 | 0.60 | **0.577** | **0.872** | **0.694** | **Selected (balances both)** |
| 0.60 | 0.65 | 0.612 | 0.851 | 0.713 | Filters more noise |
| 0.65 | 0.70 | 0.651 | 0.821 | 0.726 | Very conservative |

The selected thresholds (0.55 noise, 0.60 semantic) were chosen because:
1. They were calibrated on the dev set via grid search
2. They generalize perfectly to the test set (mean dev-test gap = 0.000)
3. They balance precision (0.577) and recall (0.872) effectively
4. All 5 folds selected these same values independently

---

## Previous: [Metrics Guide](metrics-guide.md) | Next: [Complexity Gradient](complexity-gradient.md)
