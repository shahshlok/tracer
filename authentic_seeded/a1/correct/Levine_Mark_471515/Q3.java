import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	System.out.print("Enter x1 and y1: ");
   double x1 = input.nextDouble();
    double y1 = input.nextDouble();

	 System.out.print("Enter x2 and y2: ");
      double x2 = input.nextDouble();
  double y2 = input.nextDouble();


   double diff_x = x2 - x1;
	double diffY = y2 - y1;

      double distance = Math.sqrt(diff_x * diff_x + diffY * diffY);

 System.out.println("The distance of the two points is " + distance);
  }
}