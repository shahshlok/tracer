# Thesis Report Revamp: Deep-Dive Documentation

This document explains the new analysis/reporting pipeline for your CS1 misconception-detection experiment in enough detail that a new contributor can be productive without reading the code first.

It covers:
- End-to-end data flow from `authentic_seeded/manifest.json` and `detections/` to `thesis_report.md`.
- The hybrid matcher (fuzzy + semantic + topic prior) and why it’s a novel contribution.
- Metrics, statistical tests, calibration, and agreement calculations.
- Generated visualizations and how they map to the underlying data.
- How to run the tooling and where to plug in new models/strategies.

---

## 1. High-Level Overview

At a high level, the revamped pipeline does this:

```text
manifest.json + groundtruth.json + detections/*.json
                 │
                 ▼
        [Data assembly / hybrid matching]
                 │
                 ├── detections_df (per detection row)
                 └── opportunities_df (per injected misconception opportunity)
                 │
                 ▼
        [Metrics + Statistical Analysis]
                 │
                 ├── per-strategy×model precision/recall/F1 (+ CIs)
                 ├── per-topic difficulty
                 ├── calibration + Brier
                 ├── agreement (κ) and McNemar tests
                 ▼
        [Visualizations + Report Generation]
                 │
                 ├── docs/report_assets/*.png
                 └── thesis_report.md + thesis_report.json
```

Two main entry points:
- `uv run analyze` — runs analysis only on existing detections.
- `uv run pipeline` — full pipeline: generate dataset → run LLMs → analyze and write report.

---

## 2. Data Sources and Schemas

### 2.1 Ground Truth (`data/a2/groundtruth.json`)

This file defines the misconception universe. Each entry:
- `id`: short code (e.g., `INP-02`).
- `misconception_name`: human-readable name.
- `misconception_explanation`: explanation of the misconception.
- `student_thinking`: canonicalized “student belief”.
- `category`: conceptual category (e.g., `"Input / Data Types"`).

This is treated as the authoritative list of misconceptions that can be seeded into student code.

### 2.2 Manifest (`authentic_seeded/manifest.json`)

This file defines the experimental design: which student/question pairs are seeded with which misconception.

For each student:
- `folder_name`: student identifier matching detection files (e.g., `Anderson_Ryan_662436`).
- `files`: mapping of question ID (`"Q1"`–`"Q4"`) to:
  - `type`: `"SEEDED"` or `"CLEAN"`.
  - `misconception_id`: ID from `groundtruth.json` (for seeded).
  - `misconception_name`: redundant human-readable name.
  - `instruction`: description of how to inject the bug.

This gives us the **expected misconception** or indicates that a file is truly clean.

### 2.3 Detections (`detections/<strategy>/*.json`)

For each prompting strategy (`baseline`, `minimal`, `rubric_only`, `socratic`), there is a directory with one JSON per student-question:

```json
{
  "student": "Powell_Thomas_498799",
  "question": "Q1",
  "status": "success",
  "models": {
    "openai/gpt-5.1": {
      "misconceptions": [ ... ],
      "count": 1
    },
    "google/gemini-2.5-flash": {
      "misconceptions": [ ... ],
      "count": 1
    }
  },
  "timestamp": "..."
}
```

Each `misconceptions` entry includes:
- `topic`, `task`, `name`, `description`.
- `severity`, `category` (procedural/conceptual).
- `student_belief`, `correct_understanding`, `symptoms`, `root_cause`, `remediation_hint`, `related_concepts`.
- `confidence` and `confidence_rationale`.
- `evidence` (code snippets, file, line spans).

These rich descriptions are used both for matching against ground truth and for error/hallucination analysis.

---

## 3. Hybrid Matcher: Fuzzy + Semantic + Topic Priors

File: `utils/matching/hybrid.py`

### 3.1 Goals

The hybrid matcher aims to answer:

> “Given an LLM’s misconception description, which ground-truth misconception (if any) is it actually talking about?”

This is central to the research question: we need to decide whether a detection is a **true positive**, **false positive**, or **interesting** (in clean files).

The previous approach:
- Used fuzzy name/description matching and a separate semantic matcher, but combined them in an ad-hoc way.

The new hybrid matcher:
- Uses both signals in a controlled fusion.
- Adds a **topic prior** so we exploit the known conceptual category without overfitting.

### 3.2 Inputs & Outputs

Inputs:
- `detection`: single LLM detection dict (name, description, student_belief, topic).
- `groundtruth`: full list of ground truth misconceptions.
- Thresholds and blending parameters:
  - `fuzzy_threshold` (default ~0.55).
  - `semantic_threshold` (default ~0.65).
  - `blend_weight` (e.g., 0.55 = slightly favor fuzzy).

Output: `HybridMatchResult`:
- `matched_id`: chosen ground-truth ID (or `None`).
- `score`: blended score used for ranking.
- `detail`: human-readable score breakdown (fuzzy/semantic/prior).
- `fuzzy_score`, `semantic_score`, `prior_bonus`: components that fed the final blend.

### 3.3 Algorithm (Step-by-Step)

For each detection:

1. **Fuzzy match**  
   - Use `fuzzy_match_misconception(detected_name, detected_description, groundtruth, threshold)`.
   - This considers:
     - Sequence similarity between detection and GT names (`difflib.SequenceMatcher`).
     - Token overlap between names and between descriptions/explanations.
   - Returns `(fuzzy_id, fuzzy_score, method)`.

2. **Semantic match**  
   - Use `semantic_match_misconception(detected_name, description, student_belief, groundtruth, threshold)`.
   - Builds a concatenated “detection text” and “ground truth text”:
     - `Misconception: ...`, `Description: ...`, `Student believes: ...`.
   - Uses OpenAI embeddings (or other providers, future) to compute cosine similarity between detection and each GT.
   - Returns `(sem_id, sem_score, "semantic")` above a similarity threshold.

3. **Candidate set construction**  
   - Add fuzzy match to candidate set (if above threshold).
   - Add semantic match to candidate set (if above threshold).
   - If we have nothing, we run semantic matching again with **threshold 0** and keep the single best semantic candidate as a fallback (this ensures we can still inspect near misses and “soft” matches).

4. **Topic prior**  
   - For each candidate ID, we look up its GT topic (`category` in groundtruth) and compute token overlap with the detection’s `topic`.
   - The prior bonus is computed as:

     ```text
     prior_bonus = min(0.1, overlap * 0.1)
     ```

   - This ensures topic alignment nudges a candidate up, but cannot dominate fuzzy/semantic scores.

5. **Blending and selection**

   For each candidate misconception ID:

   ```text
   blended_score = blend_weight * fuzzy_score
                   + (1 - blend_weight) * semantic_score
                   + prior_bonus
   ```

   We pick the candidate with the highest `blended_score` as the `matched_id`.

### 3.4 Conceptual Diagram

```text
              +-------------------+
              |  LLM Detection    |
              | name, desc, topic |
              +---------+---------+
                        |
           +------------+------------+
           |                         |
   [Fuzzy matcher]           [Semantic matcher]
   scores: s_fuzzy           scores: s_semantic
           |                         |
           +------------+------------+
                        |
                 [Candidate set]
                        |
                        v
                [Topic prior λ(topic)]
                        |
                        v
       blended = w*s_fuzzy + (1-w)*s_semantic + λ
                        |
                        v
              best ID + score breakdown
```

This design is interesting for CS education research because it explicitly encodes:
- Multiple textual “views” of a misconception (names, descriptions, student beliefs).
- Conceptual category priors (topics).
- A tunable fusion strategy that you can experiment with (e.g., change `blend_weight`, prior magnitude, thresholds).

---

## 4. Data Assembly: Building Analysis Tables

File: `analyze_cli.py` (functions: `build_dataframes`, etc.)

The core of the analysis is two dataframes:
- `detections_df`: one row per **detection** or **missed opportunity**.
- `opportunities_df`: one row per **injected misconception opportunity**.

### 4.1 From Raw Files to DataFrames

For each strategy:
1. Load all `detections/<strategy>/*.json` where `status == "success"`.
2. For each file:
   - Extract `student`, `question`.
   - Look up `(expected_id, is_clean)` using `manifest.json`.
   - For each model:
     - For each misconception produced by that model:
       - Run hybrid matcher → `matched_id` and scores.
       - Classify result:
         - **CLEAN file**:
           - Any detection with a match ⇒ `result = "interesting"`.
           - Detection without any match ⇒ `result = "false_positive"`.
         - **SEEDED file**:
           - `matched_id == expected_id` ⇒ `result = "true_positive"`.
           - Else ⇒ `result = "false_positive"`.
     - Track whether **any** detection from that model produced a TP (`has_tp`).
     - If the file is SEEDED and `has_tp` is `False`, we write an explicit FN row.

### 4.2 `detections_df` Schema

Columns (per row):
- `strategy`: prompting strategy (e.g., `"baseline"`).
- `model`: full model name (e.g., `"openai/gpt-5.1"`).
- `student`: student identifier.
- `question`: question ID (`"Q1"`–`"Q4"`).
- `expected_id`: ground-truth misconception ID (or `None` for clean).
- `matched_id`: ID chosen by hybrid matcher (or `None` if no match).
- `match_score`: blended hybrid score.
- `match_detail`: breakdown string (e.g., `"fuzzy=0.83, semantic=0.76, prior=0.05"`).
- `result`: one of:
  - `"true_positive"`, `"false_positive"`, `"false_negative"`, `"interesting"`.
- `is_clean`: boolean flag from manifest.
- `confidence`: numeric confidence from the LLM (if provided).
- `detected_name`: detection’s `name` field.
- `detected_topic`: detection’s `topic` field.
- `expected_topic`: topic/category from ground truth for `expected_id` (if any).

This dataframe describes **what the LLM said** and **how we interpret it**.

### 4.3 `opportunities_df` Schema

Each row represents one seeded opportunity for a given (strategy, model, student, question):
- `strategy`
- `model`
- `student`
- `question`
- `expected_id`
- `topic` (from ground truth)
- `success`: boolean, `True` if that model had at least one TP for this opportunity.

This is the basis for:
- **Recall** (per-topic, per-strategy, per-model).
- **Agreement** between models (same set of opportunities, different success booleans).
- **McNemar’s test** (paired recall comparisons).

---

## 5. Metrics and Statistical Analysis

File: `analyze_cli.py` (functions: `summarize_metrics`, `bootstrap_metrics`, `expected_calibration_error`, `brier_score`, `cohen_kappa`, `mcnemar`).

### 5.1 Core Counts

For any slice of `detections_df`, we compute:
- `TP`: count of rows with `result == "true_positive"`.
- `FP`: count with `result == "false_positive"`.
- `FN`: count with `result == "false_negative"`.

We aggregate per `(strategy, model)` using `summarize_metrics`.

From these:
- `precision = TP / (TP + FP)` (how often a detection is correct).
- `recall = TP / (TP + FN)` (how often we catch an injected misconception).
- `F1 = 2 * precision * recall / (precision + recall)`.

### 5.2 Bootstrap Confidence Intervals

Research-grade claims need uncertainty estimates. We use **non-parametric bootstrapping**:

1. Define the unit of resampling as a `(student, question)` pair.
2. For each `(strategy, model)`:
   - Collect all rows for that slice.
   - Extract unique `(student, question)` pairs as units.
3. Repeat for `iters` (e.g., 400; 150 in quick mode):
   - Sample units with replacement.
   - Rebuild a dataset by merging sampled units back into the slice.
   - Compute precision/recall/F1 for that bootstrap sample.
4. At the end:
   - `precision_lo = 2.5th percentile of bootstrap precision`.
   - `precision_hi = 97.5th percentile`, etc.

This yields 95% CIs for each metric, capturing variability across student-question instances.

### 5.3 Calibration: ECE and Brier Score

We use `confidence` provided by each model to quantify calibration.

1. Filter `detections_df` to rows where `result` is TP or FP (i.e., scored predictions).
2. **ECE (Expected Calibration Error)**:
   - Bin predictions into 10 confidence bins between 0 and 1.
   - For each bin:
     - `bin_conf` = average confidence.
     - `bin_acc` = average correctness (1 for TP, 0 for FP).
   - ECE = sum over bins of `(|bin_acc - bin_conf| * (bin_size / total_size))`.
3. **Brier Score**:
   - Treat confidence as probability that detection is correct.
   - Brier = `mean((confidence - label)^2)` where `label` is 1 (TP) or 0 (FP).

Low ECE and Brier indicate well-calibrated models; high values imply over/under-confidence.

### 5.4 Agreement and McNemar’s Test

On each seeded opportunity in `opportunities_df`, models either succeed (`success=True`) or fail (`success=False`).

For two models A and B:
- We collect lists `success_a`, `success_b` across the same opportunities.

**Cohen’s κ**:
- Measures agreement above chance:

```text
κ = (P(agree) - P(expected agree)) / (1 - P(expected agree))
```

Where:
- `P(agree)` = frequency with which both say success or both say failure.
- `P(expected agree)` = probability of agreement if A and B were independent (based on their marginal success rates).

**McNemar’s Test**:
- Tests whether recall (sensitivity) of A and B is statistically different on the same opportunities.

We compute:
- `a_only` = # of opportunities where A is correct and B is wrong.
- `b_only` = # where B is correct and A is wrong.
- `both` and `neither` for completeness.
- McNemar statistic:

```text
χ² = (|a_only - b_only| - 0.5)² / (a_only + b_only)
```

- p-value derived from χ² with 1 degree of freedom via a normal CDF approximation.

This appears in the report as:
- κ per strategy.
- McNemar p-values with contingency tables.

### 5.5 Topic Difficulty

Using `opportunities_df`:
- For each topic:
  - `recall = mean(success)` across all opportunities in that topic.
  - `N = count` of opportunities.

This tells you which conceptual areas (e.g., `Input`, `Data Types`, `State / Variables`) are hardest for the models.

### 5.6 Hallucination Analysis

Using `detections_df`:
- Filter rows where `result == "false_positive"`.
- Group by `detected_name` to count how often particular “misconceptions” appear that don’t correspond to any injected ID.
- The report lists the most frequent hallucinated labels; the bar chart visualizes the top 10.

---

## 6. Visualizations

All figures are saved under `docs/report_assets/`.

### 6.1 Topic Heatmap (`topic_heatmap.png`)

Built from `opportunities_df`.

- We create a combined column `strategy_model = strategy + " | " + short(model)`.
- For each unique topic and strategy_model, compute:

```text
recall = mean(success)  ∈ [0, 1]
```

- Pivot into a table: rows = topics, columns = strategy_model, values = recall.
- Use seaborn’s heatmap:
  - Color scale from 0 (dark/poor recall) to 1 (light/good recall).
  - Annotated with numeric recall values.

This directly visualizes which combinations of strategy and model are strong/weak for each topic.

### 6.2 Calibration Curves (`calibration.png`)

Built from `detections_df` (TP/FP rows with confidence).

For each `(strategy, model)` pair:
- Bin predictions by confidence into 10 bins.
- For each bin:
  - X-axis: average confidence.
  - Y-axis: empirical correctness.
- Plot a line of points per pair, overlayed on the **diagonal** (ideal calibration).

Interpretation:
- If a line lies above the diagonal, detections are under-confident.
- Below the diagonal ⇒ over-confident.

### 6.3 Hallucination Bar Chart (`hallucinations.png`)

Built from FPs in `detections_df`:
- Group by `detected_name` (falling back to `"<unknown>"` for empty names).
- Take top 10 by frequency.
- Plot a horizontal bar chart (count vs hallucinated name).

This makes it obvious which “imagined” misconceptions the models systematically hallucinate.

---

## 7. Report Generation

File: `analyze_cli.py` (function: `generate_report`).

The report is built as a structured markdown document and written to `thesis_report.md`. It is **not** free-form; it reflects the data model and metrics described above.

### 7.1 Structure

Sections:
1. **Title + Timestamp**
   - `# LLM Misconception Detection: Revamped Analysis`
   - Generated with a UTC timestamp.

2. **Executive Highlights**
   - Bullets summarizing:
     - Hybrid matcher usage.
     - Presence of bootstrap intervals.
     - Overall calibration metrics (ECE, Brier).

3. **Strategy × Model Performance**
   - A markdown table with:
     - `Strategy`, `Model`, `TP`, `FP`, `FN`, `Precision`, `Recall`, `F1`, and 95% CIs.
   - Built from `metrics` and bootstrap `ci`.

4. **Topic Difficulty (Recall)**
   - A markdown table per topic with `Recall` and `N` (# of opportunities).

5. **Calibration & Hallucinations**
   - Text references to:
     - `docs/report_assets/calibration.png`.
     - `docs/report_assets/hallucinations.png`.
   - Bullet list summarizing top hallucinated misconception names.

6. **Topic Heatmap**
   - Embeds `![Topic Heatmap](docs/report_assets/topic_heatmap.png)` if present.

7. **Methods**
   - Concise narrative describing:
     - Dataset and experimental setup.
     - Detection models and strategies.
     - Hybrid matching approach (fuzzy + semantic + topic priors).
     - Metrics and statistics used (precision/recall/F1, CI, calibration, κ, McNemar).

8. **Agreement & Significance**
   - For each strategy, an item with:
     - κ between the first two models for that strategy.
     - McNemar statistic and p-value.
     - Contingency table (both correct / only A / only B / both wrong).

### 7.2 JSON Export

We also write `thesis_report.json` for machine consumption:
- Keys:
  - `"metrics"`: list of per `(strategy, model)` metrics.
  - `"ci"`: list of bootstrap CI rows.
  - `"opportunities"`: list of per-opportunity records.

This is useful for:
- Additional ad-hoc visualizations.
- Re-running analyses in a Jupyter notebook.
- Comparing multiple runs or experimental settings.

---

## 8. CLI Usage and Behaviour

### 8.1 `uv run analyze`

Entry point: `analyze_cli.py:app`.

Command:
- `uv run analyze`
  - Discovers strategies under `detections/`.
  - Loads `authentic_seeded/manifest.json` and `data/a2/groundtruth.json`.
  - Builds dataframes with hybrid matching (embeddings enabled).
  - Computes metrics and CI (400 bootstrap iterations).
  - Generates:
    - `docs/report_assets/topic_heatmap.png`
    - `docs/report_assets/calibration.png`
    - `docs/report_assets/hallucinations.png`
    - `thesis_report.md`
    - `thesis_report.json`
  - Prints a summary table to the console via Rich.

Flags:
- `--quick`
  - Disables embeddings (avoids external API calls).
  - Uses fewer bootstrap iterations (e.g., 150).
  - Useful for fast iteration on code changes.

### 8.2 `uv run pipeline`

Entry point: `pipeline.py:run`.

High-level steps:
1. Generate dataset (if not skipped).
2. Run LLM detection with configured strategies and models.
3. Run the revamped analysis (internally calling the same data assembly and reporting functions as `analyze`).

This maintains the “one-command” experiment story:
- For a fresh end-to-end run:
  - `uv run pipeline --students 60 --strategies all`

---

## 9. Extensibility and Future Work

### 9.1 Adding New Models or Strategies

- New strategies just appear as new subdirectories under `detections/`. The analysis automatically picks them up during discovery (any non-underscore directory).
- New models (e.g., `anthropic/claude-3.5`) show up as new `model` values in detection JSONs:
  - Hybrid matching is model-agnostic.
  - Metrics and plots automatically incorporate new model names into groupings and legends.

### 9.2 Alternative Embedding Providers

Currently:
- Semantic matching uses OpenAI embeddings by default.

You can:
- Introduce a separate OpenRouter client module (e.g., `utils/llm/openrouter_embeddings.py`) to avoid touching the existing `utils/llm/openrouter.py`.
- Plug that into `semantic.py`/`hybrid.py` behind a configuration switch to experiment with:
  - `openai/text-embedding-3-large`
  - `google/gemini-embedding-001`

### 9.3 Pseudonymization

Right now:
- Student identifiers remain as in the manifest.

For publication:
- Add a simple mapping layer before writing report/JSON:
  - Replace `student` values with `S001`, `S002`, etc.
  - Keep the mapping in a separate file (not under version control) if you want to reconstruct.

### 9.4 Extra Visuals

Easy additions:
- Per-question difficulty plots (e.g., `Q1` vs `Q2` vs `Q3` vs `Q4`).
- Per-student coverage plots (how many TPs/FNs per student).
- Confidence histograms separated by TP vs FP.

---

## 10. Summary

In summary, the new system:
- Treats misconception detection as a well-defined **classification over a known misconception universe** backed by a hybrid textual + semantic matcher with conceptual priors.
- Produces **research-grade metrics** with uncertainty (bootstrap CIs), calibration diagnostics, and model agreement tests.
- Surfaces **where and how** the models fail (topics, hallucinations, missed injections) via interpretable tables and figures.
- Keeps the workflow simple (`uv run analyze` / `uv run pipeline`) while being flexible enough to support new models, strategies, and embedding providers.

If someone new to the project reads just this document and then looks at:
- `analyze_cli.py`
- `utils/matching/hybrid.py`
- `docs/report_assets/*`
- `thesis_report.md`

they should be able to trace every piece of the analysis from raw data to final figures and text without surprises.
