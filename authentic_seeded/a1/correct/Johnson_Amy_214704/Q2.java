import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance_miles = input.nextDouble();

        System.out.print("Enter miles per gallon: ");
    double milesPerGallon = input.nextDouble();

		System.out.print("Enter price in $ per gallon: ");
      double price_per_gallon = input.nextDouble();


      double cost = (distance_miles / milesPerGallon) * price_per_gallon;

      System.out.println("The cost of driving is $" + cost);

      input.close();
   }
}