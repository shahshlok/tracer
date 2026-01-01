import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeightN = userInputScanner.nextInt();

        // We will use a loop variable for the current row number
        int currentRowNumber;

        // Outer loop to go through each row from 1 to N
        for (currentRowNumber = 1; currentRowNumber <= triangleHeightN; currentRowNumber++) {

            // For each row, we calculate how many stars to print
            // In a right triangle, the number of stars in row r is r
            int numberOfStarsInRow = currentRowNumber;

            // We declare a loop variable for the stars
            int currentStarIndex;

            // Inner loop to print the correct number of stars for this row
            for (currentStarIndex = 1; currentStarIndex <= numberOfStarsInRow; currentStarIndex++) {
                // Print one star without moving to the next line
                System.out.print("*");
            }

            // After printing all stars for this row, move to the next line
            System.out.println();
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}