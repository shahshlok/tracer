import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // Create a variable to hold the letter grade as a String
        String letterGradeResult = "";

        // Extra safety: check that the numeric grade is within 0 to 100
        // Even though the assignment does not say what to do otherwise,
        // I will still handle it by treating out-of-range as F, to be safe.
        boolean isGradeInValidRange = (numericGradeInput >= 0) && (numericGradeInput <= 100);

        if (isGradeInValidRange) {
            // Check for A grade: 90 to 100
            if (numericGradeInput >= 90 && numericGradeInput <= 100) {
                letterGradeResult = "A";
            } else {
                // Check for B grade: 80 to 89
                if (numericGradeInput >= 80 && numericGradeInput <= 89) {
                    letterGradeResult = "B";
                } else {
                    // Check for C grade: 70 to 79
                    if (numericGradeInput >= 70 && numericGradeInput <= 79) {
                        letterGradeResult = "C";
                    } else {
                        // Check for D grade: 60 to 69
                        if (numericGradeInput >= 60 && numericGradeInput <= 69) {
                            letterGradeResult = "D";
                        } else {
                            // Anything below 60 is an F
                            if (numericGradeInput < 60) {
                                letterGradeResult = "F";
                            }
                        }
                    }
                }
            }
        } else {
            // If not in the valid range, I will still assign F nervously
            // because the problem statement does not specify behavior.
            letterGradeResult = "F";
        }

        // Print the final letter grade result
        System.out.println("Letter grade: " + letterGradeResult);

        // Close the scanner to be tidy
        userInputScanner.close();
    }
}