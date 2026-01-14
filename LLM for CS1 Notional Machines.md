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

## 1. Introduction

LLMs are rapidly becoming part of CS1 feedback workflows, both in research and practice [7]. They can explain compiler errors, generate hints, and propose code edits at tutor-like scale. This has led to a common framing of “AI-as-tutor” focused on **correction**: identify what is wrong in the code and prescribe a fix.

Our position is that this framing targets the wrong pedagogical object. In CS1, the most instructionally valuable signal is often not correctness, but what a submission suggests about the student’s *notional machine* (NM): their internal model of how programs execute [1, 2]. When an LLM fixes code, it may remove friction without repairing the misconception that produced it.

This shift changes the diagnostic task from **error detection** (properties of the program text) to **belief attribution** (hypotheses about the agent who wrote the program). Belief attribution is intrinsically uncertain and **risk-asymmetric**: a false-positive diagnosis (attributing a misconception the student does not hold) can be more harmful than abstaining.

We therefore advocate **instructor-facing LLMs**: systems that propose plausible misconception hypotheses, grounded in specific code evidence, for human verification. This is not a claim that LLMs can recover “true beliefs.” Rather, the claim is that LLMs can generate useful, inspectable hypotheses and clusters at scale—while interfaces and evaluation must explicitly reward restraint.

## 2. Notional Machines and Misconceptions

A notional machine is the simplified explanatory “machine” implied by a language and used for teaching. It is a pedagogical device that makes execution intelligible by hiding irrelevant physical detail and emphasizing relevant state changes (variables, control flow, references, etc.) [1, 2].

Students build *mental models* that approximate this notional machine. Misconceptions in CS1 are often structured divergences in these mental models: e.g., treating variables as reactive spreadsheet cells, expecting prompt text to control input binding, or assuming mathematical notation transfers directly to programming syntax.

Notional machines matter for practice because they are actionable at multiple levels:

- **Individual support:** instructors can correct a specific misconception with targeted explanations and tracing.
- **Cohort intervention:** misconceptions cluster; instructors can adjust lectures, examples, and activities when a pattern emerges.
- **Assessment:** students can write correct code by chance or by copying patterns; a stable notional machine is a stronger indicator of understanding than a single correct output.

## 3. Belief Attribution as an Inverse Problem

Belief attribution from code is an inverse problem: a latent belief state produces an observable artifact, and we infer backwards.

Let:

- `b` be a latent belief state (a student’s notional-machine variant)
- `t` be the task and context (problem statement, constraints)
- `s` be surface style (naming/formatting and other presentation factors)
- `c` be the observed code

The forward process is `p(c | b, t, s)`. Diagnosis is the posterior `p(b | c, t, s)`. This posterior is generally **multi-modal**: multiple beliefs can plausibly explain the same code.

Historically, intelligent tutoring systems approached this inverse problem using plan recognition and buggy-rule libraries, achieving high precision within narrow domains but struggling with diversity in novice code [5]. LLMs are less brittle, but their flexibility increases the temptation to over-interpret.

### 3.1 Theory of Mind as a tutoring analogy

Human tutors perform Theory-of-Mind-like inference: they interpret student artifacts as evidence of a student’s internal model. We use ToM as an analogy for the *type of inference* required (reasoning from behavior to mental state), not as a claim that LLMs literally possess human mental-state understanding [3, 4].

LLMs can produce coherent belief narratives conditioned on code and context, which makes them plausible hypothesis generators. However, coherence is not correctness: generating a plausible story is exactly what can lead to over-interpretation.

### 3.2 One bug, many plausible beliefs (Java example)

Consider a student trying to sum an array:

```java
int sum = 0;
for (int i = 0; i < nums.length; i++) {
    sum += i; // uses index, not value
}
```

The behavior is wrong, but the cause is ambiguous:

- **Hypothesis A (index–value confusion):** the student believes `i` *is* the array element.
- **Hypothesis B (attention slip):** the student intended `nums[i]` but omitted it.
- **Hypothesis C (template transfer):** the student copied a loop skeleton without integrating the meaning of `i`.

A system asked to “diagnose the misconception” is incentivized to commit to the most coherent hypothesis. If delivered directly to students, this commitment is the primary safety risk.

## 4. Evidence from TRACER

To support the position with empirical evidence, we summarize results from TRACER(Taxonomic Research of Aligned Cognitive Error Recognition): a controlled benchmark for notional-machine misconception detection in CS1 Java.

### 4.1 Benchmark construction (synthetic, labeled, behavior-validated)

TRACER uses **synthetic** student submissions to obtain known misconception labels (a common strategy when ground-truth beliefs are otherwise unobservable at scale) [9]. Critically, this provides what classroom data cannot: observable ground truth. In real classrooms, a student's belief state is latent and unknowable; human annotators can only infer it. By determining the belief *ex ante* via generation, TRACER allows us to measure "belief recovery" with an internal validity impossible in the wild. Submissions are generated and validated as compiled Java programs:

- **Clean programs**: compile and pass TRACER’s black-box I/O tests (compile with `javac`, execute `main` on multiple stdin cases, and check stdout).
- **Seeded programs**: compile, differ from the clean solution, and fail at least one TRACER black-box test.

The benchmark contains 1,200 submissions: 100 synthetic students per assignment (A1–A3), with four files each (one seeded, three clean).

To introduce realistic surface variability, TRACER uses a persona matrix during generation (coding style × cognitive profile). The personas are designed to vary presentation (naming, indentation, verbosity, cautious checks) rather than the target misconception.

### 4.2 Misconception labels

Across assignments A1–A3, TRACER includes **18 unique misconception IDs** grounded in notional-machine theory. These are organized into notional-machine categories (e.g., reactive state, anthropomorphic I/O, fluid types, algebraic syntax, “void” assumptions, control-flow misconceptions, array/reference misconceptions). The set is illustrative rather than exhaustive.

### 4.3 Detection and matching

TRACER evaluates multiple models and prompting strategies. Models output structured JSON describing detected misconceptions and an evidence trace (line numbers/snippets). Free-form model outputs are mapped to ground truth via semantic similarity matching, with thresholds calibrated by 5-fold stratified cross-validation.

A key evaluation choice is whether to allow label leakage (including misconception names/categories in the embedding match). TRACER’s default matching excludes label text and relies primarily on the “student thinking” narrative to better reflect belief alignment rather than name matching. The label-inclusive variant includes label text in the match and serves as an ablation of this design choice.

### 4.4 Results and what they imply

**Table 1: TRACER summary metrics (5-fold CV)**

| Condition | Precision | Recall | Specificity |
|---|---:|---:|---:|
| Standard matching (main run) | 0.577 | 0.872 | 0.848 |
| Label-inclusive matching (ablation) | 0.511 | 0.982 | 0.774 |

The key trade-off is visible in the main-vs-ablation contrast: allowing label-inclusive matching increases recall, but it worsens specificity substantially—exactly the failure mode that most directly creates false accusations in clean code.

**Figure 1: Structural vs. semantic misconception gap (TRACER main run).**

![TRACER structural vs semantic gap](runs/run_final_main/assets/category_structural_vs_semantic.png)

**Figure 2: False positives are dominated by clean-code over-diagnosis.**

![TRACER false-positive flow](runs/run_final_main/assets/hallucinations_sankey.png)

Two findings matter for the position paper:

1. **LLMs can recover many injected misconceptions** (high recall), supporting their use as instructor-facing hypothesis generators.
2. **False positives concentrate on clean programs**, the failure mode with the highest pedagogical risk: 86.6% of false positives in the main run are diagnoses made on programs that are clean under TRACER’s behavioral tests. While the benchmark included diverse personas (e.g., messy, verbose) to test robustness, this high false positive rate suggests that over-diagnosis is a fundamental property of current instruction-tuned models, rather than an artifact of specific coding styles.

TRACER also indicates a **structural vs. semantic gap**: misconceptions with strong surface signatures are easier to detect than those requiring deeper semantic inference. This gap motivates an instructor-in-the-loop design even when aggregate metrics look strong.

## 5. Position: Instructor-Facing LLMs for Belief Attribution

We propose reframing CS1 LLM support as instructor-facing belief attribution, with interfaces that treat outputs as hypotheses rather than diagnoses.

### 5.1 A high-specificity workflow

A practical workflow is:

1. **Ingest** submissions for an activity or assignment.
2. **Generate hypotheses** (misconception candidates) with evidence traces.
3. **Filter for safety** (prioritize specificity; suppress low-evidence or historically high-FP patterns).
4. **Cluster by belief** (aggregate likely misconceptions across students). Given the high false positive rate, this step is crucial for instructor sanity: systems should flag *patterns* (e.g., "15 students show signs of the Fluid Type Machine"), not just individual students.
5. **Verify and act** (instructors inspect representative examples from a cluster, then intervene via lecture, targeted messaging, or tracing exercises). This cohort-level verification allows instructors to dismiss a hallucinated cluster with a single check, mitigating the cognitive load of the model's low specificity.

This shifts the LLM from an oracle that tells students what they believe to a triage assistant that helps instructors decide where to look.

### 5.2 Design requirements for diagnostic humility

Instructor-facing systems should:

- Use probabilistic language (“suggests”, “consistent with”) rather than definitive labels.
- Always show **evidence** (code spans) alongside hypotheses.
- Support **abstention** as a first-class output (e.g., “bug observed, belief ambiguous”).
- Prefer precision-oriented aggregation (e.g., ensemble agreement across prompts/models) over single-shot diagnosis.

## 6. Implications for ITiCSE 2026 and Beyond

The position articulated here has broader implications for the computing education research (CER) community and for how we should evaluate and deploy LLM-supported feedback.

### 6.1 Reframing evaluation standards

CER should move beyond single headline metrics (e.g., accuracy or F1) when evaluating belief-attribution tools. For diagnosis, **specificity** and **false positive rate** must be treated as primary safety metrics, alongside recall.

A paper claiming “high accuracy” without reporting clean-code false positives can hide the central harm mechanism: over-diagnosis that produces plausible-sounding but incorrect attributions. The label-inclusive matching ablation in TRACER is a concrete example: it inflates recall while degrading specificity.

We recommend the following minimum reporting standards:

- Report **specificity** and **false positives on clean programs** explicitly.
- Avoid **label-inclusive matching/scoring** unless additional labels are explicitly treated as errors.
- Separate surface-style variation from misconception content (e.g., personas) and report sensitivity to presentation.
- Benchmark with **abstention**: evaluate models that can say “uncertain” rather than forcing a diagnosis.

### 6.2 A renaissance of notional machines

The rise of LLM-mediated programming support necessitates a renewed focus on notional machines as an explicit instructional target [1, 2]. Beyond refining existing taxonomies, we need new models that account for the “AI-augmented learner.”

What is the notional machine of a student who writes code by prompting a chatbot? Their mental model may not primarily be of the programming language semantics, but of the *LLM* (e.g., “if I ask it the right way, it will fix the bug”). Diagnosing these meta-cognitive misconceptions is a likely next frontier.

### 6.3 Epistemic rights of learners

Finally, belief attribution raises a normative question: what do students have a right to expect from automated feedback?

We argue students have an epistemic right not to be casually misdiagnosed, labeled, or confused by systems that speak with unwarranted authority. Restricting belief attribution to the instructor-facing layer provides a practical safeguard: the heavy weight of “diagnosing the mind” remains a human responsibility, supported—but not usurped—by the machine.

## 7. Limitations and future work

TRACER is a controlled probe, not a deployment-ready student model.

- **Synthetic data**: injected misconceptions are known by construction, but synthetic programs may not fully match real novice error processes.
- **Partial coverage**: 18 misconception labels cover a slice of CS1; they are not exhaustive.
- **Belief uncertainty**: even in real classrooms, beliefs are not directly observable; instructor verification remains essential.

A key future direction is modeling the “AI-augmented learner,” including notional machines about the LLM itself (e.g., beliefs about what kinds of prompts produce correct fixes), and then validating whether instructor-facing belief attribution transfers from controlled benchmarks to real classrooms.

## 8. Conclusion

The integration of LLMs into CS1 is fast becoming commonplace, but its form is not predetermined. We stand at a crossroads between an **auto-fixer** that optimizes for surface correctness and an **AI-augmented instructor** that optimizes for insight and learning.

The evidence from TRACER makes the trade-off concrete. Belief attribution is risk-asymmetric: false positives carry disproportionate harm, and TRACER shows they frequently arise from over-diagnosis on programs that are clean under behavioral tests. This demands **epistemic restraint** and evaluation standards that reward specificity rather than coverage.

LLMs are powerful engines of hypothesis generation: they can scan large volumes of code and surface plausible misconception candidates with supporting evidence traces. But they should not be treated as final judges of student belief. By keeping belief attribution instructor-facing, we unlock the benefits of scale while protecting learners from the costs of confident misattribution.

In short: LLMs can help instructors recover the “true labor” of tutoring—understanding *why*—while restoring the notional machine to the center of CS1 pedagogy.

## References

[1] Notional Machines in Computing Education. DSpace (PDF). https://dspace.library.uu.nl/bitstream/handle/1874/414811/3437800.3439202.pdf?sequence=1

[2] Notional Machines and Programming Language Semantics in Education. Schloss Dagstuhl (Dagstuhl Reports). https://drops.dagstuhl.de/storage/04dagstuhl-reports/volume09/issue07/19281/DagRep.9.7.1/DagRep.9.7.1.pdf

[3] Theory of Mind and Preference Learning at the Interface of Cognitive Science, Neuroscience, and AI: A Review. *Frontiers in Artificial Intelligence* (2022). https://www.frontiersin.org/journals/artificial-intelligence/articles/10.3389/frai.2022.778852/full

[4] Re-evaluating Theory of Mind evaluation in large language models. *Philosophical Transactions of the Royal Society B*. https://royalsocietypublishing.org/rstb/article/380/1932/20230499/235070/Re-evaluating-Theory-of-Mind-evaluation-in-large

[5] Plan Recognition Strategies in Student Modeling: Prediction and Description. AAAI (1982). https://cdn.aaai.org/AAAI/1982/AAAI82-079.pdf

[6] McMining: Automated Discovery of Misconceptions in Student Code. arXiv (2025). https://arxiv.org/pdf/2510.08827

[7] From Pilots to Practices: A Scoping Review of GenAI-Enabled Personalization in Computer Science Education. arXiv (2025). https://arxiv.org/pdf/2512.20714

[8] Large Language Models for In-Context Student Modeling: Synthesizing Student's Behavior in Visual Programming. *Educational Data Mining* (EDM 2024 short paper). https://educationaldatamining.org/edm2024/proceedings/2024.EDM-short-papers.31/index.html

[9] Machine Learning for Synthetic Data Generation: A Review. arXiv (2023). https://arxiv.org/html/2302.04062v9
