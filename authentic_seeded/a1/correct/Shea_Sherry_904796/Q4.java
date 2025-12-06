import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Ask the user for x1 and y1 together on one line
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Ask the user for x2 and y2 together on one line
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Ask the user for x3 and y3 together on one line
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Calculate the length of side1 using the distance formula between point 1 and point 2
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXValue - pointOneXValue, 2) +
                Math.pow(pointTwoYValue - pointOneYValue, 2)
        );

        // Calculate the length of side2 using the distance formula between point 2 and point 3
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointTwoXValue, 2) +
                Math.pow(pointThreeYValue - pointTwoYValue, 2)
        );

        // Calculate the length of side3 using the distance formula between point 3 and point 1
        double sideThreeLength = Math.sqrt(
                Math.pow(pointOneXValue - pointThreeXValue, 2) +
                Math.pow(pointOneYValue - pointThreeYValue, 2)
        );

        // Calculate the semi-perimeter s using the given formula
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Calculate the area using Heron's formula
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner
        userInputScanner.close();
    }
}