from openai import OpenAI

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="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",
)

completion = client.chat.completions.create(
    extra_headers={
        "HTTP-Referer": "<YOUR_SITE_URL>",  # Optional. Site URL for rankings on openrouter.ai.
        "X-Title": "<YOUR_SITE_NAME>",  # Optional. Site title for rankings on openrouter.ai.
    },
    model="openai/gpt-4o",
    messages=[{"role": "user", "content": "What is the meaning of life?"}],
)

print(completion.choices[0].message.content)
