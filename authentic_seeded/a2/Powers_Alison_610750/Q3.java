import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Declare intermediate variables for boundary calculations
        int boundaryA = 90;
        int boundaryB = 80;
        int boundaryC = 70;
        int boundaryD = 60;

        // Declare a variable for the resulting letter grade
        char letterGradeResult;

        // Use if-else statements to determine the letter grade based on numeric grade
        if (numericGradeValue >= boundaryA && numericGradeValue <= 100) {
            // Numeric grade is between 90 and 100, so it is an A
            letterGradeResult = 'A';
        } else if (numericGradeValue >= boundaryB && numericGradeValue <= 89) {
            // Numeric grade is between 80 and 89, so it is a B
            letterGradeResult = 'B';
        } else if (numericGradeValue >= boundaryC && numericGradeValue <= 79) {
            // Numeric grade is between 70 and 79, so it is a C
            letterGradeResult = 'C';
        } else if (numericGradeValue >= boundaryD && numericGradeValue <= 69) {
            // Numeric grade is between 60 and 69, so it is a D
            letterGradeResult = 'D';
        } else {
            // Numeric grade is below 60, so it is an F
            letterGradeResult = 'F';
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}