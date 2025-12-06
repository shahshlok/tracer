import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = userInputScanner.nextDouble();  // Read x1
        double firstPointYValue = userInputScanner.nextDouble();  // Read y1

        // Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = userInputScanner.nextDouble(); // Read x2
        double secondPointYValue = userInputScanner.nextDouble(); // Read y2

        // Compute the difference between x2 and x1
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Compute the difference between y2 and y1
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Add the two squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Print the final distance
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner because we are done with input
        userInputScanner.close();
    }
}