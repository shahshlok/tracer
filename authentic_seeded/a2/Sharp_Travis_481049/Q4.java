import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read the user input value from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the triangle height N from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will print triangleHeight rows
        // Each rowNumber will go from 1 up to triangleHeight
        int rowNumber;
        for (rowNumber = 1; rowNumber < triangleHeight; rowNumber++) {

            // For each rowNumber, we need to print rowNumber many asterisks
            // Let a represent the number of stars to print on this row
            int a = rowNumber;

            // Use a loop to print a asterisks on this row
            int starCounter;
            for (starCounter = 1; starCounter < a; starCounter++) {
                System.out.print("*");
            }

            // Move to the next line after printing all asterisks for this row
            System.out.println();
        }

        // Close the scanner to be neat
        userInputScanner.close();
    }
}