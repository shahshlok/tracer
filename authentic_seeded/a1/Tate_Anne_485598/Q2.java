import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Scanner input  = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance_miles = input.nextDouble();

      	 System.out.print("Enter miles per gallon: ");
      double milesPerGallon = input.nextDouble();

      
      System.out.print("Enter price in $ per gallon: ");
         double price_per_gallon = input.nextDouble();


      double tempDistance = distance_miles;
      double tempMPG = milesPerGallon;
      double tempPrice = price_per_gallon;

      double gallons_used = 0.0;
      if (tempMPG != 0) {
         gallons_used = tempDistance / tempMPG;
      }

      		double cost = 0.0;
      if (gallons_used != 0 || tempPrice != 0) {
         cost = gallons_used * tempPrice;
      }

      System.out.println("The cost of driving is $" + cost);

      input.close();
   }
}