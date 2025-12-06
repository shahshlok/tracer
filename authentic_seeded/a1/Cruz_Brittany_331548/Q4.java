import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter three points for a triangle
        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");

        // Read x1 and y1 as integers
        int x1InputValue = userInputScanner.nextInt();
        int y1InputValue = userInputScanner.nextInt();

        System.out.print("(x2, y2):");

        // Read x2 and y2 as integers
        int x2InputValue = userInputScanner.nextInt();
        int y2InputValue = userInputScanner.nextInt();

        System.out.print("(x3, y3):");

        // Read x3 and y3 as integers
        int x3InputValue = userInputScanner.nextInt();
        int y3InputValue = userInputScanner.nextInt();

        // Convert integer point values to double for more precise math
        double x1DoubleValue = x1InputValue;
        double y1DoubleValue = y1InputValue;
        double x2DoubleValue = x2InputValue;
        double y2DoubleValue = y2InputValue;
        double x3DoubleValue = x3InputValue;
        double y3DoubleValue = y3InputValue;

        // Calculate the length of side1 between point1 (x1, y1) and point2 (x2, y2)
        // Using distance formula: distance = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        double aSide1DifferenceX = x2DoubleValue - x1DoubleValue;
        double bSide1DifferenceY = y2DoubleValue - y1DoubleValue;
        double cSide1SquareSum = aSide1DifferenceX * aSide1DifferenceX + bSide1DifferenceY * bSide1DifferenceY;
        double side1LengthValue = Math.sqrt(cSide1SquareSum);

        // Calculate the length of side2 between point2 (x2, y2) and point3 (x3, y3)
        double aSide2DifferenceX = x3DoubleValue - x2DoubleValue;
        double bSide2DifferenceY = y3DoubleValue - y2DoubleValue;
        double cSide2SquareSum = aSide2DifferenceX * aSide2DifferenceX + bSide2DifferenceY * bSide2DifferenceY;
        double side2LengthValue = Math.sqrt(cSide2SquareSum);

        // Calculate the length of side3 between point3 (x3, y3) and point1 (x1, y1)
        double aSide3DifferenceX = x1DoubleValue - x3DoubleValue;
        double bSide3DifferenceY = y1DoubleValue - y3DoubleValue;
        double cSide3SquareSum = aSide3DifferenceX * aSide3DifferenceX + bSide3DifferenceY * bSide3DifferenceY;
        double side3LengthValue = Math.sqrt(cSide3SquareSum);

        // Now use Heron's formula to calculate the area of the triangle
        // First calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double aSemiPerimeterNumerator = side1LengthValue + side2LengthValue + side3LengthValue;
        double sSemiPerimeterValue = aSemiPerimeterNumerator / 2.0;

        // Next calculate the expression inside the square root:
        // areaSquaredExpression = s * (s - side1) * (s - side2) * (s - side3)
        double aFirstTerm = sSemiPerimeterValue;
        double bSecondTerm = sSemiPerimeterValue - side1LengthValue;
        double cThirdTerm = sSemiPerimeterValue - side2LengthValue;
        double dFourthTerm = sSemiPerimeterValue - side3LengthValue;

        double areaSquaredExpressionValue = aFirstTerm * bSecondTerm * cThirdTerm * dFourthTerm;

        // Now take the square root to get the actual area
        double triangleAreaValue = Math.sqrt(areaSquaredExpressionValue);

        // Print the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}