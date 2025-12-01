"""
Utility for evaluating LLM misconception detection against ground truth.

Compares detected misconceptions from student_evals/ against the ground truth
in authentic_seeded/manifest.json to calculate precision, recall, and F1.
"""

import json
import os
import re
from dataclasses import dataclass, field
from pathlib import Path

from pydantic_models.evaluation import EvaluationDocument


@dataclass
class GroundTruth:
    """Ground truth misconception for a student-question pair."""

    student_id: str
    question_id: str
    misconception_id: str | None
    is_correct: bool


@dataclass
class DetectedMisconception:
    """A misconception detected by an LLM."""

    student_id: str
    question_id: str
    model: str
    topic: str
    name: str
    description: str
    confidence: float
    mapped_id: str | None = None  # Mapped to ground truth ID after analysis


@dataclass
class ComparisonResult:
    """Result of comparing detected vs ground truth for one student-question."""

    student_id: str
    question_id: str
    ground_truth_id: str | None
    ground_truth_correct: bool
    detected_by_models: dict[str, list[DetectedMisconception]]

    # Classification
    true_positive: bool = False  # GT has error, LLM found it
    false_positive: bool = False  # GT is correct, LLM found error
    false_negative: bool = False  # GT has error, LLM missed it
    true_negative: bool = False  # GT is correct, LLM found none

    # Mapping quality
    mapped_correctly: bool = False  # LLM description maps to correct GT ID


@dataclass
class EvaluationMetrics:
    """Aggregate metrics for misconception detection evaluation."""

    total_submissions: int = 0

    # Ground truth counts
    gt_with_errors: int = 0
    gt_correct: int = 0

    # Detection counts
    true_positives: int = 0
    false_positives: int = 0
    false_negatives: int = 0
    true_negatives: int = 0

    # Derived metrics
    precision: float = 0.0
    recall: float = 0.0
    f1_score: float = 0.0
    accuracy: float = 0.0

    # Per-model breakdown
    model_metrics: dict = field(default_factory=dict)

    # Per-misconception-type breakdown
    misconception_detection_rate: dict = field(default_factory=dict)

    def calculate(self):
        """Calculate derived metrics from counts."""
        # Precision = TP / (TP + FP)
        if self.true_positives + self.false_positives > 0:
            self.precision = self.true_positives / (self.true_positives + self.false_positives)

        # Recall = TP / (TP + FN)
        if self.true_positives + self.false_negatives > 0:
            self.recall = self.true_positives / (self.true_positives + self.false_negatives)

        # F1 = 2 * (P * R) / (P + R)
        if self.precision + self.recall > 0:
            self.f1_score = 2 * (self.precision * self.recall) / (self.precision + self.recall)

        # Accuracy = (TP + TN) / Total
        total = (
            self.true_positives + self.false_positives + self.false_negatives + self.true_negatives
        )
        if total > 0:
            self.accuracy = (self.true_positives + self.true_negatives) / total


class MisconceptionEvaluator:
    """Evaluates LLM misconception detection against ground truth."""

    # Keywords for mapping detected misconceptions to ground truth IDs
    MISCONCEPTION_PATTERNS = {
        "DT001": [
            r"int instead of double",
            r"using int.*decimal",
            r"integer.*when.*double",
            r"nextInt.*instead.*nextDouble",
        ],
        "DT002": [
            r"integer division",
            r"truncat",
            r"int.*division",
            r"casting.*int.*division",
        ],
        "DT003": [
            r"type mismatch",
            r"nextInt.*nextDouble",
            r"scanner.*wrong.*type",
        ],
        "VAR001": [
            r"operator precedence",
            r"order of operations",
            r"parenthes",
            r"missing.*parenthes",
        ],
        "VAR002": [
            r"addition instead.*subtraction",
            r"wrong.*operator",
            r"\+ instead.*-",
            r"plus.*minus",
        ],
        "VAR003": [
            r"fuel.*formula",
            r"cost.*calculation.*wrong",
            r"multipl.*instead.*divid",
        ],
        "VAR004": [
            r"semi-?perimeter",
            r"intermediate.*variable",
            r"inline.*incorrect",
        ],
        "CONST001": [
            r"\^.*instead.*pow",
            r"caret.*xor",
            r"exponent.*wrong",
            r"\^.*power",
        ],
        "CONST002": [
            r"missing.*sqrt",
            r"no.*sqrt",
            r"square root.*missing",
            r"forgot.*sqrt",
        ],
        "CONST003": [
            r"pow.*argument.*order",
            r"swap.*base.*exponent",
            r"Math\.pow.*wrong.*order",
        ],
        "INPUT001": [
            r"missing.*import",
            r"no.*import.*Scanner",
            r"import.*java\.util",
        ],
        "INPUT002": [
            r"wrong.*number.*input",
            r"missing.*input",
            r"not.*reading.*all",
            r"fewer.*input",
        ],
        "INPUT003": [
            r"not.*clos.*scanner",
            r"resource.*leak",
            r"scanner\.close",
            r"missing.*close",
        ],
        "OTHER001": [
            r"wrong.*problem",
            r"different.*problem",
            r"misunderstand.*question",
            r"computing.*wrong.*quantity",
        ],
        "OTHER002": [
            r"hardcod",
            r"literal.*instead.*input",
            r"not.*reading.*input",
        ],
    }

    def __init__(
        self,
        manifest_path: str = "authentic_seeded/manifest.json",
        evals_dir: str = "student_evals",
        strategy: str = "minimal",
    ):
        self.manifest_path = manifest_path
        self.evals_dir = Path(evals_dir) / strategy
        self.strategy = strategy

        self.ground_truth: dict[tuple[str, str], GroundTruth] = {}
        self.evaluations: list[EvaluationDocument] = []
        self.comparison_results: list[ComparisonResult] = []

    def load_ground_truth(self) -> int:
        """Load ground truth from manifest.json."""
        with open(self.manifest_path) as f:
            manifest = json.load(f)

        for entry in manifest:
            gt = GroundTruth(
                student_id=entry["student_id"],
                question_id=entry["question_id"],
                misconception_id=entry.get("misconception_id"),
                is_correct=entry.get("is_correct", entry.get("misconception_id") is None),
            )
            # Key by (student_id, question_id)
            self.ground_truth[(gt.student_id, gt.question_id)] = gt

        return len(self.ground_truth)

    def load_evaluations(self) -> int:
        """Load evaluation documents from evals directory."""
        if not self.evals_dir.exists():
            return 0

        for eval_file in self.evals_dir.glob("*_eval.json"):
            with open(eval_file) as f:
                data = json.load(f)
                eval_doc = EvaluationDocument(**data)
                self.evaluations.append(eval_doc)

        return len(self.evaluations)

    def map_misconception_to_id(self, detected: DetectedMisconception) -> str | None:
        """
        Attempt to map a detected misconception description to a ground truth ID.
        Uses keyword/pattern matching.
        """
        # Combine name and description for matching
        text = f"{detected.name} {detected.description}".lower()

        for gt_id, patterns in self.MISCONCEPTION_PATTERNS.items():
            for pattern in patterns:
                if re.search(pattern, text, re.IGNORECASE):
                    return gt_id

        return None

    def extract_detected_misconceptions(
        self, eval_doc: EvaluationDocument
    ) -> dict[str, list[DetectedMisconception]]:
        """Extract detected misconceptions from an evaluation document."""
        detected_by_model: dict[str, list[DetectedMisconception]] = {}

        # Extract student_id without the suffix (e.g., "_Correct", "_Mixed", "_DT003")
        full_student_id = eval_doc.submission.student_id
        # The manifest uses student_id without suffix, need to extract base
        # Format: LastName_FirstName_ID_Suffix -> LastName_FirstName_ID
        parts = full_student_id.rsplit("_", 1)
        if (
            len(parts) == 2
            and parts[1] in ("Correct", "Mixed")
            or parts[1].startswith(("DT", "VAR", "CONST", "INPUT", "OTHER"))
        ):
            base_student_id = parts[0]
        else:
            base_student_id = full_student_id

        # Get question_id from context
        question_id = eval_doc.context.question_id

        for model_name, model_eval in eval_doc.models.items():
            detected_by_model[model_name] = []

            if model_eval.misconceptions:
                for misc in model_eval.misconceptions:
                    detected = DetectedMisconception(
                        student_id=base_student_id,
                        question_id=question_id,
                        model=model_name,
                        topic=misc.topic,
                        name=misc.name,
                        description=misc.description,
                        confidence=misc.confidence,
                    )
                    # Try to map to ground truth ID
                    detected.mapped_id = self.map_misconception_to_id(detected)
                    detected_by_model[model_name].append(detected)

        return detected_by_model

    def compare_single(
        self,
        student_id: str,
        question_id: str,
        detected_by_model: dict[str, list[DetectedMisconception]],
    ) -> ComparisonResult:
        """Compare detected misconceptions against ground truth for one submission."""
        # Look up ground truth
        gt = self.ground_truth.get((student_id, question_id))

        if gt is None:
            # No ground truth available - skip
            return ComparisonResult(
                student_id=student_id,
                question_id=question_id,
                ground_truth_id=None,
                ground_truth_correct=True,  # Assume correct if no GT
                detected_by_models=detected_by_model,
            )

        result = ComparisonResult(
            student_id=student_id,
            question_id=question_id,
            ground_truth_id=gt.misconception_id,
            ground_truth_correct=gt.is_correct,
            detected_by_models=detected_by_model,
        )

        # Check if ANY model detected ANY misconception
        any_detected = any(len(detections) > 0 for detections in detected_by_model.values())

        if gt.is_correct:
            # Ground truth: no error
            if any_detected:
                result.false_positive = True
            else:
                result.true_negative = True
        else:
            # Ground truth: has error
            if any_detected:
                result.true_positive = True
                # Check if mapping is correct
                for detections in detected_by_model.values():
                    for d in detections:
                        if d.mapped_id == gt.misconception_id:
                            result.mapped_correctly = True
                            break
            else:
                result.false_negative = True

        return result

    def run_comparison(self) -> EvaluationMetrics:
        """Run full comparison and calculate metrics."""
        metrics = EvaluationMetrics()

        # Track per-misconception detection
        misconception_detected: dict[str, int] = {}  # ID -> times detected
        misconception_total: dict[str, int] = {}  # ID -> times it appears in GT

        # Track per-model metrics
        model_tp: dict[str, int] = {}
        model_fp: dict[str, int] = {}
        model_fn: dict[str, int] = {}
        model_tn: dict[str, int] = {}

        for eval_doc in self.evaluations:
            # Extract student base ID
            full_student_id = eval_doc.submission.student_id
            parts = full_student_id.rsplit("_", 1)
            if len(parts) == 2 and (
                parts[1] in ("Correct", "Mixed")
                or any(parts[1].startswith(p) for p in ("DT", "VAR", "CONST", "INPUT", "OTHER"))
            ):
                base_student_id = parts[0]
            else:
                base_student_id = full_student_id

            question_id = eval_doc.context.question_id

            # Extract detected misconceptions
            detected_by_model = self.extract_detected_misconceptions(eval_doc)

            # Compare against ground truth
            result = self.compare_single(base_student_id, question_id, detected_by_model)
            self.comparison_results.append(result)

            # Update aggregate metrics
            metrics.total_submissions += 1

            if result.ground_truth_correct:
                metrics.gt_correct += 1
            else:
                metrics.gt_with_errors += 1
                # Track misconception type
                gt_id = result.ground_truth_id
                if gt_id:
                    misconception_total[gt_id] = misconception_total.get(gt_id, 0) + 1

            if result.true_positive:
                metrics.true_positives += 1
                if result.ground_truth_id:
                    misconception_detected[result.ground_truth_id] = (
                        misconception_detected.get(result.ground_truth_id, 0) + 1
                    )
            elif result.false_positive:
                metrics.false_positives += 1
            elif result.false_negative:
                metrics.false_negatives += 1
            elif result.true_negative:
                metrics.true_negatives += 1

            # Per-model tracking
            for model_name, detections in detected_by_model.items():
                if model_name not in model_tp:
                    model_tp[model_name] = 0
                    model_fp[model_name] = 0
                    model_fn[model_name] = 0
                    model_tn[model_name] = 0

                model_detected = len(detections) > 0

                if result.ground_truth_correct:
                    if model_detected:
                        model_fp[model_name] += 1
                    else:
                        model_tn[model_name] += 1
                else:
                    if model_detected:
                        model_tp[model_name] += 1
                    else:
                        model_fn[model_name] += 1

        # Calculate aggregate metrics
        metrics.calculate()

        # Calculate per-misconception detection rates
        for gt_id, total in misconception_total.items():
            detected = misconception_detected.get(gt_id, 0)
            metrics.misconception_detection_rate[gt_id] = {
                "total": total,
                "detected": detected,
                "rate": detected / total if total > 0 else 0.0,
            }

        # Calculate per-model metrics
        for model_name in model_tp.keys():
            tp = model_tp[model_name]
            fp = model_fp[model_name]
            fn = model_fn[model_name]
            tn = model_tn[model_name]

            precision = tp / (tp + fp) if (tp + fp) > 0 else 0.0
            recall = tp / (tp + fn) if (tp + fn) > 0 else 0.0
            f1 = (
                2 * (precision * recall) / (precision + recall) if (precision + recall) > 0 else 0.0
            )

            metrics.model_metrics[model_name] = {
                "tp": tp,
                "fp": fp,
                "fn": fn,
                "tn": tn,
                "precision": round(precision, 3),
                "recall": round(recall, 3),
                "f1": round(f1, 3),
            }

        return metrics

    def generate_report(self) -> str:
        """Generate a markdown report of the evaluation results."""
        metrics = self.run_comparison()

        lines = [
            "# Misconception Detection Evaluation Report",
            "",
            f"**Strategy:** {self.strategy}",
            f"**Total Submissions:** {metrics.total_submissions}",
            "",
            "## Overall Metrics",
            "",
            "| Metric | Value |",
            "|--------|-------|",
            f"| Precision | {metrics.precision:.1%} |",
            f"| Recall | {metrics.recall:.1%} |",
            f"| F1 Score | {metrics.f1_score:.1%} |",
            f"| Accuracy | {metrics.accuracy:.1%} |",
            "",
            "## Confusion Matrix",
            "",
            "| | Predicted Error | Predicted Correct |",
            "|---|---|---|",
            f"| **Actual Error** | TP: {metrics.true_positives} | FN: {metrics.false_negatives} |",
            f"| **Actual Correct** | FP: {metrics.false_positives} | TN: {metrics.true_negatives} |",
            "",
            "## Per-Model Performance",
            "",
            "| Model | TP | FP | FN | TN | Precision | Recall | F1 |",
            "|-------|----|----|----|----|-----------|--------|-----|",
        ]

        for model_name, m in metrics.model_metrics.items():
            short_name = model_name.split("/")[-1]
            lines.append(
                f"| {short_name} | {m['tp']} | {m['fp']} | {m['fn']} | {m['tn']} | "
                f"{m['precision']:.1%} | {m['recall']:.1%} | {m['f1']:.1%} |"
            )

        lines.extend(
            [
                "",
                "## Per-Misconception Detection Rate",
                "",
                "| Misconception ID | Total | Detected | Rate |",
                "|------------------|-------|----------|------|",
            ]
        )

        for gt_id, data in sorted(metrics.misconception_detection_rate.items()):
            lines.append(f"| {gt_id} | {data['total']} | {data['detected']} | {data['rate']:.1%} |")

        # Add detailed results
        lines.extend(
            [
                "",
                "## Detailed Results",
                "",
            ]
        )

        for result in self.comparison_results:
            status = ""
            if result.true_positive:
                status = "TP"
            elif result.false_positive:
                status = "FP"
            elif result.false_negative:
                status = "FN"
            elif result.true_negative:
                status = "TN"

            lines.append(f"### {result.student_id} - {result.question_id} [{status}]")
            lines.append("")
            lines.append(f"**Ground Truth:** {result.ground_truth_id or 'Correct'}")

            for model_name, detections in result.detected_by_models.items():
                short_name = model_name.split("/")[-1]
                if detections:
                    for d in detections:
                        mapped = f" -> {d.mapped_id}" if d.mapped_id else ""
                        lines.append(
                            f"- **{short_name}:** {d.name}{mapped} (conf: {d.confidence:.2f})"
                        )
                else:
                    lines.append(f"- **{short_name}:** No misconceptions detected")

            lines.append("")

        return "\n".join(lines)


def main():
    """Run the misconception evaluation."""
    evaluator = MisconceptionEvaluator(strategy="minimal")

    print("Loading ground truth...")
    gt_count = evaluator.load_ground_truth()
    print(f"  Loaded {gt_count} ground truth entries")

    print("Loading evaluations...")
    eval_count = evaluator.load_evaluations()
    print(f"  Loaded {eval_count} evaluations")

    print("\nGenerating report...")
    report = evaluator.generate_report()

    # Save report
    output_path = "misconception_eval_report.md"
    with open(output_path, "w") as f:
        f.write(report)

    print(f"Report saved to {output_path}")

    # Print summary
    metrics = evaluator.run_comparison()
    print("\n" + "=" * 50)
    print("SUMMARY")
    print("=" * 50)
    print(f"Precision: {metrics.precision:.1%}")
    print(f"Recall:    {metrics.recall:.1%}")
    print(f"F1 Score:  {metrics.f1_score:.1%}")
    print(f"Accuracy:  {metrics.accuracy:.1%}")


if __name__ == "__main__":
    main()
