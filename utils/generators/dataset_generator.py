"""Synthetic dataset generator for seeded student submissions.

This script builds a manifest and optionally generates Java files via the OpenAI
Responses API. It supports manifest-only and generation-only runs, plus a full
pipeline mode, and keeps per-student personas consistent across files.
"""

from __future__ import annotations

import asyncio
import json
import random
import re
from datetime import datetime
from pathlib import Path
from typing import Any

import typer
from dotenv import load_dotenv
from faker import Faker
from openai import AsyncOpenAI
from rich.console import Console
from rich.progress import (
    BarColumn,
    Progress,
    SpinnerColumn,
    TextColumn,
    TimeElapsedColumn,
)

console = Console()
app = typer.Typer(help="Generate seeded synthetic Java submissions for Q1-Q4.")

load_dotenv()

DEFAULT_MODEL = "gpt-5.1-2025-11-13"
DEFAULT_STUDENT_COUNT = 60
DEFAULT_OUTPUT_ROOT = Path("authentic_seeded/a1")
DEFAULT_MANIFEST_PATH = DEFAULT_OUTPUT_ROOT / "manifest.json"

# Assignment-specific question files and briefs
ASSIGNMENTS = {
    "a1": {
        "question_files": {
            "Q1": Path("data/a1/q1.md"),
            "Q2": Path("data/a1/q2.md"),
            "Q3": Path("data/a1/q3.md"),
            "Q4": Path("data/a1/q4.md"),
        },
        "question_briefs": {
            "Q1": "Acceleration: compute (v1 - v0) / t using user input.",
            "Q2": "Road trip cost: (distance / mpg) * price using user input.",
            "Q3": "Distance between two points using sqrt((x2-x1)^2 + (y2-y1)^2).",
            "Q4": "Triangle area with Heron's formula; sides from point distances (Q3 logic).",
        },
        "groundtruth": Path("data/a1/groundtruth.json"),
    },
}

# Legacy aliases for backward compatibility
QUESTION_FILES = ASSIGNMENTS["a1"]["question_files"]
QUESTION_BRIEFS = ASSIGNMENTS["a1"]["question_briefs"]

PERSONAS = [
    "Single-letter variables, minimal whitespace, no comments.",
    "Verbose comments everywhere, snake_case variables, standard indentation.",
    "All-caps variable names, mixed indentation, braces on new lines.",
    "Overly compact one-liners, heavy use of inline calculations, no spacing.",
    "Tabs for indentation, braces on separate lines, prints extra debug statements.",
    "Extra blank lines, descriptive camelCase variables, polite prompts to the user.",
    "Uses System.out.printf for everything, avoids temporary variables.",
    "Adds helper methods unnecessarily, tries to 'organize' novice code.",
    "Copies textbook style: clear spacing, camelCase, minimal comments.",
    "Pretends to optimize: reuses variables aggressively, minimal Scanner prompts.",
    "Likes while-loops for input, inconsistent indentation width, mixes tabs/spaces.",
    "Heavy String concatenation for outputs, avoids printf, lots of println calls.",
    "Puts opening braces at end-of-line, tight spacing around operators, no blank lines.",
    "Random debug prints with variable states, overuses temporary holders like temp1.",
    "Tries to be fancy with ternaries where simple assignments would do.",
    "Spacing before semicolons and parentheses, awkwardly placed comments mid-line.",
    "Inserts TODO comments and leaves half-finished thoughts, but code still runs.",
    "Always closes Scanner immediately after reads, even inside mid-logic.",
    "Mixes camelCase and snake_case in the same file, inconsistent naming.",
    "Breaks long formulas into many small steps with descriptive variable names.",
]


# --------------------------------------------------------------------------- #
# Interactive helpers
# --------------------------------------------------------------------------- #


def choose_path(label: str, default: Path, must_exist: bool = False) -> Path:
    """Ask whether to use the default path, otherwise prompt for a new one."""
    default = Path(default)
    if typer.confirm(f"Use default {label}? ({default})", default=True):
        path = default
    else:
        while True:
            raw = typer.prompt(f"Enter {label} path").strip()
            if not raw:
                console.print("[yellow]Path cannot be empty.[/yellow]")
                continue
            path = Path(raw).expanduser()
            if must_exist and not path.exists():
                console.print(f"[red]Path not found: {path}. Try again.[/red]")
                continue
            break
    path = path.expanduser()
    if must_exist and not path.exists():
        raise FileNotFoundError(path)
    return path


def choose_int(label: str, default: int, min_value: int = 1) -> int:
    if typer.confirm(f"Use default {label}? ({default})", default=True):
        return default
    while True:
        raw = typer.prompt(f"Enter {label}").strip()
        try:
            value = int(raw)
            if value < min_value:
                raise ValueError
            return value
        except ValueError:
            console.print(f"[red]{label} must be an integer >= {min_value}.[/red]")


def choose_str(label: str, default: str) -> str:
    if typer.confirm(f"Use default {label}? ({default})", default=True):
        return default
    raw = typer.prompt(f"Enter {label}").strip()
    return raw or default


# --------------------------------------------------------------------------- #
# Helpers for file loading and text munging
# --------------------------------------------------------------------------- #


def load_question_texts(assignment: str = "a1") -> dict[str, str]:
    """Load full markdown for Q1-Q4 from disk for the specified assignment."""
    if assignment not in ASSIGNMENTS:
        raise ValueError(f"Unknown assignment: {assignment}. Valid: {list(ASSIGNMENTS.keys())}")
    question_files = ASSIGNMENTS[assignment]["question_files"]
    texts: dict[str, str] = {}
    for q, path in question_files.items():
        if not path.exists():
            raise FileNotFoundError(f"Missing question prompt: {path}")
        texts[q] = path.read_text(encoding="utf-8").strip()
    return texts


def load_misconceptions(path: Path) -> list[dict[str, Any]]:
    """Load misconceptions JSON."""
    if not path.exists():
        raise FileNotFoundError(f"Missing misconceptions file: {path}")
    data = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(data, list):
        raise ValueError("Misconceptions file must contain a list.")
    return data


def strip_code_fences(text: str) -> str:
    """Remove markdown code fences if present."""
    fenced = re.match(r"```[a-zA-Z]*\n(.+?)\n```", text, re.DOTALL)
    if fenced:
        return fenced.group(1).strip()
    return text.strip()


def instruction_for_question(instructions: dict[str, str], question: str) -> str | None:
    """Pick the instruction string for a specific question, handling combined keys."""
    if question in instructions:
        return instructions[question]

    q_lower = question.lower()
    for key, value in instructions.items():
        key_lower = key.lower()
        # Direct mention of the question token
        parts = [part for part in re.split(r"[^\w]+", key_lower) if part]
        if q_lower in parts:
            return value
        # Generic instructions like "Any Q" or "All Qs"
        if "all" in key_lower or "any" in key_lower:
            return value
    return None


def question_applies(misconception: dict[str, Any], question: str) -> bool:
    """Check if a misconception applies to a question."""
    applicable = misconception.get("applicable_questions", [])
    return question.upper() in {q.upper() for q in applicable}


# --------------------------------------------------------------------------- #
# Manifest generation
# --------------------------------------------------------------------------- #


def generate_manifest(
    misconceptions: list[dict[str, Any]],
    question_texts: dict[str, str],
    seed: int,
    student_count: int = DEFAULT_STUDENT_COUNT,
    assignment: str = "a1",
) -> dict[str, Any]:
    """Construct the manifest structure.

    Distribution (realistic classroom):
    - 40% perfect students: 0 misconceptions (all clean files)
    - 35% single-issue: 1 misconception (1 seeded, 3 clean)
    - 20% struggling: 2 misconceptions spread across questions (2 seeded, 2 clean)
    - 5% severely struggling: 3 misconceptions spread across questions (3 seeded, 1 clean)

    Key constraint: Each file has AT MOST one seeded misconception.
    """
    random.seed(seed)
    faker = Faker()
    faker.seed_instance(seed)

    # Get assignment-specific config
    question_briefs = ASSIGNMENTS[assignment]["question_briefs"]
    questions = list(ASSIGNMENTS[assignment]["question_files"].keys())

    # Calculate student counts per category
    n_perfect = int(student_count * 0.40)
    n_single = int(student_count * 0.35)
    n_struggling = int(student_count * 0.20)
    n_severe = student_count - n_perfect - n_single - n_struggling  # remainder (~5%)

    # Create student type assignments
    student_types: list[int] = (
        [0] * n_perfect  # 0 misconceptions
        + [1] * n_single  # 1 misconception
        + [2] * n_struggling  # 2 misconceptions
        + [3] * n_severe  # 3 misconceptions
    )
    random.shuffle(student_types)

    used_ids = set()
    students: list[dict[str, Any]] = []

    for misconception_count in student_types:
        first = faker.first_name()
        last = faker.last_name()
        student_id = random.randint(100000, 999999)
        while student_id in used_ids:
            student_id = random.randint(100000, 999999)
        used_ids.add(student_id)

        persona = random.choice(PERSONAS)
        files: dict[str, dict[str, Any]] = {}
        assigned_misconception_ids: list[str] = []

        if misconception_count == 0:
            # Perfect student: all clean files
            for q in questions:
                files[q] = {"type": "CLEAN", "misconception_id": None, "instruction": None}
        else:
            # Assign misconceptions to specific questions (one per file max)
            # Shuffle questions to randomly distribute misconceptions
            shuffled_qs = questions.copy()
            random.shuffle(shuffled_qs)
            seeded_questions = shuffled_qs[:misconception_count]

            for q in questions:
                if q in seeded_questions:
                    # Find a misconception applicable to this question
                    applicable = [m for m in misconceptions if question_applies(m, q)]
                    if applicable:
                        chosen = random.choice(applicable)
                        instructions = chosen.get("instructions", {}) or {}
                        specific = instruction_for_question(instructions, q)
                        fallback = chosen.get("misconception_explanation") or chosen.get(
                            "misconception_name"
                        )
                        files[q] = {
                            "type": "SEEDED",
                            "misconception_id": chosen.get("id"),
                            "misconception_name": chosen.get("misconception_name"),
                            "instruction": specific or fallback,
                            "student_thinking": chosen.get("student_thinking", ""),
                        }
                        assigned_misconception_ids.append(chosen.get("id"))
                    else:
                        # No applicable misconception for this question, make it clean
                        files[q] = {"type": "CLEAN", "misconception_id": None, "instruction": None}
                else:
                    files[q] = {"type": "CLEAN", "misconception_id": None, "instruction": None}

        folder_name = f"{last}_{first}_{student_id}"
        students.append(
            {
                "folder_name": folder_name,
                "first_name": first,
                "last_name": last,
                "student_id": student_id,
                "persona": persona,
                "assigned_misconceptions": assigned_misconception_ids,
                "files": files,
            }
        )

    return {
        "generated_at": datetime.utcnow().isoformat(),
        "seed": seed,
        "student_count": student_count,
        "assignment": assignment,
        "model": DEFAULT_MODEL,
        "questions": question_briefs,
        "question_texts": question_texts,
        "students": students,
    }


def write_manifest(manifest: dict[str, Any], path: Path, force: bool = False) -> None:
    """Persist manifest to disk."""
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists() and not force:
        raise FileExistsError(f"Manifest already exists at {path}. Use --force to overwrite.")
    path.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    console.print(f"[green]Manifest written to {path}[/green]")


# --------------------------------------------------------------------------- #
# OpenAI Responses client helpers
# --------------------------------------------------------------------------- #


def extract_text_from_response(response: Any) -> str:
    """Extract text content from a Responses API result."""
    # Expected path: response.output[0].content[0].text
    try:
        outputs = getattr(response, "output", None) or []
        collected: list[str] = []
        for output in outputs:
            for content in getattr(output, "content", []) or []:
                text = getattr(content, "text", None)
                if text:
                    collected.append(text)
        if collected:
            return "\n".join(collected).strip()
    except Exception:
        pass

    # Fallbacks for unexpected shapes
    maybe_text = getattr(response, "output_text", None)
    if maybe_text:
        return str(maybe_text).strip()
    return str(response)


def build_messages(
    persona: str,
    question: str,
    question_text: str,
    brief: str,
    file_entry: dict[str, Any],
) -> list[dict[str, str]]:
    """Construct system and user messages for Responses API."""
    system = (
        "You are a CS1 student writing Java code. "
        f"Your coding style is: {persona} "
        "Respond with Java source code only, no explanations or markdown fences."
    )

    instruction = file_entry.get("instruction") or "Introduce the specified conceptual error."
    student_thinking = file_entry.get("student_thinking") or ""

    if file_entry["type"] == "SEEDED":
        thinking_section = ""
        if student_thinking:
            thinking_section = f"\nYour mindset as this student: {student_thinking}\n"
        user = (
            f"Question {question}: {brief}\n\n"
            f"Full assignment text:\n{question_text}\n\n"
            "Write a solution for this question. "
            "You must include the following specific conceptual error:\n"
            f"{instruction}"
            f"{thinking_section}\n"
            "Keep the style consistent with the given persona. Output only the Java code."
        )
    else:
        user = (
            f"Question {question}: {brief}\n\n"
            f"Full assignment text:\n{question_text}\n\n"
            "Write a correct, working, novice-level solution. "
            "Keep the style consistent with the persona. Output only the Java code."
        )

    return [
        {"role": "system", "content": system},
        {"role": "user", "content": user},
    ]


async def generate_file(
    client: AsyncOpenAI,
    model: str,
    persona: str,
    question: str,
    question_text: str,
    brief: str,
    file_entry: dict[str, Any],
    output_path: Path,
    semaphore: asyncio.Semaphore | None = None,
    max_retries: int = 3,
) -> None:
    """Generate a single Java file."""

    async def _call_api() -> str:
        messages = build_messages(persona, question, question_text, brief, file_entry)
        response = await client.responses.create(
            model=model,
            input=messages,
            max_output_tokens=800,
        )
        return extract_text_from_response(response)

    attempt = 0
    last_error: Exception | None = None

    while attempt < max_retries:
        try:
            if semaphore:
                async with semaphore:
                    text = await _call_api()
            else:
                text = await _call_api()
            cleaned = strip_code_fences(text)
            output_path.parent.mkdir(parents=True, exist_ok=True)
            output_path.write_text(cleaned + "\n", encoding="utf-8")
            return
        except Exception as exc:  # noqa: BLE001
            last_error = exc
            attempt += 1
            await asyncio.sleep(min(2**attempt, 8))

    raise RuntimeError(
        f"Failed to generate {output_path} after {max_retries} attempts"
    ) from last_error


# --------------------------------------------------------------------------- #
# Orchestration
# --------------------------------------------------------------------------- #


async def run_generation(
    manifest_path: Path,
    output_root: Path,
    model: str,
    concurrency: int,
    dry_run: bool = False,
) -> None:
    """Generate Java files from an existing manifest."""
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    assignment = manifest.get("assignment", "a1")
    question_texts = manifest.get("question_texts") or load_question_texts(assignment)
    question_briefs = ASSIGNMENTS[assignment]["question_briefs"]
    students = manifest.get("students", [])
    semaphore = asyncio.Semaphore(concurrency) if concurrency and concurrency > 0 else None

    client = AsyncOpenAI()

    tasks = []
    for student in students:
        persona = student["persona"]
        folder = output_root / student["folder_name"]
        for question, file_entry in student["files"].items():
            target_path = folder / f"{question}.java"
            if dry_run:
                console.print(f"[cyan]DRY RUN[/cyan] would write {target_path}")
                continue

            tasks.append(
                generate_file(
                    client=client,
                    model=model,
                    persona=persona,
                    question=question,
                    question_text=question_texts[question],
                    brief=question_briefs[question],
                    file_entry=file_entry,
                    output_path=target_path,
                    semaphore=semaphore,
                )
            )

    if dry_run:
        return

    progress = Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        BarColumn(),
        TimeElapsedColumn(),
        console=console,
    )

    with progress:
        task_id = progress.add_task("Generating Java files", total=len(tasks))

        async def runner(coro):
            try:
                await coro
            finally:
                progress.advance(task_id)

        await asyncio.gather(*(runner(task) for task in tasks))


# --------------------------------------------------------------------------- #
# CLI commands
# --------------------------------------------------------------------------- #


@app.command()
def manifest(
    assignment: str = typer.Option(
        "a1",
        help="Assignment to generate for.",
    ),
    manifest_path: Path = typer.Option(
        None,
        help="Where to write the manifest. Defaults to authentic_seeded/<assignment>/manifest.json.",
    ),
    students: int = typer.Option(DEFAULT_STUDENT_COUNT, help="Number of students to simulate."),
    seed: int | None = typer.Option(None, help="Random seed. Defaults to current UNIX time."),
    force: bool = typer.Option(False, help="Overwrite existing manifest if present."),
):
    """Generate manifest.json only."""
    if assignment not in ASSIGNMENTS:
        console.print(f"[red]Unknown assignment: {assignment}. Valid: {list(ASSIGNMENTS.keys())}[/red]")
        raise typer.Exit(1)
    if manifest_path is None:
        manifest_path = DEFAULT_OUTPUT_ROOT / assignment / "manifest.json"
    if seed is None:
        seed = int(datetime.utcnow().timestamp())
    question_texts = load_question_texts(assignment)
    misconceptions = load_misconceptions(ASSIGNMENTS[assignment]["groundtruth"])
    data = generate_manifest(misconceptions, question_texts, seed=seed, student_count=students, assignment=assignment)
    write_manifest(data, manifest_path, force=force)


@app.command()
def generate(
    manifest_path: Path = typer.Option(DEFAULT_MANIFEST_PATH, help="Path to manifest.json."),
    output_root: Path = typer.Option(None, help="Root output directory. Auto-detected from manifest if not set."),
    model: str = typer.Option(DEFAULT_MODEL, help="OpenAI model to use."),
    concurrency: int = typer.Option(20, help="Max concurrent requests (0 or 1 for sequential)."),
    dry_run: bool = typer.Option(False, help="Skip API calls and file writes."),
):
    """Generate Java files from an existing manifest."""
    if not manifest_path.exists():
        raise FileNotFoundError(f"Manifest not found at {manifest_path}")
    # Auto-detect output_root from manifest parent if not specified
    if output_root is None:
        output_root = manifest_path.parent
    asyncio.run(run_generation(manifest_path, output_root, model, concurrency, dry_run=dry_run))
    console.print("[green]Generation complete[/green]")


@app.command()
def run(
    assignment: str = typer.Option(
        "a1",
        help="Assignment to generate for.",
    ),
    manifest_path: Path = typer.Option(
        None,
        help="Where to write the manifest. Defaults to authentic_seeded/<assignment>/manifest.json.",
    ),
    output_root: Path = typer.Option(
        None,
        help="Root output directory. Defaults to authentic_seeded/<assignment>/.",
    ),
    students: int = typer.Option(DEFAULT_STUDENT_COUNT, help="Number of students to simulate."),
    seed: int | None = typer.Option(None, help="Random seed. Defaults to current UNIX time."),
    model: str = typer.Option(DEFAULT_MODEL, help="OpenAI model to use."),
    concurrency: int = typer.Option(20, help="Max concurrent requests (0 or 1 for sequential)."),
    force: bool = typer.Option(False, help="Overwrite manifest if it exists."),
    dry_run: bool = typer.Option(False, help="Skip API calls and file writes."),
):
    """Full pipeline: manifest then generation."""
    if assignment not in ASSIGNMENTS:
        console.print(f"[red]Unknown assignment: {assignment}. Valid: {list(ASSIGNMENTS.keys())}[/red]")
        raise typer.Exit(1)
    if manifest_path is None:
        manifest_path = DEFAULT_OUTPUT_ROOT / assignment / "manifest.json"
    if output_root is None:
        output_root = DEFAULT_OUTPUT_ROOT / assignment
    if seed is None:
        seed = int(datetime.utcnow().timestamp())
    question_texts = load_question_texts(assignment)
    misconceptions = load_misconceptions(ASSIGNMENTS[assignment]["groundtruth"])
    data = generate_manifest(misconceptions, question_texts, seed=seed, student_count=students, assignment=assignment)
    write_manifest(data, manifest_path, force=force)
    asyncio.run(run_generation(manifest_path, output_root, model, concurrency, dry_run=dry_run))
    console.print("[green]Full pipeline complete[/green]")


def interactive_main() -> None:
    """Interactive CLI entry point."""
    console.print("[bold cyan]Misconception Dataset Generator[/bold cyan]")
    defaults = {
        "misconceptions_path": Path("data/a1/groundtruth.json"),
        "manifest_path": DEFAULT_MANIFEST_PATH,
        "output_root": DEFAULT_OUTPUT_ROOT,
        "students": DEFAULT_STUDENT_COUNT,
        "model": DEFAULT_MODEL,
        "concurrency": 20,
    }

    while True:
        console.print(
            "\n[bold]Choose an option:[/bold]\n"
            "1) Generate manifest only\n"
            "2) Run generation from manifest\n"
            "3) Full pipeline (manifest + generation)\n"
            "4) Exit"
        )
        choice = typer.prompt("Selection", default="1").strip()

        if choice == "1":
            m_path = choose_path(
                "misconceptions file", defaults["misconceptions_path"], must_exist=True
            )
            manifest_path = choose_path("manifest output path", defaults["manifest_path"])
            students = choose_int("number of students", defaults["students"])
            use_default_seed = typer.confirm("Use current UNIX time as seed?", default=True)
            seed = (
                None
                if use_default_seed
                else choose_int("custom seed", int(datetime.utcnow().timestamp()), min_value=0)
            )
            force = typer.confirm("Overwrite manifest if exists?", default=False)
            try:
                manifest(
                    misconceptions_path=m_path,
                    manifest_path=manifest_path,
                    students=students,
                    seed=seed,
                    force=force,
                )
            except Exception as exc:  # noqa: BLE001
                console.print(f"[red]Error: {exc}[/red]")
        elif choice == "2":
            manifest_path = choose_path("manifest path", defaults["manifest_path"], must_exist=True)
            output_root = choose_path("output root", defaults["output_root"])
            model = choose_str("model", defaults["model"])
            concurrency = choose_int(
                "max concurrency (1 = sequential)", defaults["concurrency"], min_value=1
            )
            dry_run = typer.confirm("Dry run (no API calls or writes)?", default=False)
            try:
                generate(
                    manifest_path=manifest_path,
                    output_root=output_root,
                    model=model,
                    concurrency=concurrency,
                    dry_run=dry_run,
                )
            except Exception as exc:  # noqa: BLE001
                console.print(f"[red]Error: {exc}[/red]")
        elif choice == "3":
            m_path = choose_path(
                "misconceptions file", defaults["misconceptions_path"], must_exist=True
            )
            manifest_path = choose_path("manifest output path", defaults["manifest_path"])
            output_root = choose_path("output root", defaults["output_root"])
            students = choose_int("number of students", defaults["students"])
            use_default_seed = typer.confirm("Use current UNIX time as seed?", default=True)
            seed = (
                None
                if use_default_seed
                else choose_int("custom seed", int(datetime.utcnow().timestamp()), min_value=0)
            )
            model = choose_str("model", defaults["model"])
            concurrency = choose_int(
                "max concurrency (1 = sequential)", defaults["concurrency"], min_value=1
            )
            force = typer.confirm("Overwrite manifest if exists?", default=False)
            dry_run = typer.confirm("Dry run (skip API calls)?", default=False)
            try:
                run(
                    misconceptions_path=m_path,
                    manifest_path=manifest_path,
                    output_root=output_root,
                    students=students,
                    seed=seed,
                    model=model,
                    concurrency=concurrency,
                    force=force,
                    dry_run=dry_run,
                )
            except Exception as exc:  # noqa: BLE001
                console.print(f"[red]Error: {exc}[/red]")
        elif choice == "4":
            console.print("[green]Goodbye![/green]")
            break
        else:
            console.print("[red]Invalid selection. Please choose 1-4.[/red]")


if __name__ == "__main__":
    app()
