import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    
    double distance_miles = 0;
      double milesPerGallon = 0;
        double price_per_gallon = 0;

    
    double cost = (distance_miles / milesPerGallon) * price_per_gallon;

    System.out.print("Enter the driving distance in miles: ");
    distance_miles = input.nextDouble();

      System.out.print("Enter miles per gallon: ");
   milesPerGallon = input.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
	price_per_gallon = input.nextDouble();


      System.out.println("The cost of driving is $" + cost);

    input.close();
  }
}