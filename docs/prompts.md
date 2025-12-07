# Prompt Strategies

This document describes the 4 prompt architectures used to detect notional machine misconceptions.

## Overview

| Strategy     | Philosophy                           | When to Use              |
| ------------ | ------------------------------------ | ------------------------ |
| **baseline** | Simple error classification          | Control condition        |
| **taxonomy** | Explicit notional machine categories | When taxonomy is known   |
| **cot**      | Line-by-line execution tracing       | For complex state bugs   |
| **socratic** | Mental model probing                 | For pedagogical analysis |

---

## 1. Baseline (Control)

**File:** `prompts/strategies.py::build_baseline_prompt`

Minimal guidance - just asks the LLM to find bugs without any theoretical framework.

```
You are a CS1 code reviewer.

TASK:
1. Determine if the code produces correct output
2. Identify the root cause of any bug
3. Classify conceptual errors

Do NOT report: syntax typos, style issues
DO report: fundamental misunderstandings about Java
```

**Purpose:** Establishes baseline performance without pedagogical scaffolding.

---

## 2. Taxonomy

**File:** `prompts/strategies.py::build_taxonomy_prompt`

Provides the 5 notional machine categories explicitly:

1. **The Reactive State Machine** - Variables update automatically like Excel
2. **The Anthropomorphic I/O Machine** - Computer reads prompt text
3. **The Fluid Type Machine** - Type conversions happen magically
4. **The Algebraic Syntax Machine** - Math notation works in code
5. **The Void Machine** - Methods modify arguments in place

**Purpose:** Tests if explicit taxonomy improves detection.

---

## 3. Chain of Thought (CoT)

**File:** `prompts/strategies.py::build_cot_prompt`

Forces step-by-step execution tracing:

```
INSTRUCTIONS:
1. TRACE EXECUTION - Go line by line, track all variables
2. IDENTIFY DIVERGENCE - Compare actual vs expected values
3. DIAGNOSE THE MENTAL MODEL - What would the student believe?
4. SUMMARIZE MISCONCEPTIONS

Show your work, then output the final JSON.
```

**Purpose:** Tests if explicit reasoning improves state-based bug detection.

---

## 4. Socratic

**File:** `prompts/strategies.py::build_socratic_prompt`

Approaches as a tutor probing student beliefs:

```
Your goal is NOT to fix the code, but to understand the student's MENTAL MODEL.

PROBE THE STUDENT'S BELIEFS:
- Does the student think the computer understands English prompts?
- Does the student think variables update automatically?
- Does the student think math notation works the same?
- Does the student think methods modify arguments in place?
- Does the student understand type conversion?
```

**Purpose:** Tests pedagogical framing for misconception detection.

---

## Output Schema

All strategies use the same JSON output format:

```json
{
  "misconceptions": [
    {
      "inferred_category_name": "Short name for the mental model failure",
      "student_thought_process": "The student believes...",
      "conceptual_gap": "Gap between belief and Java semantics",
      "error_manifestation": "How this manifests (wrong output, etc.)",
      "confidence": 0.85,
      "evidence": [
        {"line_number": 5, "code_snippet": "double a = (v1-v0)/t;"}
      ]
    }
  ]
}
```

---

## Adding New Strategies

1. Add builder function in `prompts/strategies.py`:
   ```python
   def build_my_prompt(problem_description: str, student_code: str) -> str:
       return f'''...'''
   ```

2. Add to `PromptStrategy` enum:
   ```python
   MY_STRATEGY = "my_strategy"
   ```

3. Register in `STRATEGIES` dict:
   ```python
   STRATEGIES = {
       ...,
       PromptStrategy.MY_STRATEGY: build_my_prompt,
   }
   ```
