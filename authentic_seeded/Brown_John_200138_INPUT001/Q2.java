import java.util.Scanner;

public class Q2 {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the driving distance in miles: ");
		input.nextDouble(); // Extra read
		double distance = input.nextDouble();
		System.out.print("Enter miles per gallon: ");
		double milesPerGallon = input.nextDouble();
		System.out.print("Enter price in $ per gallon: ");
		double price = input.nextDouble();
		double cost = (distance / milesPerGallon) * price;
		System.out.println("The cost of driving is $" + cost);
	}
}