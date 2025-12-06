import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask for first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1InputValue = userInputScanner.nextDouble();
        double y1InputValue = userInputScanner.nextDouble();

        // Ask for second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2InputValue = userInputScanner.nextDouble();
        double y2InputValue = userInputScanner.nextDouble();

        // Ask for third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3InputValue = userInputScanner.nextDouble();
        double y3InputValue = userInputScanner.nextDouble();

        // Calculate side1 using distance formula between point1 and point2
        double side1DifferenceX = x2InputValue - x1InputValue;
        double side1DifferenceY = y2InputValue - y1InputValue;
        double side1DifferenceXSquared = side1DifferenceX * side1DifferenceX;
        double side1DifferenceYSquared = side1DifferenceY * side1DifferenceY;
        double side1SumOfSquares = side1DifferenceXSquared + side1DifferenceYSquared;
        double side1LengthValue = Math.sqrt(side1SumOfSquares);

        // Calculate side2 using distance formula between point2 and point3
        double side2DifferenceX = x3InputValue - x2InputValue;
        double side2DifferenceY = y3InputValue - y2InputValue;
        double side2DifferenceXSquared = side2DifferenceX * side2DifferenceX;
        double side2DifferenceYSquared = side2DifferenceY * side2DifferenceY;
        double side2SumOfSquares = side2DifferenceXSquared + side2DifferenceYSquared;
        double side2LengthValue = Math.sqrt(side2SumOfSquares);

        // Calculate side3 using distance formula between point3 and point1
        double side3DifferenceX = x1InputValue - x3InputValue;
        double side3DifferenceY = y1InputValue - y3InputValue;
        double side3DifferenceXSquared = side3DifferenceX * side3DifferenceX;
        double side3DifferenceYSquared = side3DifferenceY * side3DifferenceY;
        double side3SumOfSquares = side3DifferenceXSquared + side3DifferenceYSquared;
        double side3LengthValue = Math.sqrt(side3SumOfSquares);

        // Now apply Heron's formula
        // First calculate s = (side1 + side2 + side3) / 2 using integers then cast result
        int side1LengthAsInt = (int) side1LengthValue;
        int side2LengthAsInt = (int) side2LengthValue;
        int side3LengthAsInt = (int) side3LengthValue;

        int semiPerimeterNumeratorInt = side1LengthAsInt + side2LengthAsInt + side3LengthAsInt;
        int semiPerimeterInt = semiPerimeterNumeratorInt / 2;

        double semiPerimeterValue = (double) semiPerimeterInt;

        // Calculate (s - side1), (s - side2), (s - side3) carefully
        double semiPerimeterMinusSide1 = semiPerimeterValue - side1LengthValue;
        double semiPerimeterMinusSide2 = semiPerimeterValue - side2LengthValue;
        double semiPerimeterMinusSide3 = semiPerimeterValue - side3LengthValue;

        // Calculate the value under the square root: s * (s - side1) * (s - side2) * (s - side3)
        double heronUnderSquareRootValue = semiPerimeterValue * semiPerimeterMinusSide1;
        heronUnderSquareRootValue = heronUnderSquareRootValue * semiPerimeterMinusSide2;
        heronUnderSquareRootValue = heronUnderSquareRootValue * semiPerimeterMinusSide3;

        // Because of floating point issues, heronUnderSquareRootValue might be slightly negative
        // even when it should be zero, so we add an explicit safety check
        if (heronUnderSquareRootValue < 0 && heronUnderSquareRootValue > -0.0000001) {
            heronUnderSquareRootValue = 0.0;
        }

        // Initialize area to 0.0 just in case
        double triangleAreaValue = 0.0;

        // Only take square root if the value is not negative
        if (heronUnderSquareRootValue >= 0.0) {
            triangleAreaValue = Math.sqrt(heronUnderSquareRootValue);
        }

        // Print the final area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}