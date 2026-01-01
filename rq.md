# Research Questions & Thesis

## Thesis Statement

> "We characterize the **'Notional Machine Gap'** in Large Language Models—the systematic variance in their ability to diagnose student mental models based on the *type* of misconception, not the assignment complexity. Through a controlled study using semantic embedding matching, we demonstrate that LLMs achieve near-perfect recall (>95%) on **structural misconceptions** (array bounds, string immutability, API misuse) but exhibit a **Diagnostic Ceiling** (<65% recall) on **semantic misconceptions** involving implicit type behavior, execution order, and control flow ambiguity. We further show that **simple prompting strategies outperform pedagogical role-playing**, and that **ensemble voting** can improve precision at acceptable recall cost."

---

## Primary Research Questions

### RQ1. The Notional Machine Detection Gap

**Question:** Does LLM diagnostic performance vary systematically by **Notional Machine category**?

**Hypothesis:** We hypothesize that misconception detectability depends on the *nature* of the error, not the assignment complexity:

- **Structural Misconceptions (High Detection):** Errors with concrete, visible effects (array bounds, string immutability, unused return values)
- **Semantic Misconceptions (Low Detection):** Errors involving implicit behavior (type coercion, execution order, control flow ambiguity)

**Key Findings:**
| Category | Recall | Classification |
|----------|--------|----------------|
| Mutable String Machine | 0.99 | Easy (Structural) |
| Void Machine | 0.99 | Easy (Structural) |
| Human Index Machine | 0.97 | Easy (Structural) |
| Teleological Control | 0.93 | Easy (Structural) |
| Reactive State Machine | 0.65 | Hard (Semantic) |
| Fluid Type Machine | 0.59 | Hard (Semantic) |
| Independent Switch | 0.63 | Hard (Semantic) |

**Goal:** To create a taxonomy of misconceptions that are "safe" for AI diagnosis vs. those requiring human oversight.

### RQ2. The Diagnostic Ceiling

**Question:** Which specific **Mental Models** are invisible to LLMs?

**Explanation:**
We identify three misconceptions with <70% recall that represent the "Diagnostic Ceiling":

1. **Dangling Else (16% recall):** LLMs struggle with indentation-based control flow errors
2. **Narrowing Cast (31% recall):** Misplaced type casts are rarely detected correctly
3. **Spreadsheet View (65% recall):** Reactive state assumptions are borderline detectable

**Implication:** These misconceptions require human intervention in any AI-assisted grading system.

### RQ3. Prompting Strategy Effectiveness

**Question:** Which prompting strategy maximizes diagnostic accuracy?

**Hypothesis:** We initially hypothesized that Chain-of-Thought (mechanical tracing) would outperform Socratic (pedagogical role-playing).

**Findings:**
| Strategy | F1 Score | Interpretation |
|----------|----------|----------------|
| Baseline | 0.519 | **Best** - Simple prompts work |
| Taxonomy | 0.518 | Near-best - Category hints help |
| CoT | 0.489 | Worse - Over-analysis hurts |
| Socratic | 0.391 | Worst - Role-playing degrades accuracy |

**Key Finding:** Simple, direct prompts outperform complex prompting strategies. The "Prompting Paradox" is inverted—*less* structure produces *better* results.

### RQ4. Ensemble Voting Effectiveness

**Question:** Can ensemble voting improve precision without catastrophic recall loss?

**Explanation:**
With raw precision at 32%, we test two ensemble approaches:

1. **Strategy Ensemble (≥2/4):** Requires 2+ prompting strategies to agree
2. **Model Ensemble (≥2/6):** Requires 2+ models to agree

**Goal:** Determine if consensus-based filtering produces a viable precision-recall tradeoff for practical deployment.

---

## Methodology Summary

- **Dataset:** 300 synthetic students (100 per assignment) with LLM-injected misconceptions
- **Assignments:** A1 (Variables), A2 (Loops), A3 (Arrays)
- **Models:** 6 LLMs (GPT-5.2, Claude-Haiku, Gemini-3-Flash + reasoning variants)
- **Strategies:** 4 prompting approaches (Baseline, Taxonomy, CoT, Socratic)
- **Matching:** Semantic embedding similarity (OpenAI text-embedding-3-large, threshold ≥0.65)
- **Statistics:** Bootstrap CIs, McNemar's test, Cochran's Q, Cliff's Delta
