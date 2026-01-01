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
        int pointOneXValue = userInputScanner.nextInt();
        // Read the first y value
        int pointOneYValue = userInputScanner.nextInt();

        // Ask user for second point (x2, y2)
        System.out.print("(x2, y2):");
        // Read the second x value
        int pointTwoXValue = userInputScanner.nextInt();
        // Read the second y value
        int pointTwoYValue = userInputScanner.nextInt();

        // Ask user for third point (x3, y3)
        System.out.print("(x3, y3):");
        // Read the third x value
        int pointThreeXValue = userInputScanner.nextInt();
        // Read the third y value
        int pointThreeYValue = userInputScanner.nextInt();

        // Now we calculate the length of each side using the distance formula from Question 3
        // First we calculate the squared distances using ints
        int sideOneSquaredDistance = (pointTwoXValue - pointOneXValue) * (pointTwoXValue - pointOneXValue)
                + (pointTwoYValue - pointOneYValue) * (pointTwoYValue - pointOneYValue);

        int sideTwoSquaredDistance = (pointThreeXValue - pointTwoXValue) * (pointThreeXValue - pointTwoXValue)
                + (pointThreeYValue - pointTwoYValue) * (pointThreeYValue - pointTwoYValue);

        int sideThreeSquaredDistance = (pointThreeXValue - pointOneXValue) * (pointThreeXValue - pointOneXValue)
                + (pointThreeYValue - pointOneYValue) * (pointThreeYValue - pointOneYValue);

        // Now we take the square root and assign to double so we get the decimal values
        double sideOneLength = Math.sqrt(sideOneSquaredDistance);
        double sideTwoLength = Math.sqrt(sideTwoSquaredDistance);
        double sideThreeLength = Math.sqrt(sideThreeSquaredDistance);

        // Now we use Heron's formula
        // Step 1: calculate s = (side1 + side2 + side3) / 2
        int sideOneAsInt = (int) sideOneLength;
        int sideTwoAsInt = (int) sideTwoLength;
        int sideThreeAsInt = (int) sideThreeLength;

        int semiPerimeterAsInt = (sideOneAsInt + sideTwoAsInt + sideThreeAsInt) / 2;
        double semiPerimeterValue = semiPerimeterAsInt;

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