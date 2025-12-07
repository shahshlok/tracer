import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // First, check that the numeric grade is within a reasonable range 0-100
        // Even though the problem says 0-100, we will still check for safety
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {

            // Use if-else statements to determine the letter grade
            if (numericGradeInput >= 90 && numericGradeInput <= 100) {
                // Grade is between 90 and 100, so it is an A
                letterGradeResult = "A";
            } else if (numericGradeInput >= 80 && numericGradeInput <= 89) {
                // Grade is between 80 and 89, so it is a B
                letterGradeResult = "B";
            } else if (numericGradeInput >= 70 && numericGradeInput <= 79) {
                // Grade is between 70 and 79, so it is a C
                letterGradeResult = "C";
            } else if (numericGradeInput >= 60 && numericGradeInput <= 69) {
                // Grade is between 60 and 69, so it is a D
                letterGradeResult = "D";
            } else {
                // Grade is below 60 but still 0 or higher, so it is an F
                // We can add an explicit check just to be safe
                if (numericGradeInput < 60 && numericGradeInput >= 0) {
                    letterGradeResult = "F";
                } else {
                    // This else should not normally happen because of the outer range check
                    letterGradeResult = "F";
                }
            }

            // Output the letter grade
            System.out.println("Letter grade: " + letterGradeResult);

        } else {
            // If the grade is out of the 0-100 range, we can still handle it
            // For this assignment, we can treat out-of-range as F or show a message
            // To stay simple but cautious, we will print F and a note
            letterGradeResult = "F";
            System.out.println("Letter grade: " + letterGradeResult);
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}