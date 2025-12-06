import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the first point's coordinates
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 as double values
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Prompt the user for the second point's coordinates
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 as double values
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Compute the difference in x values (x2 - x1)
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Compute the difference in y values (y2 - y1)
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squares together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Just a nervous check that the sum is not negative before sqrt (it should never be)
        if (sumOfSquaresValue < 0) {
            // If this ever happens, something is very wrong, but handle it anyway
            sumOfSquaresValue = 0;
        }

        // Compute the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Output the distance result
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}