import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = input.nextDouble();

         System.out.print("Enter miles per gallon: ");
      double mpg_value = input.nextDouble();

		System.out.print("Enter price in $ per gallon: ");
      double pricePerGallon = input.nextDouble();


      double temp_distance = distance;
      double temp_mpg = mpg_value;
      double temp_price = pricePerGallon;

      double gallons_used = 0.0;
      if (temp_mpg != 0) {
         gallons_used = temp_distance / temp_mpg;
      } else {
         gallons_used = 0.0;
      }

      double cost = 0.0;
      if (temp_price != 0) {
         double temp_gallons = gallons_used;
         cost = temp_gallons * temp_price;
      } else {
         cost = 0.0;
      }

      
      System.out.println("The cost of driving is $" + cost);

      input.close();
   }
}