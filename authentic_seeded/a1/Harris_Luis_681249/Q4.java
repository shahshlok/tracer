import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the coordinates for three points of the triangle
        System.out.println("Enter three points for a triangle.");

        // Read first point (x1, y1)
        System.out.print("(x1, y1):");
        String firstPointInputString = userInputScanner.nextLine();

        // Read second point (x2, y2)
        System.out.print("(x2, y2):");
        String secondPointInputString = userInputScanner.nextLine();

        // Read third point (x3, y3)
        System.out.print("(x3, y3):");
        String thirdPointInputString = userInputScanner.nextLine();

        // Extract x1 and y1 from the first input
        double x1CoordinateValue = 0.0;
        double y1CoordinateValue = 0.0;
        if (firstPointInputString != null) {
            String trimmedFirstPointInputString = firstPointInputString.trim();
            if (trimmedFirstPointInputString.length() >= 2) {
                String x1Substring = trimmedFirstPointInputString.substring(0, 1);
                String y1Substring = trimmedFirstPointInputString.substring(1);
                if (x1Substring.length() > 0 && y1Substring.length() > 0) {
                    try {
                        x1CoordinateValue = Double.parseDouble(x1Substring);
                    } catch (NumberFormatException numberFormatException) {
                        x1CoordinateValue = 0.0;
                    }
                    try {
                        y1CoordinateValue = Double.parseDouble(y1Substring);
                    } catch (NumberFormatException numberFormatException) {
                        y1CoordinateValue = 0.0;
                    }
                }
            }
        }

        // Extract x2 and y2 from the second input
        double x2CoordinateValue = 0.0;
        double y2CoordinateValue = 0.0;
        if (secondPointInputString != null) {
            String trimmedSecondPointInputString = secondPointInputString.trim();
            if (trimmedSecondPointInputString.length() >= 2) {
                String x2Substring = trimmedSecondPointInputString.substring(0, 1);
                String y2Substring = trimmedSecondPointInputString.substring(1);
                if (x2Substring.length() > 0 && y2Substring.length() > 0) {
                    try {
                        x2CoordinateValue = Double.parseDouble(x2Substring);
                    } catch (NumberFormatException numberFormatException) {
                        x2CoordinateValue = 0.0;
                    }
                    try {
                        y2CoordinateValue = Double.parseDouble(y2Substring);
                    } catch (NumberFormatException numberFormatException) {
                        y2CoordinateValue = 0.0;
                    }
                }
            }
        }

        // Extract x3 and y3 from the third input
        double x3CoordinateValue = 0.0;
        double y3CoordinateValue = 0.0;
        if (thirdPointInputString != null) {
            String trimmedThirdPointInputString = thirdPointInputString.trim();
            if (trimmedThirdPointInputString.length() >= 2) {
                String x3Substring = trimmedThirdPointInputString.substring(0, 1);
                String y3Substring = trimmedThirdPointInputString.substring(1);
                if (x3Substring.length() > 0 && y3Substring.length() > 0) {
                    try {
                        x3CoordinateValue = Double.parseDouble(x3Substring);
                    } catch (NumberFormatException numberFormatException) {
                        x3CoordinateValue = 0.0;
                    }
                    try {
                        y3CoordinateValue = Double.parseDouble(y3Substring);
                    } catch (NumberFormatException numberFormatException) {
                        y3CoordinateValue = 0.0;
                    }
                }
            }
        }

        // Calculate the lengths of the sides using the distance formula from question 3
        // side1 is the distance between point1 (x1, y1) and point2 (x2, y2)
        double xDifferenceSide1 = x2CoordinateValue - x1CoordinateValue;
        double yDifferenceSide1 = y2CoordinateValue - y1CoordinateValue;
        double xDifferenceSide1Squared = xDifferenceSide1 * xDifferenceSide1;
        double yDifferenceSide1Squared = yDifferenceSide1 * yDifferenceSide1;
        double side1LengthValue = Math.sqrt(xDifferenceSide1Squared + yDifferenceSide1Squared);

        // side2 is the distance between point2 (x2, y2) and point3 (x3, y3)
        double xDifferenceSide2 = x3CoordinateValue - x2CoordinateValue;
        double yDifferenceSide2 = y3CoordinateValue - y2CoordinateValue;
        double xDifferenceSide2Squared = xDifferenceSide2 * xDifferenceSide2;
        double yDifferenceSide2Squared = yDifferenceSide2 * yDifferenceSide2;
        double side2LengthValue = Math.sqrt(xDifferenceSide2Squared + yDifferenceSide2Squared);

        // side3 is the distance between point3 (x3, y3) and point1 (x1, y1)
        double xDifferenceSide3 = x1CoordinateValue - x3CoordinateValue;
        double yDifferenceSide3 = y1CoordinateValue - y3CoordinateValue;
        double xDifferenceSide3Squared = xDifferenceSide3 * xDifferenceSide3;
        double yDifferenceSide3Squared = yDifferenceSide3 * yDifferenceSide3;
        double side3LengthValue = Math.sqrt(xDifferenceSide3Squared + yDifferenceSide3Squared);

        // Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double sumOfAllSidesValue = side1LengthValue + side2LengthValue + side3LengthValue;
        double semiPerimeterValue = 0.0;
        if (sumOfAllSidesValue != 0) {
            semiPerimeterValue = sumOfAllSidesValue / 2.0;
        }

        // Calculate the expression inside the square root for Heron's formula
        double semiPerimeterMinusSide1 = semiPerimeterValue - side1LengthValue;
        double semiPerimeterMinusSide2 = semiPerimeterValue - side2LengthValue;
        double semiPerimeterMinusSide3 = semiPerimeterValue - side3LengthValue;

        double productUnderSquareRootValue = semiPerimeterValue
                * semiPerimeterMinusSide1
                * semiPerimeterMinusSide2
                * semiPerimeterMinusSide3;

        // In case of very small negative due to floating point, protect the sqrt
        if (productUnderSquareRootValue < 0 && productUnderSquareRootValue > -0.0000001) {
            productUnderSquareRootValue = 0.0;
        }

        double areaOfTriangleValue = 0.0;
        if (productUnderSquareRootValue >= 0) {
            areaOfTriangleValue = Math.sqrt(productUnderSquareRootValue);
        }

        // Print the result exactly as required
        System.out.println("The area of the triangle is " + areaOfTriangleValue);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}