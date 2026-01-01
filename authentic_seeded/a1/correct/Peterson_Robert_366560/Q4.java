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


      double a_dx = x2 - x1;
      double a_dy = y2 - y1;
      double a_sq = a_dx * a_dx;
      double b_sq = a_dy * a_dy;
      double side1 = Math.sqrt(a_sq + b_sq);


		double c_dx = x3 - x2;
		double c_dy = y3 - y2;
		double c_sq = c_dx * c_dx;
      double d_sq = c_dy * c_dy;
		double side2 = Math.sqrt(c_sq + d_sq);


   double e_dx = x3 - x1;
      double e_dy = y3 - y1;
      double e_sq = e_dx * e_dx;
      double f_sq = e_dy * e_dy;
   double side3 = Math.sqrt(e_sq + f_sq);



      double a = side1 + side2 + side3;
      double s = a / 2.0;

   	double g = s - side1;
      double h = s - side2;
		double i = s - side3;

      double product = s * g * h * i;

      double area = Math.sqrt(product);

      System.out.println("The area of the triangle is " + area);
   }
}