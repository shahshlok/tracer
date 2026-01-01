import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int numericGradeInput = keyboardInputScanner.nextInt();

        // Declare intermediate math variables to think about boundary values
        int lowerBoundaryA = 90;
        int lowerBoundaryB = 80;
        int lowerBoundaryC = 70;
        int lowerBoundaryD = 60;

        // I will use separate if statements because each rule is independent
        // If grade is 90 or above, the letter grade is A
        if (numericGradeInput >= lowerBoundaryA) {
            System.out.println("Letter grade: A");
        }

        // If grade is 80 or above, the letter grade is B
        if (numericGradeInput >= lowerBoundaryB) {
            System.out.println("Letter grade: B");
        }

        // If grade is 70 or above, the letter grade is C
        if (numericGradeInput >= lowerBoundaryC) {
            System.out.println("Letter grade: C");
        }

        // If grade is 60 or above, the letter grade is D
        if (numericGradeInput >= lowerBoundaryD) {
            System.out.println("Letter grade: D");
        }

        // If grade is below 60, the letter grade is F
        if (numericGradeInput < lowerBoundaryD) {
            System.out.println("Letter grade: F");
        }

        // Close the Scanner to free system resources
        keyboardInputScanner.close();
    }
}