import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double distance = 0;
        double mpg = 0;
        double price = 0;

        System.out.print("Enter the driving distance in miles: ");
        input.nextDouble();

        System.out.print("Enter miles per gallon: ");
        input.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        input.nextDouble();

        double a = distance / mpg;
        double cost = a * price;

        System.out.println("The cost of driving is $" + cost);

        input.close();
    }
}