# Quick Start Guide

Welcome to the Ensemble Model Evaluation (EME) Framework! This guide will help you get up and running in just a few minutes.

## What is this project?

The EME Framework automatically grades student code submissions using multiple AI models (like ChatGPT, Claude, Gemini). It compares how different models evaluate the same code, helping discover misconceptions and analyze grading patterns.

**Simple workflow:**
```
Student Code → Multiple AI Models → Compare Results → Analyze Patterns
```

## Installation (5 minutes)

### Step 1: Clone the repository

```bash
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli
```

### Step 2: Install dependencies

We recommend using `uv` (a fast Python package manager):

```bash
uv sync
```

Or with pip:

```bash
pip install -e .
```

### Step 3: Set up API keys

The system needs keys to access AI models. Create a `.env` file in the project root:

```bash
# Required: Get from https://platform.openai.com/api-keys
OPENAI_API_KEY=sk-...

# Required: Get from https://openrouter.ai/keys
OPENROUTER_API_KEY=sk-or-...
```

**Don't have API keys?**
- OpenAI: Sign up at https://platform.openai.com (includes free credits)
- OpenRouter: Sign up at https://openrouter.ai (routes to multiple models)

## Run Your First Evaluation (2 minutes)

Once installed, run the example:

```bash
uv run python grade_sergio.py
```

This will:
1. Load an example assignment (Cuboid class design)
2. Load example student code (Java)
3. Ask multiple AI models to grade the code
4. Save results to `student_evals/sergio_eval.json`

**Expected output:** A JSON file with scores from multiple models and identified misconceptions.

## What happens next?

After running the evaluation, you'll have:

```
student_evals/
└── sergio_eval.json          ← Results file with all grading data
```

This JSON file contains:
- Student info
- Scores from each AI model
- Feedback and misconceptions
- Confidence levels

## Next steps

- **👉 Learn the basics**: Read [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)
- **🏗️ Understand the structure**: Read [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
- **📚 Complete reference**: Read [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)
- **🔧 API details**: Read [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
- **🏛️ Architecture**: Read [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)

## Troubleshooting

**Error: "OpenAI API key not found"**
- Make sure your `.env` file has `OPENAI_API_KEY=sk-...`
- Check the file is in the project root (same directory as `README.md`)

**Error: "Module not found"**
- Run `uv sync` again to make sure dependencies are installed

**Error: "Rate limit exceeded"**
- You're making too many API requests. Wait a few seconds and try again.

---

**Need help?** Check the [documentation index](@docs/INDEX.md) or create an issue on GitHub.
