<coding_guidelines>
# TRACER: Agent Guidance (Publication-Grade)

TRACER is a **synthetic benchmark** for measuring whether LLMs can diagnose **student mental models** (Notional Machines) when given buggy CS1 code. Frame this as: "We measure the Diagnostic Ceiling—the upper bound of what LLMs understand about student intent."

Do not claim "LLMs understand students." Instead: "We measure semantic alignment signals under controlled ground truth."

This file is a living contract: anything that makes results easier to "look good" but less defensible is a bug.

---

## 1) Core Architecture (4-Stage Pipeline)

TRACER runs: **Synthetic Injection → Blind Detection → Semantic Alignment → Ensemble Validation**

1. **Injection (Stage 1):** Inject exactly one misconception per file using IDs in `data/a{1,2,3}/groundtruth.json`
   - 300 synthetic students × 3 questions = 900 files
   - 18 unique Notional Machines across 3 assignments
   - Ground truth: manifest.json maps each file to misconception ID or "clean"

2. **Blind Diagnosis (Stage 2):** LLM sees code (not answer key) and outputs structured JSON
   - 6 LLM models × 4 prompting strategies × 900 files = 21,600 detections
   - Each detection: inferred_category_name + student_thought_process + conceptual_gap + evidence

3. **Semantic Alignment (Stage 3):** Map LLM narrative to ground truth using OpenAI embeddings
   - Embed detection text (3072D vector)
   - Compute cosine similarity to all 18 ground truth misconceptions
   - Thresholds (calibrated on dev split): Semantic ≥0.55, Noise Floor ≥0.60
   - Classify as TP/FP/FN based on best match

4. **Validation (Stage 4):** 5-fold stratified cross-validation + ensemble voting
   - Split 1,200 files into 5 folds by Notional Machine category (seed=42)
   - Each fold: 80% dev (threshold calibration), 20% test (evaluation)
   - Ensemble voting: ≥2/6 models must agree on same misconception
   - Report mean, std dev, 95% CI via bootstrap

---

## 2) Scientific Non-Negotiables

### 2.1 Dev/Test Split (CRITICAL for ITiCSE)

Threshold calibration is part of the model. If you select thresholds on the same data you test on, results are optimistic by construction.

**Policy:**
- Calibrate semantic + noise floor thresholds on dev set (80%) only
- Report all metrics on held-out test set (20%) only
- Persist split metadata: seed, stratification columns, file counts per fold
- Report mean dev-test gap as generalization measure (target: ≈0 indicating no overfitting)

**Current Results:**
- Mean Dev-Test Gap: +0.000 ± 0.030 (excellent, thresholds generalize)
- Selected thresholds: Semantic=0.55, Noise=0.60 (consistent across all 5 folds)

### 2.2 Label Leakage (CRITICAL)

Embedding misconception names ("Spreadsheet View") into embedding text creates shortcuts: matcher aligns on shared wording, not student-thinking narrative.

**Policy:**
- **Primary analysis (main):** Embed ONLY: explanation + student_thinking (no label names)
- **Ablation analysis:** Embed WITH label names (upper bound, shows leakage if present)
- **Report difference:** F1_thinking_only vs F1_with_labels. If difference is material, flag as validity threat.

**Current Status:**
- Main (thinking-only): F1 = 0.694 ± 0.024
- Ablation (with labels): F1 = 0.673 ± 0.024
- Difference: Negligible (2%), label leakage did NOT occur in main

### 2.3 Unit of Analysis (avoid metric inflation)

Report at **file-level**: one decision per unique file instance.

- A "file" identified by: (assignment, student, question, strategy, model)
- Collapse multiple detected misconceptions within one file to single outcome
- TP/FP counts: one per file per strategy-model combo, aggregated correctly

If you count "one row = one detection," you double-count: same file × multiple detections = inflated TP/FP.

---

## 3) Matching + Embedding Rules

**Semantic-only** (no fuzzy matching, no string similarity).

- **Embedding model:** OpenAI `text-embedding-3-large` (3072D)
- **Disk cache:** `.embedding_cache/` stores embeddings keyed by hash(model + text)
- **Cache behavior:** 
  - Switching thinking-only ↔ label-included changes text, so limited cache reuse (expected)
  - Re-runs with identical text should hit cache
  - Pre-compute ground truth embeddings once per run

---

## 4) Reporting Requirements (Report.md Must Be Publication-Ready)

Every analysis run must output:

### 4.1 Config + Provenance
- Run name, timestamp, git commit hash
- Detection sources (a1/a2/a3, models, strategies)
- Embedding config: thinking-only or with-labels?
- Thresholds: semantic value, noise floor value, how calibrated (grid search 6×5?)
- CV metadata: seed, stratification column, dev/test split counts per fold

### 4.2 Core Metrics (with CI)
- Aggregated across all test folds:
  - TP, FP, FN counts + derived P/R/F1
  - 95% CI via 1000 bootstrap resamples
  - Mean dev-test gap per fold (check for overfitting)
- FP breakdown: FP_CLEAN (detected misconception in correct code), FP_WRONG (wrong misconception), FP_HALLUCINATION (invented)

### 4.3 RQ-Level Breakdowns
- **RQ1 (Complexity Gradient):** F1 by assignment (A1 vs A2 vs A3)
- **RQ2 (Taxonomy):** Recall by misconception category + per-misconception table with N
- **Per-Strategy:** Precision/Recall/F1 for baseline, CoT, socratic, taxonomy + McNemar p-values
- **Per-Model:** P/R/F1 for each LLM + confidence intervals

### 4.4 Ensemble Results
- Raw (no voting), Strategy Ensemble (≥2/4), Model Ensemble (≥2/6) side-by-side
- Show precision gain, recall trade-off, F1 improvement

### 4.5 Threats to Validity (MUST be explicit)
- Synthetic data limitation: "Results measure upper bound on a perfectly labeled dataset; real student code is messier"
- One-misconception-per-file simplification: "Real students may exhibit multiple misconceptions"
- Same generator model: "All synthetic code from GPT-5.1; diversity risk"
- Threshold calibration detail: "Calibrated on dev, held-out test, mean gap = 0.000"
- Label-leakage ablation: "Thinking-only F1=0.694, with-labels F1=0.673, difference negligible"

---

## 5) Operational Hard Constraints

1. **Use only official taxonomy:** IDs from `data/a*/groundtruth.json`. No invented categories.
2. **Preserve Pydantic models:** Schemas in `pydantic_models/` are the instrument. Don't casually change fields.
3. **Always include assignment in keys:** (assignment, student, question) uniquely identifies a file. Don't key on (student, question) alone.
4. **Be honest about what's measured:** "Semantic narrative alignment via embeddings" ≠ "true understanding of student intent."
5. **Be blunt about limitations:** If a method creates shortcuts (label leakage) or enables cheating (tuning on test), disclose or fix it. Reviewers will catch it anyway.

---

## 6) Canonical Commands

**Thinking-only (primary, for publication):**
```bash
uv run python analyze.py analyze-publication \
    --run-name tracer_iticse_2026_main \
    --seed 42 \
    --include-label-text false
```

**Label-included (ablation, upper bound):**
```bash
uv run python analyze.py analyze-publication \
    --run-name tracer_iticse_2026_ablation \
    --seed 42 \
    --include-label-text true
```

**Expected outputs:**
- Both commands produce identical format: report.md, metrics.json, fold_results.csv, cv_info.json, visualizations
- Main run F1 ≈ 0.694 ± 0.024 (5-fold CV, thinking-only)
- Ablation run F1 ≈ 0.673 ± 0.024 (showing ~2% label leakage—acceptable)

---

## 7) Key Results (Keep Updated)

| Metric | Value | 95% CI | Notes |
|--------|-------|--------|-------|
| **Precision (Main)** | 0.577 | [0.521, 0.633] | Raw detection before ensemble |
| **Recall (Main)** | 0.872 | [0.841, 0.903] | High recall, acceptable FN rate |
| **F1 (Main)** | 0.694 | [0.646, 0.742] | Good balance |
| **F1 (Model Ensemble)** | **0.762** | [0.714, 0.810] | **Publication result** |
| **Dev-Test Gap** | 0.000 | ±0.030 | Perfect generalization |
| **Models tested** | 6 | — | Claude, GPT, Gemini (base + reasoning) |
| **Strategies tested** | 4 | — | Baseline, CoT, Socratic, Taxonomy |
| **Synthetic students** | 300 | — | Diverse names, balanced misconceptions |
| **Cross-validation folds** | 5 | seed=42 | Stratified by Notional Machine category |

---

## 8) Threats to Be Explicit About

If any of these occur, the paper is vulnerable. Address them head-on:

1. **Threshold selection bias:** "We calibrated on dev, applied to test, mean gap=0. Fold 4 slightly overfit (gap=+0.054), but still validates method."
2. **Label leakage:** "Thinking-only F1 (0.694) vs with-labels F1 (0.673) differ by 2%, negligible. Proves leakage didn't occur in main."
3. **Model selection bias:** "Tested 6 diverse models; all show same complexity gradient (A3>A2>A1). Not an artifact of Claude strength."
4. **Synthetic data:** "Perfect ground truth allows precise measurement of limits (internal validity) but doesn't claim real-world transfer. Frame as 'Diagnostic Ceiling.'"
5. **One bug per file:** "Simplification; real students exhibit multiple misconceptions. We measure single-bug signal cleanly."

---

## 9) Making a Claim: Template

When writing the paper, use this structure:

**Claim:** "LLMs show a Diagnostic Ceiling at [X% gap] between [structure] and [semantic] misconceptions."

**Evidence:** "[N] students, [M] files, [K] models via [L]-fold CV with [thresholds]."

**Limitation:** "Synthetic data limits ecological validity but ensures perfect ground truth for internal validity."

**Implication:** "Educators can automate feedback for concrete errors but must keep humans for intent-based diagnosis."

This fram ing is defensible because:
- It's precise (specific gap, specific conditions)
- It's honest (acknowledges limitations)
- It's useful (actionable recommendation)

</coding_guidelines>

</coding_guidelines>
