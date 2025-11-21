import unittest
import json
import os
from pydantic_models.evaluation import EvaluationDocument
from utils.comparison_generator import (
    generate_category_agreement,
    generate_category_insights,
    generate_pairwise_differences,
    generate_score_summary,
)

class TestComparisonGenerator(unittest.TestCase):
    def test_generate_score_summary_real_data(self):
        # Load the real student evaluation file
        file_path = "student_evals/Diaz_Sergio_100029_eval.json"
        if not os.path.exists(file_path):
            self.skipTest(f"File {file_path} not found")
            
        with open(file_path, "r") as f:
            data = json.load(f)
            
        # Extract models directly to avoid validation errors on stale JSON
        # (The JSON on disk doesn't have pairwise_differences yet, but the model requires it)
        models_data = data.get("models", {})
        # We need to convert dicts to ModelEvaluation objects
        from pydantic_models.models import ModelEvaluation
        models = {k: ModelEvaluation(**v) for k, v in models_data.items()}

        # Generate the summary
        summary = generate_score_summary(models)

        # Print it out so we can see it
        print("\n=== Generated Score Summary ===")
        print(summary.model_dump_json(indent=2))
        print("===============================\n")

        # Basic assertions to ensure it calculated something
        self.assertTrue(len(summary.per_model_percentage) > 0)
        self.assertGreater(summary.mean, 0)
        
        # Verify specific values if we know them, or just sanity checks
        self.assertLessEqual(summary.min, summary.max)
        self.assertEqual(summary.range, round(summary.max - summary.min, 2))

    def test_generate_pairwise_differences_real_data(self):
        file_path = "student_evals/Diaz_Sergio_100029_eval.json"
        if not os.path.exists(file_path):
            self.skipTest(f"File {file_path} not found")
            
        with open(file_path, "r") as f:
            data = json.load(f)
            
        # Extract models directly
        models_data = data.get("models", {})
        from pydantic_models.models import ModelEvaluation
        models = {k: ModelEvaluation(**v) for k, v in models_data.items()}

        pairwise_diffs = generate_pairwise_differences(models)

        print("\n=== Pairwise Differences ===")
        print(json.dumps([p.model_dump() for p in pairwise_diffs], indent=2))
        print("===========================\n")

        # Assertions
        # With 2 models, we expect exactly 1 pair
        self.assertEqual(len(pairwise_diffs), 1)
        
        pair = pairwise_diffs[0]
        self.assertTrue(pair.model_a in models)
        self.assertTrue(pair.model_b in models)
        self.assertNotEqual(pair.model_a, pair.model_b)
        
        # Check math
        diff = pair.total_points_diff
        
        # Get max_points dynamically from one of the models
        model_a = models[pair.model_a]
        max_points = model_a.scores.max_points
        
        expected_percent = round((diff / max_points) * 100, 2)
        self.assertEqual(pair.percentage_diff, expected_percent)
        
        # Debug Category Mismatch if empty
        if not pair.category_differences:
            print("\n[DEBUG] Category Differences is empty. Checking IDs:")
            print(f"Model A ({pair.model_a}) Categories: {[c.category_id for c in models[pair.model_a].category_scores]}")
            print(f"Model B ({pair.model_b}) Categories: {[c.category_id for c in models[pair.model_b].category_scores]}")
        else:
            # If we have categories, check one
            cat_diff = pair.category_differences[0]
            self.assertIsInstance(cat_diff.difference, int)

    def test_generate_category_agreement_and_insights_real_data(self):
        file_path = "student_evals/Diaz_Sergio_100029_eval.json"
        if not os.path.exists(file_path):
            self.skipTest(f"File {file_path} not found")
            
        with open(file_path, "r") as f:
            data = json.load(f)
            
        # Extract models directly
        models_data = data.get("models", {})
        from pydantic_models.models import ModelEvaluation
        models = {k: ModelEvaluation(**v) for k, v in models_data.items()}

        # 1. Agreement
        agreement = generate_category_agreement(models)
        
        print("\n=== Category Agreement ===")
        # Print first one for brevity
        if agreement:
            print(json.dumps(agreement[0].model_dump(), indent=2))
        print("==========================\n")

        self.assertTrue(len(agreement) > 0)
        first_cat = agreement[0]
        self.assertIn("google/gemini-2.5-flash-lite", first_cat.all_model_scores)
        self.assertIn("moonshotai/kimi-k2-0905", first_cat.all_model_scores)
        
        # 2. Insights
        insights = generate_category_insights(agreement)
        
        print("\n=== Category Insights ===")
        print(json.dumps(insights.model_dump(), indent=2))
        print("=========================\n")
        
        self.assertIsNotNone(insights.most_controversial)
        self.assertIsNotNone(insights.most_agreed)
        self.assertIsNotNone(insights.lowest_confidence)
        
        # Verify logic: controversial should have >= CV than agreed
        self.assertGreaterEqual(
            insights.most_controversial.cv, 
            insights.most_agreed.cv
        )

if __name__ == '__main__':
    unittest.main()
