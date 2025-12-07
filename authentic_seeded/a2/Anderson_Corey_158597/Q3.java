import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide which letter grade matches the numeric grade
        String letterGradeResult;

        // Step 5: Use if-else statements to check the grade range and assign the letter
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeResult = "A";
        } else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeResult = "B";
        } else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeResult = "C";
        } else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeResult = "D";
        } else {
            // If the grade is below 60, it is an F
            letterGradeResult = "F";
        }

        // Step 6: Print the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // Step 7: Close the scanner because we are done with input
        userInputScanner.close();
    }
}