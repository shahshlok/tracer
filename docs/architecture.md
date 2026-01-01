# System Architecture

This document provides a complete technical overview of the LLM Misconception Detection Framework. It explains how each component works together to measure whether LLMs can diagnose student mental models.

---

## Table of Contents

1. [Research Goal](#research-goal)
2. [The 4-Stage Pipeline](#the-4-stage-pipeline)
3. [Stage 1: Synthetic Injection](#stage-1-synthetic-injection)
4. [Stage 2: Blind Detection](#stage-2-blind-detection)
5. [Stage 3: Semantic Alignment](#stage-3-semantic-alignment)
6. [Stage 4: Ensemble Voting](#stage-4-ensemble-voting)
7. [Directory Structure](#directory-structure)
8. [Data Models](#data-models)
9. [Technology Stack](#technology-stack)

---

## Research Goal

This framework measures **Cognitive Alignment**—the degree to which LLMs can identify not just *what* is wrong with code, but *why* the student wrote it that way (their mental model).

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           WHAT WE MEASURE                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   Traditional Code Analysis        vs        Mental Model Diagnosis          │
│   ─────────────────────────────              ──────────────────────────      │
│                                                                             │
│   "Line 5 has an array index       vs   "Student believes arrays start      │
│    out of bounds error"                  at index 1, like in mathematics"   │
│                                                                             │
│   ↓ Identifies symptom                   ↓ Identifies cognitive cause       │
│   ↓ Any static analyzer can do this     ↓ Requires understanding intent    │
│                                                                             │
│   This framework measures whether LLMs can do the SECOND type.              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## The 4-Stage Pipeline

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                          COMPLETE RESEARCH PIPELINE                           │
└──────────────────────────────────────────────────────────────────────────────┘

  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
  │    STAGE 1      │    │    STAGE 2      │    │    STAGE 3      │    │    STAGE 4      │
  │   SYNTHETIC     │───▶│     BLIND       │───▶│    SEMANTIC     │───▶│    ENSEMBLE     │
  │   INJECTION     │    │   DETECTION     │    │   ALIGNMENT     │    │     VOTING      │
  └─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                      │                      │
         │                       │                      │                      │
         ▼                       ▼                      ▼                      ▼

  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
  │ • 18 misconcp-  │    │ • 6 LLM models  │    │ • OpenAI embed- │    │ • Require ≥2    │
  │   tions defined │    │ • 4 prompts     │    │   dings (3072D) │    │   strategies    │
  │ • 300 students  │    │ • 360 files     │    │ • Cosine sim-   │    │   to agree      │
  │ • 1 bug/file    │    │ • 8,640 outputs │    │   ilarity ≥0.65 │    │ • Filters 92%   │
  │                 │    │                 │    │                 │    │   of false pos  │
  └─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                      │                      │
         ▼                       ▼                      ▼                      ▼

  authentic_seeded/      detections/              TP/FP/FN            Final metrics:
  ├── a1/                ├── a1_multi/            classification      P=0.649, R=0.871
  ├── a2/                ├── a2_multi/                                F1=0.744
  └── a3/                └── a3_multi/
```

---

## Stage 1: Synthetic Injection

### Purpose
Create a dataset of student code with **known, labeled misconceptions** that can serve as ground truth for evaluation.

### Process

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         SYNTHETIC INJECTION PROCESS                          │
└─────────────────────────────────────────────────────────────────────────────┘

  INPUT: groundtruth.json                    OUTPUT: Student Java files
  ───────────────────────                    ────────────────────────────

  {                                          // Student: Allen_Andrew_600171
    "id": "NM_STATE_01",                     // Question: Q1 (Acceleration)
    "category": "Reactive State Machine",   
    "name": "Spreadsheet View",              double v0 = 0, v1 = 0, t = 0;
    "student_thinking": "Variables           double a = (v1 - v0) / t;  // Bug!
      update automatically when              v0 = scan.nextDouble();
      their sources change"                  v1 = scan.nextDouble();
  }                                          t = scan.nextDouble();
        │                                    System.out.println(a);
        │                                           │
        ▼                                           │
  GPT-4 generates code                              │
  that exhibits this                                ▼
  misconception                              authentic_seeded/a1/
                                             Allen_Andrew_600171/a1q1.java
```

### Key Files

| File | Purpose |
|------|---------|
| `data/a1/groundtruth.json` | 8 misconception definitions for A1 |
| `data/a2/groundtruth.json` | 6 misconception definitions for A2 |
| `data/a3/groundtruth.json` | 4 misconception definitions for A3 |
| `utils/generators/dataset_generator.py` | Generates student files |
| `authentic_seeded/a{1,2,3}/manifest.json` | Maps student → misconception |

### Important Constraint

**One misconception per file.** This ensures clean labeling—each file has exactly one ground truth ID (or none if "clean").

---

## Stage 2: Blind Detection

### Purpose
Have LLMs analyze student code **without knowing the ground truth**, to measure their natural diagnostic ability.

### Detection Grid

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DETECTION MATRIX                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                              PROMPTING STRATEGIES                            │
│               ┌──────────┬──────────┬──────────┬──────────┐                 │
│               │ baseline │ taxonomy │   cot    │ socratic │                 │
│ ┌─────────────┼──────────┼──────────┼──────────┼──────────┤                 │
│ │ GPT-5.2     │    ●     │    ●     │    ●     │    ●     │                 │
│ │ GPT-5.2:r   │    ●     │    ●     │    ●     │    ●     │                 │
│ │ Claude      │    ●     │    ●     │    ●     │    ●     │                 │
│ │ Claude:r    │    ●     │    ●     │    ●     │    ●     │                 │
│ │ Gemini      │    ●     │    ●     │    ●     │    ●     │                 │
│ │ Gemini:r    │    ●     │    ●     │    ●     │    ●     │                 │
│ └─────────────┴──────────┴──────────┴──────────┴──────────┘                 │
│                                                                             │
│  6 models × 4 strategies = 24 detection configurations                      │
│  24 configs × 360 files = 8,640 total detections                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Prompting Strategies

| Strategy | Description | Example Prompt Excerpt |
|----------|-------------|----------------------|
| **baseline** | Simple bug-finding | "Identify any misconceptions in this code" |
| **taxonomy** | Provides category list | "Given these categories: [list], which applies?" |
| **cot** | Chain-of-thought tracing | "Trace line-by-line, then identify misconceptions" |
| **socratic** | Mental model probing | "What does the student believe about how Java works?" |

### LLM Output Schema

Every detection produces structured JSON:

```json
{
  "misconceptions": [
    {
      "inferred_category_name": "Early Calculation Error",
      "student_thought_process": "Student believes variables update automatically...",
      "conceptual_gap": "Variables are assigned once, not reactive",
      "evidence": [
        {"line_number": 3, "code_snippet": "double a = (v1 - v0) / t;"}
      ],
      "confidence": 0.85
    }
  ]
}
```

### Key Files

| File | Purpose |
|------|---------|
| `prompts/strategies.py` | 4 prompt builders |
| `miscons.py` | Detection orchestrator |
| `utils/llm/*.py` | API clients (OpenAI, Anthropic, Google) |
| `detections/{a}_multi/{strategy}/` | Output directory |

---

## Stage 3: Semantic Alignment

### Purpose
Match LLM outputs to ground truth despite **terminology differences**. An LLM might call it "Auto-Update Error" while we call it "Reactive State Machine"—same concept, different words.

### The Matching Problem

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            THE TERMINOLOGY GAP                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  LLM Output:              "Student thinks variables auto-update"            │
│  Ground Truth:            "NM_STATE_01: Reactive State Machine"             │
│                                                                             │
│  String matching:         0% overlap                                         │
│  Semantic similarity:     87% match ✓                                        │
│                                                                             │
│  We use SEMANTIC EMBEDDINGS to bridge this gap.                             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Embedding Pipeline

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          SEMANTIC MATCHING PROCESS                           │
└─────────────────────────────────────────────────────────────────────────────┘

  LLM Detection Text                          Ground Truth Definitions
  ──────────────────                          ────────────────────────

  "Student computed formula         ←─────→   NM_STATE_01: "Variables are
   before reading inputs,                     treated like spreadsheet cells
   expecting auto-update"                     that update automatically"

         │                                            │
         ▼                                            ▼

  OpenAI text-embedding-3-large             OpenAI text-embedding-3-large
  [0.12, -0.45, 0.78, ...]                  [0.15, -0.42, 0.81, ...]
  (3072 dimensions)                         (3072 dimensions)

                        │
                        ▼

              Cosine Similarity = 0.87

                        │
                        ▼

              0.87 ≥ 0.65 threshold?
                     YES ✓
                        │
                        ▼

              MATCH: NM_STATE_01
              Classification: TRUE POSITIVE
```

### Thresholds

| Threshold | Value | Purpose |
|-----------|-------|---------|
| **Semantic Match** | 0.65 | Score above this = match |
| **Noise Floor** | 0.55 | Score below this = ignore (pedantic noise) |

### Classification Rules

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CLASSIFICATION LOGIC                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Score < 0.55:     NOISE (discarded, not counted)                           │
│                    "Didn't close Scanner" - pedantic, not misconception     │
│                                                                             │
│  Score 0.55-0.65:  FALSE POSITIVE (hallucination)                           │
│                    LLM claimed something that doesn't match ground truth    │
│                                                                             │
│  Score ≥ 0.65:     Check if matched_id == expected_id                       │
│                    ├── YES: TRUE POSITIVE (correct detection)               │
│                    └── NO:  FALSE POSITIVE (wrong misconception)            │
│                                                                             │
│  No detection:     FALSE NEGATIVE (missed the bug)                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Key Files

| File | Purpose |
|------|---------|
| `utils/matching/semantic.py` | OpenAI embedding + cosine similarity |
| `analyze.py` | Main analysis with matching logic |

---

## Stage 4: Ensemble Voting

### Purpose
Reduce hallucinations by requiring **consensus** across strategies. If only 1 of 4 strategies detects something, it's likely a false positive.

### The Voting Algorithm

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          ENSEMBLE VOTING LOGIC                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  For each (student, question, misconception_id) tuple:                      │
│                                                                             │
│      Count how many strategies detected it:                                  │
│      ┌─────────────┬──────────────────────────────────────┐                 │
│      │ baseline    │ Detected NM_STATE_01? YES            │                 │
│      │ taxonomy    │ Detected NM_STATE_01? YES            │                 │
│      │ cot         │ Detected NM_STATE_01? NO             │                 │
│      │ socratic    │ Detected NM_STATE_01? YES            │                 │
│      └─────────────┴──────────────────────────────────────┘                 │
│                                                                             │
│      Agreement count: 3/4                                                    │
│      Threshold: ≥2                                                          │
│      Result: VALIDATED ✓                                                     │
│                                                                             │
│  ────────────────────────────────────────────────────────────────────────── │
│                                                                             │
│      Example of REJECTION:                                                  │
│      ┌─────────────┬──────────────────────────────────────┐                 │
│      │ baseline    │ Detected "Redundant Logic"? NO       │                 │
│      │ taxonomy    │ Detected "Redundant Logic"? NO       │                 │
│      │ cot         │ Detected "Redundant Logic"? NO       │                 │
│      │ socratic    │ Detected "Redundant Logic"? YES      │  ← hallucination│
│      └─────────────┴──────────────────────────────────────┘                 │
│                                                                             │
│      Agreement count: 1/4                                                    │
│      Threshold: ≥2                                                          │
│      Result: REJECTED (filtered out)                                         │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Impact of Ensemble Voting

| Metric | Before Ensemble | After Ensemble | Change |
|--------|-----------------|----------------|--------|
| Precision | 0.322 | **0.649** | +107% |
| Recall | 0.868 | 0.871 | stable |
| F1 | 0.469 | **0.744** | +61% |
| False Positives | 14,236 | 1,164 | -92% |

---

## Directory Structure

```
ensemble-eval-cli/
│
├── data/                               # GROUND TRUTH DEFINITIONS
│   ├── a1/
│   │   ├── groundtruth.json            # 8 misconceptions for Variables/Math
│   │   ├── a1.md                       # Assignment description
│   │   ├── q1.md, q2.md, q3.md         # Question prompts
│   │   └── tests/                      # Test cases
│   ├── a2/                             # 6 misconceptions for Loops/Control
│   └── a3/                             # 4 misconceptions for Arrays/Strings
│
├── authentic_seeded/                   # GENERATED STUDENT CODE
│   ├── a1/
│   │   ├── manifest.json               # Student → misconception mapping
│   │   └── {Student_Name}/
│   │       ├── a1q1.java               # Student submission Q1
│   │       ├── a1q2.java               # Student submission Q2
│   │       └── a1q3.java               # Student submission Q3
│   ├── a2/                             # 100 students × 3 questions
│   └── a3/                             # 100 students × 3 questions
│
├── detections/                         # LLM DETECTION OUTPUTS
│   ├── a1_multi/
│   │   ├── baseline/                   # Strategy: baseline
│   │   │   └── {model}/*.json          # Per-model outputs
│   │   ├── taxonomy/
│   │   ├── cot/
│   │   └── socratic/
│   ├── a2_multi/
│   └── a3_multi/
│
├── runs/                               # ANALYSIS RESULTS
│   └── multi/
│       └── run_final_analysis_100/     # FINAL RESULTS
│           ├── report.md               # Full markdown report
│           ├── metrics.json            # Numeric metrics
│           ├── results.csv             # Per-file breakdown
│           ├── compliance.csv          # TP/FP/FN per file
│           └── assets/                 # PNG visualizations
│               ├── assignment_comparison.png
│               ├── model_comparison.png
│               ├── strategy_f1.png
│               ├── category_recall.png
│               └── ...
│
├── prompts/
│   └── strategies.py                   # 4 prompt builders
│
├── pydantic_models/
│   ├── evaluation.py                   # DetectionResult schema
│   └── submission/models.py            # Student submission schema
│
├── utils/
│   ├── llm/
│   │   ├── openai.py                   # OpenAI client
│   │   ├── anthropic.py                # Anthropic client
│   │   └── gemini.py                   # Google client
│   ├── matching/
│   │   └── semantic.py                 # Embedding pipeline
│   ├── generators/
│   │   └── dataset_generator.py        # Synthetic data generation
│   └── statistics.py                   # Bootstrap CI, McNemar's test
│
├── docs/                               # DOCUMENTATION
├── analyze.py                          # Main analysis CLI
├── miscons.py                          # Detection orchestrator
└── pipeline.py                         # Full pipeline runner
```

---

## Data Models

### Ground Truth Schema (`groundtruth.json`)

```json
{
  "misconceptions": [
    {
      "id": "NM_STATE_01",
      "category": "The Reactive State Machine",
      "name": "Spreadsheet View (Early Calculation)",
      "explanation": "The student treats variables like spreadsheet cells...",
      "student_thinking": "I'll define the formula first, then read inputs...",
      "code_pattern": "Compute derived value BEFORE reading dependent inputs",
      "applicable_questions": ["Q1", "Q2", "Q3"]
    }
  ]
}
```

### Manifest Schema (`manifest.json`)

```json
{
  "students": [
    {
      "name": "Allen_Andrew_600171",
      "questions": {
        "Q1": {"is_clean": false, "misconception_id": "NM_STATE_01"},
        "Q2": {"is_clean": true, "misconception_id": null},
        "Q3": {"is_clean": false, "misconception_id": "NM_TYP_01"}
      }
    }
  ]
}
```

### Detection Output Schema

```json
{
  "student": "Allen_Andrew_600171",
  "question": "Q1",
  "strategy": "baseline",
  "model": "gpt-5.2",
  "misconceptions": [
    {
      "inferred_category_name": "string",
      "student_thought_process": "string",
      "conceptual_gap": "string",
      "evidence": [{"line_number": 3, "code_snippet": "..."}],
      "confidence": 0.85
    }
  ]
}
```

---

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Package Manager | uv | Latest |
| Python | Python | 3.12+ |
| LLM APIs | OpenRouter, OpenAI, Anthropic, Google | Latest |
| Embeddings | OpenAI text-embedding-3-large | 3072D |
| Data Processing | pandas, numpy | Latest |
| Statistics | scipy | Latest |
| Visualization | matplotlib, seaborn | Latest |
| CLI | typer | Latest |
| Validation | pydantic | v2 |

---

## Next: [Analysis Pipeline](analysis-pipeline.md)
