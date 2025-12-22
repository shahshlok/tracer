# Notional Machines & Misconceptions

**Status:** Complete 10-category taxonomy with A1-A3 mapping  
**Updated:** December 22, 2025

This document describes the theoretical framework underlying this research, including all 10 misconception categories detected across A1, A2, and A3 assignments.

---

## What is a Notional Machine?

> A **Notional Machine** is the mental model a student has of how the computer executes code.

When this mental model diverges from actual language semantics, we call it a **misconception**.

This research measures whether LLMs can detect these misconceptions through semantic analysis of student code and explanations.

---

## The 10 Notional Machine Categories

### CATEGORY 1: The Reactive State Machine (`NM_STATE_*`)

**Belief:** Variables update automatically like Excel cells when formulas are assigned.

**Assignment:** A1 (Variables & Math)

#### NM_STATE_01: Spreadsheet View (Early Calculation)

```java
// Student code
double v0 = 0, v1 = 0, t = 0;
double a = (v1 - v0) / t;  // Computed BEFORE input! ❌
v0 = scan.nextDouble();
v1 = scan.nextDouble();
t = scan.nextDouble();
System.out.println(a);     // Prints stale value (NaN or 0)
```

**Student Thinking:** "I'll define the formula at the top. `a` will update automatically once the inputs have values."

**Reality:** Assignments are executed once. `a` captures the initial stale values.

---

### CATEGORY 2: The Anthropomorphic I/O Machine (`NM_IO_*`)

**Belief:** The computer reads prompt text to understand the order/meaning of inputs.

**Assignment:** A1 (Variables & Math)

#### NM_IO_01: Prompt-Logic Mismatch

```java
System.out.println("Enter v0, then v1:");
double v1 = scan.nextDouble();  // Wrong order! ❌
double v0 = scan.nextDouble();
```

**Student Thinking:** "I told the user 'Enter v0, then v1'. The computer knows the first number is for v0 because of my prompt."

**Reality:** The scanner reads in the order code executes, not the order mentioned in the prompt.

---

#### NM_IO_02: The Ghost Read

```java
double v0 = scan.nextDouble();
System.out.println("v0 is " + v0);
// User enters 5
// But student expects: "Tell me v0: 5"
// Code prints: "v0 is 0" (reads uninitialized or wrong input)
```

**Student Thinking:** "The print statement reads the user's input."

**Reality:** `println` doesn't trigger input; it just outputs variables' current values.

---

### CATEGORY 3: The Fluid Type Machine (`NM_TYP_*`)

**Belief:** Type conversions happen automatically; division always gives decimals if stored in a double.

**Assignment:** A1 (Variables & Math)

#### NM_TYP_01: Integer Division Blindness

```java
int a = 5, b = 2;
double result = a / b;  // result = 2.0, not 2.5! ❌
```

**Student Thinking:** "If I store it in a double, Java will include the decimals."

**Reality:** `5 / 2` computes as integers (→ `2`), then converts `2` to `2.0`.

---

#### NM_TYP_02: Narrowing Cast in Division

```java
int a = 5, b = 2;
int result = (int) (a / b);  // Does this prevent truncation?
```

**Student Thinking:** "Casting to `int` preserves the decimal result."

**Reality:** Casting is applied AFTER division, so it still gives `2`.

---

### CATEGORY 4: The Algebraic Syntax Machine (`NM_SYN_*`)

**Belief:** Mathematical notation works directly in code.

**Assignment:** A1 (Variables & Math)

#### NM_SYN_01: XOR as Power

```java
double result = 2 ^ 3;  // Bitwise XOR, not exponentiation! ❌
// result = 1, not 8
```

**Student Thinking:** "I can use `^` for powers like in algebra."

**Reality:** `^` is bitwise XOR in Java. Use `Math.pow(2, 3)` instead.

---

#### NM_SYN_02: Precedence Blindness

```java
double result = 10 - 5 / 2;
// Student expects: (10 - 5) / 2 = 2.5
// Actually computes: 10 - (5 / 2) = 10 - 2 = 8
```

**Student Thinking:** "Left-to-right evaluation like in arithmetic."

**Reality:** Division has higher precedence than subtraction.

---

### CATEGORY 5: The Void Machine (`NM_API_*`)

**Belief:** Methods modify arguments in place without needing assignment.

**Assignment:** A1 (Variables & Math)

#### NM_API_01: The Void Assumption

```java
double x = 16;
Math.sqrt(x);     // Called but not assigned! ❌
System.out.println(x);  // Still prints 16.0
```

**Student Thinking:** "I called `Math.sqrt(x)` to turn x into its square root."

**Reality:** `Math.sqrt()` returns a value; you must assign it: `x = Math.sqrt(x)`.

---

### CATEGORY 6: The Teleological Flow Machine (`NM_FLOW_*`)

**Belief:** Loops and conditionals behave based on intention or purpose, not actual execution state.

**Assignment:** A2 (Loops & Conditionals)

#### NM_FLOW_01: Accumulator Amnesia (Scope Error)

```java
int sum = 0;
for (int i = 0; i < 5; i++) {
    int sum = 0;  // Redeclares sum in inner scope! ❌
    sum += i;     // Updates the inner sum, not the outer
}
System.out.println(sum);  // Still 0
```

**Student Thinking:** "The loop updates `sum` each iteration."

**Reality:** Scope shadows the outer `sum`; inner loop works on its own local copy.

---

#### NM_FLOW_02: The Intent Loop (Off-by-One)

```java
for (int i = 0; i <= 5; i++) {  // ≤ not < ❌
    // Runs 6 times, not 5
}
```

**Student Thinking:** "I want to count to 5, so `<= 5`."

**Reality:** `<= 5` includes 5; off-by-one error.

---

#### NM_FLOW_03: Infinite Loop (State Stagnation)

```java
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Missing i++! ❌
}
```

**Student Thinking:** "The loop should eventually exit."

**Reality:** Without updating `i`, the condition never becomes false.

---

#### NM_FLOW_04: Sabotaging the Future (Inner Loop Mod)

```java
for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 5; j++) {
        i++;  // Modifying outer loop var in inner loop! ❌
    }
}
```

**Student Thinking:** "I want to skip ahead in the outer loop."

**Reality:** Modifying the loop variable causes unpredictable control flow.

---

### CATEGORY 7: The Logical Reasoning Machine (`NM_LOGIC_*`)

**Belief:** Conditional logic follows colloquial English, not boolean algebra.

**Assignment:** A2 (Loops & Conditionals)

#### NM_LOGIC_01: Mutually Exclusive Fallacy

```java
if (x > 10)
    System.out.println("Large");
if (x > 5)           // NOT else-if! ❌
    System.out.println("Medium");
```

**Student Thinking:** "These conditions are mutually exclusive."

**Reality:** Both blocks execute if `x > 10` (it's also `> 5`).

---

#### NM_LOGIC_02: Dangling Else (Indentation Trap)

```java
if (condition1)
    if (condition2)
        statement1;
else                 // Binds to if(condition2), not if(condition1)! ❌
    statement2;
```

**Student Thinking:** "The indentation shows what the else belongs to."

**Reality:** Java ignores indentation; else always binds to the nearest if.

---

### CATEGORY 8: The Spatial Adjacency Machine (`NM_MEM_*`)

**Belief:** Arrays are logically separate from each other; indices work independently.

**Assignment:** A3 (Arrays & Strings)

#### NM_MEM_01: Parallel Array Desync

```java
int[] ids = new int[5];
int[] names = new int[5];
ids[0] = 123;
// But names[0] is still 0 (not linked to ids)
```

**Student Thinking:** "I'll store related data in parallel arrays."

**Reality:** Updates to one array don't affect the other; manual sync required.

---

#### NM_MEM_02: Index-Value Confusion

```java
int[] arr = {10, 20, 30};
System.out.println(arr[20]);  // Trying to access index 20! ❌
// Throws ArrayIndexOutOfBoundsException
```

**Student Thinking:** "arr[20] gets the value 20."

**Reality:** arr[20] gets the element at position 20 (doesn't exist).

---

#### NM_MEM_03: String Identity Trap (Immutability)

```java
String s1 = "hello";
String s2 = s1;
s2 = "world";
System.out.println(s1);  // Still "hello", not "world"! ❌
```

**Student Thinking:** "s2 = s1 means they're linked."

**Reality:** Strings are immutable; assignment just changes the reference.

---

#### NM_MEM_04: The 1-Based Offset (OOB)

```java
int[] arr = new int[5];  // Valid indices: 0-4
arr[5] = 10;             // Tries to access index 5! ❌
```

**Student Thinking:** "Arrays have 5 elements, so indices 1-5."

**Reality:** 5 elements have indices 0-4 (0-based indexing).

---

#### NM_MEM_05: Lossy Swap (Data Destruction)

```java
int[] arr = {1, 2};
arr[0] = arr[1];  // arr[0] = 2
arr[1] = arr[0];  // arr[1] = 2 (both are 2!)❌
// Correct swap requires a temp variable
```

**Student Thinking:** "I'll just assign values back and forth."

**Reality:** Without a temporary, data is lost.

---

## Ground Truth Data Structure

Each misconception in `data/{assignment}/groundtruth.json`:

```json
{
  "id": "NM_STATE_01",
  "category": "The Reactive State Machine",
  "name": "Spreadsheet View (Early Calculation)",
  "explanation": "The student views variables as reactive entities that update when assigned a formula, similar to Excel cells...",
  "student_thinking": "I'll define the formula at the top of the code. Then when the inputs are read, the formula will automatically update.",
  "code_pattern": "Compute derived value BEFORE reading inputs that it depends on",
  "applicable_questions": ["Q1", "Q2", "Q3"]
}
```

### Key Fields:
- **id:** Unique identifier (e.g., `NM_STATE_01`)
- **category:** One of the 8 machine types (above)
- **name:** Human-readable, memorable title
- **explanation:** Ground truth description (used for semantic matching)
- **student_thinking:** Internal monologue showing the misconception
- **code_pattern:** How to inject this bug into valid code
- **applicable_questions:** Which questions can exhibit this misconception

---

## Distribution Across Assignments

### A1: Scalar State (8 categories, Low Complexity)
- **NM_STATE_01** — Spreadsheet View (Early Calculation)
- **NM_IO_01** — Prompt-Logic Mismatch
- **NM_IO_02** — The Ghost Read
- **NM_TYP_01** — Integer Division Blindness
- **NM_TYP_02** — Narrowing Cast in Division
- **NM_SYN_01** — XOR as Power
- **NM_SYN_02** — Precedence Blindness
- **NM_API_01** — The Void Assumption

**Focus:** Mental models about variables, I/O, types, and method calls

---

### A2: Temporal State (6 categories, Medium Complexity)
- **NM_FLOW_01** — Accumulator Amnesia (Scope Error)
- **NM_FLOW_02** — The Intent Loop (Off-by-One)
- **NM_FLOW_03** — Infinite Loop (State Stagnation)
- **NM_FLOW_04** — Sabotaging the Future (Inner Loop Mod)
- **NM_LOGIC_01** — Mutually Exclusive Fallacy
- **NM_LOGIC_02** — Dangling Else (Indentation Trap)

**Focus:** Mental models about control flow, loops, conditionals, and scope

---

### A3: Spatial State (5 categories, High Complexity)
- **NM_MEM_01** — Parallel Array Desync
- **NM_MEM_02** — Index-Value Confusion
- **NM_MEM_03** — String Identity Trap (Immutability)
- **NM_MEM_04** — The 1-Based Offset (OOB)
- **NM_MEM_05** — Lossy Swap (Data Destruction)

**Focus:** Mental models about arrays, indexing, and memory/reference semantics

---

## Research Questions

| RQ  | Question                                                |
| --- | ------------------------------------------------------- |
| RQ1 | What is the diagnostic ceiling for LLM detection?       |
| RQ2 | Does LLM performance degrade with cognitive complexity? |
| RQ3 | Which prompt strategy (baseline, taxonomy, CoT, Socratic) is most effective? |
| RQ4 | Do different LLM models agree or provide complementary insights? |
| RQ5 | Can ensemble voting reduce hallucinations?              |

---

## See Also

- `complexity-gradient.md` — Why A3 (0.890 F1) > A2 (0.751) > A1 (0.592)
- `architecture.md` — How misconceptions are injected and detected
- `analysis-pipeline.md` — The complete end-to-end flow
- `metrics-guide.md` — Performance metrics explained
