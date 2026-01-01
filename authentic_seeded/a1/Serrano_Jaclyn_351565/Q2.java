import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distanceInput = scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double mpgInput = scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double priceInput = scanner.nextDouble();

        double distance = distanceInput;
        double mpg = mpgInput;
        double price = priceInput;

        double cost = 0.0;
        if (mpg != 0.0) {
            double gallonsUsed = distance / mpg;
            double gallonsHolder = gallonsUsed;
            if (price != 0.0 || price == 0.0) {
                cost = gallonsHolder * price;
            }
        }

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}