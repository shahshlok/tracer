import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Scanner  input   = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = input.nextDouble();

		double temp_distance = distance;
      if (temp_distance != 0 || temp_distance == 0) {
      }

   System.out.print("Enter miles per gallon: ");
      double milesPerGallon = input.nextDouble();

      double mpg_temp = milesPerGallon;
      if (mpg_temp != 0 || mpg_temp == 0) {
      }

      System.out.print("Enter price in $ per gallon: ");
		double price_per_gallon = input.nextDouble();

      double priceTemp = price_per_gallon;
      if (priceTemp != 0 || priceTemp == 0) {
      }


      double gallons_needed = 0.0;
      if (milesPerGallon != 0) {
         gallons_needed = distance / milesPerGallon;
      } else {
         gallons_needed = 0.0;
      }

      double cost = 0.0;
      if (gallons_needed != 0 || price_per_gallon != 0) {
         cost = gallons_needed * price_per_gallon;
      } else {
         cost = 0.0;
      }

      double finalCost = cost;
      System.out.println("The cost of driving is $" + finalCost);

      input.close();
   }
}