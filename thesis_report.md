# LLM Misconception Detection: Analysis Report
_Generated: 2025-12-04T17:35:27.949713+00:00_

## Executive Highlights
- **Matcher Ablation Study**: Comparing fuzzy_only, semantic_only, and hybrid matchers.
- Bootstrap CIs included for statistical rigor.
- Same detection data, different matching strategies.

## Matcher Ablation: Fuzzy vs Semantic vs Hybrid

### Summary (averaged across strategies and models)
| Matcher | Total TP | Total FP | Total FN | Avg Precision | Avg Recall | Avg F1 |
|---------|----------|----------|----------|---------------|------------|--------|
| fuzzy_only | 34 | 798 | 400 | 0.042 | 0.078 | 0.053 |
| hybrid | 298 | 268 | 151 | 0.538 | 0.664 | 0.591 |
| semantic_only | 305 | 271 | 144 | 0.542 | 0.679 | 0.598 |

![Matcher Ablation](docs/report_assets/matcher_ablation.png)

### Precision-Recall by Matcher
![Matcher PR Scatter](docs/report_assets/matcher_pr_scatter.png)

### Full Results Table
| Matcher | Strategy | Model | TP | FP | FN | Precision | Recall | F1 | CI (F1) |
|---------|----------|-------|----|----|----|-----------|--------|----|---------|
| fuzzy_only | baseline | gemini-2.5-flash | 6 | 118 | 48 | 0.048 | 0.111 | 0.067 | 0.02–0.12 |
| fuzzy_only | baseline | gpt-5.1 | 4 | 53 | 50 | 0.070 | 0.074 | 0.072 | 0.02–0.15 |
| fuzzy_only | minimal | gemini-2.5-flash | 6 | 160 | 49 | 0.036 | 0.109 | 0.054 | 0.01–0.11 |
| fuzzy_only | minimal | gpt-5.1 | 2 | 69 | 52 | 0.028 | 0.037 | 0.032 | 0.00–0.08 |
| fuzzy_only | rubric_only | gemini-2.5-flash | 4 | 118 | 50 | 0.033 | 0.074 | 0.045 | 0.01–0.09 |
| fuzzy_only | rubric_only | gpt-5.1 | 6 | 66 | 48 | 0.083 | 0.111 | 0.095 | 0.03–0.17 |
| fuzzy_only | socratic | gemini-2.5-flash | 6 | 147 | 49 | 0.039 | 0.109 | 0.058 | 0.01–0.12 |
| fuzzy_only | socratic | gpt-5.1 | 0 | 67 | 54 | 0.000 | 0.000 | 0.000 | 0.00–0.00 |
| hybrid | baseline | gemini-2.5-flash | 41 | 38 | 16 | 0.519 | 0.719 | 0.603 | 0.49–0.71 |
| hybrid | baseline | gpt-5.1 | 34 | 20 | 22 | 0.630 | 0.607 | 0.618 | 0.50–0.73 |
| hybrid | minimal | gemini-2.5-flash | 39 | 42 | 17 | 0.481 | 0.696 | 0.569 | 0.48–0.67 |
| hybrid | minimal | gpt-5.1 | 33 | 24 | 21 | 0.579 | 0.611 | 0.595 | 0.48–0.71 |
| hybrid | rubric_only | gemini-2.5-flash | 37 | 42 | 22 | 0.468 | 0.627 | 0.536 | 0.42–0.68 |
| hybrid | rubric_only | gpt-5.1 | 39 | 26 | 16 | 0.600 | 0.709 | 0.650 | 0.54–0.75 |
| hybrid | socratic | gemini-2.5-flash | 39 | 52 | 19 | 0.429 | 0.672 | 0.523 | 0.44–0.62 |
| hybrid | socratic | gpt-5.1 | 36 | 24 | 18 | 0.600 | 0.667 | 0.632 | 0.52–0.75 |
| semantic_only | baseline | gemini-2.5-flash | 41 | 38 | 15 | 0.519 | 0.732 | 0.607 | 0.49–0.72 |
| semantic_only | baseline | gpt-5.1 | 34 | 20 | 22 | 0.630 | 0.607 | 0.618 | 0.50–0.73 |
| semantic_only | minimal | gemini-2.5-flash | 41 | 44 | 16 | 0.482 | 0.719 | 0.577 | 0.46–0.69 |
| semantic_only | minimal | gpt-5.1 | 33 | 25 | 21 | 0.569 | 0.611 | 0.589 | 0.47–0.70 |
| semantic_only | rubric_only | gemini-2.5-flash | 41 | 39 | 18 | 0.512 | 0.695 | 0.590 | 0.49–0.71 |
| semantic_only | rubric_only | gpt-5.1 | 39 | 26 | 16 | 0.600 | 0.709 | 0.650 | 0.55–0.76 |
| semantic_only | socratic | gemini-2.5-flash | 40 | 55 | 18 | 0.421 | 0.690 | 0.523 | 0.42–0.62 |
| semantic_only | socratic | gpt-5.1 | 36 | 24 | 18 | 0.600 | 0.667 | 0.632 | 0.51–0.74 |

## Topic Difficulty (Recall)
| Topic | Recall | N |
|-------|--------|---|
| Input | 0.287 | 80 |
| State / Variables | 0.385 | 104 |
| Algebraic Reasoning | 0.792 | 24 |
| Data Types | 0.852 | 88 |
| Input / Data Types | 0.889 | 72 |
| State / Input | 0.906 | 32 |
| Methods | 0.969 | 32 |

![Topic Recall](docs/report_assets/topic_recall_bars.png)

## Topic Heatmap
![Topic Heatmap](docs/report_assets/topic_heatmap.png)

## Hallucination Analysis
![Hallucinations](docs/report_assets/hallucinations.png)

- **Incorrect order of input reading** (18 times)
- **Incorrect data type for input variables** (17 times)
- **Incorrect Scanner method for double input** (13 times)
- **Unnecessary absolute value check for Math.sqrt result** (12 times)
- **Incomplete Input Reading** (12 times)

## Methods
- Data: 60 students × 4 questions (seeded/clean) with manifest-driven ground truth.
- Detection: GPT-5.1 and Gemini 2.5 Flash across strategies (baseline, minimal, rubric_only, socratic).
- Matching: Ablation comparing fuzzy-only, semantic-only (text-embedding-3-large), and hybrid (fuzzy + semantic + topic prior).
- Metrics: Precision/Recall/F1 with bootstrap CIs; agreement via κ; significance via McNemar where applicable.

## Agreement & Significance
- baseline: κ=0.279, McNemar p=0.1949 (stat=1.681) | table={'both_correct': 26, 'only_a': 12, 'only_b': 6, 'both_wrong': 10}
- minimal: κ=0.354, McNemar p=0.3816 (stat=0.766) | table={'both_correct': 27, 'only_a': 10, 'only_b': 6, 'both_wrong': 11}
- rubric_only: κ=0.279, McNemar p=0.1949 (stat=1.681) | table={'both_correct': 26, 'only_a': 6, 'only_b': 12, 'both_wrong': 10}
- socratic: κ=0.548, McNemar p=0.8802 (stat=0.023) | table={'both_correct': 30, 'only_a': 5, 'only_b': 6, 'both_wrong': 13}