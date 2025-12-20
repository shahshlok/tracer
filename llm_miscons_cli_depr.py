"""
LLM Misconception Detection CLI

Uses OpenRouter to run 6 LLMs (3 base + 3 reasoning) across 4 prompt strategies
to detect notional machine misconceptions in student code.
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
from pydantic_models import LLMDetectionResponse
from utils.llm.router import get_structured_response, get_reasoning_response

load_dotenv()

app = typer.Typer(help="LLM Misconception Detection CLI")
console = Console()

# ============================================================================
# Configuration
# ============================================================================

MODELS = [
    "openai/gpt-5.1",
    "google/gemini-2.5-flash-preview-09-2025",
    "anthropic/claude-haiku-4.5",
]

MODEL_SHORT_NAMES = {
    "openai/gpt-5.1": "GPT-5.1",
    "google/gemini-2.5-flash-preview-09-2025": "Gemini-2.5-Flash",
    "anthropic/claude-haiku-4.5": "Haiku-4.5",
}

REASONING_SHORT_NAMES = {
    "openai/gpt-5.1:reasoning": "GPT-5.1-R",
    "google/gemini-2.5-flash-preview-09-2025:reasoning": "Gemini-2.5-R",
    "anthropic/claude-haiku-4.5:reasoning": "Haiku-4.5-R",
}

ALL_MODEL_SHORT_NAMES = {**MODEL_SHORT_NAMES, **REASONING_SHORT_NAMES}

STRATEGIES = ["baseline", "taxonomy", "cot", "socratic"]
MAX_CONCURRENCY = 30

# Paths
SUBMISSION_DIR = Path("authentic_seeded/a3")
DEFAULT_OUTPUT_DIR = Path("detections/a3")
QUESTIONS_DIR = Path("data/a3")

# ============================================================================
# Data Loading
# ============================================================================


def get_student_list() -> list[str]:
    """Get list of student folders."""
    if not SUBMISSION_DIR.exists():
        return []
    return sorted(
        [d.name for d in SUBMISSION_DIR.iterdir() if d.is_dir() and not d.name.startswith(".")]
    )


def load_manifest() -> dict[str, Any]:
    """Load manifest.json with ground truth."""
    manifest_path = SUBMISSION_DIR / "manifest.json"
    if not manifest_path.exists():
        return {}
    return json.loads(manifest_path.read_text())


def load_question_text(question: str) -> str:
    """Load question markdown file."""
    q_num = question.lower()  # Q1 -> q1
    question_file = QUESTIONS_DIR / f"{q_num}.md"
    if not question_file.exists():
        raise FileNotFoundError(f"Question file not found: {question_file}")
    return question_file.read_text()


# ============================================================================
# Detection Core
# ============================================================================


async def detect_for_file(
    model: str,
    problem_description: str,
    student_code: str,
    strategy: str,
    use_reasoning: bool = False,
) -> LLMDetectionResponse:
    """Run detection for a single file with one model."""
    prompt = build_prompt(PromptStrategy(strategy), problem_description, student_code)
    messages = [{"role": "user", "content": prompt}]

    try:
        if use_reasoning:
            return await get_reasoning_response(messages, LLMDetectionResponse, model=model)
        else:
            return await get_structured_response(messages, LLMDetectionResponse, model=model)
    except Exception as e:
        console.print(f"[red]Error with {ALL_MODEL_SHORT_NAMES.get(model, model)}: {e}[/red]")
        return LLMDetectionResponse(misconceptions=[])


async def process_student_question(
    student_id: str,
    question: str,
    strategy: str,
    semaphore: asyncio.Semaphore,
    include_reasoning: bool = True,
) -> dict[str, Any]:
    """Process one student-question pair with all models."""
    async with semaphore:
        student_file = SUBMISSION_DIR / student_id / f"{question}.java"

        if not student_file.exists():
            return {
                "student": student_id,
                "question": question,
                "strategy": strategy,
                "status": "skipped",
                "reason": "file_not_found",
            }

        try:
            problem_description = load_question_text(question)
            student_code = student_file.read_text()
        except Exception as e:
            return {
                "student": student_id,
                "question": question,
                "strategy": strategy,
                "status": "error",
                "reason": str(e),
            }

        # Build model configs: (key, model_id, use_reasoning)
        model_configs = [(model, model, False) for model in MODELS]
        if include_reasoning:
            model_configs.extend([(f"{model}:reasoning", model, True) for model in MODELS])

        # Run all models in parallel
        tasks = [
            detect_for_file(model_id, problem_description, student_code, strategy, use_reasoning)
            for (_, model_id, use_reasoning) in model_configs
        ]
        results = await asyncio.gather(*tasks)

        model_results = {}
        for (model_key, _, _), response in zip(model_configs, results):
            model_results[model_key] = {
                "misconceptions": [m.model_dump() for m in response.misconceptions],
                "count": len(response.misconceptions),
            }

        return {
            "student": student_id,
            "question": question,
            "strategy": strategy,
            "status": "success",
            "models": model_results,
            "timestamp": datetime.now(timezone.utc).isoformat(),
        }


async def run_detection(
    students: list[str],
    strategy: str,
    output_dir: Path,
    include_reasoning: bool = True,
) -> dict[str, Any]:
    """Run detection for all students."""
    semaphore = asyncio.Semaphore(MAX_CONCURRENCY)
    strategy_dir = output_dir / strategy
    strategy_dir.mkdir(parents=True, exist_ok=True)

    questions = ["Q1", "Q2", "Q3", "Q4"]
    total_tasks = len(students) * len(questions)

    # Build model keys for stats
    all_model_keys = list(MODELS)
    if include_reasoning:
        all_model_keys.extend([f"{m}:reasoning" for m in MODELS])

    stats = {
        "total_processed": 0,
        "successful": 0,
        "skipped": 0,
        "errors": 0,
        "total_misconceptions": dict.fromkeys(all_model_keys, 0),
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

        tasks = [
            process_student_question(student_id, question, strategy, semaphore, include_reasoning)
            for student_id in students
            for question in questions
        ]

        for coro in asyncio.as_completed(tasks):
            result = await coro
            progress.advance(task_id)

            stats["total_processed"] += 1
            if result["status"] == "success":
                stats["successful"] += 1

                # Save individual result
                output_file = strategy_dir / f"{result['student']}_{result['question']}.json"
                output_file.write_text(json.dumps(result, indent=2))

                # Update model stats
                for model_key in result["models"]:
                    if model_key in stats["total_misconceptions"]:
                        stats["total_misconceptions"][model_key] += result["models"][model_key][
                            "count"
                        ]
            elif result["status"] == "skipped":
                stats["skipped"] += 1
            else:
                stats["errors"] += 1

    # Save stats
    stats["strategy"] = strategy
    stats["timestamp"] = datetime.now(timezone.utc).isoformat()
    stats["students_processed"] = len(students)

    stats_file = strategy_dir / "_stats.json"
    stats_file.write_text(json.dumps(stats, indent=2))

    return stats


# ============================================================================
# CLI UI
# ============================================================================


def create_header():
    """Create CLI header panel."""
    title = Text("NOTIONAL MACHINE DETECTOR", style="bold white on magenta", justify="center")
    subtitle = Text("LLM-based Misconception Discovery", style="italic cyan", justify="center")
    return Panel(
        Text.assemble(title, "\n", subtitle),
        box=box.DOUBLE,
        border_style="magenta",
        padding=(1, 2),
    )


def create_strategy_menu() -> Panel:
    """Create strategy selection menu."""
    text = Text()
    text.append("Select prompting strategy:\n\n", style="bold white")
    text.append("  [1] ", style="bold green")
    text.append("baseline", style="white")
    text.append(" - Simple error classification (control)\n", style="dim")
    text.append("  [2] ", style="bold yellow")
    text.append("taxonomy", style="white")
    text.append(" - Explicit notional machine categories\n", style="dim")
    text.append("  [3] ", style="bold cyan")
    text.append("cot", style="white")
    text.append(" - Chain-of-thought line-by-line tracing\n", style="dim")
    text.append("  [4] ", style="bold magenta")
    text.append("socratic", style="white")
    text.append(" - Mental model probing\n", style="dim")

    return Panel(text, box=box.ROUNDED, border_style="green", title="[bold]Strategy[/bold]")


def display_results(stats: dict[str, Any], strategy: str):
    """Display results summary."""
    console.print()
    console.rule(f"[bold magenta]Results: {strategy}[/bold magenta]")
    console.print()

    # Summary
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
    model_table.add_column("Misconceptions", justify="right", style="magenta")

    for model_key, count in stats["total_misconceptions"].items():
        short_name = ALL_MODEL_SHORT_NAMES.get(model_key, model_key)
        model_table.add_row(short_name, str(count))

    console.print(model_table)
    console.print()
    console.print(f"[dim]Results saved to {DEFAULT_OUTPUT_DIR}/{strategy}/[/dim]")


# ============================================================================
# CLI Commands
# ============================================================================


@app.command()
def detect(
    strategy: str = typer.Option("taxonomy", help="Strategy: baseline, taxonomy, cot, socratic"),
    students: int = typer.Option(0, help="Number of students (0 = all)"),
    output: Path = typer.Option(DEFAULT_OUTPUT_DIR, help="Output directory"),
    no_reasoning: bool = typer.Option(False, help="Disable reasoning models"),
):
    """Run misconception detection."""
    student_list = get_student_list()
    if not student_list:
        console.print("[red]No students found in authentic_seeded/a1/a1[/red]")
        raise typer.Exit(1)

    if students > 0:
        student_list = student_list[:students]

    console.print(create_header())
    console.print()
    console.print(f"[cyan]Running {strategy} on {len(student_list)} students...[/cyan]")
    console.print(
        f"[dim]Models: {len(MODELS)} base + {0 if no_reasoning else len(MODELS)} reasoning[/dim]"
    )
    console.print()

    stats = asyncio.run(run_detection(student_list, strategy, output, not no_reasoning))
    display_results(stats, strategy)
    console.print("[bold green]Detection complete![/bold green]")


@app.command()
def all_strategies(
    students: int = typer.Option(0, help="Number of students (0 = all)"),
    output: Path = typer.Option(DEFAULT_OUTPUT_DIR, help="Output directory"),
):
    """Run detection with all 4 strategies."""
    student_list = get_student_list()
    if not student_list:
        console.print("[red]No students found.[/red]")
        raise typer.Exit(1)

    if students > 0:
        student_list = student_list[:students]

    console.print(create_header())
    console.print()
    console.print(f"[bold]Running all 4 strategies on {len(student_list)} students[/bold]")
    console.print()

    for strategy in STRATEGIES:
        console.rule(f"[bold cyan]{strategy}[/bold cyan]")
        stats = asyncio.run(run_detection(student_list, strategy, output))
        display_results(stats, strategy)
        console.print()

    console.print("[bold green]All strategies complete![/bold green]")


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    """LLM Notional Machine Misconception Detector."""
    if ctx.invoked_subcommand is None:
        # Interactive mode
        console.print(create_header())
        console.print()

        students = get_student_list()
        if not students:
            console.print("[red]No students found in authentic_seeded/a1/a1[/red]")
            return

        manifest = load_manifest()
        if manifest:
            console.print(f"[dim]Loaded: {manifest.get('student_count', '?')} students[/dim]")
        console.print()

        # Strategy selection
        console.print(create_strategy_menu())
        choice = Prompt.ask(
            "[bold]Select strategy[/bold]", choices=["1", "2", "3", "4"], default="2"
        )
        strategy = STRATEGIES[int(choice) - 1]
        console.print(f"[green]Selected: {strategy}[/green]")
        console.print()

        # Student count
        n = Prompt.ask(
            f"[bold]Number of students[/bold] (max {len(students)})", default=str(len(students))
        )
        try:
            n = min(int(n), len(students))
        except ValueError:
            n = len(students)
        selected = students[:n]

        console.print()
        if not typer.confirm(f"Run {strategy} on {len(selected)} students?", default=True):
            console.print("[yellow]Cancelled.[/yellow]")
            return

        console.print()
        stats = asyncio.run(run_detection(selected, strategy, DEFAULT_OUTPUT_DIR))
        display_results(stats, strategy)
        console.print("[bold green]Detection complete![/bold green]")


if __name__ == "__main__":
    app()
