import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Close the scanner since we do not need more input
        userInputScanner.close();

        // Create a temporary variable to hold the letter grade result as a String
        String letterGradeResult = "";

        // First, check that the numeric grade is within the expected 0-100 range
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {

            // Now determine letter grade based on given scale
            
            // Check for A: 90-100
            if (numericGradeInput >= 90 && numericGradeInput <= 100) {
                letterGradeResult = "A";
            }
            // Check for B: 80-89
            else if (numericGradeInput >= 80 && numericGradeInput <= 89) {
                letterGradeResult = "B";
            }
            // Check for C: 70-79
            else if (numericGradeInput >= 70 && numericGradeInput <= 79) {
                letterGradeResult = "C";
            }
            // Check for D: 60-69
            else if (numericGradeInput >= 60 && numericGradeInput <= 69) {
                letterGradeResult = "D";
            }
            // Anything below 60 within the valid range is an F
            else if (numericGradeInput >= 0 && numericGradeInput < 60) {
                letterGradeResult = "F";
            }

            // Print the resulting letter grade if it is not an empty string
            if (!letterGradeResult.equals("")) {
                System.out.println("Letter grade: " + letterGradeResult);
            } else {
                // This is a safety check; it really should not happen with the above conditions
                System.out.println("Letter grade: Unknown");
            }

        } else {
            // If the grade is out of range, print a message (edge case handling)
            System.out.println("Letter grade: Invalid input");
        }
    }
}