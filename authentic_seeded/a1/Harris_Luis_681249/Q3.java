import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point coordinates x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXCoordinate = userInputScanner.nextDouble();
        double firstPointYCoordinate = userInputScanner.nextDouble();

        // Prompt the user to enter the second point coordinates x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXCoordinate = userInputScanner.nextDouble();
        double secondPointYCoordinate = userInputScanner.nextDouble();

        // Compute the difference in x coordinates: (x2 - x1)
        double xCoordinateDifference = secondPointXCoordinate - firstPointXCoordinate;
        // Just to be extra careful, store squared difference in a separate variable
        double xCoordinateDifferenceSquared = xCoordinateDifference * xCoordinateDifference;

        // Compute the difference in y coordinates: (y2 - y1)
        double yCoordinateDifference = secondPointYCoordinate - firstPointYCoordinate;
        // Store squared difference for y in another variable
        double yCoordinateDifferenceSquared = yCoordinateDifference * yCoordinateDifference;

        // Add the squared differences together
        double sumOfSquaredDifferences = xCoordinateDifferenceSquared + yCoordinateDifferenceSquared;

        // Prepare a variable to hold the final distance
        double distanceBetweenTwoPoints = 0.0;

        // Only compute the square root if the sum is not negative (paranoid check)
        if (sumOfSquaredDifferences >= 0.0) {
            distanceBetweenTwoPoints = Math.sqrt(sumOfSquaredDifferences);
        }

        // Print the result exactly as required
        System.out.println("The distance of the two points is " + distanceBetweenTwoPoints);

        // Close the scanner to be neat
        userInputScanner.close();
    }
}