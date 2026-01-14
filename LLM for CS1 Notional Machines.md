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

TRACER uses **synthetic** student submissions to obtain known misconception labels (a common strategy when ground-truth beliefs are otherwise unobservable at scale) [9]. Submissions are generated and validated as compiled Java programs:

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
2. **False positives concentrate on clean programs**, the failure mode with the highest pedagogical risk: 86.6% of false positives in the main run are diagnoses made on programs that are clean under TRACER’s behavioral tests.

TRACER also indicates a **structural vs. semantic gap**: misconceptions with strong surface signatures are easier to detect than those requiring deeper semantic inference. This gap motivates an instructor-in-the-loop design even when aggregate metrics look strong.

## 5. Position: Instructor-Facing LLMs for Belief Attribution

We propose reframing CS1 LLM support as instructor-facing belief attribution, with interfaces that treat outputs as hypotheses rather than diagnoses.

### 5.1 A high-specificity workflow

A practical workflow is:

1. **Ingest** submissions for an activity or assignment.
2. **Generate hypotheses** (misconception candidates) with evidence traces.
3. **Filter for safety** (prioritize specificity; suppress low-evidence or historically high-FP patterns).
4. **Cluster by belief** (aggregate likely misconceptions across students).
5. **Verify and act** (instructors inspect representative examples, then intervene via lecture, targeted messaging, or tracing exercises).

This shifts the LLM from an oracle that tells students what they believe to a triage assistant that helps instructors decide where to look.

### 5.2 Design requirements for diagnostic humility

Instructor-facing systems should:

- Use probabilistic language (“suggests”, “consistent with”) rather than definitive labels.
- Always show **evidence** (code spans) alongside hypotheses.
- Support **abstention** as a first-class output (e.g., “bug observed, belief ambiguous”).
- Prefer precision-oriented aggregation (e.g., ensemble agreement across prompts/models) over single-shot diagnosis.

## 6. Recommendations for ITiCSE and the CER community

We recommend that research and tools in this space adopt the following standards:

- **Report specificity and clean-code false positives** as primary safety metrics, not only recall/F1.
- **Avoid label-inclusive matching/scoring** for belief attribution unless additional labels are explicitly treated as errors.
- **Separate surface-style variation from misconception content** (e.g., personas) and report sensitivity to presentation.
- **Benchmark with abstention**: evaluate models that can say “uncertain” rather than forcing a diagnosis.

## 7. Limitations and future work

TRACER is a controlled probe, not a deployment-ready student model.

- **Synthetic data**: injected misconceptions are known by construction, but synthetic programs may not fully match real novice error processes.
- **Partial coverage**: 18 misconception labels cover a slice of CS1; they are not exhaustive.
- **Belief uncertainty**: even in real classrooms, beliefs are not directly observable; instructor verification remains essential.

A key future direction is modeling the “AI-augmented learner”: students may develop notional machines not only about the programming language, but about the LLM itself (e.g., beliefs about what kinds of prompts produce correct fixes).

## 8. Conclusion

Belief attribution is central to CS1 instruction, but it is fundamentally uncertain and risk-asymmetric. TRACER provides early evidence that LLMs can detect many misconception patterns, but it also highlights a safety-critical failure mode: over-diagnosis on clean programs, especially under evaluation shortcuts that reward shotgun labeling.

We therefore argue for instructor-facing LLMs designed for diagnostic humility: systems that generate inspectable hypotheses, prioritize specificity, support abstention, and keep the final attribution decision with the instructor.

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
