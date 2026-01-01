import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read from standard input (keyboard)
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the grade
        System.out.print("Enter grade: ");

        // Read the numeric grade from the user
        int userInputNumericGrade = userInputScanner.nextInt();

        // We will check that the grade is within 0 to 100 range
        // Even though the problem does not say what to do otherwise,
        // we will still do basic validation and then apply the scale.
        if (userInputNumericGrade < 0) {
            // If grade is less than 0, we cap it at 0 to avoid weird values
            int adjustedNumericGrade = 0;
            userInputNumericGrade = adjustedNumericGrade;
        }

        if (userInputNumericGrade > 100) {
            // If grade is greater than 100, we cap it at 100
            int adjustedNumericGrade = 100;
            userInputNumericGrade = adjustedNumericGrade;
        }

        // Now we apply the grading scale very carefully
        // I will check each range separately using independent if statements

        // First check if it is in the A range (90-100)
        if (userInputNumericGrade >= 90) {
            System.out.println("Letter grade: A");
        }

        // Then check if it is in the B range (80-89)
        if (userInputNumericGrade >= 80) {
            System.out.println("Letter grade: B");
        }

        // Then check if it is in the C range (70-79)
        if (userInputNumericGrade >= 70) {
            System.out.println("Letter grade: C");
        }

        // Then check if it is in the D range (60-69)
        if (userInputNumericGrade >= 60) {
            System.out.println("Letter grade: D");
        }

        // Finally check if it is in the F range (below 60)
        if (userInputNumericGrade < 60) {
            System.out.println("Letter grade: F");
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}