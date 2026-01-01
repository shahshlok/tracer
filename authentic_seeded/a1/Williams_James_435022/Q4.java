import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Prompt the user for the three points of the triangle
        System.out.println("Enter three points for a triangle.");

        // Read point 1: (x1, y1)
        System.out.print("(x1, y1):");
        int pointOneXCoordinate = keyboardInput.nextInt(); // x1
        int pointOneYCoordinate = keyboardInput.nextInt(); // y1

        // Read point 2: (x2, y2)
        System.out.print("(x2, y2):");
        int pointTwoXCoordinate = keyboardInput.nextInt(); // x2
        int pointTwoYCoordinate = keyboardInput.nextInt(); // y2

        // Read point 3: (x3, y3)
        System.out.print("(x3, y3):");
        int pointThreeXCoordinate = keyboardInput.nextInt(); // x3
        int pointThreeYCoordinate = keyboardInput.nextInt(); // y3

        // Now we will calculate the lengths of the three sides of the triangle
        // using the distance formula between two points:
        // distance = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )

        // Side 1 is between point 1 and point 2
        double distanceSideOneAXDifference = pointTwoXCoordinate - pointOneXCoordinate;
        double distanceSideOneAYDifference = pointTwoYCoordinate - pointOneYCoordinate;
        double distanceSideOneASquaredX = distanceSideOneAXDifference * distanceSideOneAXDifference;
        double distanceSideOneASquaredY = distanceSideOneAYDifference * distanceSideOneAYDifference;
        double distanceSideOneASumSquares = distanceSideOneASquaredX + distanceSideOneASquaredY;
        double sideOneLength = Math.sqrt(distanceSideOneASumSquares);

        // Side 2 is between point 2 and point 3
        double distanceSideTwoBXDifference = pointThreeXCoordinate - pointTwoXCoordinate;
        double distanceSideTwoBYDifference = pointThreeYCoordinate - pointTwoYCoordinate;
        double distanceSideTwoBSquaredX = distanceSideTwoBXDifference * distanceSideTwoBXDifference;
        double distanceSideTwoBSquaredY = distanceSideTwoBYDifference * distanceSideTwoBYDifference;
        double distanceSideTwoBSumSquares = distanceSideTwoBSquaredX + distanceSideTwoBSquaredY;
        double sideTwoLength = Math.sqrt(distanceSideTwoBSumSquares);

        // Side 3 is between point 1 and point 3
        double distanceSideThreeCXDifference = pointThreeXCoordinate - pointOneXCoordinate;
        double distanceSideThreeCYDifference = pointThreeYCoordinate - pointOneYCoordinate;
        double distanceSideThreeCSquaredX = distanceSideThreeCXDifference * distanceSideThreeCXDifference;
        double distanceSideThreeCSquaredY = distanceSideThreeCYDifference * distanceSideThreeCYDifference;
        double distanceSideThreeCSumSquares = distanceSideThreeCSquaredX + distanceSideThreeCSquaredY;
        double sideThreeLength = Math.sqrt(distanceSideThreeCSumSquares);

        // Now we will use Heron's formula to calculate the area of the triangle.
        // First compute s = (side1 + side2 + side3) / 2
        double heronFormulaA = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterS = heronFormulaA / 2.0;

        // Then area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double heronFormulaB = semiPerimeterS - sideOneLength;
        double heronFormulaC = semiPerimeterS - sideTwoLength;
        double heronFormulaD = semiPerimeterS - sideThreeLength;

        double heronFormulaProduct = semiPerimeterS * heronFormulaB * heronFormulaC * heronFormulaD;

        double triangleArea = Math.sqrt(heronFormulaProduct);

        // Display the result of the area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the scanner
        keyboardInput.close();
    }
}