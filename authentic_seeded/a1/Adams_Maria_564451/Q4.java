import java.util.Scanner;

public class TriangleAreaHeron {

    public static void main(String[] args) {

        Scanner inputScanner = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");

        System.out.print("(x1, y1): ");
        double x1 = inputScanner.nextDouble();
        double y1 = inputScanner.nextDouble();

        System.out.print("(x2, y2): ");
        double x2 = inputScanner.nextDouble();
        double y2 = inputScanner.nextDouble();

        System.out.print("(x3, y3): ");
        double x3 = inputScanner.nextDouble();
        double y3 = inputScanner.nextDouble();


        double side1 = distanceBetweenPoints(x1, y1, x2, y2);
        double side2 = distanceBetweenPoints(x2, y2, x3, y3);
        double side3 = distanceBetweenPoints(x3, y3, x1, y1);


        double semiPerimeter = (side1 + side2 + side3) / 2.0;

        double area = Math.sqrt(semiPerimeter
                * (semiPerimeter - side1)
                * (semiPerimeter - side2)
                * (semiPerimeter - side3));

        System.out.println("The area of the triangle is " + area);

        inputScanner.close();
    }


    public static double distanceBetweenPoints(double startX, double startY, double endX, double endY) {

        double differenceX = endX - startX;
        double differenceY = endY - startY;

        double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY);

        return distance;
    }
}
