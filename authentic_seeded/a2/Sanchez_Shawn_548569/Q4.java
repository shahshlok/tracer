import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height from the user
        int triangleHeight = userInputScanner.nextInt();

        // Close the scanner since we do not need more input
        userInputScanner.close();

        // Check that the height is greater than 0 before printing anything
        if (triangleHeight > 0) {
            // Use a loop to go through each row of the triangle
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {
                // For each row, we will build a string of asterisks
                String currentRowStars = "";

                // Use another loop to add the correct number of asterisks for this row
                int currentStarCount = 1;
                while (currentStarCount <= currentRowNumber) {
                    // Add one asterisk to the current row string
                    currentRowStars = currentRowStars + "*";

                    // Move to the next star
                    currentStarCount = currentStarCount + 1;
                }

                // Print the completed row of asterisks
                System.out.println(currentRowStars);

                // Move to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        } else {
            // If the height is not positive, we will not print a triangle
            // This is an extra safety check for an edge case
            // We do not print anything else because the sample run does not show an error message
        }
    }
}