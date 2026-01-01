import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program expects
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        int pointOneXValue = userInputScanner.nextInt();
        int pointOneYValue = userInputScanner.nextInt();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        int pointTwoXValue = userInputScanner.nextInt();
        int pointTwoYValue = userInputScanner.nextInt();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        int pointThreeXValue = userInputScanner.nextInt();
        int pointThreeYValue = userInputScanner.nextInt();

        // Calculate squared differences for the first side between point 1 and point 2
        double sideOneDifferenceX = pointTwoXValue - pointOneXValue;
        double sideOneDifferenceY = pointTwoYValue - pointOneYValue;
        double sideOneDifferenceXSquared = sideOneDifferenceX * sideOneDifferenceX;
        double sideOneDifferenceYSquared = sideOneDifferenceY * sideOneDifferenceY;

        // Use the distance formula for side 1: sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        double sideOneLength = Math.sqrt(sideOneDifferenceXSquared + sideOneDifferenceYSquared);

        // Calculate squared differences for the second side between point 2 and point 3
        double sideTwoDifferenceX = pointThreeXValue - pointTwoXValue;
        double sideTwoDifferenceY = pointThreeYValue - pointTwoYValue;
        double sideTwoDifferenceXSquared = sideTwoDifferenceX * sideTwoDifferenceX;
        double sideTwoDifferenceYSquared = sideTwoDifferenceY * sideTwoDifferenceY;

        // Use the distance formula for side 2: sqrt( (x3 - x2)^2 + (y3 - y2)^2 )
        double sideTwoLength = Math.sqrt(sideTwoDifferenceXSquared + sideTwoDifferenceYSquared);

        // Calculate squared differences for the third side between point 3 and point 1
        double sideThreeDifferenceX = pointOneXValue - pointThreeXValue;
        double sideThreeDifferenceY = pointOneYValue - pointThreeYValue;
        double sideThreeDifferenceXSquared = sideThreeDifferenceX * sideThreeDifferenceX;
        double sideThreeDifferenceYSquared = sideThreeDifferenceY * sideThreeDifferenceY;

        // Use the distance formula for side 3: sqrt( (x1 - x3)^2 + (y1 - y3)^2 )
        double sideThreeLength = Math.sqrt(sideThreeDifferenceXSquared + sideThreeDifferenceYSquared);

        // Now use Heron's formula to calculate the area of the triangle
        // First calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterNumerator = sideOneLength + sideTwoLength + sideThreeLength;
        double semiPerimeterDenominator = 2.0;

        // Use intermediate integer values to represent the sum and then divide by 2 using integer division
        int semiPerimeterIntegerSum = (int) semiPerimeterNumerator;
        int semiPerimeterIntegerDenominator = 2;
        int semiPerimeterIntegerResult = semiPerimeterIntegerSum / semiPerimeterIntegerDenominator;

        // Cast the result of the division to double to get the semi-perimeter as a decimal
        double semiPerimeterValue = (double) semiPerimeterIntegerResult;

        // Next calculate the expression under the square root:
        // s * (s - side1) * (s - side2) * (s - side3)
        double heronPartA = semiPerimeterValue;
        double heronPartB = semiPerimeterValue - sideOneLength;
        double heronPartC = semiPerimeterValue - sideTwoLength;
        double heronPartD = semiPerimeterValue - sideThreeLength;

        double heronProductValue = heronPartA * heronPartB * heronPartC * heronPartD;

        // Finally, the area is the square root of that product
        double triangleAreaValue = Math.sqrt(heronProductValue);

        // Display the area of the triangle to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}