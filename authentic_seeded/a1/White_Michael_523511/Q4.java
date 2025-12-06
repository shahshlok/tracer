import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the three points of a triangle
        System.out.println("Enter three points for a triangle.");

        // Declare variables for the coordinates of the three points
        double x1Coordinate = 0.0;
        double y1Coordinate = 0.0;
        double x2Coordinate = 0.0;
        double y2Coordinate = 0.0;
        double x3Coordinate = 0.0;
        double y3Coordinate = 0.0;

        // Ask for (x1, y1)
        System.out.print("(x1, y1):");
        // Read x1 and y1 from user input
        x1Coordinate = userInputScanner.nextDouble();
        y1Coordinate = userInputScanner.nextDouble();

        // Ask for (x2, y2)
        System.out.print("(x2, y2):");
        // Read x2 and y2 from user input
        x2Coordinate = userInputScanner.nextDouble();
        y2Coordinate = userInputScanner.nextDouble();

        // Ask for (x3, y3)
        System.out.print("(x3, y3):");
        // Read x3 and y3 from user input
        x3Coordinate = userInputScanner.nextDouble();
        y3Coordinate = userInputScanner.nextDouble();

        // Calculate the lengths of each side using the distance formula
        // side1 is the distance between point1 (x1, y1) and point2 (x2, y2)
        double side1DifferenceX = x2Coordinate - x1Coordinate;
        double side1DifferenceY = y2Coordinate - y1Coordinate;
        double side1SquareX = side1DifferenceX * side1DifferenceX;
        double side1SquareY = side1DifferenceY * side1DifferenceY;
        double side1SumSquares = side1SquareX + side1SquareY;
        double side1Length = Math.sqrt(side1SumSquares);

        // side2 is the distance between point2 (x2, y2) and point3 (x3, y3)
        double side2DifferenceX = x3Coordinate - x2Coordinate;
        double side2DifferenceY = y3Coordinate - y2Coordinate;
        double side2SquareX = side2DifferenceX * side2DifferenceX;
        double side2SquareY = side2DifferenceY * side2DifferenceY;
        double side2SumSquares = side2SquareX + side2SquareY;
        double side2Length = Math.sqrt(side2SumSquares);

        // side3 is the distance between point3 (x3, y3) and point1 (x1, y1)
        double side3DifferenceX = x1Coordinate - x3Coordinate;
        double side3DifferenceY = y1Coordinate - y3Coordinate;
        double side3SquareX = side3DifferenceX * side3DifferenceX;
        double side3SquareY = side3DifferenceY * side3DifferenceY;
        double side3SumSquares = side3SquareX + side3SquareY;
        double side3Length = Math.sqrt(side3SumSquares);

        // Now we use Heron's formula to calculate the area
        // First calculate s = (side1 + side2 + side3) / 2 using an int and then cast the result
        double sumOfSides = side1Length + side2Length + side3Length;

        // Create an int version of the sum of sides to avoid issues
        int sumOfSidesAsInt = (int) sumOfSides;

        // Use integer division first, then cast the result to double
        double semiPerimeterS = (double) (sumOfSidesAsInt / 2);

        // To be very careful, check if the semiPerimeterS is non-negative
        if (semiPerimeterS < 0.0) {
            semiPerimeterS = 0.0;
        }

        // Calculate the terms (s - side1), (s - side2), (s - side3)
        double semiPerimeterMinusSide1 = semiPerimeterS - side1Length;
        double semiPerimeterMinusSide2 = semiPerimeterS - side2Length;
        double semiPerimeterMinusSide3 = semiPerimeterS - side3Length;

        // Calculate the inside of the square root: s * (s - side1) * (s - side2) * (s - side3)
        double heronInsideSquareRoot = semiPerimeterS * semiPerimeterMinusSide1 * semiPerimeterMinusSide2 * semiPerimeterMinusSide3;

        // Be nervous about negative due to floating point; if negative but very close to zero, clamp to zero
        if (heronInsideSquareRoot < 0.0) {
            // In a very strict sense, area cannot be negative. Set to 0 if negative.
            heronInsideSquareRoot = 0.0;
        }

        // Finally calculate the area using square root
        double triangleArea = 0.0;
        if (heronInsideSquareRoot >= 0.0) {
            triangleArea = Math.sqrt(heronInsideSquareRoot);
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);
    }
}