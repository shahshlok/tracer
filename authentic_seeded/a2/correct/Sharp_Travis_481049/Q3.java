import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade from the user
        int userInputGrade = keyboardInputScanner.nextInt();

        // We will use math-style variables to help understand the ranges
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // Initialize a variable to store the resulting letter grade as a String
        String resultingLetterGrade;

        // Use if-else statements to determine which letter grade the numeric grade belongs to
        if (userInputGrade >= lowerBoundA && userInputGrade <= 100) {
            // If the grade is between 90 and 100 inclusive, it is an A
            resultingLetterGrade = "A";
        } else if (userInputGrade >= lowerBoundB && userInputGrade <= 89) {
            // If the grade is between 80 and 89 inclusive, it is a B
            resultingLetterGrade = "B";
        } else if (userInputGrade >= lowerBoundC && userInputGrade <= 79) {
            // If the grade is between 70 and 79 inclusive, it is a C
            resultingLetterGrade = "C";
        } else if (userInputGrade >= lowerBoundD && userInputGrade <= 69) {
            // If the grade is between 60 and 69 inclusive, it is a D
            resultingLetterGrade = "D";
        } else {
            // If the grade is below 60, it is an F
            resultingLetterGrade = "F";
        }

        // Print the final letter grade result
        System.out.println("Letter grade: " + resultingLetterGrade);

        // Close the Scanner to be polite to system resources
        keyboardInputScanner.close();
    }
}