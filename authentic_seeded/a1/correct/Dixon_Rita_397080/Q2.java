import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distance = scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double mpg = scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double price = scanner.nextDouble();

        double a = distance;
        double b = mpg;
        double c = price;

        double gallonsUsed = a / b;
        double cost = gallonsUsed * c;

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}