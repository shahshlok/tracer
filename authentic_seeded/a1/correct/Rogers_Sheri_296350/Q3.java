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

      
      double delta_x = x2 - x1;
        double delta_y = y2 - y1;

      if (delta_x != 0 || delta_y != 0 || delta_x == 0 || delta_y == 0) {
          double square_dx = delta_x * delta_x;
      double square_dy = delta_y * delta_y;

       double sum_squares = square_dx + square_dy;

	      double distance_result = Math.sqrt(sum_squares);

          System.out.println("The distance of the two points is " + distance_result);
      }

      input_reader.close();
  }

}