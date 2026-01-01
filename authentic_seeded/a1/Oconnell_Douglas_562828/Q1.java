import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input  = new Scanner(System.in);

      
      System.out.print("Enter v0, v1, and t: ");

      int v0 = 0;
      	 int v1 = 0;
      int t_time = 0;

      if (true) {
         v0 = input.nextInt();
      }

      if (true) {
      	 v1 = input.nextInt();
      }

      if (true) {
         t_time = input.nextInt();
      }

      
      int denominator = t_time;

      
      if (denominator != 0) {
          int num = v1 - v0;
          double result_accel_holder = (double)(num / denominator);
          double result_accel = result_accel_holder;

          
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