import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 1: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = keyboardScanner.nextDouble();
        double firstPointYValue = keyboardScanner.nextDouble();

        // Step 2: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = keyboardScanner.nextDouble();
        double secondPointYValue = keyboardScanner.nextDouble();

        // Step 3: Calculate the difference in x values (x2 - x1)
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Step 4: Calculate the difference in y values (y2 - y1)
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Step 5: Square the differences for x and y
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Step 6: Add the squared differences together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Step 7: Take the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Step 8: Display the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Step 9: Close the scanner to free resources
        keyboardScanner.close();
    }
}