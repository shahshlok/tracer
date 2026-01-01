import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardInputScanner.nextInt();

        // Declare intermediate math variables to think about boundary values
        int lowerBoundaryA = 90;
        int lowerBoundaryB = 80;
        int lowerBoundaryC = 70;
        int lowerBoundaryD = 60;

        // Declare a variable to store the final letter grade as a String
        String finalLetterGradeResult;

        // Use if-else conditions to determine the letter grade based on the numeric range
        if (numericGradeInput >= lowerBoundaryA && numericGradeInput <= 100) {
            // If grade is between 90 and 100, the letter grade is A
            finalLetterGradeResult = "A";
        } else if (numericGradeInput >= lowerBoundaryB && numericGradeInput <= 89) {
            // If grade is between 80 and 89, the letter grade is B
            finalLetterGradeResult = "B";
        } else if (numericGradeInput >= lowerBoundaryC && numericGradeInput <= 79) {
            // If grade is between 70 and 79, the letter grade is C
            finalLetterGradeResult = "C";
        } else if (numericGradeInput >= lowerBoundaryD && numericGradeInput <= 69) {
            // If grade is between 60 and 69, the letter grade is D
            finalLetterGradeResult = "D";
        } else {
            // If grade is below 60, the letter grade is F
            finalLetterGradeResult = "F";
        }

        // Print the resulting letter grade exactly in the desired format
        System.out.println("Letter grade: " + finalLetterGradeResult);

        // Close the Scanner to free system resources
        keyboardInputScanner.close();
    }
}