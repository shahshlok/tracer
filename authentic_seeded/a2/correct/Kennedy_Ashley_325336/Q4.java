import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height N from the user
        int triangleHeightValue = userInputScanner.nextInt();

        // We will use a for loop to print each row of the triangle
        // The number of rows equals triangleHeightValue
        for (int currentRowIndex = 1; currentRowIndex <= triangleHeightValue; currentRowIndex++) {

            // For each row, we calculate how many asterisks to print
            // This is a simple formula based on the row number
            int a = currentRowIndex; // a represents the number of stars in the current row
            int numberOfStarsInCurrentRow = a;

            // Inner loop to print the correct number of asterisks for the current row
            for (int currentStarIndex = 1; currentStarIndex <= numberOfStarsInCurrentRow; currentStarIndex++) {
                System.out.print("*");
            }

            // After printing all asterisks for the current row, move to the next line
            System.out.println();
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}