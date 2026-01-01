import java.util.Scanner;

public class Q4 {

    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program is asking for
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Step 4: Ask for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Step 5: Ask for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double pointThreeXValue = userInputScanner.nextDouble();
        double pointThreeYValue = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 using the distance formula between point 1 and point 2
        double sideOneLength = calculateDistanceBetweenTwoPoints(pointOneXValue, pointOneYValue,
                                                                 pointTwoXValue, pointTwoYValue);

        // Step 7: Calculate the length of side2 using the distance formula between point 2 and point 3
        double sideTwoLength = calculateDistanceBetweenTwoPoints(pointTwoXValue, pointTwoYValue,
                                                                 pointThreeXValue, pointThreeYValue);

        // Step 8: Calculate the length of side3 using the distance formula between point 3 and point 1
        double sideThreeLength = calculateDistanceBetweenTwoPoints(pointThreeXValue, pointThreeYValue,
                                                                   pointOneXValue, pointOneYValue);

        // Step 9: Calculate s using Heron's formula part 1: s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Step 10: Calculate area using Heron's formula part 2:
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double triangleAreaValue = Math.sqrt(semiPerimeterValue
                                             * (semiPerimeterValue - sideOneLength)
                                             * (semiPerimeterValue - sideTwoLength)
                                             * (semiPerimeterValue - sideThreeLength));

        // Step 11: Print the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }

    // This method calculates the distance between two points (x1, y1) and (x2, y2)
    public static double calculateDistanceBetweenTwoPoints(double firstPointXValue, double firstPointYValue,
                                                           double secondPointXValue, double secondPointYValue) {

        // Step A: Calculate the difference in x values
        double xDifferenceValue = secondPointXValue - firstPointXValue;

        // Step B: Calculate the difference in y values
        double yDifferenceValue = secondPointYValue - firstPointYValue;

        // Step C: Use the distance formula: distance = sqrt( (x2 - x1)^2 + (y2 - y1)^2 )
        double distanceValue = Math.sqrt(xDifferenceValue * xDifferenceValue
                                         + yDifferenceValue * yDifferenceValue);

        // Step D: Return the distance
        return distanceValue;
    }
}