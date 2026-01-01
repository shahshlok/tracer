import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer from the user
        int userInputNumericGrade = userInputScanner.nextInt();

        // Step 4: Create a variable to hold the letter grade result
        char letterGradeResult;

        // Step 5: Use if-else statements to determine the letter grade based on the numeric grade
        if (userInputNumericGrade >= 90 && userInputNumericGrade <= 100) {
            // If the grade is between 90 and 100, it is an A
            letterGradeResult = 'A';
        } else if (userInputNumericGrade >= 80 && userInputNumericGrade <= 89) {
            // If the grade is between 80 and 89, it is a B
            letterGradeResult = 'B';
        } else if (userInputNumericGrade >= 70 && userInputNumericGrade <= 79) {
            // If the grade is between 70 and 79, it is a C
            letterGradeResult = 'C';
        } else if (userInputNumericGrade >= 60 && userInputNumericGrade <= 69) {
            // If the grade is between 60 and 69, it is a D
            letterGradeResult = 'D';
        } else {
            // If the grade is below 60, it is an F
            letterGradeResult = 'F';
        }

        // Step 6: Print the resulting letter grade to the screen
        System.out.println("Letter grade: " + letterGradeResult);

        // Step 7: Close the Scanner to free system resources
        userInputScanner.close();
    }
}