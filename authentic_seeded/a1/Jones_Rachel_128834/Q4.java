import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program does
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Step 4: Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Step 5: Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double thirdPointXValue = userInputScanner.nextDouble();
        double thirdPointYValue = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 (between point1 and point2)
        double sideOneLength = calculateDistanceBetweenTwoPoints(
                firstPointXValue, firstPointYValue,
                secondPointXValue, secondPointYValue
        );

        // Step 7: Calculate the length of side2 (between point2 and point3)
        double sideTwoLength = calculateDistanceBetweenTwoPoints(
                secondPointXValue, secondPointYValue,
                thirdPointXValue, thirdPointYValue
        );

        // Step 8: Calculate the length of side3 (between point3 and point1)
        double sideThreeLength = calculateDistanceBetweenTwoPoints(
                thirdPointXValue, thirdPointYValue,
                firstPointXValue, firstPointYValue
        );

        // Step 9: Calculate the semi-perimeter s using Heron's formula
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Step 10: Calculate the area using Heron's formula
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue
                        * (semiPerimeterValue - sideOneLength)
                        * (semiPerimeterValue - sideTwoLength)
                        * (semiPerimeterValue - sideThreeLength)
        );

        // Step 11: Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }

    // This method calculates the distance between two points using the distance formula
    public static double calculateDistanceBetweenTwoPoints(double firstPointXValue,
                                                          double firstPointYValue,
                                                          double secondPointXValue,
                                                          double secondPointYValue) {
        // Step A: Find the difference in x values
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Step B: Find the difference in y values
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Step C: Square the differences
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step D: Add the squared differences and take the square root
        double distanceBetweenPoints = Math.sqrt(squaredDifferenceInXValues + squaredDifferenceInYValues);

        // Step E: Return the distance
        return distanceBetweenPoints;
    }
}