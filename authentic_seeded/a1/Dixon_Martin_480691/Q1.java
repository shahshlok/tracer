import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {

   Scanner input   = new Scanner(System.in);

   System.out.print("Enter v0, v1, and t: ");

   double v0 = 0.0;
      double v1   = 0.0;
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

      double time_holder = t_time;

      if (time_holder != 0.0) {

          double numerator = v1 - v0;

          double a = numerator / time_holder;

          double result_holder = a;

          System.out.println("The average acceleration is " + result_holder);

      } else {

          double a = 0.0;
          if (a == 0.0) {
             System.out.println("The average acceleration is " + a);
          }

      }

      input.close();
  }

}