import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the integer grade from the user
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate variables for boundaries (math-style)
        int boundaryA = 90;
        int boundaryB = 80;
        int boundaryC = 70;
        int boundaryD = 60;

        // Declare a character variable to store the resulting letter grade
        char computedLetterGrade;

        // Use if-else logic to map the numeric grade to a letter grade
        if (userInputGrade >= boundaryA && userInputGrade <= 100) {
            // Grade is between 90 and 100, inclusive
            computedLetterGrade = 'A';
        } else if (userInputGrade >= boundaryB && userInputGrade <= 89) {
            // Grade is between 80 and 89, inclusive
            computedLetterGrade = 'B';
        } else if (userInputGrade >= boundaryC && userInputGrade <= 79) {
            // Grade is between 70 and 79, inclusive
            computedLetterGrade = 'C';
        } else if (userInputGrade >= boundaryD && userInputGrade <= 69) {
            // Grade is between 60 and 69, inclusive
            computedLetterGrade = 'D';
        } else {
            // Grade is below 60, or outside the usual range
            computedLetterGrade = 'F';
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + computedLetterGrade);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}