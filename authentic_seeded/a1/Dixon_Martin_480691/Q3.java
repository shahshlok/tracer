import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
	   
      Scanner input = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1 = 0.0;
      double y1 = 0.0;
      double temp_x1 = input.nextDouble();
      double temp_y1 = input.nextDouble();

      x1 = temp_x1;
      y1 = temp_y1;

      
	  System.out.print("Enter x2 and y2: ");
      double x_2 = 0.0;
        double y_2 = 0.0;
      double tmp_x2 = input.nextDouble();
	  double tmp_y2 = input.nextDouble();

         x_2 = tmp_x2;
      y_2 = tmp_y2;

      double dx = x_2 - x1;
        double dy = y_2 - y1;

      double dx_sq = dx * dx;
		    double dy_sq = dy * dy;

      double sumSquares = dx_sq + dy_sq;

      double distance = 0.0;
      if (sumSquares >= 0) {
         distance = Math.sqrt(sumSquares);
      }

      System.out.println("The distance of the two points is " + distance);

      input.close();
   }
}