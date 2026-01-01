import java.util.Scanner;

public class Q3 {

    public static void main(String[] args) {

        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // Extra safety check: make sure the grade is within a typical 0-100 range
        // Even though the problem statement assumes 0-100, I am nervous about edge cases
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {

            // Temporary holder for checking the grade
            int numericGradeHolder = numericGradeInput;

            // Check for grade A (90-100)
            if (numericGradeHolder >= 90 && numericGradeHolder <= 100) {
                letterGradeResult = "A";
            } else {
                // Check for grade B (80-89)
                if (numericGradeHolder >= 80 && numericGradeHolder <= 89) {
                    letterGradeResult = "B";
                } else {
                    // Check for grade C (70-79)
                    if (numericGradeHolder >= 70 && numericGradeHolder <= 79) {
                        letterGradeResult = "C";
                    } else {
                        // Check for grade D (60-69)
                        if (numericGradeHolder >= 60 && numericGradeHolder <= 69) {
                            letterGradeResult = "D";
                        } else {
                            // Anything below 60 is an F
                            if (numericGradeHolder < 60) {
                                letterGradeResult = "F";
                            }
                        }
                    }
                }
            }

        } else {
            // If the grade is out of range, just handle it as F to stay within the letter system
            // This is a conservative choice because the assignment only defines 0-100
            letterGradeResult = "F";
        }

        // Extra check to make sure letterGradeResult is not an empty string
        if (letterGradeResult.equals("")) {
            // If somehow still empty (should not happen), default to F
            letterGradeResult = "F";
        }

        // Print the letter grade result as required
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}