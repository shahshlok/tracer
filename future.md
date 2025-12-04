# Future Work

Items explicitly deferred from the current evaluation framework to maintain scope and defensibility.

---

## 1. Confidence Score Analysis

**Current status:** The LLM schema includes a `confidence` field, but we do not analyze calibration (ECE, Brier) in this version.

**Rationale:** LLM self-reported confidence is inherently subjective and may be uninformative (e.g., always 1.0). Without validation that confidence correlates with actual correctness, including it risks a reviewer asking "how did you validate the confidence scale?" which we cannot answer.

**Future direction:**
- Run calibration analysis (ECE, Brier score) once we have a larger dataset
- Experiment with prompts that elicit more thoughtful confidence estimates
- Compare calibration across models to see if some are better calibrated than others

---

## 2. Difficulty Labels for Misconceptions

**Current status:** We do not assign "easy/medium/hard" labels to misconceptions.

**Rationale:** Difficulty is subjective and requires either:
- Large-scale classroom data showing error rates per misconception
- Multiple expert raters with inter-rater reliability

Neither is available for this version. A chair could ask "how did you determine difficulty?" and we would have no defensible answer.

**Future direction:**
- Collect real student submission data with labeled misconceptions
- Compute empirical "miss rates" per misconception across models
- Use data-driven difficulty labels (e.g., based on model recall)
- Validate with multiple expert raters

---

## 3. Multiple Misconceptions Per File

**Current status:** Each file has at most one seeded misconception.

**Rationale:** Multi-misconception files complicate:
- Ground truth labeling (which detection maps to which misconception?)
- Matching logic (need multi-label matching, not single-label)
- Success/failure definitions (partial credit? all-or-nothing?)

Keeping it simple allows cleaner metrics and easier interpretation.

**Future direction:**
- Extend the hybrid matcher to return ranked list of matches
- Define partial-credit metrics for multi-label scenarios
- Analyze whether LLMs detect primary vs. secondary misconceptions differently

---

## 4. Severity Field

**Current status:** The LLM schema includes `severity`, but we do not analyze it.

**Rationale:** Severity is:
- Subjective (what makes a misconception "severe"?)
- Not used in TP/FP/FN decisions
- Potentially confusing with difficulty

**Future direction:**
- Define severity operationally (e.g., "would cause runtime error" vs "would cause wrong output" vs "stylistic")
- Analyze whether LLMs assign severity consistently
- Correlate severity with actual student outcomes if real data is available

---

## 5. Additional Models and Fine-Tuning

**Current status:** Two models (GPT-5.1, Gemini 2.5 Flash) with zero-shot prompting.

**Future direction:**
- Add more models (Claude, Llama, Mistral)
- Experiment with few-shot prompting
- Fine-tune a smaller model on misconception detection
- Compare cost/performance tradeoffs

---

## 6. Broader Misconception Taxonomy

**Current status:** ~15 misconceptions focused on input/output, types, and basic arithmetic.

**Future direction:**
- Expand to additional CS1 concepts: conditionals, loops, methods, arrays, parameter passing, etc.
- Incorporate misconceptions from CS education literature (e.g., Sorva's taxonomy) and align our synthetic injectors with those definitions.
- Cross-validate with other institutions' misconception lists to avoid overfitting to a single instructor/course, even if we remain in a fully synthetic regime.
- Design multiple **synthetic assignments** (e.g., kinematics calculator, grade calculator, coordinate transforms) that each exercise overlapping subsets of the taxonomy, so we can study model behavior across different problem contexts.

---

## 7. Replication Dimension

**Current status:** Single random seed for dataset generation on a single synthetic assignment.

**Future direction:**
- Generate **3–5 independent synthetic cohorts** per assignment using different random seeds (same taxonomy, same assignment).
- For each cohort, run the full pipeline (generation → detection → analysis) and store:
  - The manifest used.
  - A snapshot of key configuration (models, strategies, thresholds).
  - The aggregated metrics JSON and report for that run.
- Repeat this across **multiple synthetic assignments** (see Section 7), so we can:
  - Estimate variance in metrics across seeds for a fixed assignment.
  - Estimate variance across assignments (task sensitivity).
  - Strengthen robustness claims by showing that key patterns (e.g., topic difficulty, matcher behavior, model ordering) are stable across seeds and assignments.

---

## Priority Order for Future Work

1. Broader taxonomy + multiple synthetic assignments (higher impact while staying within synthetic regime)
2. Replication across seeds and assignments (robustness)
3. Additional models and prompting variants
4. Difficulty labels (data-driven, post-hoc, using synthetic performance)
5. Confidence calibration (once we trust the numbers)
6. Multiple misconceptions per file




So the 3 things to do are and in no particular order:
  1. Storage system
  2. Multiple assignments
  3. multiple seeded pipeline runs per assignment
  4. Per misconception analysis to find what misconceptions are easily caught by the models and which ones are not .

 A higher-level synthesis document/chapter that:
          - Summarizes patterns across runs and assignments.
          - Pulls out the key per-misconception and per-topic findings that are stable.
          - Connects them to CS ed literature.