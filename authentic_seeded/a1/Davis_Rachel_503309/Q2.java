import java.util.Scanner;
public class Q2 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double y = x.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double n = x.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double a = x.nextDouble();
        double b = 0;
        if (n != 0) {
            double c = y / n;
            if (a != 0) {
                double d = c * a;
                b = d;
            }
        }
        System.out.println("The cost of driving is $" + b);
    }
}