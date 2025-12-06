import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what the program is going to do
        System.out.println("Enter three points for a triangle.");

        // Ask user for the first point (x1, y1)
        System.out.print("(x1, y1):");
        // Read the first x and y as strings, then parse to double so input like "00" works
        String userInputX1String = userInputScanner.next();
        String userInputY1String = userInputScanner.next();
        double userInputX1Value = Double.parseDouble(userInputX1String);
        double userInputY1Value = Double.parseDouble(userInputY1String);

        // Ask user for the second point (x2, y2)
        System.out.print("(x2, y2):");
        String userInputX2String = userInputScanner.next();
        String userInputY2String = userInputScanner.next();
        double userInputX2Value = Double.parseDouble(userInputX2String);
        double userInputY2Value = Double.parseDouble(userInputY2String);

        // Ask user for the third point (x3, y3)
        System.out.print("(x3, y3):");
        String userInputX3String = userInputScanner.next();
        String userInputY3String = userInputScanner.next();
        double userInputX3Value = Double.parseDouble(userInputX3String);
        double userInputY3Value = Double.parseDouble(userInputY3String);

        // Now we calculate the length of each side of the triangle using distance formula
        // side1 is the distance between point1 (x1, y1) and point2 (x2, y2)
        double side1Length = Math.sqrt(Math.pow(userInputX2Value - userInputX1Value, 2) +
                                       Math.pow(userInputY2Value - userInputY1Value, 2));

        // side2 is the distance between point2 (x2, y2) and point3 (x3, y3)
        double side2Length = Math.sqrt(Math.pow(userInputX3Value - userInputX2Value, 2) +
                                       Math.pow(userInputY3Value - userInputY2Value, 2));

        // side3 is the distance between point1 (x1, y1) and point3 (x3, y3)
        double side3Length = Math.sqrt(Math.pow(userInputX3Value - userInputX1Value, 2) +
                                       Math.pow(userInputY3Value - userInputY1Value, 2));

        // Now we calculate the semi-perimeter s using the formula s = (side1 + side2 + side3) / 2
        double semiPerimeterValue = (side1Length + side2Length + side3Length) / 2.0;

        // Now we calculate the area using Heron's formula:
        // area = sqrt( s * (s - side1) * (s - side2) * (s - side3) )
        double triangleAreaValue = Math.sqrt(semiPerimeterValue *
                                             (semiPerimeterValue - side1Length) *
                                             (semiPerimeterValue - side2Length) *
                                             (semiPerimeterValue - side3Length));

        // Display the result to the user
        System.out.println("The area of the triangle is " + triangleAreaValue);

        // Close the scanner because we are done with input
        userInputScanner.close();
    }
}