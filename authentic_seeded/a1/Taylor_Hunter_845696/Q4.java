import java.util.Scanner;

public class Q4 {
   public static void main(String[] args){


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



      double a, b, c;

      
      double dx12 = x2 - x1;
      double dy12 = y2 - y1;
      a = dx12 * dx12 + dy12 * dy12;
      double side1 = Math.sqrt(a);

      double dx23 = x3 - x2;
      double dy23 = y3 - y2;
      b = dx23 * dx23 + dy23 * dy23;
      double side2 = Math.sqrt(b);

   double dx31 = x1 - x3;
   double dy31 = y1 - y3;
   c = dx31 * dx31 + dy31 * dy31;
   double side3 = Math.sqrt(c);



      double s_num = side1 + side2 + side3;
      double s = side1 + side2 + side3 / 2;



      double term1 = s;
      double term2 = (s - side1);
      double term3 = (s - side2);
      double term4 = (s - side3);

      double product = term1 * term2 * term3 * term4;

      double area = Math.sqrt(product);


      System.out.println("The area of the triangle is " + area);

      input.close();
   }
}