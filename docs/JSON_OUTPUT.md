# Structured JSON Outputs (OpenAI & OpenRouter)

This guide explains how EduBench is configured to get **strict,
structured JSON outputs** from all language models, using both the
OpenAI SDK and OpenRouter (for Gemini 2.5 Flash). It is written for
beginners and walks through concepts step-by-step.

If you only skim one section, read:
- [Quick Summary](#quick-summary)
- [How to Call the JSON Clients](#how-to-call-the-json-clients)

---

## Why Structured JSON Outputs?

Large language models (LLMs) usually answer with free-form text:

> “The student’s code is correct. Total score: 90/100…”

This is hard for programs to consume directly. For EduBench, we want
**structured grading data** (scores, feedback) that can be:

- Validated against a schema
- Stored in SQLite
- Analyzed and compared across runs

To make this reliable, we do two things:

1. We tell the model:  
   > “Do not write an essay. Return **only JSON** as your answer.”
2. We also give the model a **JSON schema** that defines the fields
   we expect (scores and feedback).

This is called **structured outputs** in the OpenAI docs, and both:

- the **OpenAI Python SDK**, and  
- **OpenRouter** (via an OpenAI-compatible API)

support it via a `response_format` parameter with
`type: "json_schema"`.

---

## Quick Summary

- EduBench now uses **structured JSON mode** for:
  - GPT‑5 Nano (OpenAI Responses API)
  - Gemini 2.5 Flash (via OpenRouter)
- Both clients are implemented with the **OpenAI Python SDK**.
- We pass a `response_format` that uses a **JSON schema**:

  ```python
  from utils.structured_outputs import get_grading_response_format

  response_format = get_grading_response_format()
  ```

  which tells the model:

  - Output must be a single valid JSON object.
  - It **must** contain: `total_score`, `max_possible_score`,
    `overall_feedback`.
  - Additional fields are allowed but not required.

- The JSON is then parsed and normalized so that downstream code
  always sees a dictionary with grading fields.

---

## Where the Clients Live in the Codebase

All model client code is under `utils/`:

- `utils/openai_client.py`
  - GPT‑5 Nano via OpenAI Responses API
- `utils/eduai_client.py`
  - GPT‑OSS 120B via EduAI HTTP API (unchanged, not JSON-mode aware)
- `utils/openrouter_client.py`
  - Gemini 2.5 Flash via OpenRouter → OpenAI SDK
- `utils/ai_clients.py`
  - A convenience **facade** that re-exports:
    - `get_openai_eval(...)`
    - `get_eduai_eval(...)`, `get_eduai_eval_async(...)`
    - `get_openrouter_eval(...)`

If you want to keep things simple, import from the facade:

```python
from utils.ai_clients import get_openai_eval, get_openrouter_eval
```

If you want more control over one provider, import from the specific
client module instead.

---

## OpenAI GPT‑5 Nano: Structured JSON Mode

**File:** `utils/openai_client.py`  
**Key function:** `_call_openai_eval`

### How It Calls the API

Internally, we use the **OpenAI Responses API** with structured outputs:

```python
import openai
from utils.structured_outputs import get_grading_response_format

response = openai.responses.create(
    model="gpt-5-nano",
    input=prompt,
    response_format=get_grading_response_format(),
)
```

Important details:

- `model="gpt-5-nano"` — The GPT‑5 Nano evaluation model.
- `input=prompt` — A single string prompt that must explain:
  - the question,
  - the rubric, and
  - the student code,
  and instruct the model to grade and **return only JSON**.
- `response_format=get_grading_response_format()` — This:
  - Forces JSON output.
  - Enforces a small schema with `total_score`, `max_possible_score`,
    and `overall_feedback`.

### What the Client Returns

`_call_openai_eval(prompt: str)`:

- Calls the API as above.
- Extracts the model’s textual output using `_extract_text_response`.
- Parses it into a Python `dict` using `_safe_json_loads`.
- Attaches helpful debugging fields:

  ```python
  parsed["_raw_response"]  # fully serialized API object
  parsed["_raw_content"]   # raw JSON string from the model
  ```

- Returns the parsed dict, or a fallback:

  ```python
  {"_raw_content": content, "_raw_response": serialized}
  ```

`get_openai_eval(prompt: str)` is the **async wrapper**:

- Uses a semaphore if `MAX_CONCURRENT_MODELS` is set.
- Runs `_call_openai_eval` in a thread.
- Returns the same structure.

### How to Use It

Typical usage (most callers already use this):

```python
from utils.ai_clients import get_openai_eval

result = await get_openai_eval(prompt)
```

Where:

- `prompt` is a string that explains the evaluation task.
- `result` is a dict with grading fields (if parsing succeeds) plus
  `_raw_*` metadata.

You do **not** need to set `response_format` yourself—this is baked
into the client.

---

## Gemini 2.5 Flash (via OpenRouter): Structured JSON Mode

**File:** `utils/openrouter_client.py`  
**Key function:** `_call_openrouter_eval`

OpenRouter exposes an **OpenAI-compatible** API. We use the *same* OpenAI
Python SDK, but point it at OpenRouter’s base URL.

### Configuration (Environment Variables)

In your `.env` file or environment, set:

```env
OPENROUTER_API_KEY=sk-or-...   # from openrouter.ai
OPENROUTER_MODEL=google/gemini-2.5-flash-preview-09-2025

# Optional, for rankings/analytics on OpenRouter:
OPENROUTER_SITE_URL=https://your-app-url.com
OPENROUTER_SITE_NAME=Your App Name
```

If `OPENROUTER_MODEL` is not set, the client defaults to:

```text
google/gemini-2.5-flash-preview-09-2025
```

### How It Calls the API

We construct an OpenAI client that talks to OpenRouter:

```python
from openai import OpenAI

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=os.environ["OPENROUTER_API_KEY"],
)

from utils.structured_outputs import get_grading_response_format

completion = client.chat.completions.create(
    model=model,  # e.g. google/gemini-2.5-flash-preview-09-2025
    messages=[{"role": "user", "content": prompt}],
    response_format=get_grading_response_format(),
    extra_headers={
        "HTTP-Referer": OPENROUTER_SITE_URL,  # if set
        "X-Title": OPENROUTER_SITE_NAME,      # if set
    } or None,
)
```

Notes for beginners:

- `base_url` is what makes this an **OpenRouter** call instead of a
  direct OpenAI call.
- The `messages` array is the standard Chat Completions format:
  - We only use a single `"user"` message that contains the full prompt.
- `response_format=get_grading_response_format()` again forces
  structured JSON output with the grading schema.
- `extra_headers` are optional and used by OpenRouter for their ranking UI.

### What the Client Returns

`_call_openrouter_eval(prompt: str)`:

- Reads `OPENROUTER_API_KEY` and `OPENROUTER_MODEL`.
- Builds the client and headers.
- Calls `chat.completions.create(...)` as shown above.
- Extracts the first choice’s message:

  ```python
  content = completion.choices[0].message.content
  ```

- Parses JSON using the same `_safe_json_loads` helper shared with the
  OpenAI client.
- Attaches `_raw_response` and `_raw_content` just like the OpenAI
  client.

`get_openrouter_eval(prompt: str)` is the **async wrapper**:

- Uses the same semaphore as the OpenAI client (`MAX_CONCURRENT_MODELS`)
  if set.
- Runs `_call_openrouter_eval` in a thread.

### How to Use It

To call Gemini 2.5 Flash via OpenRouter:

```python
from utils.ai_clients import get_openrouter_eval

result = await get_openrouter_eval(prompt)
```

Again:

- `prompt` is your grading instruction string.
- `result` is parsed JSON with `_raw_*` metadata.

The client automatically:
- Picks the right `OPENROUTER_MODEL`, and
- Requests JSON-only output from the model.

---

## How to Call the JSON Clients

Most of the project only needs two functions:

```python
from utils.ai_clients import get_openai_eval, get_openrouter_eval
```

Example usage in an async context:

```python
gpt5_json = await get_openai_eval(prompt)
gemini_json = await get_openrouter_eval(prompt)
```

Where:

- `prompt` is a string with all the information needed to grade.
- Both functions:
  - Ask the model to answer in JSON only.
  - Parse that JSON into a dict.
  - Return `None` if something goes wrong (network errors, invalid JSON).

If you want to run them in parallel, you can use `asyncio.gather`:

```python
import asyncio
from utils.ai_clients import get_openai_eval, get_openrouter_eval

gpt5_json, gemini_json = await asyncio.gather(
    get_openai_eval(prompt),
    get_openrouter_eval(prompt),
)
```

---

## Relationship to the Evaluator and Schema

**File:** `utils/evaluator.py`

The evaluator:

- Builds a detailed grading prompt using `prompts/eme_prompt.py`.
- Calls `get_openai_eval(...)` and `get_eduai_eval_async(...)` in
  parallel (for GPT‑5 Nano and GPT‑OSS 120B).
- Normalizes the returned JSON into a standard shape:
  - `total_score`
  - `max_possible_score`
  - `overall_feedback`
  - plus any extra metadata.

Now that the OpenAI and OpenRouter clients are in JSON-only mode:

- The evaluator sees **fewer malformed payloads**.
- Schema validation (`evaluation_schema.json`) is more robust because
  the model output is always valid JSON.

If you later decide to add Gemini (OpenRouter) as a third model in
`evaluator.py`, you can:

1. Import `get_openrouter_eval`.
2. Add it to the `asyncio.gather(...)` call.
3. Normalize its JSON payload the same way as the others.

---

## Troubleshooting & Tips (Beginner-Friendly)

### “I’m getting a JSON decode error”

Even with JSON mode, models can occasionally misbehave. When that
happens:

- `_safe_json_loads` returns `None`.
- The client falls back to returning a dict with:

  ```python
  {"_raw_content": content, "_raw_response": serialized}
  ```

What you can do:

- Log `"_raw_content"` to see exactly what the model produced.
- Consider tightening your prompt to emphasize:
  - “Return **only** JSON.”
  - “Do not include any explanations or Markdown.”

### “Where do I put my API keys?”

In your `.env` file or environment:

```env
OPENAI_API_KEY=sk-...

OPENROUTER_API_KEY=sk-or-...
OPENROUTER_MODEL=google/gemini-2.5-flash-preview-09-2025
# Optional:
# OPENROUTER_SITE_URL=https://your-app-url.com
# OPENROUTER_SITE_NAME=Your App Name
```

You do **not** need separate keys for each model vendor behind
OpenRouter—the OpenRouter key covers routing to Gemini.

### “Do I need to know the OpenAI SDK in detail?”

Not really. This project already wraps the SDK for you:

- For GPT‑5 Nano, call `get_openai_eval`.
- For Gemini via OpenRouter, call `get_openrouter_eval`.

If you later want more control (e.g. temperature, max_tokens), you can:

- Add optional parameters to the wrapper functions.
- Pass them through to `openai.responses.create(...)` or
  `client.chat.completions.create(...)`.

---

## Summary

- EduBench now **enforces JSON-only outputs** for:
  - GPT‑5 Nano (OpenAI Responses API)
  - Gemini 2.5 Flash via OpenRouter
- This is done using:

  ```python
  response_format={"type": "json_object"}
  ```

  in both clients.

- You usually only need two entrypoints:

  ```python
  from utils.ai_clients import get_openai_eval, get_openrouter_eval
  ```

- Under the hood:
  - The OpenAI SDK handles HTTP and authentication.
  - The clients parse and normalize JSON for you.
  - The evaluator and database work with clean, structured data.

If you’d like to extend this system (e.g., add more models on
OpenRouter or use `json_schema` for stricter validation), you can do
so by evolving the client modules while keeping the same simple
facade functions for the rest of your code.
