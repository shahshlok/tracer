import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade from the user
        int numericGradeValue = userInputScanner.nextInt();

        // Step 4: Decide and print the letter grade based on the numeric grade

        // Step 5: Check if grade is 90 or higher for an A
        if (numericGradeValue >= 90)
            System.out.println("Letter grade: A");
        // Step 6: Check if grade is 80 or higher for a B
        else if (numericGradeValue >= 80)
            System.out.println("Letter grade: B");
        // Step 7: Check if grade is 70 or higher for a C
        else if (numericGradeValue >= 70)
            System.out.println("Letter grade: C");
        // Step 8: Check if grade is 60 or higher and then check for D or F
        else if (numericGradeValue >= 60)
            if (numericGradeValue >= 65)
                System.out.println("Letter grade: D");
            else
                System.out.println("Letter grade: F");

        // Step 11: Close the scanner
        userInputScanner.close();
    }
}