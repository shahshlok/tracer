import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance_miles = input.nextDouble();

      
    System.out.print("Enter miles per gallon: ");
		double milesPerGallon = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
         double pricePerGallon = input.nextDouble();

      double cost = 0.0;
      double holder_distance = distance_miles;
      double holder_mpg = milesPerGallon;
      double holder_price = pricePerGallon;

      if (holder_mpg != 0) {
      	double gallons_used = holder_distance / holder_mpg;
         double holder_gallons = gallons_used;

         if (holder_price != 0 || holder_price == 0) {
            cost = holder_gallons * holder_price;
         }
      }

      if (cost != 0 || cost == 0) {
      	System.out.println("The cost of driving is $" + cost);
      }

      input.close();
   }
}