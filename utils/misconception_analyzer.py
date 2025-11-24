"""Misconception analysis module for aggregating and analyzing student misconceptions.

This module processes evaluation JSON files from student_evals/ directory and provides:
- Per-student misconception analysis
- Class-wide misconception aggregation
- Bloom's taxonomy level difficulty analysis
- Task-based difficulty analysis
- Model agreement metrics
"""

import json
from collections import defaultdict
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path

from pydantic_models.evaluation import EvaluationDocument


@dataclass
class MisconceptionRecord:
    """A single misconception record with metadata."""

    student_id: str
    model_name: str
    bloom_level: str
    task: str
    name: str
    description: str
    confidence: float
    evidence_count: int


@dataclass
class BloomTaskStats:
    """Statistics for a Bloom level + Task combination."""

    bloom_level: str
    task: str
    student_count: int = 0
    total_students: int = 0
    misconception_count: int = 0
    avg_confidence: float = 0.0
    model_agreement_rate: float = 0.0
    common_misconceptions: list[tuple[str, int]] = field(default_factory=list)

    @property
    def percentage_affected(self) -> float:
        if self.total_students == 0:
            return 0.0
        return (self.student_count / self.total_students) * 100


@dataclass
class MisconceptionTypeStats:
    """Statistics for a specific misconception type/name."""

    name: str
    bloom_level: str
    task: str
    student_count: int = 0
    total_students: int = 0
    occurrence_count: int = 0
    avg_confidence: float = 0.0
    models_detecting: set = field(default_factory=set)

    @property
    def percentage_affected(self) -> float:
        if self.total_students == 0:
            return 0.0
        return (self.student_count / self.total_students) * 100

    @property
    def model_agreement_count(self) -> int:
        return len(self.models_detecting)


@dataclass
class StudentAnalysis:
    """Analysis results for a single student."""

    student_id: str
    student_name: str
    total_misconceptions: int = 0
    misconceptions_by_bloom: dict = field(default_factory=dict)
    misconceptions_by_task: dict = field(default_factory=dict)
    model_agreement: dict = field(default_factory=dict)
    weighted_severity: float = 0.0


@dataclass
class ClassAnalysis:
    """Class-wide analysis results."""

    total_students: int = 0
    total_misconceptions: int = 0
    bloom_task_stats: list[BloomTaskStats] = field(default_factory=list)
    misconception_type_stats: list[MisconceptionTypeStats] = field(default_factory=list)
    model_agreement_summary: dict = field(default_factory=dict)
    generated_at: datetime = field(default_factory=datetime.now)


class MisconceptionAnalyzer:
    """Analyzes misconceptions across student evaluations."""

    def __init__(self, evals_dir: str = "student_evals"):
        self.evals_dir = evals_dir
        self.evaluations: list[EvaluationDocument] = []
        self.misconception_records: list[MisconceptionRecord] = []

    def load_evaluations(self) -> int:
        """Load all evaluation JSON files from the evals directory.

        Returns:
            Number of evaluations loaded.
        """
        self.evaluations = []
        evals_path = Path(self.evals_dir)

        if not evals_path.exists():
            return 0

        for json_file in sorted(evals_path.glob("*_eval.json")):
            try:
                with open(json_file) as f:
                    data = json.load(f)
                eval_doc = EvaluationDocument(**data)
                self.evaluations.append(eval_doc)
            except Exception as e:
                print(f"Warning: Could not load {json_file}: {e}")

        return len(self.evaluations)

    def extract_misconceptions(self) -> list[MisconceptionRecord]:
        """Extract all misconceptions from loaded evaluations.

        Returns:
            List of MisconceptionRecord objects.
        """
        self.misconception_records = []

        for eval_doc in self.evaluations:
            student_id = eval_doc.submission.student_id

            for model_name, model_eval in eval_doc.models.items():
                for misconception in model_eval.misconceptions:
                    record = MisconceptionRecord(
                        student_id=student_id,
                        model_name=model_name,
                        bloom_level=misconception.bloom_level,
                        task=misconception.task,
                        name=misconception.name,
                        description=misconception.description,
                        confidence=misconception.confidence,
                        evidence_count=len(misconception.evidence),
                    )
                    self.misconception_records.append(record)

        return self.misconception_records

    def analyze_student(self, student_id: str) -> StudentAnalysis | None:
        """Analyze misconceptions for a specific student.

        Args:
            student_id: The student identifier.

        Returns:
            StudentAnalysis object or None if student not found.
        """
        eval_doc = None
        for e in self.evaluations:
            if e.submission.student_id == student_id:
                eval_doc = e
                break

        if not eval_doc:
            return None

        analysis = StudentAnalysis(
            student_id=student_id,
            student_name=eval_doc.submission.student_name,
        )

        bloom_counts: dict[str, int] = defaultdict(int)
        task_counts: dict[str, int] = defaultdict(int)
        misconception_by_name: dict[str, set[str]] = defaultdict(set)
        total_weighted_confidence = 0.0
        total_count = 0

        for model_name, model_eval in eval_doc.models.items():
            for misconception in model_eval.misconceptions:
                bloom_counts[misconception.bloom_level] += 1
                task_counts[misconception.task] += 1
                misconception_by_name[misconception.name].add(model_name)
                total_weighted_confidence += misconception.confidence
                total_count += 1

        analysis.total_misconceptions = total_count
        analysis.misconceptions_by_bloom = dict(bloom_counts)
        analysis.misconceptions_by_task = dict(task_counts)
        analysis.model_agreement = {
            name: len(models) for name, models in misconception_by_name.items()
        }

        if total_count > 0:
            analysis.weighted_severity = total_weighted_confidence / total_count

        return analysis

    def analyze_class(self) -> ClassAnalysis:
        """Perform class-wide misconception analysis.

        Returns:
            ClassAnalysis object with aggregated statistics.
        """
        if not self.misconception_records:
            self.extract_misconceptions()

        total_students = len(self.evaluations)
        analysis = ClassAnalysis(
            total_students=total_students,
            total_misconceptions=len(self.misconception_records),
        )

        bloom_task_data: dict[tuple[str, str], dict] = defaultdict(
            lambda: {
                "students": set(),
                "misconceptions": [],
                "confidences": [],
                "models": defaultdict(set),
            }
        )

        misconception_type_data: dict[str, dict] = defaultdict(
            lambda: {
                "students": set(),
                "occurrences": 0,
                "confidences": [],
                "models": set(),
                "bloom_level": "",
                "task": "",
            }
        )

        model_misconception_counts: dict[str, int] = defaultdict(int)

        for record in self.misconception_records:
            key = (record.bloom_level, record.task)

            bloom_task_data[key]["students"].add(record.student_id)
            bloom_task_data[key]["misconceptions"].append(record.name)
            bloom_task_data[key]["confidences"].append(record.confidence)
            bloom_task_data[key]["models"][record.name].add(record.model_name)

            misconception_type_data[record.name]["students"].add(record.student_id)
            misconception_type_data[record.name]["occurrences"] += 1
            misconception_type_data[record.name]["confidences"].append(record.confidence)
            misconception_type_data[record.name]["models"].add(record.model_name)
            misconception_type_data[record.name]["bloom_level"] = record.bloom_level
            misconception_type_data[record.name]["task"] = record.task

            model_misconception_counts[record.model_name] += 1

        for (bloom_level, task), data in bloom_task_data.items():
            misconception_counts = defaultdict(int)
            for name in data["misconceptions"]:
                misconception_counts[name] += 1

            common = sorted(misconception_counts.items(), key=lambda x: -x[1])[:3]

            total_models = len(set(r.model_name for r in self.misconception_records))
            agreement_rates = []
            for name, models in data["models"].items():
                agreement_rates.append(len(models) / total_models if total_models > 0 else 0)
            avg_agreement = sum(agreement_rates) / len(agreement_rates) if agreement_rates else 0

            stats = BloomTaskStats(
                bloom_level=bloom_level,
                task=task,
                student_count=len(data["students"]),
                total_students=total_students,
                misconception_count=len(data["misconceptions"]),
                avg_confidence=(
                    sum(data["confidences"]) / len(data["confidences"])
                    if data["confidences"]
                    else 0
                ),
                model_agreement_rate=avg_agreement,
                common_misconceptions=common,
            )
            analysis.bloom_task_stats.append(stats)

        analysis.bloom_task_stats.sort(key=lambda x: -x.percentage_affected)

        for name, data in misconception_type_data.items():
            stats = MisconceptionTypeStats(
                name=name,
                bloom_level=data["bloom_level"],
                task=data["task"],
                student_count=len(data["students"]),
                total_students=total_students,
                occurrence_count=data["occurrences"],
                avg_confidence=(
                    sum(data["confidences"]) / len(data["confidences"])
                    if data["confidences"]
                    else 0
                ),
                models_detecting=data["models"],
            )
            analysis.misconception_type_stats.append(stats)

        analysis.misconception_type_stats.sort(key=lambda x: -x.occurrence_count)
        analysis.model_agreement_summary = dict(model_misconception_counts)

        return analysis

    def generate_markdown_report(self, output_path: str = "misconception_report.md") -> str:
        """Generate a markdown report of the analysis.

        Args:
            output_path: Path to save the markdown file.

        Returns:
            The markdown content as a string.
        """
        class_analysis = self.analyze_class()

        lines = [
            "# Misconception Analysis Report",
            "",
            f"**Generated:** {class_analysis.generated_at.strftime('%Y-%m-%d %H:%M:%S')}",
            f"**Total Students Analyzed:** {class_analysis.total_students}",
            f"**Total Misconceptions Detected:** {class_analysis.total_misconceptions}",
            "",
            "---",
            "",
            "## Executive Summary",
            "",
        ]

        if class_analysis.bloom_task_stats:
            lines.append("### Most Difficult Areas (by % of class affected)")
            lines.append("")
            lines.append("| Rank | Bloom Level | Task | Students Affected | Avg Confidence |")
            lines.append("|------|-------------|------|-------------------|----------------|")

            for i, stat in enumerate(class_analysis.bloom_task_stats[:5], 1):
                task_short = stat.task[:40] + "..." if len(stat.task) > 40 else stat.task
                lines.append(
                    f"| {i} | {stat.bloom_level} | {task_short} | "
                    f"{stat.student_count}/{stat.total_students} ({stat.percentage_affected:.0f}%) | "
                    f"{stat.avg_confidence:.2f} |"
                )
            lines.append("")

        if class_analysis.misconception_type_stats:
            lines.append("### Most Common Misconceptions")
            lines.append("")
            lines.append("| Rank | Misconception | Bloom Level | Occurrences | Models Agreeing |")
            lines.append("|------|---------------|-------------|-------------|-----------------|")

            for i, stat in enumerate(class_analysis.misconception_type_stats[:10], 1):
                name_short = stat.name[:35] + "..." if len(stat.name) > 35 else stat.name
                models_str = ", ".join(m.split("/")[-1] for m in stat.models_detecting)
                lines.append(
                    f"| {i} | {name_short} | {stat.bloom_level} | "
                    f"{stat.occurrence_count} | {stat.model_agreement_count} ({models_str}) |"
                )
            lines.append("")

        lines.extend(
            [
                "---",
                "",
                "## Model Agreement Analysis",
                "",
                "| Model | Misconceptions Detected |",
                "|-------|------------------------|",
            ]
        )

        for model, count in sorted(
            class_analysis.model_agreement_summary.items(), key=lambda x: -x[1]
        ):
            model_short = model.split("/")[-1]
            lines.append(f"| {model_short} | {count} |")

        lines.append("")

        lines.extend(
            [
                "---",
                "",
                "## Detailed Analysis by Bloom Level + Task",
                "",
            ]
        )

        for stat in class_analysis.bloom_task_stats:
            lines.append(f"### {stat.bloom_level}: {stat.task}")
            lines.append("")
            lines.append(
                f"- **Students Affected:** {stat.student_count}/{stat.total_students} "
                f"({stat.percentage_affected:.0f}%)"
            )
            lines.append(f"- **Total Misconceptions:** {stat.misconception_count}")
            lines.append(f"- **Average Confidence:** {stat.avg_confidence:.2f}")
            lines.append(f"- **Model Agreement Rate:** {stat.model_agreement_rate:.2%}")
            lines.append("")

            if stat.common_misconceptions:
                lines.append("**Common Misconceptions:**")
                for name, count in stat.common_misconceptions:
                    lines.append(f"- {name} ({count} occurrences)")
                lines.append("")

        lines.extend(
            [
                "---",
                "",
                "## Per-Student Summary",
                "",
                "| Student | Total Misconceptions | Weighted Severity | Top Bloom Level |",
                "|---------|---------------------|-------------------|-----------------|",
            ]
        )

        for eval_doc in self.evaluations:
            student_analysis = self.analyze_student(eval_doc.submission.student_id)
            if student_analysis:
                top_bloom = (
                    max(student_analysis.misconceptions_by_bloom.items(), key=lambda x: x[1])[0]
                    if student_analysis.misconceptions_by_bloom
                    else "N/A"
                )
                lines.append(
                    f"| {student_analysis.student_id} | "
                    f"{student_analysis.total_misconceptions} | "
                    f"{student_analysis.weighted_severity:.2f} | "
                    f"{top_bloom} |"
                )

        lines.extend(
            [
                "",
                "---",
                "",
                "*Report generated by Ensemble Evaluation CLI - Misconception Analyzer*",
            ]
        )

        content = "\n".join(lines)

        with open(output_path, "w") as f:
            f.write(content)

        return content
