import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
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

        // Calculate side1 using the distance formula between point 1 and point 2
        double sideOneDifferenceX = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceY = pointTwoYValue - pointOneYValue;

        // Use temporary variables to store the squared values
        double sideOneDifferenceXSquared = sideOneDifferenceX * sideOneDifferenceX;
        double sideOneDifferenceYSquared = sideOneDifferenceY * sideOneDifferenceY;

        // Sum the squared differences
        double sideOneSumOfSquares = sideOneDifferenceXSquared + sideOneDifferenceYSquared;

        // Calculate the length of side1
        double sideOneLength = Math.sqrt(sideOneSumOfSquares);

        // Calculate side2 using the distance formula between point 2 and point 3
        double sideTwoDifferenceX = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceY = pointThreeYValue - pointTwoYValue;

        double sideTwoDifferenceXSquared = sideTwoDifferenceX * sideTwoDifferenceX;
        double sideTwoDifferenceYSquared = sideTwoDifferenceY * sideTwoDifferenceY;

        double sideTwoSumOfSquares = sideTwoDifferenceXSquared + sideTwoDifferenceYSquared;

        double sideTwoLength = Math.sqrt(sideTwoSumOfSquares);

        // Calculate side3 using the distance formula between point 3 and point 1
        double sideThreeDifferenceX = pointOneXValue - pointThreeXValue;
        double sideThreeDifferenceY = pointOneYValue - pointThreeYValue;

        double sideThreeDifferenceXSquared = sideThreeDifferenceX * sideThreeDifferenceX;
        double sideThreeDifferenceYSquared = sideThreeDifferenceY * sideThreeDifferenceY;

        double sideThreeSumOfSquares = sideThreeDifferenceXSquared + sideThreeDifferenceYSquared;

        double sideThreeLength = Math.sqrt(sideThreeSumOfSquares);

        // Now calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSides = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterValue = sumOfAllSides / 2.0;

        // To be extra careful, check that the semi-perimeter is not zero
        if (semiPerimeterValue == 0) {
            // If the semi-perimeter is zero, the area must be zero (degenerate triangle)
            System.out.println("The area of the triangle is 0");
        } else {
            // Compute the terms (s - side1), (s - side2), (s - side3)
            double semiPerimeterMinusSideOne = semiPerimeterValue - sideOneLength;
            double semiPerimeterMinusSideTwo = semiPerimeterValue - sideTwoLength;
            double semiPerimeterMinusSideThree = semiPerimeterValue - sideThreeLength;

            // Calculate the product inside the square root: s * (s - side1) * (s - side2) * (s - side3)
            double heronInnerProduct = semiPerimeterValue * semiPerimeterMinusSideOne * semiPerimeterMinusSideTwo * semiPerimeterMinusSideThree;

            // If due to rounding issues heronInnerProduct becomes slightly negative, clamp it to zero
            if (heronInnerProduct < 0 && heronInnerProduct > -1e-10) {
                heronInnerProduct = 0;
            }

            double triangleAreaValue = 0.0;

            // Only take the square root if the value is non-negative
            if (heronInnerProduct >= 0) {
                triangleAreaValue = Math.sqrt(heronInnerProduct);
            }

            // Print the area of the triangle
            System.out.println("The area of the triangle is " + triangleAreaValue);
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}