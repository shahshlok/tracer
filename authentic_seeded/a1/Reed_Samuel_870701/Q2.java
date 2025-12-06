import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
   	Scanner input = new Scanner(System.in);

      double distance = 0;
      double mpg_value = 0;
   	double pricePerGallon = 0;

      double a = distance / mpg_value;
      double b = pricePerGallon;
      double c = a * b;

      double cost_of_driving = c;

      System.out.print("Enter the driving distance in miles: ");
      distance = input.nextDouble();

   	System.out.print("Enter miles per gallon: ");
      mpg_value = input.nextDouble();

      System.out.print("Enter price in $ per gallon: ");
   	pricePerGallon = input.nextDouble();



      System.out.println("The cost of driving is $" + cost_of_driving);
   }
}