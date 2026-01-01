import java.util.Scanner;
public class Q2 {
    public static void main(String[] x) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double a = s.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double b = s.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double c = s.nextDouble();
        double d = 0.0;
        if (b != 0.0) {
            d = a / b;
        }
        double e = 0.0;
        if (c != 0.0 || c == 0.0) {
            e = d * c;
        }
        System.out.print("The cost of driving is $" + e);
        s.close();
    }
}