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

## 6. Real Student Submissions

**Current status:** All data is synthetic (LLM-generated with seeded misconceptions).

**Future direction:**
- Obtain IRB approval for real student code
- Collect submissions from actual CS1 course
- Have TAs label misconceptions (establish inter-rater reliability)
- Validate synthetic results against real data

---

## 7. Broader Misconception Taxonomy

**Current status:** ~15 misconceptions focused on input/output, types, and basic arithmetic.

**Future direction:**
- Expand to conditionals, loops, methods, arrays
- Incorporate misconceptions from CS education literature (e.g., Sorva's taxonomy)
- Cross-validate with other institutions' misconception lists

---

## 8. Replication Dimension

**Current status:** Single random seed for dataset generation.

**Future direction:**
- Generate 3-5 datasets with different seeds
- Report variance in metrics across replications
- Strengthen claims about robustness

---

## Priority Order for Future Work

1. Real student submissions (highest impact on validity)
2. Difficulty labels (data-driven, post-hoc)
3. Confidence calibration (once we trust the numbers)
4. Multiple misconceptions per file
5. Additional models
6. Broader taxonomy
