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
      double x_3 = input.nextDouble();
      double y_3 = input.nextDouble();


      double side1 = distance(x1, y1, x2, y2);
   double side_two = distance(x2, y2, x_3, y_3);
		double side3 = distance(x_3, y_3, x1, y1);


      double s = (side1 + side_two + side3) / 2.0;

      double area = Math.sqrt(s * (s - side1) * (s - side_two) * (s - side3));

      System.out.println("The area of the triangle is " + area);

      input.close();
   }

   public static double distance(double x1, double y1, double x2, double y2) {
   	double dx = x2 - x1;
      double dy = y2 - y1;
      double dist = Math.sqrt(dx * dx + dy * dy);
      return dist;
   }
}