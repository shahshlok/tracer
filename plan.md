# Plan: Authentic Seeded Submission Generation

## Objective
Create a high-fidelity validation dataset, `authentic_seeded/`, that mirrors and improves upon the authenticity of `student_submissions/`. The goal is to have a directory of "student" submissions that look completely real—messy, varied, and diverse—but contain specific, ground-truth misconceptions for validation.

## Strategy: The "Authentic Simulation" Approach

Instead of reusing existing student files as a fixed "control group," we will treat the entire `student_submissions/` directory as a **style corpus**. We will analyze the styles, variable naming patterns, and structural quirks of *all* students, and then generate new, unique seeded submissions that mimic these authentic traits.

### 1. Style Analysis
We will analyze the existing 25 students to understand:
*   **Scanner Names**: `sc`, `input`, `scanner`, `reader`, `keyboard`.
*   **Variable Naming**: `v0` vs `startVelocity`, `t` vs `time`.
*   **Formatting**: Indentation (spaces vs tabs, 2 vs 4), brace placement (end-of-line vs new-line).
*   **Noise**: Extra newlines, comments, missing spaces around operators.

### 2. "Authentic Seeded" Generation
We will create `utils/authentic_generator.py` to generate completely new student profiles (e.g., `Lastname_firstname_001`, `Lastname_firstname_002`) that exhibit these realistic traits.

**Process per Seeded Student:**
1.  **Assign Persona**: Randomly pick a "coding persona" (e.g., "The Minimalist", "The Verbose", "The Messy").
2.  **Generate Correct Base**: Construct the correct Java code using the persona's style preferences (variable names, whitespace).
3.  **Inject Misconception**: "Break" the code logic according to the specific misconception ID (e.g., `DT001` - Int Division).
4.  **Add Noise**: Randomly add "human" touches like spacing inconsistencies or comments.

### 3. Injection Logic
The injection will happen at the *generation* stage, not post-processing. This is more robust.

| Misconception | Injection Strategy |
| :--- | :--- |
| **DT001 (Int Types)** | Declare all inputs as `int` instead of `double`. |
| **DT002 (Int Div)** | Cast operands to `(int)` before division. |
| **VAR001 (Precedence)** | Remove parentheses in `(v1 - v0) / t`. |
| **CONST001 (Caret)** | Use `^` instead of `Math.pow()`. |
| **...and so on** | (Full mapping for all 11 misconceptions) |

### 4. Output Structure
The `authentic_seeded/` directory will look indistinguishable from a real class roster, except for the folder naming convention to track ground truth.

```
authentic_seeded/
├── lastname_firstname_DT001/   <-- unique student name + Misconception Tag
│   └── Q1.java                   <-- The messy, "real" looking file
├── lastname_firstname_VAR003/
│   └── Q2.java
├── lastname_firstname_Correct/
│   └── Q1.java
└── manifest.json                 <-- Full ground truth map
```

### 5. Implementation Plan
1.  **`utils/authentic_generator.py`**:
    *   `CodeBuilder` class: Handles indentation, spacing, and variable naming context.
    *   `StyleProfile`: Dataclass defining a student's specific quirks.
    *   `Templates`: Logic-agnostic templates that `CodeBuilder` fleshes out.
2.  **Execution**: Generate 50+ unique seeded submissions covering all misconceptions + control correct ones.
3.  **Validation**: Ensure the code compiles (except for syntax-error misconceptions) and looks "human".

## Benefits
*   **No Data Leakage**: We aren't reusing the exact code from the training/test set (`student_submissions`).
*   **Infinite Diversity**: We can generate 1000s of unique-looking submissions.
*   **Ground Truth**: We know exactly what error is in `Authentic_Seeded_055` because we put it there.
