import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is asking for
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3CoordinateValue = userInputScanner.nextDouble();
        double y3CoordinateValue = userInputScanner.nextDouble();

        // Calculate the length of side1 using the distance formula between point 1 and point 2
        double side1LengthValue = Math.sqrt(
                Math.pow(x2CoordinateValue - x1CoordinateValue, 2) +
                Math.pow(y2CoordinateValue - y1CoordinateValue, 2)
        );

        // Calculate the length of side2 using the distance formula between point 2 and point 3
        double side2LengthValue = Math.sqrt(
                Math.pow(x3CoordinateValue - x2CoordinateValue, 2) +
                Math.pow(y3CoordinateValue - y2CoordinateValue, 2)
        );

        // Calculate the length of side3 using the distance formula between point 3 and point 1
        double side3LengthValue = Math.sqrt(
                Math.pow(x1CoordinateValue - x3CoordinateValue, 2) +
                Math.pow(y1CoordinateValue - y3CoordinateValue, 2)
        );

        // Calculate the semi-perimeter s of the triangle
        double semiPerimeterValue = (side1LengthValue + side2LengthValue + side3LengthValue) / 2.0;

        // Calculate the area using Heron's formula
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - side1LengthValue) *
                (semiPerimeterValue - side2LengthValue) *
                (semiPerimeterValue - side3LengthValue)
        );

        // Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}