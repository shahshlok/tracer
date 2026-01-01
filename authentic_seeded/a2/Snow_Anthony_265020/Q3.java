import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = keyboardScanner.nextInt();

        // Step 4: Decide the letter grade using if-else statements
        String letterGradeValue;

        // Step 5: Check if the grade is an A (90 to 100)
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeValue = "A";
        }
        // Step 6: Check if the grade is a B (80 to 89)
        else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeValue = "B";
        }
        // Step 7: Check if the grade is a C (70 to 79)
        else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeValue = "C";
        }
        // Step 8: Check if the grade is a D (60 to 69)
        else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeValue = "D";
        }
        // Step 9: Anything below 60 is an F
        else {
            letterGradeValue = "F";
        }

        // Step 10: Print the letter grade result
        System.out.println("Letter grade: " + letterGradeValue);

        // Step 11: Close the scanner because we are done using it
        keyboardScanner.close();
    }
}