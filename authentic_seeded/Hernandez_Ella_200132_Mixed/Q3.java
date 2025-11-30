import java.util.Scanner;
public class Q3
{
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		System.out.print("Enter x1 and y1: ");
		double x1 = s.nextDouble();
		double y1 = s.nextDouble();
		System.out.print("Enter x2 and y2: "); // calculate result
		double x2 = s.nextDouble();
		double y2 = s.nextDouble();
		double distance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
		System.out.println("The distance of the two points is " + distance);
	}
}