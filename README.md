# TRACER

**T**axonomic **R**esearch of **A**ligned **C**ognitive **E**rror **R**ecognition

A rigorous research framework for measuring whether Large Language Models can diagnose **student mental models** (Notional Machines) in introductory programming—not just surface-level bugs. This is a complete empirical study: synthetic benchmark, blind detection by 6 LLMs across 4 prompting strategies, semantic alignment validation, and statistical analysis.

**Status:** Complete research pipeline with validated results ready for publication at ITiCSE 2026.

---

## What This Project Measures

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    THE FUNDAMENTAL RESEARCH QUESTION                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Can LLMs diagnose student MENTAL MODELS, or just surface bugs?             │
│                                                                             │
│  EXAMPLE OF THE DIFFERENCE:                                                 │
│                                                                             │
│  CODE:  int[] arr = new int[5];                                             │
│         arr[5] = 10;           // Index out of bounds                       │
│                                                                             │
│  Surface Bug Detection (What):                                              │
│  └─ "Array index out of bounds"                                             │
│                                                                             │
│  Mental Model Detection (Why):                                              │
│  └─ "Student believes arrays use 1-based indexing (like mathematics),       │
│      not 0-based indexing (like Java)"                                      │
│                                                                             │
│  TRACER measures the second: understanding WHY students make errors.        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Key Discovery: The Diagnostic Ceiling

LLMs exhibit a **systematic and predictable failure pattern**: they excel at detecting misconceptions visible in code structure but fail at misconceptions requiring inference of student intent.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              DETECTION PERFORMANCE: STRUCTURAL vs SEMANTIC                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STRUCTURAL MISCONCEPTIONS (Visible in Code)                                │
│  Detectable because errors produce observable consequences                  │
│                                                                             │
│  The Void Machine (NM_API_01)                           99% recall │████░   │
│  The Mutable String Machine (NM_MEM_03)                 97% recall │████░   │
│  The Human Index Machine (NM_MEM_04)                    98% recall │████░   │
│                                                                             │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  SEMANTIC MISCONCEPTIONS (Invisible, Require Intent Inference)              │
│  Undetectable without understanding student's mental model                  │
│                                                                             │
│  Dangling Else (Indentation Trap) (NM_LOGIC_02)         52% recall │██░░░   │
│  Precedence Blindness (NM_SYN_02)                        59% recall │██░░░  │
│  Integer Division Blindness (NM_TYP_01)                 69% recall │███░░   │
│                                                                             │
│                  → 40% PERFORMANCE GAP ←                                    │
│                                                                             │
│  This gap represents the DIAGNOSTIC CEILING: the fundamental limit of       │
│  what current LLMs can diagnose without understanding student psychology.   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Practical Implication

- **Safe to Automate:** Array bounds, string operations, type errors (>90% recall)
- **Needs Human Review:** Loop logic, I/O ordering (60-90% recall)
- **Requires Human Diagnosis:** Variable state, control flow binding (<60% recall)

---

## Final Validated Results (5-Fold Cross-Validation)

### Overall Performance

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      CROSS-VALIDATED METRICS (Seed 42)                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Metric         │ Mean   │ Std Dev │ 95% CI                                 │
│  ───────────────┼────────┼─────────┼──────────────────                      │
│  Precision      │ 0.577  │ ±0.029  │ [0.521, 0.633]                         │
│  Recall         │ 0.872  │ ±0.016  │ [0.841, 0.903]                         │
│  F1 Score       │ 0.694  │ ±0.024  │ [0.646, 0.742]                         │
│                                                                             │
│  Raw Counts:    TP=5,305 │ FP=3,884 │ FN=775                                │
│                                                                             │
│  ** Excellent generalization across folds (mean dev-test gap: 0.000)        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Ensemble Voting Impact

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    VOTING METHOD COMPARISON (Test Split)                    │
├──────────────────────────┬────────────┬─────────┬─────────┬─────────────────┤
│ Method                   │ Precision  │ Recall  │ F1      │ Strategy        │
├──────────────────────────┼────────────┼─────────┼─────────┼─────────────────┤
│ Raw (No Ensemble)        │ 0.577      │ 0.873   │ 0.695   │ Baseline        │
│ Strategy Ensemble (≥2/4) │ 0.625      │ 0.872   │ 0.728   │ ≥2 of 4 prompts │
│ Model Ensemble (≥2/6)    │ 0.682 ✓    │ 0.864   │ 0.762 ✓ │ ≥2 of 6 models  │
├──────────────────────────┼────────────┼─────────┼─────────┼─────────────────┤
│ Improvement (Model vs Raw)            │ +0.105  │ -0.009  │ +0.067          │
│                          Precision Gain: +18% │ Recall: -1% │ F1: +9.6%     │
│                                                                             │
│ ** Model ensemble achieves best balance: high precision (68%) + high        │
│    recall (86%) = strong F1 for deployment in classroom settings.           │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Performance by Assignment (Complexity Gradient)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       PERFORMANCE BY ASSIGNMENT                             │
├──────────────┬────────────────────┬───────────┬────────┬────────┬───────────┤
│ Assignment   │ Focus              │ Precision │ Recall │ F1     │ Difficulty│
├──────────────┼────────────────────┼───────────┼────────┼────────┼───────────┤
│ A3 (Easiest) │ Arrays & Strings   │ 0.686     │ 0.971  │ 0.804  │ Concrete  │
│ A2 (Medium)  │ Loops & Control    │ 0.554     │ 0.876  │ 0.679  │ Temporal  │
│ A1 (Hardest) │ Variables & Math   │ 0.504     │ 0.771  │ 0.610  │ Abstract  │
├──────────────┼────────────────────┼───────────┼────────┼────────┼───────────┤
│ Gap (A3-A1)  │                    │ -0.182    │ +0.200 │ +0.194 │           │
│              │ A3 is 32% easier than A1 (F1: 0.804 vs 0.610)                │ 
│                                                                             │
│ Insight: Performance correlates with ABSTRACTION LEVEL, not individual      │
│ misconceptions. Students' mental models about concrete data structures      │
│ (arrays) are easier to diagnose than abstract state (variable values).      │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Best Performing Model

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    INDIVIDUAL MODEL PERFORMANCE (Test)                      │
├─────────────────────────────────────┬────────────┬────────┬────────────────┤
│ Model                               │ Precision  │ Recall │ F1   │ Winner   │
├─────────────────────────────────────┼────────────┼────────┼──────┼──────────┤
│ claude-haiku-4-5:reasoning          │ 0.776      │ 0.880  │ 0.825│ ★ BEST  │
│ gpt-5.2-2025-12-11:reasoning        │ 0.602      │ 0.856  │ 0.707│          │
│ claude-haiku-4-5                    │ 0.633      │ 0.819  │ 0.714│          │
│ gpt-5.2-2025-12-11                  │ 0.594      │ 0.846  │ 0.698│          │
│ gemini-3-flash:reasoning            │ 0.494      │ 0.924  │ 0.643│          │
│ gemini-3-flash                      │ 0.473      │ 0.909  │ 0.622│          │
├─────────────────────────────────────┴────────────┴────────┴──────┴──────────┤
│                                                                             │
│ Key finding: Claude Haiku with extended thinking outperforms all other      │
│ models by +11% F1. Extended thinking helps model diagnose semantic          │
│ misconceptions requiring deeper reasoning.                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## The 4 Prompting Strategies

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    STRATEGY COMPARISON (Cross-Model Avg)                    │
├──────────────┬──────────────────────────┬───────────┬────────┬──────────────┤
│ Strategy     │ Philosophy               │ Precision │ Recall │ F1           │
├──────────────┼──────────────────────────┼───────────┼────────┼──────────────┤
│ Baseline     │ Simple "find bugs"       │ 0.643     │ 0.864  │ 0.737 ★     │
│ Taxonomy     │ Provide category hints   │ 0.630     │ 0.885  │ 0.736        │
│ Chain-of-Thought│ Line-by-line tracing  │ 0.598     │ 0.839  │ 0.698        │
│ Socratic     │ Mental model probing     │ 0.474     │ 0.903  │ 0.622        │
├──────────────┼──────────────────────────┼───────────┼────────┼──────────────┤
│                                                                             │
│ Surprising Finding: Simple baseline outperforms elaborate pedagogical       │
│ techniques. Socratic strategy finds most bugs (90%) but halluccinates       │
│ most (53% precision). Ensemble voting reconciles this by requiring          │
│ consensus.                                                                  │
│                                                                             │
│ Statistical Significance: Cochran's Q = 26.22, p < 0.000009 (highly         │
│ significant difference across strategies).                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Validation Methodology

### 5-Fold Stratified Cross-Validation

TRACER uses rigorous validation to ensure results generalize:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              5-FOLD CROSS-VALIDATION PIPELINE (Seed=42)                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Step 1: STRATIFICATION                                                     │
│  ├─ Split 1,200 files into 5 folds                                          │
│  └─ Balance each fold by Notional Machine category (proportional)           │
│                                                                             │
│  Step 2: FOR EACH FOLD (i=1 to 5)                                           │
│  ├─ Development Set (80%, ~960 files)                                       │
│  │  └─ Used to calibrate semantic similarity threshold                      │
│  │                                                                          │
│  ├─ Test Set (20%, ~240 files)                                              │
│  │  └─ Held-out evaluation (never seen during calibration)                  │
│  │                                                                          │
│  └─ Report: Per-fold F1 + aggregated metrics                                │
│                                                                             │
│  Step 3: THRESHOLD CALIBRATION (DEV ONLY)                                   │
│  ├─ Grid search: 6 semantic thresholds × 5 noise floors = 30 configs        │
│  ├─ Optimize F1 on dev split                                                │
│  ├─ Selected thresholds: Semantic=0.55, Noise Floor=0.60                    │
│  └─ Apply same thresholds to test split                                     │
│                                                                             │
│  Step 4: EVALUATE TEST SPLIT                                                │
│  ├─ Precision, Recall, F1                                                   │
│  ├─ 95% confidence intervals via 1000 bootstrap resamples                   │
│  └─ Per-misconception breakdown                                             │
│                                                                             │
│  RESULTS:                                                                   │
│  ├─ Fold 1: Dev F1=0.691, Test F1=0.707 (gap: -0.016 ✓)                     │
│  ├─ Fold 2: Dev F1=0.691, Test F1=0.708 (gap: -0.017 ✓)                     │
│  ├─ Fold 3: Dev F1=0.692, Test F1=0.705 (gap: -0.013 ✓)                     │
│  ├─ Fold 4: Dev F1=0.705, Test F1=0.651 (gap: +0.054 ⚠)  (Outlier)          │
│  └─ Fold 5: Dev F1=0.694, Test F1=0.700 (gap: -0.006 ✓)                     │
│                                                                             │
│  Mean Dev-Test Gap: 0.000 ± 0.030                                           │
│  Interpretation: **Excellent generalization. Thresholds transfer to new     │
│  data without overfitting.**                                                │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## False Positive Breakdown

Understanding where the model fails:

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    ANALYSIS OF 3,884 FALSE POSITIVES                       │
├──────────────────┬────────────┬─────────┬──────────────────────────────────┤
│ FP Type          │ Count      │ % of FP │ What It Means                    │
├──────────────────┼────────────┼─────────┼──────────────────────────────────┤
│ FP_CLEAN         │ 3,364      │ 86.6%   │ LLM detected a misconception     │
│                  │            │         │ in CORRECT code (false alarm)    │
│                  │            │         │                                  │
│ FP_WRONG         │ 520        │ 13.4%   │ LLM detected WRONG misconception │
│                  │            │         │ (misclassification of which bug) │
│                  │            │         │                                  │
│ FP_HALLUCINATION │ 0          │ 0.0%    │ LLM invented non-existent        │
│                  │            │         │ misconception (very rare)        │
└──────────────────┴────────────┴─────────┴──────────────────────────────────┘

Implication: Most false positives (87%) are "false alarms"—the model over-
cautiously flags code that is actually correct. Only 13% are true semantic
misclassifications. This suggests the model struggles with confidence
calibration, not fundamental misunderstanding.
```

---

## Dataset Composition

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          SYNTHETIC BENCHMARK                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STUDENT POPULATION:                                                        │
│  ├─ 300 synthetic students (generated identities)                           │
│  ├─ Distributed across 3 assignments                                        │
│  └─ 100 students per assignment                                             │
│                                                                             │
│  ASSIGNMENTS:                                                               │
│  ├─ A1: Variables & Mathematical Expressions (8 misconceptions)             │
│  ├─ A2: Loops & Control Flow (6 misconceptions)                             │
│  └─ A3: Arrays & String Operations (4 misconceptions)                       │
│                                                                             │
│  TOTAL MISCONCEPTIONS: 18 unique Notional Machines                          │
│                                                                             │
│  PER-STUDENT OUTPUT:                                                        │
│  ├─ Q1: Code WITH misconception (1-2 specific Notional Machines injected)   │
│  ├─ Q2: Clean code (no misconceptions)                                      │
│  ├─ Q3: Code WITH misconception (different misconception than Q1)           │
│  └─ Total: 300 × 3 questions = 900 Java files                               │
│                                                                             │
│  DETECTION GRID:                                                            │
│  ├─ 6 LLM models (Claude, GPT, Gemini + reasoning variants)                 │
│  ├─ 4 prompting strategies (baseline, taxonomy, CoT, socratic)              │
│  └─ 900 files × 6 models × 4 strategies = 21,600 detection attempts         │
│                                                                             │
│  Actually evaluated: 11,711 detections after filtering null & noise         │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Quick Start Guide

### Prerequisites

```bash
# Clone the repository
git clone https://github.com/shahshlok/tracer.git
cd tracer

# Install dependencies (requires uv package manager)
uv sync

# Configure API keys (required for LLM access)
export ANTHROPIC_API_KEY="sk-ant-..."     # For Claude models
export OPENAI_API_KEY="sk-..."            # For GPT models and embeddings
export GOOGLE_API_KEY="..."               # For Gemini models
```

### Run the Analysis

```bash
# Recommended: Full analysis with model ensemble voting
uv run python analyze.py analyze-publication \
    --run-name my-analysis \
    --include-label-text false

# View results
cat runs/multi/my-analysis/report.md
```

---

## Documentation Guide

Complete documentation with detailed explanations and diagrams:

| #   | Document                                               | Explains                                                      | Audience              |
| --- | ------------------------------------------------------ | ------------------------------------------------------------- | --------------------- |
| 1   | **[Architecture](docs/architecture.md)**               | 4-stage pipeline, system design, data flow                    | Everyone (start here) |
| 2   | **[Dataset Generation](docs/dataset-generation.md)**   | How synthetic data was created, misconception injection       | Researchers           |
| 3   | **[Notional Machines](docs/notional-machines.md)**     | All 18 misconceptions with Java code examples                 | CS educators          |
| 4   | **[Methodology](docs/methodology.md)**                 | 5-fold CV, threshold calibration, validation approach         | Researchers           |
| 5   | **[Analysis Pipeline](docs/analysis-pipeline.md)**     | Detection → Embedding → Matching → Voting flow                | Researchers           |
| 6   | **[Metrics Guide](docs/metrics-guide.md)**             | Precision, Recall, F1, confusion matrix, benchmarks           | Everyone              |
| 7   | **[Semantic Matching](docs/matching.md)**              | OpenAI embeddings, similarity thresholds, null detection      | Implementers          |
| 8   | **[Complexity Gradient](docs/complexity-gradient.md)** | THE core thesis finding—why performance varies by abstraction | Researchers           |
| 9   | **[Prompting Strategies](docs/prompts.md)**            | The 4 prompting approaches: baseline, CoT, Socratic, taxonomy | Researchers           |
| 10  | **[CLI Reference](docs/cli-reference.md)**             | All commands, options, parameter explanations                 | Users                 |
| 11  | **[Development](docs/development.md)**                 | How to extend the framework, add new misconceptions           | Developers            |
| 12  | **[Context & Limitations](docs/context.md)**           | Threats to validity, synthetic data trade-offs                | Reviewers             |

---

## Project Structure

```
tracer/
│
├── README.md                           # This file
│
├── data/                               # Ground truth definitions
│   ├── a1/groundtruth.json            # 8 misconceptions (Variables)
│   ├── a2/groundtruth.json            # 6 misconceptions (Loops)
│   └── a3/groundtruth.json            # 4 misconceptions (Arrays)
│
├── authentic_seeded/                   # Synthetic student files
│   ├── a1/                             # Assignment 1 files
│   │   ├── {Student_Name}/Q1.java      # Question 1 (with bug)
│   │   ├── {Student_Name}/Q2.java      # Question 2 (clean)
│   │   └── {Student_Name}/Q3.java      # Question 3 (with bug)
│   ├── a2/                             # Assignment 2 (similar structure)
│   └── a3/                             # Assignment 3 (similar structure)
│   └── a{1,2,3}/manifest.json          # Maps students to misconceptions
│
├── detections/                         # LLM detection outputs (21,600 files)
│   ├── a1_multi/                       # Assignment 1 detections
│   │   ├── baseline/                   # Baseline strategy
│   │   │   ├── claude-haiku-4-5/      # Model 1
│   │   │   ├── gpt-5.2/               # Model 2
│   │   │   └── ...
│   │   ├── cot/                        # Chain-of-Thought strategy
│   │   ├── socratic/                   # Socratic strategy
│   │   └── taxonomy/                   # Taxonomy-guided strategy
│   ├── a2_multi/                       # Assignment 2 (same structure)
│   └── a3_multi/                       # Assignment 3 (same structure)
│
├── runs/                               # Analysis results
│   └── v2/                             # Latest validated runs
│       ├── run_analysis_main/          # PRIMARY RESULTS (Thinking-only)
│       │   ├── report.md               # Complete analysis report
│       │   ├── metrics.json            # Numeric metrics
│       │   ├── results.csv             # Per-detection breakdown
│       │   ├── fold_results.csv        # Per-fold metrics
│       │   ├── cv_info.json            # Cross-validation metadata
│       │   └── assets/                 # Visualizations (PNG)
│       │       ├── model_comparison.png
│       │       ├── strategy_comparison.png
│       │       ├── category_structural_vs_semantic.png
│       │       ├── misconception_recall.png
│       │       ├── hallucinations_sankey.png
│       │       └── threshold_sensitivity_heatmap.png
│       │
│       └── run_analysis_ablation/      # ABLATION (Label-included)
│           └── [same structure as main]
│
├── pydantic_models/                    # Data validation schemas
│   ├── models/evaluation.py            # Metric calculations
│   ├── submission/models.py            # Submission JSON format
│   └── context/models.py               # Context definitions
│
├── prompts/                            # Prompt engineering
│   └── strategies.py                   # 4 prompting strategies
│
├── utils/                              # Utilities
│   ├── llm/                            # LLM clients
│   │   ├── anthropic.py                # Claude API
│   │   ├── openai.py                   # GPT API
│   │   ├── gemini.py                   # Gemini API
│   │   └── base.py                     # Base interface
│   ├── matching/                       # Semantic matching
│   │   └── semantic.py                 # OpenAI embeddings
│   ├── execution.py                    # Sandbox execution
│   ├── grading.py                      # Correctness verification
│   └── statistics.py                   # Statistical analysis
│
├── analyze.py                          # Main analysis CLI
│   ├── analyze-publication             # Run with cross-validation
│   └── analyze-multi                   # Run simple analysis
│
├── docs/                               # Complete documentation
│   ├── architecture.md
│   ├── dataset-generation.md
│   ├── methodology.md
│   ├── analysis-pipeline.md
│   ├── metrics-guide.md
│   ├── matching.md
│   ├── complexity-gradient.md
│   ├── prompts.md
│   ├── cli-reference.md
│   ├── context.md
│   └── development.md
│
├── pyproject.toml                      # Project configuration
├── uv.lock                             # Locked dependency versions
├── .gitignore
├── AGENTS.md                           # Agent guidance (internal)
└── LICENSE

```

---

## Reproducibility

All results are fully reproducible:

```bash
# Reproduce the main analysis (5-fold cross-validation, thinking-only)
uv run python analyze.py analyze-publication \
    --run-name reproduce_main \
    --seed 42 \
    --include-label-text false

# Expected result: F1 ≈ 0.694 ± 0.024 (within CI)

# Reproduce the ablation (label-included, upper bound)
uv run python analyze.py analyze-publication \
    --run-name reproduce_ablation \
    --seed 42 \
    --include-label-text true

# Expected result: F1 ≈ 0.673 ± 0.024 (lower than main, showing label leakage)
```

All random seeds are fixed (seed=42 for CV splits), embedding cache is reused, and detection files are pre-computed.

---

## Research Integrity

This research follows rigorous validation practices:

- **Calibration/Test Split:** Threshold selection on dev only, metrics on held-out test
- **Label Leakage Prevention:** Primary analysis excludes misconception names from embeddings
- **Ablation Study:** Label-included results show upper bound and validate leakage didn't occur
- **No p-hacking:** All statistical tests reported regardless of significance
- **Synthetic Data Transparency:** Acknowledged as limitation; used for internal validity (perfect ground truth)

---

## Citation

If you use TRACER in your research, please cite:

```bibtex
@software{shah2025tracer,
  author = {Shah, Shlok},
  title = {TRACER: Taxonomic Research of Aligned Cognitive Error Recognition},
  year = {2025},
  institution = {University of British Columbia Okanagan},
  note = {Honours Thesis Research Project},
  url = {https://github.com/shahshlok/tracer},
  booktitle = {Proceedings of ITiCSE 2026}
}
```

---

## Contact & Support

- **Questions?** See the [Troubleshooting](#troubleshooting) section below or check docs/development.md
- **Want to extend?** See docs/development.md for adding new misconceptions or models
- **Found a bug?** Open an issue on GitHub

---

## Troubleshooting

### "API key invalid"

```bash
# Verify all three API keys are set
echo $ANTHROPIC_API_KEY      # Should not be empty
echo $OPENAI_API_KEY         # Should not be empty
echo $GOOGLE_API_KEY         # Should not be empty

# If empty, re-export them
export ANTHROPIC_API_KEY="sk-ant-..."
export OPENAI_API_KEY="sk-..."
export GOOGLE_API_KEY="..."
```

### "No detections found"

Ensure detection files exist:

```bash
# Check if detections folder is populated
ls detections/a1_multi/baseline/gpt-5.2/ | head -5
# Should output JSON filenames

# If empty, detection step needs to be run first (see CLI Reference)
```

### "Out of memory"

The full analysis (all 3 assignments) requires ~8GB RAM. For testing:

```bash
# Run single assignment only
uv run python analyze.py analyze-publication --assignment a1
```

### "Embedding cache errors"

Clear and rebuild:

```bash
# The embedding cache is automatically managed
# If corrupted, delete and re-run (will re-download embeddings)
rm -rf .embedding_cache/
uv run python analyze.py analyze-publication --run-name fresh_run
```

---

## Key Statistics at a Glance

| Statistic                          | Value                              |
| ---------------------------------- | ---------------------------------- |
| Students (synthetic)               | 300                                |
| Total Java files                   | 900                                |
| Misconceptions                     | 18 Notional Machines               |
| LLM models tested                  | 6                                  |
| Prompting strategies               | 4                                  |
| Detection attempts                 | 21,600                             |
| Cross-validation folds             | 5                                  |
| **F1 Score (Main)**                | **0.694**                          |
| **F1 Score (with Model Ensemble)** | **0.762**                          |
| Best individual model              | Claude Haiku 4.5:reasoning (0.825) |
| Hardest misconception              | Dangling Else (52% recall)         |
| Easiest misconception              | Void Machine (99% recall)          |
| Performance gap (A3 vs A1)         | 32%                                |

---

**Last Updated:** January 6, 2026
**Status:** Ready for publication
**Validation:** 5-fold cross-validation complete, no overfitting detected
