import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point coordinates x1 and y1
        System.out.print("Enter x1 and y1: ");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Prompt the user to enter the second point coordinates x2 and y2
        System.out.print("Enter x2 and y2: ");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Compute the horizontal difference (x2 - x1)
        double horizontalDifferenceValue = x2CoordinateValue - x1CoordinateValue;

        // Compute the vertical difference (y2 - y1)
        double verticalDifferenceValue = y2CoordinateValue - y1CoordinateValue;

        // Declare intermediate math variables a, b, c
        // a will store (x2 - x1)^2
        double a = horizontalDifferenceValue * horizontalDifferenceValue;

        // b will store (y2 - y1)^2
        double b = verticalDifferenceValue * verticalDifferenceValue;

        // c will store the sum a + b
        double c = a + b;

        // Use the distance formula: sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double distanceBetweenPointsValue = Math.sqrt(c);

        // Display the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}