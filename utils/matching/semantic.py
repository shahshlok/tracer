"""Semantic embedding-based matching for misconceptions."""

import os
from typing import Any

import numpy as np
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()

# Cache for embeddings to avoid redundant API calls
_embedding_cache: dict[str, list[float]] = {}
_client: OpenAI | None = None


def get_client() -> OpenAI:
    """Get or create OpenAI client."""
    global _client
    if _client is None:
        _client = OpenAI(api_key=os.environ.get("OPENAI_API_KEY"))
    return _client


def get_embedding(text: str, model: str = "text-embedding-3-large") -> list[float]:
    """Get embedding for text, with caching."""
    cache_key = f"{model}:{text[:100]}"
    if cache_key in _embedding_cache:
        return _embedding_cache[cache_key]

    client = get_client()
    response = client.embeddings.create(input=text, model=model)
    embedding = response.data[0].embedding

    _embedding_cache[cache_key] = embedding
    return embedding


def cosine_similarity(vec1: list[float], vec2: list[float]) -> float:
    """Calculate cosine similarity between two vectors."""
    a = np.array(vec1)
    b = np.array(vec2)
    return float(np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b)))


def build_detection_text(detection: dict[str, Any]) -> str:
    """Build searchable text from a detection."""
    parts = []
    if detection.get("name"):
        parts.append(f"Misconception: {detection['name']}")
    if detection.get("description"):
        parts.append(f"Description: {detection['description']}")
    if detection.get("student_belief"):
        parts.append(f"Student believes: {detection['student_belief']}")
    return " ".join(parts)


def build_groundtruth_text(gt: dict[str, Any]) -> str:
    """Build searchable text from a groundtruth entry."""
    parts = []
    if gt.get("name"):  # Fixed: was misconception_name
        parts.append(f"Misconception: {gt['name']}")
    if gt.get("explanation"):  # Fixed: was misconception_explanation
        parts.append(f"Explanation: {gt['explanation']}")
    if gt.get("student_thinking"):
        parts.append(f"Student thinking: {gt['student_thinking']}")
    if gt.get("category"):  # Added: include category for better matching
        parts.append(f"Category: {gt['category']}")
    return " ".join(parts)


def semantic_match_misconception(
    detected_name: str,
    detected_description: str,
    detected_student_belief: str,
    groundtruth: list[dict[str, Any]],
    threshold: float = 0.7,
    precomputed_gt_embeddings: dict[str, list[float]] | None = None,
) -> tuple[str | None, float, str]:
    """
    Match a detected misconception using semantic embeddings.

    Args:
        detected_name: The name from LLM detection
        detected_description: The description from LLM detection
        detected_student_belief: The student_belief field from detection
        groundtruth: List of groundtruth misconception definitions
        threshold: Minimum similarity to consider a match
        precomputed_gt_embeddings: Optional dict of {gt_id: embedding} for faster matching

    Returns:
        Tuple of (matched_id, similarity_score, "semantic")
    """
    detection_text = build_detection_text(
        {
            "name": detected_name,
            "description": detected_description,
            "student_belief": detected_student_belief,
        }
    )

    if not detection_text.strip():
        return None, 0.0, "no_text"

    try:
        detection_embedding = get_embedding(detection_text)
    except Exception:
        return None, 0.0, "embedding_error"

    best_match_id = None
    best_score = 0.0

    # Use precomputed embeddings if available (much faster!)
    if precomputed_gt_embeddings:
        for gt_id, gt_embedding in precomputed_gt_embeddings.items():
            similarity = cosine_similarity(detection_embedding, gt_embedding)
            if similarity > best_score:
                best_score = similarity
                best_match_id = gt_id
    else:
        # Fallback: compute embeddings on the fly (slow!)
        for gt in groundtruth:
            gt_text = build_groundtruth_text(gt)
            if not gt_text.strip():
                continue

            try:
                gt_embedding = get_embedding(gt_text)
                similarity = cosine_similarity(detection_embedding, gt_embedding)

                if similarity > best_score:
                    best_score = similarity
                    best_match_id = gt.get("id")
            except Exception:
                continue

    if best_score >= threshold:
        return best_match_id, best_score, "semantic"

    return None, best_score, "below_threshold"


def precompute_groundtruth_embeddings(groundtruth: list[dict[str, Any]]) -> dict[str, list[float]]:
    """
    Precompute embeddings for all groundtruth entries.
    Call this once before batch matching to speed up comparisons.
    """
    embeddings = {}
    for gt in groundtruth:
        gt_id = gt.get("id", "")
        gt_text = build_groundtruth_text(gt)
        if gt_text.strip():
            try:
                embeddings[gt_id] = get_embedding(gt_text)
            except Exception:
                pass
    return embeddings


def batch_semantic_match(
    detections: list[dict[str, Any]],
    groundtruth: list[dict[str, Any]],
    threshold: float = 0.7,
    precomputed_gt_embeddings: dict[str, list[float]] | None = None,
) -> list[dict[str, Any]]:
    """
    Match a batch of detections using semantic similarity.

    Args:
        detections: List of detection dicts with name, description, student_belief
        groundtruth: List of groundtruth definitions
        threshold: Minimum similarity score
        precomputed_gt_embeddings: Optional precomputed embeddings for groundtruth

    Returns:
        List of match results
    """
    # Precompute GT embeddings if not provided
    if precomputed_gt_embeddings is None:
        precomputed_gt_embeddings = precompute_groundtruth_embeddings(groundtruth)

    results = []
    for detection in detections:
        detection_text = build_detection_text(detection)

        if not detection_text.strip():
            results.append(
                {
                    "detection": detection,
                    "matched_id": None,
                    "match_score": 0.0,
                    "match_method": "no_text",
                }
            )
            continue

        try:
            detection_embedding = get_embedding(detection_text)
        except Exception:
            results.append(
                {
                    "detection": detection,
                    "matched_id": None,
                    "match_score": 0.0,
                    "match_method": "embedding_error",
                }
            )
            continue

        best_match_id = None
        best_score = 0.0

        for gt_id, gt_embedding in precomputed_gt_embeddings.items():
            similarity = cosine_similarity(detection_embedding, gt_embedding)
            if similarity > best_score:
                best_score = similarity
                best_match_id = gt_id

        results.append(
            {
                "detection": detection,
                "matched_id": best_match_id if best_score >= threshold else None,
                "match_score": best_score,
                "match_method": "semantic" if best_score >= threshold else "below_threshold",
            }
        )

    return results
