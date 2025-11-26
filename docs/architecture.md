# Architecture

This document describes the system architecture, component relationships, and data flow of the Ensemble Model Evaluation CLI.

## Table of Contents

- [System Overview](#system-overview)
- [Pipeline Architecture](#pipeline-architecture)
- [Component Details](#component-details)
- [Data Flow](#data-flow)
- [Concurrency Model](#concurrency-model)
- [Extension Points](#extension-points)

## System Overview

The EME CLI is designed as a pipeline architecture with clear separation between:

1. **Input Layer** - Question, rubric, and student code loading
2. **Prompt Layer** - Strategy-specific prompt construction
3. **Evaluation Layer** - Multi-model LLM evaluation with structured outputs
4. **Storage Layer** - JSON document persistence
5. **Analysis Layer** - Misconception aggregation and reporting

```
┌─────────────────────────────────────────────────────────────┐
│                    EVALUATION PIPELINE                       │
└─────────────────────────────────────────────────────────────┘

Input (Question + Rubric + Student Code)
    │
    ▼
Prompt Generation (Direct/Reverse/EME)
    │
    ▼
Multi-Model LLM Evaluation (OpenRouter/OpenAI)
    │
    ▼
Structured Response Parsing (Pydantic + Instructor)
    │
    ▼
EvaluationDocument Assembly
    │
    ├──▶ Context (Course/Assignment/Question)
    ├──▶ Submission (Student Files)
    ├──▶ Rubric (Categories & Criteria)
    └──▶ Models (Per-Model Evaluations)
    │
    ▼
JSON Output (student_evals/*.json)
    │
    ▼
[Optional] Misconception Analysis
    │
    ├──▶ Fuzzy Clustering
    ├──▶ Topic Normalization
    ├──▶ Model Agreement
    └──▶ Markdown Report
```

## Pipeline Architecture

### Phase 1: Discovery & Loading

```python
# cli.py
students = get_student_list("student_submissions/")
# Returns: ["Chen_Wei_200023", "Alice_Smith_200024", ...]

# utils/grading.py
question = load_question("data/a2/q1.md")
rubric = load_rubric("data/a2/rubric_q1.md")
code = load_student_submission(student_id, "Q1.java")
```

### Phase 2: Prompt Construction

Three grading strategies are available in `prompts/`:

| Strategy | File | Description |
|----------|------|-------------|
| Direct | `direct_prompt.py` | Grade code directly against rubric |
| Reverse | `reverse_prompt.py` | Generate ideal solution, then compare |
| EME | `eme_prompt.py` | Multi-model ensemble with 100-point normalization |

```python
# prompts/direct_prompt.py
def build_prompt(question: str, rubric_json: dict, student_code: str) -> str:
    # Returns formatted prompt string
```

### Phase 3: Multi-Model Evaluation

```python
# utils/openrouter_sdk.py
async def get_structured_response(
    messages: list[dict],
    response_model: type[T],  # Pydantic model
    model: str = "google/gemini-2.5-flash-lite"
) -> T:
    # Returns validated Pydantic model instance
```

The SDK uses:
- **Instructor library** for structured output enforcement
- **Tenacity** for automatic retries (3 attempts, exponential backoff)
- **AsyncOpenAI client** pointing to OpenRouter API

### Phase 4: Document Assembly

```python
# utils/grading.py
def create_evaluation_document(
    student_id: str,
    student_name: str,
    question_text: str,
    rubric_data: dict,
    student_file_name: str,
    model_evaluations: dict[str, ModelEvaluation],
    ...
) -> EvaluationDocument:
    # Returns complete evaluation document
```

### Phase 5: Analysis (Optional)

```python
# utils/misconception_analyzer.py
analyzer = MisconceptionAnalyzer(evals_dir="student_evals")
analyzer.load_evaluations()
analyzer.extract_misconceptions()
class_analysis = analyzer.analyze_class()
analyzer.generate_markdown_report("misconception_report.md")
```

## Component Details

### CLI (`cli.py`)

**Framework:** Typer + Rich

**Key Functions:**

| Function | Purpose |
|----------|---------|
| `main()` | Interactive menu dispatch |
| `run_grading()` | Orchestrates batch grading workflow |
| `run_misconception_analysis()` | Runs analysis and displays results |
| `batch_grade_students()` | Async parallel grading with semaphore |
| `grade_student_with_models()` | Grades one student against all models |
| `process_student_wrapper()` | Worker function for one student (Q1-Q4) |

### Grading Utilities (`utils/grading.py`)

**Key Functions:**

| Function | Purpose |
|----------|---------|
| `load_question()` | Load question markdown |
| `load_rubric()` | Load rubric (JSON or markdown table) |
| `parse_markdown_rubric()` | Parse markdown table format |
| `construct_prompt()` | Build grading prompt from components |
| `grade_with_model()` | Async LLM call returning `ModelEvaluation` |
| `create_evaluation_document()` | Assemble complete document |

### OpenRouter SDK (`utils/openrouter_sdk.py`)

**Configuration:**
- Base URL: `https://openrouter.ai/api/v1`
- Uses Instructor for schema enforcement
- Retry: 3 attempts, 4-10s exponential backoff with jitter

```python
client = instructor.from_openai(
    AsyncOpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key=os.getenv("OPENROUTER_API_KEY"),
    ),
    mode=instructor.Mode.JSON,
)
```

### Misconception Analyzer (`utils/misconception_analyzer.py`)

**Key Classes:**

| Class | Purpose |
|-------|---------|
| `MisconceptionAnalyzer` | Main analyzer class |
| `MisconceptionRecord` | Single misconception with metadata |
| `StudentAnalysis` | Per-student analysis results |
| `ClassAnalysis` | Class-wide aggregated results |
| `TopicTaskStats` | Statistics for Topic+Task combinations |
| `QuestionStats` | Per-question statistics |
| `ProgressionAnalysis` | Q3→Q4 progression tracking |

**Topic Normalization:**

LLM-generated topics are mapped to 4 canonical topics:
1. Variables
2. Data Types
3. Constants
4. Reading input from the keyboard

See [Misconception Analysis](misconception-analysis.md) for fuzzy clustering details.

## Data Flow

### Grading Data Flow

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│   data/a2/       │     │ student_         │     │   cli.py         │
│   q1.md          │────▶│ submissions/     │────▶│                  │
│   rubric_q1.md   │     │ {student}/Q1.java│     │ batch_grade_     │
└──────────────────┘     └──────────────────┘     │ students()       │
                                                   └────────┬─────────┘
                                                            │
                              ┌──────────────────────────────┘
                              ▼
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ prompts/         │     │ utils/           │     │ OpenRouter       │
│ direct_prompt.py │◀────│ grading.py       │────▶│ API              │
│                  │     │ construct_prompt │     │ (via Instructor) │
└──────────────────┘     └──────────────────┘     └────────┬─────────┘
                                                            │
                              ┌──────────────────────────────┘
                              ▼
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ pydantic_models/ │     │ utils/           │     │ student_evals/   │
│ ModelEvaluation  │◀────│ grading.py       │────▶│ {student}_{q}_   │
│ EvaluationDoc    │     │ create_eval_doc  │     │ eval.json        │
└──────────────────┘     └──────────────────┘     └──────────────────┘
```

### Analysis Data Flow

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ student_evals/   │     │ MisconceptionAna │     │ ClassAnalysis    │
│ *_eval.json      │────▶│ lyzer            │────▶│ TopicTaskStats   │
│                  │     │ load_evaluations │     │ QuestionStats    │
└──────────────────┘     └──────────────────┘     └────────┬─────────┘
                                                            │
                              ┌──────────────────────────────┘
                              ▼
                         ┌──────────────────┐
                         │ misconception_   │
                         │ report.md        │
                         └──────────────────┘
```

## Concurrency Model

The CLI uses a **two-level parallelism** strategy:

### Level 1: Student Parallelism

A semaphore limits concurrent students to avoid rate limiting:

```python
MAX_CONCURRENT_STUDENTS = 5
sem = asyncio.Semaphore(MAX_CONCURRENT_STUDENTS)

async with sem:
    # Process one student (Q1-Q4)
```

### Level 2: Model Parallelism

Within each student, all models are called in parallel:

```python
async def grade_student_with_models(student_code, question, rubric):
    tasks = [grade_with_model(model, messages) for model in MODELS]
    results = await asyncio.gather(*tasks)
    return dict(zip(MODELS, results))
```

### Visualization

```
Time ──────────────────────────────────────────────────────▶

Student 1: ┌──────────────────────────┐
           │ Q1: [Gemini][GPT-5] ──▶  │
           │ Q2: [Gemini][GPT-5] ──▶  │
           │ Q3: [Gemini][GPT-5] ──▶  │
           │ Q4: [Gemini][GPT-5] ──▶  │
           └──────────────────────────┘

Student 2: ┌──────────────────────────┐
           │ Q1: [Gemini][GPT-5] ──▶  │  (concurrent with Student 1)
           ...
           └──────────────────────────┘

... up to MAX_CONCURRENT_STUDENTS (5)
```

## Extension Points

### Adding New Models

1. Add to `MODELS` list in `cli.py`:
```python
MODELS = [
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5-nano",
    "anthropic/claude-3-sonnet",  # New model
]
```

### Adding New Prompt Strategies

1. Create `prompts/new_strategy.py`:
```python
def build_prompt(question: str, rubric_json: dict, student_code: str) -> str:
    # Return formatted prompt
```

2. Import and use in `utils/grading.py`

### Adding New Comparison Metrics

1. Create models in `pydantic_models/comparison/`
2. Implement computation in `utils/comparison_generator.py`

### Adding New Misconception Topics

Update `TOPIC_MAPPING` in `utils/misconception_analyzer.py`:
```python
TOPIC_MAPPING = {
    "new topic variant": "Canonical Topic",
    ...
}
```

## Related Documentation

- [Grading Workflow](grading-workflow.md) - Step-by-step grading process
- [Pydantic Models](pydantic-models.md) - Data model reference
- [CLI Reference](cli-reference.md) - Command documentation
- [Configuration](configuration.md) - Setup and customization
