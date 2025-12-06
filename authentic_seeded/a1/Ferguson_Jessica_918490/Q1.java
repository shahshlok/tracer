import java.util.Scanner;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v = 0;
        double w = 0;
        double t = 0;
        double y = 0;
        x.nextDouble();
        x.nextDouble();
        x.nextDouble();
        if (t != 0) {
            double n = w - v;
            y = n / t;
        }
        System.out.println("The average acceleration is " + y);
    }
}