import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

      Scanner input  = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = 0.0;
      if (true) {
         distance = input.nextDouble();
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

      
      double cost = 0.0;
      if (mpg != 0.0) {
         double distanceHolder = distance;
         double mpgHolder = mpg;
         double priceHolder = price_per_gallon;

         double gallons_used = distanceHolder / mpgHolder;

         if (gallons_used != 0.0 || gallons_used == 0.0) {
            cost = gallons_used * priceHolder;
         }
      }

      
      System.out.println("The cost of driving is $" + cost);

      input.close();
  }
}