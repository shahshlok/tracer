import java.util.Scanner;

public class Q4 {

    // This method calculates the distance between two points using the distance formula
    public static double calculateDistanceBetweenTwoPoints(double firstPointXValue, double firstPointYValue,
                                                           double secondPointXValue, double secondPointYValue) {
        // Step 1: Calculate the difference in x values
        double differenceInXValues = secondPointXValue - firstPointXValue;
        // Step 2: Calculate the difference in y values
        double differenceInYValues = secondPointYValue - firstPointYValue;
        // Step 3: Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;
        // Step 4: Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;
        // Step 5: Add the squared differences
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;
        // Step 6: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);
        // Step 7: Return the distance
        return distanceBetweenPoints;
    }

    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask the user to enter the first point (x1, y1)
        System.out.print("(x1, y1):");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the second point (x2, y2)
        System.out.print("(x2, y2):");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Step 5: Ask the user to enter the third point (x3, y3)
        System.out.print("(x3, y3):");
        double thirdPointXValue = userInputScanner.nextDouble();
        double thirdPointYValue = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 using the distance between point1 and point2
        double triangleSideOneLength = calculateDistanceBetweenTwoPoints(firstPointXValue, firstPointYValue,
                                                                         secondPointXValue, secondPointYValue);

        // Step 7: Calculate the length of side2 using the distance between point2 and point3
        double triangleSideTwoLength = calculateDistanceBetweenTwoPoints(secondPointXValue, secondPointYValue,
                                                                         thirdPointXValue, thirdPointYValue);

        // Step 8: Calculate the length of side3 using the distance between point3 and point1
        double triangleSideThreeLength = calculateDistanceBetweenTwoPoints(thirdPointXValue, thirdPointYValue,
                                                                           firstPointXValue, firstPointYValue);

        // Step 9: Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double triangleSemiPerimeter = (triangleSideOneLength + triangleSideTwoLength + triangleSideThreeLength) / 2.0;

        // Step 10: Calculate the area using Heron's formula:
        // area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double triangleAreaValue = Math.sqrt(
                triangleSemiPerimeter
                        * (triangleSemiPerimeter - triangleSideOneLength)
                        * (triangleSemiPerimeter - triangleSideTwoLength)
                        * (triangleSemiPerimeter - triangleSideThreeLength)
        );

        // Step 11: Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }
}