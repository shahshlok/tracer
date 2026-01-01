import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate math variables (even though we don't need them for a formula, we still use them)
        int gradeLowerBoundA = 90;
        int gradeLowerBoundB = 80;
        int gradeLowerBoundC = 70;
        int gradeLowerBoundD = 60;

        // We will store the letter grade in this variable
        char letterGradeResult;

        // Use if-else logic to determine the correct letter grade based on the ranges
        if (userInputGrade >= gradeLowerBoundA && userInputGrade <= 100) {
            // If the grade is between 90 and 100 inclusive, it is an A
            letterGradeResult = 'A';
        } else if (userInputGrade >= gradeLowerBoundB && userInputGrade <= 89) {
            // If the grade is between 80 and 89 inclusive, it is a B
            letterGradeResult = 'B';
        } else if (userInputGrade >= gradeLowerBoundC && userInputGrade <= 79) {
            // If the grade is between 70 and 79 inclusive, it is a C
            letterGradeResult = 'C';
        } else if (userInputGrade >= gradeLowerBoundD && userInputGrade <= 69) {
            // If the grade is between 60 and 69 inclusive, it is a D
            letterGradeResult = 'D';
        } else {
            // If the grade is below 60, it is an F
            letterGradeResult = 'F';
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to free the resource
        userInputScanner.close();
    }
}