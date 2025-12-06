import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
  	Scanner in = new Scanner(System.in);

    System.out.print("Enter x1 and y1: ");
    double x1_temp = 0.0;
      double y1_temp = 0.0;
    if (true) {
      x1_temp = in.nextDouble();
      y1_temp = in.nextDouble();
    }

        System.out.print("Enter x2 and y2: ");
    double x2Value = 0.0;
      double y2_value = 0.0;
    if (true) {
      x2Value = in.nextDouble();
      y2_value = in.nextDouble();
    }

    
      double dx = x2Value - x1_temp;
   double dy = y2_value - y1_temp;

    double dx_sq = dx * dx;
    	 double dy_sq = dy * dy;

    double sum_squares = dx_sq + dy_sq;

    double distanceSquared = 0.0;
    if (sum_squares >= 0) {
        distanceSquared = sum_squares;
    }

    if (distanceSquared >= 0) {
       Math.sqrt(distanceSquared);
    }

        System.out.println("The distance of the two points is " + distanceSquared);

    in.close();
  }
}