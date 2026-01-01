import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is asking for
        System.out.println("Enter three points for a triangle.");

        // Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        // Read x1 and y1 as doubles so that we can handle decimal values
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Now we will calculate the lengths of the three sides of the triangle
        // Using the distance formula: distance = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )

        // First side is between point 1 and point 2
        double sideOneDifferenceXValue = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceYValue = pointTwoYValue - pointOneYValue;
        double sideOneSquareXValue = sideOneDifferenceXValue * sideOneDifferenceXValue;
        double sideOneSquareYValue = sideOneDifferenceYValue * sideOneDifferenceYValue;
        double sideOneSumOfSquaresValue = sideOneSquareXValue + sideOneSquareYValue;
        double sideOneLengthValue = Math.sqrt(sideOneSumOfSquaresValue);

        // Second side is between point 2 and point 3
        double sideTwoDifferenceXValue = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceYValue = pointThreeYValue - pointTwoYValue;
        double sideTwoSquareXValue = sideTwoDifferenceXValue * sideTwoDifferenceXValue;
        double sideTwoSquareYValue = sideTwoDifferenceYValue * sideTwoDifferenceYValue;
        double sideTwoSumOfSquaresValue = sideTwoSquareXValue + sideTwoSquareYValue;
        double sideTwoLengthValue = Math.sqrt(sideTwoSumOfSquaresValue);

        // Third side is between point 3 and point 1
        double sideThreeDifferenceXValue = pointOneXValue - pointThreeXValue;
        double sideThreeDifferenceYValue = pointOneYValue - pointThreeYValue;
        double sideThreeSquareXValue = sideThreeDifferenceXValue * sideThreeDifferenceXValue;
        double sideThreeSquareYValue = sideThreeDifferenceYValue * sideThreeDifferenceYValue;
        double sideThreeSumOfSquaresValue = sideThreeSquareXValue + sideThreeSquareYValue;
        double sideThreeLengthValue = Math.sqrt(sideThreeSumOfSquaresValue);

        // Now we will apply Heron's formula to compute the area of the triangle
        // First compute the semi-perimeter: s = (side1 + side2 + side3) / 2

        double semiPerimeterNumeratorValue = sideOneLengthValue + sideTwoLengthValue + sideThreeLengthValue;
        double semiPerimeterValue = semiPerimeterNumeratorValue / 2.0;

        // Now compute the inside of the square root for the area
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )

        double heronFactorOneValue = semiPerimeterValue;
        double heronFactorTwoValue = semiPerimeterValue - sideOneLengthValue;
        double heronFactorThreeValue = semiPerimeterValue - sideTwoLengthValue;
        double heronFactorFourValue = semiPerimeterValue - sideThreeLengthValue;

        double heronProductValue = heronFactorOneValue * heronFactorTwoValue * heronFactorThreeValue * heronFactorFourValue;

        double triangleAreaValue;

        // If the product is negative or very close to zero due to rounding, the area should be zero
        if (heronProductValue <= 0) {
            triangleAreaValue = 0.0;
        } else {
            triangleAreaValue = Math.sqrt(heronProductValue);
        }

        // Display the final area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}