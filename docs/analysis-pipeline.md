# Analysis Pipeline: From Injection to Ensemble Voting

**Purpose:** This document traces the complete data flow through the ensemble evaluation system, from synthetic bug injection through semantic alignment to ensemble voting.

---

## Overview: The 4-Stage Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│ STAGE 1: INJECTION     │ STAGE 2: DETECTION │ STAGE 3: MATCHING │ STAGE 4: ENSEMBLE
├─────────────────────────────────────────────────────────────────┤
│ Generate buggy code    │ Run LLMs with 4    │ Semantic embed &  │ Vote on consensus
│ from ground truth      │ prompt strategies  │ cosine similarity │ across strategies
└─────────────────────────────────────────────────────────────────┘
```

---

## Stage 1: Synthetic Injection (Dataset Generation)

### Input Data
- **Location:** `data/{a1,a2,a3}/groundtruth.json`
- **Structure:** 10 Notional Machine categories with 50+ misconceptions each
- **Key Fields per Misconception:**
  - `id`: Unique identifier (e.g., `NM_STATE_REACTIVE`)
  - `category`: Human-readable name (e.g., "Reactive State Machine")
  - `explanation`: Ground truth description of the misconception
  - `student_thinking`: Narrative of what the student believes
  - `code_pattern`: Template showing the buggy code

### Generation Process
**File:** `utils/generators/dataset_generator.py`

1. **Load ground truth:** Parse `groundtruth.json` to extract all misconception definitions
2. **Select target misconception:** Choose one `id` per student submission (clean constraint)
3. **Inject bug:** Modify a valid solution using `code_pattern` to introduce the misconception
4. **Save output:** Write to `authentic_seeded/{assignment}/{student_id}/Q{question_num}.py`

### Output Dataset
- **Location:** `authentic_seeded/{a1,a2,a3}/`
- **Scale:** 120 students × 3 questions = 360 files per assignment
- **Composition:** 40% clean (no bugs), 60% seeded with one misconception each
- **Property:** Each file has exactly one labeled misconception (or none)

**Rationale:** Clean labeling ensures we measure detection accuracy, not annotation quality.

---

## Stage 2: Blind Detection (LLM Analysis)

### LLM Models (6 variants)
1. GPT-5.2 (base model)
2. GPT-5.2 Reasoning (extended reasoning)
3. Claude Haiku (base)
4. Claude Haiku Reasoning (extended)
5. Gemini Flash (base)
6. Gemini Flash Reasoning (extended)

### Prompt Strategies (4 variants)
Each strategy is a different way of asking the LLM to diagnose the code:

#### 1. **Baseline**
- Simple, direct prompt: "Find the bug in this code."
- Expects open-ended analysis
- **Purpose:** Baseline cognitive ability without guidance

#### 2. **Taxonomy**
- Provides full list of 10 categories from ground truth
- Example: "Is this: (a) Reactive State, (b) Teleological Flow, (c) Spatial Adjacency?"
- **Purpose:** Measures ability to recognize given definitions

#### 3. **Chain-of-Thought (CoT)**
- Prompts step-by-step execution tracing
- "Trace the state at each line... what changes?"
- **Purpose:** Measures ability to reason through program state

#### 4. **Socratic**
- Asks probing questions about student's mental model
- "What does the student think happens when...?"
- **Purpose:** Measures ability to infer misconceptions via dialogue

### Detection Output Schema
**File:** `pydantic_models/evaluation.py` → `DetectionResult`

Each LLM output is normalized to:
```json
{
  "inferred_category_name": "string",        // Open-ended LLM answer
  "student_thought_process": "string",       // Narrative of misconception
  "conceptual_gap": "string",                // Explanation of the gap
  "evidence": [line_numbers],                // Code lines supporting diagnosis
  "confidence": 0.0-1.0,                     // LLM's confidence
  "is_clean": boolean                        // Did LLM find a bug (or say code is clean)?
}
```

### Detection Execution
**File:** `llm_miscons_cli.py` (deprecated) → **New:** `analyze.py` (unified interface)

```bash
python analyze.py detect \
  --assignment a1 \
  --strategy baseline \
  --model gpt-5.2 \
  --output-dir detections/a1_multi/baseline/
```

### Output Structure
- **Location:** `detections/{a1,a2,a3}_multi/{strategy}/`
- **Files:** One JSON per student question per model
  - `student_Q1.json` (contains results from all 6 models)
- **Scale:** 360 files × 4 strategies × 6 models = 8,640 total detections

---

## Stage 3: Semantic Alignment (Matching)

### Challenge: The Terminology Problem
The LLM might say:
- **"Auto-Update Error"** (its terminology)
- **"Reactive State Machine"** (ground truth category)

Both refer to the same misconception. Keyword matching would fail.

**Solution:** Semantic embeddings + cosine similarity

### Matching Pipeline
**File:** `utils/matching/semantic.py`

#### Step 1: Load Detections and Ground Truth
```python
# Detection (from LLM output)
detection_text = detection_result["student_thought_process"]

# Ground truth (from data/groundtruth.json)
groundtruth_text = groundtruth[misconception_id]["explanation"]
```

#### Step 2: Embed Both Texts
Using OpenAI `text-embedding-3-large` (1536 dimensions):
```
detection_vector = embed(detection_text)          # 1536D
groundtruth_vector = embed(groundtruth_text)      # 1536D
```

#### Step 3: Compute Cosine Similarity
```
similarity = cosine(detection_vector, groundtruth_vector)
# Range: -1.0 to 1.0 (typically 0.0 to 1.0 for text)
```

#### Step 4: Classify Result
**Threshold:** 0.65 (configurable)

If `similarity > threshold`:
- **True Positive (TP):** LLM detected the actual misconception
- Detection matches the ground truth misconception

Else (or if detection contradicts ground truth):
- **False Positive (FP_HALLUCIN):** LLM invented a misconception
- **FP_CLEAN:** LLM missed the bug (said code is clean when it's not)

For clean files (ground truth has no bug):
- **True Negative (TN):** LLM correctly says code is clean
- **False Positive (FP):** LLM falsely flags a non-existent bug

### Matching Execution
**File:** `analyze.py`

```bash
python analyze.py match \
  --assignment a1 \
  --strategy baseline \
  --output-dir runs/multi/run_analysis_baseline/
```

### Output: Single-Strategy Metrics
- **Location:** `runs/multi/run_analysis_{strategy}/`
- **Files:**
  - `data.json` → All matched detections with similarity scores
  - `report.md` → Human-readable analysis
  - `metrics.json` → Precision, Recall, F1, false positive count

**Example Results (A1 + Baseline Strategy):**
```
Precision:  0.313  (31% of detections were correct)
Recall:     0.872  (87% of actual bugs were caught)
F1:         0.461  (harmonic mean)
False Positives: 4,722 (many hallucinations)
```

---

## Stage 4: Ensemble Voting (Consensus Filtering)

### Problem: High False Positives
Single strategies are prone to hallucinations. The baseline strategy detected 4,722 false positives while only catching 87% of real bugs.

### Solution: Majority Consensus
Require at least 2 strategies to agree on a detection before counting it as valid.

### Ensemble Logic
**File:** `analyze.py` (ensemble-voting mode)

For each unique (student, question, misconception_id) triple:
1. **Count agreement:** How many of the 4 strategies detected this?
   - Baseline: detected? (yes/no)
   - Taxonomy: detected? (yes/no)
   - CoT: detected? (yes/no)
   - Socratic: detected? (yes/no)

2. **Apply voting rule:**
   ```python
   if agreement_count >= 2:
       VALIDATE(detection)  # Count this result
   else:
       REJECT(detection)    # Filter as hallucination
   ```

3. **Generate ensemble metrics:** Compute precision, recall, F1 on validated set

### Ensemble Execution
```bash
python analyze.py ensemble-voting \
  --assignment a1 \
  --output-dir runs/multi/run_analysis3/
```

### Output: Ensemble Metrics
- **Location:** `runs/multi/run_analysis3/`
- **Files:**
  - `data.json` → Detections with voting consensus info
  - `report.md` → Comparison of single-strategy vs. ensemble
  - `metrics.json` → Aggregated precision, recall, F1

**Example Results (A1 + Ensemble Voting):**
```
Precision:  0.649  (+107% improvement from baseline 0.313)
Recall:     0.871  (stable, slight decrease from 0.872)
F1:         0.744  (+61% improvement from baseline 0.461)
False Positives: 1,164 (-75% reduction from 4,722)
```

---

## Data Flow Diagram: Complete End-to-End

```
┌─────────────────────────────────────────────────────────────┐
│ GROUND TRUTH                                                │
│ data/a1/groundtruth.json                                    │
│ ├─ NM_STATE_REACTIVE                                        │
│ ├─ NM_STATE_SPREADSHEET                                     │
│ └─ ... (10 categories total)                                │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ STAGE 1: INJECTION (dataset_generator.py)                   │
│ Select: NM_STATE_REACTIVE                                   │
│ Inject: into Student_123_Q1.py                              │
│ Output: authentic_seeded/a1/Student_123_Q1.py               │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ STAGE 2: DETECTION (analyze.py detect)                      │
│ Models: [gpt-5.2, claude-haiku, gemini-flash] × reasoning   │
│ Strategies: [baseline, taxonomy, cot, socratic]             │
│ Run: 6 × 4 = 24 LLM calls per file                          │
│ Output: detections/a1/{strategy}/student_Q*.json            │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ STAGE 3: MATCHING (analyze.py match)                        │
│ Embed: LLM student_thought_process vs ground truth          │
│ Score: cosine_similarity (threshold 0.65)                   │
│ Classify: TP / FP_HALLUCIN / FP_CLEAN / TN                 │
│ Output: runs/multi/run_analysis_{strategy}/*.json           │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ STAGE 4: ENSEMBLE VOTING (analyze.py ensemble-voting)       │
│ Aggregate: 4 strategies × 6 models                          │
│ Vote: ≥2 strategies must agree                              │
│ Filter: Reject single-strategy hallucinations               │
│ Output: runs/multi/run_analysis3/*.json (final metrics)     │
└─────────────────────────────────────────────────────────────┘
```

---

## Key Metrics Explained

### Precision
**Definition:** Of all detections (bugs flagged), how many were correct?
```
Precision = TP / (TP + FP)
```
- **High precision** = Few false alarms
- **Low precision** = Many hallucinations (Stage 4 fixes this)

### Recall
**Definition:** Of all actual bugs, how many did we find?
```
Recall = TP / (TP + FN)
```
- **High recall** = We didn't miss real bugs
- **Low recall** = Too many false negatives

### F1 Score
**Definition:** Harmonic mean of precision and recall
```
F1 = 2 × (Precision × Recall) / (Precision + Recall)
```
- Balances precision and recall
- Single number for comparing models/strategies

### The "Gap"
**Definition:** The difference between syntax detection and state detection
```
Gap = Syntax_F1 (~0.99) - State_F1 (~0.46 baseline, ~0.74 ensemble)
```
- **Syntax errors:** Easy to detect (99%+ success)
- **State errors:** Hard to detect (<50% single-strategy)
- **The thesis:** This gap represents the cognitive challenge of understanding student mental models

---

## File Reference Map

| Module | Purpose | Input | Output |
|--------|---------|-------|--------|
| `utils/generators/dataset_generator.py` | Inject bugs | `data/groundtruth.json` | `authentic_seeded/` |
| `utils/llm/anthropic.py`, `openai.py`, etc. | Call LLMs | Code + prompt | Raw JSON response |
| `analyze.py detect` | Run detections | `authentic_seeded/` | `detections/` |
| `utils/matching/semantic.py` | Embed & match | `detections/` + `groundtruth.json` | Similarity scores |
| `analyze.py match` | Single-strategy metrics | Similarity scores | `runs/multi/run_analysis_{strategy}/` |
| `analyze.py ensemble-voting` | Consensus voting | All 4 strategies | `runs/multi/run_analysis3/` (final) |

---

## Validating the Pipeline

### Unit Tests
```bash
pytest tests/test_llm_miscons2.py
```
Validates:
- LLM response parsing (Pydantic correctness)
- Embedding API calls
- Similarity computation

### Integration Tests
```bash
python analyze.py validate --run runs/multi/run_analysis3/
```
Checks:
- No missing detections
- No duplicate counts
- Voting logic correctness

### Manual Spot Checks
```bash
# View a single detection result
cat detections/a1_multi/baseline/student_Q1.json | jq '.detections[0]'

# Check matching quality
cat runs/multi/run_analysis_baseline/data.json | jq '.results[] | select(.is_tp == true) | .similarity_score' | sort -n
```

---

## Configuration & Reproducibility

### Threshold Tuning
Similarity threshold is configurable (default: 0.65):
```bash
python analyze.py match --threshold 0.70 ...
```
Lower threshold → higher recall, lower precision
Higher threshold → lower recall, higher precision

### Random Seeds
All runs use seeded RNGs for reproducibility:
- Dataset generation: `seed=1765036611`
- Embedding: Deterministic (OpenAI API)
- Voting: Deterministic (no randomness)

### API Versioning
Pinned in `pyproject.toml`:
```toml
openai = "==1.52.0"
anthropic = "==0.38.0"
```
Ensures embedding dimensions and model behavior remain consistent

---

## Next Steps: Extending the Pipeline

### Planned Additions
1. **A4 Assignment:** Objects and references (heap indirection)
2. **Cross-assignment analysis:** How does complexity affect LLM performance?
3. **Error classification:** Track types of hallucinations (e.g., invented bugs vs. misclassified bugs)
4. **Confidence weighting:** Weight votes by LLM confidence scores instead of simple majority
5. **Statistical testing:** McNemar's test for strategy comparison

---

## See Also
- [Architecture Overview](architecture.md) — High-level system design
- [Matching & Semantic Alignment](matching.md) — Detailed embedding methodology
- [Metrics Guide](metrics-guide.md) — Interpretation of precision, recall, F1
- [Notional Machines Reference](notional-machines.md) — All 10 categories and definitions
- [Complexity Gradient](complexity-gradient.md) — Why A1 < A2 < A3 in difficulty
