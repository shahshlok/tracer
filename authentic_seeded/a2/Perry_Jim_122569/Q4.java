import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read the user's input
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeightN = userInputScanner.nextInt();

        // We will use a loop variable for the current row number
        int currentRowNumber;

        // Loop from row 1 up to row N
        for (currentRowNumber = 1; currentRowNumber <= triangleHeightN; currentRowNumber++) {

            // For each row, we want to print a number of asterisks equal to the row number
            int numberOfAsterisksInRow = currentRowNumber;

            // We will use another loop variable for counting asterisks
            int currentAsteriskCount;

            // Loop to print each asterisk in the current row
            for (currentAsteriskCount = 1; currentAsteriskCount <= numberOfAsterisksInRow; currentAsteriskCount++) {
                System.out.print("*");
            }

            // After printing all asterisks in the current row, move to the next line
            System.out.println();
        }

        // Close the scanner to be polite and free resources
        userInputScanner.close();
    }
}