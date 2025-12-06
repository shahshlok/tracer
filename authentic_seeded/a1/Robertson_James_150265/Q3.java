import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the first point's x and y values
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 from the user
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Prompt the user for the second point's x and y values
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 from the user
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Compute the difference in x-coordinates (x2 - x1)
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Compute the difference in y-coordinates (y2 - y1)
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences together
        double sumOfSquaredDifferencesValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Just an extra nervous check to make sure the sum is not negative due to any weird issue
        if (sumOfSquaredDifferencesValue < 0) {
            // If something went very wrong, set it to 0 as a fallback
            sumOfSquaredDifferencesValue = 0;
        }

        // Compute the square root of the sum to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaredDifferencesValue);

        // Output the final distance between the two points
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}