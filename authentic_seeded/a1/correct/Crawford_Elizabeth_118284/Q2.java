import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

	 System.out.print("Enter the driving distance in miles: ");
    double distance_miles = input.nextDouble();

      System.out.print("Enter miles per gallon: ");
   double mpg = input.nextDouble();

System.out.print("Enter price in $ per gallon: ");
      double pricePerGallon = input.nextDouble();


      double cost_of_driving = (distance_miles / mpg) * pricePerGallon;

 System.out.println("The cost of driving is $" + cost_of_driving);

  }
}