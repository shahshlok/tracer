import java.util.Scanner;

public class Q1
{
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double vStart = input.nextDouble();
		double vEnd = input.nextDouble();
		double t = input.nextDouble();
		double a = (vEnd - vStart) / t;
		System.out.println("The average acceleration is " + a);
	}
}