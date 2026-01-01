import java.util.Scanner;

public class Q4 {

    // Method to calculate distance between two points using the distance formula
    public static double calculateDistanceBetweenTwoPoints(double firstPointXValue, double firstPointYValue,
                                                           double secondPointXValue, double secondPointYValue) {
        // Calculate the difference in x values
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Calculate the difference in y values
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Calculate the square of x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Calculate the square of y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Calculate the distance using square root
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Return the calculated distance
        return distanceBetweenPointsValue;
    }

    public static void main(String[] args) {

        // Create scanner to read user input
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Declare variables for all coordinates
        double pointOneXValue = 0.0;
        double pointOneYValue = 0.0;
        double pointTwoXValue = 0.0;
        double pointTwoYValue = 0.0;
        double pointThreeXValue = 0.0;
        double pointThreeYValue = 0.0;

        // Read first point (x1, y1)
        System.out.print("(x1, y1):");
        if (userInputScanner.hasNextDouble()) {
            pointOneXValue = userInputScanner.nextDouble();
        } else {
            pointOneXValue = 0.0;
        }
        if (userInputScanner.hasNextDouble()) {
            pointOneYValue = userInputScanner.nextDouble();
        } else {
            pointOneYValue = 0.0;
        }

        // Read second point (x2, y2)
        System.out.print("(x2, y2):");
        if (userInputScanner.hasNextDouble()) {
            pointTwoXValue = userInputScanner.nextDouble();
        } else {
            pointTwoXValue = 0.0;
        }
        if (userInputScanner.hasNextDouble()) {
            pointTwoYValue = userInputScanner.nextDouble();
        } else {
            pointTwoYValue = 0.0;
        }

        // Read third point (x3, y3)
        System.out.print("(x3, y3):");
        if (userInputScanner.hasNextDouble()) {
            pointThreeXValue = userInputScanner.nextDouble();
        } else {
            pointThreeXValue = 0.0;
        }
        if (userInputScanner.hasNextDouble()) {
            pointThreeYValue = userInputScanner.nextDouble();
        } else {
            pointThreeYValue = 0.0;
        }

        // Calculate length of each side of the triangle using the method from Q3

        // Side 1: distance between point 1 and point 2
        double sideOneLengthValue = calculateDistanceBetweenTwoPoints(pointOneXValue, pointOneYValue,
                                                                      pointTwoXValue, pointTwoYValue);

        // Side 2: distance between point 2 and point 3
        double sideTwoLengthValue = calculateDistanceBetweenTwoPoints(pointTwoXValue, pointTwoYValue,
                                                                      pointThreeXValue, pointThreeYValue);

        // Side 3: distance between point 1 and point 3
        double sideThreeLengthValue = calculateDistanceBetweenTwoPoints(pointOneXValue, pointOneYValue,
                                                                        pointThreeXValue, pointThreeYValue);

        // Now calculate the semi-perimeter s = (side1 + side2 + side3) / 2

        // First, calculate the sum of all three sides
        double sumOfAllSidesValue = sideOneLengthValue + sideTwoLengthValue + sideThreeLengthValue;

        // Initialize semi-perimeter
        double semiPerimeterValue = 0.0;

        // Check to avoid strange behavior, even though dividing by 2 is safe
        if (sumOfAllSidesValue != 0.0) {
            semiPerimeterValue = sumOfAllSidesValue / 2.0;
        } else {
            semiPerimeterValue = 0.0;
        }

        // Now calculate area using Heron's formula:
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )

        // Calculate (s - side1)
        double semiPerimeterMinusSideOneValue = semiPerimeterValue - sideOneLengthValue;

        // Calculate (s - side2)
        double semiPerimeterMinusSideTwoValue = semiPerimeterValue - sideTwoLengthValue;

        // Calculate (s - side3)
        double semiPerimeterMinusSideThreeValue = semiPerimeterValue - sideThreeLengthValue;

        // Calculate the product inside the square root
        double productInsideSquareRootValue = semiPerimeterValue
                * semiPerimeterMinusSideOneValue
                * semiPerimeterMinusSideTwoValue
                * semiPerimeterMinusSideThreeValue;

        // To be safe about invalid triangles or floating point issues, check product
        if (productInsideSquareRootValue < 0.0) {
            // If negative, we will treat the area as 0 to avoid NaN
            productInsideSquareRootValue = 0.0;
        }

        // Finally calculate the area
        double triangleAreaValue = Math.sqrt(productInsideSquareRootValue);

        // Output the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}