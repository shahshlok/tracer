import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the first point coordinates
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 as double values
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Prompt the user for the second point coordinates
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 as double values
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Compute the difference in x values (x2 - x1)
        double temporaryXDifferenceHolder = secondPointXValue - firstPointXValue;
        double xDifferenceValue = temporaryXDifferenceHolder;

        // Compute the difference in y values (y2 - y1)
        double temporaryYDifferenceHolder = secondPointYValue - firstPointYValue;
        double yDifferenceValue = temporaryYDifferenceHolder;

        // Square the x difference (xDifferenceValue * xDifferenceValue)
        double temporaryXSquaredHolder = xDifferenceValue * xDifferenceValue;
        double xDifferenceSquaredValue = temporaryXSquaredHolder;

        // Square the y difference (yDifferenceValue * yDifferenceValue)
        double temporaryYSquaredHolder = yDifferenceValue * yDifferenceValue;
        double yDifferenceSquaredValue = temporaryYSquaredHolder;

        // Add the squared differences
        double temporarySumOfSquaresHolder = xDifferenceSquaredValue + yDifferenceSquaredValue;
        double sumOfSquaresValue = temporarySumOfSquaresHolder;

        // Compute the square root of the sum of squares using Math.sqrt
        double temporaryDistanceHolder = Math.sqrt(sumOfSquaresValue);
        double distanceBetweenPointsValue = temporaryDistanceHolder;

        // Extra cautious check even though Math.sqrt should always return a value here
        if (distanceBetweenPointsValue >= 0) {
            // Output the distance between the two points
            System.out.println("The distance of the two points is " + distanceBetweenPointsValue);
        } else {
            // This path should not logically happen, but included due to nervousness about edge cases
            System.out.println("An unexpected error occurred while calculating the distance.");
        }

        // Close the Scanner to avoid any resource leaks
        userInputScanner.close();
    }
}