import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what to enter
        System.out.println("Enter three points for a triangle.");

        // Prompt and read x1 and y1
        System.out.print("(x1, y1):");
        int x1InputValue = userInputScanner.nextInt();
        int y1InputValue = userInputScanner.nextInt();

        // Prompt and read x2 and y2
        System.out.print("(x2, y2):");
        int x2InputValue = userInputScanner.nextInt();
        int y2InputValue = userInputScanner.nextInt();

        // Prompt and read x3 and y3
        System.out.print("(x3, y3):");
        int x3InputValue = userInputScanner.nextInt();
        int y3InputValue = userInputScanner.nextInt();

        // Calculate differences for side 1: between point1 and point2
        int side1DifferenceX = x2InputValue - x1InputValue;
        int side1DifferenceY = y2InputValue - y1InputValue;

        // Convert to double for distance calculation
        double side1DifferenceXSquared = side1DifferenceX * side1DifferenceX;
        double side1DifferenceYSquared = side1DifferenceY * side1DifferenceY;

        // Calculate side1 using distance formula
        double side1Length = Math.sqrt(side1DifferenceXSquared + side1DifferenceYSquared);

        // Calculate differences for side 2: between point2 and point3
        int side2DifferenceX = x3InputValue - x2InputValue;
        int side2DifferenceY = y3InputValue - y2InputValue;

        // Convert to double for distance calculation
        double side2DifferenceXSquared = side2DifferenceX * side2DifferenceX;
        double side2DifferenceYSquared = side2DifferenceY * side2DifferenceY;

        // Calculate side2 using distance formula
        double side2Length = Math.sqrt(side2DifferenceXSquared + side2DifferenceYSquared);

        // Calculate differences for side 3: between point3 and point1
        int side3DifferenceX = x1InputValue - x3InputValue;
        int side3DifferenceY = y1InputValue - y3InputValue;

        // Convert to double for distance calculation
        double side3DifferenceXSquared = side3DifferenceX * side3DifferenceX;
        double side3DifferenceYSquared = side3DifferenceY * side3DifferenceY;

        // Calculate side3 using distance formula
        double side3Length = Math.sqrt(side3DifferenceXSquared + side3DifferenceYSquared);

        // Now compute the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfSideLengths = side1Length + side2Length + side3Length;
        double semiPerimeterValue = sumOfSideLengths / 2.0;

        // Nervous about edge cases: check if the sides can form a triangle
        // Triangle inequality: each side < sum of other two
        boolean canFormTriangle = false;
        if (side1Length > 0 && side2Length > 0 && side3Length > 0) {
            if (side1Length < side2Length + side3Length &&
                side2Length < side1Length + side3Length &&
                side3Length < side1Length + side2Length) {
                canFormTriangle = true;
            }
        }

        double triangleAreaValue = 0.0;

        if (canFormTriangle) {
            // Use Heron's formula:
            // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))

            double semiPerimeterMinusSide1 = semiPerimeterValue - side1Length;
            double semiPerimeterMinusSide2 = semiPerimeterValue - side2Length;
            double semiPerimeterMinusSide3 = semiPerimeterValue - side3Length;

            // Nervous about negative interior for sqrt; force to zero if extremely small negative due to rounding
            double heronInsideValue = semiPerimeterValue * semiPerimeterMinusSide1 * semiPerimeterMinusSide2 * semiPerimeterMinusSide3;

            if (heronInsideValue < 0 && heronInsideValue > -1e-10) {
                heronInsideValue = 0;
            }

            if (heronInsideValue >= 0) {
                triangleAreaValue = Math.sqrt(heronInsideValue);
            } else {
                // If something is really wrong, keep area as 0
                triangleAreaValue = 0.0;
            }
        } else {
            // If it cannot form a triangle, area is 0.0
            triangleAreaValue = 0.0;
        }

        // Print the area of the triangle
        // The example uses "The area of the triangle is 12.5"
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner
        userInputScanner.close();
    }
}