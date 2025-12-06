# AGENT GUIDANCE 

This repository contains a research harness for evaluating **LLM-based misconception detection** in CS1-style programming assignments. It is designed to support synthetic experiments.

The primary user is working toward a **Bachelor’s honours thesis** and a publishable paper in **CS education venues** (e.g., ITiCSE, SIGCSE, possibly L@S). Any agent working in this repo should prioritize:

- **Research integrity** – honest limitations, no over-claiming.
- **Statistical rigor** – clear metrics, uncertainty estimates, and appropriate tests.
- **Pedagogical relevance** – insights about misconceptions and LLM behavior that matter to instructors and CS1 course design.

---

## 1. Research Goal & Thesis

**Current Thesis Statement:**
"We conducted a controlled ablation study to define the 'Diagnostic Ceiling' of modern LLMs. We prove that while LLMs have solved Syntax/API misconceptions (approaching 100% recall), they remain statistically incompetent at diagnosing State/Logic flow (often <25% recall), regardless of model size or prompting strategy. Furthermore, complex pedagogical prompting (e.g., Socratic) often degrades diagnostic performance compared to zero-shot classification."

**Key Research Questions:**
1.  **Diagnostic Ceiling:** What is the theoretical upper bound of LLM detection capabilities on controlled (seeded) data?
2.  **Failure Boundaries:** Exactly *which* types of misconceptions are invisible to LLMs (e.g., State vs. Syntax)?
3.  **The Prompting Paradox:** Why do simpler prompts often outperform complex "Socratic" or role-playing prompts?
4.  **Generalizability:** Do these failure patterns hold across different assignments (Kinematics vs. String Processing)?

**Strategy:**
This project uses **synthetic data** as a form of *Controlled Sensitivity Analysis*. By injecting known errors, we measure the model's ability to find them. If it fails on these clear, seeded examples, it will certainly fail on ambiguous real-world data.

---

## 2. Core Architecture

The system is structured into three conceptual stages:

1. **Dataset Generation**
   - Files: `utils/generators/dataset_generator.py`, `authentic_seeded/manifest.json`, `data/a2/groundtruth.json`.
   - Uses a misconception taxonomy (`groundtruth.json`) and assignment texts to:
     - Generate a manifest of which student gets which misconception on which question (or a clean file).
     - Ask an LLM to generate 60-100 student “personas” and their code submissions (synthetic CS1 solutions) with injected misconceptions.
   - Key constraints:
     - **One injected misconception per file** (for simpler evaluation).
     - Realistic grade distribution (40% Perfect, 35% Single-Issue, 20% Struggling, 5% Severe).

2. **Detection**
   - Files: `llm_miscons_cli.py`, `detections/`.
   - For each student+question, and for each prompting strategy (baseline, minimal, rubric_only, socratic), the system:
     - Calls LLMs (currently GPT-5.1, Gemini 2.5 Flash, Claude Haiku 4.5).
     - Asks them to detect and describe misconceptions in the code.
     - Stores detection JSONs under `detections/<strategy>/student_question.json`.

3. **Alignment + Evaluation**
   - Files: `analyze_cli.py`, `utils/matching/{fuzzy.py,semantic.py,hybrid.py}`, `thesis_report.md`.
   - **Alignment (matcher)**:
     - Maps each LLM detection onto the ground-truth taxonomy.
     - **Findings:** Fuzzy matching is near-useless; Semantic/Hybrid matching is required for this task.
   - **Evaluation**:
     - Builds tidy dataframes of detections + “opportunities” (seeded misconceptions).
     - Computes metrics per (strategy, model[, match_mode]):
       - True positives / false positives / false negatives.
       - Precision, recall, F1 with bootstrap confidence intervals.
       - Topic-wise recall (Input, State / Variables, Data Types, etc.).
       - **Hallucination analysis** (recurring FPs that don’t map to known IDs).

---

## 3. Methodology Standards

**Critical Reporting Rule: The "Honest N-Count"**
To avoid statistical inflation, we distinguish between **Instance Performance** and **Model Reliability**:

*   **Potential Recall (Unique File Recall):** Measures *Competence / Detectability*.
    *   *Definition:* Of the $N$ unique files containing Error X, what percentage were found by *at least one* model/strategy combination?
    *   *Goal:* Defines the "Diagnostic Ceiling" — the theoretical upper bound of the system.

*   **Average Recall:** Measures *Reliability*.
    *   *Definition:* Of all $N \times M$ inference runs, what percentage successfully found the error?

*   **Consistency:** Measures *Stability*.
    *   *Definition:* $\text{Consistency} = \frac{\text{Average Recall}}{\text{Potential Recall}}$.
    *   *Goal:* Distinguishes between "blindness" (Potential Recall $\approx$ 0) and "flakiness" (High Potential, Low Average).

*   **N-Count Reporting:** Always report $N$ as the number of *unique student submissions*, not the number of model inference runs.

**The "Prompting Paradox"**
We have observed that simpler prompts often outperform complex "Socratic" or role-playing prompts. This is a key finding. We do not "fix" this by forcing complex prompts to work; we report it as a limitation of current LLMs in pedagogical contexts.

---

## 4. Roadmap

1.  **Metric Refinement:** Implement "Unique File Recall" and "Consistency" metrics to accurately report current performance without inflation.
2.  **Scale:** Run the full pipeline on **100 students** (Seed 2025) to establish a solid statistical baseline on Assignment 2 (Kinematics).
3.  **Generalize:** Introduce **Assignment 3** (e.g., Arrays/Strings) to test if the "State vs. Syntax" failure mode is universal.
4.  **Analyze:** Finalize the thesis report with the "Failure Boundaries" narrative.

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
    - `uv run pipeline run --students 100 --strategies all --force --yes`

- `analyze_cli.py`
  - Runs analysis/matcher ablation on existing detections.
  - Typical usage:
    - `uv run analyze` (default: hybrid matcher).
    - `uv run analyze --match-mode all` (run fuzzy vs semantic vs hybrid ablation).

- `thesis_report.md`
  - The main markdown report summarizing quantitative results and key takeaways.
  - Generated by `analyze_cli.py` or the analysis step in `pipeline.py`.

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