import asyncio
import os
import random

from dotenv import load_dotenv
from rich.console import Console

from sandbox.utils.grading import (
    construct_prompt,
    create_evaluation_document,
    grade_with_model,
    load_question,
    load_rubric,
    load_student_submission,
)

load_dotenv()
console = Console()

MODELS = ["google/gemini-2.5-flash-lite", "openai/gpt-5-nano"]


async def grade_single_student():
    # 1. Setup
    console.print("[bold blue]Experiment: Single Student Grading[/bold blue]")

    submission_dir = "student_submissions"
    if not os.path.exists(submission_dir):
        console.print(f"[red]Submission directory '{submission_dir}' not found.[/red]")
        return

    students = [
        d for d in os.listdir(submission_dir) if os.path.isdir(os.path.join(submission_dir, d))
    ]
    if not students:
        console.print("[red]No students found.[/red]")
        return

    console.print("[yellow] Selecting random student...[/yellow]")
    student_id = random.choice(students)

    console.print(f"Selected Student: [cyan]{student_id}[/cyan]")

    # 2. Load Resources
    try:
        question_text = load_question("data/question_insurance.md")
        rubric_data = load_rubric("data/rubric_insurance2.md")
        student_code, filename = load_student_submission(student_id)
    except Exception as e:
        console.print(f"[red]Error loading resources: {e}[/red]")
        return

    # 3. Grade
    prompt = construct_prompt(question_text, rubric_data, student_code)
    messages = [{"role": "user", "content": prompt}]

    model_evals = {}

    for model in MODELS:
        console.print(f"Grading with [magenta]{model}[/magenta]...")
        try:
            eval_result = await grade_with_model(model, messages)
            model_evals[model] = eval_result
            console.print(f"[green]Success ({model})[/green]")
        except Exception as e:
            console.print(f"[red]Failed ({model}): {e}[/red]")

    if not model_evals:
        console.print("[red]All models failed.[/red]")
        return

    # 4. Save
    student_name = student_id.replace("_", " ")
    eval_doc = create_evaluation_document(
        student_id,
        student_name,
        question_text,
        rubric_data,
        filename,
        model_evals,
        question_source_path="data/question_cuboid.md",
        rubric_source_path="data/rubric_cuboid.json",
    )

    output_dir = "sandbox/evals"
    os.makedirs(output_dir, exist_ok=True)
    output_file = f"{output_dir}/{student_id}_eval.json"

    with open(output_file, "w") as f:
        f.write(eval_doc.model_dump_json(indent=2))

    console.print(f"[bold green]Saved evaluation to {output_file}[/bold green]")


def main():
    """Entry point for uv run experiment command."""
    asyncio.run(grade_single_student())


if __name__ == "__main__":
    main()
