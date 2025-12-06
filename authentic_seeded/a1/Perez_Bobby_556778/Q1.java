import java.util.Scanner;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v = x.nextDouble();
        double y = x.nextDouble();
        double n = x.nextDouble();
        double a = y - v;
        double b = 0;
        if (n != 0) {
            b = a / n;
        }
        System.out.print("The average acceleration is ");
        System.out.print(b);
    }
}