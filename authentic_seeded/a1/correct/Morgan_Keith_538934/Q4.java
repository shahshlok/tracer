import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program is asking for
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        int firstPointXValue = userInputScanner.nextInt();
        int firstPointYValue = userInputScanner.nextInt();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        int secondPointXValue = userInputScanner.nextInt();
        int secondPointYValue = userInputScanner.nextInt();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        int thirdPointXValue = userInputScanner.nextInt();
        int thirdPointYValue = userInputScanner.nextInt();

        // Calculate the length of the first side using distance formula between point 1 and point 2
        int firstSideDeltaX = secondPointXValue - firstPointXValue;
        int firstSideDeltaY = secondPointYValue - firstPointYValue;

        // Convert to double for precise mathematical calculation
        double firstSideDeltaXSquared = firstSideDeltaX * firstSideDeltaX;
        double firstSideDeltaYSquared = firstSideDeltaY * firstSideDeltaY;

        double firstSideSumOfSquares = firstSideDeltaXSquared + firstSideDeltaYSquared;
        double firstSideLength = 0.0;
        if (firstSideSumOfSquares >= 0) { // extra check, though squares should be non-negative
            firstSideLength = Math.sqrt(firstSideSumOfSquares);
        }

        // Calculate the length of the second side using distance formula between point 2 and point 3
        int secondSideDeltaX = thirdPointXValue - secondPointXValue;
        int secondSideDeltaY = thirdPointYValue - secondPointYValue;

        double secondSideDeltaXSquared = secondSideDeltaX * secondSideDeltaX;
        double secondSideDeltaYSquared = secondSideDeltaY * secondSideDeltaY;

        double secondSideSumOfSquares = secondSideDeltaXSquared + secondSideDeltaYSquared;
        double secondSideLength = 0.0;
        if (secondSideSumOfSquares >= 0) {
            secondSideLength = Math.sqrt(secondSideSumOfSquares);
        }

        // Calculate the length of the third side using distance formula between point 1 and point 3
        int thirdSideDeltaX = thirdPointXValue - firstPointXValue;
        int thirdSideDeltaY = thirdPointYValue - firstPointYValue;

        double thirdSideDeltaXSquared = thirdSideDeltaX * thirdSideDeltaX;
        double thirdSideDeltaYSquared = thirdSideDeltaY * thirdSideDeltaY;

        double thirdSideSumOfSquares = thirdSideDeltaXSquared + thirdSideDeltaYSquared;
        double thirdSideLength = 0.0;
        if (thirdSideSumOfSquares >= 0) {
            thirdSideLength = Math.sqrt(thirdSideSumOfSquares);
        }

        // Now we use Heron's formula to calculate the area of the triangle

        // First compute s = (side1 + side2 + side3) / 2
        double sumOfAllSides = firstSideLength + secondSideLength + thirdSideLength;

        double triangleSemiPerimeter = 0.0;
        if (sumOfAllSides != 0) { // check to avoid weird division, even though 0 would mean no triangle
            triangleSemiPerimeter = sumOfAllSides / 2.0;
        }

        // Compute each (s - sideX) part
        double semiPerimeterMinusFirstSide = triangleSemiPerimeter - firstSideLength;
        double semiPerimeterMinusSecondSide = triangleSemiPerimeter - secondSideLength;
        double semiPerimeterMinusThirdSide = triangleSemiPerimeter - thirdSideLength;

        // Calculate the expression inside the square root: s * (s - side1) * (s - side2) * (s - side3)
        double heronExpression = triangleSemiPerimeter
                * semiPerimeterMinusFirstSide
                * semiPerimeterMinusSecondSide
                * semiPerimeterMinusThirdSide;

        double triangleAreaValue = 0.0;

        // Check that the value inside the square root is not negative
        if (heronExpression > 0) {
            triangleAreaValue = Math.sqrt(heronExpression);
        } else if (heronExpression == 0) {
            // This means the points might be collinear or degenerate triangle, area is 0
            triangleAreaValue = 0.0;
        } else {
            // If negative due to floating point rounding, set to 0 as a safety
            triangleAreaValue = 0.0;
        }

        // Print the final area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}