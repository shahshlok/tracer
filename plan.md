# Plan: Matcher Ablation – Fuzzy vs Semantic vs Hybrid

**Goal:** Compare three misconception-matching strategies on the *same* dataset and analysis pipeline so we can make a clear, defensible claim about whether our hybrid matcher actually adds value beyond simpler baselines.

**Matchers to compare**
- `fuzzy_only` – uses only string-based similarity on names/descriptions (current `fuzzy_match_misconception`).
- `semantic_only` – uses only embedding-based semantic similarity on detection/ground-truth texts.
- `hybrid` – current fusion of fuzzy + semantic + topic prior.

Everything else (dataset, manifest, ground truth, detections, metrics) remains fixed while we vary the matcher.

---

## Phase 0 – Fix and Freeze the Experimental Ground

Before ablation, we assume (or enforce) that:

- Dataset distribution is stable and realistic: 60 students × 4 questions, ~20–25% seeded files, **max one injected misconception per file**.
- `uv run pipeline` completes end-to-end and produces coherent detection JSONs and a working `thesis_report.md` for the hybrid matcher.
- The analysis code can already compute:
  - Precision/recall/F1 per (strategy, model) with bootstrap CIs.
  - Topic-wise recall.
  - Hallucination patterns.
  - Agreement (κ, McNemar) for model comparisons.

We do not change dataset generation or prompting while doing matcher ablation.

---

## Phase 1 – Wire Match Modes into the Analyzer

**Objective:** Allow the analysis to run in `fuzzy_only`, `semantic_only`, and `hybrid` modes with identical downstream evaluation.

1. **Define match modes**
   - Introduce a small enum/constant set: `match_mode ∈ {"fuzzy_only", "semantic_only", "hybrid"}`.
   - Implement a dispatcher that, given a detection and `match_mode`, returns `(matched_id, match_score, detail)` by calling:
     - `fuzzy_match_misconception` for `fuzzy_only`.
     - `semantic_match_misconception` for `semantic_only`.
     - `hybrid_match_misconception` for `hybrid`.

2. **Keep evaluation logic identical**
   - TP/FP/FN classification must be the same logic for all modes:
     - Same notion of “expected ID”, seeded vs clean, hallucination vs interesting.
   - Only the way we obtain `matched_id` and `match_score` changes between modes.

3. **CLI interface**
   - Add an option to `uv run analyze`, e.g. `--match-mode`:
     - Values: `fuzzy_only`, `semantic_only`, `hybrid`, or `all`.
   - In `match-mode=all`, run all three matchers in one invocation and carry forward an explicit `match_mode` column in the resulting dataframe(s).

---

## Phase 2 – Compute Metrics per Matcher

**Objective:** For each matcher, compute the same metrics so we can compare them apples-to-apples.

For each `match_mode`:

1. **Global metrics by strategy × model**
   - TP, FP, FN.
   - Precision, recall, F1.
   - Bootstrap confidence intervals for precision/recall/F1 using (student, question) as the resampling unit (same procedure as current hybrid).

2. **Topic-wise recall**
   - For each topic and (strategy, model, match_mode), compute recall over opportunities.
   - This shows whether certain matchers are better/worse on specific conceptual areas (Input, State, Data Types, etc.).

3. **Hallucination profile**
   - For each matcher, compute:
     - Total FP count.
     - Top hallucinated misconception names and their counts.
   - This allows statements like “semantic-only hallucinates fewer ‘Input’ misconceptions but more ‘Data Types’ ones” or vice versa.

4. **(Optional) Model agreement within a matcher**
   - Keep κ/McNemar focused on model comparisons within each matcher.
   - Cross-matcher agreement (e.g., hybrid vs fuzzy on the same model) can be explored later if it turns out to be interesting.

All metrics should be stored in a way that we can filter/slice by `match_mode` in code and in the JSON export.

---

## Phase 3 – Extend the Report with Matcher Ablation

**Objective:** Integrate matcher comparisons into `thesis_report.md` so a reader can see the impact without reading code.

1. **New section: “Matcher Ablation: Fuzzy vs Semantic vs Hybrid”**
   - Add a table summarizing overall performance:
     - Columns: `Matcher`, `Strategy`, `Model`, `TP`, `FP`, `FN`, `Precision`, `Recall`, `F1`, and CIs.
   - Optionally include a small figure (e.g., precision vs recall scatter) with points colored by `match_mode`.

2. **Matcher-level narrative**
   - Add bullets such as:
     - “Hybrid improves recall on Input and State / Variables by X–Y percentage points over fuzzy-only, with only a modest increase in FPs.”
     - “Semantic-only underperforms on short, idiosyncratic misconception names where fuzzy matching excels,” or vice versa depending on results.
   - If hybrid is not clearly superior, be honest and adjust our framing (“hybrid behaves similarly”; “benefits are modest but present on specific topics”).

3. **Topic-by-matcher comparison**
   - Either:
     - Add a topic × matcher × model recall heatmap, or
     - Present a smaller table for key topics (Input, State / Variables, Data Types) showing recall per matcher.
   - Use this to argue about where hybrid helps (or fails) conceptually, not just numerically.

---

## Phase 4 – Qualitative Case Studies

**Objective:** Show concrete examples where matchers behave differently and why that matters.

1. **Select representative cases**
   - Identify 2–3 interesting detection instances where:
     - Fuzzy-only mislabels but semantic-only/hybrid get it right.
     - Semantic-only fails but fuzzy-only/hybrid succeed.
     - Hybrid chooses the correct ID where fuzzy and semantic disagree, aided by topic prior or blended score.

2. **Document each case**
   - For each:
     - Note student ID (or pseudonym), question, injected misconception ID and name.
     - Include detection text (name/description/student belief).
     - Show matcher outputs: `(matched_id, score)` for fuzzy-only, semantic-only, hybrid.
   - Add 1–2 sentences explaining why hybrid behaves differently and what that tells us about the underlying representations (string vs semantics vs topic priors).

Placed in the report (or paper) these act as “mini vignettes” that make the quantitative differences human-readable.

---

## Phase 5 – Interpret Results and Refine Contribution

**Objective:** Use the ablation results to sharpen what we claim in the thesis/paper.

1. **Refine our main claim about the matcher**
   - If hybrid clearly improves recall (especially on hard topics) with acceptable FP cost:
     - Emphasize hybrid as a substantive methodological contribution.
   - If gains are marginal or inconsistent:
     - Reframe: our contribution becomes more about the **evaluation framework and experimental setup** than about the hybrid heuristic itself.

2. **Position relative to future work**
   - Based on what we learn, decide how much energy to invest in:
     - More sophisticated hybridization (e.g., learned weights).
     - Difficulty labels and multi-misconception files (`future.md`).
     - Real student data collection and model fine-tuning.

3. **Update ITiCSE narrative**
   - Ensure the matcher ablation results are clearly visible in:
     - Abstract (one sentence).
     - Results section (a table + figure + interpretation).
     - Discussion (how this informs using LLMs for misconception detection).

---

## Success Criteria

We consider this ablation plan successful when:

- [ ] `uv run analyze --match-mode all` (or equivalent) runs end-to-end and produces metrics and JSON that include `match_mode` as a first-class dimension.
- [ ] `thesis_report.md` has a clear “Matcher Ablation” section with:
  - A comparative table across `fuzzy_only`, `semantic_only`, and `hybrid`.
  - At least one figure (e.g., precision–recall scatter) contrasting matchers.
  - A short narrative highlighting where hybrid helps or does not.
- [ ] At least 2–3 qualitative case studies are documented showing distinct matcher behavior.
- [ ] We can state, in one or two precise sentences, how hybrid compares to fuzzy-only and semantic-only (e.g., “Hybrid improves recall on Input/State misconceptions by ~X points with similar precision”), or explicitly acknowledge that it does *not* outperform simpler baselines and adjust our claimed contribution accordingly.
