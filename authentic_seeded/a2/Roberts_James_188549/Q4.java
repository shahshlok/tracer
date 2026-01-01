import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value from the user
        int triangleHeightInput = userInputScanner.nextInt();

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Store the height in another variable just to be extra clear
        int triangleHeight = triangleHeightInput;

        // Check if the height is greater than 0 before proceeding
        if (triangleHeight > 0) {
            // Use a loop to control the number of rows in the triangle
            int currentRowNumber = 1;
            while (currentRowNumber <= triangleHeight) {
                // For each row, print the correct number of asterisks
                int numberOfAsterisksToPrint = currentRowNumber;

                // We will build each row as a string before printing
                String currentRowString = "";

                // Use a loop to add asterisks to the current row string
                int currentAsteriskCount = 0;
                while (currentAsteriskCount < numberOfAsterisksToPrint) {
                    // Add one asterisk
                    currentRowString = currentRowString + "*";
                    currentAsteriskCount = currentAsteriskCount + 1;
                }

                // After building the row, print it
                System.out.println(currentRowString);

                // Move on to the next row
                currentRowNumber = currentRowNumber + 1;
            }
        }
        // If triangleHeight is 0 or negative, we do not print anything
        // This handles the edge case where the input is not positive
    }
}