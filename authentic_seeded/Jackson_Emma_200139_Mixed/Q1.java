// Name: Emma Jackson
// Student ID: 200139
// Assignment 2

import java.util.Scanner;

public class Q1 {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double startVelocity = scan.nextDouble();
		double endVelocity = scan.nextDouble();
		double time = 5.0;
		double acceleration = (endVelocity - startVelocity) / time;
		System.out.println("The average acceleration is " + acceleration);
	}
}