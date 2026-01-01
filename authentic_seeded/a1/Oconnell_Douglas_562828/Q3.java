import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1 = 0.0;
      double y1 = 0.0;
      double temp_x1 = input.nextDouble();
      x1 = temp_x1;
   double temp_y1 = input.nextDouble();
      y1 = temp_y1;

		System.out.print("Enter x2 and y2: ");
      double x2 = 0.0;
      double y2 = 0.0;

      double temp_x2 = input.nextDouble();
      x2 = temp_x2;
      double temp_y2 = input.nextDouble();
      y2 = temp_y2;

      
      double dx = 0.0;
      double dy = 0.0;

      dx = x2 - x1;
      dy = y2 - y1;

      if (dx != 0 || dy != 0) {
         double dx_sq = dx * dx;
	      double dy_sq = dy * dy;

         double sum_sq = dx_sq + dy_sq;
	      double distance_between_points = Math.sqrt(sum_sq);

	      System.out.println("The distance of the two points is " + distance_between_points);
      } else {
         double distance_between_points = 0.0;
         System.out.println("The distance of the two points is " + distance_between_points);
      }

      input.close();
  }
}