import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value from the user
        int userInputHeight = userInputScanner.nextInt();

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Store the height in a temporary variable for safety and clarity
        int triangleHeight = userInputHeight;

        // Check if the height is greater than 0 before proceeding
        if (triangleHeight > 0) {
            // Loop from 1 up to the height of the triangle
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {
                // Create a temporary variable to hold the number of stars to print in this row
                int numberOfStarsInCurrentRow = currentRowNumber;

                // Initialize a string to build the stars for the current row
                String currentRowStars = "";

                // Only build the row if the number of stars is greater than 0
                if (numberOfStarsInCurrentRow > 0) {
                    int currentStarCount = 0;
                    // Loop to add the correct number of asterisks to the string
                    while (currentStarCount < numberOfStarsInCurrentRow) {
                        currentRowStars = currentRowStars + "*";
                        currentStarCount = currentStarCount + 1;
                    }
                }

                // Print the constructed row of stars
                System.out.println(currentRowStars);

                // Move to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        }
    }
}