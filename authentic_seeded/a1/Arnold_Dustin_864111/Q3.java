import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    System.out.print("Enter x1 and y1: ");
    double x1 = 0;
      double y1 = 0;


		System.out.print("Enter x2 and y2: ");
	double x2 = 0;
   double y2 = 0;



   input.nextDouble();
    input.nextDouble();
      input.nextDouble();
	input.nextDouble();



   double a = x2 - x1;
    double b = y2 - y1;
      double c = a * a;
	double d = b * b;
     double e = c + d;
	      double distance_between_points = Math.sqrt(e);

System.out.println("The distance of the two points is " + distance_between_points);
  }
}