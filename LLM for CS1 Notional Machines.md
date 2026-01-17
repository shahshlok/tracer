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

Large language models (LLMs) have become central to CS1 feedback workflows as tools for student-centered learning: they serve as on-demand tutors that help students learn from their mistakes by interpreting error messages, providing pedagogical guidance, and offering just-in-time assistance at scale. By helping students understand and resolve their mistakes in real-time, these systems reduce friction and expand access to personalized support.

This paper takes a position on a distinct, instructor-centered opportunity that is currently under-developed: using LLMs to help instructors reason about what student code suggests about students’ mental models of the notional machine (du Boulay et al., 1981). Instructors often care less about whether a single submission is correct than about what patterns of errors reveal about how students think, because that is what determines what to teach next.

This is not an argument against student-facing “AI-as-tutor” tools—those systems offer genuine value for practice and accessibility. Rather, it is an argument for broadening the scope of LLMs in CS education to include a complementary direction: instructor-facing belief attribution. Bug fixing is about the program. Belief attribution is about the person who wrote it.

### 1.1 Gaps in current approaches

Prior work in computing education has deeply documented misconceptions and execution-model failures (e.g., du Boulay, 1986; Pea, 1986) and has built tutoring systems that model student knowledge to deliver targeted feedback (e.g., Johnson & Soloway, 1985). More recently, LLM-based tools can produce fluent explanations and feedback for novice programs (e.g., Azaiz et al., 2024).

However, in the CS1 LLM era, there is a specific gap: we lack norms and evaluations for when a model should *not* diagnose. Belief attribution is intrinsically uncertain and risk-asymmetric: a false-positive diagnosis (“this student believes X”) can be more harmful than abstaining. Novices often cannot reliably adjudicate between competing explanations, and a confident but wrong story can induce confusion, mistrust, and wasted effort.

### 1.2 Proposal and research questions

We propose treating LLMs as instructor-facing hypothesis generators: tools that surface *plausible* notional-machine hypotheses grounded in code evidence, with uncertainty explicit and abstention normalized.

To make the position concrete, we pose three questions that an instructor-facing belief attribution direction must answer:

1. **Feasibility / capability:** Given a CS1 submission (and task context), can an LLM generate plausible hypotheses about the student’s latent mental model beyond surface bug descriptions?
2. **Safety / trigger-happiness:** How often does the model over-diagnose—attributing a misconception when the program is clean or the evidence is ambiguous—and what false-positive burden does that impose?
3. **Coverage limits / blind spots:** Which misconception families are reliably diagnosable versus systematically hard, and what does that imply for how instructors should interpret hypotheses?

### 1.3 Contributions and scope

This paper contributes:

- A position: instructor-facing belief attribution is a distinct design goal from bug fixing.
- Design and evaluation requirements for “diagnostic humility” (abstention and specificity-first reporting).
- Empirical grounding using TRACER (Taxonomic Research of Aligned Cognitive Error Recognition), a controlled probe illustrating feasibility, false-positive risk, and structural-vs-semantic blind spots.

Scope: we focus on CS1-style imperative programs written as standalone Java `main` programs, and on instructor-facing use (not grading; not authoritative student-facing diagnosis).

## 2. Related Work

This section traces the evolution of belief attribution in CS1 through three historical phases: the theoretical establishment of the notional machine, the scalability bottleneck of early automated diagnosis, and the emerging capabilities, and ethical risks, of LLM-based student modeling.

### 2.1 The Theoretical Basis: Hidden Minds and Superbugs

The concept of the **notional machine** (du Boulay et al., 1981) provides the theoretical bedrock for this work. They argued that the computer acts as a "black box," and learning to program requires the student to construct a mental model of its execution rules. Difficulties arise when students fill the gaps in their understanding with **misconceptions**—systematic, incorrect beliefs. Pea (1986) famously characterized these as "superbugs," such as the belief that a "hidden mind" inside the computer interprets the student's intent rather than their literal code.

Crucially, these are not random errors. Sirkiä and Sorva (2012) showed that novice programmers’ mistakes in visual simulation exercises aren’t just random slips. Many reflect systematic, internally consistent misunderstandings of core programming concepts (e.g., the semantics of assignment). These patterns support the view that learners form robust but flawed mental models that can be diagnosed with tasks that go beyond surface-level code inspection. This confirms that "buggy" mental models are distinct cognitive states that can be diagnosed, provided one looks beyond the code's surface.

### 2.2 The Scalability Bottleneck: From PROUST to Big Data

Attributing these beliefs has been a long-standing goal of intelligent tutoring systems. In the 1980s, **PROUST** (Johnson & Soloway, 1985) pioneered intention-based diagnosis by matching student code against a library of "buggy plans." However, PROUST was brittle: it relied on hand-crafted rules that could not scale to the diversity of student approaches.

In the absence of scalable tools, the field relied on instructor intuition or aggregate error statistics. Brown and Altadmri (2014) show that even expert judgment about *which mistakes are most frequent* can be internally unstable: when multiple educators independently rank common novice errors, their *between-educator* agreement is weak (reported via a conversion to Spearman’s ρ for ranked data, ρ = 0.400). As they put it, “informally, this means that the educators are closer to chance agreement than they are to complete agreement,” and that the surveyed educators “form a very weak consensus about which errors are most frequently made by students.” This matters for instructor-facing analytics: if instructors do not reliably converge on what is common from experience alone, tooling that surfaces cohort-level patterns from data (and does so transparently and conservatively) can help close the gap between intuition and evidence.

### 2.3 The LLM Opportunity and the Safety Gap

Recent advances offer a solution to this scalability challenge. Emerging work, such as **McMining** (Al-Hossami & Bunescu, 2025) and cognitive simulation models (Sonkar et al., 2024), suggests that LLMs can automate the discovery of misconceptions without the brittle libraries of PROUST.

However, this capability introduces a new risk: **reliability**. Current evaluations (Azaiz et al., 2024; Koutcheme et al., 2024) focus on feedback helpfulness, often neglecting the specific harms of misdiagnosis. This oversight is dangerous because LLMs are prone to **hallucination** (Ji et al., 2023).

In an instructor-facing context, this risk is amplified by **automation bias** (Parasuraman & Riley, 1997)—the tendency for humans to substitute their own vigilance with the machine's output (the "heuristic of least effort"). If an instructor blindly accepts a false diagnosis (a "commission error"), they risk intervening on a problem that doesn't exist. Bandura (1997) warns that such mislabeling can lead to **"self-debilitating attribution,"** where students internalize the false error as a lack of ability, damaging their self-efficacy. Thus, the transition to AI-driven belief attribution requires a shift from maximizing *coverage* to maximizing *diagnostic humility*.

## 3. Notional Machines and Belief Attribution: An Inverse Problem

### 3.1 Terms: notional machine, mental model, misconception

We use:

- **Notional machine** to mean the simplified explanatory machine a course implicitly teaches (du Boulay et al., 1981).
- **Mental model** to mean a student’s internal approximation of that machine.
- **Misconception** to mean a systematic divergence: a coherent but incorrect rule the student may be using.

A single incorrect submission can arise from many latent causes: a misconception, a partial understanding, or a one-off slip. Therefore, inferring a student's mental model from code is an inverse problem with a multi-modal posterior.

This diagnostic task is a form of Theory of Mind (ToM): attributing mental states—beliefs, intents, knowledge—to another agent based on observable behavior (Premack & Woodruff, 1978). When an instructor looks at buggy code and infers "this student believes assignment works like algebraic equality," they are simulating the student's reasoning process. For an LLM to perform this attribution, it must go beyond pattern-matching surface errors to generating hypotheses about the cognitive state that produced them. Unlike syntax checking, which identifies properties of the text, belief attribution infers properties of the agent who wrote it—a fundamentally more uncertain task.

### 3.2 Structural vs. semantic misconceptions (definition)

We define a misconception as:

- **Structural** if it manifests as a distinct surface signature where the student's code violates the operational rules of language constructs. These errors often leave a unique fingerprint detectable via static analysis. Examples in our taxonomy include the *Void Machine*, where value-returning methods are treated as void commands (e.g., calling `Math.sqrt(x)` without assignment), and *Human Indexing*, where students apply 1-based counting to 0-indexed arrays. In these cases, the divergence is visible in the artifact’s structure itself.
- **Semantic** if it involves an invisible mental model of *execution state* that requires inferring intent beyond surface form. These are harder to detect because the code is often syntactically valid but functionally incoherent under the standard notional machine. Examples include the *Reactive State Machine*, characterized by the belief that variables automatically update when dependencies change, and the *Independent Switch*, involving a misunderstanding of the mutual exclusivity of conditional blocks. Diagnosis here requires contrasting the student's implied causal model with the actual program flow.

This is a binary operationalization for reporting and evaluation, not a claim that misconception observability is inherently binary.

### 3.3 Worked example: Spreadsheet View 

The following TRACER submission exemplifies a semantic misconception: the student computes a formula early, apparently expecting the variable to later “update” when inputs are read.

```java
// Spreadsheet View (Early Calculation)

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

Alongside automated repair, a key unit of value in this direction is providing instructors with the pedagogical insight needed to decide where to focus their attention next.

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
- Each student produces 4 submissions; by design, each contributes 1 misconception-seeded submission and 3 behaviorally correct submissions.

Generation uses a 4×3 persona matrix (coding style × cognitive profile) to inject diversity and prevent the generator LLM from producing a single canonical solution. Without personas, LLMs tend to output nearly identical "textbook" code for each problem, which would make misconception detection trivially easy (the detector could simply memorize surface patterns). The four coding styles—*minimal*, *verbose*, *textbook*, and *messy* —vary surface presentation. The three cognitive profiles—*procedural*, *mathematical*, and *cautious*—vary problem decomposition strategy. This yields 12 distinct "student types" per assignment, forcing detection to rely on conceptual signatures rather than stylistic cues.

**Why synthetic data is a strength, not a limitation.** Evaluating belief attribution requires known ground truth: we must know what the student "actually believes" to measure whether the model's hypothesis is correct. Authentic student data cannot provide this—real misconceptions are ambiguous, often multiple, and fundamentally unobservable. By contrast, synthetic data with injected misconceptions gives us controlled inputs, allowing us to focus entirely on the quality of outputs (the model's belief hypotheses).

This methodological choice enables precise measurement of true positives, false positives, and the structural-vs-semantic gap. It also frames TRACER as a *capability probe*: if LLMs can align with injected beliefs in clean, single-misconception code, the direction is worth pursuing; if they fail  here, the approach needs rethinking before deployment. Transfer to authentic classroom data remains important for ecological validity (Section 7), but the synthetic design is what makes the core evaluation possible.

TRACER validates behavioral correctness using black-box I/O testing on compiled Java programs (compile with `javac`, execute `main` on fixed stdin test cases, check stdout for expected patterns).

### 5.2 Matching and what is scored

TRACER evaluates whether an LLM can generate a belief hypothesis aligned with injected ground truth. In the main condition, semantic matching compares LLM hypothesized “student belief" to injected “student thinking” while excluding label text. An ablation includes label text to illustrate how evaluation shortcuts can inflate apparent capability.

Importantly, current matching does not verify that the model’s cited evidence spans *entail* its hypothesis; it measures semantic alignment to the injected "student thinking" rather than evidence quality.

### 5.3 Results: feasibility, safety, and blind spots

**Table 1: Performance trade-offs in belief attribution (5-fold cross-validation).** We compare a label-exclusive approach against a label-inclusive baseline. While including label text saturates recall (0.98), it significantly degrades specificity (0.77), illustrating the risk of optimizing for coverage at the expense of diagnostic humility.

| Condition | Precision | Recall | Specificity |
|---|---:|---:|---:|
| Label-exclusive matching (main) | 0.577 | 0.872 | 0.848 |
| Label-inclusive matching | 0.511 | 0.982 | 0.774 |

Our evaluation identifies an inherent trade-off between the model's ability to recognize misconceptions and its potential for over-diagnosis. **Table 1** summarizes this dynamic across two matching conditions: **label-exclusive matching (main)**, which relies solely on semantic alignment between the hypothesis and the ground truth description, and **label-inclusive matching**, a baseline where the explicit misconception name (e.g., "Void Machine") is exposed during evaluation. While the model demonstrates high feasibility with a recall of 0.87 in the label-exclusive condition, the precision of 0.577 indicates a substantial rate of false-positive diagnoses. Notably, the label-inclusive condition demonstrates that evaluation metrics are susceptible to artificial inflation: providing the model with label names increases recall to near-saturation (0.98) but compromises safety, as evidenced by the drop in specificity to 0.77. This finding underscores that optimizing for coverage metrics in isolation can obscure significant risks in diagnostic reliability.

**The Observability Gap.** Not all beliefs are equally visible in code. **Figure 1** breaks down model performance by misconception category, revealing a distinct "observability gap." Structural misconceptions—those that violate language rules, such as the *Void Machine*—are detected with high reliability because they leave syntactic fingerprints. In contrast, semantic misconceptions like the *Independent Switch*, which exist only in the student's flawed mental model of execution, are far harder to diagnose reliably. This suggests that instructor-facing tools should explicitly differentiate between syntactically evident errors and speculative semantic inferences.

**Figure 1: The observability gap.** Detection rates (Recall) disaggregated by misconception type. Structural misconceptions (blue), which leave distinct syntactic fingerprints like the *Void Machine*, are detected with high reliability. In contrast, semantic misconceptions (orange), which require inferring a mental model of execution state like the *Reactive State Machine*, show significantly higher variance and lower detection rates, highlighting a critical blind spot for current models.

![TRACER structural vs semantic gap](runs/run_final_main/assets/category_structural_vs_semantic.png)

**Characterization of False Positives.** The aggregate metrics in Table 1 do not fully capture the nature of the system's errors. **Figure 2** decomposes these diagnoses using a Sankey diagram to trace the flow from student intent to model output. The data reveal that the predominant proportion of false positives (represented by the red flow) occurs on behaviorally correct programs. We classify these as "Invented Bugs": instances where the model attributes a specific misconception to code that is functionally valid. This indicates a bias toward positive diagnosis, where the system provides a plausible-sounding hypothesis rather than abstaining when the evidence is ambiguous. This behavior reinforces the necessity for the abstention mechanisms discussed in Section 5.5.

**Figure 2: The anatomy of over-diagnosis.** A Sankey diagram tracing the flow from ground truth (left) to model diagnosis (right). The dominant red flow illustrates the system's primary failure mode: "Invented Bugs," where the model hallucinates a specific misconception on a behaviorally correct (clean) submission. This visualizes the risk asymmetry of the task: false positives are not random noise but a systematic bias toward diagnosing problems that do not exist.

![TRACER false-positive flow](runs/run_final_main/assets/hallucinations_sankey.png)

### 5.4 Worked example: Dangling Else / Indentation Trap

The following seeded submission illustrates a semantic misconception where indentation is treated as defining block structure.

```java
// Dangling Else (Indentation Trap)

else if (numeric_grade >= 60)
  if (numeric_grade >= 65)
      System.out.println("Letter grade: D");
  else
      System.out.println("Letter grade: F");
```

In one detection, the model hypothesizes an “indentation-as-blocks / dangling-else confusion,” i.e., the student believes the `else` belongs to the visually-indented outer branch rather than binding to the nearest unmatched `if`.

### 5.5 Abstention in practice

One practical implication of TRACER's clean-code false positives is that instructor-facing systems should implement abstention mechanisms. In our experiments, we operationalize abstention through two complementary filters:

1. **Semantic thresholds.** Hypotheses with low similarity scores (below 0.55) to any known misconception are treated as noise—observations about code style or minor issues rather than conceptual gaps. A secondary "noise floor" (0.60) separates borderline cases from confident detections.

2. **Ensemble agreement.** Requiring consensus across multiple models or prompting strategies (e.g., ≥2 of 4 strategies must flag the same misconception) filters spurious detections. In TRACER, ensemble voting reduces false positives by approximately 50% while preserving 99% of true positives.

These are lightweight, implementable mechanisms that treat "I don't know" as a first-class output. More sophisticated approaches—such as calibrated confidence scores or explicit uncertainty quantification—remain important directions for future work.

## 6. Discussion & Implications

Clean-code false positives are uniquely harmful in CS1 belief attribution: when a program is correct, there may be no misconception to remediate, and a speculative diagnosis can teach a wrong rule. For novice learners, the LLM's fluency can be mistaken for authority, making "helpful" over-diagnosis a direct risk to concept formation.

### 6.1 Why false positives outweigh false negatives

Belief attribution exhibits risk asymmetry: the cost of a false positive (diagnosing a misconception that does not exist) exceeds the cost of a false negative (missing a real misconception). Three mechanisms drive this asymmetry:

1. **Epistemic interference.** When a student with a correct mental model is told they are confused, the feedback can induce doubt in valid understanding. The student may abandon correct reasoning in favor of the system's erroneous suggestion, wasting cognitive effort on "fixing" what was never broken.

2. **Labeling effects.** Novice programmers, who often experience fragile self-efficacy, are vulnerable to deficit labels. Being told by an authoritative-seeming system that their thinking is flawed—especially when it is not—can reinforce imposter syndrome and reduce persistence (Bandura, 1997).

3. **Trust erosion.** Repeated false alarms degrade trust in the system. Once students or instructors learn that the tool "cries wolf," even valid feedback is discounted, effectively destroying the system's utility (Parasuraman & Riley, 1997).

These dynamics motivate a design stance we term *diagnostic humility*: AI systems should prioritize specificity (avoiding false alarms) over recall, and abstention should be treated as a responsible output rather than a failure mode.

### 6.2 Implications for tool builders

- Prefer evidence-first interfaces: show behavior and code spans before any misconception label.
- Treat “no diagnosis” and “multiple plausible hypotheses” as normal outputs.
- Implement abstention explicitly: require minimum evidence strength (e.g., calibrated thresholds or cross-model agreement) before surfacing a hypothesis.
- Separate *structural evidence* from *semantic inference* in the UI so instructors can see what is directly supported vs. conjectured.
- Support instructor verification loops (e.g., inspect a few representative submissions per cluster before acting).
- Avoid student-facing authoritative labels; keep hypotheses instructor-facing by default.

### 6.3 Implications for computing education research

- Benchmarks should explicitly include clean submissions to measure over-diagnosis.
- Reporting should include specificity/false positives, not only recall/accuracy.
- Structural vs. semantic performance should be reported separately because observability differs.
- “Evidence-grounded” claims require evaluation beyond label alignment (e.g., entailment/justification quality audits).
- Synthetic probes should be paired with follow-on studies that check transfer to authentic student work before making classroom-impact claims.

### 6.4 Implications for classroom practice

- Use hypothesis clusters to guide instructional action (e.g., add tracing exercises, revise examples, or target short interventions).
- Treat diagnoses as leads for investigation, not as student labels; verify with representative code and, when possible, student explanations.
- Prefer conservative rollouts: start with cohort-level analytics rather than per-student misconception claims.

## 7. Limitations and Future Work

The synthetic data design that enables ground-truth evaluation also bounds what TRACER can claim:

- **Ecological validity.** Synthetic submissions may not capture the full diversity of novice reasoning—real students produce messier code, hold multiple misconceptions simultaneously, and make errors that no taxonomy anticipates. Follow-on studies with authentic student data are needed to validate transfer.
- **Proxy-alignment, not "true belief."** Injected misconceptions operationalize beliefs as textual descriptions; matching measures alignment to these descriptions, not to students' actual cognitive states.
- **Evidence quality unscored.** Current evaluation checks whether the model's hypothesis aligns with ground truth, not whether its cited code spans logically entail the hypothesis. A model could produce the right label through shallow pattern-matching rather than sound pedagogical reasoning.

These boundaries define the scope of the contribution: TRACER demonstrates that LLM-based belief attribution is *feasible and worth pursuing*, not that it is classroom-ready.

## 8. Conclusion

The integration of LLMs into CS1 is accelerating. Student-facing tools that explain errors and suggest fixes offer genuine value for accessibility and practice. This paper argues for broadening that scope: alongside repair-oriented tools, there is a distinct and complementary direction—instructor-facing hypothesis generators that surface patterns in student thinking.

The evidence from TRACER supports this direction. High recall demonstrates feasibility: LLMs can generate plausible belief hypotheses from code. But the concentration of false positives on correct programs reveals that belief attribution requires more caution than bug detection. When the goal shifts from "what is wrong with the code" to "what does the student believe," the cost of confident misdiagnosis rises sharply.

By treating LLMs as instructor-facing instruments, we unlock their capacity to surface the hidden labor of tutoring—understanding not just *what* is wrong, but *why* the student wrote it—while insulating learners from the noise of confident misattribution. The path forward requires diagnostic humility: evidence-first interfaces, normalized abstention, and evaluation standards that reward specificity over coverage.

If the community embraces this framing, we can achieve something valuable: as our tools become more artificial, our teaching can become more profoundly human.

## References

du Boulay, B., O'Shea, T., & Monk, J. (1981). The black box inside the glass box: Presenting computing concepts to novices. *International Journal of Man-Machine Studies*, 14(3), 237–249. https://doi.org/10.1016/S0020-7373(81)80047-9

Johnson, W. L., & Soloway, E. (1985). PROUST: Knowledge-based program understanding. *IEEE Transactions on Software Engineering*, SE-11(3), 267–275. https://doi.org/10.1109/TSE.1985.232210

du Boulay, B. (1986). Some difficulties of learning to program. *Journal of Educational Computing Research*, 2(1), 57–73. https://doi.org/10.2190/3LFX-9RRF-67T8-UVK9

Pea, R. D. (1986). Language-independent conceptual "bugs" in novice programming. *Journal of Educational Computing Research*, 2(1), 25–36. https://doi.org/10.2190/689T-1R2A-X4W4-29J2

Bandura, A. (1997). *Self-efficacy: The exercise of control*. W. H. Freeman.

Parasuraman, R., & Riley, V. (1997). Humans and automation: Use, misuse, disuse, abuse. *Human Factors*, 39(2), 230–253. https://doi.org/10.1518/001872097778543886

Sirkiä, T., & Sorva, J. (2012). Exploring programming misconceptions: An analysis of student mistakes in visual program simulation exercises. In *Proceedings of the 12th Koli Calling International Conference on Computing Education Research* (Koli Calling '12), 19–28. https://doi.org/10.1145/2401796.2401799

Brown, N. C. C., & Altadmri, A. (2014). Investigating novice programming mistakes: Educator beliefs vs. student data. In *Proceedings of the 10th Annual Conference on International Computing Education Research* (ICER '14), 43–50. https://doi.org/10.1145/2632320.2632343

Ji, Z., Lee, N., Frieske, R., Yu, T., Su, D., Xu, Y., Ishii, E., Bang, Y., Madotto, A., & Fung, P. (2023). Survey of hallucination in natural language generation. *ACM Computing Surveys*, 55(12), Article 248. https://doi.org/10.1145/3571730

Koutcheme, C., Dainese, N., Sarsa, S., Hellas, A., Leinonen, J., & Denny, P. (2024). Open source language models can provide feedback: Evaluating LLMs' ability to help students using GPT-4-as-a-judge. In *Proceedings of the 2024 ACM Conference on Innovation and Technology in Computer Science Education* (ITiCSE '24). https://doi.org/10.1145/3649837.3654178

Azaiz, I., Kiesler, N., & Strickroth, S. (2024). Feedback-generation for programming exercises with GPT-4. In *Proceedings of the 2024 ACM Conference on Innovation and Technology in Computer Science Education* (ITiCSE '24). https://doi.org/10.1145/3649217.3653594

Sonkar, S., Chen, X., Liu, N., Baraniuk, R. G., & Sachan, M. (2024). LLM-based cognitive models of students with misconceptions. *arXiv preprint arXiv:2410.12294*. https://doi.org/10.48550/arXiv.2410.12294

Al-Hossami, E., & Bunescu, R. (2025). McMining: Automated discovery of misconceptions in student code. *arXiv preprint arXiv:2510.08827*. https://doi.org/10.48550/arXiv.2510.08827
