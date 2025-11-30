import java.util.*;

public class Q1
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double startVelocity=scanner.nextDouble();
		double endVelocity=scanner.nextDouble();
		double time=scanner.nextDouble();
		double velocity=startVelocity + endVelocity * time;
		System.out.println("The velocity is " + velocity);
	}
}