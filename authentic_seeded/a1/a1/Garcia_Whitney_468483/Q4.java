import java.util.Scanner;

public class Q4_TriangleArea {

    public static void main(String[] args) {

        // create a scanner to read input from the keyboard
        Scanner input = new Scanner(System.in);

        // print instructions for the user
        System.out.println("Enter three points for a triangle.");

        // declare variables for all coordinates of the three points
        double x1, y1, x2, y2, x3, y3;

        // ask the user for the first point
        System.out.print("(x1, y1):");
        x1 = input.nextDouble();
        y1 = input.nextDouble();

        // ask the user for the second point
        System.out.print("(x2, y2):");
        x2 = input.nextDouble();
        y2 = input.nextDouble();

        // ask the user for the third point
        System.out.print("(x3, y3):");
        x3 = input.nextDouble();
        y3 = input.nextDouble();

        // calculate length of side1 (between point1 and point2) using distance formula from Q3
        double side1 = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        // calculate length of side2 (between point2 and point3)
        double side2 = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));

        // calculate length of side3 (between point3 and point1)
        double side3 = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));

        // calculate semi-perimeter s using Heron's formula
        double s = (side1 + side2 + side3) / 2.0;

        // calculate the area using Heron's formula
        double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));

        // display the area of the triangle
        System.out.println("The area of the triangle is " + area);

        // close the scanner
        input.close();
    }
}
