import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter three points for a triangle
        System.out.println("Enter three points for a triangle.");

        // Ask for and read the first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Ask for and read the second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Ask for and read the third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3CoordinateValue = userInputScanner.nextDouble();
        double y3CoordinateValue = userInputScanner.nextDouble();

        // Use the distance formula from Question 3 to calculate the length of each side of the triangle

        // First compute common intermediate variables for side1 between point1 (x1, y1) and point2 (x2, y2)
        double side1DifferenceX = x2CoordinateValue - x1CoordinateValue;
        double side1DifferenceY = y2CoordinateValue - y1CoordinateValue;
        double side1SquareX = side1DifferenceX * side1DifferenceX;
        double side1SquareY = side1DifferenceY * side1DifferenceY;
        double side1SumOfSquares = side1SquareX + side1SquareY;
        double side1Length = Math.sqrt(side1SumOfSquares);

        // Compute intermediate variables for side2 between point2 (x2, y2) and point3 (x3, y3)
        double side2DifferenceX = x3CoordinateValue - x2CoordinateValue;
        double side2DifferenceY = y3CoordinateValue - y2CoordinateValue;
        double side2SquareX = side2DifferenceX * side2DifferenceX;
        double side2SquareY = side2DifferenceY * side2DifferenceY;
        double side2SumOfSquares = side2SquareX + side2SquareY;
        double side2Length = Math.sqrt(side2SumOfSquares);

        // Compute intermediate variables for side3 between point3 (x3, y3) and point1 (x1, y1)
        double side3DifferenceX = x1CoordinateValue - x3CoordinateValue;
        double side3DifferenceY = y1CoordinateValue - y3CoordinateValue;
        double side3SquareX = side3DifferenceX * side3DifferenceX;
        double side3SquareY = side3DifferenceY * side3DifferenceY;
        double side3SumOfSquares = side3SquareX + side3SquareY;
        double side3Length = Math.sqrt(side3SumOfSquares);

        // Now use Heron's formula to compute the area of the triangle

        // First compute the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterNumerator = side1Length + side2Length + side3Length;
        double semiPerimeterValue = semiPerimeterNumerator / 2.0;

        // Then compute the expression inside the square root:
        // semiPerimeterValue * (semiPerimeterValue - side1) * (semiPerimeterValue - side2) * (semiPerimeterValue - side3)
        double heronTermA = semiPerimeterValue;
        double heronTermB = semiPerimeterValue - side1Length;
        double heronTermC = semiPerimeterValue - side2Length;
        double heronTermD = semiPerimeterValue - side3Length;
        double heronProductValue = heronTermA * heronTermB * heronTermC * heronTermD;

        // Finally compute the area as the square root of that product
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner
        userInputScanner.close();
    }
}