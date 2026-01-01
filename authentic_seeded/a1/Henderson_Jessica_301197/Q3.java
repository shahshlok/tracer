import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Prompt the user to enter the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double userInputX1 = keyboardInput.nextDouble();
        double userInputY1 = keyboardInput.nextDouble();

        // Prompt the user to enter the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double userInputX2 = keyboardInput.nextDouble();
        double userInputY2 = keyboardInput.nextDouble();

        // Compute the horizontal difference between the x-coordinates: (x2 - x1)
        double horizontalDifference = userInputX2 - userInputX1;

        // Compute the vertical difference between the y-coordinates: (y2 - y1)
        double verticalDifference = userInputY2 - userInputY1;

        // Declare intermediate math variables to mirror the distance formula structure
        // a will represent (x2 - x1)^2
        double a = horizontalDifference * horizontalDifference;

        // b will represent (y2 - y1)^2
        double b = verticalDifference * verticalDifference;

        // c will represent the sum (x2 - x1)^2 + (y2 - y1)^2
        double c = a + b;

        // Now compute the square root of c to get the distance between the two points
        double distanceBetweenPoints = Math.sqrt(c);

        // Display the final distance result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to free system resources
        keyboardInput.close();
    }
}