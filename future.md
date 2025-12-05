# Future Work & Research Roadmap

This document outlines the strategic direction for the thesis, moving from "Does it work?" to "Where exactly does it break?".

## core Research Strategy: The "State Complexity" Progression

Current findings suggest LLMs struggle with variable state (e.g., `v1` vs `v0`). To prove this is a fundamental limitation rather than a fluke, we will replicate the study across a spectrum of increasing state complexity.

### 1. Assignment 2 (A2): Mathematical State [DONE]
- **Focus:** Variables, Arithmetic, Precedence.
- **State Complexity:** Low (Single assignments, linear flow).
- **Hypothesis:** LLMs handle syntax well but fail at invisible logic errors (e.g., operator precedence).

### 2. Assignment 3 (A3): Temporal State [NEXT]
- **Focus:** Loops, Conditionals, Boolean Logic.
- **State Complexity:** Medium (Variables change values over time/iterations).
- **Hypothesis:** LLMs will struggle to trace execution flow (e.g., off-by-one errors, infinite loops).

### 3. Assignment 4 (A4): Memory State
- **Focus:** Arrays, Strings.
- **State Complexity:** High (Mutable data structures, indexing).
- **Hypothesis:** Catastrophic failure on index-based logic (e.g., `arr[i]` vs `arr[i+1]`).

### 4. Assignment 5 (A5): Object State
- **Focus:** Classes, References, `this`.
- **State Complexity:** Very High (Shared references, heap vs stack).
- **Hypothesis:** LLMs will fail to distinguish between object identity (`==`) and value equality (`.equals`).

---

## Technical Enhancements

### 1. Robust Replication
- **Current:** Single seed per run.
- **Future:** 3-5 random seeds per assignment to generate error bars.
- **Goal:** Prove that "LLM Blindness" is statistically significant, not just bad luck.

### 2. Cross-Assignment Synthesis
- A meta-analysis script that aggregates Recall/Precision across A2, A3, A4.
- **Key Deliverable:** A single chart showing "Recall vs State Complexity". If the line goes down as complexity goes up, the thesis is proven.

### 3. Advanced Metrics
- **Potential Recall (The Ceiling):** The fraction of misconceptions found by *at least one* model/strategy configuration. Measures the theoretical maximum capability.
- **Average Recall (The Reliability):** The standard mean recall across all runs. Measures how likely a single agent is to succeed.
- **Consistency:** Measures stability. If we run the same model multiple times (or different models), do they agree? quantifying the "noise" in LLM grading.

---
