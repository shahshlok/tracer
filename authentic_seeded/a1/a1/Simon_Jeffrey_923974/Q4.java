import java.util.Scanner;

public class Q4TriangleArea {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		double x1, y1, x2, y2, x3, y3;

		System.out.println("Enter three points for a triangle.");

		System.out.print("(x1, y1):");
		x1 = input.nextDouble();
		y1 = input.nextDouble();

		System.out.print("(x2, y2):");
		x2 = input.nextDouble();
		y2 = input.nextDouble();

		System.out.print("(x3, y3):");
		x3 = input.nextDouble();
		y3 = input.nextDouble();

		// side lengths using distance formula from Q3
		double side1 = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
		double side2 = Math.sqrt((x3 - x2)*(x3 - x2) + (y3 - y2)*(y3 - y2));
		double side3 = Math.sqrt((x1 - x3)*(x1 - x3) + (y1 - y3)*(y1 - y3));

		double s = (side1 + side2 + side3) / 2;

		double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));

		System.out.println("The area of the triangle is " + area);
	}
}
