import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program expects
        System.out.println("Enter three points for a triangle.");

        // Ask for first point (x1, y1)
        System.out.print("(x1, y1):");
        // Read the x1 and y1 digits as characters, but we will try to read them as numbers
        // To be safer, we read the entire token and then parse it
        String userInputX1String = userInputScanner.next();
        String userInputY1String = userInputScanner.next();
        double userInputX1Value = 0.0;
        double userInputY1Value = 0.0;
        // Parse x1 and y1 from String to double
        userInputX1Value = Double.parseDouble(userInputX1String);
        userInputY1Value = Double.parseDouble(userInputY1String);

        // Ask for second point (x2, y2)
        System.out.print("(x2, y2):");
        String userInputX2String = userInputScanner.next();
        String userInputY2String = userInputScanner.next();
        double userInputX2Value = 0.0;
        double userInputY2Value = 0.0;
        userInputX2Value = Double.parseDouble(userInputX2String);
        userInputY2Value = Double.parseDouble(userInputY2String);

        // Ask for third point (x3, y3)
        System.out.print("(x3, y3):");
        String userInputX3String = userInputScanner.next();
        String userInputY3String = userInputScanner.next();
        double userInputX3Value = 0.0;
        double userInputY3Value = 0.0;
        userInputX3Value = Double.parseDouble(userInputX3String);
        userInputY3Value = Double.parseDouble(userInputY3String);

        // Now we will calculate the lengths of the three sides using the distance formula
        // side1 is the distance between point1 (x1, y1) and point2 (x2, y2)
        double side1DifferenceX = userInputX2Value - userInputX1Value;
        double side1DifferenceY = userInputY2Value - userInputY1Value;
        double side1DifferenceXSquared = side1DifferenceX * side1DifferenceX;
        double side1DifferenceYSquared = side1DifferenceY * side1DifferenceY;
        double side1SumOfSquares = side1DifferenceXSquared + side1DifferenceYSquared;
        double side1Length = Math.sqrt(side1SumOfSquares);

        // side2 is the distance between point2 (x2, y2) and point3 (x3, y3)
        double side2DifferenceX = userInputX3Value - userInputX2Value;
        double side2DifferenceY = userInputY3Value - userInputY2Value;
        double side2DifferenceXSquared = side2DifferenceX * side2DifferenceX;
        double side2DifferenceYSquared = side2DifferenceY * side2DifferenceY;
        double side2SumOfSquares = side2DifferenceXSquared + side2DifferenceYSquared;
        double side2Length = Math.sqrt(side2SumOfSquares);

        // side3 is the distance between point3 (x3, y3) and point1 (x1, y1)
        double side3DifferenceX = userInputX1Value - userInputX3Value;
        double side3DifferenceY = userInputY1Value - userInputY3Value;
        double side3DifferenceXSquared = side3DifferenceX * side3DifferenceX;
        double side3DifferenceYSquared = side3DifferenceY * side3DifferenceY;
        double side3SumOfSquares = side3DifferenceXSquared + side3DifferenceYSquared;
        double side3Length = Math.sqrt(side3SumOfSquares);

        // Now we will compute the semi-perimeter s using Heron's formula
        double semiPerimeterValue = 0.0;
        double sumOfSideLengths = side1Length + side2Length + side3Length;
        semiPerimeterValue = sumOfSideLengths / 2.0;

        // Before computing the area, we will check if the triangle is valid using triangle inequality
        boolean isValidTriangle = false;
        if (side1Length + side2Length > side3Length && side1Length + side3Length > side2Length && side2Length + side3Length > side1Length) {
            isValidTriangle = true;
        }

        double triangleAreaValue = 0.0;

        if (isValidTriangle) {
            // Compute the value inside the square root for Heron's formula
            double heronInsideValue = semiPerimeterValue * (semiPerimeterValue - side1Length) * (semiPerimeterValue - side2Length) * (semiPerimeterValue - side3Length);

            // Sometimes rounding errors can make this slightly negative, so we add a cautious check
            if (heronInsideValue < 0.0) {
                heronInsideValue = 0.0;
            }

            // Calculate the area using the square root of the Heron's formula expression
            triangleAreaValue = Math.sqrt(heronInsideValue);
        } else {
            // If not a valid triangle, area is 0.0 (three collinear points for example)
            triangleAreaValue = 0.0;
        }

        // Prepare the final printed value; here we simply print the double value
        double finalTriangleAreaToPrint = triangleAreaValue;

        // Display the area of the triangle as requested
        System.out.println("The area of the triangle is " + finalTriangleAreaToPrint);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}