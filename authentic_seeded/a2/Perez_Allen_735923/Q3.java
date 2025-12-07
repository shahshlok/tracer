import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide the letter grade using if-else statements
        char letterGradeValue;

        // Step 5: Check if grade is between 90 and 100 (inclusive) for A
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeValue = 'A';
        }
        // Step 6: Check if grade is between 80 and 89 for B
        else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeValue = 'B';
        }
        // Step 7: Check if grade is between 70 and 79 for C
        else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeValue = 'C';
        }
        // Step 8: Check if grade is between 60 and 69 for D
        else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeValue = 'D';
        }
        // Step 9: Any grade below 60 is an F
        else {
            letterGradeValue = 'F';
        }

        // Step 10: Print the letter grade result
        System.out.println("Letter grade: " + letterGradeValue);

        // Step 11: Close the Scanner
        userInputScanner.close();
    }
}