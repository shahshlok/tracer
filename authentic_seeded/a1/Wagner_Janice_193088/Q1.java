import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
  	Scanner input   = new Scanner(System.in);

  	System.out.print("Enter v0, v1, and t: ");

  	double v0 = 0.0;
    double v1    = 0.0;
      double t_time = 0.0;

  	if (input.hasNextDouble()) {
  	    double temp_v0 = input.nextDouble();
  	    v0 = temp_v0;
  	}

      if (input.hasNextDouble()) {
      	double temp_v1 = input.nextDouble();
      	v1 = temp_v1;
      }

   if (input.hasNextDouble()) {
   	  double temp_t = input.nextDouble();
   	  t_time = temp_t;
   }

   double acceleration = 0.0;

   if (t_time != 0.0) {
   	double numerator = v1 - v0;
   	acceleration = numerator / t_time;
   } else {
   	acceleration = 0.0;
   }

      System.out.println("The average acceleration is " + acceleration);

   input.close();
  }
}