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


	// side1 between (x1,y1) and (x2,y2)
	double a = x2 - x1;
	double b = y2 - y1;
	double side1 = Math.sqrt(a * a + b * b);

   // side2 between (x2,y2) and (x3,y3)
   double c = x3 - x2;
   double d = y3 - y2;
   double side2 = Math.sqrt(c * c + d * d);

      // side3 between (x3,y3) and (x1,y1)
      double e = x1 - x3;
      double f = y1 - y3;
      double side3 = Math.sqrt(e * e + f * f);


      double s = (side1 + side2 + side3) / 2.0;

   double g = s - side1;
   double h = s - side2;
	double i = s - side3;

	double area = Math.sqrt(s * g * h * i);


      System.out.println("The area of the triangle is " + area);
  }
}