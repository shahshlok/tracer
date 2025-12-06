import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double x1UserInputValue = userInputScanner.nextDouble();
        double y1UserInputValue = userInputScanner.nextDouble();

        // Ask the user to enter the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double x2UserInputValue = userInputScanner.nextDouble();
        double y2UserInputValue = userInputScanner.nextDouble();

        // Declare intermediate variables to match the distance formula structure
        // a will be the difference in x-values: (x2 - x1)
        double a = x2UserInputValue - x1UserInputValue;

        // b will be the difference in y-values: (y2 - y1)
        double b = y2UserInputValue - y1UserInputValue;

        // c will be the distance value, using the formula:
        // sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        double aSquaredValue = a * a; // (x2 - x1)^2
        double bSquaredValue = b * b; // (y2 - y1)^2

        double sumOfSquaresValue = aSquaredValue + bSquaredValue; // (x2 - x1)^2 + (y2 - y1)^2

        double c = Math.sqrt(sumOfSquaresValue); // sqrt( (x2 - x1)^2 + (y2 - y1)^2 )

        // Display the result exactly as required
        System.out.println("The distance of the two points is " + c);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}