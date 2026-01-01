import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardScanner.nextInt();

        // Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // Extra nervous check: make sure the grade is within a typical 0-100 range
        // but still handle values outside the range in a reasonable way
        int safeNumericGrade = numericGradeInput;

        if (safeNumericGrade < 0) {
            // If the grade is less than 0, we will just treat it as 0
            safeNumericGrade = 0;
        }

        if (safeNumericGrade > 100) {
            // If the grade is greater than 100, we will just treat it as 100
            safeNumericGrade = 100;
        }

        // Determine the letter grade based on the (possibly adjusted) numeric grade
        if (safeNumericGrade >= 90 && safeNumericGrade <= 100) {
            // Grade is between 90 and 100 inclusive, so it is an A
            letterGradeResult = "A";
        } else if (safeNumericGrade >= 80 && safeNumericGrade <= 89) {
            // Grade is between 80 and 89 inclusive, so it is a B
            letterGradeResult = "B";
        } else if (safeNumericGrade >= 70 && safeNumericGrade <= 79) {
            // Grade is between 70 and 79 inclusive, so it is a C
            letterGradeResult = "C";
        } else if (safeNumericGrade >= 60 && safeNumericGrade <= 69) {
            // Grade is between 60 and 69 inclusive, so it is a D
            letterGradeResult = "D";
        } else {
            // Grade is below 60, so it is an F
            letterGradeResult = "F";
        }

        // Print out the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to be safe
        keyboardScanner.close();
    }
}