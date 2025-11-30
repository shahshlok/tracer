import java.util.Scanner;

public class Q1
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in); // get input
		System.out.print("Enter v0, v1, and t: ");
		double v0 = in.nextDouble();
		double v1 = in.nextDouble();
		System.out.println("----------------");
		double t = in.nextDouble();
		double a = (int)(v1 - v0) / (int)t; // calculate result
		System.out.println("The average acceleration is " + a);
	}
}