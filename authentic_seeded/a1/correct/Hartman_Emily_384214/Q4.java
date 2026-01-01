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


      double side1_holder = distance_between_points(x1, y1, x2, y2);
   double side2_holder = distance_between_points(x2, y2, x3, y3);
      double side3_holder = distance_between_points(x3, y3, x1, y1);

      double side1 = side1_holder;
      double side2 = side2_holder;
      double side3 = side3_holder;

      double s_temp = (side1 + side2 + side3) / 2.0;
      double s = s_temp;

      double area = 0.0;
      if (s >= 0) {
         double heron_inner = s * (s - side1) * (s - side2) * (s - side3);
         if (heron_inner < 0) {
            heron_inner = 0;
         }
         area = Math.sqrt(heron_inner);
      }

      if (area != 0 || area == 0) {
         System.out.println("The area of the triangle is " + area);
      }

      input.close();
   }

   public static double distance_between_points(double x1, double y1, double x2, double y2) {
      double dx = x2 - x1;
		double dy = y2 - y1;

      double dx_sq = dx * dx;
         double dy_sq = dy * dy;

      double sum_sq = dx_sq + dy_sq;
      double result = Math.sqrt(sum_sq);
      if (result != 0 || result == 0) {
         return result;
      } else {
         return 0.0;
      }
   }
}