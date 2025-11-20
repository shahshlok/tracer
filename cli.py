import os
import typer
from rich.console import Console
from rich.panel import Panel
from rich.progress import Progress, SpinnerColumn, TextColumn, BarColumn, TimeElapsedColumn
from rich.table import Table
from rich import box
from rich.text import Text
from rich.align import Align
from typing import List, Dict, Any
from dotenv import load_dotenv

from utils.grading import (
    load_question,
    load_rubric,
    load_student_submission,
    construct_prompt,
    grade_with_model,
    create_evaluation_document
)

load_dotenv()

app = typer.Typer()
console = Console()

MODELS = ["google/gemini-2.5-flash-lite", "moonshotai/kimi-k2-0905"]
BATCH_LIMIT = 10

def get_student_list(submission_dir: str = "student_submissions") -> List[str]:
    if not os.path.exists(submission_dir):
        return []
    return sorted([d for d in os.listdir(submission_dir) if os.path.isdir(os.path.join(submission_dir, d))])

def create_header():
    title = Text("ENSEMBLE EVALUATION BENCHMARK", style="bold white on blue", justify="center")
    subtitle = Text("Batch Processing Protocol v2.0", style="italic cyan", justify="center")
    
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

@app.command(name="bench")
def main():
    # 1. Header
    console.print(create_header())
    console.print()

    # 2. Discovery & Batching
    all_students = get_student_list()
    if not all_students:
        console.print(Panel("[red]No student submissions found in 'student_submissions/'[/red]", title="Error", border_style="red"))
        return

    students_to_grade = all_students[:BATCH_LIMIT]
    console.print(f"[bold]Found {len(all_students)} submissions. Processing top {len(students_to_grade)}...[/bold]")
    console.print()

    # 3. Setup Resources
    try:
        with console.status("[bold yellow]Loading Assignment Resources...[/bold yellow]", spinner="dots"):
            question_text = load_question("question_cuboid.md")
            rubric_data = load_rubric("rubric_cuboid.json")
    except Exception as e:
        console.print(f"[red]Error loading resources: {e}[/red]")
        return

    # 4. Batch Execution
    results = []

    with Progress(
        SpinnerColumn("earth"),
        TextColumn("[bold blue]{task.description}", justify="right"),
        BarColumn(bar_width=None, style="black", complete_style="green", finished_style="green"),
        TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
        TimeElapsedColumn(),
        console=console,
        expand=True
    ) as progress:
        
        overall_task = progress.add_task("Batch Progress", total=len(students_to_grade))
        
        for student_id in students_to_grade:
            student_name = student_id.replace("_", " ")
            progress.update(overall_task, description=f"Grading: [cyan]{student_id}[/cyan]")
            
            try:
                # Load submission
                student_code, filename = load_student_submission(student_id)
                prompt = construct_prompt(question_text, rubric_data, student_code)
                messages = [{"role": "user", "content": prompt}]
                
                model_evals = {}
                
                # Grade with each model
                for model in MODELS:
                    try:
                        eval_result = grade_with_model(model, messages)
                        model_evals[model] = eval_result
                    except Exception as e:
                        # Log error but continue
                        model_evals[model] = None
                
                # Save if we have at least one result
                valid_evals = {k: v for k, v in model_evals.items() if v is not None}
                if valid_evals:
                    eval_doc = create_evaluation_document(
                        student_id,
                        student_name,
                        question_text,
                        rubric_data,
                        filename,
                        valid_evals
                    )
                    
                    output_dir = "student_evals"
                    os.makedirs(output_dir, exist_ok=True)
                    output_file = f"{output_dir}/{student_id}_eval.json"
                    with open(output_file, "w") as f:
                        f.write(eval_doc.model_dump_json(indent=2))
                    
                    results.append({
                        "student": student_id,
                        "evals": valid_evals
                    })
            
            except Exception as e:
                console.print(f"[red]Failed to process {student_id}: {e}[/red]")
            
            progress.advance(overall_task)

    # 5. Comparative Table
    console.print()
    console.rule("[bold]Ensembling Method Evaluation[/bold]")
    console.print()

    table = Table(box=None, show_lines=False, pad_edge=False)
    
    table.add_column("Student", style="white")
    table.add_column("Gemini", justify="right", style="cyan")
    table.add_column("Kimi", justify="right", style="magenta")
    table.add_column("Avg", justify="right", style="bold white")
    table.add_column("Diff", justify="right", style="yellow")
    table.add_column("Conf", justify="right", style="blue")
    table.add_column("Flag", justify="center")
    table.add_column("Comment", style="dim white")

    for res in results:
        student = res["student"]
        evals = res["evals"]
        
        gemini_eval = evals.get("google/gemini-2.5-flash-lite")
        kimi_eval = evals.get("moonshotai/kimi-k2-0905")
        
        gemini_score = gemini_eval.scores.total_points_awarded if gemini_eval else 0
        kimi_score = kimi_eval.scores.total_points_awarded if kimi_eval else 0
        
        avg_score = (gemini_score + kimi_score) / 2
        diff = abs(gemini_score - kimi_score)
        
        # Calculate average confidence
        confs = []
        if gemini_eval:
            confs.extend([cs.confidence for cs in gemini_eval.category_scores])
        if kimi_eval:
            confs.extend([cs.confidence for cs in kimi_eval.category_scores])
        
        avg_conf = (sum(confs) / len(confs) * 100) if confs else 0
        
        # Flag logic
        flag = "✅" if diff <= 10 else "🚩"
        
        # Comment logic
        if diff <= 5:
            comment = "Models agree within tolerance"
        elif gemini_score < kimi_score:
            comment = f"Gemini stricter by {diff:.1f} pts"
        else:
            comment = f"Kimi stricter by {diff:.1f} pts"

        table.add_row(
            student,
            f"{gemini_score:.1f}",
            f"{kimi_score:.1f}",
            f"{avg_score:.1f}",
            f"{diff:.1f}",
            f"{avg_conf:.0f}%",
            flag,
            comment
        )

    console.print(table)
    console.print()
    console.print(f"[dim]Processed {len(results)} students. Detailed logs saved to student_evals/[/dim]")

if __name__ == "__main__":
    app()
