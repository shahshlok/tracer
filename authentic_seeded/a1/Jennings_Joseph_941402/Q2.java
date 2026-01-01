import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter the driving distance in miles: ");
      double distance = input.nextDouble();

    System.out.print("Enter miles per gallon: ");
		double mpg_value = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
   double pricePerGallon = input.nextDouble();


      double a = distance;
   double b = mpg_value;
		double c = pricePerGallon;

      double gallons_used = a / b;
		double cost = gallons_used * c;

      System.out.println("The cost of driving is $" + cost);
   }
}