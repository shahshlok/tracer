# Misconception Analysis

This document explains the misconception analysis system, including fuzzy clustering, topic normalization, and pattern detection.

## Table of Contents

- [Overview](#overview)
- [The Problem: Data Fragmentation](#the-problem-data-fragmentation)
- [Topic Normalization](#topic-normalization)
- [Fuzzy Clustering](#fuzzy-clustering)
- [Analysis Pipeline](#analysis-pipeline)
- [Report Generation](#report-generation)
- [Metrics and Formulas](#metrics-and-formulas)

## Overview

The Misconception Analyzer processes evaluation JSON files to:

1. Extract misconceptions from all model evaluations
2. Normalize topics to 4 canonical categories
3. Cluster similar misconception names using fuzzy matching
4. Calculate class-wide statistics
5. Track Q3→Q4 progression patterns
6. Generate markdown reports

**Location:** `utils/misconception_analyzer.py`

## The Problem: Data Fragmentation

LLMs are creative, which is a double-edged sword. When asked to identify a misconception, they produce variations like:

- *"Incorrect formula application"*
- *"Formula Misapplication"*
- *"Incorrect application of formula"*

To a human, these are obviously the same. To a computer counting strings, these are **3 distinct errors** with 1 occurrence each.

**Consequence:**
- The "Most Common Misconceptions" table becomes useless because counts are diluted
- Major trends (e.g., "Formula issues") are hidden behind a long tail of unique strings

## Topic Normalization

LLM-generated topics are normalized to 4 canonical topics aligned with course learning objectives:

| Canonical Topic | Example Mappings |
|----------------|------------------|
| Variables | "variable declaration", "formula application", "operator precedence" |
| Data Types | "data types", "type mismatch", "wrong data types for velocity" |
| Constants | "Math library", "Math.sqrt", "exponentiation in java" |
| Reading input from the keyboard | "Scanner", "input handling", "input/output" |

**Implementation:**

```python
CANONICAL_TOPICS = [
    "Variables",
    "Data Types",
    "Constants",
    "Reading input from the keyboard",
]

TOPIC_MAPPING = {
    "variables": "Variables",
    "variable declaration": "Variables",
    "formula application": "Variables",
    "data types": "Data Types",
    "scanner": "Reading input from the keyboard",
    # ... many more mappings
}

def normalize_topic(topic: str) -> str:
    # Direct match
    if topic in CANONICAL_TOPICS:
        return topic
    
    # Lowercase lookup
    if topic.lower() in TOPIC_MAPPING:
        return TOPIC_MAPPING[topic.lower()]
    
    # Fuzzy substring matching
    for key, canonical in TOPIC_MAPPING.items():
        if key in topic.lower() or topic.lower() in key:
            return canonical
    
    # Fallback
    return "Variables"
```

## Fuzzy Clustering

The core algorithm groups similar misconception names using Levenshtein distance.

### How It Works

**Step 1: Sort by Frequency**

Sort all misconceptions so the most common terms come first. These become "canonical names" (anchors).

**Step 2: Iterative Matching**

Compare each new term against existing anchors:

```python
def cluster_misconceptions(self, threshold: float = 0.8) -> dict[str, str]:
    # Get names sorted by frequency (most common first)
    name_counts = defaultdict(int)
    for record in self.misconception_records:
        name_counts[record.name] += 1
    
    sorted_names = sorted(name_counts.keys(), key=lambda x: name_counts[x], reverse=True)
    
    clusters = {}
    canonical_names = []
    
    for name in sorted_names:
        # Find best match in existing canonical names
        best_match = None
        best_ratio = 0.0
        
        for canonical in canonical_names:
            ratio = difflib.SequenceMatcher(None, name.lower(), canonical.lower()).ratio()
            if ratio > best_ratio:
                best_ratio = ratio
                best_match = canonical
        
        if best_match and best_ratio >= threshold:
            clusters[name] = best_match  # Merge
        else:
            canonical_names.append(name)  # New anchor
            clusters[name] = name
    
    return clusters
```

### Walkthrough Example

**Input:**
1. "Missing Semicolon" (Frequency: 10)
2. "Missing semicolon" (Frequency: 5)
3. "Formula Error" (Frequency: 4)
4. "Formula error in calculation" (Frequency: 1)

**Processing:**

| Compare | Similarity | Action |
|---------|------------|--------|
| "Missing semicolon" vs "Missing Semicolon" | 0.94 | MERGE → "Missing Semicolon" = 15 |
| "Formula Error" vs "Missing Semicolon" | 0.15 | NEW ANCHOR |
| "Formula error in calculation" vs "Formula Error" | 0.65 | NEW ANCHOR (below 0.80) |

**Result:**
```
{
    "Missing Semicolon": "Missing Semicolon",
    "Missing semicolon": "Missing Semicolon",  # Merged
    "Formula Error": "Formula Error",
    "Formula error in calculation": "Formula error in calculation"  # Not merged
}
```

### The Threshold (0.80)

We use a similarity threshold of **0.80 (80%)**.

| Threshold | Effect |
|-----------|--------|
| Too High (95%) | Only catches typos (e.g., "color" vs "colour") |
| Too Low (50%) | Merges unrelated things (e.g., "Syntax Error" and "Logic Error") |
| 80% (optimal) | Merges variations while preserving distinct concepts |

### Empirical Results

Actual merges from our dataset:

| Original (Absorbed) | Canonical (Target) | Similarity |
|--------------------|-------------------|------------|
| Incorrect Data Type Selection | Incorrect data type usage | 0.82 |
| Misinterpretation of Problem Requirements | Misinterpreting Problem Requirements | 0.91 |
| Incorrect exponentiation operator | Incorrect use of exponentiation operator | 0.90 |
| Missing import statement for Scanner | Missing Import Statement | 0.80 |

### Limitations

**False Positives:**

Sometimes distinct concepts have similar names:
- "Incorrect application of distance formula" vs "Incorrect application of Heron's formula"
- Similarity: 0.84 → MERGED

For high-level reports, this is usually acceptable (both are formula errors), but it loses specificity.

**Opaque Merges:**

The report shows final counts but doesn't explicitly list what was merged unless you run the debug script.

## Analysis Pipeline

### Load Evaluations

```python
analyzer = MisconceptionAnalyzer(evals_dir="student_evals")
num_loaded = analyzer.load_evaluations()
# Loads all *_eval.json files as EvaluationDocument instances
```

### Extract Misconceptions

```python
analyzer.extract_misconceptions()
# Creates MisconceptionRecord for each misconception in each model evaluation
# Normalizes topics during extraction
```

### Analyze Class

```python
class_analysis = analyzer.analyze_class()
```

Returns a `ClassAnalysis` object containing:

| Field | Description |
|-------|-------------|
| `total_students` | Unique student count |
| `total_misconceptions` | Total misconception records |
| `topic_task_stats` | Statistics per Topic+Task combination |
| `misconception_type_stats` | Statistics per unique misconception name |
| `question_stats` | Per-question breakdown |
| `progression_analysis` | Q3→Q4 tracking |
| `model_agreement_summary` | Misconceptions detected per model |

## Report Generation

### Generate Markdown Report

```python
analyzer.generate_markdown_report("misconception_report.md")
```

### Report Sections

1. **Executive Summary**
   - Total students analyzed
   - Total misconceptions detected
   - Generation timestamp

2. **Most Difficult Areas (by Topic)**
   - Ranked by % of class affected
   - Shows total misconceptions, student count, avg confidence

3. **Most Common Misconceptions**
   - Top 10 specific misconception types
   - Shows occurrences, models agreeing

4. **Per-Question Analysis**
   - Q1-Q4 breakdown
   - Misconception rate, top misconception per question

5. **Progression Analysis (Q3→Q4)**
   - Students who struggled in both
   - Students who improved
   - Students who regressed
   - Persistent misconceptions

6. **Model Agreement Analysis**
   - How many misconceptions each model detected

7. **Legend: Formulas and Metrics**
   - Explains all calculations

## Metrics and Formulas

### Students Affected %

$$\text{Students Affected \%} = \frac{\text{unique students with misconceptions}}{\text{total students}} \times 100\%$$

### Average Confidence

$$\text{Avg Confidence} = \frac{\sum \text{confidence scores}}{\text{count(misconceptions)}}$$

### Model Agreement Rate

$$\text{Agreement Rate} = \frac{\text{models detecting this misconception}}{\text{total models}}$$

### Misconception Persistence Rate

$$\text{Persistence Rate} = \frac{\text{struggled in both Q3 \& Q4}}{\text{struggled in Q3}} \times 100\%$$

### Improvement Rate

$$\text{Improvement Rate} = \frac{\text{improved (Q3 issues, Q4 clean)}}{\text{struggled in Q3}} \times 100\%$$

### Confidence Score Interpretation

| Range | Interpretation |
|-------|----------------|
| 0.0 - 0.5 | Low confidence (uncertain/borderline) |
| 0.5 - 0.7 | Moderate confidence |
| 0.7 - 0.9 | High confidence |
| 0.9 - 1.0 | Very high confidence (strong evidence) |

## Concrete Walkthrough: From Submission to Report

### Step 1: Student Submission

**Student:** Chen_Wei_200023  
**File:** Q1.java

```java
double distance = Math.sqrt((x2 - x1) + (y2 - y1)); // Missing squares!
```

### Step 2: Model Evaluation

**Model A (Gemini 2.5 Flash Lite):**
```json
{
  "misconceptions": [{
    "name": "Incorrect application of distance formula",
    "topic": "Variables",
    "confidence": 0.95
  }]
}
```

**Model B (GPT-5 Nano):**
```json
{
  "misconceptions": [{
    "name": "Incorrect application of Heron's formula",
    "topic": "Variables",
    "confidence": 0.85
  }]
}
```

### Step 3: Fuzzy Clustering

```python
# Similarity: 0.84 > 0.80 → MERGE
cluster_mapping = {
    "Incorrect application of distance formula": "Incorrect application of distance formula",
    "Incorrect application of Heron's formula": "Incorrect application of distance formula"
}
```

### Step 4: Final Report

| Rank | Misconception | Occurrences |
|------|---------------|-------------|
| 1 | Incorrect application of distance formula | **2** |

The report correctly shows 2 models found a formula error, instead of listing two separate single-occurrence errors.

## Related Documentation

- [Grading Workflow](grading-workflow.md) - How evaluations are created
- [Pydantic Models](pydantic-models.md) - Data model reference
- [CLI Reference](cli-reference.md) - Running the analyzer
- [Architecture](architecture.md) - System design
