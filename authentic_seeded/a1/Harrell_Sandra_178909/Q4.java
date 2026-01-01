import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter three points for a triangle
        System.out.println("Enter three points for a triangle.");

        // Prompt and read the first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Prompt and read the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Prompt and read the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Compute the three side lengths of the triangle using the distance formula

        // side1 is the distance between point 1 and point 2
        double sideOneDifferenceX = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceY = pointTwoYValue - pointOneYValue;
        double sideOneSquareX = sideOneDifferenceX * sideOneDifferenceX;
        double sideOneSquareY = sideOneDifferenceY * sideOneDifferenceY;
        double sideOneSumSquares = sideOneSquareX + sideOneSquareY;
        double sideOneLength = Math.sqrt(sideOneSumSquares);

        // side2 is the distance between point 2 and point 3
        double sideTwoDifferenceX = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceY = pointThreeYValue - pointTwoYValue;
        double sideTwoSquareX = sideTwoDifferenceX * sideTwoDifferenceX;
        double sideTwoSquareY = sideTwoDifferenceY * sideTwoDifferenceY;
        double sideTwoSumSquares = sideTwoSquareX + sideTwoSquareY;
        double sideTwoLength = Math.sqrt(sideTwoSumSquares);

        // side3 is the distance between point 3 and point 1
        double sideThreeDifferenceX = pointOneXValue - pointThreeXValue;
        double sideThreeDifferenceY = pointOneYValue - pointThreeYValue;
        double sideThreeSquareX = sideThreeDifferenceX * sideThreeDifferenceX;
        double sideThreeSquareY = sideThreeDifferenceY * sideThreeDifferenceY;
        double sideThreeSumSquares = sideThreeSquareX + sideThreeSquareY;
        double sideThreeLength = Math.sqrt(sideThreeSumSquares);

        // Use Heron's formula to compute the area of the triangle

        // First compute s = (side1 + side2 + side3) / 2
        // I will compute numerator and then divide by 2, like in the math formula
        double semiPerimeterNumerator = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterValue = sideOneLength + sideTwoLength + sideThreeLength / 2.0;

        // Now compute the inside of the square root: s * (s - side1) * (s - side2) * (s - side3)
        double heronFactorOne = semiPerimeterValue;
        double heronFactorTwo = semiPerimeterValue - sideOneLength;
        double heronFactorThree = semiPerimeterValue - sideTwoLength;
        double heronFactorFour = semiPerimeterValue - sideThreeLength;

        double heronProductValue = heronFactorOne * heronFactorTwo * heronFactorThree * heronFactorFour;

        // Now compute the area as the square root of that product
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}