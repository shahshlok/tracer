import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
      double x1 = input.nextDouble();
   double y1_value = input.nextDouble();

		System.out.print("Enter x2 and y2: ");
		double x2_point = input.nextDouble();
      double y2 = input.nextDouble();


      double a = x2_point - x1;
   double b = y2 - y1_value;

      double c = a * a;
		double d = b * b;

      double sum_of_squares = c + d;

   double distance = Math.sqrt(sum_of_squares);

		System.out.println("The distance of the two points is " + distance);
   }
}