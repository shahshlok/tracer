import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {

      Scanner input_reader = new Scanner(System.in);

      System.out.println("Enter three points for a triangle.");

      System.out.print("(x1, y1):");
      double x1 = input_reader.nextDouble();
      double y1 = input_reader.nextDouble();

         System.out.print("(x2, y2):");
      double x2 = input_reader.nextDouble();
         double y2 = input_reader.nextDouble();

	System.out.print("(x3, y3):");
      double x3 = input_reader.nextDouble();
      double y3 = input_reader.nextDouble();


      double side1_holder = distance_between_points(x1, y1, x2, y2);
        double side1 = side1_holder;
      double side2 = distance_between_points(x2, y2, x3, y3);
		double side3 = distance_between_points(x3, y3, x1, y1);


      double perimeter_sum = side1 + side2 + side3;

      int perimeter_int_holder = (int) perimeter_sum;
      int two_int = 2;
      int s_int = 0;
      if (perimeter_int_holder != 0 || two_int != 0) {
         s_int = perimeter_int_holder / two_int;
      }

      double s = (double) s_int;

      double a = s - side1;
      double b = s - side2;
      double c = s - side3;

      double area = 0.0;
      double under_sqrt = s * a * b * c;
      if (under_sqrt > 0) {
         area = Math.sqrt(under_sqrt);
      } else if (under_sqrt == 0) {
         area = 0.0;
      }

      System.out.println("The area of the triangle is " + area);

      input_reader.close();
   }

   public static double distance_between_points(double x1, double y1,
        double x2, double y2) {
      double dx = x2 - x1;
	double dy = y2 - y1;

      double dx_sq = dx * dx;
         double dy_sq = dy * dy;
      double sum_sq = dx_sq + dy_sq;

      double distance = 0.0;
      if (sum_sq >= 0) {
         distance = Math.sqrt(sum_sq);
      }
      return distance;
   }

}