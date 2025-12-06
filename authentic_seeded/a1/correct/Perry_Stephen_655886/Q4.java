import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Ask for first point (x1, y1)
        System.out.print("(x1, y1):");
        double x1UserInput = userInputScanner.nextDouble();
        double y1UserInput = userInputScanner.nextDouble();

        // Ask for second point (x2, y2)
        System.out.print("(x2, y2):");
        double x2UserInput = userInputScanner.nextDouble();
        double y2UserInput = userInputScanner.nextDouble();

        // Ask for third point (x3, y3)
        System.out.print("(x3, y3):");
        double x3UserInput = userInputScanner.nextDouble();
        double y3UserInput = userInputScanner.nextDouble();

        // Now calculate the lengths of the three sides using distance formula
        // side1 is the distance between point1 and point2
        double side1DifferenceX = x2UserInput - x1UserInput;
        double side1DifferenceY = y2UserInput - y1UserInput;
        double side1DifferenceXSquared = side1DifferenceX * side1DifferenceX;
        double side1DifferenceYSquared = side1DifferenceY * side1DifferenceY;
        double side1SumOfSquares = side1DifferenceXSquared + side1DifferenceYSquared;
        double side1Length = Math.sqrt(side1SumOfSquares);

        // side2 is the distance between point2 and point3
        double side2DifferenceX = x3UserInput - x2UserInput;
        double side2DifferenceY = y3UserInput - y2UserInput;
        double side2DifferenceXSquared = side2DifferenceX * side2DifferenceX;
        double side2DifferenceYSquared = side2DifferenceY * side2DifferenceY;
        double side2SumOfSquares = side2DifferenceXSquared + side2DifferenceYSquared;
        double side2Length = Math.sqrt(side2SumOfSquares);

        // side3 is the distance between point3 and point1
        double side3DifferenceX = x1UserInput - x3UserInput;
        double side3DifferenceY = y1UserInput - y3UserInput;
        double side3DifferenceXSquared = side3DifferenceX * side3DifferenceX;
        double side3DifferenceYSquared = side3DifferenceY * side3DifferenceY;
        double side3SumOfSquares = side3DifferenceXSquared + side3DifferenceYSquared;
        double side3Length = Math.sqrt(side3SumOfSquares);

        // Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSides = side1Length + side2Length + side3Length;
        double semiPerimeterS = sumOfAllSides / 2.0;

        // Use Heron's formula: area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double semiPerimeterMinusSide1 = semiPerimeterS - side1Length;
        double semiPerimeterMinusSide2 = semiPerimeterS - side2Length;
        double semiPerimeterMinusSide3 = semiPerimeterS - side3Length;

        // Extra cautious: make sure we are not dealing with negative values under the square root
        double productUnderSquareRoot = semiPerimeterS * semiPerimeterMinusSide1 * semiPerimeterMinusSide2 * semiPerimeterMinusSide3;

        if (productUnderSquareRoot < 0) {
            // If this happens, something is wrong with the triangle sides (maybe they do not form a valid triangle)
            // To be safe, we will set area to 0.0
            productUnderSquareRoot = 0.0;
        }

        double triangleArea = Math.sqrt(productUnderSquareRoot);

        // Print the area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}