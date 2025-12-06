import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask for point 1 coordinates
        System.out.print("(x1, y1):");
        // Read the x and y coordinates for the first point as doubles
        double pointOneXCoordinate = userInputScanner.nextDouble();
        double pointOneYCoordinate = userInputScanner.nextDouble();

        // Ask for point 2 coordinates
        System.out.print("(x2, y2):");
        // Read the x and y coordinates for the second point as doubles
        double pointTwoXCoordinate = userInputScanner.nextDouble();
        double pointTwoYCoordinate = userInputScanner.nextDouble();

        // Ask for point 3 coordinates
        System.out.print("(x3, y3):");
        // Read the x and y coordinates for the third point as doubles
        double pointThreeXCoordinate = userInputScanner.nextDouble();
        double pointThreeYCoordinate = userInputScanner.nextDouble();

        // Calculate the length of side1: distance between point 1 and point 2
        double sideOneDeltaX = pointTwoXCoordinate - pointOneXCoordinate;
        double sideOneDeltaY = pointTwoYCoordinate - pointOneYCoordinate;
        double sideOneDeltaXSquared = sideOneDeltaX * sideOneDeltaX;
        double sideOneDeltaYSquared = sideOneDeltaY * sideOneDeltaY;
        double sideOneDistanceSquared = sideOneDeltaXSquared + sideOneDeltaYSquared;
        double sideOneLength = Math.sqrt(sideOneDistanceSquared);

        // Calculate the length of side2: distance between point 2 and point 3
        double sideTwoDeltaX = pointThreeXCoordinate - pointTwoXCoordinate;
        double sideTwoDeltaY = pointThreeYCoordinate - pointTwoYCoordinate;
        double sideTwoDeltaXSquared = sideTwoDeltaX * sideTwoDeltaX;
        double sideTwoDeltaYSquared = sideTwoDeltaY * sideTwoDeltaY;
        double sideTwoDistanceSquared = sideTwoDeltaXSquared + sideTwoDeltaYSquared;
        double sideTwoLength = Math.sqrt(sideTwoDistanceSquared);

        // Calculate the length of side3: distance between point 3 and point 1
        double sideThreeDeltaX = pointOneXCoordinate - pointThreeXCoordinate;
        double sideThreeDeltaY = pointOneYCoordinate - pointThreeYCoordinate;
        double sideThreeDeltaXSquared = sideThreeDeltaX * sideThreeDeltaX;
        double sideThreeDeltaYSquared = sideThreeDeltaY * sideThreeDeltaY;
        double sideThreeDistanceSquared = sideThreeDeltaXSquared + sideThreeDeltaYSquared;
        double sideThreeLength = Math.sqrt(sideThreeDistanceSquared);

        // Calculate semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSides = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterValue = sumOfAllSides / 2.0;

        // Prepare to calculate the area using Heron's formula:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double semiPerimeterMinusSideOne = semiPerimeterValue - sideOneLength;
        double semiPerimeterMinusSideTwo = semiPerimeterValue - sideTwoLength;
        double semiPerimeterMinusSideThree = semiPerimeterValue - sideThreeLength;

        // Extra cautious check: ensure the sides can form a triangle using triangle inequality
        // If they cannot form a triangle, we will consider the area as 0.0 to avoid invalid Math.sqrt
        boolean sidesFormTriangle = false;
        if (sideOneLength > 0 && sideTwoLength > 0 && sideThreeLength > 0) {
            if (sideOneLength + sideTwoLength > sideThreeLength
                    && sideOneLength + sideThreeLength > sideTwoLength
                    && sideTwoLength + sideThreeLength > sideOneLength) {
                sidesFormTriangle = true;
            }
        }

        double triangleAreaValue = 0.0;

        if (sidesFormTriangle) {
            // Calculate the product inside the square root carefully
            double heronFormulaProduct = semiPerimeterValue;
            heronFormulaProduct = heronFormulaProduct * semiPerimeterMinusSideOne;
            heronFormulaProduct = heronFormulaProduct * semiPerimeterMinusSideTwo;
            heronFormulaProduct = heronFormulaProduct * semiPerimeterMinusSideThree;

            // Extra cautious check before taking the square root
            if (heronFormulaProduct >= 0) {
                triangleAreaValue = Math.sqrt(heronFormulaProduct);
            } else {
                // If something went wrong and product is negative, set area to 0
                triangleAreaValue = 0.0;
            }
        } else {
            // If sides do not form a valid triangle, keep area as 0.0
            triangleAreaValue = 0.0;
        }

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to avoid resource leak (extra cautious)
        userInputScanner.close();
    }
}