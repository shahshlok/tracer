 # Ensemble Grading Design
 
 This document explains a practical ensemble for grading introductory programming submissions using two LLM graders (gpt5_nano and eduai), plus lightweight static checks. It is designed to be predictable, fair, and aligned with the rubric while minimizing inconsistency.
 
 Goals
 - Maximize fairness on core requirements (compute, not hardcode; correct output; basic logic correctness).
 - Reduce instability across prompt variants (direct vs reverse) and grader models.
 - Produce actionable, consistent feedback aligned to rubric categories.
 
 Inputs
 - Source: `student_submissions/*/SumCalculator.java`
 - Model scores + feedback: JSON outputs from direct, reverse, and optional EME (mutual critique) runs
   - `data/results_direct.json`
   - `data/results_reverse.json`
   - `data/results_eme.json` (optional)
 - Rubric (100 points total)
   - Syntax & Compilation: 25
   - Logic & Correctness: 25
   - Output & Functionality: 25
   - Style & Documentation: 25
 
 Architecture
 - Static policy checks: fast, deterministic detectors for critical failure modes.
 - Dual graders: independent scores from gpt5_nano and eduai.
 - Disagreement handler: robust aggregator to resolve conflicting scores.
 - Feedback combiner: merge, prioritize, and de-duplicate comments.
 - Consistency guard: detect run-to-run variance and flag for review when unstable.
 
 Static Policy Checks (non-negotiables)
 These are rule-based detectors that cap the maximum possible total regardless of model scores. They directly encode assignment intent.
 - HardcodedOnly: Flags if output is hardcoded (e.g., prints 5050) with no computation.
   - Heuristics: no loop or accumulation or formula; presence of string "5050" or literal 5050 printed; no arithmetic summation or range loop.
   - Cap: total ≤ 25.
 - PrintInLoop: Prints inside the loop, yielding running totals rather than final output.
   - Heuristics: `System.out.println` inside a `for`/`while` loop.
   - Cap: total ≤ 65.
 - EvenOnly: Sums only even numbers or increments by 2 while claiming full sum.
   - Heuristics: `(i % 2 == 0)` gate around accumulation; `i += 2` style for 2..100; variable names not sufficient alone.
   - Cap: total ≤ 55.
 - OffByOne: Excludes 100 (e.g., `i < 100`) while summing 1..100.
   - Heuristics: loop upper bound `< 100` with accumulation; absence of a compensating print of 5050.
   - Cap: total ≤ 60.
 - Compilation failure (optional if you compile): If code does not compile, total ≤ 25.
 
 Notes
 - Multiple flags: apply the minimum cap among triggered flags.
 - Heuristics are conservative by design; treat uncertainty as a non-trigger and rely on models to penalize.
 
 Model Grading
 - Run both models with the same rubric and strict, deterministic prompts.
 - Recommended generation settings: low temperature (≤ 0.2), fixed seed if available, no tool augmentation.
 - Ask for component-level scores (per rubric category), plus bullet reasons and concrete evidence lines.
 
 Suggested Prompt (sketch)
 - System: "You grade student Java code. Follow the rubric exactly and return strict JSON. Do not speculate."
 - User: Include (1) rubric, (2) assignment description, (3) student code, (4) explicit binary checks:
   - "Does the program compute the sum (yes/no)?"
   - "Is any output printed inside a loop (yes/no)?"
   - "Is there an off-by-one upper bound or even-only logic (yes/no)?"
   - Return JSON with fields: `syntax`, `logic`, `output`, `style`, `total`, `issues`[], `flags`.
 
 Disagreement Handling (score aggregation)
 - Define `g = gpt5_total`, `e = eduai_total`, `cap = policy_cap` (∞ if no flags).
 - If any non-negotiable flag triggers → `final = min(min(g, e), cap)`.
 - Else, resolve by agreement band:
   - If |g − e| ≤ 5 → `final = round((g + e) / 2)`.
   - If 5 < |g − e| ≤ 15 → `final = min(g, e)`.
   - If |g − e| > 15 → `final = min(g, e)` and set `instability_flag = true`.
 - Optional: If EME (mutual critique) run exists, you may compute model means over direct+reverse before applying bands.
 
 Feedback Aggregation
 - Consensus first: intersection of issues both models cite.
 - Then complement: union of remaining issues, labeled by source ("gpt5" / "eduai").
 - Promote policy flags to the top with clear, actionable directives.
 - Keep feedback aligned to rubric sections and reflect numeric deductions.
 
 Consistency Guard (run-to-run)
 - Compute per-model deltas between direct and reverse.
 - If a model’s |Δ| > 15 on the same submission, reduce trust in that model for aggregation (prefer the other or the minimum), and surface an "instability" note.
 
 End-to-End Workflow
 - Input: student code path.
 - Step 1: Policy checks
   - Run static detectors (HardcodedOnly, PrintInLoop, EvenOnly, OffByOne, optional compile).
   - Compute `cap` as the minimum applicable cap, else ∞.
 - Step 2: Dual grading
   - Query eduai and gpt5_nano with identical rubric prompts.
   - Optionally run direct and reverse (prompt frames swapped) to measure stability.
   - Optionally run EME: each model reviews the other’s grade and rationale.
 - Step 3: Aggregate scores
   - For each model, average direct+reverse (if both present).
   - Apply disagreement handling with `cap`.
 - Step 4: Aggregate feedback
   - Build consensus issues, then model-specific issues.
   - Prepend any policy-flag violations with fixes.
 - Step 5: Emit result
   - Final score, instability flags (if any), caps applied and why, and consolidated feedback.
 
 Heuristic Detectors (pseudo)
 - HardcodedOnly (Java):
   - If no `for` or `while` present AND output contains literal `5050` AND no accumulation (`+= i`, `sum = sum + i`, or `n*(n+1)/2`) → flag.
 - PrintInLoop:
   - If `System.out.println` appears syntactically within loop braces detected by simple nesting → flag.
 - EvenOnly:
   - If `(i % 2 == 0)` guards the accumulation OR the loop increments by 2 with start at 2 and no compensating odd sum → flag.
 - OffByOne:
   - If accumulation loop condition uses `< 100` with start at 1, and no separate add of 100 → flag.
 
 Suggested Caps (tunable defaults)
 - HardcodedOnly: 25
 - EvenOnly: 55
 - OffByOne: 60
 - PrintInLoop: 65
 - Compilation failure: 25
 
 Example: Taylor_Chris_901234 (hardcoded)
 - Policy checks: HardcodedOnly = true → cap = 25.
 - Model scores: g=55, e=55 (vary by run).
 - Aggregation: final = min(min(55, 55), 25) = 25.
 - Feedback (consensus + policy):
   - "Must compute the sum (loop or formula); hardcoded output is not acceptable."
   - Any style notes appear after the core fix.
 
 Example: Moore_Jessica_890123 (print-in-loop)
 - Policy checks: PrintInLoop = true → cap = 65.
 - Model scores vary by run (e.g., g=60→85, e=65→63).
 - Aggregation: with disagreement > 15 in some runs, choose min(g, e) first, then apply cap → final ≤ 65. Instability flagged when |g − e| > 15.
 
 Operational Tips
 - Keep prompts identical across runs; use low temperature.
 - Require models to output JSON with per-category scores to align deductions and feedback with the rubric.
 - Log detector outputs alongside model grades for auditability.
 - Periodically calibrate caps by sampling human grades.
 
 Extending Beyond This Question
 - Add more detectors tailored to other tasks (e.g., I/O handling, file naming, exception handling).
 - Integrate a compile-run sandbox to verify numeric outputs and detect trivial hardcoding at runtime (optional but recommended).
 
 Deliverables
 - Final score with provenance (gpt5 vs eduai, caps applied).
 - Consolidated feedback with policy violations first, consensus second, model-specific insights last.
 - Stability indicators (per-model deltas, disagreement band).
 
 Limitations
 - Static detectors are heuristic and may miss cleverly disguised cases; prefer conservative triggering.
 - Without per-category scores from models, aligning feedback to rubric points requires either model re-prompting or post-hoc scaling.
 
Change Control
- All caps and thresholds are configuration knobs; adjust after small blind trials to best match human grading.

---

# Workflow Diagram

Below are two equivalent representations of the grading workflow: a Mermaid diagram for renderers that support it, and an ASCII diagram for universal readability.

Mermaid

```mermaid
flowchart TD
    A[Student Code<br/>SumCalculator.java] --> B[Static Policy Checks<br/>(HardcodedOnly, PrintInLoop, EvenOnly, OffByOne, Compile?) ]
    B --> C{Any cap triggered?}
    C -- Yes --> C1[Compute cap (min of triggered caps)]
    C -- No --> D
    C1 --> D
    subgraph Dual Grading
      D1[Prompt: direct] --> D[gpt5_nano]
      E1[Prompt: direct] --> E[eduai]
      D2[Prompt: reverse] --> D
      E2[Prompt: reverse] --> E
    end
    D --> H[Per-model avg (direct+reverse)]
    E --> H
    H --> I{EME available?}
    I -- Yes --> I1[EME critique round]
    I -- No --> J
    I1 --> J[Aggregate Scores]
    J --> K{Disagreement band}
    K -- |g-e| ≤ 5 --> L[Average (g+e)/2]
    K -- 5 < |g-e| ≤ 15 --> M[Minimum(g,e)]
    K -- |g-e| > 15 --> N[Minimum(g,e) + Instability Flag]
    L --> O[Apply cap]
    M --> O
    N --> O
    O --> P[Feedback Aggregation
              • Policy flags first
              • Consensus issues
              • Model-specific issues]
    P --> Q[Final Grade & Report
             • Score, Caps, Flags
             • Feedback
             • Stability notes]
```

ASCII

```
  [Student Code]
         |
         v
  [Static Policy Checks] --caps/flags--> (cap)
         |
         v
  +------------------- Dual Grading -------------------+
  |  gpt5_nano: direct + reverse  -> per-model average |
  |  eduai:     direct + reverse  -> per-model average |
  +----------------------------------------------------+
         |
         v
  [Optional EME critique round]
         |
         v
  [Aggregate Scores]
         |
         v
  {Disagreement band}
   |-- |g-e| ≤ 5  --> use average
   |-- 5<|g-e|≤15 --> use minimum(g,e)
   |-- |g-e| > 15 --> use minimum + set instability flag
         |
         v
  [Apply cap from policy checks]
         |
         v
  [Aggregate Feedback]
   - Policy violations first (with fixes)
   - Consensus issues from both models
   - Model-specific insights (labeled)
         |
         v
  [Final Grade & Report]
   - Score, caps applied, flags
   - Consolidated feedback
   - Stability/consistency notes
```
