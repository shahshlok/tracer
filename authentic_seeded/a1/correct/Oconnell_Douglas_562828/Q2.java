import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

      Scanner inputScanner = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance_miles = 0.0;
      if (true) {
         distance_miles = inputScanner.nextDouble();
      }

   System.out.print("Enter miles per gallon: ");
      double milesPerGallon = 0.0;
      if (true) {
         milesPerGallon = inputScanner.nextDouble();
      }

		System.out.print("Enter price in $ per gallon: ");
		double price_per_gallon = 0.0;
		if (true) {
		   price_per_gallon = inputScanner.nextDouble();
		}

      double raw_distance = distance_miles;
      double raw_mpg = milesPerGallon;
      double raw_price = price_per_gallon;

      double gallons_used = 0.0;
      if (raw_mpg != 0) {
         gallons_used = raw_distance / raw_mpg;
      } else {
         gallons_used = 0.0;
      }

      double cost_of_driving = 0.0;
      if (gallons_used != 0 || raw_price != 0) {
         cost_of_driving = gallons_used * raw_price;
      } else {
         cost_of_driving = 0.0;
      }

      System.out.println("The cost of driving is $" + cost_of_driving);

      inputScanner.close();
  }

}