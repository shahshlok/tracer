# Matching Strategies

This document describes how LLM detection outputs are matched to ground truth misconceptions.

## The Challenge

The LLM might call it **"Auto-Update Error"** while groundtruth calls it **"Reactive State Machine"**.

We need semantic alignment, not just string matching.

---

## 1. Fuzzy Matching

**File:** `utils/matching/fuzzy.py`

Uses token overlap and sequence similarity.

### Methods

| Method              | Description                                   |
| ------------------- | --------------------------------------------- |
| Token Overlap       | Jaccard similarity of word sets               |
| Sequence Similarity | `difflib.SequenceMatcher` ratio               |
| Name Match          | Compare detected name → GT name               |
| Category Match      | Compare detected name → GT category           |
| Description Match   | Compare detected description → GT explanation |

### Threshold

Default: `0.3` (low, exploratory mode)

### When It Works

- Exact or near-exact name matches
- Common keywords present

### When It Fails

- Paraphrased descriptions
- Different vocabulary for same concept

---

## 2. Semantic Matching

**File:** `utils/matching/semantic.py`

Uses OpenAI embeddings + cosine similarity.

### How It Works

1. Build searchable text from detection:
   ```
   Misconception: {name}
   Description: {description}
   Student believes: {student_belief}
   ```

2. Build searchable text from ground truth:
   ```
   Misconception: {name}
   Explanation: {explanation}
   Student thinking: {student_thinking}
   Category: {category}
   ```

3. Embed both with `text-embedding-3-large`
4. Compute cosine similarity
5. Return best match above threshold

### Threshold

Default: `0.7`

### When It Works

- Paraphrased descriptions
- Conceptually similar explanations
- Different vocabulary, same meaning

### When It Fails

- Very short or vague descriptions
- Hallucinated misconceptions (high false positives)

---

## 3. Hybrid Matching (Recommended)

**File:** `utils/matching/hybrid.py`

Fuses fuzzy + semantic + topic prior.

### Formula

```
score = (blend_weight × fuzzy) + ((1 - blend_weight) × semantic) + prior
```

Default `blend_weight = 0.55`

### Topic Prior

Small boost (max 0.1) if detected topic overlaps with GT category.

```python
prior = min(0.1, token_overlap(detected_topic, gt_category) * 0.1)
```

### When It Works

- Best overall performance
- Balances precision and recall
- Catches both exact matches and paraphrases

---

## Matcher Ablation Results

From a typical run:

| Matcher       | Precision | Recall   | F1       |
| ------------- | --------- | -------- | -------- |
| Fuzzy Only    | 0.05      | 0.13     | 0.07     |
| Semantic Only | 0.59      | 0.87     | 0.70     |
| **Hybrid**    | **0.65**  | **0.87** | **0.74** |

**Conclusion:** Hybrid is the default recommendation.

---

## API Reference

### Fuzzy Match

```python
from utils.matching.fuzzy import fuzzy_match_misconception

matched_id, score, method = fuzzy_match_misconception(
    detected_name="Early Calculation Error",
    detected_description="Student computes before reading input",
    groundtruth=groundtruth_list,
    threshold=0.3
)
```

### Semantic Match

```python
from utils.matching.semantic import semantic_match_misconception

matched_id, score, method = semantic_match_misconception(
    detected_name="Early Calculation Error",
    detected_description="Student computes before reading input",
    detected_student_belief="Variables update automatically",
    groundtruth=groundtruth_list,
    threshold=0.7
)
```

### Hybrid Match

```python
from utils.matching.hybrid import hybrid_match_misconception

result = hybrid_match_misconception(
    detection={
        "name": "Early Calculation Error",
        "description": "...",
        "student_belief": "...",
        "topic": "State Machine"
    },
    groundtruth=groundtruth_list,
    fuzzy_threshold=0.55,
    semantic_threshold=0.65,
    blend_weight=0.55
)
# result.matched_id, result.score, result.detail
```
