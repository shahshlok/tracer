import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the three points of the triangle
        System.out.println("Enter three points for a triangle.");

        // Read first point (x1, y1)
        System.out.print("(x1, y1):");
        int x1InputValue = userInputScanner.nextInt();
        int y1InputValue = userInputScanner.nextInt();

        // Read second point (x2, y2)
        System.out.print("(x2, y2):");
        int x2InputValue = userInputScanner.nextInt();
        int y2InputValue = userInputScanner.nextInt();

        // Read third point (x3, y3)
        System.out.print("(x3, y3):");
        int x3InputValue = userInputScanner.nextInt();
        int y3InputValue = userInputScanner.nextInt();

        // Use distance formula to calculate each side of the triangle
        // side = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )

        // Calculate side1 between point1 and point2
        double side1DifferenceXValue = x2InputValue - x1InputValue;
        double side1DifferenceYValue = y2InputValue - y1InputValue;
        double side1SquareXValue = side1DifferenceXValue * side1DifferenceXValue;
        double side1SquareYValue = side1DifferenceYValue * side1DifferenceYValue;
        double side1LengthValue = Math.sqrt(side1SquareXValue + side1SquareYValue);

        // Calculate side2 between point2 and point3
        double side2DifferenceXValue = x3InputValue - x2InputValue;
        double side2DifferenceYValue = y3InputValue - y2InputValue;
        double side2SquareXValue = side2DifferenceXValue * side2DifferenceXValue;
        double side2SquareYValue = side2DifferenceYValue * side2DifferenceYValue;
        double side2LengthValue = Math.sqrt(side2SquareXValue + side2SquareYValue);

        // Calculate side3 between point3 and point1
        double side3DifferenceXValue = x1InputValue - x3InputValue;
        double side3DifferenceYValue = y1InputValue - y3InputValue;
        double side3SquareXValue = side3DifferenceXValue * side3DifferenceXValue;
        double side3SquareYValue = side3DifferenceYValue * side3DifferenceYValue;
        double side3LengthValue = Math.sqrt(side3SquareXValue + side3SquareYValue);

        // Apply Heron's formula
        // s = (side1 + side2 + side3) / 2
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )

        double semiPerimeterSValue = (side1LengthValue + side2LengthValue + side3LengthValue) / 2.0;

        double heronFactorAValue = semiPerimeterSValue;
        double heronFactorBValue = semiPerimeterSValue - side1LengthValue;
        double heronFactorCValue = semiPerimeterSValue - side2LengthValue;
        double heronFactorDValue = semiPerimeterSValue - side3LengthValue;

        double triangleAreaSquaredInsideRootValue = heronFactorAValue * heronFactorBValue * heronFactorCValue * heronFactorDValue;

        double triangleAreaValue = Math.sqrt(triangleAreaSquaredInsideRootValue);

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner
        userInputScanner.close();
    }
}