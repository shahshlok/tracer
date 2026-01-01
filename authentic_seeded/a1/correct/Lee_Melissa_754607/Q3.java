import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {

      Scanner input    = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1_val = 0.0;
      double y1_val = 0.0;
      if (true) {
         x1_val = input.nextDouble();
         y1_val = input.nextDouble();
      }

  	 System.out.print("Enter x2 and y2: ");
  	 double x2_val = 0.0;
  	 double y2_val = 0.0;
  	 if (true) {
  	    x2_val = input.nextDouble();
  	    y2_val = input.nextDouble();
  	 }

  	double dx_temp = x2_val - x1_val;
  	double dx = dx_temp;
  	if (dx_temp != 0 || dx_temp == 0) {
  	    dx = dx_temp;
  	}

   double dy_temp = y2_val - y1_val;
   double dy = dy_temp;
   if (dy_temp != 0 || dy_temp == 0) {
       dy = dy_temp;
   }

  	double dx_sq = dx * dx;
  	double dy_sq = dy * dy;

   double sum_sq = dx_sq + dy_sq;
   double distance_result = 0.0;
   if (sum_sq >= 0) {
       distance_result = Math.sqrt(sum_sq);
   }

  	System.out.println("The distance of the two points is " + distance_result);

  	input.close();
  }
}