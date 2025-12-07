import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate math variables for clarity
        int lowerBoundA = 90;
        int upperBoundA = 100;

        int lowerBoundB = 80;
        int upperBoundB = 89;

        int lowerBoundC = 70;
        int upperBoundC = 79;

        int lowerBoundD = 60;
        int upperBoundD = 69;

        int failingThreshold = 60;

        // Declare a variable to store the resulting letter grade
        char resultingLetterGrade;

        // Use if-else logic to determine the correct letter grade
        if (userInputNumericGrade >= lowerBoundA && userInputNumericGrade <= upperBoundA) {
            // If the grade is between 90 and 100, it is an A
            resultingLetterGrade = 'A';
        } else if (userInputNumericGrade >= lowerBoundB && userInputNumericGrade <= upperBoundB) {
            // If the grade is between 80 and 89, it is a B
            resultingLetterGrade = 'B';
        } else if (userInputNumericGrade >= lowerBoundC && userInputNumericGrade <= upperBoundC) {
            // If the grade is between 70 and 79, it is a C
            resultingLetterGrade = 'C';
        } else if (userInputNumericGrade >= lowerBoundD && userInputNumericGrade <= upperBoundD) {
            // If the grade is between 60 and 69, it is a D
            resultingLetterGrade = 'D';
        } else if (userInputNumericGrade < failingThreshold) {
            // If the grade is below 60, it is an F
            resultingLetterGrade = 'F';
        } else {
            // If the grade is outside 0-100, handle it as an F by default
            resultingLetterGrade = 'F';
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + resultingLetterGrade);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}