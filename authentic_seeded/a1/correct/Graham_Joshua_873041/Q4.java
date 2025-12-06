import java.util.Scanner;

public class Q4 {

    public static void main(String[] args) {

        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Prompt the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Prompt the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Prompt the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Now we will calculate the lengths of the three sides of the triangle
        // Use temporary variables for differences between x and y coordinates for each side

        // Side 1 is between point 1 and point 2
        double sideOneXDifference = pointTwoXValue - pointOneXValue;
        double sideOneYDifference = pointTwoYValue - pointOneYValue;
        double sideOneXDifferenceSquared = sideOneXDifference * sideOneXDifference;
        double sideOneYDifferenceSquared = sideOneYDifference * sideOneYDifference;
        double sideOneSumOfSquares = sideOneXDifferenceSquared + sideOneYDifferenceSquared;
        double sideOneLength = 0.0;
        if (sideOneSumOfSquares >= 0) { // extra check to be safe with sqrt
            sideOneLength = Math.sqrt(sideOneSumOfSquares);
        }

        // Side 2 is between point 2 and point 3
        double sideTwoXDifference = pointThreeXValue - pointTwoXValue;
        double sideTwoYDifference = pointThreeYValue - pointTwoYValue;
        double sideTwoXDifferenceSquared = sideTwoXDifference * sideTwoXDifference;
        double sideTwoYDifferenceSquared = sideTwoYDifference * sideTwoYDifference;
        double sideTwoSumOfSquares = sideTwoXDifferenceSquared + sideTwoYDifferenceSquared;
        double sideTwoLength = 0.0;
        if (sideTwoSumOfSquares >= 0) { // extra check to be safe with sqrt
            sideTwoLength = Math.sqrt(sideTwoSumOfSquares);
        }

        // Side 3 is between point 3 and point 1
        double sideThreeXDifference = pointOneXValue - pointThreeXValue;
        double sideThreeYDifference = pointOneYValue - pointThreeYValue;
        double sideThreeXDifferenceSquared = sideThreeXDifference * sideThreeXDifference;
        double sideThreeYDifferenceSquared = sideThreeYDifference * sideThreeYDifference;
        double sideThreeSumOfSquares = sideThreeXDifferenceSquared + sideThreeYDifferenceSquared;
        double sideThreeLength = 0.0;
        if (sideThreeSumOfSquares >= 0) { // extra check to be safe with sqrt
            sideThreeLength = Math.sqrt(sideThreeSumOfSquares);
        }

        // Now calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSides = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterValue = 0.0;
        if (sumOfAllSides != 0) { // extra check to avoid dividing by zero
            semiPerimeterValue = sumOfAllSides / 2.0;
        }

        // Use Heron's formula to calculate the area:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double semiPerimeterMinusSideOne = semiPerimeterValue - sideOneLength;
        double semiPerimeterMinusSideTwo = semiPerimeterValue - sideTwoLength;
        double semiPerimeterMinusSideThree = semiPerimeterValue - sideThreeLength;

        // Multiply the terms inside the square root step by step
        double heronFirstProduct = semiPerimeterValue * semiPerimeterMinusSideOne;
        double heronSecondProduct = heronFirstProduct * semiPerimeterMinusSideTwo;
        double heronThirdProduct = heronSecondProduct * semiPerimeterMinusSideThree;

        double triangleAreaValue = 0.0;

        // Just in case numeric issues make the value slightly negative, guard the sqrt
        if (heronThirdProduct < 0 && heronThirdProduct > -1e-10) {
            // If it is a very small negative due to rounding, treat as zero
            heronThirdProduct = 0.0;
        }

        if (heronThirdProduct >= 0) {
            triangleAreaValue = Math.sqrt(heronThirdProduct);
        }

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner (even though program is ending, just to be safe)
        userInputScanner.close();
    }
}