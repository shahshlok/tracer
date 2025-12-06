import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXCoordinate = userInputScanner.nextDouble();  // Read x1
        double firstPointYCoordinate = userInputScanner.nextDouble();  // Read y1

        // Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXCoordinate = userInputScanner.nextDouble();  // Read x2
        double secondPointYCoordinate = userInputScanner.nextDouble();  // Read y2

        // Compute the difference between x2 and x1
        double xCoordinateDifference = secondPointXCoordinate - firstPointXCoordinate;

        // Compute the difference between y2 and y1
        double yCoordinateDifference = secondPointYCoordinate - firstPointYCoordinate;

        // Compute the square of the x difference
        double xDifferenceSquared = xCoordinateDifference * xCoordinateDifference;

        // Compute the square of the y difference
        double yDifferenceSquared = yCoordinateDifference * yCoordinateDifference;

        // Add the two squared values together
        double sumOfSquares = xDifferenceSquared + yDifferenceSquared;

        // Take the square root of the sum of squares to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquares);

        // Print the distance in the required format
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}