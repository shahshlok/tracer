# AGENT GUIDANCE – ensemble-eval-cli

This repository contains a research harness for evaluating **LLM-based misconception detection** in CS1-style programming assignments. It is designed to support synthetic experiments first, with a long-term goal of extending to real student code.

The primary user is working toward a **Bachelor’s honours thesis** and a publishable paper in **CS education venues** (e.g., ITiCSE, SIGCSE, possibly L@S). Any agent working in this repo should prioritize:

- **Research integrity** – honest limitations, no over-claiming.
- **Statistical rigor** – clear metrics, uncertainty estimates, and appropriate tests.
- **Pedagogical relevance** – insights about misconceptions and LLM behavior that matter to instructors and CS1 course design.

---

## 1. High-Level Goal

Design and evaluate a framework where **LLMs act as misconception detectors** for CS1 code submissions.

Concretely:
- Given a taxonomy of misconceptions (e.g., Scanner misuse, integer division, state/variable misunderstandings) and a program submission,
- Use LLMs (GPT-5.1, Gemini 2.5 Flash, and future models) to:
  1. Read the student’s code,
  2. Describe any misconceptions they detect,
  3. Map those descriptions to the taxonomy,
  4. Evaluate how accurately and reliably they do so.

Short-term and long-term focus: **synthetic data only**, where we inject known misconceptions into LLM-generated CS1 programs, so we can tightly control:
- Which misconception is present in each file.
- Distribution of seeded vs clean files.
- Assignments and prompts.

By explicit design choice from the primary user, this project will **never use real student submissions**; all experiments are synthetic, though they are intended to approximate CS1 contexts as faithfully as possible.

---

## 2. Core Architecture

The system is structured into three conceptual stages:

1. **Dataset Generation**
   - Files: `utils/generators/dataset_generator.py`, `authentic_seeded/manifest.json`, `data/a2/groundtruth.json`.
   - Uses a misconception taxonomy (`groundtruth.json`) and assignment texts to:
     - Generate a manifest of which student gets which misconception on which question (or a clean file).
     - Ask an LLM to generate 60 student “personas” and their code submissions (synthetic CS1 solutions) with injected misconceptions.
   - Key constraints:
     - ~60 students × 4 questions = 240 files.
     - ~20–25% seeded files, remainder clean.
     - **One injected misconception per file** (for simpler evaluation).

2. **Detection**
   - Files: `llm_miscons_cli.py`, `detections/`.
   - For each student+question, and for each prompting strategy (baseline, minimal, rubric_only, socratic), the system:
     - Calls LLMs (currently GPT-5.1 and Gemini 2.5 Flash).
     - Asks them to detect and describe misconceptions in the code.
     - Stores detection JSONs under `detections/<strategy>/student_question.json`.
   - Each detection includes detailed fields:
     - `name`, `description`, `student_belief`, `correct_understanding`, `symptoms`, `root_cause`, `remediation_hint`, `evidence`, `confidence`, etc.

3. **Alignment + Evaluation**
   - Files: `analyze_cli.py`, `utils/matching/{fuzzy.py,semantic.py,hybrid.py}`, `thesis_report.md`.
   - **Alignment (matcher)**:
     - Maps each LLM detection onto the ground-truth taxonomy using different matchers:
       - `fuzzy_only`: string-based similarity on names/descriptions.
       - `semantic_only`: embedding-based similarity (OpenAI `text-embedding-3-*` or similar).
       - `hybrid`: current fusion of fuzzy + semantic + topic prior.
     - Uses `match_mode` (fuzzy_only, semantic_only, hybrid, all) to run matcher ablation.
   - **Evaluation**:
     - Builds tidy dataframes of detections + “opportunities” (seeded misconceptions).
     - Computes metrics per (strategy, model[, match_mode]):
       - True positives / false positives / false negatives.
       - Precision, recall, F1 with bootstrap confidence intervals.
       - Topic-wise recall (Input, State / Variables, Data Types, etc.).
       - Hallucination analysis (recurring FPs that don’t map to known IDs).
       - Agreement between models via Cohen’s κ and McNemar tests.
     - Generates a markdown report (`thesis_report.md`) and assets in `docs/report_assets/`.

---

## 3. Current Status (What’s Implemented)

- **Synthetic dataset**:
  - 60 students × 4 questions (Assignment A2: CS1 kinematics-like problem).
  - Manifest-driven injection of ~15–18 misconceptions, one per seeded file.
  - ~54 seeded files, ~186 clean files (~22.5% seeded).

- **LLM detectors**:
  - GPT-5.1 and Gemini 2.5 Flash via OpenAI or compatible APIs.
  - Multiple prompting strategies (baseline, minimal, rubric_only, socratic).

- **Matchers** (with ablation):
  - `fuzzy_only` – naive string similarity; currently performs very poorly (F1 ≈ 0.05).
  - `semantic_only` – embedding-based semantic similarity; performs reasonably well (F1 ≈ 0.6).
  - `hybrid` – fuzzy + semantic + topic prior; roughly similar performance to semantic-only on current data.

- **Analysis/reporting**:
  - Per (strategy, model, match_mode) P/R/F1 with bootstrap CIs.
  - Topic-wise recall; heatmaps and bar charts.
  - Hallucination plots (top hallucinated misconception names).
  - Matcher ablation section in `thesis_report.md`.
  - Agreement metrics (κ, McNemar) between GPT and Gemini.

---

## 4. Research Intent & Target Venues

The end goal is to produce:

1. A **thesis-level** body of work showing:
   - That an LLM + matcher pipeline can detect many seeded misconceptions in CS1-style code.
   - Where it fails (by topic/misconception type).
   - How different matchers and prompting strategies affect behavior.

2. A **publishable paper** at a top CS education venue, initially targeting:
   - **ITiCSE** (International Conference on Innovation and Technology in Computer Science Education).
   - Possibly later: **SIGCSE Technical Symposium**, **Learning@Scale**, or similar venues.

The expected contribution is a mix of:
- A reusable **evaluation framework** (architecture + tooling).
- A **matcher study** showing the necessity of semantic similarity (vs naive string matching) for this task.
- **Insights about which misconception types LLMs find easy vs hard**, and what this reveals about their understanding of code and CS1 concepts.

---

## 5. Design Philosophy and Constraints

Agents working here should assume:

- **Synthetic-first**
  - For now, we stay in the synthetic regime for tight control and rapid iteration.

- **No over-claiming.**
  - Never present synthetic results as if they are directly about real students.
  - Be explicit about limitations and threat-to-validity.

- **Methodological honesty.**
  - If a matcher (e.g., hybrid) is not clearly superior to semantic-only, say so.
  - Negative results (e.g., confidence calibration failing, fuzzy-only being terrible) are still valuable.

- **Incremental enhancement.**
  - Prefer small, well-justified improvements (new matchers, new analyses, new visualizations) over large, ungrounded refactors.
  - When adding capabilities (e.g., new match modes, new assignments, replication across seeds), update `plan.md` / `future.md` as a roadmap for humans and agents.

---

## 6. Key Files and How to Run

- `pipeline.py`
  - Orchestrates end-to-end runs: dataset generation → detection → analysis.
  - Typical usage:
    - `uv run pipeline run --students 60 --strategies all --force --yes`

- `analyze_cli.py`
  - Runs analysis/matcher ablation on existing detections.
  - Typical usage:
    - `uv run analyze` (default: hybrid matcher).
    - `uv run analyze --match-mode all` (run fuzzy vs semantic vs hybrid ablation).

- `thesis_report.md`
  - The main markdown report summarizing quantitative results and key takeaways.
  - Generated by `analyze_cli.py` or the analysis step in `pipeline.py`.

- `plan.md`
  - The *current* high-level plan; often focused on matcher ablation and next experimental steps.

- `future.md`
  - Deferred ideas and long-term directions (real data, broader taxonomy, confidence calibration, multi-misconception files, etc.).

---

## 7. How Agents Should Help

When modifying or extending this repo, agents should:

1. Preserve and improve the **evaluation pipeline**, not just the raw numbers.
2. Be explicit about:
   - What experimental condition is being changed (match mode, model, assignment, seed).
   - How new metrics/plots fit into the broader research questions.
3. Favor:
   - Clear, reproducible analysis code.
   - Concise, data-backed narrative in `thesis_report.md`.
4. Avoid:
   - Introducing new “magic” heuristics without baselines or ablations.
   - Removing existing metrics/plots unless there’s a clear reasoning and it’s recorded in `plan.md` or `future.md`.

If in doubt, agents should default to:
- Asking: “How does this change help us understand LLM misconception detection better?”
- Updating `plan.md`/`future.md` to keep humans in the loop about intended directions.
