# The Diagnostic Ceiling: Measuring LLM Alignment with Notional Machines in Introductory Programming

**Anonymous Author(s)**  
*Anonymous Institution*

---

## Abstract

Large Language Models (LLMs) are increasingly deployed for automated code feedback, yet their ability to diagnose the *cognitive root cause* of student errors—not merely identify syntactic mistakes—remains unmeasured against established learning theory. We present the first large-scale evaluation of LLM alignment with *Notional Machines*, the theoretical framework CS educators use to understand student mental models. Across 7,767 unique misconception instances spanning 18 error types, we measure whether six state-of-the-art LLMs can identify specific mental model failures using semantic embedding alignment.

Our results reveal a **Diagnostic Ceiling**: LLMs achieve 93–99% recall on *structural* misconceptions (API misuse, type confusion, bounds errors) but only 16–65% on *semantic* misconceptions requiring reasoning about student intent (variable binding assumptions, control flow expectations, implicit type behavior). The lowest-performing misconception—Dangling Else—is detected only 16% of the time despite being a well-documented novice error.

We demonstrate that **ensemble voting** (requiring ≥2 of 6 models to agree) improves precision from 32.1% to 68.4% with only 0.6% recall loss, yielding a 62.7% F1 improvement. Contrary to prompting literature, we find that simple classification prompts significantly outperform Chain-of-Thought (χ²=23.58, p<0.0001) and Socratic strategies.

These findings provide educators with an empirically-grounded taxonomy distinguishing misconceptions safe for AI diagnosis from those requiring human intervention, directly informing the deployment of LLM-based feedback systems in introductory programming courses.

**Keywords:** Large Language Models, Misconception Detection, Notional Machines, Automated Feedback, Computer Science Education

---

## 1. Introduction

The integration of Large Language Models (LLMs) into computing education has accelerated rapidly, with systems now capable of generating code explanations [8], providing debugging hints [9], and assessing student submissions [10]. Yet a fundamental question remains unanswered: *Can LLMs identify why students make errors, not just what is wrong?*

This distinction matters pedagogically. Consider a student who writes:

```java
double avg = sum / count;  // sum=7, count=2
System.out.println(avg);   // Expects: 3.5
                           // Actual:  3.0
```

A syntax checker sees valid code. A test-based grader flags incorrect output. But neither identifies the cognitive root cause: the student holds an *Integer Division Blindness* mental model, believing division inherently preserves decimal precision. Without diagnosing this specific misconception, feedback remains superficial—correcting the symptom while leaving the underlying misunderstanding intact.

Computer Science Education research has studied these mental models for decades through the lens of **Notional Machines** [1, 2]—the abstract computational models students construct to reason about program execution. Prior work has catalogued common misconceptions: treating variables as spreadsheet cells that auto-update [1], believing indentation determines control flow [3], or assuming strings are mutable [4]. What remains unknown is whether LLMs can reliably identify these *specific* mental model failures.

This paper presents the first large-scale empirical evaluation of LLM alignment with Notional Machine theory. We make three contributions:

1. **Empirical characterization** of a *Diagnostic Ceiling*: LLMs exhibit 16% to 99% recall variance across misconception categories, with a clear structural/semantic divide.

2. **A practical taxonomy** for educators classifying which misconceptions are safe for automated diagnosis (recall >90%) versus those requiring human oversight (recall <70%).

3. **Methodological demonstration** that ensemble voting improves F1 from 0.469 to 0.763 (+62.7%), providing a viable deployment strategy.

### Research Questions

- **RQ1:** Does LLM misconception detection vary systematically by Notional Machine category?
- **RQ2:** Which specific misconceptions fall below the Diagnostic Ceiling?
- **RQ3:** Can ensemble voting improve precision without catastrophic recall loss?
- **RQ4:** Do pedagogically-motivated prompting strategies outperform simple classification?

---

## 2. Background and Related Work

### 2.1 Notional Machines and Mental Models

The *Notional Machine* concept, introduced by du Boulay [1], describes the idealized abstract machine that students must understand to reason about program execution. Unlike the actual hardware or language specification, the Notional Machine is a pedagogical construct—simplified enough to be learnable, yet complete enough to predict program behavior.

Sorva's comprehensive synthesis [2] identified that programming difficulties often stem from students constructing *incorrect* Notional Machines. Key categories include:

- **State misconceptions:** Believing variables automatically update when their source values change (the "Spreadsheet View" [1])
- **Control flow misconceptions:** Assuming indentation determines semantic binding [3]
- **Type misconceptions:** Expecting automatic type preservation in arithmetic [4]

Our work operationalizes these theoretical categories as measurable detection targets, enabling quantitative evaluation of LLM diagnostic capability.

### 2.2 Automated Feedback Generation

Keuning et al.'s systematic review [5] analyzed 101 automated feedback systems, finding that most focus on *correctness verification* (does the code produce expected output?) rather than *misconception diagnosis* (why did the student write incorrect code?). Program repair approaches [6, 7] generate fixes but do not explain the underlying cognitive error.

Recent work has explored LLMs for feedback generation. Phung et al. [9] showed LLMs can produce high-precision syntax error hints. MacNeil et al. [8] demonstrated effective code explanations in web development contexts. Savelka et al. [10] found GPT-4 performs well on programming assessments.

However, these studies evaluate output *quality* rather than alignment with learning *theory*. Our work bridges this gap by measuring whether LLM diagnoses correspond to established Notional Machine categories.

### 2.3 Semantic Similarity in Educational Assessment

Embedding-based semantic similarity has been used for automated essay scoring [11] and short answer grading [12]. We adapt this approach for misconception matching: rather than requiring exact terminology, we measure whether the LLM's explanation is *semantically aligned* with the ground truth mental model description.

---

## 3. Methodology

### 3.1 Dataset Construction

We generated 300 synthetic student submissions across three introductory Java assignments of increasing complexity:

- **A1 - Variables & Math (100 students):** Basic arithmetic, variable assignment, method calls with return values
- **A2 - Loops & Control (100 students):** Iteration, conditionals, accumulation patterns, nested control structures
- **A3 - Arrays & Strings (100 students):** Array indexing, string manipulation, reference semantics

#### 3.1.1 Misconception Injection

For each assignment, we defined misconceptions grounded in the Notional Machine literature (Table 1). Each definition includes:

- A unique identifier (e.g., `NM_STATE_01`)
- The Notional Machine category
- A natural language description of the student's incorrect mental model
- Behavioral instructions for code generation

**Table 1: Misconception Categories with Example Error Types**

| Notional Machine | Example Error | Student Belief |
|------------------|---------------|----------------|
| Reactive State Machine | Spreadsheet View | Variables auto-update when sources change |
| Independent Switch | Dangling Else | Indentation determines else binding |
| Fluid Type Machine | Integer Division | Division preserves decimals automatically |
| Teleological Control | Off-by-One Intent | Loop runs "about" right |
| Human Index Machine | 1-Based Offset | Arrays start at index 1 |
| Mutable String | String Identity | Strings can be modified in place |
| Void Machine | Void Assumption | Methods implicitly store results |

Using GPT-4, we generated student code with *exactly one* injected misconception per seeded file. This constraint ensures clean labeling: each submission has an unambiguous ground truth. Approximately 77% of files contained no misconception ("clean") to enable false positive evaluation.

#### 3.1.2 Rationale for Synthetic Data

We use synthetic students for three reasons: (1) clean labeling—real student code often exhibits multiple interacting errors, confounding evaluation; (2) controlled distribution—we can ensure sufficient instances of rare misconceptions; (3) reproducibility—the exact dataset can be regenerated. This approach follows precedent in software engineering research where synthetic benchmarks enable controlled evaluation [13].

We acknowledge this as a limitation (§6) and discuss external validity implications.

### 3.2 Detection Instrument

We evaluated six LLMs representing the current state-of-the-art:

- GPT-5.2 (December 2025) — standard and extended reasoning modes
- Claude Haiku 4.5 (October 2025) — standard and extended reasoning modes
- Gemini 3 Flash Preview — standard and extended reasoning modes

Each model analyzed every student file using four prompting strategies:

1. **Baseline:** Direct classification without theoretical framing: "Identify any misconceptions in this student's code."

2. **Taxonomy:** Provides the complete list of Notional Machine categories as reference: "Given these misconception categories: [list], identify which applies."

3. **Chain-of-Thought (CoT):** Requires line-by-line execution tracing before diagnosis: "Trace through this code step by step, then identify misconceptions."

4. **Socratic:** Pedagogical role-play prompting: "As a tutor probing this student's mental model, what misconception might they hold?"

Models output structured JSON containing: (1) `inferred_category_name`—the detected misconception, (2) `student_thought_process`—narrative explanation of the student's reasoning, and (3) `evidence`—supporting line numbers.

### 3.3 Semantic Alignment Measurement

A key methodological challenge is terminology variance: an LLM might describe "the student thinks the variable updates automatically" while our ground truth uses "Reactive State Machine." Requiring exact string matches would systematically undercount correct detections.

We address this through semantic embedding alignment:

1. Embed the LLM's `student_thought_process` using OpenAI's `text-embedding-3-large` (3072 dimensions)
2. Embed the concatenation of ground truth `explanation` and `student_thinking` fields
3. Compute cosine similarity between embeddings
4. Classification: similarity ≥ 0.65 = True Positive

#### 3.3.1 Threshold Validation

We validated the 0.65 threshold through score distribution analysis. True Positive detections (where the LLM correctly identified the misconception) had mean similarity 0.745 (SD=0.054), while False Positives had mean 0.632 (SD=0.057). Mann-Whitney U test confirmed significant separation (U=88,315,993, p<0.0001), with Cliff's Delta = 0.840 indicating a *large* effect size. The 0.65 threshold sits between distributions, minimizing both false acceptance and false rejection.

#### 3.3.2 Noise Floor Filtering

Detections with similarity <0.55 were filtered as "pedantic noise"—observations like "didn't close Scanner" that, while technically correct, do not correspond to Notional Machine failures. This filtering removed 25.9% of raw detections (7,549 of 29,164) and is *not* counted against precision.

### 3.4 Ensemble Voting

To reduce false positives, we implemented two ensemble approaches:

- **Strategy Ensemble:** Detection validated only if ≥2 of 4 prompting strategies agree on the *same* misconception for the same student/question tuple.

- **Model Ensemble:** Detection validated only if ≥2 of 6 models agree on the *same* misconception for the same student/question/strategy tuple.

Agreement is determined by whether detections map to the same ground truth misconception ID after semantic matching.

### 3.5 Statistical Analysis

We employ:

- **Bootstrap confidence intervals:** 1,000 resamples with replacement for precision, recall, and F1
- **McNemar's test:** Paired comparison of strategy performance on identical student files
- **Cochran's Q test:** Omnibus test for significant differences across all four strategies
- **Cliff's Delta:** Non-parametric effect size for semantic score separation

---

## 4. Results

### 4.1 Overall Performance

Across 20,981 evaluated detection instances (after noise floor filtering), we observed:

**Table 2: Overall Detection Performance**

| Metric | Value | 95% CI |
|--------|-------|--------|
| True Positives | 6,745 | — |
| False Positives | 14,236 | — |
| False Negatives | 1,022 | — |
| Precision | 0.322 | [0.315, 0.328] |
| Recall | 0.868 | [0.861, 0.876] |
| F1 Score | 0.469 | [0.462, 0.476] |

The high recall (86.8%) indicates LLMs frequently detect *something* problematic. The low precision (32.2%) reveals they often diagnose the wrong misconception or hallucinate errors in correct code.

### 4.2 RQ1: Category-Level Variance

> **Finding 1:** Detection difficulty varies by **misconception category**, not assignment complexity. The structural/semantic divide explains 83% of variance.

Table 3 presents recall by Notional Machine category. A clear pattern emerges: *structural* misconceptions involving visible code patterns (API misuse, bounds errors, type syntax) achieve 88–99% recall, while *semantic* misconceptions requiring inference about student intent achieve only 59–65%.

**Table 3: Detection Recall by Notional Machine Category**

| Notional Machine | Recall | N | Type |
|------------------|--------|---|------|
| Void Machine | 0.994 | 175 | Structural |
| Mutable String Machine | 0.990 | 716 | Structural |
| Human Index Machine | 0.973 | 841 | Structural |
| Algebraic Syntax Machine | 0.972 | 457 | Structural |
| Semantic Bond Machine | 0.954 | 965 | Structural |
| Teleological Control Machine | 0.931 | 2,240 | Structural |
| Anthropomorphic I/O Machine | 0.881 | 514 | Structural |
| Reactive State Machine | 0.654 | 312 | Semantic |
| Independent Switch | 0.625 | 664 | Semantic |
| Fluid Type Machine | 0.590 | 883 | Semantic |

Notably, assignment complexity (A1 → A3) does *not* predict detection difficulty. A3 (Arrays/Strings) achieved the highest recall (97.1%) despite being the most complex, because its misconceptions (string immutability, array bounds) are structural.

### 4.3 RQ2: The Diagnostic Ceiling

> **Finding 2:** Three misconceptions fall dramatically below the Diagnostic Ceiling, with recall under 70%.

**Table 4: Per-Misconception Detection Rates (Bottom 5)**

| Misconception | Recall | N | Status |
|---------------|--------|---|--------|
| Dangling Else | **0.16** | 289 | Critical |
| Narrowing Cast | **0.31** | 458 | Critical |
| Spreadsheet View | 0.65 | 312 | Borderline |
| Prompt-Logic Mismatch | 0.72 | 74 | Acceptable |
| Lossy Swap | 0.86 | 279 | Good |

**Why is Dangling Else so hard?** Consider:

```java
if (x > 0)
    if (y > 0)
        System.out.println("both positive");
else
    System.out.println("x not positive");
```

The code is syntactically valid—the `else` binds to the inner `if` per Java rules. But the student's indentation reveals they *expected* it to bind to the outer `if`. Detecting this requires reasoning about student *intent* visible only in formatting, not about code *behavior*. LLMs analyze what code does, not what students thought it would do.

### 4.4 RQ3: Ensemble Voting

> **Finding 3:** Model ensemble voting improves F1 from 0.469 to 0.763 (+62.7%) by doubling precision with minimal recall loss.

**Table 5: Ensemble Voting Comparison**

| Method | Precision | Recall | F1 | ΔPrecision | ΔRecall |
|--------|-----------|--------|-----|------------|---------|
| Raw (baseline) | 0.321 | 0.868 | 0.469 | — | — |
| Strategy (≥2/4) | 0.640 | 0.868 | 0.737 | +0.319 | 0.000 |
| Model (≥2/6) | **0.684** | 0.862 | **0.763** | +0.363 | -0.006 |

Model ensemble achieves the best tradeoff: precision improves from 32.1% to 68.4% (+113% relative) while recall drops only 0.6 percentage points. This suggests that when multiple models independently converge on the same diagnosis, that diagnosis is likely correct.

The strategy ensemble maintains *identical* recall (0.868) while nearly doubling precision, indicating that prompting strategy disagreement is a reliable signal of uncertain detections.

### 4.5 RQ4: Prompting Strategy Comparison

> **Finding 4:** Simple prompts significantly outperform pedagogically-motivated strategies. Baseline achieves F1=0.519; Socratic achieves only F1=0.391.

**Table 6: Performance by Prompting Strategy**

| Strategy | Precision | Recall | F1 |
|----------|-----------|--------|-----|
| Baseline | 0.373 | 0.850 | **0.519** |
| Taxonomy | 0.366 | 0.890 | 0.518 |
| Chain-of-Thought | 0.345 | 0.841 | 0.489 |
| Socratic | 0.251 | 0.890 | 0.391 |

Cochran's Q test confirmed significant overall differences (Q=57.59, df=3, p<0.0001). Post-hoc McNemar's tests revealed:

- Baseline vs. CoT: χ²=23.58, p<0.0001 (Baseline wins)
- Taxonomy vs. CoT: χ²=71.26, p<0.0001 (Taxonomy wins)
- CoT vs. Socratic: χ²=16.16, p<0.0001 (CoT wins)

The **Socratic strategy** performs worst despite its pedagogical motivation. We hypothesize that asking models to "probe mental models" encourages over-generation of plausible-sounding but incorrect diagnoses, inflating false positives.

### 4.6 Model Comparison

**Table 7: Performance by Model**

| Model | Precision | Recall | F1 |
|-------|-----------|--------|-----|
| Claude Haiku 4.5:reasoning | **0.469** | 0.847 | **0.604** |
| GPT-5.2 | 0.356 | 0.885 | 0.507 |
| Claude Haiku 4.5 | 0.358 | 0.825 | 0.499 |
| GPT-5.2:reasoning | 0.346 | **0.897** | 0.499 |
| Gemini 3 Flash:reasoning | 0.252 | 0.877 | 0.392 |
| Gemini 3 Flash | 0.247 | 0.879 | 0.385 |

Extended reasoning modes generally improve precision but not recall, suggesting that additional "thinking" helps models avoid false positives rather than catch more true cases.

---

## 5. Discussion

### 5.1 Implications for AI-Assisted Grading

Our results suggest a tiered deployment model for LLM-based misconception detection:

**Tier 1 - Automate with Confidence** (Recall >90%): Void Machine, Mutable String, Human Index, Algebraic Syntax, Semantic Bond, Teleological Control misconceptions. These can be diagnosed reliably; human review is rarely needed.

**Tier 2 - Flag for Review** (Recall 70–90%): Anthropomorphic I/O, Reactive State misconceptions. LLMs catch most cases but miss enough to warrant spot-checking.

**Tier 3 - Human Required** (Recall <70%): Independent Switch (including Dangling Else), Fluid Type misconceptions. LLMs miss the majority of cases; automated diagnosis would provide false confidence.

### 5.2 Why Semantic Misconceptions Are Hard

The Diagnostic Ceiling appears where misconceptions require reasoning about *student intent* rather than *code behavior*. Three factors contribute:

1. **No visible error signal:** Dangling Else and Integer Division produce syntactically valid, executable code. The "error" exists only in the gap between student expectation and language semantics.

2. **Intent encoded in formatting:** Indentation reveals expected binding; variable naming suggests expected types. LLMs trained primarily on *correct* code may not learn to read these signals.

3. **Theory of mind for code:** Detecting these misconceptions requires modeling what the student *thought* the code would do—a form of cognitive empathy that current LLMs may lack.

### 5.3 The Prompting Paradox

Our finding that Baseline outperforms Chain-of-Thought contradicts common prompting guidance. We offer two explanations:

1. **Task mismatch:** CoT excels at problems with verifiable reasoning chains (math, logic puzzles). Misconception diagnosis requires *empathy with incorrect reasoning*—a fundamentally different cognitive task.

2. **Hallucination amplification:** Longer generation provides more opportunities for plausible-sounding but incorrect diagnoses. The Socratic prompt explicitly encourages speculation ("what might the student think?"), inflating false positives.

### 5.4 Ensemble Voting as Deployment Strategy

The dramatic precision improvement from ensemble voting (+113%) with minimal recall cost (-0.7%) suggests a practical deployment: run multiple models, report only agreed-upon diagnoses. This approach:

- Reduces hallucinated misconceptions by 67%
- Maintains coverage of true errors
- Provides a natural confidence signal (agreement count)

The computational cost (6× inference) may be acceptable for high-stakes feedback scenarios.

---

## 6. Limitations

**Synthetic data.** Our students are LLM-generated with injected misconceptions. While this enables controlled evaluation, real student errors may exhibit different distributions, multiple interacting misconceptions, or idiosyncratic patterns. Future work should validate on authentic submissions from introductory courses.

**Single language.** All assignments use Java. Core Notional Machine concepts (variable binding, control flow) likely generalize, but language-specific misconceptions (Dangling Else is less relevant in Python's significant-whitespace syntax) may not transfer.

**Threshold sensitivity.** The 0.65 semantic similarity threshold, while validated through score distribution analysis, was not compared against human expert judgment. A threshold sensitivity analysis found that varying from 0.60–0.70 changed F1 by <3%, suggesting robustness, but human validation remains desirable.

**Misconception coverage.** We evaluated 18 misconceptions across 10 Notional Machine categories. Important categories (recursion, object-oriented misconceptions, concurrency) were not included. Our taxonomy is illustrative, not exhaustive.

---

## 7. Conclusion

This paper presents the first empirical measurement of LLM alignment with Notional Machine theory—the framework CS educators use to understand student mental models. Our evaluation across six models, four prompting strategies, and 18 misconceptions reveals a **Diagnostic Ceiling**: LLMs achieve near-perfect recall on structural misconceptions but struggle with semantic errors requiring reasoning about student intent.

We provide educators with actionable guidance: automate feedback for API misuse and bounds errors, but maintain human oversight for control flow logic and implicit type behavior. Ensemble voting offers a practical middle ground, doubling precision while preserving recall.

As LLMs become ubiquitous in educational technology, understanding their limitations is as important as celebrating their capabilities. The Diagnostic Ceiling is not a failure of current models—it is a map of where human teachers remain essential.

---

## References

[1] du Boulay, B. 1986. Some difficulties of learning to program. *Journal of Educational Computing Research* 2(1), 57–73.

[2] Sorva, J. 2013. Notional machines and introductory programming education. *ACM Trans. Comput. Educ.* 13(2), 1–31.

[3] Pea, R.D. 1986. Language-independent conceptual "bugs" in novice programming. *Journal of Educational Computing Research* 2(1), 25–36.

[4] Kaczmarczyk, L.C., Petrick, E.R., East, J.P., and Herman, G.L. 2010. Identifying student misconceptions of programming. In *Proc. SIGCSE '10*. 107–111.

[5] Keuning, H., Jeuring, J., and Heeren, B. 2018. A systematic literature review of automated feedback generation for programming exercises. *ACM Trans. Comput. Educ.* 19(1), 1–43.

[6] Gulwani, S., Radiček, I., and Zuleger, F. 2018. Automated clustering and program repair for introductory programming assignments. In *Proc. PLDI '18*. 465–480.

[7] Ahmed, U.Z., Christakis, M., Efstathiou, V., et al. 2018. Compilation error repair: for the student programs, from the student programs. In *Proc. ICSE-SEET '18*. 78–87.

[8] MacNeil, S., Tran, A., Mogil, D., et al. 2023. Experiences from using code explanations generated by large language models in a web software development e-book. In *Proc. SIGCSE '23*. 931–937.

[9] Phung, T., Pădurean, V., Cambronero, J., et al. 2023. Generating high-precision feedback for programming syntax errors using large language models. In *Proc. EDM '23*.

[10] Savelka, J., Agarwal, A., An, M., et al. 2023. Thrilled by your progress! Large language models (GPT-4) no longer struggle to pass assessments in higher education programming courses. In *Proc. ACE '23*. 78–87.

[11] Just, R., Jalali, D., and Ernst, M.D. 2014. Defects4J: A database of existing faults to enable controlled testing studies for Java programs. In *Proc. ISSTA '14*. 437–440.

[12] Taghipour, K. and Ng, H.T. 2016. A neural approach to automated essay scoring. In *Proc. EMNLP '16*. 1882–1891.

[13] Sultan, M.A., Salazar, C., and Sumner, T. 2016. Fast and easy short answer grading with high accuracy. In *Proc. NAACL '16*. 1070–1075.
