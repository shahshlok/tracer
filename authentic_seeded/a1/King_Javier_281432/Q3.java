import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter x1 and y1 values
        System.out.print("Enter x1 and y1: ");
        double firstPointXCoordinate = userInputScanner.nextDouble();
        double firstPointYCoordinate = userInputScanner.nextDouble();

        // Ask the user to enter x2 and y2 values
        System.out.print("Enter x2 and y2: ");
        double secondPointXCoordinate = userInputScanner.nextDouble();
        double secondPointYCoordinate = userInputScanner.nextDouble();

        // Calculate the difference in x coordinates
        double xDifference = secondPointXCoordinate - firstPointXCoordinate;
        double temporaryHolderForXSquared = xDifference * xDifference;

        // Explicitly check even though it is not strictly necessary
        if (xDifference == 0) {
            temporaryHolderForXSquared = 0;
        }

        // Calculate the difference in y coordinates
        double yDifference = secondPointYCoordinate - firstPointYCoordinate;
        double temporaryHolderForYSquared = yDifference * yDifference;

        // Explicitly check even though it is not strictly necessary
        if (yDifference == 0) {
            temporaryHolderForYSquared = 0;
        }

        // Add the squared differences together
        double sumOfSquares = temporaryHolderForXSquared + temporaryHolderForYSquared;

        // Explicitly check if the sum is not negative before taking square root
        if (sumOfSquares < 0) {
            // This should not happen mathematically, but I am checking just in case
            sumOfSquares = 0;
        }

        // Use Math.sqrt to get the distance between the two points
        double distanceBetweenPoints = Math.sqrt(sumOfSquares);

        // Print the result in the required format
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}