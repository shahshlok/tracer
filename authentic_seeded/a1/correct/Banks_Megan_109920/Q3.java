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

        // Declare intermediate variables to follow the distance formula
        // a will be (x2 - x1)
        double a = x2Coordinate - x1Coordinate;

        // b will be (y2 - y1)
        double b = y2Coordinate - y1Coordinate;

        // c will be the sum of squares: (x2 - x1)^2 + (y2 - y1)^2
        double c = (a * a) + (b * b);

        // Now compute the distance using the square root of c
        double distanceBetweenPoints = Math.sqrt(c);

        // Display the result following the example format
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}