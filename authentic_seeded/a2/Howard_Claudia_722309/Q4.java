import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the integer height value from the user
        int triangleHeightInput = userInputScanner.nextInt();

        // Close the scanner since we do not need more input
        userInputScanner.close();

        // Store the height in a separate variable in case we need the original later
        int triangleHeight = triangleHeightInput;

        // Check that the height is greater than zero
        // If it is not, then we will not print anything because a non-positive height
        // does not make sense for this triangle pattern
        if (triangleHeight > 0) {
            // Use a for loop to go through each row of the triangle
            int currentRowNumberHolder = 1; // Start with row 1
            for (int currentRowNumber = 1; currentRowNumber < triangleHeight; currentRowNumber++) {
                // Set a temporary holder so we do not accidentally change the loop variable
                currentRowNumberHolder = currentRowNumber;

                // For each row, we need to print as many asterisks as the row number
                int numberOfAsterisksToPrint = currentRowNumberHolder;

                // Extra safety check: make sure numberOfAsterisksToPrint is at least 0
                if (numberOfAsterisksToPrint >= 0) {
                    // Build the string of asterisks for this row
                    String currentRowString = "";

                    // Loop to append asterisks one by one
                    int currentAsteriskIndexHolder = 0;
                    for (int currentAsteriskIndex = 0; currentAsteriskIndex < numberOfAsterisksToPrint; currentAsteriskIndex++) {
                        currentAsteriskIndexHolder = currentAsteriskIndex;

                        // Append one asterisk to the current row string
                        currentRowString = currentRowString + "*";
                    }

                    // After building the row, print it out
                    System.out.println(currentRowString);
                }
            }
        }
    }
}