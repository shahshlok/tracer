import json
import os
from pathlib import Path
import pytest
from dotenv import load_dotenv

from utils.openai_client import evaluate_with_openai
from pydantic_models import ModelEvaluation

# Load environment variables
load_dotenv()

# Skip tests if OPENAI_API_KEY is not set
@pytest.mark.skipif(not os.environ.get("OPENAI_API_KEY"), reason="OPENAI_API_KEY not set")
class TestSimpleOpenAI:
    def test_evaluate_with_openai(self):
        base_path = Path(__file__).parent.parent
        
        with open(base_path / "question_cuboid.md", "r") as f:
            question = f.read()
            
        with open(base_path / "rubric_cuboid.json", "r") as f:
            rubric = f.read()
            
        # Using Johnson_Natalie_100010 as mentioned in the request
        submission_path = base_path / "student_submissions" / "Johnson_Natalie_100010" / "Cuboid.java"
        with open(submission_path, "r") as f:
            student_code = f.read()
            
        evaluation = evaluate_with_openai(question, rubric, student_code)
        
        assert isinstance(evaluation, ModelEvaluation)
        assert evaluation.scores.total_points_awarded >= 0
        assert evaluation.scores.max_points > 0
        assert len(evaluation.category_scores) > 0
