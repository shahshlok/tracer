#!/bin/bash
# Verification script for generated samples

TESTS_DIR="data/a1/tests"
JUNIT_JAR="$TESTS_DIR/lib/junit-platform-console-standalone-1.10.0.jar"

verify_sample() {
    local student=$1
    local question=$2
    local correct_file="authentic_seeded/a1/correct/$student/$question.java"
    local seeded_file="authentic_seeded/a1/$student/$question.java"
    
    echo "=== $student ($question) ==="
    
    # Create temp dir
    tmpdir=$(mktemp -d)
    
    # Test correct code
    echo -n "  Correct compiles: "
    cp "$correct_file" "$tmpdir/"
    if javac "$tmpdir"/*.java 2>/dev/null; then
        echo "YES"
    else
        echo "NO (PROBLEM!)"
        rm -rf "$tmpdir"
        return 1
    fi
    
    # Test seeded code  
    rm "$tmpdir"/*.java "$tmpdir"/*.class 2>/dev/null
    echo -n "  Seeded compiles: "
    cp "$seeded_file" "$tmpdir/"
    if javac "$tmpdir"/*.java 2>/dev/null; then
        echo "YES"
    else
        echo "NO (PROBLEM!)"
        rm -rf "$tmpdir"
        return 1
    fi
    
    rm -rf "$tmpdir"
    echo "  Status: OK"
    echo ""
}

# Verify each sample from manifest
verify_sample "Price_Emily_803353" "Q2"
verify_sample "Vincent_Christian_632939" "Q2"
verify_sample "Romero_Patricia_985563" "Q2"
verify_sample "Lewis_Megan_209615" "Q4"
verify_sample "Garcia_Paul_632923" "Q4"
verify_sample "Watts_Ashley_128156" "Q2"
verify_sample "Graham_Breanna_263850" "Q1"

echo "=== VERIFICATION COMPLETE ==="
