import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distance = scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double mpg = scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallon = scanner.nextDouble();

        double cost = 0.0;
        double tempDistance = distance;
        double tempMpg = mpg;
        double tempPrice = pricePerGallon;

        if (tempMpg != 0.0) {
            double gallonsUsed = tempDistance / tempMpg;
            double tempGallonsUsed = gallonsUsed;
            cost = tempGallonsUsed * tempPrice;
        }

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}