# LLM Misconception Detection Framework

A research framework for measuring whether Large Language Models can diagnose **student mental models** (Notional Machines) in introductory programming—not just surface-level bugs. Part of a Bachelor's Honours Thesis at UBCO targeting **ITiCSE/SIGCSE**.

```
                                    THE RESEARCH QUESTION

    ┌─────────────────────────────────────────────────────────────────────────┐
    │                                                                         │
    │   Can LLMs identify WHY students make errors, not just WHAT is wrong?   │
    │                                                                         │
    └─────────────────────────────────────────────────────────────────────────┘

                    "The student used arr[5] on a 5-element array"
                                        vs
            "The student believes arrays use 1-based indexing like in math"
                                        ↓
                    The second is a MENTAL MODEL diagnosis.
                    That's what we measure.
```

---

## Key Finding: The Diagnostic Ceiling

LLMs show **systematic variance** in detecting misconceptions based on their **conceptual type**:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DETECTION PERFORMANCE BY TYPE                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STRUCTURAL (visible in code)                      SEMANTIC (invisible)     │
│  ───────────────────────────────                   ─────────────────────    │
│                                                                             │
│  Array bounds (97%)  ████████████████████░░░       Integer division (59%)   │
│  String immutable (99%) ████████████████████░       Spreadsheet View (65%)  │
│  Void assumption (99%) ████████████████████░        Dangling Else (16%)     │
│                                                                             │
│                     LLMs excel                      LLMs struggle           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

    This 40% gap between structural and semantic misconceptions is the
    DIAGNOSTIC CEILING—the fundamental limit of LLM misconception detection.
```

**Practical Impact:** Educators can safely automate feedback for array/string errors, but must maintain human oversight for variable state and control flow misconceptions.

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                              4-STAGE RESEARCH PIPELINE                        │
└──────────────────────────────────────────────────────────────────────────────┘

STAGE 1                    STAGE 2                   STAGE 3                STAGE 4
SYNTHETIC INJECTION        BLIND DETECTION           SEMANTIC ALIGNMENT     ENSEMBLE VOTING
───────────────────        ───────────────           ──────────────────     ───────────────

┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐    ┌─────────────────┐
│ groundtruth.json│       │ 6 LLMs analyze  │       │ Embed & compare │    │ ≥2 strategies   │
│ defines 18      │──────▶│ 360 Java files  │──────▶│ LLM output vs   │───▶│ must agree      │
│ misconceptions  │       │ with 4 prompts  │       │ ground truth    │    │ to count        │
└─────────────────┘       └─────────────────┘       └─────────────────┘    └─────────────────┘
         │                         │                         │                      │
         ▼                         ▼                         ▼                      ▼
   300 synthetic           8,640 detection           Cosine similarity      Filtered results:
   students with           JSON outputs              ≥0.65 = match         Precision +107%
   known bugs                                                               F1 +61%
```

---

## Final Results (run_final_analysis_100)

| Metric | Raw Detection | With Ensemble | Improvement |
|--------|---------------|---------------|-------------|
| **Precision** | 0.322 | **0.649** | +107% |
| **Recall** | 0.868 | **0.871** | stable |
| **F1 Score** | 0.469 | **0.744** | +61% |
| False Positives | 14,236 | 1,164 | -92% |

### By Assignment (The Complexity Gradient)

| Assignment | Focus | F1 Score | Interpretation |
|------------|-------|----------|----------------|
| **A3** | Arrays & Strings | **0.626** | Easiest - concrete, visible errors |
| **A2** | Loops & Control | **0.481** | Medium - temporal state reasoning |
| **A1** | Variables & Math | **0.341** | Hardest - abstract mental models |

The 30% performance gap from A3 to A1 demonstrates that LLMs struggle with abstract state reasoning.

---

## Quick Start

### Prerequisites

```bash
# Clone repository
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli

# Install dependencies (requires uv)
uv sync

# Set API keys
export OPENROUTER_API_KEY="sk-or-..."    # For LLM detection (6 models)
export OPENAI_API_KEY="sk-..."           # For semantic embeddings
```

### Run Analysis

```bash
# Recommended: Ensemble analysis with voting
uv run python analyze.py analyze-multi \
    --run-name my-analysis \
    --semantic-threshold 0.65 \
    --noise-floor 0.55

# View results
cat runs/multi/run_my-analysis/report.md
```

---

## Documentation Guide

Start here and follow the reading order:

| Doc | Purpose | Read Time |
|-----|---------|-----------|
| **[Architecture](docs/architecture.md)** | System design with data flow diagrams | 10 min |
| **[Notional Machines](docs/notional-machines.md)** | All 18 misconceptions with code examples | 15 min |
| **[Analysis Pipeline](docs/analysis-pipeline.md)** | How detection and matching work | 10 min |
| **[Metrics Guide](docs/metrics-guide.md)** | Precision, Recall, F1 explained | 5 min |
| **[Matching](docs/matching.md)** | Semantic embedding methodology | 8 min |
| **[Complexity Gradient](docs/complexity-gradient.md)** | Core thesis finding explained | 5 min |
| **[CLI Reference](docs/cli-reference.md)** | All commands and options | 5 min |
| **[Prompts](docs/prompts.md)** | The 4 prompting strategies | 5 min |
| **[Development](docs/development.md)** | Extending the framework | 5 min |

---

## Project Structure

```
ensemble-eval-cli/
├── data/                           # Ground truth definitions
│   ├── a1/groundtruth.json         # 8 misconceptions (Variables/Math)
│   ├── a2/groundtruth.json         # 6 misconceptions (Loops/Control)
│   └── a3/groundtruth.json         # 4 misconceptions (Arrays/Strings)
│
├── authentic_seeded/               # 300 synthetic students × 3 questions
│   ├── a1/{Student_Name}/*.java    # Files with known bugs injected
│   ├── a2/{Student_Name}/*.java
│   └── a3/{Student_Name}/*.java
│
├── detections/                     # LLM outputs (8,640 files)
│   ├── a1_multi/{strategy}/{model}/*.json
│   ├── a2_multi/{strategy}/{model}/*.json
│   └── a3_multi/{strategy}/{model}/*.json
│
├── runs/multi/                     # Analysis results
│   └── run_final_analysis_100/     # FINAL RESULTS
│       ├── report.md               # Full report with charts
│       ├── metrics.json            # Numeric results
│       ├── results.csv             # Per-file breakdown
│       └── assets/*.png            # Visualizations
│
├── prompts/strategies.py           # 4 prompt architectures
├── utils/matching/semantic.py      # OpenAI embedding pipeline
├── analyze.py                      # Main analysis CLI
└── docs/                           # This documentation
```

---

## The 6 Models Tested

| Model | Provider | Type | Best F1 |
|-------|----------|------|---------|
| **Claude Haiku 4.5** | Anthropic | Base | 0.499 |
| **Claude Haiku 4.5:reasoning** | Anthropic | Extended thinking | **0.604** |
| **GPT-5.2** | OpenAI | Base | 0.507 |
| **GPT-5.2:reasoning** | OpenAI | Extended thinking | 0.499 |
| **Gemini 3 Flash** | Google | Base | 0.385 |
| **Gemini 3 Flash:reasoning** | Google | Extended thinking | 0.392 |

**Winner:** Claude Haiku with reasoning mode achieved the highest individual F1 (0.604).

---

## The 4 Prompting Strategies

| Strategy | Philosophy | Precision | Recall |
|----------|------------|-----------|--------|
| **Baseline** | Simple "find bugs" | 0.373 | 0.850 |
| **Taxonomy** | Provides misconception categories | 0.366 | 0.890 |
| **Chain-of-Thought** | Line-by-line tracing | 0.345 | 0.841 |
| **Socratic** | Mental model probing | 0.251 | 0.890 |

**Finding:** Simple prompts outperform pedagogical prompts. Socratic finds the most bugs but hallucinates the most. Ensemble voting recovers precision.

---

## Research Context

| Field | Value |
|-------|-------|
| **Project** | Honours Thesis: LLM Detection of Notional Machine Misconceptions |
| **Institution** | University of British Columbia Okanagan (UBCO) |
| **Researcher** | Shlok Shah |
| **Academic Year** | 2024-2025 |
| **Target Venue** | ITiCSE / SIGCSE 2025 |

---

## Citation

```bibtex
@software{shah2025llm_misconceptions,
  author = {Shah, Shlok},
  title = {LLM Notional Machine Misconception Detection Framework},
  year = {2025},
  institution = {University of British Columbia Okanagan},
  note = {Honours Thesis Research Project},
  url = {https://github.com/shahshlok/ensemble-eval-cli}
}
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `API key invalid` | Check `OPENROUTER_API_KEY` and `OPENAI_API_KEY` |
| `No detections found` | Run detection first or verify `detections/` exists |
| `Out of memory` | Use `--assignment a1` instead of `multi` |

---

**Last Updated:** December 31, 2025
**Final Run:** `runs/multi/run_final_analysis_100/`
