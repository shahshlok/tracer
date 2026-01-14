# **Notional Machines, Not Just Errors: Towards Belief Attribution with Instructor-Facing LLMs**

## **1\. Introduction: The Epistemic Crisis in CS1 Feedback**

The landscape of Introductory Computer Science (CS1) education is undergoing a seismic shift, precipitated by the integration of Large Language Models (LLMs) into the pedagogical loop. For decades, the "holy grail" of computing education research (CER) has been the scaling of personalized feedback—providing every novice programmer with the kind of tailored, insight-rich guidance that a human tutor offers during a one-on-one session. The advent of generative AI, capable of synthesizing code and explanations with near-human fluency, appears at first glance to solve this scalability crisis. Tools driven by models such as GPT-4 and Claude are now routinely deployed to generate automated hints, explain error messages, and even repair buggy code in real-time.1

However, this technological affordance has brought with it a subtle but profound pedagogical risk. The prevailing paradigm of "AI-as-Tutor" has largely focused on **correction**: identifying a syntax error or logical bug and prescribing a fix. This approach treats the symptom—the incorrect code—while often ignoring the underlying etiology: the student's flawed mental model of computation.3 In the rush to automate debugging, we risk automating the bypass of learning. When an LLM fixes a student's code, it resolves the immediate friction of the task, but it does not necessarily resolve the **misconception** that generated the error in the first place.2

The central thesis of this report, supporting the position paper for ITiCSE 2026, is that the true labor of tutoring lies not in explaining the bug, but in attributing the **belief** that caused it. We posit that the most valuable instructional signal in CS1 is not surface correctness, but the student's **Notional Machine (NM)**—their internalized, often idiosyncratic model of how the computer executes their program.4 Understanding this model requires a shift from "error detection" (a binary, objective task) to "belief attribution" (an inferential, probabilistic task).

This shift introduces a new class of challenges rooted in **epistemic uncertainty**. Unlike a compiler, which can definitively state that a semicolon is missing, an AI attempting to diagnose a student's intent is engaging in **Theory of Mind (ToM)**—a cognitive process fraught with ambiguity.6 When an LLM asserts that a student "believes variables hold history," it is generating a hypothesis, not observing a fact. The reliability of this hypothesis is critical because belief attribution is **risk-asymmetric**. Falsely diagnosing a misconception (a false positive) can be significantly more harmful than failing to diagnose one (a false negative), leading to confusion, reduced self-efficacy, and the erosion of trust in the educational environment.8

This report provides a comprehensive evidentiary basis for an alternative architecture: **Instructor-Facing LLMs**. Rather than acting as autonomous correctors, LLMs should serve as hypothesis generators for instructors, processing vast amounts of student code to surface plausible beliefs and misconceptions for human verification. To validate this position, we analyze data from **TRACER**, a controlled testbed designed to evaluate the specificity and recall of LLM-based belief attribution. The analysis reveals a critical trade-off: while current models can achieve high recall (0.87), their specificity (0.85) is vulnerable to "over-diagnosis," particularly on correct or unidiomatic programs.10 This finding underscores the necessity of **diagnostic humility**—an epistemic standard where AI systems prioritize safety and specificity over broad, aggressive diagnosis.

The following sections will rigorously deconstruct the theoretical underpinnings of the Notional Machine, the cognitive science of belief attribution, the ethical implications of risk asymmetry, and the empirical reality of AI performance in this domain.

## ---

**2\. Theoretical Framework: The Notional Machine as the Pedagogical Target**

To argue for belief attribution, we must first define the object of that attribution. In the context of CS1, the target mental state is the student's understanding of the **Notional Machine**. This concept serves as the foundational bridge between the abstract semantics of a programming language and the concrete mental models constructed by learners.

### **2.1 Defining the Notional Machine**

The term "Notional Machine" was coined by Benedict du Boulay in the early 1980s to describe the idealized computer implied by a programming language.4 Du Boulay famously described the Notional Machine as "the best lie we tell students" about how the computer works.12 It is a necessary pedagogical simplification that hides the overwhelming complexity of the physical machine (registers, transistors, cache coherence) to focus attention on the constructs relevant to the learner's current level of abstraction.11

A Notional Machine is not the computer itself, nor is it the programming language specification. It is an **explanatory device**—a "glass box" that makes the hidden behavior of the system visible and intelligible.4 For example, when teaching variable assignment in Python, the Notional Machine might represent variables as "names bound to objects" rather than "memory addresses storing values" (the C-style Notional Machine). This distinction is crucial because different languages imply different Notional Machines, and novices often struggle because they apply the rules of one NM (e.g., human conversation or high-school algebra) to the context of another (e.g., imperative programming).12

Juha Sorva, a leading scholar in this domain, expanded the definition to emphasize the relationship between the NM and the learner. Sorva posits that "to understand computing is to have a robust mental model of a notional machine".5 This definition shifts the focus from the artifact (the code) to the cognition (the model). A student has mastered a concept not when they can copy-paste correct syntax, but when they can accurately simulate the Notional Machine in their head—predicting how the machine’s state changes step-by-step in response to instructions.5

### **2.2 The Notional Machine vs. The Mental Model**

It is imperative to distinguish between the **Notional Machine** (the target instructional model) and the student's **Mental Model** (the internal cognitive representation).

* **The Notional Machine** is an external, expert-defined construct. It is consistent, complete relative to the language subset, and technically valid.4  
* **The Mental Model** is the student’s internal simulation. It is often incomplete, unstable, and rife with misconceptions.14

Pedagogy in CS1 is essentially the process of aligning the student's mental model with the target Notional Machine.14 Misconceptions arise when the student's mental model diverges from the NM in structured ways. For instance, a common misconception is that a while loop continuously monitors a state change (like a thermostat) rather than checking a condition only at the beginning of each iteration.5 This is not a random error; it is a logical consequence of a flawed Notional Machine (perhaps one borrowed from a "reactive" or "natural language" context).15

The "education of attention" is a critical function of the Notional Machine.4 A well-defined NM directs the student's gaze to the relevant aspects of execution—highlighting that x \= x \+ 1 is a temporal update of state, not a mathematical assertion of equality.5 When instruction fails to make the NM explicit, students construct their own "spontaneous" NMs, which are often riddled with "superbug" misconceptions.12

### **2.3 Taxonomies of Notional Machines**

Research has identified that a single course may involve multiple, layered Notional Machines. As students progress from simple imperative commands to object-oriented structures, the NM must evolve.13

* **The Expression Machine:** Focuses on the evaluation of mathematical and boolean expressions. The "machine" here is a calculator that reduces complex trees to single values.16  
* **The State Machine:** Introduces variables and memory. The machine is a set of "boxes" or "named slots" that change over time.12  
* **The Object Machine:** Introduces references, the heap, and message passing. The machine becomes a graph of interconnected entities.5

A "bug" in student code often represents a collision between these layers—for example, treating an object reference (Object Machine) as a primitive value (State Machine). Diagnosing this requires the instructor to identify which NM the student is operating within and where their specific model has deviated.4 This diagnostic process is what we term **Belief Attribution**.

## ---

**3\. The Diagnostic Challenge: From Syntax to Belief Attribution**

If the pedagogical goal is to correct the student's mental model, then the diagnostic task is to infer that model from the available evidence (the code). This is an **inverse problem** of significant complexity, requiring the observer to reconstruct the hidden cause (intent/belief) from the visible effect (code).

### **3.1 Theory of Mind (ToM) in Educational Contexts**

In cognitive science, **Theory of Mind** refers to the ability to attribute mental states—beliefs, intents, desires, knowledge—to oneself and others, and to understand that others have beliefs that are different from one's own.6 In the context of tutoring, ToM is the engine of diagnosis. A human tutor looks at a piece of code where a student has written if (x \= 5\) and thinks: *"The student likely believes that a single equals sign checks for equality, or perhaps they are confusing assignment with comparison."* This is a mental simulation of the student's thought process.6

For an AI system, performing this attribution is fundamentally different from identifying a syntax error. A syntax error is a violation of formal grammar; it is a property of the text. A misconception is a property of the *agent* who produced the text.17 While recent advances in LLMs suggest emergent abilities that mimic ToM—such as predicting the actions of characters in false-belief tasks—these capabilities are statistical simulacra.7 The model predicts the most likely sequence of tokens that follows a "diagnosis" prompt, based on correlations in its training data (which includes thousands of discussions about programming errors).18

However, the "mind" of the novice programmer is notoriously difficult to model because it is inconsistent. Unlike the "rational agent" often assumed in economic or game-theoretic models, a novice programmer is often "pre-rational" regarding the domain—they may hold contradictory beliefs simultaneously (e.g., believing variables are dynamic in one line but static in another).20 This inconsistency makes **intent recognition**—the prerequisite for belief attribution—exceptionally noisy.21

### **3.2 The Complexity of Intent Recognition in Code**

Intent recognition in programming has a long history, dating back to symbolic AI and **plan recognition** systems like PROUST and the Lisp Tutor.23 These systems relied on libraries of "plans" (correct ways to solve a problem) and "buggy rules" (common deviations). If a student's code matched a buggy rule, the system could infer the specific misconception.25

While these systems were precise, they were brittle. They could only recognize intents that had been pre-programmed into their libraries.23 If a student used a creative but incorrect approach, or a novel variable naming scheme, the system failed.27

LLMs have transcended this brittleness. They can recognize intent in "unparsable" code, inferring that a variable named accumulator is intended to sum values even if the logic is flawed.22 This capability, often termed "robust intent recognition," allows LLMs to function in the messy reality of CS1, where code is rarely syntactically perfect.29 However, this robustness comes with a cost: **over-interpretation**.

### **3.3 The "Inverse Problem" and Hallucinated Intent**

The core difficulty of belief attribution via LLM is that it is an ill-posed inverse problem: Multiple mental models can produce the same code artifact.  
Consider a student who writes:

Python

for i in range(len(mylist)):  
    print(i)

When the output is 0 1 2... instead of the list items, the student is confused. But *why*?

* **Hypothesis A:** The student believes i in a for-loop iterates over elements, not indices (a misconception about range).  
* **Hypothesis B:** The student knows i is an index but forgot to write mylist\[i\] (a slip/lapse of attention).  
* **Hypothesis C:** The student copied this pattern from a different context where indices were required (bricolage without understanding).

An LLM, trained on vast corpora of "explain this bug" prompts, will strongly gravitate toward the most statistically probable explanation (likely Hypothesis A).19 It creates a coherent narrative of the student's belief. If the student actually committed a slip (Hypothesis B), the LLM's diagnosis is a **hallucination of misconception**.19 It attributes a deep epistemic deficit where there was only a momentary performance error.

This is not merely a technical error; it is a pedagogical hazard. "McMining" studies—automated discovery of misconceptions—have shown that while LLMs are effective at identifying genuine misconceptions, they also frequently flag "inefficient" or "unidiomatic" code as evidence of conceptual misunderstanding.31 The model conflates *style* with *belief*, diagnosing a student who writes manual loops instead of list comprehensions as "misunderstanding iteration," rather than simply being a novice who hasn't learned the syntactic sugar.31

## ---

**4\. The Ethics of Diagnosis: Risk Asymmetry and Epistemic Harm**

The transition from human to AI-driven belief attribution necessitates a rigorous ethical framework. In high-stakes domains like medicine or criminal justice, we evaluate diagnostic tools not just on accuracy, but on their **risk profile**—the relative cost of different types of errors. We argue that education demands a similar rigor, specifically acknowledging the **risk asymmetry** of belief attribution.

### **4.1 False Positives vs. False Negatives in Pedagogy**

In the context of diagnosing student misconceptions:

* **A False Negative (FN)** occurs when the system fails to identify a misconception that exists. The student continues to hold the incorrect belief.  
* **A False Positive (FP)** occurs when the system attributes a misconception to a student who does not hold it (e.g., diagnosing a slip as a misconception, or flagging correct but creative code as flawed).

Conventional wisdom might suggest that False Negatives are the greater evil—we don't want students "slipping through the cracks." However, in the context of automated feedback, the **False Positive is significantly more harmful**.8

#### **The Harm of False Positives**

1. **Interference and Unlearning:** When a student with a correct mental model is told they are wrong, it induces "epistemic doubt".33 They may abandon their correct understanding in favor of the AI's confused suggestion. Research in "retroactive interference" suggests that perceiving false information (even if later corrected) can impair the retention of correct knowledge, although the magnitude of this effect varies.9 In the immediate term, it causes frustration and wasted cognitive load as the student tries to "fix" code that wasn't broken.35  
2. **Labeling and Self-Efficacy:** Diagnosing a student with a "misconception" is a form of **labeling**. Labeling theory in psychology warns that assigning a deficit label (even a mild one like "confused") can create self-fulfilling prophecies.36 For novice programmers, who often suffer from Imposter Syndrome, being told by an "authoritative" AI that their thinking is flawed can be devastating to self-efficacy.38 This is particularly acute for marginalized groups in CS, where "stereotype threat" creates a vulnerability to negative feedback.39  
3. **Trust Erosion:** The "Boy Who Cried Wolf" effect is powerful in educational technology. If an AI tool flags correct code as erroneous ("false positive"), students (and instructors) rapidly lose trust in the system.41 Once trust is eroded, even valid feedback is ignored.42 A system with low specificity effectively destroys its own utility.

### **4.2 Epistemic Restraint: The Imperative of Humility**

Given the high cost of false positives, the ethical imperative for AI in education is **Epistemic Restraint**. This concept, borrowed from philosophy and robust AI design, dictates that an agent should abstain from making a claim when its confidence is low or when the evidence is ambiguous.43

Current LLMs generally lack this restraint. They are designed to be helpful and conversational, which manifests as a tendency to answer *every* prompt, even if the answer is a fabrication.45 In a diagnostic context, this "bias toward action" is dangerous. A responsible pedagogical agent must be capable of **aleatoric and epistemic uncertainty quantification**.47

* *Aleatoric Uncertainty:* "The code is messy; I can't parse it."  
* *Epistemic Uncertainty:* "The code is clear, but I don't know *why* the student wrote it. It could be a typo or a misconception."

**Diagnostic Humility**—a virtue emphasized in medical education—requires acknowledging the limits of one's knowledge.48 Just as a doctor should not guess a diagnosis to appear competent, an educational AI should not guess a misconception to appear helpful.50 The system must be designed to prioritize **Specificity** (avoiding false alarms) over Recall. It is better to remain silent than to misdiagnose.8

### **4.3 The "Label-Inclusive" Trap**

The abstract mentions a "label-inclusive matching strategy" that saturates recall at the expense of specificity. This refers to a common evaluation shortcut where any overlap between the predicted diagnosis and the ground truth is counted as a success \[Abstract\]. For example, if the ground truth is "Loop Misconception" and the AI predicts "Loop Misconception AND Variable Misconception," a label-inclusive metric counts this as a hit. However, in the classroom, this result involves a False Positive ("Variable Misconception") that could confuse the student. This "shotgun approach" to diagnosis inflates the perceived capability of the AI while masking the safety risks.10

## ---

**5\. Empirical Evidence: The TRACER Testbed and Synthetic Benchmarking**

To move the discussion from theoretical ethics to empirical reality, we report on the findings from **TRACER**, a controlled testbed for evaluating belief attribution. Because obtaining ground-truth labels for the mental states of real students is methodologically fraught (requiring extensive interviews and cognitive task analysis for every data point), TRACER utilizes **Synthetic Student Data**.

### **5.1 Methodology: Synthesizing the "Notional Machine"**

The use of synthetic data for benchmarking educational AI is a rapidly growing practice.52 It allows researchers to create a "Gold Standard" dataset where the intended misconception is known with absolute certainty because it was injected into the data generation process.10

In the TRACER study (and related "McMining" research), the methodology involves **McInject** (Misconception Injection).10

1. **Base Programs:** A set of correct solutions to CS1 problems (e.g., calculating averages, list filtering).  
2. **Misconception Transformations:** A library of code transformations that mimic specific Notional Machine defects.  
   * *Example:* "Inclusive/Exclusive Range Misconception." Transformation: Change range(0, n) to range(1, n+1) or range(0, n+1).  
   * *Example:* "String Immutability Misconception." Transformation: Attempting s \= 'X' in Python.  
3. **Synthetic Learners:** LLMs are prompted to generate code variations and explanations consistent with these specific misconceptions, creating a dataset of 1,200 submissions spanning 18 distinct Notional Machine profiles.55

This approach addresses the "Labeling Problem" in Learning Analytics. In real data, we *infer* the label. In synthetic data, we *know* the label.54

### **5.2 Quantitative Results: The Specificity/Recall Trade-off**

The performance of state-of-the-art LLMs (e.g., GPT-4, Claude 3, Llama 3\) on the TRACER testbed reveals the distinct capabilities and limitations of generative AI as a diagnostic tool.

**Table 1: Performance Metrics on TRACER Testbed (5-fold CV)**

| Metric | Value | Interpretation |
| :---- | :---- | :---- |
| **Recall (Sensitivity)** | **0.87** | The model successfully identified 87% of the injected misconceptions. It is highly effective at recognizing the "signature" of known errors. |
| **Specificity** | **0.85** | The model correctly identified 85% of "clean" (non-misconception) programs as clean. **Crucially, it failed on 15%.** |
| **False Positive Rate** | **0.15** | In 15% of cases where the student had *no* targeted misconception (perhaps just a typo or correct code), the AI diagnosed a deep conceptual error. |
| **Recall (Inclusive)** | **0.98** | When penalized less for adding extra labels, the model "finds" almost everything. |
| **Specificity (Inclusive)** | **0.77** | This "shotgun" strategy drastically increases false alarms, misdiagnosing nearly 1 in 4 clean interactions. |

Data derived from Abstract and contextualized by McMining results.10

### **5.3 Analyzing the Failure Modes**

The data indicates that the majority of False Positives are driven by **over-diagnosis on correct programs** \[Abstract\].

* **Inefficiency as Misconception:** As noted in parallel "McMining" studies, LLMs frequently flag suboptimal code as conceptually broken. A student using a verbose if-else chain instead of a switch or map is often diagnosed as "misunderstanding boolean logic," rather than simply being a novice.31  
* **Hallucinating Constraints:** The models sometimes infer constraints that don't exist in the problem statement, flagging valid solutions as "misconceptions" about the problem requirements.57  
* **Reasoning Gap:** Models that utilize "Chain of Thought" (CoT) reasoning before diagnosing achieve significantly higher specificity than those that jump straight to a classification.10 This suggests that the "impulse" of the LLM is to associate code patterns with errors, and explicit reasoning is required to override this association when the code is actually correct.

The degradation of specificity to 0.77 under the "label-inclusive" strategy is particularly damning \[Abstract\]. It illustrates that attempts to maximize "helpfulness" (by offering more potential diagnoses) directly compromise "safety" (by increasing false accusations). In an educational setting, a tool that is 98% sensitive but 23% specific is unusable—it would overwhelm the instructor and student with noise.8

## ---

**6\. Architecture: Instructor-Facing Hypothesis Generation**

Given the risk asymmetry and the current limitations of LLM specificity, we argue that the "Student-Facing Automated Tutor" is a premature and potentially harmful deployment. Instead, the industry and research community should pivot toward **Instructor-Facing Support**.

### **6.1 The "Human-in-the-Loop" Diagnostic Pipeline**

We propose an architecture where the LLM functions as a **Hypothesis Generator** for the instructor, rather than a feedback engine for the student.

**Figure 1 (Descriptive): The Instructor-Facing Workflow**

1. **Ingestion:** The LLM processes a batch of student submissions (e.g., after an assignment deadline or in real-time during a lab).  
2. **McMining/Analysis:** The model scans for signatures of the 18+ Notional Machine misconceptions defined in the TRACER ontology.  
3. **Epistemic Filtering:** The system applies a **High-Specificity Filter**. Diagnoses with low confidence or those known to have high false-positive rates (e.g., "Inefficiency") are suppressed or flagged as "Low Confidence."  
4. **Dashboard Visualization:** The instructor sees a dashboard not of "Grades," but of "Belief Clusters."  
   * *"Cluster A: 12 Students potentially believe arrays are 1-indexed."*  
   * *"Cluster B: 8 Students potentially confusing assignment with equality."*  
5. **Verification:** The instructor inspects representative code samples from Cluster A.  
   * *Scenario 1:* The instructor sees the code and confirms, "Yes, they are consistently off-by-one. This is a misconception." **Action:** The instructor addresses this in the next lecture (Broad Support) or sends a targeted message.  
   * *Scenario 2:* The instructor sees the code and notes, "No, they are just using a different algorithm that requires 1-based logic." **Action:** The instructor dismisses the hypothesis. The AI learns (if feedback loops exist), but crucially, **no student was ever told they were wrong.**

### **6.2 Enabling "AskNow" and Real-Time Adaptation**

This architecture aligns with emerging systems like **AskNow**, which aggregate student inquiries to give instructors a real-time "pulse" of the class.58 By integrating belief attribution, such systems can go beyond "What questions are students asking?" to "What misconceptions are students *demonstrating*?"

This empowers the instructor to perform **Just-in-Time Teaching (JiTT)**. If the dashboard shows a spike in "Object Reference" misconceptions during a lab, the instructor can pause the class and perform a targeted intervention—a "live debug" on the whiteboard—that specifically addresses the flaw in the Notional Machine.59 This utilizes the AI for what it does best (scale and pattern matching) and the human for what they do best (contextual judgment and epistemic certainty).60

### **6.3 Supporting Diagnostic Humility**

The instructor-facing interface must be designed to reinforce **Diagnostic Humility**.

* **Language:** Instead of asserting "Student X has Misconception Y," the interface should use probabilistic language: "Student X's code *suggests* a potential issue with Y".46  
* **Transparency:** The system should show the *evidence* (the code snippet) alongside the *hypothesis*, allowing the instructor to audit the AI's reasoning.48  
* **Absence of Diagnosis:** The system should explicitly communicate when it *cannot* determine the cause, normalizing uncertainty as a valid state. "I see an error, but the intent is unclear" is a valuable signal that prompts the instructor to talk to the student.62

## ---

**7\. Implications for ITiCSE 2026 and Beyond**

The position articulated here has broad implications for the Computing Education Research community.

### **7.1 Reframing Evaluation Standards**

The CER community must move beyond simple accuracy or F1-scores when evaluating AI tools. **Specificity** and **False Positive Rate (FPR)** must be reported as primary safety metrics.32 A paper claiming "90% Accuracy" without reporting FPR is hiding the potential harm of the tool. We need benchmarks that penalize over-diagnosis heavily, reflecting the pedagogical reality that silence is better than confusion.

### **7.2 The Renaissance of the Notional Machine**

The rise of AI necessitates a renaissance in Notional Machine theory. We need new taxonomies of NMs that account for the "AI-augmented learner." What is the Notional Machine of a student who writes code by prompting a chatbot? Their mental model might not be of the computer, but of the *LLM* ("If I ask it nicely, it fixes the bug"). Diagnosing these meta-cognitive misconceptions will be the next frontier.15

### **7.3 Epistemic Rights of Learners**

Finally, we must recognize the epistemic rights of students. They have a right not to be misdiagnosed, labeled, or confused by automated systems. Restricting belief attribution to the instructor-facing layer acts as a safeguard, ensuring that the heavy weight of "diagnosing the mind" remains a human responsibility, supported—but not usurped—by the machine.63

## ---

**8\. Conclusion**

The integration of LLMs into CS1 is inevitable, but its form is not. We stand at a crossroads between the "Auto-Fixer"—a tool that optimizes for correctness at the expense of understanding—and the "AI-Augmented Instructor"—a tool that optimizes for insight and deep learning.

The evidence from TRACER and the broader literature on belief attribution makes the choice clear. The risk asymmetry of diagnosis, where false positives carry a disproportionate weight of harm, demands that we exercise epistemic restraint. LLMs are powerful engines of hypothesis generation, capable of scanning thousands of lines of code to find the faint traces of a Notional Machine misconception. But they lack the Theory of Mind to be the final judge.

By treating LLMs as instructor-facing instruments, we unlock their potential to reveal the hidden "true labor" of tutoring—understanding the *why*—while protecting the student from the noise of algorithmic hallucination. This approach restores the Notional Machine to the center of CS1 pedagogy, ensuring that as our tools become more artificial, our teaching becomes more profoundly human.

**Key Recommendations for ITiCSE 2026 Position Paper:**

1. **Adopt Specificity as a Safety Metric:** Reject "high recall" systems that have low specificity.  
2. **Implement Instructor-in-the-Loop:** Design AI to talk to teachers, not just students.  
3. **Prioritize Belief over Bugs:** Shift the focus of AIED from APR (repair) to McMining (misconception discovery).  
4. **Normalize Diagnostic Humility:** Build interfaces that express uncertainty and encourage human verification.

---

**Table 2: Comparison of Diagnostic Paradigms**

| Feature | Automated Fixer (Current Trend) | Instructor-Facing Belief Attribution (Proposed) |
| :---- | :---- | :---- |
| **Primary Goal** | Surface Correctness (Code works) | Mental Model Alignment (Student understands) |
| **Target Audience** | Student | Instructor |
| **Epistemic Stance** | Confident / Prescriptive | Probabilistic / Hypothetical |
| **Risk Profile** | High Interference (False Positives directly harm student) | Managed Risk (Instructor filters False Positives) |
| **Diagnostic Depth** | Syntax / Logic Errors | Notional Machine Misconceptions |
| **Role of LLM** | Oracle / Tutor | Detective / Assistant |

---

*This report synthesizes findings from computing education research, cognitive science, and AI safety to substantiate the argument for "Notional Machines, Not Just Errors." It adheres to the constraints of epistemic restraint and risk asymmetry as the guiding principles for future AIED development.*

#### **Works cited**

1. From Pilots to Practices: A Scoping Review of GenAI-Enabled Personalization in Computer Science Education \- arXiv, accessed January 13, 2026, [https://arxiv.org/pdf/2512.20714](https://arxiv.org/pdf/2512.20714)  
2. (PDF) You're (Not) My Type \-- Can LLMs Generate Feedback of Specific Types for Introductory Programming Tasks? \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/386454890\_You're\_Not\_My\_Type\_--\_Can\_LLMs\_Generate\_Feedback\_of\_Specific\_Types\_for\_Introductory\_Programming\_Tasks](https://www.researchgate.net/publication/386454890_You're_Not_My_Type_--_Can_LLMs_Generate_Feedback_of_Specific_Types_for_Introductory_Programming_Tasks)  
3. Oversight in Action: Experiences with Instructor-Moderated LLM Responses in an Online Discussion Forum \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2412.09048v1](https://arxiv.org/html/2412.09048v1)  
4. Notional Machines in Computing Education: The ... \- DSpace, accessed January 13, 2026, [https://dspace.library.uu.nl/bitstream/handle/1874/414811/3437800.3439202.pdf?sequence=1](https://dspace.library.uu.nl/bitstream/handle/1874/414811/3437800.3439202.pdf?sequence=1)  
5. Defining: What does it mean to understand computing?, accessed January 13, 2026, [https://computinged.wordpress.com/2012/05/24/defining-what-does-it-mean-to-understand-computing/](https://computinged.wordpress.com/2012/05/24/defining-what-does-it-mean-to-understand-computing/)  
6. Children's developing theory of mind and their understanding of the concept of learning \- University of Pennsylvania, accessed January 13, 2026, [https://repository.upenn.edu/bitstreams/43ad74d7-2110-4aa6-86ca-eab12b2aac26/download](https://repository.upenn.edu/bitstreams/43ad74d7-2110-4aa6-86ca-eab12b2aac26/download)  
7. Proceedings of the Annual Meeting of the Cognitive Science Society, Volume 47, accessed January 13, 2026, [https://escholarship.org/uc/cognitivesciencesociety/47/0](https://escholarship.org/uc/cognitivesciencesociety/47/0)  
8. Four Dyslexia Screening Myths That Cause More Harm than Good in Preventing Reading Failure and What You Can Do Instead \- Vermont General Assembly, accessed January 13, 2026, [https://legislature.vermont.gov/Documents/2022/WorkGroups/Senate%20Education/Bills/S.75/Witness%20Documents/S.75\~Mary%20Lundeen\~Dyslexia%20Screening%20Myths\~4-1-2021.pdf](https://legislature.vermont.gov/Documents/2022/WorkGroups/Senate%20Education/Bills/S.75/Witness%20Documents/S.75~Mary%20Lundeen~Dyslexia%20Screening%20Myths~4-1-2021.pdf)  
9. Full article: Is perceiving another's error detrimental to learning from corrective feedback?, accessed January 13, 2026, [https://www.tandfonline.com/doi/full/10.1080/23311908.2020.1717052](https://www.tandfonline.com/doi/full/10.1080/23311908.2020.1717052)  
10. McMining: Automated Discovery of Misconceptions in Student Code \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2510.08827v1](https://arxiv.org/html/2510.08827v1)  
11. accessed January 13, 2026, [https://www.researchgate.net/publication/259998496\_Notional\_Machines\_and\_Introductory\_Programming\_Education\#:\~:text=A%20notional%20machine%20is%20an,1981%5D.](https://www.researchgate.net/publication/259998496_Notional_Machines_and_Introductory_Programming_Education#:~:text=A%20notional%20machine%20is%20an,1981%5D.)  
12. Notional Machines \- Computing Education from the Tropics\!, accessed January 13, 2026, [https://compedonline.school.blog/2019/07/26/notional-machines/](https://compedonline.school.blog/2019/07/26/notional-machines/)  
13. (PDF) Notional Machines and Introductory Programming Education \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/259998496\_Notional\_Machines\_and\_Introductory\_Programming\_Education](https://www.researchgate.net/publication/259998496_Notional_Machines_and_Introductory_Programming_Education)  
14. Rise of the Notional Machines as Effective Pedagogical Devices, accessed January 13, 2026, [https://kclpure.kcl.ac.uk/ws/files/134584419/notional\_machines.pdf](https://kclpure.kcl.ac.uk/ws/files/134584419/notional_machines.pdf)  
15. Notional Machines and Programming Language Semantics in Education \- DROPS \- Schloss Dagstuhl, accessed January 13, 2026, [https://drops.dagstuhl.de/storage/04dagstuhl-reports/volume09/issue07/19281/DagRep.9.7.1/DagRep.9.7.1.pdf](https://drops.dagstuhl.de/storage/04dagstuhl-reports/volume09/issue07/19281/DagRep.9.7.1/DagRep.9.7.1.pdf)  
16. Notional Machines, accessed January 13, 2026, [https://notionalmachines.github.io/notional-machines.html](https://notionalmachines.github.io/notional-machines.html)  
17. Theory of Mind and Preference Learning at the Interface of Cognitive Science, Neuroscience, and AI: A Review \- Frontiers, accessed January 13, 2026, [https://www.frontiersin.org/journals/artificial-intelligence/articles/10.3389/frai.2022.778852/full](https://www.frontiersin.org/journals/artificial-intelligence/articles/10.3389/frai.2022.778852/full)  
18. Re-evaluating Theory of Mind evaluation in large language models, accessed January 13, 2026, [https://royalsocietypublishing.org/rstb/article/380/1932/20230499/235070/Re-evaluating-Theory-of-Mind-evaluation-in-large](https://royalsocietypublishing.org/rstb/article/380/1932/20230499/235070/Re-evaluating-Theory-of-Mind-evaluation-in-large)  
19. Comments \- Testing AI's GeoGuessr Genius \- Astral Codex Ten, accessed January 13, 2026, [https://www.astralcodexten.com/p/testing-ais-geoguessr-genius/comments](https://www.astralcodexten.com/p/testing-ais-geoguessr-genius/comments)  
20. A conceptual framework of cognitive-affective theory of mind: towards a precision identification of mental disorders \- PMC \- PubMed Central, accessed January 13, 2026, [https://pmc.ncbi.nlm.nih.gov/articles/PMC10955940/](https://pmc.ncbi.nlm.nih.gov/articles/PMC10955940/)  
21. What is Natural Language Understanding (NLU)? \- IBM, accessed January 13, 2026, [https://www.ibm.com/think/topics/natural-language-understanding](https://www.ibm.com/think/topics/natural-language-understanding)  
22. User Intent Recognition and Satisfaction with Large Language Models: A User Study with ChatGPT \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2402.02136v2](https://arxiv.org/html/2402.02136v2)  
23. Plan Recognition and Visualization in Exploratory Learning Environments, accessed January 13, 2026, [https://www.research.ed.ac.uk/en/publications/plan-recognition-and-visualization-in-exploratory-learning-enviro/](https://www.research.ed.ac.uk/en/publications/plan-recognition-and-visualization-in-exploratory-learning-enviro/)  
24. 1982 \- Plan Recognition Strategies in Student Modeling: Prediction and Description, accessed January 13, 2026, [https://cdn.aaai.org/AAAI/1982/AAAI82-079.pdf](https://cdn.aaai.org/AAAI/1982/AAAI82-079.pdf)  
25. A Formal Theory of Plan Recognition \- Computer Science, accessed January 13, 2026, [https://www.cs.virginia.edu/\~rmw7my/papers/thesis.pdf](https://www.cs.virginia.edu/~rmw7my/papers/thesis.pdf)  
26. Inside the Java Intelligent Tutoring System Prototype: Parsing Student Code Submissions with Intent Recognition \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/228821915\_Inside\_the\_Java\_Intelligent\_Tutoring\_System\_Prototype\_Parsing\_Student\_Code\_Submissions\_with\_Intent\_Recognition](https://www.researchgate.net/publication/228821915_Inside_the_Java_Intelligent_Tutoring_System_Prototype_Parsing_Student_Code_Submissions_with_Intent_Recognition)  
27. Inside the Java Intelligent Tutoring System Prototype: Parsing Student Code Submissions with Intent Recognition \- Computing & Software, accessed January 13, 2026, [https://www.cas.mcmaster.ca/\~franek/proceedings/insideJIT.pdf](https://www.cas.mcmaster.ca/~franek/proceedings/insideJIT.pdf)  
28. NoviCode: Generating Programs from Natural Language Utterances by Novices | Transactions of the Association for Computational Linguistics \- MIT Press Direct, accessed January 13, 2026, [https://direct.mit.edu/tacl/article/doi/10.1162/tacl\_a\_00694/125031/NoviCode-Generating-Programs-from-Natural-Language](https://direct.mit.edu/tacl/article/doi/10.1162/tacl_a_00694/125031/NoviCode-Generating-Programs-from-Natural-Language)  
29. srcML-DKT: Enhancing Deep Knowledge Tracing with Robust Code Representations from srcML \- Educational Data Mining, accessed January 13, 2026, [https://educationaldatamining.org/EDM2025/proceedings/2025.EDM.short-papers.83/index.html](https://educationaldatamining.org/EDM2025/proceedings/2025.EDM.short-papers.83/index.html)  
30. TRACEALIGN \-- Tracing the Drift: Attributing Alignment Failures to Training-Time Belief Sources in LLMs \- arXiv, accessed January 13, 2026, [https://arxiv.org/pdf/2508.02063?](https://arxiv.org/pdf/2508.02063)  
31. McMining: Automated Discovery of Misconceptions in Student Code \- arXiv, accessed January 13, 2026, [https://arxiv.org/pdf/2510.08827](https://arxiv.org/pdf/2510.08827)  
32. The reproducibility of research and the misinterpretation of p-values \- PubMed Central \- NIH, accessed January 13, 2026, [https://pmc.ncbi.nlm.nih.gov/articles/PMC5750014/](https://pmc.ncbi.nlm.nih.gov/articles/PMC5750014/)  
33. Comparing ChatGPT Feedback and Peer Feedback in Shaping Students' Evaluative Judgement of Statistical Analysis: A Case Study \- PubMed Central, accessed January 13, 2026, [https://pmc.ncbi.nlm.nih.gov/articles/PMC12292746/](https://pmc.ncbi.nlm.nih.gov/articles/PMC12292746/)  
34. Is perceiving another's error detrimental to learning from corrective feedback?, accessed January 13, 2026, [https://www.tandfonline.com/doi/abs/10.1080/23311908.2020.1717052](https://www.tandfonline.com/doi/abs/10.1080/23311908.2020.1717052)  
35. Extending the Hint Factory for the assis- tance dilemma: A novel, data-driven Help- Need Predictor for proactive problem-solving, accessed January 13, 2026, [https://www.cs.ubc.ca/\~conati/522/532b-2024/papers/NewSping2024/ExtendingHintFactoryManiktala2020.pdf](https://www.cs.ubc.ca/~conati/522/532b-2024/papers/NewSping2024/ExtendingHintFactoryManiktala2020.pdf)  
36. Harmful Labelling. Why Shouldn't We Diagnose Others? \- Multi.Life, accessed January 13, 2026, [https://multi.life/en/article/harmful-labelling-why-shouldnt-we-diagnose-others](https://multi.life/en/article/harmful-labelling-why-shouldnt-we-diagnose-others)  
37. The Inequality of Labeling, Labels that Disable | by Robert Vergeson | Medium, accessed January 13, 2026, [https://robertvergeson1.medium.com/the-inequality-of-labeling-labels-that-disable-824e606378fb](https://robertvergeson1.medium.com/the-inequality-of-labeling-labels-that-disable-824e606378fb)  
38. A mixed method examination: how stigma experienced by autistic adults relates to metrics of social identity and social functioning \- NIH, accessed January 13, 2026, [https://pmc.ncbi.nlm.nih.gov/articles/PMC10640997/](https://pmc.ncbi.nlm.nih.gov/articles/PMC10640997/)  
39. Attachment to Diagnostic Labels: Social Media, Over Identification, and Self-Efficacy for Personal Recovery \- Eagle Scholar, accessed January 13, 2026, [https://scholar.umw.edu/cgi/viewcontent.cgi?article=1614\&context=student\_research](https://scholar.umw.edu/cgi/viewcontent.cgi?article=1614&context=student_research)  
40. Our Social Packaging: How Labels in Society Affect our Perceptions of Ourselves and What This Implicates for the Overdiagnosis a \- Scholar Commons, accessed January 13, 2026, [https://scholarcommons.sc.edu/cgi/viewcontent.cgi?article=1621\&context=senior\_theses](https://scholarcommons.sc.edu/cgi/viewcontent.cgi?article=1621&context=senior_theses)  
41. Am I Wrong, or Is the Autograder Wrong? Effects of AI Grading Mistakes on Learning | Request PDF \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/373829125\_Am\_I\_Wrong\_or\_Is\_the\_Autograder\_Wrong\_Effects\_of\_AI\_Grading\_Mistakes\_on\_Learning](https://www.researchgate.net/publication/373829125_Am_I_Wrong_or_Is_the_Autograder_Wrong_Effects_of_AI_Grading_Mistakes_on_Learning)  
42. EFFECT OF A METACOGNITIVE INTERVENTION ON COGNITIVE HEURISTIC USE DURING DIAGNOSTIC REASONING by Velma Lucille Payne Bachelor o \- D-Scholarship@Pitt, accessed January 13, 2026, [https://d-scholarship.pitt.edu/7305/1/Velma\_L\_Payne\_April\_2011.pdf](https://d-scholarship.pitt.edu/7305/1/Velma_L_Payne_April_2011.pdf)  
43. Improving Large Language Models' Handling of Contradictions: Fostering Epistemic Humility | by Micheal Bee | Medium, accessed January 13, 2026, [https://medium.com/@mbonsign/improving-large-language-models-handling-of-contradictions-fostering-epistemic-humility-629fca6abcf0](https://medium.com/@mbonsign/improving-large-language-models-handling-of-contradictions-fostering-epistemic-humility-629fca6abcf0)  
44. Beyond overconfidence: Embedding curiosity and humility for ethical medical AI \- PMC, accessed January 13, 2026, [https://pmc.ncbi.nlm.nih.gov/articles/PMC12768375/](https://pmc.ncbi.nlm.nih.gov/articles/PMC12768375/)  
45. Distinguishing the Knowable from the Unknowable with Language Models, accessed January 13, 2026, [https://kempnerinstitute.harvard.edu/research/deeper-learning/distinguishing-the-knowable-from-the-unknowable-with-language-models/](https://kempnerinstitute.harvard.edu/research/deeper-learning/distinguishing-the-knowable-from-the-unknowable-with-language-models/)  
46. Rebuilding Epistemic Trust in an AI-Mediated Internet \- The Decision Lab, accessed January 13, 2026, [https://thedecisionlab.com/big-problems/rebuilding-epistemic-trust-in-an-ai-mediated-internet](https://thedecisionlab.com/big-problems/rebuilding-epistemic-trust-in-an-ai-mediated-internet)  
47. AI-Aided Decision-Making in Education: Uncertainty, Explainability, and Human Responsibility | Request PDF \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/397415139\_AI-Aided\_Decision-Making\_in\_Education\_Uncertainty\_Explainability\_and\_Human\_Responsibility](https://www.researchgate.net/publication/397415139_AI-Aided_Decision-Making_in_Education_Uncertainty_Explainability_and_Human_Responsibility)  
48. Teaching by Mistake: Radiological AI Errors as Learning Tools, accessed January 13, 2026, [https://www.acr.org/Blogs/DSI/2025/teaching-by-mistake](https://www.acr.org/Blogs/DSI/2025/teaching-by-mistake)  
49. The Illusion of Smarter Learning: Why AI Can't Replace Real Education in Healthcare, accessed January 13, 2026, [https://dxrgroup.com/the-illusion-of-smarter-learning-why-ai-cant-replace-real-education-in-healthcare/](https://dxrgroup.com/the-illusion-of-smarter-learning-why-ai-cant-replace-real-education-in-healthcare/)  
50. The Divine Paradox in Clinical Practice: Presence, Absence, and the Therapeutic Encounter, accessed January 13, 2026, [https://www.scivisionpub.com/pdfs/the-divine-paradox-in-clinical-practice-presence-absence-and-the-therapeutic-encounter-3876.pdf](https://www.scivisionpub.com/pdfs/the-divine-paradox-in-clinical-practice-presence-absence-and-the-therapeutic-encounter-3876.pdf)  
51. Sensitivity and Specificity: A Complete Guide \- DataCamp, accessed January 13, 2026, [https://www.datacamp.com/tutorial/sensitivity-specificity-complete-guide](https://www.datacamp.com/tutorial/sensitivity-specificity-complete-guide)  
52. Machine Learning for Synthetic Data Generation: A Review \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2302.04062v9](https://arxiv.org/html/2302.04062v9)  
53. Synthetic data generator for student data serving learning analytics: A comparative study \- Learning Letters, accessed January 13, 2026, [https://learningletters.org/index.php/learn/article/download/4/19/212](https://learningletters.org/index.php/learn/article/download/4/19/212)  
54. Synthetic data generator for student data serving learning analytics: A comparative study, accessed January 13, 2026, [https://www.researchgate.net/publication/373558351\_Synthetic\_data\_generator\_for\_student\_data\_serving\_learning\_analytics\_A\_comparative\_study](https://www.researchgate.net/publication/373558351_Synthetic_data_generator_for_student_data_serving_learning_analytics_A_comparative_study)  
55. Towards Valid Student Simulation with Large Language Models \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2601.05473v1](https://arxiv.org/html/2601.05473v1)  
56. Large Language Models for In-Context Student Modeling: Synthesizing Student's Behavior in Visual Programming \- Educational Data Mining, accessed January 13, 2026, [https://educationaldatamining.org/edm2024/proceedings/2024.EDM-short-papers.31/index.html](https://educationaldatamining.org/edm2024/proceedings/2024.EDM-short-papers.31/index.html)  
57. Investigating Student Mistakes in Introductory Data Science Programming \- DSpace@MIT, accessed January 13, 2026, [https://dspace.mit.edu/bitstream/handle/1721.1/154064/3626252.3630884.pdf?sequence=1\&isAllowed=y](https://dspace.mit.edu/bitstream/handle/1721.1/154064/3626252.3630884.pdf?sequence=1&isAllowed=y)  
58. AskNow: An LLM-powered Interactive System for Real-Time Question Answering in Large-Scale Classrooms \- arXiv, accessed January 13, 2026, [https://arxiv.org/html/2511.01248v1](https://arxiv.org/html/2511.01248v1)  
59. Thinking Like a Student: AI-Supported Reflective Planning in a Theory-Intensive Computer Science Course \- ResearchGate, accessed January 13, 2026, [https://www.researchgate.net/publication/397280077\_Thinking\_Like\_a\_Student\_AI-Supported\_Reflective\_Planning\_in\_a\_Theory-Intensive\_Computer\_Science\_Course](https://www.researchgate.net/publication/397280077_Thinking_Like_a_Student_AI-Supported_Reflective_Planning_in_a_Theory-Intensive_Computer_Science_Course)  
60. AI Meets Three-Dimensional Learning: Guiding Scientific Thinking With New Features of NotebookLM | NSTA \- National Science Teachers Association, accessed January 13, 2026, [https://www.nsta.org/blog/ai-meets-three-dimensional-learning-guiding-scientific-thinking-new-features-notebooklm](https://www.nsta.org/blog/ai-meets-three-dimensional-learning-guiding-scientific-thinking-new-features-notebooklm)  
61. Machine Learning and Generative AI in Learning Analytics for Higher Education: A Systematic Review of Models, Trends, and Challenges \- MDPI, accessed January 13, 2026, [https://www.mdpi.com/2076-3417/15/15/8679](https://www.mdpi.com/2076-3417/15/15/8679)  
62. Mount Sinai: AI That Asks Its Own Questions Could Transform Clinical Diagnostics \- HIT Leaders and News, accessed January 13, 2026, [https://us.hitleaders.news/academic-research/49781/mount-sinai-ai-that-asks-its-own-questions-could-transform-clinical-diagnostics/](https://us.hitleaders.news/academic-research/49781/mount-sinai-ai-that-asks-its-own-questions-could-transform-clinical-diagnostics/)  
63. Beyond Tools: Generative AI as Epistemic Infrastructure in Education \- arXiv, accessed January 13, 2026, [https://arxiv.org/pdf/2504.06928](https://arxiv.org/pdf/2504.06928)  
64. Fall 2025: Angela Pecora | The Elon Journal, accessed January 13, 2026, [https://www.elon.edu/u/academics/communications/journal/archive/fall-2025/fall-2025-angela-pecora/](https://www.elon.edu/u/academics/communications/journal/archive/fall-2025/fall-2025-angela-pecora/)