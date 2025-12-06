import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    double x1 = 0;
      double y1_value = 0;


		double x2_value = 0;
  double y2 = 0;



    System.out.print("Enter x1 and y1: ");
    input.nextDouble();
      input.nextDouble();


		System.out.print("Enter x2 and y2: ");
		input.nextDouble();
  input.nextDouble();


   double dx = x2_value - x1;
      double dy_difference = y2 - y1_value;

	double distance = Math.sqrt(dx * dx + dy_difference * dy_difference);

    System.out.println("The distance of the two points is " + distance);

    input.close();
  }
}