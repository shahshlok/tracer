import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate math variables to reason about the ranges
        int lowerBoundaryA = 90;
        int lowerBoundaryB = 80;
        int lowerBoundaryC = 70;
        int lowerBoundaryD = 60;

        // We will compute the letter grade based on these numeric boundaries
        String computedLetterGrade;

        // If the grade is greater than or equal to 90, it is an A
        if (userInputNumericGrade >= lowerBoundaryA && userInputNumericGrade <= 100) {
            computedLetterGrade = "A";
        }
        // If the grade is between 80 and 89, it is a B
        else if (userInputNumericGrade >= lowerBoundaryB) {
            computedLetterGrade = "B";
        }
        // If the grade is between 70 and 79, it is a C
        else if (userInputNumericGrade >= lowerBoundaryC) {
            computedLetterGrade = "C";
        }
        // If the grade is between 60 and 69, it is a D
        else if (userInputNumericGrade >= lowerBoundaryD) {
            computedLetterGrade = "D";
        }
        // If the grade is below 60, it is an F
        else {
            computedLetterGrade = "F";
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + computedLetterGrade);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}