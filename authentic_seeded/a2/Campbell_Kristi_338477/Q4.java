import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeightN = userInputScanner.nextInt();

        // We will use a for loop to go through each row from 1 up to N
        for (int currentRowIndex = 1; currentRowIndex <= triangleHeightN; currentRowIndex++) {

            // Let a represent the current number of stars to print in this row
            int a = currentRowIndex;

            // Use an inner loop to print exactly 'a' asterisks for this row
            for (int currentStarIndex = 1; currentStarIndex <= a; currentStarIndex++) {
                System.out.print("*");
            }

            // After printing all the stars for this row, move to the next line
            System.out.println();
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}