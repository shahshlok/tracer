import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point (x1 and y1)
        System.out.print("Enter x1 and y1: ");

        // Read x1 from the user
        double x1InputValue = userInputScanner.nextDouble();

        // Read y1 from the user
        double y1InputValue = userInputScanner.nextDouble();

        // Prompt the user to enter the second point (x2 and y2)
        System.out.print("Enter x2 and y2: ");

        // Read x2 from the user
        double x2InputValue = userInputScanner.nextDouble();

        // Read y2 from the user
        double y2InputValue = userInputScanner.nextDouble();

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