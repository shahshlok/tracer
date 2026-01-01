import java.util.Scanner;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v = 0;
        double w = 0;
        double t = 0;
        if (x.hasNextDouble()) v = x.nextDouble();
        if (x.hasNextDouble()) w = x.nextDouble();
        if (x.hasNextDouble()) t = x.nextDouble();
        double a = 0;
        if (t != 0) a = (w - v) / t;
        System.out.print("The average acceleration is " + a);
    }
}