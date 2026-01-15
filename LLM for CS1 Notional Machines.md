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

Large language models are increasingly present in CS1 feedback workflows: they explain compiler errors, propose patches, and generate hints at scale. Used well, these systems can reduce friction and expand access to help.

This paper takes a position on a distinct, instructor-centered opportunity that is currently under-developed: using LLMs to help instructors reason about what student code suggests about students’ notional machines—students’ mental models of how programs execute (du Boulay, 1986). Instructors often care less about whether a single submission is correct than about what patterns of errors reveal about how students think, because that is what determines what to teach next.

This is not an argument against student-facing “AI-as-tutor” tools. Rather, it is an argument about what the field should treat as the primary pedagogical object and safety target when LLMs infer beliefs. Bug fixing is about the program. Belief attribution is about the person who wrote it.

### 1.1 Gaps in current approaches

Prior work in computing education has deeply documented misconceptions and execution-model failures (e.g., du Boulay, 1986; Pea, 1986; Qian & Lehman, 2017) and has built tutoring systems that model student knowledge to deliver targeted feedback (e.g., Anderson et al., 1989; Johnson & Soloway, 1985). More recently, LLM-based tools can produce fluent explanations and feedback for novice programs (e.g., Azaiz et al., 2024).

However, in the CS1 LLM era, there is a specific gap: we lack norms and evaluations for when a model should *not* diagnose. Belief attribution is intrinsically uncertain and risk-asymmetric: a false-positive diagnosis (“this student believes X”) can be more harmful than abstaining. Novices often cannot reliably adjudicate between competing explanations, and a confident but wrong story can induce confusion, mistrust, and wasted effort.

### 1.2 Proposal and research questions

We propose treating LLMs as instructor-facing hypothesis generators: tools that surface *plausible* notional-machine hypotheses grounded in code evidence, with uncertainty explicit and abstention normalized.

To make the position concrete, we pose three questions that an instructor-facing belief attribution direction must answer:

1. **Feasibility / capability:** Given a CS1 submission (and task context), can an LLM generate plausible hypotheses about the student’s latent notional machine beyond surface bug descriptions?
2. **Safety / trigger-happiness:** How often does the model over-diagnose—attributing a misconception when the program is clean or the evidence is ambiguous—and what false-positive burden does that impose?
3. **Coverage limits / blind spots:** Which misconception families are reliably diagnosable versus systematically hard, and what does that imply for how instructors should interpret hypotheses?

### 1.3 Contributions and scope

This paper contributes:

- A position: instructor-facing belief attribution is a distinct design goal from bug fixing.
- Design and evaluation requirements for “diagnostic humility” (abstention and specificity-first reporting).
- Empirical grounding using TRACER (Taxonomic Research of Aligned Cognitive Error Recognition), a controlled probe illustrating feasibility, false-positive risk, and structural-vs-semantic blind spots.

Scope: we focus on CS1-style imperative programs written as standalone Java `main` programs, and on instructor-facing use (not grading; not authoritative student-facing diagnosis).

## 2. Related Work

This section briefly situates the paper in four threads: (i) misconceptions and notional machines, (ii) student modeling and intelligent tutoring systems, (iii) automated feedback and error mining at scale, and (iv) LLM-based feedback and reliability.

### 2.1 Notional machines and misconceptions in CS1

Notional machines frame programming difficulties as failures of an execution model that students carry in their heads (du Boulay, 1986). Related work emphasizes that many novice errors are not random slips but systematic conceptual “bugs” that generalize across languages and persist over time (Pea, 1986; Qian & Lehman, 2017).

Computing education research has developed methods to elicit and catalog misconceptions, including interviews and concept inventories (Kaczmarczyk et al., 2010) and tracing/simulation exercises that expose execution-model gaps (Sirkiä & Sorva, 2012). Community efforts have also aggregated misconception inventories to unify terminology and examples (Chiodini et al., 2021).

### 2.2 Student modeling and intelligent tutoring systems

A long line of work models student intent and misconceptions to support tutoring and feedback. PROUST (Johnson & Soloway, 1985) pioneered plan-based program understanding to diagnose where student code deviates from canonical solution plans. Cognitive tutors, such as the LISP Tutor, use explicit models of student knowledge to recognize errors and provide targeted support (Anderson et al., 1989). Early empirical work also challenged “folk wisdom” about novice mistakes and argued for systematic, evidence-based accounts of error patterns (Spohrer & Soloway, 1986).

These systems demonstrate that belief/intent attribution is valuable, but they depend on heavy manual knowledge engineering and are often domain- or task-specific—motivating investigation of whether modern LLMs can generate *hypotheses* at lower authoring cost while preserving safety.

### 2.3 Automated feedback and error mining at scale

Large-scale telemetry and data mining enable measurement of novice error patterns and mismatches between instructor intuition and observed behavior (Brown & Altadmri, 2014). More recent approaches use code representations and clustering to semi-automatically surface candidate misconception families for instructor inspection (Shi et al., 2021). These threads emphasize scaling diagnosis beyond one student at a time, but still require interpretive work to translate patterns into instructional hypotheses.

### 2.4 LLM-based feedback in programming education

Recent work evaluates LLMs’ ability to generate feedback for programming exercises and observes reliability issues such as inconsistent or incorrect advice (Azaiz et al., 2024). The broader education community also highlights opportunities and risks of LLMs, including over-trust in fluent outputs and the need for careful pedagogical integration (Kasneci et al., 2023).

### 2.5 Reliability and “hallucination” as a design constraint

In natural language generation, hallucination and overconfident errors are well-studied failure modes (Ji et al., 2023). For belief attribution, these failures are not merely inconvenient; they are pedagogically harmful because they can mislabel students’ thinking. This motivates explicit “diagnostic humility”: abstention, calibrated uncertainty, and workflows that keep hypotheses instructor-facing rather than student-authoritative.

## 3. Notional Machines and Belief Attribution as an Inverse Problem

### 3.1 Terms: notional machine, mental model, misconception

We use:

- **Notional machine** to mean the simplified explanatory machine a course implicitly teaches (du Boulay, 1986).
- **Mental model** to mean a student’s internal approximation of that machine.
- **Misconception** to mean a systematic divergence: a coherent but incorrect rule the student may be using.

A single incorrect submission can arise from many latent causes: a misconception, a partial understanding, or a one-off slip. Therefore, inferring a student’s notional machine from code is an inverse problem with a multi-modal posterior.

### 3.2 Structural vs. semantic misconceptions (definition)

We define a misconception as:

- **Structural** if the evidence for it is primarily *surface-observable in the artifact* (e.g., a missing assignment of a return value, an out-of-bounds index expression, or a visibly wrong operator). Structural misconceptions tend to have stable, local signatures in code.
- **Semantic** if correct diagnosis requires inferring the student’s *intended meaning* or expected execution behavior beyond surface form (e.g., assumptions about evaluation order, reactive recomputation, or indentation-as-block structure). Semantic misconceptions are less directly observable and therefore more uncertainty-prone.

This is a binary operationalization for reporting and evaluation, not a claim that misconception observability is inherently binary.

### 3.3 Worked example (TRACER NM_STATE_01: “Spreadsheet View / Early Calculation”)

The following TRACER submission exemplifies a semantic misconception: the student computes a formula early, apparently expecting the variable to later “update” when inputs are read.

```java
// authentic_seeded/a1/Miller_Bryan_335743/Q2.java
// NM_STATE_01: Spreadsheet View (Early Calculation)

double y=0;
double n=0;
double z=0;
double c=y/n*z;
System.out.print("Enter the driving distance in miles: ");
y=x.nextDouble();
System.out.print("Enter miles per gallon: ");
n=x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
z=x.nextDouble();
System.out.println("The cost of driving is $"+c);
```

A plausible hypothesis is that the student is treating `c` as a “live formula” (spreadsheet mental model) rather than a one-time assignment. Importantly, the artifact alone cannot rule out alternatives (e.g., copying a template or a simple ordering slip). That ambiguity motivates a hypothesis set (not a single asserted belief) and instructor verification.

## 4. Instructor-Facing LLMs as Hypothesis Generators

### 4.1 What the system should do

An instructor-facing belief attribution system should:

- Generate a small set of plausible misconception hypotheses (not a single definitive label).
- Ground each hypothesis in concrete evidence (code spans and behavior when available).
- Express uncertainty and support abstention when evidence is ambiguous.
- Aggregate across submissions to surface cohort-level patterns rather than making isolated claims about individuals.

The unit of value is not “the model fixed the code,” but “the model helped the instructor decide where to look next.”

### 4.2 Evaluation requirements (designing for diagnostic humility)

Belief attribution cannot be evaluated only with accuracy/F1. Evaluation should report:

- **False positives on clean programs** (behaviorally correct submissions), because over-diagnosis is pedagogically costly.
- **Specificity** (true negative rate), because “not diagnosing” is often the safe choice.
- **Blind spots by misconception family**, because some misconceptions are less observable.

Abstention should be treated as a first-class behavior: systems should be rewarded for declining to diagnose under weak evidence.

### 4.3 What we are (and are not) claiming

We are not claiming:

- That an LLM can recover a student’s “true beliefs.”
- That belief hypotheses should be delivered to students as authoritative diagnosis.
- That belief hypotheses should be used for grading.

We are claiming:

- Hypothesis generation about notional machines can be useful to instructors when it is evidence-linked, uncertainty-aware, and verified by humans.
- Evaluation must explicitly reward humility (specificity and abstention), not just coverage.

## 5. Empirical Grounding: TRACER

TRACER (Taxonomic Research of Aligned Cognitive Error Recognition) is used here as supporting evidence for the position, not as a claim of classroom-ready student modeling.

### 5.1 What TRACER is (and what it is not)

TRACER is a controlled probe designed to test whether LLM outputs about “student thinking” can align with operationalized ground truth misconceptions.

- Dataset: 1,200 synthetic CS1 Java submissions from 300 synthetic “students” across three assignments.
- Each student produces 4 submissions; the intended design is 3 behaviorally correct + 1 misconception-seeded.
- In the current dataset manifests, 275/1200 files are seeded and 925/1200 are clean (seeding sometimes falls back to clean when constraints cannot be satisfied).

Generation uses a 4×3 persona matrix (coding style × cognitive profile) to reduce near-duplicate canonical forms and create surface-level variation.

TRACER validates behavioral correctness using black-box I/O testing on compiled Java programs (compile with `javac`, execute `main` on fixed stdin test cases, check stdout for expected patterns).

### 5.2 Matching and what is scored

TRACER evaluates whether an LLM can generate a belief hypothesis aligned with injected ground truth. In the main condition, semantic matching compares hypothesized “student thinking” to injected “student thinking” while excluding label text. An ablation includes label text to illustrate how evaluation shortcuts can inflate apparent capability.

Importantly, current matching does not verify that the model’s cited evidence spans *entail* its hypothesis; it measures belief-text alignment rather than evidence quality.

### 5.3 Results: feasibility, safety, and blind spots

**Table 1: TRACER summary metrics (5-fold CV)**

| Condition | Precision | Recall | Specificity |
|---|---:|---:|---:|
| Standard matching (main run) | 0.577 | 0.872 | 0.848 |
| Label-inclusive matching (ablation) | 0.511 | 0.982 | 0.774 |

**Feasibility:** Recall is high in both settings, suggesting models can often produce belief hypotheses aligned with injected misconceptions.

**Safety:** False positives concentrate on clean programs. In the main run, 3,364 of 3,884 false positives (86.6%) occur on behaviorally correct submissions, indicating a trigger-happy tendency that is harmful if surfaced directly to students.

**Blind spots:** Figure 1 summarizes a structural vs. semantic gap, consistent with the idea that some misconception families are less observable from code alone.

**Figure 1: Structural vs. semantic misconception gap (TRACER main run).**

![TRACER structural vs semantic gap](runs/run_final_main/assets/category_structural_vs_semantic.png)

**Figure 2: False positives are dominated by clean-code over-diagnosis (TRACER main run).**

![TRACER false-positive flow](runs/run_final_main/assets/hallucinations_sankey.png)

### 5.4 Worked example (TRACER NM_LOGIC_02: “Dangling Else / Indentation Trap”)

The following seeded submission illustrates a semantic misconception where indentation is treated as defining block structure.

```java
// authentic_seeded/a2/Wagner_Sarah_543972/Q3.java
// NM_LOGIC_02: Dangling Else (Indentation Trap)

else if (numeric_grade >= 60)
  if (numeric_grade >= 65)
      System.out.println("Letter grade: D");
  else
      System.out.println("Letter grade: F");
```

In one detection, the model hypothesizes an “indentation-as-blocks / dangling-else confusion,” i.e., the student believes the `else` belongs to the visually-indented outer branch rather than binding to the nearest unmatched `if`.

### 5.5 Abstention in practice (brief)

One practical implication of TRACER’s clean-code false positives is that instructor-facing systems should implement abstention mechanisms. Even lightweight mechanisms—such as consensus across multiple strategies/models (ensemble voting) or calibrated thresholds for accepting a diagnosis—can serve as operational “abstention-by-default” filters.

## 6. Discussion & Implications

### 6.1 Implications for tool builders

- Prefer evidence-first interfaces: show behavior and code spans before any misconception label.
- Treat “no diagnosis” and “multiple plausible hypotheses” as normal outputs.
- Provide cohort-level clustering so instructors verify patterns, not individual students.
- Make uncertainty legible: avoid confident narrative explanations when evidence is weak.

### 6.2 Implications for computing education research

- Benchmarks should explicitly include clean submissions to measure over-diagnosis.
- Reporting should include specificity/false positives, not only recall/accuracy.
- Structural vs. semantic performance should be reported separately because observability differs.
- “Evidence-grounded” claims require additional evaluation beyond label alignment.

### 6.3 Implications for classroom practice

Instructor-facing hypothesis generators can help instructors regain a key part of teaching at scale: noticing patterns in student thinking. Used as hypotheses (not judgments), these systems can support faster, more targeted interventions without exposing students to confident misattribution.

## 7. Limitations

- Synthetic data may not capture the full diversity of novice reasoning and error processes (du Boulay, 1986; Qian & Lehman, 2017).
- Injected ground truth measures proxy-alignment to operationalized misconceptions, not students’ “true beliefs.”
- Current evaluation does not test whether cited evidence truly supports (entails) the hypothesis.

## 8. Conclusion

Alongside student-facing explanations and repairs, there is a distinct and promising direction for LLMs in CS1: instructor-facing belief attribution grounded in notional machines.

Because belief attribution is uncertain and risk-asymmetric, the path forward requires diagnostic humility—evidence, abstention, and specificity-first evaluation. If the community treats LLMs as hypothesis generators for instructors rather than arbiters of student belief, we can gain scale while protecting learners from the costs of confident misattribution.

## References (working list)

- Anderson, J. R., Conrad, F. G., & Corbett, A. T. (1989). Skill Acquisition and the LISP Tutor. Cognitive Science.
- Azaiz, I., Kiesler, N., & Strickroth, S. (2024). Feedback-Generation for Programming Exercises With GPT-4. ITiCSE.
- Bender, E. M., Gebru, T., McMillan-Major, A., & Shmitchell, S. (2021). On the Dangers of Stochastic Parrots. FAccT.
- Brown, N. C. C., & Altadmri, A. (2014). Investigating novice programming mistakes. ICER.
- Chiodini, L., et al. (2021). A Curated Inventory of Programming Language Misconceptions. ITiCSE.
- du Boulay, B. (1986). Some Difficulties of Learning to Program. Journal of Educational Computing Research.
- Ji, Z., et al. (2023). Survey of Hallucination in Natural Language Generation. ACM Computing Surveys.
- Johnson, W. L., & Soloway, E. (1985). PROUST: Knowledge-Based Program Understanding. IEEE Transactions on Software Engineering.
- Kaczmarczyk, L. C., Petrick, E. R., East, J. P., & Herman, G. L. (2010). Identifying student misconceptions of programming. SIGCSE.
- Kasneci, E., et al. (2023). ChatGPT for good? On opportunities and challenges of large language models for education. Learning and Individual Differences.
- Pea, R. D. (1986). Language-Independent Conceptual “Bugs” in Novice Programming. Journal of Educational Computing Research.
- Qian, Y., & Lehman, J. (2017). Students’ Misconceptions and Other Difficulties in Introductory Programming: A Literature Review. ACM TOCE.
- Shi, Y., et al. (2021). Toward Semi-Automatic Misconception Discovery Using Code Embeddings. LAK.
- Sirkiä, T., & Sorva, J. (2012). Exploring programming misconceptions: an analysis of student mistakes in visual program simulation exercises. Koli Calling.
- Spohrer, J. C., & Soloway, E. (1986). Novice Mistakes: Are the Folk Wisdoms Correct? Communications of the ACM.
