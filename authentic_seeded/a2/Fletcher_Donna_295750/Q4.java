import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value entered by the user
        int triangleHeight = userInputScanner.nextInt();

        // Step 4: Use an outer loop to go through each row from 1 up to the triangle height
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {
            // Step 5: For each row, use an inner loop to print the correct number of asterisks
            for (int currentColumnNumber = 1; currentColumnNumber <= currentRowNumber; currentColumnNumber++) {
                // Step 6: Print a single asterisk without moving to the next line
                System.out.print("*");
            }
            // Step 7: After printing all asterisks for the current row, move to the next line
            System.out.println();
        }

        // Step 8: Close the Scanner because we are done with input
        userInputScanner.close();
    }
}