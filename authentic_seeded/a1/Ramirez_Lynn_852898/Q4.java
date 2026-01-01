import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask for and read the first point (x1, y1) as two doubles
        System.out.print("(x1, y1):");
        double pointOneXCoordinate = userInputScanner.nextDouble();
        double pointOneYCoordinate = userInputScanner.nextDouble();

        // Ask for and read the second point (x2, y2) as two doubles
        System.out.print("(x2, y2):");
        double pointTwoXCoordinate = userInputScanner.nextDouble();
        double pointTwoYCoordinate = userInputScanner.nextDouble();

        // Ask for and read the third point (x3, y3) as two doubles
        System.out.print("(x3, y3):");
        double pointThreeXCoordinate = userInputScanner.nextDouble();
        double pointThreeYCoordinate = userInputScanner.nextDouble();

        // Step 1: Calculate the length of side1 between point 1 and point 2 using distance formula
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXCoordinate - pointOneXCoordinate, 2) +
                Math.pow(pointTwoYCoordinate - pointOneYCoordinate, 2)
        );

        // Step 2: Calculate the length of side2 between point 2 and point 3 using distance formula
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXCoordinate - pointTwoXCoordinate, 2) +
                Math.pow(pointThreeYCoordinate - pointTwoYCoordinate, 2)
        );

        // Step 3: Calculate the length of side3 between point 3 and point 1 using distance formula
        double sideThreeLength = Math.sqrt(
                Math.pow(pointOneXCoordinate - pointThreeXCoordinate, 2) +
                Math.pow(pointOneYCoordinate - pointThreeYCoordinate, 2)
        );

        // Step 4: Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideTwoLength + sideThreeLength - sideTwoLength) / 2.0;

        // Step 5: Calculate the area using Heron's formula: area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Step 6: Display the area of the triangle to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}