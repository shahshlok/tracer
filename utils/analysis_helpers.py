"""
Helper classes for advanced thesis-quality analysis.
"""

from collections import defaultdict
from dataclasses import dataclass, field
from typing import Any

from .matching.classifier import ClassificationResult, MatchResult, StudentQuestionAnalysis


@dataclass
class MisconceptionStats:
    """Statistics for a specific misconception ID."""
    misconception_id: str
    topic: str
    name: str
    true_positives: int = 0
    false_negatives: int = 0
    false_positives: int = 0  # Detections matched to this ID incorrectly
    
    @property
    def total_opportunities(self) -> int:
        return self.true_positives + self.false_negatives
    
    @property
    def recall(self) -> float:
        return self.true_positives / self.total_opportunities if self.total_opportunities > 0 else 0.0
    
    @property
    def precision(self) -> float:
        total_detections = self.true_positives + self.false_positives
        return self.true_positives / total_detections if total_detections > 0 else 0.0


class StrategyComparison:
    """Compares multiple strategies."""
    
    def __init__(self):
        self.strategies: dict[str, dict[str, Any]] = {}
        
    def add_strategy_metrics(self, strategy_name: str, metrics: dict[str, Any]):
        self.strategies[strategy_name] = metrics
        
    def get_best_strategy_per_metric(self) -> dict[str, str]:
        """Returns the strategy name with the best score for each metric."""
        best = {}
        metrics_to_compare = ["f1_score", "precision", "recall", "true_positives"]
        
        for metric in metrics_to_compare:
            best_val = -1
            best_strat = "None"
            for strat, m in self.strategies.items():
                if "error" in m:
                    continue
                val = m.get(metric, 0)
                if val > best_val:
                    best_val = val
                    best_strat = strat
            best[metric] = best_strat
        return best


class ConfidenceAnalysis:
    """Analyzes confidence calibration."""
    
    def __init__(self):
        self.tp_confidences: list[float] = []
        self.fp_confidences: list[float] = []
        
    def add_detection(self, classification: ClassificationResult, confidence: float):
        if classification.result == MatchResult.TRUE_POSITIVE:
            self.tp_confidences.append(confidence)
        elif classification.result == MatchResult.FALSE_POSITIVE:
            self.fp_confidences.append(confidence)
            
    @property
    def avg_tp_confidence(self) -> float:
        return sum(self.tp_confidences) / len(self.tp_confidences) if self.tp_confidences else 0.0
        
    @property
    def avg_fp_confidence(self) -> float:
        return sum(self.fp_confidences) / len(self.fp_confidences) if self.fp_confidences else 0.0
    
    @property
    def calibration_gap(self) -> float:
        """Difference between avg confidence of correct vs incorrect detections."""
        return self.avg_tp_confidence - self.avg_fp_confidence


def compute_misconception_stats(
    analyses: list[StudentQuestionAnalysis],
    groundtruth: list[dict[str, Any]]
) -> dict[str, MisconceptionStats]:
    """Compute detailed stats per misconception ID."""
    stats: dict[str, MisconceptionStats] = {}
    
    # Initialize from groundtruth
    gt_map = {m["id"]: m for m in groundtruth}
    
    for analysis in analyses:
        # Track False Negatives and True Positives (Recall side)
        if not analysis.is_clean and analysis.expected_id:
            mid = analysis.expected_id
            if mid not in stats:
                m_def = gt_map.get(mid, {})
                stats[mid] = MisconceptionStats(
                    misconception_id=mid,
                    topic=m_def.get("category", "Unknown"),
                    name=m_def.get("misconception_name", "Unknown")
                )
            
            if analysis.has_true_positive:
                stats[mid].true_positives += 1
            else:
                stats[mid].false_negatives += 1
                
        # Track False Positives (Precision side)
        for c in analysis.classifications:
            if c.matched_id:
                mid = c.matched_id
                if mid not in stats:
                    m_def = gt_map.get(mid, {})
                    stats[mid] = MisconceptionStats(
                        misconception_id=mid,
                        topic=m_def.get("category", "Unknown"),
                        name=m_def.get("misconception_name", "Unknown")
                    )
                
                if c.result == MatchResult.FALSE_POSITIVE:
                    stats[mid].false_positives += 1
                    
    return stats


def cluster_false_positives(analyses: list[StudentQuestionAnalysis]) -> list[dict[str, Any]]:
    """Group false positives by detected name to find recurring hallucinations."""
    fp_clusters = defaultdict(list)
    
    for analysis in analyses:
        for c in analysis.classifications:
            if c.result == MatchResult.FALSE_POSITIVE:
                # Use detected name as key
                key = c.detected_name.strip()
                fp_clusters[key].append(c)
                
    # Sort by frequency
    sorted_clusters = sorted(fp_clusters.items(), key=lambda x: len(x[1]), reverse=True)
    
    results = []
    for name, occurrences in sorted_clusters:
        if len(occurrences) > 1:  # Only recurring ones
            results.append({
                "name": name,
                "count": len(occurrences),
                "example_student": occurrences[0].student,
                "example_question": occurrences[0].question,
                "models": list(set(o.model for o in occurrences))
            })
            
    return results
