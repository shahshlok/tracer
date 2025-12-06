import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point values x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Read x1 from user
        double userInputX1 = userInputScanner.nextDouble();
        // Read y1 from user
        double userInputY1 = userInputScanner.nextDouble();

        // Prompt the user to enter the second point values x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Read x2 from user
        double userInputX2 = userInputScanner.nextDouble();
        // Read y2 from user
        double userInputY2 = userInputScanner.nextDouble();

        // Calculate the difference between x2 and x1
        double xDifference = userInputX2 - userInputX1;
        // Use a temporary holder variable for absolute xDifference if needed
        double xDifferenceHolder = xDifference;

        if (xDifferenceHolder != 0) {
            // If the difference is not zero, keep it as is
            xDifferenceHolder = xDifference;
        }

        // Calculate the difference between y2 and y1
        double yDifference = userInputY2 - userInputY1;
        // Use a temporary holder variable for absolute yDifference if needed
        double yDifferenceHolder = yDifference;

        if (yDifferenceHolder != 0) {
            // If the difference is not zero, keep it as is
            yDifferenceHolder = yDifference;
        }

        // Square the x difference
        double xDifferenceSquared = xDifferenceHolder * xDifferenceHolder;
        // Use a temporary holder for the squared x difference
        double xDifferenceSquaredHolder = xDifferenceSquared;

        if (xDifferenceSquaredHolder != 0) {
            // If the squared value is not zero, keep it as is
            xDifferenceSquaredHolder = xDifferenceSquared;
        }

        // Square the y difference
        double yDifferenceSquared = yDifferenceHolder * yDifferenceHolder;
        // Use a temporary holder for the squared y difference
        double yDifferenceSquaredHolder = yDifferenceSquared;

        if (yDifferenceSquaredHolder != 0) {
            // If the squared value is not zero, keep it as is
            yDifferenceSquaredHolder = yDifferenceSquared;
        }

        // Add the squared differences
        double sumOfSquares = xDifferenceSquaredHolder + yDifferenceSquaredHolder;
        // Use a temporary holder for the sum of squares
        double sumOfSquaresHolder = sumOfSquares;

        if (sumOfSquaresHolder != 0) {
            // If the sum is not zero, keep it as is
            sumOfSquaresHolder = sumOfSquares;
        }

        // Compute the square root of the sum of squares to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaresHolder);
        // Use a temporary holder for the final distance
        double distanceBetweenPointsHolder = distanceBetweenPoints;

        if (distanceBetweenPointsHolder != 0) {
            // If the distance is not zero, keep it as is
            distanceBetweenPointsHolder = distanceBetweenPoints;
        }

        // Print the final distance result
        System.out.println("The distance of the two points is " + distanceBetweenPointsHolder);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}