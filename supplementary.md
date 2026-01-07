# TRACER Supplementary Material (Opus2 Runs)

This supplement contains supporting figures/tables removed from `draft_paper.md` to help fit the ITiCSE paper length constraints. Primary sources are `runs/v2/run_opus2_main/report.md` and `runs/v2/run_opus2_ablation/report.md`.

## S1. Cross-Validation Fold Stability (Main Run)
From `runs/v2/run_opus2_main/fold_results.csv` (semantic=0.55, noise=0.60 selected in all folds):

| Fold | Dev F1 | Test F1 | Dev Specificity | Test Specificity |
| :---: | ---: | ---: | ---: | ---: |
| 1 | 0.691 | 0.707 | 0.851 | 0.837 |
| 2 | 0.691 | 0.708 | 0.851 | 0.840 |
| 3 | 0.692 | 0.705 | 0.845 | 0.864 |
| 4 | 0.705 | 0.651 | 0.850 | 0.842 |
| 5 | 0.694 | 0.700 | 0.846 | 0.859 |

## S2. Detection Outcome Flows (Main vs Ablation)

![Detection Flow (Opus2 Main)](runs/v2/run_opus2_main/assets/hallucinations_sankey.png)
*Figure S1a: Main run outcome flow (label-blind evaluation).*

![Detection Flow (Opus2 Ablation)](runs/v2/run_opus2_ablation/assets/hallucinations_sankey.png)
*Figure S1b: Ablation outcome flow (label-aware matching).*

## S3. Detection Filtering Pipeline
From `runs/v2/run_opus2_main/report.md` and `runs/v2/run_opus2_ablation/report.md`:

| Stage | Main Count | Main % | Ablation Count | Ablation % |
| :--- | ---: | ---: | ---: | ---: |
| Raw detections | 29,164 | 100.0% | 29,164 | 100.0% |
| Null-template filtered | 634 | 2.2% | 634 | 2.2% |
| Noise floor filtered (<0.6) | 16,819 | 57.7% | 12,441 | 42.7% |
| **Evaluated** | **11,711** | **40.2%** | **16,089** | **55.2%** |

## S4. Assignment-Level Performance (Main vs Ablation)
From `runs/v2/run_opus2_main/report.md` and `runs/v2/run_opus2_ablation/report.md`:

| Run | Assignment | Focus | Precision | Recall | F1 |
| :--- | :--- | :--- | ---: | ---: | ---: |
| main | a1 | variables/math | 0.504 | 0.771 | 0.610 |
| main | a2 | loops/control | 0.554 | 0.876 | 0.679 |
| main | a3 | arrays/strings | 0.686 | 0.971 | 0.804 |
| ablation | a1 | variables/math | 0.433 | 0.980 | 0.601 |
| ablation | a2 | loops/control | 0.515 | 0.980 | 0.675 |
| ablation | a3 | arrays/strings | 0.612 | 0.987 | 0.756 |

## S5. Per-Misconception Recall (Main Run)

![Per-Misconception Recall (Main)](runs/v2/run_opus2_main/assets/misconception_recall.png)
*Figure S2: Recall by misconception in the label-blind main run.*

## S6. Prompting Strategy Comparison (Main Run)

![Strategy Comparison (Main)](runs/v2/run_opus2_main/assets/strategy_comparison.png)
*Figure S3: Prompting strategy comparison (see `runs/v2/run_opus2_main/report.md` for McNemar/Cochran tests).*

| Strategy | Precision | Recall | F1 |
| :--- | ---: | ---: | ---: |
| baseline | 0.643 | 0.864 | 0.737 |
| taxonomy | 0.630 | 0.885 | 0.736 |
| chain-of-thought | 0.598 | 0.839 | 0.698 |
| socratic | 0.474 | 0.903 | 0.622 |

## S7. Model Comparison (Main Run)

![Model Comparison (Main)](runs/v2/run_opus2_main/assets/model_comparison.png)
*Figure S4: Model family comparison in the main run.*

| Model | Precision | Recall | F1 |
| :--- | ---: | ---: | ---: |
| claude-haiku-4-5 (reasoning) | 0.776 | 0.880 | 0.825 |
| claude-haiku-4-5 | 0.633 | 0.819 | 0.714 |
| gpt-5.2 (reasoning) | 0.602 | 0.856 | 0.707 |
| gpt-5.2 | 0.594 | 0.846 | 0.698 |
| gemini-3-flash (reasoning) | 0.494 | 0.924 | 0.643 |
| gemini-3-flash | 0.473 | 0.909 | 0.622 |

## S8. Threshold Sensitivity Heatmap (Ablation)

![Threshold Sensitivity Heatmap (Opus2 Ablation)](runs/v2/run_opus2_ablation/assets/threshold_sensitivity_heatmap.png)
*Figure S5: F1 across the threshold grid in the label-aware ablation (`run_opus2_ablation`).*

