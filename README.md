# Ensemble Model Evaluation (EME) Framework

Research-grade harness for grading student code submissions with multiple LLMs, analyzing misconception patterns, and evaluating ensemble grading strategies via structured cross-model analytics.

## Status

**Schema:** v1.0.0 (stable, Pydantic-based)  
**Implementation:** Evaluation pipeline + async batch CLI implemented; comparison/DB/analytics layers in progress.

---

## Documentation

All project documentation lives in `docs/`. The docs are written for instructors, applied ML practitioners, and researchers comfortable with Python and LLM APIs.

- Overview & installation: `docs/00-QUICK-START.md`
- Conceptual model and workflows: `docs/01-GETTING-STARTED.md`
- Codebase layout: `docs/02-PROJECT-STRUCTURE.md`
- End-to-end usage: `docs/03-USAGE-GUIDE.md`
- Data/API reference: `docs/04-API-REFERENCE.md`
- Architecture and extension: `docs/05-ARCHITECTURE.md`

Documentation index: `docs/INDEX.md`

---

### Implemented
- **Complete Pydantic schema**: All data models for evaluations, submissions, rubrics, and comparisons
- **Multi-model evaluation support**: OpenAI and OpenRouter SDK integration with Instructor
- **Structured LLM outputs**: Enforced JSON schema validation for all model responses
- **Misconception tracking**: Inductive approach with evidence linking and model attribution
- **Comprehensive comparison models**: Score analysis, reliability metrics, ensemble decisions, and quality assessment
- **Three grading strategies**: Direct, Reverse, and Ensemble Method Evaluation (EME) prompts

### In Development
- **Comparison computation engine**: Score statistics, agreement metrics (ICC, Krippendorff's alpha, etc.), ensemble decisions
- **Database persistence**: SQLite-backed storage and query interface for historical evaluations
- **CLI extensions**: Additional commands for loading, validating, and analyzing evaluation corpora
- **Visualization tools**: Dashboards for ensemble behaviour, agreement heatmaps, and misconception distributions

### Architecture Overview

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
Multi-Model LLM Evaluation (OpenAI/OpenRouter)
    │
    ▼
Structured Response Parsing (Pydantic Models)
    │
    ▼
EvaluationDocument Assembly
    │
    ├──> Context (Course/Assignment/Question)
    ├──> Submission (Student Files)
    ├──> Rubric (Categories & Criteria)
    └──> Models (Per-Model Evaluations)
    │
    ▼
[Future] Comparison Analysis
    │
    ├──> Score Statistics & Agreement
    ├──> Misconception Overlap Analysis
    ├──> Confidence Patterns
    ├──> Reliability Metrics (ICC, Pearson, Spearman, Krippendorff's α)
    └──> Ensemble Decision & Quality Assessment
    │
    ▼
JSON Output (student_evals/*.json)
```

---

## Quick Start

Minimal setup to execute the provided example workflow:

```bash
# 1. Clone repository
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli

# 2. Install dependencies
uv sync

# 3. Create .env file with your API keys
# (See docs/00-QUICK-START.md for details)
echo "OPENAI_API_KEY=sk-..." > .env
echo "OPENROUTER_API_KEY=sk-or-..." >> .env

# 4. Run the example script
uv run python grade_sergio.py
```

**Results saved to:** `student_evals/sergio_eval.json`

For a more detailed description of the pipeline and configuration options, see `docs/00-QUICK-START.md` and `docs/03-USAGE-GUIDE.md`.

---

## Batch CLI (Async Benchmark Harness)

An async batch CLI is provided for running ensemble evaluations over a cohort of students and summarizing cross-model behaviour.

From the project root:

```bash
# Grade a batch of students using the async CLI
uv run python cli.py bench
```

The CLI:
- Discovers student IDs under `student_submissions/`
- Loads `question_cuboid.md` and `rubric_cuboid.json`
- Evaluates each student against the configured models in parallel
- Writes one `*_eval.json` per student under `student_evals/`
- Renders a comparative table (per-model scores, average, disagreement flags, confidence)

See `cli.py` and `utils/grading.py` for the orchestration logic, and `docs/03-USAGE-GUIDE.md` for integrating the CLI into your own assignment/rubric layouts.

---

## Features

### Evaluation Capabilities

✅ **Multi-Model Grading**
- Support for OpenAI models (GPT-4, GPT-4o, etc.)
- Support for OpenRouter providers (Gemini, Claude, Kimi, Llama, etc.)
- Parallel evaluation across multiple models for ensemble analysis
- Configurable model selection and parameters

✅ **Three Grading Strategies**
- **Direct Grading** (`prompts/direct_prompt.py`): Grade student code directly against rubric
- **Reverse Grading** (`prompts/reverse_prompt.py`): Generate ideal solution first, then compare student work
- **Ensemble Method Evaluation** (`prompts/eme_prompt.py`): Multi-model ensemble approach with structured 100-point rubric enforcement

✅ **Structured Output Validation**
- Pydantic models enforce type safety and schema compliance
- Automatic validation of all LLM responses
- Field-level descriptions for comprehensive documentation
- Support for nested structures and complex validation rules

✅ **Rich Misconception Detection**
- Inductive approach: models identify misconceptions directly from student code
- Evidence linking: each misconception tied to specific code lines and snippets
- Confidence scoring: models rate their certainty in each identified misconception
- Cross-model comparison: track which models identify which misconceptions

### Data Models & Schema

✅ **Comprehensive Pydantic Models** (`pydantic_models/`)
- **Context**: Course, assignment, and question metadata
- **Submission**: Student files, submission time, programming language
- **Rubric**: Hierarchical grading criteria with category weights
- **ModelEvaluation**: Per-model scores, category breakdowns, feedback, and misconceptions
- **Comparison** (designed, not yet computed):
  - Score statistics and pairwise model differences
  - Category-level agreement analysis
  - Misconception overlap and patterns
  - Confidence analysis and model characteristics
  - Inter-rater reliability metrics (ICC, Pearson, Spearman, Krippendorff's alpha)
  - Ensemble decision strategies (mean, median, weighted)
  - Quality assessment and automated review flags

### Integration & Extensibility

✅ **Flexible LLM Integration**
- OpenAI structured outputs API via `utils/openai_client.py`
- OpenRouter SDK with Instructor library via `utils/openrouter_sdk.py`
- Generic interface for adding new providers
- Fallback handling and error recovery

✅ **Data Validation**
- Schema version tracking and validation
- Type checking across all models
- Business logic validation (score calculations, percentages)
- Graceful error handling with detailed error messages

---

## Project Structure

```
ensemble-eval-cli/
├── docs/                        # 📚 Complete documentation
│   ├── 00-QUICK-START.md
│   ├── 01-GETTING-STARTED.md
│   ├── 02-PROJECT-STRUCTURE.md
│   ├── 03-USAGE-GUIDE.md
│   ├── 04-API-REFERENCE.md
│   ├── 05-ARCHITECTURE.md
│   └── INDEX.md
│
├── pydantic_models/             # Data definitions (Pydantic v2)
├── prompts/                     # Grading strategies
├── utils/                       # LLM integrations and grading helpers
├── tests/                       # Test suite
│
├── student_submissions/         # Input: student code (one folder per student)
├── student_evals/               # Output: evaluation results (one JSON per eval)
│
├── cli.py                       # Async batch CLI (Typer + Rich)
├── grade_sergio.py              # Single-student example workflow
├── question_cuboid.md           # Example assignment
├── rubric_cuboid.json           # Example grading rubric
├── pyproject.toml               # Project configuration
└── .env                         # API keys (not in repo)
```

For a complete walkthrough of the directory layout and model dependencies, see `docs/02-PROJECT-STRUCTURE.md`.

---

## Usage Guide

**Full usage guide with step-by-step examples:** [`docs/03-USAGE-GUIDE.md`](docs/03-USAGE-GUIDE.md)

Topics covered:
- Evaluating your first student
- Batch evaluating entire classes
- Choosing different AI models
- Understanding evaluation results
- Advanced usage patterns

---

## Structured LLM Outputs

The framework uses **Pydantic models** to ensure reliable, schema-validated evaluations from language models.

**For complete details:** [`docs/04-API-REFERENCE.md`](docs/04-API-REFERENCE.md#structured-lllm-outputs)

Key features:
- Type-safe evaluation responses
- Automatic validation
- Clear error messages on invalid data
- No parsing errors or data loss

---

## Testing

```bash
# Run all tests
uv run pytest

# Run with verbose output
uv run pytest -v
```

**Current coverage:** ~5-10%

**For testing best practices:** [`docs/05-ARCHITECTURE.md`](docs/05-ARCHITECTURE.md#testing-strategy)

---

## Configuration

**For configuration details:** [`docs/03-USAGE-GUIDE.md`](docs/03-USAGE-GUIDE.md#choosing-different-ai-models)

Supported models:
- **OpenAI:** GPT-4, GPT-4o, GPT-3.5-turbo
- **Anthropic:** Claude 3 Opus, Sonnet
- **Google:** Gemini models
- **Others:** Via OpenRouter (Moonshot, Llama, etc.)

---

## Output Format

Evaluation results are saved as JSON files in `student_evals/`.

**For output format details:** [`docs/03-USAGE-GUIDE.md`](docs/03-USAGE-GUIDE.md#understanding-your-results)

Each file contains:
- Student information
- Scores from each AI model
- Feedback (strengths and areas for improvement)
- Identified misconceptions with evidence
- Metadata (timestamps, model versions, etc.)

---

## Development Roadmap

### Phase 1: Schema & Infrastructure ✅ **COMPLETE**
- [x] Finalize evaluation JSON schema (v1.0.0)
- [x] Define misconception structure (inductive approach)
- [x] Design comprehensive comparison metrics
- [x] Document all fields and interpretations
- [x] Implement complete Pydantic model hierarchy
- [x] Integrate OpenAI and OpenRouter SDKs
- [x] Create example evaluation workflow (`grade_sergio.py`)

### Phase 2: Comparison Engine **IN PROGRESS**
- [ ] Implement comparison computation logic
  - [ ] Score statistics calculation (mean, median, std dev, SEM)
  - [ ] Pairwise model comparisons
  - [ ] Category-level agreement analysis
  - [ ] Misconception overlap detection
  - [ ] Confidence pattern analysis
- [ ] Implement reliability metrics
  - [ ] Intraclass Correlation Coefficient (ICC)
  - [ ] Pearson correlation
  - [ ] Spearman rank correlation
  - [ ] Krippendorff's alpha
- [ ] Implement ensemble decision strategies
  - [ ] Mean/median score aggregation
  - [ ] Weighted ensemble (by confidence)
  - [ ] Quality assessment metrics

### Phase 3: CLI & Database
- [ ] Build interactive CLI (using Typer & Rich)
  - [ ] Batch evaluation interface
  - [ ] Model selection and configuration
  - [ ] Progress reporting
- [ ] Implement SQLite database persistence
  - [ ] Schema migration from JSON
  - [ ] Query interface for historical data
  - [ ] Export and analysis tools

### Phase 4: Analysis & Visualization
- [ ] LLM-powered misconception pattern analysis
- [ ] Statistical analysis scripts
- [ ] Instructor dashboard with visualizations
  - [ ] Score distributions
  - [ ] Model agreement heatmaps
  - [ ] Misconception frequency charts
  - [ ] Confidence vs accuracy plots

### Phase 5: Research Applications
- [ ] Ensemble strategy comparison studies
- [ ] Model reliability & calibration analysis
- [ ] Misconception clustering and insights
- [ ] Publication-ready data exports

---

## Current Limitations & Future Work

### Known Limitations
- Single-file submissions: current workflow assumes one code file per student
- No database: evaluations are stored as individual JSON files
- Comparison logic not implemented: comparison models exist, but computation is not wired
- Manual model configuration: model lists and parameters are edited in code/CLI config

### Planned Enhancements
1. **Multi-file submission support**
   - Handle projects with multiple files (main class, tests, utilities)
   - Concatenate files with clear delimiters for LLM context
   - Track individual file metadata
   - Support mixed file types (`.java`, `.py`, `.cpp`, etc.)

2. **Batch evaluation**
   - Process entire classrooms of students
   - Parallel model queries for efficiency
   - Progress tracking and resume capability

3. **Interactive CLI**
   - Menu-driven interface for non-programmers
   - Configuration wizards
   - Result browsing and filtering

4. **Advanced analytics**
   - Cohort-level statistics
   - Longitudinal tracking across assignments
   - Anomaly detection (unusual scoring patterns)
   - Model calibration studies

---

## Contributing

**For detailed development guidance:** [`docs/05-ARCHITECTURE.md`](docs/05-ARCHITECTURE.md#extension-guide)

When adding features:
1. Follow architectural patterns
2. Maintain schema integrity with Pydantic
3. Document thoroughly
4. Write tests with good coverage

---

## Research Context

**Project:** Honours Thesis Research - Ensemble Model Evaluation for Code Grading
**Institution:** University of British Columbia Okanagan (UBCO)
**Course:** COSC 499 - Honours Thesis
**Researcher:** Shlok Shah
**Academic Year:** 2024-2025

## Citation

If you use this schema or framework in your research, please cite:

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
