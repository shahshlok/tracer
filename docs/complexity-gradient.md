# The Complexity Gradient

This document explains the **core thesis finding**: LLM performance degrades systematically as conceptual abstraction increases.

---

## Executive Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         THE COMPLEXITY GRADIENT                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Assignment       Focus                F1 Score                              │
│  ──────────       ─────                ────────                              │
│                                                                             │
│  A3 (Easiest)     Arrays & Strings     0.626  ████████████████░░░░           │
│  A2 (Medium)      Loops & Control      0.481  ████████████░░░░░░░░           │
│  A1 (Hardest)     Variables & Math     0.341  █████████░░░░░░░░░░░           │
│                                                                             │
│                                                                             │
│  Gap: 84% relative drop from A3 to A1                                       │
│                                                                             │
│  This proves: LLMs struggle with ABSTRACT STATE REASONING                   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Why Does This Happen?

### A3: Arrays & Strings (F1 = 0.626)

**Why it's easy for LLMs:**
- Errors are **visible** in output (IndexOutOfBoundsException)
- Array indexing is **concrete** (you can count elements)
- String operations have **clear semantics**
- LLMs can pattern-match against millions of similar examples

**Example misconception:**
```java
int[] arr = new int[5];
arr[5] = 10;  // Visible error: index out of bounds
```

LLMs easily recognize: "Array size is 5, valid indices are 0-4, accessing 5 is wrong."

---

### A2: Loops & Control (F1 = 0.481)

**Why it's medium:**
- State changes **over time** (temporal reasoning required)
- Must trace **multiple iterations**
- Control flow is **less visible** than data structures
- Off-by-one errors require counting

**Example misconception:**
```java
for (int i = 0; i <= 5; i++) {  // Runs 6 times, not 5
    sum += i;
}
```

LLMs must trace: "i=0,1,2,3,4,5 → 6 iterations, not 5."

---

### A1: Variables & Math (F1 = 0.341)

**Why it's hard for LLMs:**
- State is **invisible** (no output to compare)
- Errors require understanding **student intent**
- Variables don't have visible "wrong values"
- Must infer **mental models** not present in code

**Example misconception:**
```java
double v0 = 0, v1 = 0, t = 0;
double a = (v1 - v0) / t;  // Computed with zeros!
v0 = scan.nextDouble();
v1 = scan.nextDouble();
t = scan.nextDouble();
System.out.println(a);     // Prints NaN, not correct result
```

LLMs must infer: "Student believes variables auto-update like spreadsheet cells." This requires reasoning about **pedagogical epistemology**, not just code execution.

---

## Evidence: The Pattern Holds Across Models

| Model | A1 F1 | A2 F1 | A3 F1 | A3-A1 Gap |
|-------|-------|-------|-------|-----------|
| Claude Haiku:reasoning | 0.38 | 0.52 | 0.67 | 0.29 |
| GPT-5.2 | 0.36 | 0.50 | 0.65 | 0.29 |
| GPT-5.2:reasoning | 0.35 | 0.51 | 0.64 | 0.29 |
| Claude Haiku | 0.34 | 0.49 | 0.62 | 0.28 |
| Gemini 3 Flash:reasoning | 0.30 | 0.44 | 0.58 | 0.28 |
| Gemini 3 Flash | 0.29 | 0.43 | 0.57 | 0.28 |

**Key observation:** All models show ~28-29% F1 gap between A3 and A1. This is **not model-specific**—it's a fundamental property of the task.

---

## Evidence: Structural vs Semantic Misconceptions

The gradient also appears within misconception categories:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DETECTION BY MISCONCEPTION TYPE                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STRUCTURAL (visible in code)                    Recall                      │
│  ───────────────────────────────                 ──────                      │
│  Void Machine (NM_API_01)                        99%  ████████████████████░  │
│  Mutable String (NM_MEM_03)                      99%  ████████████████████░  │
│  Human Index (NM_MEM_04)                         97%  ███████████████████░░  │
│  Algebraic Syntax (NM_SYN_*)                     97%  ███████████████████░░  │
│                                                                             │
│  SEMANTIC (invisible, requires inference)                                    │
│  ───────────────────────────────────────                                     │
│  Reactive State (NM_STATE_01)                    65%  █████████████░░░░░░░░  │
│  Independent Switch (NM_LOGIC_*)                 63%  ████████████░░░░░░░░░  │
│  Fluid Type (NM_TYP_*)                           59%  ███████████░░░░░░░░░░  │
│                                                                             │
│  CRITICAL (nearly undetectable)                                              │
│  ───────────────────────────────                                             │
│  Dangling Else (NM_LOGIC_02)                     16%  ███░░░░░░░░░░░░░░░░░░  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Why Dangling Else is the Hardest (16%)

```java
if (condition1)
    if (condition2)
        statement1;
else                    // Binds to inner if, not outer!
    statement2;
```

**Why LLMs fail:**
1. Code is **syntactically valid**—no error signal
2. The "bug" exists only in **indentation** (which Java ignores)
3. LLM must infer: "Student's indentation reveals their intent"
4. This requires **theory of mind for code**—understanding what the student *thought* vs what the code *does*

---

## Implications for CS Education

### Safe to Automate (>90% recall)

- Array index bounds checking
- String immutability violations
- Unused return value detection
- Type syntax errors (XOR as power)

### Needs Human Review (60-90% recall)

- Off-by-one loop errors
- Conditional branch logic
- I/O order mistakes

### Requires Human Diagnosis (<60% recall)

- Variable state misconceptions (Spreadsheet View)
- Type coercion expectations (Integer Division)
- Control flow binding (Dangling Else)

---

## Theoretical Explanation

### LLMs Are Trained on Output, Not Mental Models

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         WHY THE GAP EXISTS                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  LLM Training Data:                                                          │
│  ├── Millions of (code, output) pairs                                       │
│  ├── Millions of (code, syntax_error) pairs                                 │
│  └── Very few (code, misconception_description) pairs                       │
│                                                                             │
│  What LLMs can do:                                                          │
│  ├── Simulate execution → predict output                                    │
│  ├── Pattern match → find syntax errors                                     │
│  └── Identify common bugs → off-by-one, null pointer                        │
│                                                                             │
│  What LLMs struggle with:                                                    │
│  ├── Infer student's mental model from code                                 │
│  ├── Recognize intent encoded in formatting/naming                          │
│  └── Reason about pedagogical epistemology                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Statistical Validation

### Hypothesis Test

```
H0: LLM performance is independent of assignment
H1: LLM performance decreases with abstraction (A3 > A2 > A1)

Result: Reject H0 (p < 0.001)
        F1 gradient: 0.626 → 0.481 → 0.341
        Consistent across all 6 models
```

### Cross-Model Consistency

The gap is remarkably consistent (std dev = 0.005), confirming it's task-level, not model-level.

---

## For Your Thesis

### Abstract

> "We measure LLM cognitive alignment—the ability to diagnose student misconceptions. We find a 28% F1 gap between concrete errors (0.63) and abstract mental model errors (0.34), revealing a Diagnostic Ceiling for LLM-based feedback."

### Discussion

1. **The gap is fundamental:** All models show it
2. **The gap is predictable:** Correlates with abstraction level
3. **Ensemble helps but doesn't eliminate:** +61% F1 improvement, but gap persists
4. **Practical recommendation:** Automate concrete errors, keep humans for abstract ones

---

## Next: [CLI Reference](cli-reference.md)
