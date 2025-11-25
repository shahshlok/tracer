# Misconception Analysis Report

**Generated:** 2025-11-25 12:31:17
**Total Students Analyzed:** 25
**Total Misconceptions Detected:** 56

---

## Executive Summary

### Most Difficult Areas (by % of class affected)

| Rank | Topic | Total Misconceptions | Students Affected | Avg Confidence |
|------|-------|---------------------|-------------------|----------------|
| 1 | Variables | 39 | 11/25 (44%) | 0.88 |
| 2 | Reading input from the keyboard | 9 | 4/25 (16%) | 0.86 |
| 3 | Data Types | 5 | 4/25 (16%) | 0.87 |
| 4 | Constants | 3 | 3/25 (12%) | 0.75 |

### Most Common Misconceptions

| Rank | Misconception | Topic | Occurrences | Models Agreeing |
|------|---------------|-------|-------------|-----------------|
| 1 | Missing Semicolon | Variables | 2 | 1 (gemini-2.5-flash-lite) |
| 2 | Incorrect input handling | Reading input from the keyboard | 2 | 1 (gemini-2.5-flash-lite) |
| 3 | Incorrect formula application | Variables | 2 | 1 (gemini-2.5-flash-lite) |
| 4 | Incorrect operator precedence | Variables | 2 | 2 (gemini-2.5-flash-lite, gpt-5-nano) |
| 5 | Syntax errors prevent compilation | Variables | 1 | 1 (gpt-5-nano) |
| 6 | Missing semicolon after print statement | Variables | 1 | 1 (gpt-5-nano) |
| 7 | Misinterpreting the problem and irrelevant input/output | Variables | 1 | 1 (gemini-2.5-flash-lite) |
| 8 | Incorrect data type usage | Variables | 1 | 1 (gemini-2.5-flash-lite) |
| 9 | Wrong data types for velocity/time | Variables | 1 | 1 (gpt-5-nano) |
| 10 | Inappropriate use of integer data types | Data Types | 1 | 1 (gemini-2.5-flash-lite) |

---

## Per-Question Analysis

| Question | Submissions | Misconception Rate | Top Misconception | Topic Breakdown |
|----------|-------------|-------------------|-------------------|-----------------|
| Q1 | 25 | 10/25 (40%) | Incorrect operator precedence | Variables: 15, Reading: 3 |
| Q2 | 25 | 5/25 (20%) | Missing Semicolon | Variables: 6, Reading: 3, Data: 1 |
| Q3 | 24 | 6/24 (25%) | Misinterpreting Problem Requirements | Variables: 8, Constants: 3, Data: 2, Reading: 1 |
| Q4 | 23 | 9/23 (39%) | Missing semicolon after print statement | Variables: 10, Reading: 2, Data: 2 |

---

## Progression Analysis: Q3 → Q4

Tracks whether students who struggled with Q3 also struggled with Q4,
helping identify persistent misconceptions vs learning/improvement.

### Student Progression Summary

| Category | Count | Percentage |
|----------|-------|------------|
| Struggled in both Q3 & Q4 | 4 | 17% |
| Improved (Q3 issues → Q4 clean) | 2 | 9% |
| Regressed (Q3 clean → Q4 issues) | 5 | 22% |
| Consistent (no issues in either) | 12 | 52% |

### Key Metrics

- **Misconception Persistence Rate:** 67% of Q3 strugglers also struggled in Q4
- **Improvement Rate:** 33% of Q3 strugglers had no issues in Q4
- **Students with incomplete data:** 1 (only Q3 or only Q4)

---

## Model Agreement Analysis

| Model | Misconceptions Detected |
|-------|------------------------|
| gemini-2.5-flash-lite | 35 |
| gpt-5-nano | 21 |


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