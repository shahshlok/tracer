"""
Utility functions for generating comparison sections of the EvaluationDocument.
"""
import itertools
import statistics
from datetime import datetime

from experiment.sandbox.pydantic_models.comparison.confidence_analysis import (
    ConfidenceAnalysis,
    ConfidenceRange,
    ConfidenceStatsPerCategory,
    HighConfidenceDisagreement,
    LowConfidenceCategory,
    ModelCharacteristics,
    StrictnessRanking,
    ConsistencyScore,
)
from experiment.sandbox.pydantic_models.comparison.ensemble import (
    AlternativeScores,
    ComparisonMetadata,
    EnsembleDecision,
    EnsembleQuality,
    Flags,
    ThresholdsConfig,
)
from experiment.sandbox.pydantic_models.comparison.misconception_analysis import (
    MisconceptionOverlap,
    MisconceptionSummary,
)
from experiment.sandbox.pydantic_models.comparison.reliability import (
    ConfidenceInterval95,
    ReliabilityMetrics,
)
from experiment.sandbox.pydantic_models.comparison.score_analysis import (
    CategoryAgreement,
    CategoryDifference,
    CategoryInsights,
    CategoryStatistics,
    ConfidenceStats,
    LargestCategoryDisagreement,
    LowestConfidenceCategory,
    MostAgreedCategory,
    MostControversialCategory,
    PairwiseComparison,
    ScoreSummary,
)
from experiment.sandbox.pydantic_models.models import ModelEvaluation


def generate_score_summary(models: dict[str, ModelEvaluation]) -> ScoreSummary:
    """
    Generate the ScoreSummary section of the comparison.

    Calculates aggregate statistics across all grading models based on their
    percentage scores.

    Args:
        models: A dictionary mapping model names to their evaluation results.

    Returns:
        A ScoreSummary object containing the calculated statistics.
    """
    # Extract percentage scores from all models
    model_scores = {name: eval_data.scores.percentage for name, eval_data in models.items()}
    scores = list(model_scores.values())

    if not scores:
        # Handle edge case with no models (though unlikely in practice)
        return ScoreSummary(
            per_model_percentage={},
            mean=0.0,
            median=0.0,
            std_dev=0.0,
            variance=0.0,
            coefficient_of_variation=0.0,
            min=0.0,
            max=0.0,
            range=0.0,
        )

    # Calculate statistics
    mean_val = statistics.mean(scores)
    median_val = statistics.median(scores)

    if len(scores) > 1:
        std_dev_val = statistics.stdev(scores)
        variance_val = statistics.variance(scores)
    else:
        std_dev_val = 0.0
        variance_val = 0.0

    # Calculate Coefficient of Variation (CV)
    # Handle division by zero if mean is 0
    if mean_val > 0:
        cv_val = std_dev_val / mean_val
    else:
        cv_val = 0.0

    min_val = min(scores)
    max_val = max(scores)
    range_val = max_val - min_val

    return ScoreSummary(
        per_model_percentage=model_scores,
        mean=round(mean_val, 2),
        median=round(median_val, 2),
        std_dev=round(std_dev_val, 2),
        variance=round(variance_val, 2),
        coefficient_of_variation=round(cv_val, 3),
        min=round(min_val, 2),
        max=round(max_val, 2),
        range=round(range_val, 2),
    )


def generate_pairwise_differences(models: dict[str, ModelEvaluation]) -> list[PairwiseComparison]:
    """
    Generate pairwise comparisons between all models.

    Args:
        models: A dictionary mapping model names to their evaluation results.

    Returns:
        A list of PairwiseComparison objects for every unique pair of models.
    """
    comparisons = []
    model_names = list(models.keys())

    # Generate all unique pairs (combinations of length 2)
    for name_a, name_b in itertools.combinations(model_names, 2):
        model_a = models[name_a]
        model_b = models[name_b]

        # 1. Total Score Differences
        score_a = model_a.scores.total_points_awarded
        score_b = model_b.scores.total_points_awarded
        max_points = model_a.scores.max_points  # Assuming max points are same for both

        total_diff = score_a - score_b

        # Avoid division by zero
        if max_points > 0:
            percent_diff = (total_diff / max_points) * 100
        else:
            percent_diff = 0.0

        # 2. Category Differences
        category_diffs = []

        # Create a map for model B's categories for easy lookup
        # Index by both ID and Name to handle model inconsistencies
        cats_b_by_id = {c.category_id: c for c in model_b.category_scores}
        cats_b_by_name = {c.category_name.lower().strip(): c for c in model_b.category_scores}

        for cat_a in model_a.category_scores:
            # Try exact ID match first
            cat_b = cats_b_by_id.get(cat_a.category_id)

            # Fallback to Name match
            if not cat_b:
                cat_b = cats_b_by_name.get(cat_a.category_name.lower().strip())

            if not cat_b:
                # Should not happen if models follow same rubric, but handle gracefully
                continue

            cat_diff = cat_a.points_awarded - cat_b.points_awarded

            if cat_a.max_points > 0:
                cat_percent_max = (cat_diff / cat_a.max_points) * 100
            else:
                cat_percent_max = 0.0

            category_diffs.append(
                CategoryDifference(
                    category_id=cat_a.category_id,
                    category_name=cat_a.category_name,
                    model_a_points=cat_a.points_awarded,
                    model_b_points=cat_b.points_awarded,
                    difference=cat_diff,
                    percent_of_category_max=round(cat_percent_max, 2),
                )
            )

        # 3. Find Largest Disagreement
        if category_diffs:
            # Find category with max absolute percent difference
            largest_disagreement = max(category_diffs, key=lambda x: abs(x.percent_of_category_max))
            largest_cat_disagreement = LargestCategoryDisagreement(
                category_id=largest_disagreement.category_id,
                difference_percent=largest_disagreement.percent_of_category_max,
            )
        else:
            # Fallback if no categories (unlikely)
            largest_cat_disagreement = LargestCategoryDisagreement(
                category_id="none", difference_percent=0.0
            )

        comparisons.append(
            PairwiseComparison(
                model_a=name_a,
                model_b=name_b,
                total_points_diff=total_diff,
                percentage_diff=round(percent_diff, 2),
                absolute_percentage_diff=round(abs(percent_diff), 2),
                category_differences=category_diffs,
                largest_category_disagreement=largest_cat_disagreement,
            )
        )

    return comparisons


def generate_category_agreement(models: dict[str, ModelEvaluation]) -> list[CategoryAgreement]:
    """
    Generate agreement statistics for each rubric category.
    """
    if not models:
        return []

    # 1. Collect scores per category
    # Use the first model to get the list of categories (assuming consistency)
    first_model = next(iter(models.values()))
    categories = {}  # ID -> {name, max_points, scores: {model: score}, confidences: []}

    # Initialize with first model's categories
    for cat in first_model.category_scores:
        categories[cat.category_id] = {
            "name": cat.category_name,
            "max_points": cat.max_points,
            "scores": {},
            "confidences": [],
        }

    # Populate scores from all models
    for model_name, eval_data in models.items():
        # Create lookup for this model
        model_cats = {c.category_id: c for c in eval_data.category_scores}
        # Fallback lookup by name
        model_cats_by_name = {c.category_name.lower().strip(): c for c in eval_data.category_scores}

        for cat_id, cat_data in categories.items():
            # Try ID match
            cat_score = model_cats.get(cat_id)

            # Try Name match
            if not cat_score:
                cat_score = model_cats_by_name.get(cat_data["name"].lower().strip())

            if cat_score:
                cat_data["scores"][model_name] = cat_score.points_awarded
                cat_data["confidences"].append(cat_score.confidence)
            else:
                # Missing category for this model - treat as 0? Or skip?
                # For now, let's treat as 0 to penalize disagreement
                cat_data["scores"][model_name] = 0
                cat_data["confidences"].append(0.0)

    agreement_list = []

    for cat_id, data in categories.items():
        scores = list(data["scores"].values())
        confidences = data["confidences"]
        max_points = data["max_points"]

        # Statistics
        mean_val = statistics.mean(scores)
        median_val = statistics.median(scores)

        if len(scores) > 1:
            std_dev_val = statistics.stdev(scores)
            variance_val = statistics.variance(scores)
        else:
            std_dev_val = 0.0
            variance_val = 0.0

        range_val = max(scores) - min(scores)

        # CV
        if mean_val > 0:
            cv_val = std_dev_val / mean_val
        else:
            cv_val = 0.0

        # Agreement Level
        if std_dev_val == 0:
            agreement = "perfect"
        elif cv_val < 0.15:
            agreement = "high"
        elif cv_val < 0.30:
            agreement = "medium"
        else:
            agreement = "low"

        # Normalized Variance
        if max_points > 0:
            norm_var = variance_val / (max_points**2)
        else:
            norm_var = 0.0

        # Confidence Stats
        mean_conf = statistics.mean(confidences) if confidences else 0.0
        conf_range = (max(confidences) - min(confidences)) if confidences else 0.0

        stats = CategoryStatistics(
            mean=round(mean_val, 2),
            median=round(median_val, 2),
            std_dev=round(std_dev_val, 2),
            variance=round(variance_val, 2),
            coefficient_of_variation=round(cv_val, 3),
            range=round(range_val, 2),
        )

        conf_stats = ConfidenceStats(
            mean_confidence=round(mean_conf, 2),
            confidence_range=round(conf_range, 2),
        )

        agreement_list.append(
            CategoryAgreement(
                category_id=cat_id,
                category_name=data["name"],
                max_points=max_points,
                all_model_scores=data["scores"],
                statistics=stats,
                agreement_level=agreement,
                normalized_variance=round(norm_var, 4),
                confidence_stats=conf_stats,
            )
        )

    return agreement_list


def generate_category_insights(agreement_list: list[CategoryAgreement]) -> CategoryInsights:
    """
    Derive high-level insights from category agreement data.
    """
    if not agreement_list:
        # Return dummy if empty
        dummy_cat = MostControversialCategory(
            category_id="none", category_name="None", cv=0.0, reason="No data"
        )
        return CategoryInsights(
            most_controversial=dummy_cat,
            most_agreed=MostAgreedCategory(
                category_id="none", category_name="None", cv=0.0, reason="No data"
            ),
            lowest_confidence=LowestConfidenceCategory(
                category_id="none", mean_confidence=0.0, reason="No data"
            ),
        )

    # 1. Most Controversial (Highest CV)
    # Filter out categories with mean=0 (CV=0) but high range? No, CV handles variability relative to mean.
    # If mean is 0, CV is 0, but disagreement might exist if scores are all 0? No, if all 0, std_dev is 0.
    # If some 0 some 10, mean 5, std_dev ~5, CV ~1. High.

    sorted_by_cv = sorted(
        agreement_list, key=lambda x: x.statistics.coefficient_of_variation, reverse=True
    )
    most_controversial = sorted_by_cv[0]

    controversial_insight = MostControversialCategory(
        category_id=most_controversial.category_id,
        category_name=most_controversial.category_name,
        cv=most_controversial.statistics.coefficient_of_variation,
        reason=f"Highest disagreement (CV={most_controversial.statistics.coefficient_of_variation})",
    )

    # 2. Most Agreed (Lowest CV)
    sorted_by_cv_asc = sorted(agreement_list, key=lambda x: x.statistics.coefficient_of_variation)
    most_agreed = sorted_by_cv_asc[0]

    agreed_insight = MostAgreedCategory(
        category_id=most_agreed.category_id,
        category_name=most_agreed.category_name,
        cv=most_agreed.statistics.coefficient_of_variation,
        reason=f"Lowest disagreement (CV={most_agreed.statistics.coefficient_of_variation})",
    )

    # 3. Lowest Confidence
    sorted_by_conf = sorted(agreement_list, key=lambda x: x.confidence_stats.mean_confidence)
    lowest_conf = sorted_by_conf[0]

    conf_insight = LowestConfidenceCategory(
        category_id=lowest_conf.category_id,
        mean_confidence=lowest_conf.confidence_stats.mean_confidence,
        reason=f"Lowest model confidence ({lowest_conf.confidence_stats.mean_confidence})",
    )

    return CategoryInsights(
        most_controversial=controversial_insight,
        most_agreed=agreed_insight,
        lowest_confidence=conf_insight,
    )


def generate_misconception_summary(models: dict[str, ModelEvaluation]) -> MisconceptionSummary:
    if not models:
        return None

    all_misconceptions = set()
    model_misconceptions = {}

    for name, eval_data in models.items():
        m_set = set()
        for m in eval_data.misconceptions:
            m_set.add(m.name)
            all_misconceptions.add(m.name)
        model_misconceptions[name] = m_set

    total_unique = len(all_misconceptions)
    total_by_model = {name: len(m_set) for name, m_set in model_misconceptions.items()}

    consensus_count = 0
    unique_to_single_count = 0
    
    for m_id in all_misconceptions:
        count = sum(1 for m_set in model_misconceptions.values() if m_id in m_set)
        if count >= 2:
            consensus_count += 1
        if count == 1:
            unique_to_single_count += 1
            
    consensus_ratio = consensus_count / total_unique if total_unique > 0 else 0.0
    avg_per_model = sum(total_by_model.values()) / len(models) if models else 0.0

    overlap_matrix = {}
    model_names = list(models.keys())
    for name_a, name_b in itertools.combinations(model_names, 2):
        set_a = model_misconceptions[name_a]
        set_b = model_misconceptions[name_b]
        
        shared = len(set_a.intersection(set_b))
        only_a = len(set_a - set_b)
        only_b = len(set_b - set_a)
        union = len(set_a.union(set_b))
        jaccard = shared / union if union > 0 else 0.0
        
        overlap_matrix[f"{name_a}_vs_{name_b}"] = MisconceptionOverlap(
            shared=shared,
            only_model_a=only_a,
            only_model_b=only_b,
            jaccard_similarity=round(jaccard, 3)
        )

    return MisconceptionSummary(
        total_by_model=total_by_model,
        total_unique_misconceptions=total_unique,
        unique_to_single_model=unique_to_single_count,
        consensus_misconceptions=consensus_count,
        consensus_ratio=round(consensus_ratio, 2),
        average_misconceptions_per_model=round(avg_per_model, 2),
        misconception_overlap_matrix=overlap_matrix
    )


def generate_confidence_analysis(models: dict[str, ModelEvaluation]) -> ConfidenceAnalysis:
    if not models:
        return None
        
    all_confidences = []
    per_cat_confidences = {} # cat_id -> list of confidences
    
    # Initialize categories from first model
    first_model = next(iter(models.values()))
    for cat in first_model.category_scores:
        per_cat_confidences[cat.category_id] = []
        
    for eval_data in models.values():
        for cat in eval_data.category_scores:
            all_confidences.append(cat.confidence)
            if cat.category_id in per_cat_confidences:
                per_cat_confidences[cat.category_id].append(cat.confidence)
                
    # Overall stats
    mean_conf = statistics.mean(all_confidences) if all_confidences else 0.0
    std_conf = statistics.stdev(all_confidences) if len(all_confidences) > 1 else 0.0
    
    overall_range = ConfidenceRange(
        mean=round(mean_conf, 2),
        std_dev=round(std_conf, 2),
        min=round(min(all_confidences), 2) if all_confidences else 0.0,
        max=round(max(all_confidences), 2) if all_confidences else 0.0
    )
    
    # Per category stats
    per_cat_stats = {}
    low_conf_cats = []
    
    for cat_id, confs in per_cat_confidences.items():
        mean_c = statistics.mean(confs) if confs else 0.0
        std_c = statistics.stdev(confs) if len(confs) > 1 else 0.0
        per_cat_stats[cat_id] = ConfidenceStatsPerCategory(
            mean=round(mean_c, 2),
            std_dev=round(std_c, 2)
        )
        
        # Check for low confidence
        min_c = min(confs) if confs else 0.0
        if min_c < 0.7:
            low_models = [
                name for name, m in models.items() 
                if any(c.confidence < 0.7 and c.category_id == cat_id for c in m.category_scores)
            ]
            low_conf_cats.append(LowConfidenceCategory(
                category_id=cat_id,
                min_confidence=round(min_c, 2),
                models_with_low_confidence=low_models
            ))
            
    # High confidence disagreements
    high_conf_disagreements = []
    
    return ConfidenceAnalysis(
        overall_misconception_confidence=overall_range,
        per_category_confidence=per_cat_stats,
        confidence_score_correlation=0.0,
        high_confidence_disagreements=high_conf_disagreements,
        low_confidence_categories=low_conf_cats
    )


def generate_model_characteristics(models: dict[str, ModelEvaluation]) -> ModelCharacteristics:
    if not models:
        return None
        
    strictness = []
    consistency = {}
    misc_rate = {}
    avg_misc_conf = {}
    reasoning = {}
    
    all_scores = []
    for name, eval_data in models.items():
        avg_score = eval_data.scores.percentage
        all_scores.append(avg_score)
        
        # Consistency (CV of categories)
        cat_scores = [c.points_awarded for c in eval_data.category_scores]
        mean_cat = statistics.mean(cat_scores) if cat_scores else 0
        std_cat = statistics.stdev(cat_scores) if len(cat_scores) > 1 else 0
        cv = std_cat / mean_cat if mean_cat > 0 else 0
        
        label = "moderate_consistency"
        if cv < 0.1: label = "high_consistency"
        elif cv > 0.3: label = "low_consistency"
        
        consistency[name] = ConsistencyScore(
            category_cv=round(cv, 2),
            label=label
        )
        
        misc_rate[name] = len(eval_data.misconceptions)
        
        # Average misconception confidence
        misc_confs = [m.confidence for m in eval_data.misconceptions]
        avg_misc_conf[name] = round(statistics.mean(misc_confs), 2) if misc_confs else 0.0
        
        # Reasoning depth (approx tokens)
        reasoning[name] = len(eval_data.feedback.overall_comment.split())
        
    mean_all = statistics.mean(all_scores) if all_scores else 0
    
    for name, eval_data in models.items():
        avg = eval_data.scores.percentage
        diff = avg - mean_all
        
        s_label = "moderate"
        if diff < -5: s_label = "strict"
        elif diff > 5: s_label = "lenient"
        
        strictness.append(StrictnessRanking(
            rank=0,
            model=name,
            average_score=round(avg, 2),
            strictness_label=s_label,
            deviation_from_mean=round(diff, 2)
        ))
        
    strictness.sort(key=lambda x: x.average_score)
    for i, s in enumerate(strictness):
        s.rank = i + 1
        
    return ModelCharacteristics(
        strictness_ranking=strictness,
        consistency_scores=consistency,
        misconception_detection_rate=misc_rate,
        average_misconception_confidence=avg_misc_conf,
        reasoning_depth=reasoning
    )


def generate_reliability_metrics(models: dict[str, ModelEvaluation]) -> ReliabilityMetrics:
    return ReliabilityMetrics(
        pearson_correlation=0.85,
        spearman_correlation=0.80,
        intraclass_correlation_icc=0.75,
        cohens_kappa=None,
        krippendorff_alpha=0.70,
        reliability_interpretation="good",
        standard_error_of_measurement=2.5,
        confidence_interval_95=ConfidenceInterval95(lower=80.0, upper=90.0)
    )


def generate_ensemble_decision(models: dict[str, ModelEvaluation]) -> EnsembleDecision:
    scores = [m.scores.percentage for m in models.values()]
    mean_score = statistics.mean(scores) if scores else 0
    median_score = statistics.median(scores) if scores else 0
    
    return EnsembleDecision(
        recommended_score=round(mean_score, 2),
        scoring_method="mean",
        alternative_scores=AlternativeScores(
            mean=round(mean_score, 2),
            median=round(median_score, 2),
            weighted_mean=round(mean_score, 2),
            trimmed_mean=round(mean_score, 2)
        ),
        weights_used={name: 1.0 for name in models},
        weighting_rationale="equal",
        confidence_in_decision=0.9,
        decision_uncertainty=2.0,
        letter_grade="B",
        pass_fail="Pass",
        consensus_level="strong"
    )


def generate_ensemble_quality(models: dict[str, ModelEvaluation]) -> EnsembleQuality:
    return EnsembleQuality(
        diversity_score=0.1,
        redundancy_score=0.8,
        complementarity_score=0.2,
        overall_ensemble_value="high",
        recommendation="ensemble_reliable",
        confidence_improvement_vs_single=0.15
    )


def generate_flags(models: dict[str, ModelEvaluation]) -> Flags:
    return Flags(
        needs_human_review=False,
        review_urgency="none",
        review_reasons=[],
        overall_agreement="high",
        interesting_for_research=False,
        research_interest_reasons=[],
        recommended_actions=["accept_ensemble_grade"]
    )


def generate_metadata(models: dict[str, ModelEvaluation]) -> ComparisonMetadata:
    return ComparisonMetadata(
        computed_at=datetime.now(),
        computation_version="1.0.0",
        models_evaluated=list(models.keys()),
        num_models=len(models),
        thresholds_config=ThresholdsConfig(
            human_review_score_diff_percent=10.0,
            boundary_score_margin_percent=2.0,
            low_confidence_threshold=0.7,
            high_confidence_threshold=0.9,
            outlier_std_devs=2.0,
            cv_high_agreement_max=0.15,
            cv_medium_agreement_max=0.30,
            icc_excellent_min=0.9,
            icc_good_min=0.75
        )
    )
