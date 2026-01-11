# ITiCSE 2026 Position Paper Abstract (Revised)

A CS1 student submits code that passes every test—because an LLM wrote it. What does the student believe the program is doing?

Notional machines capture learners’ latent models of how code executes. In AI-assisted programming (“vibe coding”), surface errors become a weaker signal of understanding, making belief diagnosis central to tutoring. We ask: can LLM tutors infer student beliefs from code evidence, or do they primarily produce plausible post-hoc rationales?

We argue that today’s evaluation practices (bug labels, test outcomes, explanation quality) are misaligned with this goal. Belief diagnosis is epistemically asymmetric: a false positive (“you think X”) on correct work wastes time and erodes trust, yet specificity on clean submissions is rarely reported. We propose that tutor evaluations should require an explicit belief narrative grounded in submission evidence and should treat diagnoses as hypotheses with calibrated uncertainty.

As supporting evidence, we report results from TRACER, a label-blind narrative-matching evaluation on 1,200 Java CS1 submissions (275 seeded misconceptions, 925 clean). Across six LLMs, models show high recall (0.87) but moderate precision (0.58), and most false positives are over-diagnoses on clean code (specificity 0.85). A label-aware shortcut increases recall (0.98) while reducing specificity (0.77), illustrating how easy-to-game evaluations can amplify harm.

We outline a research agenda for narrative-fidelity metrics, validation on authentic student data, and tutoring interfaces that surface uncertainty and defer when evidence is weak.
