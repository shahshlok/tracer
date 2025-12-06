import java.util.Scanner;
public class Q2 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        double y = 0;
        double n = 0;
        double p = 0;
        double d = 0;
        if (n != 0) {
            d = (y / n) * p;
        }
        System.out.print("Enter the driving distance in miles: ");
        if (y == 0) {
            y = x.nextDouble();
        }
        System.out.print("Enter miles per gallon: ");
        if (n == 0) {
            n = x.nextDouble();
        }
        System.out.print("Enter price in $ per gallon: ");
        if (p == 0) {
            p = x.nextDouble();
        }
        System.out.print("The cost of driving is $" + d);
        x.close();
    }
}