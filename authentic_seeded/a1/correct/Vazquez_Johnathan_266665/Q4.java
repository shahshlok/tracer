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
   double dx1, dy1, dx2, dy2, dx3, dy3;

   dx1 = x2 - x1;
   dy1 = y2 - y1;
   a = Math.sqrt(dx1*dx1 + dy1*dy1);

    dx2 = x3 - x2;
    dy2 = y3 - y2;
    b = Math.sqrt(dx2*dx2 + dy2*dy2);

      dx3 = x1 - x3;
      dy3 = y1 - y3;
      c = Math.sqrt(dx3*dx3 + dy3*dy3);


      double s, area;
      s = (a + b + c) / 2.0;

      area = Math.sqrt(s * (s - a) * (s - b) * (s - c));


      System.out.println("The area of the triangle is " + area);

      input.close();
  }
}