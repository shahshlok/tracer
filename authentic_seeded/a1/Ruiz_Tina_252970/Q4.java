import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        String userInputFirstLine = userInputScanner.nextLine();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        String userInputSecondLine = userInputScanner.nextLine();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        String userInputThirdLine = userInputScanner.nextLine();

        // Temporary variables to hold the cleaned input without spaces
        String cleanedFirstLine = userInputFirstLine.replace(" ", "");
        String cleanedSecondLine = userInputSecondLine.replace(" ", "");
        String cleanedThirdLine = userInputThirdLine.replace(" ", "");

        // Temporary variables to hold the split values as strings
        String[] firstPointParts = null;
        String[] secondPointParts = null;
        String[] thirdPointParts = null;

        // Split the first point into x1 and y1 if the cleaned string is not empty
        if (cleanedFirstLine != null && cleanedFirstLine.length() > 0) {
            firstPointParts = cleanedFirstLine.split("");
        }

        // Split the second point into x2 and y2 if the cleaned string is not empty
        if (cleanedSecondLine != null && cleanedSecondLine.length() > 0) {
            secondPointParts = cleanedSecondLine.split("");
        }

        // Split the third point into x3 and y3 if the cleaned string is not empty
        if (cleanedThirdLine != null && cleanedThirdLine.length() > 0) {
            thirdPointParts = cleanedThirdLine.split("");
        }

        // Initialize coordinate values with 0.0 as a safe default
        double x1Value = 0.0;
        double y1Value = 0.0;
        double x2Value = 0.0;
        double y2Value = 0.0;
        double x3Value = 0.0;
        double y3Value = 0.0;

        // Convert the first point string parts to numeric values, if they exist
        if (firstPointParts != null && firstPointParts.length >= 2) {
            // Concatenate parts to handle possible multi-digit numbers
            StringBuilder x1Builder = new StringBuilder();
            StringBuilder y1Builder = new StringBuilder();

            // Use a temporary middle index to help split between x and y
            int middleIndexFirstPoint = firstPointParts.length / 2;

            for (int currentIndex = 0; currentIndex < firstPointParts.length; currentIndex++) {
                if (currentIndex < middleIndexFirstPoint) {
                    x1Builder.append(firstPointParts[currentIndex]);
                } else {
                    y1Builder.append(firstPointParts[currentIndex]);
                }
            }

            String x1String = x1Builder.toString();
            String y1String = y1Builder.toString();

            if (x1String != null && x1String.length() != 0) {
                x1Value = Double.parseDouble(x1String);
            }

            if (y1String != null && y1String.length() != 0) {
                y1Value = Double.parseDouble(y1String);
            }
        }

        // Convert the second point string parts to numeric values, if they exist
        if (secondPointParts != null && secondPointParts.length >= 2) {
            StringBuilder x2Builder = new StringBuilder();
            StringBuilder y2Builder = new StringBuilder();

            int middleIndexSecondPoint = secondPointParts.length / 2;

            for (int currentIndex = 0; currentIndex < secondPointParts.length; currentIndex++) {
                if (currentIndex < middleIndexSecondPoint) {
                    x2Builder.append(secondPointParts[currentIndex]);
                } else {
                    y2Builder.append(secondPointParts[currentIndex]);
                }
            }

            String x2String = x2Builder.toString();
            String y2String = y2Builder.toString();

            if (x2String != null && x2String.length() != 0) {
                x2Value = Double.parseDouble(x2String);
            }

            if (y2String != null && y2String.length() != 0) {
                y2Value = Double.parseDouble(y2String);
            }
        }

        // Convert the third point string parts to numeric values, if they exist
        if (thirdPointParts != null && thirdPointParts.length >= 2) {
            StringBuilder x3Builder = new StringBuilder();
            StringBuilder y3Builder = new StringBuilder();

            int middleIndexThirdPoint = thirdPointParts.length / 2;

            for (int currentIndex = 0; currentIndex < thirdPointParts.length; currentIndex++) {
                if (currentIndex < middleIndexThirdPoint) {
                    x3Builder.append(thirdPointParts[currentIndex]);
                } else {
                    y3Builder.append(thirdPointParts[currentIndex]);
                }
            }

            String x3String = x3Builder.toString();
            String y3String = y3Builder.toString();

            if (x3String != null && x3String.length() != 0) {
                x3Value = Double.parseDouble(x3String);
            }

            if (y3String != null && y3String.length() != 0) {
                y3Value = Double.parseDouble(y3String);
            }
        }

        // Now calculate the lengths of the three sides using the distance formula
        // side1 is between point 1 (x1, y1) and point 2 (x2, y2)
        double xDifferenceSide1 = x2Value - x1Value;
        double yDifferenceSide1 = y2Value - y1Value;
        double xDifferenceSide1Squared = xDifferenceSide1 * xDifferenceSide1;
        double yDifferenceSide1Squared = yDifferenceSide1 * yDifferenceSide1;
        double sumSquaresSide1 = xDifferenceSide1Squared + yDifferenceSide1Squared;
        double side1Length = Math.sqrt(sumSquaresSide1);

        // side2 is between point 2 (x2, y2) and point 3 (x3, y3)
        double xDifferenceSide2 = x3Value - x2Value;
        double yDifferenceSide2 = y3Value - y2Value;
        double xDifferenceSide2Squared = xDifferenceSide2 * xDifferenceSide2;
        double yDifferenceSide2Squared = yDifferenceSide2 * yDifferenceSide2;
        double sumSquaresSide2 = xDifferenceSide2Squared + yDifferenceSide2Squared;
        double side2Length = Math.sqrt(sumSquaresSide2);

        // side3 is between point 3 (x3, y3) and point 1 (x1, y1)
        double xDifferenceSide3 = x1Value - x3Value;
        double yDifferenceSide3 = y1Value - y3Value;
        double xDifferenceSide3Squared = xDifferenceSide3 * xDifferenceSide3;
        double yDifferenceSide3Squared = yDifferenceSide3 * yDifferenceSide3;
        double sumSquaresSide3 = xDifferenceSide3Squared + yDifferenceSide3Squared;
        double side3Length = Math.sqrt(sumSquaresSide3);

        // Now compute s = (side1 + side2 + side3) / 2 using a temporary perimeter variable
        double perimeterOfTriangle = side1Length + side2Length + side3Length;
        double semiPerimeterValue = 0.0;
        if (perimeterOfTriangle != 0) {
            semiPerimeterValue = perimeterOfTriangle / 2.0;
        }

        // Now compute the area using Heron's formula:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double semiMinusSide1 = semiPerimeterValue - side1Length;
        double semiMinusSide2 = semiPerimeterValue - side2Length;
        double semiMinusSide3 = semiPerimeterValue - side3Length;

        double productUnderSquareRoot = semiPerimeterValue * semiMinusSide1 * semiMinusSide2 * semiMinusSide3;

        double triangleArea = 0.0;
        if (productUnderSquareRoot >= 0) {
            triangleArea = Math.sqrt(productUnderSquareRoot);
        }

        // Print out the final area of the triangle
        System.out.println("The area of the triangle is " + triangleArea);

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}