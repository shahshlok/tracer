import java.util.Scanner;

public class Q4 {

    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program is doing
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Step 5: Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 using distance between point 1 and point 2
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXValue - pointOneXValue, 2) +
                Math.pow(pointTwoYValue - pointOneYValue, 2)
        );

        // Step 7: Calculate the length of side2 using distance between point 2 and point 3
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointTwoXValue, 2) +
                Math.pow(pointThreeYValue - pointTwoYValue, 2)
        );

        // Step 8: Calculate the length of side3 using distance between point 3 and point 1
        double sideThreeLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointOneXValue, 2) +
                Math.pow(pointThreeYValue - pointOneYValue, 2)
        );

        // Step 9: Calculate the semi-perimeter s using Heron's formula
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Step 10: Calculate the area using Heron's formula
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Step 11: Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }
}