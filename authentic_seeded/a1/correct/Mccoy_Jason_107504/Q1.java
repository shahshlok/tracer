import java.util.Scanner;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v0 = x.nextDouble();
        double v1 = x.nextDouble();
        double t = x.nextDouble();
        double n = 0;
        if (t != 0) {
            double y = v1 - v0;
            n = y / t;
        }
        System.out.println("The average acceleration is " + n);
    }
}