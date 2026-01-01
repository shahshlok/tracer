import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the integer height value from the user
        int userInputHeight = userInputScanner.nextInt();

        // Close the scanner to avoid any resource leaks
        userInputScanner.close();

        // Use a temporary variable to hold the height for safety
        int triangleHeight = userInputHeight;

        // Check that the height is greater than or equal to 1
        if (triangleHeight >= 1) {

            // Loop from 1 up to the height to print each row
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {

                // For each row, we will build a string of asterisks
                String currentRowString = "";

                // Use another variable to control the number of stars in this row
                int numberOfStarsInRow = currentRowNumber;

                // We check that the number of stars is at least 1
                if (numberOfStarsInRow >= 1) {
                    int currentStarCount = 1;
                    while (currentStarCount <= numberOfStarsInRow) {
                        // Add one asterisk to the row string
                        currentRowString = currentRowString + "*";
                        currentStarCount = currentStarCount + 1;
                    }
                }

                // Print the completed row of asterisks
                System.out.println(currentRowString);

                // Move to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        }
    }
}