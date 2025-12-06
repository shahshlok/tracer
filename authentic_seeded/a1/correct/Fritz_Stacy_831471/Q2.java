import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

   System.out.print("Enter the driving distance in miles: ");
      double distance_miles = input.nextDouble();

		System.out.print("Enter miles per gallon: ");
   double mpg = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
  double price_per_gallon = input.nextDouble();



   double holder_distance = distance_miles;
      double holder_mpg = mpg;
		double holder_price = price_per_gallon;

   double cost = 0.0;

   if (holder_mpg != 0) {
        double gallons_needed = holder_distance / holder_mpg;

	    double temp_gallons = gallons_needed;

        if (temp_gallons != 0) {
          cost = temp_gallons * holder_price;
        } else {
          cost = 0.0;
        }
   } else {
        cost = 0.0;
   }


      System.out.print("The cost of driving is $" + cost);

   input.close();
  }
}