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

      double cost = 0.0;

      if (mpg != 0) {
         double distance_holder = distance;
         double mpg_holder = mpg;
         double price_holder = price_per_gallon;

         double gallons_used = distance_holder / mpg_holder;

         if (gallons_used != 0 || gallons_used == 0) {
            cost = gallons_used * price_holder;
         }
      }

      System.out.println("The cost of driving is $" + cost);

      input.close();
  }

}