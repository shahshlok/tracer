import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {

      Scanner inputScanner = new Scanner(System.in);

      System.out.println("Enter three points for a triangle.");

      System.out.print("(x1, y1):");
      double x1 = inputScanner.nextDouble();
      double y1 = inputScanner.nextDouble();

    System.out.print("(x2, y2):");
      double x2 = inputScanner.nextDouble();
   double y2 = inputScanner.nextDouble();

		System.out.print("(x3, y3):");
      double x3 = inputScanner.nextDouble();
      double y3 = inputScanner.nextDouble();



      double side1_len = distance_between_points(x1, y1, x2, y2);
  double side2Len = distance_between_points(x2, y2, x3, y3);
		double side3_length = distance_between_points(x3, y3, x1, y1);


      double side1_holder = side1_len;
      double side2_holder = side2Len;
      double side3_holder = side3_length;

      double s = 0.0;
      if (side1_holder >= 0 && side2_holder >= 0 && side3_holder >= 0) {
         s = (side1_holder + side2_holder + side3_holder) / 2.0;
      }

      double area = 0.0;

      if (s != 0) {
         double a_term = s - side1_holder;
         double b_term = s - side2_holder;
         double c_term = s - side3_holder;

         if (a_term >= 0 && b_term >= 0 && c_term >= 0) {
            double underRoot = s * a_term * b_term * c_term;

            if (underRoot >= 0) {
               area = Math.sqrt(underRoot);
            }
         }
      }

      System.out.println("The area of the triangle is " + area);

      inputScanner.close();
   }

   public static double distance_between_points(double x1, double y1, double x2, double y2) {

      double dx = x2 - x1;
      		double dy = y2 - y1;

      double dx_sq = dx * dx;
      double dy_sq = dy * dy;

      double sum_sq = dx_sq + dy_sq;
      double distance = 0.0;

      if (sum_sq >= 0) {
         distance = Math.sqrt(sum_sq);
      }

      return distance;
   }
}