import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = userInputScanner.nextDouble(); // Read x1
        double firstPointYValue = userInputScanner.nextDouble(); // Read y1

        // Step 3: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = userInputScanner.nextDouble(); // Read x2
        double secondPointYValue = userInputScanner.nextDouble(); // Read y2

        // Step 4: Compute the difference in x values (x2 - x1)
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Step 5: Compute the difference in y values (y2 - y1)
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Step 6: Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Step 7: Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Step 8: Add the squared differences
        double sumOfSquaredDifferencesValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Step 9: Take the square root of the sum to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaredDifferencesValue);

        // Step 10: Print the result
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Step 11: Close the Scanner
        userInputScanner.close();
    }
}