"""Seeded submission generator for validation.

This module generates Java code submissions with known misconceptions
injected for measuring detection precision and recall.
"""

import json
import os
import random
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path

from utils.misconception_catalog import load_catalog, Misconception


# =============================================================================
# CODE TEMPLATES - Correct implementations for each question
# =============================================================================

TEMPLATE_Q1_CORRECT = '''import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter v0, v1, and t: ");
        double v0 = scanner.nextDouble();
        double v1 = scanner.nextDouble();
        double t = scanner.nextDouble();
        
        double acceleration = (v1 - v0) / t;
        
        System.out.println("The average acceleration is " + acceleration);
        
        scanner.close();
    }
}
'''

TEMPLATE_Q2_CORRECT = '''import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the driving distance in miles: ");
        double distance = scanner.nextDouble();
        
        System.out.print("Enter miles per gallon: ");
        double mpg = scanner.nextDouble();
        
        System.out.print("Enter price in $ per gallon: ");
        double price = scanner.nextDouble();
        
        double cost = (distance / mpg) * price;
        
        System.out.println("The cost of driving is $" + cost);
        
        scanner.close();
    }
}
'''

TEMPLATE_Q3_CORRECT = '''import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter x1 and y1: ");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();
        
        System.out.print("Enter x2 and y2: ");
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();
        
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        
        System.out.println("The distance of the two points is " + distance);
        
        scanner.close();
    }
}
'''

TEMPLATE_Q4_CORRECT = '''import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter three points for a triangle.");
        
        System.out.print("(x1, y1): ");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();
        
        System.out.print("(x2, y2): ");
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();
        
        System.out.print("(x3, y3): ");
        double x3 = scanner.nextDouble();
        double y3 = scanner.nextDouble();
        
        double side1 = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double side2 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double side3 = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));
        
        double s = (side1 + side2 + side3) / 2;
        double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
        
        System.out.println("The area of the triangle is " + area);
        
        scanner.close();
    }
}
'''

# =============================================================================
# MISCONCEPTION INJECTORS - Functions that inject specific misconceptions
# =============================================================================

def inject_DT001_int_instead_of_double(code: str, question: str) -> str:
    """Inject: Using int instead of double for decimal values."""
    # Replace double declarations with int
    code = code.replace("double v0 = scanner.nextDouble();", "int v0 = scanner.nextInt();")
    code = code.replace("double v1 = scanner.nextDouble();", "int v1 = scanner.nextInt();")
    code = code.replace("double t = scanner.nextDouble();", "int t = scanner.nextInt();")
    code = code.replace("double distance = scanner.nextDouble();", "int distance = scanner.nextInt();")
    code = code.replace("double mpg = scanner.nextDouble();", "int mpg = scanner.nextInt();")
    code = code.replace("double price = scanner.nextDouble();", "int price = scanner.nextInt();")
    code = code.replace("double x1 = scanner.nextDouble();", "int x1 = scanner.nextInt();")
    code = code.replace("double y1 = scanner.nextDouble();", "int y1 = scanner.nextInt();")
    code = code.replace("double x2 = scanner.nextDouble();", "int x2 = scanner.nextInt();")
    code = code.replace("double y2 = scanner.nextDouble();", "int y2 = scanner.nextInt();")
    code = code.replace("double x3 = scanner.nextDouble();", "int x3 = scanner.nextInt();")
    code = code.replace("double y3 = scanner.nextDouble();", "int y3 = scanner.nextInt();")
    return code


def inject_DT002_integer_division(code: str, question: str) -> str:
    """Inject: Integer division truncation."""
    if question == "q1":
        # Change formula to use integer literals
        code = code.replace(
            "double acceleration = (v1 - v0) / t;",
            "double acceleration = (int)(v1 - v0) / (int)t;  // integer division!"
        )
    elif question == "q2":
        code = code.replace(
            "double cost = (distance / mpg) * price;",
            "double cost = ((int)distance / (int)mpg) * price;  // integer division!"
        )
    return code


def inject_VAR001_operator_precedence(code: str, question: str) -> str:
    """Inject: Incorrect operator precedence (missing parentheses)."""
    if question == "q1":
        code = code.replace(
            "double acceleration = (v1 - v0) / t;",
            "double acceleration = v1 - v0 / t;  // wrong precedence!"
        )
    elif question == "q2":
        code = code.replace(
            "double cost = (distance / mpg) * price;",
            "double cost = distance / mpg * price;  // precedence issue"
        )
    return code


def inject_VAR002_wrong_operator(code: str, question: str) -> str:
    """Inject: Wrong formula - addition instead of subtraction."""
    if question == "q1":
        code = code.replace(
            "double acceleration = (v1 - v0) / t;",
            "double acceleration = (v1 + v0) / t;  // wrong: should be subtraction!"
        )
    return code


def inject_VAR003_wrong_fuel_formula(code: str, question: str) -> str:
    """Inject: Incorrect formula derivation for fuel cost."""
    if question == "q2":
        code = code.replace(
            "double cost = (distance / mpg) * price;",
            "double cost = distance * mpg * price;  // wrong formula!"
        )
    return code


def inject_CONST001_caret_instead_of_pow(code: str, question: str) -> str:
    """Inject: Using ^ instead of Math.pow() for exponentiation."""
    if question in ["q3", "q4"]:
        code = code.replace("Math.pow(x2 - x1, 2)", "(x2 - x1) ^ 2")
        code = code.replace("Math.pow(y2 - y1, 2)", "(y2 - y1) ^ 2")
        code = code.replace("Math.pow(x3 - x2, 2)", "(x3 - x2) ^ 2")
        code = code.replace("Math.pow(y3 - y2, 2)", "(y3 - y2) ^ 2")
        code = code.replace("Math.pow(x1 - x3, 2)", "(x1 - x3) ^ 2")
        code = code.replace("Math.pow(y1 - y3, 2)", "(y1 - y3) ^ 2")
    return code


def inject_CONST002_missing_sqrt(code: str, question: str) -> str:
    """Inject: Missing Math.sqrt() for square root."""
    if question == "q3":
        code = code.replace(
            "double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));",
            "double distance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);  // missing sqrt!"
        )
    elif question == "q4":
        code = code.replace(
            "double side1 = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));",
            "double side1 = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);  // missing sqrt!"
        )
        code = code.replace(
            "double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));",
            "double area = s * (s - side1) * (s - side2) * (s - side3);  // missing sqrt!"
        )
    return code


def inject_CONST003_pow_args_swapped(code: str, question: str) -> str:
    """Inject: Incorrect Math.pow() argument order."""
    if question in ["q3", "q4"]:
        code = code.replace("Math.pow(x2 - x1, 2)", "Math.pow(2, x2 - x1)")
        code = code.replace("Math.pow(y2 - y1, 2)", "Math.pow(2, y2 - y1)")
    return code


def inject_INPUT002_missing_input(code: str, question: str) -> str:
    """Inject: Scanner not reading correct number of inputs."""
    if question == "q1":
        # Remove reading of t
        code = code.replace(
            "double t = scanner.nextDouble();",
            "// forgot to read t!"
        )
        code = code.replace(
            "double acceleration = (v1 - v0) / t;",
            "double acceleration = (v1 - v0) / 1;  // t not read!"
        )
    elif question == "q3":
        # Remove reading of y2
        code = code.replace(
            "double y2 = scanner.nextDouble();",
            "double y2 = 0;  // forgot to read!"
        )
    return code


def inject_OTHER001_wrong_problem(code: str, question: str) -> str:
    """Inject: Computing wrong quantity (different problem)."""
    if question == "q1":
        # Student computes velocity instead of acceleration
        code = code.replace(
            "double acceleration = (v1 - v0) / t;",
            "double velocity = v0 + v1 * t;  // wrong! computing velocity, not acceleration"
        )
        code = code.replace(
            'System.out.println("The average acceleration is " + acceleration);',
            'System.out.println("The velocity is " + velocity);'
        )
    elif question == "q3":
        # Student computes midpoint instead of distance
        code = code.replace(
            "double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));",
            "double midX = (x1 + x2) / 2;\n        double midY = (y1 + y2) / 2;  // wrong! computing midpoint"
        )
        code = code.replace(
            'System.out.println("The distance of the two points is " + distance);',
            'System.out.println("The midpoint is (" + midX + ", " + midY + ")");'
        )
    return code


def inject_OTHER002_hardcoded_values(code: str, question: str) -> str:
    """Inject: Hardcoded values instead of user input."""
    if question == "q1":
        code = code.replace(
            'System.out.print("Enter v0, v1, and t: ");\n        double v0 = scanner.nextDouble();\n        double v1 = scanner.nextDouble();\n        double t = scanner.nextDouble();',
            '// Hardcoded values instead of input!\n        double v0 = 3.0;\n        double v1 = 30.4;\n        double t = 1.5;'
        )
    elif question == "q2":
        code = code.replace(
            'System.out.print("Enter the driving distance in miles: ");\n        double distance = scanner.nextDouble();',
            '// Hardcoded!\n        double distance = 155.0;'
        )
    return code


# Map misconception IDs to injector functions
INJECTORS = {
    "DT001": inject_DT001_int_instead_of_double,
    "DT002": inject_DT002_integer_division,
    "VAR001": inject_VAR001_operator_precedence,
    "VAR002": inject_VAR002_wrong_operator,
    "VAR003": inject_VAR003_wrong_fuel_formula,
    "CONST001": inject_CONST001_caret_instead_of_pow,
    "CONST002": inject_CONST002_missing_sqrt,
    "CONST003": inject_CONST003_pow_args_swapped,
    "INPUT002": inject_INPUT002_missing_input,
    "OTHER001": inject_OTHER001_wrong_problem,
    "OTHER002": inject_OTHER002_hardcoded_values,
}

# Templates by question
TEMPLATES = {
    "q1": TEMPLATE_Q1_CORRECT,
    "q2": TEMPLATE_Q2_CORRECT,
    "q3": TEMPLATE_Q3_CORRECT,
    "q4": TEMPLATE_Q4_CORRECT,
}


# =============================================================================
# SEEDED SUBMISSION GENERATOR
# =============================================================================

@dataclass
class SeededSubmission:
    """A generated submission with known misconceptions."""
    student_id: str
    question_id: str
    code: str
    injected_misconceptions: list[str]  # List of misconception IDs
    is_correct: bool  # True if no misconceptions injected


@dataclass 
class GenerationManifest:
    """Manifest tracking all generated submissions."""
    generated_at: str
    total_submissions: int
    submissions: list[dict] = field(default_factory=list)
    
    def add(self, submission: SeededSubmission):
        self.submissions.append({
            "student_id": submission.student_id,
            "question_id": submission.question_id,
            "injected_misconceptions": submission.injected_misconceptions,
            "is_correct": submission.is_correct,
        })
        self.total_submissions = len(self.submissions)
    
    def to_json(self) -> str:
        return json.dumps({
            "generated_at": self.generated_at,
            "total_submissions": self.total_submissions,
            "submissions": self.submissions,
        }, indent=2)


class SeededGenerator:
    """Generates seeded submissions with known misconceptions."""
    
    def __init__(self, output_dir: str = "seeded_submissions"):
        self.output_dir = Path(output_dir)
        self.catalog = load_catalog()
        self.manifest = GenerationManifest(
            generated_at=datetime.now().isoformat(),
            total_submissions=0
        )
    
    def generate_submission(
        self,
        student_id: str,
        question_id: str,
        misconception_ids: list[str] | None = None
    ) -> SeededSubmission:
        """Generate a single submission with specified misconceptions.
        
        Args:
            student_id: ID for the synthetic student
            question_id: Which question (q1, q2, q3, q4)
            misconception_ids: List of misconception IDs to inject, or None for correct submission
        
        Returns:
            SeededSubmission object
        """
        question_id = question_id.lower()
        if question_id not in TEMPLATES:
            raise ValueError(f"Unknown question: {question_id}")
        
        # Start with correct template
        code = TEMPLATES[question_id]
        injected = []
        
        # Inject misconceptions if specified
        if misconception_ids:
            for misc_id in misconception_ids:
                if misc_id in INJECTORS:
                    # Check if misconception applies to this question
                    misconception = self.catalog.get_by_id(misc_id)
                    if misconception and misconception.applies_to(question_id):
                        code = INJECTORS[misc_id](code, question_id)
                        injected.append(misc_id)
                    else:
                        print(f"Warning: {misc_id} does not apply to {question_id}")
                else:
                    print(f"Warning: No injector for {misc_id}")
        
        return SeededSubmission(
            student_id=student_id,
            question_id=question_id,
            code=code,
            injected_misconceptions=injected,
            is_correct=len(injected) == 0
        )
    
    def generate_batch(
        self,
        num_correct: int = 5,
        num_per_misconception: int = 2,
        questions: list[str] | None = None
    ) -> list[SeededSubmission]:
        """Generate a batch of submissions for validation.
        
        Args:
            num_correct: Number of correct submissions per question
            num_per_misconception: Number of submissions per misconception
            questions: Which questions to generate for (default: all)
        
        Returns:
            List of SeededSubmission objects
        """
        questions = questions or ["q1", "q2", "q3", "q4"]
        submissions = []
        student_counter = 1
        
        for question_id in questions:
            # Generate correct submissions
            for i in range(num_correct):
                student_id = f"Seeded_Correct_{student_counter:03d}"
                submission = self.generate_submission(student_id, question_id, None)
                submissions.append(submission)
                self.manifest.add(submission)
                student_counter += 1
            
            # Generate submissions with each applicable misconception
            applicable_miscs = self.catalog.get_for_question(question_id)
            for misc in applicable_miscs:
                if misc.id not in INJECTORS:
                    continue  # Skip if no injector
                
                for i in range(num_per_misconception):
                    student_id = f"Seeded_{misc.id}_{student_counter:03d}"
                    submission = self.generate_submission(
                        student_id, question_id, [misc.id]
                    )
                    submissions.append(submission)
                    self.manifest.add(submission)
                    student_counter += 1
        
        return submissions
    
    def save_submissions(self, submissions: list[SeededSubmission]) -> None:
        """Save submissions to disk."""
        # Create output directory
        self.output_dir.mkdir(parents=True, exist_ok=True)
        
        for submission in submissions:
            # Create student directory
            student_dir = self.output_dir / submission.student_id
            student_dir.mkdir(exist_ok=True)
            
            # Save code file
            filename = f"{submission.question_id.upper()}.java"
            filepath = student_dir / filename
            with open(filepath, "w") as f:
                f.write(submission.code)
        
        # Save manifest
        manifest_path = self.output_dir / "manifest.json"
        with open(manifest_path, "w") as f:
            f.write(self.manifest.to_json())
        
        print(f"Saved {len(submissions)} submissions to {self.output_dir}")
        print(f"Manifest saved to {manifest_path}")
    
    def generate_and_save(
        self,
        num_correct: int = 5,
        num_per_misconception: int = 2,
        questions: list[str] | None = None
    ) -> list[SeededSubmission]:
        """Generate and save a batch of submissions."""
        submissions = self.generate_batch(num_correct, num_per_misconception, questions)
        self.save_submissions(submissions)
        return submissions


def main():
    """Generate seeded submissions for validation."""
    generator = SeededGenerator(output_dir="seeded_submissions")
    
    # Generate batch: 5 correct + 2 per misconception per question
    submissions = generator.generate_and_save(
        num_correct=3,
        num_per_misconception=2,
        questions=["q1", "q2", "q3", "q4"]
    )
    
    # Print summary
    print(f"\nGenerated {len(submissions)} total submissions")
    
    correct_count = sum(1 for s in submissions if s.is_correct)
    print(f"  Correct submissions: {correct_count}")
    print(f"  With misconceptions: {len(submissions) - correct_count}")
    
    # Count by misconception
    misc_counts: dict[str, int] = {}
    for s in submissions:
        for misc_id in s.injected_misconceptions:
            misc_counts[misc_id] = misc_counts.get(misc_id, 0) + 1
    
    print("\nMisconceptions injected:")
    for misc_id, count in sorted(misc_counts.items()):
        print(f"  {misc_id}: {count}")


if __name__ == "__main__":
    main()
