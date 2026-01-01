import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide the letter grade based on the numeric grade
        String letterGradeResult;

        // Step 5: Check if the grade is in the A range (90-100)
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeResult = "A";
        }
        // Step 6: Check if the grade is in the B range (80-89)
        else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeResult = "B";
        }
        // Step 7: Check if the grade is in the C range (70-79)
        else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeResult = "C";
        }
        // Step 8: Check if the grade is in the D range (60-69)
        else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeResult = "D";
        }
        // Step 9: All other grades below 60 are F
        else {
            letterGradeResult = "F";
        }

        // Step 10: Print the letter grade result
        System.out.println("Letter grade: " + letterGradeResult);

        // Step 11: Close the Scanner
        userInputScanner.close();
    }
}