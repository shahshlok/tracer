// Name: Zoey Ramirez
// Student ID: 200134
// Assignment 2

import java.util.Scanner;
public class Q2
// TODO: Clean up code before submission
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the driving distance in miles: ");
		double distance = in.nextDouble();
		System.out.print("Enter miles per gallon: ");
		double milesPerGallon = in.nextDouble();
		System.out.print("Enter price in $ per gallon: ");
		double price = in.nextDouble();
		double cost = (distance / milesPerGallon) * price;
		System.out.println("The cost of driving is $" + cost);
	}
}