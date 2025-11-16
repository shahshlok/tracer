# Ensemble Model Evaluation (EME) Schema Documentation

## Overview
This document describes the complete data schema for the Ensemble Model Evaluation (EME) research tool. The system grades student code submissions using multiple LLM models and generates rich comparison analytics to support ensemble grading research and misconception pattern discovery.

---

## Table of Contents
1. [Evaluation JSON Structure](#evaluation-json-structure)
2. [SQLite Database Schema](#sqlite-database-schema)
3. [Data Flow](#data-flow)
4. [Comparison Metrics Reference](#comparison-metrics-reference)
5. [Research Applications](#research-applications)

---

## Evaluation JSON Structure

### Top-Level Schema
Each evaluation represents **one student's submission for one question, graded by multiple models**.

```jsonc
{
  "evaluation_id": "eval_cuboid_s123_q1_m2025_001",
  "schema_version": "1.0.0",
  "created_at": "2025-03-01T10:30:00Z",
  "created_by": "automated_pipeline_v1",

  "context": { ... },
  "submission": { ... },
  "rubric": { ... },
  "models": { ... },
  "comparison": { ... }
}
```

---

### Context Section
Links the evaluation to course, assignment, and question metadata.

```jsonc
"context": {
  "course_id": "cosc111_intro_programming",
  "course_name": "COSC111: Introduction to Programming",
  "assignment_id": 5,                        // Integer assignment number
  "assignment_title": "Cuboid Class and OOP Basics",
  "question_id": "q1_cuboid_class",
  "question_title": "Implementing a Cuboid class in Java",
  "question_source_path": "question_cuboid.md",
  "rubric_source_path": "rubric_cuboid.json"
}
```

**Fields:**
- `assignment_id`: Integer (not string) for programmatic sorting/filtering
- `question_source_path`: Path to original question file (not embedded markdown)
- `rubric_source_path`: Path to rubric file for traceability

---

### Submission Section
Student's submitted code and metadata.

```jsonc
"submission": {
  "student_id": "12345678",
  "student_name": "Shlok Shah",
  "submitted_at": "2025-03-01T09:55:00Z",
  "programming_language": "java",
  "files": [
    {
      "path": "Cuboid.java",
      "language": "java",
      "content": "public class Cuboid { ... }"
    }
  ]
}
```

**Note:** The `files` array contains the full source code to provide context for evidence snippets.

---

### Rubric Section
Snapshot of the rubric used for grading.

```jsonc
"rubric": {
  "rubric_id": "rubric_cuboid_v1",
  "title": "Cuboid OOP Rubric",
  "total_points": 100,
  "categories": [
    {
      "category_id": "syntax_compilation",
      "name": "Syntax & Compilation",
      "max_points": 40,
      "description": "Code compiles without errors..."
    },
    // ... more categories
  ]
}
```

---

### Models Section
**Plug-and-play design:** Each LLM grading service outputs its results into this section. The backend computes the comparison section afterward.

```jsonc
"models": {
  "gpt-5-nano": {
    "model_name": "gpt-5-nano",
    "provider": "openai",
    "model_version": "2025-03-01",
    "run_id": "run_2025_03_01_01",

    "config": {
      "system_prompt_id": "grading_system_v3",
      "rubric_prompt_id": "rubric_cuboid_prompt_v2"
    },

    "scores": {
      "total_points_awarded": 85,
      "max_points": 100,
      "percentage": 85.0
    },

    "category_scores": [ ... ],
    "feedback": { ... },
    "misconceptions": [ ... ]
  },
  "gpt-oss-120b": { ... }
}
```

#### Category Scores (per model)
**New fields added for richer analysis:**

```jsonc
"category_scores": [
  {
    "category_id": "syntax_compilation",
    "category_name": "Syntax & Compilation",
    "points_awarded": 38,
    "max_points": 40,
    "reasoning": "Code compiles cleanly...",
    "confidence": 0.95,          // NEW: Model's confidence (0-1)
    "reasoning_tokens": 156      // NEW: Token count (depth proxy)
  }
]
```

**LLMs should provide:**
- `confidence`: How confident they are in this category score (0.0 to 1.0)
- `reasoning_tokens`: Number of tokens in their reasoning (can be computed from `reasoning` field)

#### Feedback (per model)
```jsonc
"feedback": {
  "overall_comment": "Good implementation...",
  "strengths": ["Correct use of constructor overloading..."],
  "areas_for_improvement": ["Add comments to explain methods..."]
}
```

#### Misconceptions (per model)
**Finalized structure (inductive approach - no predefined IDs):**

```jsonc
"misconceptions": [
  {
    "name": "Does not match required output format exactly",
    "description": "The student prints information in a readable way but does not follow the exact output format specified...",
    "confidence": 0.92,
    "evidence": [
      {
        "source": "student_code",
        "file_path": "Cuboid.java",
        "language": "java",
        "snippet": "public void displayInfo() { ... }",
        "line_start": 18,
        "line_end": 24,
        "note": "The assignment requires a specific output format..."
      }
    ],
    "generated_by": "gpt-5-nano",
    "validated_by": null
  }
]
```

**Removed fields (from earlier iterations):**
- ❌ `misconception_id` - No predefined taxonomy
- ❌ `category` - Discovered inductively, not predefined
- ❌ `severity` - Not needed for pattern discovery
- ❌ `rubric_category_ids` - Not needed
- ❌ `suggested_fix` - Not needed for research

**Evidence structure:**
- `source`: Always "student_code" for code grading
- `line_start`, `line_end`: Line range for UI highlighting
- `snippet`: Actual code demonstrating the issue
- `note`: Explanation of why this is evidence

---

### Comparison Section
**Computed by backend code** after all model outputs are collected. This is the research goldmine.

#### Structure Overview
```jsonc
"comparison": {
  "score_summary": { ... },
  "pairwise_differences": [ ... ],
  "category_agreement": [ ... ],
  "category_insights": { ... },
  "misconception_summary": { ... },
  "confidence_analysis": { ... },
  "model_characteristics": { ... },
  "reliability_metrics": { ... },
  "ensemble_decision": { ... },
  "ensemble_quality": { ... },
  "flags": { ... },
  "metadata": { ... }
}
```

See [Comparison Metrics Reference](#comparison-metrics-reference) below for detailed field documentation.

---

## SQLite Database Schema

### Tables

#### 1. `evaluations` Table
Stores complete evaluation records (full JSON blobs).

```sql
CREATE TABLE evaluations (
    evaluation_id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL,
    assignment_id INTEGER NOT NULL,
    question_id TEXT NOT NULL,
    created_at TEXT NOT NULL,
    full_evaluation_json JSON NOT NULL
);

CREATE INDEX idx_evaluations_student ON evaluations(student_id);
CREATE INDEX idx_evaluations_assignment ON evaluations(assignment_id);
CREATE INDEX idx_evaluations_question ON evaluations(question_id);
CREATE INDEX idx_evaluations_created ON evaluations(created_at);
```

**Purpose:** Audit trail and complete reference. All data is preserved for analysis.

---

#### 2. `misconceptions` Table
Flattened misconception records for efficient querying and LLM analysis.

```sql
CREATE TABLE misconceptions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    evaluation_id TEXT NOT NULL,
    assignment_id INTEGER NOT NULL,
    question_id TEXT NOT NULL,
    student_id TEXT NOT NULL,
    model_name TEXT NOT NULL,
    misconception_name TEXT NOT NULL,
    misconception_description TEXT NOT NULL,
    evidence_json JSON NOT NULL,
    confidence REAL NOT NULL,
    generated_by TEXT NOT NULL,
    validated_by TEXT,
    created_at TEXT NOT NULL,
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(evaluation_id)
);

CREATE INDEX idx_misconceptions_assignment ON misconceptions(assignment_id);
CREATE INDEX idx_misconceptions_question ON misconceptions(question_id);
CREATE INDEX idx_misconceptions_student ON misconceptions(student_id);
CREATE INDEX idx_misconceptions_model ON misconceptions(model_name);
CREATE INDEX idx_misconceptions_name ON misconceptions(misconception_name);
CREATE INDEX idx_misconceptions_confidence ON misconceptions(confidence);
```

**Purpose:**
- Fast querying/aggregation across students
- LLM analysis of misconception patterns
- Instructor dashboard queries

**Note:** `assignment_id` is INTEGER to match the evaluation schema.

---

## Data Flow

```
┌─────────────────────┐
│ Student Submission  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────────────────────────────┐
│ Multiple LLM Grading Services               │
│ (OpenAI, eduai, OpenRouter, etc.)           │
└──────────┬──────────────────────────────────┘
           │ Each outputs grading JSON
           ▼
┌─────────────────────────────────────────────┐
│ Normalization Layer                         │
│ Combine into single evaluation JSON         │
│ (models.{model_name} structure)             │
└──────────┬──────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────┐
│ Backend Comparison Engine                   │
│ Compute all comparison metrics              │
└──────────┬──────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────┐
│ Store in SQLite                             │
│ • evaluations table (full JSON)             │
│ • misconceptions table (flattened)          │
└──────────┬──────────────────────────────────┘
           │
           ├──────────────┬──────────────────┐
           ▼              ▼                  ▼
    ┌──────────┐   ┌──────────┐     ┌──────────────┐
    │Dashboard │   │LLM Anal. │     │Research Data │
    │(Instruct)│   │(Patterns)│     │Export        │
    └──────────┘   └──────────┘     └──────────────┘
```

---

## Comparison Metrics Reference

### 1. Score Summary
Aggregate statistics across all models.

| Field | Type | Description | Interpretation |
|-------|------|-------------|----------------|
| `mean` | float | Arithmetic mean score | Central tendency |
| `median` | float | Median score | Robust to outliers |
| `std_dev` | float | Standard deviation | Spread/variability |
| `variance` | float | σ² | Variance in scores |
| `coefficient_of_variation` | float | CV = σ/μ | **Relative variability**<br>< 0.1 = low variance<br>0.1-0.2 = moderate<br>> 0.2 = high |
| `range` | float | max - min | Total spread |

---

### 2. Pairwise Differences
Model-vs-model comparisons (scales to N models).

**Per pair:**
- `total_points_diff`: Raw point difference
- `percentage_diff`: Percentage point difference
- `category_differences[]`: Per-category breakdowns with `percent_of_category_max`
- `largest_category_disagreement`: Which category shows most disagreement

---

### 3. Category Agreement
Per-rubric-category statistics.

| Field | Description | Interpretation |
|-------|-------------|----------------|
| `agreement_level` | Categorical label | `perfect` (σ=0), `high` (CV<0.15),<br>`medium` (0.15≤CV<0.30), `low` (CV≥0.30) |
| `normalized_variance` | σ² / max_points² | Allows cross-category comparison |
| `confidence_stats` | Mean confidence for category | Which categories are models uncertain about? |

**Category Insights:**
- `most_controversial`: Highest CV/disagreement
- `most_agreed`: Lowest CV/disagreement
- `lowest_confidence`: Where models are least confident

---

### 4. Misconception Summary

| Metric | Description | Research Value |
|--------|-------------|----------------|
| `total_unique_misconceptions` | Distinct issues after deduplication | Total misconception types found |
| `unique_to_single_model` | Found by only 1 model | Potential blind spots or false positives |
| `consensus_misconceptions` | Found by 2+ models | Higher confidence issues |
| `consensus_ratio` | consensus / total | **>0.7**: Models agree on what's wrong<br>**<0.4**: Models see different issues |
| `misconception_overlap_matrix` | Pairwise Jaccard similarity | Which models see similar issues? |

---

### 5. Confidence Analysis

| Metric | Description | Research Value |
|--------|-------------|----------------|
| `confidence_score_correlation` | Correlation(confidence, score) | Do models express confidence consistently? |
| `high_confidence_disagreements[]` | Confident but divergent cases | **Most interesting for research!** |
| `per_category_confidence` | Confidence by rubric category | Which categories are inherently harder to grade? |

---

### 6. Model Characteristics
Understanding each model's grading behavior.

| Field | Description | Research Value |
|-------|-------------|----------------|
| `strictness_ranking` | Models ordered by avg score | Identify lenient vs strict models |
| `consistency_scores` | CV of category scores per model | How internally consistent is each model? |
| `reasoning_depth` | Avg tokens in reasoning | Which models provide more detailed explanations? |
| `misconception_detection_rate` | # misconceptions per model | Which models are more thorough? |

---

### 7. Inter-Rater Reliability
**Critical for research publication.**

| Metric | Formula | Interpretation |
|--------|---------|----------------|
| **Pearson correlation** | r | >0.9 excellent, 0.7-0.9 strong, 0.4-0.7 moderate, <0.4 weak |
| **Spearman correlation** | ρ | Rank-order (robust to non-linearity) |
| **ICC (Intraclass Correlation)** | ICC(2,1) | >0.90 excellent, 0.75-0.90 good, 0.50-0.75 moderate, <0.50 poor |
| **Krippendorff's alpha** | α | >0.80 good, 0.67-0.80 tentative, <0.67 discard data |
| **SEM (Standard Error)** | σ√(1-ICC) | Typical measurement error |
| **95% CI** | μ ± 1.96×SEM | Confidence interval for true score |

---

### 8. Ensemble Decision
Final grade recommendations.

| Field | Description |
|-------|-------------|
| `recommended_score` | Final ensemble grade |
| `scoring_method` | `mean` \| `median` \| `weighted` \| `trimmed_mean` |
| `alternative_scores` | All possible ensemble methods computed |
| `weights_used` | Model-specific weights (if using weighted method) |
| `confidence_in_decision` | Based on model agreement (1 - CV) |
| `consensus_level` | `strong` (CV<0.10) \| `moderate` (0.10-0.20) \| `weak` (0.20-0.30) \| `divided` (≥0.30) |

---

### 9. Ensemble Quality
Assessing the value of using multiple models.

| Metric | Range | Interpretation |
|--------|-------|----------------|
| `diversity_score` | 0-1 | How different are assessments?<br>Sweet spot: 0.05-0.15 |
| `redundancy_score` | 0-1 | Misconceptions flagged by multiple models<br>High (>0.7) = reliable, consistent |
| `complementarity_score` | 0-1 | Unique misconceptions per model<br>High = models add unique value |
| `overall_ensemble_value` | Categorical | `excellent` \| `high` \| `moderate` \| `low` \| `poor` |
| `recommendation` | Action | `ensemble_reliable` \| `ensemble_adds_value` \| `single_model_sufficient` \| `needs_more_models` \| `too_divergent` |

---

### 10. Flags & Recommendations
Automated decision support.

**Review Reasons:**
- `large_score_gap`: Score diff > threshold (e.g., 15%)
- `boundary_score`: Near pass/fail cutoff
- `low_confidence`: Model confidence < threshold
- `high_confidence_disagreement`: Confident but divergent
- `outlier_detected`: Model >2σ from mean
- `category_conflict`: Significant category disagreement
- `misconception_mismatch`: Models find different issues
- `low_reliability`: ICC/correlation below threshold

**Research Interest Reasons:**
- `high_inter_rater_reliability`
- `low_inter_rater_reliability`
- `strong_model_agreement`
- `strong_model_disagreement`
- `unique_misconception_pattern`
- `confidence_score_anomaly`
- `boundary_case`
- `outlier_model_behavior`
- `category_specific_patterns`

---

## Research Applications

### 1. Ensemble Strategy Research
**Questions answered:**
- Which ensemble method works best? (mean vs median vs weighted)
- How many models are needed for reliable grading?
- Which model combinations provide best coverage?
- When does ensemble add value vs single model?

**Key metrics:**
- `ensemble_quality.*`
- `reliability_metrics.*`
- `ensemble_decision.alternative_scores`

---

### 2. Model Reliability & Calibration
**Questions answered:**
- Which models are most/least reliable?
- Are certain models systematically lenient/strict?
- Which models have highest inter-rater agreement?
- Do models agree on what constitutes each rubric category?

**Key metrics:**
- `model_characteristics.strictness_ranking`
- `reliability_metrics.pearson_correlation`
- `category_agreement[].agreement_level`

---

### 3. Misconception Pattern Discovery
**Questions answered:**
- What misconceptions are common across students?
- Which misconceptions do models consistently identify?
- Are there misconceptions only certain models catch?
- How do misconceptions cluster by assignment/question?

**Key metrics:**
- `misconception_summary.*`
- Aggregate queries on `misconceptions` table
- LLM analysis of misconception descriptions

---

### 4. Rubric Clarity & Design
**Questions answered:**
- Which rubric categories cause most grading disagreement?
- Are certain categories inherently subjective?
- Where do models express low confidence?
- Should the rubric be revised for clarity?

**Key metrics:**
- `category_insights.most_controversial`
- `confidence_analysis.per_category_confidence`
- `category_agreement[].agreement_level`

---

### 5. Educational Insights
**Questions answered:**
- Where do students struggle most?
- What concepts need more instruction?
- Are certain misconceptions prerequisites to others?
- How do misconceptions evolve across assignments?

**Key metrics:**
- Misconception frequency by assignment/question
- Longitudinal tracking of student misconceptions
- Cluster analysis of misconception descriptions

---

## Configuration & Thresholds

The `comparison.metadata.thresholds_config` section documents all configurable thresholds:

```jsonc
"thresholds_config": {
  "human_review_score_diff_percent": 15.0,
  "boundary_score_margin_percent": 5.0,
  "low_confidence_threshold": 0.6,
  "high_confidence_threshold": 0.8,
  "outlier_std_devs": 2.0,
  "cv_high_agreement_max": 0.10,
  "cv_medium_agreement_max": 0.20,
  "icc_excellent_min": 0.90,
  "icc_good_min": 0.75
}
```

**Note:** These can be tuned based on empirical research findings.

---

## Schema Versioning

- **Current version:** 1.0.0
- **Tracked in:** `evaluation.schema_version` and `comparison.metadata.computation_version`
- **Migration strategy:** When schema evolves, increment version and maintain backward compatibility where possible

---

## Summary

This schema provides:
1. ✅ **Complete audit trail** (full evaluations stored)
2. ✅ **Efficient querying** (flattened misconceptions table)
3. ✅ **Rich analytics** (comprehensive comparison metrics)
4. ✅ **Research-grade reliability** (ICC, Krippendorff's alpha, correlations)
5. ✅ **Extensibility** (plug-and-play model outputs, versioned schema)
6. ✅ **Practical value** (ensemble decisions, human review flags)
7. ✅ **Publication-ready** (standard statistical measures, interpretations)

**This is a grading benchmark in the making.**
