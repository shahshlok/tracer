import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      
      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = input.nextDouble();

		System.out.print("Enter miles per gallon: ");
      double miles_per_gallon = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
  	 double pricePerGallon = input.nextDouble();


      double holder_distance = distance;
      double holder_mpg = miles_per_gallon;
      double holder_price = pricePerGallon;

      double cost = 0.0;

      if (holder_mpg != 0) {
      	  double gallons_used = holder_distance / holder_mpg;
         if (holder_price != 0 || holder_price == 0) {
            cost = gallons_used * holder_price;
         }
      }

      System.out.println("The cost of driving is $" + cost);

      input.close();
   }
}