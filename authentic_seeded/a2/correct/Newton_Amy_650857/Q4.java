import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value as an integer
        int triangleHeight = userInputScanner.nextInt();

        // Step 4: Use a loop to go through each row from 1 up to the height
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {

            // Step 5: For each row, print the correct number of asterisks
            for (int currentAsteriskCount = 1; currentAsteriskCount <= currentRowNumber; currentAsteriskCount++) {
                System.out.print("*");
            }

            // Step 6: After finishing one row of asterisks, move to the next line
            System.out.println();
        }

        // Step 7: Close the Scanner because we are done with input
        userInputScanner.close();
    }
}