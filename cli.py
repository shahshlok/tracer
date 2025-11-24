import asyncio
import os
from typing import Any

import typer
from dotenv import load_dotenv
from rich import box
from rich.align import Align

# Rich Imports
from rich.console import Console
from rich.panel import Panel
from rich.progress import BarColumn, Progress, SpinnerColumn, TaskID, TextColumn, TimeElapsedColumn
from rich.table import Table
from rich.text import Text

# Local Imports
from utils.grading import (
    construct_prompt,
    create_evaluation_document,
    grade_with_model,
    load_question,
    load_rubric,
    load_student_submission,
)

load_dotenv()

app = typer.Typer()
console = Console()

# --- Configuration ---
# Reduce this if you hit rate limits (429 errors), increase if your tier allows.
MAX_CONCURRENT_STUDENTS = 5
MODELS = [
    "google/gemini-2.5-flash",
    "openai/gpt-5.1",
    "google/gemini-2.5-flash-lite",
    "openai/gpt-5-nano",
]
BATCH_LIMIT = 3  # Process 3 students

# --- Helper Functions ---


def get_student_list(submission_dir: str = "student_submissions") -> list[str]:
    if not os.path.exists(submission_dir):
        return []
    return sorted(
        [d for d in os.listdir(submission_dir) if os.path.isdir(os.path.join(submission_dir, d))]
    )


def create_header():
    title = Text("ENSEMBLE EVALUATION BENCHMARK", style="bold white on blue", justify="center")
    subtitle = Text("Async Batch Protocol v3.0 (Parallel)", style="italic cyan", justify="center")

    content = Align.center(
        Text.assemble(title, "\n", subtitle),
        vertical="middle",
    )
    return Panel(
        content,
        box=box.DOUBLE,
        border_style="blue",
        padding=(1, 2),
        title="[bold green]• SYSTEM ONLINE •[/bold green]",
        title_align="right",
    )


async def grade_student_with_models(
    student_code: str, question_text: str, rubric_data: dict[str, Any]
) -> dict[str, Any]:
    """
    Grades a single student against ALL models in parallel.
    """
    prompt = construct_prompt(question_text, rubric_data, student_code)
    messages = [{"role": "user", "content": prompt}]

    async def grade_with_error_handling(model: str):
        try:
            # grading.py's grade_with_model is already async
            return await grade_with_model(model, messages)
        except Exception:
            # Return None on error, will be filtered out later
            return None

    # Run model calls in parallel (Inner Loop Parallelism)
    tasks = [grade_with_error_handling(model) for model in MODELS]
    results = await asyncio.gather(*tasks)

    # Map results back to model names
    return dict(zip(MODELS, results, strict=True))


async def process_student_wrapper(
    sem: asyncio.Semaphore,
    student_id: str,
    question_text: str,
    rubric_data: dict[str, Any],
    progress: Progress,
    task_id: TaskID,
) -> dict[str, Any] | None:
    """
    Worker function that processes one student within the semaphore limit.
    """
    student_name = student_id.replace("_", " ")

    # Wait here if we have reached MAX_CONCURRENT_STUDENTS
    async with sem:
        progress.update(task_id, description=f"Grading: [cyan]{student_id}[/cyan]")

        try:
            # 1. Load submission (Fast I/O)
            student_code, filename = load_student_submission(student_id)

            # 2. Grade (Slow Network Call)
            model_evals = await grade_student_with_models(student_code, question_text, rubric_data)

            # 3. Save Results (Fast I/O)
            valid_evals = {k: v for k, v in model_evals.items() if v is not None}

            if valid_evals:
                eval_doc = create_evaluation_document(
                    student_id,
                    student_name,
                    question_text,
                    rubric_data,
                    filename,
                    valid_evals,
                    question_source_path="data/question_insurance.md",
                    rubric_source_path="data/rubric_insurance2.md",
                )

                output_dir = "student_evals"
                os.makedirs(output_dir, exist_ok=True)
                output_file = f"{output_dir}/{student_id}_eval.json"
                with open(output_file, "w") as f:
                    f.write(eval_doc.model_dump_json(indent=2))

                progress.advance(task_id)
                return {"status": "success", "student": student_id, "evals": valid_evals}
            else:
                # Both models failed
                progress.console.print(f"[red]All models failed for {student_id}[/red]")
                progress.advance(task_id)
                return {"status": "error", "student": student_id, "error": "All models failed"}

        except Exception as e:
            progress.console.print(f"[red]Error processing {student_id}: {e}[/red]")
            progress.advance(task_id)  # Advance anyway to not hang the bar
            return {"status": "error", "student": student_id, "error": str(e)}


async def batch_grade_students(
    students: list[str], question_text: str, rubric_data: dict[str, Any]
) -> list[dict]:
    """
    Orchestrates the parallel grading of multiple students.
    """
    sem = asyncio.Semaphore(MAX_CONCURRENT_STUDENTS)
    results = []

    with Progress(
        SpinnerColumn("earth"),
        TextColumn("[bold blue]{task.description}", justify="right"),
        BarColumn(bar_width=None, style="black", complete_style="green", finished_style="green"),
        TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
        TimeElapsedColumn(),
        console=console,
        expand=True,
    ) as progress:
        overall_task = progress.add_task("Batch Progress", total=len(students))

        # Create a list of pending coroutines
        tasks = []
        for student_id in students:
            tasks.append(
                process_student_wrapper(
                    sem, student_id, question_text, rubric_data, progress, overall_task
                )
            )

        # Fire them all!
        # returns a list of results in the same order as tasks
        results = await asyncio.gather(*tasks)

        # No filtering needed anymore, we want to see failures

    return results


# --- Main Command ---


@app.command(name="bench")
def main():
    # 1. Header
    console.print(create_header())
    console.print()

    # 2. Discovery
    all_students = get_student_list()
    if not all_students:
        console.print(
            Panel(
                "[red]No student submissions found in 'student_submissions/'[/red]",
                title="Error",
                border_style="red",
            )
        )
        return

    students_to_grade = all_students[:BATCH_LIMIT]
    console.print(
        f"[bold]Found {len(all_students)} submissions. Processing top {len(students_to_grade)}...[/bold]"
    )
    console.print(f"[dim]Concurrency Limit: {MAX_CONCURRENT_STUDENTS} students at a time[/dim]")
    console.print()

    # 3. Setup Resources
    try:
        with console.status(
            "[bold yellow]Loading Assignment Resources...[/bold yellow]", spinner="dots"
        ):
            question_text = load_question("data/question_insurance.md")
            rubric_data = load_rubric("data/rubric_insurance2.md")
    except Exception as e:
        console.print(f"[red]Error loading resources: {e}[/red]")
        return

    # 4. Async Batch Execution
    # This replaces the old manual for-loop
    results = asyncio.run(batch_grade_students(students_to_grade, question_text, rubric_data))

    # 5. Comparative Table
    console.print()
    console.rule("[bold]Ensembling Method Evaluation[/bold]")
    console.print()

    table = Table(box=None, show_lines=False, pad_edge=False)

    table.add_column("Student", style="white")
    table.add_column("GemFlash", justify="right", style="cyan")
    table.add_column("GPT5.1", justify="right", style="green")
    table.add_column("GemLite", justify="right", style="magenta")
    table.add_column("GPT5N", justify="right", style="yellow")
    table.add_column("Avg", justify="right", style="bold white")
    table.add_column("Range", justify="right", style="red")
    table.add_column("Conf", justify="right", style="blue")
    table.add_column("Flag", justify="center")

    for res in results:
        student = res["student"]

        if res.get("status") == "error":
            # Render Error Row
            table.add_row(
                student,
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "❌",
            )
            continue

        # Success Case
        evals = res["evals"]

        # Get evaluations for each model
        gemini_flash_eval = evals.get("google/gemini-2.5-flash")
        gpt51_eval = evals.get("openai/gpt-5.1")
        gemini_lite_eval = evals.get("google/gemini-2.5-flash-lite")
        gpt5nano_eval = evals.get("openai/gpt-5-nano")

        # Get scores
        gemini_flash_score = (
            gemini_flash_eval.scores.total_points_awarded if gemini_flash_eval else 0
        )
        gpt51_score = gpt51_eval.scores.total_points_awarded if gpt51_eval else 0
        gemini_lite_score = gemini_lite_eval.scores.total_points_awarded if gemini_lite_eval else 0
        gpt5nano_score = gpt5nano_eval.scores.total_points_awarded if gpt5nano_eval else 0

        # Handle missing models for average calc
        valid_scores = []
        for ev in [gemini_flash_eval, gpt51_eval, gemini_lite_eval, gpt5nano_eval]:
            if ev:
                valid_scores.append(ev.scores.total_points_awarded)

        avg_score = sum(valid_scores) / len(valid_scores) if valid_scores else 0
        score_range = max(valid_scores) - min(valid_scores) if valid_scores else 0

        # Calculate average confidence
        confs = []
        for ev in [gemini_flash_eval, gpt51_eval, gemini_lite_eval, gpt5nano_eval]:
            if ev:
                confs.extend([cs.confidence for cs in ev.category_scores])

        avg_conf = (sum(confs) / len(confs) * 100) if confs else 0

        # Flag logic - flag if range is > 1.5 points (significant disagreement)
        flag = "✅" if score_range <= 1.5 else "🚩"

        table.add_row(
            student,
            f"{gemini_flash_score:.1f}" if gemini_flash_eval else "-",
            f"{gpt51_score:.1f}" if gpt51_eval else "-",
            f"{gemini_lite_score:.1f}" if gemini_lite_eval else "-",
            f"{gpt5nano_score:.1f}" if gpt5nano_eval else "-",
            f"{avg_score:.1f}",
            f"{score_range:.1f}",
            f"{avg_conf:.0f}%",
            flag,
        )

    console.print(table)
    console.print()
    console.print(
        f"[dim]Processed {len(results)} students successfully. Logs in student_evals/[/dim]"
    )


if __name__ == "__main__":
    app()
