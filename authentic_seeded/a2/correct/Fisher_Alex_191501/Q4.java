import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value as an integer from the user
        int userInputHeight = userInputScanner.nextInt();

        // Close the scanner to avoid any possible resource leaks
        userInputScanner.close();

        // Create a variable to hold the currentRowNumber while we loop
        int currentRowNumber = 0;

        // We want to print from row 1 up to row N (userInputHeight)
        // We add extra explicit checks to be careful about edge cases
        if (userInputHeight > 0) {
            // Loop through each row from 1 to userInputHeight
            for (currentRowNumber = 1; currentRowNumber <= userInputHeight; currentRowNumber = currentRowNumber + 1) {

                // For each row, we need to print as many asterisks as the row number
                int currentColumnNumber = 0; // temporary holder for star loop

                // Print the asterisks for this row
                if (currentRowNumber > 0) {
                    for (currentColumnNumber = 1; currentColumnNumber <= currentRowNumber; currentColumnNumber = currentColumnNumber + 1) {
                        // Print a single asterisk without moving to the next line yet
                        System.out.print("*");
                    }
                }

                // After finishing one row of asterisks, move to the next line
                System.out.println();
            }
        }
    }
}