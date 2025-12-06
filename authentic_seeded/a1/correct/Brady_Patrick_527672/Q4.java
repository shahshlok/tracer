import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is doing
        System.out.println("Enter three points for a triangle.");

        // Ask the user for point 1 (x1, y1)
        System.out.print("(x1, y1):");
        double x1CoordinateValue = userInputScanner.nextDouble();
        double y1CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for point 2 (x2, y2)
        System.out.print("(x2, y2):");
        double x2CoordinateValue = userInputScanner.nextDouble();
        double y2CoordinateValue = userInputScanner.nextDouble();

        // Ask the user for point 3 (x3, y3)
        System.out.print("(x3, y3):");
        double x3CoordinateValue = userInputScanner.nextDouble();
        double y3CoordinateValue = userInputScanner.nextDouble();

        // Now we calculate the length of each side of the triangle using the distance formula
        // side1 is the distance between point 1 and point 2
        double side1LengthValue = calculateDistanceBetweenPoints(x1CoordinateValue, y1CoordinateValue, x2CoordinateValue, y2CoordinateValue);

        // side2 is the distance between point 2 and point 3
        double side2LengthValue = calculateDistanceBetweenPoints(x2CoordinateValue, y2CoordinateValue, x3CoordinateValue, y3CoordinateValue);

        // side3 is the distance between point 3 and point 1
        double side3LengthValue = calculateDistanceBetweenPoints(x3CoordinateValue, y3CoordinateValue, x1CoordinateValue, y1CoordinateValue);

        // Now we calculate s using the formula s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (side1LengthValue + side2LengthValue + side3LengthValue) / 2.0;

        // Now we calculate the area using Heron's formula:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue
                        * (semiPerimeterValue - side1LengthValue)
                        * (semiPerimeterValue - side2LengthValue)
                        * (semiPerimeterValue - side3LengthValue)
        );

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner because we are done with user input
        userInputScanner.close();
    }

    // This method calculates the distance between two points (x1, y1) and (x2, y2)
    // using the distance formula: sqrt((x2 - x1)^2 + (y2 - y1)^2)
    public static double calculateDistanceBetweenPoints(double x1CoordinateValue, double y1CoordinateValue,
                                                        double x2CoordinateValue, double y2CoordinateValue) {

        // Calculate the difference in x values
        double xDifferenceValue = x2CoordinateValue - x1CoordinateValue;

        // Calculate the difference in y values
        double yDifferenceValue = y2CoordinateValue - y1CoordinateValue;

        // Calculate the square of the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Calculate the square of the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Add the squared differences
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Take the square root to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Return the distance
        return distanceBetweenPointsValue;
    }
}