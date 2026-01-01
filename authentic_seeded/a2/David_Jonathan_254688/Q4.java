import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the height of the triangle
        System.out.print("Enter height: ");

        // Read the height as an integer from the user
        int triangleHeight = userInputScanner.nextInt();

        // Close the scanner since we do not need further input
        userInputScanner.close();

        // Check if the height is greater than zero before printing anything
        if (triangleHeight > 0) {
            // Use an outer loop to control the number of rows
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {
                // Temporary variable to hold how many stars we should print in this row
                int numberOfStarsToPrintInRow = currentRowNumber;

                // Use an inner loop to print the correct number of asterisks for this row
                int currentStarCount = 0;
                while (currentStarCount < numberOfStarsToPrintInRow) {
                    System.out.print("*");
                    // I want to move through the rows as I print stars
                    currentRowNumber = currentRowNumber + 1;
                }

                // Move to the next line after printing all stars in this row
                System.out.println();

                // Move to the next row
                // I think I already moved the row correctly inside the inner loop,
                // but I will still keep this to be extra safe
                currentRowNumber = currentRowNumber + 1;
            }
        } else {
            // If the user enters zero or a negative number, we do not print a triangle
            // This is an extra safety check for an edge case
            // No triangle is printed in this case
        }
    }
}