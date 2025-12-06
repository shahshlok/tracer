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
      double x2 = 0.0;
   double y2 = 0.0;
		double temp_x2 = input.nextDouble();
      double temp_y2 = input.nextDouble();

    x2 = temp_x2;
  y2 = temp_y2;



   double dx = x2 - x1;
      double dy = y2 - y1;

	 double dx_squared = dx * dx;
  double dy_squared = dy * dy;

    double sum_squares = dx_squared + dy_squared;
      double distance = 0.0;

    if (sum_squares >= 0) {
	 distance = Math.sqrt(sum_squares);
    }

      double final_distance = distance;

   System.out.println("The distance of the two points is " + final_distance);

      input.close();
  }
}