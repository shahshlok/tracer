"""Test compile_java on actual generated student submissions.

Run with:
    uv run python tests/test_execution.py
"""

import json
from collections import defaultdict
from pathlib import Path

from rich.console import Console
from rich.table import Table

from utils.execution import compile_java

console = Console()

# Paths
AUTHENTIC_SEEDED = Path("authentic_seeded")
A2_DIR = AUTHENTIC_SEEDED / "a2"
A3_DIR = AUTHENTIC_SEEDED / "a3"


def load_manifest(assignment_dir: Path) -> dict:
    """Load manifest.json from an assignment directory."""
    manifest_path = assignment_dir / "manifest.json"
    if not manifest_path.exists():
        console.print(f"[red]Manifest not found: {manifest_path}[/red]")
        return {}
    return json.loads(manifest_path.read_text())


def get_expected_compile_status(manifest: dict, student_folder: str, question: str) -> bool | None:
    """
    Determine if a file should compile based on its misconception type.

    Returns:
        True if expected to compile (Clean or Logic error)
        False if expected to fail (Syntax error)
        None if unknown
    """
    for student in manifest.get("students", []):
        if student["folder_name"] == student_folder:
            file_info = student.get("files", {}).get(question, {})
            file_type = file_info.get("type", "UNKNOWN")

            if file_type == "CLEAN":
                return True  # Should compile

            if file_type == "SEEDED":
                # Check if it's a syntax-related misconception
                miscon_id = file_info.get("misconception_id", "")
                # Syntax errors typically have SYN- prefix or contain "syntax"
                if miscon_id and ("SYN" in miscon_id.upper() or "SYNTAX" in miscon_id.upper()):
                    return False  # Should NOT compile
                return True  # Logic errors should compile

    return None


def test_assignment(assignment_dir: Path, assignment_name: str) -> dict:
    """Test all Java files in an assignment directory."""
    console.rule(f"[bold blue]Testing {assignment_name}[/bold blue]")

    manifest = load_manifest(assignment_dir)
    if not manifest:
        return {}

    stats = {
        "total": 0,
        "compiled": 0,
        "failed": 0,
        "expected_pass_actual_pass": 0,  # True Positive for "should compile"
        "expected_pass_actual_fail": 0,  # False Negative
        "expected_fail_actual_fail": 0,  # True Positive for "should not compile"
        "expected_fail_actual_pass": 0,  # False Negative (LLM auto-corrected)
        "unknown": 0,
    }

    failures = []
    auto_corrected = []

    student_dirs = sorted([d for d in assignment_dir.iterdir() if d.is_dir()])

    for student_dir in student_dirs:
        student_folder = student_dir.name

        for java_file in sorted(student_dir.glob("*.java")):
            question = java_file.stem  # e.g., "Q1"

            stats["total"] += 1
            result = compile_java(java_file)

            expected = get_expected_compile_status(manifest, student_folder, question)

            if result.success:
                stats["compiled"] += 1
            else:
                stats["failed"] += 1

            if expected is None:
                stats["unknown"] += 1
            elif expected and result.success:
                stats["expected_pass_actual_pass"] += 1
            elif expected and not result.success:
                stats["expected_pass_actual_fail"] += 1
                failures.append({
                    "file": str(java_file),
                    "error": result.stderr[:200],
                })
            elif not expected and not result.success:
                stats["expected_fail_actual_fail"] += 1
            elif not expected and result.success:
                stats["expected_fail_actual_pass"] += 1
                auto_corrected.append(str(java_file))

    # Display results
    table = Table(title=f"{assignment_name} Compilation Results")
    table.add_column("Metric", style="cyan")
    table.add_column("Count", style="green", justify="right")

    table.add_row("Total Files", str(stats["total"]))
    table.add_row("Compiled Successfully", str(stats["compiled"]))
    table.add_row("Failed to Compile", str(stats["failed"]))
    table.add_row("---", "---")
    table.add_row("Expected Pass → Passed ✓", str(stats["expected_pass_actual_pass"]))
    table.add_row("Expected Pass → Failed ✗", str(stats["expected_pass_actual_fail"]))
    table.add_row("Expected Fail → Failed ✓", str(stats["expected_fail_actual_fail"]))
    table.add_row("Expected Fail → Passed (AUTO-CORRECTED) ⚠", str(stats["expected_fail_actual_pass"]))

    console.print(table)

    if failures:
        console.print(f"\n[yellow]Unexpected compilation failures ({len(failures)}):[/yellow]")
        for f in failures[:5]:
            console.print(f"  - {f['file']}")
            console.print(f"    [dim]{f['error']}[/dim]")

    if auto_corrected:
        console.print(f"\n[red]Auto-corrected files ({len(auto_corrected)}):[/red]")
        for f in auto_corrected[:5]:
            console.print(f"  - {f}")
        console.print("[red]These were expected to have syntax errors but compiled successfully![/red]")

    return stats


def main():
    console.print("[bold]Compiler Verification Test[/bold]\n")

    # Check if Java is available
    import subprocess
    try:
        result = subprocess.run(["javac", "-version"], capture_output=True, text=True)
        console.print(f"[green]Java compiler found:[/green] {result.stderr.strip() or result.stdout.strip()}\n")
    except FileNotFoundError:
        console.print("[red]Error: javac not found. Please install Java JDK.[/red]")
        return

    # Test both assignments
    all_stats = {}

    if A2_DIR.exists():
        all_stats["a2"] = test_assignment(A2_DIR, "Assignment 2")
    else:
        console.print(f"[yellow]Skipping A2: {A2_DIR} not found[/yellow]")

    console.print()

    if A3_DIR.exists():
        all_stats["a3"] = test_assignment(A3_DIR, "Assignment 3")
    else:
        console.print(f"[yellow]Skipping A3: {A3_DIR} not found[/yellow]")

    # Summary
    console.print()
    console.rule("[bold green]Summary[/bold green]")

    total_files = sum(s.get("total", 0) for s in all_stats.values())
    total_compiled = sum(s.get("compiled", 0) for s in all_stats.values())
    total_auto_corrected = sum(s.get("expected_fail_actual_pass", 0) for s in all_stats.values())

    console.print(f"Total files tested: {total_files}")
    console.print(f"Successfully compiled: {total_compiled} ({100*total_compiled/total_files:.1f}%)")
    console.print(f"Auto-corrected (validity issue): {total_auto_corrected}")


if __name__ == "__main__":
    main()
