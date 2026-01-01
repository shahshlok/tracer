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


      double side1_length = distanceBetweenPoints(x1, y1, x2, y2);
  	  double side2Length = distanceBetweenPoints(x2, y2, x3, y3);
      double side3_length = distanceBetweenPoints(x3, y3, x1, y1);

      double tempPerimeter = side1_length + side2Length + side3_length;
      double s = tempPerimeter / 2.0;

      double part1 = s - side1_length;
      double part2 = s - side2Length;
  	  double part3 = s - side3_length;

      if (part1 < 0) part1 = 0;
      if (part2 < 0) part2 = 0;
      if (part3 < 0) part3 = 0;

      double area_mult = s * part1 * part2 * part3;
      if (area_mult < 0) {
         area_mult = 0;
      }

      double area = Math.sqrt(area_mult);

      System.out.println("The area of the triangle is " + area);
      input.close();
   }

   public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
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