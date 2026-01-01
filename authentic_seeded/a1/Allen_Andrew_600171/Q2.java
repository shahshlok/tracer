import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the driving distance in miles: ");
        double distanceInput = scanner.nextDouble();
        double distance = distanceInput;

        System.out.print("Enter miles per gallon: ");
        double mpgInput = scanner.nextDouble();
        double milesPerGallon = mpgInput;

        System.out.print("Enter price in $ per gallon: ");
        double priceInput = scanner.nextDouble();
        double pricePerGallon = priceInput;

        double cost = 0.0;
        if (milesPerGallon != 0) {
            double gallonsUsed = distance / milesPerGallon;
            double totalCost = gallonsUsed * pricePerGallon;
            cost = totalCost;
        }

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}