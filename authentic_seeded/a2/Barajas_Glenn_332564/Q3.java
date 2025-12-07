import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide the letter grade based on the numeric grade using if-else statements
        char letterGradeValue;

        // Step 5: Check if the grade is in the A range (90 to 100)
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            letterGradeValue = 'A';
        }
        // Step 6: Check if the grade is in the B range (80 to 89)
        else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            letterGradeValue = 'B';
        }
        // Step 7: Check if the grade is in the C range (70 to 79)
        else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            letterGradeValue = 'C';
        }
        // Step 8: Check if the grade is in the D range (60 to 69)
        else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            letterGradeValue = 'D';
        }
        // Step 9: If none of the above, then the grade is an F (below 60)
        else {
            letterGradeValue = 'F';
        }

        // Step 10: Print out the letter grade result
        System.out.println("Letter grade: " + letterGradeValue);

        // Step 11: Close the scanner to free system resources
        userInputScanner.close();
    }
}