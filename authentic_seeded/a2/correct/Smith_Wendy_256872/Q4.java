import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userKeyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the integer height value N from the user
        int triangleHeightN = userKeyboardInputScanner.nextInt();

        // We will use a for loop to go through each row number from 1 up to N
        // Each rowNumber will correspond to how many asterisks we print on that row
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeightN; currentRowNumber++) {

            // In math terms, the number of asterisks in this row equals the row index:
            // a = currentRowNumber
            int a = currentRowNumber;

            // Now we will print exactly 'a' asterisks on this row
            for (int currentColumnNumber = 1; currentColumnNumber <= a; currentColumnNumber++) {
                // Print one asterisk without moving to the next line yet
                System.out.print("*");
            }

            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the scanner to free system resources
        userKeyboardInputScanner.close();
    }
}