import os

from openai import OpenAI

client = OpenAI(
    # This is the default and can be omitted
    api_key=os.environ.get("OPENAI_API_KEY"),
)

response = client.responses.create(
    model="gpt-5-mini",
    input="How do I check if a Python object is an instance of a class?",
)

print(response.output_text)
