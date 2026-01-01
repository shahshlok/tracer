import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter the driving distance in miles: ");
    double driving_distance = sc.nextDouble();

      System.out.print("Enter miles per gallon: ");
      double milesPerGallon = sc.nextDouble();

   System.out.print("Enter price in $ per gallon: ");
   double price_per_gallon = sc.nextDouble();


      double cost = (driving_distance / milesPerGallon) * price_per_gallon;

	System.out.println("The cost of driving is $" + cost);
  }
}