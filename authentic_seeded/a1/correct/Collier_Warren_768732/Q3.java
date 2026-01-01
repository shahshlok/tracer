import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter x1 and y1 values
        System.out.print("Enter x1 and y1: ");
        // Read the first x-coordinate
        double firstPointXCoordinate = userInputScanner.nextDouble();
        // Read the first y-coordinate
        double firstPointYCoordinate = userInputScanner.nextDouble();

        // Ask the user to enter x2 and y2 values
        System.out.print("Enter x2 and y2: ");
        // Read the second x-coordinate
        double secondPointXCoordinate = userInputScanner.nextDouble();
        // Read the second y-coordinate
        double secondPointYCoordinate = userInputScanner.nextDouble();

        // Compute the difference in x values
        double temporaryXDifferenceHolder = secondPointXCoordinate - firstPointXCoordinate;
        double xDifferenceValue = temporaryXDifferenceHolder;

        // Compute the difference in y values
        double temporaryYDifferenceHolder = secondPointYCoordinate - firstPointYCoordinate;
        double yDifferenceValue = temporaryYDifferenceHolder;

        // Square the difference in x values
        double xDifferenceSquared = xDifferenceValue * xDifferenceValue;

        // Square the difference in y values
        double yDifferenceSquared = yDifferenceValue * yDifferenceValue;

        // Add the squared differences together
        double sumOfSquares = xDifferenceSquared + yDifferenceSquared;

        // Just to be extra safe, check that the sum of squares is not negative
        if (sumOfSquares < 0) {
            // If this happens, set it to 0 (this should not really occur with these operations)
            sumOfSquares = 0;
        }

        // Take the square root of the sum of squares to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquares);

        // Output the distance between the two points
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}