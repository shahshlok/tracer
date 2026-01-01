import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is asking for
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point coordinates (x1, y1)
        System.out.print("(x1, y1):");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for the second point coordinates (x2, y2)
        System.out.print("(x2, y2):");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for the third point coordinates (x3, y3)
        System.out.print("(x3, y3):");
        double x3CoordinateValue = userInputScanner.nextDouble();
        double y3CoordinateValue = userInputScanner.nextDouble();

        // Calculate the length of side1 using the distance formula between point1 and point2
        double side1LengthValue = Math.sqrt(
                Math.pow(x2CoordinateValue - x1CoordinateValue, 2) +
                Math.pow(y2CoordinateValue - y1CoordinateValue, 2)
        );

        // Calculate the length of side2 using the distance formula between point2 and point3
        double side2LengthValue = Math.sqrt(
                Math.pow(x3CoordinateValue - x2CoordinateValue, 2) +
                Math.pow(y3CoordinateValue - y2CoordinateValue, 2)
        );

        // Calculate the length of side3 using the distance formula between point3 and point1
        double side3LengthValue = Math.sqrt(
                Math.pow(x1CoordinateValue - x3CoordinateValue, 2) +
                Math.pow(y1CoordinateValue - y3CoordinateValue, 2)
        );

        // Calculate the semi-perimeter s using the formula s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (side1LengthValue + side2LengthValue + side3LengthValue) / 2.0;

        // Calculate the area using Heron's formula:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double areaOfTriangleValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - side1LengthValue) *
                (semiPerimeterValue - side2LengthValue) *
                (semiPerimeterValue - side3LengthValue)
        );

        // Display the result to the user
        System.out.println("The area of the triangle is " + areaOfTriangleValue);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}