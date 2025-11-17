# 📋 Schema Quick Reference

**Version 2.0.0** - Fast lookup guide for evaluation JSON and database schema

> **For complete documentation**: See [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md)

---

## 📊 Evaluation JSON Structure

### Top-Level Overview

```jsonc
{
  "evaluation_id": "eval_{question}_{student}_{timestamp}",
  "schema_version": "1.0.0",
  "created_at": "2025-03-01T10:30:00Z",

  "context": {/* course, assignment, question info */},
  "submission": {/* student code and metadata */},
  "rubric": {/* grading rubric snapshot */},
  "models": {/* per-model grading results */},
  "comparison": {/* cross-model statistics */}
}
```

---

## 🎯 Key Sections Explained

### 1. Context

Links evaluation to course/assignment/question:

```jsonc
"context": {
  "course_id": "cosc111_intro_programming",
  "assignment_id": 5,              // INTEGER (not string!)
  "question_id": "q1_cuboid_class",
  "question_source_path": "question_cuboid.md",
  "rubric_source_path": "rubric_cuboid.json"
}
```

**Key Fields:**
- `assignment_id`: Integer for sorting/filtering
- `question_source_path`: Where the question file lives
- No embedded question text (keep files separate)

---

### 2. Submission

Student's code and metadata:

```jsonc
"submission": {
  "student_id": "12345678",
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

**Key Fields:**
- `files[]`: Array of submitted code files (full source)
- Used for evidence linking in misconceptions

---

### 3. Rubric

Snapshot of grading criteria:

```jsonc
"rubric": {
  "rubric_id": "rubric_cuboid_v1",
  "total_points": 100,
  "categories": [
    {
      "category_id": "syntax_compilation",  // Used in category_scores
      "name": "Syntax & Compilation",
      "max_points": 40,
      "description": "Code compiles without errors..."
    }
  ]
}
```

**Key Fields:**
- `category_id`: Links to `models[].category_scores[]`
- Embedded (not a reference) to preserve rubric at grading time

---

### 4. Models (Per-Model Results)

Each LLM's grading output:

```jsonc
"models": {
  "gpt-5-nano": {
    "model_name": "gpt-5-nano",
    "provider": "openai",

    "scores": {
      "total_points_awarded": 85,
      "percentage": 85.0
    },

    "category_scores": [
      {
        "category_id": "syntax_compilation",
        "points_awarded": 38,
        "max_points": 40,
        "reasoning": "Code compiles cleanly...",
        "confidence": 0.95,        // NEW in v2
        "reasoning_tokens": 156    // NEW in v2
      }
    ],

    "feedback": {
      "overall_comment": "Good implementation...",
      "strengths": ["..."],
      "areas_for_improvement": ["..."]
    },

    "misconceptions": [/* see below */]
  }
}
```

**What LLMs Must Provide (v2):**
- ✅ Per-category `confidence` (0.0-1.0)
- ✅ `reasoning_tokens` count
- ✅ Misconceptions with evidence

---

### 5. Misconceptions (Per Model)

**Simplified v2 structure** (no predefined taxonomy):

```jsonc
"misconceptions": [
  {
    "name": "Does not match required output format",
    "description": "Student prints info but doesn't follow exact format...",
    "confidence": 0.92,
    "evidence": [
      {
        "source": "student_code",
        "file_path": "Cuboid.java",
        "language": "java",
        "snippet": "public void displayInfo() { ... }",
        "line_start": 18,
        "line_end": 24,
        "note": "Labels differ from sample output"
      }
    ],
    "generated_by": "gpt-5-nano",
    "validated_by": null  // TA can validate later
  }
]
```

**Removed from v1:**
- ❌ `misconception_id` (no predefined taxonomy)
- ❌ `category` (discovered inductively)
- ❌ `severity`
- ❌ `rubric_category_ids`
- ❌ `suggested_fix`

**Evidence Structure:**
- Points to specific code location
- Includes snippet for context
- Line numbers for UI highlighting

---

### 6. Comparison (Computed by Backend)

**Rich cross-model analytics:**

```jsonc
"comparison": {
  // Basic statistics
  "score_summary": {
    "mean": 81.5,
    "median": 81.5,
    "std_dev": 4.95,
    "coefficient_of_variation": 0.061  // Key metric!
  },

  // Per-category agreement
  "category_agreement": [{
    "category_id": "syntax_compilation",
    "agreement_level": "high",  // perfect|high|medium|low
    "confidence_stats": {...}
  }],

  // Misconception overlap
  "misconception_summary": {
    "consensus_misconceptions": 1,   // Found by 2+ models
    "unique_to_single_model": 0      // Only 1 model found
  },

  // Statistical reliability
  "reliability_metrics": {
    "pearson_correlation": 0.996,
    "intraclass_correlation_icc": 0.973,
    "krippendorff_alpha": 0.985,
    "confidence_interval_95": {"lower": 76.56, "upper": 86.44}
  },

  // Final recommendation
  "ensemble_decision": {
    "recommended_score": 81.5,
    "scoring_method": "mean",  // mean|median|weighted
    "confidence_in_decision": 0.87
  },

  // Flags
  "flags": {
    "needs_human_review": false,
    "overall_agreement": "high",  // perfect|high|medium|low|conflicted
    "interesting_for_research": true
  }
}
```

**Key Metrics:**
- **CV (Coefficient of Variation)**: <0.1 = high agreement
- **ICC**: >0.9 = excellent reliability
- **Krippendorff's α**: >0.8 = good reliability

See [Comparison Metrics](#comparison-metrics-reference) below for interpretations.

---

## 🗄️ Database Schema

### Tables

#### 1. `evaluations` Table

Stores full JSON blobs:

```sql
CREATE TABLE evaluations (
    evaluation_id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL,
    assignment_id INTEGER NOT NULL,  -- Integer!
    question_id TEXT NOT NULL,
    created_at TEXT NOT NULL,
    full_evaluation_json JSON NOT NULL
);
```

**Purpose:** Complete audit trail

---

#### 2. `misconceptions` Table

Flattened for querying:

```sql
CREATE TABLE misconceptions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    evaluation_id TEXT NOT NULL,
    assignment_id INTEGER NOT NULL,
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
```

**Purpose:** Fast pattern analysis

**Common Queries:**

```sql
-- Most common misconceptions
SELECT misconception_name, COUNT(*) as count
FROM misconceptions
WHERE assignment_id = 5
GROUP BY misconception_name
ORDER BY count DESC;

-- Students with specific misconception
SELECT DISTINCT student_id
FROM misconceptions
WHERE misconception_name LIKE '%output format%';

-- Average confidence by model
SELECT model_name, AVG(confidence) as avg_conf
FROM misconceptions
GROUP BY model_name;
```

---

## 📊 Comparison Metrics Reference

### Score Statistics

| Metric | Formula | Interpretation |
|--------|---------|----------------|
| **Mean** | Σ scores / N | Average score |
| **Median** | Middle value | Robust to outliers |
| **Std Dev** | σ | Spread of scores |
| **CV** | σ / μ | **<0.1** = low variance<br>**0.1-0.2** = moderate<br>**>0.2** = high |

### Reliability Metrics

| Metric | Range | Interpretation |
|--------|-------|----------------|
| **Pearson r** | -1 to 1 | **>0.9** excellent<br>**0.7-0.9** strong<br>**0.4-0.7** moderate |
| **ICC** | 0 to 1 | **>0.90** excellent<br>**0.75-0.90** good<br>**0.50-0.75** moderate |
| **Krippendorff's α** | 0 to 1 | **>0.80** good<br>**0.67-0.80** tentative<br>**<0.67** discard |

### Agreement Levels

| Level | Criteria | Meaning |
|-------|----------|---------|
| **Perfect** | σ = 0 | All models gave same score |
| **High** | CV < 0.15 | Strong agreement |
| **Medium** | 0.15 ≤ CV < 0.30 | Moderate agreement |
| **Low** | CV ≥ 0.30 | Weak agreement |

---

## 🔧 Quick Lookup: Field Types

```
STRING FIELDS:
- evaluation_id, student_id, question_id
- misconception_name, misconception_description
- file_path, snippet, note

INTEGER FIELDS:
- assignment_id (IMPORTANT!)
- line_start, line_end
- points_awarded, max_points

FLOAT FIELDS:
- confidence (0.0-1.0)
- percentage, mean, std_dev, CV
- ICC, correlations

BOOLEAN FIELDS:
- needs_human_review
- interesting_for_research

ARRAY FIELDS:
- files[], category_scores[], misconceptions[]
- evidence[], pairwise_differences[]

OBJECT FIELDS:
- context, submission, rubric, models, comparison
```

---

## ⚠️ Common Mistakes

### ❌ Wrong
```jsonc
{
  "assignment_id": "5",  // STRING
  "misconception_id": "OUTPUT_FORMAT",  // Don't use IDs
  "category": "procedural",  // No categories in v2
  "severity": "minor"  // Removed
}
```

### ✅ Correct
```jsonc
{
  "assignment_id": 5,  // INTEGER
  "name": "Output format mismatch",  // Descriptive name
  "description": "Full description...",  // Let LLM describe
  "confidence": 0.92  // Confidence only
}
```

---

## 🎯 Field Checklist for LLMs

When implementing LLM grading, ensure output includes:

**Per Category:**
- [ ] `points_awarded`
- [ ] `reasoning`
- [ ] `confidence` (NEW in v2)
- [ ] `reasoning_tokens` (NEW in v2)

**Per Misconception:**
- [ ] `name`
- [ ] `description`
- [ ] `confidence`
- [ ] `evidence[]` with code snippets
- [ ] `generated_by`

**DON'T Include (removed in v2):**
- [ ] ~~`misconception_id`~~
- [ ] ~~`category`~~
- [ ] ~~`severity`~~
- [ ] ~~`suggested_fix`~~

---

## 📚 See Also

- **Complete Reference**: [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md)
- **Annotated Example**: [../example.jsonc](../example.jsonc)
- **Getting Started**: [GETTING_STARTED.md](GETTING_STARTED.md)
- **Architecture**: [ARCHITECTURE.md](ARCHITECTURE.md)

---

**Last Updated:** Version 2.0.0 (November 2024)
