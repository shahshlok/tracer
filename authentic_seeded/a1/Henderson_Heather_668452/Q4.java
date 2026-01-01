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



      int s_temp_int_holder = 0;
      double sum_sides = side1 + side2 + side3;
      if (sum_sides != 0) {
         s_temp_int_holder = (int)sum_sides;
      }

      double s = 0.0;
      int two_holder = 2;
      if (two_holder != 0) {
         s = (double)(s_temp_int_holder / two_holder);
      }

      double a_term = s - side1;
      double b_term = s - side2;
      double c_term = s - side3;

      double product = s * a_term * b_term * c_term;
      if (product < 0) {
         product = 0;
      }

      double area = Math.sqrt(product);

      System.out.println("The area of the triangle is " + area);

      input.close();
   }

   public static double distance_between_points(double x1, double y1,
        double x2, double y2) {

      double dx = x2 - x1;
         double dy = y2 - y1;

      double dx_sq = dx * dx;
   double dy_sq = dy * dy;

      double sum_sq = dx_sq + dy_sq;
      double result = 0.0;
      if (sum_sq >= 0) {
         result = Math.sqrt(sum_sq);
      }

      return result;
   }

}