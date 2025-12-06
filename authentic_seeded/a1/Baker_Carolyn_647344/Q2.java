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


      double safe_distance = distance;
      double safe_mpg = mpg_value;
      double safe_price = pricePerGallon;

      double cost = 0.0;

      if (safe_mpg != 0) {
         cost = (safe_distance / safe_mpg) * safe_price;
      } else {
      	cost = 0.0;
      }

      double final_cost = cost;

      System.out.println("The cost of driving is $" + final_cost);

      input.close();
   }
}