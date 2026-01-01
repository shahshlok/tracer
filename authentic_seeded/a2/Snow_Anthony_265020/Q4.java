import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read input from the user
        Scanner inputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value from the user
        int triangleHeight = inputScanner.nextInt();

        // Step 4: Use a loop to go through each row from 1 up to the given height
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {

            // Step 5: For each row, print the correct number of asterisks
            for (int currentStarCount = 1; currentStarCount <= currentRowNumber; currentStarCount++) {
                System.out.print("*");
            }

            // Step 6: After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Step 7: Close the scanner because we are done with user input
        inputScanner.close();
    }
}