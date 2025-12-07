import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the integer height value N from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will print rows from 1 up to triangleHeight
        // For each rowNumber, we print rowNumber asterisks
        for (int rowNumber = 1; rowNumber <= triangleHeight; rowNumber++) {

            // Mathematical idea: numberOfStarsInRow = rowNumber
            int numberOfStarsInRow = rowNumber;

            // Print the correct number of asterisks for this row
            for (int starCounter = 1; starCounter <= numberOfStarsInRow; starCounter++) {
                System.out.print("*");
            }

            // After printing all asterisks in this row, move to the next line
            System.out.println();
        }

        // Close the scanner to be polite with system resources
        userInputScanner.close();
    }
}