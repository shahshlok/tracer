import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use a for loop to control the number of rows from 1 to N
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {
            // For each row, we want to print a number of asterisks equal to the row number
            // So for row 1: 1 star, row 2: 2 stars, ..., row N: N stars

            // Declare some intermediate math-style variables for clarity
            int a = currentRowNumber;  // a represents the number of stars for this row
            int b = 1;                 // b will be our loop counter starting at 1
            int c = a;                 // c represents the maximum number of stars to print

            // Inner loop prints asterisks for the current row
            for (int currentStarCount = b; currentStarCount <= c; currentStarCount++) {
                // Print an asterisk without moving to the next line
                System.out.print("*");
            }

            // After printing all asterisks for the current row, move to the next line
            System.out.println();
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}