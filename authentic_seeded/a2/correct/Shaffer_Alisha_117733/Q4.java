import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Step 3: Read the height value from the user
        int triangleHeight = keyboardScanner.nextInt();

        // Step 4: Use a loop to go through each row from 1 up to the height
        for (int currentRowNumber = 1; currentRowNumber <= triangleHeight; currentRowNumber++) {

            // Step 5: For each row, print as many asterisks as the row number
            for (int starCounter = 1; starCounter <= currentRowNumber; starCounter++) {
                // Step 6: Print one asterisk without moving to the next line
                System.out.print("*");
            }

            // Step 7: After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Step 8: Close the Scanner because we are done with input
        keyboardScanner.close();
    }
}