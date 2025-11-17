# CLI Provider Selection & Multi-Provider Plan

This document outlines the plan for making the EduBench CLI a
provider-aware, fun, and intuitive tool that supports **EduAI**,
**OpenAI (GPT‑5 Nano)**, and **OpenRouter (Gemini 2.5 Flash)** in any
combination.

The goal is to integrate provider selection cleanly into the CLI UX,
while keeping the internal code structure simple and extensible.

---

## High-Level Goals

- Let users choose **which providers** to use **before each run**:
  - EduAI (GPT‑OSS 120B)
  - OpenAI (GPT‑5 Nano)
  - OpenRouter (Gemini 2.5 Flash)
- Allow **any combination** of providers per run:
  - e.g. OpenAI only, EduAI + OpenAI, OpenAI + OpenRouter, all three, etc.
- Keep the CLI **fun, intuitive, and demo-friendly**:
  - Interactive provider selection using arrow keys + space bar.
  - Clear status messages about which providers are active.
- Handle missing or broken configuration gracefully:
  - If a selected provider is misconfigured, prompt to skip it or cancel.
- For now, **remove the current EME mode** and focus on:
  - Direct Grading
  - Reverse Grading
  - All (Direct + Reverse run sequentially)

---

## UX Flow Overview

For each benchmark run:

1. **Provider Selection Screen**
   - Shown immediately after launching `bench benchmark` (or
     equivalent).
   - Interactive multi-select using arrow keys and space bar.
2. **Strategy Selection Screen**
   - Existing menu, minus the current EME option.
   - Options:
     - Direct Grading
     - Reverse Grading
     - All (Direct then Reverse, sequentially)
3. **Configuration Check**
   - For each selected provider, check required environment variables.
   - If any are missing/invalid, prompt the user:
     - “Provider X misconfigured. Continue without it? [y/N]”
4. **Run Evaluation(s)**
   - For each selected strategy:
     - Run the evaluation using all remaining providers.
     - Show results.
   - If “All” was chosen:
     - Run Direct with chosen providers → show results.
     - Then run Reverse with the same providers → show results.
5. **Return to Main Menu**
   - Allow user to start another run with different providers/strategies.

---

## Provider Selection UI Details

### Behavior

- The provider selection screen appears **before every run**.
- It shows a list of providers with:
  - A **cursor** indicating the current selection line.
  - A **checkbox** indicating whether the provider is enabled.
- Controls:
  - `↑` / `↓` (and optionally `k` / `j`) move the cursor.
  - `Space` toggles the checkbox for the provider under the cursor.
  - `Enter` confirms the selection.
- At least **one provider must be selected**; otherwise we show a
  friendly message and re-render.

### Example Rendering

```text
Select providers (Space = toggle, ↑/↓ = move, Enter = continue):

  > [x] OpenAI (GPT-5 Nano)
    [ ] EduAI (GPT-OSS 120B)
    [ ] OpenRouter (Gemini 2.5 Flash)

Current selection: OpenAI
```

After toggling:

```text
Select providers (Space = toggle, ↑/↓ = move, Enter = continue):

    [x] OpenAI (GPT-5 Nano)
  > [x] EduAI (GPT-OSS 120B)
    [ ] OpenRouter (Gemini 2.5 Flash)

Current selection: OpenAI, EduAI
```

If the user presses Enter with no providers selected:

```text
You must select at least one provider.
Press any key to return to the provider list.
```

---

## Provider Configuration & Error Handling

### Required Environment Variables

- **OpenAI (GPT‑5 Nano)**:
  - `OPENAI_API_KEY`
- **EduAI (GPT‑OSS 120B)**:
  - `EDUAI_API_KEY`
  - Optional:
    - `EDUAI_ENDPOINT`
    - `EDUAI_MODEL`
    - `EDUAI_COURSE_CODE`
- **OpenRouter (Gemini 2.5 Flash)**:
  - `OPENROUTER_API_KEY`
  - `OPENROUTER_MODEL`
    - Defaults to `google/gemini-2.5-flash-preview-09-2025` if unset.
  - Optional:
    - `OPENROUTER_SITE_URL`
    - `OPENROUTER_SITE_NAME`

### Misconfigured Providers

When a run starts:

1. For each selected provider, perform a minimal configuration check:
   - If any required env var is missing or empty, mark the provider as
     misconfigured.
2. If **no** selected providers are valid:
   - Show a clear error and return to the provider selection screen.
3. If **some** providers are misconfigured:
   - Prompt per provider, e.g.:

     ```text
     OpenRouter configuration is incomplete (missing OPENROUTER_API_KEY).
     Continue without OpenRouter? [y/N]:
     ```

   - If user answers `y`:
     - Drop that provider and continue.
   - If user answers `n`:
     - Cancel the entire run and return to provider selection.

---

## Strategy Selection & Execution

### Strategy Menu (Updated)

- After provider selection and configuration checks, show the strategy
  menu:

  ```text
  Select a grading strategy:

    [1] Direct Grading
    [2] Reverse Grading
    [3] All (Direct + Reverse)

  Enter choice [1/2/3]:
  ```

- The old EME option is removed/hidden for now, pending a future
  redesign that supports multi-model “debate” ensembles.

### Behavior per Strategy

- **Direct Grading**:
  - For each student submission:
    - Call all **selected providers** using the direct grading prompt.
  - Collect results under explicit fields in the output JSON, e.g.:
    - `gpt5_nano_result`
    - `gpt_oss_120b_result`
    - `openrouter_gemini_2_5_flash_result` (or similar explicit name).

- **Reverse Grading**:
  - Exactly as above, but using the reverse grading strategy.

- **All (Direct + Reverse)**:
  - Run Direct first:
    - With the selected providers.
    - Show results.
  - Then run Reverse:
    - With the same providers.
    - Show results as a separate block.
  - Return to main menu afterwards.

---

## Results Presentation (Summary + Drill-Down)

The new provider-aware runs will output richer data; the CLI should
reflect that in an engaging way.

### Summary View

For each strategy run:

- Display a **summary table** with one row per student and columns for
  each provider, for example:

```text
Student                  OpenAI   EduAI   OpenRouter   Avg   Max diff
----------------------   ------   -----   ----------   ---   --------
Smith_John_123456           92      90         94      92.0      4.0
Doe_Jane_654321             78      80         79      79.0      2.0
...
```

- Columns:
  - Student identifier
  - One score per selected provider (percentage or raw score)
  - Average (if meaningful)
  - Max difference between providers

### Drill-Down View

- Allow the user to select a student (e.g. by index or by moving a
  cursor) and see a detailed view:

```text
Details for Smith_John_123456

  OpenAI (GPT-5 Nano)
  -------------------
  Total score:        92 / 100
  Feedback:
    ...

  EduAI (GPT-OSS 120B)
  --------------------
  Total score:        90 / 100
  Feedback:
    ...

  OpenRouter (Gemini 2.5 Flash)
  -----------------------------
  Total score:        94 / 100
  Feedback:
    ...

  [B]ack to summary
```

- Simple keys:
  - Up/Down or numeric selection to choose a student.
  - A key (e.g. `Enter` or a letter) to enter drill-down.
  - `B` or `Esc` to go back to summary.

(The exact navigation bindings can be finalized when wiring the CLI.)

---

## Data Model Changes (High-Level)

- Extend the in-memory evaluation result structure to include a field
  per provider:
  - `gpt5_nano_result`: existing OpenAI GPT‑5 Nano payload.
  - `gpt_oss_120b_result`: existing EduAI payload.
  - `openrouter_gemini_2_5_flash_result`: new field for the OpenRouter
    Gemini model.
- When a provider is not selected or fails, its field may be `null` or
  absent; downstream code should handle that gracefully.
- The database schema and JSON schema can remain largely the same,
  assuming they allow optional provider result fields.

---

## Future Work (Beyond This Plan)

- Reintroduce **EME** as a true multi-model ensemble:
  - Allow multiple providers to “debate” or vote on a grade.
  - Use structured outputs as the communication channel between models.
  - Design prompts that encourage models to critique and refine each
    other’s grades.
- Add CLI flags for non-interactive usage:
  - `--providers openai,openrouter`
  - `--strategy direct`
  so that scripts can bypass the interactive menus when desired.
- Enhance the results drill-down with:
  - Side-by-side diff views.
  - Highlighting where providers disagree.
  - Export options for specific providers only.
