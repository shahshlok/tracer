# Authentic Seeded Submission Generator

## Overview

The "Authentic Seeded" generator creates **high-fidelity** synthetic Java submissions. Unlike traditional generators that use rigid templates, this system simulates realistic student coding styles (variable naming quirks, messy indentation, different scanner names) while injecting specific, ground-truth misconceptions.

This allows us to measure the **Precision** and **Recall** of our misconception detection system against "real-world" messy code, avoiding the "clean lab" bias of template-based tests.

## How It Works

The system uses a `CodeBuilder` engine driven by randomized `StyleProfiles`.

```
┌──────────────────────┐      ┌──────────────────────┐      ┌──────────────────────┐
│  Style Profile       │      │  Logic Generator     │      │  Seeded Output       │
│  (Randomized)        │      │  (Q1-Q4)             │      │                      │
├──────────────────────┤      ├──────────────────────┤      ├──────────────────────┤
│ • Scanner: "sc"      │ ───► │ 1. Apply Formatting  │ ───► │ authentic_seeded/    │
│ • Indent: 2 spaces   │      │ 2. Inject Errors     │      │ ├── Smith_John_DT001/│
│ • Vars: "v1", "v2"   │      │    (e.g., Int Div)   │      │ │   └── Q1.java      │
│ • Comments: Yes      │      │ 3. Add "Noise"       │      │ ├── Jones_Amy_Correct│
└──────────────────────┘      └──────────────────────┘      │ │   └── Q1.java      │
                                                            │ └── manifest.json    │
                                                            └──────────────────────┘
```

## Usage

### CLI Usage

To generate a fresh batch of submissions:

```bash
uv run python utils/authentic_generator.py
```

This will create/overwrite the `authentic_seeded/` directory with:
- ~60 student folders (e.g., `Lastname_Firstname_Num_Tag`)
- `manifest.json` (Machine-readable ground truth)
- `README.md` (Human-readable report)

### Python API

```python
from utils.authentic_generator import AuthenticGenerator

# Generate 100 submissions
gen = AuthenticGenerator(output_dir="authentic_seeded")
gen.generate_batch(count=100)
```

## Directory Structure

The output mimics the structure of the real `student_submissions` directory:

```
authentic_seeded/
├── Anderson_Emma_200123_OTHER002/   <-- Student Folder
│   └── Q2.java                      <-- The Code File
├── Baker_Logan_200147_OTHER001/
│   └── Q4.java
├── Carter_Joseph_200119_Correct/    <-- Control Group (No Error)
│   └── Q4.java
├── manifest.json                    <-- Ground Truth Data
└── README.md                        <-- Summary Report
```

## Features

### Style Simulation
The generator randomly assigns each "student" a unique style profile:
*   **Scanner Names**: `sc`, `input`, `scanner`, `reader`, `in`
*   **Variable Names**: Short (`v0`, `t`), Descriptive (`startVelocity`, `time`), or Mixed (`vStart`).
*   **Indentation**: 2 spaces, 4 spaces, or tabs.
*   **Braces**: End-of-line (`) {`) or new-line (`)\n{`).
*   **Noise**: Random extra newlines, optional comments.

### Misconception Injection
The generator supports the full catalog of misconceptions by modifying the logic construction logic *before* code generation:

| ID | Misconception | Injection Strategy |
|----|---------------|--------------------|
| **DT001** | Int vs Double | Declare variables as `int` and use `nextInt()` |
| **DT002** | Int Division | Cast operands to `(int)` in division formula |
| **VAR001** | Precedence | Remove necessary parentheses in formula |
| **VAR002** | Wrong Operator | Use `+` instead of `-` |
| **CONST001** | Caret Power | Use `^` instead of `Math.pow()` |
| **CONST002** | Missing Sqrt | Return squared distance/area instead of root |
| **INPUT002** | Missing Input | Skip reading one of the input variables |
| **...** | ...and more | See `manifest.json` for full list |

## extending the Generator

To add a new question or style:

1.  **Edit `utils/authentic_generator.py`**
2.  **Add Logic Class**: Create a `Q5Logic` class inheriting from `QuestionLogic`.
3.  **Register**: Add it to the `self.generators` dictionary in `AuthenticGenerator`.

To add a new misconception:

1.  **Update Logic**: Modify the `generate()` method of the relevant Question class to check for the new `misconception_id` and alter the code string accordingly.
