import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Initialize all coordinate values to 0 because we will register input later
        double x1CoordinateValue = 0;
        double y1CoordinateValue = 0;
        double x2CoordinateValue = 0;
        double y2CoordinateValue = 0;

        // Prompt the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Call nextDouble() to register the two values the user enters
        keyboardInput.nextDouble();
        keyboardInput.nextDouble();

        // Prompt the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Call nextDouble() again to register the next two values
        keyboardInput.nextDouble();
        keyboardInput.nextDouble();

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