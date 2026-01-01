"""
Prompt strategies for notional machine misconception detection.

This module contains 4 prompt architectures for thesis experiments:
1. BASELINE - Simple error classification (control condition)
2. TAXONOMY - Explicit notional machine categories
3. COT - Chain of thought line-by-line tracing
4. SOCRATIC - Mental model probing

The key research question: Can LLMs discover notional machine misconceptions
using different prompting strategies?
"""

from __future__ import annotations

from enum import Enum


class PromptStrategy(str, Enum):
    """Available prompt strategies for misconception detection."""

    BASELINE = "baseline"  # Simple error classification
    TAXONOMY = "taxonomy"  # Taxonomy-guided detection
    COT = "cot"  # Chain of thought tracing
    SOCRATIC = "socratic"  # Mental model probing


# JSON output schema for all strategies
OUTPUT_SCHEMA = """
Return your analysis as JSON matching this exact schema:

{
  "misconceptions": [
    {
      "inferred_category_name": "Short descriptive name for the mental model failure",
      "student_thought_process": "The student believes...",
      "conceptual_gap": "Explanation of the gap between student's model and actual Java semantics",
      "error_manifestation": "How this manifests (wrong output, compile error, etc.)",
      "confidence": 0.0 to 1.0,
      "evidence": [
        {"line_number": 5, "code_snippet": "double a = (v1-v0)/t;"}
      ]
    }
  ]
}

If no misconceptions are found, return: {"misconceptions": []}
"""


def build_baseline_prompt(problem_description: str, student_code: str) -> str:
    """
    BASELINE (Control): Simple error classification without notional machine guidance.

    Tests whether LLMs can detect misconceptions without explicit framework.
    """
    return f"""You are a CS1 code reviewer.

PROBLEM:
{problem_description}

STUDENT CODE:
```java
{student_code}
```

TASK:
1. Determine if the code produces correct output for the given problem.
2. If there is a bug, identify the root cause.
3. Classify any conceptual errors you find.

Do NOT report:
- Syntax typos (missing semicolons, misspelled variables)
- Style issues (formatting, naming conventions)

DO report:
- Fundamental misunderstandings about how Java works
- Logic errors that suggest flawed mental models
- Incorrect assumptions about program execution

""".strip()


def build_taxonomy_prompt(problem_description: str, student_code: str) -> str:
    """
    TAXONOMY: Explicit notional machine categories guide detection.

    Provides the 5 notional machine categories to help classify misconceptions.
    """
    return f"""You are a CS1 Teaching Assistant analyzing student code for notional machine misconceptions.

A "Notional Machine" is the mental model a student has of how the computer executes their code.
Misconceptions occur when this mental model diverges from actual Java execution.

NOTIONAL MACHINES TAXONOMY:

1. THE REACTIVE STATE MACHINE
   - Belief: Variables update automatically like Excel cells
   - Example: Computing a formula BEFORE reading input, expecting the result to update later

2. THE ANTHROPOMORPHIC I/O MACHINE
   - Belief: The computer "reads" the prompt text to know which variable to fill
   - Example: Prompt says "Enter x1, y1:" but code reads into y1 first

3. THE FLUID TYPE MACHINE
   - Belief: Type conversions happen automatically; division always gives decimals
   - Example: int/int gives truncated result, but student expects decimal

4. THE ALGEBRAIC SYNTAX MACHINE
   - Belief: Mathematical notation works directly in code
   - Example: Using ^ for power instead of Math.pow, or ignoring operator precedence

5. THE VOID MACHINE
   - Belief: Methods modify arguments in place without needing assignment
   - Example: Calling Math.sqrt(x) without assigning the result

PROBLEM:
{problem_description}

STUDENT CODE:
```java
{student_code}
```

TASK:
Analyze the code for notional machine misconceptions. For each misconception:
- Name the category (use your own words, don't just copy the taxonomy names)
- Explain what the student believes vs. reality
- Point to specific evidence
""".strip()


def build_cot_prompt(problem_description: str, student_code: str) -> str:
    """
    COT (Chain of Thought): Line-by-line execution tracing.

    Forces the LLM to trace execution step-by-step to find where
    student mental model diverges from actual execution.
    """
    return f"""You are a runtime environment simulator.

PROBLEM:
{problem_description}

STUDENT CODE:
```java
{student_code}
```

INSTRUCTIONS:
Step through this code systematically:

1. TRACE EXECUTION
   - Go line by line
   - Track the state of ALL variables after each line
   - Note the order of input/output operations

2. IDENTIFY DIVERGENCE
   - Compare actual computed values to mathematically expected values
   - Find where the student's intended behavior differs from actual behavior

3. DIAGNOSE THE MENTAL MODEL
   - For each divergence, ask: "What would the student have to believe for this to make sense?"
   - Identify the flawed assumption about how Java works

4. SUMMARIZE MISCONCEPTIONS
   - Name each conceptual error found
   - Explain the gap between belief and reality

Show your work, then output the final JSON.
""".strip()


def build_socratic_prompt(problem_description: str, student_code: str) -> str:
    """
    SOCRATIC: Mental model probing through questioning.

    Approaches the code as a tutor trying to understand student thinking.
    """
    return f"""You are an expert Computer Science tutor using the Socratic Method.

Your goal is NOT to fix the code, but to understand the student's MENTAL MODEL.

PROBLEM:
{problem_description}

STUDENT CODE:
```java
{student_code}
```

PROBE THE STUDENT'S BELIEFS:

Look at the code and ask yourself:
- Does the student think the computer understands English prompts?
- Does the student think variables update automatically when their dependencies change?
- Does the student think math notation (like ^) works the same as on paper?
- Does the student think methods modify their arguments in place?
- Does the student understand when type conversion happens?

For each flawed belief you identify:
1. State what the student appears to believe
2. Explain why this belief is incorrect in Java
3. Point to the specific code that reveals this belief

Be empathetic - these are common, reasonable mistakes for beginners.

""".strip()


# Strategy registry
STRATEGIES = {
    PromptStrategy.BASELINE: build_baseline_prompt,
    PromptStrategy.TAXONOMY: build_taxonomy_prompt,
    PromptStrategy.COT: build_cot_prompt,
    PromptStrategy.SOCRATIC: build_socratic_prompt,
}


def build_prompt(
    strategy: PromptStrategy,
    problem_description: str,
    student_code: str,
) -> str:
    """
    Build a prompt using the specified strategy.

    Args:
        strategy: Which prompt architecture to use
        problem_description: The assignment problem text
        student_code: The student's Java submission

    Returns:
        The constructed prompt string
    """
    if isinstance(strategy, str):
        strategy = PromptStrategy(strategy)

    builder = STRATEGIES.get(strategy)
    if builder is None:
        raise ValueError(f"Unknown strategy: {strategy}. Valid: {list(PromptStrategy)}")

    return builder(problem_description, student_code)
