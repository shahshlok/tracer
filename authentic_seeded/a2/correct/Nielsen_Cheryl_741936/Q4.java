import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value as an integer
        int userInputHeight = userInputScanner.nextInt();

        // We will print rows from 1 up to userInputHeight
        // Let a represent the current row number
        for (int a = 1; a <= userInputHeight; a++) {
            // For each row a, we want to print a asterisks
            // Let b represent the current column position within the row
            for (int b = 1; b <= a; b++) {
                // Print one asterisk without moving to the next line
                System.out.print("*");
            }
            // After finishing one row of asterisks, move to the next line
            System.out.println();
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}