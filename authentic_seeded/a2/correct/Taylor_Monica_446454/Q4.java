import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use a for loop to go through each row from 1 up to N
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {

            // For each row, we compute how many stars we need to print
            // Here the mathematical relationship is: numberOfStarsInRow = currentRowNumber
            int a = currentRowNumber; // a represents the current row number
            int numberOfStarsInRow = a;

            // Now we use another loop to print the correct number of asterisks for this row
            for (int currentStarIndex = 1; currentStarIndex <= numberOfStarsInRow; currentStarIndex++) {
                // Print one asterisk without moving to the next line
                System.out.print("*");
            }

            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}