import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Determine the letter grade based on the numeric grade
        String letterGradeValue;

        // Step 5: Use if-else statements to decide which letter grade to assign
        if (numericGradeValue >= 90 && numericGradeValue <= 100) {
            // If grade is between 90 and 100, it is an A
            letterGradeValue = "A";
        } else if (numericGradeValue >= 80 && numericGradeValue <= 89) {
            // If grade is between 80 and 89, it is a B
            letterGradeValue = "B";
        } else if (numericGradeValue >= 70 && numericGradeValue <= 79) {
            // If grade is between 70 and 79, it is a C
            letterGradeValue = "C";
        } else if (numericGradeValue >= 60 && numericGradeValue <= 69) {
            // If grade is between 60 and 69, it is a D
            letterGradeValue = "D";
        } else {
            // If grade is below 60, it is an F
            letterGradeValue = "F";
        }

        // Step 6: Print out the letter grade result
        System.out.println("Letter grade: " + letterGradeValue);

        // Step 7: Close the Scanner because we are done using it
        userInputScanner.close();
    }
}