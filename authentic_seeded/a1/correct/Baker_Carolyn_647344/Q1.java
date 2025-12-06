import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
         double v1	  = 0.0;
   double t_time = 0.0;

      if (input.hasNextDouble()) {
         v0 = input.nextDouble();
      }

      if (input.hasNextDouble()) {
          v1 = input.nextDouble();
      }

      		if (input.hasNextDouble()) {
     	    t_time = input.nextDouble();
		}

      double denom = t_time;
      double diff_velocity = v1 - v0;
      double acceleration = 0.0;

      if (denom != 0.0) {
         double temp_result = diff_velocity / denom;
         acceleration = temp_result;
      }

      System.out.println("The average acceleration is " + acceleration);

      input.close();
   }

}