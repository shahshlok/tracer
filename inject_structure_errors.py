import os
import random
import re
from pathlib import Path

def inject_class_name_mismatch(content, q_name):
    # Change "public class Q1" to "public class q1" or "class Q1"
    if random.random() < 0.5:
        # Lowercase class name
        return content.replace(f"class {q_name}", f"class {q_name.lower()}")
    else:
        # Remove public
        return content.replace(f"public class {q_name}", f"class {q_name}")

def inject_import_issues(content):
    if random.random() < 0.5:
        # Remove Scanner import
        return content.replace("import java.util.Scanner;", "")
    else:
        # Wildcard import
        return content.replace("import java.util.Scanner;", "import java.util.*;")

def inject_package_declaration(content):
    # Add package declaration at the top
    packages = ["com.student", "assignment2", "mycode", "default"]
    pkg = random.choice(packages)
    return f"package {pkg};\n" + content

def inject_debug_prints(content):
    lines = content.split('\n')
    new_lines = []
    debug_stmts = [
        'System.out.println("DEBUG: reached here");',
        'System.out.println("test");',
        'System.out.println("v0=" + v0);', # This might fail if v0 doesn't exist, but that's realistic
        'System.out.println("----------------");'
    ]
    
    for line in lines:
        new_lines.append(line)
        # Inject after variable declarations or inside main
        if ";" in line and "import" not in line and "package" not in line and random.random() < 0.1:
            stmt = random.choice(debug_stmts)
            # Try to match indentation
            indent = re.match(r"\s*", line).group(0)
            new_lines.append(indent + stmt)
            
    return '\n'.join(new_lines)

def main():
    base_dir = Path('/Users/Shlok/Seventh Term/Honours/ensemble-eval-cli/authentic_seeded')
    
    files_modified = 0
    
    for student_dir in base_dir.iterdir():
        if not student_dir.is_dir():
            continue
            
        # Per-student randomness for "bad habits"
        # Some students might always use wildcard imports, etc.
        # But here we'll just randomize per file for simplicity/variety as per plan
        
        for q in ['Q1', 'Q2', 'Q3', 'Q4']:
            file_path = student_dir / f"{q}.java"
            if not file_path.exists():
                continue
                
            with open(file_path, 'r') as f:
                content = f.read()
            
            original_content = content
            
            # Independent probabilities
            if random.random() < 0.05:
                content = inject_class_name_mismatch(content, q)
            
            if random.random() < 0.05:
                content = inject_import_issues(content)
                
            if random.random() < 0.05:
                content = inject_package_declaration(content)
                
            if random.random() < 0.10:
                content = inject_debug_prints(content)
            
            if content != original_content:
                with open(file_path, 'w') as f:
                    f.write(content)
                files_modified += 1

    print(f"Structure injection complete. Modified {files_modified} files.")

if __name__ == "__main__":
    main()
