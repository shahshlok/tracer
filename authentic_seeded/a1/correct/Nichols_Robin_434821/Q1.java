import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input   = new Scanner(System.in);

    System.out.print("Enter v0, v1, and t: ");
    
	 double v0 = input.nextDouble();
    double v1_value = input.nextDouble();
      double t_time = input.nextDouble();

      double diff_v = v1_value - v0;
      
      double avg_acceleration = 0.0;

      if (t_time != 0) {
         avg_acceleration = diff_v / t_time;
      } else {
         avg_acceleration = 0.0;
      }

      double result_holder = avg_acceleration;

      System.out.println("The average acceleration is " + result_holder);
  }
}