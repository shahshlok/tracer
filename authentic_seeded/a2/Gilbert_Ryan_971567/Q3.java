import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Declare intermediate math variables for the lower bounds of each grade range
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // Declare a variable to store the resulting letter grade
        String letterGradeResult;

        // Use if-else statements to determine the letter grade based on the numeric grade
        if (numericGradeInput >= lowerBoundA) {
            // If the grade is 90 or above, it is an A
            letterGradeResult = "A";
        } else if (numericGradeInput >= lowerBoundB) {
            // If the grade is at least 80 but less than 90, it is a B
            letterGradeResult = "B";
        } else if (numericGradeInput >= lowerBoundC) {
            // If the grade is at least 70 but less than 80, it is a C
            letterGradeResult = "C";
        } else if (numericGradeInput >= lowerBoundD) {
            // If the grade is at least 60 but less than 70, it is a D
            letterGradeResult = "D";
        } else {
            // If the grade is below 60, it is an F
            letterGradeResult = "F";
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}