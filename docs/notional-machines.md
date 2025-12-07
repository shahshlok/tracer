# Notional Machines & Misconceptions

This document describes the theoretical framework underlying this research.

## What is a Notional Machine?

> A **Notional Machine** is the mental model a student has of how the computer executes code.

When this mental model diverges from actual language semantics, we call it a **misconception**.

---

## The 5 Notional Machine Categories

### 1. The Reactive State Machine

**Belief:** Variables update automatically like Excel cells.

**Example Misconception:** Spreadsheet View (Early Calculation)

```java
// Student code
double v0 = 0, v1 = 0, t = 0;
double a = (v1 - v0) / t;  // Computed BEFORE input!
v0 = scan.nextDouble();
v1 = scan.nextDouble();
t = scan.nextDouble();
System.out.println(a);     // Prints stale value
```

**Student Thinking:** "I'll define the formula at the top. `a` will update automatically once the inputs have values."

---

### 2. The Anthropomorphic I/O Machine

**Belief:** The computer reads prompt text to know which variable to fill.

**Example Misconception:** Prompt-Logic Mismatch

```java
System.out.println("Enter v0, then v1:");
double v1 = scan.nextDouble();  // Wrong order!
double v0 = scan.nextDouble();
```

**Student Thinking:** "I told the user 'Enter v0, then v1'. The computer knows the first number is for v0 because of my prompt."

---

### 3. The Fluid Type Machine

**Belief:** Type conversions happen automatically; division always gives decimals.

**Example Misconception:** Integer Division Blindness

```java
int a = 5, b = 2;
double result = a / b;  // result = 2.0, not 2.5!
```

**Student Thinking:** "If I store it in a double, Java will include the decimals."

---

### 4. The Algebraic Syntax Machine

**Belief:** Mathematical notation works directly in code.

**Example Misconceptions:**
- **XOR as Power:** Using `x^2` instead of `Math.pow(x, 2)`
- **Precedence Blindness:** Writing `v1 - v0 / t` expecting `(v1-v0)/t`

---

### 5. The Void Machine

**Belief:** Methods modify arguments in place without needing assignment.

**Example Misconception:** The Void Assumption

```java
double x = 16;
Math.sqrt(x);     // Called but not assigned!
System.out.println(x);  // Still prints 16
```

**Student Thinking:** "I called `Math.sqrt(x)` to turn x into its square root."

---

## Ground Truth Schema

Each misconception in `groundtruth.json`:

```json
{
  "id": "NM_STATE_01",
  "category": "The Reactive State Machine",
  "name": "Spreadsheet View (Early Calculation)",
  "applicable_questions": ["Q1", "Q2"],
  "explanation": "The student views variables as reactive constraints...",
  "student_thinking": "\"I'll define the formula at the top...\"",
  "instructions_for_llm": {
    "Q1": "Compute result BEFORE reading input...",
    "Q2": "Initialize to 0, compute, then read..."
  }
}
```

---

## Adding New Misconceptions

1. Add entry to `data/{assignment}/groundtruth.json`
2. Include:
   - Unique `id` (format: `NM_{CATEGORY}_{NUMBER}`)
   - `category` (one of the 5 machines)
   - `name` (short, memorable)
   - `applicable_questions` (which questions can exhibit this)
   - `explanation` (for matching)
   - `student_thinking` (quoted internal monologue)
   - `instructions_for_llm` (how to generate the bug)

3. Regenerate dataset:
   ```bash
   uv run pipeline run --skip-detection --skip-analysis
   ```

---

## Research Questions

| RQ  | Question                                             |
| --- | ---------------------------------------------------- |
| RQ1 | What is the diagnostic ceiling for LLM detection?    |
| RQ2 | Does LLM performance correlate with cognitive depth? |
| RQ3 | Which prompt strategy works best?                    |
| RQ4 | Do models agree or complement each other?            |
