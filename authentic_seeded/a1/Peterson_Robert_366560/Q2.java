import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      
      double distance = 0;
   double mpg_value = 0;
      	double pricePerGallon = 0;

      
      double a = distance;
      	double b = mpg_value;
   double c = pricePerGallon;

      
      double fuel_used = a / b;
      	double cost = fuel_used * c;

      System.out.print("Enter the driving distance in miles: ");
      distance = input.nextDouble();

  	 System.out.print("Enter miles per gallon: ");
      mpg_value = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
      	 pricePerGallon = input.nextDouble();

      System.out.println("The cost of driving is $" + cost);

      input.close();
   }
}