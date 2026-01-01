#!/bin/bash
# Test runner for A2 JUnit tests
# Usage: ./run_tests.sh <question> <student_java_file>
# Example: ./run_tests.sh Q1 /path/to/student/Q1.java

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LIB_DIR="$SCRIPT_DIR/lib"
SRC_DIR="$SCRIPT_DIR/src"
BUILD_DIR="$SCRIPT_DIR/build"
JUNIT_JAR="$LIB_DIR/junit-platform-console-standalone-1.10.0.jar"

QUESTION=$1
STUDENT_FILE=$2

if [ -z "$QUESTION" ] || [ -z "$STUDENT_FILE" ]; then
    echo "Usage: $0 <question> <student_java_file>"
    echo "Example: $0 Q1 /path/to/Q1.java"
    exit 1
fi

# Create build directory
mkdir -p "$BUILD_DIR"

# Clean previous builds
rm -rf "$BUILD_DIR"/*

# Copy student file to build directory with correct name
cp "$STUDENT_FILE" "$BUILD_DIR/${QUESTION}.java"

# Copy the test file
cp "$SRC_DIR/${QUESTION}Test.java" "$BUILD_DIR/"

# Compile student code and test
echo "Compiling ${QUESTION}..."
javac -cp "$JUNIT_JAR" -d "$BUILD_DIR" "$BUILD_DIR/${QUESTION}.java" "$BUILD_DIR/${QUESTION}Test.java"

# Run tests
echo "Running tests for ${QUESTION}..."
java -jar "$JUNIT_JAR" --class-path "$BUILD_DIR" --select-class "${QUESTION}Test" --details=tree

echo "Done!"
