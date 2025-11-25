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
from rich.prompt import Prompt
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
from utils.misconception_analyzer import MisconceptionAnalyzer

load_dotenv()

app = typer.Typer()
console = Console()

# --- Configuration ---
# Reduce this if you hit rate limits (429 errors), increase if your tier allows.
MAX_CONCURRENT_STUDENTS = 5
MODELS = [
    # "google/gemini-2.5-flash",
    # "openai/gpt-5.1",
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


def create_menu_panel():
    """Create the interactive menu panel."""
    menu_text = Text()
    menu_text.append("What would you like to do?\n\n", style="bold white")
    menu_text.append("  [1] ", style="bold cyan")
    menu_text.append("Grade Students", style="white")
    menu_text.append(f" (Process up to {BATCH_LIMIT} students)\n", style="dim")
    menu_text.append("  [2] ", style="bold magenta")
    menu_text.append("Analyze Misconceptions", style="white")
    menu_text.append(" (Analyze existing evaluations)\n", style="dim")
    menu_text.append("  [3] ", style="bold red")
    menu_text.append("Exit", style="white")

    return Panel(
        menu_text,
        box=box.ROUNDED,
        border_style="cyan",
        title="[bold]Main Menu[/bold]",
        title_align="left",
        padding=(1, 2),
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
    progress: Progress,
    task_id: TaskID,
) -> list[dict[str, Any]]:
    """
    Worker function that processes one student within the semaphore limit.
    Iterates through Questions 1-4.
    """
    student_name = student_id.replace("_", " ")
    student_results = []

    # Wait here if we have reached MAX_CONCURRENT_STUDENTS
    async with sem:
        progress.update(task_id, description=f"Grading: [cyan]{student_id}[/cyan]")

        for q_num in range(1, 5):
            q_id = f"q{q_num}"
            try:
                # 1. Load Resources for this question
                question_file = f"data/a2/{q_id}.md"
                rubric_file = f"data/a2/rubric_{q_id}.md"

                try:
                    question_text = load_question(question_file)
                    rubric_data = load_rubric(rubric_file)
                except Exception as e:
                    progress.console.print(f"[red]Error loading resources for {q_id}: {e}[/red]")
                    student_results.append(
                        {
                            "status": "error",
                            "student": student_id,
                            "question": q_id,
                            "error": f"Resource load failed: {e}",
                        }
                    )
                    continue

                # 2. Load submission (Fast I/O)
                # We need to manually construct the path to find Q{q_num}.java
                submission_dir = "student_submissions"
                student_dir = os.path.join(submission_dir, student_id)
                student_file_name = f"Q{q_num}.java"
                student_file_path = os.path.join(student_dir, student_file_name)

                if not os.path.exists(student_file_path):
                    # progress.console.print(f"[yellow]File {student_file_name} not found for {student_id}. Skipping.[/yellow]")
                    student_results.append(
                        {
                            "status": "skipped",
                            "student": student_id,
                            "question": q_id,
                            "error": "File not found",
                        }
                    )
                    continue

                with open(student_file_path, "r") as f:
                    student_code = f.read()

                # 3. Grade (Slow Network Call)
                model_evals = await grade_student_with_models(
                    student_code, question_text, rubric_data
                )

                # 4. Save Results (Fast I/O)
                valid_evals = {k: v for k, v in model_evals.items() if v is not None}

                if valid_evals:
                    eval_doc = create_evaluation_document(
                        student_id,
                        student_name,
                        question_text,
                        rubric_data,
                        student_file_name,
                        valid_evals,
                        question_source_path=question_file,
                        rubric_source_path=rubric_file,
                    )

                    # Update context with correct question ID
                    eval_doc.context.question_id = q_id
                    eval_doc.context.question_title = f"Question {q_num}"

                    output_dir = "student_evals"
                    os.makedirs(output_dir, exist_ok=True)
                    output_file = f"{output_dir}/{student_id}_{q_id}_eval.json"
                    with open(output_file, "w") as f:
                        f.write(eval_doc.model_dump_json(indent=2))

                    student_results.append(
                        {
                            "status": "success",
                            "student": student_id,
                            "question": q_id,
                            "evals": valid_evals,
                        }
                    )
                else:
                    # Both models failed
                    progress.console.print(f"[red]All models failed for {student_id} {q_id}[/red]")
                    student_results.append(
                        {
                            "status": "error",
                            "student": student_id,
                            "question": q_id,
                            "error": "All models failed",
                        }
                    )

            except Exception as e:
                progress.console.print(f"[red]Error processing {student_id} {q_id}: {e}[/red]")
                student_results.append(
                    {"status": "error", "student": student_id, "question": q_id, "error": str(e)}
                )

        progress.advance(task_id)
        return student_results


async def batch_grade_students(students: list[str]) -> list[dict]:
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
            tasks.append(process_student_wrapper(sem, student_id, progress, overall_task))

        # Fire them all!
        # returns a list of lists of results
        nested_results = await asyncio.gather(*tasks)

        # Flatten results
        results = [item for sublist in nested_results for item in sublist]

    return results


def display_grading_results(results: list[dict]):
    """Display grading results in a formatted table."""
    console.print()
    console.rule("[bold]Ensembling Method Evaluation[/bold]")
    console.print()

    table = Table(box=None, show_lines=False, pad_edge=False)

    table.add_column("Student", style="white")
    table.add_column("Q", style="yellow")
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

        if res.get("status") in ("error", "skipped"):
            # Render Error/Skipped Row
            table.add_row(
                student,
                res.get("question", "-"),
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "[yellow]S[/yellow]" if res.get("status") == "skipped" else "[red]X[/red]",
            )
            continue

        # Success Case
        evals = res["evals"]
        question = res.get("question", "?")

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
        flag = "[green]OK[/green]" if score_range <= 1.5 else "[red]!![/red]"

        table.add_row(
            student,
            question,
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


def run_grading():
    """Execute the grading workflow."""
    # Discovery
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

    # Setup Resources
    # Resources are now loaded per-question inside the grading loop

    # Async Batch Execution
    results = asyncio.run(batch_grade_students(students_to_grade))

    # Display Results
    display_grading_results(results)


def run_misconception_analysis():
    """Execute the misconception analysis workflow."""
    console.print()
    console.rule("[bold magenta]Misconception Analysis[/bold magenta]")
    console.print()

    # Initialize analyzer
    analyzer = MisconceptionAnalyzer(evals_dir="student_evals")

    # Load evaluations
    with console.status("[bold yellow]Loading evaluation files...[/bold yellow]", spinner="dots"):
        num_loaded = analyzer.load_evaluations()

    if num_loaded == 0:
        console.print(
            Panel(
                "[red]No evaluation files found in 'student_evals/'[/red]\n\n"
                "[dim]Run grading first to generate evaluation files.[/dim]",
                title="Error",
                border_style="red",
            )
        )
        return

    console.print(f"[green]Loaded {num_loaded} student evaluations[/green]")
    console.print()

    # Extract misconceptions
    with console.status("[bold yellow]Extracting misconceptions...[/bold yellow]", spinner="dots"):
        analyzer.extract_misconceptions()

    # Perform class analysis
    with console.status("[bold yellow]Analyzing data...[/bold yellow]", spinner="dots"):
        class_analysis = analyzer.analyze_class()

    # Display Summary Panel
    summary_text = Text()
    summary_text.append("Total Students: ", style="bold")
    summary_text.append(f"{class_analysis.total_students}\n", style="cyan")
    summary_text.append("Total Misconceptions: ", style="bold")
    summary_text.append(f"{class_analysis.total_misconceptions}\n", style="magenta")
    summary_text.append("Unique Bloom+Task Areas: ", style="bold")
    summary_text.append(f"{len(class_analysis.bloom_task_stats)}\n", style="yellow")
    summary_text.append("Unique Misconception Types: ", style="bold")
    summary_text.append(f"{len(class_analysis.misconception_type_stats)}", style="green")

    console.print(
        Panel(
            summary_text,
            title="[bold]Analysis Summary[/bold]",
            border_style="blue",
            padding=(1, 2),
        )
    )
    console.print()

    # Display Bloom + Task Table (Most Difficult Areas)
    console.rule("[bold]Most Difficult Areas by Bloom Level + Task[/bold]")
    console.print()

    bloom_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    bloom_table.add_column("Rank", style="dim", width=4)
    bloom_table.add_column("Bloom Level", style="cyan")
    bloom_table.add_column("Task", style="white", max_width=35)
    bloom_table.add_column("Students", justify="center", style="yellow")
    bloom_table.add_column("%", justify="right", style="magenta")
    bloom_table.add_column("Avg Conf", justify="right", style="blue")
    bloom_table.add_column("Agreement", justify="right", style="green")

    for i, stat in enumerate(class_analysis.bloom_task_stats[:10], 1):
        # Truncate task name if too long
        task_display = stat.task[:32] + "..." if len(stat.task) > 35 else stat.task

        bloom_table.add_row(
            str(i),
            stat.bloom_level,
            task_display,
            f"{stat.student_count}/{stat.total_students}",
            f"{stat.percentage_affected:.0f}%",
            f"{stat.avg_confidence:.2f}",
            f"{stat.model_agreement_rate:.0%}",
        )

    console.print(bloom_table)
    console.print()

    # Display Misconception Types Table
    console.rule("[bold]Most Common Misconceptions[/bold]")
    console.print()

    misconception_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    misconception_table.add_column("Rank", style="dim", width=4)
    misconception_table.add_column("Misconception", style="white", max_width=40)
    misconception_table.add_column("Bloom", style="cyan")
    misconception_table.add_column("Count", justify="center", style="yellow")
    misconception_table.add_column("Students", justify="center", style="magenta")
    misconception_table.add_column("Models", justify="center", style="green")

    for i, stat in enumerate(class_analysis.misconception_type_stats[:10], 1):
        # Truncate name if too long
        name_display = stat.name[:37] + "..." if len(stat.name) > 40 else stat.name

        misconception_table.add_row(
            str(i),
            name_display,
            stat.bloom_level,
            str(stat.occurrence_count),
            f"{stat.student_count}/{stat.total_students}",
            f"{stat.model_agreement_count}/4",
        )

    console.print(misconception_table)
    console.print()

    # Display Model Agreement Summary
    console.rule("[bold]Model Agreement Summary[/bold]")
    console.print()

    model_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    model_table.add_column("Model", style="white")
    model_table.add_column("Misconceptions Detected", justify="right", style="cyan")
    model_table.add_column("Bar", style="magenta")

    max_count = (
        max(class_analysis.model_agreement_summary.values())
        if class_analysis.model_agreement_summary
        else 1
    )

    for model, count in sorted(class_analysis.model_agreement_summary.items(), key=lambda x: -x[1]):
        model_short = model.split("/")[-1]
        bar_width = int((count / max_count) * 20)
        bar = "[green]" + "█" * bar_width + "[/green]" + "░" * (20 - bar_width)

        model_table.add_row(model_short, str(count), bar)

    console.print(model_table)
    console.print()

    # Per-Student Summary
    console.rule("[bold]Per-Student Summary[/bold]")
    console.print()

    student_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    student_table.add_column("Student", style="white")
    student_table.add_column("Total", justify="center", style="cyan")
    student_table.add_column("Avg Confidence", justify="right", style="yellow")
    student_table.add_column("Top Topic", style="magenta")
    student_table.add_column("Top Task", style="dim", max_width=30)

    for eval_doc in analyzer.evaluations:
        student_analysis = analyzer.analyze_student(eval_doc.submission.student_id)
        if student_analysis:
            top_topic = (
                max(student_analysis.misconceptions_by_topic.items(), key=lambda x: x[1])[0]
                if student_analysis.misconceptions_by_topic
                else "N/A"
            )
            top_task = (
                max(student_analysis.misconceptions_by_task.items(), key=lambda x: x[1])[0]
                if student_analysis.misconceptions_by_task
                else "N/A"
            )
            top_task_display = top_task[:27] + "..." if len(top_task) > 30 else top_task

        student_table.add_row(
            student_analysis.student_id,
            str(student_analysis.total_misconceptions),
            f"{student_analysis.avg_misconception_confidence:.2f}",
            top_topic,
            top_task_display,
        )

    console.print(student_table)
    console.print()

    # Generate Markdown Report
    console.print()
    save_report = Prompt.ask(
        "[bold]Save detailed report to markdown?[/bold]",
        choices=["y", "n"],
        default="y",
    )

    if save_report.lower() == "y":
        report_path = "misconception_report.md"
        with console.status(
            f"[bold yellow]Generating report: {report_path}[/bold yellow]", spinner="dots"
        ):
            analyzer.generate_markdown_report(report_path)
        console.print(f"[green]Report saved to {report_path}[/green]")

    console.print()
    console.print("[dim]Analysis complete.[/dim]")


# --- Main Command ---


@app.command()
def grade():
    """Directly run the grading workflow."""
    run_grading()


@app.command()
def analyze():
    """Directly run the misconception analysis workflow."""
    run_misconception_analysis()


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    # If a subcommand was invoked (like 'grade' or 'analyze'), don't run the interactive menu
    if ctx.invoked_subcommand is not None:
        return

    # 1. Header
    console.print(create_header())
    console.print()

    # 2. Interactive Menu
    console.print(create_menu_panel())
    console.print()

    choice = Prompt.ask(
        "[bold]Enter your choice[/bold]",
        choices=["1", "2", "3"],
        default="1",
    )

    console.print()

    if choice == "1":
        # Grading workflow
        console.rule("[bold cyan]Student Grading[/bold cyan]")
        console.print()
        run_grading()

    elif choice == "2":
        # Misconception Analysis workflow
        run_misconception_analysis()

    elif choice == "3":
        # Exit
        console.print("[dim]Goodbye![/dim]")
        return


if __name__ == "__main__":
    app()
