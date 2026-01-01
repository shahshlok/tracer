import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read the user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the coordinates for the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");

        // Read the x1 and y1 values from the user
        double x1Coordinate = userInputScanner.nextDouble();
        double y1Coordinate = userInputScanner.nextDouble();

        // Prompt the user to enter the coordinates for the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");

        // Read the x2 and y2 values from the user
        double x2Coordinate = userInputScanner.nextDouble();
        double y2Coordinate = userInputScanner.nextDouble();

        // Declare intermediate math variables to match the distance formula
        double a; // (x2 - x1)
        double b; // (y2 - y1)
        double c; // a^2 + b^2

        // Compute the difference in x-coordinates
        a = x2Coordinate - x1Coordinate;

        // Compute the difference in y-coordinates
        b = y2Coordinate - y1Coordinate;

        // Compute the sum of squares a^2 + b^2
        c = a * a + b * b;

        // Compute the final distance using the square root of c
        double distanceBetweenPoints = Math.sqrt(c);

        // Display the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}