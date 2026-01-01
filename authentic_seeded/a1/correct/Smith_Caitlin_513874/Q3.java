import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Prompt the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 as double values
        double x1CoordinateValue = keyboardInput.nextDouble();
        double y1CoordinateValue = keyboardInput.nextDouble();

        // Prompt the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 as double values
        double x2CoordinateValue = keyboardInput.nextDouble();
        double y2CoordinateValue = keyboardInput.nextDouble();

        // Declare intermediate math variables for the formula
        double aDifferenceInX = x2CoordinateValue - x1CoordinateValue;   // (x2 - x1)
        double bDifferenceInY = y2CoordinateValue - y1CoordinateValue;   // (y2 - y1)

        // Square the differences
        double cSquareOfDifferenceInX = aDifferenceInX * aDifferenceInX; // (x2 - x1)^2
        double dSquareOfDifferenceInY = bDifferenceInY * bDifferenceInY; // (y2 - y1)^2

        // Add the squared differences
        double eSumOfSquares = cSquareOfDifferenceInX + dSquareOfDifferenceInY; // (x2 - x1)^2 + (y2 - y1)^2

        // Take the square root of the sum to get the distance
        double distanceBetweenTwoPoints = Math.sqrt(eSumOfSquares); // sqrt((x2 - x1)^2 + (y2 - y1)^2)

        // Display the result
        System.out.println("The distance of the two points is " + distanceBetweenTwoPoints);

        // Close the Scanner
        keyboardInput.close();
    }
}