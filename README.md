# LLM Notional Machine Misconception Detection Framework

A research framework for detecting notional machine misconceptions in CS1 student code using Large Language Models. Part of a Bachelor's Honours Thesis at UBCO investigating cognitive alignment between LLMs and CS education theory.

## Research Goal

> Can LLMs detect **Notional Machine** misconceptions—flawed mental models about how code executes?

This is **not** a grading tool. It measures the **Cognitive Alignment** of LLMs with CS Education theory.

## The 3-Stage Pipeline

```
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│ 1. SYNTHETIC        │    │ 2. BLIND            │    │ 3. SEMANTIC         │
│    INJECTION        │───▶│    DETECTION        │───▶│    ALIGNMENT        │
│                     │    │                     │    │                     │
│ groundtruth.json    │    │ 3 LLMs × 4 prompts  │    │ Fuzzy/Semantic/     │
│ → manifest.json     │    │ → detections/       │    │ Hybrid matching     │
│ → Java files        │    │                     │    │ → report.md         │
└─────────────────────┘    └─────────────────────┘    └─────────────────────┘
```

## Quick Start

```bash
# Clone and install
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli
uv sync

# Configure API keys
echo "OPENROUTER_API_KEY=sk-or-..." > .env
echo "OPENAI_API_KEY=sk-..." >> .env

# Run full pipeline (interactive)
uv run pipeline

# Or run specific stages
uv run miscons              # Generate synthetic students
uv run llm-miscons detect   # Run LLM detection
uv run analyze analyze      # Generate analysis report
```

## Documentation

| Document                                                     | Description                                               |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| [Architecture](docs/architecture.md)                         | System design and data flow                               |
| [CLI Reference](docs/cli-reference.md)                       | Commands and options                                      |
| [Analysis Methodology](docs/analysis-methodology.md)         | **Complete guide to the analysis pipeline** (start here!) |
| [Understanding the Report](docs/understanding-the-report.md) | How to read and interpret reports                         |
| [Prompt Strategies](docs/prompts.md)                         | 4 detection strategies                                    |
| [Matching](docs/matching.md)                                 | Fuzzy, semantic, hybrid matchers                          |
| [Notional Machines](docs/notional-machines.md)               | Theoretical framework                                     |
| [Development](docs/development.md)                           | Extending the framework                                   |
| [AGENTS.md](AGENTS.md)                                       | AI agent guidance                                         |

## Project Structure

```
ensemble-eval-cli/
├── data/a1/                 # Ground truth and questions
│   ├── groundtruth.json     # Misconception taxonomy (8 types)
│   └── q*.md                # Question prompts
├── authentic_seeded/a1/     # Generated student files
│   ├── manifest.json        # Student→misconception mapping
│   └── {Student_Name}/      # Java files
├── detections/a1/           # LLM detection outputs
│   ├── baseline/
│   ├── taxonomy/
│   ├── cot/
│   └── socratic/
├── runs/a1/                 # Analysis results
│   └── run_{id}/
│       ├── report.md
│       ├── config.json
│       └── assets/          # Visualizations
├── prompts/strategies.py    # 4 prompt strategies
├── utils/
│   ├── matching/            # Fuzzy, semantic, hybrid
│   ├── generators/          # Synthetic data generation
│   └── llm/                 # OpenRouter API
├── pipeline.py              # Full pipeline orchestrator
├── analyze_cli.py           # Metrics and reporting
└── llm_miscons_cli.py       # Detection CLI
```

## The 5 Notional Machines

| Category                | Belief                         | Example Misconception      |
| ----------------------- | ------------------------------ | -------------------------- |
| **Reactive State**      | Variables update automatically | Early Calculation          |
| **Anthropomorphic I/O** | Computer reads prompt text     | Prompt-Logic Mismatch      |
| **Fluid Type**          | Division always gives decimals | Integer Division Blindness |
| **Algebraic Syntax**    | Math notation works in code    | XOR as Power               |
| **Void Machine**        | Methods modify in place        | The Void Assumption        |

## Detection Models

| Model            | Provider  |
| ---------------- | --------- |
| GPT-5.1          | OpenAI    |
| Gemini-2.5-Flash | Google    |
| Claude Haiku-4.5 | Anthropic |

Plus `:reasoning` variants for each.

## Prompt Strategies

| Strategy     | Philosophy                            |
| ------------ | ------------------------------------- |
| **baseline** | Simple error classification (control) |
| **taxonomy** | Explicit notional machine categories  |
| **cot**      | Chain-of-thought execution tracing    |
| **socratic** | Mental model probing                  |

## Sample Results

From a typical run (hybrid matcher):

| Metric                     | Value                    |
| -------------------------- | ------------------------ |
| Potential Recall (Ceiling) | 100%                     |
| Average Recall             | 86%                      |
| Best F1                    | 0.84 (Gemini + baseline) |

## Environment Variables

```bash
OPENROUTER_API_KEY=sk-or-...  # Required: LLM API access
OPENAI_API_KEY=sk-...         # Required: Semantic matching
```

## Research Context

**Project:** Honours Thesis - LLM Detection of Notional Machine Misconceptions
**Institution:** University of British Columbia Okanagan (UBCO)
**Researcher:** Shlok Shah
**Academic Year:** 2024-2025
**Target Venue:** ITiCSE/SIGCSE

## Citation

```bibtex
@software{misconception_framework_2025,
  author = {Shah, Shlok},
  title = {LLM Notional Machine Misconception Detection Framework},
  year = {2025},
  institution = {University of British Columbia Okanagan},
  note = {Honours Thesis Research Project}
}
```

## License

TBD (Academic Research Project)
