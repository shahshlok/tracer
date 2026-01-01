import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Step 3: Ask the user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Step 5: Ask the user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        double thirdPointXValue = userInputScanner.nextDouble();
        double thirdPointYValue = userInputScanner.nextDouble();

        // Step 6: Calculate the length of side1 using the distance formula between point1 and point2
        double sideOneLength = Math.sqrt(
                Math.pow(secondPointXValue - firstPointXValue, 2) +
                Math.pow(secondPointYValue - firstPointYValue, 2)
        );

        // Step 7: Calculate the length of side2 using the distance formula between point2 and point3
        double sideTwoLength = Math.sqrt(
                Math.pow(thirdPointXValue - secondPointXValue, 2) +
                Math.pow(thirdPointYValue - secondPointYValue, 2)
        );

        // Step 8: Calculate the length of side3 using the distance formula between point3 and point1
        double sideThreeLength = Math.sqrt(
                Math.pow(firstPointXValue - thirdPointXValue, 2) +
                Math.pow(firstPointYValue - thirdPointYValue, 2)
        );

        // Step 9: Calculate the semi-perimeter s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (sideOneLength + sideTwoLength + sideThreeLength) / 2.0;

        // Step 10: Calculate the area using Heron's formula: area = sqrt(s(s-side1)(s-side2)(s-side3))
        double triangleAreaValue = Math.sqrt(
                semiPerimeterValue *
                (semiPerimeterValue - sideOneLength) *
                (semiPerimeterValue - sideTwoLength) *
                (semiPerimeterValue - sideThreeLength)
        );

        // Step 11: Display the area of the triangle
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Step 12: Close the Scanner
        userInputScanner.close();
    }
}