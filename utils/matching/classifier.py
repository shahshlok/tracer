"""Classification logic for TP/FP/FN determination."""

from dataclasses import dataclass, field
from enum import Enum
from typing import Any

from .fuzzy import fuzzy_match_misconception
from .semantic import semantic_match_misconception


class MatchResult(str, Enum):
    """Classification result for a detection."""

    TRUE_POSITIVE = "true_positive"  # Found the injected misconception
    FALSE_POSITIVE = "false_positive"  # Found misconception not injected (or in CLEAN file)
    FALSE_NEGATIVE = "false_negative"  # Missed the injected misconception
    INTERESTING = "interesting"  # FP that might be genuine (in CLEAN file)


@dataclass
class ClassificationResult:
    """Result of classifying a single detection."""

    result: MatchResult
    detected_name: str
    detected_description: str
    expected_id: str | None  # The injected misconception ID (None if CLEAN)
    matched_id: str | None  # What the detection matched to
    match_score: float
    match_method: str  # fuzzy, semantic, or none
    model: str
    student: str
    question: str
    is_clean_file: bool


@dataclass
class StudentQuestionAnalysis:
    """Analysis for one student-question pair."""

    student: str
    question: str
    expected_id: str | None
    is_clean: bool
    classifications: list[ClassificationResult] = field(default_factory=list)

    @property
    def has_true_positive(self) -> bool:
        return any(c.result == MatchResult.TRUE_POSITIVE for c in self.classifications)

    @property
    def false_positive_count(self) -> int:
        return sum(1 for c in self.classifications if c.result == MatchResult.FALSE_POSITIVE)

    @property
    def interesting_count(self) -> int:
        return sum(1 for c in self.classifications if c.result == MatchResult.INTERESTING)


def classify_detection(
    detection: dict[str, Any],
    expected_misconception_id: str | None,
    groundtruth: list[dict[str, Any]],
    is_clean_file: bool,
    model: str,
    student: str,
    question: str,
    fuzzy_threshold: float = 0.8,
    semantic_threshold: float = 0.7,
) -> ClassificationResult:
    """
    Classify a single detection as TP, FP, FN, or Interesting.

    Args:
        detection: The misconception detection from LLM
        expected_misconception_id: The ID that was injected (None if CLEAN)
        groundtruth: Full groundtruth definitions list
        is_clean_file: Whether this was a CLEAN (no injection) file
        model: Which model made this detection
        student: Student ID
        question: Question ID (Q1-Q4)
        fuzzy_threshold: Threshold for fuzzy matching
        semantic_threshold: Threshold for semantic matching

    Returns:
        ClassificationResult with all details
    """
    detected_name = detection.get("name", "")
    detected_description = detection.get("description", "")
    detected_student_belief = detection.get("student_belief", "")

    # Step 1: Try fuzzy matching
    matched_id, fuzzy_score, fuzzy_method = fuzzy_match_misconception(
        detected_name, detected_description, groundtruth, fuzzy_threshold
    )

    match_score = fuzzy_score
    match_method = fuzzy_method

    # Step 2: If fuzzy fails, try semantic matching
    if matched_id is None and semantic_threshold > 0:
        try:
            sem_matched_id, sem_score, sem_method = semantic_match_misconception(
                detected_name,
                detected_description,
                detected_student_belief,
                groundtruth,
                semantic_threshold,
            )
            if sem_matched_id is not None:
                matched_id = sem_matched_id
                match_score = sem_score
                match_method = sem_method
        except Exception:
            pass  # Semantic matching failed, stick with fuzzy result

    # Step 3: Determine classification
    if is_clean_file:
        # Any detection in a CLEAN file is either FP or Interesting
        result = MatchResult.INTERESTING if matched_id else MatchResult.FALSE_POSITIVE
    elif expected_misconception_id is None:
        # Shouldn't happen, but treat as FP
        result = MatchResult.FALSE_POSITIVE
    elif matched_id == expected_misconception_id:
        # Detection matches what was injected
        result = MatchResult.TRUE_POSITIVE
    else:
        # Detection doesn't match what was injected
        result = MatchResult.FALSE_POSITIVE

    return ClassificationResult(
        result=result,
        detected_name=detected_name,
        detected_description=detected_description,
        expected_id=expected_misconception_id,
        matched_id=matched_id,
        match_score=match_score,
        match_method=match_method,
        model=model,
        student=student,
        question=question,
        is_clean_file=is_clean_file,
    )


def analyze_student_question(
    detections_by_model: dict[str, list[dict[str, Any]]],
    expected_misconception_id: str | None,
    is_clean_file: bool,
    groundtruth: list[dict[str, Any]],
    student: str,
    question: str,
    fuzzy_threshold: float = 0.8,
    semantic_threshold: float = 0.7,
) -> StudentQuestionAnalysis:
    """
    Analyze all model detections for a single student-question pair.

    Args:
        detections_by_model: Dict mapping model name to list of detections
        expected_misconception_id: What was injected (None if CLEAN)
        is_clean_file: Whether this was a CLEAN file
        groundtruth: Full groundtruth list
        student: Student ID
        question: Question ID

    Returns:
        StudentQuestionAnalysis with all classifications
    """
    analysis = StudentQuestionAnalysis(
        student=student,
        question=question,
        expected_id=expected_misconception_id,
        is_clean=is_clean_file,
    )

    for model, detections in detections_by_model.items():
        for detection in detections:
            classification = classify_detection(
                detection=detection,
                expected_misconception_id=expected_misconception_id,
                groundtruth=groundtruth,
                is_clean_file=is_clean_file,
                model=model,
                student=student,
                question=question,
                fuzzy_threshold=fuzzy_threshold,
                semantic_threshold=semantic_threshold,
            )
            analysis.classifications.append(classification)

    return analysis


def compute_metrics(analyses: list[StudentQuestionAnalysis]) -> dict[str, Any]:
    """
    Compute aggregate metrics from a list of analyses.

    Returns dict with:
    - true_positives, false_positives, false_negatives counts
    - precision, recall, f1
    - per-model breakdown
    - interesting discoveries count
    """
    tp = 0
    fp = 0
    fn = 0
    interesting = 0

    model_stats: dict[str, dict[str, int]] = {}

    for analysis in analyses:
        # Check for false negative (missed injection)
        if not analysis.is_clean and analysis.expected_id and not analysis.has_true_positive:
            fn += 1

        for c in analysis.classifications:
            # Initialize model stats
            if c.model not in model_stats:
                model_stats[c.model] = {"tp": 0, "fp": 0, "interesting": 0, "total": 0}

            model_stats[c.model]["total"] += 1

            if c.result == MatchResult.TRUE_POSITIVE:
                tp += 1
                model_stats[c.model]["tp"] += 1
            elif c.result == MatchResult.FALSE_POSITIVE:
                fp += 1
                model_stats[c.model]["fp"] += 1
            elif c.result == MatchResult.INTERESTING:
                interesting += 1
                model_stats[c.model]["interesting"] += 1

    # Compute aggregate metrics
    precision = tp / (tp + fp) if (tp + fp) > 0 else 0.0
    recall = tp / (tp + fn) if (tp + fn) > 0 else 0.0
    f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0.0

    return {
        "true_positives": tp,
        "false_positives": fp,
        "false_negatives": fn,
        "interesting_discoveries": interesting,
        "precision": precision,
        "recall": recall,
        "f1_score": f1,
        "per_model": model_stats,
        "total_analyses": len(analyses),
    }
