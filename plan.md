# Plan: Thesis Report Revamp (Data-Rich, Statistical, Novel)

1. **Data assembly layer**
   - Build a tidy dataframe joining manifest, groundtruth, and every detection (model/strategy/question), including classification outcome, match scores, confidence, match method, and evidence snippet metadata.
   - Cache intermediate tables (e.g., parquet) and enable a “quick mode” that reuses cached data.

2. **Hybrid matching upgrade**
   - Add a fusion matcher combining fuzzy (token/sequence), multi-view semantic similarity (name/description/belief/evidence), and priors from question/topic to rerank matches (e.g., reciprocal-rank fusion).
   - Support embeddings via OpenAI (text-embedding-3-small/large) and optional OpenRouter models (e.g., google/gemini-embedding-001) using a separate OpenRouter client module to avoid touching existing utils/llm/openrouter.py.
   - Calibrate thresholds per strategy/model using manifest-aware validation; report gains vs. current matcher.

3. **Expanded metrics & statistical analysis**
   - Compute slices per strategy/model/question/topic/misconception and per-student coverage.
   - Add calibration metrics (ECE/Brier), confidence histograms, and agreement scores (Cohen’s κ) between models.
   - Add paired significance tests (McNemar variants, paired bootstrap CIs for precision/recall/F1) with effect sizes.
   - Error taxonomy: FP types (hallucination vs. near-miss) and FN breakdown by topic/misconception difficulty.

4. **Visualization pipeline**
   - Generate seaborn/matplotlib figures: heatmaps (topic × strategy/model), calibration curves, confidence histograms, agreement/confusion matrices, misconception difficulty bars, and hallucination frequency plots.
   - Save assets under `docs/report_assets/` and reference them from `thesis_report.md` (markdown is the primary output).

5. **Report generator rewrite**
   - Replace string-append writer with a templated generator that pulls from the dataframe and metrics, embeds figure links, and emits `thesis_report.md` (optionally a JSON data dump for internal use).
   - Include methods (dataset, matching, thresholds), key findings with CIs/p-values/effect sizes, limitations, and recommendations.
   - Leave pseudonymization optional (can be applied later if needed).

6. **CLI integration & configurability**
   - Expose flags for: asset output path, enabling hybrid matcher, bootstrap iterations, caching/quick mode, and section toggles.
   - Wire into `analyze`/`pipeline` commands and add smoke tests for analytics components.
