import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

	 System.out.print("Enter the driving distance in miles: ");
    double distanceMiles = input.nextDouble();

      System.out.print("Enter miles per gallon: ");
	double miles_per_gallon = input.nextDouble();

   System.out.print("Enter price in $ per gallon: ");
      double pricePerGallon = input.nextDouble();


      double cost_of_driving = (distanceMiles / miles_per_gallon) * pricePerGallon;

 System.out.println("The cost of driving is $" + cost_of_driving);

    input.close();
  }
}