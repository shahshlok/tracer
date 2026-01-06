<coding_guidelines>
# TRACER: Agent Guidance (Paper-Grade)

TRACER is a **synthetic benchmark** for evaluating whether LLMs can diagnose **student mental models** (Notional Machines) when given buggy CS1 code. Do not frame this as “LLMs understand students”; frame it as measurable **alignment signals** under controlled ground truth.

This file is a living contract: anything that makes results easier to “look good” but less defensible should be treated as a bug.

---

## 1) Architecture (Pipeline)

TRACER runs a **Synthetic Injection → Blind Detection → Semantic Alignment** pipeline:

1. **Injection (dataset generation):** inject exactly one misconception per file using IDs in:
   - `data/a1/groundtruth.json`
   - `data/a2/groundtruth.json`
   - `data/a3/groundtruth.json`
2. **Blind diagnosis (detection):** LLM sees code (not answer key) and outputs structured JSON (Pydantic in `pydantic_models/`).
3. **Evaluation (semantic alignment):** map the LLM’s *thinking narrative* to ground truth using embeddings and cosine similarity.

---

## 2) Scientific Non‑Negotiables (These Make It Publishable)

### 2.1 Unit of Analysis (avoid metric inflation)
Report at **file-level**: one decision per unique file instance, not “one row per detection”.

- A “file” is identified by: `(assignment, student, question, strategy, model)`
- A file can contain multiple detected misconceptions; you must collapse to a single outcome per file for P/R/F1.

If you don’t do this, TPs/FPs can be double-counted and the paper gets shredded in review.

### 2.2 Calibration Leakage (dev/test split)
Threshold selection is part of the model. If you choose thresholds on the same data you evaluate on, your numbers are optimistic by construction.

- **Calibrate thresholds on dev only** (e.g., 80% of files)
- **Report metrics on held-out test only** (remaining 20%)
- Persist split metadata (seed, key columns) alongside the run artifacts.

If results are reported without a held-out evaluation, describe them as exploratory and expect reviewer pushback.

### 2.3 Label Leakage (embedding text confound)
Embedding “labels” (misconception names/categories) into the text you embed can create a shortcut: the matcher may align on shared wording rather than the student-thinking narrative.

Policy:
- **Default analysis must be thinking-only** (student belief vs groundtruth explanation+student_thinking).
- Run a **label-included ablation** explicitly, and describe it as a leaky/upper-bound condition.
- If label-included performance changes materially, call it out as a validity threat that you measured.

---

## 3) Matching + Caching Rules (Semantic Only)

This repo is semantic-matching-only. Do not reintroduce fuzzy/hybrid matchers unless explicitly requested.

- Embedding model: OpenAI `text-embedding-3-large`.
- Disk cache: `.embedding_cache/` stores embeddings keyed by hash of `(model + exact text)`.
- Cache expectation:
  - Re-runs with identical embedding texts should reuse the cache.
  - Switching thinking-only ↔ label-included changes embedding texts, so cache hits will be limited (this is expected).

---

## 4) Reporting Requirements (Report.md Must Be “Write-the-Paper Ready”)

Every publication run should output:

1. **Config + provenance**
   - run name, timestamp
   - detection sources (assignments, strategies, models)
   - whether embeddings include label text
   - threshold ranges + selected thresholds
   - dev/test split info (seed, key columns, file counts)
2. **Core metrics**
   - TP/FP/FN + Precision/Recall/F1 with CIs (bootstrap)
   - FP breakdown (`FP_CLEAN`, `FP_WRONG`, `FP_HALLUCINATION`)
3. **RQ-level breakdowns**
   - category recall table
   - per-misconception recall table with N
   - per-strategy and per-model tables
4. **Ensembles**
   - report raw, strategy-ensemble, model-ensemble metrics side-by-side
5. **Threats to validity (must be explicit)**
   - synthetic data limitation
   - one-misconception-per-file simplification
   - same generator model for dataset
   - threshold calibration details
   - label-leakage ablation result

---

## 5) Operational Directives (Hard Constraints)

1. **Do not invent new taxonomies.** Use IDs from `data/*/groundtruth.json`.
2. **Preserve the Pydantic models.** `pydantic_models/` is the instrument; don’t drift the schema casually.
3. **Always include `assignment` in multi-assignment keys** to avoid cross-assignment collisions.
4. **Be honest about what’s measured:** “alignment of narratives via embeddings,” not “true understanding.”
5. **Be blunt in analysis:** if a method creates shortcuts (label leakage) or leakage (tuning on test), it must be disclosed or fixed.

---

## 6) Canonical Commands (uv)

Thinking-only (primary):
- `uv run python analyze.py analyze-publication --run-name codex_iticse`

Label-included (ablation / upper bound):
- `uv run python analyze.py analyze-publication --run-name codex_iticse_labels --include-label-text`
</coding_guidelines>
