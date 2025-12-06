import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 as double values
        double userInputX1 = userInputScanner.nextDouble();
        double userInputY1 = userInputScanner.nextDouble();

        // Prompt the user to enter the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 as double values
        double userInputX2 = userInputScanner.nextDouble();
        double userInputY2 = userInputScanner.nextDouble();

        // Compute the horizontal difference (x2 - x1)
        double differenceInX = userInputX2 - userInputX1;

        // Compute the vertical difference (y2 - y1)
        double differenceInY = userInputY2 - userInputY1;

        // Declare intermediate math variables for the squared differences
        double a = differenceInX * differenceInX; // (x2 - x1)^2
        double b = differenceInY * differenceInY; // (y2 - y1)^2

        // Declare another variable for the sum of squares
        double c = a + b; // (x2 - x1)^2 + (y2 - y1)^2

        // Compute the distance using the square root of the sum of squares
        double distanceBetweenPoints = Math.sqrt(c);

        // Display the final result
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}