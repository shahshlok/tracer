import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
	Scanner input  = new Scanner(System.in);

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

      
      double dx_holder = x2 - x1;
   double dy_holder = y2 - y1;

      double dx_squared = dx_holder * dx_holder;
         double dy_squared = dy_holder * dy_holder;

      double sum_squares = dx_squared + dy_squared;
      if (sum_squares == 0) {
      	// distance is zero, but still compute
      }

      double distance_result = Math.sqrt(sum_squares);

      if (distance_result != 0 || sum_squares == 0) {
            System.out.println("The distance of the two points is " + distance_result);
      }

      input.close();
   }
}