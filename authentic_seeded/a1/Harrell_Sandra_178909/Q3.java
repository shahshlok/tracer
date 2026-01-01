import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the coordinates for the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double x1Coordinate = userInputScanner.nextDouble();
        double y1Coordinate = userInputScanner.nextDouble();

        // Ask the user to enter the coordinates for the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double x2Coordinate = userInputScanner.nextDouble();
        double y2Coordinate = userInputScanner.nextDouble();

        // Compute the horizontal difference (x2 - x1)
        double horizontalDifference = x2Coordinate - x1Coordinate;

        // Compute the vertical difference (y2 - y1)
        double verticalDifference = y2Coordinate - y1Coordinate;

        // Declare intermediate math variables a, b, c to align with formula steps
        // a will represent (x2 - x1)^2
        double a = horizontalDifference * horizontalDifference;

        // b will represent (y2 - y1)^2
        double b = verticalDifference * verticalDifference;

        // c will represent the sum of the squares: a + b
        double c = a + b;

        // Now compute the distance using the square root of c
        double distanceBetweenPoints = Math.sqrt(c);

        // Print the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}