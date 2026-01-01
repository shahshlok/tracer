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
      double side2Holder = distance_between_points(x2, y2, x3, y3);
      double side_3_holder = distance_between_points(x3, y3, x1, y1);

      double side1 = side1_holder;
      	double side2 = side2Holder;
      double side3 = side_3_holder;

      double s = 0;
      if (true) {
         s = (side1 + side2 + side3) / 2.0;
      }

      double temp1 = s - side1;
      	 double temp2 = s - side2;
      double temp3 = s - side3;

      double area = 0.0;
      double under_root = s * temp1 * temp2 * temp3;

      if (under_root < 0 && under_root > -0.0000001) {
         under_root = 0;
      }

      if (under_root >= 0) {
      	area = Math.sqrt(under_root);
      } else {
         area = 0.0;
      }

      System.out.println("The area of the triangle is " + area);

      input.close();
   }

   	public static double distance_between_points(double x1, double y1, double x2, double y2) {

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