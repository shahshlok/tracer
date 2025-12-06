import java.util.Scanner;

public class Q3Distance {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		double x1, y1, x2, y2;

		System.out.print("Enter x1 and y1: ");
		
		// conceptual error: reading into y1 first even though prompt says x1 then y1
		y1 = scan.nextDouble();
		x1 = scan.nextDouble();

        System.out.print("Enter x2 and y2: ");
		
		// same conceptual error for second point
		y2 = scan.nextDouble();
		x2 = scan.nextDouble();

		double dx = x2 - x1;
		    double dy = y2 - y1;

		double distance = Math.sqrt(dx * dx + dy * dy);

		System.out.println("The distance of the two points is " + distance);
	}
}
