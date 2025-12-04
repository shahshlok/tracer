# LLM Misconception Detection: Revamped Analysis
_Generated: 2025-12-04T06:24:30.586836+00:00_

## Executive Highlights
- Hybrid matcher (fuzzy + semantic + topic prior) applied across all strategies/models.
- Bootstrap CIs and calibration metrics included; heavy computations allowed.
- Overall ECE: 0.443 | Brier: 0.445

## Strategy × Model Performance
| Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (Precision) | CI (Recall) | CI (F1) |
|----------|-------|----|----|----|-----------|--------|----|----------------|-------------|---------|
| baseline | gemini-2.5-flash | 111 | 114 | 46 | 0.493 | 0.707 | 0.581 | 0.43–0.55 | 0.63–0.77 | 0.51–0.64 |
| baseline | gpt-5.1 | 89 | 53 | 59 | 0.627 | 0.601 | 0.614 | 0.55–0.70 | 0.52–0.67 | 0.53–0.68 |
| minimal | gemini-2.5-flash | 123 | 144 | 56 | 0.461 | 0.687 | 0.552 | 0.40–0.51 | 0.61–0.75 | 0.49–0.61 |
| minimal | gpt-5.1 | 109 | 69 | 61 | 0.612 | 0.641 | 0.626 | 0.55–0.68 | 0.57–0.71 | 0.56–0.69 |
| rubric_only | gemini-2.5-flash | 108 | 138 | 68 | 0.439 | 0.614 | 0.512 | 0.38–0.51 | 0.54–0.69 | 0.45–0.58 |
| rubric_only | gpt-5.1 | 107 | 92 | 68 | 0.538 | 0.611 | 0.572 | 0.47–0.61 | 0.54–0.68 | 0.51–0.64 |
| socratic | gemini-2.5-flash | 51 | 66 | 33 | 0.436 | 0.607 | 0.507 | 0.35–0.54 | 0.50–0.73 | 0.41–0.61 |
| socratic | gpt-5.1 | 45 | 51 | 36 | 0.469 | 0.556 | 0.508 | 0.38–0.57 | 0.44–0.67 | 0.41–0.61 |

## Topic Difficulty (Recall)
| Topic | Recall | N |
|-------|--------|---|
| Input | 0.230 | 178 |
| State / Variables | 0.443 | 334 |
| State / Representation | 0.500 | 6 |
| Algebraic Reasoning | 0.773 | 132 |
| Data Types | 0.788 | 198 |
| State / Input | 0.845 | 110 |
| Input / Data Types | 0.875 | 96 |
| Methods | 1.000 | 72 |

## Calibration & Hallucinations
- Calibration curves: see `docs/report_assets/calibration.png`
- Hallucination bar chart: see `docs/report_assets/hallucinations.png`

- **Hardcoding Input Values Instead of Reading from User** (14 times)
- **Incorrect data type for input variables** (11 times)
- **Incomplete Input Reading** (10 times)
- **Missing input for 'price'** (8 times)
- **Division by zero** (6 times)

## Topic Heatmap
![Topic Heatmap](docs/report_assets/topic_heatmap.png)

## Methods
- Data: 60 students × 4 questions (seeded/clean) with manifest-driven ground truth.
- Detection: GPT-5.1 and Gemini 2.5 Flash across strategies (baseline, minimal, rubric_only, socratic).
- Matching: Hybrid fusion of fuzzy similarity, semantic embeddings (OpenAI/OpenRouter optional), and topic priors.
- Metrics: Precision/Recall/F1 with bootstrap CIs; calibration (ECE/Brier); agreement via κ; significance via McNemar where applicable.

## Agreement & Significance
- baseline: κ=0.515, McNemar p=0.0296 (stat=4.735) | table={'both_correct': 78, 'only_a': 23, 'only_b': 10, 'both_wrong': 36}
- minimal: κ=0.282, McNemar p=0.5440 (stat=0.368) | table={'both_correct': 83, 'only_a': 30, 'only_b': 25, 'both_wrong': 31}
- rubric_only: κ=0.459, McNemar p=0.9399 (stat=0.006) | table={'both_correct': 79, 'only_a': 22, 'only_b': 22, 'both_wrong': 46}
- socratic: κ=0.611, McNemar p=0.5186 (stat=0.417) | table={'both_correct': 36, 'only_a': 9, 'only_b': 6, 'both_wrong': 27}