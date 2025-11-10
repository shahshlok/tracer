# Grading Analysis (v2): gpt5_nano vs eduai

This report re-evaluates both models on the current benchmark (v2) and replaces prior write-ups that mixed in older runs and incorrect assumptions. It follows the stated five‑step methodology and uses only:
- `data/v2_results_direct.json`
- `data/v2_results_reverse.json`
- The Java files in `student_submissions/*/SumCalculator.java`

Question: Write a Java program named `SumCalculator.java` that calculates and prints the sum of all integers from 1 to 100 (inclusive).

Rubric (100 pts):
- Syntax & Compilation (25)
- Logic & Correctness (25)
- Output & Functionality (25)
- Style & Documentation (25)

Important correction vs older analyses: In the v2 submissions, every file is `SumCalculator.java` with `public class SumCalculator`. There is no class/file naming mismatch in v2.

---

## Methodology (Applied)
- Ground truth first: Manually reviewed each submission against the rubric (without model scores).
- Collated data: Pulled both models’ scores and feedback from the v2 direct and reverse JSONs.
- Per‑student comparison: Judged score fairness and feedback accuracy against ground truth.
- Consistency: Compared each model’s direct vs reverse scores and comments per student.
- Synthesis: Aggregated patterns, quantified run‑to‑run variance, and issued a verdict with recommendations.

Ground truth interpretation used here:
- Must compute the sum (not hardcode).
- Acceptable: `for` or `while`, printing once at the end. Descriptive message text is fine; printing the running total is incorrect.

---

## Ground Truth by Student

- Anderson_Laura_012345 (`student_submissions/Anderson_Laura_012345/SumCalculator.java`:2)
  - Status: Correct. Computes 1..100; prints final total once.

- Brown_Emily_456789 (`student_submissions/Brown_Emily_456789/SumCalculator.java`:2)
  - Status: Correct. Loop 0..100 still computes correct total; prints once.

- Davis_Michael_567890 (`student_submissions/Davis_Michael_567890/SumCalculator.java`:6)
  - Status: Incorrect. Sums even numbers only; wrong total and output.

- Johnson_Jane_234567 (`student_submissions/Johnson_Jane_234567/SumCalculator.java`:2)
  - Status: Correct. Style is weak (variables `s`, `x`) but functionally correct.

- Miller_Sarah_678901 (`student_submissions/Miller_Sarah_678901/SumCalculator.java`:2)
  - Status: Correct. Computes and prints once.

- Moore_Jessica_890123 (`student_submissions/Moore_Jessica_890123/SumCalculator.java`:7)
  - Status: Incorrect. Prints running total inside the loop; wrong output format.

- Smith_John_123456 (`student_submissions/Smith_John_123456/SumCalculator.java`:2)
  - Status: Correct. Computes and prints once.

- Taylor_Chris_901234 (`student_submissions/Taylor_Chris_901234/SumCalculator.java`:4)
  - Status: Incorrect. Hardcodes “5050”; no computation.

- Williams_Robert_345678 (`student_submissions/Williams_Robert_345678/SumCalculator.java`:5)
  - Status: Incorrect. Off‑by‑one (`i < 100`), sums 1..99; wrong total and output.

- Wilson_David_789012 (`student_submissions/Wilson_David_789012/SumCalculator.java`:2)
  - Status: Correct. `while` loop; prints once.

---

## Per‑Student Findings (Scores: direct → reverse)

- Anderson (Correct)
  - gpt5: 100 → 100 — Fair; feedback aligned.
  - eduai: 95 → 97 — Fair; feedback aligned.

- Brown (Correct)
  - gpt5: 100 → 95 — Fair; small style/output nit in reverse.
  - eduai: 97 → 95 — Fair.

- Davis (Even‑only)
  - gpt5: 50 → 50 — Fair to slightly lenient; error well identified.
  - eduai: 45 → 50 — Fair to slightly lenient.

- Johnson (Correct, weak naming style)
  - gpt5: 100 → 100 — Slightly lenient on style; acceptable overall.
  - eduai: 95 → 95 — Fair; notes style.

- Miller (Correct)
  - gpt5: 100 → 90 — Fair; reverse slightly stricter on output phrasing.
  - eduai: 98 → 98 — Fair.

- Moore (Print‑in‑loop)
  - gpt5: 55 → 75 — 55 Fair; 75 Unfairly lenient given incorrect output.
  - eduai: 65 → 63 — Fair; feedback accurate.

- Smith (Correct)
  - gpt5: 100 → 95 — Fair; minor nits in reverse.
  - eduai: 98 → 95 — Fair.

- Taylor (Hardcoded)
  - gpt5: 75 → 60 — Unfairly lenient; feedback admits “no computation,” score too high.
  - eduai: 70 → 65 — Unfairly lenient for missing the assignment’s core requirement.

- Williams (Off‑by‑one)
  - gpt5: 50 → 50 — Fair; feedback accurate.
  - eduai: 70 → 55 — 70 Unfairly lenient; 55 Fair.

- Wilson (Correct)
  - gpt5: 100 → 100 — Fair.
  - eduai: 97 → 95 — Fair.

---

## Consistency and Strictness (v2 only)

- Run‑to‑run variance (|reverse − direct|; mean over 10 students)
  - gpt5: ≈ 5.5 pts (max 20: Moore 55→75; −15 Taylor; −10 Miller)
  - eduai: ≈ 3.6 pts (max 15: Williams 70→55)
  - Takeaway: eduai is more consistent; gpt5 has a few larger swings.

- Average scores by ground‑truth group
  - Correct submissions (6 students)
    - gpt5: 100.0 (direct), 96.7 (reverse)
    - eduai: 96.7 (direct), 95.8 (reverse)
  - Incorrect submissions (4 students)
    - gpt5: 57.5 (direct), 58.8 (reverse)
    - eduai: 62.5 (direct), 58.3 (reverse)
  - Takeaway: Both models are lenient on incorrect work; gpt5 slightly stricter on the direct run; both too generous on hardcoded output.

- Feedback accuracy
  - Generally solid for both models: they correctly name even‑only (Davis), off‑by‑one (Williams), and print‑in‑loop (Moore).
  - Mismatch on Taylor: both call out “no computation,” yet still assign passing scores (60–75).

---

## What the Previous Write‑Up Got Wrong (and why this replaces it)

- It asserted widespread class/file naming mismatches and compilation failures. In v2, every submission is `public class SumCalculator` in `SumCalculator.java`; no such failures occur.
- It cited large GPT‑5 swings (e.g., 100 vs 37 for the same submission) that appear only in `data/old_results_*`, not in v2.
- Verdicts and per‑student narratives were driven by older data and do not match the code or v2 JSONs.

---

## Verdict

- Neither model is an ideal grader for this task as‑is.
- eduai: More consistent across runs and generally accurate in feedback, but tends to be lenient on serious requirement failures (notably hardcoded output).
- gpt5_nano: Sometimes slightly stricter on logic errors, but inconsistent run‑to‑run and also lenient on the critical “must compute” requirement.
- If forced to choose for stability today: eduai. If prioritizing stricter logic penalties: gpt5_nano, but expect variance and still fix hardcoded‑answer leniency.

---

## Recommendations

- Enforce non‑negotiables in prompts or post‑processing:
  - Automatic near‑fail if no computation is performed (reject pure hardcoded output).
  - Penalize printing running totals; require a single final print.
  - Keep compilation as a gate (already met in v2).
- Calibrate scoring: Align point deductions with rubric categories so feedback severity matches the numeric score.
- Add determinism aids: Provide the same, minimal context and seed where possible; constrain temperature for grading prompts.
- Consider an ensemble: Use eduai for stability, gpt5_nano for second‑opinion strictness on logic; take the minimum score for flagged failure modes (hardcoded, off‑by‑one, print‑in‑loop).

---

## Appendix: Score Snapshot (v2)

- Anderson: gpt5 100→100, eduai 95→97 (Correct)
- Brown: gpt5 100→95, eduai 97→95 (Correct)
- Davis: gpt5 50→50, eduai 45→50 (Even‑only)
- Johnson: gpt5 100→100, eduai 95→95 (Correct, weak style)
- Miller: gpt5 100→90, eduai 98→98 (Correct)
- Moore: gpt5 55→75, eduai 65→63 (Print‑in‑loop)
- Smith: gpt5 100→95, eduai 98→95 (Correct)
- Taylor: gpt5 75→60, eduai 70→65 (Hardcoded)
- Williams: gpt5 50→50, eduai 70→55 (Off‑by‑one)
- Wilson: gpt5 100→100, eduai 97→95 (Correct)

Data sources: `data/v2_results_direct.json`, `data/v2_results_reverse.json`.

