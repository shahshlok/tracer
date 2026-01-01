import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer from the user
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Use separate if statements to decide and print the letter grade based on the numeric grade
        if (numericGradeValue >= 90) {
            // If the grade is 90 or higher, it is an A
            System.out.println("Letter grade: A");
        }

        if (numericGradeValue >= 80) {
            // If the grade is 80 or higher, it is a B
            System.out.println("Letter grade: B");
        }

        if (numericGradeValue >= 70) {
            // If the grade is 70 or higher, it is a C
            System.out.println("Letter grade: C");
        }

        if (numericGradeValue >= 60) {
            // If the grade is 60 or higher, it is a D
            System.out.println("Letter grade: D");
        }

        if (numericGradeValue < 60) {
            // If the grade is below 60, it is an F
            System.out.println("Letter grade: F");
        }

        // Step 5: Close the scanner because we are done using it
        userInputScanner.close();
    }
}