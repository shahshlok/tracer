import java.util.Scanner;
public class Q2 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double d = x.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double m = x.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double p = x.nextDouble();
        double c = 0.0;
        if (m != 0.0) {
            double t = d / m;
            if (p != 0.0 || p == 0.0) {
                c = t * p;
            }
        }
        System.out.println("The cost of driving is $" + c);
    }
}