import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double inputDistance = scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double inputMpg = scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double inputPrice = scanner.nextDouble();

        double distance = inputDistance;
        double mpg = inputMpg;
        double price = inputPrice;

        double gallonsUsed = 0.0;
        if (mpg != 0.0) {
            gallonsUsed = distance / mpg;
        }

        double cost = gallonsUsed * price;

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}