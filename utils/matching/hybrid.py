"""
Hybrid matcher that fuses fuzzy, semantic, and simple priors to improve
misconception alignment.
"""

from dataclasses import dataclass
from typing import Any

from .fuzzy import fuzzy_match_misconception, token_overlap
from .semantic import (
    precompute_groundtruth_embeddings,
    semantic_match_misconception,
)


@dataclass
class HybridMatchResult:
    matched_id: str | None
    score: float
    detail: str
    fuzzy_score: float
    semantic_score: float
    prior_bonus: float


def _topic_prior(detected_topic: str, gt_topic: str) -> float:
    """Lightweight prior: small boost if the topics overlap."""
    if not detected_topic or not gt_topic:
        return 0.0
    overlap = token_overlap(detected_topic, gt_topic)
    # Cap the prior so it nudges rather than overwhelms semantic/fuzzy.
    return min(0.1, overlap * 0.1)


def hybrid_match_misconception(
    detection: dict[str, Any],
    groundtruth: list[dict[str, Any]],
    fuzzy_threshold: float = 0.55,
    semantic_threshold: float = 0.65,
    blend_weight: float = 0.55,
    gt_embeddings: dict[str, list[float]] | None = None,
) -> HybridMatchResult:
    """
    Fuse fuzzy and semantic scores and add a topic prior to select the best match.

    Args:
        detection: LLM detection dict containing name/description/student_belief/topic.
        groundtruth: List of groundtruth misconception entries.
        fuzzy_threshold: Minimum fuzzy score for consideration.
        semantic_threshold: Minimum semantic score for consideration.
        blend_weight: Weight for fuzzy vs semantic in the linear blend (0-1).
        gt_embeddings: Optional precomputed embeddings for groundtruth.

    Returns:
        HybridMatchResult with the chosen ID and scoring breakdown.
    """
    name = detection.get("name", "")
    description = detection.get("description", "")
    belief = detection.get("student_belief", "")
    detected_topic = detection.get("topic", "")

    # Fuzzy match
    fuzzy_id, fuzzy_score, fuzzy_method = fuzzy_match_misconception(
        name, description, groundtruth, threshold=fuzzy_threshold
    )

    # Semantic match
    sem_id, sem_score, sem_method = semantic_match_misconception(
        name,
        description,
        belief,
        groundtruth,
        threshold=semantic_threshold,
        precomputed_gt_embeddings=gt_embeddings,
    )

    candidates: dict[str, dict[str, float | str]] = {}
    if fuzzy_id:
        candidates[fuzzy_id] = {"fuzzy": fuzzy_score, "semantic": 0.0, "method": fuzzy_method}
    if sem_id:
        cand = candidates.get(sem_id, {"fuzzy": 0.0, "semantic": 0.0, "method": sem_method})
        cand["semantic"] = sem_score
        cand["method"] = cand.get("method") or sem_method
        candidates[sem_id] = cand

    # If neither hit thresholds, we still want to consider the best semantic candidate
    if not candidates:
        # Try semantic again with best-scoring candidate even if below threshold
        fallback_id, fallback_sem_score, _ = semantic_match_misconception(
            name,
            description,
            belief,
            groundtruth,
            threshold=0.0,
            precomputed_gt_embeddings=gt_embeddings,
        )
        if fallback_id:
            candidates[fallback_id] = {
                "fuzzy": 0.0,
                "semantic": fallback_sem_score,
                "method": "semantic_fallback",
            }

    best_id = None
    best_score = 0.0
    best_detail = ""
    best_prior = 0.0
    best_fuzzy = 0.0
    best_sem = 0.0

    # Precompute gt topics for priors
    topic_map = {gt.get("id"): gt.get("category", "") for gt in groundtruth}

    for mid, scores in candidates.items():
        fuzzy_component = float(scores.get("fuzzy", 0.0))
        semantic_component = float(scores.get("semantic", 0.0))
        prior = _topic_prior(detected_topic, topic_map.get(mid, ""))
        blended = blend_weight * fuzzy_component + (1 - blend_weight) * semantic_component + prior

        if blended > best_score:
            best_score = blended
            best_id = mid
            best_prior = prior
            best_fuzzy = fuzzy_component
            best_sem = semantic_component
            best_detail = (
                f"fuzzy={fuzzy_component:.3f}, semantic={semantic_component:.3f}, prior={prior:.3f}"
            )

    return HybridMatchResult(
        matched_id=best_id,
        score=best_score,
        detail=best_detail or "no_match",
        fuzzy_score=best_fuzzy,
        semantic_score=best_sem,
        prior_bonus=best_prior,
    )


def precompute_gt_embeddings_for_hybrid(
    groundtruth: list[dict[str, Any]],
) -> dict[str, list[float]]:
    """Expose embedding precomputation to callers that want performance."""
    return precompute_groundtruth_embeddings(groundtruth)
