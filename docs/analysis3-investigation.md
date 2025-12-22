# Analysis 3: Ensemble Voting Results

_Date: 2025-12-22_  
_Status: **COMPLETE**_  
_Run: `runs/multi/run_analysis3/`_

---

## Executive Summary

Analysis 3 implemented **Ensemble Voting** to address the precision crisis from Analysis 2.2. By requiring at least 2 strategies to agree on a misconception before counting it, we eliminated most hallucinations while maintaining recall.

**Key Result:**
- Precision: **0.313 → 0.649** (+107% improvement)
- Recall: **0.872 → 0.871** (stable, -0.1%)
- F1: **0.461 → 0.744** (+61% improvement)
- False Positives: **4,722 → 1,164** (-75% reduction)

---

## The Problem (Analysis 2.2)

In Analysis 2.2, the **Socratic prompting strategy** produced **1,873 False Positives** versus only **589 True Positives**, yielding a precision of **0.239**—the worst among all four strategies.

The Socratic strategy was identifying **pedagogical concerns** (code style, unnecessary variables) that are *not* in our formal Notional Machine taxonomy, but these were being counted as hallucinations.

---

## The Solution: Ensemble Voting

### Concept

Instead of trusting any single strategy, we require **consensus**:

```
Detection is VALID only if ≥ N strategies agree on the same misconception
for the same (student, question) pair.
```

### Implementation

```python
def apply_ensemble_filter(df, ensemble_threshold=2):
    """
    Group detections by (student, question, matched_id).
    Keep only those with >= ensemble_threshold strategies agreeing.
    """
    agreement_map = {}  # (student, question, matched_id) -> set of strategies
    
    for row in df:
        key = (row.student, row.question, row.matched_id)
        agreement_map[key].add(row.strategy)
    
    validated = {key for key, strategies in agreement_map.items() 
                 if len(strategies) >= ensemble_threshold}
    
    # Filter: keep TPs/FPs only if validated, keep FNs unchanged
    return df[df.key in validated or df.result == "FN"]
```

### Why N=2?

| Threshold | Expected Effect |
|-----------|-----------------|
| N ≥ 2 | Moderate consensus - filters ~75% of hallucinations |
| N ≥ 3 | Strong consensus - very high precision, lower recall |
| N ≥ 4 | Unanimous - too conservative, misses valid detections |

We chose **N ≥ 2** as the optimal balance.

---

## Results

### Before vs After Ensemble Filtering

| Metric | Before (2.2) | After (3.0) | Change |
|--------|--------------|-------------|--------|
| **Precision** | 0.313 | **0.649** | **+107%** |
| **Recall** | 0.872 | 0.871 | -0.1% |
| **F1 Score** | 0.461 | **0.744** | **+61%** |
| True Positives | 2,151 | 2,150 | -1 |
| False Positives | 4,722 | **1,164** | **-75%** |
| False Negatives | 317 | 318 | +1 |

### By Assignment (Complexity Gradient)

| Assignment | Focus | Precision | Recall | F1 |
|------------|-------|-----------|--------|-----|
| **A3** | Arrays/Strings | **0.810** | 0.989 | **0.890** |
| **A2** | Loops/Control | 0.653 | 0.885 | 0.751 |
| **A1** | Variables/Math | 0.499 | 0.728 | 0.592 |

The **Complexity Gradient** is preserved:
- A3 (Surface errors): 0.890 F1
- A1 (Deep state errors): 0.592 F1
- Gap: 0.298 (50% drop)

### By Strategy (Ensemble Effect)

| Strategy | Before Precision | After Precision | Change |
|----------|------------------|-----------------|--------|
| baseline | 0.377 | **0.714** | +89% |
| taxonomy | 0.347 | **0.654** | +88% |
| cot | 0.342 | **0.668** | +95% |
| socratic | 0.239 | **0.584** | +144% |

Socratic saw the **largest improvement** because its unique hallucinations were filtered out by the ensemble.

### By Model

| Model | Precision | Recall | F1 |
|-------|-----------|--------|-----|
| claude-haiku:reasoning | **0.784** | 0.857 | **0.819** |
| gpt-5.2:reasoning | 0.702 | 0.897 | 0.788 |
| gpt-5.2 | 0.690 | 0.895 | 0.779 |
| claude-haiku | 0.697 | 0.813 | 0.751 |
| gemini-3-flash:reasoning | 0.551 | 0.887 | 0.680 |
| gemini-3-flash | 0.531 | 0.877 | 0.661 |

**Claude Haiku with Reasoning** achieved the best overall performance.

---

## How Ensemble Filtering Works

### Before Ensemble (Single Strategy)

```
Student: Anderson_Charles_664944, Q2
Expected: NM_STATE_01 (Spreadsheet View)

Strategy Detections:
┌─────────────────┬──────────────────────────────────────────┐
│ baseline        │ [No detection]                           │
│ cot             │ [No detection]                           │
│ taxonomy        │ [No detection]                           │
│ socratic        │ "Redundant Variable Aliasing" (FP)       │
└─────────────────┴──────────────────────────────────────────┘

Result: 1 FP counted → harms precision
```

### After Ensemble (N≥2)

```
Same student/question:

Agreement on "Redundant Variable Aliasing": 1/4 strategies
Required: ≥2 strategies

Result: Detection REJECTED by ensemble → no FP counted
        File becomes FN (missed the real misconception)
```

**Trade-off:** We lose one TP (the file is now FN), but we eliminate one FP. Since we had way more FPs than TPs, this trade-off dramatically improves precision.

---

## Notional Machine Detection (Hard vs Easy)

| Category | Recall | Difficulty |
|----------|--------|------------|
| The Human Index Machine | 1.000 | Easy |
| The Algebraic Syntax Machine | 1.000 | Easy |
| The Mutable String Machine | 0.988 | Easy |
| The Semantic Bond Machine | 0.984 | Easy |
| The Void Machine | 0.979 | Easy |
| The Anthropomorphic I/O Machine | 0.937 | Easy |
| The Teleological Control Machine | 0.934 | Easy |
| The Independent Switch | 0.745 | Medium |
| The Reactive State Machine | **0.598** | Medium |
| The Fluid Type Machine | **0.403** | **Hard** |

The **Fluid Type Machine** (integer division blindness, narrowing casts) remains the hardest category for LLMs to detect.

---

## Reproducing Analysis 3

```bash
python analyze.py analyze-ensemble \
    --run-name analysis3 \
    --ensemble-threshold 2 \
    --semantic-threshold 0.65 \
    --noise-floor 0.55
```

Output: `runs/multi/run_analysis3/`

---

## Conclusions

1. **Ensemble voting works:** +107% precision with negligible recall loss
2. **Socratic is salvaged:** Its precision improved +144% when filtered by ensemble
3. **Complexity Gradient holds:** A1 remains harder than A3 even with ensemble
4. **Reasoning models win:** Claude Haiku with reasoning achieves 0.819 F1

---

## Next Steps

1. **Thesis Write-up:** These results are publication-ready for ITiCSE/SIGCSE
2. **Ablation Study:** Test N=3 and N=4 ensemble thresholds
3. **Per-Model Ensemble:** What if we require 2+ models (not strategies) to agree?
|---|---|---|
| "Redundant variable aliasing" | NM_STATE_01, NM_TYP_01, etc. | ❌ Not aligned |
| "Atomic operation belief" | NM_TYP_01, NM_FLOW_01, etc. | ⚠️ Partial overlap |
| "Assignment creates live link" | NM_MEM_01, NM_STATE_01 | ⚠️ Weak alignment |

**Insight:** Socratic models are operating in a **Code Quality** domain, not a **Mental Model** domain.

#### Problem 2: Semantic Drift at the Threshold

Looking at False Positives with scores between 0.55–0.65:

```
FP Score Distribution (Socratic Strategy):
┌─────────────────────────────────────────┐
│ Count                                   │
│     ┌───────┐                           │
│   3 │       │                           │
│   2 │   ┌───────────┐                   │
│   1 │   │       ┌───────────┐           │
│   0 └───┴───────┴───────────┴───────────┘
│       0.55   0.60   0.65   0.70   0.75
│       ^^^^^^^^^^^^^ NOISE FLOOR / THRESHOLD
│
│ Mean TP Score:  0.750 ✓
│ Mean FP Score:  0.628 (very close to 0.65!)
└─────────────────────────────────────────┘
```

Many Socratic FPs cluster *just below* our 0.65 threshold, suggesting:
- The semantic embedding is capturing *something*, but not ground truth
- We're at the boundary where Socratic's "creative pedagogy" looks similar to real misconceptions

#### Problem 3: Strategy-Specific Language Divergence

Different strategies use different language to describe the same misconception:

**Baseline (Direct):**
> "The student reads input in the wrong order, assigning the first number to t instead of v0."

**Socratic (Exploratory):**
> "The student believes the program magically knows which input corresponds to which variable based on the prompt text, ignoring execution order."

**Taxonomic (Formal):**
> "NM_IO_01: Prompt-Logic Mismatch. The student conflates program I/O semantics with natural language reading order."

All three describe the same mental model, but with **different semantic embeddings**, making cross-strategy alignment difficult.

---

## Semantic Pipeline Architecture

### Current Pipeline (Single-Strategy Analysis)

```
┌──────────────────────────────────────────────────────────────┐
│                   DETECTION PIPELINE (Current)                │
└──────────────────────────────────────────────────────────────┘

1. LOAD DETECTION
   ┌─────────────────────────────────────────┐
   │ For strategy (baseline|cot|taxonomy|socratic):
   │   Load JSON: student_Q*.json            │
   │   Extract: misconceptions[] list        │
   └──────────────────┬──────────────────────┘
                      │
                      ▼
2. FILTER NULL DETECTIONS
   ┌─────────────────────────────────────────┐
   │ Two-stage check:                        │
   │   - Keyword match: "no misconception"   │
   │   - Semantic match: vs NULL_TEMPLATES   │
   │   Remove if matched                     │
   └──────────────────┬──────────────────────┘
                      │
                      ▼
3. SEMANTIC MATCHING (Core Logic)
   ┌─────────────────────────────────────────┐
   │ For each detection:                     │
   │   Embed: inferred_category_name +       │
   │           student_thought_process       │
   │   Compare against all ground truth      │
   │   Find best match (cosine similarity)   │
   └──────────────────┬──────────────────────┘
                      │
                      ▼
4. APPLY THRESHOLDS
   ┌─────────────────────────────────────────┐
   │ If score < 0.55: NOISE (filtered)       │
   │ If score < 0.65: FP_HALLUCINATION       │
   │ If matched_id == expected_id: TP        │
   │ Otherwise: FP_WRONG                     │
   └──────────────────┬──────────────────────┘
                      │
                      ▼
5. OUTPUT RESULT
   ┌─────────────────────────────────────────┐
   │ result_type in {TP, FP_CLEAN,           │
   │                 FP_WRONG, FP_HALLUCIN}  │
   │ confidence: semantic_score              │
   └─────────────────────────────────────────┘

PROBLEM: Single-strategy signals are noisy.
         Socratic creates 1,873 FP detections
         that confuse the analysis.
```

### Proposed Ensemble Pipeline

```
┌──────────────────────────────────────────────────────────────┐
│             ENSEMBLE DETECTION PIPELINE (Proposed)            │
└──────────────────────────────────────────────────────────────┘

1. PARALLEL DETECTION (All Strategies)
   ┌─────────────┬──────────────┬──────────────┬──────────────┐
   │  Baseline   │     CoT      │   Taxonomy   │   Socratic   │
   │  Detection  │  Detection   │  Detection   │  Detection   │
   └──────┬──────┴──────┬───────┴──────┬───────┴──────┬───────┘
          │             │               │              │
          ▼             ▼               ▼              ▼
   ┌──────────────────────────────────────────────────────────┐
   │  2. COMPUTE SEMANTIC SCORES (All Strategies)              │
   │     For each (student, question):                         │
   │       - Baseline → semantic_score[]                       │
   │       - CoT → semantic_score[]                            │
   │       - Taxonomy → semantic_score[]                       │
   │       - Socratic → semantic_score[]                       │
   │     Total: ~6 models × 4 strategies = 24 scores/file      │
   └──────────────────┬─────────────────────────────────────┘
                      │
                      ▼
   ┌──────────────────────────────────────────────────────────┐
   │  3. ENSEMBLE AGGREGATION (MAJORITY VOTING)                │
   │     For each detected misconception ID (NM_*)              │
   │       Count agreement across strategies:                  │
   │       if agreement_count >= THRESHOLD (e.g., 2 of 4)     │
   │         → VALIDATE detection (count as TP)               │
   │       else                                                │
   │         → REJECT detection (filter as noise)             │
   │                                                           │
   │     Example:                                              │
   │       NM_STATE_01 detected by: [baseline, taxonomy]      │
   │       Agreement: 2/4 strategies ✓ VALID                  │
   │                                                           │
   │       NM_IO_02 detected by: [socratic] only              │
   │       Agreement: 1/4 strategies ✗ REJECT                │
   └──────────────────┬─────────────────────────────────────┘
                      │
                      ▼
   ┌──────────────────────────────────────────────────────────┐
   │  4. FINAL CLASSIFICATION                                  │
   │     Per (student, question, expected_misconception):      │
   │       - Ensemble agreement on expected_id → TP            │
   │       - Ensemble agreement on wrong_id → FP_WRONG         │
   │       - No ensemble agreement on clean file → PASS        │
   │       - Ensemble rejects all detections on dirty file     │
   │         → FN                                              │
   └──────────────────┬─────────────────────────────────────┘
                      │
                      ▼
   ┌──────────────────────────────────────────────────────────┐
   │  5. METRICS (Ensemble Mode)                               │
   │     Precision: TP / (TP + FP)  [Higher, fewer hallucins] │
   │     Recall:    TP / (TP + FN)  [May lower, validation]   │
   │     F1: Balanced score                                    │
   │                                                           │
   │     Expected outcome:                                     │
   │     - Precision: 0.313 → 0.50+ (eliminate 1-off errors)  │
   │     - Recall:    0.872 → 0.75+ (some valid detections)   │
   └──────────────────────────────────────────────────────────┘
```

---

## Ensemble Voting: Detailed Logic

### Vote Aggregation Rules

**Definition: An "Agreement" requires N strategies to detect the same misconception ID for the same student/question.**

#### Voting Thresholds

| Threshold | Rule | Expected Effect |
|-----------|------|---|
| **N ≥ 2 of 4** | "Moderate Consensus" | Filter ~70% of Socratic hallucinations, keep most valid detections |
| **N ≥ 3 of 4** | "Strong Consensus" | Filter ~85% of hallucinations, but may miss valid single-strategy detections |
| **N ≥ 4 of 4** | "Unanimous" | Extremely conservative; high precision, very low recall |

**Recommended: Start with N ≥ 2** (Moderate Consensus).

---

### Case Study: Anderson_Charles_664944_Q2

**Expected Ground Truth:** `NM_STATE_01` (Spreadsheet View)

**Raw Strategy Detections:**

```
┌─────────────────┬──────────────────────────────────────────┐
│    Strategy     │     Detected Misconception ID             │
├─────────────────┼──────────────────────────────────────────┤
│ baseline        │ [No detection] (empty list)              │
│ cot             │ [No detection] (empty list)              │
│ taxonomy        │ [No detection] (empty list)              │
│ socratic        │ "Redundant Variable Aliasing"            │
│                 │ → Semantic match: None (score 0.58)      │
│                 │ → FP_HALLUCINATION                       │
└─────────────────┴──────────────────────────────────────────┘
```

**Ensemble Voting (N ≥ 2):**
```
Agreements on NM_STATE_01:  0/4 strategies ✗
Agreements on NM_* (any):   0/4 strategies ✗

Socratic's "Redundant Aliasing" detection:
  - Matches no ground truth ID
  - Only 1 strategy detected it
  - REJECTED by ensemble

Result: FN (missed the Spreadsheet View misconception)
        (No false hallucination counted)
```

**Why This Matters:**
- Single-Strategy: +1 FP_HALLUCINATION (harms precision)
- Ensemble: FN (doesn't harm precision, but harms recall)
- **Net benefit:** Precision improves significantly.

---

## Semantic Score Analysis: Socratic vs. Baseline

### Distribution Comparison

```
TRUE POSITIVES (TP) - Both Strategies
┌──────────────────────────────────────────────────────┐
│ Baseline TP Mean Score:  0.765    Std: 0.058        │
│ Socratic TP Mean Score:  0.742    Std: 0.065        │
│                                                      │
│ ✓ Both strategies achieve high TP scores            │
│   (LLM correctly identified the misconception)      │
└──────────────────────────────────────────────────────┘

FALSE POSITIVES (FP) - Divergence
┌──────────────────────────────────────────────────────┐
│ Baseline FP Mean Score:  0.598    Std: 0.045        │
│ Socratic FP Mean Score:  0.642    Std: 0.052        │
│                                                      │
│ ⚠️ Socratic FPs score HIGHER than Baseline FPs      │
│    This means Socratic's hallucinations are          │
│    "semantically plausible" but wrong.              │
│                                                      │
│ Interpretation: Socratic models are generating       │
│ creative explanations that *sound* like real         │
│ misconceptions but don't match our taxonomy.        │
└──────────────────────────────────────────────────────┘

Distribution Plots:
┌────────────────────────────────────┐
│ BASELINE (TP vs FP)                │
│  0.5  0.6  0.7  0.8  0.9           │
│  ✓ Clear separation                │
│    FP: ███ (0.598)                 │
│    TP:      ███████ (0.765)        │
└────────────────────────────────────┘

┌────────────────────────────────────┐
│ SOCRATIC (TP vs FP)                │
│  0.5  0.6  0.7  0.8  0.9           │
│  ⚠️ Overlap!                        │
│    FP:    █████ (0.642)            │
│    TP:       ██████ (0.742)        │
└────────────────────────────────────┘
```

---

## Implementation Roadmap: Ensemble Mode

### Step 1: Refactor `build_results_df` to Support Ensemble

**Current behavior:**
- Process each strategy independently
- Return a flat DataFrame with all detections

**Proposed behavior:**
- If `ensemble_mode=True`:
  - Group detections by `(student, question, matched_id)`
  - Count strategies that agree on each misconception
  - Only include detections with `agreement_count >= ensemble_threshold`
  - Recalculate TP/FP/FN based on ensemble votes

**Pseudo-code:**
```python
def ensemble_filter_detections(
    detections_by_strategy: dict[str, list[Detection]],
    ensemble_threshold: int = 2,
) -> list[Detection]:
    """
    Group detections by (student, question, matched_id).
    Keep only those with >= ensemble_threshold strategies agreeing.
    """
    agreement_map = defaultdict(set)
    
    # Count which strategies detected each misconception
    for strategy, detections in detections_by_strategy.items():
        for det in detections:
            key = (det['student'], det['question'], det['matched_id'])
            agreement_map[key].add(strategy)
    
    # Filter: keep only high-agreement detections
    validated = []
    for (student, question, matched_id), strategies in agreement_map.items():
        if len(strategies) >= ensemble_threshold:
            # Create ensemble detection
            validated.append({
                'student': student,
                'question': question,
                'matched_id': matched_id,
                'agreeing_strategies': list(strategies),
                'agreement_count': len(strategies),
            })
    
    return validated
```

### Step 2: Add CLI Flag

```bash
python analyze.py --ensemble --ensemble-threshold 2
```

### Step 3: Metrics Comparison (Expected Results)

| Metric | Single-Strategy (Current) | Ensemble (N≥2) | Ensemble (N≥3) |
|--------|---------------------------|---|---|
| **Precision** | 0.313 | **0.48–0.55** | **0.60–0.68** |
| **Recall** | 0.872 | **0.75–0.82** | **0.65–0.75** |
| **F1 Score** | 0.461 | **0.59–0.61** | **0.63–0.69** |
| **Total TP** | 2,151 | ~1,800 | ~1,500 |
| **Total FP** | 4,722 | **~1,800** | **~900** |

**Rationale:**
- Socratic contributes ~1,873 FPs
- With N ≥ 2 ensemble voting, we filter most of Socratic's unique hallucinations
- Baseline/CoT/Taxonomy maintain agreement on real misconceptions

---

## Socratic Strategy: Recommendations

### Option A: Fine-Tune Socratic Prompt (Expensive)

Modify the Socratic prompt template to align more closely with Notional Machine theory:

**Current Socratic Prompt:**
> "You are a CS education expert. Diagnose the student's mental model by asking exploratory questions and identifying gaps in their understanding."

**Revised Prompt:**
> "You are a CS education expert familiar with Notional Machines—mental models of how programs execute. Identify misconceptions about Variables, I/O, Type Systems, Control Flow, Memory, and APIs. Be specific and cite the theoretical framework."

**Expected effect:**
- Socratic models generate explanations more aligned with Ground Truth language
- Semantic scores improve from ~0.58 to ~0.65+
- FP hallucinations decrease

**Cost:** Re-run all 300 students × 6 models × 4 strategies = 7,200 API calls

---

### Option B: Use Socratic in Ensemble Only (Recommended)

Keep Socratic as-is, but only trust it when it **agrees with other strategies**.

**Rationale:**
- Socratic is valuable for edge cases (creative pedagogy)
- When it agrees with Baseline/CoT, it's likely correct
- Eliminates the precision hit

**Cost:** Negligible (no re-runs needed)

---

## Complexity Gradient Observation

The report reveals a **clear complexity degradation** from A3 to A1:

```
Performance by Assignment (Current Analysis 2.2):

┌────────────────────────────────────────┐
│ Precision by Assignment Complexity     │
│                                        │
│ A3 (Arrays)     ████░░░░░ 0.455       │
│ A2 (Loops)      ██████░░░ 0.345       │
│ A1 (Variables)  ██░░░░░░░ 0.197       │
│                                        │
│ Δ(A3 - A1) = 0.258 (131% drop!)       │
└────────────────────────────────────────┘
```

**Interpretation:**
- LLMs are excellent at detecting **Surface Errors** (Syntax, Type, Indexing)
- LLMs are poor at detecting **Deep State Errors** (Mental Models of Variables)
- This aligns with **RQ1** (Complexity Gradient)

**With Ensemble voting:**
```
Expected Precision by Assignment (Projected):

┌────────────────────────────────────────┐
│ Ensemble N≥2                           │
│                                        │
│ A3 (Arrays)     ███████░░ 0.68        │
│ A2 (Loops)      ██████░░░ 0.58        │
│ A1 (Variables)  ████░░░░░ 0.42        │
│                                        │
│ Δ(A3 - A1) = 0.26 (same gap,          │
│              but higher absolute)     │
└────────────────────────────────────────┘
```

---

## Next Steps

1. **Implement Ensemble Mode** in `analyze.py` (TODO #4)
2. **Run Analysis 3** with `--ensemble --ensemble-threshold 2`
3. **Compare metrics** to Analysis 2.2
4. **Generate updated report** with ensemble results
5. **Visualize complexity gradient** with new charts

---

## References

- `AGENTS.md` – Core architecture and data hierarchy
- `analyze.py` – Current single-strategy pipeline
- `utils/statistics.py` – Bootstrap and statistical tests
- `data/*/groundtruth.json` – Ground truth definitions
- `runs/multi/run_analysis2.2/report.md` – Current (Analysis 2.2) results
