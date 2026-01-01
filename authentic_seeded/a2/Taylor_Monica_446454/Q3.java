import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInputValue = userInputScanner.nextInt();

        // Declare intermediate variables for mathematical thinking (even though we just compare ranges)
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // Declare a variable to store the final letter grade as a String
        String letterGradeResult;

        // Use if-else statements to determine the correct letter grade range
        if (numericGradeInputValue >= lowerBoundA && numericGradeInputValue <= 100) {
            // If the grade is between 90 and 100, it is an A
            letterGradeResult = "A";
        } else if (numericGradeInputValue >= lowerBoundB && numericGradeInputValue <= 89) {
            // If the grade is between 80 and 89, it is a B
            letterGradeResult = "B";
        } else if (numericGradeInputValue >= lowerBoundC && numericGradeInputValue <= 79) {
            // If the grade is between 70 and 79, it is a C
            letterGradeResult = "C";
        } else if (numericGradeInputValue >= lowerBoundD && numericGradeInputValue <= 69) {
            // If the grade is between 60 and 69, it is a D
            letterGradeResult = "D";
        } else {
            // If the grade is below 60, it is an F
            letterGradeResult = "F";
        }

        // Print the resulting letter grade to the user
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to be polite with system resources
        userInputScanner.close();
    }
}