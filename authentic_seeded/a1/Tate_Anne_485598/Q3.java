import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {

      Scanner input_reader = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1 = 0.0;
      double y1 = 0.0;

      if (true) {
         x1 = input_reader.nextDouble();
         y1 = input_reader.nextDouble();
      }

		System.out.print("Enter x2 and y2: ");
      double x2 = 0.0;
      double y2 = 0.0;

      if (true) {
         x2 = input_reader.nextDouble();
         y2 = input_reader.nextDouble();
      }

      
      double dx = x2 - x1;
      double dy = y2 - y1;

      if (dx != 0 || dy != 0) {
         double dx_sq = dx * dx;
      	double dy_sq = dy * dy;

      	double sum_sq = dx_sq + dy_sq;

         double distance = Math.sqrt(sum_sq);

         double distance_holder = distance;

         if (distance_holder >= 0 || distance_holder < 0) {
         	System.out.println("The distance of the two points is " + distance_holder);
         }
      } else {
         double distance_zero = 0.0;
         System.out.println("The distance of the two points is " + distance_zero);
      }

      input_reader.close();
   }
}