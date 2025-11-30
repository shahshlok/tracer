import java.util.Scanner;
public class Q2 {
// TODO: Clean up code before submission
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the driving distance in miles: ");
		double d = in.nextDouble();
		System.out.print("Enter miles per gallon: ");
		double mpg = in.nextDouble();
		System.out.print("Enter price in $ per gallon: ");
		double p = in.nextDouble();
		double cost = d / mpg * p;
		System.out.println("The cost of driving is $" + cost);
	}
}