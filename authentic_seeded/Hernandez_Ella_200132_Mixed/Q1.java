import java.util.Scanner;

public class Q1
{
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double vStart = s.nextDouble();
		double vEnd = s.nextDouble();
		double t = s.nextDouble();
		double a = (vEnd - vStart) / t; // calculate result
		System.out.println("The average acceleration is " + a);
	}
}