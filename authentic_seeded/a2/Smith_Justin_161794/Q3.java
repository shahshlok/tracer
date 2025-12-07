import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer value
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate math variables for the grade boundaries
        int aGradeLowerBound = 90;
        int bGradeLowerBound = 80;
        int cGradeLowerBound = 70;
        int dGradeLowerBound = 60;

        // Declare a variable to store the resulting letter grade
        String calculatedLetterGrade;

        // Use if-else logic to determine the letter grade based on numeric ranges
        if (userInputNumericGrade >= aGradeLowerBound) {
            // If the grade is 90 or higher, it is an A
            calculatedLetterGrade = "A";
        } else if (userInputNumericGrade >= bGradeLowerBound) {
            // If the grade is at least 80 but less than 90, it is a B
            calculatedLetterGrade = "B";
        } else if (userInputNumericGrade >= cGradeLowerBound) {
            // If the grade is at least 70 but less than 80, it is a C
            calculatedLetterGrade = "C";
        } else if (userInputNumericGrade >= dGradeLowerBound) {
            // If the grade is at least 60 but less than 70, it is a D
            calculatedLetterGrade = "D";
        } else {
            // If the grade is below 60, it is an F
            calculatedLetterGrade = "F";
        }

        // Print the final letter grade result
        System.out.println("Letter grade: " + calculatedLetterGrade);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}