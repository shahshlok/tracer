import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter three points for the triangle
        System.out.println("Enter three points for a triangle.");

        // Ask for first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXCoordinate = userInputScanner.nextDouble();
        double pointOneYCoordinate = userInputScanner.nextDouble();

        // Ask for second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXCoordinate = userInputScanner.nextDouble();
        double pointTwoYCoordinate = userInputScanner.nextDouble();

        // Ask for third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXCoordinate = userInputScanner.nextDouble();
        double pointThreeYCoordinate = userInputScanner.nextDouble();

        // Now we calculate the length of each side using the distance formula
        // sideLength = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )

        // For side1: distance between point 1 and point 2
        double distanceSideOneXDifference = pointTwoXCoordinate - pointOneXCoordinate;
        double distanceSideOneYDifference = pointTwoYCoordinate - pointOneYCoordinate;
        double distanceSideOneXSquared = distanceSideOneXDifference * distanceSideOneXDifference;
        double distanceSideOneYSquared = distanceSideOneYDifference * distanceSideOneYDifference;
        double distanceSideOneSumOfSquares = distanceSideOneXSquared + distanceSideOneYSquared;
        double sideOneLength = Math.sqrt(distanceSideOneSumOfSquares);

        // For side2: distance between point 2 and point 3
        double distanceSideTwoXDifference = pointThreeXCoordinate - pointTwoXCoordinate;
        double distanceSideTwoYDifference = pointThreeYCoordinate - pointTwoYCoordinate;
        double distanceSideTwoXSquared = distanceSideTwoXDifference * distanceSideTwoXDifference;
        double distanceSideTwoYSquared = distanceSideTwoYDifference * distanceSideTwoYDifference;
        double distanceSideTwoSumOfSquares = distanceSideTwoXSquared + distanceSideTwoYSquared;
        double sideTwoLength = Math.sqrt(distanceSideTwoSumOfSquares);

        // For side3: distance between point 3 and point 1
        double distanceSideThreeXDifference = pointOneXCoordinate - pointThreeXCoordinate;
        double distanceSideThreeYDifference = pointOneYCoordinate - pointThreeYCoordinate;
        double distanceSideThreeXSquared = distanceSideThreeXDifference * distanceSideThreeXDifference;
        double distanceSideThreeYSquared = distanceSideThreeYDifference * distanceSideThreeYDifference;
        double distanceSideThreeSumOfSquares = distanceSideThreeXSquared + distanceSideThreeYSquared;
        double sideThreeLength = Math.sqrt(distanceSideThreeSumOfSquares);

        // Now we use Heron's formula
        // s = (side1 + side2 + side3) / 2
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )

        // Step 1: calculate the semi-perimeter s
        double semiPerimeterNumerator = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeter = semiPerimeterNumerator / 2.0;

        // Step 2: calculate the inner product for Heron's formula
        double heronTermOne = semiPerimeter;
        double heronTermTwo = semiPerimeter - sideOneLength;
        double heronTermThree = semiPerimeter - sideTwoLength;
        double heronTermFour = semiPerimeter - sideThreeLength;

        double heronProduct = heronTermOne * heronTermTwo * heronTermThree * heronTermFour;

        // Step 3: area is the square root of the product
        double triangleArea = Math.sqrt(heronProduct);

        // Print the area in the required format
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the scanner
        userInputScanner.close();
    }
}