import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Create a variable to hold the letter grade result, even though we will print directly
        String letterGradeResult = "";

        // First, check that the numeric grade is within a reasonable range 0-100
        // Even though the problem says 0-100, we will still check for safety
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {
            // I will use a temporary variable to hold the grade while checking
            int temporaryGradeHolder = numericGradeInput;

            // Check if the grade is at least 90 for an A
            if (temporaryGradeHolder >= 90) {
                letterGradeResult = "A";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 80 for a B
            if (temporaryGradeHolder >= 80) {
                letterGradeResult = "B";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 70 for a C
            if (temporaryGradeHolder >= 70) {
                letterGradeResult = "C";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 60 for a D
            if (temporaryGradeHolder >= 60) {
                letterGradeResult = "D";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is less than 60 for an F
            if (temporaryGradeHolder < 60) {
                letterGradeResult = "F";
                System.out.println("Letter grade: " + letterGradeResult);
            }

        } else {
            // If the grade is out of the 0-100 range, we will be cautious
            // and still give an F, but we will go through the same style
            int temporaryGradeHolder = numericGradeInput;

            // Check if the grade is at least 90 for an A
            if (temporaryGradeHolder >= 90) {
                letterGradeResult = "A";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 80 for a B
            if (temporaryGradeHolder >= 80) {
                letterGradeResult = "B";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 70 for a C
            if (temporaryGradeHolder >= 70) {
                letterGradeResult = "C";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is at least 60 for a D
            if (temporaryGradeHolder >= 60) {
                letterGradeResult = "D";
                System.out.println("Letter grade: " + letterGradeResult);
            }

            // Check if the grade is less than 60 for an F
            if (temporaryGradeHolder < 60) {
                letterGradeResult = "F";
                System.out.println("Letter grade: " + letterGradeResult);
            }
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}