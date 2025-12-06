import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        int x1 = input.nextInt();
        int y1 = input.nextInt();

        System.out.print("(x2, y2):");
        int x2 = input.nextInt();
        int y2 = input.nextInt();

        System.out.print("(x3, y3):");
        int x3 = input.nextInt();
        int y3 = input.nextInt();

        double aX = x2 - x1;
        double aY = y2 - y1;
        double side1 = Math.sqrt(aX * aX + aY * aY);

        double bX = x3 - x2;
        double bY = y3 - y2;
        double side2 = Math.sqrt(bX * bX + bY * bY);

        double cX = x1 - x3;
        double cY = y1 - y3;
        double side3 = Math.sqrt(cX * cX + cY * cY);

        double s = (side1 + side2 + side3) / 2.0;

        double a = s;
        double b = s - side1;
        double c = s - side2;
        double d = s - side3;

        double area = Math.sqrt(a * b * c * d);

        System.out.println("The area of the triangle is " + area);
    }
}