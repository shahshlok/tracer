// Name: Olivia Carter
// Student ID: 200128
// Assignment 2

import java.util.Scanner;
public class Q2 {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the driving distance in miles: ");
		double distance = scan.nextDouble();
		System.out.print("Enter miles per gallon: ");
		double milesPerGallon = scan.nextDouble();
		System.out.print("Enter price in $ per gallon: ");
		double price = scan.nextDouble();
		double cost = (distance / milesPerGallon) * price;
		System.out.println("The cost of driving is $" + cost);
	}
}