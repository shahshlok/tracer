# Ensemble Model Evaluation (EME) CLI

A research-grade framework for grading student code submissions using multiple Large Language Models, with capabilities for analyzing misconception patterns, evaluating ensemble grading strategies, and generating cross-model analytics.

**Research Context:** Honours Thesis Research at UBCO, investigating ensemble methods for automated code grading.

## Status

| Component | Status |
|-----------|--------|
| Pydantic Schema | v1.0.0 (stable) |
| Evaluation Pipeline | ✅ Complete |
| Async Batch CLI | ✅ Complete |
| Misconception Analysis | ✅ Complete |
| Comparison Engine | 🔄 In Progress |

## Quick Start

```bash
# Clone and install
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli
uv sync

# Configure API keys
echo "OPENROUTER_API_KEY=sk-or-..." > .env

# Run grading
uv run python cli.py grade

# Run misconception analysis
uv run python cli.py analyze
```

**Output:** `student_evals/{student_id}_{question}_eval.json`

## Documentation

| Document | Description |
|----------|-------------|
| [Architecture](docs/architecture.md) | System design, data flow, and component relationships |
| [CLI Reference](docs/cli-reference.md) | Commands, options, and usage examples |
| [Grading Workflow](docs/grading-workflow.md) | End-to-end grading process explained |
| [Misconception Analysis](docs/misconception-analysis.md) | Fuzzy clustering and pattern detection |
| [Pydantic Models](docs/pydantic-models.md) | Complete data model reference |
| [Prompt Strategies](docs/prompts.md) | Direct, Reverse, and EME grading approaches |
| [Configuration](docs/configuration.md) | Setup, environment variables, and customization |

## Features

### Multi-Model Grading
- OpenAI models (GPT-4, GPT-4o, GPT-5 series)
- OpenRouter providers (Gemini, Claude, Llama, etc.)
- Parallel evaluation with configurable concurrency
- Structured output validation via Pydantic

### Three Grading Strategies
- **Direct Grading:** Evaluate code directly against rubric
- **Reverse Grading:** Generate ideal solution, then compare
- **EME (Ensemble Method Evaluation):** Multi-model ensemble with 100-point normalization

### Misconception Detection
- Inductive approach: models identify misconceptions from code
- Evidence linking with code snippets and line numbers
- Fuzzy clustering to merge similar misconception names
- Cross-model agreement tracking

### Rich Analytics
- Per-question difficulty analysis
- Topic-based misconception aggregation
- Q3→Q4 progression tracking
- Model agreement heatmaps

## Project Structure

```
ensemble-eval-cli/
├── docs/                    # Documentation
├── pydantic_models/         # Data models (Pydantic v2)
│   ├── evaluation.py        # Root EvaluationDocument
│   ├── context/             # Course/assignment metadata
│   ├── submission/          # Student files
│   ├── rubric/              # Grading criteria
│   ├── models/              # Per-model evaluations
│   └── comparison/          # Multi-model analysis
├── prompts/                 # Grading strategies
│   ├── direct_prompt.py
│   ├── reverse_prompt.py
│   └── eme_prompt.py
├── utils/                   # Core utilities
│   ├── grading.py           # Grading orchestration
│   ├── openrouter_sdk.py    # LLM integration
│   ├── misconception_analyzer.py
│   └── comparison_generator.py
├── data/a2/                 # Questions and rubrics
├── student_submissions/     # Input: student code
├── student_evals/           # Output: evaluation JSONs
└── cli.py                   # Main CLI entry point
```

## Usage

### Interactive Mode

```bash
uv run python cli.py
```

Presents a menu:
1. **Grade Students** - Run batch grading
2. **Analyze Misconceptions** - Analyze existing evaluations
3. **Exit**

### Direct Commands

```bash
# Grade all students in student_submissions/
uv run python cli.py grade

# Analyze misconceptions from student_evals/
uv run python cli.py analyze
```

### Sandbox Experiment

```bash
# Single-student evaluation for testing
uv run python sandbox/single_submission.py
```

## Output Format

Evaluations are saved as JSON files conforming to the `EvaluationDocument` schema:

```json
{
  "evaluation_id": "eval_abc123",
  "schema_version": "1.0.0",
  "context": { "course_id": "COSC121", "question_id": "q1" },
  "submission": { "student_id": "Chen_Wei_200023", "files": [...] },
  "rubric": { "total_points": 4, "categories": [...] },
  "models": {
    "google/gemini-2.5-flash-lite": { "scores": {...}, "misconceptions": [...] },
    "openai/gpt-5-nano": { "scores": {...}, "misconceptions": [...] }
  }
}
```

## Configuration

### Environment Variables

```bash
# Required for OpenRouter models
OPENROUTER_API_KEY=sk-or-...

# Optional: Direct OpenAI access
OPENAI_API_KEY=sk-...
```

### In-Code Configuration (`cli.py`)

```python
MAX_CONCURRENT_STUDENTS = 5  # Reduce if hitting rate limits
BATCH_LIMIT = 25             # Students per batch
MODELS = [
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5-nano",
]
```

## Testing

```bash
uv run pytest
uv run pytest -v  # Verbose
```

## Research Context

**Project:** Honours Thesis - Ensemble Model Evaluation for Code Grading  
**Institution:** University of British Columbia Okanagan (UBCO)  
**Researcher:** Shlok Shah  
**Academic Year:** 2024-2025

## Citation

```bibtex
@software{eme_framework_2025,
  author = {Shah, Shlok},
  title = {Ensemble Model Evaluation Framework for Code Grading},
  year = {2025},
  institution = {University of British Columbia Okanagan},
  note = {Honours Thesis Research Project}
}
```

## License

TBD (Academic Research Project)
