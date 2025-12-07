"""Synthetic Dataset Pipeline for generating student code with misconceptions.

This module implements a 6-step pipeline:
1. Generate correct code with persona
2. Compile correct code
3. Test correct code (must pass all tests)
4. Generate seeded code with misconception
5. Compile seeded code
6. Test seeded code (must fail at least 1 test + differ from correct)

Each step has 3-retry logic before discarding.
"""

from __future__ import annotations

import asyncio
import json
import random
import re
import subprocess
import tempfile
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path
from typing import Any

import typer
from dotenv import load_dotenv
from faker import Faker
from openai import AsyncOpenAI
from rich.console import Console
from rich.progress import Progress, SpinnerColumn, TextColumn, BarColumn, TimeElapsedColumn

load_dotenv()

console = Console()
app = typer.Typer(help="Generate synthetic student submissions with seeded misconceptions.")

# ============================================================================
# Configuration
# ============================================================================

DEFAULT_MODEL = "gpt-5.1-2025-11-13"
DEFAULT_STUDENT_COUNT = 10
DEFAULT_ASSIGNMENT = "a1"
MAX_RETRIES = 3

# ============================================================================
# Persona Matrix
# ============================================================================

CODING_STYLES = {
    "style_minimal": "Use single-letter variable names (x, y, n). No comments. Prefer one-liners. Avoid blank lines.",
    "style_verbose": "Use long, descriptive variable names (e.g., userInputValue). Add comments explaining each step in plain English.",
    "style_textbook": "Write clean, standard Java. Use camelCase. Consistent 4-space indentation. Minimal comments.",
    "style_messy": "Use inconsistent indentation (mix 2-space, 3-space, tabs). Mix camelCase and snake_case. Random blank lines.",
}

COGNITIVE_PROFILES = {
    "cog_procedural": "You think about code like following a recipe. Do step 1, then step 2, then you're done. Structure code linearly.",
    "cog_mathematical": "You love math. Structure code around formulas. Declare intermediate math variables like a, b, c before computing.",
    "cog_cautious": "You are nervous about edge cases. Add explicit checks (e.g., if (x != 0)) even when not strictly needed. Use temporary holder variables.",
}


def build_persona_prompt(style_id: str, cog_id: str) -> str:
    """Construct the full persona system prompt from style and cognitive profile."""
    base = "You are a CS1 student writing Java code."
    style = CODING_STYLES.get(style_id, "")
    cog = COGNITIVE_PROFILES.get(cog_id, "")
    return f"{base} {style} {cog}"


def get_persona_matrix() -> list[tuple[str, str]]:
    """Return all combinations of style × cognitive profile."""
    return [(s, c) for s in CODING_STYLES for c in COGNITIVE_PROFILES]


# ============================================================================
# Data Classes
# ============================================================================

@dataclass
class TestCase:
    """A single test case with input and expected output."""
    name: str
    input: str
    expected_output: str


@dataclass
class PipelineStats:
    """Track pipeline execution statistics."""
    correct_compile_failures: int = 0
    correct_test_failures: int = 0
    seeded_compile_failures: int = 0
    seeded_test_pass_failures: int = 0  # Seeded passed all tests (bad)
    seeded_no_diff_failures: int = 0  # Seeded is identical to correct (bad)
    seeded_fallback_to_clean: int = 0  # Students who got 4 clean codes due to seeding failure
    successful_samples: int = 0
    discarded_samples: int = 0

    def to_dict(self) -> dict[str, int]:
        return {
            "correct_compile_failures": self.correct_compile_failures,
            "correct_test_failures": self.correct_test_failures,
            "seeded_compile_failures": self.seeded_compile_failures,
            "seeded_test_pass_failures": self.seeded_test_pass_failures,
            "seeded_no_diff_failures": self.seeded_no_diff_failures,
            "seeded_fallback_to_clean": self.seeded_fallback_to_clean,
            "successful_samples": self.successful_samples,
            "discarded_samples": self.discarded_samples,
        }


@dataclass
class StudentSample:
    """A single student sample in the dataset."""
    student_id: str
    folder_name: str
    persona_style: str
    persona_cognitive: str
    seeded_question: str | None      # Which question has the bug (Q1-Q4) or None
    misconception_id: str | None
    misconception_name: str | None
    correct_codes: dict[str, str]    # {"Q1": "...", "Q2": "...", ...}
    seeded_code: str | None          # Only the seeded version of seeded_question



# ============================================================================
# Java Execution Helpers
# ============================================================================

def extract_class_name(java_source: str) -> str | None:
    """Extract the main class name from Java source code."""
    match = re.search(r'\bpublic\s+class\s+(\w+)', java_source)
    if match:
        return match.group(1)
    match = re.search(r'\bclass\s+(\w+)', java_source)
    return match.group(1) if match else None


def compile_java(java_source: str, timeout: float = 30.0) -> tuple[bool, str]:
    """Compile Java source code. Returns (success, stderr)."""
    class_name = extract_class_name(java_source)
    if not class_name:
        return False, "No class found in source"

    with tempfile.TemporaryDirectory() as tmp:
        tmp_path = Path(tmp)
        java_file = tmp_path / f"{class_name}.java"
        java_file.write_text(java_source, encoding="utf-8")

        try:
            result = subprocess.run(
                ["javac", str(java_file)],
                cwd=tmp_path,
                capture_output=True,
                text=True,
                timeout=timeout,
            )
            return result.returncode == 0, result.stderr
        except subprocess.TimeoutExpired:
            return False, "Compilation timed out"
        except FileNotFoundError:
            return False, "javac not found"


def run_java(java_source: str, stdin_input: str, timeout: float = 10.0) -> tuple[bool, str, str]:
    """Compile and run Java code. Returns (success, stdout, stderr)."""
    class_name = extract_class_name(java_source)
    if not class_name:
        return False, "", "No class found in source"

    with tempfile.TemporaryDirectory() as tmp:
        tmp_path = Path(tmp)
        java_file = tmp_path / f"{class_name}.java"
        java_file.write_text(java_source, encoding="utf-8")

        # Compile
        try:
            result = subprocess.run(
                ["javac", str(java_file)],
                cwd=tmp_path,
                capture_output=True,
                text=True,
                timeout=30.0,
            )
            if result.returncode != 0:
                return False, "", result.stderr
        except (subprocess.TimeoutExpired, FileNotFoundError) as e:
            return False, "", str(e)

        # Run
        try:
            result = subprocess.run(
                ["java", class_name],
                cwd=tmp_path,
                input=stdin_input,
                capture_output=True,
                text=True,
                timeout=timeout,
            )
            return result.returncode == 0, result.stdout, result.stderr
        except subprocess.TimeoutExpired:
            return False, "", "Execution timed out"
        except FileNotFoundError:
            return False, "", "java not found"


# ============================================================================
# Test Case Loading
# ============================================================================

def load_test_cases(tests_dir: Path, question: str) -> list[TestCase]:
    """Load test cases from the tests directory for a specific question."""
    question_dir = tests_dir / "reference"
    # For now, we'll use the reference solutions to validate
    # In the full implementation, we'd run JUnit tests
    
    # Simple test case loading from the question prompts
    test_cases = {
        "Q1": [
            TestCase("sample", "3 30.4 1.5", "18.266666666666666"),
            TestCase("simple", "0 100 10", "10.0"),
            TestCase("decel", "50 20 5", "-6.0"),
        ],
        "Q2": [
            TestCase("sample", "155\n23.5\n5.2", "34.29787234042553"),
            TestCase("round", "100\n25\n4", "16.0"),
        ],
        "Q3": [
            TestCase("sample", "1 3.5\n2.1 4.5", "1.4866068747318506"),
            TestCase("horizontal", "0 0\n5 0", "5.0"),
            TestCase("pythagorean", "0 0\n3 4", "5.0"),
        ],
        "Q4": [
            TestCase("sample", "0 0\n5 0\n0 5", "12.5"),
            TestCase("right", "0 0\n3 0\n0 4", "6.0"),
        ],
    }
    return test_cases.get(question, [])


def run_tests(java_source: str, test_cases: list[TestCase]) -> tuple[int, int, list[str]]:
    """Run test cases against Java code.
    
    Returns: (passed_count, total_count, failure_messages)
    """
    passed = 0
    failures = []
    
    for tc in test_cases:
        success, stdout, stderr = run_java(java_source, tc.input)
        if not success:
            failures.append(f"{tc.name}: Execution failed - {stderr}")
            continue
        
        # Check if expected output is in stdout (flexible matching)
        if tc.expected_output in stdout or tc.expected_output.rstrip('0').rstrip('.') in stdout:
            passed += 1
        else:
            failures.append(f"{tc.name}: Expected '{tc.expected_output}' in output, got '{stdout.strip()}'")
    
    return passed, len(test_cases), failures


# ============================================================================
# LLM Generation
# ============================================================================

def strip_code_fences(text: str) -> str:
    """Remove markdown code fences if present."""
    fenced = re.match(r"```[a-zA-Z]*\n(.+?)\n```", text, re.DOTALL)
    if fenced:
        return fenced.group(1).strip()
    return text.strip()


async def generate_correct_code(
    client: AsyncOpenAI,
    model: str,
    question_text: str,
    question_brief: str,
    persona_prompt: str,
    question: str,
) -> str:
    """Generate correct, working code for a question using the given persona."""
    system = f"{persona_prompt} Respond with Java source code only, no explanations or markdown fences."
    
    user = (
        f"Question {question}: {question_brief}\n\n"
        f"Full assignment text:\n{question_text}\n\n"
        f"Write a correct, working solution. The class MUST be named '{question}' (e.g., 'public class {question}'). "
        "The code must compile and produce the correct output. "
        "Keep the style consistent with your persona. Output only the Java code."
    )
    
    response = await client.responses.create(
        model=model,
        input=[
            {"role": "system", "content": system},
            {"role": "user", "content": user},
        ],
    )
    
    # Extract text from response
    text = ""
    for output in getattr(response, "output", []):
        for content in getattr(output, "content", []):
            if hasattr(content, "text"):
                text += content.text
    
    return strip_code_fences(text)


async def generate_seeded_code(
    client: AsyncOpenAI,
    model: str,
    correct_code: str,
    misconception: dict[str, Any],
    persona_prompt: str,
    question: str,
) -> str:
    """Inject a misconception into correct code."""
    explanation = misconception.get("explanation", "")
    category = misconception.get("category", "Misconception")
    student_thinking = misconception.get("student_thinking", "")
    specific_instruction = misconception.get("instructions_for_llm", {}).get(question, "")

    system = (
        f"{persona_prompt} You are a student with a specific misunderstanding of the Notional Machine."
        "Respond with Java source code only, no explanations or markdown fences."
    )
    
    user = (
        f"Here is a correct solution to the problem:\n"
        f"```java\n{correct_code}\n```\n\n"
        f"Your Task: Rewrite this code acting as a student who holds the '{category}' misconception.\n\n"
        f"Your Mental Model ({category}):\n"
        f"{explanation}\n\n"
        f"Your Internal Monologue:\n"
        f"{student_thinking}\n\n"
        f"Action Plan:\n"
        f"{specific_instruction}\n\n"
        "Instructions:\n"
        "- You believe your code is correct. Do not intentionally write 'bad' code, but write code that follows your flawed mental model.\n"
        "- Keep the original coding style (indentation, variable naming) of the provided code.\n"
        "- Ensure the class name remains the same as in the original code.\n"
        "- The code MUST compile.\n"
        "- Do NOT add comments explaining the error.\n"
        "Output only the rewritten Java code."
    )
    
    response = await client.responses.create(
        model=model,
        input=[
            {"role": "system", "content": system},
            {"role": "user", "content": user},
        ],
    )
    
    text = ""
    for output in getattr(response, "output", []):
        for content in getattr(output, "content", []):
            if hasattr(content, "text"):
                text += content.text
    
    return strip_code_fences(text)


# ============================================================================
# Pipeline Orchestration
# ============================================================================

async def generate_sample(
    client: AsyncOpenAI,
    model: str,
    question_texts: dict[str, str],
    question_briefs: dict[str, str],
    persona: tuple[str, str],
    seeded_question: str | None,
    misconception: dict[str, Any] | None,
    all_test_cases: dict[str, list[TestCase]],
    stats: PipelineStats,
) -> StudentSample | None:
    """Generate a complete assignment (all 4 questions) with optional misconception seeding.
    
    Args:
        seeded_question: Which question (Q1-Q4) to inject the misconception into, or None for all clean
        misconception: The misconception to inject (should be applicable to seeded_question)
        all_test_cases: Dict mapping question ID to test cases
    
    Returns:
        StudentSample with all 4 correct codes + optionally 1 seeded code, or None if generation failed
    """
    style_id, cog_id = persona
    persona_prompt = build_persona_prompt(style_id, cog_id)
    
    correct_codes = {}
    
    # Step 1-3: Generate and validate correct code for ALL 4 questions
    for question in ["Q1", "Q2", "Q3", "Q4"]:
        question_text = question_texts.get(question, "")
        question_brief = question_briefs.get(question, "")
        test_cases = all_test_cases.get(question, [])
        
        correct_code = None
        for attempt in range(MAX_RETRIES):
            try:
                code = await generate_correct_code(
                    client, model, question_text, question_brief, persona_prompt, question
                )
                
                # Step 2: Compile check
                compiles, stderr = compile_java(code)
                if not compiles:
                    console.print(f"  [yellow]{question} correct code compile failed (attempt {attempt+1}): {stderr[:100]}[/yellow]")
                    stats.correct_compile_failures += 1
                    continue
                
                # Step 3: Test check (all must pass)
                passed, total, failures = run_tests(code, test_cases)
                if passed < total:
                    console.print(f"  [yellow]{question} correct code tests failed (attempt {attempt+1}): {passed}/{total}[/yellow]")
                    stats.correct_test_failures += 1
                    continue
                
                correct_code = code
                break
            except Exception as e:
                console.print(f"  [red]Error generating {question} correct code: {e}[/red]")
        
        if not correct_code:
            # Failed to generate correct code for this question → discard entire student
            stats.discarded_samples += 1
            return None
        
        correct_codes[question] = correct_code
    
    # Step 4-6: Optionally generate seeded code for the designated question
    seeded_code = None
    
    if seeded_question and misconception:
        correct_code_for_seeding = correct_codes[seeded_question]
        test_cases_for_seeding = all_test_cases.get(seeded_question, [])
        
        for attempt in range(MAX_RETRIES):
            try:
                code = await generate_seeded_code(
                    client, model, correct_code_for_seeding, misconception, persona_prompt, seeded_question
                )
                
                # Step 5: Compile check
                compiles, stderr = compile_java(code)
                if not compiles:
                    console.print(f"  [yellow]{seeded_question} seeded code compile failed (attempt {attempt+1}): {stderr[:100]}[/yellow]")
                    stats.seeded_compile_failures += 1
                    continue
                
                # Check if code differs from correct
                if code.strip() == correct_code_for_seeding.strip():
                    console.print(f"  [yellow]{seeded_question} seeded code identical to correct (attempt {attempt+1})[/yellow]")
                    stats.seeded_no_diff_failures += 1
                    continue
                
                # Step 6: Test check (at least 1 must fail)
                passed, total, failures = run_tests(code, test_cases_for_seeding)
                if passed == total:
                    console.print(f"  [yellow]{seeded_question} seeded code passed all tests (attempt {attempt+1})[/yellow]")
                    stats.seeded_test_pass_failures += 1
                    continue
                
                seeded_code = code
                break
            except Exception as e:
                console.print(f"  [red]Error generating {seeded_question} seeded code: {e}[/red]")
        
        if not seeded_code:
            # Failed to inject misconception → fallback to clean submission
            console.print(f"  [cyan]{seeded_question} seeding failed, falling back to clean submission[/cyan]")
            stats.seeded_fallback_to_clean += 1
            seeded_question = None  # Mark as clean
    
    stats.successful_samples += 1
    return StudentSample(
        student_id="",
        folder_name="",
        persona_style=style_id,
        persona_cognitive=cog_id,
        seeded_question=seeded_question if seeded_code else None,
        misconception_id=misconception.get("id") if seeded_code else None,
        misconception_name=misconception.get("name") if seeded_code else None,
        correct_codes=correct_codes,
        seeded_code=seeded_code,
    )




# ============================================================================
# Main Entry Points
# ============================================================================

async def run_pipeline(
    assignment: str,
    student_count: int,
    model: str,
    output_root: Path,
    seed: int,
) -> None:
    """Run the full synthetic generation pipeline."""
    random.seed(seed)
    faker = Faker()
    faker.seed_instance(seed)
    
    # Load assignment data
    data_dir = Path("data") / assignment
    groundtruth_path = data_dir / "groundtruth.json"
    tests_dir = data_dir / "tests"
    
    if not groundtruth_path.exists():
        raise FileNotFoundError(f"Ground truth not found: {groundtruth_path}")
    
    misconceptions = json.loads(groundtruth_path.read_text())
    
    # Load question texts
    question_files = {
        "Q1": data_dir / "q1.md",
        "Q2": data_dir / "q2.md",
        "Q3": data_dir / "q3.md",
        "Q4": data_dir / "q4.md",
    }
    question_texts = {q: f.read_text() for q, f in question_files.items() if f.exists()}
    
    question_briefs = {
        "Q1": "Acceleration: compute (v1 - v0) / t using user input.",
        "Q2": "Road trip cost: (distance / mpg) * price using user input.",
        "Q3": "Distance between two points using sqrt((x2-x1)^2 + (y2-y1)^2).",
        "Q4": "Triangle area with Heron's formula; sides from point distances.",
    }
    
    # Setup output directories
    correct_dir = output_root / "correct"
    correct_dir.mkdir(parents=True, exist_ok=True)
    
    # Get persona matrix
    personas = get_persona_matrix()
    
    # Initialize client
    client = AsyncOpenAI()
    
    # Statistics
    stats = PipelineStats()
    
    # Generate students
    console.print(f"\n[bold cyan]Generating {student_count} students...[/bold cyan]\n")
    
    manifest_students = []
    
    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(),
        TimeElapsedColumn(),
        console=console,
    ) as progress:
        task = progress.add_task("Generating students", total=student_count)
        
        for i in range(student_count):
            first = faker.first_name()
            last = faker.last_name()
            student_id = random.randint(100000, 999999)
            folder_name = f"{last}_{first}_{student_id}"
            
            # Pick random persona
            style_id, cog_id = random.choice(personas)
            
            # Pick ONE random question to seed
            seeded_question = random.choice(["Q1", "Q2", "Q3", "Q4"])
            applicable_miscns = [m for m in misconceptions if seeded_question.upper() in [q.upper() for q in m.get("applicable_questions", [])]]
            misconception = random.choice(applicable_miscns) if applicable_miscns else None
            
            progress.update(task, description=f"Student {i+1}: {folder_name}")
            
            # Load test cases for ALL questions
            all_test_cases = {q: load_test_cases(tests_dir, q) for q in ["Q1", "Q2", "Q3", "Q4"]}
            
            # Generate sample (all 4 questions)
            sample = await generate_sample(
                client=client,
                model=model,
                question_texts=question_texts,
                question_briefs=question_briefs,
                persona=(style_id, cog_id),
                seeded_question=seeded_question,
                misconception=misconception,
                all_test_cases=all_test_cases,
                stats=stats,
            )
            
            if sample:
                sample.student_id = str(student_id)
                sample.folder_name = folder_name
                
                # Save all 4 correct codes
                student_correct_dir = correct_dir / folder_name
                student_correct_dir.mkdir(parents=True, exist_ok=True)
                for question, code in sample.correct_codes.items():
                    (student_correct_dir / f"{question}.java").write_text(code)
                
                # Save student submission folder (3 clean + 1 seeded OR 4 clean)
                student_submission_dir = output_root / folder_name
                student_submission_dir.mkdir(parents=True, exist_ok=True)
                
                for question in ["Q1", "Q2", "Q3", "Q4"]:
                    if question == sample.seeded_question and sample.seeded_code:
                        # Use seeded version
                        (student_submission_dir / f"{question}.java").write_text(sample.seeded_code)
                    else:
                        # Use correct version
                        (student_submission_dir / f"{question}.java").write_text(sample.correct_codes[question])
                
                # Add to manifest
                files_dict = {}
                for question in ["Q1", "Q2", "Q3", "Q4"]:
                    is_seeded = (question == sample.seeded_question and sample.seeded_code is not None)
                    files_dict[question] = {
                        "type": "SEEDED" if is_seeded else "CLEAN",
                        "misconception_id": sample.misconception_id if is_seeded else None,
                        "misconception_name": sample.misconception_name if is_seeded else None,
                    }
                
                manifest_students.append({
                    "folder_name": folder_name,
                    "student_id": student_id,
                    "first_name": first,
                    "last_name": last,
                    "persona_style": style_id,
                    "persona_cognitive": cog_id,
                    "files": files_dict
                })
            
            progress.advance(task)

    
    # Save manifest
    manifest = {
        "manifest_version": "2.0",
        "generated_at": datetime.utcnow().isoformat(),
        "seed": seed,
        "model": model,
        "assignment": assignment,
        "student_count": student_count,
        "students": manifest_students,
    }
    (output_root / "manifest.json").write_text(json.dumps(manifest, indent=2))
    
    # Save stats
    (output_root / "pipeline_stats.json").write_text(json.dumps(stats.to_dict(), indent=2))
    
    # Print summary
    console.print("\n[bold green]Pipeline Complete![/bold green]")
    console.print(f"  Successful samples: {stats.successful_samples}")
    console.print(f"  Discarded samples: {stats.discarded_samples}")
    console.print(f"  Correct compile failures: {stats.correct_compile_failures}")
    console.print(f"  Correct test failures: {stats.correct_test_failures}")
    console.print(f"  Seeded compile failures: {stats.seeded_compile_failures}")
    console.print(f"  Seeded passed all tests: {stats.seeded_test_pass_failures}")
    console.print(f"  Seeded identical to correct: {stats.seeded_no_diff_failures}")
    console.print(f"  Seeded fallback to clean: {stats.seeded_fallback_to_clean}")
    console.print(f"\nOutput saved to: {output_root}")


@app.command()
def generate(
    assignment: str = typer.Option(DEFAULT_ASSIGNMENT, help="Assignment ID (a1, a2, etc.)"),
    students: int = typer.Option(DEFAULT_STUDENT_COUNT, help="Number of students to generate"),
    model: str = typer.Option(DEFAULT_MODEL, help="OpenAI model to use"),
    output: Path = typer.Option(Path("authentic_seeded/a1"), help="Output directory"),
    seed: int = typer.Option(None, help="Random seed (default: current timestamp)"),
):
    """Generate synthetic student submissions with seeded misconceptions."""
    if seed is None:
        seed = int(datetime.utcnow().timestamp())
    
    console.print(f"[bold]Synthetic Dataset Pipeline[/bold]")
    console.print(f"  Assignment: {assignment}")
    console.print(f"  Students: {students}")
    console.print(f"  Model: {model}")
    console.print(f"  Seed: {seed}")
    console.print(f"  Output: {output}")
    
    asyncio.run(run_pipeline(assignment, students, model, output, seed))


if __name__ == "__main__":
    app()
