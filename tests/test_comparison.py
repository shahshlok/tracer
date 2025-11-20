import unittest
import json
import os
from pydantic_models.evaluation import EvaluationDocument
from utils.comparison_generator import generate_score_summary

class TestComparisonGenerator(unittest.TestCase):
    def test_generate_score_summary_real_data(self):
        # Load the real student evaluation file
        file_path = "student_evals/Diaz_Sergio_100029_eval.json"
        if not os.path.exists(file_path):
            self.skipTest(f"File {file_path} not found")
            
        with open(file_path, "r") as f:
            data = json.load(f)
            
        # Parse into EvaluationDocument to ensure schema validity
        # This also checks if our schema changes (optional comparison) work with existing files
        try:
            eval_doc = EvaluationDocument(**data)
        except Exception as e:
            self.fail(f"Failed to parse real data: {e}")

        # Generate the summary
        summary = generate_score_summary(eval_doc.models)

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

if __name__ == '__main__':
    unittest.main()
