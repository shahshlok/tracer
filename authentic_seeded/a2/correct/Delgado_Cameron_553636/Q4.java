import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the integer value for the height
        int userInputHeight = userInputScanner.nextInt();

        // Temporary variable to hold the validated height
        int validatedHeight = userInputHeight;

        // Check if the user entered a non-positive number
        // If height is less than or equal to 0, do not print any stars
        if (validatedHeight > 0) {
            // Outer loop to handle the number of rows
            int currentRowNumber = 1;
            while (currentRowNumber <= validatedHeight) {
                // For each row, we need to print a certain number of asterisks
                int numberOfAsterisksToPrint = currentRowNumber;

                // Inner loop to print the correct number of asterisks on this row
                int currentAsteriskCount = 0;
                while (currentAsteriskCount < numberOfAsterisksToPrint) {
                    // Print one asterisk without moving to the next line
                    System.out.print("*");

                    // Increase the asterisk count
                    currentAsteriskCount = currentAsteriskCount + 1;
                }

                // After printing all asterisks for this row, move to the next line
                System.out.println();

                // Move to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}