"""Misconception analysis module for aggregating and analyzing student misconceptions.

This module processes evaluation JSON files from student_evals/ directory and provides:
- Per-student misconception analysis
- Class-wide misconception aggregation
- Topic difficulty analysis
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
    topic: str
    task: str
    name: str
    description: str
    confidence: float
    evidence_count: int


@dataclass
class TopicTaskStats:
    """Statistics for a Topic + Task combination."""

    topic: str
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
    topic: str
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
    misconceptions_by_topic: dict = field(default_factory=dict)
    misconceptions_by_task: dict = field(default_factory=dict)
    model_agreement: dict = field(default_factory=dict)
    avg_misconception_confidence: float = 0.0


@dataclass
class ClassAnalysis:
    """Class-wide analysis results."""

    total_students: int = 0
    total_misconceptions: int = 0
    topic_task_stats: list[TopicTaskStats] = field(default_factory=list)
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
                        topic=misconception.topic,
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

        topic_counts: dict[str, int] = defaultdict(int)
        task_counts: dict[str, int] = defaultdict(int)
        misconception_by_name: dict[str, set[str]] = defaultdict(set)
        total_weighted_confidence = 0.0
        total_count = 0

        for model_name, model_eval in eval_doc.models.items():
            for misconception in model_eval.misconceptions:
                topic_counts[misconception.topic] += 1
                task_counts[misconception.task] += 1
                misconception_by_name[misconception.name].add(model_name)
                total_weighted_confidence += misconception.confidence
                total_count += 1

        analysis.total_misconceptions = total_count
        analysis.misconceptions_by_topic = dict(topic_counts)
        analysis.misconceptions_by_task = dict(task_counts)
        analysis.model_agreement = {
            name: len(models) for name, models in misconception_by_name.items()
        }

        if total_count > 0:
            analysis.avg_misconception_confidence = total_weighted_confidence / total_count

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

        topic_task_data: dict[tuple[str, str], dict] = defaultdict(
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
                "topic": "",
                "task": "",
            }
        )

        model_misconception_counts: dict[str, int] = defaultdict(int)

        for record in self.misconception_records:
            key = (record.topic, record.task)

            topic_task_data[key]["students"].add(record.student_id)
            topic_task_data[key]["misconceptions"].append(record.name)
            topic_task_data[key]["confidences"].append(record.confidence)
            topic_task_data[key]["models"][record.name].add(record.model_name)

            misconception_type_data[record.name]["students"].add(record.student_id)
            misconception_type_data[record.name]["occurrences"] += 1
            misconception_type_data[record.name]["confidences"].append(record.confidence)
            misconception_type_data[record.name]["models"].add(record.model_name)
            misconception_type_data[record.name]["topic"] = record.topic
            misconception_type_data[record.name]["task"] = record.task

            model_misconception_counts[record.model_name] += 1

        for (topic, task), data in topic_task_data.items():
            misconception_counts = defaultdict(int)
            for name in data["misconceptions"]:
                misconception_counts[name] += 1

            common = sorted(misconception_counts.items(), key=lambda x: -x[1])[:3]

            total_models = len(set(r.model_name for r in self.misconception_records))
            agreement_rates = []
            for name, models in data["models"].items():
                agreement_rates.append(len(models) / total_models if total_models > 0 else 0)
            avg_agreement = sum(agreement_rates) / len(agreement_rates) if agreement_rates else 0

            stats = TopicTaskStats(
                topic=topic,
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
            analysis.topic_task_stats.append(stats)

        analysis.topic_task_stats.sort(key=lambda x: -x.percentage_affected)

        for name, data in misconception_type_data.items():
            stats = MisconceptionTypeStats(
                name=name,
                topic=data["topic"],
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

        if class_analysis.topic_task_stats:
            lines.append("### Most Difficult Areas (by % of class affected)")
            lines.append("")
            lines.append("| Rank | Topic | Task | Students Affected | Avg Confidence |")
            lines.append("|------|-------|------|-------------------|----------------|")

            for i, stat in enumerate(class_analysis.topic_task_stats[:5], 1):
                lines.append(
                    f"| {i} | {stat.topic} | {stat.task} | "
                    f"{stat.student_count}/{stat.total_students} ({stat.percentage_affected:.0f}%) | "
                    f"{stat.avg_confidence:.2f} |"
                )
            lines.append("")

        # Add Topic Summary
        topic_summary: dict[str, dict] = defaultdict(
            lambda: {"total": 0, "students": set(), "avg_confidence": []}
        )
        for record in self.misconception_records:
            topic_summary[record.topic]["total"] += 1
            topic_summary[record.topic]["students"].add(record.student_id)
            topic_summary[record.topic]["avg_confidence"].append(record.confidence)

        if topic_summary:
            lines.append("### Top Topics by Misconceptions")
            lines.append("")
            lines.append(
                "| Rank | Topic | Total Misconceptions | Students Affected | Avg Confidence |"
            )
            lines.append(
                "|------|-------|---------------------|-------------------|----------------|"
            )

            sorted_topic = sorted(topic_summary.items(), key=lambda x: -x[1]["total"])
            for i, (topic, data) in enumerate(sorted_topic, 1):
                student_count = len(data["students"])
                percentage = (
                    (student_count / class_analysis.total_students * 100)
                    if class_analysis.total_students > 0
                    else 0
                )
                avg_conf = (
                    sum(data["avg_confidence"]) / len(data["avg_confidence"])
                    if data["avg_confidence"]
                    else 0
                )
                lines.append(
                    f"| {i} | {topic} | {data['total']} | "
                    f"{student_count}/{class_analysis.total_students} ({percentage:.0f}%) | "
                    f"{avg_conf:.2f} |"
                )
            lines.append("")

        if class_analysis.misconception_type_stats:
            lines.append("### Most Common Misconceptions")
            lines.append("")
            lines.append("| Rank | Misconception | Topic | Occurrences | Models Agreeing |")
            lines.append("|------|---------------|-------|-------------|-----------------|")

            for i, stat in enumerate(class_analysis.misconception_type_stats[:10], 1):
                name_short = stat.name[:35] + "..." if len(stat.name) > 35 else stat.name
                models_str = ", ".join(m.split("/")[-1] for m in stat.models_detecting)
                lines.append(
                    f"| {i} | {name_short} | {stat.topic} | "
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
                "## Detailed Analysis by Topic + Task",
                "",
            ]
        )

        for stat in class_analysis.topic_task_stats:
            lines.append(f"### {stat.topic}: {stat.task}")
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
                "| Student | Total Misconceptions | Avg Model Confidence | Top Topic |",
                "|---------|---------------------|---------------------|-----------|",
            ]
        )

        for eval_doc in self.evaluations:
            student_analysis = self.analyze_student(eval_doc.submission.student_id)
            if student_analysis:
                top_topic = (
                    max(student_analysis.misconceptions_by_topic.items(), key=lambda x: x[1])[0]
                    if student_analysis.misconceptions_by_topic
                    else "N/A"
                )
                lines.append(
                    f"| {student_analysis.student_id} | "
                    f"{student_analysis.total_misconceptions} | "
                    f"{student_analysis.avg_misconception_confidence:.2f} | "
                    f"{top_topic} |"
                )

        lines.extend(
            [
                "",
                "---",
                "",
                "## Legend: Formulas and Metrics",
                "",
                "This section explains how each metric in the report is calculated:",
                "",
                "### Executive Summary Tables",
                "",
                "**Most Difficult Areas (by % of class affected)**",
                "",
                "- **Students Affected**: Count and percentage of students who had misconceptions for this Topic + Task combination",
                "",
                "  $$\\text{Students Affected \\%} = \\frac{\\text{students with misconceptions}}{\\text{total students}} \\times 100\\%$$",
                "",
                "- **Avg Confidence**: Average confidence score across all misconceptions in this category",
                "",
                "  $$\\text{Avg Confidence} = \\frac{\\sum \\text{confidence scores}}{\\text{count(misconceptions)}}$$",
                "",
                "**Top Topics by Misconceptions**",
                "",
                "- **Total Misconceptions**: Total count of misconceptions flagged at this Topic",
                "- **Students Affected**: Count and percentage of unique students with misconceptions at this Topic",
                "",
                "  $$\\text{Students Affected \\%} = \\frac{\\text{unique students with misconceptions}}{\\text{total students}} \\times 100\\%$$",
                "",
                "- **Avg Confidence**: Average model confidence for misconceptions at this Topic",
                "",
                "  $$\\text{Avg Confidence} = \\frac{\\sum \\text{confidence scores}}{\\text{count(misconceptions)}}$$",
                "",
                "**Most Common Misconceptions**",
                "",
                "- **Occurrences**: Number of times this specific misconception was detected across all students",
                "- **Models Agreeing**: Number of different models that detected this misconception",
                "  - Shows the specific model names that flagged it",
                "",
                "### Model Agreement Analysis",
                "",
                "- **Misconceptions Detected**: Total number of misconceptions each model identified across all students",
                "",
                "### Detailed Analysis by Topic + Task",
                "",
                "- **Students Affected**: Students who had misconceptions in this category",
                "",
                "  $$\\text{Students Affected \\%} = \\frac{\\text{students with misconceptions}}{\\text{total students}} \\times 100\\%$$",
                "",
                "- **Total Misconceptions**: Total count of misconceptions in this category",
                "- **Average Confidence**: Mean confidence score for misconceptions in this category",
                "",
                "  $$\\text{Avg Confidence} = \\frac{\\sum \\text{confidence scores}}{\\text{count(misconceptions)}}$$",
                "",
                "- **Model Agreement Rate**: Proportion of models that agreed on misconceptions in this category",
                "",
                "  $$\\text{Model Agreement Rate} = \\text{average}\\left(\\frac{\\text{models detecting each misconception}}{\\text{total models}}\\right)$$",
                "",
                "### Per-Student Summary",
                "",
                "- **Total Misconceptions**: Count of all misconceptions flagged for this student across all models",
                "- **Avg Model Confidence**: Average confidence across all misconceptions for this student",
                "",
                "  $$\\text{Avg Model Confidence} = \\frac{\\sum_{i=1}^{n} \\text{confidence}_i}{n}$$",
                "",
                "  where $n$ = count of misconceptions for the student",
                "",
                "  - Higher values indicate models are more confident about the misconceptions they detected",
                "- **Top Topic**: The Topic with the most misconceptions for this student",
                "",
                "### Confidence Scores",
                "",
                "All confidence scores range from 0.0 to 1.0:",
                "",
                "- **0.0 - 0.5**: Low confidence (uncertain/borderline misconception)",
                "- **0.5 - 0.7**: Moderate confidence",
                "- **0.7 - 0.9**: High confidence",
                "- **0.9 - 1.0**: Very high confidence (strong evidence of misconception)",
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
