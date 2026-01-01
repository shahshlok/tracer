# Prompting Strategies

This document describes the 4 prompting strategies used to elicit misconception diagnoses from LLMs.

---

## Overview

| Strategy | Philosophy | Precision | Recall | F1 |
|----------|------------|-----------|--------|-----|
| **baseline** | Simple "find bugs" | 0.373 | 0.850 | **0.519** |
| **taxonomy** | Provide category list | 0.366 | 0.890 | 0.518 |
| **cot** | Chain-of-thought tracing | 0.345 | 0.841 | 0.489 |
| **socratic** | Mental model probing | 0.251 | 0.890 | 0.391 |

**Key Finding:** Simple prompts outperform pedagogically-motivated prompts. Socratic finds the most bugs but hallucinates the most.

---

## Strategy 1: Baseline

**File:** `prompts/strategies.py::build_baseline_prompt`

### Philosophy

Minimal guidance—just ask the LLM to find misconceptions without theoretical framing.

### Prompt Structure

```
You are a CS1 code reviewer analyzing student submissions.

TASK:
1. Determine if the code produces correct output for the given problem
2. Identify the root cause of any bug (not just the symptom)
3. Classify any conceptual errors the student demonstrates

DO NOT report: syntax typos, style issues, or missing comments
DO report: fundamental misunderstandings about how Java works

[Problem description]
[Student code]

Output your analysis as JSON:
{
  "misconceptions": [
    {
      "inferred_category_name": "...",
      "student_thought_process": "...",
      "conceptual_gap": "...",
      "evidence": [{"line_number": N, "code_snippet": "..."}],
      "confidence": 0.0-1.0
    }
  ]
}
```

### Why It Works

- LLMs are pre-trained on code analysis
- Simple prompts reduce overthinking
- Direct task framing yields focused responses

---

## Strategy 2: Taxonomy

**File:** `prompts/strategies.py::build_taxonomy_prompt`

### Philosophy

Provide the complete list of Notional Machine categories to guide detection.

### Prompt Structure

```
You are analyzing student code for specific types of misconceptions.

MISCONCEPTION CATEGORIES:
1. The Reactive State Machine - Student thinks variables auto-update
2. The Anthropomorphic I/O Machine - Student thinks computer reads prompts
3. The Fluid Type Machine - Student expects automatic type conversion
4. The Algebraic Syntax Machine - Student uses math notation in code
5. The Void Machine - Student thinks methods modify arguments in place
[... all 10 categories ...]

Identify which category (if any) best describes the student's error.

[Problem description]
[Student code]

Output as JSON...
```

### Trade-offs

- **Pro:** Higher recall (0.890 vs 0.850)—finds more bugs
- **Con:** Slightly lower precision—may force-fit categories

---

## Strategy 3: Chain-of-Thought (CoT)

**File:** `prompts/strategies.py::build_cot_prompt`

### Philosophy

Force step-by-step execution tracing before diagnosis.

### Prompt Structure

```
Analyze this student code using chain-of-thought reasoning.

STEP 1: TRACE EXECUTION
Go line by line. Track all variable values.

STEP 2: IDENTIFY DIVERGENCE
Where does actual behavior differ from expected?

STEP 3: DIAGNOSE MENTAL MODEL
What does the student believe about how the code works?

STEP 4: SUMMARIZE
Provide the misconception in structured format.

[Problem description]
[Student code]

Show your reasoning, then output JSON...
```

### Results

- Longer responses (more tokens)
- Not significantly better than baseline
- May introduce more hallucinations via extended reasoning

---

## Strategy 4: Socratic

**File:** `prompts/strategies.py::build_socratic_prompt`

### Philosophy

Approach as a tutor probing student beliefs.

### Prompt Structure

```
You are a CS1 tutor trying to understand your student's mental model.

Your goal is NOT to fix the code, but to understand WHY the student
wrote it this way.

PROBE THE STUDENT'S BELIEFS:
- Does the student think the computer understands English prompts?
- Does the student think variables update automatically like Excel?
- Does the student think math notation (^, implicit multiplication) works?
- Does the student understand type conversion rules?
- Does the student know methods return values vs modify in place?

Describe what the student likely believes, then classify the misconception.

[Problem description]
[Student code]

Output as JSON...
```

### Results

- **Highest recall:** 0.890 (finds the most bugs)
- **Lowest precision:** 0.251 (most hallucinations)
- Generates creative but often incorrect diagnoses

---

## Output Schema

All strategies use the same JSON output format:

```json
{
  "misconceptions": [
    {
      "inferred_category_name": "Short descriptive name",
      "student_thought_process": "What the student believes...",
      "conceptual_gap": "The gap between belief and reality",
      "error_manifestation": "How this causes wrong output",
      "confidence": 0.85,
      "evidence": [
        {"line_number": 5, "code_snippet": "double a = (v1-v0)/t;"}
      ]
    }
  ]
}
```

---

## Ensemble Benefit

Each strategy has strengths and weaknesses. Ensemble voting combines them:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         STRATEGY ENSEMBLE                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  File: Allen_Andrew_600171_Q1.java                                           │
│  Ground Truth: NM_STATE_01 (Spreadsheet View)                               │
│                                                                             │
│  ┌─────────────┬────────────────────────────────────────────┐               │
│  │ baseline    │ Detected "Early Calculation" → NM_STATE_01 │  ✓           │
│  │ taxonomy    │ Detected "Reactive State"    → NM_STATE_01 │  ✓           │
│  │ cot         │ No detection                               │  ✗           │
│  │ socratic    │ Detected "Auto-Update"       → NM_STATE_01 │  ✓           │
│  └─────────────┴────────────────────────────────────────────┘               │
│                                                                             │
│  Agreement: 3/4 strategies → VALIDATED                                       │
│                                                                             │
│  After ensemble: Precision improves +107%, recall stable                    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Adding New Strategies

### Step 1: Create Builder Function

```python
# prompts/strategies.py

def build_my_strategy_prompt(problem_description: str, student_code: str) -> str:
    return f'''
Your custom prompt here...

Problem:
{problem_description}

Code:
{student_code}

{OUTPUT_SCHEMA}
'''.strip()
```

### Step 2: Add to Enum

```python
class PromptStrategy(str, Enum):
    BASELINE = "baseline"
    TAXONOMY = "taxonomy"
    COT = "cot"
    SOCRATIC = "socratic"
    MY_STRATEGY = "my_strategy"  # Add here
```

### Step 3: Register

```python
STRATEGIES = {
    PromptStrategy.BASELINE: build_baseline_prompt,
    PromptStrategy.TAXONOMY: build_taxonomy_prompt,
    PromptStrategy.COT: build_cot_prompt,
    PromptStrategy.SOCRATIC: build_socratic_prompt,
    PromptStrategy.MY_STRATEGY: build_my_strategy_prompt,  # Add here
}
```

---

## Next: [Development Guide](development.md)
