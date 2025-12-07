import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardInputScanner.nextInt();

        // Close the scanner since we do not need more input
        keyboardInputScanner.close();

        // Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // First, check that the numeric grade is within the range 0 to 100
        if (numericGradeInput >= 0 && numericGradeInput <= 100) {

            // Check if grade is between 90 and 100 for letter A
            if (numericGradeInput >= 90 && numericGradeInput <= 100) {
                letterGradeResult = "A";
            }
            // Check if grade is between 80 and 89 for letter B
            else if (numericGradeInput >= 80 && numericGradeInput <= 89) {
                letterGradeResult = "B";
            }
            // Check if grade is between 70 and 79 for letter C
            else if (numericGradeInput >= 70 && numericGradeInput <= 79) {
                letterGradeResult = "C";
            }
            // Check if grade is between 60 and 69 for letter D
            else if (numericGradeInput >= 60 && numericGradeInput <= 69) {
                letterGradeResult = "D";
            }
            // Otherwise, if it is less than 60 but still >= 0, it is an F
            else if (numericGradeInput < 60 && numericGradeInput >= 0) {
                letterGradeResult = "F";
            }

            // Print the final letter grade result
            // I am checking that letterGradeResult is not empty just in case
            if (!letterGradeResult.equals("")) {
                System.out.println("Letter grade: " + letterGradeResult);
            }

        } else {
            // If the grade is out of range, we will still print something reasonable
            // The assignment does not say exactly what to do here, but I do not want to crash
            System.out.println("Letter grade: Invalid grade");
        }
    }
}