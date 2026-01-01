import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Create a variable to store the letter grade result
        String letterGradeResult;

        // Step 5: Use if-else statements to determine the letter grade based on the numeric grade
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            // If grade is between 90 and 100, it is an A
            letterGradeResult = "A";
        } else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            // If grade is between 80 and 89, it is a B
            letterGradeResult = "B";
        } else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            // If grade is between 70 and 79, it is a C
            letterGradeResult = "C";
        } else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            // If grade is between 60 and 69, it is a D
            letterGradeResult = "D";
        } else {
            // If grade is below 60, it is an F
            letterGradeResult = "F";
        }

        // Step 6: Print the letter grade result
        System.out.println("Letter grade: " + letterGradeResult);

        // Step 7: Close the Scanner to free system resources
        userInputScanner.close();
    }
}