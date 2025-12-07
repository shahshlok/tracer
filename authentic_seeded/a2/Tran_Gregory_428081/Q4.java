import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the integer height value from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use a for loop to go through each row from 1 up to triangleHeight
        // rowIndex will represent the current row number
        for (int rowIndex = 1; rowIndex <= triangleHeight; rowIndex++) {

            // Declare an integer to hold how many asterisks we should print in this row
            int numberOfAsterisksInRow = rowIndex;

            // Use another for loop to print the correct number of asterisks in the current row
            for (int asteriskIndex = 1; asteriskIndex <= numberOfAsterisksInRow; asteriskIndex++) {
                // Print one asterisk without moving to the next line
                System.out.print("*");
            }

            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the scanner because we are done with user input
        userInputScanner.close();
    }
}