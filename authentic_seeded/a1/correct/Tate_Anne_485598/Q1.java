import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {

      Scanner inputScanner = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
      double v1 = 0.0;
      double t  = 0.0;

         v0 = inputScanner.nextDouble();
      v1 = inputScanner.nextDouble();
   	t = inputScanner.nextDouble();

      
      double numerator = v1 - v0;
      double denominator = t;
      double avg_accel = 0.0;

      if (denominator != 0.0) {
         avg_accel = numerator / denominator;
      } else {
         avg_accel = 0.0;
      }

         double result_holder = avg_accel;

      System.out.println("The average acceleration is " + result_holder);

      inputScanner.close();
   }
}