"""Authentic Seeded Submission Generator.

This module generates synthetic student submissions that look authentically "messy"
and "human" while containing specific, controlled misconceptions.
It does NOT copy existing student code; it simulates student coding styles.
"""

import json
import random
import os
from dataclasses import dataclass, field
from pathlib import Path
from typing import List, Dict, Optional

from utils.misconception_catalog import load_catalog

# =============================================================================
# NAMES & DATA GENERATION
# =============================================================================

FIRST_NAMES = [
    "Emma", "Liam", "Olivia", "Noah", "Ava", "Oliver", "Isabella", "Elijah",
    "Sophia", "Lucas", "Mia", "Mason", "Charlotte", "Logan", "Amelia", "Ethan",
    "Harper", "Jacob", "Evelyn", "Michael", "Abigail", "Daniel", "Emily", "Henry",
    "Sofia", "Jackson", "Avery", "Sebastian", "Ella", "Aiden", "Scarlett", "Matthew",
    "Grace", "Samuel", "Chloe", "David", "Camila", "Joseph", "Penelope", "Carter",
    "Riley", "Owen", "Layla", "Wyatt", "Lillian", "John", "Nora", "Jack", "Zoey", "Luke"
]

LAST_NAMES = [
    "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
    "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
    "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
    "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
    "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
    "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter"
]

def generate_student_id(index: int) -> str:
    """Generate a student ID in format: Lastname_Firstname_200{index}."""
    first = random.choice(FIRST_NAMES)
    last = random.choice(LAST_NAMES)
    # Ensure 3 digits for ID suffix
    student_num = f"200{index + 100}" 
    return f"{last}_{first}_{student_num}"

# =============================================================================
# STYLE PROFILES
# =============================================================================

@dataclass
class StyleProfile:
    """Defines the coding quirks and style of a simulated student."""
    scanner_name: str       # e.g., "sc", "input", "scanner", "reader"
    indent_style: str       # e.g., "    ", "  ", "\t"
    brace_style: str        # "end-of-line" or "new-line"
    spacing_around_ops: bool # True for "a + b", False for "a+b"
    extra_newlines: bool    # True to add random blank lines
    use_comments: bool      # True to add comments
    variable_style: str     # "short" (v0), "descriptive" (startVelocity), "camel" (startVel)
    
    @staticmethod
    def random_profile() -> 'StyleProfile':
        return StyleProfile(
            scanner_name=random.choice(["sc", "input", "scanner", "reader", "scan", "in", "s"]),
            indent_style=random.choice(["    ", "  ", "\t", "   "]),
            brace_style=random.choice(["end-of-line", "new-line", "mixed"]),
            spacing_around_ops=random.choice([True, False, True]), # Bias towards proper spacing
            extra_newlines=random.choice([True, False]),
            use_comments=random.choice([True, False, False]),
            variable_style=random.choice(["short", "descriptive", "mixed"])
        )

# =============================================================================
# CODE GENERATION ENGINE
# =============================================================================

class CodeBuilder:
    """Builds Java code based on a style profile."""
    
    def __init__(self, style: StyleProfile):
        self.style = style
        self.code_lines = []
        self.indent_level = 0
        
    def add_line(self, text: str = ""):
        if not text:
            if self.style.extra_newlines and random.random() < 0.2:
                self.code_lines.append("") # Random extra blank line
            self.code_lines.append("")
            return

        indent = self.style.indent_style * self.indent_level
        self.code_lines.append(f"{indent}{text}")

    def open_brace(self):
        if self.style.brace_style == "new-line":
            self.add_line("{")
        elif self.style.brace_style == "end-of-line":
            # Append to last line if possible
            if self.code_lines and not self.code_lines[-1].strip().endswith("{"):
                self.code_lines[-1] += " {"
        else: # Mixed
             if random.random() > 0.5:
                 self.add_line("{")
             else:
                 if self.code_lines: self.code_lines[-1] += " {"
        self.indent_level += 1

    def close_brace(self):
        self.indent_level -= 1
        self.add_line("}")

    def var_decl(self, type_: str, name: str, value: str):
        op = " = " if self.style.spacing_around_ops else "="
        self.add_line(f"{type_} {name}{op}{value};")

    def get_code(self) -> str:
        return "\n".join(self.code_lines)

# =============================================================================
# LOGIC TEMPLATES & INJECTORS
# =============================================================================

class QuestionLogic:
    """Base class for question generation logic."""
    def generate(self, style: StyleProfile, misconception_id: Optional[str]) -> str:
        raise NotImplementedError

class Q1Logic(QuestionLogic):
    """Q1: Acceleration = (v1 - v0) / t"""
    
    def generate(self, style: StyleProfile, misconception_id: Optional[str]) -> str:
        builder = CodeBuilder(style)
        builder.add_line("import java.util.Scanner;")
        builder.add_line()
        builder.add_line("public class Q1")
        builder.open_brace()
        builder.add_line("public static void main(String[] args)")
        builder.open_brace()
        
        sc = style.scanner_name
        builder.add_line(f"Scanner {sc} = new Scanner(System.in);")
        builder.add_line('System.out.print("Enter v0, v1, and t: ");')
        
        # Variable names
        if style.variable_style == "short":
            v0, v1, t = "v0", "v1", "t"
        elif style.variable_style == "descriptive":
            v0, v1, t = "startVelocity", "endVelocity", "time"
        else:
            v0, v1, t = "vStart", "vEnd", "t"

        # Input type handling (DT001 injection)
        type_decl = "double"
        method = "nextDouble()"
        if misconception_id == "DT001":
            type_decl = "int"
            method = "nextInt()"
        
        builder.var_decl(type_decl, v0, f"{sc}.{method}")
        builder.var_decl(type_decl, v1, f"{sc}.{method}")
        
        # INPUT002: Missing input injection
        if misconception_id == "INPUT002":
            if style.use_comments: builder.add_line("// Forgot to read time")
        else:
             builder.var_decl(type_decl, t, f"{sc}.{method}")

        # Logic Injection
        acc_var = "acceleration" if style.variable_style == "descriptive" else "a"
        
        logic = ""
        op_minus = " - " if style.spacing_around_ops else "-"
        op_div = " / " if style.spacing_around_ops else "/"
        
        if misconception_id == "DT001":
            # Logic remains correct structure, but types are ints
             logic = f"({v1}{op_minus}{v0}){op_div}{t}"
        elif misconception_id == "DT002": # Int division
            logic = f"(int)({v1}{op_minus}{v0}){op_div}(int){t}"
        elif misconception_id == "VAR001": # Precedence
            logic = f"{v1}{op_minus}{v0}{op_div}{t}"
        elif misconception_id == "VAR002": # Wrong operator
            logic = f"({v1} + {v0}){op_div}{t}"
        elif misconception_id == "INPUT002": # Missing input logic
             logic = f"({v1}{op_minus}{v0}){op_div}1"
        elif misconception_id == "OTHER001": # Wrong problem (Velocity)
             logic = f"{v0} + {v1} * {t}"
             acc_var = "velocity"
        else: # Correct
            logic = f"({v1}{op_minus}{v0}){op_div}{t}"

        builder.var_decl("double", acc_var, logic)
        
        if misconception_id == "OTHER001":
             builder.add_line(f'System.out.println("The velocity is " + {acc_var});')
        else:
             builder.add_line(f'System.out.println("The average acceleration is " + {acc_var});')
        
        builder.close_brace()
        builder.close_brace()
        return builder.get_code()

class Q2Logic(QuestionLogic):
    """Q2: Cost = (distance / mpg) * price"""
    
    def generate(self, style: StyleProfile, misconception_id: Optional[str]) -> str:
        builder = CodeBuilder(style)
        builder.add_line("import java.util.Scanner;")
        builder.add_line("public class Q2")
        builder.open_brace()
        builder.add_line("public static void main(String[] args)")
        builder.open_brace()
        
        sc = style.scanner_name
        builder.add_line(f"Scanner {sc} = new Scanner(System.in);")
        
        # Vars
        if style.variable_style == "short":
            dist, mpg, price = "d", "mpg", "p"
        else:
            dist, mpg, price = "distance", "milesPerGallon", "price"
            
        type_decl = "int" if misconception_id == "DT001" else "double"
        method = "nextInt()" if misconception_id == "DT001" else "nextDouble()"

        builder.add_line('System.out.print("Enter the driving distance in miles: ");')
        builder.var_decl(type_decl, dist, f"{sc}.{method}")
        
        builder.add_line('System.out.print("Enter miles per gallon: ");')
        builder.var_decl(type_decl, mpg, f"{sc}.{method}")
        
        builder.add_line('System.out.print("Enter price in $ per gallon: ");')
        builder.var_decl(type_decl, price, f"{sc}.{method}")

        cost_var = "cost"
        op_mult = " * " if style.spacing_around_ops else "*"
        op_div = " / " if style.spacing_around_ops else "/"
        
        logic = ""
        if misconception_id == "DT002": # Int Div
            logic = f"((int){dist}{op_div}(int){mpg}){op_mult}{price}"
        elif misconception_id == "VAR001": # Precedence (though strictly left-associative works here, students mess it up)
             logic = f"{dist}{op_div}{mpg}{op_mult}{price}" # Actually correct in Java but often flagged if they mean price/(dist/mpg) - sticking to plan: no parens logic
        elif misconception_id == "VAR003": # Wrong formula
             logic = f"{dist}{op_mult}{mpg}{op_mult}{price}"
        else:
             logic = f"({dist}{op_div}{mpg}){op_mult}{price}"

        builder.var_decl("double", cost_var, logic)
        builder.add_line(f'System.out.println("The cost of driving is $" + {cost_var});')
        
        builder.close_brace()
        builder.close_brace()
        return builder.get_code()

class Q3Logic(QuestionLogic):
    """Q3: Distance = sqrt((x2-x1)^2 + (y2-y1)^2)"""
    
    def generate(self, style: StyleProfile, misconception_id: Optional[str]) -> str:
        builder = CodeBuilder(style)
        builder.add_line("import java.util.Scanner;")
        builder.add_line("public class Q3")
        builder.open_brace()
        builder.add_line("public static void main(String[] args)")
        builder.open_brace()
        
        sc = style.scanner_name
        builder.add_line(f"Scanner {sc} = new Scanner(System.in);")
        
        builder.add_line('System.out.print("Enter x1 and y1: ");')
        type_decl = "int" if misconception_id == "DT001" else "double"
        method = "nextInt()" if misconception_id == "DT001" else "nextDouble()"
        
        builder.var_decl(type_decl, "x1", f"{sc}.{method}")
        builder.var_decl(type_decl, "y1", f"{sc}.{method}")
        
        builder.add_line('System.out.print("Enter x2 and y2: ");')
        builder.var_decl(type_decl, "x2", f"{sc}.{method}")
        
        if misconception_id == "INPUT002":
             builder.var_decl(type_decl, "y2", "0") # Forgot input
        else:
             builder.var_decl(type_decl, "y2", f"{sc}.{method}")

        dist_var = "distance"
        
        # Helper for power logic
        def power(base, exp):
            if misconception_id == "CONST001": return f"({base}) ^ {exp}" # Caret
            if misconception_id == "CONST003": return f"Math.pow({exp}, {base})" # Swapped
            return f"Math.pow({base}, {exp})"

        term1 = power("x2 - x1", 2)
        term2 = power("y2 - y1", 2)
        
        logic = ""
        if misconception_id == "CONST002": # Missing Sqrt
            logic = f"{term1} + {term2}"
        elif misconception_id == "OTHER001": # Midpoint
            builder.var_decl("double", "mx", "(x1+x2)/2")
            builder.var_decl("double", "my", "(y1+y2)/2")
            builder.add_line('System.out.println("Midpoint: " + mx + "," + my);')
            builder.close_brace()
            builder.close_brace()
            return builder.get_code()
        else:
            logic = f"Math.sqrt({term1} + {term2})"

        builder.var_decl("double", dist_var, logic)
        builder.add_line(f'System.out.println("The distance of the two points is " + {dist_var});')

        builder.close_brace()
        builder.close_brace()
        return builder.get_code()

class Q4Logic(QuestionLogic):
    """Q4: Triangle Area"""
    
    def generate(self, style: StyleProfile, misconception_id: Optional[str]) -> str:
        builder = CodeBuilder(style)
        builder.add_line("import java.util.Scanner;")
        builder.add_line("public class Q4")
        builder.open_brace()
        builder.add_line("public static void main(String[] args)")
        builder.open_brace()
        
        sc = style.scanner_name
        builder.add_line(f"Scanner {sc} = new Scanner(System.in);")
        builder.add_line('System.out.println("Enter three points for a triangle.");')
        
        type_decl = "int" if misconception_id == "DT001" else "double"
        method = "nextInt()" if misconception_id == "DT001" else "nextDouble()"
        
        for i in range(1, 4):
             builder.add_line(f'System.out.print("(x{i}, y{i}): ");')
             builder.var_decl(type_decl, f"x{i}", f"{sc}.{method}")
             builder.var_decl(type_decl, f"y{i}", f"{sc}.{method}")

        # Side calc helper
        def side_len(p1, p2):
            base1 = f"x{p2} - x{p1}"
            base2 = f"y{p2} - y{p1}"
            if misconception_id == "CONST001": # Caret
                 return f"Math.sqrt(({base1})^2 + ({base2})^2)"
            if misconception_id == "CONST002": # Missing Sqrt (sides)
                 return f"Math.pow({base1}, 2) + Math.pow({base2}, 2)"
            return f"Math.sqrt(Math.pow({base1}, 2) + Math.pow({base2}, 2))"

        builder.var_decl("double", "side1", side_len(1, 2))
        builder.var_decl("double", "side2", side_len(2, 3))
        builder.var_decl("double", "side3", side_len(3, 1))

        builder.var_decl("double", "s", "(side1 + side2 + side3) / 2")
        
        area_logic = "s * (s - side1) * (s - side2) * (s - side3)"
        if misconception_id != "CONST002": # If not missing sqrt
             area_logic = f"Math.sqrt({area_logic})"
        
        builder.var_decl("double", "area", area_logic)
        builder.add_line('System.out.println("The area of the triangle is " + area);')

        builder.close_brace()
        builder.close_brace()
        return builder.get_code()

# =============================================================================
# GENERATOR ORCHESTRATOR
# =============================================================================

@dataclass
class AuthenticSubmission:
    student_id: str
    question_id: str
    misconception_id: Optional[str]
    code: str

class AuthenticGenerator:
    def __init__(self, output_dir: str = "authentic_seeded"):
        self.output_dir = Path(output_dir)
        self.catalog = load_catalog()
        self.generators = {
            "q1": Q1Logic(),
            "q2": Q2Logic(),
            "q3": Q3Logic(),
            "q4": Q4Logic(),
        }

    def generate_batch(self, count: int = 50):
        submissions = []
        manifest = []
        
        # Generate 'count' students, each doing all 4 questions
        for i in range(count):
            # 1. Pick Student ID & Style (Once per student)
            student_id = generate_student_id(i)
            style = StyleProfile.random_profile()
            
            # 2. Determine if this student is "Correct" or has a specific misconception
            # We assign a "primary" misconception to the student to simulate a systematic error,
            # OR we can randomize per question.
            # Let's randomize per question to get better coverage, but keep style consistent.
            
            questions = ["q1", "q2", "q3", "q4"]
            
            # Track the "tag" for the folder. 
            # If a student has ANY misconception, tag it with the first one found or "Mixed".
            # If all correct, tag "Correct".
            student_misconceptions = []
            
            for q_id in questions:
                # Weighted: 30% chance of misconception per question
                is_correct = random.random() > 0.3 
                misc_id = None
                
                if not is_correct:
                    applicable = self.catalog.get_for_question(q_id)
                    if applicable:
                        misc_id = random.choice(applicable).id
                        student_misconceptions.append(misc_id)
                
                # Generate Code
                generator = self.generators[q_id]
                code = generator.generate(style, misc_id)
                
                submissions.append(AuthenticSubmission(
                    student_id=student_id,
                    question_id=q_id,
                    misconception_id=misc_id,
                    code=code
                ))
                
                manifest.append({
                    "student_id": student_id,
                    "question_id": q_id,
                    "misconception_id": misc_id,
                    "is_correct": misc_id is None
                })

        self.save_results(submissions, manifest)

    def save_results(self, submissions: List[AuthenticSubmission], manifest: List[Dict]):
        self.output_dir.mkdir(exist_ok=True)
        
        # Group by student_id to determine folder name
        student_map = {}
        for sub in submissions:
            if sub.student_id not in student_map:
                student_map[sub.student_id] = {"subs": [], "miscs": set()}
            student_map[sub.student_id]["subs"].append(sub)
            if sub.misconception_id:
                student_map[sub.student_id]["miscs"].add(sub.misconception_id)
        
        for student_id, data in student_map.items():
            # Determine Tag
            # If no misconceptions -> Correct
            # If 1 misconception -> ThatID
            # If >1 -> Mixed
            miscs = list(data["miscs"])
            if not miscs:
                tag = "Correct"
            elif len(miscs) == 1:
                tag = miscs[0]
            else:
                tag = "Mixed" # Or just list the first one? "Mixed" is clearer.
                
            folder_name = f"{student_id}_{tag}"
            student_dir = self.output_dir / folder_name
            student_dir.mkdir(exist_ok=True)
            
            for sub in data["subs"]:
                filename = f"{sub.question_id.upper()}.java"
                with open(student_dir / filename, "w") as f:
                    f.write(sub.code)

                
        # Save Manifest
        with open(self.output_dir / "manifest.json", "w") as f:
            json.dump(manifest, f, indent=2)
            
        # Save Markdown Report
        self.save_markdown_report(manifest)
            
        print(f"Generated {len(submissions)} authentic seeded submissions in {self.output_dir}")

    def save_markdown_report(self, manifest: List[Dict]):
        """Generates a human-readable Markdown report of the seeded submissions."""
        report_lines = [
            "# Authentic Seeded Submissions Report",
            "",
            f"**Generated At:** {os.path.dirname(__file__)}",  # Timestamp would be better if passed
            f"**Total Submissions:** {len(manifest)}",
            "",
            "## Summary by Misconception",
            "| ID | Count | Description |",
            "| :--- | :--- | :--- |"
        ]
        
        # Count misconceptions
        counts = {}
        descriptions = {m.id: m.name for m in self.catalog.misconceptions}
        descriptions["Correct"] = "Control Group (No Errors)"
        
        for entry in manifest:
            mid = entry["misconception_id"] if entry["misconception_id"] else "Correct"
            counts[mid] = counts.get(mid, 0) + 1
            
        for mid, count in sorted(counts.items()):
            desc = descriptions.get(mid, "Unknown Misconception")
            report_lines.append(f"| **{mid}** | {count} | {desc} |")
            
        report_lines.extend([
            "",
            "## Detailed Submission List",
            "| Student Directory | Question | Status | Misconception |",
            "| :--- | :--- | :--- | :--- |"
        ])
        
        for entry in manifest:
            mid = entry["misconception_id"] if entry["misconception_id"] else "Correct"
            status = "✅ Correct" if entry["is_correct"] else "❌ Incorrect"
            # Reconstruct folder name logic
            folder_name = f"{entry['student_id']}_{mid}"
            
            report_lines.append(f"| `{folder_name}` | {entry['question_id'].upper()} | {status} | {mid} |")
            
        with open(self.output_dir / "README.md", "w") as f:
            f.write("\n".join(report_lines))


if __name__ == "__main__":
    gen = AuthenticGenerator()
    gen.generate_batch(60)
