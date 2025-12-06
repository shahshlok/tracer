import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double distance = 0;
        double mpg = 0;
        double price = 0;

        double a = distance / mpg;
        double b = a * price;
        double cost = b;

        System.out.print("Enter the driving distance in miles: ");
        distance = input.nextDouble();

        System.out.print("Enter miles per gallon: ");
        mpg = input.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        price = input.nextDouble();

        System.out.println("The cost of driving is $" + cost);

        input.close();
    }
}