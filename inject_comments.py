import os
import json
import random
from pathlib import Path

def inject_header_comment(content, student_name, student_id):
    header = f"// Name: {student_name}\n// Student ID: {student_id}\n// Assignment 2\n\n"
    return header + content

def inject_inline_comment(content):
    lines = content.split('\n')
    new_lines = []
    
    comments = [
        " // calculate result",
        " // get input",
        " // formula",
        " // print output",
        " // initialize variables"
    ]
    
    for line in lines:
        new_lines.append(line)
        if ";" in line and "}" not in line and "import" not in line and random.random() < 0.15:
             new_lines[-1] += random.choice(comments)
    
    return '\n'.join(new_lines)

def inject_dead_code(content):
    lines = content.split('\n')
    new_lines = []
    
    dead_code_snippets = [
        "// System.out.println(\"Debug: \" + v0);",
        "// double temp = 0;",
        "// if (x < 0) { System.out.println(\"Error\"); }",
        "// Scanner s = new Scanner(System.in);",
        "// TODO: check this logic"
    ]
    
    for line in lines:
        new_lines.append(line)
        if "{" in line and "class" not in line and random.random() < 0.1:
            new_lines.append("    " + random.choice(dead_code_snippets))
            
    return '\n'.join(new_lines)

def inject_todo_comment(content):
    lines = content.split('\n')
    new_lines = []
    
    for line in lines:
        new_lines.append(line)
        if "public class" in line:
             new_lines.append("// TODO: Clean up code before submission")
    
    return '\n'.join(new_lines)

def main():
    base_dir = Path('/Users/Shlok/Seventh Term/Honours/ensemble-eval-cli/authentic_seeded')
    manifest_path = base_dir / 'manifest.json'
    
    with open(manifest_path, 'r') as f:
        manifest = json.load(f)
    
    # Group manifest by student to get names/IDs easily
    student_info = {}
    for entry in manifest:
        sid = entry['student_id']
        # Assuming format Name_ID in manifest, but directory is Name_ID_Archetype
        # We can extract name and ID from the student_id string in manifest if it follows a pattern
        # Looking at previous list_dir: "Anderson_Noah_200113" -> Name: Anderson Noah, ID: 200113
        parts = sid.split('_')
        if len(parts) >= 3:
             name = f"{parts[1]} {parts[0]}"
             id_num = parts[2]
             student_info[sid] = {'name': name, 'id': id_num}
        else:
             student_info[sid] = {'name': sid, 'id': 'Unknown'}

    print(f"Loaded info for {len(student_info)} students.")
    
    files_modified = 0
    
    for student_dir in base_dir.iterdir():
        if not student_dir.is_dir():
            continue
            
        # Match directory to student info
        matched_sid = None
        for sid in student_info.keys():
            if student_dir.name.startswith(sid):
                matched_sid = sid
                break
        
        if not matched_sid:
            continue
            
        student = student_info[matched_sid]
        
        # Determine what kind of comments this student adds (per student consistency)
        add_header = random.random() < 0.4
        add_inline = random.random() < 0.2
        add_dead = random.random() < 0.1
        add_todo = random.random() < 0.05
        
        for q in ['Q1', 'Q2', 'Q3', 'Q4']:
            file_path = student_dir / f"{q}.java"
            if not file_path.exists():
                continue
                
            with open(file_path, 'r') as f:
                content = f.read()
            
            original_content = content
            
            if add_header:
                content = inject_header_comment(content, student['name'], student['id'])
            
            if add_inline:
                content = inject_inline_comment(content)
                
            if add_dead:
                content = inject_dead_code(content)
                
            if add_todo:
                content = inject_todo_comment(content)
            
            if content != original_content:
                with open(file_path, 'w') as f:
                    f.write(content)
                files_modified += 1

    print(f"Injection complete. Modified {files_modified} files.")

if __name__ == "__main__":
    main()
