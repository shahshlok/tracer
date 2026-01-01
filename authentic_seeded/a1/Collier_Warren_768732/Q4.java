import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what to enter
        System.out.println("Enter three points for a triangle.");

        // Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double thirdPointXValue = userInputScanner.nextDouble();
        double thirdPointYValue = userInputScanner.nextDouble();

        // Calculate the three side lengths using the distance formula
        // side1 is distance between point1 and point2
        double side1DifferenceX = secondPointXValue - firstPointXValue;
        double side1DifferenceY = secondPointYValue - firstPointYValue;
        double side1DifferenceXSquared = side1DifferenceX * side1DifferenceX;
        double side1DifferenceYSquared = side1DifferenceY * side1DifferenceY;
        double side1UnderSquareRoot = side1DifferenceXSquared + side1DifferenceYSquared;
        if (side1UnderSquareRoot < 0) {
            side1UnderSquareRoot = 0; // extra safety check, even though it should not be negative
        }
        double side1Length = Math.sqrt(side1UnderSquareRoot);

        // side2 is distance between point2 and point3
        double side2DifferenceX = thirdPointXValue - secondPointXValue;
        double side2DifferenceY = thirdPointYValue - secondPointYValue;
        double side2DifferenceXSquared = side2DifferenceX * side2DifferenceX;
        double side2DifferenceYSquared = side2DifferenceY * side2DifferenceY;
        double side2UnderSquareRoot = side2DifferenceXSquared + side2DifferenceYSquared;
        if (side2UnderSquareRoot < 0) {
            side2UnderSquareRoot = 0; // extra safety check
        }
        double side2Length = Math.sqrt(side2UnderSquareRoot);

        // side3 is distance between point3 and point1
        double side3DifferenceX = firstPointXValue - thirdPointXValue;
        double side3DifferenceY = firstPointYValue - thirdPointYValue;
        double side3DifferenceXSquared = side3DifferenceX * side3DifferenceX;
        double side3DifferenceYSquared = side3DifferenceY * side3DifferenceY;
        double side3UnderSquareRoot = side3DifferenceXSquared + side3DifferenceYSquared;
        if (side3UnderSquareRoot < 0) {
            side3UnderSquareRoot = 0; // extra safety check
        }
        double side3Length = Math.sqrt(side3UnderSquareRoot);

        // Now apply Heron's formula
        // First compute the perimeter in double
        double trianglePerimeterSum = side1Length + side2Length + side3Length;

        // Convert perimeter to an int first because I want to make sure the division is clean
        int trianglePerimeterSumAsInt = (int) trianglePerimeterSum;

        // s = (side1 + side2 + side3) / 2
        // I know integer division is bad, so I will cast the result of the division to double
        double semiPerimeterValue = (double) (trianglePerimeterSumAsInt / 2);

        // Calculate each (s - side) part separately
        double semiPerimeterMinusSide1 = semiPerimeterValue - side1Length;
        double semiPerimeterMinusSide2 = semiPerimeterValue - side2Length;
        double semiPerimeterMinusSide3 = semiPerimeterValue - side3Length;

        // Multiply all parts together for the value under the square root
        double heronUnderSquareRoot = semiPerimeterValue * semiPerimeterMinusSide1 * semiPerimeterMinusSide2 * semiPerimeterMinusSide3;

        // Due to possible floating point rounding issues, make sure it is not negative
        if (heronUnderSquareRoot < 0) {
            heronUnderSquareRoot = 0;
        }

        // Now take the square root to get the area
        double triangleAreaValue = Math.sqrt(heronUnderSquareRoot);

        // Output the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner (even though program is ending, just to be safe)
        userInputScanner.close();
    }
}