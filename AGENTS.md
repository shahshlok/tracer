<coding_guidelines>
# AGENT GUIDANCE & ARCHITECTURE

This repository contains a research harness for a Bachelor's Honours Thesis targeting **ITiCSE/SIGCSE**.
The goal is **not** to build a grading tool, but to measure the **Cognitive Alignment** of LLMs with CS Education theory—specifically, their ability to diagnose student *mental models* (Notional Machines).

---

## 1. Core Architecture

The system operates on a **Synthetic Injection -> Blind Detection -> Semantic Alignment** pipeline.

### Stage 1: The Notional Machine Injection (Dataset Generation)
*   **Source:** We use rigorous JSON definitions of Notional Machines, not random bugs.
*   **Files:** `data/a1/groundtruth.json`, `data/a2/groundtruth.json`, `data/a3/groundtruth.json`
*   **Mechanism:**
    *   Select a specific misconception (e.g., `NM_STATE_01`).
    *   Use LLM to inject it into a valid solution using `student_thinking` and `instructions_for_llm` fields.
    *   **Constraint:** One misconception per file to ensure clean labeling.
*   **Generator:** `utils/generators/dataset_generator.py`

### Stage 2: The Blind Diagnosis (Detection)
*   **Philosophy:** We do *not* give the LLM the answer key. We measure if it can "discover" the Notional Machine failure.
*   **The Instrument:** LLMs must output JSON (via Pydantic) containing:
    1.  `inferred_category_name` (String, open-ended)
    2.  `student_thought_process` (Narrative description)
    3.  `evidence` (Line numbers)
*   **Prompts:** 4 strategies in `prompts/strategies.py`:
    1.  **Baseline:** Simple error classification
    2.  **Taxonomy:** Explicit Notional Machine categories provided
    3.  **CoT:** Chain-of-thought line-by-line tracing
    4.  **Socratic:** Mental model probing
*   **Detector:** `miscons.py`

### Stage 3: Semantic Alignment (Evaluation)
*   **The Challenge:** LLM might call it "Auto-Update Error" while we call it "Reactive State Machine."
*   **The Solution:** Cosine Similarity via OpenAI `text-embedding-3-large`.
*   **Logic:**
    *   Embed the `student_thought_process` from LLM detection
    *   Embed the `explanation` + `student_thinking` from Ground Truth
    *   If `Similarity ≥ 0.65`, count as **True Positive**
*   **Analyzer:** `analyze.py`

---

## 2. The Notional Machine Taxonomy

The thesis focuses on **category-level detection variance**, not assignment complexity.

### Key Finding: Structural vs. Semantic Misconceptions

| Category | Type | Detection | Example Misconception |
|----------|------|-----------|----------------------|
| The Void Machine | Structural | **Easy** (99%) | Calling Math.sqrt() without assignment |
| The Mutable String Machine | Structural | **Easy** (99%) | String immutability ignored |
| The Human Index Machine | Structural | **Easy** (97%) | 1-based array indexing |
| The Teleological Control Machine | Structural | **Easy** (93%) | Loop control flow errors |
| The Reactive State Machine | Semantic | **Hard** (65%) | Variables as spreadsheet cells |
| The Independent Switch | Semantic | **Hard** (63%) | if/else-if confusion |
| The Fluid Type Machine | Semantic | **Hard** (59%) | Integer division blindness |

### Per-Assignment Categories

| Assignment | Categories | Avg Recall |
|------------|------------|------------|
| **A1** | Void, Algebraic Syntax, Anthropomorphic I/O, Reactive State, Fluid Type | 0.767 |
| **A2** | Teleological Control, Independent Switch | 0.861 |
| **A3** | Human Index, Semantic Bond, Mutable String | 0.971 |

---

## 3. Methodology Standards

**1. The "Honest N-Count"**
*   Report results based on **Unique (student, question, misconception)** tuples.
*   Example: "Of 13 files containing 'Spreadsheet View', 65% were correctly diagnosed."

**2. The "Hallucination Trap"**
*   Track **False Positives** aggressively (current: 14,236 FPs vs 6,745 TPs).
*   `FP_CLEAN`: LLM invented a bug in correct code
*   `FP_WRONG`: LLM detected wrong misconception
*   `FP_HALLUCINATION`: LLM invented non-existent error pattern

**3. Statistical Rigor**
*   **Bootstrap CIs:** 1000 resamples for precision/recall/F1
*   **McNemar's Test:** Paired strategy comparison
*   **Cochran's Q:** Omnibus test across all strategies
*   **Cliff's Delta:** Effect size for semantic score separation

**4. Ensemble Voting**
*   **Strategy Ensemble:** ≥2/4 strategies must agree
*   **Model Ensemble:** ≥2/6 models must agree
*   Purpose: Trade recall for precision to reduce hallucinations

---

## 4. Operational Directives for Agents

1.  **Do not invent new taxonomies.** Use IDs from `data/*/groundtruth.json`.
2.  **Preserve the Pydantic Models.** The output schema in `pydantic_models/` is the scientific instrument.
3.  **Focus on the Category Gap.** The thesis is about variance across *Notional Machine categories*, not assignments.
4.  **Track Both Ensembles.** Report strategy-ensemble and model-ensemble results in all analyses.
5.  **The Diagnostic Ceiling:** Misconceptions <70% recall require human oversight:
    *   `NM_LOGIC_02` (Dangling Else): 16% recall
    *   `NM_TYP_02` (Narrowing Cast): 31% recall
    *   `NM_STATE_01` (Spreadsheet View): 65% recall
</coding_guidelines>
