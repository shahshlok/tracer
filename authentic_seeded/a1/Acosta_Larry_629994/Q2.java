import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter the driving distance in miles: ");
    double distance_miles = input.nextDouble();

      System.out.print("Enter miles per gallon: ");
      double milesPerGallon = input.nextDouble();

  	 System.out.print("Enter price in $ per gallon: ");
  	 double price_per_gallon = input.nextDouble();


    double temp_cost = 0.0;

    if (milesPerGallon != 0) {
       double fuel_used = distance_miles / milesPerGallon;

       if (price_per_gallon != 0 || price_per_gallon == 0) {
          temp_cost = fuel_used * price_per_gallon;
       }
    }

      double final_cost = temp_cost;

      System.out.print("The cost of driving is $" + final_cost);

      input.close();
  }
}