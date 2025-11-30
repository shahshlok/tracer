import java.util.Scanner;
public class Q3
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter x1 and y1: ");
		double x1 = in.nextDouble();
		double y1 = in.nextDouble();
		System.out.print("Enter x2 and y2: "); // print output
		double x2 = in.nextDouble(); // print output
		double y2 = in.nextDouble();
		double distance = Math.sqrt((x2 - x1) ^ 2 + (y2 - y1) ^ 2); // calculate result
		System.out.println("The distance of the two points is " + distance);
	}
}