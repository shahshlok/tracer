import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer from the user
        int userInputNumericGrade = userInputScanner.nextInt();

        // We will use intermediate variables to think of the grade in ranges
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // Declare a variable that will store the final letter grade as text
        String computedLetterGrade;

        // Use if-else statements to determine which interval the grade falls into
        if (userInputNumericGrade >= lowerBoundA) {
            // Grade is 90 or above, so the letter grade is A
            computedLetterGrade = "A";
        } else if (userInputNumericGrade >= lowerBoundB) {
            // Grade is between 80 and 89, so the letter grade is B
            computedLetterGrade = "B";
        } else if (userInputNumericGrade >= lowerBoundC) {
            // Grade is between 70 and 79, so the letter grade is C
            computedLetterGrade = "C";
        } else if (userInputNumericGrade >= lowerBoundD) {
            // Grade is between 60 and 69, so the letter grade is D
            computedLetterGrade = "D";
        } else {
            // Grade is below 60, so the letter grade is F
            computedLetterGrade = "F";
        }

        // Print the resulting letter grade, matching the sample output format
        System.out.println("Letter grade: " + computedLetterGrade);

        // Close the scanner to free the resource
        userInputScanner.close();
    }
}