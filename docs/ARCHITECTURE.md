# Architecture & Codebase Tour

This document gives you a **guided tour of the EduBench codebase** and shows how all the pieces fit together. It is meant to be readable even if you are fairly new to Python or CLIs.

---

## 1. Top‑Level Structure

From the project root:

```text
EME_testing/
├── cli.py                 # Main CLI (uv run bench ...)
├── single_submission.py   # One‑off runner for a single student
├── modes/                 # Grading strategies (direct / reverse / EME)
├── prompts/               # Prompt templates for the models
├── utils/                 # Shared utilities (models, evaluation, display, validation)
├── db/                    # SQLite schema + database manager
├── docs/                  # Documentation (this folder)
├── data/                  # JSON results workspace
├── student_submissions/   # Java submissions (input)
├── evaluation_schema.json # JSON schema for validation
└── evaluations.db         # SQLite database (created automatically)
```

At a high level:

- **Input**: `.java` files in `student_submissions/`.
- **Processing**: CLI chooses a mode → prompts are built → models are called → metrics are computed.
- **Output**: JSON in `data/` and rows in `evaluations.db`.

---

## 2. End‑to‑End Pipeline

Here is the data flow for a typical benchmark run:

```text
           ┌────────────────────────────────────────────┐
           │             User runs CLI                  │
           │    uv run bench benchmark --mode eme    │
           └─────────────────────────┬──────────────────┘
                                     │
                                     ▼
                              cli._run_benchmark_async()
                                     │
                     ┌───────────────┼────────────────┐
                     ▼               ▼                ▼
          _load_question_and_rubric() │      _discover_submissions()
          (question_*.md + rubric_*.json)     (find *.java files)
                     │                               │
                     └───────────────┬───────────────┘
                                     ▼
                          _run_single_mode("eme", ...)
                                     │
                                     ▼
                       modes/eme_grading.run_eme_grading()
                                     │
                                     ▼
                     utils.evaluator.evaluate_submission()
                                     │
                     ┌───────────────┴─────────────────────┐
                     ▼                                     ▼
     utils.ai_clients.get_openai_eval(prompt)   utils.ai_clients.get_eduai_eval_async(prompt)
         (GPT‑5 Nano via OpenAI)                      (GPT‑OSS 120B via EduAI)
                     │                                     │
                     └───────────────┬─────────────────────┘
                                     ▼
                     utils.evaluator.compute_metrics()
                                     │
                                     ▼
                      Results list (per student, per mode)
                                     │
                                     ▼
                             cli._save_results()
                         (write JSON + ingest DB)
                                     │
                                     ▼
                   data/results_*.json    db/evaluations.db
```

You can then:

- inspect JSON files directly under `data/`, or
- use the CLI analysis menu to restore JSON files from the database.

For more on the database, see `docs/DATABASE.md`.

---

## 3. CLI (`cli.py`)

**Purpose:** User‑facing interface to run benchmarks and restore results.

Key pieces:

- `app = typer.Typer(...)`  
  Defines the `bench` command exposed via `pyproject.toml`.

- `@app.command() def benchmark(...)`  
  The main entrypoint:
  - displays a banner,
  - shows the strategy selection menu if `--mode` is not given,
  - delegates to `_run_benchmark_async(...)`.

- `_prompt_mode_selection()`  
  Uses Rich tables to display the menu:
  - Direct grading
  - Reverse grading
  - EME (ensemble)
  - Run All
  - Analysis (restore JSON from DB)

- `_run_benchmark_async(mode, advanced)`  
  - loads environment with `dotenv`,
  - calls `_load_question_and_rubric()` (selects `question_*.md` and `rubric_*.json`),
  - discovers `.java` submissions,
  - calls `_run_single_mode(...)` or `_run_all_modes(...)`.

- `_run_single_mode(...)`  
  - picks the right runner from `modes/` based on `mode`,
  - shows a Rich progress bar (one tick per student),
  - collects results in a list.

- `_display_mode_results(...)` and `_display_cross_paradigm_comparison(...)`  
  - render results tables and strategy comparison tables in the terminal.

- `_save_results(mode, results)`  
  - writes stripped‑down JSON to `data/results_{mode}_{timestamp}.json`,
  - calls `db.manager.init_db(...)` and `db.manager.ingest_results_file(...)` to populate `evaluations.db`,
  - shows warnings if database ingestion fails, but **never loses the JSON file**.

- `_run_analysis_menu()` + `_restore_json_from_db()`  
  - provide the analysis menu option,
  - call `db.manager.restore_json_files(...)` to rebuild JSON files from `evaluations.db` into `data/`.

---

## 4. Grading Modes (`modes/`)

Each file under `modes/` wraps a specific **grading paradigm**, but they all share the same core evaluator.

- `modes/direct_grading.py`

  ```python
  async def run_direct_grading(submissions, question, rubric, progress_callback=None):
      # uses prompts.direct_prompt.build_prompt
      # then calls utils.evaluator.evaluate_submission(...)
  ```

  - “Grade directly against the rubric.”
  - Prompts the models to score the code in one step.

- `modes/reverse_grading.py`

  ```python
  async def run_reverse_grading(...):
      # uses prompts.reverse_prompt.build_prompt
  ```

  - “Imagine the ideal solution first, then compare the student’s code to that ideal.”
  - Encourages the model to reason about what perfect code would look like.

- `modes/eme_grading.py`

  ```python
  async def run_eme_grading(...):
      # uses prompts.eme_prompt.build_eme_prompt
  ```

  - The **Ensembling Method Evaluation (EME)** from the RIAYN paper, adapted for this project.
  - Enforces a 100‑point rubric; both models grade using the same balanced prompt.

All three:

- build a list of `evaluate_submission(...)` tasks (one per student),
- await them with `asyncio.as_completed(...)`,
- call `progress_callback` to update the CLI progress bar.

---

## 5. Prompts (`prompts/`)

The prompt builders decide *how* the models are instructed.

- `prompts/direct_prompt.py`
  - `build_prompt(question, rubric_json, student_code)`  
  - Instructs the model to:
    - read the rubric,
    - read the student code,
    - return **only JSON** with:
      - `criteria_scores` (array),
      - `total_score`,
      - `max_possible_score`,
      - `overall_feedback`.

- `prompts/reverse_prompt.py`
  - Similar structure, but explicitly tells the model to:
    - mentally construct an ideal solution,
    - compare the student code to that ideal,
    - explain the gaps in the feedback.

- `prompts/eme_prompt.py`
  - `build_eme_prompt(question, rubric_json, student_code)`  
  - Enforces a **100‑point scale**:
    - `max_possible_score` must be 100.
    - Each rubric category becomes a criterion.
  - Strongly emphasizes:
    - impartial grading,
    - JSON‑only output,
    - short, focused feedback.

If you want to adjust grading behavior (e.g., more lenient, more strict, extra criteria), this is usually the safest place to start.

---

## 6. Evaluation Core (`utils/evaluator.py`)

This module contains the **heart of the pipeline**: calling models and computing metrics.

- `evaluate_submission(code_path, question, rubric, prompt_builder=None)`

  Steps:

  1. Reads the student’s Java file.
  2. Picks a prompt builder:
     - if `prompt_builder` is `None`, uses the EME prompt by default,
     - otherwise uses the direct or reverse prompt.
  3. Builds a single prompt string containing:
     - the question,
     - the rubric (as pretty‑printed JSON),
     - the student code,
     - strict instructions to return JSON only.
  4. Calls two models in parallel via `asyncio.gather(...)`:
     - `get_openai_eval(prompt)` – GPT‑5 Nano,
     - `get_eduai_eval_async(prompt)` – GPT‑OSS 120B (EduAI).
  5. Normalizes the payloads with `_normalize_model_result(...)` so both results always have:
     - `total_score`,
     - `max_possible_score`,
     - `overall_feedback` (string).
  6. Computes comparison metrics with `compute_metrics(...)`.

- `compute_metrics(gpt5_nano_result, gpt_oss_120b_result)`

  - Extracts `total`, `max`, and `%` from both models.
  - Computes:
    - `avg_pct` – average of the two percentages (if both available),
    - `diff_pct` – absolute difference between percentages.
  - Sets:
    - `flag`:
      - ✅ if the difference is ≤ 10 percentage points,
      - 🚩 otherwise or if any data is missing.
    - `comment`:
      - “Models agree within tolerance”
      - or which model is stricter and by how much.

The returned structure is what drives:

- the Rich tables in `cli.py`,
- the JSON schema in `evaluation_schema.json`,
- the database ingestion in `db/manager.py`.

---

## 7. Model Clients (`utils/ai_clients.py` and friends)

The project uses a **facade** plus dedicated client modules:

- `utils/ai_clients.py`

  - Re‑exports:
    - `get_openai_eval` – GPT‑5 Nano via OpenAI,
    - `get_eduai_eval`, `get_eduai_eval_async` – GPT‑OSS 120B via EduAI,
    - `get_openrouter_eval` – (optional) models via OpenRouter.
  - Most of the codebase imports from here.

- `utils/openai_client.py`
  - Implements `get_openai_eval` using the **OpenAI Python SDK** and the **Responses API**.
  - Uses `utils.structured_outputs.get_grading_response_format()` to force the model to return JSON matching a small grading schema.
  - Contains helpers:
    - `_extract_text_response`,
    - `_safe_json_loads`,
    - `_serialize_openai_response`.

- `utils/eduai_client.py`
  - Implements `get_eduai_eval` / `get_eduai_eval_async` using `curl` under the hood.
  - Reads `EDUAI_ENDPOINT`, `EDUAI_API_KEY`, and `EDUAI_MODEL` from the environment.
  - Uses the same JSON parsing helper `_safe_json_loads`.

- `utils/openrouter_client.py`
  - Implements `get_openrouter_eval` using the **OpenAI SDK pointed at OpenRouter** (`base_url="https://openrouter.ai/api/v1"`).
  - Also uses `get_grading_response_format()` to keep outputs structured.

- `utils/structured_outputs.py`
  - Defines the schema used in `response_format`, ensuring that the model always returns:
    - `total_score` (number),
    - `max_possible_score` (number),
    - `overall_feedback` (string).

For a deep dive into how JSON outputs are configured, see `docs/JSON_OUTPUT.md`.

---

## 8. Validation (`utils/validation.py` and `evaluation_schema.json`)

Before any JSON file is ingested into the database, it is validated.

- `evaluation_schema.json`
  - JSON Schema for one student’s evaluation record.
  - Requires:
    - `student`,
    - `gpt5_nano_result`,
    - `gpt_oss_120b_result`,
    - `metrics`.
  - Enforces sensible ranges (e.g., percentages between 0 and 100).

- `utils/validation.py`
  - Provides `ValidationResult` (holds `is_valid`, `errors`, `warnings`).
  - `validate_json_file(file_path, schema_path=None)`:
    - ensures the file exists and parses as JSON,
    - checks that the root is an array,
    - validates each record against the schema and “business rules”:
      - `avg_pct` matches the average of the model percentages,
      - `diff_pct` matches their absolute difference,
      - student IDs are non‑empty strings.
  - Returns a `ValidationResult` with helpful error messages used in logs.

The database manager (`db/manager.py`) uses this function to keep `evaluations.db` consistent.

---

## 9. Database Layer (`db/`)

Summarized here; see `docs/DATABASE.md` for full details.

- `db/schema.sql`
  - Defines:
    - `runs` table (one row per batch / JSON file),
    - `evaluations` table (one row per student per run),
    - indexes on `run_id` and `student_name`.

- `db/manager.py`
  - `DatabaseManager` class with methods:
    - `init_db()` – create tables if needed,
    - `ingest_results_file(file_path)` – validate + insert all evaluations from a JSON file,
    - `restore_json_files(data_dir, validate=False)` – rebuild JSON files from the database.
  - Convenience functions:
    - `init_db(db_path)`,
    - `ingest_results_file(db_path, file_path)`,
    - `restore_json_files(db_path, data_dir, validate=False)`.

The CLI (`cli.py`) calls these helpers automatically. You rarely need to call them directly unless you’re doing custom analysis.

---

## 10. Single‑Submission Script (`single_submission.py`)

This script is a **compact example** of using the evaluator outside of the Typer CLI.

Flow:

```text
python single_submission.py path/to/submission.java
    │
    ▼
  load .env (question + rubric)
    │
    ▼
  find .java file (or first .java in a directory)
    │
    ▼
  run evaluate_submission(...) with an EME prompt
    │
    ▼
  show Rich table + grade summary
    │
    └─ optionally dump clean JSON to a file
```

This is a good reference if you want to embed EduBench in another tool or notebook.

---

## 11. Extending the System

Common extension points:

- **New grading mode**
  - Add `modes/new_mode.py` with a function like `run_new_mode(...)`.
  - Add a prompt builder in `prompts/new_mode_prompt.py`.
  - Wire it into:
    - `_prompt_mode_selection()` (CLI menu),
    - `_run_single_mode()` / `_run_all_modes()` in `cli.py`.

- **New model provider**
  - Add a client module (e.g., `utils/my_model_client.py`).
  - Re‑export from `utils/ai_clients.py`.
  - Update `utils/evaluator.py` to call it alongside existing models.

- **Different language or file layout**
  - Adjust `_discover_submissions(...)` in `cli.py` (e.g., search for `.py` instead of `.java`).

When you change data formats or metrics, remember to:

- Update `evaluation_schema.json`.
- Adjust validation logic in `utils/validation.py`.
- Update or add documentation under `docs/`.
