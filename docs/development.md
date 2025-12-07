# Development Guide

This document covers how to extend and develop the ensemble-eval-cli framework.

## Setup

```bash
# Clone
git clone https://github.com/shahshlok/ensemble-eval-cli
cd ensemble-eval-cli

# Install with uv
uv sync

# Configure API keys
cp .env.example .env
# Edit .env with your keys
```

### Required Environment Variables

```bash
OPENROUTER_API_KEY=sk-or-...  # For LLM detection
OPENAI_API_KEY=sk-...         # For semantic matching (embeddings)
```

---

## Running Tests

```bash
uv run pytest
uv run pytest -v  # Verbose
uv run pytest tests/test_matching.py  # Specific test
```

---

## Adding a New Assignment

1. **Create data directory:**
   ```bash
   mkdir -p data/a2
   ```

2. **Add question files:**
   ```
   data/a2/q1.md
   data/a2/q2.md
   ...
   ```

3. **Create groundtruth.json** with misconceptions applicable to the new questions

4. **Update ASSIGNMENTS in `dataset_generator.py`:**
   ```python
   ASSIGNMENTS = {
       "a1": {...},
       "a2": {
           "question_files": {
               "Q1": Path("data/a2/q1.md"),
               ...
           },
           "question_briefs": {
               "Q1": "Calculate widget cost",
               ...
           },
           "groundtruth": Path("data/a2/groundtruth.json"),
       },
   }
   ```

5. **Generate dataset:**
   ```bash
   uv run pipeline run --skip-detection --skip-analysis
   ```

---

## Adding a New LLM Model

1. **Add to `llm_miscons_cli.py`:**
   ```python
   MODELS = [
       "openai/gpt-5.1",
       "google/gemini-2.5-flash-preview-09-2025",
       "anthropic/claude-haiku-4.5",
       "your-provider/your-model",  # Add here
   ]
   
   MODEL_SHORT_NAMES = {
       ...,
       "your-provider/your-model": "YourModel",
   }
   ```

2. **Ensure OpenRouter supports the model** (or add custom API client)

---

## Adding a New Prompt Strategy

1. **Add builder in `prompts/strategies.py`:**
   ```python
   def build_my_strategy_prompt(problem_description: str, student_code: str) -> str:
       return f'''Your custom prompt...
       
       {OUTPUT_SCHEMA}
       '''.strip()
   ```

2. **Add to enum:**
   ```python
   class PromptStrategy(str, Enum):
       ...
       MY_STRATEGY = "my_strategy"
   ```

3. **Register:**
   ```python
   STRATEGIES = {
       ...
       PromptStrategy.MY_STRATEGY: build_my_strategy_prompt,
   }
   ```

---

## Adding a New Matcher

1. **Create `utils/matching/my_matcher.py`:**
   ```python
   def my_match_misconception(
       detected_name: str,
       detected_description: str,
       groundtruth: list[dict],
       threshold: float = 0.5,
   ) -> tuple[str | None, float, str]:
       # Your matching logic
       return matched_id, score, "my_method"
   ```

2. **Add to `MatchMode` enum in `analyze_cli.py`:**
   ```python
   class MatchMode(str, Enum):
       ...
       MY_MATCHER = "my_matcher"
   ```

3. **Update `dispatch_matcher`:**
   ```python
   elif match_mode == MatchMode.MY_MATCHER:
       result = my_match_misconception(...)
   ```

---

## Code Style

- **Formatter:** Ruff
- **Line length:** 100
- **Python:** 3.10+

```bash
uv run ruff format .
uv run ruff check .
```

---

## Key Files to Understand

| File                                    | Purpose                                 |
| --------------------------------------- | --------------------------------------- |
| `pipeline.py`                           | Main orchestrator                       |
| `analyze_cli.py`                        | Metrics and visualization (2500+ lines) |
| `llm_miscons_cli.py`                    | LLM API calls                           |
| `utils/generators/dataset_generator.py` | Synthetic data                          |
| `prompts/strategies.py`                 | Prompt templates                        |
| `utils/matching/hybrid.py`              | Best matcher                            |
