import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read from standard input (keyboard)
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the grade
        System.out.print("Enter grade: ");

        // Read the numeric grade from the user
        int userInputNumericGrade = userInputScanner.nextInt();

        // Create a variable to hold the letter grade
        char calculatedLetterGrade = 'F'; // default to F just in case

        // We will check that the grade is within 0 to 100 range
        // Even though the problem does not say what to do otherwise,
        // we will still do basic validation and then apply the scale.
        if (userInputNumericGrade < 0) {
            // If grade is less than 0, we cap it at 0 to avoid weird values
            int adjustedNumericGrade = 0;
            userInputNumericGrade = adjustedNumericGrade;
        }

        if (userInputNumericGrade > 100) {
            // If grade is greater than 100, we cap it at 100
            int adjustedNumericGrade = 100;
            userInputNumericGrade = adjustedNumericGrade;
        }

        // Now we apply the grading scale very carefully
        // First check if it is in the A range (90-100)
        if (userInputNumericGrade >= 90 && userInputNumericGrade <= 100) {
            calculatedLetterGrade = 'A';
        } else if (userInputNumericGrade >= 80 && userInputNumericGrade <= 89) {
            // B range
            calculatedLetterGrade = 'B';
        } else if (userInputNumericGrade >= 70 && userInputNumericGrade <= 79) {
            // C range
            calculatedLetterGrade = 'C';
        } else if (userInputNumericGrade >= 60 && userInputNumericGrade <= 69) {
            // D range
            calculatedLetterGrade = 'D';
        } else {
            // Anything below 60 is an F
            if (userInputNumericGrade < 60) {
                calculatedLetterGrade = 'F';
            }
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + calculatedLetterGrade);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}