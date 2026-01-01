import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
      Scanner input   = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");
      
      double v0 = 0.0;
      double v1 = 0.0;
      double t  = 0.0;

      if (input != null) {
         v0 = input.nextDouble();
         v1 = input.nextDouble();
         t  = input.nextDouble();
      }

      
      double delta_v = v1 - v0;
      
      		double acceleration = 0.0;

      if (t != 0.0) {
         double temp = delta_v / t;
         acceleration = temp;
      }

      System.out.println("The average acceleration is " + acceleration);

      input.close();
  }
}