import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the integer height value from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use a loop to print each row of the triangle
        // rowNumber will go from 1 up to triangleHeight (inclusive)
        for (int rowNumber = 1; rowNumber <= triangleHeight; rowNumber++) {

            // For each row, we print as many asterisks as the current row number
            // Here, a is the number of asterisks to print on this row
            int a = rowNumber;

            // Use another loop to print a asterisks on the current line
            for (int starCount = 1; starCount <= a; starCount++) {
                // Print a single asterisk without moving to a new line
                System.out.print("*");
            }

            // After printing all asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}