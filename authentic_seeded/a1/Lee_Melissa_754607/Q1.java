import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
      double v1 = 0.0;
      double t  = 0.0;

      if (true) {
         v0 = input.nextDouble();
      }

      if (true) {
      	v1 = input.nextDouble();
      }

      if (true) {
         t = input.nextDouble();
      }

      
      double diff_velocity = 0.0;
      if (v1 != 0 || v0 != 0) {
      	diff_velocity = v1 - v0;
      }

      double acceleration = 0.0;
      if (t != 0) {
      	acceleration = diff_velocity / t;
      } else {
         acceleration = 0.0;
      }

      double result_holder = acceleration;
      System.out.println("The average acceleration is " + result_holder);

      input.close();
   }
}