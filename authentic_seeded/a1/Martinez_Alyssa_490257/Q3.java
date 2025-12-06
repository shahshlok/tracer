import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point's coordinates
        System.out.print("Enter x1 and y1: ");

        // Read the x1 value as a double
        double firstPointXValue = userInputScanner.nextDouble();

        // Read the y1 value as a double
        double firstPointYValue = userInputScanner.nextDouble();

        // Prompt the user to enter the second point's coordinates
        System.out.print("Enter x2 and y2: ");

        // Read the x2 value as a double
        double secondPointXValue = userInputScanner.nextDouble();

        // Read the y2 value as a double
        double secondPointYValue = userInputScanner.nextDouble();

        // Calculate the difference in x values (x2 - x1)
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Calculate the difference in y values (y2 - y1)
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Just an extra cautious check to ensure the sum is not negative before taking the square root
        // (mathematically it should never be negative, but checking anyway)
        if (sumOfSquaredDifferences < 0) {
            // If something went really wrong, set it to 0 to avoid Math.sqrt of a negative number
            sumOfSquaredDifferences = 0;
        }

        // Compute the distance using the square root of the sum of squared differences
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Output the final distance
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}