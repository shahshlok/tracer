import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade from the user
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide the letter grade based on the numeric grade
        char letterGradeValue;

        // Step 5: Check if grade is between 90 and 100 for an A
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeValue = 'A';
        }
        // Step 6: Check if grade is between 80 and 89 for a B
        else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeValue = 'B';
        }
        // Step 7: Check if grade is between 70 and 79 for a C
        else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeValue = 'C';
        }
        // Step 8: Check if grade is between 60 and 69 for a D
        else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeValue = 'D';
        }
        // Step 9: If none of the above, the grade is below 60 and is an F
        else {
            letterGradeValue = 'F';
        }

        // Step 10: Print the letter grade result
        System.out.println("Letter grade: " + letterGradeValue);

        // Step 11: Close the scanner
        userInputScanner.close();
    }
}