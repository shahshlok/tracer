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

        double cost = (distance / milesPerGallon) * pricePerGallon;

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}