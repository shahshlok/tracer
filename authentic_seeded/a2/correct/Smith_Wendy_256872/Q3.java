import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer value
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate variables for grade boundaries
        int boundaryAStart = 90;  // Start of A range (inclusive)
        int boundaryBStart = 80;  // Start of B range (inclusive)
        int boundaryCStart = 70;  // Start of C range (inclusive)
        int boundaryDStart = 60;  // Start of D range (inclusive)

        // Declare a variable to store the final letter grade
        char finalLetterGrade;

        // Use if-else statements to determine the letter grade based on numeric ranges
        if (userInputGrade >= boundaryAStart) {
            // Grade is 90-100, so letter grade is A
            finalLetterGrade = 'A';
        } else if (userInputGrade >= boundaryBStart) {
            // Grade is 80-89, so letter grade is B
            finalLetterGrade = 'B';
        } else if (userInputGrade >= boundaryCStart) {
            // Grade is 70-79, so letter grade is C
            finalLetterGrade = 'C';
        } else if (userInputGrade >= boundaryDStart) {
            // Grade is 60-69, so letter grade is D
            finalLetterGrade = 'D';
        } else {
            // Grade is below 60, so letter grade is F
            finalLetterGrade = 'F';
        }

        // Print the resulting letter grade
        System.out.println("Letter grade: " + finalLetterGrade);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}