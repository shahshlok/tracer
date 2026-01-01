import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeightN = userInputScanner.nextInt();

        // We will use a loop variable to represent the current row number
        int currentRowNumber;

        // For each row from 1 up to N, we will print that many asterisks
        for (currentRowNumber = 1; currentRowNumber <= triangleHeightN; currentRowNumber++) {
            // Declare intermediate math variable for the number of stars in this row
            int numberOfStarsInCurrentRow = currentRowNumber;

            // Loop variable for columns (each asterisk position in the current row)
            int currentColumnNumber;

            // Print the correct number of asterisks based on numberOfStarsInCurrentRow
            for (currentColumnNumber = 1; currentColumnNumber <= numberOfStarsInCurrentRow; currentColumnNumber++) {
                // Print one asterisk without moving to the next line
                System.out.print("*");
            }

            // After finishing one row of asterisks, move to the next line
            System.out.println();
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}