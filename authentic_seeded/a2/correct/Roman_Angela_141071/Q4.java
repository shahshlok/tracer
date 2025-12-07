import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height N from the user
        int userInputHeight = userInputScanner.nextInt();

        // We will build the triangle row by row
        // The number of rows is equal to userInputHeight
        int currentRowNumber;
        int currentColumnNumber;

        // Loop through each row from 1 up to userInputHeight
        for (currentRowNumber = 1; currentRowNumber <= userInputHeight; currentRowNumber++) {

            // For each row, we print as many asterisks as the row number
            // So row 1 has 1 star, row 2 has 2 stars, etc.
            for (currentColumnNumber = 1; currentColumnNumber <= currentRowNumber; currentColumnNumber++) {
                System.out.print("*");
            }

            // Move to the next line after finishing one row of stars
            System.out.println();
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}