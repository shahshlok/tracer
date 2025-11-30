// Name: Zoey Ramirez
// Student ID: 200134
// Assignment 2

import java.util.Scanner;
public class Q3 {
// TODO: Clean up code before submission
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter x1 and y1: ");
		double x1 = in.nextDouble();
		double y1 = in.nextDouble();
		System.out.print("Enter x2 and y2: ");
		double x2 = in.nextDouble();
		double y2 = in.nextDouble();
		double distance = Math.sqrt((x2 - x1) ^ 2 + (y2 - y1) ^ 2);
		System.out.println("The distance of the two points is " + distance);
	}
}