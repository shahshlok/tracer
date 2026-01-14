# Synthetic Data Generation: Testing/Validation Audit (Conversation Findings)

This note summarizes what we verified about TRACER’s **synthetic data generation validation** during an interactive audit.

## Summary

- TRACER’s dataset generator validates programs using **black-box I/O testing on compiled Java programs** (compile with `javac`, run `main` with fixed stdin, check stdout for expected patterns).
- The repository also contains a **separate JUnit harness** under `data/a{1,2,3}/tests/`, which can test `main` programs by redirecting stdin/stdout, but it is **not** what the generator currently uses.
- When we ran the JUnit harness against `authentic_seeded/`, we found:
  - `a1`: most CLEAN files pass, but some CLEAN files fail due to input parsing assumptions (e.g., `nextInt()` when tests supply decimals).
  - `a2`: CLEAN files pass reliably; SEEDED files fail as expected in most questions (but some SEEDED cases can still pass JUnit depending on the misconception).
  - `a3`: the JUnit harness is incomplete (missing `data/a3/tests/src/Q*Test.java`), so it cannot currently be used to validate `a3`.

## What the generator actually enforces

In `utils/generators/dataset_generator.py`, TRACER enforces:

- **Correct (CLEAN) code**: must compile and pass all internal test cases.
- **Seeded (SEEDED) code**: must compile, differ from the correct solution, and fail **at least one** internal test case.

The internal tests are implemented as black-box stdin/stdout checks:

- Test cases are defined in a `TEST_CASES` structure (stdin strings + expected output substrings).
- Matching is primarily `expected_output in stdout`, with a small float-relaxation and optional forbidden substrings.

This is appropriate for CS1-style submissions written as standalone `main` programs and keeps the benchmark focused on *behavioral correctness* rather than unit-level design.

## JUnit harness: what exists vs what is used

- JUnit-based scripts exist at:
  - `data/a1/tests/run_tests.sh`
  - `data/a2/tests/run_tests.sh`
  - `data/a3/tests/run_tests.sh`
- For `a1` and `a2`, `run_tests.sh` compiles the student `Q*.java` plus `Q*Test.java` and runs the tests using `junit-platform-console-standalone`.
- For `a3`, `run_tests.sh` expects test sources under `data/a3/tests/src/`, but that directory is missing; attempting to run it fails immediately.

## Observations from running JUnit on `authentic_seeded/`

### A1

- `Q1`:
  - CLEAN files passed; SEEDED files failed (as expected).
  - Some failures were `InputMismatchException`, indicating the program read integers while tests supplied decimals.
- `Q4`:
  - Some files labeled CLEAN (by the generator) failed JUnit due to `InputMismatchException`.
  - Example failure: `authentic_seeded/a1/Williams_James_435022/Q4.java` fails `Q4Test.testEquilateralLike` when the test provides a decimal coordinate.

### A2

When executed sequentially (important; see concurrency note below):

- `Q1`: CLEAN passed; SEEDED failed.
- `Q2`: CLEAN passed; SEEDED failed.
- `Q4`: CLEAN passed; SEEDED failed.
- `Q3`: CLEAN passed; some SEEDED files passed (meaning some injected misconceptions may not violate the particular JUnit spec/tests for that question).

### A3

- JUnit harness cannot run because `data/a3/tests/src/Q1Test.java` (and friends) do not exist.

## Concurrency caveat

The `data/a*/tests/run_tests.sh` scripts write into a shared `data/a*/tests/build/` directory and clean it each run.

- Running the JUnit scripts in parallel across many students/questions causes race conditions (build directory clobbering) and produces invalid results.
- Any JUnit-based audit should run **sequentially** or use per-run isolated build directories.

## Implications for the position paper

- It is defensible and accurate to describe TRACER correctness checking as **black-box I/O testing on compiled programs**.
- Avoid claiming that TRACER uses “JUnit” unless you explicitly change the pipeline.
- Treat TRACER as a **controlled benchmark/probe** (proof-of-concept evidence), not a deployment-ready student-modeling evaluation.

## Suggested paper wording (1 sentence)

> TRACER validates generated submissions using black-box I/O testing on compiled Java programs (compile + execute `main` on multiple stdin test cases and check stdout), enabling controlled measurement of belief-attribution recall/specificity under known injected misconceptions.
