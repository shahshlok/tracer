import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate math variables to structure thinking
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // The letter grade will be stored in this variable
        char letterGradeResult;

        // Use mathematical comparisons to determine the correct letter grade
        if (userInputGrade >= lowerBoundA) {
            // Grade is 90 to 100, so it is an A
            letterGradeResult = 'A';
        } else if (userInputGrade >= lowerBoundB) {
            // Grade is between 80 and 89, so it is a B
            letterGradeResult = 'B';
        } else if (userInputGrade >= lowerBoundC) {
            // Grade is between 70 and 79, so it is a C
            letterGradeResult = 'C';
        } else if (userInputGrade >= lowerBoundD) {
            // Grade is between 60 and 69, so it is a D
            letterGradeResult = 'D';
        } else {
            // Grade is below 60, so it is an F
            letterGradeResult = 'F';
        }

        // Print the final letter grade result
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}