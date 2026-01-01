import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate variables to represent the numeric ranges
        int lowerBoundA = 90;
        int upperBoundA = 100;
        int lowerBoundB = 80;
        int upperBoundB = 89;
        int lowerBoundC = 70;
        int upperBoundC = 79;
        int lowerBoundD = 60;
        int upperBoundD = 69;
        int lowerBoundF = 0;
        int upperBoundF = 59;

        // Declare a variable to store the resulting letter grade
        char resultingLetterGrade;

        // Use if-else logic to determine which interval the grade falls into
        if (userInputNumericGrade >= lowerBoundA && userInputNumericGrade <= upperBoundA) {
            // Grade is between 90 and 100, inclusive
            resultingLetterGrade = 'A';
        } else if (userInputNumericGrade >= lowerBoundB && userInputNumericGrade <= upperBoundB) {
            // Grade is between 80 and 89, inclusive
            resultingLetterGrade = 'B';
        } else if (userInputNumericGrade >= lowerBoundC && userInputNumericGrade <= upperBoundC) {
            // Grade is between 70 and 79, inclusive
            resultingLetterGrade = 'C';
        } else if (userInputNumericGrade >= lowerBoundD && userInputNumericGrade <= upperBoundD) {
            // Grade is between 60 and 69, inclusive
            resultingLetterGrade = 'D';
        } else {
            // Grade is below 60, this includes all values less than or equal to upperBoundF
            resultingLetterGrade = 'F';
        }

        // Print out the corresponding letter grade
        System.out.println("Letter grade: " + resultingLetterGrade);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}