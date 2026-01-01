import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        double x1 = input.nextDouble();
        double y1 = input.nextDouble();

        System.out.print("(x2, y2):");
        double x2 = input.nextDouble();
        double y2 = input.nextDouble();

        System.out.print("(x3, y3):");
        double x3 = input.nextDouble();
        double y3 = input.nextDouble();

        double aDx = x2 - x1;
        double aDy = y2 - y1;
        double aSumSquares = aDx * aDx + aDy * aDy;
        double side1 = Math.sqrt(aSumSquares);

        double bDx = x3 - x2;
        double bDy = y3 - y2;
        double bSumSquares = bDx * bDx + bDy * bDy;
        double side2 = Math.sqrt(bSumSquares);

        double cDx = x1 - x3;
        double cDy = y1 - y3;
        double cSumSquares = cDx * cDx + cDy * cDy;
        double side3 = Math.sqrt(cSumSquares);

        double sNumerator = side1 + side2 + side3;
        double s = sNumerator / 2.0;

        double a = s - side1;
        double b = s - side2;
        double c = s - side3;

        double underRoot = s * a * b * c;
        double area;
        if (underRoot <= 0) {
            area = 0.0;
        } else {
            area = Math.sqrt(underRoot);
        }

        System.out.println("The area of the triangle is " + area);
    }
}