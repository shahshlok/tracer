# Documentation Index

The Ensemble Model Evaluation (EME) documentation is organized as a compact technical manual for instructors, applied ML engineers, and researchers. This index lists the primary entry points and how they relate.

## Overview

| Document | Primary audience | Purpose |
|----------|------------------|---------|
| [`00-QUICK-START.md`](00-QUICK-START.md) | All users | Install, configure, run the reference workflow |
| [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) | All users | Conceptual model, terminology, evaluation lifecycle |
| [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) | Developers | Codebase layout and module responsibilities |
| [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) | Practitioners | End-to-end usage, single/batch evaluation, configuration |
| [`04-API-REFERENCE.md`](04-API-REFERENCE.md) | Developers | Pydantic models, utility functions, prompt builders |
| [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) | Advanced | System design, data flow, extension patterns, testing |

All documents assume familiarity with Python, JSON, and basic LLM API concepts.

---

## Task-Oriented Map

### Setup and First Run

- Install and run the reference example: [`00-QUICK-START.md`](00-QUICK-START.md)
- Understand what the system is computing: [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)

### Using the Evaluation Pipeline

- Evaluate a single student: [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) — “Evaluating Your First Student”
- Evaluate a full cohort: [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) — “Evaluating Multiple Students”
- Switch and tune models: [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) — “Choosing Different AI Models”
- Interpret the JSON output: [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) — “Understanding Your Results”

### CLI and Automation

- Async batch CLI (Typer + Rich): `README.md` and [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)
- Orchestration helpers for grading: [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) (root files, `utils/grading.py`)

### Codebase and Architecture

- File-level organization and responsibilities: [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
- Pipeline and data flow diagrams: [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) — “Data Flow Pipeline”
- Extension patterns (providers, prompts, comparison): [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) — “Extension Guide”

### Data Models and APIs

- Pydantic model reference: [`04-API-REFERENCE.md`](04-API-REFERENCE.md) — “Data Models”
- LLM integration helpers: [`04-API-REFERENCE.md`](04-API-REFERENCE.md) — “Utility Functions”
- Prompt construction strategies: [`04-API-REFERENCE.md`](04-API-REFERENCE.md) and [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)

### Troubleshooting and Testing

- Installation and environment issues: [`00-QUICK-START.md`](00-QUICK-START.md) — “Troubleshooting”
- Validation and schema errors: [`04-API-REFERENCE.md`](04-API-REFERENCE.md) — “Error Handling”
- Testing strategy and patterns: [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) — “Testing Strategy”

---

## Quick “Where Do I Read?” Guide

- Minimal to run and inspect an evaluation: `00-QUICK-START.md` → `03-USAGE-GUIDE.md`
- Minimal to understand the data structures: `01-GETTING-STARTED.md` → `04-API-REFERENCE.md`
- Minimal to extend or integrate into your own tooling: `02-PROJECT-STRUCTURE.md` → `05-ARCHITECTURE.md`

---

## Search Guide

Looking for a specific topic:

- API functions and signatures → [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
-,Data models and schema semantics → [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
- Architecture and design decisions → [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
- File locations and module boundaries → [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
- Evaluation workflows and examples → [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)
- Installation, environment, and keys → [`00-QUICK-START.md`](00-QUICK-START.md)


---

## Document Conventions

**Terminology boxes** - Explain unfamiliar terms
```
A "misconception" is when a student shows a misunderstanding
of a programming concept. Example: Using mutable default arguments.
```

**Code blocks** - Show actual code to copy/use
```python
from pydantic_models import EvaluationDocument
eval_doc = EvaluationDocument(...)
```

**Command blocks** - Commands to run in terminal
```bash
uv run python grade_sergio.py
```

**Diagrams** - Show how components interact
```
Input → Processing → Output
```

**Tips** - Helpful advice
> **Tip:** Use the example script as a template for your evaluations.

**Warnings** - Important information
> **⚠️ Important:** Never commit your `.env` file with API keys!

---

## Feedback & Updates

These documents are maintained along with the project.

**Found a typo or unclear section?**
→ Create an issue on GitHub

**Want to request new documentation?**
→ Create an issue on GitHub

**Want to contribute improvements?**
→ Submit a pull request

---

## Related Resources

**External Links:**
- [Pydantic Documentation](https://docs.pydantic.dev/) - Data validation library
- [OpenAI API Docs](https://platform.openai.com/docs) - OpenAI integration
- [OpenRouter Docs](https://openrouter.ai/docs) - Multi-model support
- [Python Official Docs](https://docs.python.org/3/) - Python language

---

## Quick Start Checklist

- [ ] Read [`00-QUICK-START.md`](00-QUICK-START.md)
- [ ] Install dependencies (`uv sync`)
- [ ] Create `.env` file with API keys
- [ ] Run `uv run python grade_sergio.py`
- [ ] Read [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)
- [ ] Try [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) with your own student

---

**Don't know where to start?**
→ Begin with [`00-QUICK-START.md`](00-QUICK-START.md)

**Want to evaluate students now?**
→ Jump to [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)

**Need technical details?**
→ Go to [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
