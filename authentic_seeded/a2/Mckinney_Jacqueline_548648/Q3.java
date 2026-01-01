import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = userInputScanner.nextInt();

        // I will make a temporary variable to hold the grade
        int temporaryNumericGradeHolder = numericGradeInput;

        // Extra safety: check that the numeric grade is within 0 to 100
        // Even though the assignment does not say what to do otherwise,
        // I will still handle it by treating out-of-range as F, to be safe.
        boolean isGradeInValidRange = (temporaryNumericGradeHolder >= 0) && (temporaryNumericGradeHolder <= 100);

        // I am going to check each possible letter grade separately,
        // because I think of them as independent rules.
        if (isGradeInValidRange == true) {
            // Check for A grade: 90 to 100
            if (temporaryNumericGradeHolder >= 90) {
                System.out.println("Letter grade: A");
            }

            // Check for B grade: 80 to 89 (but I will just check >= 80)
            if (temporaryNumericGradeHolder >= 80) {
                System.out.println("Letter grade: B");
            }

            // Check for C grade: 70 to 79 (but I will just check >= 70)
            if (temporaryNumericGradeHolder >= 70) {
                System.out.println("Letter grade: C");
            }

            // Check for D grade: 60 to 69 (but I will just check >= 60)
            if (temporaryNumericGradeHolder >= 60) {
                System.out.println("Letter grade: D");
            }

            // Anything below 60 is an F
            if (temporaryNumericGradeHolder < 60) {
                System.out.println("Letter grade: F");
            }
        }

        // If the grade is not in the valid range, I will still print F
        if (isGradeInValidRange == false) {
            System.out.println("Letter grade: F");
        }

        // Close the scanner to be tidy
        userInputScanner.close();
    }
}