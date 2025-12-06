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


    double a_x = x2 - x1;
      double a_y = y2 - y1;
   double side1 = Math.sqrt(a_x * a_x + a_y * a_y);

		double b_x = x3 - x2;
  double b_y = y3 - y2;
      double side2 = Math.sqrt(b_x * b_x + b_y * b_y);

   double c_x = x1 - x3;
	double c_y = y1 - y3;
    double side3 = Math.sqrt(c_x * c_x + c_y * c_y);



    double s_num = side1 + side2 + side3;
      double   s = s_num / 2.0;

      double t1 = s;
   double t2 = s - side1;
		double t3 = s - side2;
    double t4 = s - side3;

      double prod = t1 * t2 * t3 * t4;
  double area = Math.sqrt(prod);


   System.out.println("The area of the triangle is " + area);
  }
}