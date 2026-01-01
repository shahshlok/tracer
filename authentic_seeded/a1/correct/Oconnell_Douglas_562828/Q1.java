import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input  = new Scanner(System.in);

      
      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
      	 double v1 = 0.0;
      double t_time = 0.0;

      if (true) {
         v0 = input.nextDouble();
      }

      if (true) {
      	 v1 = input.nextDouble();
      }

      if (true) {
         t_time = input.nextDouble();
      }

      
      double denominator = t_time;

      
      if (denominator != 0.0) {
          double num = v1 - v0;
          double result_accel = num / denominator;

          
          System.out.println("The average acceleration is " + result_accel);
      } else {
          double result_accel = 0.0;
          if (result_accel == 0.0) {
              System.out.println("The average acceleration is " + result_accel);
          }
      }

      input.close();
   }
}