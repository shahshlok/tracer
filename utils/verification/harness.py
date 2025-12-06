"""Two-Phase Verification Harness.

Phase 1: Compiler Gate - Check if code compiles as expected based on error class.
Phase 2: Differential Execution - Compare seeded output to clean reference.

Simple. Clean. No legacy code.
"""

from __future__ import annotations

import asyncio
import hashlib
import json
from pathlib import Path
from typing import Any

from openai import AsyncOpenAI

from utils.execution import compile_only, compile_and_run

# Paths
CACHE_DIR = Path(".cache/verification")
INPUTS_PATH = Path("data/inputs.json")

# Error classes that expect compilation to FAIL
SYNTAX_ERRORS = {"Compile-Error", "Compile-Warning-Logic"}

# Error classes that expect compilation to SUCCEED
LOGIC_ERRORS = {"Runtime-Logic", "Runtime-Hang", "Runtime-Exception"}


def load_inputs() -> dict[str, dict[str, str]]:
    """Load test inputs for each question."""
    if not INPUTS_PATH.exists():
        return {}
    return json.loads(INPUTS_PATH.read_text(encoding="utf-8"))


class VerificationHarness:
    """Two-phase verification for generated student code."""
    
    def __init__(
        self,
        client: AsyncOpenAI,
        model: str,
        assignment: str,
        question_texts: dict[str, str],
        question_briefs: dict[str, str],
        misconceptions: list[dict[str, Any]] | None = None,
    ):
        self.client = client
        self.model = model
        self.assignment = assignment
        self.question_texts = question_texts
        self.question_briefs = question_briefs
        self._inputs = load_inputs()
        self._reference_cache: dict[str, str] = {}
        
        # Build misconception lookup
        self._misconceptions = {}
        if misconceptions:
            for m in misconceptions:
                if m.get("id"):
                    self._misconceptions[m["id"]] = m
        
        CACHE_DIR.mkdir(parents=True, exist_ok=True)
    
    def _get_input(self, question: str) -> str:
        """Get stdin input for a question."""
        return self._inputs.get(self.assignment, {}).get(question, "")
    
    def _get_error_class(self, misconception_id: str | None) -> str | None:
        """Get the error class for a misconception."""
        if not misconception_id:
            return None
        m = self._misconceptions.get(misconception_id)
        return m.get("error_class") if m else None
    
    def _cache_key(self, persona: str, question: str) -> str:
        """Generate cache key for reference output."""
        h = hashlib.sha256(persona.encode()).hexdigest()[:12]
        return f"{h}_{question}"
    
    async def _generate_clean_code(self, persona: str, question: str) -> str:
        """Generate a clean (correct) solution for comparison."""
        from utils.generators.dataset_generator import (
            build_messages,
            extract_text_from_response,
            strip_code_fences,
        )
        
        file_entry = {"type": "CLEAN", "instruction": None}
        messages = build_messages(
            persona=persona,
            question=question,
            question_text=self.question_texts[question],
            brief=self.question_briefs[question],
            file_entry=file_entry,
        )
        response = await self.client.responses.create(
            model=self.model,
            input=messages,
            max_output_tokens=800,
        )
        return strip_code_fences(extract_text_from_response(response))
    
    async def get_reference_output(self, persona: str, question: str) -> str | None:
        """Get (or generate and cache) reference output for comparison."""
        key = self._cache_key(persona, question)
        
        # Check memory cache
        if key in self._reference_cache:
            return self._reference_cache[key]
        
        # Check disk cache
        cache_file = CACHE_DIR / f"{key}.txt"
        if cache_file.exists():
            output = cache_file.read_text(encoding="utf-8")
            self._reference_cache[key] = output
            return output
        
        # Generate clean code and run it
        clean_code = await self._generate_clean_code(persona, question)
        stdin_input = self._get_input(question)
        
        compile_result, run_result = compile_and_run(clean_code, stdin_input)
        if not compile_result.success or run_result is None or not run_result.success:
            return None
        
        # Cache it
        output = run_result.stdout
        self._reference_cache[key] = output
        cache_file.write_text(output, encoding="utf-8")
        return output
    
    async def verify_submission(
        self,
        seeded_code: str,
        persona: str,
        question: str,
        misconception_id: str | None,
    ) -> tuple[bool, str]:
        """Two-phase verification.
        
        Phase 1 (Compiler Gate):
        - Syntax errors: Code MUST NOT compile
        - Logic errors: Code MUST compile
        
        Phase 2 (Differential Execution):
        - Logic errors: Output MUST differ from clean reference
        
        Returns:
            (is_valid, reason)
        """
        error_class = self._get_error_class(misconception_id)
        
        # PHASE 1: Compiler Gate
        compile_result = compile_only(seeded_code)
        
        if error_class in SYNTAX_ERRORS:
            # Expected to NOT compile
            if compile_result.success:
                return False, "SYNTAX_AUTOCORRECTED"
            return True, "SYNTAX_CONFIRMED"
        
        if error_class in LOGIC_ERRORS:
            # Expected to compile successfully
            if not compile_result.success:
                return False, "HALLUCINATED_SYNTAX"
        
        # If unknown error_class or clean, just check compilation
        if not compile_result.success:
            return False, "COMPILE_FAILED"
        
        # PHASE 2: Differential Execution
        reference_output = await self.get_reference_output(persona, question)
        if reference_output is None:
            return False, "NO_REFERENCE"
        
        stdin_input = self._get_input(question)
        compile_result, run_result = compile_and_run(seeded_code, stdin_input)
        
        if run_result is None:
            return False, "COMPILE_FAILED"
        
        if run_result.timed_out:
            # Timeout = different behavior = valid
            return True, "TIMEOUT_DIFFERENT"
        
        if run_result.stdout.strip() == reference_output.strip():
            return False, "SILENT_BUG"
        
        return True, "OK"
