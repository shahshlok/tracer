import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
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

        // Calculate the length of side1 using distance between point1 and point2
        double side1LengthValue = calculateDistanceBetweenTwoPoints(
                x1CoordinateValue, y1CoordinateValue,
                x2CoordinateValue, y2CoordinateValue
        );

        // Calculate the length of side2 using distance between point2 and point3
        double side2LengthValue = calculateDistanceBetweenTwoPoints(
                x2CoordinateValue, y2CoordinateValue,
                x3CoordinateValue, y3CoordinateValue
        );

        // Calculate the length of side3 using distance between point3 and point1
        double side3LengthValue = calculateDistanceBetweenTwoPoints(
                x3CoordinateValue, y3CoordinateValue,
                x1CoordinateValue, y1CoordinateValue
        );

        // Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (side1LengthValue + side2LengthValue + side3LengthValue) / 2.0;

        // Calculate the area using Heron's formula:
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue
                        * (semiPerimeterValue - side1LengthValue)
                        * (semiPerimeterValue - side2LengthValue)
                        * (semiPerimeterValue - side3LengthValue)
        );

        // Display the final result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }

    // This method calculates the distance between two points (x1, y1) and (x2, y2)
    public static double calculateDistanceBetweenTwoPoints(double x1CoordinateValue, double y1CoordinateValue,
                                                           double x2CoordinateValue, double y2CoordinateValue) {
        // Calculate the difference in x values
        double xDifferenceValue = x2CoordinateValue - x1CoordinateValue;

        // Calculate the difference in y values
        double yDifferenceValue = y2CoordinateValue - y1CoordinateValue;

        // Use the distance formula: sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        double distanceValue = Math.sqrt(
                (xDifferenceValue * xDifferenceValue) + (yDifferenceValue * yDifferenceValue)
        );

        // Return the calculated distance
        return distanceValue;
    }
}