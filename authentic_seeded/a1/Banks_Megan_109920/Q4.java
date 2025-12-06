import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the three points of a triangle
        System.out.println("Enter three points for a triangle.");

        // Read first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Read second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Read third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3CoordinateValue = userInputScanner.nextDouble();
        double y3CoordinateValue = userInputScanner.nextDouble();

        // Calculate length of side1 between point1 (x1, y1) and point2 (x2, y2)
        double side1DifferenceXValue = x2CoordinateValue - x1CoordinateValue;
        double side1DifferenceYValue = y2CoordinateValue - y1CoordinateValue;
        double side1SquareXValue = side1DifferenceXValue * side1DifferenceXValue;
        double side1SquareYValue = side1DifferenceYValue * side1DifferenceYValue;
        double side1SumOfSquaresValue = side1SquareXValue + side1SquareYValue;
        double side1LengthValue = Math.sqrt(side1SumOfSquaresValue);

        // Calculate length of side2 between point2 (x2, y2) and point3 (x3, y3)
        double side2DifferenceXValue = x3CoordinateValue - x2CoordinateValue;
        double side2DifferenceYValue = y3CoordinateValue - y2CoordinateValue;
        double side2SquareXValue = side2DifferenceXValue * side2DifferenceXValue;
        double side2SquareYValue = side2DifferenceYValue * side2DifferenceYValue;
        double side2SumOfSquaresValue = side2SquareXValue + side2SquareYValue;
        double side2LengthValue = Math.sqrt(side2SumOfSquaresValue);

        // Calculate length of side3 between point3 (x3, y3) and point1 (x1, y1)
        double side3DifferenceXValue = x1CoordinateValue - x3CoordinateValue;
        double side3DifferenceYValue = y1CoordinateValue - y3CoordinateValue;
        double side3SquareXValue = side3DifferenceXValue * side3DifferenceXValue;
        double side3SquareYValue = side3DifferenceYValue * side3DifferenceYValue;
        double side3SumOfSquaresValue = side3SquareXValue + side3SquareYValue;
        double side3LengthValue = Math.sqrt(side3SumOfSquaresValue);

        // Use Heron's formula to calculate the area of the triangle
        // First calculate s = (side1 + side2 + side3) / 2
        double semiPerimeterNumeratorValue = side1LengthValue + side2LengthValue + side3LengthValue;
        double semiPerimeterDenominatorValue = 2.0;
        double semiPerimeterValue = semiPerimeterNumeratorValue / semiPerimeterDenominatorValue;

        // Now calculate the expression inside the square root:
        // s(s - side1)(s - side2)(s - side3)
        double differenceSemiPerimeterSide1Value = semiPerimeterValue - side1LengthValue;
        double differenceSemiPerimeterSide2Value = semiPerimeterValue - side2LengthValue;
        double differenceSemiPerimeterSide3Value = semiPerimeterValue - side3LengthValue;

        double heronFactorAValue = semiPerimeterValue;
        double heronFactorBValue = differenceSemiPerimeterSide1Value;
        double heronFactorCValue = differenceSemiPerimeterSide2Value;
        double heronFactorDValue = differenceSemiPerimeterSide3Value;

        double heronProductValue = heronFactorAValue * heronFactorBValue * heronFactorCValue * heronFactorDValue;

        // The area is the square root of the heronProductValue
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}