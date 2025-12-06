import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = input.nextDouble();

      
		System.out.print("Enter miles per gallon: ");
		double mpg = input.nextDouble();

   System.out.print("Enter price in $ per gallon: ");
   double price_per_gallon = input.nextDouble();

      double holder_cost = 0.0;

      if (mpg != 0) {
         double gallons_needed = distance / mpg;
         
         if (price_per_gallon != 0 || price_per_gallon == 0) {
            holder_cost = gallons_needed * price_per_gallon;
         }
      }

      double total_cost = holder_cost;

      System.out.println("The cost of driving is $" + total_cost);

      input.close();
   }
}