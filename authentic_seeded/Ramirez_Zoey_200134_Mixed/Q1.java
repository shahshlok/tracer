// Name: Zoey Ramirez
// Student ID: 200134
// Assignment 2

import java.util.Scanner;

public class Q1
// TODO: Clean up code before submission
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double startVelocity = in.nextDouble();
		double endVelocity = in.nextDouble();
		double time = in.nextDouble();
		double acceleration = endVelocity - startVelocity / time;
		System.out.println("The average acceleration is " + acceleration);
	}
}