import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height N entered by the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use a for loop to go through each row from 1 up to triangleHeight
        // Each rowNumber represents how many asterisks should be printed on that row
        for (int rowNumber = 1; rowNumber <= triangleHeight; rowNumber++) {
            // Declare a variable for the number of asterisks to print on this row
            int numberOfAsterisksInRow = rowNumber;

            // Initialize a counter variable for how many asterisks we have printed so far in this row
            int printedAsterisksSoFar = 0;

            // Use a while loop to print the exact number of asterisks equal to numberOfAsterisksInRow
            while (printedAsterisksSoFar < numberOfAsterisksInRow) {
                // Print one asterisk without moving to the next line
                System.out.print("*");

                // Increase the count of printed asterisks by 1
                printedAsterisksSoFar = printedAsterisksSoFar + 1;
            }

            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}