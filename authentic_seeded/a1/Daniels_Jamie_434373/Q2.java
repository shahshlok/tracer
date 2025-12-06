import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

    System.out.print("Enter the driving distance in miles: ");
    double distance_in_miles = 0;

    input.nextDouble();

      System.out.print("Enter miles per gallon: ");
  double milesPerGallon = 0;

		input.nextDouble();

		System.out.print("Enter price in $ per gallon: ");
		double price_per_gallon = 0;

		input.nextDouble();


    double a = distance_in_miles;
      double b = milesPerGallon;
   double c = price_per_gallon;

	  double gallons_used = a / b;
  double cost_of_driving = gallons_used * c;

        System.out.println("The cost of driving is $" + cost_of_driving);

    input.close();
 }
}