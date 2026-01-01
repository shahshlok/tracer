import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double x1Coordinate = userInputScanner.nextDouble();
        double y1Coordinate = userInputScanner.nextDouble();

        // Prompt the user for the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double x2Coordinate = userInputScanner.nextDouble();
        double y2Coordinate = userInputScanner.nextDouble();

        // Compute the intermediate values based on the distance formula
        // a will be (x2 - x1)
        double a = x2Coordinate - x1Coordinate;

        // b will be (y2 - y1)
        double b = y2Coordinate - y1Coordinate;

        // c will be a^2 + b^2
        double c = (a * a) + (b * b);

        // Use the square root of c to get the distance between the two points
        double distanceBetweenPoints = Math.sqrt(c);

        // Print the result of the distance computation
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}