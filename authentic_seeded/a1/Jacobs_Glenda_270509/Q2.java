import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

   double distance = 0;
	double mpg = 0;
      double price_per_gallon = 0;


      double a = distance;
   double b = mpg;
	    double c = price_per_gallon;



double gallons_used = a / b;
      double cost = gallons_used * c;

    System.out.print("Enter the driving distance in miles: ");
    distance = input.nextDouble();

	 System.out.print("Enter miles per gallon: ");
    mpg = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
price_per_gallon = input.nextDouble();


 System.out.println("The cost of driving is $" + cost);
  }
}