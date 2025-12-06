import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double firstPointX1Value = userInputScanner.nextDouble();
        double firstPointY1Value = userInputScanner.nextDouble();

        // Ask the user to enter the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double secondPointX2Value = userInputScanner.nextDouble();
        double secondPointY2Value = userInputScanner.nextDouble();

        // Now we apply the distance formula: sqrt((x2 - x1)^2 + (y2 - y1)^2)

        // Step 1: Compute the differences in x and y
        double a = secondPointX2Value - firstPointX1Value; // a represents (x2 - x1)
        double b = secondPointY2Value - firstPointY1Value; // b represents (y2 - y1)

        // Step 2: Square the differences
        double c = a * a; // c represents (x2 - x1)^2
        double d = b * b; // d represents (y2 - y1)^2

        // Step 3: Add the squared differences
        double e = c + d; // e represents (x2 - x1)^2 + (y2 - y1)^2

        // Step 4: Take the square root of the sum to get the distance
        double distanceBetweenTwoPointsValue = Math.sqrt(e);

        // Display the result exactly as requested
        System.out.println("The distance of the two points is " + distanceBetweenTwoPointsValue);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}