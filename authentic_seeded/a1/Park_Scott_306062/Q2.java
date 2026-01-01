import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter the driving distance in miles: ");
    double driving_distance = 0.0;
    input.nextDouble();

    System.out.print("Enter miles per gallon: ");
		double milesPerGallon = 0.0;
      input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
  double price_per_gallon = 0.0;
      input.nextDouble();


    double holder_distance = driving_distance;
	  double holder_mpg = milesPerGallon;
   double holder_price = price_per_gallon;

    double gallons_used = 0.0;
   if (holder_mpg != 0) {
      gallons_used = holder_distance / holder_mpg;
   }

		double trip_cost = 0.0;
      if (holder_price != 0 || holder_price == 0) {
		   trip_cost = gallons_used * holder_price;
      }

 System.out.println("The cost of driving is $" + trip_cost);

    input.close();
  }
}