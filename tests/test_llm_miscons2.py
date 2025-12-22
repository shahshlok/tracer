import asyncio
import json
from pathlib import Path
from typing import Any

import pytest

from llm_miscons2 import (
    DEFAULT_OUTPUT_DIR,
    SUBMISSION_DIR,
    get_student_list,
    process_student_question,
    run_detection,
)
from pydantic_models import LLMDetectionResponse, NotionalMisconception, Evidence
from utils.llm import openai as openai_module


class DummyLLMResponse(LLMDetectionResponse):
    @classmethod
    def single(cls, label: str) -> "DummyLLMResponse":
        evidence = Evidence(line_number=1, code_snippet="int x = 0;")
        mis = NotionalMisconception(
            inferred_category_name=label,
            student_thought_process="The student believes variables update automatically.",
            conceptual_gap="They confuse assignment with ongoing linkage.",
            error_manifestation="wrong output",
            confidence=0.9,
            evidence=[evidence],
        )
        return cls(misconceptions=[mis])


@pytest.mark.asyncio
async def test_process_student_question_uses_openai_helper(tmp_path, monkeypatch):
    submissions_root = tmp_path / "authentic_seeded" / "a3"
    submissions_root.mkdir(parents=True)

    student_dir = submissions_root / "StudentA"
    student_dir.mkdir()
    java_file = student_dir / "Q1.java"
    java_file.write_text("public class Q1 {}")

    questions_dir = tmp_path / "data" / "a3"
    questions_dir.mkdir(parents=True)
    (questions_dir / "q1.md").write_text("Question text")

    monkeypatch.setattr("llm_miscons2.SUBMISSION_DIR", submissions_root)
    monkeypatch.setattr("llm_miscons2.QUESTIONS_DIR", questions_dir)

    async def dummy_structured(messages, response_model, model):
        return DummyLLMResponse.single(label=model)

    async def dummy_reasoning(messages, response_model, model, reasoning_tokens=1024):
        return DummyLLMResponse.single(label=f"{model}-R")

    monkeypatch.setattr(openai_module, "get_structured_response", dummy_structured)
    monkeypatch.setattr(openai_module, "get_reasoning_response", dummy_reasoning)

    semaphore = asyncio.Semaphore(10)
    result = await process_student_question(
        student_id="StudentA",
        question="Q1",
        strategy="taxonomy",
        semaphore=semaphore,
        include_reasoning=True,
    )

    assert result["status"] == "success"
    assert result["student"] == "StudentA"
    assert result["question"] == "Q1"
    assert "models" in result
    assert result["models"]


def test_get_student_list_respects_submission_dir(tmp_path, monkeypatch):
    submissions_root = tmp_path / "authentic_seeded" / "a3"
    submissions_root.mkdir(parents=True)

    (submissions_root / "StudentA").mkdir()
    (submissions_root / "StudentB").mkdir()

    monkeypatch.setattr("llm_miscons2.SUBMISSION_DIR", submissions_root)

    students = get_student_list()
    assert students == ["StudentA", "StudentB"]


@pytest.mark.asyncio
async def test_run_detection_writes_outputs(tmp_path, monkeypatch):
    submissions_root = tmp_path / "authentic_seeded" / "a3"
    submissions_root.mkdir(parents=True)
    student_dir = submissions_root / "StudentA"
    student_dir.mkdir()
    for q in ["Q1", "Q2", "Q3", "Q4"]:
        (student_dir / f"{q}.java").write_text("public class Test {}")

    questions_dir = tmp_path / "data" / "a3"
    questions_dir.mkdir(parents=True)
    for q in ["q1", "q2", "q3", "q4"]:
        (questions_dir / f"{q}.md").write_text("Question text")

    output_dir = tmp_path / "detections" / "a3"
    output_dir.mkdir(parents=True)

    monkeypatch.setattr("llm_miscons2.SUBMISSION_DIR", submissions_root)
    monkeypatch.setattr("llm_miscons2.QUESTIONS_DIR", questions_dir)
    monkeypatch.setattr("llm_miscons2.DEFAULT_OUTPUT_DIR", output_dir)

    async def dummy_structured(messages, response_model, model):
        return DummyLLMResponse.single(label=model)

    async def dummy_reasoning(messages, response_model, model, reasoning_tokens=1024):
        return DummyLLMResponse.single(label=f"{model}-R")

    monkeypatch.setattr(openai_module, "get_structured_response", dummy_structured)
    monkeypatch.setattr(openai_module, "get_reasoning_response", dummy_reasoning)

    students = ["StudentA"]
    stats = await run_detection(
        students=students,
        strategy="taxonomy",
        output_dir=output_dir,
        include_reasoning=False,
    )

    assert stats["total_processed"] == 4
    assert stats["successful"] == 4

    strategy_dir = output_dir / "taxonomy"
    assert strategy_dir.exists()

    files = list(strategy_dir.glob("StudentA_Q*.json"))
    assert len(files) == 4

    stats_file = strategy_dir / "_stats.json"
    assert stats_file.exists()
    loaded_stats: dict[str, Any] = json.loads(stats_file.read_text())
    assert loaded_stats["strategy"] == "taxonomy"
