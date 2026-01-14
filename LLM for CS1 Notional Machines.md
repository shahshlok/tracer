# Notional Machines, Not Just Errors: Toward Belief Attribution with Instructor-Facing LLMs

## Abstract

Explaining a bug is a straightforward task; however, the true labor of tutoring lies in understanding why a student wrote it. In CS1, the better instructional signal is often a student’s notional machine, their mental model of how programs execute, rather than surface correctness alone. Reasoning about these beliefs can reveal where instruction breaks down, but it demands epistemic restraint: separating what the code structurally supports from higher-level inferences about student intent. Moreover, belief attribution is risk-asymmetric: falsely diagnosing a misconception is significantly more harmful than abstaining.

As large language models (LLMs) enter CS1 classrooms, we argue that their primary opportunity lies not in automated fixing but in supporting instructors in reasoning about plausible student beliefs expressed through code. LLMs should be treated as instructor-facing hypothesis generators that help instructors understand students’ notional machines, beyond syntax and logic errors.

To ground this position, we report evidence from TRACER, a controlled testbed for evaluating belief attribution. In 5-fold cross-validation on 1,200 synthetic CS1 Java submissions spanning 18 notional machines, models achieve recall of 0.87 and specificity of 0.85, with a majority of false positives driven by over-diagnosis on correct programs. A label-inclusive matching strategy, by comparison, saturates recall (0.98) while degrading specificity (0.77), illustrating how evaluation shortcuts can inflate impressions of capability while worsening safety. Consequently, we argue that belief-oriented LLM support is a worthwhile research direction, but it requires standards that prioritize diagnostic humility over broad coverage.

## CCS Concepts

- Applied computing → Education
- Computing methodologies → Artificial intelligence

## Keywords

CS1; notional machines; misconceptions; student modeling; instructor-facing tools; large language models; diagnostic humility

## 1. Introduction: Beyond Fixes, Toward Belief-Aware Teaching

Large language models are now part of many CS1 feedback workflows. They can explain compiler errors, propose edits, and generate hints at scale. Used well, these systems reduce friction and increase access to help—especially for students who cannot get timely human support.

Our position is that there is a second, instructor-centered opportunity that is currently under-developed: using LLMs to help instructors reason about what student code suggests about students’ notional machines—their mental models of how programs execute. Instructors often care less about whether a single submission is correct than about what a pattern of errors reveals about how students think, because that is what determines what to teach next.

This is not an argument against “AI-as-tutor.” Automated explanations and repairs can be genuinely helpful, particularly for practice. The argument is about what the field should treat as the primary pedagogical object and safety target when LLMs infer beliefs. Bug fixing is about the program. Belief attribution is about the person who wrote it.

Belief attribution is intrinsically uncertain and risk-asymmetric: a false-positive diagnosis (“this student believes X”) can be more harmful than abstaining. Novices often cannot reliably adjudicate between competing explanations, and a confident but wrong story can induce confusion, mistrust, and wasted effort.

**Thesis.** LLMs should be designed and evaluated primarily as instructor-facing hypothesis generators for students’ notional machines, not as student-facing arbiters of what students “believe.”

To make this position concrete, we pose three questions that a serious instructor-facing system must answer:

1. **Feasibility / capability:** Given only a CS1 submission (and its task context), can an LLM generate plausible hypotheses about the student’s latent notional machine—beyond surface bug descriptions?
2. **Safety / trigger-happiness:** How often does the model over-diagnose—attributing a misconception when the program is clean or the evidence is ambiguous—and what false-positive burden does that impose?
3. **Coverage limits / blind spots:** Which misconception families are reliably diagnosable versus systematically hard, and what does that imply for how instructors should interpret hypotheses?

This paper contributes (i) an argument for instructor-facing belief attribution as a distinct design goal, (ii) a set of design and evaluation requirements that operationalize diagnostic humility, and (iii) a brief empirical grounding using a controlled probe (TRACER) that illustrates both feasibility and safety risks.

## 2. Background: Notional Machines and the Diagnostic Task

### 2.1 Notional machines and mental models

A notional machine is the simplified explanatory “machine” that a course implicitly teaches: how variables change, how control flow proceeds, how functions/methods return values, how references behave, and so on. Students develop mental models that approximate this notional machine. Misconceptions are structured divergences—reasonable-seeming rules that work in other domains (algebra, natural language, spreadsheets) but conflict with programming semantics.

Notional machines matter because they connect single submissions to instructional action. A buggy program can be “fixed” in many ways, but a misconception can be addressed only by targeting the underlying model.

### 2.2 Belief attribution as an inverse problem

Inferring a student’s notional machine from code is an inverse problem: a latent belief state produces an observable artifact. Multiple different beliefs (or a simple slip) can generate the same code. The posterior over beliefs is often multi-modal.

This uncertainty is exactly why belief attribution should not be treated as a one-shot “diagnose the misconception” prompt. The goal is to produce inspectable hypotheses that an instructor can verify.

### 2.3 One bug, many plausible beliefs (worked example)

Consider a student trying to sum an array:

```java
int sum = 0;
for (int i = 0; i < nums.length; i++) {
    sum += i; // uses index, not value
}
```

Several explanations are plausible:

- **Hypothesis A (index–value confusion):** the student believes `i` is the element.
- **Hypothesis B (attention slip):** the student intended `nums[i]` but omitted it.
- **Hypothesis C (template transfer):** the student copied a loop skeleton without integrating what `i` means.

A student-facing system that commits to one story risks misdiagnosis. An instructor-facing system can surface alternatives and ask for verification.

## 3. Position: Instructor-Facing LLMs as Hypothesis Generators

### 3.1 What an instructor-facing belief attribution system should do

An instructor-facing LLM should:

- Generate a small set of plausible misconception hypotheses (not a single definitive label).
- Ground each hypothesis in concrete evidence (code spans, and I/O behavior if available).
- Express uncertainty and support abstention when evidence is ambiguous.
- Aggregate across many submissions to surface cohort-level patterns.

The unit of value is not “the model fixed the code,” but “the model helped the instructor decide where to look next.”

### 3.2 A high-specificity workflow

A practical workflow is:

1. **Ingest** submissions for an activity or assignment.
2. **Generate hypotheses** with evidence traces, optionally including multiple competing hypotheses.
3. **Filter for safety** by prioritizing specificity and suppressing low-evidence conjectures.
4. **Cluster by belief** to surface patterns across students rather than making isolated claims about individuals.
5. **Verify and act:** instructors inspect representative examples from clusters, then intervene via lecture adjustments, targeted messages, or tracing exercises.

This workflow keeps belief attribution instructor-facing while still leveraging LLM scale.

### 3.3 Diagnostic humility is the central design constraint

Because belief attribution is risk-asymmetric, the default stance must be restraint:

- Prefer “I’m not sure” over a confident guess.
- Prefer “here are two plausible explanations” over a single narrative.
- Prefer “here is what the code supports” over “here is what the student believes.”

### 3.4 What we are (and are not) claiming

We are not claiming:

- That an LLM can recover a student’s “true beliefs.”
- That belief attribution should be delivered directly to students as authoritative diagnosis.
- That belief hypotheses should be used for grading or labeling.
- That current systems are safe enough without careful interface design and evaluation.

We are claiming:

- That hypothesis generation about notional machines can be useful to instructors when it is evidence-linked, uncertainty-aware, and verified by humans.
- That evaluation must explicitly reward humility (specificity and abstention), not just coverage.

## 4. How to Evaluate This Direction (Q1–Q3)

### 4.1 Two-stage plausibility

“Plausible hypothesis” is not a single property. We propose a two-stage evaluation:

- **Stage 1 (proxy-alignment on controlled probes):** does the model produce hypotheses that align with a known injected or otherwise operationalized ground truth?
- **Stage 2 (instructor-judged plausibility/usefulness):** do instructors judge the hypotheses (and their evidence) as reasonable and actionable for teaching decisions?

Stage 1 is necessary to study feasibility at scale. Stage 2 is necessary to claim educational usefulness.

### 4.2 Safety metrics must be first-class

For belief attribution, reporting only accuracy or F1 is insufficient. At minimum, evaluation should report:

- False positives on clean programs (behaviorally correct submissions).
- Abstention behavior (when the model chooses not to diagnose).
- The verification burden: how much instructor effort is needed to dismiss spurious clusters.

### 4.3 Coverage and blind spots

Not all misconceptions are equally observable from code. Some produce strong surface signatures; others require deeper semantic inference or depend on unseen intent. Evaluation should therefore report performance by misconception family and make the “structural vs. semantic” gap explicit.

## 5. Evidence: TRACER as a Controlled Probe

TRACER is used here as supporting evidence, not as a claim of classroom-ready student modeling. It consists of 1,200 synthetic CS1 Java submissions across three assignments and 18 misconception labels. The benchmark is designed so each synthetic student contributes one behaviorally failing submission with an injected misconception and three behaviorally correct submissions; generation uses a 4×3 persona matrix (coding style × cognitive profile) to reduce near-duplicate canonical forms.

TRACER evaluates whether an LLM can generate a hypothesis about student thinking aligned with the injected ground truth. In the main condition, matching compares hypothesized “student thinking” to injected “student thinking” while excluding label text. An ablation includes label text to illustrate how evaluation shortcuts can inflate apparent capability while worsening safety. The matching does not score whether cited evidence spans entail the hypothesis.

**Table 1: TRACER summary metrics (5-fold CV)**

| Condition | Precision | Recall | Specificity |
|---|---:|---:|---:|
| Standard matching (main run) | 0.577 | 0.872 | 0.848 |
| Label-inclusive matching (ablation) | 0.511 | 0.982 | 0.774 |

**Figure 1: Structural vs. semantic misconception gap (TRACER main run).**

![TRACER structural vs semantic gap](runs/run_final_main/assets/category_structural_vs_semantic.png)

**Figure 2: False positives are dominated by clean-code over-diagnosis.**

![TRACER false-positive flow](runs/run_final_main/assets/hallucinations_sankey.png)

Three takeaways map directly onto Q1–Q3:

1. **Feasibility (Q1):** LLMs can often recover injected misconceptions (high recall), consistent with the idea that they can generate plausible belief hypotheses from code.
2. **Safety (Q2):** False positives concentrate on clean programs (86.6% of false positives in the main run), highlighting a trigger-happy tendency that makes instructor-facing use and filtering necessary.
3. **Blind spots (Q3):** The structural vs. semantic gap suggests systematic limits: some misconception families are easier because they leave strong surface traces; others remain hard without deeper semantic or contextual evidence.

## 6. Implications and a Research Agenda

### 6.1 For tool builders

If building instructor-facing belief attribution tools, prioritize:

- Evidence-first displays (code spans and behavior before labels).
- Abstention and ambiguity as normal outputs.
- Cohort-level clustering to reduce verification burden.
- Interfaces that encourage instructors to verify before acting.

### 6.2 For computing education researchers

The community needs shared benchmarks and reporting norms that reflect the risk profile of belief attribution:

- Treat clean-code false positives as a primary outcome.
- Evaluate abstention and calibration.
- Separate “label matching” from “belief alignment.”
- Validate usefulness with instructor studies, not only synthetic probes.

### 6.3 For classroom practice

LLMs can help instructors regain a key part of teaching at scale: noticing patterns in student thinking. Used as hypothesis generators rather than judges, these systems can support faster, more targeted interventions without exposing students to confident misattribution.

## 7. Limitations and Next Steps

This position is grounded by a controlled probe, not validated by classroom outcomes. Key limitations include:

- Synthetic data may not capture the full diversity of novice reasoning and error processes.
- Injected ground truth measures proxy-alignment, not “true belief.”
- Current evaluation does not test whether evidence truly supports the hypothesis.

Next steps include instructor-judgement studies of plausibility/usefulness, evaluation of abstention-first models, and developing methods to score evidence entailment.

## 8. Conclusion

LLMs can support CS1 in more than one way. Alongside student-facing explanations and repairs, there is a distinct and promising direction: instructor-facing belief attribution grounded in notional machines.

Because belief attribution is uncertain and risk-asymmetric, the path forward requires diagnostic humility—evidence, abstention, and specificity-first evaluation. If the community treats LLMs as hypothesis generators for instructors rather than arbiters of student belief, we can gain scale while protecting learners from the costs of confident misattribution.
