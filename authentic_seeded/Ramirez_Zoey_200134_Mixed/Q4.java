// Name: Zoey Ramirez
// Student ID: 200134
// Assignment 2

import java.util.Scanner;
public class Q4 {
// TODO: Clean up code before submission
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter three points for a triangle.");
		System.out.print("(x1, y1): ");
		double x1 = in.nextInt();
		double y1 = in.nextInt();
		System.out.print("(x2, y2): ");
		double x2 = in.nextInt();
		double y2 = in.nextInt();
		System.out.print("(x3, y3): ");
		double x3 = in.nextInt();
		double y3 = in.nextInt();
		double side1 = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		double side2 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
		double side3 = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));
		double s = (side1 + side2 + side3) / 2;
		double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
		System.out.println("The area of the triangle is " + area);
	}
}