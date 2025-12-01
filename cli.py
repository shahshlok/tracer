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
)
from utils.misconception_analyzer import MisconceptionAnalyzer

load_dotenv()

app = typer.Typer()
console = Console()

# --- Configuration ---
# Reduce this if you hit rate limits (429 errors), increase if your tier allows.
MAX_CONCURRENT_STUDENTS = 10
MODELS = ["google/gemini-2.5-flash-preview-09-2025", "openai/gpt-5.1"]
MODEL_SHORT_NAMES = {
    "google/gemini-2.5-flash-preview-09-2025": "2.5-Flash",
    "openai/gpt-5.1": "GPT5.1",
}
BATCH_LIMIT = 25  # Process 25 students

# Representative sample of 10 students (20% Correct, 30% Mixed, 50% Single Error)
# Balanced to match class distribution: 2 Correct, 3 Mixed, 5 Single (DT, VAR, CONST, INPUT, OTHER)
REPRESENTATIVE_STUDENTS = [
    "Walker_Chloe_200124_Correct",  # All correct (control)
    "Robinson_Noah_200127_Correct",  # All correct (control 2)
    "Torres_Daniel_200148_Mixed",  # DT002 + INPUT003
    "Ramirez_Zoey_200134_Mixed",  # VAR001 + CONST001 + DT003
    "Lopez_Abigail_200117_Mixed",  # OTHER001 + INPUT001
    "Anderson_Noah_200113_DT003",  # DT003
    "Hill_Michael_200140_VAR001",  # VAR001: operator precedence
    "Smith_Mason_200158_CONST002",  # CONST002: missing Math.sqrt
    "Flores_Emily_200126_INPUT002",  # INPUT002: scanner.nextLine() vs next()
    "Rodriguez_Owen_200104_OTHER002",  # OTHER002: miscellaneous
]

# --- Helper Functions ---


def get_student_list(submission_dir: str = "authentic_seeded") -> list[str]:
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


def create_strategy_panel():
    """Create the prompt strategy selection panel."""
    strategy_text = Text()
    strategy_text.append("Select a prompt strategy:\n\n", style="bold white")
    strategy_text.append("  [1] ", style="bold green")
    strategy_text.append("minimal", style="white")
    strategy_text.append(" - No examples, LLM discovers misconceptions freely\n", style="dim")
    strategy_text.append("  [2] ", style="bold yellow")
    strategy_text.append("baseline", style="white")
    strategy_text.append(" - Lists example misconceptions (control/biased)\n", style="dim")
    strategy_text.append("  [3] ", style="bold cyan")
    strategy_text.append("socratic", style="white")
    strategy_text.append(" - Chain-of-thought reasoning through code\n", style="dim")
    strategy_text.append("  [4] ", style="bold magenta")
    strategy_text.append("rubric_only", style="white")
    strategy_text.append(" - Grade only, no misconception detection\n", style="dim")

    return Panel(
        strategy_text,
        box=box.ROUNDED,
        border_style="green",
        title="[bold]Prompt Strategy[/bold]",
        title_align="left",
        padding=(1, 2),
    )


class GradingTracker:
    """Thread-safe tracker for grading progress across models."""

    def __init__(self):
        self.model_status: dict[str, str] = {model: "idle" for model in MODELS}
        self.model_completed: dict[str, int] = {model: 0 for model in MODELS}
        self.model_errors: dict[str, int] = {model: 0 for model in MODELS}
        self.lock = asyncio.Lock()

    async def set_status(self, model: str, status: str):
        async with self.lock:
            self.model_status[model] = status

    async def mark_completed(self, model: str, success: bool = True):
        async with self.lock:
            if success:
                self.model_completed[model] += 1
            else:
                self.model_errors[model] += 1
            self.model_status[model] = "idle"


async def grade_student_with_models(
    student_code: str,
    question_text: str,
    rubric_data: dict[str, Any],
    strategy: str = "minimal",
    tracker: GradingTracker | None = None,
    student_id: str = "",
    q_id: str = "",
) -> dict[str, Any]:
    """
    Grades a single student against ALL models in parallel.

    Args:
        student_code: The student's code submission
        question_text: The assignment question
        rubric_data: The grading rubric
        strategy: Prompt strategy - "baseline", "minimal", "socratic", "rubric_only"
        tracker: Optional tracker for progress updates
        student_id: Student identifier for status display
        q_id: Question identifier for status display
    """
    prompt = construct_prompt(question_text, rubric_data, student_code, strategy=strategy)
    messages = [{"role": "user", "content": prompt}]

    async def grade_with_error_handling(model: str):
        try:
            if tracker:
                short_name = MODEL_SHORT_NAMES.get(model, model.split("/")[-1])
                await tracker.set_status(model, f"{student_id[:15]}:{q_id}")

            # grading.py's grade_with_model is already async
            result = await grade_with_model(model, messages)

            if tracker:
                await tracker.mark_completed(model, success=True)

            return result
        except Exception:
            if tracker:
                await tracker.mark_completed(model, success=False)
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
    strategy: str = "minimal",
    tracker: GradingTracker | None = None,
) -> list[dict[str, Any]]:
    """
    Worker function that processes one student within the semaphore limit.
    Iterates through Questions 1-4.

    Args:
        sem: Semaphore for concurrency control
        student_id: The student's ID
        progress: Rich progress bar
        task_id: Task ID for progress tracking
        strategy: Prompt strategy - "baseline", "minimal", "socratic", "rubric_only"
        tracker: Optional tracker for per-model progress
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
                submission_dir = "authentic_seeded"
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

                with open(student_file_path) as f:
                    student_code = f.read()

                # 3. Grade (Slow Network Call)
                model_evals = await grade_student_with_models(
                    student_code,
                    question_text,
                    rubric_data,
                    strategy=strategy,
                    tracker=tracker,
                    student_id=student_id,
                    q_id=q_id,
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

                    output_dir = f"student_evals/{strategy}"
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


async def batch_grade_students(students: list[str], strategy: str = "minimal") -> list[dict]:
    """
    Orchestrates the parallel grading of multiple students with live progress display.

    Args:
        students: List of student IDs to grade
        strategy: Prompt strategy - "baseline", "minimal", "socratic", "rubric_only"
    """
    from rich.layout import Layout
    from rich.live import Live
    from rich.spinner import Spinner

    sem = asyncio.Semaphore(MAX_CONCURRENT_STUDENTS)
    tracker = GradingTracker()
    results = []

    total_questions = len(students) * 4  # 4 questions per student
    completed_count = 0

    def create_progress_display():
        """Create the live progress display layout."""
        nonlocal completed_count

        # Main layout
        layout = Layout()

        # Overall progress section
        pct = (completed_count / total_questions * 100) if total_questions > 0 else 0
        bar_width = 40
        filled = int(bar_width * completed_count / total_questions) if total_questions > 0 else 0
        bar = "█" * filled + "░" * (bar_width - filled)

        overall_text = Text()
        overall_text.append("Overall Progress\n", style="bold white")
        overall_text.append(f"[{bar}] ", style="cyan")
        overall_text.append(f"{completed_count}/{total_questions} ", style="bold white")
        overall_text.append(f"({pct:.0f}%)\n", style="dim")
        overall_text.append(f"Students: {len(students)} | ", style="dim")
        overall_text.append(f"Concurrency: {MAX_CONCURRENT_STUDENTS} | ", style="dim")
        overall_text.append(f"Strategy: {strategy}", style="cyan")

        # Model status section
        model_text = Text()
        model_text.append("\nModel Status\n", style="bold white")

        for model in MODELS:
            short_name = MODEL_SHORT_NAMES.get(model, model.split("/")[-1])
            status = tracker.model_status.get(model, "idle")
            completed = tracker.model_completed.get(model, 0)
            errors = tracker.model_errors.get(model, 0)

            # Model name with color
            if "gemini" in model.lower():
                model_text.append(f"  {short_name:<10}", style="cyan")
            else:
                model_text.append(f"  {short_name:<10}", style="green")

            # Status indicator
            if status == "idle":
                model_text.append("● ", style="dim")
                model_text.append("idle          ", style="dim")
            else:
                model_text.append("◉ ", style="yellow")
                model_text.append(f"{status:<14}", style="yellow")

            # Stats
            model_text.append(f" done: {completed:>3}", style="green")
            if errors > 0:
                model_text.append(f" err: {errors}", style="red")
            model_text.append("\n")

        # Combine into panel
        content = Text.assemble(overall_text, model_text)
        return Panel(
            content,
            box=box.ROUNDED,
            border_style="blue",
            title="[bold]Grading Progress[/bold]",
            title_align="left",
            padding=(1, 2),
        )

    async def process_with_tracking(student_id: str) -> list[dict]:
        """Process a student and update the completion counter."""
        nonlocal completed_count

        student_results = []
        student_name = student_id.replace("_", " ")

        async with sem:
            for q_num in range(1, 5):
                q_id = f"q{q_num}"
                try:
                    # 1. Load Resources
                    question_file = f"data/a2/{q_id}.md"
                    rubric_file = f"data/a2/rubric_{q_id}.md"

                    try:
                        question_text = load_question(question_file)
                        rubric_data = load_rubric(rubric_file)
                    except Exception as e:
                        student_results.append(
                            {
                                "status": "error",
                                "student": student_id,
                                "question": q_id,
                                "error": f"Resource load failed: {e}",
                            }
                        )
                        completed_count += 1
                        continue

                    # 2. Load submission
                    submission_dir = "authentic_seeded"
                    student_dir = os.path.join(submission_dir, student_id)
                    student_file_name = f"Q{q_num}.java"
                    student_file_path = os.path.join(student_dir, student_file_name)

                    if not os.path.exists(student_file_path):
                        student_results.append(
                            {
                                "status": "skipped",
                                "student": student_id,
                                "question": q_id,
                                "error": "File not found",
                            }
                        )
                        completed_count += 1
                        continue

                    with open(student_file_path) as f:
                        student_code = f.read()

                    # 3. Grade with all models
                    model_evals = await grade_student_with_models(
                        student_code,
                        question_text,
                        rubric_data,
                        strategy=strategy,
                        tracker=tracker,
                        student_id=student_id,
                        q_id=q_id,
                    )

                    # 4. Save results
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

                        eval_doc.context.question_id = q_id
                        eval_doc.context.question_title = f"Question {q_num}"

                        output_dir = f"student_evals/{strategy}"
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
                        student_results.append(
                            {
                                "status": "error",
                                "student": student_id,
                                "question": q_id,
                                "error": "All models failed",
                            }
                        )

                    completed_count += 1

                except Exception as e:
                    student_results.append(
                        {
                            "status": "error",
                            "student": student_id,
                            "question": q_id,
                            "error": str(e),
                        }
                    )
                    completed_count += 1

        return student_results

    # Run with live display
    with Live(create_progress_display(), console=console, refresh_per_second=4) as live:

        async def update_display():
            """Background task to update the display."""
            while completed_count < total_questions:
                live.update(create_progress_display())
                await asyncio.sleep(0.25)

        # Start display updater
        display_task = asyncio.create_task(update_display())

        # Process all students in parallel
        tasks = [process_with_tracking(student_id) for student_id in students]
        nested_results = await asyncio.gather(*tasks)

        # Cancel display updater
        display_task.cancel()
        try:
            await display_task
        except asyncio.CancelledError:
            pass

        # Final display update
        live.update(create_progress_display())

    # Flatten results
    results = [item for sublist in nested_results for item in sublist]

    return results


def display_grading_results(results: list[dict], strategy: str = "minimal"):
    """Display grading results in a formatted table."""
    console.print()
    console.rule("[bold]Ensembling Method Evaluation[/bold]")
    console.print()

    table = Table(box=None, show_lines=False, pad_edge=False)

    table.add_column("Student", style="white")
    table.add_column("Q", style="yellow")
    table.add_column("2.5-Flash", justify="right", style="cyan")
    table.add_column("GPT5.1", justify="right", style="green")
    table.add_column("Avg", justify="right", style="bold white")
    table.add_column("Range", justify="right", style="red")
    table.add_column("Conf", justify="right", style="blue")
    table.add_column("Flag", justify="center")

    displayed_count = 0

    for res in results:
        student = res["student"]

        if res.get("status") in ("error", "skipped"):
            # Always show errors/skipped items
            table.add_row(
                student,
                res.get("question", "-"),
                "-",
                "-",
                "-",
                "-",
                "-",
                "[yellow]S[/yellow]" if res.get("status") == "skipped" else "[red]X[/red]",
            )
            displayed_count += 1
            continue

        # Success Case
        evals = res["evals"]
        question = res.get("question", "?")

        # Get evaluations for each model
        gemini_flash_eval = evals.get("google/gemini-2.5-flash-preview-09-2025")
        gpt51_eval = evals.get("openai/gpt-5.1")

        # Get scores
        valid_scores = []
        for ev in [gemini_flash_eval, gpt51_eval]:
            if ev:
                valid_scores.append(ev.scores.total_points_awarded)

        avg_score = sum(valid_scores) / len(valid_scores) if valid_scores else 0
        score_range = max(valid_scores) - min(valid_scores) if len(valid_scores) >= 2 else 0

        # Calculate average confidence
        confs = []
        for ev in [gemini_flash_eval, gpt51_eval]:
            if ev:
                confs.extend([cs.confidence for cs in ev.category_scores])

        avg_conf = (sum(confs) / len(confs) * 100) if confs else 0

        # Flag logic - flag if range is > 1.5 points (significant disagreement)
        is_flagged = score_range > 1.5
        flag = "[red]!![/red]" if is_flagged else "[green]OK[/green]"

        # Filter: Only show if flagged or error (already handled above)
        if is_flagged:
            table.add_row(
                student,
                question,
                f"{gemini_flash_eval.scores.total_points_awarded:.1f}"
                if gemini_flash_eval
                else "-",
                f"{gpt51_eval.scores.total_points_awarded:.1f}" if gpt51_eval else "-",
                f"{avg_score:.1f}",
                f"{score_range:.1f}",
                f"{avg_conf:.0f}%",
                flag,
            )
            displayed_count += 1

    if displayed_count > 0:
        console.print(table)
    else:
        console.print("[green]No significant disagreements or errors found![/green]")

    console.print()
    console.print(
        f"[dim]Processed {len(results)} evaluations. Showing {displayed_count} flagged/error items.[/dim]"
    )
    console.print(f"[dim]Full results saved to student_evals/{strategy}/[/dim]")


# Available prompt strategies
PROMPT_STRATEGIES = ["baseline", "minimal", "socratic", "rubric_only"]


def run_grading(
    strategy: str = "minimal", students: list[str] | None = None, batch_size: int | None = None
):
    """Execute the grading workflow.

    Args:
        strategy: Prompt strategy to use. Options:
            - "baseline": Current approach with example misconceptions (control)
            - "minimal": No examples, minimal guidance (recommended for research)
            - "socratic": Chain-of-thought reasoning
            - "rubric_only": Grade only, no misconception detection
        students: Specific list of student IDs to grade. If None, uses batch_size.
        batch_size: Number of students to grade (from beginning of list). If None, uses BATCH_LIMIT.
    """
    # Discovery
    all_students = get_student_list()
    if not all_students:
        console.print(
            Panel(
                "[red]No student submissions found in 'authentic_seeded/'[/red]",
                title="Error",
                border_style="red",
            )
        )
        return

    # Determine which students to grade
    if students is not None:
        students_to_grade = students
    else:
        limit = batch_size if batch_size is not None else BATCH_LIMIT
        students_to_grade = all_students[:limit]

    console.print(
        f"[bold]Processing {len(students_to_grade)} of {len(all_students)} students...[/bold]"
    )
    console.print(f"[dim]Concurrency Limit: {MAX_CONCURRENT_STUDENTS} students at a time[/dim]")
    console.print(f"[cyan]Prompt Strategy: {strategy}[/cyan]")
    console.print()

    # Async Batch Execution
    results = asyncio.run(batch_grade_students(students_to_grade, strategy=strategy))

    # Display Results
    display_grading_results(results, strategy=strategy)


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
    summary_text.append("Unique Topic+Task Areas: ", style="bold")
    summary_text.append(f"{len(class_analysis.topic_task_stats)}\n", style="yellow")
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

    # Display Topic + Task Table (Most Difficult Areas)
    console.rule("[bold]Most Difficult Areas by Topic + Task[/bold]")
    console.print()

    topic_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    topic_table.add_column("Rank", style="dim", width=4)
    topic_table.add_column("Topic", style="cyan")
    topic_table.add_column("Task", style="white", max_width=35)
    topic_table.add_column("Students", justify="center", style="yellow")
    topic_table.add_column("%", justify="right", style="magenta")
    topic_table.add_column("Avg Conf", justify="right", style="blue")
    topic_table.add_column("Agreement", justify="right", style="green")

    for i, stat in enumerate(class_analysis.topic_task_stats[:10], 1):
        # Truncate task name if too long
        task_display = stat.task[:32] + "..." if len(stat.task) > 35 else stat.task

        topic_table.add_row(
            str(i),
            stat.topic,
            task_display,
            f"{stat.student_count}/{stat.total_students}",
            f"{stat.percentage_affected:.0f}%",
            f"{stat.avg_confidence:.2f}",
            f"{stat.model_agreement_rate:.0%}",
        )

    console.print(topic_table)
    console.print()

    # Display Misconception Types Table
    console.rule("[bold]Most Common Misconceptions[/bold]")
    console.print()

    misconception_table = Table(box=box.SIMPLE, show_header=True, header_style="bold")
    misconception_table.add_column("Rank", style="dim", width=4)
    misconception_table.add_column("Misconception", style="white", max_width=40)
    misconception_table.add_column("Topic", style="cyan")
    misconception_table.add_column("Count", justify="center", style="yellow")
    misconception_table.add_column("Students", justify="center", style="magenta")
    misconception_table.add_column("Models", justify="center", style="green")

    for i, stat in enumerate(class_analysis.misconception_type_stats[:10], 1):
        # Truncate name if too long
        name_display = stat.name[:37] + "..." if len(stat.name) > 40 else stat.name

        misconception_table.add_row(
            str(i),
            name_display,
            stat.topic,
            str(stat.occurrence_count),
            f"{stat.student_count}/{stat.total_students}",
            f"{stat.model_agreement_count}/{len(MODELS)}",
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

    # Get unique student IDs (evaluations contains one entry per student×question)
    unique_student_ids = sorted(set(e.submission.student_id for e in analyzer.evaluations))

    for student_id in unique_student_ids:
        student_analysis = analyzer.analyze_student(student_id)
        if student_analysis:
            if student_analysis.total_misconceptions == 0:
                # Skip students with no misconceptions to reduce noise
                continue

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


@app.callback(invoke_without_command=True)
def main(ctx: typer.Context):
    # If a subcommand was invoked, don't run the interactive menu
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
        # Grading workflow - ask for strategy
        console.rule("[bold cyan]Student Grading[/bold cyan]")
        console.print()

        # Show strategy selection panel
        console.print(create_strategy_panel())
        console.print()

        strategy_choice = Prompt.ask(
            "[bold]Select strategy[/bold]",
            choices=["1", "2", "3", "4"],
            default="1",
        )

        # Map choice to strategy name
        strategy_map = {
            "1": "minimal",
            "2": "baseline",
            "3": "socratic",
            "4": "rubric_only",
        }
        strategy = strategy_map[strategy_choice]

        console.print()

        # Ask for student selection
        all_students = get_student_list()
        console.print(f"[dim]Found {len(all_students)} students in authentic_seeded/[/dim]")
        console.print()
        console.print("[bold]Select students to grade:[/bold]")
        console.print("  [1] Representative sample (10 students, covers 12/15 misconception types)")
        console.print("  [2] Custom number (first N alphabetically)")
        console.print("  [3] All students")
        console.print()

        student_choice = Prompt.ask(
            "[bold]Selection[/bold]",
            choices=["1", "2", "3"],
            default="1",
        )

        if student_choice == "1":
            # Use representative sample
            students_to_use = [s for s in REPRESENTATIVE_STUDENTS if s in all_students]
            console.print(
                f"[green]Using representative sample: {len(students_to_use)} students[/green]"
            )
        elif student_choice == "2":
            batch_size = Prompt.ask(
                "[bold]How many students?[/bold]",
                default="5",
            )
            try:
                batch_size = int(batch_size)
                batch_size = min(batch_size, len(all_students))
            except ValueError:
                batch_size = 5
            students_to_use = all_students[:batch_size]
        else:
            students_to_use = all_students

        console.print()
        run_grading(strategy=strategy, students=students_to_use)

    elif choice == "2":
        # Misconception Analysis workflow
        run_misconception_analysis()

    elif choice == "3":
        # Exit
        console.print("[dim]Goodbye![/dim]")
        return


if __name__ == "__main__":
    app()
