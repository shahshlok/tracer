# LLM Misconception Detection Analysis Report

Generated: 2025-12-03T22:09:42.379572+00:00

## Summary

| Strategy | TP | FP | FN | Precision | Recall | F1 |
|----------|----|----|----|-----------|---------|----|
| rubric_only | 19 | 49 | 11 | 0.279 | 0.633 | 0.388 |
| baseline | 15 | 49 | 16 | 0.234 | 0.484 | 0.316 |
| minimal | 23 | 52 | 7 | 0.307 | 0.767 | 0.438 |
| socratic | 19 | 58 | 12 | 0.247 | 0.613 | 0.352 |

## Per-Strategy Details

### Rubric_Only

- Total detection files: 40
- True Positives: 19
- False Positives: 49
- False Negatives: 11
- Precision: 0.279
- Recall: 0.633
- F1 Score: 0.388

#### Per-Model Breakdown

| Model | TP | FP | Interesting | Total |
|-------|----|----|-------------|-------|
| gpt-5.1 | 8 | 23 | 0 | 31 |
| gemini-2.5-flash | 11 | 26 | 1 | 38 |

### Baseline

- Total detection files: 40
- True Positives: 15
- False Positives: 49
- False Negatives: 16
- Precision: 0.234
- Recall: 0.484
- F1 Score: 0.316

#### Per-Model Breakdown

| Model | TP | FP | Interesting | Total |
|-------|----|----|-------------|-------|
| gpt-5.1 | 5 | 20 | 0 | 25 |
| gemini-2.5-flash | 10 | 29 | 2 | 41 |

### Minimal

- Total detection files: 40
- True Positives: 23
- False Positives: 52
- False Negatives: 7
- Precision: 0.307
- Recall: 0.767
- F1 Score: 0.438

#### Per-Model Breakdown

| Model | TP | FP | Interesting | Total |
|-------|----|----|-------------|-------|
| gpt-5.1 | 12 | 17 | 0 | 29 |
| gemini-2.5-flash | 11 | 35 | 4 | 50 |

### Socratic

- Total detection files: 40
- True Positives: 19
- False Positives: 58
- False Negatives: 12
- Precision: 0.247
- Recall: 0.613
- F1 Score: 0.352

#### Per-Model Breakdown

| Model | TP | FP | Interesting | Total |
|-------|----|----|-------------|-------|
| gpt-5.1 | 9 | 22 | 0 | 31 |
| gemini-2.5-flash | 10 | 36 | 3 | 49 |


## Interesting Discoveries

These are misconceptions detected in CLEAN files (no injection). They may represent:
- Genuine issues in the generated 'correct' code
- LLM over-detection / false alarms
- Edge cases worth investigating

### Rubric_Only (1 discoveries)

- **Bradley_Nancy_674303** Q2 (gemini-2.5-flash)
  - Detected: Missing nextLine() after nextDouble() block
  - Matched to: INP-02 (score: 0.72)

### Baseline (2 discoveries)

- **Benson_Jennifer_117472** Q4 (gemini-2.5-flash)
  - Detected: Incorrect input parsing for two numbers on single line
  - Matched to: INP-01 (score: 0.75)
- **Benson_Jennifer_117472** Q3 (gemini-2.5-flash)
  - Detected: Incorrect re-assignment of input variables
  - Matched to: STA-03 (score: 0.73)

### Minimal (4 discoveries)

- **Brown_Michael_716436** Q2 (gemini-2.5-flash)
  - Detected: Lack of output formatting for currency
  - Matched to: STA-05 (score: 0.72)
- **Bradley_Nancy_674303** Q2 (gemini-2.5-flash)
  - Detected: Conditional Scanner Input Reading
  - Matched to: INP-03 (score: 0.71)
- **Allen_Kathryn_839472** Q2 (gemini-2.5-flash)
  - Detected: Lack of output formatting for currency
  - Matched to: STA-05 (score: 0.74)
- **Anderson_Ryan_662436** Q4 (gemini-2.5-flash)
  - Detected: Incorrect Scanner usage for multiple inputs on one line
  - Matched to: INP-02 (score: 0.76)

### Socratic (3 discoveries)

- **Bradley_Nancy_674303** Q2 (gemini-2.5-flash)
  - Detected: Input handling for invalid numerical input
  - Matched to: INP-03 (score: 0.73)
- **Benson_Jennifer_117472** Q4 (gemini-2.5-flash)
  - Detected: Incorrect handling of Scanner input for multiple values on one line
  - Matched to: INP-01 (score: 0.73)
- **Benson_Jennifer_117472** Q3 (gemini-2.5-flash)
  - Detected: Overwriting Input Variables Before Calculation
  - Matched to: STA-03 (score: 0.71)
