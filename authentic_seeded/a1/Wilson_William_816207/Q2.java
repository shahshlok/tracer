import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distance = scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double milesPerGallon = scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallon = scanner.nextDouble();

        double costOfDriving = 0.0;
        double distanceHolder = distance;
        double mpgHolder = milesPerGallon;
        double priceHolder = pricePerGallon;

        if (mpgHolder != 0.0) {
            double gallonsUsed = distanceHolder / mpgHolder;
            double gallonsUsedHolder = gallonsUsed;
            costOfDriving = gallonsUsedHolder * priceHolder;
        }

        System.out.println("The cost of driving is $" + costOfDriving);

        scanner.close();
    }
}