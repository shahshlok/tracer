import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXCoordinate = 0.0;
        double firstPointYCoordinate = 0.0;

        // Read x1 and y1 from the user
        if (keyboardInputScanner.hasNextDouble()) {
            firstPointXCoordinate = keyboardInputScanner.nextDouble();
        }
        if (keyboardInputScanner.hasNextDouble()) {
            firstPointYCoordinate = keyboardInputScanner.nextDouble();
        }

        // Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXCoordinate = 0.0;
        double secondPointYCoordinate = 0.0;

        // Read x2 and y2 from the user
        if (keyboardInputScanner.hasNextDouble()) {
            secondPointXCoordinate = keyboardInputScanner.nextDouble();
        }
        if (keyboardInputScanner.hasNextDouble()) {
            secondPointYCoordinate = keyboardInputScanner.nextDouble();
        }

        // Compute the difference between x2 and x1
        double xDifferenceValue = secondPointXCoordinate - firstPointXCoordinate;
        // Make sure we use the value even if it is zero
        double safeXDifferenceValue = xDifferenceValue;
        if (safeXDifferenceValue != 0) {
            safeXDifferenceValue = xDifferenceValue;
        }

        // Compute the difference between y2 and y1
        double yDifferenceValue = secondPointYCoordinate - firstPointYCoordinate;
        // Make sure we use the value even if it is zero
        double safeYDifferenceValue = yDifferenceValue;
        if (safeYDifferenceValue != 0) {
            safeYDifferenceValue = yDifferenceValue;
        }

        // Compute (x2 - x1)^2
        double xDifferenceSquaredValue = safeXDifferenceValue * safeXDifferenceValue;
        if (xDifferenceSquaredValue == 0) {
            // This check is mostly for peace of mind; zero squared is still zero
            xDifferenceSquaredValue = 0;
        }

        // Compute (y2 - y1)^2
        double yDifferenceSquaredValue = safeYDifferenceValue * safeYDifferenceValue;
        if (yDifferenceSquaredValue == 0) {
            // This check is also just to be explicit
            yDifferenceSquaredValue = 0;
        }

        // Add the squared differences: (x2 - x1)^2 + (y2 - y1)^2
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Compute the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = 0.0;
        if (sumOfSquaresValue >= 0) {
            distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);
        }

        // Output the final result
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the Scanner to be safe
        keyboardInputScanner.close();
    }
}