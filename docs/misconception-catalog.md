# Misconception Catalog

## Overview

The misconception catalog defines **known misconceptions** that can be injected into synthetic student submissions for validation. This enables measuring precision/recall of the detection system.

## Catalog Structure

```
data/misconception_catalog.json
├── catalog_version: "1.0.0"
├── description: "..."
└── misconceptions: [
    {
        id: "DT001",
        name: "Using int instead of double...",
        topic: "Data Types",
        description: "...",
        applicable_questions: ["q1", "q2", "q3", "q4"],
        severity: "high",
        example_incorrect: "int velocity = ...",
        example_correct: "double velocity = ...",
        detection_keywords: ["int", "nextInt"]
    },
    ...
]
```

## Misconception Fields

| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique identifier (e.g., "DT001", "VAR002") |
| `name` | string | Human-readable name |
| `topic` | string | One of: Variables, Data Types, Constants, Reading input, Other |
| `description` | string | Detailed explanation of the misconception |
| `applicable_questions` | string[] | Which questions this can appear in |
| `severity` | string | "high", "medium", or "low" |
| `example_incorrect` | string | Code showing the misconception |
| `example_correct` | string | Code showing correct approach |
| `detection_keywords` | string[] | Keywords LLMs should mention when detecting |

## Catalog Contents (v1.0.0)

### By Topic

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        MISCONCEPTIONS BY TOPIC                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  DATA TYPES (3)                                                             │
│  ├── DT001: Using int instead of double for decimal values                  │
│  ├── DT002: Integer division truncation                                     │
│  └── DT003: Type mismatch in Scanner input                                  │
│                                                                             │
│  VARIABLES (4)                                                              │
│  ├── VAR001: Incorrect operator precedence                                  │
│  ├── VAR002: Wrong formula - addition instead of subtraction                │
│  ├── VAR003: Incorrect formula derivation for fuel cost                     │
│  └── VAR004: Missing intermediate variable for semi-perimeter               │
│                                                                             │
│  CONSTANTS (3)                                                              │
│  ├── CONST001: Using ^ instead of Math.pow() for exponentiation             │
│  ├── CONST002: Missing Math.sqrt() for square root                          │
│  └── CONST003: Incorrect Math.pow() argument order                          │
│                                                                             │
│  READING INPUT FROM KEYBOARD (3)                                            │
│  ├── INPUT001: Missing Scanner import                                       │
│  ├── INPUT002: Scanner not reading correct number of inputs                 │
│  └── INPUT003: Not closing Scanner resource                                 │
│                                                                             │
│  OTHER (2)                                                                  │
│  ├── OTHER001: Computing wrong quantity (different problem)                 │
│  └── OTHER002: Hardcoded values instead of user input                       │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### By Severity

| Severity | Count | Description |
|----------|-------|-------------|
| High | 9 | Critical misconceptions - must detect |
| Medium | 4 | Important but less critical |
| Low | 2 | Minor issues |

### By Question Applicability

| Question | Applicable Misconceptions |
|----------|--------------------------|
| Q1 | 10 misconceptions |
| Q2 | 10 misconceptions |
| Q3 | 11 misconceptions |
| Q4 | 12 misconceptions |

## Python API

### Loading the Catalog

```python
from utils.misconception_catalog import load_catalog, MisconceptionCatalog

# Load catalog
catalog = load_catalog()

# Or with custom path
catalog = MisconceptionCatalog("path/to/catalog.json")
```

### Querying Misconceptions

```python
# Get all misconceptions
all_misconceptions = catalog.get_all()

# Get by ID
m = catalog.get_by_id("DT001")
print(m.name)  # "Using int instead of double..."

# Get by topic
data_type_miscs = catalog.get_by_topic("Data Types")

# Get for specific question
q3_miscs = catalog.get_for_question("q3")

# Get high-severity only (recommended for validation)
high_severity = catalog.get_high_severity()
```

### Checking Applicability

```python
misconception = catalog.get_by_id("VAR002")

# Check if applicable to Q1
if misconception.applies_to("q1"):
    print("VAR002 can be injected into Q1")
```

### Summary

```python
catalog.print_summary()
# Output:
# Misconception Catalog v1.0.0
# Total: 15 misconceptions
# 
# By Topic:
#   Constants: 3
#   Data Types: 3
#   ...
```

## High-Severity Misconceptions (Recommended for Validation)

These 9 misconceptions are critical and should be reliably detected:

| ID | Name | Topic |
|----|------|-------|
| DT001 | Using int instead of double | Data Types |
| DT002 | Integer division truncation | Data Types |
| VAR001 | Incorrect operator precedence | Variables |
| VAR002 | Wrong formula (+ instead of -) | Variables |
| VAR003 | Incorrect fuel cost formula | Variables |
| CONST001 | Using ^ instead of Math.pow() | Constants |
| CONST002 | Missing Math.sqrt() | Constants |
| INPUT002 | Wrong number of inputs | Reading input |
| OTHER001 | Solving different problem | Other |

## Adding New Misconceptions

To add a new misconception, edit `data/misconception_catalog.json`:

```json
{
  "id": "NEW001",
  "name": "Descriptive name",
  "topic": "Data Types",
  "description": "Detailed explanation...",
  "applicable_questions": ["q1", "q2"],
  "severity": "high",
  "example_incorrect": "// bad code",
  "example_correct": "// good code",
  "detection_keywords": ["keyword1", "keyword2"]
}
```

### ID Naming Convention

- `DT###` - Data Types
- `VAR###` - Variables
- `CONST###` - Constants
- `INPUT###` - Reading input from keyboard
- `OTHER###` - Other

## Usage in Validation Pipeline

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   Misconception │────►│  Seeded Code     │────►│  LLM Detection  │
│   Catalog       │     │  Generator       │     │                 │
└─────────────────┘     └──────────────────┘     └────────┬────────┘
                                                          │
                                                          ▼
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   Precision/    │◄────│  Compare         │◄────│  Detected       │
│   Recall        │     │  Injected vs     │     │  Misconceptions │
│   Metrics       │     │  Detected        │     │                 │
└─────────────────┘     └──────────────────┘     └─────────────────┘
```

## Next Steps

1. **Phase 2.2**: Build seeded submission generator that injects these misconceptions
2. **Phase 2.3**: Build validation pipeline to measure precision/recall
