# Matcher Ablation Study

This document explains the three matching strategies and how to compare them.

## The Problem

When an LLM detects a misconception, it outputs free-form text like:

```
"The student used integer division when they needed decimal division"
```

We need to **align** this to our ground truth taxonomy, which has entries like:

```json
{
  "id": "TYP-01",
  "misconception_name": "Integer Division Trap",
  "description": "Student performs integer division when floating-point is needed"
}
```

The matcher determines whether this detection should be classified as:
- **True Positive**: Correctly identified TYP-01
- **False Positive**: Hallucination (no matching ground truth)
- **False Negative**: Missed a seeded misconception

## Matching Strategies

### 1. Fuzzy Matching (`fuzzy_only`)

**How it works:**
- Computes string similarity (Levenshtein distance, token overlap)
- Compares detection name/description to ground truth name/description
- Threshold: 0.5 similarity

**Pros:**
- Fast (no API calls)
- Interpretable

**Cons:**
- Fails when LLM uses different phrasing
- Example: "Int instead of double" vs "Integer Division Trap" → low match

**Typical performance:** F1 ≈ 0.05 (fails badly)

### 2. Semantic Matching (`semantic_only`)

**How it works:**
- Embeds detection and ground truth using OpenAI's `text-embedding-3-large`
- Computes cosine similarity between embeddings
- Threshold: 0.5 similarity

**Pros:**
- Captures semantic meaning, not just words
- Handles paraphrasing well

**Cons:**
- Requires API calls (slower, costs money)
- May match semantically similar but incorrect misconceptions

**Typical performance:** F1 ≈ 0.60

### 3. Hybrid Matching (`hybrid`)

**How it works:**
- Combines fuzzy + semantic scores
- Adds topic prior (boost if topics match)
- Weighted formula: `0.3*fuzzy + 0.5*semantic + 0.2*topic_prior`

**Pros:**
- Theoretically should be best of both worlds

**Cons:**
- In practice, ~same as semantic only
- Added complexity without benefit

**Typical performance:** F1 ≈ 0.59

## Running Ablation

```bash
uv run analyze analyze --match-mode all --run-tag "ablation_v1"
```

This runs all three matchers on the same detection data and produces:
- Comparison table
- Matcher × Strategy distribution plot
- Precision-Recall scatter by matcher

## Expected Results

| Matcher | Avg Precision | Avg Recall | Avg F1 |
|---------|---------------|------------|--------|
| fuzzy_only | 0.042 | 0.078 | 0.053 |
| semantic_only | 0.542 | 0.679 | 0.598 |
| hybrid | 0.538 | 0.664 | 0.591 |

**Key finding:** Semantic matching alone is sufficient. The hybrid approach doesn't add value over pure semantic matching, and fuzzy matching fails completely.

## Interpretation

### Why Fuzzy Fails

LLMs don't use the exact same words as our ground truth. Compare:

| LLM Detection | Ground Truth | Fuzzy Match? |
|---------------|--------------|--------------|
| "Int instead of double" | "Integer Division Trap" | ❌ Low |
| "Wrong operator precedence" | "Precedence Blindness" | ❌ Low |
| "Forgot to close Scanner" | "Scanner Resource Leak" | ❌ Low |

Even when detecting the same misconception, the phrasing is different enough that string matching fails.

### Why Semantic Works

Embeddings capture meaning, not just words:

| LLM Detection | Ground Truth | Semantic Match? |
|---------------|--------------|-----------------|
| "Int instead of double" | "Integer Division Trap" | ✅ High |
| "Wrong operator precedence" | "Precedence Blindness" | ✅ High |
| "Forgot to close Scanner" | "Scanner Resource Leak" | ✅ High |

### Why Hybrid ≈ Semantic

The hybrid approach adds:
1. Fuzzy component (but fuzzy is terrible, so this adds noise)
2. Topic prior (minor boost when topics match)

In practice, the semantic signal dominates, and the other components add little value.

## Implications for Research

1. **Semantic matching is essential** for LLM-based misconception detection
2. **Fuzzy matching is not viable** as a standalone approach
3. **Simpler is better**: Pure semantic matching is as good as hybrid
4. **Embedding quality matters**: We use `text-embedding-3-large` (best available)

## Visualizations

The ablation study generates these plots:

### Matcher Ablation Bar Chart
`docs/report_assets/matcher_ablation.png`

Shows F1 by matcher, clearly showing fuzzy's failure.

### Matcher × Strategy Grid
`docs/report_assets/matcher_strategy_grid.png`

Grouped bar chart showing F1 for each matcher across all strategies.

### Precision-Recall by Matcher
`docs/report_assets/matcher_pr_scatter.png`

Scatter plot showing P/R tradeoff for each configuration, colored by matcher.

## Code Location

| Component | File |
|-----------|------|
| Fuzzy matcher | `utils/matching/fuzzy.py` |
| Semantic matcher | `utils/matching/semantic.py` |
| Hybrid matcher | `utils/matching/hybrid.py` |
| Matcher dispatcher | `analyze_cli.py:dispatch_matcher()` |
| Ablation plots | `analyze_cli.py:plot_matcher_*()` |
