import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input values from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the first point's x and y values
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 as doubles
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Prompt the user for the second point's x and y values
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 as doubles
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Compute the difference in x values
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Compute the difference in y values
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Square the difference in x values
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Square the difference in y values
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences together
        double sumOfSquaredDifferencesValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Initialize distance value
        double distanceBetweenPointsValue = 0.0;

        // Check that the sum is not negative (nervous about edge cases, even though it should not be)
        if (sumOfSquaredDifferencesValue >= 0.0) {
            // Compute the square root of the sum to get the distance
            distanceBetweenPointsValue = Math.sqrt(sumOfSquaredDifferencesValue);
        }

        // Print the result exactly in the required format
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}