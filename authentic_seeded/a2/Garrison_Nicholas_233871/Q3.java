import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade between 0 and 100
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer value
        int numericGradeValue = userInputScanner.nextInt();

        // Declare variables that represent the boundaries for each letter grade
        int lowerBoundA = 90;
        int upperBoundA = 100;

        int lowerBoundB = 80;
        int upperBoundB = 89;

        int lowerBoundC = 70;
        int upperBoundC = 79;

        int lowerBoundD = 60;
        int upperBoundD = 69;

        int lowerBoundF = 0;
        int upperBoundF = 59;

        // Declare a variable to store the letter grade that we will compute
        char letterGradeValue;

        // Use conditional logic to determine which letter grade matches the numeric grade
        if (numericGradeValue >= lowerBoundA && numericGradeValue <= upperBoundA) {
            // Numeric grade is between 90 and 100, so the letter grade is A
            letterGradeValue = 'A';
        } else if (numericGradeValue >= lowerBoundB && numericGradeValue <= upperBoundB) {
            // Numeric grade is between 80 and 89, so the letter grade is B
            letterGradeValue = 'B';
        } else if (numericGradeValue >= lowerBoundC && numericGradeValue <= upperBoundC) {
            // Numeric grade is between 70 and 79, so the letter grade is C
            letterGradeValue = 'C';
        } else if (numericGradeValue >= lowerBoundD && numericGradeValue <= upperBoundD) {
            // Numeric grade is between 60 and 69, so the letter grade is D
            letterGradeValue = 'D';
        } else {
            // Any numeric grade below 60 is an F
            // We also allow values below the lowerBoundF in this branch
            letterGradeValue = 'F';
        }

        // Print the resulting letter grade to the user
        System.out.println("Letter grade: " + letterGradeValue);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}