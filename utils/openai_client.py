import os
import json
from dotenv import load_dotenv
from openai import OpenAI
from pydantic_models import ModelEvaluationResponse

load_dotenv()

client = OpenAI(
    api_key=os.environ.get("OPENAI_API_KEY"),
)

# Read the question, rubric, and student submission
with open("question_cuboid.md", "r") as f:
    question = f.read()

with open("rubric_cuboid.json", "r") as f:
    rubric = f.read()

with open("student_submissions/Johnson_Natalie_100010/Cuboid.java", "r") as f:
    student_code = f.read()

# Build the evaluation prompt
evaluation_prompt = f"""You are an expert code evaluator. Evaluate the following student submission against the assignment requirements and rubric.

## Assignment Requirements:
{question}

## Grading Rubric:
{rubric}

## Student Submission (Cuboid.java):
```java
{student_code}
```

Please evaluate this submission and provide:
1. Overall scores for each category based on the rubric
2. Detailed feedback with strengths and areas for improvement
3. Any identified misconceptions about the requirements

Ensure you calculate the total points and percentage correctly."""

# Request structured evaluation
rsp = client.responses.parse(
    input=evaluation_prompt,
    model="gpt-5-nano",
    text_format=ModelEvaluationResponse,
)

print("Parsed Evaluation Response (JSON):")
print(json.dumps(rsp.output_parsed.model_dump(), indent=2))
