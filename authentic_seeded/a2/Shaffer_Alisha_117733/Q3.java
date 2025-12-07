import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer
        int numericGradeValue = keyboardScanner.nextInt();

        // Step 4: Use separate if statements to decide the letter grade
        // I will check each grade range one by one and print the matching letter

        // Step 5: If grade is 90 or more, it is an A
        if (numericGradeValue >= 90) {
            System.out.println("Letter grade: A");
        }

        // Step 6: If grade is 80 or more, it is a B
        if (numericGradeValue >= 80) {
            System.out.println("Letter grade: B");
        }

        // Step 7: If grade is 70 or more, it is a C
        if (numericGradeValue >= 70) {
            System.out.println("Letter grade: C");
        }

        // Step 8: If grade is 60 or more, it is a D
        if (numericGradeValue >= 60) {
            System.out.println("Letter grade: D");
        }

        // Step 9: If grade is less than 60, it is an F
        if (numericGradeValue < 60) {
            System.out.println("Letter grade: F");
        }

        // Step 10: Close the scanner
        keyboardScanner.close();
    }
}