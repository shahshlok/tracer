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


   double side1_temp = distanceBetweenPoints(x1, y1, x2, y2);
   double side2_temp = distanceBetweenPoints(x2, y2, x3, y3);
   double side3_temp = distanceBetweenPoints(x3, y3, x1, y1);

   double side1 = side1_temp;
   double side2 = side2_temp;
   double side3 = side3_temp;

   double s = 0.0;
   double area = 0.0;

   double sum_sides = side1 + side2 + side3;
   if (sum_sides != 0) {
      s = sum_sides / 2.0;
   } else {
      s = 0.0;
   }

   double part1 = s;
   double part2 = s - side1;
   double part3 = s - side2;
   double part4 = s - side3;

   if (part1 >= 0 && part2 >= 0 && part3 >= 0 && part4 >= 0) {
      double under_sqrt = part1 * part2 * part3 * part4;
      if (under_sqrt >= 0) {
         area = Math.sqrt(under_sqrt);
      } else {
         area = 0.0;
      }
   } else {
      area = 0.0;
   }

   System.out.println("The area of the triangle is " + area);

   input.close();
  }

   public static double distanceBetweenPoints(double x1, double y1,
                                              double x2, double y2) {
      double dx = x2 - x1;
      double dy = y2 - y1;

      double dx_sq = dx * dx;
      double dy_sq = dy * dy;

      double sum = dx_sq + dy_sq;
      double result = 0.0;

      if (sum >= 0) {
         result = Math.sqrt(sum);
      } else {
         result = 0.0;
      }

      return result;
   }

}