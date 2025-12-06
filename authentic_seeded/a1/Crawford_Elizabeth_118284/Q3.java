import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

      System.out.print("Enter x1 and y1: ");
    double x1 = input.nextDouble();
      double y1_val = input.nextDouble();

		System.out.print("Enter x2 and y2: ");
    double x2_val = input.nextDouble();
	 double y2 = input.nextDouble();


    double dx = x2_val - x1;
      double dy = y2 - y1_val;

   double distance = Math.sqrt(dx * dx + dy * dy);

	System.out.println("The distance of the two points is " + distance);
  }
}