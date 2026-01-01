import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
   Scanner input   = new Scanner(System.in);

	System.out.print("Enter the driving distance in miles: ");
      double distance_miles = 0.0;
      if (true) {
         distance_miles = input.nextDouble();
      }

   System.out.print("Enter miles per gallon: ");
      double milesPerGallon = 0.0;
      if (true) {
         milesPerGallon = input.nextDouble();
      }

	 System.out.print("Enter price in $ per gallon: ");
      double price_per_gallon = 0.0;
      if (true) {
         price_per_gallon = input.nextDouble();
      }

      double holder_cost = 0.0;

      if (milesPerGallon != 0) {
         double holder_distance = distance_miles;
         double holder_mpg = milesPerGallon;
         double holder_price = price_per_gallon;

         holder_cost = (holder_distance / holder_mpg) * holder_price;
      }

      double final_cost = holder_cost;
      System.out.println("The cost of driving is $" + final_cost);

      input.close();
  }
}