# Notional Machines & Misconceptions

This document describes all 17 misconceptions detected by this framework, organized by their **Notional Machine** category. A Notional Machine is the mental model a student has about how code executes.

---

## What is a Notional Machine?

> A **Notional Machine** is the abstract model in a student's mind of how the computer executes their code.

When this mental model diverges from actual language semantics, we call it a **misconception**. This framework measures whether LLMs can detect these misconceptions.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     NOTIONAL MACHINE EXAMPLES                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Student's Mental Model              Reality                                 │
│  ─────────────────────────           ───────                                 │
│                                                                             │
│  "Variables update auto-      vs     Variables hold fixed values at         │
│   matically like Excel"              the point of assignment                │
│                                                                             │
│  "Arrays start at index 1     vs     Java uses 0-based indexing             │
│   like in mathematics"                                                      │
│                                                                             │
│  "The ^ operator means        vs     ^ is bitwise XOR in Java               │
│   exponentiation"                                                           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Detection Difficulty Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    MISCONCEPTION DETECTION DIFFICULTY                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STRUCTURAL (easy to detect)                                                 │
│  ────────────────────────────────────────────────────────────────────────── │
│  The Void Machine             ████████████████████░ 99%   NM_API_01          │
│  The Mutable String Machine   ████████████████████░ 99%   NM_MEM_03          │
│  The Human Index Machine      ███████████████████░░ 97%   NM_MEM_04          │
│  The Algebraic Syntax Machine ███████████████████░░ 97%   NM_SYN_*           │
│  The Semantic Bond Machine    ███████████████████░░ 95%   NM_MEM_01,05       │
│  The Teleological Control     ██████████████████░░░ 93%   NM_FLOW_*          │
│  The Anthropomorphic I/O      █████████████████░░░░ 88%   NM_IO_*            │
│                                                                             │
│  SEMANTIC (hard to detect)                                                   │
│  ────────────────────────────────────────────────────────────────────────── │
│  The Reactive State Machine   █████████████░░░░░░░░ 65%   NM_STATE_01        │
│  The Independent Switch       ████████████░░░░░░░░░ 63%   NM_LOGIC_*         │
│  The Fluid Type Machine       ███████████░░░░░░░░░░ 59%   NM_TYP_*           │
│                                                                             │
│  Lowest individual:                                                          │
│  Dangling Else (NM_LOGIC_02)  ███░░░░░░░░░░░░░░░░░░ 16%   CRITICAL           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Category 1: The Reactive State Machine

**Mental Model:** Variables update automatically like Excel cells when their sources change.

**Assignment:** A1 (Variables & Math)  
**Recall:** 65% (Hard to detect)

### NM_STATE_01: Spreadsheet View (Early Calculation)

The student believes that defining a formula before reading inputs will cause the formula to auto-update when inputs are later assigned.

**Buggy Code:**
```java
double v0 = 0, v1 = 0, t = 0;
double a = (v1 - v0) / t;  // Calculated with zeros!
v0 = scan.nextDouble();
v1 = scan.nextDouble();
t = scan.nextDouble();
System.out.println(a);     // Prints NaN or 0, not correct result
```

**Student Thinking:**
> "I'll define the formula first. When I read the inputs, `a` will automatically recalculate."

**Reality:**
Assignment in Java is a one-time operation. `a` captures the value at the moment of assignment and never updates.

**Why LLMs Struggle:**
This misconception is invisible in the code structure—there's no syntax error. The LLM must infer that the student expected reactive behavior.

---

## Category 2: The Anthropomorphic I/O Machine

**Mental Model:** The computer reads prompt text to understand the order/meaning of inputs.

**Assignment:** A1 (Variables & Math)  
**Recall:** 88% (Medium)

### NM_IO_01: Prompt-Logic Mismatch

The student believes the computer understands English prompts and assigns inputs based on the prompt text, not the code order.

**Buggy Code:**
```java
System.out.println("Enter v0, then v1:");
double v1 = scan.nextDouble();  // Wrong order!
double v0 = scan.nextDouble();
```

**Student Thinking:**
> "I told the user 'Enter v0, then v1', so the computer knows the first input goes to v0."

**Reality:**
The scanner reads in execution order. The first `nextDouble()` goes to `v1`, not `v0`.

---

### NM_IO_02: The Ghost Read

The student believes `println()` reads input from the user.

**Buggy Code:**
```java
double x = 0;
System.out.println("Enter x: " + x);  // Student thinks this reads input
System.out.println(x);                 // Still prints 0
```

**Student Thinking:**
> "When I print 'Enter x:', the computer reads what the user types."

**Reality:**
`println()` only outputs; it doesn't read input. You need `scan.nextDouble()` to read.

---

## Category 3: The Fluid Type Machine

**Mental Model:** Type conversions happen automatically; the result type determines computation precision.

**Assignment:** A1 (Variables & Math)  
**Recall:** 59% (Hard to detect)

### NM_TYP_01: Integer Division Blindness

The student believes storing a division result in a `double` automatically preserves decimals.

**Buggy Code:**
```java
int a = 5, b = 2;
double result = a / b;  // result = 2.0, not 2.5!
```

**Student Thinking:**
> "If I put it in a `double`, Java will include the decimals."

**Reality:**
`5 / 2` is computed as integer division first (→ 2), then converted to 2.0.

**Correct:**
```java
double result = (double) a / b;  // Cast BEFORE division
```

---

### NM_TYP_02: Narrowing Cast in Division

The student believes casting after division preserves precision.

**Buggy Code:**
```java
int a = 5, b = 2;
int result = (int)(a / b);  // Still 2, not 2.5 truncated
```

**Student Thinking:**
> "Casting to int will take the integer part of 2.5."

**Reality:**
`a / b` already computed as 2 (integer division). Casting 2 to int is still 2.

---

## Category 4: The Algebraic Syntax Machine

**Mental Model:** Mathematical notation translates directly to code.

**Assignment:** A1 (Variables & Math)  
**Recall:** 97% (Easy)

### NM_SYN_01: XOR as Power

The student uses `^` for exponentiation like in mathematics.

**Buggy Code:**
```java
double result = 2 ^ 3;  // result = 1, not 8!
```

**Student Thinking:**
> "In math, 2^3 = 8, so this should give me 8."

**Reality:**
`^` is bitwise XOR in Java. Use `Math.pow(2, 3)` for exponentiation.

---

### NM_SYN_02: Precedence Blindness

The student assumes left-to-right evaluation or their intended grouping.

**Buggy Code:**
```java
double result = 10 - 5 / 2;
// Student expects: (10 - 5) / 2 = 2.5
// Actually: 10 - (5 / 2) = 10 - 2 = 8
```

**Student Thinking:**
> "I want to subtract 5 from 10, then divide by 2."

**Reality:**
Division has higher precedence than subtraction. Use parentheses: `(10 - 5) / 2`.

---

## Category 5: The Void Machine

**Mental Model:** Calling a method modifies the argument in place.

**Assignment:** A1 (Variables & Math)  
**Recall:** 99% (Easy)

### NM_API_01: The Void Assumption

The student believes method calls modify their arguments without needing assignment.

**Buggy Code:**
```java
double x = 16;
Math.sqrt(x);           // Called but result not assigned!
System.out.println(x);  // Still prints 16.0
```

**Student Thinking:**
> "I called Math.sqrt(x) so x is now 4."

**Reality:**
`Math.sqrt()` returns a value; it doesn't modify `x`. Must assign: `x = Math.sqrt(x)`.

---

## Category 6: The Teleological Control Machine

**Mental Model:** Loops and conditionals behave based on intention, not actual state.

**Assignment:** A2 (Loops & Control)  
**Recall:** 93% (Easy)

### NM_FLOW_01: Accumulator Amnesia (Scope Error)

The student redeclares the accumulator inside the loop, shadowing the outer variable.

**Buggy Code:**
```java
int sum = 0;
for (int i = 0; i < 5; i++) {
    int sum = 0;  // Redeclares sum! Shadows outer
    sum += i;     // Modifies inner sum
}
System.out.println(sum);  // Still 0
```

**Student Thinking:**
> "The loop adds to sum each iteration."

**Reality:**
The inner `sum` shadows the outer one. Each iteration creates a new local `sum`.

---

### NM_FLOW_02: The Intent Loop (Off-by-One)

The student uses `<=` instead of `<` or vice versa.

**Buggy Code:**
```java
for (int i = 0; i <= 5; i++) {  // Runs 6 times, not 5
    sum += i;
}
// sum = 0+1+2+3+4+5 = 15, not 10
```

**Student Thinking:**
> "I want to count to 5, so <= 5."

**Reality:**
`<= 5` includes 5, running 6 times (0,1,2,3,4,5).

---

### NM_FLOW_03: Infinite Loop (State Stagnation)

The student forgets to update the loop variable.

**Buggy Code:**
```java
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Missing i++!
}
```

**Student Thinking:**
> "The loop should eventually exit."

**Reality:**
Without `i++`, the condition never becomes false.

---

### NM_FLOW_04: Sabotaging the Future (Inner Loop Modification)

The student modifies the outer loop variable inside the inner loop.

**Buggy Code:**
```java
for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 5; j++) {
        i++;  // Modifying outer loop variable!
    }
}
```

**Student Thinking:**
> "I want to skip ahead in the outer loop."

**Reality:**
This causes unpredictable control flow; outer loop runs fewer times than expected.

---

## Category 7: The Independent Switch

**Mental Model:** If statements are independent; else binds to my intended if.

**Assignment:** A2 (Loops & Control)  
**Recall:** 63% (Hard)

### NM_LOGIC_01: Mutually Exclusive Fallacy

The student treats non-exclusive conditions as if they're mutually exclusive.

**Buggy Code:**
```java
if (x > 10)
    System.out.println("Large");
if (x > 5)  // Not else-if!
    System.out.println("Medium");
// If x = 15: prints BOTH "Large" AND "Medium"
```

**Student Thinking:**
> "These conditions are mutually exclusive."

**Reality:**
Both `if` statements execute independently. Use `else if` for mutual exclusion.

---

### NM_LOGIC_02: Dangling Else (Indentation Trap)

The student believes indentation determines which `if` the `else` belongs to.

**Buggy Code:**
```java
if (condition1)
    if (condition2)
        statement1;
else              // Binds to condition2, not condition1!
    statement2;
```

**Student Thinking:**
> "The else belongs to the outer if because of my indentation."

**Reality:**
Java ignores indentation. `else` binds to the nearest `if` (condition2).

**Detection Rate:** 16% — the hardest misconception for LLMs to detect.

---

## Category 8: The Semantic Bond Machine

**Mental Model:** Related data structures stay in sync automatically.

**Assignment:** A3 (Arrays & Strings)  
**Recall:** 95% (Easy)

### NM_MEM_01: Parallel Array Desync

The student believes parallel arrays automatically stay synchronized.

**Buggy Code:**
```java
int[] ids = {101, 102, 103};
String[] names = {"Alice", "Bob", "Carol"};
ids[0] = 999;  // names[0] is still "Alice", not synced!
```

**Student Thinking:**
> "I'll use parallel arrays for related data. They stay in sync."

**Reality:**
Arrays are independent. Updates to one don't affect the other.

---

### NM_MEM_05: Lossy Swap (Data Destruction)

The student attempts to swap values without a temporary variable.

**Buggy Code:**
```java
int a = 5, b = 10;
a = b;  // a = 10, original a (5) is lost!
b = a;  // b = 10 (not 5)
// Result: both are 10
```

**Student Thinking:**
> "I'll just assign them back and forth."

**Reality:**
Without a temp variable, the first assignment destroys the original value.

---

## Category 9: The Human Index Machine

**Mental Model:** Arrays use 1-based indexing like mathematics.

**Assignment:** A3 (Arrays & Strings)  
**Recall:** 97% (Easy)

### NM_MEM_04: The 1-Based Offset (OOB)

The student uses index N to access the Nth element of an N-element array.

**Buggy Code:**
```java
int[] arr = new int[5];  // Valid indices: 0-4
arr[5] = 10;             // ArrayIndexOutOfBoundsException!
```

**Student Thinking:**
> "5 elements means indices 1-5."

**Reality:**
Java uses 0-based indexing. 5 elements have indices 0-4.

---

## Category 10: The Mutable String Machine

**Mental Model:** Strings can be modified in place; assignment creates a link.

**Assignment:** A3 (Arrays & Strings)  
**Recall:** 99% (Easy)

### NM_MEM_03: String Identity Trap (Immutability)

The student believes string assignment creates a link between variables.

**Buggy Code:**
```java
String s1 = "hello";
String s2 = s1;
s2 = "world";
System.out.println(s1);  // Still "hello", not "world"
```

**Student Thinking:**
> "s2 = s1 means they're linked. Changing s2 changes s1."

**Reality:**
Strings are immutable. Assignment copies the reference; reassigning `s2` just points it elsewhere.

---

## Summary Table

| ID | Name | Category | Recall | Assignment |
|----|------|----------|--------|------------|
| NM_STATE_01 | Spreadsheet View | Reactive State Machine | 65% | A1 |
| NM_IO_01 | Prompt-Logic Mismatch | Anthropomorphic I/O | 72% | A1 |
| NM_IO_02 | Ghost Read | Anthropomorphic I/O | 91% | A1 |
| NM_TYP_01 | Integer Division Blindness | Fluid Type Machine | 90% | A1 |
| NM_TYP_02 | Narrowing Cast | Fluid Type Machine | 31% | A1 |
| NM_SYN_01 | XOR as Power | Algebraic Syntax | 99% | A1 |
| NM_SYN_02 | Precedence Blindness | Algebraic Syntax | 96% | A1 |
| NM_API_01 | Void Assumption | Void Machine | 99% | A1 |
| NM_FLOW_01 | Accumulator Amnesia | Teleological Control | 97% | A2 |
| NM_FLOW_02 | Intent Loop (Off-by-One) | Teleological Control | 90% | A2 |
| NM_FLOW_03 | Infinite Loop | Teleological Control | 95% | A2 |
| NM_FLOW_04 | Sabotaging the Future | Teleological Control | 91% | A2 |
| NM_LOGIC_01 | Mutually Exclusive Fallacy | Independent Switch | 98% | A2 |
| NM_LOGIC_02 | Dangling Else | Independent Switch | **16%** | A2 |
| NM_MEM_01 | Parallel Array Desync | Semantic Bond | 99% | A3 |
| NM_MEM_03 | String Identity Trap | Mutable String | 99% | A3 |
| NM_MEM_04 | 1-Based Offset (OOB) | Human Index | 97% | A3 |
| NM_MEM_05 | Lossy Swap | Semantic Bond | 86% | A3 |

---

## Previous: [Dataset Generation](dataset-generation.md) | Next: [Methodology](methodology.md)
