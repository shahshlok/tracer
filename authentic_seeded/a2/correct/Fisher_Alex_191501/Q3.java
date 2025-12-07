import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardScanner.nextInt();

        // Close the scanner to avoid resource leaks
        keyboardScanner.close();

        // Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // We will be very careful with ranges and edge cases

        // First, ensure the grade is within a reasonable range 0-100
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {
            // Temporary holder variable to make decisions easier to read
            int gradeValueHolder = numericGradeInput;

            // Check for A range: 90 to 100
            if (gradeValueHolder >= 90 && gradeValueHolder <= 100) {
                letterGradeResult = "A";
            } else if (gradeValueHolder >= 80 && gradeValueHolder <= 89) {
                // Check for B range: 80 to 89
                letterGradeResult = "B";
            } else if (gradeValueHolder >= 70 && gradeValueHolder <= 79) {
                // Check for C range: 70 to 79
                letterGradeResult = "C";
            } else if (gradeValueHolder >= 60 && gradeValueHolder <= 69) {
                // Check for D range: 60 to 69
                letterGradeResult = "D";
            } else {
                // If it is not A, B, C, or D within 0-100, it must be below 60, so F
                letterGradeResult = "F";
            }
        } else {
            // If the input is outside 0-100, we will still handle it,
            // but here we decide to treat anything below 60 as F,
            // and anything above 100 as A, just to be explicit.
            // However, the assignment focuses on 0-100, so this is just extra caution.
            if (numericGradeInput < 60) {
                letterGradeResult = "F";
            } else {
                letterGradeResult = "A";
            }
        }

        // Print the corresponding letter grade
        System.out.println("Letter grade: " + letterGradeResult);
    }
}