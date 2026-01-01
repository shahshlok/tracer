import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the integer height value from the user
        int triangleHeight = userInputScanner.nextInt();

        // Step 4: Use an outer loop to control the number of rows from 1 up to the height
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {
            // Step 5: For each row, use an inner loop to print the correct number of asterisks
            for (int currentStarCount = 1; currentStarCount <= currentRowNumber; currentStarCount++) {
                // Step 6: Print one asterisk without moving to the next line
                System.out.print("*");
            }
            // Step 7: After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Step 8: Close the scanner because we are done reading input
        userInputScanner.close();
    }
}