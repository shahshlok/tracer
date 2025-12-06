"""Execution utilities for Java code verification.

This module provides functions to compile and run Java code,
used for verifying generated student submissions.
"""

from __future__ import annotations

import subprocess
import tempfile
from dataclasses import dataclass
from pathlib import Path


@dataclass
class CompileResult:
    """Result of a Java compilation attempt."""

    success: bool
    return_code: int
    stdout: str
    stderr: str
    file_path: Path


def compile_java(file_path: Path | str, timeout: float = 30.0) -> CompileResult:
    """
    Compile a Java file using javac.

    Args:
        file_path: Path to the .java file to compile.
        timeout: Maximum seconds to wait for compilation.

    Returns:
        CompileResult with success status and output details.
    """
    file_path = Path(file_path)

    if not file_path.exists():
        return CompileResult(
            success=False,
            return_code=-1,
            stdout="",
            stderr=f"File not found: {file_path}",
            file_path=file_path,
        )

    if not file_path.suffix == ".java":
        return CompileResult(
            success=False,
            return_code=-1,
            stdout="",
            stderr=f"Not a Java file: {file_path}",
            file_path=file_path,
        )

    try:
        # Resolve to absolute path to avoid path issues
        abs_path = file_path.resolve()

        result = subprocess.run(
            ["javac", str(abs_path)],
            capture_output=True,
            text=True,
            timeout=timeout,
            cwd=abs_path.parent,  # Run in file's directory
        )

        return CompileResult(
            success=(result.returncode == 0),
            return_code=result.returncode,
            stdout=result.stdout,
            stderr=result.stderr,
            file_path=file_path,
        )

    except subprocess.TimeoutExpired:
        return CompileResult(
            success=False,
            return_code=-2,
            stdout="",
            stderr=f"Compilation timed out after {timeout}s",
            file_path=file_path,
        )

    except FileNotFoundError:
        return CompileResult(
            success=False,
            return_code=-3,
            stdout="",
            stderr="javac not found. Is Java installed?",
            file_path=file_path,
        )


def compile_all_in_directory(
    directory: Path | str,
    pattern: str = "*.java",
) -> list[CompileResult]:
    """
    Compile all Java files in a directory.

    Args:
        directory: Directory containing Java files.
        pattern: Glob pattern for finding files.

    Returns:
        List of CompileResult for each file.
    """
    directory = Path(directory)
    results = []

    for java_file in directory.glob(pattern):
        results.append(compile_java(java_file))

    return results


# Placeholder for Phase 2
@dataclass
class RunResult:
    """Result of running a Java program."""

    success: bool
    return_code: int
    stdout: str
    stderr: str
    timed_out: bool


def run_java(
    class_name: str,
    working_dir: Path | str,
    stdin_input: str = "",
    timeout: float = 10.0,
) -> RunResult:
    """
    Run a compiled Java class.

    Args:
        class_name: Name of the class to run (without .class extension).
        working_dir: Directory containing the .class file.
        stdin_input: Input to provide via stdin.
        timeout: Maximum seconds to wait for execution.

    Returns:
        RunResult with execution details.

    Note:
        This is a placeholder for Phase 2 (Differential Execution).
    """
    working_dir = Path(working_dir)

    try:
        result = subprocess.run(
            ["java", class_name],
            capture_output=True,
            text=True,
            timeout=timeout,
            cwd=working_dir,
            input=stdin_input,
        )

        return RunResult(
            success=(result.returncode == 0),
            return_code=result.returncode,
            stdout=result.stdout,
            stderr=result.stderr,
            timed_out=False,
        )

    except subprocess.TimeoutExpired:
        return RunResult(
            success=False,
            return_code=-1,
            stdout="",
            stderr=f"Execution timed out after {timeout}s",
            timed_out=True,
        )

    except FileNotFoundError:
        return RunResult(
            success=False,
            return_code=-2,
            stdout="",
            stderr="java not found. Is Java installed?",
            timed_out=False,
        )
