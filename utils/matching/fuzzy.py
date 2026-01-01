"""Fuzzy name-based matching for misconceptions."""

from difflib import SequenceMatcher
from typing import Any


def normalize_text(text: str) -> str:
    """Normalize text for comparison."""
    return text.lower().strip().replace("-", " ").replace("_", " ")


def token_overlap(text1: str, text2: str) -> float:
    """Calculate token overlap ratio between two texts."""
    tokens1 = set(normalize_text(text1).split())
    tokens2 = set(normalize_text(text2).split())
    if not tokens1 or not tokens2:
        return 0.0
    intersection = tokens1 & tokens2
    union = tokens1 | tokens2
    return len(intersection) / len(union)


def sequence_similarity(text1: str, text2: str) -> float:
    """Calculate sequence similarity using SequenceMatcher."""
    return SequenceMatcher(None, normalize_text(text1), normalize_text(text2)).ratio()


def fuzzy_match_misconception(
    detected_name: str,
    detected_description: str,
    groundtruth: list[dict[str, Any]],
    threshold: float = 0.3,  # Lowered for exploratory mode
) -> tuple[str | None, float, str]:
    """
    Attempt to match a detected misconception to ground truth using fuzzy matching.

    Args:
        detected_name: The inferred_category_name from LLM detection
        detected_description: The conceptual_gap from LLM detection
        groundtruth: List of groundtruth misconception definitions
        threshold: Minimum score to consider a match (lowered for exploration)

    Returns:
        Tuple of (matched_id, confidence_score, match_method)
        Returns (None, 0.0, "no_match") if no match found above threshold
    """
    best_match_id = None
    best_score = 0.0
    best_method = "no_match"

    for gt in groundtruth:
        gt_id = gt.get("id", "")
        gt_name = gt.get("name", "")  # Fixed: was misconception_name
        gt_explanation = gt.get("explanation", "")  # Fixed: was misconception_explanation
        gt_student_thinking = gt.get("student_thinking", "")
        gt_category = gt.get("category", "")

        # Method 1: Direct name matching
        name_seq_score = sequence_similarity(detected_name, gt_name)
        name_token_score = token_overlap(detected_name, gt_name)
        name_score = max(name_seq_score, name_token_score)

        if name_score > best_score:
            best_score = name_score
            best_match_id = gt_id
            best_method = "name_match"

        # Method 2: Category matching (e.g., "Operator Precedence" vs "Algebraic Syntax Machine")
        if gt_category:
            cat_score = token_overlap(detected_name, gt_category)
            if cat_score > best_score:
                best_score = cat_score
                best_match_id = gt_id
                best_method = "category_match"

        # Method 3: Description vs explanation matching
        if detected_description and gt_explanation:
            desc_score = token_overlap(detected_description, gt_explanation)
            if desc_score > best_score:
                best_score = desc_score
                best_match_id = gt_id
                best_method = "description_match"

        # Method 4: Check against student_thinking
        if detected_description and gt_student_thinking:
            thinking_score = token_overlap(detected_description, gt_student_thinking)
            if thinking_score > best_score:
                best_score = thinking_score
                best_match_id = gt_id
                best_method = "thinking_match"

    if best_score >= threshold:
        return best_match_id, best_score, best_method

    return None, best_score, "no_match"


def batch_fuzzy_match(
    detections: list[dict[str, Any]],
    groundtruth: list[dict[str, Any]],
    threshold: float = 0.5,
) -> list[dict[str, Any]]:
    """
    Match a batch of detections against ground truth.

    Returns list of match results with detection info and match data.
    """
    results = []
    for detection in detections:
        name = detection.get("name", "")
        description = detection.get("description", "")

        match_id, score, method = fuzzy_match_misconception(
            name, description, groundtruth, threshold
        )

        results.append(
            {
                "detection": detection,
                "matched_id": match_id,
                "match_score": score,
                "match_method": method,
            }
        )

    return results
