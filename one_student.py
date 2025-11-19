import json
import os
from datetime import datetime
from pathlib import Path
from typing import Any, Dict

from dotenv import load_dotenv

from pydantic_models import (
    Comparison,
    Context,
    EvaluationDocument,
    ModelEvaluation,
    Rubric,
    Submission,
    StudentFile,
    ScoreSummary,
    CategoryInsights,
    MisconceptionSummary,
    ConfidenceAnalysis,
    ModelCharacteristics,
    ReliabilityMetrics,
    EnsembleDecision,
    EnsembleQuality,
    Flags,
    ComparisonMetadata,
    MostControversialCategory,
    MostAgreedCategory,
    LowestConfidenceCategory,
    AlternativeScores,
    ThresholdsConfig,
)
from pydantic_models import __version__ as MODELS_VERSION
from utils.openai_client import evaluate_with_openai

load_dotenv()


def build_evaluation_document(
    student_id: str,
    student_name: str,
    student_code: str,
    question_content: str,
    rubric_content: Dict[str, Any],
    model_evaluation: ModelEvaluation,
) -> EvaluationDocument:
    """
    Construct a full EvaluationDocument from the grading results.
    """
    
    # 1. Context
    context = Context(
        course_id="cosc111_intro_programming",
        course_name="COSC111: Introduction to Programming",
        assignment_id=5,
        assignment_title="Cuboid Class and OOP Basics",
        question_source_path="question_cuboid.md",
        question_id="q1_cuboid_class",
        question_title="Implementing a Cuboid class in Java",
        rubric_source_path="rubric_cuboid.json",
    )

    # 2. Submission
    submission = Submission(
        student_id=student_id,
        student_name=student_name,
        submitted_at=datetime.now(), # Placeholder
        programming_language="java",
        files=[
            StudentFile(
                path="Cuboid.java",
                language="java",
                content=student_code
            )
        ]
    )

    # 3. Rubric
    # Need to adapt the raw rubric JSON to the Rubric model
    # Assuming rubric_content matches the structure expected by Rubric model or close to it
    # For now, let's try to parse it directly if it matches, or construct it.
    # The example.jsonc shows a slightly different structure than rubric_cuboid.json
    # rubric_cuboid.json has "totalPoints", "categories" (list of dicts)
    # pydantic_models.Rubric expects... let's check the model definition if needed, 
    # but for now I'll assume I can instantiate it from the dict.
    # Actually, looking at example.jsonc, Rubric model has rubric_id, title, total_points, categories.
    # rubric_cuboid.json has totalPoints, categories.
    
    rubric_categories = []
    for cat in rubric_content.get("categories", []):
        # Map rubric_cuboid.json category to RubricCategory model
        # We need category_id. Let's generate one from the name.
        cat_id = cat["name"].lower().replace(" & ", "_").replace(" ", "_")
        rubric_categories.append({
            "category_id": cat_id,
            "name": cat["name"],
            "max_points": cat["points"],
            "description": cat["description"]
        })

    rubric = Rubric(
        rubric_id="rubric_cuboid_v1",
        title="Cuboid OOP Rubric",
        total_points=rubric_content.get("totalPoints", 100),
        categories=rubric_categories
    )

    # 4. Models
    models = {
        model_evaluation.model_name: model_evaluation
    }

    # 5. Comparison (Placeholder for single model)
    # We need to populate all required fields with dummy/default data since we only have one model
    comparison = Comparison(
        score_summary=ScoreSummary(
            per_model_percentage={model_evaluation.model_name: model_evaluation.scores.percentage},
            mean=model_evaluation.scores.percentage,
            median=model_evaluation.scores.percentage,
            std_dev=0.0,
            variance=0.0,
            coefficient_of_variation=0.0,
            min=model_evaluation.scores.percentage,
            max=model_evaluation.scores.percentage,
            range=0.0
        ),
        pairwise_differences=[],
        category_agreement=[], # Empty for single model
        category_insights=CategoryInsights(
            most_controversial=MostControversialCategory(
                category_id="none",
                category_name="None",
                cv=0.0,
                reason="Single model run"
            ),
            most_agreed=MostAgreedCategory(
                category_id="none",
                category_name="None",
                cv=0.0,
                reason="Single model run"
            ),
            lowest_confidence=LowestConfidenceCategory(
                category_id="none",
                mean_confidence=1.0,
                reason="Single model run"
            )
        ),
        misconception_summary=MisconceptionSummary(
            total_by_model={model_evaluation.model_name: len(model_evaluation.misconceptions)},
            total_unique_misconceptions=len(model_evaluation.misconceptions),
            unique_to_single_model=len(model_evaluation.misconceptions),
            consensus_misconceptions=0,
            consensus_ratio=0.0,
            average_misconceptions_per_model=float(len(model_evaluation.misconceptions)),
            misconception_overlap_matrix={}
        ),
        confidence_analysis=ConfidenceAnalysis(
            overall_misconception_confidence={"mean": 0.0, "std_dev": 0.0, "min": 0.0, "max": 0.0}, # Placeholder
            per_category_confidence={},
            confidence_score_correlation=0.0,
            high_confidence_disagreements=[],
            low_confidence_categories=[]
        ),
        model_characteristics=ModelCharacteristics(
            strictness_ranking=[],
            consistency_scores={},
            misconception_detection_rate={},
            average_misconception_confidence={},
            reasoning_depth={}
        ),
        reliability_metrics=ReliabilityMetrics(
            pearson_correlation=1.0,  # Perfect correlation for single model
            spearman_correlation=1.0,  # Perfect correlation for single model
            intraclass_correlation_icc=1.0,  # Perfect correlation for single model
            cohens_kappa=None,  # Not applicable for single model
            krippendorff_alpha=1.0,  # Perfect agreement for single model
            reliability_interpretation="excellent",  # Single model is perfectly reliable with itself
            standard_error_of_measurement=0.0,
            confidence_interval_95={"lower": model_evaluation.scores.percentage, "upper": model_evaluation.scores.percentage}
        ),
        ensemble_decision=EnsembleDecision(
            recommended_score=model_evaluation.scores.percentage,
            scoring_method="mean",  # Valid literal
            alternative_scores=AlternativeScores(
                mean=model_evaluation.scores.percentage,
                median=model_evaluation.scores.percentage,
                weighted_mean=model_evaluation.scores.percentage,
                trimmed_mean=model_evaluation.scores.percentage
            ),
            weights_used={model_evaluation.model_name: 1.0},
            weighting_rationale="equal",  # Valid literal
            confidence_in_decision=1.0,
            decision_uncertainty=0.0,
            letter_grade="N/A",
            pass_fail="N/A",
            consensus_level="strong"  # Valid literal
        ),
        ensemble_quality=EnsembleQuality(
            diversity_score=0.0,
            redundancy_score=0.0,
            complementarity_score=0.0,
            overall_ensemble_value="moderate",  # Valid literal
            recommendation="single_model_sufficient",
            confidence_improvement_vs_single=0.0
        ),
        flags=Flags(
            needs_human_review=False,
            review_urgency="none",
            review_reasons=[],
            overall_agreement="perfect",  # Valid literal
            interesting_for_research=False,
            research_interest_reasons=[],
            recommended_actions=["accept_ensemble_grade"]
        ),
        metadata=ComparisonMetadata(
            computed_at=datetime.now(), # Added missing field
            computation_version="1.0.0", # Renamed from generator_version
            models_evaluated=[model_evaluation.model_name], # Renamed/Added
            num_models=1, # Added
            thresholds_config=ThresholdsConfig(
                human_review_score_diff_percent=10.0,
                boundary_score_margin_percent=5.0,
                low_confidence_threshold=0.7,
                high_confidence_threshold=0.9,
                outlier_std_devs=2.0,
                cv_high_agreement_max=0.1,
                cv_medium_agreement_max=0.2,
                icc_excellent_min=0.75,
                icc_good_min=0.6
            )
        )
    )

    # Assemble Document
    doc = EvaluationDocument(
        evaluation_id=f"eval_cuboid_{student_id}_{model_evaluation.run_id}",
        schema_version=MODELS_VERSION,
        created_at=datetime.now(),
        created_by="one_student_script",
        context=context,
        submission=submission,
        rubric=rubric,
        models=models,
        comparison=comparison
    )

    return doc


def main():
    # 1. Setup paths
    base_path = Path(__file__).parent
    student_id = "Johnson_Natalie_100010"
    student_name = "Natalie Johnson" # Hardcoded for now
    submission_path = base_path / "student_submissions" / student_id / "Cuboid.java"
    question_path = base_path / "question_cuboid.md"
    rubric_path = base_path / "rubric_cuboid.json"
    output_dir = base_path / "student_evals"
    output_dir.mkdir(exist_ok=True)

    # 2. Load Data
    print(f"Loading data for student: {student_id}...")
    with open(question_path, "r") as f:
        question = f.read()
    
    with open(rubric_path, "r") as f:
        rubric_content = json.load(f)
        rubric_str = json.dumps(rubric_content) # evaluate_with_openai takes str
    
    with open(submission_path, "r") as f:
        student_code = f.read()

    # 3. Grade with OpenAI
    print("Grading with OpenAI...")
    model_evaluation = evaluate_with_openai(
        question=question,
        rubric=rubric_str,
        student_code=student_code
    )
    print("Grading complete.")

    # 4. Build Document
    print("Building EvaluationDocument...")
    doc = build_evaluation_document(
        student_id=student_id,
        student_name=student_name,
        student_code=student_code,
        question_content=question,
        rubric_content=rubric_content,
        model_evaluation=model_evaluation
    )

    # 5. Save to JSON
    output_file = output_dir / f"{student_id}_eval.json"
    print(f"Saving to {output_file}...")
    with open(output_file, "w") as f:
        f.write(doc.model_dump_json(indent=2))
    
    print("Done!")


if __name__ == "__main__":
    main()
