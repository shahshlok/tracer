import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade from the user
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Create a variable to hold the letter grade result
        String letterGradeResult = "";

        // Step 5: Use if-else statements to decide which letter grade matches the numeric grade
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            // Numeric grade is between 90 and 100, so it is an A
            letterGradeResult = "A";
        } else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            // Numeric grade is between 80 and 89, so it is a B
            letterGradeResult = "B";
        } else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            // Numeric grade is between 70 and 79, so it is a C
            letterGradeResult = "C";
        } else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            // Numeric grade is between 60 and 69, so it is a D
            letterGradeResult = "D";
        } else {
            // Numeric grade is below 60, so it is an F
            letterGradeResult = "F";
        }

        // Step 6: Print out the final letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Step 7: Close the Scanner because we are done using it
        userInputScanner.close();
    }
}