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

      double holder_cost = 0.0;

      if (milesPerGallon != 0.0) {
          double distance_holder = distance_miles;
          double mpg_holder = milesPerGallon;
          double price_holder = price_per_gallon;

          holder_cost = (distance_holder / mpg_holder) * price_holder;
      }

      double cost_of_driving = holder_cost;

      System.out.print("The cost of driving is $" + cost_of_driving);
   }
}