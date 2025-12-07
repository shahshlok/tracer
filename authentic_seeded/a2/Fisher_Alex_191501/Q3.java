import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardScanner.nextInt();

        // Close the scanner to avoid resource leaks
        keyboardScanner.close();

        // Create a variable to hold the letter grade result even though
        // we will print directly, just to be extra explicit
        String letterGradeResult = "";

        // Temporary holder variable to make decisions easier to read
        int gradeValueHolder = numericGradeInput;

        // I want to be very explicit about the ranges and edge cases,
        // so I will check each possible letter grade with its own rule.
        // I am using separate if statements because I think of each one
        // as its own independent bucket.

        // Check for A range: 90 to 100 (and above, just in case)
        if (gradeValueHolder >= 90) {
            letterGradeResult = "A";
            System.out.println("Letter grade: " + letterGradeResult);
        }

        // Check for B range: 80 to 89 (and above, just in case),
        // as a separate rule
        if (gradeValueHolder >= 80) {
            letterGradeResult = "B";
            System.out.println("Letter grade: " + letterGradeResult);
        }

        // Check for C range: 70 to 79 (and above, just in case),
        // as another separate rule
        if (gradeValueHolder >= 70) {
            letterGradeResult = "C";
            System.out.println("Letter grade: " + letterGradeResult);
        }

        // Check for D range: 60 to 69 (and above, just in case),
        // again as its own rule
        if (gradeValueHolder >= 60) {
            letterGradeResult = "D";
            System.out.println("Letter grade: " + letterGradeResult);
        }

        // Finally, if the grade is below 60, it should be F.
        // I still use an independent if statement here.
        if (gradeValueHolder < 60) {
            letterGradeResult = "F";
            System.out.println("Letter grade: " + letterGradeResult);
        }
    }
}