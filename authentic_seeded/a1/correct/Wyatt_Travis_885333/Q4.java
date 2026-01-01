import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1Coordinate = userInputScanner.nextDouble();
        double y1Coordinate = userInputScanner.nextDouble();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2Coordinate = userInputScanner.nextDouble();
        double y2Coordinate = userInputScanner.nextDouble();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3Coordinate = userInputScanner.nextDouble();
        double y3Coordinate = userInputScanner.nextDouble();

        // Step 1: Calculate the length of side1 between point1 (x1, y1) and point2 (x2, y2)
        // Use distance formula: distance = sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double side1Length = Math.sqrt(
                Math.pow(x2Coordinate - x1Coordinate, 2) +
                Math.pow(y2Coordinate - y1Coordinate, 2)
        );

        // Step 2: Calculate the length of side2 between point2 (x2, y2) and point3 (x3, y3)
        double side2Length = Math.sqrt(
                Math.pow(x3Coordinate - x2Coordinate, 2) +
                Math.pow(y3Coordinate - y2Coordinate, 2)
        );

        // Step 3: Calculate the length of side3 between point3 (x3, y3) and point1 (x1, y1)
        double side3Length = Math.sqrt(
                Math.pow(x1Coordinate - x3Coordinate, 2) +
                Math.pow(y1Coordinate - y3Coordinate, 2)
        );

        // Step 4: Calculate the semi-perimeter s using Heron's formula part 1
        // s = (side1 + side2 + side3) / 2
        double semiPerimeterS = (side1Length + side2Length + side3Length) / 2.0;

        // Step 5: Calculate the area using Heron's formula
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double triangleArea = Math.sqrt(
                semiPerimeterS *
                (semiPerimeterS - side1Length) *
                (semiPerimeterS - side2Length) *
                (semiPerimeterS - side3Length)
        );

        // Step 6: Display the final area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the scanner because we are done reading input
        userInputScanner.close();
    }
}