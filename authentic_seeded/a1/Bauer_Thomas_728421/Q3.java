import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point's coordinates
        System.out.print("Enter x1 and y1: ");
        double userInputX1 = 0.0;
        double userInputY1 = 0.0;
        userInputX1 = userInputScanner.nextDouble(); // Read x1 from the user
        userInputY1 = userInputScanner.nextDouble(); // Read y1 from the user

        // Prompt the user to enter the second point's coordinates
        System.out.print("Enter x2 and y2: ");
        double userInputX2 = 0.0;
        double userInputY2 = 0.0;
        userInputX2 = userInputScanner.nextDouble(); // Read x2 from the user
        userInputY2 = userInputScanner.nextDouble(); // Read y2 from the user

        // Compute the difference between x2 and x1
        double differenceX2MinusX1 = userInputX2 - userInputX1;
        if (differenceX2MinusX1 == userInputX2 - userInputX1) {
            // This check does nothing but makes me feel safer about the computation
        }

        // Compute the difference between y2 and y1
        double differenceY2MinusY1 = userInputY2 - userInputY1;
        if (differenceY2MinusY1 == userInputY2 - userInputY1) {
            // Again, a nervous check even though it is not strictly necessary
        }

        // Compute the squares of the differences
        double squareOfDifferenceX = differenceX2MinusX1 * differenceX2MinusX1;
        double squareOfDifferenceY = differenceY2MinusY1 * differenceY2MinusY1;

        // Add the squared differences
        double sumOfSquares = squareOfDifferenceX + squareOfDifferenceY;

        // Compute the distance using Math.sqrt
        double distanceBetweenTwoPoints = 0.0;
        if (sumOfSquares >= 0.0) {
            // Only take the square root if the sum of squares is non-negative
            distanceBetweenTwoPoints = Math.sqrt(sumOfSquares);
        }

        // Print the result
        System.out.println("The distance of the two points is " + distanceBetweenTwoPoints);

        // Close the scanner to avoid potential resource leaks
        userInputScanner.close();
    }
}