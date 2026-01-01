import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Step 3: Read the numeric grade as an integer from the user
        int userInputNumericGrade = userInputScanner.nextInt();

        // Step 4: Use if-else statements to determine and print the letter grade based on the numeric grade
        if (userInputNumericGrade >= 90)
            System.out.println("Letter grade: A");
        else if (userInputNumericGrade >= 80)
            System.out.println("Letter grade: B");
        else if (userInputNumericGrade >= 70)
            System.out.println("Letter grade: C");
        else if (userInputNumericGrade >= 60)
            if (userInputNumericGrade >= 65)
                System.out.println("Letter grade: D");
            else
                System.out.println("Letter grade: F");

        // Step 5: Close the Scanner to free system resources
        userInputScanner.close();
    }
}