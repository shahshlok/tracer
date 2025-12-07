import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Ask the user to enter the height of the right triangle
        System.out.print("Enter height: ");

        // Read the height value N from the user
        int triangleHeight = keyboardInput.nextInt();

        // We will use a for loop to control the row number from 1 to N
        // Let a represent the current row number
        for (int a = 1; a <= triangleHeight; a++) {

            // For each row a, we need to print exactly a asterisks
            // Let b represent the current asterisk position in the row
            for (int b = 1; b <= a; b++) {
                // Print one asterisk without moving to the next line yet
                System.out.print("*");
            }

            // After printing all a asterisks in this row, move to the next line
            System.out.println();
        }

        // Close the Scanner object since we are done with input
        keyboardInput.close();
    }
}