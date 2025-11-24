# Realistic Student Submissions Overhaul - Walkthrough

I have successfully transformed the student submissions from "perfect AI clones" to a realistic, messy dataset representing an introductory CS course.

## Changes Implemented

I categorized 23 of the 26 students into specific archetypes and introduced targeted errors and bad practices.

### 1. The Syntax Struggler (Compilation Errors)
These files will **not compile**.
- **Adams:** Missing semicolon, lowercase `system`.
- **Brooks:** Missing closing brace `}`, lowercase `string`.
- **Campbell:** Missing import, assignment in `if`.
- **Davis:** Capitalized `Else`.

### 2. The Logic Fumbler (Runtime/Logic Errors)
These files compile but produce **incorrect results**.
- **Edwards:** Integer division `1/4` results in 0 surcharge.
- **Flores:** Boolean logic `&&` instead of `||` for eligibility.
- **Green:** Calculates accident surcharge on base price only, ignoring age surcharge.
- **Hall:** Off-by-one error `age <= 24`.
- **Inoue:** Missing `return` after printing "Ineligible", continues execution.
- **Jackson:** Adds `$25` instead of `25%`.

### 3. The Style Disaster (Messy but Functional)
These files work but are **painful to read**.
- **Kim:** One-liner code, no indentation.
- **Lewis:** Variable names `a`, `b`, `c`, `d`.
- **Morgan:** Excessive comments and TODOs.
- **Nguyen:** Weird spacing `if( age<18 )` and hardcoded values.
- **Ortiz:** Mixed brace styles and random newlines.

### 4. The Over-Complicator (Weird Valid Solutions)
These files work but use **bizarre approaches**.
- **Patel:** Uses `int[]` array for inputs.
- **Quinn:** Uses a static inner `Calculator` class.
- **Rivera:** Parses inputs from `String`.
- **Smith:** Nested `if/else` hell.
- **Thompson:** Uses `while(true)` loop with `break`.

### 5. The Scanner Victim (Input Issues)
These files struggle with `java.util.Scanner`.
- **Underwood:** Mixes `nextInt()` and `nextLine()` incorrectly.
- **Vasquez:** Creates a `new Scanner` for every input.
- **Walker:** Closes `Scanner` too early.

### 6. The Golden Standard (Control Group)
The remaining 3 students (`Xu`, `Young`, `Zhang`) remain as "perfect" submissions to serve as a control group.

## Verification Results

- **Adams (Syntax Struggler):** `javac` failed with `illegal start of expression`.
- **Xu (Golden Standard):** `javac` passed successfully.

## Next Steps
You can now run your grading pipeline against this dataset. You should expect:
- ~15% of submissions to fail compilation.
- ~25% to fail logic tests.
- ~10% to crash on input (Scanner issues).
- The rest to pass, but with varying degrees of code quality.
