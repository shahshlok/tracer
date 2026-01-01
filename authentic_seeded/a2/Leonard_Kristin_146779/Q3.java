import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate variables to represent the numeric ranges
        int lowerBoundA = 90;
        int upperBoundA = 100;
        int lowerBoundB = 80;
        int upperBoundB = 89;
        int lowerBoundC = 70;
        int upperBoundC = 79;
        int lowerBoundD = 60;
        int upperBoundD = 69;
        int lowerBoundF = 0;
        int upperBoundF = 59;

        // Declare a variable to store the resulting letter grade (not used directly in this version)
        char resultingLetterGrade;

        // Use if-else logic to determine which interval the grade falls into and print directly
        if (userInputNumericGrade >= lowerBoundA)
            System.out.println("Letter grade: A");
        else if (userInputNumericGrade >= lowerBoundB)
            System.out.println("Letter grade: B");
        else if (userInputNumericGrade >= lowerBoundC)
            System.out.println("Letter grade: C");
        else if (userInputNumericGrade >= lowerBoundD)
            if (userInputNumericGrade >= 65)
                System.out.println("Letter grade: D");
            else
                System.out.println("Letter grade: F");

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}