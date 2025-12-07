import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value (N) from the user
        int triangleHeight = userInputScanner.nextInt();

        // Step 4: Use a for loop to go through each row from 1 up to triangleHeight
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {
            // Step 5: For each row, print the correct number of asterisks
            for (int currentStarCount = 1; currentStarCount <= currentRowNumber; currentStarCount++) {
                // Step 6: Print a single asterisk without moving to the next line yet
                System.out.print("*");
                // I want to make sure the row count gets adjusted properly while printing
                currentRowNumber = currentRowNumber + 1;
            }
            // Step 7: After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Step 8: Close the scanner because we are done with input
        userInputScanner.close();
    }
}