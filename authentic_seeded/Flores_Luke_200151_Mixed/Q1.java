// Name: Luke Flores
// Student ID: 200151
// Assignment 2

import java.util.Scanner;

public class Q1
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double startVelocity=sc.nextDouble();
		double endVelocity=sc.nextDouble();
		double time=sc.nextDouble();
		double acceleration=(endVelocity-startVelocity)/time;
		System.out.println("The average acceleration is " + acceleration);
	}
}