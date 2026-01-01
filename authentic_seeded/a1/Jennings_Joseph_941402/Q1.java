import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	System.out.print("Enter v0, v1, and t: ");
   double v0 = input.nextDouble();
    double v1_value = input.nextDouble();
      double t_time = input.nextDouble();

      
      double a, b, c;
      a = v1_value;
      b = v0;
      c = t_time;

      double acceleration_result = (a - b) / c;

      System.out.println("The average acceleration is " + acceleration_result);

      input.close();
  }
}