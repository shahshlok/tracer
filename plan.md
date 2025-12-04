# Plan: ITiCSE-Ready Misconception Detection Framework

**Goal:** Produce a rigorous evaluation framework for LLM-based CS1 misconception detection.

**Contribution:** The hybrid matcher (fuzzy + semantic + topic prior) and evaluation methodology, not the generation approach.

**Scope constraints:** See `future.md` for deferred items (confidence calibration, difficulty labels, multi-misconception files, severity analysis).

---

## Phase 1: Fix Dataset Distribution (Day 1)

### 1.1 Update `generate_manifest()` in `utils/generators/dataset_generator.py`

**Current distribution:**
- 70% seeded, 30% clean
- Avg 2.8 misconceptions/student

**Target distribution:**
- 35% seeded, 65% clean
- Avg 0.8-1.0 misconceptions/student
- **One misconception per file maximum**

**Student breakdown (60 students):**
| Type | Count | Misconceptions | Files |
|------|-------|----------------|-------|
| Perfect students | 24 (40%) | 0 | 96 clean |
| Single-issue | 21 (35%) | 1 | 21 seeded, 63 clean |
| Struggling | 12 (20%) | 2 | 24 seeded, 24 clean |
| Severely struggling | 3 (5%) | 3 | 9 seeded, 3 clean |

**Result:** ~54 seeded files, ~186 clean files (~22% seeded)

**Key constraint:** Each file has AT MOST one seeded misconception. Struggling students have misconceptions spread across different questions, not stacked in one file.

### 1.2 Keep deterministic seeding

Explicit instructions stay (e.g., "DO NOT use Scanner, hardcode values"). This ensures ground truth is reliable.

Add persona-driven **style noise** only:
- "Write as a student who uses inconsistent indentation"
- "Include a debug print statement the student forgot to remove"
- "Use unhelpful variable names like `x` and `temp`"

The misconception is deterministic; the noise is cosmetic.

---

## Phase 2: Simplify Analysis (Day 1-2)

### 2.1 Remove from current analysis:
- **Confidence calibration** (ECE, Brier) - moved to `future.md`
- **Severity field** - not used in metrics
- **Difficulty stratification** - no defensible labels yet

### 2.2 Keep in analysis:
- Precision / Recall / F1 per (strategy, model)
- Bootstrap CIs
- Topic-based breakdown (using existing `category` field from groundtruth)
- Cohen's κ for inter-model agreement
- McNemar's test for strategy comparison
- Hallucination analysis (FP breakdown)
- Topic heatmap

### 2.3 Update `analyze_cli.py`:
- Remove `expected_calibration_error()` and `brier_score()` calls from report generation
- Remove calibration curves visualization
- Keep everything else

---

## Phase 3: Regenerate Dataset (Day 2)

1. `uv run pipeline --students 60 --strategies all --force`
2. Spot-check 10-15 generated files manually to verify misconceptions present
3. Document any files where generation failed

---

## Phase 4: Re-run Analysis and Validate (Day 3)

1. `uv run analyze`
2. Review metrics - expect:
   - Lower overall recall (fewer seeded files)
   - Higher precision (fewer FPs on clean files)
   - Topic-based variation in recall
3. Validate that clean files don't generate excessive FPs (hallucination check)

---

## Phase 5: Update Report for ITiCSE Framing (Day 3-4)

### Key sections to emphasize:

1. **Contribution:** "We present an evaluation framework for LLM-based misconception detection, featuring a novel hybrid matcher that combines fuzzy string matching, semantic embeddings, and topic priors."

2. **Methodology:** 
   - Controlled synthetic dataset (acknowledge limitation)
   - Multiple prompting strategies
   - Bootstrap CIs, inter-model agreement (κ), McNemar tests
   - Topic-based analysis

3. **Findings:**
   - Which strategy works best?
   - Which misconception categories (topics) are detectable?
   - Where do LLMs hallucinate?
   - Do models agree on what they detect?

4. **Limitations:**
   - Synthetic data (future: real student submissions)
   - Limited misconception taxonomy (future: broader coverage)
   - Two models only (future: more models, fine-tuned approaches)
   - No confidence calibration analysis (future work)

---

## Files to Modify

| File | Changes |
|------|---------|
| `utils/generators/dataset_generator.py` | New distribution logic (40/35/20/5 split), one misconception per file max |
| `analyze_cli.py` | Remove calibration analysis, keep topic-based metrics |
| `thesis_report.md` | Updated framing, remove calibration section |

---

## Success Criteria

Before submitting to ITiCSE:

- [x] Distribution is realistic (~22% seeded, ~78% clean) ✓ Verified: 22.5% seeded, 77.5% clean
- [x] Perfect students included (40% of cohort) ✓ Verified: 24/60 = 40%
- [x] One misconception per file maximum ✓ Implemented in generate_manifest()
- [ ] Clean file FP rate is reasonable (<20%)
- [ ] Topic-based analysis shows variation
- [ ] Limitations are explicit and honest
- [x] Inter-model agreement (κ) is reported ✓ Already in analyze_cli.py
- [x] Calibration analysis removed (deferred to future.md) ✓ Removed from analyze_cli.py and pipeline.py

---

## Timeline

| Day | Task |
|-----|------|
| 1 | Phase 1: Fix distribution in dataset_generator.py |
| 2 | Phase 2-3: Simplify analysis, regenerate dataset |
| 3 | Phase 4-5: Re-run analysis, update report framing |
| 4 | Buffer / polish |

**Total: ~4 days**

---

## Decision Log

- **Q1 (Validation):** Option D - deterministic misconceptions, persona for style noise only
- **Q2 (Contribution):** Option B - evaluation methodology, not generation
- **Q3 (Scale):** 60 students for now, scale later if needed
- **Q4 (Subtlety):** Keep all misconceptions, report by topic (not difficulty)
- **Q5 (Re-run):** Yes, committed to re-running

---

## Deferred Items

See `future.md` for:
- Confidence calibration analysis
- Difficulty labels for misconceptions
- Multiple misconceptions per file
- Severity analysis

---

## Next Immediate Step

Start with Phase 1: modify `utils/generators/dataset_generator.py` to implement the new distribution.

Ready when you are.

---

## Addendum (Archived)

_The following items were discussed but deferred. See `future.md` for current status._

- Difficulty labeling procedure → deferred (no defensible labels without classroom data)
- Multi-misconception files → deferred (one per file max for tractable analysis)
- Difficulty-integrated statistics → deferred (using topic-based analysis instead)
- Severity analysis → deferred (not used in metrics)
- Confidence calibration → deferred (future work)
- Threats to validity → kept in report, but simplified
