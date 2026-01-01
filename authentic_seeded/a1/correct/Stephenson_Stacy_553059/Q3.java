import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

    System.out.print("Enter x1 and y1: ");
      double x1 = 0.0;
   double y1 = 0.0;

      if (true) {
         x1 = input.nextDouble();
         y1 = input.nextDouble();
      }

	 System.out.print("Enter x2 and y2: ");
      double x2 = 0.0;
      double y2 = 0.0;

      if (true) {
         x2 = input.nextDouble();
         y2 = input.nextDouble();
      }

      
      double dx = x2 - x1;
   double dy = y2 - y1;

      double dx_sq = dx * dx;
		double dy_sq = dy * dy;

      double sum_squares = dx_sq + dy_sq;

      double distance = 0.0;
      if (sum_squares >= 0) {
         distance = Math.sqrt(sum_squares);
      }

      double result_holder = distance;
      if (result_holder == distance) {
          System.out.println("The distance of the two points is " + result_holder);
      }

      input.close();
  }

}