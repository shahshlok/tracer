import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point values x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Read x1 from the user
        double pointOneXValue = userInputScanner.nextDouble();
        // Read y1 from the user
        double pointOneYValue = userInputScanner.nextDouble();

        // Prompt the user to enter the second point values x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Read x2 from the user
        double pointTwoXValue = userInputScanner.nextDouble();
        // Read y2 from the user
        double pointTwoYValue = userInputScanner.nextDouble();

        // Compute the difference in x values: (x2 - x1)
        double xDifferenceValue = pointTwoXValue - pointOneXValue;

        // Compute the difference in y values: (y2 - y1)
        double yDifferenceValue = pointTwoYValue - pointOneYValue;

        // Compute the square of the difference in x values: (x2 - x1)^2
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Compute the square of the difference in y values: (y2 - y1)^2
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences together: (x2 - x1)^2 + (y2 - y1)^2
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Just to be extra safe, check that the sum of squares is not negative before taking the square root
        // In theory this should never be negative, but we are nervous about edge cases
        if (sumOfSquaresValue < 0) {
            // If it is negative (due to some unexpected reason), we set it to 0 as a safe fallback
            sumOfSquaresValue = 0;
        }

        // Compute the distance using the square root of the sum of squares
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Output the final computed distance
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to be tidy with resources
        userInputScanner.close();
    }
}