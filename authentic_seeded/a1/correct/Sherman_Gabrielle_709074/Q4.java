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
      double side1 = Math.sqrt(a_dx * a_dx + a_dy * a_dy);

  	 double b_dx = x3 - x2;
  	 double b_dy = y3 - y2;
  	 double side2 = Math.sqrt(b_dx * b_dx + b_dy * b_dy);

      double c_dx = x1 - x3;
      double c_dy = y1 - y3;
      double side3 = Math.sqrt(c_dx * c_dx + c_dy * c_dy);



      double s_perimeter = side1 + side2 + side3;
      double s = s_perimeter / 2.0;

      double a = s;
      double b = (s - side1);
      double c = (s - side2);
      double d = (s - side3);

      double area_squared_inside = a * b * c * d;
      double area = Math.sqrt(area_squared_inside);

      System.out.println("The area of the triangle is " + area);

      input.close();
   }
}