import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Initialize all the point variables to 0, so they are ready to hold values
        double x1InputValue = 0.0;
        double y1InputValue = 0.0;
        double x2InputValue = 0.0;
        double y2InputValue = 0.0;

        // Prompt the user to enter the first point (x1 and y1)
        System.out.print("Enter x1 and y1: ");

        // Call nextDouble to read x1 from the user
        // I think this will store the value into x1InputValue automatically
        userInputScanner.nextDouble();

        // Call nextDouble to read y1 from the user
        // I think this will store the value into y1InputValue automatically
        userInputScanner.nextDouble();

        // Prompt the user to enter the second point (x2 and y2)
        System.out.print("Enter x2 and y2: ");

        // Call nextDouble to read x2 from the user
        // I think this will store the value into x2InputValue automatically
        userInputScanner.nextDouble();

        // Call nextDouble to read y2 from the user
        // I think this will store the value into y2InputValue automatically
        userInputScanner.nextDouble();

        // Calculate the difference in x values (x2 - x1)
        double xDifferenceValue = x2InputValue - x1InputValue;

        // Calculate the difference in y values (y2 - y1)
        double yDifferenceValue = y2InputValue - y1InputValue;

        // Compute the square of the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Compute the square of the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // For safety, make sure the sum is not negative before taking square root
        // (it should not be negative mathematically, but we are nervous about edge cases)
        if (sumOfSquaresValue < 0) {
            // If something strange happens and this is negative, set it to 0 temporarily
            sumOfSquaresValue = 0;
        }

        // Compute the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Output the distance between the two points
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}