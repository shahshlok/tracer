# LLM Misconception Detection Analysis Report

Generated: 2025-12-03T06:53:53.877431+00:00

## Summary

| Strategy | TP  | FP  | FN  | Precision | Recall | F1    |
| -------- | --- | --- | --- | --------- | ------ | ----- |
| minimal  | 21  | 51  | 12  | 0.292     | 0.636  | 0.400 |

## Per-Strategy Details

### Minimal

- Total detection files: 43
- True Positives: 21
- False Positives: 51
- False Negatives: 12
- Precision: 0.292
- Recall: 0.636
- F1 Score: 0.400

#### Per-Model Breakdown

| Model            | TP  | FP  | Interesting | Total |
| ---------------- | --- | --- | ----------- | ----- |
| gpt-5.1          | 10  | 19  | 0           | 29    |
| gemini-2.5-flash | 11  | 32  | 3           | 46    |

## Interesting Discoveries

These are misconceptions detected in CLEAN files (no injection). They may represent:

- Genuine issues in the generated 'correct' code
- LLM over-detection / false alarms
- Edge cases worth investigating

### Minimal (3 discoveries)

- **Allen_Kathryn_839472** Q2 (gemini-2.5-flash)
    - Detected: Lack of output formatting for currency
    - Matched to: STA-05 (score: 0.74)
- **Benson_Jennifer_117472** Q4 (gemini-2.5-flash)
    - Detected: Reading multiple inputs on one line
    - Matched to: INP-01 (score: 0.71)
- **Bradley_Nancy_674303** Q1 (gemini-2.5-flash)
    - Detected: Ternary Operator for Division by Zero
    - Matched to: TYP-01 (score: 0.52)
