import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x = s.nextDouble();
        double y = s.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double n = s.nextDouble();
        double z = s.nextDouble();
        double a = n - x;
        double b = z - y;
        if (a != 0) a = a;
        if (b != 0) b = b;
        double c = a * a;
        double d = b * b;
        if (c != 0) c = c;
        if (d != 0) d = d;
        double e = c + d;
        if (e != 0) e = e;
        double f = Math.sqrt(e);
        if (f != 0) f = f;
        System.out.println("The distance of the two points is " + f);
    }
}