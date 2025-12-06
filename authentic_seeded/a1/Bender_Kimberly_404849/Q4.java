import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Declare variables for each coordinate of the three points
        double pointOneXValue = 0.0;
        double pointOneYValue = 0.0;
        double pointTwoXValue = 0.0;
        double pointTwoYValue = 0.0;
        double pointThreeXValue = 0.0;
        double pointThreeYValue = 0.0;

        // Ask the user for (x1, y1)
        System.out.print("(x1, y1):");
        if (userInputScanner.hasNextDouble()) {
            pointOneXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointOneYValue = userInputScanner.nextDouble();
        }

        // Ask the user for (x2, y2)
        System.out.print("(x2, y2):");
        if (userInputScanner.hasNextDouble()) {
            pointTwoXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointTwoYValue = userInputScanner.nextDouble();
        }

        // Ask the user for (x3, y3)
        System.out.print("(x3, y3):");
        if (userInputScanner.hasNextDouble()) {
            pointThreeXValue = userInputScanner.nextDouble();
        }
        if (userInputScanner.hasNextDouble()) {
            pointThreeYValue = userInputScanner.nextDouble();
        }

        // Calculate the length of each side of the triangle using distance formula
        // side1 is distance between point 1 and point 2
        double sideOneDeltaXValue = pointTwoXValue - pointOneXValue;
        double sideOneDeltaYValue = pointTwoYValue - pointOneYValue;
        double sideOneSquaredXValue = sideOneDeltaXValue * sideOneDeltaXValue;
        double sideOneSquaredYValue = sideOneDeltaYValue * sideOneDeltaYValue;
        double sideOneSumOfSquaresValue = sideOneSquaredXValue + sideOneSquaredYValue;
        double sideOneLengthValue = Math.sqrt(sideOneSumOfSquaresValue);

        // side2 is distance between point 2 and point 3
        double sideTwoDeltaXValue = pointThreeXValue - pointTwoXValue;
        double sideTwoDeltaYValue = pointThreeYValue - pointTwoYValue;
        double sideTwoSquaredXValue = sideTwoDeltaXValue * sideTwoDeltaXValue;
        double sideTwoSquaredYValue = sideTwoDeltaYValue * sideTwoDeltaYValue;
        double sideTwoSumOfSquaresValue = sideTwoSquaredXValue + sideTwoSquaredYValue;
        double sideTwoLengthValue = Math.sqrt(sideTwoSumOfSquaresValue);

        // side3 is distance between point 3 and point 1
        double sideThreeDeltaXValue = pointOneXValue - pointThreeXValue;
        double sideThreeDeltaYValue = pointOneYValue - pointThreeYValue;
        double sideThreeSquaredXValue = sideThreeDeltaXValue * sideThreeDeltaXValue;
        double sideThreeSquaredYValue = sideThreeDeltaYValue * sideThreeDeltaYValue;
        double sideThreeSumOfSquaresValue = sideThreeSquaredXValue + sideThreeSquaredYValue;
        double sideThreeLengthValue = Math.sqrt(sideThreeSumOfSquaresValue);

        // Now we apply Heron's formula
        // First calculate s = (side1 + side2 + side3) / 2
        // I will make an integer version of the perimeter first
        int sideOneLengthIntValue = (int) sideOneLengthValue;
        int sideTwoLengthIntValue = (int) sideTwoLengthValue;
        int sideThreeLengthIntValue = (int) sideThreeLengthValue;

        int semiPerimeterIntegerNumeratorValue = sideOneLengthIntValue + sideTwoLengthIntValue + sideThreeLengthIntValue;
        int semiPerimeterIntegerValue = semiPerimeterIntegerNumeratorValue / 2;

        double semiPerimeterValue = (double) semiPerimeterIntegerValue;

        // Calculate each term (s - sideX) separately
        double semiPerimeterMinusSideOneValue = semiPerimeterValue - sideOneLengthValue;
        double semiPerimeterMinusSideTwoValue = semiPerimeterValue - sideTwoLengthValue;
        double semiPerimeterMinusSideThreeValue = semiPerimeterValue - sideThreeLengthValue;

        // Calculate the product s * (s - side1) * (s - side2) * (s - side3)
        double heronProductValue = semiPerimeterValue * semiPerimeterMinusSideOneValue
                * semiPerimeterMinusSideTwoValue * semiPerimeterMinusSideThreeValue;

        // Just in case, ensure heronProductValue is not negative due to rounding
        if (heronProductValue < 0.0) {
            heronProductValue = 0.0;
        }

        // Finally calculate the area = sqrt(heronProductValue)
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Print the result exactly as required
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}