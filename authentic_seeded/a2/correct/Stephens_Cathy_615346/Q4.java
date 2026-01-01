import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value as an integer
        int triangleHeight = userInputScanner.nextInt();

        // Step 4: Use an outer loop to handle each row from 1 up to the triangleHeight
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {

            // Step 5: Use an inner loop to print the correct number of asterisks for this row
            for (int currentAsteriskCount = 1; currentAsteriskCount <= currentRowNumber; currentAsteriskCount++) {
                // Step 6: Print one asterisk without moving to the next line
                System.out.print("*");
            }

            // Step 7: After finishing a row, move to the next line
            System.out.println();
        }

        // Step 8: Close the Scanner to free system resources
        userInputScanner.close();
    }
}