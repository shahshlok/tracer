import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program expects
        System.out.println("Enter three points for a triangle.");

        // Declare variables for the three points (x1, y1), (x2, y2), (x3, y3)
        double pointOneXValue = 0.0;
        double pointOneYValue = 0.0;
        double pointTwoXValue = 0.0;
        double pointTwoYValue = 0.0;
        double pointThreeXValue = 0.0;
        double pointThreeYValue = 0.0;

        // Prompt for first point
        System.out.print("(x1, y1):");
        // Read x1 and y1 from the user
        if (userInputScanner.hasNextDouble()) {
            pointOneXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointOneYValue = userInputScanner.nextDouble();
        }

        // Prompt for second point
        System.out.print("(x2, y2):");
        // Read x2 and y2 from the user
        if (userInputScanner.hasNextDouble()) {
            pointTwoXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointTwoYValue = userInputScanner.nextDouble();
        }

        // Prompt for third point
        System.out.print("(x3, y3):");
        // Read x3 and y3 from the user
        if (userInputScanner.hasNextDouble()) {
            pointThreeXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointThreeYValue = userInputScanner.nextDouble();
        }

        // Calculate the lengths of the sides using the distance formula from Q3

        // side1 is distance between point 1 and point 2
        double sideOneDifferenceXValue = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceYValue = pointTwoYValue - pointOneYValue;
        double sideOneDifferenceXSquaredValue = sideOneDifferenceXValue * sideOneDifferenceXValue;
        double sideOneDifferenceYSquaredValue = sideOneDifferenceYValue * sideOneDifferenceYValue;
        double sideOneSumOfSquaresValue = sideOneDifferenceXSquaredValue + sideOneDifferenceYSquaredValue;
        double sideOneLengthValue = Math.sqrt(sideOneSumOfSquaresValue);

        // side2 is distance between point 2 and point 3
        double sideTwoDifferenceXValue = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceYValue = pointThreeYValue - pointTwoYValue;
        double sideTwoDifferenceXSquaredValue = sideTwoDifferenceXValue * sideTwoDifferenceXValue;
        double sideTwoDifferenceYSquaredValue = sideTwoDifferenceYValue * sideTwoDifferenceYValue;
        double sideTwoSumOfSquaresValue = sideTwoDifferenceXSquaredValue + sideTwoDifferenceYSquaredValue;
        double sideTwoLengthValue = Math.sqrt(sideTwoSumOfSquaresValue);

        // side3 is distance between point 1 and point 3
        double sideThreeDifferenceXValue = pointThreeXValue - pointOneXValue;
        double sideThreeDifferenceYValue = pointThreeYValue - pointOneYValue;
        double sideThreeDifferenceXSquaredValue = sideThreeDifferenceXValue * sideThreeDifferenceXValue;
        double sideThreeDifferenceYSquaredValue = sideThreeDifferenceYValue * sideThreeDifferenceYValue;
        double sideThreeSumOfSquaresValue = sideThreeDifferenceXSquaredValue + sideThreeDifferenceYSquaredValue;
        double sideThreeLengthValue = Math.sqrt(sideThreeSumOfSquaresValue);

        // Use Heron's formula to calculate the area
        // s = (side1 + side2 + side3) / 2
        double semiPerimeterNumeratorValue = sideOneLengthValue + sideTwoLengthValue + sideThreeLengthValue;
        double semiPerimeterValue = semiPerimeterNumeratorValue / 2.0;

        // Now calculate area = sqrt(s (s - side1) (s - side2) (s - side3))
        double semiPerimeterMinusSideOneValue = semiPerimeterValue - sideOneLengthValue;
        double semiPerimeterMinusSideTwoValue = semiPerimeterValue - sideTwoLengthValue;
        double semiPerimeterMinusSideThreeValue = semiPerimeterValue - sideThreeLengthValue;

        // Nervous about edge cases: ensure none of the terms are negative when they should not be
        if (semiPerimeterValue < 0) {
            semiPerimeterValue = 0;
        }
        if (semiPerimeterMinusSideOneValue < 0) {
            semiPerimeterMinusSideOneValue = 0;
        }
        if (semiPerimeterMinusSideTwoValue < 0) {
            semiPerimeterMinusSideTwoValue = 0;
        }
        if (semiPerimeterMinusSideThreeValue < 0) {
            semiPerimeterMinusSideThreeValue = 0;
        }

        double productForAreaValue = semiPerimeterValue * semiPerimeterMinusSideOneValue * semiPerimeterMinusSideTwoValue * semiPerimeterMinusSideThreeValue;

        // Another edge case check: product cannot be negative for a real triangle area
        if (productForAreaValue < 0) {
            productForAreaValue = 0;
        }

        double triangleAreaValue = Math.sqrt(productForAreaValue);

        // Print the final area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}