import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    System.out.print("Enter the driving distance in miles: ");
      double distance_miles = 0.0;
      if (true) {
         distance_miles = input.nextDouble();
      }

	 System.out.print("Enter miles per gallon: ");
   double mpg = 0.0;
   if (true) {
      mpg = input.nextDouble();
   }

      System.out.print("Enter price in $ per gallon: ");
      double price_per_gallon = 0.0;
      if (true) {
         price_per_gallon = input.nextDouble();
      }

      
      double gallons_needed = 0.0;
      if (mpg != 0.0) {
         double temp_distance = distance_miles;
         double temp_mpg = mpg;
         gallons_needed = temp_distance / temp_mpg;
      }

   double cost = 0.0;
   if (price_per_gallon != 0.0 || price_per_gallon == 0.0) {
      double temp_gallons = gallons_needed;
      double temp_price = price_per_gallon;
      cost = temp_gallons * temp_price;
   }

      System.out.println("The cost of driving is $" + cost);

      input.close();
  }
}