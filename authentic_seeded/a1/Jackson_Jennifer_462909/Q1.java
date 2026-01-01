import java.util.Scanner;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v = x.nextDouble();
        double w = x.nextDouble();
        double t = x.nextDouble();
        double y = 0.0;
        double n = 0.0;
        if (w != v) {
            n = w - v;
        } else {
            n = 0.0;
        }
        if (t != 0.0) {
            y = n / t;
        } else {
            y = 0.0;
        }
        System.out.println("The average acceleration is " + y);
    }
}