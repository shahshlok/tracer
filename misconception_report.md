# Misconception Analysis Report

**Generated:** 2025-11-30 03:58:18
**Total Students Analyzed:** 25
**Total Misconceptions Detected:** 49

---

### Most Difficult Areas (by % of class affected)

| Rank | Topic | Total Misconceptions | Students Affected | Avg Confidence |
|------|-------|---------------------|-------------------|----------------|
| 1 | Other | 24 | 6/25 (24%) | 0.88 |
| 2 | Variables | 8 | 6/25 (24%) | 0.86 |
| 3 | Data Types | 6 | 4/25 (16%) | 0.89 |
| 4 | Constants | 4 | 4/25 (16%) | 0.81 |
| 5 | Reading input from the keyboard | 7 | 3/25 (12%) | 0.86 |

### 'Other' Category Breakdown

The 'Other' category contains 24 misconceptions that don't fit the 4 course topics.
These are grouped by semantic similarity:

| Sub-category | Count | Examples |
|--------------|-------|----------|
| Problem Understanding | 10 | "Wrong approach / Solution not matching p...", "Misinterpretation of Problem Requirement..." |
| Miscellaneous | 8 | "Incorrect sign in velocity change", "Incorrect problem implementation (fuel-c..." |
| Formula Application | 5 | "Formula Misapplication", "Solved a different problem (acceleration..." |
| Output Issues | 1 | "Incorrect Output Value" |

### Most Common Misconceptions

| Rank | Misconception | Topic | Occurrences | Models Agreeing |
|------|---------------|-------|-------------|-----------------|
| 1 | Incorrect data type usage | Data Types | 2 | 1 (gemini-2.5-flash-lite) |
| 2 | Incorrect input handling | Reading input from the keyboard | 2 | 1 (gemini-2.5-flash-lite) |
| 3 | Misinterpreting Problem Requirements | Other | 2 | 1 (gemini-2.5-flash-lite) |
| 4 | Incorrect application of distance formula | Other | 2 | 1 (gemini-2.5-flash-lite) |
| 5 | Incorrect formula application | Variables | 2 | 1 (gemini-2.5-flash-lite) |
| 6 | Incorrect use of exponentiation operator | Data Types | 2 | 1 (gemini-2.5-flash-lite) |
| 7 | Incorrect operator precedence | Variables | 2 | 2 (gpt-5-nano, gemini-2.5-flash-lite) |
| 8 | Misinterpreting the problem and irrelevant input/output | Other | 1 | 1 (gemini-2.5-flash-lite) |
| 9 | Wrong data types for velocity/time | Variables | 1 | 1 (gpt-5-nano) |
| 10 | Inappropriate use of integer data types | Data Types | 1 | 1 (gemini-2.5-flash-lite) |

---

## Per-Question Analysis

| Question | Submissions | Misconception Rate | Top Misconception | Topic Breakdown |
|----------|-------------|-------------------|-------------------|-----------------|
| Q1 | 25 | 7/25 (28%) | Incorrect operator precedence | Other: 7, Variables: 5, Reading: 1 |
| Q2 | 25 | 4/25 (16%) | Incorrect input handling | Other: 5, Reading: 3, Data: 1 |
| Q3 | 24 | 6/24 (25%) | Misinterpreting Problem Requirements | Other: 5, Constants: 4, Data: 3, Reading: 1, Variables: 1 |
| Q4 | 23 | 8/23 (35%) | Incorrect input handling | Other: 7, Reading: 2, Data: 2, Variables: 2 |

---

## Progression Analysis: Q3 → Q4

Tracks whether students who struggled with Q3 also struggled with Q4,
helping identify persistent misconceptions vs learning/improvement.

### Student Progression Summary

| Category | Count | Percentage |
|----------|-------|------------|
| Struggled in both Q3 & Q4 | 4 | 17% |
| Improved (Q3 issues → Q4 clean) | 2 | 9% |
| Regressed (Q3 clean → Q4 issues) | 4 | 17% |
| Consistent (no issues in either) | 13 | 57% |

### Key Metrics

- **Misconception Persistence Rate:** 67% of Q3 strugglers also struggled in Q4
- **Improvement Rate:** 33% of Q3 strugglers had no issues in Q4
- **Students with incomplete data:** 1 (only Q3 or only Q4)

---

## Model Agreement Analysis

| Model | Misconceptions Detected |
|-------|------------------------|
| gemini-2.5-flash-lite | 31 |
| gpt-5-nano | 18 |


---

## Legend: Formulas and Metrics

This section explains how each metric in the report is calculated:

### Executive Summary Tables

**Most Difficult Areas (by % of class affected)**

- **Topic**: The unique topic where misconceptions were detected
- **Total Misconceptions**: Total count of misconceptions flagged at this Topic
- **Students Affected**: Count and percentage of unique students with misconceptions at this Topic

  $$\text{Students Affected \%} = \frac{\text{unique students with misconceptions}}{\text{total students}} \times 100\%$$

- **Avg Confidence**: Average model confidence for misconceptions at this Topic

  $$\text{Avg Confidence} = \frac{\sum \text{confidence scores}}{\text{count(misconceptions)}}$$

Sorted by: Number of students affected (descending), then by total misconceptions (descending)

**Most Common Misconceptions**

- **Occurrences**: Number of times this specific misconception was detected across all students
- **Models Agreeing**: Number of different models that detected this misconception
  - Shows the specific model names that flagged it

### Model Agreement Analysis

- **Misconceptions Detected**: Total number of misconceptions each model identified across all students

### Confidence Scores

All confidence scores range from 0.0 to 1.0:

- **0.0 - 0.5**: Low confidence (uncertain/borderline misconception)
- **0.5 - 0.7**: Moderate confidence
- **0.7 - 0.9**: High confidence
- **0.9 - 1.0**: Very high confidence (strong evidence of misconception)

### Progression Analysis Metrics

- **Misconception Persistence Rate**: Percentage of students who had misconceptions in Q3 that also had misconceptions in Q4

  $$\text{Persistence Rate} = \frac{\text{struggled in both Q3 \& Q4}}{\text{struggled in Q3}} \times 100\%$$

- **Improvement Rate**: Percentage of Q3 strugglers who had no misconceptions in Q4

  $$\text{Improvement Rate} = \frac{\text{improved (Q3 issues, Q4 clean)}}{\text{struggled in Q3}} \times 100\%$$

- **Persistent Misconceptions**: Same misconception name detected in both Q3 and Q4 for the same student

---

*Report generated by Ensemble Evaluation CLI - Misconception Analyzer*