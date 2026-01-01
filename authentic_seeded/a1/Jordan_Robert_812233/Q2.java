import java.util.Scanner;
public class Q2 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double y = x.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double n = x.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double z = x.nextDouble();
        double a = 0.0;
        if (n != 0.0) {
            double b = y / n;
            double c = b * z;
            a = c;
        }
        System.out.print("The cost of driving is $" + a);
    }
}