import java.util.Scanner;
public class Q2
// TODO: Clean up code before submission
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double distance=scan.nextDouble();
        System.out.print("Enter miles per gallon: ");
        System.out.println("----------------");
        double milesPerGallon=scan.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double price=scan.nextDouble();
        double cost=(distance/milesPerGallon)*price;
        System.out.println("The cost of driving is $" + cost);
    }
}