import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double distance = 0;
        double mpg = 0;
        double pricePerGallon = 0;

        System.out.print("Enter the driving distance in miles: ");
        scanner.nextDouble();

        System.out.print("Enter miles per gallon: ");
        scanner.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        scanner.nextDouble();

        double cost = (distance / mpg) * pricePerGallon;

        System.out.println("The cost of driving is $" + cost);

        scanner.close();
    }
}