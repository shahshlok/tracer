import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

	 double distanceMiles = 0.0;
    double miles_per_gallon = 0.0;
      double pricePerGallon = 0.0;


      double cost_of_driving = (distanceMiles / miles_per_gallon) * pricePerGallon;

	 System.out.print("Enter the driving distance in miles: ");
    distanceMiles = input.nextDouble();

      System.out.print("Enter miles per gallon: ");
	miles_per_gallon = input.nextDouble();

   System.out.print("Enter price in $ per gallon: ");
      pricePerGallon = input.nextDouble();


 System.out.println("The cost of driving is $" + cost_of_driving);

    input.close();
  }
}