import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner so we can read values that the user types on the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program does
        System.out.println("Enter three points for a triangle.");

        // Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double thirdPointXValue = userInputScanner.nextDouble();
        double thirdPointYValue = userInputScanner.nextDouble();

        // Now we calculate the length of each side of the triangle using the distance formula
        // side1 is between point1 and point2
        int sideOneLength = (int) Math.sqrt(
                Math.pow(secondPointXValue - firstPointXValue, 2) +
                Math.pow(secondPointYValue - firstPointYValue, 2)
        );

        // side2 is between point2 and point3
        int sideTwoLength = (int) Math.sqrt(
                Math.pow(thirdPointXValue - secondPointXValue, 2) +
                Math.pow(thirdPointYValue - secondPointYValue, 2)
        );

        // side3 is between point3 and point1
        int sideThreeLength = (int) Math.sqrt(
                Math.pow(firstPointXValue - thirdPointXValue, 2) +
                Math.pow(firstPointYValue - thirdPointYValue, 2)
        );

        // Now we use Heron's formula to calculate the area
        // First calculate s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2;

        // Then calculate area = sqrt(s * (s - side1) * (s - side2) * (s - side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the Scanner because we are done reading input
        userInputScanner.close();
    }
}