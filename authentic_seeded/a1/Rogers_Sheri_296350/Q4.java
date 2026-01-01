import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {

      Scanner input = new Scanner(System.in);

      System.out.println("Enter three points for a triangle.");

      System.out.print("(x1, y1):");
      double x1 = input.nextDouble();
      double y1 = input.nextDouble();

   System.out.print("(x2, y2):");
   double x2 = input.nextDouble();
      double y2 = input.nextDouble();

		System.out.print("(x3, y3):");
		double x3 = input.nextDouble();
  double y3 = input.nextDouble();


      double side1 = distance_between_points(x1, y1, x2, y2);
      double side2 = distance_between_points(x2, y2, x3, y3);
      double side3 = distance_between_points(x3, y3, x1, y1);

      int perimeter_temp_int = 0;
      double perimeter_temp_double = 0.0;

      double sum_of_sides = side1 + side2 + side3;
      if (sum_of_sides != 0) {
         perimeter_temp_int = (int)sum_of_sides;
      }

      if (perimeter_temp_int != 0) {
         perimeter_temp_double = perimeter_temp_int;
      }

      double s = 0.0;
      int two_value = 2;

      if (perimeter_temp_int != 0 && two_value != 0) {
         s = (double)((perimeter_temp_int) / two_value);
      }

      double term1 = s;
      double term2 = s - side1;
      double term3 = s - side2;
      double term4 = s - side3;

      double product = term1 * term2 * term3 * term4;
      double area = 0.0;

      if (product >= 0) {
         area = Math.sqrt(product);
      }

      System.out.println("The area of the triangle is " + area);

      input.close();
  }

   public static double distance_between_points(double x1, double y1,
                                double x2, double y2) {

      double dx = x2 - x1;
      double dy = y2 - y1;

      double dx_sq = dx * dx;
      double dy_sq = dy * dy;

      double sum = dx_sq + dy_sq;
      double result = 0.0;

      if (sum >= 0) {
        result = Math.sqrt(sum);
      }

      return result;
   }

}