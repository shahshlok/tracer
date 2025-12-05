"""
LLM Misconception Detection CLI

Interactive CLI for detecting misconceptions in student code using GPT-5.1 and Gemini 2.5 Flash.
Results are saved per prompting strategy for comparative analysis.
"""

import asyncio
import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import typer
from dotenv import load_dotenv
from rich import box
from rich.console import Console
from rich.panel import Panel
from rich.progress import BarColumn, Progress, SpinnerColumn, TextColumn, TimeElapsedColumn
from rich.prompt import Prompt
from rich.table import Table
from rich.text import Text

from prompts.strategies import PromptStrategy, build_prompt
from pydantic_models import LLMEvaluationResponse, Misconception
from utils.grading import load_question, load_rubric
from utils.llm.openrouter import get_structured_response

load_dotenv()

app = typer.Typer(help="LLM Misconception Detection CLI")
console = Console()

# Configuration
MODELS = ["openai/gpt-5.1", "google/gemini-2.5-flash", "deepseek/deepseek-v3.2-speciale"]
MODEL_SHORT_NAMES = {
    "openai/gpt-5.1": "GPT-5.1",
    "google/gemini-2.5-flash": "Gemini-2.5-Flash",
    "deepseek/deepseek-v3.2-speciale": "Deepseek-v3.2-Speciale",
}
STRATEGIES = ["minimal", "baseline", "socratic", "rubric_only"]
MAX_CONCURRENCY = 30
DEFAULT_OUTPUT_DIR = Path("detections")
SUBMISSION_DIR = Path("authentic_seeded")


def get_student_list() -> list[str]:
    """Get list of student folders from authentic_seeded/."""
    if not SUBMISSION_DIR.exists():
        return []
    return sorted(
        [
            d.name
            for d in SUBMISSION_DIR.iterdir()
            if d.is_dir() and not d.name.startswith(".") and d.name != "manifest.json"
        ]
    )


def load_manifest() -> dict[str, Any]:
    """Load the manifest.json with ground truth."""
    manifest_path = SUBMISSION_DIR / "manifest.json"
    if not manifest_path.exists():
        return {}
    return json.loads(manifest_path.read_text())


def create_header():
    """Create the CLI header panel."""
    title = Text("LLM MISCONCEPTION DETECTOR", style="bold white on magenta", justify="center")
    subtitle = Text(
        "Detect & Analyze Student Misconceptions", style="italic cyan", justify="center"
    )
    content = Text.assemble(title, "\n", subtitle)
    return Panel(
        content,
        box=box.DOUBLE,
        border_style="magenta",
        padding=(1, 2),
    )


def create_strategy_menu() -> Panel:
    """Create strategy selection menu."""
    text = Text()
    text.append("Select prompting strategy:\n\n", style="bold white")
    text.append("  [1] ", style="bold green")
    text.append("minimal", style="white")
    text.append(" - No examples, LLM discovers freely\n", style="dim")
    text.append("  [2] ", style="bold yellow")
    text.append("baseline", style="white")
    text.append(" - Lists example misconceptions (control)\n", style="dim")
    text.append("  [3] ", style="bold cyan")
    text.append("socratic", style="white")
    text.append(" - Chain-of-thought reasoning\n", style="dim")
    text.append("  [4] ", style="bold magenta")
    text.append("rubric_only", style="white")
    text.append(" - Grade only, minimal misconception focus\n", style="dim")

    return Panel(
        text, box=box.ROUNDED, border_style="green", title="[bold]Strategy[/bold]", padding=(1, 2)
    )


def create_student_menu(total_students: int) -> Panel:
    """Create student selection menu."""
    text = Text()
    text.append(f"Found {total_students} students\n\n", style="bold white")
    text.append("  [1] ", style="bold green")
    text.append("All students\n", style="white")
    text.append("  [2] ", style="bold yellow")
    text.append("First N students\n", style="white")
    text.append("  [3] ", style="bold cyan")
    text.append("Sample (10 random)\n", style="white")

    return Panel(
        text, box=box.ROUNDED, border_style="yellow", title="[bold]Students[/bold]", padding=(1, 2)
    )


async def detect_misconceptions_for_file(
    model: str,
    student_code: str,
    question_text: str,
    rubric_data: dict[str, Any],
    strategy: str,
) -> list[Misconception]:
    """Run misconception detection for a single file with one model."""
    prompt = build_prompt(PromptStrategy(strategy), question_text, rubric_data, student_code)
    messages = [{"role": "user", "content": prompt}]

    try:
        response = await get_structured_response(messages, LLMEvaluationResponse, model=model)
        return response.misconceptions
    except Exception as e:
        console.print(f"[red]Error with {MODEL_SHORT_NAMES.get(model, model)}: {e}[/red]")
        return []


async def process_student_question(
    student_id: str,
    question: str,
    strategy: str,
    semaphore: asyncio.Semaphore,
) -> dict[str, Any]:
    """Process one student-question pair with all models."""
    async with semaphore:
        q_num = question.replace("Q", "")
        question_file = f"data/a2/q{q_num}.md"
        rubric_file = f"data/a2/rubric_q{q_num}.md"
        student_file = SUBMISSION_DIR / student_id / f"{question}.java"

        if not student_file.exists():
            return {
                "student": student_id,
                "question": question,
                "status": "skipped",
                "reason": "file_not_found",
            }

        try:
            question_text = load_question(question_file)
            rubric_data = load_rubric(rubric_file)
            student_code = student_file.read_text()
        except Exception as e:
            return {
                "student": student_id,
                "question": question,
                "status": "error",
                "reason": str(e),
            }

        # Run all models in parallel
        tasks = [
            detect_misconceptions_for_file(
                model, student_code, question_text, rubric_data, strategy
            )
            for model in MODELS
        ]
        results = await asyncio.gather(*tasks)

        model_results = {}
        for model, misconceptions in zip(MODELS, results):
            model_results[model] = {
                "misconceptions": [m.model_dump() for m in misconceptions],
                "count": len(misconceptions),
            }

        return {
            "student": student_id,
            "question": question,
            "status": "success",
            "models": model_results,
            "timestamp": datetime.now(timezone.utc).isoformat(),
        }


async def run_detection(
    students: list[str],
    strategy: str,
    output_dir: Path,
) -> dict[str, Any]:
    """Run misconception detection for all students."""
    semaphore = asyncio.Semaphore(MAX_CONCURRENCY)
    strategy_dir = output_dir / strategy
    strategy_dir.mkdir(parents=True, exist_ok=True)

    questions = ["Q1", "Q2", "Q3", "Q4"]
    total_tasks = len(students) * len(questions)

    results = []
    stats = {
        "total_processed": 0,
        "successful": 0,
        "skipped": 0,
        "errors": 0,
        "total_misconceptions": dict.fromkeys(MODELS, 0),
    }

    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(),
        TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
        TimeElapsedColumn(),
        console=console,
    ) as progress:
        task_id = progress.add_task(f"[cyan]Detecting ({strategy})...", total=total_tasks)

        # Create all tasks
        tasks = []
        for student_id in students:
            for question in questions:
                tasks.append(process_student_question(student_id, question, strategy, semaphore))

        # Process with progress updates
        for coro in asyncio.as_completed(tasks):
            result = await coro
            results.append(result)
            progress.advance(task_id)

            stats["total_processed"] += 1
            if result["status"] == "success":
                stats["successful"] += 1
                # Save individual result
                output_file = strategy_dir / f"{result['student']}_{result['question']}.json"
                output_file.write_text(json.dumps(result, indent=2))

                # Update model stats
                for model in MODELS:
                    stats["total_misconceptions"][model] += result["models"][model]["count"]
            elif result["status"] == "skipped":
                stats["skipped"] += 1
            else:
                stats["errors"] += 1

    # Save aggregated stats
    stats["strategy"] = strategy
    stats["timestamp"] = datetime.now(timezone.utc).isoformat()
    stats["students_processed"] = len(students)

    stats_file = strategy_dir / "_stats.json"
    stats_file.write_text(json.dumps(stats, indent=2))

    return stats


def display_results(stats: dict[str, Any], strategy: str):
    """Display detection results summary."""
    console.print()
    console.rule(f"[bold magenta]Results: {strategy}[/bold magenta]")
    console.print()

    # Summary table
    table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    table.add_column("Metric", style="white")
    table.add_column("Value", justify="right", style="cyan")

    table.add_row("Total Processed", str(stats["total_processed"]))
    table.add_row("Successful", f"[green]{stats['successful']}[/green]")
    table.add_row("Skipped", f"[yellow]{stats['skipped']}[/yellow]")
    table.add_row("Errors", f"[red]{stats['errors']}[/red]")

    console.print(table)
    console.print()

    # Model comparison
    model_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    model_table.add_column("Model", style="white")
    model_table.add_column("Misconceptions Found", justify="right", style="magenta")

    for model in MODELS:
        short_name = MODEL_SHORT_NAMES.get(model, model)
        count = stats["total_misconceptions"][model]
        model_table.add_row(short_name, str(count))

    console.print(model_table)
    console.print()
    console.print(f"[dim]Results saved to detections/{strategy}/[/dim]")


def interactive_main():
    """Interactive CLI entry point."""
    console.print(create_header())
    console.print()

    # Check for students
    students = get_student_list()
    if not students:
        console.print(
            Panel(
                "[red]No students found in authentic_seeded/[/red]\n\n"
                "[dim]Run 'uv run miscons' first to generate the dataset.[/dim]",
                title="Error",
                border_style="red",
            )
        )
        return

    # Load manifest for context
    manifest = load_manifest()
    if manifest:
        console.print(
            f"[dim]Manifest loaded: {manifest.get('student_count', '?')} students, seed {manifest.get('seed', '?')}[/dim]"
        )
    console.print()

    # Strategy selection
    console.print(create_strategy_menu())
    strategy_choice = Prompt.ask(
        "[bold]Select strategy[/bold]", choices=["1", "2", "3", "4"], default="1"
    )
    strategy = STRATEGIES[int(strategy_choice) - 1]
    console.print(f"[green]Selected: {strategy}[/green]")
    console.print()

    # Student selection
    console.print(create_student_menu(len(students)))
    student_choice = Prompt.ask(
        "[bold]Select students[/bold]", choices=["1", "2", "3"], default="1"
    )

    if student_choice == "1":
        selected_students = students
    elif student_choice == "2":
        n = Prompt.ask("[bold]How many?[/bold]", default="10")
        try:
            n = min(int(n), len(students))
        except ValueError:
            n = 10
        selected_students = students[:n]
    else:
        import random

        selected_students = random.sample(students, min(10, len(students)))

    console.print(f"[green]Processing {len(selected_students)} students[/green]")
    console.print()

    # Confirm
    if not typer.confirm(
        f"Run detection with '{strategy}' on {len(selected_students)} students?", default=True
    ):
        console.print("[yellow]Cancelled.[/yellow]")
        return

    console.print()

    # Run detection
    stats = asyncio.run(run_detection(selected_students, strategy, DEFAULT_OUTPUT_DIR))

    # Display results
    display_results(stats, strategy)

    console.print()
    console.print("[bold green]Detection complete![/bold green]")


@app.command()
def detect(
    strategy: str = typer.Option(
        "minimal", help="Prompting strategy: minimal, baseline, socratic, rubric_only"
    ),
    students: int = typer.Option(0, help="Number of students (0 = all)"),
    output: Path = typer.Option(DEFAULT_OUTPUT_DIR, help="Output directory"),
):
    """Run misconception detection (non-interactive)."""
    student_list = get_student_list()
    if not student_list:
        console.print("[red]No students found.[/red]")
        raise typer.Exit(1)

    if students > 0:
        student_list = student_list[:students]

    console.print(f"[cyan]Running {strategy} on {len(student_list)} students...[/cyan]")
    stats = asyncio.run(run_detection(student_list, strategy, output))
    display_results(stats, strategy)


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    """LLM Misconception Detection CLI."""
    if ctx.invoked_subcommand is None:
        interactive_main()


if __name__ == "__main__":
    app()
