import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program is doing
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask for and read the first point (x1, y1)
        System.out.print("(x1, y1):");
        double point1XCoordinate = userInputScanner.nextDouble();
        double point1YCoordinate = userInputScanner.nextDouble();

        // Step 4: Ask for and read the second point (x2, y2)
        System.out.print("(x2, y2):");
        double point2XCoordinate = userInputScanner.nextDouble();
        double point2YCoordinate = userInputScanner.nextDouble();

        // Step 5: Ask for and read the third point (x3, y3)
        System.out.print("(x3, y3):");
        double point3XCoordinate = userInputScanner.nextDouble();
        double point3YCoordinate = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 using distance between point1 and point2
        double side1DifferenceInX = point2XCoordinate - point1XCoordinate;
        double side1DifferenceInY = point2YCoordinate - point1YCoordinate;
        double side1Length = Math.sqrt(side1DifferenceInX * side1DifferenceInX + side1DifferenceInY * side1DifferenceInY);

        // Step 7: Calculate the length of side2 using distance between point2 and point3
        double side2DifferenceInX = point3XCoordinate - point2XCoordinate;
        double side2DifferenceInY = point3YCoordinate - point2YCoordinate;
        double side2Length = Math.sqrt(side2DifferenceInX * side2DifferenceInX + side2DifferenceInY * side2DifferenceInY);

        // Step 8: Calculate the length of side3 using distance between point3 and point1
        double side3DifferenceInX = point1XCoordinate - point3XCoordinate;
        double side3DifferenceInY = point1YCoordinate - point3YCoordinate;
        double side3Length = Math.sqrt(side3DifferenceInX * side3DifferenceInX + side3DifferenceInY * side3DifferenceInY);

        // Step 9: Calculate the semi-perimeter s using the formula s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (side1Length + side2Length + side3Length) / 2.0;

        // Step 10: Calculate the area using Heron's formula:
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue
                        * (semiPerimeterValue - side1Length)
                        * (semiPerimeterValue - side2Length)
                        * (semiPerimeterValue - side3Length)
        );

        // Step 11: Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }
}