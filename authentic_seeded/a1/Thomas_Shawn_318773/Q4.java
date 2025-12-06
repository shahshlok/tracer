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
      double side_3 = distance_between_points(x3, y3, x1, y1);


      double s_temp = (side1 + side2 + side_3);
      double s = 0.0;
      if (s_temp != 0) {
         s = s_temp / 2.0;
      }


      double area = 0.0;
      if (side1 != 0 || side2 != 0 || side_3 != 0) {
         double temp_product = s * (s - side1) * (s - side2) * (s - side_3);
         if (temp_product < 0) {
            temp_product = 0;
         }
         area = Math.sqrt(temp_product);
      }


      System.out.println("The area of the triangle is " + area);

      input.close();
   }


   public static double distance_between_points(double x1, double y1, double x2, double y2) {
      double dx = x2 - x1;
		   double dy = y2 - y1;

      double dx_sq = dx * dx;
   double dy_sq = dy * dy;

      double sum = dx_sq + dy_sq;
      double result = 0.0;
      if (sum != 0) {
         result = Math.sqrt(sum);
      }
      return result;
   }

}