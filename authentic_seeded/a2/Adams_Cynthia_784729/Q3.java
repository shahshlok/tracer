import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer from the user
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate variables for the numeric thresholds
        int thresholdA = 90; // Minimum grade for an A
        int thresholdB = 80; // Minimum grade for a B
        int thresholdC = 70; // Minimum grade for a C
        int thresholdD = 60; // Minimum grade for a D

        // Declare a variable to store the final letter grade
        char letterGradeResult;

        // Use if-else statements to determine the letter grade
        if (userInputGrade >= thresholdA && userInputGrade <= 100) {
            // If the numeric grade is between 90 and 100, it is an A
            letterGradeResult = 'A';
        } else if (userInputGrade >= thresholdB && userInputGrade < thresholdA) {
            // If the numeric grade is between 80 and 89, it is a B
            letterGradeResult = 'B';
        } else if (userInputGrade >= thresholdC && userInputGrade < thresholdB) {
            // If the numeric grade is between 70 and 79, it is a C
            letterGradeResult = 'C';
        } else if (userInputGrade >= thresholdD && userInputGrade < thresholdC) {
            // If the numeric grade is between 60 and 69, it is a D
            letterGradeResult = 'D';
        } else {
            // If the numeric grade is below 60, it is an F
            letterGradeResult = 'F';
        }

        // Print the final letter grade result
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to free up resources
        userInputScanner.close();
    }
}