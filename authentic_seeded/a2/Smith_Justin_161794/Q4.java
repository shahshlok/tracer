import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height as an integer value from the user
        int triangleHeightValue = userInputScanner.nextInt();

        // We will use a loop variable to count the current row number
        int currentRowNumber = 1;

        // Loop from row 1 up to the triangleHeightValue
        while (currentRowNumber <= triangleHeightValue) {

            // For each row, we will first compute how many stars to print
            // In a right triangle, the number of stars in each row equals the row number
            int numberOfStarsInCurrentRow = currentRowNumber;

            // Now we print the correct number of stars using another loop
            int currentStarCount = 1;
            while (currentStarCount <= numberOfStarsInCurrentRow) {
                System.out.print("*");
                currentStarCount = currentStarCount + 1;
            }

            // After printing all stars for the current row, move to the next line
            System.out.println();

            // Move to the next row
            currentRowNumber = currentRowNumber + 1;
        }

        // Close the Scanner to free resources
        userInputScanner.close();
    }
}