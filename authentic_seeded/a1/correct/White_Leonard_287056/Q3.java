import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x1 = 0;
        double y1 = 0;
        if (x.hasNextDouble()) x1 = x.nextDouble();
        if (x.hasNextDouble()) y1 = x.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double x2 = 0;
        double y2 = 0;
        if (x.hasNextDouble()) x2 = x.nextDouble();
        if (x.hasNextDouble()) y2 = x.nextDouble();
        double n = x2 - x1;
        double y = y2 - y1;
        double d = 0;
        if (n != 0 || y != 0) d = Math.sqrt(n * n + y * y);
        System.out.println("The distance of the two points is " + d);
    }
}