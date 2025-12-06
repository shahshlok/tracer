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


      double dx12 = x2 - x1;
      double dy12 = y2 - y1;
      double temp_side1 = dx12 * dx12 + dy12 * dy12;
      double side1 = 0.0;
      if (temp_side1 >= 0) {
         side1 = Math.sqrt(temp_side1);
      }

      double dx23 = x3 - x2;
      double dy23 = y3 - y2;
      double temp_side2 = dx23 * dx23 + dy23 * dy23;
      double side2 = 0.0;
      if (temp_side2 >= 0) {
         side2 = Math.sqrt(temp_side2);
      }

      double dx31 = x1 - x3;
         double dy31 = y1 - y3;
      double temp_side3 = dx31 * dx31 + dy31 * dy31;
      double side3 = 0.0;
      if (temp_side3 >= 0) {
         side3 = Math.sqrt(temp_side3);
      }

      double sum_sides = side1 + side2 + side3;
      double s = 0.0;
      if (sum_sides != 0) {
         s = sum_sides / 2.0;
      }

      double temp_a = s - side1;
      double temp_b = s - side2;
      double temp_c = s - side3;

      double temp_area = s * temp_a * temp_b * temp_c;
      double area = 0.0;
      if (temp_area > 0) {
         area = Math.sqrt(temp_area);
      } else {
         area = 0.0;
      }

      System.out.println("The area of the triangle is " + area);

      input.close();
   }
}