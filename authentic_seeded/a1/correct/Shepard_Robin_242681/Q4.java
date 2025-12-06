import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what to enter
        System.out.println("Enter three points for a triangle.");

        // Ask for first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();  // Read x1
        double pointOneYValue = userInputScanner.nextDouble();  // Read y1

        // Ask for second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();  // Read x2
        double pointTwoYValue = userInputScanner.nextDouble();  // Read y2

        // Ask for third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();  // Read x3
        double pointThreeYValue = userInputScanner.nextDouble();  // Read y3

        // Calculate length of side1 between point 1 and point 2 using distance formula
        double sideOneDifferenceXValue = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceYValue = pointTwoYValue - pointOneYValue;
        double sideOneDifferenceXSquared = sideOneDifferenceXValue * sideOneDifferenceXValue;
        double sideOneDifferenceYSquared = sideOneDifferenceYValue * sideOneDifferenceYValue;
        double sideOneSumOfSquares = sideOneDifferenceXSquared + sideOneDifferenceYSquared;
        double sideOneLengthValue = 0.0;
        if (sideOneSumOfSquares >= 0) {
            sideOneLengthValue = Math.sqrt(sideOneSumOfSquares);
        }

        // Calculate length of side2 between point 2 and point 3 using distance formula
        double sideTwoDifferenceXValue = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceYValue = pointThreeYValue - pointTwoYValue;
        double sideTwoDifferenceXSquared = sideTwoDifferenceXValue * sideTwoDifferenceXValue;
        double sideTwoDifferenceYSquared = sideTwoDifferenceYValue * sideTwoDifferenceYValue;
        double sideTwoSumOfSquares = sideTwoDifferenceXSquared + sideTwoDifferenceYSquared;
        double sideTwoLengthValue = 0.0;
        if (sideTwoSumOfSquares >= 0) {
            sideTwoLengthValue = Math.sqrt(sideTwoSumOfSquares);
        }

        // Calculate length of side3 between point 3 and point 1 using distance formula
        double sideThreeDifferenceXValue = pointOneXValue - pointThreeXValue;
        double sideThreeDifferenceYValue = pointOneYValue - pointThreeYValue;
        double sideThreeDifferenceXSquared = sideThreeDifferenceXValue * sideThreeDifferenceXValue;
        double sideThreeDifferenceYSquared = sideThreeDifferenceYValue * sideThreeDifferenceYValue;
        double sideThreeSumOfSquares = sideThreeDifferenceXSquared + sideThreeDifferenceYSquared;
        double sideThreeLengthValue = 0.0;
        if (sideThreeSumOfSquares >= 0) {
            sideThreeLengthValue = Math.sqrt(sideThreeSumOfSquares);
        }

        // Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSidesValue = sideOneLengthValue + sideTwoLengthValue + sideThreeLengthValue;
        double semiPerimeterValue = 0.0;
        if (sumOfAllSidesValue != 0) {
            semiPerimeterValue = sumOfAllSidesValue / 2.0;
        }

        // Use Heron's formula: area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double semiPerimeterMinusSideOneValue = semiPerimeterValue - sideOneLengthValue;
        double semiPerimeterMinusSideTwoValue = semiPerimeterValue - sideTwoLengthValue;
        double semiPerimeterMinusSideThreeValue = semiPerimeterValue - sideThreeLengthValue;

        // Calculate the product inside the square root
        double heronInsideProductValue = semiPerimeterValue
                * semiPerimeterMinusSideOneValue
                * semiPerimeterMinusSideTwoValue
                * semiPerimeterMinusSideThreeValue;

        // Initialize area value
        double triangleAreaValue = 0.0;

        // Only take square root if the inside product is not negative
        if (heronInsideProductValue >= 0) {
            triangleAreaValue = Math.sqrt(heronInsideProductValue);
        }

        // Close the scanner to be safe
        userInputScanner.close();

        // Print the result in the required format
        System.out.println("The area of the triangle is " + triangleAreaValue);
    }
}