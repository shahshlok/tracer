# Position Paper Abstract (ITiCSE 2026)

## Abstract (235 words)

LLM-based tutoring systems are increasingly deployed to provide feedback in introductory programming courses. While these systems excel at identifying syntactic errors and generating fluent explanations, we argue that current evaluation practices fail to measure a critical pedagogical capability: correctly inferring students' *notional machines*—their latent beliefs about how programs execute—from code evidence alone.

We contend that prevailing benchmarks, which emphasize bug detection recall, systematically obscure a high-stakes failure mode: **over-diagnosis**, where tutors attribute misconceptions to students whose code is correct. Belief attribution is epistemically asymmetric: telling students they hold misconceptions they do not have risks undermining confidence and trust in automated feedback. Yet specificity on correct submissions—arguably the more consequential metric—remains largely unmeasured.

Preliminary evidence supports this concern. When LLMs diagnose misconceptions on a controlled benchmark with known ground truth, the majority of errors arise not from missed detections but from false accusations on correct code. Evaluation shortcuts that match misconception labels rather than reasoning about student thinking further inflate apparent performance while masking this failure mode.

We propose a research agenda: (1) *narrative-fidelity metrics* requiring diagnoses to cite observable code evidence rather than pattern-match labels; (2) *specificity benchmarks* validated on authentic student submissions; (3) *uncertainty-aware interfaces* that surface confidence before presenting diagnostic claims; and (4) *principled deferral criteria* specifying when LLM tutors should abstain from diagnosis entirely. Until these safeguards exist, we urge caution in deploying LLM-based tutors for autonomous misconception attribution in CS1 courses.

---

## Design Notes

**Position paper identity:** This abstract argues a *position* (current evaluations are misaligned; over-diagnosis is under-measured) rather than presenting a tool or completed study. TRACER is implicitly supporting evidence, not the contribution.

**Structure:**
- P1 (52 words): Context + position statement
- P2 (60 words): The core argument (asymmetry, over-diagnosis harm)
- P3 (54 words): Supporting evidence (brief, no methodology details)
- P4 (69 words): Research agenda (the forward-looking contribution)

**Key changes from abstract2.md:**
1. Removed TRACER by name (it's supporting evidence, not the contribution)
2. Removed specific precision/recall numbers (keeps it position-focused)
3. Expanded research agenda from 1 sentence to full paragraph
4. Added explicit call to action ("we urge caution")
5. Reframed from "we introduce a framework" to "we argue that..."
6. Emphasized the *asymmetry* argument more strongly
7. Word count reduced from 287 to 235 (within 250 limit)

**Alignment with ITiCSE review criteria:**
- "Is the goal clearly stated?" → Yes, in P1-P2
- "Description of problem or need?" → Yes, P2 (over-diagnosis is unmeasured)
- "Related work positioning?" → Implicit (can expand in paper)
- "How is this different from previous ideas?" → Specificity focus is novel
- "Implications for CS education community?" → Research agenda in P4
- "Value of proposal?" → Concrete, actionable items in P4
