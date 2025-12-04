# LLM Misconception Detection: Research Analysis

Generated: 2025-12-04T03:24:08.468828+00:00

## Executive Summary

### Strategy Comparison Matrix
| Strategy | Precision | Recall | F1 Score | TP | FP | FN | Conf. Gap |
|----------|-----------|--------|----------|----|----|----|-----------|
| **rubric_only** | 0.279 | 0.633 | **0.388** | 19 | 49 | 11 | -0.013 |
| **baseline** | 0.234 | 0.484 | **0.316** | 15 | 49 | 16 | 0.015 |
| **minimal** | 0.307 | 0.767 | **0.438** | 23 | 52 | 7 | 0.031 |
| **socratic** | 0.234 | 0.600 | **0.336** | 18 | 59 | 12 | -0.006 |

## Model Showdown: GPT-5.1 vs Gemini-2.5-Flash

Aggregate performance comparison across all strategies.

### Aggregate Metrics
| Model | TP | FP | Precision | Recall (est) |
|-------|----|----|-----------|--------------|
| **gpt-5.1** | 34 | 82 | 0.293 | 0.340 |
| **gemini-2.5-flash** | 41 | 127 | 0.244 | 0.410 |

### Statistical Significance (McNemar's Test)
Testing the null hypothesis that both models have equal sensitivity (recall).

**Comparison: gpt-5.1 vs gemini-2.5-flash**
- **Statistic**: 0.8897
- **P-Value**: 3.4556e-01
- **Result**: No Significant Difference (p >= 0.05)

**Contingency Table**
| | gemini-2.5-flash Correct | gemini-2.5-flash Wrong |
|---|---|---|
| **gpt-5.1 Correct** | 20 | 14 |
| **gpt-5.1 Wrong** | 20 | 46 |

## Performance by Category

Breakdown of detection performance by misconception category (Topic).
| Category | Recall | Precision | TP | FN | FP |
|----------|--------|-----------|----|----|----|
| Algebraic Reasoning | 0.62 | 1.00 | 5 | 3 | 0 |
| Data Types | 0.81 | 0.58 | 29 | 7 | 21 |
| Input | 0.14 | 0.38 | 5 | 31 | 8 |
| Input / Data Types | 0.81 | 0.59 | 13 | 3 | 9 |
| State / Representation | 0.00 | 0.00 | 0 | 0 | 0 |
| State / Variables | 0.50 | 0.12 | 2 | 2 | 14 |

## Deep Dive: Misconception Difficulty


### Hardest Misconceptions (Low Recall)
| ID | Name | Topic | Recall | TP | FN |
|----|------|-------|--------|----|----|
| INP-02 | One Scanner Call Reads All | Input | 0.00 | 0 | 24 |
| TYP-05 | Narrowing Cast in Division | Data Types | 0.00 | 0 | 0 |
| STA-03 | Variable Clobbering | State / Variables | 0.00 | 0 | 0 |
| INP-03 | Scanner Type Mismatch | Input / Data Types | 0.00 | 0 | 0 |
| STA-07 | Swapped Variables After Read | State / Variables | 0.00 | 0 | 0 |

### Most Confusing Misconceptions (High FP)
| ID | Name | Topic | FP Count |
|----|------|-------|----------|
| TYP-05 | Narrowing Cast in Division | Data Types | 11 |
| TYP-01 | Integer Division Trap | Data Types | 10 |
| INP-03 | Scanner Type Mismatch | Input / Data Types | 9 |
| INP-01 | Prompt-Logic Mismatch | Input | 7 |
| STA-07 | Swapped Variables After Read | State / Variables | 6 |

## Hallucination Analysis

Recurring false positives that do not match any known misconception ID.

### Rubric_Only Hallucinations
- **"Using int instead of double for velocities and time"** (3 times)
  - Example: Anderson_Ryan_662436 Q1
  - Models: openai/gpt-5.1
- **"Calculation before input"** (2 times)
  - Example: Benson_Jennifer_117472 Q1
  - Models: google/gemini-2.5-flash
- **"Not reading all required coordinate values from input"** (2 times)
  - Example: Andrade_Angela_166675 Q3
  - Models: openai/gpt-5.1

### Baseline Hallucinations
- **"Incomplete Input Reading"** (2 times)
  - Example: Andrade_Angela_166675 Q3
  - Models: google/gemini-2.5-flash
- **"Using int instead of double for quantities that can be decimal"** (2 times)
  - Example: Anderson_Ryan_662436 Q1
  - Models: openai/gpt-5.1
- **"Incorrect Data Type for Numerical Input"** (2 times)
  - Example: Arnold_Andrew_105200 Q2
  - Models: google/gemini-2.5-flash

### Minimal Hallucinations
- **"Not reading all required coordinate values from input"** (2 times)
  - Example: Andrade_Angela_166675 Q3
  - Models: openai/gpt-5.1
- **"Partial Input Reading"** (2 times)
  - Example: Anderson_Elizabeth_799204 Q4
  - Models: google/gemini-2.5-flash

## Interesting Discoveries (Clean Files)

Potential genuine issues found in clean files.
### Rubric_Only
- **Bradley_Nancy_674303** Q2: Missing nextLine() after nextDouble() block
  - Matched to: INP-02 (0.72)
### Baseline
- **Benson_Jennifer_117472** Q4: Incorrect input parsing for two numbers on single line
  - Matched to: INP-01 (0.75)
- **Benson_Jennifer_117472** Q3: Incorrect re-assignment of input variables
  - Matched to: STA-03 (0.73)
### Minimal
- **Brown_Michael_716436** Q2: Lack of output formatting for currency
  - Matched to: STA-05 (0.72)
- **Bradley_Nancy_674303** Q2: Conditional Scanner Input Reading
  - Matched to: INP-03 (0.71)
- **Allen_Kathryn_839472** Q2: Lack of output formatting for currency
  - Matched to: STA-05 (0.74)
- **Anderson_Ryan_662436** Q4: Incorrect Scanner usage for multiple inputs on one line
  - Matched to: INP-02 (0.76)
### Socratic
- **Bradley_Nancy_674303** Q2: Input handling for invalid numerical input
  - Matched to: INP-03 (0.73)
- **Benson_Jennifer_117472** Q4: Incorrect handling of Scanner input for multiple values on one line
  - Matched to: INP-01 (0.73)
- **Benson_Jennifer_117472** Q3: Overwriting Input Variables Before Calculation
  - Matched to: STA-03 (0.71)