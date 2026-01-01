"""
LLM Misconception Detection CLI (OpenAI + Anthropic + Gemini)

Uses the official OpenAI Responses API, Anthropic Messages API, and Google GenAI SDK via utils.llm
to run multiple models and prompting strategies for notional machine misconception detection.
"""

import asyncio
import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import aiofiles
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
from utils.llm import anthropic as anthropic_client
from utils.llm import gemini as gemini_client
from utils.llm import openai as openai_client

load_dotenv()

app = typer.Typer(help="LLM Misconception Detection CLI (OpenAI + Anthropic + Gemini)")
console = Console()

OPENAI_MODELS = ["gpt-5.2-2025-12-11"]
ANTHROPIC_MODELS = ["claude-haiku-4-5-20251001"]
GEMINI_MODELS = ["gemini-3-flash-preview"]

MODEL_SHORT_NAMES = {
    "gpt-5.2-2025-12-11": "GPT-5.2",
    "claude-haiku-4-5-20251001": "Claude-Haiku",
    "gemini-3-flash-preview": "Gemini-3-Flash",
}

REASONING_SHORT_NAMES = {
    "gpt-5.2-2025-12-11:reasoning": "GPT-5.2-R",
    "claude-haiku-4-5-20251001:reasoning": "Claude-Haiku-R",
    "gemini-3-flash-preview:reasoning": "Gemini-3-Flash-R",
}

ALL_MODEL_SHORT_NAMES = {**MODEL_SHORT_NAMES, **REASONING_SHORT_NAMES}

STRATEGIES = ["baseline", "taxonomy", "cot", "socratic"]

# Reduced concurrency to avoid rate limiting on Tier 1 accounts
# Anthropic Tier 1: 50 RPM, OpenAI/Gemini have higher limits
# With 6 models per task, 3 concurrent tasks = 18 parallel requests max (safer for 100 students)
MAX_CONCURRENCY = 3

# Configurable assignment (default a3, can be changed via CLI)
CURRENT_ASSIGNMENT = "a3"


def get_submission_dir() -> Path:
    return Path(f"authentic_seeded/{CURRENT_ASSIGNMENT}")


def get_output_dir() -> Path:
    return Path(f"detections/{CURRENT_ASSIGNMENT}_multi")


def get_questions_dir() -> Path:
    return Path(f"data/{CURRENT_ASSIGNMENT}")


# Provider mapping for models
OPENAI_MODEL_SET = set(OPENAI_MODELS)
ANTHROPIC_MODEL_SET = set(ANTHROPIC_MODELS)
GEMINI_MODEL_SET = set(GEMINI_MODELS)


def load_manifest() -> dict[str, Any]:
    manifest_path = get_submission_dir() / "manifest.json"
    if not manifest_path.exists():
        return {}
    return json.loads(manifest_path.read_text())


def get_student_list() -> list[str]:
    submission_dir = get_submission_dir()
    if not submission_dir.exists():
        return []

    manifest = load_manifest()
    students: list[str] = []

    if manifest and "students" in manifest:
        for entry in manifest["students"]:
            folder = entry.get("folder_name")
            if not folder:
                continue
            student_dir = submission_dir / folder
            if not student_dir.is_dir():
                continue
            # Require all four question files to be present for inclusion.
            if all((student_dir / f"Q{i}.java").exists() for i in range(1, 5)):
                students.append(folder)
    else:
        students = sorted(
            [d.name for d in submission_dir.iterdir() if d.is_dir() and not d.name.startswith(".")]
        )

    return students


def load_question_text(question: str) -> str:
    """Synchronous version for CLI commands that don't need async."""
    q_num = question.lower()
    question_file = get_questions_dir() / f"{q_num}.md"
    if not question_file.exists():
        raise FileNotFoundError(f"Question file not found: {question_file}")
    return question_file.read_text()


async def load_question_text_async(question: str) -> str:
    """Async version for concurrent processing."""
    q_num = question.lower()
    question_file = get_questions_dir() / f"{q_num}.md"
    if not question_file.exists():
        raise FileNotFoundError(f"Question file not found: {question_file}")
    async with aiofiles.open(question_file) as f:
        return await f.read()


async def detect_for_file(
    model: str,
    problem_description: str,
    student_code: str,
    strategy: str,
    use_reasoning: bool = False,
) -> LLMDetectionResponse:
    prompt = build_prompt(PromptStrategy(strategy), problem_description, student_code)
    messages = [{"role": "user", "content": prompt}]

    try:
        # Route to the correct provider
        if model in OPENAI_MODEL_SET:
            if use_reasoning:
                return await openai_client.get_reasoning_response(
                    messages, LLMDetectionResponse, model=model
                )
            return await openai_client.get_structured_response(
                messages, LLMDetectionResponse, model=model
            )
        elif model in ANTHROPIC_MODEL_SET:
            if use_reasoning:
                return await anthropic_client.get_reasoning_response(
                    messages, LLMDetectionResponse, model=model
                )
            return await anthropic_client.get_structured_response(
                messages, LLMDetectionResponse, model=model
            )
        elif model in GEMINI_MODEL_SET:
            if use_reasoning:
                return await gemini_client.get_reasoning_response(
                    messages, LLMDetectionResponse, model=model
                )
            return await gemini_client.get_structured_response(
                messages, LLMDetectionResponse, model=model
            )
        else:
            raise ValueError(f"Unknown model provider for: {model}")
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
    async with semaphore:
        student_file = get_submission_dir() / student_id / f"{question}.java"

        if not student_file.exists():
            return {
                "student": student_id,
                "question": question,
                "strategy": strategy,
                "status": "skipped",
                "reason": "file_not_found",
            }

        try:
            problem_description = await load_question_text_async(question)
            async with aiofiles.open(student_file) as f:
                student_code = await f.read()
        except Exception as e:
            return {
                "student": student_id,
                "question": question,
                "strategy": strategy,
                "status": "error",
                "reason": str(e),
            }

        # Build model configs for all providers
        all_models = OPENAI_MODELS + ANTHROPIC_MODELS + GEMINI_MODELS
        model_configs = [(model, model, False) for model in all_models]
        if include_reasoning:
            model_configs.extend([(f"{model}:reasoning", model, True) for model in all_models])

        tasks = [
            detect_for_file(model_id, problem_description, student_code, strategy, use_reasoning)
            for (_, model_id, use_reasoning) in model_configs
        ]
        results = await asyncio.gather(*tasks)

        model_results: dict[str, Any] = {}
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
    semaphore = asyncio.Semaphore(MAX_CONCURRENCY)
    strategy_dir = output_dir / strategy
    strategy_dir.mkdir(parents=True, exist_ok=True)

    questions = ["Q1", "Q2", "Q3", "Q4"]
    total_tasks = len(students) * len(questions)

    all_models = OPENAI_MODELS + ANTHROPIC_MODELS + GEMINI_MODELS
    all_model_keys = list(all_models)
    if include_reasoning:
        all_model_keys.extend([f"{m}:reasoning" for m in all_models])

    stats: dict[str, Any] = {
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

                output_file = strategy_dir / f"{result['student']}_{result['question']}.json"
                output_file.parent.mkdir(parents=True, exist_ok=True)
                async with aiofiles.open(output_file, mode="w") as f:
                    await f.write(json.dumps(result, indent=2))

                for model_key in result["models"]:
                    if model_key in stats["total_misconceptions"]:
                        stats["total_misconceptions"][model_key] += result["models"][model_key][
                            "count"
                        ]
            elif result["status"] == "skipped":
                stats["skipped"] += 1
            else:
                stats["errors"] += 1

    stats["strategy"] = strategy
    stats["timestamp"] = datetime.now(timezone.utc).isoformat()
    stats["students_processed"] = len(students)

    stats_file = strategy_dir / "_stats.json"
    stats_file.parent.mkdir(parents=True, exist_ok=True)
    async with aiofiles.open(stats_file, mode="w") as f:
        await f.write(json.dumps(stats, indent=2))

    # Cleanup all LLM clients in parallel to avoid "Event loop is closed" errors
    await asyncio.gather(
        openai_client.cleanup(),
        anthropic_client.cleanup(),
        gemini_client.cleanup(),
    )

    return stats


async def run_detection_no_cleanup(
    students: list[str],
    strategy: str,
    output_dir: Path,
    include_reasoning: bool = True,
) -> dict[str, Any]:
    """Run detection without cleaning up clients (for use in concurrent runs)."""
    semaphore = asyncio.Semaphore(MAX_CONCURRENCY)
    strategy_dir = output_dir / strategy
    strategy_dir.mkdir(parents=True, exist_ok=True)

    questions = ["Q1", "Q2", "Q3", "Q4"]
    total_tasks = len(students) * len(questions)

    all_models = OPENAI_MODELS + ANTHROPIC_MODELS + GEMINI_MODELS
    all_model_keys = list(all_models)
    if include_reasoning:
        all_model_keys.extend([f"{m}:reasoning" for m in all_models])

    stats: dict[str, Any] = {
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

                output_file = strategy_dir / f"{result['student']}_{result['question']}.json"
                output_file.parent.mkdir(parents=True, exist_ok=True)
                async with aiofiles.open(output_file, mode="w") as f:
                    await f.write(json.dumps(result, indent=2))

                for model_key in result["models"]:
                    if model_key in stats["total_misconceptions"]:
                        stats["total_misconceptions"][model_key] += result["models"][model_key][
                            "count"
                        ]
            elif result["status"] == "skipped":
                stats["skipped"] += 1
            else:
                stats["errors"] += 1

    stats["strategy"] = strategy
    stats["timestamp"] = datetime.now(timezone.utc).isoformat()
    stats["students_processed"] = len(students)

    stats_file = strategy_dir / "_stats.json"
    stats_file.parent.mkdir(parents=True, exist_ok=True)
    async with aiofiles.open(stats_file, mode="w") as f:
        await f.write(json.dumps(stats, indent=2))

    return stats


async def run_all_strategies_concurrent(
    students: list[str],
    output_dir: Path,
    include_reasoning: bool = True,
) -> list[dict[str, Any]]:
    """Run all strategies concurrently in a single event loop."""
    tasks = [
        run_detection_no_cleanup(students, strategy, output_dir, include_reasoning)
        for strategy in STRATEGIES
    ]
    results = await asyncio.gather(*tasks)

    # Cleanup all LLM clients once at the end
    await asyncio.gather(
        openai_client.cleanup(),
        anthropic_client.cleanup(),
        gemini_client.cleanup(),
    )

    return list(results)


def create_header():
    title = Text("NOTIONAL MACHINE DETECTOR", style="bold white on blue", justify="center")
    subtitle = Text(
        "LLM-based Misconception Discovery (OpenAI + Anthropic + Gemini)",
        style="italic cyan",
        justify="center",
    )
    return Panel(
        Text.assemble(title, "\n", subtitle),
        box=box.DOUBLE,
        border_style="blue",
        padding=(1, 2),
    )


def create_strategy_menu() -> Panel:
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
    console.print()
    console.rule(f"[bold magenta]Results: {strategy}[/bold magenta]")
    console.print()

    table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    table.add_column("Metric", style="white")
    table.add_column("Value", justify="right", style="cyan")

    table.add_row("Total Processed", str(stats["total_processed"]))
    table.add_row("Successful", f"[green]{stats['successful']}[/green]")
    table.add_row("Skipped", f"[yellow]{stats['skipped']}[/yellow]")
    table.add_row("Errors", f"[red]{stats['errors']}[/red]")

    console.print(table)
    console.print()

    model_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    model_table.add_column("Model", style="white")
    model_table.add_column("Misconceptions", justify="right", style="magenta")

    for model_key, count in stats["total_misconceptions"].items():
        short_name = ALL_MODEL_SHORT_NAMES.get(model_key, model_key)
        model_table.add_row(short_name, str(count))

    console.print(model_table)
    console.print()
    console.print(f"[dim]Results saved to {get_output_dir()}/{strategy}/[/dim]")


@app.command()
def detect(
    strategy: str = typer.Option("taxonomy", help="Strategy: baseline, taxonomy, cot, socratic"),
    students: int = typer.Option(0, help="Number of students (0 = all)"),
    output: Path = typer.Option(
        None, help="Output directory (default: detections/<assignment>_multi)"
    ),
    no_reasoning: bool = typer.Option(False, help="Disable reasoning models"),
    assignment: str = typer.Option("a3", help="Assignment: a1, a2, or a3"),
):
    global CURRENT_ASSIGNMENT
    CURRENT_ASSIGNMENT = assignment

    if output is None:
        output = get_output_dir()

    student_list = get_student_list()
    if not student_list:
        console.print(f"[red]No students found in authentic_seeded/{assignment}[/red]")
        raise typer.Exit(1)

    if students > 0:
        student_list = student_list[:students]

    console.print(create_header())
    console.print()
    console.print(f"[cyan]Running {strategy} on {len(student_list)} students...[/cyan]")
    console.print()
    all_models = OPENAI_MODELS + ANTHROPIC_MODELS + GEMINI_MODELS
    console.print(
        f"[dim]Models: {len(all_models)} base + {0 if no_reasoning else len(all_models)} reasoning[/dim]"
    )
    console.print()

    stats = asyncio.run(run_detection(student_list, strategy, output, not no_reasoning))
    display_results(stats, strategy)
    console.print("[bold green]Detection complete![/bold green]")


@app.command()
def all_strategies(
    students: int = typer.Option(0, help="Number of students (0 = all)"),
    output: Path = typer.Option(
        None, help="Output directory (default: detections/<assignment>_multi)"
    ),
    assignment: str = typer.Option("a3", help="Assignment: a1, a2, or a3"),
):
    global CURRENT_ASSIGNMENT
    CURRENT_ASSIGNMENT = assignment

    if output is None:
        output = get_output_dir()

    student_list = get_student_list()
    if not student_list:
        console.print(f"[red]No students found in authentic_seeded/{assignment}[/red]")
        raise typer.Exit(1)

    if students > 0:
        student_list = student_list[:students]

    console.print(create_header())
    console.print()
    console.print(
        f"[bold]Running all 4 strategies concurrently on {len(student_list)} students[/bold]"
    )
    console.print()

    all_stats = asyncio.run(run_all_strategies_concurrent(student_list, output))

    for stats in all_stats:
        console.rule(f"[bold cyan]{stats['strategy']}[/bold cyan]")
        display_results(stats, stats["strategy"])
        console.print()

    console.print("[bold green]All strategies complete![/bold green]")


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    if ctx.invoked_subcommand is None:
        console.print(create_header())
        console.print()

        students = get_student_list()
        if not students:
            console.print("[red]No students found in authentic_seeded/a3[/red]")
            return

        manifest = load_manifest()
        if manifest:
            console.print(f"[dim]Loaded: {manifest.get('student_count', '?')} students[/dim]")
        console.print()

        console.print(create_strategy_menu())
        choice = Prompt.ask(
            "[bold]Select strategy[/bold]", choices=["1", "2", "3", "4"], default="2"
        )
        strategy = STRATEGIES[int(choice) - 1]
        console.print(f"[green]Selected: {strategy}[/green]")
        console.print()

        n = Prompt.ask(
            f"[bold]Number of students[/bold] (max {len(students)})", default=str(len(students))
        )
        try:
            n_int = min(int(n), len(students))
        except ValueError:
            n_int = len(students)
        selected = students[:n_int]

        console.print()
        if not typer.confirm(f"Run {strategy} on {len(selected)} students?", default=True):
            console.print("[yellow]Cancelled.[/yellow]")
            return

        console.print()
        stats = asyncio.run(run_detection(selected, strategy, get_output_dir()))
        display_results(stats, strategy)
        console.print("[bold green]Detection complete![/bold green]")


if __name__ == "__main__":
    app()
