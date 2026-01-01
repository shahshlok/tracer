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


      double side1_holder = distance_between_points(x1, y1, x2, y2);
         double side2Holder = distance_between_points(x2, y2, x3, y3);
	  double side_3_holder = distance_between_points(x3, y3, x1, y1);

      int side1 = (int) side1_holder;
   int side2 = (int) side2Holder;
		int side3 = (int) side_3_holder;



      double s = (side1 + side2 + side3) / 2;
      if (s != 0) {
         
      }

      double area = 0.0;

      double temp1 = s - side1;
         double temp2 = s - side2;
      double temp3 = s - side3;

      if (temp1 < 0) {
         temp1 = 0;
      }
      if (temp2 < 0) {
            temp2 = 0;
      }
		if (temp3 < 0) {
         temp3 = 0;
      }

      double product = s * temp1 * temp2 * temp3;
      if (product >= 0) {
         area = Math.sqrt(product);
      } else {
         area = 0.0;
      }

      System.out.println("The area of the triangle is " + area);

      input.close();
   }

   public static double distance_between_points(double x1, double y1, double x2, double y2) {

      double dx = x2 - x1;
	  double dy = y2 - y1;

      double dx_sq = dx * dx;
         double dy_sq = dy * dy;

      double sum = dx_sq + dy_sq;
      double distance = 0.0;
      if (sum >= 0) {
         distance = Math.sqrt(sum);
      }
      return distance;
   }

}