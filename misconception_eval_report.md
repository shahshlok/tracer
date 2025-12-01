# Misconception Detection Evaluation Report

**Strategy:** minimal
**Total Submissions:** 40

## Overall Metrics

| Metric | Value |
|--------|-------|
| Precision | 85.7% |
| Recall | 46.2% |
| F1 Score | 60.0% |
| Accuracy | 80.0% |

## Confusion Matrix

| | Predicted Error | Predicted Correct |
|---|---|---|
| **Actual Error** | TP: 6 | FN: 7 |
| **Actual Correct** | FP: 1 | TN: 26 |

## Per-Model Performance

| Model | TP | FP | FN | TN | Precision | Recall | F1 |
|-------|----|----|----|----|-----------|--------|-----|
| gemini-2.5-flash-lite | 5 | 1 | 8 | 26 | 83.3% | 38.5% | 52.6% |
| gpt-5-nano | 6 | 1 | 7 | 26 | 85.7% | 46.2% | 60.0% |

## Per-Misconception Detection Rate

| Misconception ID | Total | Detected | Rate |
|------------------|-------|----------|------|
| CONST001 | 1 | 1 | 100.0% |
| CONST002 | 1 | 1 | 100.0% |
| DT002 | 1 | 1 | 100.0% |
| DT003 | 2 | 0 | 0.0% |
| INPUT001 | 2 | 0 | 0.0% |
| INPUT002 | 1 | 0 | 0.0% |
| INPUT003 | 1 | 0 | 0.0% |
| OTHER001 | 1 | 1 | 100.0% |
| OTHER002 | 1 | 0 | 0.0% |
| VAR001 | 2 | 2 | 100.0% |

## Detailed Results

### Ramirez_Zoey_200134 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Flores_Emily_200126 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Walker_Chloe_200124 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Anderson_Noah_200113 - q4 [FP]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** Undeclared Variable Used in Output (conf: 1.00)
- **gpt-5-nano:** Undefined variable v0 in output (conf: 0.55)

### Torres_Daniel_200148 - q3 [FN]

**Ground Truth:** INPUT003
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Ramirez_Zoey_200134 - q4 [FN]

**Ground Truth:** DT003
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Walker_Chloe_200124 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Hill_Michael_200140 - q3 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Rodriguez_Owen_200104 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Anderson_Noah_200113 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Smith_Mason_200158 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Flores_Emily_200126 - q4 [FN]

**Ground Truth:** INPUT002
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Ramirez_Zoey_200134 - q3 [TP]

**Ground Truth:** CONST001
- **gemini-2.5-flash-lite:** Incorrect Exponentiation Operator -> CONST001 (conf: 1.00)
- **gpt-5-nano:** Exponent operator misconception (conf: 0.80)

### Torres_Daniel_200148 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Flores_Emily_200126 - q3 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Hill_Michael_200140 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Lopez_Abigail_200117 - q1 [TP]

**Ground Truth:** OTHER001
- **gemini-2.5-flash-lite:** Incorrect formula implementation (conf: 1.00)
- **gemini-2.5-flash-lite:** Incorrect output message (conf: 1.00)
- **gpt-5-nano:** Incorrect formula and order of operations -> VAR001 (conf: 0.65)

### Torres_Daniel_200148 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Anderson_Noah_200113 - q3 [FN]

**Ground Truth:** DT003
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Walker_Chloe_200124 - q3 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Hill_Michael_200140 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Robinson_Noah_200127 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Lopez_Abigail_200117 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Robinson_Noah_200127 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Torres_Daniel_200148 - q1 [TP]

**Ground Truth:** DT002
- **gemini-2.5-flash-lite:** Incorrect use of integer casting with doubles -> DT002 (conf: 0.90)
- **gpt-5-nano:** Integer division due to explicit int casting -> DT002 (conf: 0.90)

### Lopez_Abigail_200117 - q2 [FN]

**Ground Truth:** INPUT001
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Smith_Mason_200158 - q3 [TP]

**Ground Truth:** CONST002
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** Missing square root -> CONST002 (conf: 0.75)

### Robinson_Noah_200127 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Hill_Michael_200140 - q1 [TP]

**Ground Truth:** VAR001
- **gemini-2.5-flash-lite:** Incorrect operator precedence -> VAR001 (conf: 1.00)
- **gpt-5-nano:** Operator precedence error in acceleration formula -> VAR001 (conf: 0.65)

### Rodriguez_Owen_200104 - q3 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Ramirez_Zoey_200134 - q1 [TP]

**Ground Truth:** VAR001
- **gemini-2.5-flash-lite:** Operator Precedence Misunderstanding -> VAR001 (conf: 1.00)
- **gpt-5-nano:** Incorrect operator precedence in acceleration formula -> VAR001 (conf: 0.65)

### Rodriguez_Owen_200104 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Flores_Emily_200126 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Smith_Mason_200158 - q4 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Lopez_Abigail_200117 - q3 [FN]

**Ground Truth:** INPUT001
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Smith_Mason_200158 - q2 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Robinson_Noah_200127 - q3 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Rodriguez_Owen_200104 - q2 [FN]

**Ground Truth:** OTHER002
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Anderson_Noah_200113 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected

### Walker_Chloe_200124 - q1 [TN]

**Ground Truth:** Correct
- **gemini-2.5-flash-lite:** No misconceptions detected
- **gpt-5-nano:** No misconceptions detected
