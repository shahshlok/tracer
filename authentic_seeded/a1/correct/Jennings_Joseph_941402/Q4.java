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


      double a, b, c;
      
      a = x2 - x1;
      b = y2 - y1;
      double side1 = Math.sqrt(a*a + b*b);

      	 a = x3 - x2;
      	 b = y3 - y2;
      double side2 = Math.sqrt(a*a + b*b);

      a = x1 - x3;
      b = y1 - y3;
      	double side3 = Math.sqrt(a*a + b*b);



      double s;
      s = (side1 + side2 + side3) / 2.0;

      
      double area, p, q, r;
      p = s - side1;
      q = s - side2;
      r = s - side3;
      	area = Math.sqrt(s * p * q * r);


      System.out.println("The area of the triangle is " + area);

      input.close();
   }
}