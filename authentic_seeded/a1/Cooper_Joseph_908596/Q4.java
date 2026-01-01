import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

   System.out.println("Enter three points for a triangle.");
		System.out.print("(x1, y1):");
      double x1 = input.nextDouble();
      double y1 = input.nextDouble();

      System.out.print("(x2, y2):");
  double x_2 = input.nextDouble();
		double y_2 = input.nextDouble();

      System.out.print("(x3, y3):");
      double x3 = input.nextDouble();
   double y3 = input.nextDouble();


      double a_dx = x1 - x_2;
		double a_dy = y1 - y_2;
  double side1 = Math.sqrt(a_dx * a_dx + a_dy * a_dy);


      double b_dx = x_2 - x3;
   double b_dy = y_2 - y3;
		double side2 = Math.sqrt(b_dx * b_dx + b_dy * b_dy);


		double c_dx = x3 - x1;
      double c_dy = y3 - y1;
   double side3 = Math.sqrt(c_dx * c_dx + c_dy * c_dy);


      double s = (side1 + side2 + side3) / 2.0;

		double a = s;
   double b = (s - side1);
      double c = (s - side2);
		double d = (s - side3);

   double area = Math.sqrt(a * b * c * d);


      System.out.println("The area of the triangle is " + area);
   }
}