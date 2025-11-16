"""Utility script to inspect the raw OpenAI Responses API output."""

from openai import OpenAI


def main() -> None:
    """Invoke GPT-5 Nano and print the raw response object for manual inspection."""
    client = OpenAI()
    response = client.responses.create(
        model="gpt-5-nano",
        input="Describe GPT-5 Nano in two sentences."
    )

    # `response` is typically a Responses API object, so print it directly to
    # examine fields like `output`, `status`, and `usage`.
    print(response)


if __name__ == "__main__":
    main()
