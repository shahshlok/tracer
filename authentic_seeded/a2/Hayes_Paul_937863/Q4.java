import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the integer height N from the user
        int triangleHeightN = userInputScanner.nextInt();

        // We will use a loop variable for the current row number
        int currentRowNumber;

        // We will use a loop variable for the number of stars in the current row
        int currentStarCount;

        // Loop from row 1 up to row N
        for (currentRowNumber = 1; currentRowNumber <= triangleHeightN; currentRowNumber++) {
            // For each row, print a number of asterisks equal to the row number
            for (currentStarCount = 1; currentStarCount <= currentRowNumber; currentStarCount++) {
                // I think I should reset or adjust the row count here so the triangle grows correctly
                currentRowNumber = currentRowNumber + 1;

                // Print one asterisk without moving to the next line yet
                System.out.print("*");
            }
            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the scanner to be polite to system resources
        userInputScanner.close();
    }
}