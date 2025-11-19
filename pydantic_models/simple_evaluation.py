from pydantic import BaseModel

class CriterionScore(BaseModel):
    criterion: str
    score: float
    max_score: float
    feedback: str

class SimpleEvaluationResponse(BaseModel):
    criteria_scores: list[CriterionScore]
    total_score: float
    max_possible_score: float
    overall_feedback: str
