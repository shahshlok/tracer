# Grading Analysis (v3): results_direct, results_reverse, results_eme

This report impartially evaluates `gpt5_nano` and `eduai` on the current non‑v2 benchmark files and follows your five‑step methodology. It uses only:
- `data/results_direct.json`
- `data/results_reverse.json`
- `data/results_eme.json`
- The Java files in `student_submissions/*/SumCalculator.java`

Question: Write a Java program named `SumCalculator.java` that calculates and prints the sum of all integers from 1 to 100 (inclusive).

Rubric (100 pts):
- Syntax & Compilation (25)
- Logic & Correctness (25)
- Output & Functionality (25)
- Style & Documentation (25)

---

## Methodology (Applied)
- Ground truth first: Manually reviewed each submission against the rubric (without model scores).
- Collated data: Pulled both models’ scores and feedback from results_direct, results_reverse, and results_eme.
- Per‑student comparison: Judged score fairness and feedback accuracy against ground truth.
- Consistency: Compared direct vs reverse scores and comments per student.
- Synthesis: Aggregated patterns, quantified run‑to‑run variance, and issued a verdict with recommendations.

Ground truth interpretation used here:
- Must compute the sum (not hardcode).
- Acceptable: `for` or `while`, printing once at the end. Descriptive message text is fine; printing the running total is incorrect.

---

## Ground Truth by Student

- Anderson_Laura_012345 (`student_submissions/Anderson_Laura_012345/SumCalculator.java`:2)
  - Correct. Computes 1..100; prints final total once.

- Brown_Emily_456789 (`student_submissions/Brown_Emily_456789/SumCalculator.java`:2)
  - Correct. Loop 0..100 computes correct total; prints once.

- Davis_Michael_567890 (`student_submissions/Davis_Michael_567890/SumCalculator.java`:6)
  - Incorrect. Sums even numbers only; wrong total and output.

- Johnson_Jane_234567 (`student_submissions/Johnson_Jane_234567/SumCalculator.java`:2)
  - Correct. Style is weak (variables `s`, `x`) but functionally correct.

- Miller_Sarah_678901 (`student_submissions/Miller_Sarah_678901/SumCalculator.java`:2)
  - Correct. Computes and prints once.

- Moore_Jessica_890123 (`student_submissions/Moore_Jessica_890123/SumCalculator.java`:7)
  - Incorrect. Prints running total inside the loop; wrong output format.

- Smith_John_123456 (`student_submissions/Smith_John_123456/SumCalculator.java`:2)
  - Correct. Computes and prints once.

- Taylor_Chris_901234 (`student_submissions/Taylor_Chris_901234/SumCalculator.java`:4)
  - Incorrect. Hardcodes “5050”; no computation.

- Williams_Robert_345678 (`student_submissions/Williams_Robert_345678/SumCalculator.java`:5)
  - Incorrect. Off‑by‑one (`i < 100`), sums 1..99; wrong total and output.

- Wilson_David_789012 (`student_submissions/Wilson_David_789012/SumCalculator.java`:2)
  - Correct. `while` loop; prints once.

---

## Per‑Student Findings (Scores and fairness)

Format per student: Direct gpt5/eduai — fairness; Reverse gpt5/eduai — fairness; EME gpt5/eduai — fairness.

- Anderson (Correct)
  - Direct 100/95 — Fair; Reverse 100/98 — Fair; EME 100/100 — Fair.

- Brown (Correct)
  - Direct 100/92 — Fair; Reverse 100/90 — Fair; EME 100/95 — Fair.

- Davis (Even‑only)
  - Direct 45/45 — Fair (significant logic error);
    Reverse 50/42 — Fair (eduai slightly stricter);
    EME 50/45 — Fair.

- Johnson (Correct, weak style)
  - Direct 95/95 — Fair; Reverse 100/95 — Fair; EME 100/97 — Fair.

- Miller (Correct)
  - Direct 100/95 — Fair; Reverse 100/95 — Fair; EME 100/98 — Fair.

- Moore (Print‑in‑loop)
  - Direct 60/65 — gpt5 Fair‑leaning, eduai Lenient;
    Reverse 85/63 — gpt5 Unfairly lenient outlier, eduai Fair;
    EME 75/65 — gpt5 Lenient, eduai Fair.

- Smith (Correct)
  - Direct 100/95 — Fair; Reverse 100/97 — Fair; EME 100/100 — Fair.

- Taylor (Hardcoded)
  - Direct 55/55 — Unfairly lenient (must compute);
    Reverse 70/60 — Unfairly lenient;
    EME 45/70 — gpt5 Stricter but still lenient; eduai Unfairly lenient.

- Williams (Off‑by‑one)
  - Direct 50/55 — gpt5 Fair, eduai Slightly lenient;
    Reverse 45/55 — gpt5 Fair, eduai Slightly lenient;
    EME 50/50 — Fair.

- Wilson (Correct)
  - Direct 100/95 — Fair; Reverse 100/95 — Fair; EME 100/100 — Fair.

---

## Consistency and Strictness

- Run‑to‑run variance (|reverse − direct|; mean over 10 students)
  - gpt5: ≈ 5.5 pts (max 25: Moore 60→85; Taylor +15)
  - eduai: ≈ 1.7 pts (max 5)
  - Takeaway: eduai is more consistent; gpt5 has a few larger swings, including one major outlier (Moore).

- Average scores by ground‑truth group
  - Correct submissions
    - Direct: gpt5 99.17, eduai 94.50
    - Reverse: gpt5 100.00, eduai 95.00
    - EME: gpt5 100.00, eduai 98.33
  - Incorrect submissions
    - Direct: gpt5 52.50, eduai 55.00
    - Reverse: gpt5 62.50, eduai 55.00
    - EME: gpt5 55.00, eduai 57.50
  - Takeaway: Both models are lenient on incorrect work; gpt5 is sometimes stricter (especially in EME for Taylor), but also produces a highly lenient outlier (Moore 85 in reverse).

- Feedback accuracy (qualitative)
  - Generally good: both models correctly flag even‑only (Davis), off‑by‑one (Williams), and print‑in‑loop (Moore).
  - Mismatch on Taylor: both acknowledge “no computation,” yet still assign passing scores across runs; this contradicts the rubric’s intent.

---

## Verdict (for results_direct, results_reverse, results_eme)

- Neither model is ideal as an automated grader for this task without guardrails.
- Consistency: eduai is more stable between direct and reverse.
- Strictness: gpt5 is sometimes stricter (notably EME on Taylor), but also exhibits larger variability and an overly lenient outlier (Moore 85 in reverse).
- Fairness on major failures: both are too lenient on hardcoded output (Taylor) and, at times, on incorrect output format (Moore).

If forced to choose for stability today: eduai. If prioritizing stricter penalties when they occur: gpt5, with the caveat of higher variance and occasional leniency spikes.

---

## Recommendations

- Enforce non‑negotiables in prompts or post‑processing:
  - Automatic near‑fail if no computation is performed (reject pure hardcoded output).
  - Penalize printing running totals; require a single final print.
- Calibrate scoring: Ensure numeric deductions match feedback severity per rubric category.
- Add determinism aids: Keep grading prompts identical across runs and lower temperature.
- Consider an ensemble: Use eduai for stability; run a gpt5 strictness check for logic/output errors; take the minimum score for flagged failures (hardcoded, off‑by‑one, print‑in‑loop).

---

## Appendix: Score Snapshot

- Anderson — Direct 100/95; Reverse 100/98; EME 100/100
- Brown — Direct 100/92; Reverse 100/90; EME 100/95
- Davis — Direct 45/45; Reverse 50/42; EME 50/45
- Johnson — Direct 95/95; Reverse 100/95; EME 100/97
- Miller — Direct 100/95; Reverse 100/95; EME 100/98
- Moore — Direct 60/65; Reverse 85/63; EME 75/65
- Smith — Direct 100/95; Reverse 100/97; EME 100/100
- Taylor — Direct 55/55; Reverse 70/60; EME 45/70
- Williams — Direct 50/55; Reverse 45/55; EME 50/50
- Wilson — Direct 100/95; Reverse 100/95; EME 100/100

Data sources: `data/results_direct.json`, `data/results_reverse.json`, `data/results_eme.json`.

