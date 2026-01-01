"""Java Execution Utilities.

Simple, clean functions for compiling and running Java code.
No legacy code, no fallbacks - just the essentials.
"""

from __future__ import annotations

import re
import subprocess
import tempfile
from dataclasses import dataclass
from pathlib import Path


@dataclass
class CompileResult:
    """Result of Java compilation."""

    success: bool
    stderr: str


@dataclass
class RunResult:
    """Result of Java execution."""

    success: bool
    stdout: str
    stderr: str
    timed_out: bool


def extract_class_name(java_source: str) -> str | None:
    """Extract the main class name from Java source code.

    Checks for public class first, then any class.
    """
    # Try public class first
    match = re.search(r"\bpublic\s+class\s+(\w+)", java_source)
    if match:
        return match.group(1)

    # Any class
    match = re.search(r"\bclass\s+(\w+)", java_source)
    return match.group(1) if match else None


def compile_and_run(
    java_source: str,
    stdin_input: str = "",
    compile_timeout: float = 30.0,
    run_timeout: float = 10.0,
) -> tuple[CompileResult, RunResult | None]:
    """Compile and run Java source code in an isolated temp directory.

    Returns:
        (CompileResult, RunResult | None)
        - RunResult is None if compilation fails
    """
    class_name = extract_class_name(java_source)
    if not class_name:
        return CompileResult(success=False, stderr="No class found in source"), None

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
                timeout=compile_timeout,
            )
            if result.returncode != 0:
                return CompileResult(success=False, stderr=result.stderr), None
        except subprocess.TimeoutExpired:
            return CompileResult(success=False, stderr="Compilation timed out"), None
        except FileNotFoundError:
            return CompileResult(success=False, stderr="javac not found"), None

        compile_result = CompileResult(success=True, stderr="")

        # Run
        try:
            result = subprocess.run(
                ["java", class_name],
                cwd=tmp_path,
                input=stdin_input,
                capture_output=True,
                text=True,
                timeout=run_timeout,
            )
            run_result = RunResult(
                success=(result.returncode == 0),
                stdout=result.stdout,
                stderr=result.stderr,
                timed_out=False,
            )
        except subprocess.TimeoutExpired:
            run_result = RunResult(
                success=False,
                stdout="",
                stderr="Execution timed out",
                timed_out=True,
            )
        except FileNotFoundError:
            run_result = RunResult(
                success=False,
                stdout="",
                stderr="java not found",
                timed_out=False,
            )

        return compile_result, run_result


def compile_only(java_source: str, timeout: float = 30.0) -> CompileResult:
    """Compile Java source code without running it."""
    class_name = extract_class_name(java_source)
    if not class_name:
        return CompileResult(success=False, stderr="No class found in source")

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
            return CompileResult(success=(result.returncode == 0), stderr=result.stderr)
        except subprocess.TimeoutExpired:
            return CompileResult(success=False, stderr="Compilation timed out")
        except FileNotFoundError:
            return CompileResult(success=False, stderr="javac not found")
