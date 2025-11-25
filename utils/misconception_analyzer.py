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


# Canonical topics aligned with course learning objectives
CANONICAL_TOPICS = [
    "Variables",
    "Data Types",
    "Constants",
    "Reading input from the keyboard",
]

# Mapping from LLM-generated topics to canonical topics
TOPIC_MAPPING: dict[str, str] = {
    # Variables mappings
    "variables": "Variables",
    "variable declaration": "Variables",
    "variable declaration and data types": "Variables",
    "declaring variables": "Variables",
    "formula application": "Variables",
    "incorrect formula application": "Variables",
    "incorrect formula": "Variables",
    "mathematical formulas": "Variables",
    "mathematical formulas and libraries": "Variables",
    "mathematical operations and formula derivation": "Variables",
    "computing acceleration using formula": "Variables",
    "computing cost vs. distance between points": "Variables",
    "distance calculation": "Variables",
    "distance formula / exponentiation": "Variables",
    "acceleration formula / physics": "Variables",
    "heron's formula / triangle area": "Variables",
    "problem decomposition and formula application": "Variables",
    "operator precedence": "Variables",
    "operator precedence and usage": "Variables",
    "incorrect operator precedence": "Variables",
    # Data Types mappings
    "data types": "Data Types",
    "variables, data types": "Data Types",
    "inappropriate use of integer data types": "Data Types",
    "wrong data types for velocity/time": "Data Types",
    "type mismatch": "Data Types",
    # Constants mappings (Math library, etc.)
    "constants": "Constants",
    "variables, constants": "Constants",
    "math library": "Constants",
    "exponentiation in java": "Constants",
    "math.sqrt": "Constants",
    "math.pow": "Constants",
    # Reading input mappings
    "reading input from the keyboard": "Reading input from the keyboard",
    "input/output": "Reading input from the keyboard",
    "scanner": "Reading input from the keyboard",
    "input handling": "Reading input from the keyboard",
    "incorrect input handling": "Reading input from the keyboard",
    "resource management / input handling": "Reading input from the keyboard",
    "resource management": "Reading input from the keyboard",
    # Syntax-related -> map to Variables (closest fit for basic programming)
    "syntax": "Variables",
    "syntax errors": "Variables",
    "java syntax": "Variables",
    "java syntax and compilation rules": "Variables",
    "java syntax/compilation": "Variables",
    "missing semicolon": "Variables",
    # Problem understanding -> map to Variables (indicates formula/logic issues)
    "problem comprehension": "Variables",
    "problem interpretation": "Variables",
    "problem understanding": "Variables",
    "problem understanding and task implementation": "Variables",
    "problem solving / algorithmic thinking": "Variables",
    "task understanding": "Variables",
    "task mismatch: distance computation vs. fuel cost calculation": "Variables",
    "programming fundamentals": "Variables",
    # Output formatting -> map to Variables
    "output formatting": "Variables",
    "displaying output": "Variables",
    # Error handling -> map to Variables
    "error handling and program robustness": "Variables",
    "division by zero handling": "Variables",
}


def normalize_topic(topic: str) -> str:
    """Normalize an LLM-generated topic to a canonical topic.

    Args:
        topic: The raw topic string from the LLM.

    Returns:
        One of the 4 canonical topics.
    """
    # Check direct match first
    if topic in CANONICAL_TOPICS:
        return topic

    # Try lowercase lookup
    topic_lower = topic.lower().strip()
    if topic_lower in TOPIC_MAPPING:
        return TOPIC_MAPPING[topic_lower]

    # Fuzzy matching: check if any mapping key is contained in the topic
    for key, canonical in TOPIC_MAPPING.items():
        if key in topic_lower or topic_lower in key:
            return canonical

    # Default fallback: Variables (most general category)
    return "Variables"


@dataclass
class MisconceptionRecord:
    """A single misconception record with metadata."""

    student_id: str
    question_id: str
    model_name: str
    topic: str
    task: str
    name: str
    description: str
    confidence: float
    evidence_count: int


@dataclass
class QuestionStats:
    """Statistics for a specific question."""

    question_id: str
    question_title: str
    submission_count: int = 0
    students_with_misconceptions: int = 0
    total_misconceptions: int = 0
    topic_breakdown: dict = field(default_factory=dict)
    top_misconception: str = ""
    top_misconception_count: int = 0

    @property
    def misconception_rate(self) -> float:
        if self.submission_count == 0:
            return 0.0
        return (self.students_with_misconceptions / self.submission_count) * 100


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
class ProgressionAnalysis:
    """Analysis of student progression from Q3 to Q4.

    Tracks whether students who struggled with Q3 also struggled with Q4,
    helping identify persistent misconceptions vs learning/improvement.
    """

    # Students who had misconceptions in both Q3 and Q4
    struggled_both: set = field(default_factory=set)
    # Students who struggled in Q3 but improved in Q4 (no misconceptions)
    improved: set = field(default_factory=set)
    # Students who were fine in Q3 but struggled in Q4
    regressed: set = field(default_factory=set)
    # Students who had no misconceptions in either
    consistent_good: set = field(default_factory=set)
    # Students who only have data for one question
    incomplete_data: set = field(default_factory=set)

    # Common misconceptions that persisted from Q3 to Q4 (same student, both questions)
    persistent_misconceptions: dict = field(
        default_factory=dict
    )  # {misconception_name: [student_ids]}

    @property
    def total_with_both(self) -> int:
        """Total students with data for both Q3 and Q4."""
        return (
            len(self.struggled_both)
            + len(self.improved)
            + len(self.regressed)
            + len(self.consistent_good)
        )

    @property
    def persistence_rate(self) -> float:
        """Percentage of Q3 strugglers who also struggled in Q4."""
        q3_strugglers = len(self.struggled_both) + len(self.improved)
        if q3_strugglers == 0:
            return 0.0
        return (len(self.struggled_both) / q3_strugglers) * 100

    @property
    def improvement_rate(self) -> float:
        """Percentage of Q3 strugglers who improved in Q4."""
        q3_strugglers = len(self.struggled_both) + len(self.improved)
        if q3_strugglers == 0:
            return 0.0
        return (len(self.improved) / q3_strugglers) * 100


@dataclass
class ClassAnalysis:
    """Class-wide analysis results."""

    total_students: int = 0
    total_misconceptions: int = 0
    topic_task_stats: list[TopicTaskStats] = field(default_factory=list)
    misconception_type_stats: list[MisconceptionTypeStats] = field(default_factory=list)
    question_stats: list[QuestionStats] = field(default_factory=list)
    progression_analysis: ProgressionAnalysis = field(default_factory=ProgressionAnalysis)
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
            question_id = eval_doc.context.question_id

            for model_name, model_eval in eval_doc.models.items():
                for misconception in model_eval.misconceptions:
                    # Normalize the topic to one of the 4 canonical topics
                    normalized_topic = normalize_topic(misconception.topic)

                    record = MisconceptionRecord(
                        student_id=student_id,
                        question_id=question_id,
                        model_name=model_name,
                        topic=normalized_topic,
                        task=misconception.task,
                        name=misconception.name,
                        description=misconception.description,
                        confidence=misconception.confidence,
                        evidence_count=len(misconception.evidence),
                    )
                    self.misconception_records.append(record)

        return self.misconception_records

    def analyze_student(self, student_id: str) -> StudentAnalysis | None:
        """Analyze misconceptions for a specific student across all questions.

        Uses pre-extracted misconception_records which have normalized topics.

        Args:
            student_id: The student identifier.

        Returns:
            StudentAnalysis object or None if student not found.
        """
        # Find ALL evaluations for this student (one per question)
        student_evals = [e for e in self.evaluations if e.submission.student_id == student_id]

        if not student_evals:
            return None

        # Use first eval for student name
        analysis = StudentAnalysis(
            student_id=student_id,
            student_name=student_evals[0].submission.student_name,
        )

        # Use misconception_records (already normalized) instead of raw eval data
        student_records = [r for r in self.misconception_records if r.student_id == student_id]

        topic_counts: dict[str, int] = defaultdict(int)
        task_counts: dict[str, int] = defaultdict(int)
        misconception_by_name: dict[str, set[str]] = defaultdict(set)
        total_weighted_confidence = 0.0

        for record in student_records:
            topic_counts[record.topic] += 1  # Already normalized
            task_counts[record.task] += 1
            misconception_by_name[record.name].add(record.model_name)
            total_weighted_confidence += record.confidence

        total_count = len(student_records)
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

        # Count unique students, not evaluations (each student has multiple question evaluations)
        unique_student_ids = set(e.submission.student_id for e in self.evaluations)
        total_students = len(unique_student_ids)
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

        # Per-question analysis
        question_data: dict[str, dict] = defaultdict(
            lambda: {
                "title": "",
                "submissions": set(),
                "students_with_misconceptions": set(),
                "misconceptions": [],
                "topics": defaultdict(int),
            }
        )

        # Collect question metadata from evaluations
        for eval_doc in self.evaluations:
            q_id = eval_doc.context.question_id
            question_data[q_id]["title"] = eval_doc.context.question_title
            question_data[q_id]["submissions"].add(eval_doc.submission.student_id)

        # Aggregate misconceptions by question
        for record in self.misconception_records:
            q_id = record.question_id
            question_data[q_id]["students_with_misconceptions"].add(record.student_id)
            question_data[q_id]["misconceptions"].append(record.name)
            question_data[q_id]["topics"][record.topic] += 1

        # Build QuestionStats
        for q_id, data in sorted(question_data.items()):
            misconception_counts = defaultdict(int)
            for name in data["misconceptions"]:
                misconception_counts[name] += 1

            top_misconception = ""
            top_count = 0
            if misconception_counts:
                top_misconception, top_count = max(misconception_counts.items(), key=lambda x: x[1])

            stats = QuestionStats(
                question_id=q_id,
                question_title=data["title"],
                submission_count=len(data["submissions"]),
                students_with_misconceptions=len(data["students_with_misconceptions"]),
                total_misconceptions=len(data["misconceptions"]),
                topic_breakdown=dict(data["topics"]),
                top_misconception=top_misconception,
                top_misconception_count=top_count,
            )
            analysis.question_stats.append(stats)

        # Progression Analysis: Q3 → Q4 correlation
        progression = ProgressionAnalysis()

        # Get students with misconceptions by question
        q3_strugglers = question_data.get("q3", {}).get("students_with_misconceptions", set())
        q4_strugglers = question_data.get("q4", {}).get("students_with_misconceptions", set())
        q3_submissions = question_data.get("q3", {}).get("submissions", set())
        q4_submissions = question_data.get("q4", {}).get("submissions", set())

        # Students with data for both Q3 and Q4
        students_with_both = q3_submissions & q4_submissions

        for student_id in students_with_both:
            in_q3 = student_id in q3_strugglers
            in_q4 = student_id in q4_strugglers

            if in_q3 and in_q4:
                progression.struggled_both.add(student_id)
            elif in_q3 and not in_q4:
                progression.improved.add(student_id)
            elif not in_q3 and in_q4:
                progression.regressed.add(student_id)
            else:
                progression.consistent_good.add(student_id)

        # Students with incomplete data (only Q3 or only Q4)
        progression.incomplete_data = q3_submissions ^ q4_submissions

        # Track persistent misconceptions (same misconception name in both Q3 and Q4 for same student)
        # Build per-student misconception sets by question
        student_q3_misconceptions: dict[str, set] = defaultdict(set)
        student_q4_misconceptions: dict[str, set] = defaultdict(set)

        for record in self.misconception_records:
            if record.question_id == "q3":
                student_q3_misconceptions[record.student_id].add(record.name)
            elif record.question_id == "q4":
                student_q4_misconceptions[record.student_id].add(record.name)

        # Find misconceptions that appear in both Q3 and Q4 for the same student
        persistent_misconceptions: dict[str, list] = defaultdict(list)
        for student_id in progression.struggled_both:
            q3_miscs = student_q3_misconceptions.get(student_id, set())
            q4_miscs = student_q4_misconceptions.get(student_id, set())
            common = q3_miscs & q4_miscs
            for misc_name in common:
                persistent_misconceptions[misc_name].append(student_id)

        progression.persistent_misconceptions = dict(persistent_misconceptions)
        analysis.progression_analysis = progression

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

        # Add Topic Summary (Most Difficult Areas by unique topics)
        topic_summary: dict[str, dict] = defaultdict(
            lambda: {"total": 0, "students": set(), "avg_confidence": []}
        )
        for record in self.misconception_records:
            topic_summary[record.topic]["total"] += 1
            topic_summary[record.topic]["students"].add(record.student_id)
            topic_summary[record.topic]["avg_confidence"].append(record.confidence)

        if topic_summary:
            lines.append("### Most Difficult Areas (by % of class affected)")
            lines.append("")
            lines.append(
                "| Rank | Topic | Total Misconceptions | Students Affected | Avg Confidence |"
            )
            lines.append(
                "|------|-------|---------------------|-------------------|----------------|"
            )

            sorted_topic = sorted(
                topic_summary.items(),
                key=lambda x: (len(x[1]["students"]), x[1]["total"]),
                reverse=True,
            )
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
                models_str = ", ".join(m.split("/")[-1] for m in stat.models_detecting)
                lines.append(
                    f"| {i} | {stat.name} | {stat.topic} | "
                    f"{stat.occurrence_count} | {stat.model_agreement_count} ({models_str}) |"
                )
            lines.append("")

        # Per-Question Analysis
        if class_analysis.question_stats:
            lines.extend(
                [
                    "---",
                    "",
                    "## Per-Question Analysis",
                    "",
                    "| Question | Submissions | Misconception Rate | Top Misconception | Topic Breakdown |",
                    "|----------|-------------|-------------------|-------------------|-----------------|",
                ]
            )

            for stat in class_analysis.question_stats:
                top_misc = stat.top_misconception if stat.top_misconception else "-"

                # Format topic breakdown
                topic_parts = []
                for topic, count in sorted(stat.topic_breakdown.items(), key=lambda x: -x[1]):
                    short_topic = topic.split()[0]  # Just first word
                    topic_parts.append(f"{short_topic}: {count}")
                topic_str = ", ".join(topic_parts) if topic_parts else "-"

                lines.append(
                    f"| {stat.question_id.upper()} | {stat.submission_count} | "
                    f"{stat.students_with_misconceptions}/{stat.submission_count} ({stat.misconception_rate:.0f}%) | "
                    f"{top_misc} | {topic_str} |"
                )

            lines.append("")

        # Progression Analysis: Q3 → Q4
        progression = class_analysis.progression_analysis
        if progression.total_with_both > 0:
            lines.extend(
                [
                    "---",
                    "",
                    "## Progression Analysis: Q3 → Q4",
                    "",
                    "Tracks whether students who struggled with Q3 also struggled with Q4,",
                    "helping identify persistent misconceptions vs learning/improvement.",
                    "",
                    "### Student Progression Summary",
                    "",
                    "| Category | Count | Percentage |",
                    "|----------|-------|------------|",
                ]
            )

            total = progression.total_with_both
            lines.append(
                f"| Struggled in both Q3 & Q4 | {len(progression.struggled_both)} | {len(progression.struggled_both) / total * 100:.0f}% |"
            )
            lines.append(
                f"| Improved (Q3 issues → Q4 clean) | {len(progression.improved)} | {len(progression.improved) / total * 100:.0f}% |"
            )
            lines.append(
                f"| Regressed (Q3 clean → Q4 issues) | {len(progression.regressed)} | {len(progression.regressed) / total * 100:.0f}% |"
            )
            lines.append(
                f"| Consistent (no issues in either) | {len(progression.consistent_good)} | {len(progression.consistent_good) / total * 100:.0f}% |"
            )
            lines.append("")

            # Key metrics
            lines.extend(
                [
                    "### Key Metrics",
                    "",
                    f"- **Misconception Persistence Rate:** {progression.persistence_rate:.0f}% of Q3 strugglers also struggled in Q4",
                    f"- **Improvement Rate:** {progression.improvement_rate:.0f}% of Q3 strugglers had no issues in Q4",
                    f"- **Students with incomplete data:** {len(progression.incomplete_data)} (only Q3 or only Q4)",
                    "",
                ]
            )

            # Persistent misconceptions (same misconception in both Q3 and Q4)
            if progression.persistent_misconceptions:
                lines.extend(
                    [
                        "### Persistent Misconceptions",
                        "",
                        "Misconceptions that appeared in both Q3 and Q4 for the same student:",
                        "",
                        "| Misconception | Students Affected |",
                        "|---------------|-------------------|",
                    ]
                )

                sorted_persistent = sorted(
                    progression.persistent_misconceptions.items(),
                    key=lambda x: len(x[1]),
                    reverse=True,
                )
                for misc_name, student_ids in sorted_persistent[:5]:
                    lines.append(f"| {misc_name} | {len(student_ids)} |")

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
                "- **Topic**: The unique topic where misconceptions were detected",
                "- **Total Misconceptions**: Total count of misconceptions flagged at this Topic",
                "- **Students Affected**: Count and percentage of unique students with misconceptions at this Topic",
                "",
                "  $$\\text{Students Affected \\%} = \\frac{\\text{unique students with misconceptions}}{\\text{total students}} \\times 100\\%$$",
                "",
                "- **Avg Confidence**: Average model confidence for misconceptions at this Topic",
                "",
                "  $$\\text{Avg Confidence} = \\frac{\\sum \\text{confidence scores}}{\\text{count(misconceptions)}}$$",
                "",
                "Sorted by: Number of students affected (descending), then by total misconceptions (descending)",
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
                "### Confidence Scores",
                "",
                "All confidence scores range from 0.0 to 1.0:",
                "",
                "- **0.0 - 0.5**: Low confidence (uncertain/borderline misconception)",
                "- **0.5 - 0.7**: Moderate confidence",
                "- **0.7 - 0.9**: High confidence",
                "- **0.9 - 1.0**: Very high confidence (strong evidence of misconception)",
                "",
                "### Progression Analysis Metrics",
                "",
                "- **Misconception Persistence Rate**: Percentage of students who had misconceptions in Q3 that also had misconceptions in Q4",
                "",
                "  $$\\text{Persistence Rate} = \\frac{\\text{struggled in both Q3 \\& Q4}}{\\text{struggled in Q3}} \\times 100\\%$$",
                "",
                "- **Improvement Rate**: Percentage of Q3 strugglers who had no misconceptions in Q4",
                "",
                "  $$\\text{Improvement Rate} = \\frac{\\text{improved (Q3 issues, Q4 clean)}}{\\text{struggled in Q3}} \\times 100\\%$$",
                "",
                "- **Persistent Misconceptions**: Same misconception name detected in both Q3 and Q4 for the same student",
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
