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

# Compare to all 18 misconceptions
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
elif best_score < 0.65:
    result = "FP"     # Hallucination
else:
    if best_match == expected_id:
        result = "TP"  # Correct detection
    else:
        result = "FP"  # Wrong misconception
```

---

## Threshold Justification

### Score Distributions

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       SEMANTIC SCORE DISTRIBUTION                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  True Positives:                                                             │
│  ├── Mean:   0.745                                                          │
│  ├── StdDev: 0.054                                                          │
│  └── Most scores: 0.70 - 0.85                                               │
│                                                                             │
│  False Positives:                                                            │
│  ├── Mean:   0.632                                                          │
│  ├── StdDev: 0.057                                                          │
│  └── Most scores: 0.55 - 0.70                                               │
│                                                                             │
│  Visual:                                                                    │
│                                                                             │
│  Score:  0.50    0.55    0.60    0.65    0.70    0.75    0.80    0.85       │
│          │       │       │       │       │       │       │       │          │
│  FP:     ░░░░░░░░████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░            │
│  TP:     ░░░░░░░░░░░░░░░░░░░░░░░░████████████████████████████████          │
│                          ↑                                                   │
│                      Threshold                                              │
│                       (0.65)                                                │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Statistical Validation

| Test | Result | Interpretation |
|------|--------|----------------|
| Mann-Whitney U | p < 0.000001 | TP scores significantly higher than FP |
| Cliff's Delta | 0.840 | Large effect size |

The 0.65 threshold sits between the TP and FP distributions, minimizing classification errors.

---

## Noise Floor (0.55)

Detections with scores below 0.55 are "pedantic noise"—observations like:
- "Didn't close the Scanner"
- "Could use better variable names"
- "Unnecessary blank lines"

These are **not** Notional Machine misconceptions. We filter them out without counting them as hallucinations.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         NOISE FLOOR FILTERING                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Raw Detections:           29,164                                            │
│  After Noise Floor:        20,981  (7,549 filtered = 25.9%)                  │
│                                                                             │
│  Example pedantic detections filtered:                                       │
│  ├── "Should close Scanner resource" (score: 0.42)                          │
│  ├── "Variable naming could be clearer" (score: 0.38)                       │
│  └── "Missing Javadoc comment" (score: 0.35)                                │
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

| Metric | Before Ensemble | After Ensemble | Change |
|--------|-----------------|----------------|--------|
| Precision | 0.322 | **0.649** | +107% |
| Recall | 0.868 | 0.871 | stable |
| F1 | 0.469 | **0.744** | +61% |
| False Positives | 14,236 | 1,164 | **-92%** |

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

What happens with different thresholds?

| Threshold | Precision | Recall | F1 |
|-----------|-----------|--------|-----|
| 0.60 | 0.28 | 0.91 | 0.43 |
| **0.65** | **0.32** | **0.87** | **0.47** |
| 0.70 | 0.38 | 0.79 | 0.51 |
| 0.75 | 0.45 | 0.68 | 0.54 |

The 0.65 threshold balances precision and recall. Higher thresholds improve precision but sacrifice recall.

---

## Next: [Complexity Gradient](complexity-gradient.md)
