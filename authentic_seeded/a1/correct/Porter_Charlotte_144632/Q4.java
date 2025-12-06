import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner inputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = inputScanner.nextDouble();
        double pointOneYValue = inputScanner.nextDouble();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = inputScanner.nextDouble();
        double pointTwoYValue = inputScanner.nextDouble();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = inputScanner.nextDouble();
        double pointThreeYValue = inputScanner.nextDouble();

        // Calculate the length of side1 using distance formula between point1 and point2
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXValue - pointOneXValue, 2) +
                Math.pow(pointTwoYValue - pointOneYValue, 2)
        );

        // Calculate the length of side2 using distance formula between point2 and point3
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointTwoXValue, 2) +
                Math.pow(pointThreeYValue - pointTwoYValue, 2)
        );

        // Calculate the length of side3 using distance formula between point3 and point1
        double sideThreeLength = Math.sqrt(
                Math.pow(pointOneXValue - pointThreeXValue, 2) +
                Math.pow(pointOneYValue - pointThreeYValue, 2)
        );

        // Calculate the semi-perimeter s using the formula s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Calculate the area using Heron's formula: area = sqrt(s(s-side1)(s-side2)(s-side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Display the final area of the triangle to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner to free up resources
        inputScanner.close();
    }
}