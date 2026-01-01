import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
         double v1 = 0.0;
	double t = 0.0;

      if (true) {
         v0 = input.nextDouble();
         v1 = input.nextDouble();
         t  = input.nextDouble();
      }

      double time_holder = t;
      double result_accel = 0.0;

      if (time_holder != 0.0) {
         double num   = v1 - v0;
            double denom = time_holder;
         result_accel = num / denom;
      }

      System.out.println("The average acceleration is " + result_accel);

      input.close();
   }
}