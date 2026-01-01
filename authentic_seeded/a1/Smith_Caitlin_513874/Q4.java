import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter three points for a triangle
        System.out.println("Enter three points for a triangle.");

        // Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        int x1Coordinate = userInputScanner.nextInt();
        int y1Coordinate = userInputScanner.nextInt();

        // Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        int x2Coordinate = userInputScanner.nextInt();
        int y2Coordinate = userInputScanner.nextInt();

        // Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        int x3Coordinate = userInputScanner.nextInt();
        int y3Coordinate = userInputScanner.nextInt();

        // Now we calculate the length of each side using the distance formula
        // sideLength = sqrt((x2 - x1)^2 + (y2 - y1)^2)

        // First side: between point 1 (x1, y1) and point 2 (x2, y2)
        double aDifferenceXForSide1 = x2Coordinate - x1Coordinate;
        double bDifferenceYForSide1 = y2Coordinate - y1Coordinate;
        double cSquareSumForSide1 = aDifferenceXForSide1 * aDifferenceXForSide1
                + bDifferenceYForSide1 * bDifferenceYForSide1;
        double side1Length = Math.sqrt(cSquareSumForSide1);

        // Second side: between point 2 (x2, y2) and point 3 (x3, y3)
        double aDifferenceXForSide2 = x3Coordinate - x2Coordinate;
        double bDifferenceYForSide2 = y3Coordinate - y2Coordinate;
        double cSquareSumForSide2 = aDifferenceXForSide2 * aDifferenceXForSide2
                + bDifferenceYForSide2 * bDifferenceYForSide2;
        double side2Length = Math.sqrt(cSquareSumForSide2);

        // Third side: between point 3 (x3, y3) and point 1 (x1, y1)
        double aDifferenceXForSide3 = x1Coordinate - x3Coordinate;
        double bDifferenceYForSide3 = y1Coordinate - y3Coordinate;
        double cSquareSumForSide3 = aDifferenceXForSide3 * aDifferenceXForSide3
                + bDifferenceYForSide3 * bDifferenceYForSide3;
        double side3Length = Math.sqrt(cSquareSumForSide3);

        // Now we use Heron's formula to calculate the area of the triangle
        // s = (side1 + side2 + side3) / 2
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))

        // Calculate the semi-perimeter s
        double aSemiPerimeterNumerator = side1Length + side2Length + side3Length;
        double bSemiPerimeterDenominator = 2.0;
        double sSemiPerimeter = aSemiPerimeterNumerator / bSemiPerimeterDenominator;

        // Calculate the inside of the square root step by step
        double aTermForArea = sSemiPerimeter;
        double bTermForArea = sSemiPerimeter - side1Length;
        double cTermForArea = sSemiPerimeter - side2Length;
        double dTermForArea = sSemiPerimeter - side3Length;

        double eProductInsideSquareRoot = aTermForArea * bTermForArea * cTermForArea * dTermForArea;

        // Now compute the area using the square root
        double triangleAreaValue = Math.sqrt(eProductInsideSquareRoot);

        // Display the result of the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}