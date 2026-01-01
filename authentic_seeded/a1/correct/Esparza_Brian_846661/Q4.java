import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is expecting
        System.out.println("Enter three points for a triangle.");

        // Ask user for first point (x1, y1)
        System.out.print("(x1, y1):");
        // Read the first x value
        double pointOneXValue = userInputScanner.nextDouble();
        // Read the first y value
        double pointOneYValue = userInputScanner.nextDouble();

        // Ask user for second point (x2, y2)
        System.out.print("(x2, y2):");
        // Read the second x value
        double pointTwoXValue = userInputScanner.nextDouble();
        // Read the second y value
        double pointTwoYValue = userInputScanner.nextDouble();

        // Ask user for third point (x3, y3)
        System.out.print("(x3, y3):");
        // Read the third x value
        double pointThreeXValue = userInputScanner.nextDouble();
        // Read the third y value
        double pointThreeYValue = userInputScanner.nextDouble();

        // Now we calculate the length of each side using the distance formula from Question 3
        // side1 is between point 1 and point 2
        double sideOneLength = Math.sqrt(
                Math.pow(pointTwoXValue - pointOneXValue, 2) +
                Math.pow(pointTwoYValue - pointOneYValue, 2)
        );

        // side2 is between point 2 and point 3
        double sideTwoLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointTwoXValue, 2) +
                Math.pow(pointThreeYValue - pointTwoYValue, 2)
        );

        // side3 is between point 3 and point 1
        double sideThreeLength = Math.sqrt(
                Math.pow(pointThreeXValue - pointOneXValue, 2) +
                Math.pow(pointThreeYValue - pointOneYValue, 2)
        );

        // Now we use Heron's formula
        // Step 1: calculate s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Step 2: calculate area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Step 3: display the area to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner because we are done with input
        userInputScanner.close();
    }
}