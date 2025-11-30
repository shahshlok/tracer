import os
import json
import re
from pathlib import Path

def analyze_submission(file_path):
    try:
        with open(file_path, 'r') as f:
            content = f.read()
        
        lines = content.split('\n')
        num_lines = len(lines)
        
        # Check indentation
        indentation_types = set()
        for line in lines:
            if line.startswith('\t'):
                indentation_types.add('tab')
            elif line.startswith(' '):
                indentation_types.add('space')
        
        # Check for comments
        has_comments = '//' in content or '/*' in content
        
        # Check for variable naming (simple heuristic)
        # Look for variable declarations like "double x =" or "int count ="
        var_decls = re.findall(r'(int|double|String|boolean)\s+([a-zA-Z0-9_]+)\s*=', content)
        var_names = [v[1] for v in var_decls]
        avg_var_len = sum(len(v) for v in var_names) / len(var_names) if var_names else 0
        
        return {
            'exists': True,
            'num_lines': num_lines,
            'indentation': list(indentation_types),
            'has_comments': has_comments,
            'avg_var_len': avg_var_len,
            'content_preview': content[:100].replace('\n', '\\n')
        }
    except FileNotFoundError:
        return {'exists': False}

def main():
    base_dir = Path('/Users/Shlok/Seventh Term/Honours/ensemble-eval-cli/authentic_seeded')
    manifest_path = base_dir / 'manifest.json'
    
    with open(manifest_path, 'r') as f:
        manifest = json.load(f)
    
    # Group manifest by student
    student_data = {}
    for entry in manifest:
        sid = entry['student_id']
        if sid not in student_data:
            student_data[sid] = []
        student_data[sid].append(entry)
    
    print(f"Found {len(student_data)} students in manifest.")
    
    results = {}
    
    # Iterate through directories in authentic_seeded
    for student_dir in base_dir.iterdir():
        if not student_dir.is_dir():
            continue
            
        student_id = student_dir.name
        # The directory name format is Name_ID_Archetype usually, but manifest has just Name_ID
        # Actually looking at the list_dir output: "Anderson_Noah_200113_DT003"
        # And manifest: "Anderson_Noah_200113"
        # So we need to match loosely.
        
        matched_sid = None
        for sid in student_data.keys():
            if student_dir.name.startswith(sid):
                matched_sid = sid
                break
        
        if not matched_sid:
            print(f"WARNING: Directory {student_dir.name} does not match any student ID in manifest.")
            continue
            
        results[student_dir.name] = {}
        
        for q in ['Q1', 'Q2', 'Q3', 'Q4']:
            file_path = student_dir / f"{q}.java"
            analysis = analyze_submission(file_path)
            results[student_dir.name][q] = analysis
            
            # Cross-reference with manifest
            # manifest entry for this question
            manifest_entry = next((e for e in student_data[matched_sid] if e['question_id'] == q.lower()), None)
            if manifest_entry:
                analysis['manifest_correct'] = manifest_entry['is_correct']
                analysis['manifest_misconception'] = manifest_entry['misconception_id']

    # Summary Statistics
    total_submissions = 0
    missing_submissions = 0
    varied_indentation = 0
    commented_code = 0
    short_vars = 0
    long_vars = 0
    
    print("\n--- Verification Report ---")
    
    for student, questions in results.items():
        print(f"\nStudent: {student}")
        for q, data in questions.items():
            if not data['exists']:
                print(f"  {q}: MISSING")
                missing_submissions += 1
            else:
                total_submissions += 1
                indent_str = ", ".join(data['indentation'])
                print(f"  {q}: Lines={data['num_lines']}, Indent={indent_str}, Comments={data['has_comments']}, AvgVarLen={data['avg_var_len']:.1f}")
                
                if 'space' in data['indentation'] and 'tab' in data['indentation']:
                    varied_indentation += 1
                if data['has_comments']:
                    commented_code += 1
                if data['avg_var_len'] < 3:
                    short_vars += 1
                elif data['avg_var_len'] > 8:
                    long_vars += 1

    print("\n--- Summary ---")
    print(f"Total Students Checked: {len(results)}")
    print(f"Total Submissions Checked: {total_submissions}")
    print(f"Missing Submissions: {missing_submissions}")
    print(f"Submissions with Comments: {commented_code}")
    print(f"Submissions with Short Vars (<3 chars): {short_vars}")
    print(f"Submissions with Long Vars (>8 chars): {long_vars}")
    
if __name__ == "__main__":
    main()
