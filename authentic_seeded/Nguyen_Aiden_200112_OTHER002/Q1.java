import java.util.Scanner;

public class Q1
{
	public static void main(String[] args)
	{
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double startVelocity = reader.nextDouble();
		double endVelocity = reader.nextDouble();
		double time = reader.nextDouble();
		double acceleration = (endVelocity - startVelocity) / time;
		System.out.println("The average acceleration is " + acceleration);
	}
}