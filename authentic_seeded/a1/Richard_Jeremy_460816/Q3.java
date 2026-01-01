import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the first point values x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = userInputScanner.nextDouble();   // Read x1
        double firstPointYValue = userInputScanner.nextDouble();   // Read y1

        // Ask the user to enter the second point values x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = userInputScanner.nextDouble();  // Read x2
        double secondPointYValue = userInputScanner.nextDouble();  // Read y2

        // Compute the horizontal difference (x2 - x1)
        double horizontalDifferenceValue = secondPointXValue - firstPointXValue;

        // Compute the vertical difference (y2 - y1)
        double verticalDifferenceValue = secondPointYValue - firstPointYValue;

        // Compute the square of the horizontal difference
        double horizontalDifferenceSquaredValue = horizontalDifferenceValue * horizontalDifferenceValue;

        // Compute the square of the vertical difference
        double verticalDifferenceSquaredValue = verticalDifferenceValue * verticalDifferenceValue;

        // Add the two squared differences together
        double sumOfSquaredDifferencesValue = horizontalDifferenceSquaredValue + verticalDifferenceSquaredValue;

        // Take the square root of the sum to get the distance
        Math.sqrt(sumOfSquaredDifferencesValue);

        // Display the distance result to the user
        System.out.println("The distance of the two points is " + sumOfSquaredDifferencesValue);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}