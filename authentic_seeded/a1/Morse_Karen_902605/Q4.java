import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Explain to the user what to enter
        System.out.println("Enter three points for a triangle.");

        // Ask user for first point (x1, y1)
        System.out.print("(x1, y1):");
        // Read the first x and y as a single string like "00" or "5 0"
        String firstPointInputString = userInputScanner.nextLine();
        // Declare variables for coordinates of first point
        double x1Coordinate;
        double y1Coordinate;
        // If the user types coordinates together like "00", we treat each character as a digit
        if (firstPointInputString.length() == 2 && !firstPointInputString.contains(" ")) {
            x1Coordinate = Character.getNumericValue(firstPointInputString.charAt(0));
            y1Coordinate = Character.getNumericValue(firstPointInputString.charAt(1));
        } else {
            // Otherwise, split by space and parse
            String[] firstPointParts = firstPointInputString.trim().split("\\s+");
            x1Coordinate = Double.parseDouble(firstPointParts[0]);
            y1Coordinate = Double.parseDouble(firstPointParts[1]);
        }

        // Ask user for second point (x2, y2)
        System.out.print("(x2, y2):");
        String secondPointInputString = userInputScanner.nextLine();
        double x2Coordinate;
        double y2Coordinate;
        if (secondPointInputString.length() == 2 && !secondPointInputString.contains(" ")) {
            x2Coordinate = Character.getNumericValue(secondPointInputString.charAt(0));
            y2Coordinate = Character.getNumericValue(secondPointInputString.charAt(1));
        } else {
            String[] secondPointParts = secondPointInputString.trim().split("\\s+");
            x2Coordinate = Double.parseDouble(secondPointParts[0]);
            y2Coordinate = Double.parseDouble(secondPointParts[1]);
        }

        // Ask user for third point (x3, y3)
        System.out.print("(x3, y3):");
        String thirdPointInputString = userInputScanner.nextLine();
        double x3Coordinate;
        double y3Coordinate;
        if (thirdPointInputString.length() == 2 && !thirdPointInputString.contains(" ")) {
            x3Coordinate = Character.getNumericValue(thirdPointInputString.charAt(0));
            y3Coordinate = Character.getNumericValue(thirdPointInputString.charAt(1));
        } else {
            String[] thirdPointParts = thirdPointInputString.trim().split("\\s+");
            x3Coordinate = Double.parseDouble(thirdPointParts[0]);
            y3Coordinate = Double.parseDouble(thirdPointParts[1]);
        }

        // Now we compute the lengths of the three sides using the distance formula

        // Compute side1 which is the distance between point1 (x1, y1) and point2 (x2, y2)
        double side1DifferenceInX = x2Coordinate - x1Coordinate;
        double side1DifferenceInY = y2Coordinate - y1Coordinate;
        double side1SquaredDifferenceInX = side1DifferenceInX * side1DifferenceInX;
        double side1SquaredDifferenceInY = side1DifferenceInY * side1DifferenceInY;
        double side1SumOfSquares = side1SquaredDifferenceInX + side1SquaredDifferenceInY;
        double side1Length = Math.sqrt(side1SumOfSquares);

        // Compute side2 which is the distance between point2 (x2, y2) and point3 (x3, y3)
        double side2DifferenceInX = x3Coordinate - x2Coordinate;
        double side2DifferenceInY = y3Coordinate - y2Coordinate;
        double side2SquaredDifferenceInX = side2DifferenceInX * side2DifferenceInX;
        double side2SquaredDifferenceInY = side2DifferenceInY * side2DifferenceInY;
        double side2SumOfSquares = side2SquaredDifferenceInX + side2SquaredDifferenceInY;
        double side2Length = Math.sqrt(side2SumOfSquares);

        // Compute side3 which is the distance between point1 (x1, y1) and point3 (x3, y3)
        double side3DifferenceInX = x3Coordinate - x1Coordinate;
        double side3DifferenceInY = y3Coordinate - y1Coordinate;
        double side3SquaredDifferenceInX = side3DifferenceInX * side3DifferenceInX;
        double side3SquaredDifferenceInY = side3DifferenceInY * side3DifferenceInY;
        double side3SumOfSquares = side3SquaredDifferenceInX + side3SquaredDifferenceInY;
        double side3Length = Math.sqrt(side3SumOfSquares);

        // Now we use Heron's formula to compute the area of the triangle

        // Step 1: compute the semi-perimeter s = (side1 + side2 + side3) / 2
        double a = side1Length;
        double b = side2Length;
        double c = side3Length;
        double semiPerimeterNumerator = a + b + c;
        double semiPerimeter = semiPerimeterNumerator / 2.0;

        // Step 2: compute the expression under the square root: s(s - a)(s - b)(s - c)
        double semiPerimeterMinusA = semiPerimeter - a;
        double semiPerimeterMinusB = semiPerimeter - b;
        double semiPerimeterMinusC = semiPerimeter - c;
        double areaUnderSquareRoot = semiPerimeter * semiPerimeterMinusA * semiPerimeterMinusB * semiPerimeterMinusC;

        // Step 3: compute the area as the square root of that expression
        double triangleArea = Math.sqrt(areaUnderSquareRoot);

        // Print the area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the scanner
        userInputScanner.close();
    }
}