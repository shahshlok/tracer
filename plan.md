Plan for the dataset generator

1) Layout & inputs
   - Create `utils/misconception_generator/` with `generate_dataset.py` (manifest + async generation) and `__init__.py` if needed.
   - Load misconceptions from `data/a2/groundtruth.json`; load question prompts from `data/a2/q1.md` … `q4.md`.
   - Use Faker for names; generate 60 students × 4 files each; seed RNG with current UNIX time and record the seed in the manifest.

2) Personas & sampling
   - Define a curated list of ~15–20 coding personas (indentation quirks, naming styles, comments, verbosity, etc.).
   - For each student: assign one persona; sample 1–2 distinct misconceptions(randomly); map to Q1–Q4, choosing one if multiple apply; flag files as SEEDED or CLEAN.

3) Manifest generation
   - Build manifest entries with folder name (`Last_First_ID`), persona, seed, and per-question entries (`type`, optional `misconception_id`, `instruction`).
   - Persist to `authentic_seeded/manifest.json` (or configurable path); ensure directory scaffolding exists for later writes.

4) Async code generation
   - Use `asyncio` + `openai.AsyncOpenAI` (Responses API) with model `gpt-5.1-2025-11-13`; add semaphore (only if necessary given our current rate limits) and retries/backoff (same for here)
   - For each manifest entry: construct system prompt with persona; user prompt includes question text and either seeded error instruction or clean novice solution request.
   - Strip markdown fences, save Java to `authentic_seeded/{folder}/Q{N}.java`; ensure persona style mention appears in prompt for consistency.
   
5) CLI & configuration
   - Provide Typer/Rich CLI with modes: (a) manifest-only, (b) generate-from-manifest, (c) full pipeline.
   - Allow options for paths, concurrency, seed override, and dry-run (no API calls).

6) Resilience & logging
   - Log progress and failures; capture per-file errors without aborting whole run; optional resume from existing manifest.
   - Validate manifest structure before generation; handle API/network exceptions with limited retries and clear output.
