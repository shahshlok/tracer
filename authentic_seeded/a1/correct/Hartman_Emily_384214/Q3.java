import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
      Scanner input    = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1_value = 0.0;
      double y1_value = 0.0;

      if (input.hasNextDouble()) {
         x1_value = input.nextDouble();
      }
      if (input.hasNextDouble()) {
         y1_value = input.nextDouble();
      }

		System.out.print("Enter x2 and y2: ");
      double x2_value = 0.0;
      double y2_value = 0.0;

      if (input.hasNextDouble()) {
         x2_value = input.nextDouble();
      }
      if (input.hasNextDouble()) {
         y2_value = input.nextDouble();
      }

      double dx  = x2_value - x1_value;
		  double dy  = y2_value - y1_value;

      double dx_sq = dx * dx;
      double dy_sq = dy * dy;

      double sum_squares = dx_sq + dy_sq;

      double distance_result = 0.0;
      if (sum_squares >= 0) {
         distance_result = Math.sqrt(sum_squares);
      }

      System.out.println("The distance of the two points is " + distance_result);

      input.close();
   }

}