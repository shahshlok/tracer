import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Declare variables for the coordinates of the three points of the triangle
        double x1CoordinateValue;
        double y1CoordinateValue;
        double x2CoordinateValue;
        double y2CoordinateValue;
        double x3CoordinateValue;
        double y3CoordinateValue;

        // Prompt the user to enter three points for a triangle
        System.out.println("Enter three points for a triangle.");

        // Prompt and read the first point (x1, y1)
        System.out.print("(x1, y1):");
        x1CoordinateValue = userInputScanner.nextDouble();
        y1CoordinateValue = userInputScanner.nextDouble();

        // Prompt and read the second point (x2, y2)
        System.out.print("(x2, y2):");
        x2CoordinateValue = userInputScanner.nextDouble();
        y2CoordinateValue = userInputScanner.nextDouble();

        // Prompt and read the third point (x3, y3)
        System.out.print("(x3, y3):");
        x3CoordinateValue = userInputScanner.nextDouble();
        y3CoordinateValue = userInputScanner.nextDouble();

        // Calculate the lengths of the sides of the triangle using the distance formula
        // side1 is distance between point1 (x1, y1) and point2 (x2, y2)
        double side1DistanceValue;
        // side2 is distance between point2 (x2, y2) and point3 (x3, y3)
        double side2DistanceValue;
        // side3 is distance between point3 (x3, y3) and point1 (x1, y1)
        double side3DistanceValue;

        // Use the distance formula: sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        // First compute differences for side1
        double side1DifferenceXValue = x2CoordinateValue - x1CoordinateValue;
        double side1DifferenceYValue = y2CoordinateValue - y1CoordinateValue;
        double side1SquaredXValue = side1DifferenceXValue * side1DifferenceXValue;
        double side1SquaredYValue = side1DifferenceYValue * side1DifferenceYValue;
        double side1SumOfSquaresValue = side1SquaredXValue + side1SquaredYValue;
        side1DistanceValue = Math.sqrt(side1SumOfSquaresValue);

        // Compute differences for side2
        double side2DifferenceXValue = x3CoordinateValue - x2CoordinateValue;
        double side2DifferenceYValue = y3CoordinateValue - y2CoordinateValue;
        double side2SquaredXValue = side2DifferenceXValue * side2DifferenceXValue;
        double side2SquaredYValue = side2DifferenceYValue * side2DifferenceYValue;
        double side2SumOfSquaresValue = side2SquaredXValue + side2SquaredYValue;
        side2DistanceValue = Math.sqrt(side2SumOfSquaresValue);

        // Compute differences for side3
        double side3DifferenceXValue = x1CoordinateValue - x3CoordinateValue;
        double side3DifferenceYValue = y1CoordinateValue - y3CoordinateValue;
        double side3SquaredXValue = side3DifferenceXValue * side3DifferenceXValue;
        double side3SquaredYValue = side3DifferenceYValue * side3DifferenceYValue;
        double side3SumOfSquaresValue = side3SquaredXValue + side3SquaredYValue;
        side3DistanceValue = Math.sqrt(side3SumOfSquaresValue);

        // Now use Heron's formula to calculate the area of the triangle
        // First compute the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterValue;
        double sumOfSidesValue = side1DistanceValue + side2DistanceValue + side3DistanceValue;
        // I see the formula as (side1 + side2 + side3) all over 2, 
        // so I write it directly without extra parentheses
        semiPerimeterValue = side1DistanceValue + side2DistanceValue + side3DistanceValue / 2.0;

        // Now compute the expression under the square root:
        // s(s - side1)(s - side2)(s - side3)
        double heronExpressionAValue = semiPerimeterValue - side1DistanceValue;
        double heronExpressionBValue = semiPerimeterValue - side2DistanceValue;
        double heronExpressionCValue = semiPerimeterValue - side3DistanceValue;

        double heronProductValue = semiPerimeterValue * heronExpressionAValue * heronExpressionBValue * heronExpressionCValue;

        // Take the square root to get the area
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}