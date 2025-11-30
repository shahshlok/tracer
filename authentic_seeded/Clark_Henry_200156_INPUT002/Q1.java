import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v0 = s.nextDouble();
        double v1 = s.nextDouble();
        double t = s.nextDouble();
        double a = (v1 - v0) / t;
        System.out.println("The average acceleration is " + a);
    }
}