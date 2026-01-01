import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value entered by the user
        int userInputHeight = userInputScanner.nextInt();

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Make a copy of the height in a temporary variable
        int triangleHeight = userInputHeight;

        // Check if the height is greater than 0 before printing the triangle
        if (triangleHeight > 0) {

            // Loop through each row of the triangle
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {

                // For each row, we will build a line of asterisks
                int numberOfAsterisksToPrint = currentRowNumber;

                // Use a StringBuilder to build the line of asterisks
                StringBuilder currentRowBuilder = new StringBuilder();

                // Loop to add the correct number of asterisks for this row
                int currentAsteriskCount = 0;
                while (currentAsteriskCount < numberOfAsterisksToPrint) {
                    currentRowBuilder.append("*");
                    currentAsteriskCount = currentAsteriskCount + 1;
                }

                // Convert the StringBuilder to a String
                String currentRowString = currentRowBuilder.toString();

                // Print the current row of asterisks
                System.out.println(currentRowString);

                // Move to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        } else {
            // If the height is not positive, do not print any triangle
            // This is an extra safety check to handle edge cases like 0 or negative input
            // No output is required in this case based on the problem description
        }
    }
}