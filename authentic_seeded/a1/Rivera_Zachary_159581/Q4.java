import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program does
        System.out.println("Enter three points for a triangle.");

        // Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        int pointOneXValue = userInputScanner.nextInt(); // read x1 as an int
        int pointOneYValue = userInputScanner.nextInt(); // read y1 as an int

        // Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        int pointTwoXValue = userInputScanner.nextInt(); // read x2 as an int
        int pointTwoYValue = userInputScanner.nextInt(); // read y2 as an int

        // Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        int pointThreeXValue = userInputScanner.nextInt(); // read x3 as an int
        int pointThreeYValue = userInputScanner.nextInt(); // read y3 as an int

        // Calculate side1 length using distance formula between point 1 and point 2
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXValue - pointOneXValue, 2) +
                Math.pow(pointTwoYValue - pointOneYValue, 2)
        );

        // Calculate side2 length using distance formula between point 2 and point 3
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointTwoXValue, 2) +
                Math.pow(pointThreeYValue - pointTwoYValue, 2)
        );

        // Calculate side3 length using distance formula between point 3 and point 1
        double sideThreeLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointOneXValue, 2) +
                Math.pow(pointThreeYValue - pointOneYValue, 2)
        );

        // Calculate s = (side1 + side2 + side3) / 2 using Heron's formula
        // I follow the formula straight, so I add the three sides on top and then divide by 2 on the bottom
        double semiPerimeterValue = sideOneLength + sideTwoLength + sideThreeLength / 2.0;

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