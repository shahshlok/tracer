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
        double t = m;
        if (t != 0.0) {
            c = (d / t) * p;
        }
        System.out.println("The cost of driving is $" + c);
        x.close();
    }
}