import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distanceInput = scanner.nextDouble();
        double distance = distanceInput;

        System.out.print("Enter miles per gallon: ");
        double mpgInput = scanner.nextDouble();
        double mpg = mpgInput;

        System.out.print("Enter price in $ per gallon: ");
        double priceInput = scanner.nextDouble();
        double price = priceInput;

        double cost = 0.0;
        if (mpg != 0.0) {
            cost = (distance / mpg) * price;
        }

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}