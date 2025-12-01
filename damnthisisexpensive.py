import asyncio
import time
import os
from rich.console import Console
from rich.panel import Panel
from rich.prompt import Confirm

# Import from existing modules
# Note: We import specific functions to reuse the logic in cli.py
from cli import batch_grade_students, get_student_list, display_grading_results
from utils.analytics import run_analysis
import utils.visualizations as viz

console = Console()

STRATEGIES = ["minimal", "baseline", "socratic", "rubric_only"]

async def main():
    # 1. Discovery
    students = get_student_list()
    if not students:
        console.print("[red]No students found in authentic_seeded/[/red]")
        return

    # Estimation
    # 60 students * 4 questions * 4 strategies * 2 models = 1920 calls
    num_students = len(students)
    num_questions = 4
    num_models = 2
    num_strategies = len(STRATEGIES)
    
    total_evals = num_students * num_questions * num_strategies
    total_api_calls = total_evals * num_models

    console.print(Panel(
        f"[bold red]WARNING: EXPENSIVE BATCH OPERATION[/bold red]\n\n"
        f"Students: [cyan]{num_students}[/cyan]\n"
        f"Strategies: [cyan]{STRATEGIES}[/cyan]\n"
        f"Total Evaluations: [yellow]{total_evals}[/yellow]\n"
        f"Total LLM API Calls: [red]~{total_api_calls}[/red]\n\n"
        "This script will run the full evaluation suite across all strategies.\n"
        "Ensure you have sufficient API credits.",
        title="damnthisisexpensive.py",
        border_style="red"
    ))

    if not Confirm.ask("Do you want to proceed?", default=False):
        console.print("[yellow]Aborted.[/yellow]")
        return

    start_time = time.time()

    # 2. Execution Loop
    for i, strategy in enumerate(STRATEGIES, 1):
        console.rule(f"[bold purple]Batch {i}/{num_strategies}: {strategy}[/bold purple]")
        console.print(f"Starting batch for {num_students} students...")
        
        # Run the grading batch (reuse cli.py logic)
        # This handles concurrency (MAX_CONCURRENT_STUDENTS=10) automatically
        results = await batch_grade_students(students, strategy=strategy)
        
        # Show filtered results table
        display_grading_results(results, strategy=strategy)
        
        console.print(f"[green]Finished strategy: {strategy}[/green]\n")
        
        # Brief pause between batches to be nice to the API
        if i < num_strategies:
            console.print("Cooling down for 5 seconds...")
            time.sleep(5)

    # 3. Analytics & Reporting
    console.rule("[bold blue]Generating Research Evidence Report[/bold blue]")
    
    # Ensure reports dir exists
    os.makedirs("reports", exist_ok=True)
    
    try:
        # Run Analytics Engine
        console.print("Running statistical analysis...")
        report_path = run_analysis(output_path="reports/research_evidence_report.md")
        
        # Run Visualizations
        console.print("Generating visualizations...")
        viz.generate_strategy_comparison_chart()
        viz.generate_misconception_heatmap()
        viz.generate_model_agreement_heatmap()
        
        console.print("[green]Visualizations saved to reports/figures/[/green]")
        
    except Exception as e:
        console.print(f"[red]Error generating report: {e}[/red]")

    # 4. Final Summary
    duration = time.time() - start_time
    minutes = duration / 60
    
    console.print(Panel(
        f"Full Evaluation Complete!\n"
        f"Time: {minutes:.1f} minutes\n"
        f"Output: reports/research_evidence_report.md",
        title="Success",
        border_style="green"
    ))

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        console.print("\n[red]Process interrupted by user.[/red]")
