import java.util.Scanner;

public class Q4 {

   public static void main(String[] args)  {

      Scanner input   = new Scanner(System.in);

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


      double side1_temp = distance_between_points(x1, y1, x2, y2);
      double side2Temp  = distance_between_points(x2, y2, x3, y3);
		double side_3_temp = distance_between_points(x3, y3, x1, y1);

      double side1 = side1_temp;
      double side2 = side2Temp;
      double side3 = side_3_temp;



      double s_temp = (side1 + side2 + side3) / 2.0;
      double s = s_temp;

      double area = 0.0;

      if (s >= 0 && side1 >= 0 && side2 >= 0 && side3 >= 0) {

         double a = s - side1;
	 double b = s - side2;
      double c = s - side3;

         double inside = s * a * b * c;

         if (inside < 0) {
            inside = 0;
         }

         area = Math.sqrt(inside);
      }

      System.out.println("The area of the triangle is " + area);

      input.close();
   }

   public static double distance_between_points(double x1, double y1, double x2, double y2)  {

      double dx = x2 - x1;
		double dy = y2 - y1;

      double dx2 = dx * dx;
      double dy2 = dy * dy;

      double sum = dx2 + dy2;

      double result = 0.0;
      if (sum >= 0) {
         result = Math.sqrt(sum);
      }
      return result;
   }

}