import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter v0, v1, and t: ");

    double v0 = input.nextDouble();
      double v1_value = input.nextDouble();
   double t_time = input.nextDouble();


double acceleration = (v1_value - v0) / t_time;

		    System.out.println("The average acceleration is " + acceleration);
  }
}