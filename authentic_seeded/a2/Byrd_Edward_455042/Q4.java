import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the height of the triangle
        System.out.print("Enter height: ");

        // Read the height N from the user
        int triangleHeight = userInputScanner.nextInt();

        // We will use loopRowIndex to count which row we are on, starting from 1 up to triangleHeight
        for (int loopRowIndex = 1; loopRowIndex <= triangleHeight; loopRowIndex++) {
            // For each row, we want to print as many asterisks as the row number
            // So on row 1 we print 1 star, on row 2 we print 2 stars, and so on

            // We can think of a simple formula: numberOfStarsInThisRow = loopRowIndex
            int numberOfStarsInThisRow = loopRowIndex;

            // Now we loop to print exactly numberOfStarsInThisRow asterisks on this row
            for (int loopStarIndex = 1; loopStarIndex <= numberOfStarsInThisRow; loopStarIndex++) {
                System.out.print("*");
            }

            // After printing all the asterisks for this row, move to the next line
            System.out.println();
        }

        // Close the scanner because we are done with input
        userInputScanner.close();
    }
}