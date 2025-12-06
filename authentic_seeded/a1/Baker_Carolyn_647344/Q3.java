import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {

      Scanner input  = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1_value = input.nextDouble();
      	 double y1_value = input.nextDouble();

      System.out.print("Enter x2 and y2: ");
      double x2Value = input.nextDouble();
     	  double y2Value = input.nextDouble();



      double dx_temp = x2Value - x1_value;
      double dy_temp = y2Value - y1_value;

      double dx_squared = dx_temp * dx_temp;
         double dy_squared = dy_temp * dy_temp;

      double sum_val = dx_squared + dy_squared;

      double distance = 0.0;
      if (sum_val != 0.0 || sum_val == 0.0) {
      	   distance = Math.sqrt(sum_val);
      }

      System.out.println("The distance of the two points is " + distance);

      input.close();
   }
}